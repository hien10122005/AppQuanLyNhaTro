# 📋 Lộ Trình Phát Triển Ứng Dụng QLNhaTro (Sắp xếp theo Module)

Tài liệu này liệt kê mọi đầu việc chi tiết cho từng Model. Hãy đánh dấu `[x]` khi bạn hoàn thành một mục.

---

## 🏠 1. Module: Quản Lý Phòng (Rooms)
- [x] Thiết kế giao diện danh sách phòng (`activity_danh_sach_phong.xml`).
- [x] Thiết kế giao diện chi tiết phòng (`activity_chi_tiet_phong.xml`).
- [x] Thiết kế giao diện thêm/sửa phòng (`activity_them_sua_phong.xml`).
- [x] **Logic**: Tạo `PhongAdapter.java` xử lý hiển thị danh sách phòng.
- [x] **Logic**: Xử lý thêm/sửa phòng trong `ThemSuaPhongActivity.java`.
- [x] **Logic**: Xử lý hiển thị thông tin và Xóa phòng (có xác nhận) trong `ChiTietPhongActivity.java`.
- [x] **Tìm kiếm**: Thêm ô tìm kiếm phòng theo số phòng hoặc tên phòng.
- [x] **Bộ lọc**: Lọc danh sách phòng theo trạng thái (Trống, Đang thuê, Bảo trì).
- [x] **Lịch sử**: Xem danh sách các khách thuê đã từng ở trong phòng này (đã thêm nút & logic repository).

## 👥 2. Module: Quản Lý Khách Thuê (Tenants)
- [x] Thiết kế giao diện danh sách khách (`activity_danh_sach_khach_thue.xml`).
- [x] Thiết kế giao diện thêm/sửa khách (`activity_them_sua_khach_thue.xml`).
- [ ] **Giao diện**: Thiết kế màn hình chi tiết khách thuê (`activity_chi_tiet_khach_thue.xml`).
- [ ] **Logic**: Tạo `KhachThueAdapter.java` hiển thị Tên, SĐT và CCCD.
- [ ] **Logic**: Viết code xử lý Lưu/Cập nhật khách (có tích hợp DatePicker cho ngày sinh).
- [ ] **Logic**: Thêm tính năng Xóa khách thuê (có kiểm tra ràng buộc hợp đồng).
- [ ] **Tìm kiếm**: Cài đặt thanh tìm kiếm khách theo Tên hoặc Số điện thoại.
- [ ] **Lịch sử**: Xem danh sách các phòng khách này đã từng thuê từ trước đến nay.

## 📜 3. Module: Quản Lý Hợp Đồng (Contracts)
- [x] Thiết kế giao diện danh sách và chi tiết hợp đồng.
- [x] Thiết kế lại giao diện lập hợp đồng mới (`activity_them_sua_hop_dong.xml`) theo phong cách Glassmorphism.
- [x] **Logic**: Tạo `HopDongAdapter.java` hiển thị Tên phòng + Tên khách + Ngày hết hạn.
- [x] **Logic**: Xử lý lập hợp đồng mới (Load phòng trống, chọn khách, tự động tính ngày kết thúc).
- [x] **Logic quan trọng**: Tự động cập nhật trạng thái Phòng sang "Đang thuê" ngay khi ký hợp đồng thành công.
- [ ] **Thông minh**: Tự động tô màu đỏ hoặc gắn nhãn "Sắp hết hạn" nếu hợp đồng còn < 15 ngày.
- [ ] **Thành viên**: Quản lý danh sách nhiều người ở trong cùng 1 phòng (sử dụng bảng `hop_dong_thanh_vien`).
- [ ] **Xóa/Kết thúc**: Xử lý kết thúc hợp đồng và trả phòng (chuyển trạng thái phòng về "Trống").

