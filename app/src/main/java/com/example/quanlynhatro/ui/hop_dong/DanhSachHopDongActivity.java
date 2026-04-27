package com.example.quanlynhatro.ui.hop_dong;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.model.HopDongVm;
import com.example.quanlynhatro.data.repository.HopDongRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class DanhSachHopDongActivity extends AppCompatActivity {
    private RecyclerView rvHopDong;
    private HopDongAdapter adapter;
    private HopDongRepository repository;
    private EditText etSearch;
    private TextView tabAll, tabActive, tabEnding;
    
    private List<HopDongVm> listFull = new ArrayList<>();
    private String currentSearch = "";
    private String currentFilter = "ALL";
    private int filterPhongId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_hop_dong);

        repository = new HopDongRepository(this);
        filterPhongId = getIntent().getIntExtra("FILTER_PHONG_ID", -1);
        
        initViews();
        setupRecyclerView();
        setupSearch();
        setupTabs();
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void initViews() {
        rvHopDong = findViewById(R.id.rvDanhSachHopDong);
        etSearch = findViewById(R.id.etSearch);
        tabAll = findViewById(R.id.tabAll);
        tabActive = findViewById(R.id.tabActive);
        tabEnding = findViewById(R.id.tabEnding);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnThemHopDong).setOnClickListener(v -> {
            startActivity(new Intent(this, ThemSuaHopDongActivity.class));
        });

        if (filterPhongId != -1) {
            ((TextView)findViewById(R.id.tvTitle)).setText("Lịch sử thuê phòng");
        }
    }

    private void setupRecyclerView() {
        rvHopDong.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HopDongAdapter();
        adapter.setOnHopDongClickListener(item -> {
            Intent intent = new Intent(this, ChiTietHopDongActivity.class);
            intent.putExtra("HOP_DONG_ID", item.getId());
            startActivity(intent);
        });
        rvHopDong.setAdapter(adapter);
    }

    private void loadData() {
        listFull = repository.getAllHopDongVm();
        applyFilter();
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearch = s.toString().toLowerCase().trim();
                applyFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupTabs() {
        tabAll.setOnClickListener(v -> {
            updateTabUI(tabAll);
            currentFilter = "ALL";
            applyFilter();
        });

        tabActive.setOnClickListener(v -> {
            updateTabUI(tabActive);
            currentFilter = "ACTIVE";
            applyFilter();
        });

        tabEnding.setOnClickListener(v -> {
            updateTabUI(tabEnding);
            currentFilter = "ENDING";
            applyFilter();
        });
    }

    private void updateTabUI(TextView selected) {
        TextView[] tabs = {tabAll, tabActive, tabEnding};
        for (TextView t : tabs) {
            t.setBackgroundResource(R.drawable.bg_glass_card);
            t.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0x0DFFFFFF));
            t.setTextColor(0xFFFFFFFF);
        }
        selected.setBackgroundResource(R.drawable.bg_primary_button);
        selected.setBackgroundTintList(null);
        selected.setTextColor(0xFFFFFFFF);
    }

    private void applyFilter() {
        List<HopDongVm> filtered = new ArrayList<>();
        for (HopDongVm item : listFull) {
            boolean matchSearch = item.getTenKhachThue().toLowerCase().contains(currentSearch) || 
                                 item.getMaHopDong().toLowerCase().contains(currentSearch) ||
                                 item.getTenPhong().toLowerCase().contains(currentSearch);
            
            boolean matchStatus = true;
            if ("ACTIVE".equals(currentFilter)) {
                matchStatus = !"DA_THANH_LY".equals(item.getTrangThai()) && !"HUY".equals(item.getTrangThai());
            } else if ("ENDING".equals(currentFilter)) {
                long days = calculateDaysRemaining(item.getNgayKetThuc());
                matchStatus = days >= 0 && days < 15 && !"DA_THANH_LY".equals(item.getTrangThai());
            }

            boolean matchRoom = filterPhongId == -1 || item.getPhongId() == filterPhongId;

            if (matchSearch && matchStatus && matchRoom) {
                filtered.add(item);
            }
        }
        
        android.view.View layoutEmpty = findViewById(R.id.layoutEmpty);
        if (layoutEmpty != null) {
            layoutEmpty.setVisibility(filtered.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
            rvHopDong.setVisibility(filtered.isEmpty() ? android.view.View.GONE : android.view.View.VISIBLE);
        }

        adapter.setListHopDong(filtered);
    }

    private long calculateDaysRemaining(String endDateStr) {
        if (endDateStr == null || endDateStr.isEmpty()) return 999;
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
            java.util.Date endDate = sdf.parse(endDateStr);
            java.util.Date today = new java.util.Date();
            long diff = endDate.getTime() - today.getTime();
            return diff / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return 999;
        }
    }

    private void setupBottomNavigation() {
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        // Highlight current tab if it exists in menu (Contracts usually maps to Home or is a subpage)
        // bottomNav.setSelectedItemId(R.id.nav_home); // or custom
        
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
