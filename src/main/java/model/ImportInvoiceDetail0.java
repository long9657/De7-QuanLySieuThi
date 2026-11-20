package model;

import java.util.Date;
import java.util.ArrayList;

public class ImportInvoiceDetail0 {
    private int id;
    private int quantity;
    private float unitPrice;   // đơn giá nhập
    private Product0 product;   // mặt hàng được nhập

    public ImportInvoiceDetail0() { super(); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public float getUnitPrice() { return unitPrice; }
    public void setUnitPrice(float unitPrice) { this.unitPrice = unitPrice; }
    public Product0 getProduct() { return product; }
    public void setProduct(Product0 product) { this.product = product; }

    /** Thành tiền của dòng = số lượng × đơn giá nhập. */
    public float getAmount() { return quantity * unitPrice; }
}
