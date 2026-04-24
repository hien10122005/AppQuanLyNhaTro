# THIẾT KẾ CƠ SỞ DỮ LIỆU SQLITE

Tài liệu này là thiết kế CSDL chi tiết cho dự án **Quản Lý Nhà Trọ**, dùng làm cơ sở triển khai thực tế bằng `SQLiteOpenHelper` trong ứng dụng Android `Java + XML`.

Trạng thái hiện tại:

- Phần lớn schema trong tài liệu này đã được hiện thực hóa vào `DatabaseHelper`.
- Các bảng chính, khóa ngoại, unique constraint, index và seed `loai_dich_vu` đã được tạo trong code.
- Tài liệu này vẫn là nguồn chuẩn để tiếp tục viết repository, CRUD và luồng nghiệp vụ.

## 1. Mục tiêu thiết kế

Schema được thiết kế theo các nguyên tắc sau:

- Phù hợp với ứng dụng offline dùng SQLite.
- Dữ liệu hóa đơn phải giữ được lịch sử, không bị sai khi đơn giá thay đổi.
- Dễ mở rộng thêm dịch vụ như rác, wifi, gửi xe, phí khác.
- Hỗ trợ thống kê dashboard, công nợ, tỷ lệ lấp đầy, chi phí bảo trì.
- Không quá phức tạp để triển khai bằng Android Java.

## 2. Quy tắc thiết kế quan trọng

### 2.1. Không tính lại hóa đơn cũ theo đơn giá mới

Đây là nguyên tắc quan trọng nhất.

Vì giá điện, nước có thể thay đổi theo thời gian nên:

- Bảng giá phải có lịch sử hiệu lực.
- Khi lập hóa đơn, đơn giá áp dụng phải được lưu lại ngay trong chi tiết hóa đơn.
- Sau này dù bảng giá thay đổi, hóa đơn cũ vẫn giữ nguyên giá trị.

### 2.2. Hóa đơn nên có phần đầu và phần chi tiết

Không nên dồn hết mọi cột tiền vào một bảng `hoa_don`.

Thiết kế chuẩn hơn là:

- `hoa_don`: lưu thông tin chung của hóa đơn.
- `hoa_don_chi_tiet`: lưu từng dòng tiền như tiền phòng, điện, nước, wifi, phí khác.

Ưu điểm:

- Dễ mở rộng.
- Dễ in hóa đơn.
- Dễ kiểm tra cách tính tiền.
- Không phải sửa schema mỗi khi thêm loại phí mới.

### 2.3. Thanh toán nên tách riêng khỏi hóa đơn

Nếu chỉ có cột `trang_thai_thanh_toan` trong `hoa_don`, sau này sẽ khó xử lý:

- Thanh toán nhiều lần.
- Trả thiếu.
- Trả dư.
- Thanh toán bằng nhiều phương thức.

Vì vậy nên có bảng `thanh_toan`.

---

## 3. Danh sách bảng đề xuất

Schema khuyến nghị gồm các bảng sau:

1. `phong`
2. `khach_thue`
3. `hop_dong`
4. `hop_dong_thanh_vien`
5. `loai_dich_vu`
6. `bang_gia_dich_vu`
7. `chi_so_dich_vu_thang`
8. `hoa_don`
9. `hoa_don_chi_tiet`
10. `thanh_toan`
11. `su_co_bao_tri`

---

## 4. Thiết kế chi tiết từng bảng

## 4.1. Bảng `phong`

Lưu thông tin phòng trọ.

| Cột | Kiểu | Ràng buộc | Ý nghĩa |
|---|---|---|---|
| `id` | INTEGER | PK AUTOINCREMENT | Khóa chính |
| `so_phong` | TEXT | NOT NULL UNIQUE | Mã hoặc số phòng |
| `ten_phong` | TEXT | NULL | Tên hiển thị nếu cần |
| `loai_phong` | TEXT | NULL | Phòng thường, studio, gác lửng |
| `gia_phong_mac_dinh` | REAL | NOT NULL DEFAULT 0 | Giá phòng mặc định |
| `dien_tich` | REAL | NULL | Diện tích |
| `so_nguoi_toi_da` | INTEGER | NOT NULL DEFAULT 1 | Số người ở tối đa |
| `trang_thai` | TEXT | NOT NULL | `TRONG`, `DANG_THUE`, `BAO_TRI`, `NGUNG_SU_DUNG` |
| `mo_ta` | TEXT | NULL | Ghi chú thêm |
| `created_at` | TEXT | NOT NULL | Thời gian tạo |
| `updated_at` | TEXT | NOT NULL | Thời gian cập nhật |

Ghi chú:

- `so_phong` nên là duy nhất.
- `gia_phong_mac_dinh` là giá gợi ý, khi lập hợp đồng có thể chốt giá riêng.

## 4.2. Bảng `khach_thue`

Lưu thông tin người thuê chính hoặc người thuê đi kèm.

| Cột | Kiểu | Ràng buộc | Ý nghĩa |
|---|---|---|---|
| `id` | INTEGER | PK AUTOINCREMENT | Khóa chính |
| `ho_ten` | TEXT | NOT NULL | Họ tên |
| `so_dien_thoai` | TEXT | NOT NULL | Số điện thoại |
| `cccd` | TEXT | NULL UNIQUE | CCCD hoặc giấy tờ tùy thân |
| `ngay_sinh` | TEXT | NULL | Định dạng `YYYY-MM-DD` |
| `gioi_tinh` | TEXT | NULL | Nếu cần |
| `dia_chi_thuong_tru` | TEXT | NULL | Địa chỉ |
| `email` | TEXT | NULL | Email |
| `ghi_chu` | TEXT | NULL | Ghi chú |
| `created_at` | TEXT | NOT NULL | Thời gian tạo |
| `updated_at` | TEXT | NOT NULL | Thời gian cập nhật |

