# QUẢN LÝ NHÀ TRỌ

Ứng dụng Android hỗ trợ quản lý nhà trọ theo hướng đơn giản, dễ dùng và hoạt động hoàn toàn offline. Dự án được xây dựng để phục vụ các nghiệp vụ thực tế như quản lý phòng, khách thuê, hợp đồng, hóa đơn điện nước, theo dõi thanh toán, thống kê vận hành và ghi nhận sự cố bảo trì.

## Thông tin chung

- **Tên dự án:** Quản Lý Nhà Trọ
- **Package:** `com.example.quanlynhatro`
- **Nền tảng:** Android Native
- **Ngôn ngữ lập trình:** Java
- **Giao diện:** XML
- **Cơ sở dữ liệu:** SQLite local
- **Mô hình hoạt động:** Offline
- **Tác giả:** Hiển và Bảo

## Mục tiêu dự án

Dự án được xây dựng nhằm hỗ trợ chủ trọ quản lý thông tin nhà trọ ngay trên thiết bị Android mà không cần phụ thuộc vào internet. Ứng dụng tập trung vào tính ổn định, thao tác nhanh, dữ liệu rõ ràng và dễ mở rộng theo từng giai đoạn.

Những mục tiêu chính:

- Quản lý danh sách phòng trọ và trạng thái sử dụng.
- Quản lý khách thuê và thông tin hợp đồng.
- Quản lý hóa đơn tiền phòng, điện, nước theo tháng.
- Theo dõi tình trạng thanh toán để biết ai còn nợ.
- Hỗ trợ thống kê tổng quan giúp chủ trọ nắm tình hình vận hành nhanh.
- Ghi nhận sự cố và chi phí bảo trì để theo dõi lợi nhuận thực tế.

## Bài toán thực tế mà ứng dụng giải quyết

Trong quá trình quản lý nhà trọ, chủ trọ thường gặp các vấn đề:

- Dễ quên phòng nào đang trống, phòng nào đã có người thuê.
- Khó theo dõi hợp đồng nào sắp hết hạn.
- Khó tổng hợp tiền phòng, điện, nước theo từng tháng.
- Mất thời gian kiểm tra ai đã thanh toán và ai còn nợ.
- Không lưu được chi phí sửa chữa phát sinh nên khó tính lợi nhuận ròng.

Ứng dụng này được định hướng để giải quyết trực tiếp các vấn đề trên bằng một hệ thống quản lý gọn, rõ và phù hợp với mô hình offline.

## Công nghệ sử dụng

- **Java:** dùng để xử lý logic nghiệp vụ.
- **XML:** dùng để xây dựng giao diện Android truyền thống.
- **SQLite:** lưu trữ dữ liệu cục bộ trên thiết bị.
- **SQLiteOpenHelper:** quản lý tạo bảng, mở kết nối và nâng cấp cơ sở dữ liệu.
- **AppCompat:** hỗ trợ tương thích giao diện Android.
- **Material Components:** cung cấp các thành phần giao diện cơ bản.
- **ConstraintLayout:** hỗ trợ xây dựng layout linh hoạt.

## Phạm vi kỹ thuật hiện tại

Repository hiện tại đang được định hướng theo các nguyên tắc sau:

- Không dùng Jetpack Compose.
- Không dùng Firebase.
- Không phụ thuộc cloud để lưu trữ dữ liệu.
- Ưu tiên kiến trúc đơn giản, phù hợp với app Android Java quy mô nhỏ đến vừa.
- Tập trung hoàn thiện nghiệp vụ cốt lõi trước khi mở rộng thêm các tính năng nâng cao.

## Chức năng chính của hệ thống

### 1. Quản lý phòng

- Thêm phòng mới.
- Cập nhật thông tin phòng.
- Xóa phòng khi cần.
- Theo dõi trạng thái phòng: trống, đang thuê, đang sửa.
- Xem danh sách và chi tiết từng phòng.

### 2. Quản lý khách thuê

- Thêm khách thuê mới.
- Cập nhật thông tin khách thuê.
- Lưu các thông tin cơ bản như họ tên, số điện thoại, CCCD, địa chỉ.
- Liên kết khách thuê với phòng hoặc hợp đồng tương ứng.

### 3. Quản lý hợp đồng

- Tạo hợp đồng thuê.
- Theo dõi ngày bắt đầu và ngày kết thúc.
- Lưu tiền cọc.
- Quản lý trạng thái hợp đồng.
- Kiểm tra hợp đồng còn hiệu lực hoặc sắp hết hạn.

### 4. Quản lý hóa đơn

