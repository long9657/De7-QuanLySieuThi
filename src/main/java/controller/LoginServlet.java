package controller;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import dao.DAO;
import model.WarehouseStaff0;

/**
 * Đăng nhập tối giản để chạy thử module nhập hàng (không thuộc 2 module của đề).
 * Lưu nhân viên kho vào session với khóa "user" – ProductServlet đọc khóa này.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static class LoginDAO extends DAO {
        /**
         * Tìm tài khoản theo tên đăng nhập (bỏ dấu cách thừa ở hai đầu, phân biệt hoa thường) và mật khẩu.
         * @return role của tài khoản, hoặc null nếu sai tên đăng nhập / mật khẩu; nếu là nhân viên kho
         *         thì đối tượng WarehouseStaff0 được gán vào staff[0]
         */
        String find(String username, String password, WarehouseStaff0[] staff) throws Exception {
            String sql = "SELECT * FROM tblUser0 WHERE username COLLATE utf8mb4_bin = ? AND password COLLATE utf8mb4_bin = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, username == null ? "" : username.trim());
                ps.setString(2, password == null ? "" : password);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) return null;
                String role = rs.getString("role");
                if ("warehouse".equals(role)) {
                    WarehouseStaff0 w = new WarehouseStaff0();
                    w.setId(rs.getInt("id"));
                    w.setName(rs.getString("name"));
                    w.setUsername(rs.getString("username"));
                    w.setPhoneNumber(rs.getString("phoneNumber"));
                    staff[0] = w;
                }
                return role;
            }
        }
    }

    /** GET: đăng xuất (btnLogout) – hủy session rồi quay về LoginUI0.jsp. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();
        res.sendRedirect(req.getContextPath() + "/view/user/LoginUI0.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            WarehouseStaff0[] staff = new WarehouseStaff0[1];
            String role = new LoginDAO().find(req.getParameter("txtUsername"), req.getParameter("txtPassword"), staff);
            if (staff[0] != null) {
                req.getSession().setAttribute("user", staff[0]);
                res.sendRedirect(req.getContextPath() + "/view/warehousestaff/WarehouseHomeUI0.jsp");
                return;
            }
            req.setAttribute("message", role == null
                    ? "Sai tên đăng nhập hoặc mật khẩu"
                    : "Tài khoản này không phải nhân viên kho (ví dụ tài khoản khách hàng), không vào được chức năng nhập hàng");
        } catch (Exception e) {
            req.setAttribute("message", "Lỗi: " + e.getMessage());
        }
        req.getRequestDispatcher("/view/user/LoginUI0.jsp").forward(req, res);
    }
}
