# Buổi 5 - Ghép toàn bộ luồng từ XML đến dữ liệu trong module Phòng

## Mục tiêu buổi học

Sau buổi này, bạn cần hiểu được:

- luồng hoàn chỉnh của một màn hình trong project
- dữ liệu đi từ đâu đến đâu
- vai trò của `XML`, `Activity`, `Repository`, `DatabaseHelper`, `RecyclerView`
- cách đọc trọn một module thật trong project
- vì sao module `Phòng` là nơi rất tốt để bắt đầu đọc hiểu dự án

---

## 1. Vì sao phải có buổi ghép luồng

Từ đầu đến giờ, bạn đã học riêng từng mảnh:

- buổi 1: `Activity`, `XML`, `findViewById(...)`, `Intent`
- buổi 2: `onCreate()`, `onResume()`, `finish()`
- buổi 3: `Model`, `Repository`, `DatabaseHelper`
- buổi 4: `RecyclerView`, `Adapter`, `ViewHolder`

Nếu chỉ học rời rạc, người mới rất dễ bị:

- biết khái niệm nhưng chưa ghép được
- mở project thật ra vẫn rối

Buổi này có nhiệm vụ:

- ghép tất cả lại thành một luồng hoàn chỉnh

---

## 2. Vì sao chọn module `Phòng`

Trong app quản lý nhà trọ, module `Phòng` rất phù hợp để học trước vì:

- dữ liệu khá dễ hiểu
- giao diện rõ ràng
- có danh sách
- có thêm/sửa
- có lọc và tìm kiếm
- nối được đầy đủ với database

Nếu bạn đọc hiểu được module này, bạn sẽ có nền để đọc:

- `KhachThue`
- `HopDong`
- `HoaDon`

---

## 3. Những file nên nhìn cùng nhau

Khi đọc module `Phòng`, bạn nên nhìn theo nhóm sau:

### Giao diện màn hình

- `activity_danh_sach_phong.xml`

### Màn hình Java

- `DanhSachPhongActivity.java`

### Giao diện một dòng

- `item_phong.xml`

### Adapter hiển thị danh sách

- `PhongAdapter.java`

### Model dữ liệu

- `Phong.java`

### Lớp truy cập dữ liệu

- `PhongRepository.java`

### Lớp quản lý SQLite

- `DatabaseHelper.java`

Đây là bộ file bạn nên tập nhìn cùng nhau.

---

## 4. Cách đọc đúng thứ tự

Khi mới học, bạn không nên mở file ngẫu nhiên.

Thứ tự tốt nhất là:

1. đọc XML của màn hình
2. đọc `Activity`
3. đọc `loadData()`
4. đọc `Repository`
5. đọc `Model`
6. đọc `Adapter`
7. đọc XML của item

### Vì sao thứ tự này tốt

Vì nó bám theo cách suy nghĩ tự nhiên:

- màn hình trông như thế nào
- màn hình xử lý ra sao
- dữ liệu lấy từ đâu
- dữ liệu hiển thị lên như thế nào

---

## 5. Bước 1 - Đọc file XML của màn hình

Ví dụ:

- `activity_danh_sach_phong.xml`

Khi đọc file này, bạn chưa cần lo logic Java ngay.
Bạn chỉ cần tự hỏi:

- màn hình có `RecyclerView` không
- có `EditText` để tìm kiếm không
- có nút thêm không
- có chip lọc trạng thái không

### Ý nghĩa

Nếu chỉ nhìn XML, bạn đã đoán được:

- đây là màn hình danh sách
- có hỗ trợ tìm kiếm
- có hỗ trợ lọc
- có hỗ trợ thêm mới

### Đây là tư duy rất quan trọng

Đọc XML để đoán chức năng trước,
sau đó mới vào Java để kiểm chứng.

---

## 6. Bước 2 - Đọc `Activity`

Ví dụ:

- `DanhSachPhongActivity.java`

Bạn hãy đọc theo thứ tự:

- `onCreate()`
- `onResume()`
- `initViews()`
- `setupRecyclerView()`
- `setupSearch()`
- `setupFilters()`
- `loadData()`

### Khi đọc `onCreate()`, tự hỏi:

- màn hình dùng XML nào
- khởi tạo repository chưa
- có setup RecyclerView chưa
- có setup search chưa
- có setup filter chưa

### Khi đọc `onResume()`, tự hỏi:

- màn hình có tải lại dữ liệu không

---

## 7. Bước 3 - Hiểu `loadData()`

Ví dụ:

```java
private void loadData() {
    listFull = phongRepository.getAllPhong();
    applyFilter();
}
```