- Nhập chỉ số điện và nước theo tháng.
- Tính tiền phòng, tiền điện, tiền nước.
- Ghi nhận tổng tiền cần thanh toán.
- Theo dõi trạng thái đã thanh toán hoặc chưa thanh toán.

### 5. Dashboard và thống kê

- Hiển thị tổng tiền thu theo tháng.
- Hiển thị số hóa đơn chưa thanh toán.
- Thống kê số phòng đang thuê và số phòng trống.
- Tính tỷ lệ lấp đầy.
- Giúp chủ trọ nắm tình hình hoạt động ngay khi mở ứng dụng.

### 6. Quản lý sự cố và bảo trì

- Ghi nhận các sự cố như bóng đèn hỏng, rò nước, hỏng khóa, hỏng thiết bị.
- Theo dõi chi phí sửa chữa.
- Lưu ngày phát sinh, trạng thái xử lý và ghi chú.
- Hỗ trợ kiểm soát chi phí vận hành để tính lợi nhuận thực tế chính xác hơn.

## Phân chia mức độ ưu tiên tính năng

### MVP - Bắt buộc hoàn thành trước

- Quản lý phòng.
- Quản lý khách thuê.
- Quản lý hợp đồng.
- Quản lý hóa đơn tiền phòng, điện, nước.
- Ghi nhận trạng thái thanh toán hóa đơn.

### Nên bổ sung sớm sau MVP

- Tìm kiếm và lọc dữ liệu.
- Dashboard và thống kê tổng quan.
- Cảnh báo hợp đồng sắp hết hạn.
- Hiển thị nhanh trạng thái phòng.
- Quản lý sự cố, bảo trì và chi phí phát sinh.

### Có thể phát triển ở giai đoạn sau

- Xuất hóa đơn PDF.
- In hóa đơn.
- Đính kèm hình ảnh giấy tờ như CCCD.
- Sao lưu và phục hồi dữ liệu.

## Mô hình dữ liệu dự kiến

Hệ thống dữ liệu của ứng dụng tập trung vào các bảng chính sau:

Lưu ý: phần mô tả dưới đây là bản tóm tắt. Thiết kế chi tiết dùng để triển khai thực tế được ghi tại [THIET_KE_CSDL.md](./THIET_KE_CSDL.md).

### Bảng `phong`

- `id`
- `so_phong`
- `loai_phong`
- `gia_phong`
- `dien_tich`
- `trang_thai`
- `ghi_chu`

### Bảng `khach_thue`

- `id`
- `ho_ten`
- `so_dien_thoai`
- `cccd`
- `ngay_sinh`
- `dia_chi_thuong_tru`

### Bảng `hop_dong`

- `id`
- `phong_id`
- `khach_thue_id`
- `ngay_bat_dau`
- `ngay_ket_thuc`
- `tien_coc`
- `trang_thai`

### Bảng `hoa_don`

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

### Bảng `bao_tri_su_co` hoặc tương đương

- `id`
- `phong_id`
- `noi_dung_su_co`
- `chi_phi`
- `ngay_bao`
- `trang_thai_xu_ly`
- `ghi_chu`

## Kiến trúc dự án

Dự án được định hướng theo cách tổ chức đơn giản, dễ mở rộng:

- `ui/`: chứa các màn hình và xử lý giao diện.
- `data/model/`: chứa các lớp mô hình dữ liệu.
- `data/database/`: chứa lớp quản lý SQLite.
- `data/repository/`: chứa phần thao tác dữ liệu.
- `utils/`: chứa các hàm dùng chung như định dạng ngày tháng, tiền tệ.

Luồng xử lý cơ bản:

1. Người dùng thao tác trên màn hình `Activity`.
2. `Activity` gọi `Repository`.
3. `Repository` làm việc với `SQLiteOpenHelper`.
4. Dữ liệu được đọc hoặc ghi vào SQLite.
5. Kết quả trả ngược về giao diện để hiển thị.

## Cấu trúc thư mục khuyến nghị

```text
app/src/main/java/com/example/quanlynhatro/
│
├── MainActivity.java
├── data/
│   ├── database/
│   │   └── DatabaseHelper.java
│   ├── model/
│   │   ├── Phong.java
│   │   ├── KhachThue.java
│   │   ├── HopDong.java
│   │   ├── HoaDon.java
│   │   └── BaoTri.java
│   └── repository/
│       ├── PhongRepository.java
│       ├── KhachThueRepository.java
│       ├── HopDongRepository.java
│       ├── HoaDonRepository.java
│       └── BaoTriRepository.java
├── ui/
│   ├── phong/
│   ├── khach_thue/
│   ├── hop_dong/
│   ├── hoa_don/
│   ├── thong_ke/
│   └── bao_tri/
└── utils/
```

