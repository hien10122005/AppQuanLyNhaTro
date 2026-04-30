# Buổi 4 - RecyclerView, Adapter, ViewHolder và cách so sánh với DataGridView

## Mục tiêu buổi học

Sau buổi này, bạn cần hiểu được:

- `RecyclerView` là gì
- `Adapter` là gì
- `ViewHolder` là gì
- `onCreateViewHolder()` dùng để làm gì
- `onBindViewHolder()` dùng để làm gì
- vì sao Android hiển thị danh sách khác WinForms
- cách nối phần này với `DataGridView`

---

## 1. Vì sao buổi này quan trọng

Đây là phần mà hầu như app quản lý nào cũng phải dùng.

Trong project của bạn:

- danh sách phòng
- danh sách khách thuê
- danh sách hóa đơn
- danh sách bảo trì

đều là các màn hình kiểu “hiển thị nhiều dòng dữ liệu”.

Trong WinForms, bạn quen với:

- `DataGridView`

Trong Android, cách phổ biến là:

- `RecyclerView`

Nếu hiểu được buổi này, bạn sẽ đọc project dễ hơn rất nhiều.

---

## 2. So sánh nhanh với WinForms

- WinForms: `DataGridView`
  Android: `RecyclerView`
  Giải thích: vùng hiển thị danh sách dữ liệu

- WinForms: `DataSource`
  Android: `List<Phong>` hoặc `List<KhachThue>`
  Giải thích: dữ liệu nguồn

- WinForms: code đổ dữ liệu lên lưới
  Android: `Adapter`
  Giải thích: lớp trung gian đưa dữ liệu lên từng dòng

- WinForms: mỗi hàng và cột được grid quản lý sẵn
  Android: mỗi dòng do bạn tự mô tả bằng XML
  Giải thích: Android linh hoạt hơn nhưng cũng khó hơn lúc đầu

### Cách nhớ

- `RecyclerView` = nơi hiển thị danh sách
- `Adapter` = người đổ dữ liệu vào danh sách
- `ViewHolder` = đại diện cho một dòng

---

## 3. `RecyclerView` là gì

`RecyclerView` là control dùng để hiển thị danh sách dữ liệu trong Android.

Ví dụ:

- danh sách phòng
- danh sách khách thuê
- danh sách hóa đơn

### Ví dụ XML

