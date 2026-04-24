package com.example.quanlynhatro.data.model;

public class BangGiaDichVu {
    private int id;
    private int loaiDichVuId;
    private Integer phongId;
    private double donGia;
    private Double soLuongMacDinh;
    private String ngayHieuLuc;
    private String ngayHetHieuLuc;
    private String ghiChu;
    private String createdAt;

    public BangGiaDichVu() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLoaiDichVuId() {
        return loaiDichVuId;
    }

    public void setLoaiDichVuId(int loaiDichVuId) {
        this.loaiDichVuId = loaiDichVuId;
    }

    public Integer getPhongId() {
        return phongId;
    }

    public void setPhongId(Integer phongId) {
        this.phongId = phongId;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public Double getSoLuongMacDinh() {
        return soLuongMacDinh;
    }

    public void setSoLuongMacDinh(Double soLuongMacDinh) {
        this.soLuongMacDinh = soLuongMacDinh;
    }

    public String getNgayHieuLuc() {
        return ngayHieuLuc;
    }

    public void setNgayHieuLuc(String ngayHieuLuc) {
        this.ngayHieuLuc = ngayHieuLuc;
    }

    public String getNgayHetHieuLuc() {
        return ngayHetHieuLuc;
    }

    public void setNgayHetHieuLuc(String ngayHetHieuLuc) {
        this.ngayHetHieuLuc = ngayHetHieuLuc;
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
