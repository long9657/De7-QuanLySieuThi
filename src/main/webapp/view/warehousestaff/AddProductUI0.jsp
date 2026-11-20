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
    <label for="txtName">Tên mặt hàng *</label><input type="text" id="txtName" name="txtName" required>
    <div class="grid2">
      <div><label for="txtType">Loại</label><input type="text" id="txtType" name="txtType"></div>
      <div><label for="txtPrice">Giá bán *</label><input type="number" id="txtPrice" name="txtPrice" min="0" required></div>
    </div>
    <label for="txtDescription">Mô tả</label><input type="text" id="txtDescription" name="txtDescription">
    <div class="center"><button type="submit" class="ok" id="btnSave" name="btnSave">Lưu</button></div>
  </form>
</div>
</body></html>
