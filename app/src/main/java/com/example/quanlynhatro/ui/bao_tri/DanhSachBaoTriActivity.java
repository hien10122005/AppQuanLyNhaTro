package com.example.quanlynhatro.ui.bao_tri;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;

public class DanhSachBaoTriActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_bao_tri);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new android.content.Intent(this, com.example.quanlynhatro.ui.thong_ke.TongQuanActivity.class));
                return true;
            } else if (id == R.id.nav_rooms) {
                startActivity(new android.content.Intent(this, com.example.quanlynhatro.ui.phong.DanhSachPhongActivity.class));
                return true;
            } else if (id == R.id.nav_invoices) {
                startActivity(new android.content.Intent(this, com.example.quanlynhatro.ui.hoa_don.DanhSachHoaDonActivity.class));
                return true;
            } else if (id == R.id.nav_reports) {
                startActivity(new android.content.Intent(this, com.example.quanlynhatro.ui.thong_ke.BaoCaoActivity.class));
                return true;
            } else if (id == R.id.nav_settings) {
                startActivity(new android.content.Intent(this, com.example.quanlynhatro.ui.setting.CaiDatActivity.class));
                return true;
            }
            return false;
        });
    }

}
