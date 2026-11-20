<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%! static String esc(String s) { return s == null ? "" : s.replace("&","&amp;").replace("\"","&quot;").replace("<","&lt;"); } %>
<% String ctx = request.getContextPath();
   String message = (String) request.getAttribute("message");
   boolean ok = message != null && message.contains("thành công"); %>
<%! String v(javax.servlet.http.HttpServletRequest r, boolean ok, String n) { return ok ? "" : esc(r.getParameter(n)); } %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><title>Đăng kí thành viên</title>
<link rel="stylesheet" href="<%= ctx %>/css/simple.css"></head><body>
<div class="card medium">
  <button type="button" class="back" id="btnBack" name="btnBack"
          onclick="location.href='<%= ctx %>/view/user/LoginUI0.jsp'">Quay lại</button>
  <h1>Đăng kí thành viên</h1>
  <% if (message != null) { %><div id="message" class="msg <%= ok ? "ok" : "" %>"><%= esc(message) %><% if (ok) { %> – <a id="lnkLogin" href="<%= ctx %>/view/user/LoginUI0.jsp">Đăng nhập</a><% } %></div><% } %>
  <form action="<%= ctx %>/register" method="post">
    <div class="grid2">
      <div><label for="txtName">Họ và tên *</label><input type="text" id="txtName" name="txtName" value="<%= v(request, ok, "txtName") %>" required></div>
      <div><label for="txtDateOfBirth">Ngày sinh *</label><input type="date" id="txtDateOfBirth" name="txtDateOfBirth" value="<%= v(request, ok, "txtDateOfBirth") %>" required></div>
      <div><label>Giới tính</label>
        <label class="inline"><input type="radio" id="rdoGender" name="rdoGender" value="Nam" checked> Nam</label> &nbsp;
        <label class="inline"><input type="radio" name="rdoGender" value="Nữ"> Nữ</label></div>
      <div><label for="txtPhoneNumber">Số điện thoại *</label><input type="text" id="txtPhoneNumber" name="txtPhoneNumber" value="<%= v(request, ok, "txtPhoneNumber") %>" pattern="0[0-9]{9}" required></div>
      <div><label for="txtEmail">Email *</label><input type="email" id="txtEmail" name="txtEmail" value="<%= v(request, ok, "txtEmail") %>" required></div>
      <div><label for="txtAddress">Địa chỉ</label><input type="text" id="txtAddress" name="txtAddress" value="<%= v(request, ok, "txtAddress") %>"></div>
      <div><label for="txtUsername">Tên đăng nhập *</label><input type="text" id="txtUsername" name="txtUsername" value="<%= v(request, ok, "txtUsername") %>" required></div>
      <div></div>
      <div><label for="txtPassword">Mật khẩu *</label><input type="password" id="txtPassword" name="txtPassword" minlength="6" required></div>
      <div><label for="txtConfirmPassword">Xác nhận mật khẩu *</label><input type="password" id="txtConfirmPassword" name="txtConfirmPassword" minlength="6" required></div>
    </div>
    <div class="center"><button type="submit" class="ok" id="btnAdd" name="btnAdd">Thêm</button></div>
  </form>
</div>
</body></html>
