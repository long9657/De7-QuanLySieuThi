package controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import dao.SupplierDAO;
import model.Supplier0;

@WebServlet("/supplier")
public class SupplierServlet extends HttpServlet {
    private final SupplierDAO supplierDAO = new SupplierDAO();

    /** GET: hiển thị trang tìm NCC; nếu có txtSupplierName thì tìm theo tên. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name = req.getParameter("txtSupplierName");
        if (name != null) {
            ArrayList<Supplier0> list = supplierDAO.findSuppliersByName(name);
            req.getSession().setAttribute("listSupplier", list);   // để ProductServlet lấy NCC được chọn
        }
        req.getRequestDispatcher("/view/warehousestaff/SearchSupplierUI0.jsp").forward(req, res);
    }

    /** POST: thêm nhà cung cấp mới (từ AddSupplierUI0.jsp) rồi hiển thị lại để chọn. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Supplier0 s = new Supplier0();
        s.setName(req.getParameter("txtName"));
        s.setAddress(req.getParameter("txtAddress"));
        s.setPhoneNumber(req.getParameter("txtPhoneNumber"));
        s.setEmail(req.getParameter("txtEmail"));
        s.setDescription(req.getParameter("txtDescription"));
        ArrayList<Supplier0> list = new ArrayList<>();
        if (supplierDAO.saveSupplier(s)) {
            list.add(s);
            req.setAttribute("message", "Thêm nhà cung cấp thành công");
        } else if (s.getId() > 0) {
            list.add(s);
            req.setAttribute("message", "Nhà cung cấp đã tồn tại (trùng tên, số điện thoại hoặc email), hãy chọn trong danh sách");
        } else {
            req.setAttribute("message", "Thêm nhà cung cấp thất bại");
        }
        req.getSession().setAttribute("listSupplier", list);
        req.getRequestDispatcher("/view/warehousestaff/SearchSupplierUI0.jsp").forward(req, res);
    }
}