## 4.3. Bảng `hop_dong`

Lưu hợp đồng thuê của một phòng tại một thời điểm.

| Cột | Kiểu | Ràng buộc | Ý nghĩa |
|---|---|---|---|
| `id` | INTEGER | PK AUTOINCREMENT | Khóa chính |
| `ma_hop_dong` | TEXT | NOT NULL UNIQUE | Mã hợp đồng |
| `phong_id` | INTEGER | NOT NULL FK | Tham chiếu `phong.id` |
| `khach_thue_dai_dien_id` | INTEGER | NOT NULL FK | Người đại diện hợp đồng |
| `ngay_ky` | TEXT | NULL | Ngày ký |
| `ngay_bat_dau` | TEXT | NOT NULL | Ngày bắt đầu |
| `ngay_ket_thuc` | TEXT | NULL | Ngày kết thúc |
| `gia_thue_chot` | REAL | NOT NULL DEFAULT 0 | Giá thuê chốt theo hợp đồng |
| `tien_coc` | REAL | NOT NULL DEFAULT 0 | Tiền cọc |
| `chu_ky_thanh_toan` | INTEGER | NOT NULL DEFAULT 1 | Số tháng mỗi kỳ thanh toán |
| `trang_thai` | TEXT | NOT NULL | `HIEU_LUC`, `HET_HAN`, `DA_THANH_LY`, `HUY` |
| `ghi_chu` | TEXT | NULL | Ghi chú |
| `created_at` | TEXT | NOT NULL | Thời gian tạo |
| `updated_at` | TEXT | NOT NULL | Thời gian cập nhật |

Ghi chú:

- Một phòng có thể có nhiều hợp đồng theo thời gian.
- Tại một thời điểm chỉ nên có tối đa một hợp đồng đang `HIEU_LUC` cho một phòng.

## 4.4. Bảng `hop_dong_thanh_vien`

Lưu người ở cùng trong hợp đồng.

| Cột | Kiểu | Ràng buộc | Ý nghĩa |
|---|---|---|---|
| `id` | INTEGER | PK AUTOINCREMENT | Khóa chính |
| `hop_dong_id` | INTEGER | NOT NULL FK | Tham chiếu `hop_dong.id` |
| `khach_thue_id` | INTEGER | NOT NULL FK | Tham chiếu `khach_thue.id` |
| `vai_tro` | TEXT | NOT NULL DEFAULT 'THANH_VIEN' | `DAI_DIEN`, `THANH_VIEN` |
| `ngay_tham_gia` | TEXT | NULL | Ngày bắt đầu ở |
| `ngay_roi_di` | TEXT | NULL | Ngày rời đi |
| `ghi_chu` | TEXT | NULL | Ghi chú |

Ghi chú:

- Bảng này giúp quản lý nhiều người ở chung một phòng.
- Người đại diện vẫn nên lưu trực tiếp trong `hop_dong` để truy vấn nhanh.

## 4.5. Bảng `loai_dich_vu`

Danh mục loại dịch vụ hoặc loại phí.

| Cột | Kiểu | Ràng buộc | Ý nghĩa |
|---|---|---|---|
| `id` | INTEGER | PK AUTOINCREMENT | Khóa chính |
| `ma_loai` | TEXT | NOT NULL UNIQUE | Ví dụ: `TIEN_PHONG`, `DIEN`, `NUOC`, `RAC`, `WIFI`, `GUI_XE`, `PHAT_SINH` |
| `ten_loai` | TEXT | NOT NULL | Tên hiển thị |
| `kieu_tinh` | TEXT | NOT NULL | `CO_DINH`, `THEO_CHI_SO`, `SO_LUONG` |
| `don_vi` | TEXT | NULL | `kWh`, `m3`, `thang`, `nguoi`, `xe` |
| `hoat_dong` | INTEGER | NOT NULL DEFAULT 1 | 1 là hoạt động, 0 là ngừng |

Ghi chú:

- Đây là bảng gốc để mở rộng thêm các dịch vụ khác sau này.

## 4.6. Bảng `bang_gia_dich_vu`

Lưu đơn giá theo thời gian hiệu lực.

| Cột | Kiểu | Ràng buộc | Ý nghĩa |
|---|---|---|---|
| `id` | INTEGER | PK AUTOINCREMENT | Khóa chính |
| `loai_dich_vu_id` | INTEGER | NOT NULL FK | Tham chiếu `loai_dich_vu.id` |
| `phong_id` | INTEGER | NULL FK | Nếu NULL thì là giá dùng chung, nếu có thì là giá riêng cho phòng |
| `don_gia` | REAL | NOT NULL | Đơn giá áp dụng |
| `so_luong_mac_dinh` | REAL | NULL | Dùng cho phí cố định theo số lượng |
| `ngay_hieu_luc` | TEXT | NOT NULL | Ngày bắt đầu hiệu lực |
| `ngay_het_hieu_luc` | TEXT | NULL | Ngày kết thúc hiệu lực |
| `ghi_chu` | TEXT | NULL | Ghi chú |
| `created_at` | TEXT | NOT NULL | Thời gian tạo |

Ghi chú:

- Khi giá điện nước thay đổi, không sửa bản ghi cũ mà thêm bản ghi mới.
- Có thể ưu tiên giá theo `phong_id`, nếu không có thì dùng giá chung.

## 4.7. Bảng `chi_so_dich_vu_thang`

