# Buổi 3 - Model, Repository, DatabaseHelper và cách nối với WinForms

## Mục tiêu buổi học

Sau buổi này, bạn cần hiểu được:

- `Model` là gì
- `Repository` là gì
- `DatabaseHelper` là gì
- cách 3 phần này phối hợp với nhau
- vì sao cấu trúc này rất gần với `DTO - DAL - BUS` trong WinForms

---

## 1. Vì sao buổi này rất quan trọng

Nếu 2 buổi trước giúp bạn hiểu:

- màn hình Android là gì
- giao diện XML là gì
- vòng đời `Activity` là gì

thì buổi này sẽ giúp bạn hiểu:

- dữ liệu trong app được tổ chức như thế nào
- dữ liệu đi từ database ra màn hình theo đường nào

Đây là phần giúp bạn bắt đầu đọc được project thật.

---

## 2. Nhắc lại tư duy WinForms bạn đã biết

Trong project WinForms kiểu quản lý, bạn thường có:

- `DTO`
  - chứa dữ liệu
- `DAL`
  - truy vấn database
- `BUS`
  - xử lý nghiệp vụ
- `Form`
  - giao diện và gọi các lớp xử lý

Trong project Android này, bạn có thể tạm map như sau:

- `Model` gần giống `DTO`
- `Repository` gần giống `DAL`
- một phần xử lý giao diện trong `Activity`
- `DatabaseHelper` là lớp quản lý SQLite

Lưu ý:

- project Android này không tách `BUS` thật rõ như WinForms
- một phần xử lý nghiệp vụ đơn giản có thể nằm trong `Repository` hoặc `Activity`

Nhưng với người mới, cách nhớ trên là rất ổn.

---

## 3. `Model` là gì

`Model` là lớp dùng để chứa dữ liệu của một đối tượng.

Ví dụ trong project của bạn có:

- `Phong.java`
- `KhachThue.java`
- `HoaDon.java`
- `HopDong.java`

Mỗi class như vậy đại diện cho một loại dữ liệu trong hệ thống.

### Ví dụ với `Phong`

Một phòng có thể có:

- `id`
- `soPhong`
- `tenPhong`
- `loaiPhong`
- `giaPhong`
- `dienTich`
- `trangThai`

Những dữ liệu đó được gom vào class `Phong`.

### So với WinForms

- `Phong.java` gần giống `PhongDTO.cs`

---

## 4. Vì sao cần `Model`

Giả sử database có một dòng:

- `id = 1`
- `so_phong = P101`
- `ten_phong = Phòng 101`
- `gia_phong_mac_dinh = 3500000`

Nếu không có `Model`, bạn sẽ phải xử lý dữ liệu rất rời rạc.

Nếu có `Model`, ta chỉ cần hiểu:

- đây là một object `Phong`

và object đó có:

- `phong.getSoPhong()`
- `phong.getGiaPhong()`
- `phong.getTrangThai()`

Nhờ vậy code dễ đọc hơn rất nhiều.

---

## 5. Ví dụ `Model` đơn giản

```java
public class Phong {
    private int id;
    private String soPhong;
    private String tenPhong;
    private double giaPhong;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSoPhong() { return soPhong; }
    public void setSoPhong(String soPhong) { this.soPhong = soPhong; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public double getGiaPhong() { return giaPhong; }
    public void setGiaPhong(double giaPhong) { this.giaPhong = giaPhong; }
}
```

### Ý nghĩa

Class này:

- không phải giao diện
- không phải câu lệnh SQL
- chỉ là nơi chứa dữ liệu

---

## 6. `Repository` là gì

`Repository` là lớp chuyên làm việc với dữ liệu.

Ví dụ trong project:

- `PhongRepository.java`
- `KhachThueRepository.java`
- `HoaDonRepository.java`

Những lớp này thường có nhiệm vụ:

- thêm dữ liệu
- sửa dữ liệu
- xóa dữ liệu
- lấy danh sách dữ liệu
- lấy chi tiết theo id

### Ví dụ các hàm thường gặp

- `addPhong(...)`
- `updatePhong(...)`
- `deletePhong(...)`
- `getPhongById(...)`
- `getAllPhong()`

### So với WinForms

`PhongRepository` gần giống:

- `PhongDAL`

hoặc đôi khi giống:

- `DAL` cộng một ít xử lý đơn giản

---

## 7. `Repository` khác `Model` như nào

