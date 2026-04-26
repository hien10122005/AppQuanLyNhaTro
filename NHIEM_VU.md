# 📋 Danh Sách Nhiệm Vụ - Người 2: Luồng Nghiệp Vụ & Tài Chính

File này liệt kê các nhiệm vụ chuyên biệt của **Người 2** (phụ trách Tài chính & Luồng chính) để bạn dễ dàng theo dõi và không bị lẫn với các module khác.

---

## ✅ Đã Hoàn Thành (Done)
- [x] **Cấu trúc chính:** Thiết lập khung sườn chính của app (`activity_main.xml`, BottomNavigationView).
- [x] **Nhập chỉ số:**
    - [x] `activity_nhap_chi_so.xml`: Giao diện nhập số điện/nước.
    - [x] `ChiSoRepository`: Logic xử lý lưu/truy vấn chỉ số.
    - [x] `NhapChiSoActivity`: Logic điều khiển màn hình nhập chỉ số.
- [x] **Màn Hình Tổng Quan (Dashboard):**
    - [x] `activity_tong_quan.xml`: Thiết kế giao diện H-TECH (Hero Card, Bento Grid, Hóa đơn gần đây, Tỉ lệ lấp đầy, Bottom Nav).
    - [x] Các drawable hỗ trợ: `bg_hero_card`, `bg_bento_*`, `bg_chip_*`, `progress_bar_green`, `bg_avatar_circle`.
    - [x] `TongQuanActivity.java`: Query DB tính tổng phòng, tiền đã thu/chưa thu, hóa đơn quá hạn, tỉ lệ lấp đầy.
- [x] **Cài đặt (Settings):**
    - [x] `activity_cai_dat.xml`: Giao diện Bento Grid, Deep Dark & Glassmorphism.
    - [x] `CaiDatActivity.java`: Logic điều hướng và xử lý sự kiện menu.
    - [x] Hệ thống icon: `ic_settings_service`, `ic_people`, `ic_notifications`, `ic_excel`, `ic_lock`, `ic_logout`.
- [x] **Cấu hình giá dịch vụ:**
    - [x] `activity_cau_hinh_gia_dich_vu.xml`: Giao diện bento với các ô nhập liệu premium cho Điện, Nước, Internet, Rác.
    - [x] `CauHinhGiaDichVuActivity.java`: Logic lưu trữ đơn giá vào Database qua `DichVuRepository`.
    - [x] Cập nhật `DichVuRepository.java`: Thêm phương thức `saveBangGiaChung`.
- [x] **Chỉnh sửa hồ sơ:**
    - [x] `activity_chinh_sua_ho_so.xml`: Giao diện mờ ảo với khu vực Avatar và các trường thông tin Họ tên, SĐT, Email, Địa chỉ.
    - [x] `ChinhSuaHoSoActivity.java`: Logic lưu trữ thông tin cá nhân bằng `SharedPreferences`.
    - [x] `ic_camera.xml`: Icon mới cho nút thay đổi ảnh đại diện.
    - [x] Tích hợp làm mới dữ liệu tại `CaiDatActivity`.
- [x] **Đổi mật khẩu:**
    - [x] `activity_doi_mat_khau.xml`: Giao diện bento với các trường mật khẩu và nút ẩn/hiện.
    - [x] `DoiMatKhauActivity.java`: Logic kiểm tra mật khẩu cũ, độ dài mật khẩu mới và khớp mật khẩu.
    - [x] `ic_visibility.xml`, `ic_visibility_off.xml`: Biểu tượng ẩn/hiện mật khẩu.
- [x] **Xuất dữ liệu Excel:**
    - [x] `activity_xuat_excel.xml`: Giao diện bento chọn loại dữ liệu và khoảng thời gian.
    - [x] `XuatExcelActivity.java`: Logic xuất dữ liệu ra file `.csv` (tương thích Excel) sử dụng Scoped Storage (`CreateDocument`).
    - [x] `ic_analytics.xml`, `ic_receipt.xml`: Các icon bổ trợ.
- [x] Đã đẩy code lên GitHub.





---

## 🚀 Nhiệm Vụ Cần Làm Tiếp (To-Do)

### 2. Nghiệp vụ Hóa Đơn (Module quan trọng nhất)
- [x] **Danh sách hóa đơn:**
    - [x] `activity_danh_sach_hoa_don.xml`: Giao diện danh sách.
    - [x] `item_hoa_don.xml`: Thiết kế từng dòng hóa đơn (mã HĐ, tên phòng, tổng tiền, trạng thái).
    - [x] `DanhSachHoaDonActivity` & `HoaDonAdapter`: Code logic hiển thị dữ liệu.
- [x] **Chi tiết hóa đơn:**
    - [x] `activity_chi_tiet_hoa_don.xml`: Hiển thị bảng kê chi tiết (Điện + Nước + Phòng + Dịch vụ).
    - [x] `ChiTietHoaDonActivity`: Hiển thị dữ liệu cụ thể của 1 hóa đơn.

### 3. Nghiệp vụ Liên Quan (Tài chính)
- [x] **Lập hóa đơn tự động (`activity_lap_hoa_don.xml`):**
    - [x] Code logic tính toán: `(Số mới - Số cũ) * Đơn giá`.
    - [x] Tổng hợp các dịch vụ trong hợp đồng để tạo hóa đơn tháng mới.
- [x] **Ghi nhận thu tiền (`activity_thu_tien.xml`):**
    - [x] Màn hình xác nhận khách đã trả tiền.
    - [x] Cập nhật trạng thái `da_thanh_toan = 1` trong Database.

### 4. Thống kê & Báo cáo
- [x] **Màn hình báo cáo (`activity_bao_cao.xml`):**
    - [x] Thống kê doanh thu, số tiền đã thu, còn nợ theo tháng/năm.
    - [x] Tỉ lệ lấp đầy phòng.
    - [x] Phân tích nguồn thu chi tiết (Tiền phòng, điện, nước, dịch vụ khác).

---
*Ghi chú: Tập trung vào tính chính xác của con số và luồng thanh toán.*
