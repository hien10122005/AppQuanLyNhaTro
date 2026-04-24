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

### Dashboard và thống kê

- Hiển thị tổng tiền thu theo tháng.
- Thống kê số phòng trống và số phòng đang thuê.
- Hiển thị số hóa đơn chưa thanh toán.
- Hỗ trợ chủ trọ theo dõi nhanh tình hình vận hành.

### Quản lý sự cố và bảo trì

- Ghi nhận các sự cố như hỏng điện, nước, nội thất.
- Theo dõi chi phí sửa chữa phát sinh.
- Lưu ngày ghi nhận, trạng thái xử lý và ghi chú.

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

## 5. TRẠNG THÁI CODE HIỆN TẠI

Hiện tại codebase đã có:

- `DatabaseHelper` với schema SQLite chính.
- Model cho các bảng quan trọng như phòng, khách thuê, hợp đồng, hóa đơn, thanh toán, bảo trì.
- Repository nền cho `phong`, `khach_thue`, `hop_dong`, `hoa_don`.
- Màn hình menu chính để điều hướng module.
- Bộ màn hình khung cho phòng, khách thuê, hợp đồng, chỉ số, hóa đơn, thu tiền, tổng quan và bảo trì.

Hiện tại codebase chưa có:

- CRUD hoàn chỉnh cho tất cả module.
- Adapter và binding dữ liệu thật cho danh sách.
- Luồng lập hóa đơn hoàn chỉnh từ chỉ số và bảng giá.
- Dashboard dùng dữ liệu thật.

---

## 6. ĐỊNH HƯỚNG PHÁT TRIỂN

1. Hoàn thiện CRUD quản lý phòng.
2. Hoàn thiện CRUD khách thuê.
3. Hoàn thiện hợp đồng và trạng thái phòng.
4. Hoàn thiện nhập chỉ số điện nước.
5. Hoàn thiện lập hóa đơn và thu tiền.
6. Nối dashboard với dữ liệu thật.
7. Bổ sung bảo trì, tìm kiếm và cảnh báo.

---

## 7. PHẠM VI CHỨC NĂNG

Để phù hợp với app offline viết bằng Java + XML, nên chia chức năng theo mức ưu tiên như sau:

### MVP - Chức năng chính

- Quản lý phòng.
- Quản lý khách thuê.
- Quản lý hợp đồng.
- Quản lý hóa đơn tiền phòng, điện, nước.
- Ghi nhận hóa đơn đã thanh toán hoặc chưa thanh toán.

### Nên bổ sung sớm

- Tìm kiếm và lọc dữ liệu.
- Dashboard và thống kê tổng quan.
- Hiển thị trạng thái phòng: trống, đang thuê, đang sửa.
- Cảnh báo hợp đồng sắp hết hạn.
- Quản lý sự cố, bảo trì và chi phí phát sinh.

### Có thể làm ở giai đoạn sau

- Xuất PDF hoặc in hóa đơn.
- Lưu hình ảnh giấy tờ như CCCD.
- Sao lưu và phục hồi dữ liệu.

---

## 8. GHI CHÚ

Tài liệu này được chuẩn hóa theo code hiện tại của repo. Nếu sau này dự án chuyển sang công nghệ khác như Room, Kotlin hoặc Jetpack Compose thì cần cập nhật lại tài liệu để tránh lệch với mã nguồn.