```xml
<androidx.recyclerview.widget.RecyclerView
    android:id="@+id/rvDanhSachPhong"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

### So với WinForms

Gần giống:

- `DataGridView`

Nhưng bạn phải nhớ một điểm quan trọng:

- `RecyclerView` không tự biết cách hiển thị dữ liệu
- nó phải nhờ `Adapter`

---

## 4. `RecyclerView` khác `DataGridView` ở đâu

### `DataGridView`

Trong WinForms:

- thường có sẵn cấu trúc hàng và cột
- có thể bind dữ liệu khá nhanh
- grid tự lo nhiều phần hiển thị cho bạn

### `RecyclerView`

Trong Android:

- bạn phải tự tạo giao diện cho từng dòng
- bạn phải có `Adapter`
- bạn phải có `ViewHolder`

### Kết luận

- `DataGridView` dễ bắt đầu hơn
- `RecyclerView` linh hoạt hơn

---

## 5. Vì sao Android không dùng kiểu grid đơn giản như WinForms

Vì giao diện điện thoại rất đa dạng.

Một dòng dữ liệu trên điện thoại không nhất thiết phải là:

- cột 1
- cột 2
- cột 3

Nó có thể là:

- tiêu đề lớn
- mô tả nhỏ bên dưới
- trạng thái có màu
- nhiều nút con trong cùng một dòng

Ví dụ trong app của bạn, một dòng phòng có thể có:

- số phòng
- loại phòng
- giá
- trạng thái
- nút sửa
- nút điện
- nút nước

Đây là lý do Android dùng mô hình:

- `RecyclerView + Adapter + ViewHolder`

---

## 6. `item_phong.xml` là gì

Trong Android, mỗi dòng danh sách thường có một file XML riêng.

Ví dụ:

- `activity_danh_sach_phong.xml` = giao diện của cả màn hình
- `item_phong.xml` = giao diện của một dòng phòng

### Điều này rất quan trọng

Bạn phải phân biệt:

- file của cả màn hình
- file của một item trong danh sách

### So với WinForms

Trong WinForms, `DataGridView` thường tự quản lý hàng/cột.
Trong Android, bạn phải nói rõ:

- “mỗi dòng sẽ trông như thế nào”

File `item_phong.xml` chính là câu trả lời.

---

## 7. `Adapter` là gì

`Adapter` là lớp trung gian giữa:

- dữ liệu
- giao diện danh sách

Ví dụ:

- dữ liệu là `List<Phong>`
- giao diện là `RecyclerView`
- lớp trung gian là `PhongAdapter`

### Nhiệm vụ của `Adapter`

- nhận dữ liệu
- tạo ra từng dòng giao diện
- đổ dữ liệu lên từng dòng
- xử lý click trên từng dòng nếu cần

### So với WinForms

Bạn có thể hiểu `Adapter` gần giống:

- phần code đổ dữ liệu vào `DataGridView`

nhưng chi tiết và mạnh hơn.

---

## 8. `ViewHolder` là gì

`ViewHolder` là lớp đại diện cho một dòng hiển thị.

Trong một dòng phòng, bạn có thể có:

- `tvSoPhong`
- `tvDetail`
- `tvGiaPhong`
- `tvTrangThai`
- `btnEditRoom`

`ViewHolder` là nơi giữ các control này.

### Vì sao cần `ViewHolder`

Nếu mỗi lần hiển thị lại phải đi tìm toàn bộ control từ đầu thì sẽ:

- chậm hơn
- rối hơn

Nên Android dùng `ViewHolder` để giữ lại các view của một dòng.

### So với WinForms

Bạn có thể hiểu:

- `ViewHolder` giống như “bộ control của một hàng”

---

## 9. Cấu trúc một Adapter thường có gì

Ví dụ một `PhongAdapter` thường có:

- danh sách dữ liệu
- `setDanhSachPhong(...)`
- `onCreateViewHolder(...)`
- `onBindViewHolder(...)`
- `getItemCount()`
- class `PhongViewHolder`

Đây là bộ khung rất phổ biến trong Android.

---

## 10. `setDanhSachPhong(...)` là gì

Ví dụ:

```java
public void setDanhSachPhong(List<Phong> list) {
    this.danhSachPhong = list;
    notifyDataSetChanged();
}
```

### Ý nghĩa

- nhận danh sách dữ liệu mới
- gán vào adapter
- báo cho giao diện biết dữ liệu đã đổi

### `notifyDataSetChanged()` là gì

Nó có thể hiểu đơn giản là:

- “dữ liệu đã thay đổi rồi, hãy vẽ lại danh sách”

### So với WinForms

Gần giống ý:

- gán `DataSource` mới
- rồi refresh lại grid

---

## 11. `onCreateViewHolder()` là gì

Đây là hàm tạo giao diện cho một dòng.

### Ví dụ

```java
@Override
public PhongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_phong, parent, false);
    return new PhongViewHolder(view);
}
```

### Ý nghĩa

- lấy file `item_phong.xml`
- biến nó thành view thật để hiển thị
- bọc view đó vào `PhongViewHolder`

### Cách hiểu đơn giản

- `onCreateViewHolder()` = tạo cái khung của một dòng

### So với WinForms

Không có bản sao tương đương hoàn toàn trực tiếp,
nhưng bạn có thể tưởng tượng:

- đây là bước tạo “giao diện một hàng”

---

## 12. `onBindViewHolder()` là gì

Đây là hàm đổ dữ liệu vào dòng đã tạo.

### Ví dụ

```java
@Override
public void onBindViewHolder(@NonNull PhongViewHolder holder, int position) {
    Phong phong = danhSachPhong.get(position);
    holder.bind(phong);
}
```

### Ý nghĩa

- lấy object `Phong` ở vị trí `position`
- truyền object đó vào dòng tương ứng

### Cách hiểu đơn giản

- `onCreateViewHolder()` = tạo khung
- `onBindViewHolder()` = đổ dữ liệu vào khung

### So với WinForms

Gần giống việc:

- lấy từng dòng dữ liệu
- gán vào các ô trong một hàng

---

## 13. `bind(phong)` là gì

Trong `ViewHolder`, thường sẽ có một hàm kiểu:

```java
public void bind(Phong phong) {
    tvSoPhong.setText("P." + phong.getSoPhong());
    tvGiaPhong.setText(...);
    setupStatusUI(phong.getTrangThai());
}
```

### Ý nghĩa

- lấy dữ liệu từ object `Phong`
- gán lên các control của dòng

### So với WinForms

Gần giống:

```csharp
lblSoPhong.Text = phong.SoPhong;
lblGia.Text = phong.GiaPhong.ToString();
```

### Kết luận

- `bind(phong)` = đổ dữ liệu của một object lên một dòng giao diện

---

## 14. `getItemCount()` là gì

Ví dụ:

```java
@Override
public int getItemCount() {
    return danhSachPhong.size();
}
```

### Ý nghĩa

- cho `RecyclerView` biết có bao nhiêu dòng cần hiển thị

Nếu danh sách có:

- 10 phần tử

thì `RecyclerView` sẽ hiển thị 10 dòng.

---

## 15. Click trên từng dòng hoạt động ra sao

Một điểm rất hay của Android là:

- không chỉ click cả dòng
- mà còn có thể click từng nút con trong dòng

Ví dụ trong một dòng phòng có thể có:

- click cả dòng để xem chi tiết
- click nút sửa để sửa
- click icon điện để nhập điện
- click icon nước để nhập nước

### Điều này được làm ở đâu

Thường nằm trong `ViewHolder`, ví dụ:

```java
itemView.setOnClickListener(...);
btnEditRoom.setOnClickListener(...);
btnElectricity.setOnClickListener(...);
btnWater.setOnClickListener(...);
```

### So với WinForms

WinForms cũng làm được,
nhưng Android tổ chức nó theo từng item rõ hơn.

---

## 16. Luồng đầy đủ của danh sách

Đây là phần bạn nên học thuộc.

### Luồng chạy

1. `Activity` mở màn hình
2. `RecyclerView` có mặt trong XML
3. `Activity` tạo `Adapter`
4. `Activity` gán `Adapter` cho `RecyclerView`
5. `Activity` gọi `loadData()`
6. `Repository` trả về `List<Phong>`
7. `Adapter` nhận danh sách đó
8. `RecyclerView` yêu cầu tạo và bind từng dòng
9. danh sách hiển thị lên màn hình

### Sơ đồ

```text
List<Phong>
   |
   v
