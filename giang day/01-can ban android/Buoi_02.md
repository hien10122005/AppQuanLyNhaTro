# Buổi 2 - Vòng đời Activity và cách Android vận hành màn hình

## Mục tiêu buổi học

Sau buổi này, bạn cần hiểu được:

- vòng đời `Activity` là gì
- `onCreate()` dùng để làm gì
- `onResume()` dùng để làm gì
- `finish()` là gì
- vì sao Android hay tải lại dữ liệu trong `onResume()`
- cách đọc một file `Activity` theo đúng thứ tự để đỡ rối
- cách nối phần này với tư duy WinForms

---

## 1. Vì sao phải học vòng đời Activity

Đây là phần khác WinForms khá rõ.

Trong WinForms, bạn thường hình dung rất đơn giản:

- mở form
- form load
- người dùng thao tác
- đóng form

Trong Android, một màn hình không chỉ có trạng thái:

- mở
- đóng

Mà nó còn có những trạng thái trung gian khác, ví dụ:

- vừa được tạo
- đang hiển thị
- bị che bởi màn hình khác
- quay lại để dùng tiếp

Android phải làm chặt như vậy vì:

- điện thoại có bộ nhớ hạn chế hơn desktop
- người dùng chuyển màn hình liên tục
- app có thể bị đưa ra nền
- app có thể quay lại từ trạng thái tạm dừng

Vì vậy, Android dùng khái niệm:

- vòng đời `Activity`

---

## 2. Hiểu Activity trước khi hiểu vòng đời

Bạn hãy nhớ lại:

- `Activity` là một màn hình Android

Ví dụ trong project của bạn:

- màn hình danh sách phòng
- màn hình thêm khách thuê
- màn hình thống kê
- màn hình hóa đơn

Mỗi màn hình đó không chỉ “xuất hiện” rồi “biến mất”.
Android cần biết:

- khi nào tạo nó
- khi nào cho người dùng dùng nó
- khi nào đóng nó
- khi nào quay lại nó

Chính những giai đoạn này tạo thành vòng đời.

---

## 3. 3 mảnh quan trọng nhất cho người mới

Người mới không cần học hết toàn bộ vòng đời ngay.
Bạn chỉ cần nắm thật chắc 3 phần này trước:

- `onCreate()`
- `onResume()`
- `finish()`

Bạn có thể tạm hiểu:

- `onCreate()` = tạo màn hình lần đầu
- `onResume()` = màn hình quay lại trạng thái người dùng có thể thao tác
- `finish()` = đóng màn hình

Chỉ cần hiểu chắc 3 ý này, bạn đã đọc được rất nhiều màn hình Android rồi.

---

## 4. So sánh nhanh với WinForms

### Cặp so sánh chính

- WinForms: constructor của Form
  Android: `onCreate()`
  Giải thích: nơi khởi tạo ban đầu

- WinForms: `Form_Load`
  Android: một phần của `onCreate()`
  Giải thích: nơi chuẩn bị giao diện và dữ liệu lúc đầu

- WinForms: form quay lại hoạt động rồi refresh dữ liệu
  Android: `onResume()`
  Giải thích: nơi thích hợp để làm mới dữ liệu

- WinForms: `this.Close()`
  Android: `finish()`
  Giải thích: đóng màn hình hiện tại

### Lưu ý quan trọng

So sánh này giúp bạn học nhanh hơn, nhưng không phải giống 100%.

Android chặt hơn WinForms vì:

- Android quản lý tài nguyên mạnh hơn
- màn hình có thể bị tạm dừng, che khuất, hoặc quay lại rất nhiều lần

---

## 5. `onCreate()` là gì

Đây là hàm thường chạy khi `Activity` được tạo lần đầu.

