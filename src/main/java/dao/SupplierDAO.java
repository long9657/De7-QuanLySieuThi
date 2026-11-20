package dao;

import java.sql.*;
import java.util.ArrayList;
import model.Supplier0;

public class SupplierDAO extends DAO {
    public SupplierDAO() { super(); }

    /**
     * Tìm nhà cung cấp có tên chứa từ khóa.
     * @param name từ khóa tên nhà cung cấp
     * @return danh sách Supplier0 (rỗng nếu không tìm thấy)
     */
    public ArrayList<Supplier0> findSuppliersByName(String name) {
        ArrayList<Supplier0> list = new ArrayList<>();
        String sql = "SELECT * FROM tblSupplier0 WHERE name LIKE ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Supplier0 s = new Supplier0();          // đóng gói thông tin thực thể
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setAddress(rs.getString("address"));
                s.setPhoneNumber(rs.getString("phoneNumber"));
                s.setEmail(rs.getString("email"));
                s.setDescription(rs.getString("description"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Thêm nhà cung cấp mới; gán id sinh ra vào s.
     * Nếu đã có nhà cung cấp trùng tên, số điện thoại hoặc email thì không thêm,
     * nạp thông tin nhà cung cấp đã có vào s và trả false.
     * @return true nếu thêm thành công
     */
    public boolean saveSupplier(Supplier0 s) {
        String sqlCheck = "SELECT * FROM tblSupplier0 WHERE LOWER(TRIM(name)) = LOWER(TRIM(?))"
                + " OR (? <> '' AND phoneNumber = ?) OR (? <> '' AND LOWER(email) = LOWER(?)) LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sqlCheck)) {
            String phone = s.getPhoneNumber() == null ? "" : s.getPhoneNumber().trim();
            String email = s.getEmail() == null ? "" : s.getEmail().trim();
            ps.setString(1, s.getName());
            ps.setString(2, phone);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setString(5, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setAddress(rs.getString("address"));
                s.setPhoneNumber(rs.getString("phoneNumber"));
                s.setEmail(rs.getString("email"));
                s.setDescription(rs.getString("description"));
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        String sql = "INSERT INTO tblSupplier0(name, address, phoneNumber, email, description) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getAddress());
            ps.setString(3, s.getPhoneNumber());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getDescription());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) s.setId(keys.getInt(1));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
