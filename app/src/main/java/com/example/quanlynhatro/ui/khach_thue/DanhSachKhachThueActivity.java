package com.example.quanlynhatro.ui.khach_thue;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlynhatro.R;
import com.example.quanlynhatro.ui.hoa_don.DanhSachHoaDonActivity;
import com.example.quanlynhatro.ui.phong.DanhSachPhongActivity;
import com.example.quanlynhatro.ui.thong_ke.BaoCaoActivity;
import com.example.quanlynhatro.ui.thong_ke.TongQuanActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DanhSachKhachThueActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_khach_thue);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        // Since Khach Thue is not a main tab, we don't highlight any item by default
        // or we can highlight the one that led here. 
        // For now, let's not select anything or select the one most relevant.
        
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(DanhSachKhachThueActivity.this, TongQuanActivity.class));
                    return true;
                } else if (id == R.id.nav_rooms) {
                    startActivity(new Intent(DanhSachKhachThueActivity.this, DanhSachPhongActivity.class));
                    return true;
                } else if (id == R.id.nav_invoices) {
                    startActivity(new Intent(DanhSachKhachThueActivity.this, DanhSachHoaDonActivity.class));
                    return true;
                } else if (id == R.id.nav_reports) {
                    startActivity(new Intent(DanhSachKhachThueActivity.this, BaoCaoActivity.class));
                    return true;
                } else if (id == R.id.nav_settings) {
                    startActivity(new Intent(DanhSachKhachThueActivity.this, com.example.quanlynhatro.ui.setting.CaiDatActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
}
