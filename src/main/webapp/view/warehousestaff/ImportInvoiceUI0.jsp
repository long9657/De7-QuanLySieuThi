<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="_guard.jspf" %>
<% ImportInvoice0 invoice = (ImportInvoice0) request.getAttribute("invoice"); %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><title>Hóa đơn nhập</title>
<link rel="stylesheet" href="<%= ctx %>/css/simple.css"></head><body>
<div class="card medium">
  <button type="button" class="back" id="btnBack" name="btnBack"
          onclick="location.href='<%= ctx %>/view/warehousestaff/WarehouseHomeUI0.jsp'">Quay lại</button>
  <%@ include file="../_message.jspf" %>
  <h1>Hóa đơn nhập hàng</h1>
  <% if (invoice != null) { Supplier0 s = invoice.getSupplier(); %>
  <p class="info">Số hóa đơn: <%= invoice.getId() %></p>
  <p class="info">Ngày nhập: <%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(invoice.getImportDate()) %></p>
  <p class="info">Nhân viên kho: <%= esc(invoice.getWarehouseStaff().getName()) %></p>
  <p class="info">Nhà cung cấp: <%= esc(s.getName()) %></p>
  <p class="info">Địa chỉ: <%= esc(s.getAddress()) %></p>
  <p class="info">Số điện thoại: <%= esc(s.getPhoneNumber()) %></p>
  <table>
    <thead><tr><th>STT</th><th>Tên mặt hàng</th><th>Đơn giá</th><th>Số lượng</th><th>Thành tiền</th></tr></thead>
    <tbody>
    <% int i = 1; for (ImportInvoiceDetail0 d : invoice.getDetails()) { %>
      <tr><td><%= i++ %></td><td><%= esc(d.getProduct().getName()) %></td><td><%= money(d.getUnitPrice()) %></td>
          <td><%= d.getQuantity() %></td><td><%= money(d.getAmount()) %></td></tr>
    <% } %>
    </tbody>
    <tfoot><tr><td colspan="4" class="num">Tổng:</td><td><%= money(invoice.getTotalAmount()) %></td></tr></tfoot>
  </table>
  <div class="center"><button type="button" id="btnPrint" name="btnPrint" onclick="window.print()">In hóa đơn</button></div>
  <% } %>
</div>
</body></html>
