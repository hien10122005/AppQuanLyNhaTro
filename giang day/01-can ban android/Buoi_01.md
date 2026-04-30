# Buổi 1 - Làm quen Android từ nền WinForms

## Mục tiêu buổi học

Sau buổi này, bạn cần hiểu được:

- Android app không quá xa lạ nếu đã học WinForms
- `Activity` trong Android gần giống `Form` trong WinForms
- `XML layout` trong Android gần giống phần giao diện thiết kế của WinForms
- `setContentView(...)`, `findViewById(...)`, `setOnClickListener(...)` là gì
- cách đọc một màn hình Android cơ bản

---

## 1. Vì sao người đã học WinForms sẽ học Android nhanh hơn

Bạn đã có lợi thế rất lớn nếu đã học WinForms, vì bạn đã quen với:

- lập trình hướng sự kiện
- giao diện người dùng
- nút bấm, ô nhập, nhãn
- mở màn hình khác
- làm việc với dữ liệu
- viết ứng dụng quản lý

Điều khác chủ yếu là:

- tên gọi khác
- cách tổ chức project khác
- Android có vòng đời màn hình rõ hơn
- Android dùng XML để mô tả giao diện
- Android có `RecyclerView`, `Adapter`, `SQLite`

Nói ngắn gọn:

WinForms và Android đều là kiểu lập trình:

- có giao diện
- có sự kiện
- có dữ liệu
- có xử lý nghiệp vụ
- có điều hướng giữa các màn hình

---

## 2. So sánh nhanh WinForms và Android

Để dễ đọc trên nhiều editor, ta viết theo dạng danh sách thay vì bảng:

- WinForms: `Form`
  Android: `Activity`
  Giải thích: mỗi cái đại diện cho một màn hình

- WinForms: `UserControl`
  Android: `Fragment`
  Giải thích: thành phần giao diện dùng lại

- WinForms: `Designer`
  Android: `XML layout`
  Giải thích: nơi mô tả giao diện

- WinForms: `Button`
  Android: `Button` / `ImageButton`
  Giải thích: nút bấm

- WinForms: `TextBox`
  Android: `EditText`
  Giải thích: ô nhập liệu

- WinForms: `Label`
  Android: `TextView`
  Giải thích: hiển thị chữ

- WinForms: `DataGridView`
  Android: `RecyclerView`
  Giải thích: hiển thị danh sách dữ liệu

- WinForms: `Form_Load`
  Android: `onCreate()` / `onResume()`
  Giải thích: giai đoạn khởi tạo hoặc làm mới màn hình

- WinForms: `this.Close()`
  Android: `finish()`
  Giải thích: đóng màn hình

- WinForms: `new FormB().Show()`
  Android: `startActivity(new Intent(...))`
  Giải thích: mở màn hình khác

### Cách nhớ quan trọng

- `Form` gần giống `Activity`
- `Designer giao diện` gần giống `XML`
- `Control` trong WinForms gần giống `View` trong Android

---

## 3. Android app gồm những phần gì

Một màn hình Android cơ bản thường có 2 phần:

### 3.1. Phần giao diện

Đây là file `.xml`.

Ví dụ:

- `activity_main.xml`
- `activity_danh_sach_phong.xml`

Nó mô tả:

- màn hình có gì
- nút ở đâu
- ô nhập nào
- màu sắc, kích thước, vị trí

### 3.2. Phần xử lý logic

Đây là file `.java`.

Ví dụ:

- `MainActivity.java`
- `DanhSachPhongActivity.java`

Nó xử lý:

- bắt sự kiện click
- lấy dữ liệu nhập
- mở màn hình khác
- gọi database
- hiển thị dữ liệu

---

## 4. `Activity` là gì

`Activity` là một màn hình trong Android.

Ví dụ:

- màn hình danh sách phòng
- màn hình thêm khách thuê
- màn hình chi tiết hóa đơn

Nếu so với WinForms:

- `FrmDanhSachPhong` gần giống `DanhSachPhongActivity`
- `FrmThemKhach` gần giống `ThemSuaKhachThueActivity`

### Ví dụ WinForms

```csharp
public partial class FrmDanhSachPhong : Form
{
    public FrmDanhSachPhong()
    {
        InitializeComponent();
    }
}
```