### Phân tích

```java
listFull = phongRepository.getAllPhong();
```

- gọi repository lấy toàn bộ phòng từ database

```java
applyFilter();
```

- không đổ thẳng lên danh sách ngay
- lọc lại theo điều kiện hiện tại

Ví dụ điều kiện có thể là:

- từ khóa tìm kiếm
- trạng thái: tất cả, trống, đang thuê, bảo trì

### Ý nghĩa lớn nhất

`loadData()` là điểm nối giữa:

- màn hình
- dữ liệu
- logic lọc

---

## 8. Bước 4 - Hiểu `Repository`

Ví dụ:

- `PhongRepository.java`

Bạn hãy tìm các hàm như:

- `getAllPhong()`
- `getPhongById(...)`
- `addPhong(...)`
- `updatePhong(...)`
- `deletePhong(...)`

### Ý nghĩa

- repository là nơi giao tiếp với database
- activity không tự viết SQL

Đây là một cách tổ chức code rất quan trọng.

### So với WinForms

Gần giống:

- `PhongDAL`

---

## 9. Bước 5 - Hiểu `mapPhong(...)`

Đây là hàm rất đáng chú ý khi đọc repository.

### Nó làm gì

- lấy một dòng dữ liệu từ database
- gán các cột vào object `Phong`
- trả về object `Phong`

### Nghĩ dễ hiểu

`mapPhong(...)` = “phiên dịch” một dòng database thành object Java

### Vì sao quan trọng

Nếu không có bước này:

- dữ liệu sẽ còn ở dạng thô
- activity và adapter sẽ khó dùng hơn

---

## 10. Bước 6 - Hiểu `Model`

Ví dụ:

- `Phong.java`

Khi đọc `Phong.java`, bạn chỉ cần tập trung vào:

- những thuộc tính dữ liệu là gì
- có getter gì
- có setter gì

### Ví dụ

- `id`
- `soPhong`
- `tenPhong`
- `giaPhong`
- `dienTich`
- `trangThai`

### Ý nghĩa

Đây là “hình đại diện dữ liệu” của một phòng trong code Java.

### So với WinForms

Gần giống:

- `PhongDTO`

---

## 11. Bước 7 - Hiểu `Adapter`

Ví dụ:

- `PhongAdapter.java`

Đây là nơi hiển thị danh sách phòng lên `RecyclerView`.

Bạn hãy tìm các phần:

- danh sách dữ liệu
- `setDanhSachPhong(...)`
- `onCreateViewHolder(...)`
- `onBindViewHolder(...)`
- `bind(...)`

### Nghĩa là

- dữ liệu đã lấy xong từ repository
- adapter sẽ chịu trách nhiệm đổ nó ra màn hình

---

## 12. Bước 8 - Hiểu `item_phong.xml`

Đây là file giao diện của một dòng.

Khi mở file này, bạn hãy hỏi:

- một dòng hiển thị những gì
- có nút gì
- có trạng thái gì
- bố cục của một phòng trông ra sao

### Liên hệ với adapter

Những control trong `item_phong.xml` sẽ được:

- `ViewHolder` lấy ra
- `bind(phong)` gán dữ liệu vào

---

## 13. Luồng hoàn chỉnh của module `Phòng`

Đây là luồng bạn nên học thuộc:

1. người dùng mở màn hình danh sách phòng
2. `DanhSachPhongActivity` chạy `onCreate()`
3. Activity gắn XML giao diện
4. Activity setup RecyclerView, search, filter
5. Activity chạy `onResume()`
6. `loadData()` được gọi
7. `PhongRepository.getAllPhong()` lấy dữ liệu từ SQLite
8. `mapPhong(...)` đổi từng dòng thành object `Phong`
9. trả về `List<Phong>`
10. `applyFilter()` lọc dữ liệu
11. `adapter.setDanhSachPhong(...)` nhận danh sách
12. `RecyclerView` hiển thị lên màn hình

### Sơ đồ

```text
Người dùng mở màn hình
   |
   v
DanhSachPhongActivity
   |
   +--> onCreate()
   |      |
   |      +--> setContentView(...)
   |      +--> initViews()
   |      +--> setupRecyclerView()
   |      +--> setupSearch()
   |      +--> setupFilters()
   |
   +--> onResume()
          |
          +--> loadData()
                 |
                 v
          PhongRepository.getAllPhong()
                 |
                 v
          DatabaseHelper / SQLite
                 |
                 v
          dữ liệu thô
                 |
                 v
          mapPhong(...)
                 |
                 v
          List<Phong>
                 |
                 v
          applyFilter()
                 |
                 v
          adapter.setDanhSachPhong(...)
                 |
                 v
          RecyclerView hiển thị
```

