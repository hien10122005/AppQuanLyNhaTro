package com.example.quanlynhatro.ui.setting;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CaiDatActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView btnEditProfile;
    private LinearLayout itemConfigService, itemManageStaff, itemNotifications, itemExportExcel, itemChangePassword, itemLogout;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_dat);


        initViews();
        setupEvents();
        setupBottomNavigation();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        itemConfigService = findViewById(R.id.itemConfigService);
        itemManageStaff = findViewById(R.id.itemManageStaff);
        itemNotifications = findViewById(R.id.itemNotifications);
        itemExportExcel = findViewById(R.id.itemExportExcel);
        itemChangePassword = findViewById(R.id.itemChangePassword);
        itemLogout = findViewById(R.id.itemLogout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set active item in bottom nav
        bottomNavigationView.setSelectedItemId(R.id.nav_settings); 
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        btnEditProfile.setOnClickListener(v -> 
            Toast.makeText(this, "Tính năng Chỉnh sửa đang được phát triển", Toast.LENGTH_SHORT).show()
        );

        itemConfigService.setOnClickListener(v -> 
            Toast.makeText(this, "Cấu hình dịch vụ", Toast.LENGTH_SHORT).show()
        );

        itemManageStaff.setOnClickListener(v -> 
            Toast.makeText(this, "Quản lý nhân viên", Toast.LENGTH_SHORT).show()
        );

        itemNotifications.setOnClickListener(v -> 
            Toast.makeText(this, "Cài đặt thông báo", Toast.LENGTH_SHORT).show()
        );

        itemExportExcel.setOnClickListener(v -> 
            Toast.makeText(this, "Xuất dữ liệu Excel", Toast.LENGTH_SHORT).show()
        );

        itemChangePassword.setOnClickListener(v -> 
            Toast.makeText(this, "Đổi mật khẩu", Toast.LENGTH_SHORT).show()
        );

        itemLogout.setOnClickListener(v -> 
            Toast.makeText(this, "Đăng xuất tài khoản", Toast.LENGTH_SHORT).show()
        );
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                finish();
                return true;
            } else if (id == R.id.nav_rooms) {
                // To be implemented by user
                return true;
            } else if (id == R.id.nav_invoices) {
                // To be implemented by user
                return true;
            } else if (id == R.id.nav_reports) {
                // To be implemented by user
                return true;
            } else if (id == R.id.nav_settings) {
                return true;
            }
            return false;
        });
    }
}

