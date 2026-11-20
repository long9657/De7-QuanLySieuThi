-- Dữ liệu mẫu để chạy thử (tài khoản chỉ dùng cho bản demo cục bộ)
INSERT INTO tblUser0(name, username, password, dateOfBirth, gender, address, email, phoneNumber, role)
VALUES ('Nguyễn Văn Kho', 'kho01', 'kho01', '1995-05-10', 'Nam', 'Hà Nội', 'kho01@qlst.vn', '0912001122', 'warehouse');
INSERT INTO tblUser0(name, username, password, dateOfBirth, gender, address, email, phoneNumber, role)
VALUES ('Trần Thị Bình', 'tranbinh', 'binh123', '2000-01-20', 'Nữ', 'TP.HCM', 'tranbinh@gmail.com', '0987000111', 'customer');

INSERT INTO tblSupplier0(name, address, phoneNumber, email, description) VALUES
('Công ty Điện Quang', 'Q. Tân Bình, TP.HCM', '0283811111', 'lienhe@dienquang.vn', 'Thiết bị điện gia dụng');
INSERT INTO tblSupplier0(name, address, phoneNumber, email, description) VALUES
('Công ty Điện máy Xanh', 'Q. Bình Thạnh, TP.HCM', '0283822222', 'b2b@dienmayxanh.vn', 'Phân phối điện máy');
INSERT INTO tblSupplier0(name, address, phoneNumber, email, description) VALUES
('Công ty TNHH Điện Việt', 'Q. Cầu Giấy, Hà Nội', '0243833333', 'sales@dienviet.vn', '');

-- Mặt hàng, tblSupplierProduct0 ghi NCC nào cung cấp mặt hàng nào (n–n)
INSERT INTO tblProduct0(name, type, description, price, quantity) VALUES
('Tivi Samsung 55 inch', 'Tivi', 'Smart TV 4K UHD', 12990000, 15);
INSERT INTO tblProduct0(name, type, description, price, quantity) VALUES
('Tủ lạnh Panasonic 300L', 'Tủ lạnh', 'Inverter, ngăn đá trên', 8790000, 6);
INSERT INTO tblProduct0(name, type, description, price, quantity) VALUES
('Máy giặt Electrolux 9kg', 'Máy giặt', 'Cửa trước, Inverter', 7490000, 4);
INSERT INTO tblProduct0(name, type, description, price, quantity) VALUES
('Tivi LG 50 inch', 'Tivi', 'Smart TV webOS', 9990000, 8);
INSERT INTO tblProduct0(name, type, description, price, quantity) VALUES
('Tivi Sony 65 inch', 'Tivi', 'Google TV 4K', 24500000, 3);
INSERT INTO tblProduct0(name, type, description, price, quantity) VALUES
('Điều hòa Daikin 1HP', 'Điều hòa', 'Inverter, 9000BTU', 10490000, 5);
INSERT INTO tblProduct0(name, type, description, price, quantity) VALUES
('Nồi cơm điện Sharp 1.8L', 'Gia dụng', 'Nắp gài, lòng chống dính', 890000, 20);
INSERT INTO tblProduct0(name, type, description, price, quantity) VALUES
('Quạt đứng Panasonic', 'Gia dụng', '5 cánh, điều khiển từ xa', 1590000, 12);

INSERT INTO tblSupplierProduct0(supplierId, productId, importPrice) VALUES
(1, 1, 11000000),
(1, 2, 7500000),
(1, 3, 6400000),
(2, 4, 8500000),
(2, 5, 21000000),
(2, 6, 9000000),
(3, 7, 700000),
(3, 8, 1300000),
(2, 1, 11200000),
(3, 2, 7600000);
