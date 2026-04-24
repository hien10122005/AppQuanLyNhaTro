package com.example.quanlynhatro.data.model;

/**
 * Lớp HoaDon chứa thông tin về hóa đơn tiền phòng hàng tháng.
 * Nó liên kết với bảng Hóa Đơn trong CSDL.
 */
public class HoaDon {
    private int id;
    private int hopDongId;
    private int phongId;
    private String maHoaDon;
    private int thang;
    private int nam;
    private String ngayLap;
    private String hanThanhToan;
    private double tongTienTruocGiam;
    private double giamTru;
    private double tongTien;
    private double daThanhToan;
    private double conNo;
    private String trangThai;
    private String ghiChu;
    private String createdAt;
    private String updatedAt;

    public HoaDon() {}

    // --- GETTER và SETTER ---
    // Vì các biến ở trên là private, nên ta cần các phương thức public này 
    // để các lớp khác có thể "Lấy" (Get) hoặc "Ghi" (Set) giá trị vào biến.

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getHopDongId() { return hopDongId; }
    public void setHopDongId(int hopDongId) { this.hopDongId = hopDongId; }

    public int getPhongId() { return phongId; }
    public void setPhongId(int phongId) { this.phongId = phongId; }

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }

    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public String getNgayLap() { return ngayLap; }
    public void setNgayLap(String ngayLap) { this.ngayLap = ngayLap; }

    public String getHanThanhToan() { return hanThanhToan; }
    public void setHanThanhToan(String hanThanhToan) { this.hanThanhToan = hanThanhToan; }

    public double getTongTienTruocGiam() { return tongTienTruocGiam; }
    public void setTongTienTruocGiam(double tongTienTruocGiam) { this.tongTienTruocGiam = tongTienTruocGiam; }

    public double getGiamTru() { return giamTru; }
    public void setGiamTru(double giamTru) { this.giamTru = giamTru; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public double getDaThanhToan() { return daThanhToan; }
    public void setDaThanhToan(double daThanhToan) { this.daThanhToan = daThanhToan; }

    public double getConNo() { return conNo; }
    public void setConNo(double conNo) { this.conNo = conNo; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
