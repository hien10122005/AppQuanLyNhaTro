# 📋 Danh Sách Nhiệm Vụ Chi Tiết - Dự Án QLNhaTro

Tài liệu này liệt kê mọi đầu việc nhỏ nhất. Hãy đánh dấu `[x]` khi bạn hoàn thành một mục.

---

## 🏠 1. Quản Lý Phòng (Rooms)
- [x] Thiết kế giao diện danh sách phòng (`activity_danh_sach_phong.xml`).
- [x] Thiết kế giao diện chi tiết phòng (`activity_chi_tiet_phong.xml`).
- [x] Thiết kế giao diện thêm/sửa phòng (`activity_them_sua_phong.xml`).
- [x] **Tạo `PhongAdapter.java`**: Xử lý hiển thị từng dòng phòng (Số phòng, Giá, Trạng thái).
- [x] **Xử lý `DanhSachPhongActivity.java`**:
    - [x] Lấy danh sách từ `PhongRepository`.
    - [x] Gán Adapter vào RecyclerView.
    - [x] Xử lý sự kiện Click vào phòng để xem chi tiết.
- [x] **Xử lý `ThemSuaPhongActivity.java`**:
    - [x] Ánh xạ các trường: Tên phòng, Loại phòng, Giá, Diện tích.
    - [x] Viết hàm Validate (không để trống tên và giá).
    - [x] Gọi `PhongRepository.insertPhong()` khi thêm mới.
    - [x] Gọi `PhongRepository.updatePhong()` khi chỉnh sửa.
- [x] **Xử lý `ChiTietPhongActivity.java`**:
    - [x] Hiển thị đầy đủ thông tin phòng.
    - [ ] Thêm nút Xóa phòng (có hộp thoại xác nhận).

---

## 👥 2. Quản Lý Khách Thuê (Tenants)
- [x] Thiết kế giao diện danh sách khách (`activity_danh_sach_khach_thue.xml`).
- [ ] **Thiết kế giao diện chi tiết khách thuê** (`activity_chi_tiet_khach_thue.xml`) - *CHƯA CÓ*.
- [x] Thiết kế giao diện thêm/sửa khách (`activity_them_sua_khach_thue.xml`).
- [ ] **Tạo `KhachThueAdapter.java`**: Hiển thị tên, SĐT và CCCD khách.
- [ ] **Xử lý `DanhSachKhachThueActivity.java`**:
    - [ ] Load dữ liệu từ `KhachThueRepository`.
    - [ ] Cài đặt thanh tìm kiếm khách theo tên hoặc SĐT.
- [ ] **Xử lý `ThemSuaKhachThueActivity.java`**:
    - [ ] Xử lý chọn ngày sinh (DatePicker).
    - [ ] Lưu thông tin khách vào Database.

---

## 📜 3. Quản Lý Hợp Đồng (Contracts)
- [x] Thiết kế giao diện danh sách hợp đồng (`activity_danh_sach_hop_dong.xml`).
- [x] Thiết kế giao diện chi tiết hợp đồng (`activity_chi_tiet_hop_dong.xml`).
- [x] **Thiết kế lại giao diện thêm/sửa hợp đồng** (`activity_them_sua_hop_dong.xml`).
- [ ] **Tạo `HopDongAdapter.java`**: Hiển thị tên phòng + tên khách + ngày hết hạn.
- [ ] **Xử lý `ThemSuaHopDongActivity.java`**:
    - [ ] Load danh sách Phòng vào Spinner (chỉ hiện phòng "Trống").
    - [ ] Load danh sách Khách thuê vào Spinner.
    - [ ] Tính toán ngày kết thúc dựa trên thời hạn hợp đồng.
    - [ ] **Logic quan trọng**: Khi ký hợp đồng xong, phải tự động cập nhật trạng thái Phòng sang "Đang thuê".

---