PhongAdapter
   |
   +--> onCreateViewHolder() -> tạo giao diện 1 dòng
   |
   +--> onBindViewHolder() -> đổ dữ liệu vào dòng
   |
   v
RecyclerView
   |
   v
Hiển thị danh sách trên màn hình
```

---

## 17. Ví dụ gắn với `loadData()`

Ví dụ:

```java
private void loadData() {
    listFull = phongRepository.getAllPhong();
    applyFilter();
}
```

Sau đó:

- `applyFilter()` lọc danh sách
- `adapter.setDanhSachPhong(filteredList)` được gọi

### Nghĩa là

- dữ liệu đi từ repository về activity
- activity xử lý lọc
- adapter nhận danh sách cuối cùng
- recycler view hiển thị

---

## 18. Những lỗi người mới hay nhầm

### Lỗi 1: Nghĩ `RecyclerView` tự hiển thị được dữ liệu

Sai.

`RecyclerView` cần:

- dữ liệu
- adapter

### Lỗi 2: Nghĩ `Adapter` là nơi lấy dữ liệu từ database

Không nên hiểu như vậy.

Thông thường:

- repository lấy dữ liệu
- activity nhận dữ liệu
- adapter chỉ lo hiển thị

### Lỗi 3: Không phân biệt file màn hình và file item

Bạn cần tách rõ:

- `activity_...xml` = cả màn hình
- `item_...xml` = một dòng trong danh sách

### Lỗi 4: Không hiểu `ViewHolder`

Bạn chỉ cần nhớ:

- `ViewHolder` là bộ control của một dòng

---

## 19. Ghi nhớ cuối buổi

Bạn chỉ cần thuộc 6 ý này:

- `RecyclerView` là nơi hiển thị danh sách
- `Adapter` là lớp trung gian đổ dữ liệu vào danh sách
- `ViewHolder` là đại diện cho một dòng
- `onCreateViewHolder()` tạo giao diện dòng
- `onBindViewHolder()` đổ dữ liệu vào dòng
- `bind(...)` là gán dữ liệu của một object lên một dòng

---

## 20. Câu hỏi tự kiểm tra

1. `RecyclerView` gần giống control nào trong WinForms?
2. `Adapter` có nhiệm vụ chính là gì?
3. `ViewHolder` là gì?
4. `onCreateViewHolder()` dùng để làm gì?
5. `onBindViewHolder()` dùng để làm gì?
6. `bind(phong)` đang xử lý việc gì?
7. Vì sao `RecyclerView` không thể hoạt động tốt nếu thiếu `Adapter`?

---

## 21. Bài tập nhỏ

### Bài 1

Hãy tự giải thích bằng lời của bạn:

- `RecyclerView` là gì
- `Adapter` là gì
- `ViewHolder` là gì

### Bài 2

Hãy chọn một adapter trong project và thử tìm:

- danh sách dữ liệu nằm ở đâu
- `setDanhSach...()` nằm ở đâu
- `onCreateViewHolder()` nằm ở đâu
- `onBindViewHolder()` nằm ở đâu
- class `ViewHolder` nằm ở đâu

### Bài 3

Hãy giải thích lại bằng lời luồng sau:

- `Repository`
- `Activity`
- `Adapter`
- `RecyclerView`

---

## 22. Chuẩn bị cho buổi sau

Sau khi hiểu bài này, bạn sẽ học tiếp:

- ghép toàn bộ luồng:
  - `XML`
  - `Activity`
  - `Repository`
  - `SQLite`
  - `RecyclerView`
- đọc trọn module `Phòng` của project
