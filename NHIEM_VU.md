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
    - [x] Thêm nút Xóa phòng (có hộp thoại xác nhận).

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
- [x] **Tạo `HopDongAdapter.java`**: Hiển thị tên phòng + tên khách + ngày hết hạn.
- [x] **Xử lý `ThemSuaHopDongActivity.java`**:
    - [x] Load danh sách Phòng vào Spinner (chỉ hiện phòng "Trống").
    - [x] Load danh sách Khách thuê vào Spinner.
    - [x] Tính toán ngày kết thúc dựa trên thời hạn hợp đồng.
    - [x] **Logic quan trọng**: Khi ký hợp đồng xong, phải tự động cập nhật trạng thái Phòng sang "Đang thuê".

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

## 🚀 9. Chức Năng Nên Thêm Sớm
- [ ] **Cảnh báo hợp đồng sắp hết hạn**:
    - [ ] Viết query lấy các hợp đồng còn hiệu lực và sắp hết hạn trong 7/15/30 ngày.
    - [ ] Hiển thị danh sách/cảnh báo trên `TongQuanActivity.java` hoặc `DanhSachHopDongActivity.java`.
    - [ ] Tô màu hoặc gắn nhãn "Sắp hết hạn" để người dùng dễ nhận biết.
- [ ] **Cảnh báo hóa đơn quá hạn, còn nợ**:
    - [ ] Viết query lấy hóa đơn có `con_no > 0` và `han_thanh_toan < ngày hiện tại`.
    - [ ] Hiển thị số lượng hóa đơn quá hạn trên dashboard.
    - [ ] Thêm filter "Quá hạn" trong `DanhSachHoaDonActivity.java`.
- [ ] **Tìm kiếm và lọc phòng, khách thuê, hóa đơn**:
    - [ ] Thêm ô Search trên các màn hình danh sách.
    - [ ] Lọc phòng theo trạng thái, loại phòng.
    - [ ] Lọc khách theo tên, SĐT, CCCD.
    - [ ] Lọc hóa đơn theo tháng, năm, trạng thái thanh toán.
- [ ] **Xóa dữ liệu có hộp thoại xác nhận**:
    - [ ] Bổ sung nút xóa trong màn hình chi tiết.
    - [ ] Hiển thị `AlertDialog` xác nhận trước khi xóa.
    - [ ] Kiểm tra ràng buộc khóa ngoại trước khi xóa dữ liệu.
- [ ] **Lịch sử thanh toán của từng hóa đơn**:
    - [ ] Viết query lấy danh sách bản ghi từ bảng `thanh_toan` theo `hoa_don_id`.
    - [ ] Tạo UI hiển thị các lần thu tiền: ngày thu, số tiền, phương thức.
    - [ ] Hiển thị tổng đã thu và số còn nợ ngay trong màn hình chi tiết hóa đơn.

---

## 🏢 10. Chức Năng Rất Thực Tế Cho Quản Lý Nhà Trọ
- [ ] **Sao lưu và khôi phục dữ liệu SQLite**:
    - [ ] Tạo chức năng export file database ra bộ nhớ máy.
    - [ ] Tạo chức năng import/khôi phục từ file database đã sao lưu.
    - [ ] Kiểm tra an toàn dữ liệu trước khi ghi đè database hiện tại.
- [ ] **Xuất PDF hóa đơn hoặc phiếu thu**:
    - [ ] Thiết kế mẫu PDF cho hóa đơn/phiếu thu.
    - [ ] Tạo file PDF từ dữ liệu hóa đơn và thanh toán.
    - [ ] Chức năng lưu/chia sẻ PDF.
- [ ] **Quản lý nhiều người ở trong một phòng**:
    - [ ] Tận dụng bảng `hop_dong_thanh_vien`.
    - [ ] Thêm giao diện chọn nhiều khách cho 1 hợp đồng.
    - [ ] Hiển thị danh sách thành viên đang ở trong phòng.
- [ ] **Lịch sử thuê của một khách**:
    - [ ] Query danh sách hợp đồng theo `khach_thue_dai_dien_id`.
    - [ ] Hiển thị các phòng đã từng thuê, thời gian ở, trạng thái hợp đồng.
- [ ] **Lịch sử ở của một phòng**:
    - [ ] Query các hợp đồng theo `phong_id`.
    - [ ] Hiển thị danh sách khách đã từng ở, thời gian thuê, tiền thuê theo gốc từng kỳ.
- [ ] **Ghi nhận chi phí phát sinh ngoài điện nước**:
    - [ ] Hoàn thiện cấu hình giá cho internet, rác, giữ xe, dịch vụ khác.
    - [ ] Cho phép gán dịch vụ phát sinh theo phòng.
    - [ ] Đưa các khoản phí này vào luồng lập hóa đơn hàng tháng.

---

## 🎨 11. Tối Ưu & Hoàn Thiện Giao Diện (UI/UX)
- [ ] Thiết kế giao diện danh sách các phòng trống (`activity_danh_sach_phong_trong.xml`) - *CHƯA CÓ*.
- [ ] Thêm mục "Phòng trống" vào Dashboard (bên cạnh mục "Phòng đang ở").
- [ ] Bổ sung hiệu ứng Ripple cho CardView, Button và các thành phần tương tác để tăng tính thẩm mỹ.
- [ ] Thống nhất icon trên BottomNavigationView: đảm bảo tất cả các tab đều có icon hiển thị đồng bộ (`ic_home`, `ic_contract`, `ic_bell`, `ic_settings`).

---
*Cập nhật: 26/04/2026 - Antigravity AI*