Đây là chỗ phải phân biệt thật rõ:

### `Model`

- là dữ liệu
- là object
- là “một bản ghi đã được gom lại”

Ví dụ:

- `Phong`
- `KhachThue`

### `Repository`

- là nơi thao tác với dữ liệu
- là nơi đọc, ghi, sửa, xóa
- là cầu nối giữa app và database

Ví dụ:

- `PhongRepository`
- `KhachThueRepository`

### Cách nhớ

- `Model` = cái được lưu
- `Repository` = nơi xử lý cái được lưu

---

## 8. `DatabaseHelper` là gì

Trong Android dùng SQLite, thường có một lớp quản lý database.

Trong project này lớp đó là:

- `DatabaseHelper.java`

Lớp này thường giữ:

- tên database
- version database
- tên bảng
- tên cột
- câu lệnh tạo bảng
- cách mở database đọc/ghi

### Ví dụ trong `DatabaseHelper`

- `DATABASE_NAME`
- `DATABASE_VERSION`
- `TABLE_PHONG`
- `COL_PHONG_SO_PHONG`
- `COL_PHONG_TRANG_THAI`

### So với WinForms

Nó không giống hẳn một lớp `DAL`.
Nó gần giống:

- lớp quản lý cấu trúc CSDL
- nơi giữ tên bảng, tên cột
- nơi tạo database SQLite

---

## 9. Vì sao `DatabaseHelper` cần nhiều hằng số

Ví dụ:

```java
public static final String TABLE_PHONG = "phong";
public static final String COL_PHONG_SO_PHONG = "so_phong";
```

Tại sao không viết thẳng `"phong"` hay `"so_phong"` ở khắp nơi?

Vì làm vậy sẽ:

- dễ gõ sai
- khó sửa khi đổi tên cột
- code rối hơn

Khi dùng hằng số:

- an toàn hơn
- dễ bảo trì hơn

---

## 10. Luồng dữ liệu hoàn chỉnh

Đây là phần quan trọng nhất của buổi này.

Giả sử màn hình danh sách phòng cần dữ liệu.

Luồng sẽ là:

1. `Activity` gọi `PhongRepository`
2. `PhongRepository` mở database qua `DatabaseHelper`
3. chạy query lấy dữ liệu
4. nhận kết quả từ database
5. chuyển dữ liệu thô thành object `Phong`
6. trả `List<Phong>` về cho `Activity`
7. `Activity` đổ danh sách này lên giao diện

### Sơ đồ

```text
Activity
   |
   v
PhongRepository
   |
   v
DatabaseHelper
   |
   v
SQLite
   |
   v
Dữ liệu thô
   |
   v
Model Phong / List<Phong>
   |
   v
Activity hiển thị lên màn hình
```

---

## 11. `getAllPhong()` thường làm gì

Một hàm như:

```java
public List<Phong> getAllPhong()
```

thường sẽ:

- mở database ở chế độ đọc
- query bảng `phong`
- duyệt từng dòng kết quả
- đổi từng dòng thành object `Phong`
- gom lại thành `List<Phong>`
- trả danh sách đó về

### So với WinForms

Nó rất giống việc:

- chạy `SELECT * FROM phong`
- đọc từng dòng từ `DataReader` hoặc `DataTable`
- tạo `PhongDTO`
- cho vào `List<PhongDTO>`

---

## 12. `mapPhong(...)` là gì

Đây là chỗ người mới rất hay thắc mắc.

Khi query database, dữ liệu trả về chưa phải object `Phong`.
Nó vẫn là dữ liệu thô theo từng cột.

Hàm `mapPhong(...)` dùng để:

- lấy một dòng dữ liệu
- đọc từng cột
- gán vào object `Phong`
- trả object đó ra

### Ví dụ ý tưởng

```java
private Phong mapPhong(Cursor cursor) {
    Phong phong = new Phong();
    phong.setId(...);
    phong.setSoPhong(...);
    phong.setTenPhong(...);
    phong.setGiaPhong(...);
    return phong;
}
```

### Ý nghĩa dễ hiểu

`mapPhong(...)` = đổi một dòng database thành một object `Phong`

### So với WinForms

Gần giống:

```csharp
PhongDTO p = new PhongDTO();
p.Id = (int)reader["id"];
p.SoPhong = reader["so_phong"].ToString();
p.TenPhong = reader["ten_phong"].ToString();
```

---

## 13. Tại sao phải map như vậy

Nếu không map:

