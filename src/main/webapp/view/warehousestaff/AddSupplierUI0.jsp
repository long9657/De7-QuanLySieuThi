<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="_guard.jspf" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><title>Thêm nhà cung cấp</title>
<link rel="stylesheet" href="<%= ctx %>/css/simple.css"></head><body>
<div class="card medium">
  <button type="button" class="back" id="btnBack" name="btnBack"
          onclick="location.href='<%= ctx %>/view/warehousestaff/SearchSupplierUI0.jsp'">Quay lại</button>
  <h1>Thêm mới nhà cung cấp</h1>
  <%@ include file="../_message.jspf" %>
  <form action="<%= ctx %>/supplier" method="post">
    <label for="txtName">Tên nhà cung cấp *</label><input type="text" id="txtName" name="txtName" required>
    <label for="txtAddress">Địa chỉ</label><input type="text" id="txtAddress" name="txtAddress">
    <div class="grid2">
      <div><label for="txtPhoneNumber">Số điện thoại</label><input type="text" id="txtPhoneNumber" name="txtPhoneNumber"></div>
      <div><label for="txtEmail">Email</label><input type="email" id="txtEmail" name="txtEmail"></div>
    </div>
    <label for="txtDescription">Mô tả</label><input type="text" id="txtDescription" name="txtDescription">
    <div class="center"><button type="submit" class="ok" id="btnSave" name="btnSave">Lưu</button></div>
  </form>
</div>
</body></html>
