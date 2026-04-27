package com.example.quanlynhatro.data.model;

/**
 * KhachThueVm (View Model) - Chứa thông tin khách thuê và tên phòng đang ở
 * Dùng để hiển thị lên danh sách.
 */
public class KhachThueVm extends KhachThue implements java.io.Serializable {
    private String tenPhong;
    private boolean dangThue;

    public String getTenPhong() {
        return tenPhong != null ? tenPhong : "Chưa thuê";
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public boolean isDangThue() {
        return dangThue;
    }

    public void setDangThue(boolean dangThue) {
        this.dangThue = dangThue;
    }
}