Lưu chỉ số điện, nước theo phòng và theo tháng.

| Cột | Kiểu | Ràng buộc | Ý nghĩa |
|---|---|---|---|
| `id` | INTEGER | PK AUTOINCREMENT | Khóa chính |
| `phong_id` | INTEGER | NOT NULL FK | Tham chiếu `phong.id` |
| `loai_dich_vu_id` | INTEGER | NOT NULL FK | Chỉ nên dùng với loại `DIEN`, `NUOC` |
| `thang` | INTEGER | NOT NULL | Từ 1 đến 12 |
| `nam` | INTEGER | NOT NULL | Năm |
| `chi_so_cu` | REAL | NOT NULL DEFAULT 0 | Chỉ số đầu kỳ |
| `chi_so_moi` | REAL | NOT NULL DEFAULT 0 | Chỉ số cuối kỳ |
| `so_luong_tieu_thu` | REAL | NOT NULL DEFAULT 0 | `chi_so_moi - chi_so_cu` |
| `ngay_chot` | TEXT | NULL | Ngày chốt |
| `ghi_chu` | TEXT | NULL | Ghi chú |
| `created_at` | TEXT | NOT NULL | Thời gian tạo |

Ràng buộc nghiệp vụ:

- Mỗi phòng, mỗi loại dịch vụ, mỗi tháng chỉ nên có một bản ghi.
- `chi_so_moi >= chi_so_cu`.

## 4.8. Bảng `hoa_don`

Lưu phần đầu của hóa đơn.

| Cột | Kiểu | Ràng buộc | Ý nghĩa |
|---|---|---|---|
| `id` | INTEGER | PK AUTOINCREMENT | Khóa chính |
| `ma_hoa_don` | TEXT | NOT NULL UNIQUE | Mã hóa đơn |
| `hop_dong_id` | INTEGER | NOT NULL FK | Tham chiếu `hop_dong.id` |
| `phong_id` | INTEGER | NOT NULL FK | Lưu trực tiếp để truy vấn nhanh |
| `thang` | INTEGER | NOT NULL | Kỳ hóa đơn |
| `nam` | INTEGER | NOT NULL | Kỳ hóa đơn |
| `ngay_lap` | TEXT | NOT NULL | Ngày lập |
| `han_thanh_toan` | TEXT | NULL | Hạn thanh toán |
| `tong_tien_truoc_giam` | REAL | NOT NULL DEFAULT 0 | Tổng trước giảm trừ |
| `giam_tru` | REAL | NOT NULL DEFAULT 0 | Số tiền giảm |
| `tong_tien` | REAL | NOT NULL DEFAULT 0 | Tổng cần thanh toán |
| `da_thanh_toan` | REAL | NOT NULL DEFAULT 0 | Tổng đã thu |
| `con_no` | REAL | NOT NULL DEFAULT 0 | Số còn nợ |
| `trang_thai` | TEXT | NOT NULL | `CHUA_THANH_TOAN`, `THANH_TOAN_MOT_PHAN`, `DA_THANH_TOAN`, `HUY` |
| `ghi_chu` | TEXT | NULL | Ghi chú |
| `created_at` | TEXT | NOT NULL | Thời gian tạo |
| `updated_at` | TEXT | NOT NULL | Thời gian cập nhật |

Ràng buộc nghiệp vụ:

- Mỗi hợp đồng chỉ nên có một hóa đơn cho mỗi tháng và năm.

## 4.9. Bảng `hoa_don_chi_tiet`

Lưu từng dòng tiền trong hóa đơn.

| Cột | Kiểu | Ràng buộc | Ý nghĩa |
|---|---|---|---|
| `id` | INTEGER | PK AUTOINCREMENT | Khóa chính |
| `hoa_don_id` | INTEGER | NOT NULL FK | Tham chiếu `hoa_don.id` |
| `loai_dich_vu_id` | INTEGER | NOT NULL FK | Tham chiếu `loai_dich_vu.id` |
| `ten_muc_phi` | TEXT | NOT NULL | Tên hiển thị trên hóa đơn |
| `so_luong` | REAL | NOT NULL DEFAULT 1 | Số lượng |
| `don_gia_ap_dung` | REAL | NOT NULL DEFAULT 0 | Đơn giá được chốt tại thời điểm lập hóa đơn |
| `thanh_tien` | REAL | NOT NULL DEFAULT 0 | `so_luong * don_gia_ap_dung` |
| `chi_so_cu` | REAL | NULL | Dùng cho điện/nước |
| `chi_so_moi` | REAL | NULL | Dùng cho điện/nước |
| `ghi_chu` | TEXT | NULL | Ghi chú |

Đây là bảng giải quyết trực tiếp vấn đề thay đổi giá điện nước:

- Đơn giá dùng để tính hóa đơn được lưu ngay tại đây.
- Hóa đơn cũ không bị ảnh hưởng nếu bảng giá đổi sau này.

## 4.10. Bảng `thanh_toan`

Lưu lịch sử thu tiền theo hóa đơn.

| Cột | Kiểu | Ràng buộc | Ý nghĩa |
|---|---|---|---|
| `id` | INTEGER | PK AUTOINCREMENT | Khóa chính |
| `hoa_don_id` | INTEGER | NOT NULL FK | Tham chiếu `hoa_don.id` |
| `ngay_thanh_toan` | TEXT | NOT NULL | Ngày thanh toán |
| `so_tien` | REAL | NOT NULL | Số tiền thu |
| `phuong_thuc` | TEXT | NOT NULL | `TIEN_MAT`, `CHUYEN_KHOAN`, `KHAC` |
| `ma_giao_dich` | TEXT | NULL | Nếu chuyển khoản |
| `ghi_chu` | TEXT | NULL | Ghi chú |
| `created_at` | TEXT | NOT NULL | Thời gian tạo |

