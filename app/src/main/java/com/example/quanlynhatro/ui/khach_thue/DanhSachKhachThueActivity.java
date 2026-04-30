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

        // Khởi tạo repository để thao tác với dữ liệu khách thuê
        repository = new com.example.quanlynhatro.data.repository.KhachThueRepository(this);
        
        initViews();            // Khởi tạo các view
        setupRecyclerView();    // Cài đặt danh sách hiển thị
        setupSearch();          // Cài đặt chức năng tìm kiếm
        setupFilters();         // Cài đặt các bộ lọc (Tất cả, Đã thuê, Chưa thuê)
        
        bottomNavigation = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(); // Cài đặt thanh điều hướng bên dưới
    }

    /**
     * Khởi tạo các view và gán sự kiện click cơ bản
     */
    private void initViews() {
        recyclerView = findViewById(R.id.rvDanhSachKhachThue);
        etSearch = findViewById(R.id.etSearch);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        
        // Nút quay lại
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        // Nút thêm khách thuê mới
        findViewById(R.id.btnThemKhachThue).setOnClickListener(v -> {
            startActivity(new Intent(this, ThemSuaKhachThueActivity.class));
        });
    }

    /**
     * Thiết lập RecyclerView để hiển thị danh sách khách thuê
     */
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        adapter = new KhachThueAdapter();
        
        // Sự kiện khi click vào một khách thuê trong danh sách
        adapter.setOnKhachThueClickListener(kt -> {
            Intent intent = new Intent(this, ChiTietKhachThueActivity.class);
            intent.putExtra("KHACH_THUE_ID", kt.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * Thiết lập ô tìm kiếm theo tên hoặc số điện thoại
     */
    private void setupSearch() {
        etSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cập nhật từ khóa tìm kiếm và áp dụng bộ lọc ngay lập tức
                currentSearch = s.toString().toLowerCase().trim();
                applyFilter();
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });
    }

    /**
     * Thiết lập các nút lọc trạng thái khách thuê (Tất cả / Đã thuê / Chưa thuê)
     */
    private void setupFilters() {
        android.widget.TextView chipTatCa = findViewById(R.id.chipTatCa);
        android.widget.TextView chipDaThue = findViewById(R.id.chipDaThue);
        android.widget.TextView chipChuaThue = findViewById(R.id.chipChuaThue);

        android.view.View.OnClickListener filterClick = v -> {
            // Reset màu sắc của các chip về trạng thái mặc định
            android.widget.TextView[] chips = {chipTatCa, chipDaThue, chipChuaThue};
            for (android.widget.TextView c : chips) {
                c.setBackgroundResource(R.drawable.bg_glass_card);
                c.setTextColor(0x80FFFFFF);
            }
            
            // Làm nổi bật chip đang được chọn
            v.setBackgroundResource(R.drawable.bg_primary_button);
            ((android.widget.TextView)v).setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.white));
            
            // Cập nhật trạng thái lọc và áp dụng
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

    /**
     * Tải lại dữ liệu từ database mỗi khi quay lại màn hình này
     */
    private void loadData() {
        listFull = repository.getAllKhachThueVm();
        applyFilter();
    }

    /**
     * Kết hợp tìm kiếm và lọc trạng thái để hiển thị danh sách cuối cùng
     */
    private void applyFilter() {
        java.util.List<com.example.quanlynhatro.data.model.KhachThueVm> filtered = new java.util.ArrayList<>();
        String filterRented = getString(R.string.filter_rented); // "Đã thuê"
        String filterAvailable = getString(R.string.filter_available); // "Chưa thuê"

        for (com.example.quanlynhatro.data.model.KhachThueVm kt : listFull) {
            if (kt == null) continue;
            
            String hoTen = kt.getHoTen() != null ? kt.getHoTen().toLowerCase() : "";
            String sdt = kt.getSoDienThoai() != null ? kt.getSoDienThoai() : "";
            
            // Kiểm tra khớp với từ khóa tìm kiếm
            boolean matchSearch = hoTen.contains(currentSearch) || sdt.contains(currentSearch);
            
            // Kiểm tra khớp với trạng thái thuê
            boolean matchStatus = currentFilter.equals("Tất cả") || currentFilter.equals(getString(R.string.filter_all));
            if (currentFilter.equals(filterRented)) {
                matchStatus = kt.isDangThue();
            } else if (currentFilter.equals(filterAvailable)) {
                matchStatus = !kt.isDangThue();
            }
            
            if (matchSearch && matchStatus) filtered.add(kt);
        }

        // Hiển thị layout trống nếu không có kết quả
        if (layoutEmpty != null) {
            layoutEmpty.setVisibility(filtered.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
            recyclerView.setVisibility(filtered.isEmpty() ? android.view.View.GONE : android.view.View.VISIBLE);
        }
        
        // Cập nhật adapter
        adapter.setDanhSach(filtered);
    }

    /**
     * Cài đặt chuyển đổi màn hình khi click vào thanh menu bên dưới
     */
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
