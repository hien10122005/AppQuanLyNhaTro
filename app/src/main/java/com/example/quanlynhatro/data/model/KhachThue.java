package com.example.quanlynhatro.data.model;

/**
 * Lớp KhachThue đại diện cho một người thuê trọ.
 * Model này chứa các thông tin cá nhân cơ bản để quản lý khách hàng.
 */
public class KhachThue {
    private int id;
    private String hoTen;
    private String soDienThoai;
    private String cccd;
    private String ngaySinh;
    private String gioiTinh;
    private String diaChiThuongTru;
    private String email;
    private String ghiChu;
    private String createdAt;
    private String updatedAt;

    // Constructor mặc định
    public KhachThue() {}

    // Constructor đầy đủ tham số
    public KhachThue(int id, String hoTen, String soDienThoai, String cccd, String ngaySinh,
                     String gioiTinh, String diaChiThuongTru, String email, String ghiChu,
                     String createdAt, String updatedAt) {
        this.id = id;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.cccd = cccd;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.diaChiThuongTru = diaChiThuongTru;
        this.email = email;
        this.ghiChu = ghiChu;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // --- GETTER và SETTER ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }
    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }
    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }
    public String getDiaChiThuongTru() { return diaChiThuongTru; }
    public void setDiaChiThuongTru(String diaChiThuongTru) { this.diaChiThuongTru = diaChiThuongTru; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