### Ví dụ Android

```java
public class DanhSachPhongActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_phong);
    }
}
```

### Ý nghĩa

- `Form` là màn hình bên WinForms
- `Activity` là màn hình bên Android

---

## 5. `XML layout` là gì

`XML layout` là file giao diện của Android.

Nó gần giống phần thiết kế giao diện trong WinForms, chỉ khác là Android hay viết bằng XML.

### Ví dụ XML đơn giản

```xml
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <TextView
        android:id="@+id/tvTitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Danh sách phòng" />

    <EditText
        android:id="@+id/etSearch"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Nhập từ khóa" />

    <Button
        android:id="@+id/btnThem"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Thêm phòng" />
</LinearLayout>
```

### So sánh với WinForms

- `TextView` gần giống `Label`
- `EditText` gần giống `TextBox`
- `Button` vẫn là `Button`

---

## 6. `setContentView(...)` là gì

Đây là lệnh gắn giao diện XML vào màn hình `Activity`.

### Ví dụ

```java
setContentView(R.layout.activity_danh_sach_phong);
```

### Ý nghĩa

- Activity hiện tại sẽ dùng file `activity_danh_sach_phong.xml`
- nếu không có dòng này thì Activity chưa biết giao diện nào sẽ hiển thị

### So sánh với WinForms

Trong WinForms, giao diện được nối với Form qua `InitializeComponent()`.

Bạn có thể tạm hiểu:

- `InitializeComponent()` của WinForms
- gần giống vai trò khởi tạo giao diện như `setContentView(...)` trong Android

Lưu ý:

- không hoàn toàn giống 100%
- nhưng với người mới có thể nhớ như vậy để dễ học

---

## 7. `findViewById(...)` là gì

Trong Android, control nằm ở XML.
Muốn dùng control đó trong Java thì phải lấy nó ra bằng `findViewById(...)`.

### Ví dụ

```java
EditText etSearch = findViewById(R.id.etSearch);
Button btnThem = findViewById(R.id.btnThem);
TextView tvTitle = findViewById(R.id.tvTitle);
```

### Ý nghĩa

- tìm control theo `id`
- gán control đó vào biến Java để xử lý

### So sánh với WinForms

Trong WinForms, bạn thường có sẵn:

```csharp
txtSearch.Text = "";
btnThem.Enabled = true;
lblTitle.Text = "Danh sách phòng";
```

Còn trong Android, thường phải làm thêm bước:

```java
EditText etSearch = findViewById(R.id.etSearch);
```

### Cách nhớ

- WinForms: control thường có sẵn trong code-behind
- Android: phải lấy từ XML ra bằng `findViewById(...)`

---

## 8. `id` trong XML dùng để làm gì

Mỗi control trong XML thường có một `id`.

### Ví dụ

```xml
android:id="@+id/btnThem"
```

### Tác dụng

- giúp Java biết chính xác control nào cần lấy
- tránh nhầm giữa nhiều control trên cùng màn hình

### So sánh với WinForms

Giống như tên control:

- `btnThem`
- `txtSearch`
- `lblTitle`

---

## 9. Bắt sự kiện trong Android

Trong WinForms bạn hay viết:

```csharp
private void btnThem_Click(object sender, EventArgs e)
{
    MessageBox.Show("Bạn vừa bấm nút thêm");
}
```

Trong Android thường viết:

```java
Button btnThem = findViewById(R.id.btnThem);

btnThem.setOnClickListener(v -> {
    Toast.makeText(this, "Bạn vừa bấm nút thêm", Toast.LENGTH_SHORT).show();
});
```

### So sánh

- `btnThem_Click` trong WinForms
- gần giống `setOnClickListener(...)` trong Android

---

## 10. Mở màn hình khác

### WinForms

```csharp
FrmThemPhong f = new FrmThemPhong();
f.Show();
```

### Android

```java
Intent intent = new Intent(this, ThemSuaPhongActivity.class);
startActivity(intent);
```

### Ý nghĩa

- tạo yêu cầu mở màn hình khác
- Android dùng `Intent` để điều hướng

### Cách nhớ

- WinForms: mở Form
- Android: mở Activity bằng `Intent`

---

