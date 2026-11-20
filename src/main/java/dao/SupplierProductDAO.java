package dao;

import java.sql.*;
import model.SupplierProduct0;

public class SupplierProductDAO extends DAO {
    public SupplierProductDAO() { super(); }

    /**
     * Ghi giá nhập gần nhất của mặt hàng sp.product từ nhà cung cấp sp.supplier
     * (gọi khi lưu hóa đơn nhập, trong cùng giao dịch với ImportInvoiceDAO.saveInvoice).
     * @return true nếu cập nhật được đúng 1 bản ghi
     */
    public boolean updateImportPrice(SupplierProduct0 sp) {
        String sql = "UPDATE tblSupplierProduct0 SET importPrice = ? WHERE supplierId = ? AND productId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setFloat(1, sp.getImportPrice());
            ps.setInt(2, sp.getSupplier().getId());
            ps.setInt(3, sp.getProduct().getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
