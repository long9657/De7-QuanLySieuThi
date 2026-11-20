package dao;

import java.sql.*;
import model.Customer0;

public class CustomerDAO extends DAO {
    public CustomerDAO() { super(); }

    /**
     * Kiểm tra tên đăng nhập, email, số điện thoại của c đã có trong tblUser0 chưa.
     * @param c khách hàng đang đăng kí
     * @return true nếu bị trùng, false nếu hợp lệ
     */
    public boolean checkCustomer(Customer0 c) {
        String sql = "SELECT COUNT(*) FROM tblUser0 WHERE username = ? OR email = ? OR phoneNumber = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getUsername());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPhoneNumber());
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * Lưu khách hàng mới vào tblUser0 với role = 'customer'; gán id sinh ra vào c.
     * @return true nếu lưu thành công
     */
    public boolean saveCustomer(Customer0 c) {
        String sql = "INSERT INTO tblUser0(name, username, password, dateOfBirth, gender, address, email, phoneNumber, role) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'customer')";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getUsername());
            ps.setString(3, c.getPassword());   // khi cài đặt thật nên lưu mật khẩu đã băm
            ps.setDate(4, new java.sql.Date(c.getDateOfBirth().getTime()));
            ps.setString(5, c.getGender());
            ps.setString(6, c.getAddress());
            ps.setString(7, c.getEmail());
            ps.setString(8, c.getPhoneNumber());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) c.setId(keys.getInt(1));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
