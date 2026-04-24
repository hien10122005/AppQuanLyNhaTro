package com.example.quanlynhatro.data.model;

public class ChiSoDichVuThang {
    private int id;
    private int phongId;
    private int loaiDichVuId;
    private int thang;
    private int nam;
    private double chiSoCu;
    private double chiSoMoi;
    private double soLuongTieuThu;
    private String ngayChot;
    private String ghiChu;
    private String createdAt;

    public ChiSoDichVuThang() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhongId() {
        return phongId;
    }

    public void setPhongId(int phongId) {
        this.phongId = phongId;
    }

    public int getLoaiDichVuId() {
        return loaiDichVuId;
    }

    public void setLoaiDichVuId(int loaiDichVuId) {
        this.loaiDichVuId = loaiDichVuId;
    }

    public int getThang() {
        return thang;
    }

    public void setThang(int thang) {
        this.thang = thang;
    }

    public int getNam() {
        return nam;
    }

    public void setNam(int nam) {
        this.nam = nam;
    }

    public double getChiSoCu() {
        return chiSoCu;
    }

    public void setChiSoCu(double chiSoCu) {
        this.chiSoCu = chiSoCu;
    }

    public double getChiSoMoi() {
        return chiSoMoi;
    }

    public void setChiSoMoi(double chiSoMoi) {
        this.chiSoMoi = chiSoMoi;
    }

    public double getSoLuongTieuThu() {
        return soLuongTieuThu;
    }

    public void setSoLuongTieuThu(double soLuongTieuThu) {
        this.soLuongTieuThu = soLuongTieuThu;
    }

    public String getNgayChot() {
        return ngayChot;
    }

    public void setNgayChot(String ngayChot) {
        this.ngayChot = ngayChot;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
