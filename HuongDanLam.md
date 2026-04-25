# HƯỚNG DẪN LÀM DỰ ÁN QUẢN LÝ NHÀ TRỌ
> Android Java + XML + SQLite Offline

---

## TỔNG QUAN DỰ ÁN

Dự án là ứng dụng Android giúp chủ trọ quản lý phòng, khách thuê, hợp đồng, hóa đơn điện nước, thu tiền và bảo trì — **hoàn toàn offline**.

**Stack kỹ thuật:**
- Ngôn ngữ: Java
- Giao diện: XML
- Database: SQLite (qua `SQLiteOpenHelper`)
- IDE: Android Studio

---

## TRẠNG THÁI HIỆN TẠI (Đã có sẵn)

| Thành phần | Trạng thái |
|---|---|
| `DatabaseHelper.java` | ✅ Đã có schema SQLite đầy đủ |
| Models (Phong, KhachThue, HopDong, HoaDon, ...) | ✅ Đã có |
| Repositories (PhongRepository, KhachThueRepository, ...) | ✅ Đã có |
| `MainActivity.java` (menu điều hướng) | ✅ Đã có |
| Các file Activity Java (khung rỗng) | ✅ Đã có nhưng chưa có logic |
| Các file Layout XML (khung rỗng) | ✅ Đã có nhưng chưa có UI đầy đủ |
| Adapter cho RecyclerView | ❌ Chưa có |
| Logic CRUD hoàn chỉnh | ❌ Chưa có |

---

## THỨ TỰ LÀM (Theo mức ưu tiên MVP)

```
Bước 1: Quản lý Phòng (CRUD hoàn chỉnh)
Bước 2: Quản lý Khách Thuê (CRUD hoàn chỉnh)
Bước 3: Quản lý Hợp Đồng
Bước 4: Nhập Chỉ Số Điện Nước
Bước 5: Lập Hóa Đơn & Thu Tiền
Bước 6: Dashboard / Thống Kê
Bước 7: Bảo Trì & Sự Cố
```

---

## BƯỚC 1: HOÀN THIỆN MODULE QUẢN LÝ PHÒNG

### 1.1 — Tạo Adapter cho danh sách phòng

Tạo file mới: `app/src/main/java/com/example/quanlynhatro/ui/phong/PhongAdapter.java`

```java
package com.example.quanlynhatro.ui.phong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.model.Phong;
import java.util.List;

public class PhongAdapter extends RecyclerView.Adapter<PhongAdapter.PhongViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Phong phong);
    }

    private final Context context;
    private List<Phong> danhSachPhong;
    private final OnItemClickListener listener;

    public PhongAdapter(Context context, List<Phong> danhSachPhong, OnItemClickListener listener) {
        this.context = context;
        this.danhSachPhong = danhSachPhong;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PhongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phong, parent, false);
        return new PhongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhongViewHolder holder, int position) {
        Phong phong = danhSachPhong.get(position);
        holder.tvSoPhong.setText("Phòng " + phong.getSoPhong());
        holder.tvGiaPhong.setText(phong.getGiaPhong() + " đ/tháng");
        holder.tvTrangThai.setText(phong.getTrangThai());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(phong));
    }

    @Override
    public int getItemCount() {
        return danhSachPhong.size();
    }

    public void updateData(List<Phong> newList) {
        this.danhSachPhong = newList;
        notifyDataSetChanged();
    }

    static class PhongViewHolder extends RecyclerView.ViewHolder {
        TextView tvSoPhong, tvGiaPhong, tvTrangThai;

        PhongViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSoPhong = itemView.findViewById(R.id.tvSoPhong);
            tvGiaPhong = itemView.findViewById(R.id.tvGiaPhong);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
        }
    }
}
```

### 1.2 — Cập nhật layout `item_phong.xml`

Mở `res/layout/item_phong.xml` và đảm bảo có 3 TextView: `tvSoPhong`, `tvGiaPhong`, `tvTrangThai`.

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="16dp"
    android:background="?attr/selectableItemBackground">

    <TextView
        android:id="@+id/tvSoPhong"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="18sp"
        android:textStyle="bold"
        android:text="Phòng 101" />

    <TextView
        android:id="@+id/tvGiaPhong"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="14sp"
        android:text="2,000,000 đ/tháng" />

    <TextView
        android:id="@+id/tvTrangThai"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="14sp"
        android:text="Đang thuê" />
</LinearLayout>
```

### 1.3 — Cập nhật layout `activity_danh_sach_phong.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rvDanhSachPhong"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1" />

    <Button
        android:id="@+id/btnThemPhong"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="+ Thêm Phòng"
        android:margin="16dp" />
