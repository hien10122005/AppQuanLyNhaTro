package com.example.quanlynhatro.ui.hoa_don;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.HoaDonVm;
import com.example.quanlynhatro.data.repository.HoaDonRepository;
import com.example.quanlynhatro.ui.chi_so.NhapChiSoActivity;
import com.example.quanlynhatro.ui.khach_thue.DanhSachKhachThueActivity;
import com.example.quanlynhatro.ui.phong.DanhSachPhongActivity;
import com.example.quanlynhatro.ui.thong_ke.TongQuanActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * DanhSachHoaDonActivity - Màn hình Danh Sách Hóa Đơn
 * =====================================================
 * Nhiệm vụ của màn hình này:
 *   1. Hiển thị danh sách hóa đơn theo tháng (dùng RecyclerView + HoaDonAdapter)
 *   2. Cho phép lọc theo trạng thái (chips: Tất cả / Chưa thu / Đã thu / Quá hạn)
 *   3. Cho phép chuyển tháng xem (nút ‹ / ›)
 *   4. Hiển thị tổng tiền phải thu / đã thu / còn nợ của tháng
 *   5. Nhấn vào 1 hóa đơn → mở màn hình Chi Tiết
 *
 * So sánh C# WinForms:
 *   Activity ≈ Form
 *   RecyclerView ≈ DataGridView (nhưng cuộn được, linh hoạt hơn)
 *   Adapter ≈ BindingSource / DataSource
 */
public class DanhSachHoaDonActivity extends AppCompatActivity {

    // ============================================================
    // KHAI BÁO BIẾN
    // ============================================================

    private HoaDonRepository repository;    // Lớp truy vấn CSDL
    private HoaDonAdapter adapter;          // Adapter cho RecyclerView

    // --- Views ---
    private TextView tvThangNam;            // Hiển thị "Tháng 4 / 2026"
    private TextView tvTongPhaiThu;         // Tổng tiền phải thu
    private TextView tvSummaryDaThu;        // Tổng đã thu
    private TextView tvSummaryChuaThu;      // Tổng còn nợ
    private RecyclerView recyclerHoaDon;    // Danh sách cuộn

    // --- Chips lọc (giống RadioButton group) ---
    private TextView chipTatCa, chipChuaThu, chipDaThu, chipQuaHan;
    private TextView chipDangChon;          // Giữ chip đang được chọn để reset màu

    // --- Trạng thái hiện tại ---
    private int thangHienTai;              // Tháng đang xem (1-12)
    private int namHienTai;               // Năm đang xem
    private String filterTrangThai = null; // null = lấy tất cả

    // ============================================================
    // VÒNG ĐỜI: onCreate
    // ============================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_hoa_don);

        // Khởi tạo Repository (cầu nối đến Database)
        repository = new HoaDonRepository(this);

        // Lấy tháng/năm hiện tại làm mặc định
        Calendar cal = Calendar.getInstance();
        thangHienTai = cal.get(Calendar.MONTH) + 1; // +1 vì Calendar bắt đầu từ 0
        namHienTai   = cal.get(Calendar.YEAR);

        // Kết nối Views và thiết lập
        khoiTaoViews();
        thietLapRecyclerView();
        thietLapFilterChips();
        thietLapDieuHuongThang();
        thietLapBottomNavigation();

        // Kiểm tra xem có yêu cầu lọc sẵn từ màn hình khác không (vd: từ Dashboard nhấn vào "Quá hạn")
        String initialFilter = getIntent().getStringExtra("FILTER_STATUS");
        if ("QUA_HAN".equals(initialFilter)) {
            filterTrangThai = "QUA_HAN";
            chonChip(chipQuaHan);
        }