```text
app/src/main/res/layout/
│
├── activity_main.xml
├── activity_danh_sach_phong.xml
├── activity_chi_tiet_phong.xml
├── activity_danh_sach_khach_thue.xml
├── activity_danh_sach_hop_dong.xml
├── activity_danh_sach_hoa_don.xml
├── activity_tong_quan.xml
├── activity_bao_tri.xml
├── item_phong.xml
├── item_khach_thue.xml
├── item_hop_dong.xml
├── item_hoa_don.xml
└── item_bao_tri.xml
```

Lưu ý: đây là cấu trúc định hướng để phát triển tiếp. Ở thời điểm hiện tại, repo vẫn đang ở giai đoạn khởi tạo cơ bản và chưa có đầy đủ các module trên.

## Trạng thái hiện tại của repository

Hiện tại dự án mới ở giai đoạn nền tảng:

- Đã có `MainActivity.java`.
- Đã có `activity_main.xml`.
- Chưa triển khai đầy đủ các màn hình nghiệp vụ.
- Chưa hoàn thiện cơ sở dữ liệu SQLite.
- Chưa có đầy đủ repository, model và chức năng nghiệp vụ.

Điều đó có nghĩa đây là thời điểm phù hợp để chuẩn hóa kiến trúc và phát triển đúng hướng ngay từ đầu.

## Hướng phát triển đề xuất

Thứ tự triển khai nên đi như sau:

1. Thiết kế cơ sở dữ liệu SQLite.
2. Xây dựng model dữ liệu.
3. Tạo `DatabaseHelper`.
4. Hoàn thiện chức năng quản lý phòng.
5. Hoàn thiện chức năng khách thuê và hợp đồng.
6. Xây dựng chức năng hóa đơn.
7. Bổ sung dashboard, tìm kiếm, lọc và cảnh báo.
8. Bổ sung quản lý sự cố, bảo trì.
9. Mở rộng sang sao lưu dữ liệu, xuất PDF và các tính năng nâng cao.

## Cách chạy dự án

### Yêu cầu môi trường

- Android Studio
- Android SDK phù hợp với project
- JDK 11
- Thiết bị Android hoặc máy ảo để chạy thử

### Các bước mở project

1. Mở Android Studio.
2. Chọn `Open` và mở thư mục dự án.
3. Chờ Gradle sync hoàn tất.
4. Kết nối thiết bị Android hoặc mở máy ảo.
5. Nhấn `Run` để chạy ứng dụng.

## Định hướng người dùng mục tiêu

Ứng dụng hướng đến các nhóm người dùng như:

- Chủ nhà trọ cá nhân.
- Người quản lý dãy trọ quy mô nhỏ và vừa.
- Người cần một ứng dụng quản lý offline, không phụ thuộc internet.
- Sinh viên hoặc nhóm phát triển muốn xây dựng app quản lý nhà trọ trên Android Java.

## Ưu điểm định hướng của dự án

- Dễ tiếp cận với người dùng phổ thông.
- Hoạt động offline nên ổn định và không phụ thuộc mạng.
- Phù hợp để phát triển theo từng giai đoạn.
- Kiến trúc đủ đơn giản để học tập và đủ rõ ràng để mở rộng.
- Gắn sát nhu cầu thực tế của bài toán quản lý nhà trọ.

## Hạn chế hiện tại

- Dự án vẫn đang ở giai đoạn đầu.
- Chưa có đầy đủ màn hình nghiệp vụ.
- Chưa hoàn thiện phần cơ sở dữ liệu.
- Chưa có chức năng sao lưu dữ liệu.
- Chưa có báo cáo và xuất file hoàn chỉnh.

## Tài liệu liên quan

- [HUONG_DAN_KIEN_TRUC.md](./HUONG_DAN_KIEN_TRUC.md)
- [Markdown.md](./Markdown.md)
- [THIET_KE_CSDL.md](./THIET_KE_CSDL.md)

## Tác giả

- **Hiển**
- **Bảo**

## Ghi chú

README này được viết bám theo định hướng hiện tại của codebase: Android Java, giao diện XML, dữ liệu lưu cục bộ bằng SQLite và phát triển theo mô hình offline. Nếu sau này dự án thay đổi công nghệ hoặc mở rộng kiến trúc, README nên được cập nhật tương ứng để luôn phản ánh đúng trạng thái thực tế của sản phẩm.