## ⚡ 4. Module: Điện, Nước & Dịch Vụ (Utilities)
- [x] Thiết kế giao diện cấu hình đơn giá dịch vụ (`activity_cau_hinh_gia_dich_vu.xml`).
- [x] Thiết kế giao diện nhập chỉ số điện nước (`activity_nhap_chi_so.xml`).
- [x] **Logic**: Lưu và cập nhật đơn giá dịch vụ vào Database.
- [x] **Logic**: Tự động lấy chỉ số cũ từ tháng trước và tính toán tiêu thụ thực tế.
- [ ] **Dịch vụ khác**: Ghi nhận các chi phí phát sinh đột xuất (phí vệ sinh thêm, tiền phạt, tiền thưởng).
- [ ] **Thống kê**: Biểu đồ theo dõi xu hướng sử dụng điện/nước của từng phòng.

## 💰 5. Module: Hóa Đơn & Thanh Toán (Billing)
- [x] **Logic**: Lập hóa đơn hàng tháng (Tổng tiền = Phòng + Điện + Nước + Dịch vụ).
- [x] **Logic**: Danh sách hóa đơn phân loại theo tháng/năm.
- [x] **Logic**: Ghi nhận thu tiền (Cho phép thu một phần, tự động tính toán số tiền còn nợ).
- [ ] **Lịch sử**: Xem danh sách các đợt trả tiền của một hóa đơn (ai thu, ngày nào, bao nhiêu).
- [ ] **Xuất bản**: Chức năng xuất hóa đơn ra file PDF hoặc ảnh để gửi cho khách qua mạng xã hội.
- [ ] **Thông minh**: Cảnh báo danh sách các hóa đơn đã quá hạn thanh toán.

## 📊 6. Module: Dashboard & Hệ Thống (System)
- [x] **Dashboard**: Thống kê số lượng phòng trống và phòng đang ở.
- [x] **Dashboard**: Hiển thị tổng doanh thu dự kiến và số tiền thực tế đã thu trong tháng.
- [x] **Hệ thống**: Chỉnh sửa thông tin hồ sơ chủ trọ và đổi mật khẩu.
- [x] **Báo cáo**: Chức năng xuất toàn bộ dữ liệu báo cáo ra file Excel.
- [ ] **Bảo mật**: Chức năng Sao lưu (Backup) toàn bộ Database ra bộ nhớ máy và Khôi phục (Restore) khi cần.
- [ ] **Giao diện**: Thêm màn hình danh sách các phòng hiện đang trống để xem nhanh.

## 🔧 7. Module: Bảo Trì & Sự Cố (Maintenance)
- [x] Thiết kế giao diện danh sách và form báo cáo sự cố.
- [ ] **Logic**: Tạo `BaoTriAdapter.java` hiển thị thẻ sự cố (Phòng nào, bị gì, ngày báo).
- [ ] **Phân loại**: Logic lọc sự cố theo trạng thái: Chờ xử lý, Đang sửa, Đã hoàn thành.
- [ ] **Chi phí**: Ghi nhận chi phí sửa chữa sự cố để trừ vào doanh thu thực tế.

## 🎨 8. Tối Ưu & Hoàn Thiện (UI/UX Polish)
- [x] Áp dụng thiết kế Bento Grid và hiệu ứng kính mờ (Glassmorphism).
- [ ] **Empty States**: Hiển thị hình ảnh minh họa "Chưa có dữ liệu" khi danh sách trống.
- [ ] **Hộp thoại**: Đảm bảo tất cả các hành động Xóa đều phải qua hộp thoại xác nhận (Confirm Dialog).
- [ ] **Hiệu ứng**: Thêm hiệu ứng Ripple và Animation khi chuyển giữa các màn hình.
- [ ] **Phản hồi**: Đảm bảo giao diện hiển thị tốt trên cả điện thoại màn hình nhỏ và máy tính bảng.

---
*Cập nhật lần cuối: 27/04/2026 - Antigravity AI (Sắp xếp theo Model)*