Ưu điểm:

- Hỗ trợ trả góp nhiều lần.
- Hỗ trợ dashboard công nợ chính xác hơn.

## 4.11. Bảng `su_co_bao_tri`

Lưu sự cố và chi phí sửa chữa.

| Cột | Kiểu | Ràng buộc | Ý nghĩa |
|---|---|---|---|
| `id` | INTEGER | PK AUTOINCREMENT | Khóa chính |
| `phong_id` | INTEGER | NOT NULL FK | Tham chiếu `phong.id` |
| `hop_dong_id` | INTEGER | NULL FK | Có thể gắn với hợp đồng nếu cần |
| `tieu_de` | TEXT | NOT NULL | Tên ngắn của sự cố |
| `noi_dung` | TEXT | NOT NULL | Mô tả chi tiết |
| `ngay_bao` | TEXT | NOT NULL | Ngày ghi nhận |
| `muc_do_uu_tien` | TEXT | NOT NULL DEFAULT 'TRUNG_BINH' | `THAP`, `TRUNG_BINH`, `CAO`, `KHAN_CAP` |
| `trang_thai` | TEXT | NOT NULL | `MOI_TAO`, `DANG_XU_LY`, `DA_XU_LY`, `HUY` |
| `chi_phi` | REAL | NOT NULL DEFAULT 0 | Chi phí sửa chữa |
| `ngay_xu_ly` | TEXT | NULL | Ngày xử lý xong |
| `nguoi_xu_ly` | TEXT | NULL | Người thợ hoặc người xử lý |
| `ghi_chu` | TEXT | NULL | Ghi chú |
| `created_at` | TEXT | NOT NULL | Thời gian tạo |
| `updated_at` | TEXT | NOT NULL | Thời gian cập nhật |

---

## 5. Quan hệ giữa các bảng

Quan hệ chính:

- `phong` 1 - n `hop_dong`
- `khach_thue` 1 - n `hop_dong` qua `khach_thue_dai_dien_id`
- `hop_dong` 1 - n `hop_dong_thanh_vien`
- `loai_dich_vu` 1 - n `bang_gia_dich_vu`
- `phong` 1 - n `chi_so_dich_vu_thang`
- `hop_dong` 1 - n `hoa_don`
- `hoa_don` 1 - n `hoa_don_chi_tiet`
- `hoa_don` 1 - n `thanh_toan`
- `phong` 1 - n `su_co_bao_tri`

---

## 6. Chỉ mục và ràng buộc nên có

Nên tạo thêm index cho các cột thường truy vấn:

- `phong(so_phong)`
- `hop_dong(phong_id, trang_thai)`
- `hop_dong(ngay_ket_thuc)`
- `hoa_don(hop_dong_id, thang, nam)`
- `hoa_don(trang_thai)`
- `chi_so_dich_vu_thang(phong_id, loai_dich_vu_id, thang, nam)`
- `su_co_bao_tri(phong_id, trang_thai)`
- `thanh_toan(hoa_don_id, ngay_thanh_toan)`

Unique nên có:

- `phong.so_phong`
- `hop_dong.ma_hop_dong`
- `hoa_don.ma_hoa_don`
- `loai_dich_vu.ma_loai`
- `(phong_id, loai_dich_vu_id, thang, nam)` trong `chi_so_dich_vu_thang`
- `(hop_dong_id, thang, nam)` trong `hoa_don`

---

## 7. Bộ giá trị trạng thái khuyến nghị

Để code dễ thống nhất, nên cố định một số giá trị:

### `phong.trang_thai`

- `TRONG`
- `DANG_THUE`
- `BAO_TRI`
- `NGUNG_SU_DUNG`

### `hop_dong.trang_thai`

- `HIEU_LUC`
- `HET_HAN`
- `DA_THANH_LY`
- `HUY`

### `hoa_don.trang_thai`

- `CHUA_THANH_TOAN`
- `THANH_TOAN_MOT_PHAN`
- `DA_THANH_TOAN`
- `HUY`

### `su_co_bao_tri.trang_thai`

- `MOI_TAO`
- `DANG_XU_LY`
- `DA_XU_LY`
- `HUY`

---

## 8. Luồng tính tiền điện nước chuẩn

Đây là luồng nên dùng để tránh sai dữ liệu:

1. Người dùng chốt chỉ số điện hoặc nước vào `chi_so_dich_vu_thang`.
2. Hệ thống lấy đơn giá đang hiệu lực từ `bang_gia_dich_vu`.
3. Khi tạo hóa đơn, hệ thống sinh một bản ghi `hoa_don`.
4. Hệ thống sinh các dòng `hoa_don_chi_tiet` cho:
   - tiền phòng
   - tiền điện
   - tiền nước
   - các khoản khác nếu có
5. Mỗi dòng chi tiết lưu:
   - số lượng
   - đơn giá áp dụng
   - thành tiền
   - chỉ số cũ/mới nếu là điện hoặc nước
6. Tính tổng và ghi vào `hoa_don`.
7. Khi thu tiền, tạo bản ghi trong `thanh_toan`, sau đó cập nhật `da_thanh_toan`, `con_no`, `trang_thai` của `hoa_don`.

---

## 9. CREATE TABLE gợi ý cho SQLite

