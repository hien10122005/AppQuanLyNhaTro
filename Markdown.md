# DỰ ÁN: QUẢN LÝ NHÀ TRỌ

**Tên package:** `com.example.quanlynhatro`  
**Nền tảng:** Android Native  
**Công nghệ giao diện:** Java + XML  
**Cơ sở dữ liệu:** SQLite local  
**Mô hình hoạt động:** Offline

---

## 1. GIỚI THIỆU TỔNG QUAN

Đây là ứng dụng Android hỗ trợ quản lý nhà trọ theo hướng gọn, dễ dùng và không phụ thuộc internet. Toàn bộ dữ liệu được lưu cục bộ bằng SQLite để chủ trọ có thể thao tác ngay trên thiết bị kể cả khi không có kết nối mạng.

Mục tiêu chính của ứng dụng:

- Quản lý danh sách phòng trọ.
- Quản lý khách thuê và hợp đồng.
- Theo dõi tiền phòng, điện, nước và hóa đơn hàng tháng.
- Hỗ trợ vận hành theo mô hình app offline đơn giản, ổn định.

---

## 2. CÁC NHÓM DỮ LIỆU CHÍNH

### Phòng trọ

- `id`
- `so_phong`
- `loai_phong`
- `gia_phong`
- `dien_tich`
- `trang_thai`
- `ghi_chu`

### Khách thuê

- `id`
- `ho_ten`
- `so_dien_thoai`
- `cccd`
- `ngay_sinh`
- `dia_chi_thuong_tru`

### Hợp đồng

- `id`
- `phong_id`
- `khach_thue_id`
- `ngay_bat_dau`
- `ngay_ket_thuc`
- `tien_coc`
- `trang_thai`

### Hóa đơn

- `id`
- `hop_dong_id`
- `thang`
- `nam`
- `tien_phong`
- `so_dien_cu`
- `so_dien_moi`
- `so_nuoc_cu`
- `so_nuoc_moi`
- `tong_tien`
- `trang_thai_thanh_toan`

---

## 3. CHỨC NĂNG CỐT LÕI

### Quản lý phòng

- Thêm, sửa, xóa phòng.
- Cập nhật trạng thái phòng: trống, đang thuê, đang sửa.
- Xem danh sách và chi tiết từng phòng.

### Quản lý khách thuê

- Thêm và cập nhật thông tin khách thuê.
- Gắn khách thuê với phòng hoặc hợp đồng.
- Lưu thông tin cơ bản phục vụ quản lý lâu dài.

### Quản lý hợp đồng

- Tạo hợp đồng thuê mới.
- Theo dõi ngày bắt đầu, ngày kết thúc, tiền cọc.
- Kiểm tra hợp đồng còn hiệu lực hay đã hết hạn.

### Quản lý hóa đơn

- Nhập chỉ số điện, nước theo tháng.
- Tính tổng tiền phòng và chi phí phát sinh.
- Theo dõi hóa đơn đã thanh toán hoặc chưa thanh toán.

---

## 4. CÔNG NGHỆ SỬ DỤNG

- **Ngôn ngữ:** Java
- **Giao diện:** XML
- **IDE:** Android Studio
- **Database:** SQLite với `SQLiteOpenHelper`
- **Thư viện giao diện hiện có:** AppCompat, Material, ConstraintLayout

Phạm vi hiện tại:

- Không dùng Jetpack Compose.
- Không dùng Firebase.
- Không phụ thuộc cloud để lưu dữ liệu.

---

## 5. ĐỊNH HƯỚNG PHÁT TRIỂN

1. Hoàn thiện cấu trúc dữ liệu SQLite cho các bảng chính.
2. Xây dựng màn hình quản lý phòng.
3. Bổ sung màn hình khách thuê và hợp đồng.
4. Hoàn thiện chức năng tính hóa đơn điện nước.
5. Tối ưu giao diện và kiểm tra dữ liệu đầu vào.

---

## 6. PHẠM VI CHỨC NĂNG

Để phù hợp với app offline viết bằng Java + XML, nên chia chức năng theo mức ưu tiên như sau:

### MVP - Chức năng chính

- Quản lý phòng.
- Quản lý khách thuê.
- Quản lý hợp đồng.
- Quản lý hóa đơn tiền phòng, điện, nước.
- Ghi nhận hóa đơn đã thanh toán hoặc chưa thanh toán.

### Nên bổ sung sớm

- Tìm kiếm và lọc dữ liệu.
- Hiển thị trạng thái phòng: trống, đang thuê, đang sửa.
- Cảnh báo hợp đồng sắp hết hạn.
- Ghi nhận chi phí phát sinh hoặc dịch vụ khác.

### Có thể làm ở giai đoạn sau

- Thống kê và báo cáo.
- Xuất PDF hoặc in hóa đơn.
- Lưu hình ảnh giấy tờ như CCCD.
- Sao lưu và phục hồi dữ liệu.

---

## 7. GHI CHÚ

Tài liệu này được chuẩn hóa theo code hiện tại của repo. Nếu sau này dự án chuyển sang công nghệ khác như Room, Kotlin hoặc Jetpack Compose thì cần cập nhật lại tài liệu để tránh lệch với mã nguồn.
