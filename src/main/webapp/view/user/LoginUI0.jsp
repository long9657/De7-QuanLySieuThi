<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% String ctx = request.getContextPath(); %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><title>Đăng nhập</title>
<link rel="stylesheet" href="<%= ctx %>/css/simple.css"></head><body>
<div class="card narrow">
  <h1>Đăng nhập</h1>
  <%@ include file="../_message.jspf" %>
  <form action="<%= ctx %>/login" method="post">
    <label for="txtUsername">Tên đăng nhập</label>
    <input type="text" id="txtUsername" name="txtUsername" required>
    <label for="txtPassword">Mật khẩu</label>
    <input type="password" id="txtPassword" name="txtPassword" required>
    <div class="center"><button type="submit" id="btnLogin" name="btnLogin">Đăng nhập</button></div>
    <div class="center"><a id="lnkRegister" href="<%= ctx %>/view/user/RegisterUI0.jsp">Đăng kí thành viên</a></div>
  </form>
</div>
</body></html>