### Ví dụ

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_danh_sach_phong);

    phongRepository = new PhongRepository(this);

    initViews();
    setupRecyclerView();
    setupBottomNavigation();
}
```

### Ý nghĩa từng dòng

```java
super.onCreate(savedInstanceState);
```

- cho lớp cha xử lý phần khởi tạo mặc định
- gần như luôn phải có

```java
setContentView(R.layout.activity_danh_sach_phong);
```

- gắn file XML giao diện vào màn hình hiện tại

```java
phongRepository = new PhongRepository(this);
```

- tạo đối tượng để làm việc với dữ liệu phòng

```java
initViews();
```

- lấy các control từ XML ra Java bằng `findViewById(...)`

```java
setupRecyclerView();
setupBottomNavigation();
```

- thiết lập các thành phần giao diện

### `onCreate()` thường dùng để làm gì

- gắn file XML bằng `setContentView(...)`
- khởi tạo biến
- tạo repository
- lấy control bằng `findViewById(...)`
- gắn sự kiện
- thiết lập recycler view, adapter, navigation

### So với WinForms

Bạn có thể hiểu gần giống:

- constructor của Form
- cộng với một phần `Form_Load`

---

## 6. Vì sao `super.onCreate(...)` quan trọng

Trong Android, khi ghi đè `onCreate()`, ta thường phải gọi:

```java
super.onCreate(savedInstanceState);
```

### Ý nghĩa

- cho lớp cha thực hiện phần khởi tạo mặc định
- nếu bỏ dòng này, Activity có thể hoạt động sai

### Cách nhớ

- khi override `onCreate()`, gần như luôn gọi `super.onCreate(...)`

Bạn cứ xem đây là “quy tắc cơ bản” khi viết Activity.

---

## 7. `onResume()` là gì

Đây là hàm chạy khi màn hình quay lại trạng thái người dùng có thể nhìn thấy và thao tác.

### Ví dụ

```java
@Override
protected void onResume() {
    super.onResume();
    loadData();
}
```

### Ý nghĩa

- màn hình đang trở lại để người dùng dùng tiếp
- đây là nơi rất phù hợp để làm mới dữ liệu

### Nói đơn giản

Nếu `onCreate()` là dựng cái khung màn hình,
thì `onResume()` thường là chỗ “cập nhật lại nội dung bên trong”.

---

## 8. Tại sao dữ liệu hay đặt ở `onResume()`

Hãy tưởng tượng luồng sau:

1. bạn đang ở màn hình danh sách phòng
2. bạn bấm sang màn hình thêm phòng
3. bạn thêm xong
4. bạn quay lại màn hình danh sách

Lúc này:

- nếu danh sách chỉ tải ở `onCreate()`, dữ liệu có thể vẫn là dữ liệu cũ
- nếu danh sách tải ở `onResume()`, nó sẽ được cập nhật lại

Đó là lý do trong project có kiểu viết:

```java
@Override
protected void onResume() {
    super.onResume();
    loadData();
}
```

### So với WinForms

Gần giống ý:

- khi form quay lại hoạt động thì refresh dữ liệu

Nhưng Android không có đúng một sự kiện giống WinForms để bạn nghĩ đơn giản như vậy, nên `onResume()` thường là nơi dễ nhớ nhất cho mục tiêu này.

---

## 9. Phân tích kỹ tình huống “thêm mới rồi quay lại”

Đây là tình huống rất thực tế trong app quản lý.

### Trường hợp 1: Chỉ load trong `onCreate()`

Giả sử:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(...);
    loadData();
}
```

Khi bạn:

- mở danh sách phòng
- sang màn hình thêm phòng
- thêm phòng xong quay lại

thì có khả năng:

- danh sách chưa tự cập nhật đúng như bạn mong muốn

### Trường hợp 2: Load trong `onResume()`

```java
@Override
protected void onResume() {
    super.onResume();
    loadData();
}
```

Khi quay lại màn hình:

- `loadData()` được gọi lại
- danh sách phòng được cập nhật mới

### Kết luận

- dữ liệu thay đổi sau khi đi sang màn hình khác
- thì `onResume()` là nơi rất hợp để refresh

---

## 10. `finish()` là gì

`finish()` dùng để đóng `Activity` hiện tại.

### Ví dụ

```java
btnBack.setOnClickListener(v -> finish());
```

### Ý nghĩa

- đóng màn hình hiện tại
- quay về màn hình trước đó

### So với WinForms

Gần giống:

```csharp
this.Close();
```

### Tình huống thường gặp

- bấm nút back tự tạo trên toolbar
- bấm nút hủy
- lưu xong rồi quay lại màn hình trước

---

