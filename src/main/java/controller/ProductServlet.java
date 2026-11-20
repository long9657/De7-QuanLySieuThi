package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import dao.ProductDAO;
import model.*;

@WebServlet("/product")
public class ProductServlet extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();

    /**
     * GET ?supplierId: tạo phiếu nhập mới trong session cho NCC được chọn → ImportUI0.jsp.
     * GET ?txtProductName: tìm mặt hàng của NCC đang nhập theo tên; trả JSON cho ô gợi ý
     *     (popup) trên ImportUI0.jsp và lưu kết quả (danh sách SupplierProduct0) vào session (listProduct).
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        String supplierId = req.getParameter("supplierId");
        if (supplierId != null) {
            int id = Integer.parseInt(supplierId);
            ArrayList<Supplier0> listSupplier = (ArrayList<Supplier0>) session.getAttribute("listSupplier");
            if (listSupplier != null) for (Supplier0 s : listSupplier) {
                if (s.getId() == id) {
                    ImportInvoice0 inv = new ImportInvoice0();
                    inv.setSupplier(s);
                    inv.setWarehouseStaff((WarehouseStaff0) session.getAttribute("user"));
                    session.setAttribute("invoice", inv);
                    session.removeAttribute("listProduct");
                }
            }
            res.sendRedirect(req.getContextPath() + "/view/warehousestaff/ImportUI0.jsp");
            return;
        }

        ImportInvoice0 inv = (ImportInvoice0) session.getAttribute("invoice");
        String name = req.getParameter("txtProductName");
        if (name != null && inv != null) {
            ArrayList<SupplierProduct0> list = productDAO.findProductsByName(name.trim(), inv.getSupplier());
            session.setAttribute("listProduct", list);
            res.setContentType("application/json; charset=UTF-8");
            PrintWriter out = res.getWriter();
            out.print("[");
            for (int i = 0; i < list.size(); i++) {
                Product0 p = list.get(i).getProduct();
                if (i > 0) out.print(",");
                out.print("{\"id\":" + p.getId() + ",\"name\":" + json(p.getName()) + ",\"type\":" + json(p.getType())
                        + ",\"price\":" + p.getPrice() + ",\"quantity\":" + p.getQuantity()
                        + ",\"importPrice\":" + list.get(i).getImportPrice() + "}");
            }
            out.print("]");
            return;
        }
        res.sendRedirect(req.getContextPath() + "/view/warehousestaff/"
                + (inv == null ? "SearchSupplierUI0.jsp" : "ImportUI0.jsp"));
    }

    /**
     * POST: thêm mặt hàng mới (từ AddProductUI0.jsp) cho NCC của phiếu nhập đang lập,
     * rồi quay về ImportUI0.jsp với mặt hàng đó được chọn sẵn để nhập số lượng, đơn giá.
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        ImportInvoice0 inv = (ImportInvoice0) session.getAttribute("invoice");
        if (inv == null) {
            res.sendRedirect(req.getContextPath() + "/view/warehousestaff/SearchSupplierUI0.jsp");
            return;
        }
        Product0 p = new Product0();
        p.setName(req.getParameter("txtName"));
        p.setType(req.getParameter("txtType"));
        p.setDescription(req.getParameter("txtDescription"));
        p.setPrice(Float.parseFloat(req.getParameter("txtPrice")));
        if (productDAO.saveProduct(p, inv.getSupplier())) {
            ArrayList<SupplierProduct0> list = (ArrayList<SupplierProduct0>) session.getAttribute("listProduct");
            if (list == null) list = new ArrayList<>();
            SupplierProduct0 sp = new SupplierProduct0();
            sp.setSupplier(inv.getSupplier());
            sp.setProduct(p);
            list.add(sp);
            session.setAttribute("listProduct", list);
            session.setAttribute("pickedProduct", p);
            session.setAttribute("message", "Đã lưu mặt hàng " + p.getName() + " cho " + inv.getSupplier().getName()
                    + " thành công (mặt hàng đã có thì chỉ ghi nhận thêm nhà cung cấp, không tạo trùng)");
            res.sendRedirect(req.getContextPath() + "/view/warehousestaff/ImportUI0.jsp");
        } else {
            req.setAttribute("message", "Thêm mặt hàng thất bại");
            req.getRequestDispatcher("/view/warehousestaff/AddProductUI0.jsp").forward(req, res);
        }
    }

    private static String json(String s) {
        if (s == null) return "\"\"";
        StringBuilder b = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            if (c == '"' || c == '\\') b.append('\\').append(c);
            else if (c < 0x20) b.append(String.format("\\u%04x", (int) c));
            else b.append(c);
        }
        return b.append('"').toString();
    }
}
