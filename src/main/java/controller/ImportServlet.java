package controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import dao.ImportInvoiceDAO;
import model.*;

@WebServlet("/import")
public class ImportServlet extends HttpServlet {
    private final ImportInvoiceDAO importInvoiceDAO = new ImportInvoiceDAO();
    private static final String GD_IMPORT = "/view/warehousestaff/ImportUI0.jsp";

    /**
     * action = addDetail: thêm mặt hàng vừa chọn trong ô gợi ý vào phiếu nhập (session).
     *          Nếu mặt hàng đã có trong phiếu thì cộng dồn số lượng và lấy đơn giá mới → ImportUI0.jsp.
     * action = updateDetail: sửa số lượng / đơn giá của dòng thứ index → ImportUI0.jsp.
     * action = deleteDetail: xóa dòng thứ index khỏi phiếu (lnkDelete) → ImportUI0.jsp.
     * action = save: lưu hóa đơn, tăng tồn kho → ImportInvoiceUI0.jsp (hiển thị + in).
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        ImportInvoice0 inv = (ImportInvoice0) session.getAttribute("invoice");
        String action = req.getParameter("action");
        if (inv == null) { // chưa chọn NCC hoặc phiếu đã lưu xong → quay lại tìm NCC
            res.sendRedirect(req.getContextPath() + "/view/warehousestaff/SearchSupplierUI0.jsp");
            return;
        }

        if ("addDetail".equals(action)) {
            int productId = Integer.parseInt(req.getParameter("productId"));
            int quantity = parseInt(req.getParameter("txtQuantity"));
            float unitPrice = parseFloat(req.getParameter("txtUnitPrice"));
            if (quantity <= 0 || unitPrice <= 0) {
                req.setAttribute("message", "Số lượng và đơn giá phải lớn hơn 0");
            } else {
                ImportInvoiceDetail0 existing = null;
                for (ImportInvoiceDetail0 d : inv.getDetails()) {
                    if (d.getProduct().getId() == productId) existing = d;
                }
                if (existing != null) {
                    existing.setQuantity(existing.getQuantity() + quantity);
                    existing.setUnitPrice(unitPrice);
                    updateTotal(inv);
                } else {
                    ArrayList<SupplierProduct0> listProduct = (ArrayList<SupplierProduct0>) session.getAttribute("listProduct");
                    // listProduct = kết quả tìm trong các mặt hàng của NCC đang nhập → chỉ nhận mặt hàng của NCC đó
                    if (listProduct != null) for (SupplierProduct0 sp : listProduct) {
                        Product0 p = sp.getProduct();
                        if (p.getId() == productId) {
                            ImportInvoiceDetail0 d = new ImportInvoiceDetail0();
                            d.setProduct(p);
                            d.setQuantity(quantity);
                            d.setUnitPrice(unitPrice);
                            inv.addDetail(d);
                        }
                    }
                }
            }
            backToImport(req, res);

        } else if ("updateDetail".equals(action)) {
            int index = Integer.parseInt(req.getParameter("index"));
            int quantity = parseInt(req.getParameter("txtQuantity"));
            float unitPrice = parseFloat(req.getParameter("txtUnitPrice"));
            if (quantity <= 0 || unitPrice <= 0) {
                req.setAttribute("message", "Số lượng và đơn giá phải lớn hơn 0");
            } else if (index >= 0 && index < inv.getDetails().size()) {
                ImportInvoiceDetail0 d = inv.getDetails().get(index);
                d.setQuantity(quantity);
                d.setUnitPrice(unitPrice);
                updateTotal(inv);
            }
            backToImport(req, res);

        } else if ("deleteDetail".equals(action)) {
            int index = Integer.parseInt(req.getParameter("index"));
            if (index >= 0 && index < inv.getDetails().size()) {
                inv.getDetails().remove(index);
                updateTotal(inv);
            }
            backToImport(req, res);

        } else if ("save".equals(action)) {
            if (inv.getDetails().isEmpty()) {
                req.setAttribute("message", "Phiếu nhập chưa có mặt hàng");
                req.getRequestDispatcher(GD_IMPORT).forward(req, res);
            } else if (importInvoiceDAO.saveInvoice(inv)) {
                session.removeAttribute("invoice");
                req.setAttribute("invoice", inv);
                req.setAttribute("message", "Nhập hàng thành công");
                req.getRequestDispatcher("/view/warehousestaff/ImportInvoiceUI0.jsp").forward(req, res);
            } else {
                req.setAttribute("message", "Lưu hóa đơn thất bại");
                req.getRequestDispatcher(GD_IMPORT).forward(req, res);
            }
        } else {
            req.getRequestDispatcher(GD_IMPORT).forward(req, res);
        }
    }

    /** Quay lại ImportUI0.jsp bằng redirect (F5 không gửi lại thao tác); thông báo, nếu có, gửi qua session. */
    private static void backToImport(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Object msg = req.getAttribute("message");
        if (msg != null) req.getSession().setAttribute("message", msg);
        res.sendRedirect(req.getContextPath() + GD_IMPORT);
    }

    /** Tổng tiền = tổng thành tiền các dòng. */
    private static void updateTotal(ImportInvoice0 inv) {
        float total = 0;
        for (ImportInvoiceDetail0 d : inv.getDetails()) total += d.getAmount();
        inv.setTotalAmount(total);
    }

    private static int parseInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }

    private static float parseFloat(String s) {
        try { return Float.parseFloat(s.trim()); } catch (Exception e) { return 0; }
    }
}
