package com.example.quanlynhatro.data.model;

/**
 * Lớp Phong là một "Model" (hoặc POJO - Plain Old Java Object).
 * Nó đóng vai trò như một cái khuôn mẫu để chứa thông tin của một căn phòng.
 * Mỗi khi bạn lấy dữ liệu từ CSDL, bạn sẽ đổ vào đối tượng Phong này để dễ dàng sử dụng trong ứng dụng.
 */
public class Phong {
    // Các biến private (Tính đóng gói): Chỉ có lớp này mới truy cập trực tiếp được các biến này.
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

    // Constructor mặc định (Không tham số): Cần thiết để một số thư viện có thể tạo đối tượng trống.
    public Phong() {}

    // Constructor có tham số: Dùng để khởi tạo nhanh một đối tượng với đầy đủ thông tin.
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

    // --- GETTER và SETTER ---
    // Vì các biến ở trên là private, nên ta cần các phương thức public này 
    // để các lớp khác có thể "Lấy" (Get) hoặc "Ghi" (Set) giá trị vào biến.

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