## 11. Một luồng chạy thực tế của màn hình

Hãy nhìn một màn hình danh sách như `DanhSachPhongActivity`.

Luồng thường là:

1. Android mở màn hình
2. gọi `onCreate()`
3. Activity gắn giao diện bằng `setContentView(...)`
4. Activity lấy control bằng `findViewById(...)`
5. Activity gắn sự kiện
6. Activity tạo adapter, repository
7. màn hình hiện ra
8. Android gọi `onResume()`
9. dữ liệu được tải hoặc tải lại

Nếu người dùng sang màn hình khác rồi quay lại:

- `onResume()` rất hay chạy lại
- nên dữ liệu được refresh

---

## 12. Hình dung bằng sơ đồ

```text
Mở Activity
   |
   v
onCreate()
   |
   +--> setContentView(...)
   +--> findViewById(...)
   +--> setup giao diện
   +--> setup sự kiện
   |
   v
Màn hình hiển thị
   |
   v
onResume()
   |
   +--> loadData()
   +--> làm mới dữ liệu
   |
   v
Người dùng thao tác
   |
   +--> sang màn hình khác
   +--> quay lại
   |
   v
onResume() chạy lại
   |
   v
Dữ liệu được cập nhật
   |
   v
finish()
   |
   v
Đóng màn hình
```

---

## 13. Khi nào không nên nhét hết mọi thứ vào `onCreate()`

Người mới hay có xu hướng viết tất cả vào `onCreate()`.

Điều đó có thể gây:

- hàm quá dài, khó đọc
- dữ liệu không refresh đúng lúc
- logic màn hình rối

### Ví dụ viết chưa đẹp

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(...);

    // lấy toàn bộ view
    // gắn toàn bộ sự kiện
    // setup recycler
    // setup navigation
    // load data
    // filter data
    // xử lý intent
    // xử lý back
    // xử lý search
    // xử lý chip filter
}
```

Hàm như vậy sẽ:

- dài
- khó sửa
- khó học cho người mới

### Cách tốt hơn là tách ra

- `onCreate()` để dựng màn hình
- `onResume()` để làm mới dữ liệu
- các hàm riêng như:
  - `initViews()`
  - `setupRecyclerView()`
  - `setupBottomNavigation()`
  - `setupSearch()`
  - `setupFilters()`
  - `loadData()`

---

## 14. Ví dụ tách hàm đẹp hơn

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_danh_sach_phong);

    initViews();
    setupRecyclerView();
    setupSearch();
    setupFilters();
    setupBottomNavigation();
}

@Override
protected void onResume() {
    super.onResume();
    loadData();
}
```

### Vì sao cách này tốt hơn

- nhìn `onCreate()` là hiểu ngay màn hình được dựng như nào
- nhìn `onResume()` là hiểu ngay màn hình có refresh dữ liệu không
- mỗi hàm nhỏ có trách nhiệm rõ ràng

---

## 15. `loadData()` thường làm gì

Ví dụ:

```java
private void loadData() {
    listFull = phongRepository.getAllPhong();
    applyFilter();
}
```

### Ý nghĩa từng dòng

```java
listFull = phongRepository.getAllPhong();
```

- gọi repository để lấy toàn bộ danh sách phòng từ database

```java
applyFilter();
```

- áp dụng lại bộ lọc hiện tại
- ví dụ: từ khóa tìm kiếm, trạng thái phòng

### Kết luận

`loadData()` là chỗ nối giữa:

- màn hình
- repository
- danh sách hiển thị

---

## 16. Cách đọc một file `Activity` cho đỡ rối

Khi mở một file `Activity`, bạn nên đọc theo thứ tự sau:

### Bước 1. Tìm `onCreate()`

Xem:

- màn hình dùng XML nào
- khởi tạo những gì
- có setup thành phần nào

### Bước 2. Tìm `onResume()`

Xem:

- màn hình có tải lại dữ liệu không
- có refresh danh sách không

### Bước 3. Tìm `initViews()`

Xem:

- màn hình lấy những control nào ra từ XML

### Bước 4. Tìm các hàm setup

Ví dụ:

- `setupRecyclerView()`
- `setupSearch()`
- `setupBottomNavigation()`

