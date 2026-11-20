# QLST – bản chạy thử (MVC – Servlet)

Code `model`, `dao` (trừ `DAO.java`), `controller` (4 Servlet) sinh nguyên văn từ tài liệu
`7_B23DCCN500_HOAN_CHINH_SERVLET.docx`. Phần chỉ dùng để chạy thử:

| File | Vai trò |
|---|---|
| `src/main/java/dao/DAO.java` | Kết nối MySQL theo `db.properties`, tự tạo bảng từ `schema.sql` và nạp `seed.sql` lần chạy đầu |
| `src/main/resources/db.properties` | URL, tài khoản, mật khẩu MySQL |
| `src/main/java/controller/LoginServlet.java` | Đăng nhập tối giản cho nhân viên kho (lưu session `"user"`) |
| `src/main/java/Launcher.java` | Khởi động Tomcat 9 nhúng |
| `src/main/webapp/view/**` | Các trang JSP |

## Chạy

Cần JDK 21 và Maven. Nhấp đúp `run.cmd` (hoặc `mvn compile exec:java`), mở
http://localhost:8080/ .

Tài khoản mẫu (chỉ cho bản demo cục bộ) nằm trong `src/main/resources/seed.sql`:
nhân viên kho `kho01` / `kho01`, khách hàng `tranbinh` / `binh123`.

## CSDL MySQL

Cần MySQL 8 chạy ở `localhost:3306`. Thông tin kết nối trong `src/main/resources/db.properties`
(mặc định user `root`). Lần chạy đầu ứng dụng tự tạo database `qlst`, các bảng (`schema.sql`)
và dữ liệu mẫu (`seed.sql`). Muốn làm lại dữ liệu từ đầu: `DROP DATABASE qlst;` rồi chạy lại.

Có thể ghi đè khi chạy: `-Dqlst.db.url=... -Dqlst.db.user=... -Dqlst.db.password=...`
