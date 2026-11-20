<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="_guard.jspf" %>
<% ImportInvoice0 invoice = (ImportInvoice0) session.getAttribute("invoice");
   if (invoice == null) { response.sendRedirect(ctx + "/view/warehousestaff/SearchSupplierUI0.jsp"); return; }
   Supplier0 supplier = invoice.getSupplier();
   Product0 picked = (Product0) session.getAttribute("pickedProduct"); // mặt hàng vừa thêm mới → chọn sẵn
   session.removeAttribute("pickedProduct"); %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><title>Nhập hàng</title>
<link rel="stylesheet" href="<%= ctx %>/css/simple.css"></head><body>
<div class="card">
  <button type="button" class="back" id="btnBack" name="btnBack"
          onclick="location.href='<%= ctx %>/view/warehousestaff/SearchSupplierUI0.jsp'">Quay lại</button>
  <h1>Nhập hàng</h1>
  <%@ include file="../_message.jspf" %>
  <p class="info">Nhân viên kho: <%= esc(user.getName()) %></p>
  <p class="info" id="lblSupplier">Nhà cung cấp: <%= esc(supplier.getName()) %></p>
  <p class="info">Địa chỉ: <%= esc(supplier.getAddress()) %></p>
  <p class="info">Số điện thoại: <%= esc(supplier.getPhoneNumber()) %></p>

  <div class="row" style="margin-top:16px">
    <div class="search grow">
      <input type="text" id="txtProductName" name="txtProductName" autocomplete="off"
             placeholder="Nhập tên mặt hàng của nhà cung cấp (click hoặc gõ để hiện danh sách)">
      <div class="pop" id="lstSuggest" role="listbox"></div>
    </div>
    <button type="button" id="btnAddProduct" name="btnAddProduct"
            onclick="location.href='<%= ctx %>/view/warehousestaff/AddProductUI0.jsp'">Thêm mới</button>
  </div>

  <form class="picked <%= picked == null ? "hidden" : "" %>" id="frmAddDetail" action="<%= ctx %>/import" method="post">
    <input type="hidden" name="action" value="addDetail">
    <input type="hidden" id="productId" name="productId" value="<%= picked == null ? "" : picked.getId() %>">
    <div class="pname">Mặt hàng: <b id="lblProduct"><%= picked == null ? "" : esc(picked.getName()) %></b>
      <small id="lblCode"><%= picked == null ? "" : String.format("SP%06d", picked.getId()) %></small></div>
    <label>Số lượng<input type="number" id="txtQuantity" name="txtQuantity" min="1" value="1" required></label>
    <label>Đơn giá nhập<input type="number" id="txtUnitPrice" name="txtUnitPrice" min="1" required></label>
    <button type="submit" id="btnAddToInvoice" name="btnAddToInvoice">Thêm vào phiếu</button>
    <button type="button" class="gray" onclick="closePicked()">Bỏ chọn</button>
  </form>

  <h2>Danh sách mặt hàng nhập</h2>
  <table id="tblDetails">
    <thead><tr><th>STT</th><th>Mã hàng</th><th>Tên mặt hàng</th><th>Loại</th><th>Số lượng</th><th>Đơn giá</th><th>Thành tiền</th><th>Xóa</th></tr></thead>
    <tbody>
    <% int i = 0; for (ImportInvoiceDetail0 d : invoice.getDetails()) { Product0 p = d.getProduct(); %>
      <tr>
        <td><%= i + 1 %></td>
        <td><%= String.format("SP%06d", p.getId()) %></td>
        <td><%= esc(p.getName()) %></td>
        <td><%= esc(p.getType()) %></td>
        <td><input class="cell" type="number" min="1" name="txtQuantity" form="upd<%= i %>"
                   value="<%= d.getQuantity() %>" onchange="this.form.submit()"></td>
        <td><input class="cell" type="number" min="1" name="txtUnitPrice" form="upd<%= i %>"
                   value="<%= (long) d.getUnitPrice() %>" onchange="this.form.submit()"></td>
        <td><%= money(d.getAmount()) %></td>
        <td><button type="submit" class="mini" name="lnkDelete" form="del<%= i %>">Xóa</button></td>
      </tr>
    <% i++; } %>
    <% if (invoice.getDetails().isEmpty()) { %>
      <tr class="empty"><td colspan="8">Phiếu nhập chưa có mặt hàng</td></tr>
    <% } %>
    </tbody>
    <tfoot><tr><td colspan="6" class="num">Tổng:</td><td id="lblTotal"><%= money(invoice.getTotalAmount()) %></td><td></td></tr></tfoot>
  </table>
  <% for (int k = 0; k < invoice.getDetails().size(); k++) { %>
    <form id="upd<%= k %>" action="<%= ctx %>/import" method="post"><input type="hidden" name="action" value="updateDetail"><input type="hidden" name="index" value="<%= k %>"></form>
    <form id="del<%= k %>" action="<%= ctx %>/import" method="post"><input type="hidden" name="action" value="deleteDetail"><input type="hidden" name="index" value="<%= k %>"></form>
  <% } %>

  <form class="center" action="<%= ctx %>/import" method="post">
    <input type="hidden" name="action" value="save">
    <button type="submit" class="ok" id="btnSubmit" name="btnSubmit" <%= invoice.getDetails().isEmpty() ? "disabled" : "" %>>Xác nhận</button>
  </form>