### Bước 5. Tìm `loadData()`

Xem:

- dữ liệu lấy từ đâu
- sau khi lấy xong thì hiển thị kiểu gì

Đây là cách đọc rất hợp với người mới vì nó đi từ “khung lớn” đến “chi tiết”.

---

## 17. Ví dụ thực tế gắn với project của bạn

Nếu trong màn hình danh sách phòng có:

```java
@Override
protected void onResume() {
    super.onResume();
    loadData();
}
```

thì bạn nên hiểu ngay:

- mỗi lần quay lại màn hình
- danh sách phòng sẽ được tải lại

Nếu màn hình có:

```java
btnBack.setOnClickListener(v -> finish());
```

thì bạn nên hiểu ngay:

- bấm nút back sẽ đóng màn hình hiện tại

Nếu trong `onCreate()` có:

```java
setContentView(R.layout.activity_danh_sach_phong);
```

thì bạn nên hiểu ngay:

- file giao diện dùng là `activity_danh_sach_phong.xml`

Nếu trong `onCreate()` có:

```java
setupRecyclerView();
```

thì bạn nên hiểu ngay:

- màn hình này có danh sách
- và đang chuẩn bị phần hiển thị dữ liệu

---

## 18. Những lỗi người mới hay gặp

### Lỗi 1: Nghĩ rằng `onCreate()` luôn chạy lại mỗi lần quay về màn hình

Không nên hiểu như vậy.

Với người mới, bạn chỉ cần nhớ:

- quay lại màn hình thì `onResume()` là nơi thường đáng chú ý để refresh

### Lỗi 2: Chỉ tải dữ liệu trong `onCreate()`

Nếu làm vậy, có thể:

- thêm mới xong quay lại
- danh sách chưa cập nhật

### Lỗi 3: Viết mọi thứ vào một hàm

Điều đó khiến code:

- dài
- rối
- khó sửa

### Lỗi 4: Không phân biệt “dựng giao diện” và “làm mới dữ liệu”

Bạn cần tách tư duy:

- dựng giao diện: `onCreate()`
- làm mới dữ liệu: `onResume()`

---

## 19. Ghi nhớ cuối buổi

Bạn chỉ cần thuộc 6 ý này:

- `onCreate()` dùng để dựng màn hình ban đầu
- `onCreate()` thường gắn XML, lấy view, setup giao diện
- `onResume()` dùng để làm mới dữ liệu khi quay lại màn hình
- `finish()` dùng để đóng màn hình
- không nên nhét mọi thứ vào `onCreate()`
- đọc `Activity` nên đọc từ `onCreate()` rồi đến `onResume()`

---

## 20. Câu hỏi tự kiểm tra

1. `onCreate()` thường dùng để làm gì?
2. `onResume()` thường dùng để làm gì?
3. Tại sao nhiều màn hình lại gọi `loadData()` trong `onResume()`?
4. `finish()` gần giống lệnh nào trong WinForms?
5. Nếu mở màn hình thêm mới rồi quay lại màn hình danh sách, hàm nào thường nên chạy để cập nhật dữ liệu?
6. Vì sao không nên viết mọi thứ vào `onCreate()`?

---

## 21. Bài tập nhỏ

### Bài 1

Hãy tự giải thích bằng lời của bạn:

- `onCreate()` là gì
- `onResume()` là gì
- `finish()` là gì

### Bài 2

Mở một file `Activity` bất kỳ trong project và thử chỉ ra:

- đâu là `onCreate()`
- đâu là `onResume()`
- đâu là phần gắn giao diện
- đâu là phần tải dữ liệu

### Bài 3

Tự viết lại bằng lời luồng sau:

- mở màn hình
- khởi tạo giao diện
- tải dữ liệu
- qua màn hình khác
- quay về
- làm mới dữ liệu

### Bài 4

Hãy tìm trong project một chỗ có `finish()` và giải thích:

- bấm nút nào thì nó chạy
- chạy xong sẽ quay về đâu

---

## 22. Chuẩn bị cho buổi sau

Sau khi hiểu bài này, bạn sẽ học tiếp:

- `Model`
- `Repository`
- `DatabaseHelper`
- cách so sánh với `DTO - DAL - BUS` trong WinForms
