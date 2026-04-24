package com.example.quanlynhatro.data.model;

public class HoaDonChiTiet {
    private int id;
    private int hoaDonId;
    private int loaiDichVuId;
    private String tenMucPhi;
    private double soLuong;
    private double donGiaApDung;
    private double thanhTien;
    private Double chiSoCu;
    private Double chiSoMoi;
    private String ghiChu;

    public HoaDonChiTiet() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHoaDonId() {
        return hoaDonId;
    }

    public void setHoaDonId(int hoaDonId) {
        this.hoaDonId = hoaDonId;
    }

    public int getLoaiDichVuId() {
        return loaiDichVuId;
    }

    public void setLoaiDichVuId(int loaiDichVuId) {
        this.loaiDichVuId = loaiDichVuId;
    }

    public String getTenMucPhi() {
        return tenMucPhi;
    }

    public void setTenMucPhi(String tenMucPhi) {
        this.tenMucPhi = tenMucPhi;
    }

    public double getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(double soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGiaApDung() {
        return donGiaApDung;
    }

    public void setDonGiaApDung(double donGiaApDung) {
        this.donGiaApDung = donGiaApDung;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public Double getChiSoCu() {
        return chiSoCu;
    }

    public void setChiSoCu(Double chiSoCu) {
        this.chiSoCu = chiSoCu;
    }

    public Double getChiSoMoi() {
        return chiSoMoi;
    }

    public void setChiSoMoi(Double chiSoMoi) {
        this.chiSoMoi = chiSoMoi;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