## 11. Ví dụ hoàn chỉnh rất nhỏ

### XML

```xml
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <TextView
        android:id="@+id/tvHello"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Xin chào" />

    <Button
        android:id="@+id/btnClick"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Bấm tôi" />
</LinearLayout>
```

### Java

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvHello = findViewById(R.id.tvHello);
        Button btnClick = findViewById(R.id.btnClick);

        btnClick.setOnClickListener(v -> {
            tvHello.setText("Bạn vừa bấm nút");
        });
    }
}
```

### Phân tích

- `setContentView(...)`: gắn giao diện
- `findViewById(...)`: lấy control
- `setOnClickListener(...)`: xử lý bấm nút
- `setText(...)`: đổi nội dung hiển thị

---

## 12. Cách đọc một màn hình Android cho người mới

Khi gặp một màn hình Android, hãy đọc theo thứ tự này:

### Bước 1. Xem file XML

Tự hỏi:

- màn hình có những control nào
- có ô nhập không
- có nút nào
- có danh sách không

### Bước 2. Xem `setContentView(...)`

Tự hỏi:

- Activity này đang dùng file XML nào

### Bước 3. Xem `findViewById(...)`

Tự hỏi:

- code Java đang lấy những control nào ra

### Bước 4. Xem sự kiện

Tự hỏi:

- bấm nút thì làm gì
- gõ chữ thì có phản ứng gì
- có mở màn hình khác không

### Bước 5. Xem dữ liệu

Tự hỏi:

- dữ liệu lấy từ đâu
- hiển thị lên chỗ nào

---

## 13. Những lỗi người mới hay nhầm

### Nhầm 1: `Form` giống `XML`

Sai.

Đúng là:

- `Form` gần giống `Activity`
- `XML` chỉ là giao diện của `Activity`

### Nhầm 2: `findViewById(...)` là khai báo control mới

Không đúng hoàn toàn.

Nó không tạo control mới.
Nó chỉ lấy control đã có trong XML ra để Java dùng.

### Nhầm 3: `setContentView(...)` là mở màn hình khác

Sai.

- `setContentView(...)` chỉ gắn giao diện cho Activity hiện tại
- mở màn hình khác phải dùng `Intent`

---

## 14. Ghi nhớ cuối buổi

Bạn chỉ cần thuộc 6 ý sau:

- `Activity` là màn hình Android
- `XML` là giao diện của màn hình
- `setContentView(...)` là gắn XML vào Activity
- `findViewById(...)` là lấy control từ XML ra Java
- `setOnClickListener(...)` là bắt sự kiện bấm
- `Intent` dùng để mở màn hình khác

---

## 15. Tóm tắt bằng 1 sơ đồ

```text
XML layout
   |
   v
Activity dùng setContentView(...)
   |
   v
findViewById(...)
   |
   v
lấy control ra để xử lý
   |
   v
setOnClickListener(...) / setText(...) / mở màn hình khác
```

---

## 16. Câu hỏi tự kiểm tra

1. `Form` trong WinForms gần giống thành phần nào trong Android?
2. `XML layout` dùng để làm gì?
3. `setContentView(...)` có vai trò gì?
4. `findViewById(...)` dùng để làm gì?
5. `EditText` gần giống control nào trong WinForms?
6. `TextView` gần giống control nào trong WinForms?
7. `startActivity(new Intent(...))` dùng để làm gì?

---

## 17. Bài tập nhỏ

### Bài 1

Hãy tự giải thích bằng lời của bạn:

- `Activity` là gì
- `XML` là gì
- `findViewById(...)` là gì

### Bài 2

Viết lại bảng so sánh:

- `Form`
- `Button`
- `TextBox`
- `Label`
- `DataGridView`

với Android tương ứng.

### Bài 3

Nhìn vào một file XML bất kỳ trong project, hãy thử chỉ ra:

- đâu là `Button`
- đâu là `EditText`
- đâu là `TextView`
- control nào có `id`

---

## 18. Chuẩn bị cho buổi sau

Sau khi hiểu bài này, bạn sẽ học tiếp:

- vòng đời `Activity`
- `onCreate()`
- `onResume()`
- vì sao Android phải làm mới dữ liệu khi quay lại màn hình