## ⚡ 4. Điện Nước & Dịch Vụ (Operations)
- [x] Thiết kế giao diện cấu hình giá dịch vụ (`activity_cau_hinh_gia_dich_vu.xml`).
- [x] Viết logic lưu đơn giá vào Database (`CauHinhGiaDichVuActivity.java`).
- [x] Thiết kế giao diện nhập chỉ số (`activity_nhap_chi_so.xml`).
- [x] **Xử lý `NhapChiSoActivity.java`**:
    - [x] Load danh sách hợp đồng đang hiệu lực.
    - [x] Tự động lấy chỉ số cũ từ tháng trước.
    - [x] Tính toán số tiêu thu (Số mới - Số cũ).
    - [x] Lưu vào bảng `chi_so_dich_vu_thang`.

---

## 💰 5. Hóa Đơn & Thu Tiền (Billing)
- [x] **Lập hóa đơn (`LapHoaDonActivity.java`)**:
    - [x] Logic tính tổng tiền: Phòng + Điện + Nước + Dịch vụ khác.
    - [x] Tự động tạo mã hóa đơn duy nhất.
- [x] **Danh sách hóa đơn**:
    - [x] Hiển thị danh sách hóa đơn theo tháng/năm.
    - [x] Phân biệt màu sắc: Đã thu (Xanh), Chưa thu (Đỏ).
- [x] **Ghi nhận thu tiền (`ThuTienActivity.java`)**:
    - [x] Nhập số tiền khách trả (hỗ trợ trả một phần).
    - [x] Cập nhật số tiền "Còn nợ" trong hóa đơn.
    - [x] Lưu lịch sử vào bảng `thanh_toan`.

---

## 📊 6. Thống Kê & Hệ Thống
- [x] **Dashboard (`TongQuanActivity.java`)**:
    - [x] Hiển thị tổng số phòng, phòng trống.
    - [x] Hiển thị doanh thu dự kiến và thực thu tháng hiện tại.
- [x] **Báo cáo (`BaoCaoActivity.java`)**:
    - [x] Biểu đồ tỉ lệ thu tiền.
    - [x] Thống kê nguồn thu chi tiết theo từng loại dịch vụ.
- [x] **Cài đặt & Tiện ích**:
    - [x] Chỉnh sửa hồ sơ chủ trọ.
    - [x] Đổi mật khẩu.
    - [x] Xuất báo cáo ra file Excel/CSV.

---

## 🔧 7. Bảo Trì & Sự Cố (Maintenance)
- [x] Thiết kế giao diện danh sách bảo trì (`activity_danh_sach_bao_tri.xml`).
- [x] Thiết kế form báo cáo sự cố mới (`activity_them_sua_bao_tri.xml`).
- [ ] **Tạo `BaoTriAdapter.java`**: Hiển thị thẻ sự cố (Tên phòng, Loại hư hỏng, Ngày báo).
- [ ] **Xử lý `DanhSachBaoTriActivity.java`**:
    - [ ] Viết logic lọc theo tab: "Đang chờ", "Đang sửa", "Hoàn thành".
    - [ ] Xử lý tìm kiếm sự cố theo phòng.
- [ ] **Xử lý `ThemSuaBaoTriActivity.java`**:
    - [ ] Chọn phòng bị sự cố từ danh sách.
    - [ ] Chụp ảnh/Chọn ảnh minh họa (nếu có thể - tùy chọn).
    - [ ] Cập nhật trạng thái và chi phí sửa chữa khi hoàn thành.

---

## 🎨 8. Tối Ưu & Đánh Bóng (Final Polish)
- [ ] Kiểm tra lỗi khi Database trống (Empty States - Hiển thị hình ảnh "Không có dữ liệu").
- [ ] **Bổ sung nút Xóa (Delete)** vào các màn hình Chi tiết (Phòng, Khách, Hợp đồng, Bảo trì).
- [ ] Thêm hộp thoại xác nhận (Confirm Dialog) trước khi Xóa bất kỳ dữ liệu nào.
- [ ] Kiểm tra tính phản hồi của giao diện trên các kích thước màn hình khác nhau.
- [ ] Tối ưu tốc độ tải danh sách (Sử dụng DiffUtil nếu cần).

---
*Cập nhật: 26/04/2026 - Antigravity AI*
