package com.example.quanlynhatro.data.model;

/**
 * ============================================================
 * MODEL: HoaDonVm (View Model - Dữ liệu đã gộp sẵn để hiển thị)
 * ============================================================
 * Lý do tạo lớp này (thay vì dùng HoaDon thuần):
 *
 * Bảng HOA_DON trong DB chỉ lưu phong_id, hop_dong_id (là số).
 * Nhưng trong danh sách, ta cần hiển thị "Tên phòng", "Tên khách".
 * Vì vậy ta JOIN nhiều bảng rồi đóng gói kết quả vào HoaDonVm.
 *
 * Tương tự trong C# WinForms: đây giống như một DTO (Data Transfer Object)
 * hoặc lớp bạn tạo để bind vào DataGridView.
 * ============================================================
 */
public class HoaDonVm {

    // --- Thông tin hóa đơn ---
    private int id;
    private String maHoaDon;     // Mã hóa đơn: "HĐ-2604001"
    private int thang;
    private int nam;
    private String hanThanhToan; // Hạn nộp tiền: "2026-05-10"
    private double tongTien;     // Tổng tiền phải trả
    private double daThanhToan;  // Tiền đã trả
    private double conNo;        // Tiền còn nợ
    private String trangThai;    // "DA_THANH_TOAN" / "CHUA_THANH_TOAN"

    // --- Thông tin JOIN từ các bảng khác ---
    // Những trường này KHÔNG có trong bảng hoa_don, được lấy từ JOIN
    private String tenPhong;     // Lấy từ bảng phong (ví dụ: "Phòng 101")
    private String tenKhach;     // Lấy từ bảng khach_thue (ví dụ: "Nguyễn Văn A")

    // Constructor rỗng (bắt buộc phải có)
    public HoaDonVm() {}

    // ============================================================
    // GETTER & SETTER
    // Trong Android/Java, biến private → bắt buộc có getter/setter
    // Tương tự property trong C#: public string TenPhong { get; set; }
    // ============================================================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }

    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public String getHanThanhToan() { return hanThanhToan; }
    public void setHanThanhToan(String hanThanhToan) { this.hanThanhToan = hanThanhToan; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public double getDaThanhToan() { return daThanhToan; }
    public void setDaThanhToan(double daThanhToan) { this.daThanhToan = daThanhToan; }

    public double getConNo() { return conNo; }
    public void setConNo(double conNo) { this.conNo = conNo; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public String getTenKhach() { return tenKhach; }
    public void setTenKhach(String tenKhach) { this.tenKhach = tenKhach; }
}
