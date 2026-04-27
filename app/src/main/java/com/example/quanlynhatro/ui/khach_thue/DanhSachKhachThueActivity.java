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
    private com.example.quanlynhatro.data.repository.KhachThueRepository repository;
    private KhachThueAdapter adapter;
    private androidx.recyclerview.widget.RecyclerView recyclerView;
    private android.widget.EditText etSearch;
    private android.view.View layoutEmpty;
    private java.util.List<com.example.quanlynhatro.data.model.KhachThueVm> listFull = new java.util.ArrayList<>();
    private String currentSearch = "";
    private String currentFilter = "Tất cả";
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_khach_thue);

        repository = new com.example.quanlynhatro.data.repository.KhachThueRepository(this);
        
        initViews();
        setupRecyclerView();
        setupSearch();
        setupFilters();
        
        bottomNavigation = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rvDanhSachKhachThue);
        etSearch = findViewById(R.id.etSearch);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnThemKhachThue).setOnClickListener(v -> {
            startActivity(new Intent(this, ThemSuaKhachThueActivity.class));
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        adapter = new KhachThueAdapter();
        adapter.setOnKhachThueClickListener(kt -> {
            Intent intent = new Intent(this, ChiTietKhachThueActivity.class);
            intent.putExtra("KHACH_THUE", kt);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearch = s.toString().toLowerCase().trim();
                applyFilter();
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void setupFilters() {
        android.widget.TextView chipTatCa = findViewById(R.id.chipTatCa);
        android.widget.TextView chipDaThue = findViewById(R.id.chipDaThue);
        android.widget.TextView chipChuaThue = findViewById(R.id.chipChuaThue);

        android.view.View.OnClickListener filterClick = v -> {
            // Reset chips
            android.widget.TextView[] chips = {chipTatCa, chipDaThue, chipChuaThue};
            for (android.widget.TextView c : chips) {
                c.setBackgroundResource(R.drawable.bg_glass_card);
                c.setTextColor(0x80FFFFFF);
            }
            
            // Active chip
            v.setBackgroundResource(R.drawable.bg_primary_button);
            ((android.widget.TextView)v).setTextColor(getResources().getColor(R.color.white));
            
            currentFilter = ((android.widget.TextView)v).getText().toString();
            applyFilter();
        };

        chipTatCa.setOnClickListener(filterClick);
        chipDaThue.setOnClickListener(filterClick);
        chipChuaThue.setOnClickListener(filterClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        listFull = repository.getAllKhachThueVm();
        applyFilter();
    }

    private void applyFilter() {
        java.util.List<com.example.quanlynhatro.data.model.KhachThueVm> filtered = new java.util.ArrayList<>();
        for (com.example.quanlynhatro.data.model.KhachThueVm kt : listFull) {
            boolean matchSearch = kt.getHoTen().toLowerCase().contains(currentSearch) || 
                                 kt.getSoDienThoai().contains(currentSearch);
            
            boolean matchStatus = currentFilter.equals("Tất cả");
            if (currentFilter.equals("Đang thuê")) matchStatus = kt.isDangThue();
            else if (currentFilter.equals("Chưa thuê")) matchStatus = !kt.isDangThue();
            
            if (matchSearch && matchStatus) filtered.add(kt);
        }

        if (layoutEmpty != null) {
            layoutEmpty.setVisibility(filtered.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
            recyclerView.setVisibility(filtered.isEmpty() ? android.view.View.GONE : android.view.View.VISIBLE);
        }
        
        adapter.setDanhSach(filtered);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(new com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@androidx.annotation.NonNull android.view.MenuItem item) {
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
