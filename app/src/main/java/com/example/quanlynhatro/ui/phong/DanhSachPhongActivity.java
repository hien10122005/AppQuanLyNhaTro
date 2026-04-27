package com.example.quanlynhatro.ui.phong;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
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
    private TextView chipAll, chipTrong, chipDangThue, chipBaoTri;
    private EditText etSearch;
    private PhongAdapter adapter;
    private com.example.quanlynhatro.data.repository.PhongRepository phongRepository;
    
    private java.util.List<com.example.quanlynhatro.data.model.Phong> listFull = new java.util.ArrayList<>();
    private String currentSearchQuery = "";
    private String currentStatusFilter = "Tất cả";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_phong);

        phongRepository = new com.example.quanlynhatro.data.repository.PhongRepository(this);
        
        // Kiểm tra xem có yêu cầu lọc sẵn trạng thái không (Dùng từ Dashboard chuyển qua)
        if (getIntent().hasExtra("FILTER_STATUS")) {
            currentStatusFilter = getIntent().getStringExtra("FILTER_STATUS");
        }

        initViews();
        setupRecyclerView();
        setupBottomNavigation();
        
        // Nếu có lọc sẵn, cập nhật giao diện chip
        if (!currentStatusFilter.equals("Tất cả")) {
            updateChipHighlight();
        }
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

        etSearch = findViewById(R.id.etSearch);
        chipAll = findViewById(R.id.chipAll);
        chipTrong = findViewById(R.id.chipTrong);
        chipDangThue = findViewById(R.id.chipDangThue);
        chipBaoTri = findViewById(R.id.chipBaoTri);

        setupSearch();
        setupFilters();
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString().toLowerCase().trim();
                applyFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupFilters() {
        android.view.View.OnClickListener filterListener = v -> {
            // Reset tất cả chips về màu mặc định (Glass)
            resetChips();
            
            // Highlight chip được chọn (Primary Blue)
            v.setBackgroundResource(R.drawable.bg_primary_button);
            ((TextView)v).setTextColor(getResources().getColor(R.color.white));
            ((TextView)v).setTypeface(null, android.graphics.Typeface.BOLD);
            
            currentStatusFilter = ((TextView)v).getText().toString();
            applyFilter();
        };

        chipAll.setOnClickListener(filterListener);
        chipTrong.setOnClickListener(filterListener);
        chipDangThue.setOnClickListener(filterListener);
        chipBaoTri.setOnClickListener(filterListener);
    }

    private void updateChipHighlight() {
        resetChips();
        TextView chipToSelect = chipAll;
        
        if (currentStatusFilter.equals("Trống")) chipToSelect = chipTrong;
        else if (currentStatusFilter.equals("Đang thuê")) chipToSelect = chipDangThue;
        else if (currentStatusFilter.equals("Bảo trì")) chipToSelect = chipBaoTri;
        
        chipToSelect.setBackgroundResource(R.drawable.bg_primary_button);
        chipToSelect.setTextColor(getResources().getColor(R.color.white));
        chipToSelect.setTypeface(null, android.graphics.Typeface.BOLD);
    }

    private void resetChips() {
        TextView[] chips = {chipAll, chipTrong, chipDangThue, chipBaoTri};
        for (TextView chip : chips) {
            chip.setBackgroundResource(R.drawable.bg_glass_card);
            chip.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0x20FFFFFF));
            chip.setTextColor(0xA0FFFFFF);
            chip.setTypeface(null, android.graphics.Typeface.NORMAL);
        }
    }

    private void applyFilter() {
        java.util.List<com.example.quanlynhatro.data.model.Phong> filteredList = new java.util.ArrayList<>();
        
        for (com.example.quanlynhatro.data.model.Phong p : listFull) {
            boolean matchSearch = p.getSoPhong().toLowerCase().contains(currentSearchQuery) || 
                                 p.getTenPhong().toLowerCase().contains(currentSearchQuery);
            
            boolean matchStatus = currentStatusFilter.equals("Tất cả");
            if (currentStatusFilter.equals("Trống")) {
                matchStatus = p.getTrangThai().equals(com.example.quanlynhatro.data.database.DatabaseHelper.TRANG_THAI_PHONG_TRONG);
            } else if (currentStatusFilter.equals("Đang thuê")) {
                matchStatus = p.getTrangThai().equals(com.example.quanlynhatro.data.database.DatabaseHelper.TRANG_THAI_PHONG_DANG_THUE);
            } else if (currentStatusFilter.equals("Bảo trì")) {
                matchStatus = p.getTrangThai().equals(com.example.quanlynhatro.data.database.DatabaseHelper.TRANG_THAI_PHONG_BAO_TRI);
            }
            
        if (matchSearch && matchStatus) {
                filteredList.add(p);
            }
        }
        
        // Cập nhật giao diện Empty State
        android.view.View layoutEmpty = findViewById(R.id.layoutEmpty);
        if (layoutEmpty != null) {
            layoutEmpty.setVisibility(filteredList.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
            recyclerPhong.setVisibility(filteredList.isEmpty() ? android.view.View.GONE : android.view.View.VISIBLE);
        }
        
        adapter.setDanhSachPhong(filteredList);
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
        listFull = phongRepository.getAllPhong();
        applyFilter(); // Áp dụng filter hiện tại lên danh sách mới load
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