</div>

<script>
(function () {
  var ctx = '<%= ctx %>';
  var box = document.getElementById('txtProductName');
  var pop = document.getElementById('lstSuggest');
  var items = [], active = -1, timer = null, seq = 0;
  function money(v) { return Math.round(v).toLocaleString('vi-VN'); }
  function code(id) { return 'SP' + String(id).padStart(6, '0'); }
  function esc(s) { var d = document.createElement('div'); d.textContent = s || ''; return d.innerHTML; }
  function hide() { pop.style.display = 'none'; active = -1; }
  function render() {
    if (!items.length) {
      pop.innerHTML = '<div class="none">' + (box.value.trim()
          ? 'Không có mặt hàng nào của nhà cung cấp này khớp “' + esc(box.value) + '”.'
          : 'Nhà cung cấp này chưa có mặt hàng nào.') +
        ' <a href="' + ctx + '/view/warehousestaff/AddProductUI0.jsp">Thêm mặt hàng mới</a></div>';
    } else {
      pop.innerHTML = items.map(function (p, i) {
        return '<div class="opt' + (i === active ? ' on' : '') + '" data-i="' + i + '">' +
          '<b>' + esc(p.name) + '</b><br><small>' + code(p.id) + ' · ' + esc(p.type) +
          ' · Tồn: ' + p.quantity + ' · Giá nhập gần nhất: ' + (p.importPrice > 0 ? money(p.importPrice) : 'chưa có') + '</small></div>';
      }).join('');
    }
    pop.style.display = 'block';
  }
  function choose(i) {
    var p = items[i]; if (!p) return;
    document.getElementById('productId').value = p.id;
    document.getElementById('lblProduct').textContent = p.name;
    document.getElementById('lblCode').textContent = code(p.id);
    document.getElementById('frmAddDetail').classList.remove('hidden');
    box.value = ''; hide();
    document.getElementById('txtUnitPrice').value = p.importPrice > 0 ? Math.round(p.importPrice) : '';  // tự điền giá nhập gần nhất
    var q = document.getElementById('txtQuantity'); q.value = 1; q.focus(); q.select();
  }
  // Ô trống → toàn bộ mặt hàng của NCC; có chữ → lọc theo tên
  function search(delay) {
    clearTimeout(timer);
    var term = box.value.trim();
    timer = setTimeout(function () {
      var my = ++seq;
      fetch(ctx + '/product?txtProductName=' + encodeURIComponent(term))
        .then(function (r) { return r.json(); })
        .then(function (list) { if (my !== seq) return; items = list; active = list.length ? 0 : -1; render(); });
    }, delay);
  }
  box.addEventListener('input', function () { search(200); });
  box.addEventListener('click', function () { if (pop.style.display !== 'block') search(0); });
  box.addEventListener('focus', function () { if (pop.style.display !== 'block') search(0); });
  box.addEventListener('keydown', function (e) {
    if (pop.style.display !== 'block') return;
    if (e.key === 'ArrowDown') { active = Math.min(active + 1, items.length - 1); render(); e.preventDefault(); }
    else if (e.key === 'ArrowUp') { active = Math.max(active - 1, 0); render(); e.preventDefault(); }
    else if (e.key === 'Enter') { choose(active); e.preventDefault(); }
    else if (e.key === 'Escape') { hide(); }
  });
  pop.addEventListener('mousedown', function (e) {
    var o = e.target.closest('.opt'); if (o) { e.preventDefault(); choose(+o.dataset.i); }
  });
  box.addEventListener('blur', function () { setTimeout(hide, 150); });
  window.closePicked = function () { document.getElementById('frmAddDetail').classList.add('hidden'); box.focus(); };
  // không tự focus ô tìm khi mở trang (focus sẽ bật danh sách); chỉ focus ô số lượng khi có mặt hàng chọn sẵn
  if (!document.getElementById('frmAddDetail').classList.contains('hidden')) document.getElementById('txtQuantity').select();
})();
</script>
</body></html>
