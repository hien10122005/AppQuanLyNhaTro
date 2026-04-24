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
├── MainActivity.java                  # Màn hình menu điều hướng hiện tại
│
├── data/                              # Tầng dữ liệu
│   ├── database/
│   │   └── DatabaseHelper.java        # SQLiteOpenHelper, đã có schema chính
│   ├── model/
│   │   ├── Phong.java
│   │   ├── KhachThue.java
│   │   ├── HopDong.java
│   │   └── HoaDon.java
│   └── repository/
│       ├── PhongRepository.java
│       ├── KhachThueRepository.java
│       ├── HopDongRepository.java
│       └── HoaDonRepository.java
│
├── ui/                                # Tầng giao diện
│   ├── phong/
│   │   ├── DanhSachPhongActivity.java
│   │   ├── ChiTietPhongActivity.java
│   │   └── ThemSuaPhongActivity.java
│   ├── khach_thue/
│   │   ├── DanhSachKhachThueActivity.java
│   │   └── ThemSuaKhachThueActivity.java
│   ├── hop_dong/
│   │   ├── DanhSachHopDongActivity.java
│   │   └── ThemSuaHopDongActivity.java
│   ├── chi_so/
│   │   └── NhapChiSoActivity.java
│   ├── hoa_don/
│   │   ├── DanhSachHoaDonActivity.java
│   │   ├── LapHoaDonActivity.java
│   │   ├── ChiTietHoaDonActivity.java
│   │   └── ThuTienActivity.java
│   └── thong_ke/
│       └── TongQuanActivity.java
│
├── ui/
│   └── bao_tri/
│       ├── DanhSachBaoTriActivity.java
│       └── ThemSuaBaoTriActivity.java
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
├── activity_them_sua_phong.xml
├── activity_danh_sach_khach_thue.xml
├── activity_them_sua_khach_thue.xml
├── activity_danh_sach_hoa_don.xml
├── activity_danh_sach_hop_dong.xml
├── activity_them_sua_hop_dong.xml
├── activity_nhap_chi_so.xml
├── activity_lap_hoa_don.xml
├── activity_chi_tiet_hoa_don.xml
├── activity_thu_tien.xml
├── activity_tong_quan.xml
├── activity_danh_sach_bao_tri.xml
├── activity_them_sua_bao_tri.xml
├── item_phong.xml
├── item_khach_thue.xml
└── item_hoa_don.xml
```

Lưu ý: phần lớn các file khung ở trên hiện đã tồn tại trong repo. Tuy nhiên đa số mới ở mức scaffold giao diện, chưa hoàn thiện adapter, form validation và luồng nghiệp vụ.

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

Hiện tại repo đã có nền tảng tương đối rõ:

- Có `DatabaseHelper` với schema SQLite chính.
- Có model cho các bảng cốt lõi và một số thực thể mở rộng.
- Có repository nền cho `phong`, `khach_thue`, `hop_dong`, `hoa_don`.
- Có menu điều hướng ở `MainActivity`.
- Có nhiều màn hình Java/XML khung cho các module chính.

Những phần chưa hoàn thiện:

- Chưa gắn dữ liệu thật lên các danh sách.
- Chưa có adapter và form xử lý nghiệp vụ hoàn chỉnh.
- Chưa hoàn thiện CRUD toàn bộ module.
- Chưa có dashboard và bảo trì dùng dữ liệu thật.

Vì vậy khi phát triển tiếp, mọi code mới nên bám theo hướng `Java + XML + SQLite local`, tận dụng đúng các file khung đã có sẵn.

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

1. Hoàn thiện CRUD phòng từ các file khung đang có.
2. Hoàn thiện CRUD khách thuê.
3. Hoàn thiện hợp đồng và cập nhật trạng thái phòng.
4. Hoàn thiện nhập chỉ số điện nước.
5. Xây dựng lập hóa đơn và thu tiền từ CSDL đã có.
6. Bổ sung dashboard, bảo trì, tìm kiếm, lọc và cảnh báo.
7. Sau cùng mới làm xuất file, backup/restore và tối ưu UX.