- code ở giao diện phải đọc từng cột
- rất rối
- khó dùng lại

Nếu có map:

- `Activity` chỉ cần làm việc với object `Phong`
- code dễ đọc hơn
- adapter cũng dễ dùng hơn

Ví dụ đẹp:

```java
for (Phong p : danhSachPhong) {
    // dùng p.getSoPhong(), p.getGiaPhong()...
}
```

thay vì phải đụng trực tiếp vào dữ liệu thô từ database.

---

## 14. `loadData()` kết nối với `Repository` như thế nào

Ví dụ:

```java
private void loadData() {
    listFull = phongRepository.getAllPhong();
    applyFilter();
}
```

Ý nghĩa:

- `Activity` không tự viết SQL
- `Activity` gọi `Repository`
- `Repository` trả danh sách `Phong`
- sau đó `Activity` lọc và hiển thị

### Tư duy đúng

- `Activity` lo phần màn hình
- `Repository` lo phần dữ liệu

Đây là cách tổ chức tốt hơn nhiều so với việc viết SQL trực tiếp trong màn hình.

---

## 15. So sánh đầy đủ với WinForms

Đây là bảng nhớ rất quan trọng cho bạn:

- WinForms: `PhongDTO`
  Android: `Phong`
  Giải thích: lớp chứa dữ liệu

- WinForms: `PhongDAL`
  Android: `PhongRepository`
  Giải thích: lớp thao tác dữ liệu

- WinForms: lớp quản lý kết nối / SQL helper
  Android: `DatabaseHelper`
  Giải thích: quản lý SQLite, bảng, cột, schema

- WinForms: `FrmDanhSachPhong`
  Android: `DanhSachPhongActivity`
  Giải thích: màn hình hiển thị dữ liệu

---

## 16. Những lỗi người mới hay nhầm

### Lỗi 1: Nghĩ `Model` là nơi viết SQL

Sai.

`Model` chỉ là dữ liệu.

### Lỗi 2: Nghĩ `Repository` là giao diện

Sai.

`Repository` không hiển thị gì cả.
Nó chỉ xử lý dữ liệu.

### Lỗi 3: Nghĩ `DatabaseHelper` là nơi hiển thị dữ liệu

Sai.

`DatabaseHelper` chủ yếu lo:

- mở database
- tạo bảng
- quản lý schema

### Lỗi 4: Viết SQL thẳng trong `Activity`

Điều này làm code:

- rối
- khó sửa
- khó đọc

---

## 17. Ghi nhớ cuối buổi

Bạn chỉ cần thuộc 5 ý này:

- `Model` là lớp chứa dữ liệu
- `Repository` là lớp thao tác dữ liệu
- `DatabaseHelper` là lớp quản lý SQLite
- `Activity` nên gọi `Repository` thay vì tự viết SQL
- dữ liệu thường đi theo luồng: `Database -> Repository -> Model -> Activity`

---

## 18. Câu hỏi tự kiểm tra

1. `Model` là gì?
2. `Repository` là gì?
3. `DatabaseHelper` dùng để làm gì?
4. `Phong.java` gần giống lớp nào trong WinForms?
5. `PhongRepository.java` gần giống lớp nào trong WinForms?
6. `mapPhong(...)` có nhiệm vụ gì?
7. Vì sao `Activity` không nên tự viết SQL trực tiếp?

---

## 19. Bài tập nhỏ

### Bài 1

Hãy tự giải thích bằng lời của bạn:

- `Model` là gì
- `Repository` là gì
- `DatabaseHelper` là gì

### Bài 2

Tự viết lại luồng sau bằng lời:

- `Activity`
- `Repository`
- `DatabaseHelper`
- `SQLite`
- `Model`

### Bài 3

Hãy chọn một class trong project, ví dụ:

- `Phong.java`
- `KhachThue.java`

và chỉ ra:

- đâu là thuộc tính dữ liệu
- đâu là getter
- đâu là setter

### Bài 4

Hãy chọn một `Repository` trong project và thử tìm:

- hàm lấy tất cả dữ liệu
- hàm thêm
- hàm sửa
- hàm xóa

---

## 20. Chuẩn bị cho buổi sau

Sau khi hiểu bài này, bạn sẽ học tiếp:

- `RecyclerView`
- `Adapter`
- `ViewHolder`
- cách Android hiển thị danh sách dữ liệu
- so sánh trực tiếp với `DataGridView` bên WinForms
