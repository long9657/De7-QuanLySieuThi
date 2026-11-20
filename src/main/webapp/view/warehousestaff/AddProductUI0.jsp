<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="_guard.jspf" %>
<% ImportInvoice0 invoice = (ImportInvoice0) session.getAttribute("invoice"); %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><title>Thêm mặt hàng</title>
<link rel="stylesheet" href="<%= ctx %>/css/simple.css"></head><body>
<div class="card medium">
  <button type="button" class="back" id="btnBack" name="btnBack"
          onclick="location.href='<%= ctx %>/view/warehousestaff/ImportUI0.jsp'">Quay lại</button>
  <h1>Thêm mới mặt hàng</h1>
  <%@ include file="../_message.jspf" %>
  <% if (invoice != null) { %><p class="info">Nhà cung cấp: <b><%= esc(invoice.getSupplier().getName()) %></b></p><% } %>
  <form action="<%= ctx %>/product" method="post">
    <input type="hidden" id="selectedProductId" name="selectedProductId" value="">
    <label for="txtName">Tên mặt hàng *</label>
    <div class="search">
      <input type="text" id="txtName" name="txtName" autocomplete="off" required
             role="combobox" aria-controls="lstCatalog" aria-expanded="false" aria-autocomplete="list"
             aria-describedby="catalogHint">
      <div class="pop" id="lstCatalog" role="listbox" aria-label="Mặt hàng có sẵn"></div>
    </div>
    <p id="catalogHint">Chọn mặt hàng có sẵn hoặc gõ tên mặt hàng mới.</p>
    <div class="grid2">
      <div><label for="txtType">Loại</label><input type="text" id="txtType" name="txtType"></div>
      <div><label for="txtPrice">Giá bán *</label><input type="number" id="txtPrice" name="txtPrice" min="0" step="0.01" required></div>
    </div>
    <label for="txtDescription">Mô tả</label><input type="text" id="txtDescription" name="txtDescription">
    <div class="center"><button type="submit" class="ok" id="btnSave" name="btnSave">Lưu</button></div>
  </form>
</div>
<script>
(function () {
  var name = document.getElementById('txtName');
  var selected = document.getElementById('selectedProductId');
  var popup = document.getElementById('lstCatalog');
  var hint = document.getElementById('catalogHint');
  var fields = ['txtType', 'txtPrice', 'txtDescription'].map(function (id) { return document.getElementById(id); });
  var products = [], draft = [], active = -1, timer, version = 0;

  function hide() {
    clearTimeout(timer);
    version++;
    active = -1;
    popup.style.display = 'none';
    name.setAttribute('aria-expanded', 'false');
    name.removeAttribute('aria-activedescendant');
  }

  function render(message) {
    popup.replaceChildren();
    products.forEach(function (p, i) {
      var option = document.createElement('div');
      option.className = 'opt';
      option.id = 'catalog-option-' + i;
      option.dataset.index = i;
      option.setAttribute('role', 'option');
      option.setAttribute('aria-selected', 'false');
      option.textContent = p.name + ' · SP' + String(p.id).padStart(6, '0');
      popup.append(option);
    });
    if (!products.length) {
      var status = document.createElement('div');
      status.className = 'none';
      status.textContent = message;
      popup.append(status);
    }
    popup.style.display = 'block';
    name.setAttribute('aria-expanded', 'true');
  }

  function search(delay) {
    hide();
    var request = version;
    var term = selected.value ? '' : name.value.trim();
    timer = setTimeout(function () {
      fetch('<%= ctx %>/product?mode=catalog&txtProductName=' + encodeURIComponent(term))
        .then(function (response) {
          if (!response.ok) throw new Error('Catalog request failed');
          return response.json();
        })
        .then(function (list) {
          if (request !== version) return;
          products = list;
          render('Không có mặt hàng phù hợp. Bạn có thể nhập mặt hàng mới.');
        })
        .catch(function () {
          if (request !== version) return;
          products = [];
          render('Không tải được danh mục. Bấm lại vào ô tên để thử lại.');
        });
    }, delay);
  }

  function choose(index) {
    var product = products[index];
    if (!product) return;
    if (!selected.value) draft = fields.map(function (field) { return field.value; });
    selected.value = product.id;
    name.value = product.name;
    [product.type, product.price, product.description].forEach(function (value, i) {
      fields[i].value = value == null ? '' : value;
      fields[i].readOnly = true;
    });
    hint.textContent = 'Bấm Lưu để gắn mặt hàng với nhà cung cấp. Sửa tên để nhập mặt hàng mới.';
    hide();
  }

  name.addEventListener('focus', function () { search(0); });
  name.addEventListener('click', function () {
    if (popup.style.display !== 'block' || !products.length) search(0);
  });
  name.addEventListener('input', function () {
    if (selected.value) {
      selected.value = '';
      fields.forEach(function (field, i) {
        field.value = draft[i] || '';
        field.readOnly = false;
      });
      hint.textContent = 'Chọn mặt hàng có sẵn hoặc gõ tên mặt hàng mới.';
    }
    search(180);
  });
  name.addEventListener('blur', hide);
  name.addEventListener('keydown', function (event) {
    if (popup.style.display !== 'block') return;
    if (event.key === 'Escape') { hide(); return; }
    if (!products.length) return;
    if (event.key === 'Enter' && active >= 0) {
      event.preventDefault();
      choose(active);
    } else if (event.key === 'ArrowDown' || event.key === 'ArrowUp') {
      event.preventDefault();
      var step = event.key === 'ArrowDown' ? 1 : -1;
      active = Math.max(0, Math.min(products.length - 1, active + step));
      Array.from(popup.children).forEach(function (option, i) {
        option.classList.toggle('on', i === active);
        option.setAttribute('aria-selected', String(i === active));
      });
      var option = popup.children[active];
      name.setAttribute('aria-activedescendant', option.id);
      option.scrollIntoView({block: 'nearest'});
    }
  });
  popup.addEventListener('mousedown', function (event) {
    if (event.target.closest('.opt')) event.preventDefault();
  });
  popup.addEventListener('click', function (event) {
    var option = event.target.closest('.opt');
    if (option) choose(Number(option.dataset.index));
  });
})();
</script>
</body></html>