</LinearLayout>
```

### 1.4 — Cập nhật `DanhSachPhongActivity.java`

```java
package com.example.quanlynhatro.ui.phong;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.model.Phong;
import com.example.quanlynhatro.data.repository.PhongRepository;
import java.util.List;

public class DanhSachPhongActivity extends AppCompatActivity {

    private RecyclerView rvDanhSachPhong;
    private PhongAdapter adapter;
    private PhongRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_phong);

        repository = new PhongRepository(this);
        rvDanhSachPhong = findViewById(R.id.rvDanhSachPhong);
        rvDanhSachPhong.setLayoutManager(new LinearLayoutManager(this));

        List<Phong> danhSach = repository.getAllPhong();
        adapter = new PhongAdapter(this, danhSach, phong -> {
            Intent intent = new Intent(this, ChiTietPhongActivity.class);
            intent.putExtra("phong_id", phong.getId());
            startActivity(intent);
        });
        rvDanhSachPhong.setAdapter(adapter);

        findViewById(R.id.btnThemPhong).setOnClickListener(v ->
            startActivity(new Intent(this, ThemSuaPhongActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Làm mới danh sách khi quay lại màn hình
        adapter.updateData(repository.getAllPhong());
    }
}
```

### 1.5 — Cập nhật layout `activity_them_sua_phong.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <EditText android:id="@+id/etSoPhong"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="Số phòng (vd: 101)" />

        <EditText android:id="@+id/etLoaiPhong"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="Loại phòng (vd: phòng đơn)" />

        <EditText android:id="@+id/etGiaPhong"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="Giá phòng (đ/tháng)"
            android:inputType="numberDecimal" />

        <EditText android:id="@+id/etDienTich"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="Diện tích (m²)"
            android:inputType="numberDecimal" />

        <EditText android:id="@+id/etGhiChu"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="Ghi chú" />

        <Button android:id="@+id/btnLuu"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Lưu" />
    </LinearLayout>
</ScrollView>
```

### 1.6 — Cập nhật `ThemSuaPhongActivity.java`

```java
package com.example.quanlynhatro.ui.phong;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.model.Phong;
import com.example.quanlynhatro.data.repository.PhongRepository;

public class ThemSuaPhongActivity extends AppCompatActivity {

    private EditText etSoPhong, etLoaiPhong, etGiaPhong, etDienTich, etGhiChu;
    private PhongRepository repository;
    private int phongId = -1; // -1 = thêm mới, khác -1 = chỉnh sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sua_phong);

        repository = new PhongRepository(this);
        etSoPhong   = findViewById(R.id.etSoPhong);
        etLoaiPhong = findViewById(R.id.etLoaiPhong);
        etGiaPhong  = findViewById(R.id.etGiaPhong);
        etDienTich  = findViewById(R.id.etDienTich);
        etGhiChu    = findViewById(R.id.etGhiChu);

        // Nếu có truyền phong_id thì là chỉnh sửa
        phongId = getIntent().getIntExtra("phong_id", -1);
        if (phongId != -1) {
            Phong phong = repository.getPhongById(phongId);
            if (phong != null) {
                etSoPhong.setText(phong.getSoPhong());
                etLoaiPhong.setText(phong.getLoaiPhong());
                etGiaPhong.setText(String.valueOf(phong.getGiaPhong()));
                etDienTich.setText(String.valueOf(phong.getDienTich()));
                etGhiChu.setText(phong.getGhiChu());
            }
        }

        findViewById(R.id.btnLuu).setOnClickListener(v -> luuPhong());
    }

    private void luuPhong() {
        String soPhong   = etSoPhong.getText().toString().trim();
        String loaiPhong = etLoaiPhong.getText().toString().trim();
        String giaStr    = etGiaPhong.getText().toString().trim();
        String dienStr   = etDienTich.getText().toString().trim();
        String ghiChu    = etGhiChu.getText().toString().trim();

        // Validate
        if (soPhong.isEmpty() || giaStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số phòng và giá phòng", Toast.LENGTH_SHORT).show();
            return;
        }

        Phong phong = new Phong();
        phong.setSoPhong(soPhong);
        phong.setLoaiPhong(loaiPhong);
        phong.setGiaPhong(Double.parseDouble(giaStr));
        phong.setDienTich(dienStr.isEmpty() ? 0 : Double.parseDouble(dienStr));
        phong.setGhiChu(ghiChu);
        phong.setTrangThai("trong"); // mặc định khi tạo mới

        if (phongId == -1) {
            long result = repository.insertPhong(phong);
            if (result > 0) {
                Toast.makeText(this, "Thêm phòng thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi thêm phòng", Toast.LENGTH_SHORT).show();
            }
        } else {
            phong.setId(phongId);
            int result = repository.updatePhong(phong);
            if (result > 0) {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
```

---

## BƯỚC 2: HOÀN THIỆN MODULE KHÁCH THUÊ

### 2.1 — Tạo `KhachThueAdapter.java`

Tạo file: `ui/khach_thue/KhachThueAdapter.java` — cấu trúc tương tự `PhongAdapter` nhưng dùng model `KhachThue`, layout `item_khach_thue.xml`.

### 2.2 — Cập nhật `activity_danh_sach_khach_thue.xml`

Thêm `RecyclerView` (id: `rvDanhSachKhachThue`) và `Button` (id: `btnThemKhachThue`).

### 2.3 — Cập nhật `DanhSachKhachThueActivity.java`

Cấu trúc tương tự `DanhSachPhongActivity` nhưng dùng `KhachThueRepository`.

### 2.4 — Cập nhật `activity_them_sua_khach_thue.xml`

Các trường cần có:
- `etHoTen` — Họ và tên
- `etSoDienThoai` — Số điện thoại (inputType: phone)
- `etCccd` — CCCD
- `etNgaySinh` — Ngày sinh
- `etDiaChiThuongTru` — Địa chỉ
- `btnLuu` — Nút lưu

### 2.5 — Cập nhật `ThemSuaKhachThueActivity.java`

Lấy dữ liệu từ form → tạo đối tượng `KhachThue` → gọi `KhachThueRepository.insertKhachThue()` hoặc `updateKhachThue()`.

---

## BƯỚC 3: HOÀN THIỆN MODULE HỢP ĐỒNG

### 3.1 — Cập nhật `activity_them_sua_hop_dong.xml`

Các trường:
- `spinnerPhong` — Chọn phòng (Spinner)
- `spinnerKhachThue` — Chọn khách thuê (Spinner)
- `etNgayBatDau` — Ngày bắt đầu
- `etNgayKetThuc` — Ngày kết thúc
- `etTienCoc` — Tiền cọc
- `btnLuu` — Lưu

### 3.2 — Cập nhật `ThemSuaHopDongActivity.java`

```java
// Load danh sách phòng vào Spinner
List<Phong> phongs = new PhongRepository(this).getAllPhong();
ArrayAdapter<String> phongAdapter = new ArrayAdapter<>(this,
    android.R.layout.simple_spinner_item,
    phongs.stream().map(p -> "Phòng " + p.getSoPhong()).collect(Collectors.toList()));
spinnerPhong.setAdapter(phongAdapter);

// Khi lưu: tạo HopDong, set phongId từ vị trí spinner, gọi repository
// Sau khi tạo hợp đồng thành công: cập nhật trạng thái phòng sang "dang_thue"
```

---

## BƯỚC 4: NHẬP CHỈ SỐ ĐIỆN NƯỚC

### 4.1 — Cập nhật `activity_nhap_chi_so.xml`

Các trường:
- `spinnerHopDong` — Chọn hợp đồng
- `etThang` / `etNam` — Tháng/Năm
- `etChiSoDienCu` / `etChiSoDienMoi` — Chỉ số điện cũ / mới
- `etChiSoNuocCu` / `etChiSoNuocMoi` — Chỉ số nước cũ / mới
- `btnLuu`

### 4.2 — Logic tính toán

```java
double soDien = chiSoDienMoi - chiSoDienCu;
double soNuoc = chiSoNuocMoi - chiSoNuocCu;
double tienDien = soDien * GIA_DIEN; // Lấy từ bảng bang_gia_dich_vu
double tienNuoc = soNuoc * GIA_NUOC;
```

---

## BƯỚC 5: LẬP HÓA ĐƠN & THU TIỀN

### 5.1 — Cập nhật `LapHoaDonActivity.java`

Luồng xử lý:
1. Chọn hợp đồng/phòng → tự động điền tháng/năm hiện tại
2. Lấy chỉ số điện nước từ `chi_so_dich_vu_thang` → tính tiền dịch vụ
3. Cộng tiền phòng + tiền điện + tiền nước = tổng tiền
4. Lưu vào bảng `hoa_don` + các dòng `hoa_don_chi_tiet`
5. Trạng thái mặc định: `chua_thanh_toan`

### 5.2 — Cập nhật `ThuTienActivity.java`

```java
// Hiển thị thông tin hóa đơn: tổng tiền, số tiền còn nợ
// Nhập số tiền thanh toán → ghi vào bảng thanh_toan
// Tính con_no = tong_tien - tong_da_thanh_toan
// Nếu con_no == 0 thì cập nhật hoa_don.trang_thai = 'da_thanh_toan'
```

---

## BƯỚC 6: DASHBOARD (TỔNG QUAN)

### 6.1 — Cập nhật `activity_tong_quan.xml`

Các thẻ thống kê cần hiển thị:
- Tổng phòng / Phòng đang thuê / Phòng trống
- Tổng tiền thu tháng này
- Số hóa đơn chưa thanh toán
- Hợp đồng sắp hết hạn (trong 30 ngày)

### 6.2 — Query SQL trong Repository

```java
// Ví dụ đếm phòng trống
"SELECT COUNT(*) FROM phong WHERE trang_thai = 'trong'"

// Tổng tiền thu tháng này
"SELECT SUM(so_tien) FROM thanh_toan WHERE strftime('%m-%Y', ngay_thanh_toan) = '04-2026'"

// Hóa đơn chưa thanh toán
"SELECT COUNT(*) FROM hoa_don WHERE trang_thai = 'chua_thanh_toan'"
```

---

## BƯỚC 7: BẢO TRÌ & SỰ CỐ

### 7.1 — Tạo `SuCoBaoTriAdapter.java`

Tương tự các adapter khác, dùng model `SuCoBaoTri`, layout `item_su_co.xml`.

### 7.2 — Cập nhật `ThemSuaBaoTriActivity.java`

Các trường: phòng, tiêu đề, nội dung, ngày báo, chi phí, trạng thái.

---

## CÁC LỖI THƯỜNG GẶP & CÁCH XỬ LÝ

### Lỗi 1: `NullPointerException` khi `findViewById`
**Nguyên nhân:** Layout XML không có View với id đó.  
**Cách fix:** Kiểm tra lại id trong XML khớp với id trong Java.

### Lỗi 2: `RecyclerView` không hiển thị dữ liệu
**Nguyên nhân:** Chưa set `LayoutManager` hoặc `Adapter`.  
**Cách fix:**
```java
recyclerView.setLayoutManager(new LinearLayoutManager(this));
recyclerView.setAdapter(adapter);
```

### Lỗi 3: App crash khi mở màn hình chưa đăng ký
**Nguyên nhân:** Activity chưa khai báo trong `AndroidManifest.xml`.  
**Cách fix:** Thêm vào `AndroidManifest.xml`:
```xml
<activity android:name=".ui.phong.DanhSachPhongActivity" />
<activity android:name=".ui.phong.ThemSuaPhongActivity" />
<!-- Thêm tất cả Activity mới vào đây -->
```

### Lỗi 4: Database không tìm thấy cột
**Nguyên nhân:** Thay đổi schema nhưng chưa tăng `DATABASE_VERSION`.  
**Cách fix:** Tăng `DATABASE_VERSION` lên 1 đơn vị trong `DatabaseHelper.java`.

### Lỗi 5: `Spinner` không load được dữ liệu
**Nguyên nhân:** Gọi `setAdapter` trước khi có dữ liệu.  
**Cách fix:** Đảm bảo load dữ liệu từ DB xong rồi mới tạo và set adapter.

---

## QUY TRÌNH LÀM MỖI MÀN HÌNH (Template)

Áp dụng 5 bước này cho mỗi module:

```
1. Thiết kế XML Layout (thêm các View cần thiết, đặt id rõ ràng)
2. Tạo Adapter (nếu màn hình có danh sách RecyclerView)
3. Cập nhật Activity Java (ánh xạ View, load dữ liệu, xử lý sự kiện)
4. Khai báo Activity trong AndroidManifest.xml
5. Test thử: thêm → xem danh sách → sửa → xóa
```

---

## KIỂM TRA TRƯỚC KHI NỘP

- [ ] CRUD phòng hoạt động (thêm, sửa, xóa, xem danh sách)
- [ ] CRUD khách thuê hoạt động
- [ ] Tạo được hợp đồng, phòng tự cập nhật trạng thái "đang thuê"
- [ ] Nhập được chỉ số điện nước theo tháng
- [ ] Lập được hóa đơn từ chỉ số đã nhập
- [ ] Thu tiền và cập nhật trạng thái hóa đơn
- [ ] Dashboard hiển thị số liệu thật từ database
- [ ] Không crash khi dùng thông thường
- [ ] Tất cả Activity đã khai báo trong `AndroidManifest.xml`

---

## GHI CHÚ QUAN TRỌNG

> **Luôn test sau mỗi bước hoàn thành.** Đừng code nhiều module cùng lúc mà chưa test từng cái.

> **Khi Repository đã có sẵn** (`PhongRepository`, `KhachThueRepository`...), hãy đọc kỹ các method có sẵn trước khi viết thêm để tránh trùng lặp.

> **onResume()** là nơi tốt để làm mới danh sách sau khi quay từ màn hình thêm/sửa về.

> **Dùng `Toast`** để thông báo kết quả thao tác cho người dùng (thêm thành công, lỗi, v.v.).
