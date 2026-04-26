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
    private TextView btnEditProfile, tvUserName;
    private LinearLayout itemConfigService, itemManageTenants, itemManageContracts, itemManageMaintenance, itemNotifications, itemExportExcel, itemChangePassword, itemLogout;
    private com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigationView;

    private android.content.SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_dat);

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

        initViews();
        setupEvents();
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshProfileData();
    }

    private void refreshProfileData() {
        String fullName = sharedPreferences.getString("fullName", "Hien Phan");
        tvUserName.setText(fullName);
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        tvUserName = findViewById(R.id.tvUserName);
        itemConfigService = findViewById(R.id.itemConfigService);
        itemManageTenants = findViewById(R.id.itemManageTenants);
        itemManageContracts = findViewById(R.id.itemManageContracts);
        itemManageMaintenance = findViewById(R.id.itemManageMaintenance);
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

        btnEditProfile.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, ChinhSuaHoSoActivity.class);
            startActivity(intent);
        });

        itemConfigService.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, CauHinhGiaDichVuActivity.class);
            startActivity(intent);
        });


        itemManageTenants.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, com.example.quanlynhatro.ui.khach_thue.DanhSachKhachThueActivity.class);
            startActivity(intent);
        });

        itemManageContracts.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, com.example.quanlynhatro.ui.hop_dong.DanhSachHopDongActivity.class);
            startActivity(intent);
        });

        itemManageMaintenance.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, com.example.quanlynhatro.ui.bao_tri.DanhSachBaoTriActivity.class);
            startActivity(intent);
        });


        itemNotifications.setOnClickListener(v -> 
            Toast.makeText(this, "Cài đặt thông báo", Toast.LENGTH_SHORT).show()
        );

        itemExportExcel.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, XuatExcelActivity.class);
            startActivity(intent);
        });

        itemChangePassword.setOnClickListener(v -> {

            android.content.Intent intent = new android.content.Intent(this, DoiMatKhauActivity.class);
            startActivity(intent);
        });

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
                startActivity(new android.content.Intent(this, com.example.quanlynhatro.ui.phong.DanhSachPhongActivity.class));
                return true;
            } else if (id == R.id.nav_invoices) {
                startActivity(new android.content.Intent(this, com.example.quanlynhatro.ui.hoa_don.DanhSachHoaDonActivity.class));
                return true;
            } else if (id == R.id.nav_reports) {
                startActivity(new android.content.Intent(this, com.example.quanlynhatro.ui.thong_ke.BaoCaoActivity.class));
                return true;
            } else if (id == R.id.nav_settings) {

                return true;
            }
            return false;
        });
    }
}