---

## 14. Cách nghĩ đúng khi đọc một module

Đừng đọc kiểu:

- file nào mở trước cũng được
- đọc từng file rời rạc

Hãy đọc theo câu hỏi:

### Câu hỏi 1

Màn hình này dùng để làm gì?

### Câu hỏi 2

Giao diện của nó có những phần nào?

### Câu hỏi 3

Dữ liệu lấy từ đâu?

### Câu hỏi 4

Dữ liệu sau khi lấy về được lọc, xử lý, hiển thị ra sao?

### Câu hỏi 5

Người dùng có thể bấm gì trên màn hình này?

Nếu bạn đọc theo 5 câu hỏi này, project sẽ dễ hơn rất nhiều.

---

## 15. Ví dụ “đọc hiểu” đúng kiểu người mới

Giả sử bạn mở `DanhSachPhongActivity.java`.

Bạn thấy:

```java
phongRepository = new PhongRepository(this);
```

Bạn nên nghĩ:

- màn hình này dùng repository để lấy dữ liệu phòng

Bạn thấy:

```java
recyclerPhong.setAdapter(adapter);
```

Bạn nên nghĩ:

- màn hình này dùng adapter để hiển thị danh sách

Bạn thấy:

```java
listFull = phongRepository.getAllPhong();
```

Bạn nên nghĩ:

- dữ liệu đang được lấy từ database

Bạn thấy:

```java
applyFilter();
```

Bạn nên nghĩ:

- dữ liệu không hiện nguyên si
- nó sẽ qua bước lọc trước

Đây chính là kiểu đọc hiểu code mà bạn cần tập.

---

## 16. Những lỗi người mới hay gặp khi đọc project

### Lỗi 1: Mở `Repository` trước khi hiểu màn hình

Điều này làm bạn rất dễ rối vì:

- chưa biết dữ liệu dùng để làm gì

### Lỗi 2: Chỉ đọc XML mà bỏ qua Java

Như vậy bạn chỉ thấy:

- màn hình trông ra sao

nhưng không hiểu:

- dữ liệu chạy như thế nào

### Lỗi 3: Chỉ đọc Activity mà không đọc Adapter

Như vậy bạn sẽ thiếu mắt xích:

- dữ liệu được hiển thị cụ thể lên từng dòng ra sao

### Lỗi 4: Không phân biệt “lấy dữ liệu” và “hiển thị dữ liệu”

Bạn cần tách rõ:

- lấy dữ liệu: `Repository`
- hiển thị dữ liệu: `Adapter` + `RecyclerView`

---

## 17. Ghi nhớ cuối buổi

Bạn chỉ cần thuộc 6 ý này:

- đọc module nên bắt đầu từ XML của màn hình
- `Activity` là trung tâm điều phối
- `Repository` lấy dữ liệu từ database
- `Model` giữ dữ liệu theo object
- `Adapter` hiển thị dữ liệu lên danh sách
- module `Phòng` là nơi rất tốt để bắt đầu hiểu dự án

---

## 18. Câu hỏi tự kiểm tra

1. Khi đọc một module, nên mở file nào trước?
2. `loadData()` đóng vai trò gì trong module `Phòng`?
3. `PhongRepository.getAllPhong()` làm gì?
4. `mapPhong(...)` dùng để làm gì?
5. `Adapter` nằm ở đoạn nào trong luồng dữ liệu?
6. `item_phong.xml` khác gì với `activity_danh_sach_phong.xml`?
7. Vì sao module `Phòng` phù hợp để học trước?

---

## 19. Bài tập nhỏ

### Bài 1

Hãy tự viết lại bằng lời của bạn luồng sau:

- XML
- Activity
- Repository
- SQLite
- Model
- Adapter
- RecyclerView

### Bài 2

Mở module `Phòng` trong project và thử chỉ ra:

- file giao diện màn hình
- file Java của màn hình
- file adapter
- file model
- file repository
- file item XML

### Bài 3

Tự trả lời:

- dữ liệu phòng bắt đầu từ đâu
- đi qua lớp nào
- đến đâu thì xuất hiện lên màn hình

---

## 20. Chuẩn bị cho phần tiếp theo

Sau buổi này, bạn đã đủ nền để chuyển dần sang phần:

- đọc hiểu project thật sâu hơn
- bắt đầu với:
  - `KhachThue`
  - `HopDong`
  - `HoaDon`

hoặc tiếp tục làm các bài thực hành nhỏ trên chính project này.