```sql
CREATE TABLE phong (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    so_phong TEXT NOT NULL UNIQUE,
    ten_phong TEXT,
    loai_phong TEXT,
    gia_phong_mac_dinh REAL NOT NULL DEFAULT 0,
    dien_tich REAL,
    so_nguoi_toi_da INTEGER NOT NULL DEFAULT 1,
    trang_thai TEXT NOT NULL,
    mo_ta TEXT,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);

CREATE TABLE khach_thue (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    ho_ten TEXT NOT NULL,
    so_dien_thoai TEXT NOT NULL,
    cccd TEXT UNIQUE,
    ngay_sinh TEXT,
    gioi_tinh TEXT,
    dia_chi_thuong_tru TEXT,
    email TEXT,
    ghi_chu TEXT,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);

CREATE TABLE hop_dong (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    ma_hop_dong TEXT NOT NULL UNIQUE,
    phong_id INTEGER NOT NULL,
    khach_thue_dai_dien_id INTEGER NOT NULL,
    ngay_ky TEXT,
    ngay_bat_dau TEXT NOT NULL,
    ngay_ket_thuc TEXT,
    gia_thue_chot REAL NOT NULL DEFAULT 0,
    tien_coc REAL NOT NULL DEFAULT 0,
    chu_ky_thanh_toan INTEGER NOT NULL DEFAULT 1,
    trang_thai TEXT NOT NULL,
    ghi_chu TEXT,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,
    FOREIGN KEY (phong_id) REFERENCES phong(id),
    FOREIGN KEY (khach_thue_dai_dien_id) REFERENCES khach_thue(id)
);

CREATE TABLE hop_dong_thanh_vien (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    hop_dong_id INTEGER NOT NULL,
    khach_thue_id INTEGER NOT NULL,
    vai_tro TEXT NOT NULL DEFAULT 'THANH_VIEN',
    ngay_tham_gia TEXT,
    ngay_roi_di TEXT,
    ghi_chu TEXT,
    FOREIGN KEY (hop_dong_id) REFERENCES hop_dong(id),
    FOREIGN KEY (khach_thue_id) REFERENCES khach_thue(id)
);

CREATE TABLE loai_dich_vu (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    ma_loai TEXT NOT NULL UNIQUE,
    ten_loai TEXT NOT NULL,
    kieu_tinh TEXT NOT NULL,
    don_vi TEXT,
    hoat_dong INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE bang_gia_dich_vu (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    loai_dich_vu_id INTEGER NOT NULL,
    phong_id INTEGER,
    don_gia REAL NOT NULL,
    so_luong_mac_dinh REAL,
    ngay_hieu_luc TEXT NOT NULL,
    ngay_het_hieu_luc TEXT,
    ghi_chu TEXT,
    created_at TEXT NOT NULL,
    FOREIGN KEY (loai_dich_vu_id) REFERENCES loai_dich_vu(id),
    FOREIGN KEY (phong_id) REFERENCES phong(id)
);

CREATE TABLE chi_so_dich_vu_thang (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    phong_id INTEGER NOT NULL,
    loai_dich_vu_id INTEGER NOT NULL,
    thang INTEGER NOT NULL,
    nam INTEGER NOT NULL,
    chi_so_cu REAL NOT NULL DEFAULT 0,
    chi_so_moi REAL NOT NULL DEFAULT 0,
    so_luong_tieu_thu REAL NOT NULL DEFAULT 0,
    ngay_chot TEXT,
    ghi_chu TEXT,
    created_at TEXT NOT NULL,
    FOREIGN KEY (phong_id) REFERENCES phong(id),
    FOREIGN KEY (loai_dich_vu_id) REFERENCES loai_dich_vu(id),
    UNIQUE (phong_id, loai_dich_vu_id, thang, nam)
);

CREATE TABLE hoa_don (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    ma_hoa_don TEXT NOT NULL UNIQUE,
    hop_dong_id INTEGER NOT NULL,
    phong_id INTEGER NOT NULL,
    thang INTEGER NOT NULL,
    nam INTEGER NOT NULL,
    ngay_lap TEXT NOT NULL,
    han_thanh_toan TEXT,
    tong_tien_truoc_giam REAL NOT NULL DEFAULT 0,
    giam_tru REAL NOT NULL DEFAULT 0,
    tong_tien REAL NOT NULL DEFAULT 0,
    da_thanh_toan REAL NOT NULL DEFAULT 0,
    con_no REAL NOT NULL DEFAULT 0,
    trang_thai TEXT NOT NULL,
    ghi_chu TEXT,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,
    FOREIGN KEY (hop_dong_id) REFERENCES hop_dong(id),
    FOREIGN KEY (phong_id) REFERENCES phong(id),
    UNIQUE (hop_dong_id, thang, nam)
);

CREATE TABLE hoa_don_chi_tiet (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    hoa_don_id INTEGER NOT NULL,
    loai_dich_vu_id INTEGER NOT NULL,
    ten_muc_phi TEXT NOT NULL,
    so_luong REAL NOT NULL DEFAULT 1,
    don_gia_ap_dung REAL NOT NULL DEFAULT 0,
    thanh_tien REAL NOT NULL DEFAULT 0,
    chi_so_cu REAL,
    chi_so_moi REAL,
    ghi_chu TEXT,
    FOREIGN KEY (hoa_don_id) REFERENCES hoa_don(id),
    FOREIGN KEY (loai_dich_vu_id) REFERENCES loai_dich_vu(id)
);

CREATE TABLE thanh_toan (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    hoa_don_id INTEGER NOT NULL,
    ngay_thanh_toan TEXT NOT NULL,
    so_tien REAL NOT NULL,
    phuong_thuc TEXT NOT NULL,
    ma_giao_dich TEXT,
    ghi_chu TEXT,
    created_at TEXT NOT NULL,
    FOREIGN KEY (hoa_don_id) REFERENCES hoa_don(id)
);

CREATE TABLE su_co_bao_tri (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    phong_id INTEGER NOT NULL,
    hop_dong_id INTEGER,
    tieu_de TEXT NOT NULL,
    noi_dung TEXT NOT NULL,
    ngay_bao TEXT NOT NULL,
    muc_do_uu_tien TEXT NOT NULL DEFAULT 'TRUNG_BINH',
    trang_thai TEXT NOT NULL,
    chi_phi REAL NOT NULL DEFAULT 0,
    ngay_xu_ly TEXT,
    nguoi_xu_ly TEXT,
    ghi_chu TEXT,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,
    FOREIGN KEY (phong_id) REFERENCES phong(id),
    FOREIGN KEY (hop_dong_id) REFERENCES hop_dong(id)
);
```

