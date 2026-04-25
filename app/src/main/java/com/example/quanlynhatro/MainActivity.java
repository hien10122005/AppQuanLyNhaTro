package com.example.quanlynhatro;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.ui.bao_tri.DanhSachBaoTriActivity;
import com.example.quanlynhatro.ui.chi_so.NhapChiSoActivity;
import com.example.quanlynhatro.ui.hoa_don.DanhSachHoaDonActivity;
import com.example.quanlynhatro.ui.hoa_don.LapHoaDonActivity;
import com.example.quanlynhatro.ui.hoa_don.ThuTienActivity;
import com.example.quanlynhatro.ui.hop_dong.DanhSachHopDongActivity;
import com.example.quanlynhatro.ui.khach_thue.DanhSachKhachThueActivity;
import com.example.quanlynhatro.ui.phong.DanhSachPhongActivity;
import com.example.quanlynhatro.ui.thong_ke.TongQuanActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DatabaseHelper(this).getWritableDatabase().close();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupBottomNavigation();
        setupQuickActions();
        loadDashboardStats(); // ← Mới: nạp số liệu thực tế từ DB
    }

    private void setupBottomNavigation() {
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Cuộn lên đầu trang khi nhấn nút Home
                ScrollView scrollView = findViewById(R.id.scrollViewMain);
                if (scrollView != null) scrollView.smoothScrollTo(0, 0);
                return true;
            } else if (itemId == R.id.nav_rooms) {
                startActivity(new Intent(this, DanhSachPhongActivity.class));
                return true;
            } else if (itemId == R.id.nav_invoices) {
                startActivity(new Intent(this, DanhSachHoaDonActivity.class));
                return true;
            } else if (itemId == R.id.nav_reports) {
                startActivity(new Intent(this, TongQuanActivity.class));
                return true;
            } else if (itemId == R.id.nav_settings) {
                Toast.makeText(this, "⚙️ Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void setupQuickActions() {
        findViewById(R.id.btnQuickMeter).setOnClickListener(v -> 
            startActivity(new Intent(this, NhapChiSoActivity.class)));
        
        findViewById(R.id.btnQuickTenant).setOnClickListener(v -> 
            startActivity(new Intent(this, DanhSachKhachThueActivity.class)));
            
        findViewById(R.id.btnQuickInvoice).setOnClickListener(v -> 
            startActivity(new Intent(this, LapHoaDonActivity.class)));
    }

    /**
     * Tải số liệu thống kê từ database và hiển thị lên Dashboard.
     * Thực hiện 3 câu query đơn giản: đếm phòng trống, tỚng doanh thu, đếm hóa đơn chưa thanh toán.
     */
    private void loadDashboardStats() {
        TextView tvPhongTrong = findViewById(R.id.tvPhongTrong);
        TextView tvDoanhThu   = findViewById(R.id.tvDoanhThu);
        TextView tvHoaDonCho  = findViewById(R.id.tvHoaDonCho);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            // --- 1. Đếm phòng trống ---
            Cursor c1 = db.rawQuery(
                "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_PHONG +
                " WHERE " + DatabaseHelper.COL_PHONG_TRANG_THAI + " = ?",
                new String[]{ DatabaseHelper.TRANG_THAI_PHONG_TRONG }
            );
            if (c1.moveToFirst()) {
                tvPhongTrong.setText(String.valueOf(c1.getInt(0)));
            }
            c1.close();

            // --- 2. Tổng tiền đã thu trong tháng hiện tại (FORMAT: 24M, 1.2T...) ---
            java.util.Calendar cal = java.util.Calendar.getInstance();
            int thang = cal.get(java.util.Calendar.MONTH) + 1;
            int nam   = cal.get(java.util.Calendar.YEAR);

            Cursor c2 = db.rawQuery(
                "SELECT SUM(" + DatabaseHelper.COL_HOA_DON_DA_THANH_TOAN + ") FROM "
                + DatabaseHelper.TABLE_HOA_DON
                + " WHERE " + DatabaseHelper.COL_HOA_DON_THANG + " = ?"
                + " AND "   + DatabaseHelper.COL_HOA_DON_NAM   + " = ?",
                new String[]{ String.valueOf(thang), String.valueOf(nam) }
            );
            if (c2.moveToFirst()) {
                double tongThu = c2.getDouble(0);
                tvDoanhThu.setText(formatTien(tongThu));
            } else {
                tvDoanhThu.setText("0");
            }
            c2.close();

            // --- 3. Đếm hóa đơn chưa thanh toán ---
            Cursor c3 = db.rawQuery(
                "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_HOA_DON +
                " WHERE " + DatabaseHelper.COL_HOA_DON_TRANG_THAI + " != ?",
                new String[]{ DatabaseHelper.TRANG_THAI_HOA_DON_DA_THANH_TOAN }
            );
            if (c3.moveToFirst()) {
                int soHoaDon = c3.getInt(0);
                tvHoaDonCho.setText(soHoaDon == 0
                    ? "✔ Tất cả đã thanh toán"
                    : soHoaDon + " hóa đơn chưa thanh toán"
                );
            }
            c3.close();

        } finally {
            db.close();
        }
    }

    /**
     * Định dạng số tiền cho gọn: 1.500.000 → "1.5M", 1.000.000.000 → "1T"
     */
    private String formatTien(double soTien) {
        if (soTien >= 1_000_000_000) {
            return String.format("%.1fT", soTien / 1_000_000_000);
        } else if (soTien >= 1_000_000) {
            return String.format("%.1fM", soTien / 1_000_000);
        } else if (soTien >= 1_000) {
            return String.format("%.0fK", soTien / 1_000);
        } else {
            return String.format("%.0f", soTien);
        }
    }
}
