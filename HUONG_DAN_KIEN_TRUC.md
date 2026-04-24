# TÀI LIỆU KIẾN TRÚC DỰ ÁN: QUẢN LÝ NHÀ TRỌ
**Package hiện tại:** `com.example.quanlynhatro`  
**Nền tảng:** Android Native  
**UI:** Java + XML  
**Cơ sở dữ liệu:** SQLite local, hoạt động offline

Tài liệu này mô tả kiến trúc nên dùng cho codebase hiện tại. Mục tiêu là giữ dự án dễ mở rộng nhưng vẫn bám sát trạng thái thật của repo: Android app viết bằng Java, giao diện XML, chưa dùng Jetpack Compose và không phụ thuộc cloud.

---

## 1. NGUYÊN TẮC TỔ CHỨC

- Tách rõ phần giao diện XML và phần xử lý Java.
- Chia package theo nghiệp vụ để dễ tìm file và dễ mở rộng.
- Ưu tiên chạy offline hoàn toàn với SQLite.
- Tên package và namespace phải thống nhất là `com.example.quanlynhatro`.
- Chỉ thêm thư mục hoặc lớp mới khi có nhu cầu thực sự, tránh tạo khung quá lớn khi app còn nhỏ.

---

## 2. CẤU TRÚC THƯ MỤC KHUYẾN NGHỊ

### Java (`app/src/main/java/com/example/quanlynhatro/`)

```text
com.example.quanlynhatro
│
├── MainActivity.java                  # Màn hình khởi chạy hiện tại
│
├── data/                              # Tầng dữ liệu
│   ├── database/
│   │   └── DatabaseHelper.java        # SQLiteOpenHelper, tạo và nâng cấp CSDL
│   ├── model/
│   │   ├── Phong.java
│   │   ├── KhachThue.java
│   │   ├── HopDong.java
│   │   └── HoaDon.java
│   └── repository/
│       ├── PhongRepository.java
│       ├── KhachThueRepository.java
│       └── HoaDonRepository.java
│
├── ui/                                # Tầng giao diện
│   ├── phong/
│   │   ├── DanhSachPhongActivity.java
│   │   └── ChiTietPhongActivity.java
│   ├── khach_thue/
│   │   └── DanhSachKhachThueActivity.java
│   ├── hoa_don/
│   │   └── DanhSachHoaDonActivity.java
│   └── thong_ke/
│       └── TongQuanActivity.java
│
└── utils/                             # Hàm dùng chung
    ├── DateUtils.java
    └── CurrencyUtils.java
```

### Resource XML (`app/src/main/res/layout/`)

```text
layout/
│
├── activity_main.xml
├── activity_danh_sach_phong.xml
├── activity_chi_tiet_phong.xml
├── activity_danh_sach_khach_thue.xml
├── activity_danh_sach_hoa_don.xml
├── activity_tong_quan.xml
├── item_phong.xml
├── item_khach_thue.xml
└── item_hoa_don.xml
```

Lưu ý: đây là cấu trúc khuyến nghị để phát triển tiếp, không có nghĩa là tất cả file trên đã tồn tại sẵn trong repo.

---

## 3. KIẾN TRÚC XỬ LÝ

Với quy mô hiện tại, app có thể đi theo hướng đơn giản như sau:

- `ui/`: hiển thị màn hình, nhận thao tác người dùng.
- `repository/`: xử lý truy vấn dữ liệu, làm việc với SQLite.
- `model/`: chứa các lớp dữ liệu.
- `database/`: quản lý tạo bảng, nâng phiên bản database, mở kết nối.
- `utils/`: định dạng tiền, ngày tháng, kiểm tra dữ liệu đầu vào.

Luồng xử lý cơ bản:

1. Người dùng thao tác trên `Activity`.
2. `Activity` gọi `Repository`.
3. `Repository` đọc/ghi dữ liệu qua `DatabaseHelper`.
4. Kết quả được trả về cho `Activity` để cập nhật giao diện XML.

---

## 4. ĐỊNH HƯỚNG SQLITE OFFLINE

Ứng dụng hoạt động offline nên dữ liệu trọng tâm sẽ được lưu trực tiếp trên máy bằng SQLite.