---

## 10. Các bản ghi seed nên tạo sẵn

Ngay khi khởi tạo database, nên seed bảng `loai_dich_vu`:

```text
TIEN_PHONG | Tiền phòng | CO_DINH | thang
DIEN       | Tiền điện  | THEO_CHI_SO | kWh
NUOC       | Tiền nước  | THEO_CHI_SO | m3
RAC        | Phí rác    | CO_DINH | thang
WIFI       | Wifi       | CO_DINH | thang
GUI_XE     | Gửi xe     | SO_LUONG | xe
PHAT_SINH  | Phí khác   | CO_DINH | lan
```

---

## 11. Kết luận triển khai

Nếu bạn muốn một schema đủ chắc để triển khai thật, thì bộ bảng cốt lõi nên là:

- `phong`
- `khach_thue`
- `hop_dong`
- `hop_dong_thanh_vien`
- `loai_dich_vu`
- `bang_gia_dich_vu`
- `chi_so_dich_vu_thang`
- `hoa_don`
- `hoa_don_chi_tiet`
- `thanh_toan`
- `su_co_bao_tri`

Đây là thiết kế cân bằng giữa:

- đủ chuẩn để dùng lâu dài
- đủ linh hoạt khi giá dịch vụ thay đổi
- đủ đơn giản để triển khai bằng SQLite trong Android Java

Tài liệu này nên được xem là nguồn chuẩn khi bạn bắt đầu viết:

- `DatabaseHelper`
- câu lệnh `CREATE TABLE`
- model Java
- repository
- luồng lập hóa đơn và thanh toán

# 12. Giải thích CSDL và Logic Vận Hành

Phần này mô tả chi tiết cách mỗi bảng trong CSDL hoạt động, mối quan hệ giữa các bảng, và luồng xử lý nghiệp vụ thực tế từ lúc khách vào thuê đến lúc thanh toán xong hóa đơn.

---

## 12.1. Tổng quan kiến trúc dữ liệu

Toàn bộ hệ thống xoay quanh 3 nhóm chức năng chính:

| Nhóm | Các bảng liên quan | Mục đích |
|---|---|---|
| **Quản lý phòng & người thuê** | `phong`, `khach_thue`, `hop_dong`, `hop_dong_thanh_vien` | Ai đang thuê phòng nào, từ khi nào |
| **Quản lý dịch vụ & chi phí** | `loai_dich_vu`, `bang_gia_dich_vu`, `chi_so_dich_vu_thang` | Tính tiền điện, nước, các loại phí |
| **Quản lý hóa đơn & thanh toán** | `hoa_don`, `hoa_don_chi_tiet`, `thanh_toan` | Lập và theo dõi việc thu tiền |

Bảng `su_co_bao_tri` đứng độc lập, phục vụ cho việc ghi nhận và xử lý sự cố hỏng hóc.

---

## 12.2. Luồng chính: Từ phòng trống → Có người thuê

### Bước 1: Tạo phòng (bảng `phong`)

Khi mới bắt đầu, chủ trọ nhập thông tin từng phòng:

```
phong {
  so_phong: "P101"           -- Mã phòng, duy nhất
  gia_phong_mac_dinh: 2500000 -- Giá gốc, dùng làm mặc định
  trang_thai: "TRONG"        -- Ban đầu phòng để trống
}
```

> **Quan trọng:** `gia_phong_mac_dinh` chỉ là giá tham khảo. Giá thực tế khi ký hợp đồng sẽ được chốt riêng trong bảng `hop_dong`.

### Bước 2: Tạo khách thuê (bảng `khach_thue`)

Trước khi ký hợp đồng, chủ trọ phải nhập thông tin khách:

```
khach_thue {
  ho_ten: "Nguyen Van A"
  so_dien_thoai: "0901234567"  -- NOT NULL, bắt buộc phải có
  cccd: "012345678901"          -- UNIQUE, không được trùng
  ngay_sinh, gioi_tinh, ...
}
```

> Một khách thuê (1 bản ghi `khach_thue`) có thể có **nhiều hợp đồng** qua các năm khác nhau — đây là thiết kế chuẩn, tránh nhập lại thông tin.

### Bước 3: Ký hợp đồng (bảng `hop_dong`)

Hợp đồng là bản ghi kết nối Phòng ↔ Khách thuê, đồng thời chốt các điều khoản:

```
hop_dong {
  phong_id: 1                     -- FK → phong(id)
  khach_thue_dai_dien_id: 5       -- FK → khach_thue(id)
  gia_thue_chot: 2700000          -- Giá thực tế 2 bên thỏa thuận
  tien_coc: 2700000               -- Tiền đặt cọc
  ngay_bat_dau: "2024-03-01"
  ngay_ket_thuc: "2025-03-01"     -- NULL = hợp đồng không xác định hạn
  trang_thai: "HIEU_LUC"
}
```

