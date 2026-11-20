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

    /** Tìm theo tên trong toàn bộ danh mục để gợi ý cho trang Thêm mặt hàng. */
    public ArrayList<Product0> findCatalogByName(String name) throws SQLException {
        ArrayList<Product0> products = new ArrayList<>();
        String sql = "SELECT * FROM tblProduct0 WHERE LOWER(name) LIKE LOWER(?) ORDER BY name, id";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product0 product = new Product0();
                    readProduct(rs, product);
                    products.add(product);
                }
            }
        }
        return products;
    }

    private static void readProduct(ResultSet rs, Product0 p) throws SQLException {
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setType(rs.getString("type"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getFloat("price"));
        p.setQuantity(rs.getInt("quantity"));
    }

    /**
     * Dùng lại mặt hàng theo ID nếu người dùng chọn từ danh mục, hoặc tìm theo tên
     * nếu họ nhập tay. Chỉ tạo mặt hàng mới khi tên nhập tay chưa tồn tại.
     * Liên kết đã có với nhà cung cấp giữ nguyên giá nhập gần nhất.
     */
    public boolean saveProduct(Product0 p, Supplier0 s) {
        boolean selected = p.getId() > 0;
        String sqlFind = selected ? "SELECT * FROM tblProduct0 WHERE id = ?"
                : "SELECT * FROM tblProduct0 WHERE LOWER(TRIM(name)) COLLATE utf8mb4_bin = LOWER(TRIM(?)) LIMIT 1";
        String sql = "INSERT INTO tblProduct0(name, type, description, price, quantity) VALUES (?, ?, ?, ?, 0)";
        String sqlLink = "INSERT INTO tblSupplierProduct0(supplierId, productId) VALUES (?, ?) "
                + "ON DUPLICATE KEY UPDATE importPrice = importPrice";
        try {
            con.setAutoCommit(false);
            boolean found;
            try (PreparedStatement pf = con.prepareStatement(sqlFind)) {
                if (selected) pf.setInt(1, p.getId());
                else pf.setString(1, p.getName());
                try (ResultSet rs = pf.executeQuery()) {
                    found = rs.next();
                    if (found) readProduct(rs, p);
                }
            }
            if (!found && selected) {
                con.rollback();
                return false;
            }
            if (!found) {
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
