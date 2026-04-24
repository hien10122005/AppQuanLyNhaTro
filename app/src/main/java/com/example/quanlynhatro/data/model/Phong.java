package com.example.quanlynhatro.data.model;

public class Phong {
    private int id;
    private String soPhong;
    private String tenPhong;
    private String loaiPhong;
    private double giaPhong;
    private double dienTich;
    private int soNguoiToiDa;
    private String trangThai;
    private String moTa;
    private String createdAt;
    private String updatedAt;

    public Phong() {}

    public Phong(int id, String soPhong, String tenPhong, String loaiPhong, double giaPhong,
                 double dienTich, int soNguoiToiDa, String trangThai, String moTa,
                 String createdAt, String updatedAt) {
        this.id = id;
        this.soPhong = soPhong;
        this.tenPhong = tenPhong;
        this.loaiPhong = loaiPhong;
        this.giaPhong = giaPhong;
        this.dienTich = dienTich;
        this.soNguoiToiDa = soNguoiToiDa;
        this.trangThai = trangThai;
        this.moTa = moTa;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSoPhong() { return soPhong; }
    public void setSoPhong(String soPhong) { this.soPhong = soPhong; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public String getLoaiPhong() { return loaiPhong; }
    public void setLoaiPhong(String loaiPhong) { this.loaiPhong = loaiPhong; }

    public double getGiaPhong() { return giaPhong; }
    public void setGiaPhong(double giaPhong) { this.giaPhong = giaPhong; }

    public double getDienTich() { return dienTich; }
    public void setDienTich(double dienTich) { this.dienTich = dienTich; }

    public int getSoNguoiToiDa() { return soNguoiToiDa; }
    public void setSoNguoiToiDa(int soNguoiToiDa) { this.soNguoiToiDa = soNguoiToiDa; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
