package com.example.quanlynhatro.ui.phong;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.ui.hoa_don.DanhSachHoaDonActivity;
import com.example.quanlynhatro.ui.khach_thue.DanhSachKhachThueActivity;
import com.example.quanlynhatro.ui.thong_ke.BaoCaoActivity;
import com.example.quanlynhatro.ui.thong_ke.TongQuanActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DanhSachPhongActivity extends AppCompatActivity {

    private RecyclerView recyclerPhong;
    private BottomNavigationView bottomNavigation;
    private TextView btnBack, btnThemPhong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_phong);

        initViews();
        setupBottomNavigation();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnThemPhong = findViewById(R.id.btnThemPhong);
        recyclerPhong = findViewById(R.id.rvDanhSachPhong);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        btnBack.setOnClickListener(v -> finish());
        btnThemPhong.setOnClickListener(v -> {
            startActivity(new Intent(this, ThemSuaPhongActivity.class));
        });


        recyclerPhong.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_rooms);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(DanhSachPhongActivity.this, TongQuanActivity.class));
                    return true;
                } else if (id == R.id.nav_rooms) {
                    return true;
                } else if (id == R.id.nav_invoices) {
                    startActivity(new Intent(DanhSachPhongActivity.this, DanhSachHoaDonActivity.class));
                    return true;
                } else if (id == R.id.nav_reports) {
                    startActivity(new Intent(DanhSachPhongActivity.this, BaoCaoActivity.class));
                    return true;
                } else if (id == R.id.nav_settings) {
                    startActivity(new Intent(DanhSachPhongActivity.this, com.example.quanlynhatro.ui.setting.CaiDatActivity.class));
                    return true;
                }

                return false;
            }
        });
    }
}