        // Tải dữ liệu lần đầu
        taiDuLieu();
    }

    // ============================================================
    // BƯỚC 1: KẾT NỐI VIEWS
    // ============================================================

    private void khoiTaoViews() {
        tvThangNam       = findViewById(R.id.tvThangNam);
        tvTongPhaiThu    = findViewById(R.id.tvTongPhaiThu);
        tvSummaryDaThu   = findViewById(R.id.tvSummaryDaThu);
        tvSummaryChuaThu = findViewById(R.id.tvSummaryChuaThu);
        recyclerHoaDon   = findViewById(R.id.recyclerHoaDon);
        chipTatCa        = findViewById(R.id.chipTatCa);
        chipChuaThu      = findViewById(R.id.chipChuaThu);
        chipDaThu        = findViewById(R.id.chipDaThu);
        chipQuaHan       = findViewById(R.id.chipQuaHan);

        // Chip mặc định đang chọn là "Tất cả"
        chipDangChon = chipTatCa;

        // Nút Quay lại
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Nút Lập hóa đơn mới
        findViewById(R.id.btnLapHoaDon).setOnClickListener(v -> {
            startActivity(new Intent(this, LapHoaDonActivity.class));
        });
    }

    // ============================================================
    // BƯỚC 2: THIẾT LẬP RECYCLERVIEW
    // ============================================================

    /**
     * Gán LayoutManager và Adapter cho RecyclerView.
     *
     * LayoutManager quyết định cách sắp xếp các item:
     *   - LinearLayoutManager → danh sách dọc (giống ListBox trong WinForms)
     *   - GridLayoutManager   → lưới nhiều cột
     */
    private void thietLapRecyclerView() {
        // LinearLayoutManager: hiển thị theo chiều dọc
        recyclerHoaDon.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Khởi tạo Adapter với danh sách rỗng trước (dữ liệu sẽ nạp sau)
        adapter = new HoaDonAdapter(this, new ArrayList<>());

        // Gán adapter vào RecyclerView
        recyclerHoaDon.setAdapter(adapter);

        // Khi người dùng nhấn vào 1 hóa đơn → mở màn hình Chi Tiết
        adapter.setOnItemClickListener(hoaDon -> {
            // Truyền ID hóa đơn sang màn hình Chi Tiết qua Intent
            // Giống như passing parameter khi mở Form con trong WinForms
            Intent intent = new Intent(this, ChiTietHoaDonActivity.class);
            intent.putExtra("hoa_don_id", hoaDon.getId()); // putExtra = gửi dữ liệu kèm theo
            startActivity(intent);
        });
    }

    // ============================================================
    // BƯỚC 3: FILTER CHIPS
    // ============================================================

    /**
     * Thiết lập sự kiện click cho 4 chip lọc trạng thái.
     * Khi chip được chọn → đổi màu + lọc lại danh sách.
     */
    private void thietLapFilterChips() {
        chipTatCa.setOnClickListener(v -> {
            chonChip(chipTatCa);
            filterTrangThai = null; // null = không lọc
            taiDuLieu();
        });

        chipChuaThu.setOnClickListener(v -> {
            chonChip(chipChuaThu);
            filterTrangThai = DatabaseHelper.TRANG_THAI_HOA_DON_CHUA_THANH_TOAN;
            taiDuLieu();
        });

        chipDaThu.setOnClickListener(v -> {
            chonChip(chipDaThu);
            filterTrangThai = DatabaseHelper.TRANG_THAI_HOA_DON_DA_THANH_TOAN;
            taiDuLieu();
        });

        chipQuaHan.setOnClickListener(v -> {
            chonChip(chipQuaHan);
            filterTrangThai = "QUA_HAN";
            taiDuLieu();
        });
    }

    /**
     * Thay đổi giao diện chip khi được chọn.
     * Chip cũ → reset về màu mờ, chip mới → nổi bật.
     */
    private void chonChip(TextView chipMoi) {
        // Reset chip cũ về màu mặc định (glass mờ)
        chipDangChon.setBackgroundResource(R.drawable.bg_glass_card);
        chipDangChon.setAlpha(0.6f); // setAlpha = độ trong suốt (0.0 = trong suốt, 1.0 = đục)

        // Đặt chip mới thành active (nổi bật)
        chipMoi.setBackgroundResource(R.drawable.bg_primary_button);
        chipMoi.setAlpha(1.0f);

        // Cập nhật chip đang chọn
        chipDangChon = chipMoi;
    }

    // ============================================================
    // BƯỚC 4: ĐIỀU HƯỚNG THÁNG (nút ‹ và ›)
    // ============================================================

    private void thietLapDieuHuongThang() {
        // Nút tháng trước
        findViewById(R.id.btnThangTruoc).setOnClickListener(v -> {
            thangHienTai--; // Lùi 1 tháng
            if (thangHienTai < 1) { // Nếu vượt qua tháng 1 → chuyển sang tháng 12 năm trước
                thangHienTai = 12;
                namHienTai--;
            }
            taiDuLieu(); // Tải lại dữ liệu cho tháng mới
        });

        // Nút tháng sau
        findViewById(R.id.btnThangSau).setOnClickListener(v -> {
            thangHienTai++; // Tiến 1 tháng
            if (thangHienTai > 12) { // Nếu vượt qua tháng 12 → chuyển sang tháng 1 năm sau
                thangHienTai = 1;
                namHienTai++;
            }
            taiDuLieu(); // Tải lại dữ liệu
        });
    }

    // ============================================================
    // BƯỚC 5: TẢI DỮ LIỆU VÀ CẬP NHẬT GIAO DIỆN
    // ============================================================

    /**
     * Hàm trung tâm: mỗi khi tháng hoặc filter thay đổi, gọi hàm này.
     * Nó sẽ:
     *   1. Cập nhật dòng tiêu đề "Tháng X / YYYY"
     *   2. Query DB lấy danh sách hóa đơn
     *   3. Query DB tính tổng tiền
     *   4. Cập nhật Adapter và Summary Card
     */
    private void taiDuLieu() {
        // --- Lấy danh sách hóa đơn từ Repository ---
        List<HoaDonVm> danhSach;
        if ("QUA_HAN".equals(filterTrangThai)) {
            // Trường hợp đặc biệt: Lọc tất cả hóa đơn quá hạn (không phụ thuộc tháng/năm đang chọn trên Toolbar)
            danhSach = repository.getDanhSachHoaDonQuaHan();
            tvThangNam.setText("Hóa đơn quá hạn (Tất cả)");
        } else {
            // --- Cập nhật tiêu đề tháng/năm ---
            tvThangNam.setText("Tháng " + thangHienTai + " / " + namHienTai);
            danhSach = repository.getDanhSachHoaDonVm(
                    thangHienTai,
                    namHienTai,
                    filterTrangThai // null = lấy tất cả, có giá trị = lọc theo trạng thái
            );
        }

        // Đưa dữ liệu mới vào Adapter → RecyclerView tự cập nhật hiển thị
        adapter.capNhatDanhSach(danhSach);

        // --- Cập nhật Empty State ---
        android.view.View layoutEmpty = findViewById(R.id.layoutEmpty);
        if (layoutEmpty != null) {
            layoutEmpty.setVisibility(danhSach.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
            recyclerHoaDon.setVisibility(danhSach.isEmpty() ? android.view.View.GONE : android.view.View.VISIBLE);
        }

        // --- Tính và hiển thị tổng tiền ---
        double[] tong = repository.tinhTongTheoThang(thangHienTai, namHienTai);
        // tong[0] = phải thu, tong[1] = đã thu, tong[2] = còn nợ

        NumberFormat fmt = NumberFormat.getNumberInstance(Locale.getDefault());
        tvTongPhaiThu.setText(fmt.format(tong[0]) + " đ");
        tvSummaryDaThu.setText(fmt.format(tong[1]) + " đ");
        tvSummaryChuaThu.setText(fmt.format(tong[2]) + " đ");
    }

    // ============================================================
    // BOTTOM NAVIGATION
    // ============================================================

    private void thietLapBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        // Đánh dấu tab "Hóa đơn" đang được chọn
        bottomNav.setSelectedItemId(R.id.nav_invoices);

        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @NonNull
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(DanhSachHoaDonActivity.this, TongQuanActivity.class));
                    return true;
                } else if (id == R.id.nav_rooms) {
                    startActivity(new Intent(DanhSachHoaDonActivity.this, DanhSachPhongActivity.class));
                    return true;
                } else if (id == R.id.nav_invoices) {
                    return true; // Đang ở trang này rồi
                } else if (id == R.id.nav_reports) {
                    startActivity(new Intent(DanhSachHoaDonActivity.this, com.example.quanlynhatro.ui.thong_ke.BaoCaoActivity.class));
                    return true;
                } else if (id == R.id.nav_settings) {
                    startActivity(new Intent(DanhSachHoaDonActivity.this, com.example.quanlynhatro.ui.setting.CaiDatActivity.class));
                    return true;
                }

                return false;
            }
        });
    }

    // ============================================================
    // VÒNG ĐỜI: onResume - Tải lại khi quay về màn hình này
    // ============================================================

    @Override
    protected void onResume() {
        super.onResume();
        // Tải lại dữ liệu khi quay về (ví dụ: vừa thu tiền xong ở màn hình khác)
        taiDuLieu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (repository != null) {
            // Đóng kết nối DB để tránh rò rỉ bộ nhớ
        }
    }
}
