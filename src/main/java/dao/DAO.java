package dao;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Lớp cha của mọi DAO: giữ một kết nối MySQL dùng chung cho toàn hệ thống.
 *
 * Thông tin kết nối đọc từ db.properties (src/main/resources); có thể ghi đè khi chạy bằng
 * -Dqlst.db.url=... -Dqlst.db.user=... -Dqlst.db.password=...
 * Lần chạy đầu (database qlst chưa có bảng) tự tạo bảng từ schema.sql và nạp dữ liệu mẫu seed.sql.
 */
public abstract class DAO {
    protected static Connection con;

    public DAO() {
        if (con == null) {
            synchronized (DAO.class) {
                if (con == null) {
                    try {
                        Properties cfg = new Properties();
                        try (InputStream in = DAO.class.getResourceAsStream("/db.properties")) {
                            if (in != null) cfg.load(in);
                        }
                        String url = System.getProperty("qlst.db.url", cfg.getProperty("qlst.db.url"));
                        String user = System.getProperty("qlst.db.user", cfg.getProperty("qlst.db.user"));
                        String pass = System.getProperty("qlst.db.password", cfg.getProperty("qlst.db.password"));
                        con = DriverManager.getConnection(url, user, pass);
                        initSchemaIfEmpty();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /** Lần chạy đầu: tạo bảng (schema.sql) và nạp dữ liệu mẫu (seed.sql). */
    private static void initSchemaIfEmpty() throws Exception {
        try (Statement st = con.createStatement()) {
            st.executeQuery("SELECT 1 FROM tblUser0 LIMIT 1").close();
            try {                                     // database tạo trước khi có cột giá nhập gần nhất
                st.executeQuery("SELECT importPrice FROM tblSupplierProduct0 LIMIT 1").close();
            } catch (SQLException thieuCot) {
                st.execute("ALTER TABLE tblSupplierProduct0 ADD COLUMN importPrice DECIMAL(12,2) NOT NULL DEFAULT 0");
            }
            return;                                   // đã có bảng trong database đang dùng
        } catch (SQLException chuaCoBang) {
            // chưa có bảng → tạo mới
        }
        for (String file : new String[]{"/schema.sql", "/seed.sql"}) {
            try (InputStream in = DAO.class.getResourceAsStream(file);
                 Statement st = con.createStatement()) {
                String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                for (String stmt : sql.split(";")) {
                    String s = stmt.replaceAll("(?m)--.*$", "").trim();
                    if (!s.isEmpty()) st.execute(s);
                }
            }
        }
    }
}
