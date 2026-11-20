package dao;

import java.sql.*;
import model.ImportInvoice0;
import model.ImportInvoiceDetail0;
import model.SupplierProduct0;

public class ImportInvoiceDAO extends DAO {
    public ImportInvoiceDAO() { super(); }

    /**
     * Lưu hóa đơn nhập và các dòng chi tiết, đồng thời tăng tồn kho từng mặt hàng và ghi giá nhập
     * gần nhất của mặt hàng từ nhà cung cấp này (SupplierProduct0).
     * Thực hiện trong một giao dịch: lỗi ở bất kì bước nào thì hủy toàn bộ.
     * @param inv hóa đơn đã có supplier, warehouseStaff, details
     * @return true nếu lưu thành công
     */
    public boolean saveInvoice(ImportInvoice0 inv) {
        String sqlInv = "INSERT INTO tblImportInvoice0(importDate, totalAmount, supplierId, warehouseStaffId) VALUES (?, ?, ?, ?)";
        String sqlDetail = "INSERT INTO tblImportInvoiceDetail0(quantity, unitPrice, importInvoiceId, productId) VALUES (?, ?, ?, ?)";
        ProductDAO productDAO = new ProductDAO();
        SupplierProductDAO supplierProductDAO = new SupplierProductDAO();
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(sqlInv, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, new java.sql.Date(inv.getImportDate().getTime()));
            ps.setFloat(2, inv.getTotalAmount());
            ps.setInt(3, inv.getSupplier().getId());
            ps.setInt(4, inv.getWarehouseStaff().getId());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) inv.setId(keys.getInt(1));

            PreparedStatement psd = con.prepareStatement(sqlDetail);
            for (ImportInvoiceDetail0 d : inv.getDetails()) {
                psd.setInt(1, d.getQuantity());
                psd.setFloat(2, d.getUnitPrice());
                psd.setInt(3, inv.getId());
                psd.setInt(4, d.getProduct().getId());
                psd.executeUpdate();
                if (!productDAO.updateStock(d.getProduct().getId(), d.getQuantity())) {
                    throw new SQLException("Không cập nhật được tồn kho");
                }
                SupplierProduct0 sp = new SupplierProduct0();
                sp.setSupplier(inv.getSupplier());
                sp.setProduct(d.getProduct());
                sp.setImportPrice(d.getUnitPrice());
                if (!supplierProductDAO.updateImportPrice(sp)) {
                    throw new SQLException("Không cập nhật được giá nhập");
                }
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ignored) { }
            e.printStackTrace();
            return false;
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException ignored) { }
        }
    }
}
