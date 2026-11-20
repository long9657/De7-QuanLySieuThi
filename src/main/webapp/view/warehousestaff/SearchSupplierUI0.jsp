<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="_guard.jspf" %>
<% ArrayList<Supplier0> listSupplier = (ArrayList<Supplier0>) session.getAttribute("listSupplier"); %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><title>Tìm nhà cung cấp</title>
<link rel="stylesheet" href="<%= ctx %>/css/simple.css"></head><body>
<div class="card">
  <button type="button" class="back" id="btnBack" name="btnBack"
          onclick="location.href='<%= ctx %>/view/warehousestaff/WarehouseHomeUI0.jsp'">Quay lại</button>
  <h1>Tìm nhà cung cấp</h1>
  <%@ include file="../_message.jspf" %>
  <form class="row" action="<%= ctx %>/supplier" method="get">
    <input class="grow" type="text" id="txtSupplierName" name="txtSupplierName" placeholder="Nhập tên nhà cung cấp"
           value="<%= esc(request.getParameter("txtSupplierName")) %>">
    <button type="submit" id="btnSearch" name="btnSearch">Tìm kiếm</button>
    <button type="button" class="right" id="btnAddSupplier" name="btnAddSupplier"
            onclick="location.href='<%= ctx %>/view/warehousestaff/AddSupplierUI0.jsp'">Thêm mới</button>
  </form>
  <table id="tblSuppliers">
    <thead><tr><th>STT</th><th>Tên nhà cung cấp</th><th>Địa chỉ</th><th>Số điện thoại</th><th>Email</th><th>Chọn</th></tr></thead>
    <tbody>
    <% if (listSupplier != null) { int i = 1; for (Supplier0 s : listSupplier) { %>
      <tr><td><%= i++ %></td><td><%= esc(s.getName()) %></td><td><%= esc(s.getAddress()) %></td>
          <td><%= esc(s.getPhoneNumber()) %></td><td><%= esc(s.getEmail()) %></td>
          <td><a class="mini" name="lnkSelect" href="<%= ctx %>/product?supplierId=<%= s.getId() %>">Chọn</a></td></tr>
    <% } } %>
    <% if (listSupplier == null || listSupplier.isEmpty()) { %>
      <tr class="empty"><td colspan="6"><%= listSupplier == null ? "Nhập tên nhà cung cấp và bấm Tìm kiếm" : "Không tìm thấy nhà cung cấp" %></td></tr>
    <% } %>
    </tbody>
  </table>
</div>
</body></html>
