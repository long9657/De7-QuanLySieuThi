package model;

import java.util.Date;
import java.util.ArrayList;

/**
 * Hóa đơn nhập hàng từ một nhà cung cấp.
 */
public class ImportInvoice0 {
    private int id;
    private Date importDate;
    private float totalAmount;
    private Supplier0 supplier;   // nhà cung cấp của lần nhập
    private WarehouseStaff0 warehouseStaff;   // nhân viên kho lập hóa đơn
    private ArrayList<ImportInvoiceDetail0> details;   // các dòng hàng nhập

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getImportDate() { return importDate; }
    public void setImportDate(Date importDate) { this.importDate = importDate; }
    public float getTotalAmount() { return totalAmount; }
    public void setTotalAmount(float totalAmount) { this.totalAmount = totalAmount; }
    public Supplier0 getSupplier() { return supplier; }
    public void setSupplier(Supplier0 supplier) { this.supplier = supplier; }
    public WarehouseStaff0 getWarehouseStaff() { return warehouseStaff; }
    public void setWarehouseStaff(WarehouseStaff0 warehouseStaff) { this.warehouseStaff = warehouseStaff; }
    public ArrayList<ImportInvoiceDetail0> getDetails() { return details; }
    public void setDetails(ArrayList<ImportInvoiceDetail0> details) { this.details = details; }

    public ImportInvoice0() {
        details = new ArrayList<>();
        importDate = new Date();
    }

    /** Thêm một dòng hàng vào phiếu và cộng dồn tổng tiền (thay cho addDetail() pha phân tích). */
    public void addDetail(ImportInvoiceDetail0 d) {
        details.add(d);
        totalAmount += d.getAmount();
    }
}