**Sau khi ký hợp đồng:** Ứng dụng phải cập nhật `phong.trang_thai = "DANG_THUE"` để phòng không còn hiển thị là trống nữa.

### Bước 4: Thêm người ở cùng (bảng `hop_dong_thanh_vien`)

Nếu phòng có nhiều người ở, mỗi người thêm vào là 1 dòng:

```
hop_dong_thanh_vien {
  hop_dong_id: 10
  khach_thue_id: 5    -- Người đại diện (cùng với trong hop_dong)
  vai_tro: "DAI_DIEN"
}
hop_dong_thanh_vien {
  hop_dong_id: 10
  khach_thue_id: 6    -- Người ở cùng
  vai_tro: "THANH_VIEN"
  ngay_tham_gia: "2024-05-01"  -- Có thể vào sau
}
```

> **Lưu ý:** Người đại diện thường cũng được thêm vào bảng `hop_dong_thanh_vien` với `vai_tro = "DAI_DIEN"` để có đủ lịch sử. Cột `ngay_roi_di` giúp ghi nhận khi ai đó rời đi trước khi hết hợp đồng.

---

## 12.3. Luồng dịch vụ: Từ cấu hình giá → Ghi chỉ số

### Bước 1: Định nghĩa loại dịch vụ (bảng `loai_dich_vu`)

Bảng này được seed sẵn khi tạo CSDL, chứa các danh mục phí chuẩn:

| ma_loai | ten_loai | kieu_tinh | don_vi | Ý nghĩa |
|---|---|---|---|---|
| `TIEN_PHONG` | Tiền phòng | `CO_DINH` | tháng | Thu đều mỗi tháng |
| `DIEN` | Tiền điện | `THEO_CHI_SO` | kWh | Tính theo số công tơ |
| `NUOC` | Tiền nước | `THEO_CHI_SO` | m3 | Tính theo đồng hồ nước |
| `RAC` | Phí rác | `CO_DINH` | tháng | Thu cố định |
| `WIFI` | Wifi | `CO_DINH` | tháng | Thu cố định |
| `GUI_XE` | Gửi xe | `SO_LUONG` | xe | Nhân đơn giá × số xe |
| `PHAT_SINH` | Phí khác | `CO_DINH` | lần | Phí phát sinh bất thường |

**Ba kiểu tính phí:**
- **`CO_DINH`**: Lấy `so_luong_mac_dinh` (thường = 1) × `don_gia`. Ví dụ: 1 tháng × 30.000đ = 30.000đ.
- **`THEO_CHI_SO`**: Lấy `(chi_so_moi - chi_so_cu)` × `don_gia`. Ví dụ: (150 - 100) kWh × 3.500đ = 175.000đ.
- **`SO_LUONG`**: Lấy số lượng thực tế nhập tay × `don_gia`. Ví dụ: 2 xe × 100.000đ = 200.000đ.

### Bước 2: Thiết lập bảng giá (bảng `bang_gia_dich_vu`)

Đây là nơi chủ trọ nhập đơn giá cụ thể:

```
-- Giá điện chung toàn nhà
bang_gia_dich_vu { loai_dich_vu_id: 2, phong_id: NULL, don_gia: 3500, ngay_hieu_luc: "2024-01-01" }

-- Phòng 101 thỏa thuận giá điện riêng cao hơn
bang_gia_dich_vu { loai_dich_vu_id: 2, phong_id: 1, don_gia: 3800, ngay_hieu_luc: "2024-03-01" }
```

**Quy tắc ưu tiên khi lấy đơn giá:**
1. Tìm bản ghi có `phong_id = <phòng cần tính>` và `ngay_hieu_luc` gần nhất với ngày lập hóa đơn.
2. Nếu không có giá riêng → dùng bản ghi có `phong_id IS NULL` (giá chung).
3. Cột `ngay_het_hieu_luc` cho phép thiết lập giá có thời hạn (ví dụ: giá ưu đãi 3 tháng).

### Bước 3: Ghi chỉ số hàng tháng (bảng `chi_so_dich_vu_thang`)

Cuối mỗi tháng, chủ trọ đi ghi số điện/nước từng phòng:

```
chi_so_dich_vu_thang {
  phong_id: 1
  loai_dich_vu_id: 2   -- Điện
  thang: 4, nam: 2024
  chi_so_cu: 1200       -- Số đầu tháng (lấy từ tháng trước)
  chi_so_moi: 1347      -- Số cuối tháng vừa đọc
  so_luong_tieu_thu: 147 -- Tự tính: 1347 - 1200
  ngay_chot: "2024-04-28"
}
```

> **Ràng buộc UNIQUE** `(phong_id, loai_dich_vu_id, thang, nam)` đảm bảo mỗi phòng chỉ có **một bản ghi chỉ số điện/nước cho mỗi tháng**, tránh nhập trùng.

---

## 12.4. Luồng hóa đơn & thanh toán

### Bước 1: Lập hóa đơn (bảng `hoa_don`)

Khi đến kỳ thu tiền (thường đầu tháng), hệ thống tạo một hóa đơn tổng:

```
hoa_don {
  ma_hoa_don: "HD-2024-04-P101"  -- Mã tự sinh
  hop_dong_id: 10
  phong_id: 1
  thang: 4, nam: 2024
  ngay_lap: "2024-05-01"
  han_thanh_toan: "2024-05-10"
  tong_tien_truoc_giam: 3200000
  giam_tru: 0
  tong_tien: 3200000
  da_thanh_toan: 0
  con_no: 3200000
  trang_thai: "CHUA_THANH_TOAN"
}
```

