package model;

/**
 * Lớp trung gian của quan hệ n–n Supplier0 – Product0: nhà cung cấp nào cung cấp mặt hàng nào,
 * kèm giá nhập gần nhất của mặt hàng đó từ nhà cung cấp đó (cập nhật khi lưu hóa đơn nhập).
 */
public class SupplierProduct0 {
    private Supplier0 supplier;
    private Product0 product;
    private float importPrice;   // giá nhập gần nhất; 0 = chưa nhập lần nào

    public SupplierProduct0() { super(); }

    public Supplier0 getSupplier() { return supplier; }
    public void setSupplier(Supplier0 supplier) { this.supplier = supplier; }
    public Product0 getProduct() { return product; }
    public void setProduct(Product0 product) { this.product = product; }
    public float getImportPrice() { return importPrice; }
    public void setImportPrice(float importPrice) { this.importPrice = importPrice; }
}
