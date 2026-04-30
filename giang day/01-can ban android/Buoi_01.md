# Buoi 1 - Lam quen Android tu nen WinForms

## Muc tieu buoi hoc

Sau buoi nay, ban can hieu duoc:

- Android app khong qua xa la neu da hoc WinForms
- `Activity` trong Android gan giong `Form` trong WinForms
- `XML layout` trong Android gan giong phan giao dien thiet ke cua WinForms
- `setContentView(...)`, `findViewById(...)`, `setOnClickListener(...)` la gi
- cach doc mot man hinh Android co ban

---

## 1. Vi sao nguoi da hoc WinForms se hoc Android nhanh hon

Ban da co loi the rat lon neu da hoc WinForms, vi ban da quen voi:

- lap trinh huong su kien
- giao dien nguoi dung
- nut bam, o nhap, nhanh
- mo man hinh khac
- lam viec voi du lieu
- viet ung dung quan ly

Dieu khac chu yeu la:

- ten goi khac
- cach to chuc project khac
- Android co vong doi man hinh ro hon
- Android dung XML de mo ta giao dien
- Android co `RecyclerView`, `Adapter`, `SQLite`

Noi ngan gon:

WinForms va Android deu la kieu lap trinh:

- co giao dien
- co su kien
- co du lieu
- co xu ly nghiep vu
- co dieu huong giua cac man hinh

---

## 2. So sanh nhanh WinForms va Android

| WinForms | Android | Giai thich |
|---|---|---|
| `Form` | `Activity` | Moi cai dai dien cho mot man hinh |
| `UserControl` | `Fragment` | Thanh phan giao dien dung lai |
| `Designer` | `XML layout` | Noi mo ta giao dien |
| `Button` | `Button` / `ImageButton` | Nut bam |
| `TextBox` | `EditText` | O nhap lieu |
| `Label` | `TextView` | Hien thi chu |
| `DataGridView` | `RecyclerView` | Hien thi danh sach du lieu |
| `Form_Load` | `onCreate()` / `onResume()` | Giai doan khoi tao hoac lam moi man hinh |
| `this.Close()` | `finish()` | Dong man hinh |
| `new FormB().Show()` | `startActivity(new Intent(...))` | Mo man hinh khac |

### Cach nho quan trong

- `Form` gan giong `Activity`
- `Designer giao dien` gan giong `XML`
- `Control` trong WinForms gan giong `View` trong Android

---

## 3. Android app gom nhung phan gi

Mot man hinh Android co ban thuong co 2 phan:

### 3.1. Phan giao dien

Day la file `.xml`.

Vi du:

- `activity_main.xml`
- `activity_danh_sach_phong.xml`

No mo ta:

- man hinh co gi
- nut o dau
- o nhap nao
- mau sac, kich thuoc, vi tri

### 3.2. Phan xu ly logic

Day la file `.java`.

Vi du:

- `MainActivity.java`
- `DanhSachPhongActivity.java`

No xu ly:

- bat su kien click
- lay du lieu nhap
- mo man hinh khac
- goi database
- hien thi du lieu

---

## 4. `Activity` la gi

`Activity` la mot man hinh trong Android.

Vi du:

- man hinh danh sach phong
- man hinh them khach thue
- man hinh chi tiet hoa don

Neu so voi WinForms:

- `FrmDanhSachPhong` gan giong `DanhSachPhongActivity`
- `FrmThemKhach` gan giong `ThemSuaKhachThueActivity`

### Vi du WinForms

```csharp
public partial class FrmDanhSachPhong : Form
{
    public FrmDanhSachPhong()
    {
        InitializeComponent();
    }
}
```

### Vi du Android

```java
public class DanhSachPhongActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_phong);
    }
}
```

### Y nghia

- `Form` la man hinh ben WinForms
- `Activity` la man hinh ben Android

---

## 5. `XML layout` la gi

`XML layout` la file giao dien cua Android.

No gan giong phan thiet ke giao dien trong WinForms, chi khac la Android hay viet bang XML.

### Vi du XML don gian

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
        android:text="Danh sach phong" />

    <EditText
        android:id="@+id/etSearch"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Nhap tu khoa" />

    <Button
        android:id="@+id/btnThem"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Them phong" />
</LinearLayout>
```

### So sanh voi WinForms

- `TextView` gan giong `Label`
- `EditText` gan giong `TextBox`
- `Button` van la `Button`

---

## 6. `setContentView(...)` la gi

Day la lenh gan giao dien XML vao man hinh `Activity`.

### Vi du

```java
setContentView(R.layout.activity_danh_sach_phong);
```

### Y nghia

- Activity hien tai se dung file `activity_danh_sach_phong.xml`
- neu khong co dong nay thi Activity chua biet giao dien nao se hien thi

### So sanh voi WinForms

Trong WinForms, giao dien duoc noi voi Form qua `InitializeComponent()`.

Ban co the tam hieu:

- `InitializeComponent()` cua WinForms
- gan giong vai tro khoi tao giao dien nhu `setContentView(...)` trong Android

Luu y:
- khong hoan toan giong 100%
- nhung voi nguoi moi co the nho nhu vay de de hoc

---

## 7. `findViewById(...)` la gi

Trong Android, control nam o XML.
Muon dung control do trong Java thi phai lay no ra bang `findViewById(...)`.

### Vi du

```java
EditText etSearch = findViewById(R.id.etSearch);
Button btnThem = findViewById(R.id.btnThem);
TextView tvTitle = findViewById(R.id.tvTitle);
```

### Y nghia

- tim control theo `id`
- gan control do vao bien Java de xu ly

### So sanh voi WinForms

Trong WinForms, ban thuong co san:

```csharp
txtSearch.Text = "";
btnThem.Enabled = true;
lblTitle.Text = "Danh sach phong";
```

Con trong Android, thuong phai lam them buoc:

```java
EditText etSearch = findViewById(R.id.etSearch);
```

### Cach nho

- WinForms: control thuong co san trong code-behind
- Android: phai lay tu XML ra bang `findViewById(...)`

---

## 8. `id` trong XML dung de lam gi

Moi control trong XML thuong co mot `id`.

### Vi du

```xml
android:id="@+id/btnThem"
```

### Tac dung

- giup Java biet chinh xac control nao can lay
- tranh nham giua nhieu control tren cung man hinh

### So sanh voi WinForms

Giong nhu ten control:

- `btnThem`
- `txtSearch`
- `lblTitle`

---

## 9. Bat su kien trong Android

Trong WinForms ban hay viet:

```csharp
private void btnThem_Click(object sender, EventArgs e)
{
    MessageBox.Show("Ban vua bam nut them");
}
```

Trong Android thuong viet:

```java
Button btnThem = findViewById(R.id.btnThem);

