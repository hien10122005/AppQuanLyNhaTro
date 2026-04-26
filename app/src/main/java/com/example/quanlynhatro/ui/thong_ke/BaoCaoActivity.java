package com.example.quanlynhatro.ui.thong_ke;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.ui.khach_thue.DanhSachKhachThueActivity;
import com.example.quanlynhatro.ui.phong.DanhSachPhongActivity;
import com.example.quanlynhatro.ui.hoa_don.DanhSachHoaDonActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BaoCaoActivity extends AppCompatActivity {

    private Spinner spinnerThang, spinnerNam;
    private TextView tvTongDoanhThu, tvDaThu, tvConNo, tvTiLeLapDay, tvPhongTrong;
    private ProgressBar pbTiLeThuTien;
    private LinearLayout layoutNguonThu;
    private ImageButton btnBack;

    private DatabaseHelper dbHelper;
    private DecimalFormat formatter = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_cao);

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupFilters();
        setupEvents();
        setupBottomNavigation();
        
        // Tải dữ liệu ban đầu
        loadBaoCao();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        spinnerThang = findViewById(R.id.spinnerThang);
        spinnerNam = findViewById(R.id.spinnerNam);
        tvTongDoanhThu = findViewById(R.id.tvTongDoanhThu);
        tvDaThu = findViewById(R.id.tvDaThu);
        tvConNo = findViewById(R.id.tvConNo);
        tvTiLeLapDay = findViewById(R.id.tvTiLeLapDay);
        tvPhongTrong = findViewById(R.id.tvPhongTrong);
        pbTiLeThuTien = findViewById(R.id.pbTiLeThuTien);
        layoutNguonThu = findViewById(R.id.layoutNguonThu);
    }

    private void setupFilters() {
        // Spinner Tháng
        String[] thangs = new String[12];
        for (int i = 0; i < 12; i++) thangs[i] = "Tháng " + (i + 1);
        ArrayAdapter<String> adapterThang = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, thangs);
        adapterThang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerThang.setAdapter(adapterThang);

        // Spinner Năm
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        List<String> nams = new ArrayList<>();
        for (int i = currentYear - 2; i <= currentYear + 1; i++) nams.add(String.valueOf(i));
        ArrayAdapter<String> adapterNam = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nams);
        spinnerNam.setAdapter(adapterNam);

        // Mặc định chọn hiện tại
        spinnerThang.setSelection(cal.get(Calendar.MONTH));
        spinnerNam.setSelection(2); // Vị trí của currentYear (vì i chạy từ currentYear-2)
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadBaoCao();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerThang.setOnItemSelectedListener(listener);
        spinnerNam.setOnItemSelectedListener(listener);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_reports);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new android.content.Intent(BaoCaoActivity.this, TongQuanActivity.class));
                    return true;
                } else if (id == R.id.nav_rooms) {
                    startActivity(new android.content.Intent(BaoCaoActivity.this, DanhSachPhongActivity.class));
                    return true;
                } else if (id == R.id.nav_invoices) {
                    startActivity(new android.content.Intent(BaoCaoActivity.this, DanhSachHoaDonActivity.class));
                    return true;
                } else if (id == R.id.nav_reports) {
                    return true;
                } else if (id == R.id.nav_settings) {
                    startActivity(new android.content.Intent(BaoCaoActivity.this, com.example.quanlynhatro.ui.setting.CaiDatActivity.class));
                    return true;
                }

                return false;
            }
        });
    }

    private void loadBaoCao() {
        int thang = spinnerThang.getSelectedItemPosition() + 1;
        int nam = Integer.parseInt(spinnerNam.getSelectedItem().toString());

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 1. Thống kê tài chính
        Cursor curTien = db.rawQuery(
                "SELECT SUM(" + DatabaseHelper.COL_HOA_DON_TONG_TIEN + "), "
                + "SUM(" + DatabaseHelper.COL_HOA_DON_DA_THANH_TOAN + "), "
                + "SUM(" + DatabaseHelper.COL_HOA_DON_CON_NO + ") "
                + "FROM " + DatabaseHelper.TABLE_HOA_DON
                + " WHERE " + DatabaseHelper.COL_HOA_DON_THANG + "=? AND " + DatabaseHelper.COL_HOA_DON_NAM + "=?",
                new String[]{String.valueOf(thang), String.valueOf(nam)}
        );

        double tong = 0, daThu = 0, conNo = 0;
        if (curTien.moveToFirst()) {
            tong = curTien.getDouble(0);
            daThu = curTien.getDouble(1);
            conNo = curTien.getDouble(2);
        }
        curTien.close();

        tvTongDoanhThu.setText(formatter.format(tong) + "đ");
        tvDaThu.setText(formatter.format(daThu) + "đ");
        tvConNo.setText(formatter.format(conNo) + "đ");
        if (tong > 0) {
            pbTiLeThuTien.setProgress((int) (daThu * 100 / tong));
        } else {
            pbTiLeThuTien.setProgress(0);
        }

        // 2. Thống kê phòng
        Cursor curPhong = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_PHONG, null);
        int tongPhong = 0; if (curPhong.moveToFirst()) tongPhong = curPhong.getInt(0);
        curPhong.close();

        Cursor curTrong = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_PHONG 
                + " WHERE " + DatabaseHelper.COL_PHONG_TRANG_THAI + "=?", new String[]{DatabaseHelper.TRANG_THAI_PHONG_TRONG});
        int phongTrong = 0; if (curTrong.moveToFirst()) phongTrong = curTrong.getInt(0);
        curTrong.close();

        tvPhongTrong.setText(String.valueOf(phongTrong));
        if (tongPhong > 0) {
            int lapDay = (int) ((tongPhong - phongTrong) * 100 / tongPhong);
            tvTiLeLapDay.setText(lapDay + "%");
        } else {
            tvTiLeLapDay.setText("0%");
        }

        // 3. Chi tiết nguồn thu (Breakdown by Service Type)
        layoutNguonThu.removeAllViews();
        Cursor curBreakdown = db.rawQuery(
                "SELECT ct." + DatabaseHelper.COL_HOA_DON_CT_TEN_MUC_PHI + ", SUM(ct." + DatabaseHelper.COL_HOA_DON_CT_THANH_TIEN + ") "
                + "FROM " + DatabaseHelper.TABLE_HOA_DON_CHI_TIET + " ct "
                + "JOIN " + DatabaseHelper.TABLE_HOA_DON + " h ON ct." + DatabaseHelper.COL_HOA_DON_CT_HOA_DON_ID + " = h." + DatabaseHelper.COL_ID
                + " WHERE h." + DatabaseHelper.COL_HOA_DON_THANG + "=? AND h." + DatabaseHelper.COL_HOA_DON_NAM + "=? "
                + "GROUP BY ct." + DatabaseHelper.COL_HOA_DON_CT_TEN_MUC_PHI
                + " ORDER BY SUM(ct." + DatabaseHelper.COL_HOA_DON_CT_THANH_TIEN + ") DESC",
                new String[]{String.valueOf(thang), String.valueOf(nam)}
        );

        while (curBreakdown.moveToNext()) {
            String ten = curBreakdown.getString(0);
            double tien = curBreakdown.getDouble(1);
            
            View row = LayoutInflater.from(this).inflate(R.layout.item_nguon_thu_row, layoutNguonThu, false);
            TextView tvTen = row.findViewById(R.id.tvTenNguonThu);
            TextView tvGiaTri = row.findViewById(R.id.tvGiaTriNguonThu);
            ProgressBar pb = row.findViewById(R.id.pbNguonThu);

            tvTen.setText(ten);
            tvGiaTri.setText(formatter.format(tien) + "đ");
            if (tong > 0) {
                pb.setProgress((int) (tien * 100 / tong));
            } else {
                pb.setProgress(0);
            }
            layoutNguonThu.addView(row);
        }
        curBreakdown.close();
    }
}
