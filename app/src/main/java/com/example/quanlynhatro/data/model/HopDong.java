package com.example.quanlynhatro.data.model;

public class HopDong {
    private int id;
    private String maHopDong;
    private int phongId;
    private int khachThueDaiDienId;
    private String ngayKy;
    private String ngayBatDau;
    private String ngayKetThuc;
    private double giaThueChot;
    private double tienCoc;
    private int chuKyThanhToan;
    private String trangThai;
    private String ghiChu;
    private String createdAt;
    private String updatedAt;

    public HopDong() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaHopDong() { return maHopDong; }
    public void setMaHopDong(String maHopDong) { this.maHopDong = maHopDong; }

    public int getPhongId() { return phongId; }
    public void setPhongId(int phongId) { this.phongId = phongId; }

    public int getKhachThueDaiDienId() { return khachThueDaiDienId; }
    public void setKhachThueDaiDienId(int khachThueDaiDienId) { this.khachThueDaiDienId = khachThueDaiDienId; }

    public String getNgayKy() { return ngayKy; }
    public void setNgayKy(String ngayKy) { this.ngayKy = ngayKy; }

    public String getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(String ngayBatDau) { this.ngayBatDau = ngayBatDau; }
    public String getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(String ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public double getGiaThueChot() { return giaThueChot; }
    public void setGiaThueChot(double giaThueChot) { this.giaThueChot = giaThueChot; }

    public double getTienCoc() { return tienCoc; }
    public void setTienCoc(double tienCoc) { this.tienCoc = tienCoc; }

    public int getChuKyThanhToan() { return chuKyThanhToan; }
    public void setChuKyThanhToan(int chuKyThanhToan) { this.chuKyThanhToan = chuKyThanhToan; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