> **Ràng buộc UNIQUE** `(hop_dong_id, thang, nam)` đảm bảo mỗi hợp đồng **chỉ có một hóa đơn duy nhất** cho mỗi tháng.

### Bước 2: Tạo chi tiết hóa đơn (bảng `hoa_don_chi_tiet`)

Mỗi dòng là một khoản phí, được tạo tự động từ các dịch vụ đã cấu hình:

```
-- Dòng 1: Tiền phòng
hoa_don_chi_tiet {
  hoa_don_id: 100, loai_dich_vu_id: 1
  ten_muc_phi: "Tiền phòng tháng 4/2024"
  so_luong: 1, don_gia_ap_dung: 2700000
  thanh_tien: 2700000
}
-- Dòng 2: Tiền điện
hoa_don_chi_tiet {
  hoa_don_id: 100, loai_dich_vu_id: 2
  ten_muc_phi: "Tiền điện tháng 4/2024 (147 kWh)"
  so_luong: 147, don_gia_ap_dung: 3500
  thanh_tien: 514500
  chi_so_cu: 1200, chi_so_moi: 1347   -- Lưu lại để in hóa đơn
}
```

**Công thức tính `tong_tien` của hóa đơn:**
```
tong_tien_truoc_giam = SUM(thanh_tien) của tất cả hoa_don_chi_tiet
tong_tien = tong_tien_truoc_giam - giam_tru
con_no = tong_tien - da_thanh_toan
```

### Bước 3: Ghi nhận thanh toán (bảng `thanh_toan`)

Khi khách đưa tiền, tạo một bản ghi thanh toán:

```
thanh_toan {
  hoa_don_id: 100
  ngay_thanh_toan: "2024-05-07"
  so_tien: 3200000
  phuong_thuc: "TIEN_MAT"   -- hoặc "CHUYEN_KHOAN"
  ma_giao_dich: NULL         -- Chỉ dùng khi chuyển khoản
}
```

**Sau khi tạo bản ghi thanh toán, hệ thống cập nhật lại hóa đơn:**

```
da_thanh_toan += so_tien_vua_thu
con_no = tong_tien - da_thanh_toan

-- Cập nhật trạng thái:
IF con_no == 0         → trang_thai = "DA_THANH_TOAN"
IF con_no < tong_tien  → trang_thai = "THANH_TOAN_MOT_PHAN"
IF da_thanh_toan == 0  → trang_thai = "CHUA_THANH_TOAN"
```

> Cho phép khách trả **nhiều lần** (trả góp) — mỗi lần trả là 1 bản ghi `thanh_toan`, đến khi `con_no = 0` thì hóa đơn mới chuyển sang `DA_THANH_TOAN`.

---

## 12.5. Luồng kết thúc hợp đồng

Khi khách trả phòng, hệ thống thực hiện theo trình tự:

1. Lập hóa đơn tháng cuối (nếu chưa có).
2. Đảm bảo tất cả hóa đơn cũ đã ở trạng thái `DA_THANH_TOAN`.
3. Hoàn trả tiền cọc (có thể trừ chi phí sửa chữa nếu cần).
4. Cập nhật `hop_dong.trang_thai = "DA_THANH_LY"`.
5. Cập nhật `phong.trang_thai = "TRONG"` để phòng sẵn sàng cho thuê lại.

---

## 12.6. Luồng bảo trì và sự cố (bảng `su_co_bao_tri`)

Khi có hỏng hóc trong phòng:

```
su_co_bao_tri {
  phong_id: 1
  hop_dong_id: 10          -- Có thể NULL nếu hỏng khi phòng trống
  tieu_de: "Vòi nước bị rỉ"
  noi_dung: "Vòi nước trong nhà vệ sinh chảy không tắt được"
  ngay_bao: "2024-04-15"
  muc_do_uu_tien: "CAO"    -- THAP / TRUNG_BINH / CAO / KHAN_CAP
  trang_thai: "MOI_TAO"
  chi_phi: 0               -- Cập nhật sau khi sửa xong
}
```

**Vòng đời trạng thái:**
```
MOI_TAO → DANG_XU_LY → DA_XU_LY
                      ↘ HUY (nếu báo nhầm)
```

Khi xử lý xong, cập nhật:
```
trang_thai: "DA_XU_LY"
chi_phi: 150000
ngay_xu_ly: "2024-04-16"
nguoi_xu_ly: "Thợ sửa ống nước Bá"
```

---

## 12.7. Ràng buộc toàn vẹn dữ liệu quan trọng

| Ràng buộc | Áp dụng ở đâu | Tác dụng |
|---|---|---|
| `FOREIGN KEY` | Toàn bộ các bảng | Không thể xóa phòng đang có hợp đồng |
| `UNIQUE (so_phong)` | `phong` | Không được đặt trùng số phòng |
| `UNIQUE (cccd)` | `khach_thue` | Mỗi người chỉ có 1 hồ sơ |
| `UNIQUE (ma_hop_dong)` | `hop_dong` | Mã hợp đồng không được trùng |
| `UNIQUE (phong_id, loai_dich_vu_id, thang, nam)` | `chi_so_dich_vu_thang` | Mỗi tháng chỉ ghi chỉ số 1 lần |
| `UNIQUE (hop_dong_id, thang, nam)` | `hoa_don` | Mỗi tháng chỉ có 1 hóa đơn/hợp đồng |
| `setForeignKeyConstraintsEnabled(true)` | `DatabaseHelper` | Bật kiểm tra FK trong SQLite |

