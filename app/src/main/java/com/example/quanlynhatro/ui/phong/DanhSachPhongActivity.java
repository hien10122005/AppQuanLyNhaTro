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
    private android.view.View btnBack, btnThemPhong;
    private PhongAdapter adapter;
    private com.example.quanlynhatro.data.repository.PhongRepository phongRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_phong);

        phongRepository = new com.example.quanlynhatro.data.repository.PhongRepository(this);
        
        initViews();
        setupRecyclerView();
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load lại dữ liệu mỗi khi quay lại màn hình này (giống như Refresh trong WinForms)
        loadData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnThemPhong = findViewById(R.id.btnThemPhong);
        recyclerPhong = findViewById(R.id.rvDanhSachPhong);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
        
        if (btnThemPhong != null) {
            btnThemPhong.setOnClickListener(v -> {
                // Mở màn hình thêm phòng mới
                startActivity(new Intent(this, ThemSuaPhongActivity.class));
            });
        }
    }

    private void setupRecyclerView() {
        // LayoutManager quyết định cách danh sách hiển thị (Linear = theo hàng dọc)
        recyclerPhong.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new PhongAdapter();
        adapter.setOnPhongClickListener(new PhongAdapter.OnPhongClickListener() {
            @Override
            public void onEditClick(com.example.quanlynhatro.data.model.Phong phong) {
                // Chuyển sang màn hình sửa và gửi ID phòng đi
                Intent intent = new Intent(DanhSachPhongActivity.this, ThemSuaPhongActivity.class);
                intent.putExtra("PHONG_ID", phong.getId());
                startActivity(intent);
            }

            @Override
            public void onItemClick(com.example.quanlynhatro.data.model.Phong phong) {
                // Xem chi tiết phòng
                Intent intent = new Intent(DanhSachPhongActivity.this, ChiTietPhongActivity.class);
                intent.putExtra("PHONG_ID", phong.getId());
                startActivity(intent);
            }

            @Override
            public void onElectricityClick(com.example.quanlynhatro.data.model.Phong phong) {
                // Chốt số điện cho phòng này
                Intent intent = new Intent(DanhSachPhongActivity.this, com.example.quanlynhatro.ui.chi_so.NhapChiSoActivity.class);
                intent.putExtra("PHONG_ID", phong.getId());
                intent.putExtra("LOAI_DV", com.example.quanlynhatro.data.database.DatabaseHelper.LOAI_DICH_VU_DIEN);
                startActivity(intent);
            }

            @Override
            public void onWaterClick(com.example.quanlynhatro.data.model.Phong phong) {
                // Chốt số nước cho phòng này
                Intent intent = new Intent(DanhSachPhongActivity.this, com.example.quanlynhatro.ui.chi_so.NhapChiSoActivity.class);
                intent.putExtra("PHONG_ID", phong.getId());
                intent.putExtra("LOAI_DV", com.example.quanlynhatro.data.database.DatabaseHelper.LOAI_DICH_VU_NUOC);
                startActivity(intent);
            }
        });
        
        recyclerPhong.setAdapter(adapter);
    }

    private void loadData() {
        // Lấy danh sách từ Database và đổ vào Adapter
        java.util.List<com.example.quanlynhatro.data.model.Phong> list = phongRepository.getAllPhong();
        adapter.setDanhSachPhong(list);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_rooms);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(DanhSachPhongActivity.this, TongQuanActivity.class));
                    finish(); // Kết thúc để không bị chồng chất Activity
                    return true;
                } else if (id == R.id.nav_rooms) {
                    return true;
                } else if (id == R.id.nav_invoices) {
                    startActivity(new Intent(DanhSachPhongActivity.this, DanhSachHoaDonActivity.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_reports) {
                    startActivity(new Intent(DanhSachPhongActivity.this, BaoCaoActivity.class));
                    finish();
                    return true;
                } else if (id == R.id.nav_settings) {
                    startActivity(new Intent(DanhSachPhongActivity.this, com.example.quanlynhatro.ui.setting.CaiDatActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
}