Các nhóm bảng chính nên có:

- `phong`: lưu thông tin phòng.
- `khach_thue`: lưu thông tin người thuê.
- `hop_dong`: lưu hợp đồng thuê.
- `hoa_don`: lưu hóa đơn điện, nước, tiền phòng.
- `chi_so_dien_nuoc`: lưu chỉ số theo tháng nếu cần tách riêng.

Nguyên tắc triển khai:

- Dùng `SQLiteOpenHelper` để quản lý tạo bảng và nâng cấp schema.
- Khóa chính nên là `INTEGER PRIMARY KEY AUTOINCREMENT`.
- Dùng khóa ngoại ở mức thiết kế dữ liệu để giữ liên kết rõ ràng.
- Không phụ thuộc internet để thao tác nghiệp vụ hàng ngày.

---

## 5. QUY ƯỚC ĐẶT TÊN

- Tên class Java: `PascalCase`, ví dụ `DanhSachPhongActivity`.
- Tên file XML: `snake_case`, ví dụ `activity_danh_sach_phong.xml`.
- Tên bảng SQLite: chữ thường, dùng `_`, ví dụ `khach_thue`.
- Tên package: chữ thường, ví dụ `ui.hoa_don`, `data.repository`.

---

## 6. TRẠNG THÁI HIỆN TẠI CỦA REPO

Hiện tại repo đang ở mức khởi tạo cơ bản:

- Có `MainActivity.java`.
- Có `activity_main.xml`.
- Chưa có module nghiệp vụ hoàn chỉnh.
- Chưa tích hợp Room, Firebase hoặc Jetpack Compose.

Vì vậy khi phát triển tiếp, mọi tài liệu và code mới nên bám theo hướng `Java + XML + SQLite local`.

---

## 7. PHẠM VI CHỨC NĂNG NÊN TRIỂN KHAI

Để phù hợp với app offline và quy mô hiện tại, nên chia chức năng thành 3 mức ưu tiên như sau:

### MVP - Bắt buộc làm trước

- Quản lý phòng trọ.
- Quản lý khách thuê.
- Quản lý hợp đồng.
- Quản lý hóa đơn tiền phòng, điện, nước.
- Ghi nhận trạng thái thanh toán hóa đơn.

Đây là nhóm chức năng cốt lõi, đủ để app vận hành được nghiệp vụ chính của chủ trọ.

### Nên có sớm sau MVP

- Tìm kiếm và lọc danh sách phòng, khách thuê, hóa đơn.
- Dashboard và thống kê tổng quan.
- Cảnh báo hợp đồng sắp hết hạn.
- Hiển thị phòng trống, đang thuê, đang sửa.
- Quản lý sự cố, bảo trì và chi phí phát sinh.

Nhóm này giúp app dùng thực tế thuận tiện hơn nhưng không bắt buộc phải hoàn thành ở phiên bản đầu tiên.

### Có thể để giai đoạn sau

- Thống kê, báo cáo doanh thu.
- Xuất hóa đơn ra PDF hoặc hình ảnh.
- Lưu ảnh CCCD hoặc hồ sơ đính kèm.
- Sao lưu và phục hồi dữ liệu SQLite.

Nhóm này mang tính nâng cao, nên triển khai sau khi luồng nghiệp vụ chính đã ổn định.

Gợi ý triển khai:

- `Dashboard`: hiển thị tổng tiền thu trong tháng, số hóa đơn chưa thanh toán, số phòng đang thuê, tỷ lệ lấp đầy.
- `Bảo trì`: ghi nhận sự cố như điện, nước, nội thất, kèm chi phí sửa chữa và ngày xử lý.

---

## 8. THỨ TỰ ƯU TIÊN KHI CODE

Thứ tự nên làm để tránh rối dữ liệu:

1. Thiết kế bảng SQLite.
2. Hoàn thiện màn hình và nghiệp vụ quản lý phòng.
3. Hoàn thiện khách thuê và hợp đồng.
4. Xây dựng tính tiền và quản lý hóa đơn.
5. Bổ sung tìm kiếm, lọc và cảnh báo.
6. Sau cùng mới làm thống kê, xuất file, sao lưu.
