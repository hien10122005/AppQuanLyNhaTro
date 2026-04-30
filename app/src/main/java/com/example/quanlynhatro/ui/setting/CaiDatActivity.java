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

        // Khởi tạo SharedPreferences để lưu trữ thông tin cấu hình người dùng
        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

        initViews();            // Ánh xạ các View
        setupEvents();          // Cài đặt sự kiện click cho các mục cài đặt
        setupBottomNavigation(); // Cài đặt thanh điều hướng bên dưới
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshProfileData();
    }

    /**
     * Cập nhật lại thông tin hồ sơ (tên người dùng) từ SharedPreferences
     */
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

    /**
     * Thiết lập các sự kiện khi người dùng click vào các mục trong menu cài đặt
     */
    private void setupEvents() {
        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Đi tới màn hình chỉnh sửa hồ sơ
        btnEditProfile.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, ChinhSuaHoSoActivity.class);
            startActivity(intent);
        });

        // Cấu hình giá điện, nước, internet, rác
        itemConfigService.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, CauHinhGiaDichVuActivity.class);
            startActivity(intent);
        });

        // Quản lý khách thuê
        itemManageTenants.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, com.example.quanlynhatro.ui.khach_thue.DanhSachKhachThueActivity.class);
            startActivity(intent);
        });

        // Quản lý hợp đồng
        itemManageContracts.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, com.example.quanlynhatro.ui.hop_dong.DanhSachHopDongActivity.class);
            startActivity(intent);
        });

        // Quản lý bảo trì, sửa chữa
        itemManageMaintenance.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, com.example.quanlynhatro.ui.bao_tri.DanhSachBaoTriActivity.class);
            startActivity(intent);
        });

        // Cài đặt thông báo
        itemNotifications.setOnClickListener(v -> 
            Toast.makeText(this, "Cài đặt thông báo", Toast.LENGTH_SHORT).show()
        );

        // Xuất dữ liệu ra file Excel
        itemExportExcel.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, XuatExcelActivity.class);
            startActivity(intent);
        });

        // Thay đổi mật khẩu đăng nhập
        itemChangePassword.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, DoiMatKhauActivity.class);
            startActivity(intent);
        });

        // Đăng xuất khỏi ứng dụng
        itemLogout.setOnClickListener(v -> 
            Toast.makeText(this, "Đăng xuất tài khoản", Toast.LENGTH_SHORT).show()
        );
    }

    /**
     * Cài đặt thanh Bottom Navigation để chuyển đổi giữa các màn hình chính
     */
    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.nav_settings);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                finish(); // Quay lại màn hình Tổng quan
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
                // Đang ở màn hình Cài đặt, không cần làm gì
                return true;
            }
            return false;
        });
    }
}

