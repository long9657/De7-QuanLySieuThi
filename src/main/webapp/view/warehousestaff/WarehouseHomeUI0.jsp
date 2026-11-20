<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="_guard.jspf" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><title>Trang chủ nhân viên kho</title>
<link rel="stylesheet" href="<%= ctx %>/css/simple.css"></head><body>
<div class="card medium">
  <h1>Trang chủ nhân viên kho</h1>
  <p class="center">Xin chào, <b><%= esc(user.getName()) %></b></p>
  <div class="stack">
    <a class="btn ok" id="lnkImport" href="<%= ctx %>/supplier">Nhập hàng từ nhà cung cấp</a>
    <a class="btn gray" id="btnLogout" href="<%= ctx %>/login">Đăng xuất</a>
  </div>
</div>
</body></html>
