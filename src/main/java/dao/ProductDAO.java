package dao;

import java.sql.*;
import java.util.ArrayList;
import model.Product0;
import model.Supplier0;
import model.SupplierProduct0;

public class ProductDAO extends DAO {
    public ProductDAO() { super(); }

    /**
     * Tìm mặt hàng do nhà cung cấp s cung cấp (bảng nối tblSupplierProduct0) có tên chứa từ khóa.
     * @return danh sách SupplierProduct0: mỗi phần tử gồm mặt hàng và giá nhập gần nhất từ s
     */
    public ArrayList<SupplierProduct0> findProductsByName(String name, Supplier0 s) {
        ArrayList<SupplierProduct0> list = new ArrayList<>();
        String sql = "SELECT p.*, sp.importPrice FROM tblProduct0 p JOIN tblSupplierProduct0 sp ON sp.productId = p.id "
                   + "WHERE sp.supplierId = ? AND LOWER(p.name) LIKE LOWER(?) ORDER BY p.name";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, s.getId());
            ps.setString(2, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product0 p = new Product0();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setType(rs.getString("type"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getFloat("price"));
                p.setQuantity(rs.getInt("quantity"));
                SupplierProduct0 sp = new SupplierProduct0();
                sp.setSupplier(s);
                sp.setProduct(p);
                sp.setImportPrice(rs.getFloat("importPrice"));
                list.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lưu mặt hàng cho nhà cung cấp s.
     * - Mặt hàng mới hoàn toàn (chưa có tên trùng, không phân biệt hoa thường): thêm vào tblProduct0
     *   (tồn kho ban đầu = 0) và tạo dòng SupplierProduct0 (giá nhập = 0 cho tới lần nhập đầu).
     * - Mặt hàng đã có (do nhà cung cấp khác cung cấp): không thêm mặt hàng mới, chỉ tạo dòng
     *   SupplierProduct0 nối mặt hàng cũ với s (đã nối rồi thì giữ nguyên); p nhận lại thông tin mặt hàng cũ.
     * @return true nếu lưu thành công; p.id là id của mặt hàng (mới hoặc cũ)
     */
    public boolean saveProduct(Product0 p, Supplier0 s) {
        String sqlFind = "SELECT * FROM tblProduct0 WHERE LOWER(TRIM(name)) COLLATE utf8mb4_bin = LOWER(TRIM(?)) LIMIT 1";
        String sql = "INSERT INTO tblProduct0(name, type, description, price, quantity) VALUES (?, ?, ?, ?, 0)";
        String sqlLink = "INSERT IGNORE INTO tblSupplierProduct0(supplierId, productId) VALUES (?, ?)";   // importPrice mặc định 0
        try {
            con.setAutoCommit(false);
            PreparedStatement pf = con.prepareStatement(sqlFind);
            pf.setString(1, p.getName());
            ResultSet rs = pf.executeQuery();
            if (rs.next()) {                                   // mặt hàng đã có → dùng lại
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setType(rs.getString("type"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getFloat("price"));
                p.setQuantity(rs.getInt("quantity"));
            } else {                                           // mặt hàng mới hoàn toàn → thêm
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, p.getName().trim());
                ps.setString(2, p.getType());
                ps.setString(3, p.getDescription());
                ps.setFloat(4, p.getPrice());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) p.setId(keys.getInt(1));
            }
            PreparedStatement pl = con.prepareStatement(sqlLink);
            pl.setInt(1, s.getId());
            pl.setInt(2, p.getId());
            pl.executeUpdate();
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

    /**
     * Cộng qty vào số lượng tồn của mặt hàng id (qty âm khi bán / xuất đơn).
     * @return true nếu cập nhật được đúng 1 bản ghi
     */
    public boolean updateStock(int id, int qty) {
        String sql = "UPDATE tblProduct0 SET quantity = quantity + ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
