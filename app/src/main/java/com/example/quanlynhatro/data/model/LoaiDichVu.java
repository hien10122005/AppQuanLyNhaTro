package com.example.quanlynhatro.data.model;

public class LoaiDichVu {
    private int id;
    private String maLoai;
    private String tenLoai;
    private String kieuTinh;
    private String donVi;
    private int hoatDong;

    public LoaiDichVu() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getKieuTinh() {
        return kieuTinh;
    }

    public void setKieuTinh(String kieuTinh) {
        this.kieuTinh = kieuTinh;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public int getHoatDong() {
        return hoatDong;
    }

    public void setHoatDong(int hoatDong) {
        this.hoatDong = hoatDong;
    }
}
