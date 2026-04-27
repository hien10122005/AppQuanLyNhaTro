package com.example.quanlynhatro.data.model;

/**
 * Lớp này giống như một "View" trong SQL hoặc một ViewModel trong C# WPF/WinForms.
 * Nó kết hợp dữ liệu từ bảng Hợp Đồng, bảng Phòng và bảng Khách Thuê.
 * Giúp Adapter hiển thị dữ liệu nhanh mà không cần gọi DB liên tục.
 */
public class HopDongVm extends HopDong {
    private String tenPhong;
    private String tenKhachThue;

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
}
