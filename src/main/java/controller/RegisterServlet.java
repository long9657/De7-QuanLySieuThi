package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import dao.CustomerDAO;
import model.Customer0;

/** Nhận form đăng kí từ RegisterUI0.jsp, kiểm tra trùng và lưu khách hàng. */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final CustomerDAO customerDAO = new CustomerDAO();

    /**
     * Đóng gói Customer0 từ request → checkCustomer → saveCustomer,
     * đặt thông báo vào request rồi forward về RegisterUI0.jsp.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String message;
        try {
            Customer0 c = new Customer0();
            c.setName(req.getParameter("txtName"));
            c.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("txtDateOfBirth")));
            c.setGender(req.getParameter("rdoGender"));
            c.setPhoneNumber(req.getParameter("txtPhoneNumber"));
            c.setEmail(req.getParameter("txtEmail"));
            c.setAddress(req.getParameter("txtAddress"));
            c.setUsername(req.getParameter("txtUsername"));
            c.setPassword(req.getParameter("txtPassword"));

            if (!c.getPassword().equals(req.getParameter("txtConfirmPassword"))) {
                message = "Mật khẩu xác nhận không khớp";
            } else if (customerDAO.checkCustomer(c)) {
                message = "Thông tin đã được đăng kí, vui lòng dùng thông tin khác";
            } else if (customerDAO.saveCustomer(c)) {
                message = "Đăng kí thành viên thành công";
            } else {
                message = "Có lỗi khi lưu, vui lòng thử lại";
            }
        } catch (java.text.ParseException e) {
            message = "Ngày sinh không hợp lệ";
        }
        req.setAttribute("message", message);
        req.getRequestDispatcher("/view/user/RegisterUI0.jsp").forward(req, res);
    }
}