btnThem.setOnClickListener(v -> {
    Toast.makeText(this, "Ban vua bam nut them", Toast.LENGTH_SHORT).show();
});
```

### So sanh

- `btnThem_Click` trong WinForms
- gan giong `setOnClickListener(...)` trong Android

---

## 10. Mo man hinh khac

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

### Y nghia

- tao yeu cau mo man hinh khac
- Android dung `Intent` de dieu huong

### Cach nho

- WinForms: mo Form
- Android: mo Activity bang `Intent`

---

## 11. Vi du hoan chinh rat nho

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
        android:text="Xin chao" />

    <Button
        android:id="@+id/btnClick"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Bam toi" />
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
            tvHello.setText("Ban vua bam nut");
        });
    }
}
```

### Phan tich

- `setContentView(...)`: gan giao dien
- `findViewById(...)`: lay control
- `setOnClickListener(...)`: xu ly bam nut
- `setText(...)`: doi noi dung hien thi

---

## 12. Cach doc mot man hinh Android cho nguoi moi

Khi gap mot man hinh Android, hay doc theo thu tu nay:

### Buoc 1. Xem file XML

Tu hoi:

- man hinh co nhung control nao
- co o nhap khong
- co nut nao
- co danh sach khong

### Buoc 2. Xem `setContentView(...)`

Tu hoi:

- Activity nay dang dung file XML nao

### Buoc 3. Xem `findViewById(...)`

Tu hoi:

- code Java dang lay nhung control nao ra

### Buoc 4. Xem su kien

Tu hoi:

- bam nut thi lam gi
- go chu thi co phan ung gi
- co mo man hinh khac khong

### Buoc 5. Xem du lieu

Tu hoi:

- du lieu lay tu dau
- hien thi len cho nao

---

## 13. Nhung loi nguoi moi hay nham

### Nham 1: `Form` giong `XML`

Sai.

Dung la:

- `Form` gan giong `Activity`
- `XML` chi la giao dien cua `Activity`

### Nham 2: `findViewById(...)` la khai bao control moi

Khong dung hoan toan.

No khong tao control moi.
No chi lay control da co trong XML ra de Java dung.

### Nham 3: `setContentView(...)` la mo man hinh khac

Sai.

- `setContentView(...)` chi gan giao dien cho Activity hien tai
- mo man hinh khac phai dung `Intent`

---

## 14. Ghi nho cuoi buoi

Ban chi can thuoc 6 y sau:

- `Activity` la man hinh Android
- `XML` la giao dien cua man hinh
- `setContentView(...)` la gan XML vao Activity
- `findViewById(...)` la lay control tu XML ra Java
- `setOnClickListener(...)` la bat su kien bam
- `Intent` dung de mo man hinh khac

---

## 15. Tom tat bang 1 so do

```text
XML layout
   |
   v
Activity dung setContentView(...)
   |
   v
findViewById(...)
   |
   v
lay control ra de xu ly
   |
   v
setOnClickListener(...) / setText(...) / mo man hinh khac
```

---

## 16. Cau hoi tu kiem tra

1. `Form` trong WinForms gan giong thanh phan nao trong Android?
2. `XML layout` dung de lam gi?
3. `setContentView(...)` co vai tro gi?
4. `findViewById(...)` dung de lam gi?
5. `EditText` gan giong control nao trong WinForms?
6. `TextView` gan giong control nao trong WinForms?
7. `startActivity(new Intent(...))` dung de lam gi?

---

## 17. Bai tap nho

### Bai 1
Hay tu giai thich bang loi cua ban:

- `Activity` la gi
- `XML` la gi
- `findViewById(...)` la gi

### Bai 2
Viet lai bang so sanh:

- `Form`
- `Button`
- `TextBox`
- `Label`
- `DataGridView`

voi Android tuong ung.

### Bai 3
Nhin vao mot file XML bat ky trong project, hay thu chi ra:

- dau la `Button`
- dau la `EditText`
- dau la `TextView`
- control nao co `id`

---

## 18. Chuan bi cho buoi sau

Sau khi hieu bai nay, ban se hoc tiep:

- vong doi `Activity`
- `onCreate()`
- `onResume()`
- vi sao Android phai lam moi du lieu khi quay lai man hinh
