-- Sinh từ mục 3.2 của tài liệu thiết kế
CREATE TABLE tblUser0 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  dateOfBirth DATE,
  gender VARCHAR(10),
  address VARCHAR(255),
  email VARCHAR(255) UNIQUE,
  phoneNumber VARCHAR(20) UNIQUE,
  role VARCHAR(20) NOT NULL      -- customer | manager | warehouse | sales | delivery
);
CREATE TABLE tblSupplier0 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  address VARCHAR(255),
  phoneNumber VARCHAR(20),
  email VARCHAR(255),
  description TEXT
);
CREATE TABLE tblProduct0 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  type VARCHAR(100),
  description TEXT,
  price DECIMAL(12,2) NOT NULL DEFAULT 0,
  quantity INT NOT NULL DEFAULT 0
);
-- Lớp trung gian SupplierProduct0: NCC nào cung cấp mặt hàng nào, kèm giá nhập gần nhất (n–n)
CREATE TABLE tblSupplierProduct0 (
  supplierId INT NOT NULL,
  productId INT NOT NULL,
  importPrice DECIMAL(12,2) NOT NULL DEFAULT 0,   -- giá nhập gần nhất từ NCC này
  PRIMARY KEY (supplierId, productId),
  FOREIGN KEY (supplierId) REFERENCES tblSupplier0(id),
  FOREIGN KEY (productId) REFERENCES tblProduct0(id)
);
CREATE TABLE tblImage0 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  path VARCHAR(1000) NOT NULL,
  productId INT NOT NULL,
  FOREIGN KEY (productId) REFERENCES tblProduct0(id)
);
CREATE TABLE tblImportInvoice0 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  importDate DATE NOT NULL,
  totalAmount DECIMAL(14,2) NOT NULL,
  supplierId INT NOT NULL,
  warehouseStaffId INT NOT NULL,
  FOREIGN KEY (supplierId) REFERENCES tblSupplier0(id),
  FOREIGN KEY (warehouseStaffId) REFERENCES tblUser0(id)
);
CREATE TABLE tblImportInvoiceDetail0 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  quantity INT NOT NULL,
  unitPrice DECIMAL(12,2) NOT NULL,
  importInvoiceId INT NOT NULL,
  productId INT NOT NULL,
  FOREIGN KEY (importInvoiceId) REFERENCES tblImportInvoice0(id),
  FOREIGN KEY (productId) REFERENCES tblProduct0(id)
);
CREATE TABLE tblOnlineOrder0 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  orderDate DATE NOT NULL,
  exportDate DATE NULL,
  totalAmount DECIMAL(14,2) NOT NULL,
  status VARCHAR(20) NOT NULL,    -- Chờ duyệt | Đã duyệt | Đã xuất
  customerId INT NOT NULL,
  warehouseStaffId INT NULL,
  deliveryStaffId INT NULL,
  FOREIGN KEY (customerId) REFERENCES tblUser0(id),
  FOREIGN KEY (warehouseStaffId) REFERENCES tblUser0(id),
  FOREIGN KEY (deliveryStaffId) REFERENCES tblUser0(id)
);
CREATE TABLE tblOnlineOrderDetail0 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  quantity INT NOT NULL,
  unitPrice DECIMAL(12,2) NOT NULL,
  onlineOrderId INT NOT NULL,
  productId INT NOT NULL,
  FOREIGN KEY (onlineOrderId) REFERENCES tblOnlineOrder0(id),
  FOREIGN KEY (productId) REFERENCES tblProduct0(id)
);
CREATE TABLE tblDirectOrder0 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  purchaseDate DATE NOT NULL,
  totalAmount DECIMAL(14,2) NOT NULL,
  salesStaffId INT NOT NULL,
  customerId INT NULL,
  FOREIGN KEY (salesStaffId) REFERENCES tblUser0(id),
  FOREIGN KEY (customerId) REFERENCES tblUser0(id)
);
CREATE TABLE tblDirectOrderDetail0 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  quantity INT NOT NULL,
  unitPrice DECIMAL(12,2) NOT NULL,
  directOrderId INT NOT NULL,
  productId INT NOT NULL,
  FOREIGN KEY (directOrderId) REFERENCES tblDirectOrder0(id),
  FOREIGN KEY (productId) REFERENCES tblProduct0(id)
);
CREATE TABLE tblCart0 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  customerId INT NOT NULL UNIQUE,
  FOREIGN KEY (customerId) REFERENCES tblUser0(id)
);
CREATE TABLE tblCartDetail0 (
  id INT AUTO_INCREMENT PRIMARY KEY,
  quantity INT NOT NULL,
  cartId INT NOT NULL,
  productId INT NOT NULL,
  FOREIGN KEY (cartId) REFERENCES tblCart0(id),
  FOREIGN KEY (productId) REFERENCES tblProduct0(id)
);
