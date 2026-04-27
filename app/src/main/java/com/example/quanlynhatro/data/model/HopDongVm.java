package com.example.quanlynhatro.data.model;

/**
 * Lớp này giống như một "View" trong SQL hoặc một ViewModel trong C# WPF/WinForms.
 * Nó kết hợp dữ liệu từ bảng Hợp Đồng, bảng Phòng và bảng Khách Thuê.
 * Giúp Adapter hiển thị dữ liệu nhanh mà không cần gọi DB liên tục.
 */
public class HopDongVm extends HopDong {
    private String tenPhong;
    private String tenKhachThue;
    private String sdtKhachThue;
    private String cccdKhachThue;

    public HopDongVm() {
        super();
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public String getTenKhachThue() {
        return tenKhachThue;
    }

    public void setTenKhachThue(String tenKhachThue) {
        this.tenKhachThue = tenKhachThue;
    }

    public String getSdtKhachThue() {
        return sdtKhachThue;
    }

    public void setSdtKhachThue(String sdtKhachThue) {
        this.sdtKhachThue = sdtKhachThue;
    }

    public String getCccdKhachThue() {
        return cccdKhachThue;
    }

    public void setCccdKhachThue(String cccdKhachThue) {
        this.cccdKhachThue = cccdKhachThue;
    }
}
