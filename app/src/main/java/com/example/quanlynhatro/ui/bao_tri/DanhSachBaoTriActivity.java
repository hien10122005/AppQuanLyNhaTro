package com.example.quanlynhatro.ui.bao_tri;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.model.SuCoBaoTriVm;
import com.example.quanlynhatro.data.repository.SuCoBaoTriRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DanhSachBaoTriActivity extends AppCompatActivity {
    private SuCoBaoTriRepository repository;
    private BaoTriAdapter adapter;
    private RecyclerView rvDanhSachBaoTri;
    private EditText etSearch;
    private TextView tvCountFixing;
    private TextView tvCountDone;
    private android.view.View layoutEmpty;
    private TextView tabAll;
    private TextView tabPending;
    private TextView tabFixing;
    private TextView tabDone;

    private final List<SuCoBaoTriVm> fullList = new ArrayList<>();
    private String currentSearch = "";
    private String currentStatusFilter = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_bao_tri);

        repository = new SuCoBaoTriRepository(this);
        initViews();
        setupRecyclerView();
        setupSearch();
        setupTabs();
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // onResume() phu hop de refresh danh sach sau khi quay ve tu man them/sua.
        loadData();
    }

    private void initViews() {
        rvDanhSachBaoTri = findViewById(R.id.rvDanhSachBaoTri);
        etSearch = findViewById(R.id.etSearch);
        tvCountFixing = findViewById(R.id.tvCountFixing);
        tvCountDone = findViewById(R.id.tvCountDone);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        tabAll = findViewById(R.id.tabAll);
        tabPending = findViewById(R.id.tabPending);
        tabFixing = findViewById(R.id.tabFixing);
        tabDone = findViewById(R.id.tabDone);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnThemBaoTri).setOnClickListener(v ->
                startActivity(new Intent(this, ThemSuaBaoTriActivity.class)));
        findViewById(R.id.btnFilter).setOnClickListener(v -> cycleFilter());
    }

    private void setupRecyclerView() {
        rvDanhSachBaoTri.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaoTriAdapter();
        adapter.setOnItemClickListener(item -> {
            Intent intent = new Intent(this, ThemSuaBaoTriActivity.class);
            intent.putExtra(ThemSuaBaoTriActivity.EXTRA_BAO_TRI_ID, item.getId());
            startActivity(intent);
        });
        rvDanhSachBaoTri.setAdapter(adapter);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Moi lan user go phim, ta loc lai danh sach ngay lap tuc.
                currentSearch = s != null ? s.toString().trim().toLowerCase(Locale.ROOT) : "";
                applyFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupTabs() {
        // currentStatusFilter rong nghia la "Tat ca".
        tabAll.setOnClickListener(v -> {
            currentStatusFilter = "";
            updateSelectedTab(tabAll);
            applyFilter();
        });
        tabPending.setOnClickListener(v -> {
            currentStatusFilter = ThemSuaBaoTriActivity.STATUS_CHO_XU_LY;
            updateSelectedTab(tabPending);
            applyFilter();
        });
        tabFixing.setOnClickListener(v -> {
            currentStatusFilter = ThemSuaBaoTriActivity.STATUS_DANG_SUA;
            updateSelectedTab(tabFixing);
            applyFilter();
        });
        tabDone.setOnClickListener(v -> {
            currentStatusFilter = ThemSuaBaoTriActivity.STATUS_HOAN_THANH;
            updateSelectedTab(tabDone);
            applyFilter();
        });
        updateSelectedTab(tabAll);
    }

    private void loadData() {
        fullList.clear();
        fullList.addAll(repository.getAllSuCoBaoTriVm());
        updateSummary();
        applyFilter();
    }

    private void updateSummary() {
        int countFixing = 0;
        int countDone = 0;

        // Vong lap nay tinh nhanh cac so tong hop o phan dau man hinh.
        for (SuCoBaoTriVm item : fullList) {
            if (ThemSuaBaoTriActivity.STATUS_DANG_SUA.equals(item.getTrangThai())) {
                countFixing++;
            } else if (ThemSuaBaoTriActivity.STATUS_HOAN_THANH.equals(item.getTrangThai())) {
                countDone++;
            }
        }
        tvCountFixing.setText(String.valueOf(countFixing));
        tvCountDone.setText(String.valueOf(countDone));
    }

    private void applyFilter() {
        List<SuCoBaoTriVm> filtered = new ArrayList<>();

        // Day la buoc "xu ly du lieu cho UI":
        // loc theo trang thai + loc theo text tim kiem.
        for (SuCoBaoTriVm item : fullList) {
            if (item == null) {
                continue;
            }

            // matchStatus = item co dung trang thai dang loc khong
            // matchSearch = item co chua tu khoa user vua nhap khong
            // Chi khi ca 2 dieu kien deu dung thi item moi duoc dua vao danh sach hien thi.
            boolean matchStatus = currentStatusFilter.isEmpty() || currentStatusFilter.equals(item.getTrangThai());
            boolean matchSearch = matchesSearch(item);

            if (matchStatus && matchSearch) {
                filtered.add(item);
            }
        }

        // Khi khong co du lieu sau loc, an RecyclerView va hien empty state.
        // Day la cach UI Android thuong dung thay vi de list rong tron.
        layoutEmpty.setVisibility(filtered.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
        rvDanhSachBaoTri.setVisibility(filtered.isEmpty() ? android.view.View.GONE : android.view.View.VISIBLE);
        adapter.setItems(filtered);
    }

    private boolean matchesSearch(SuCoBaoTriVm item) {
        if (currentSearch.isEmpty()) {
            return true;
        }

        // Chuyen ve chu thuong de tim kiem khong phan biet hoa/thuong.
        String room = safeLower(item.getTenPhong());
        String title = safeLower(item.getTieuDe());
        String content = safeLower(item.getNoiDung());
        String worker = safeLower(item.getNguoiXuLy());

        // contains() o day tuong tu nhu chuoi.Contains(...) trong C#.
        // Chi can 1 trong cac truong co chua tu khoa thi xem nhu khop.
        return room.contains(currentSearch)
                || title.contains(currentSearch)
                || content.contains(currentSearch)
                || worker.contains(currentSearch);
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }

    private void cycleFilter() {
        // Nut filter o day dang duoc dung theo kieu "bam de chuyen vong"
        // giua Tat ca -> Cho xu ly -> Dang sua -> Hoan thanh.
        if (currentStatusFilter.isEmpty()) {
            currentStatusFilter = ThemSuaBaoTriActivity.STATUS_CHO_XU_LY;
            updateSelectedTab(tabPending);
        } else if (ThemSuaBaoTriActivity.STATUS_CHO_XU_LY.equals(currentStatusFilter)) {
            currentStatusFilter = ThemSuaBaoTriActivity.STATUS_DANG_SUA;
            updateSelectedTab(tabFixing);
        } else if (ThemSuaBaoTriActivity.STATUS_DANG_SUA.equals(currentStatusFilter)) {
            currentStatusFilter = ThemSuaBaoTriActivity.STATUS_HOAN_THANH;
            updateSelectedTab(tabDone);
        } else {
            currentStatusFilter = "";
            updateSelectedTab(tabAll);
        }
        applyFilter();
    }

    private void updateSelectedTab(TextView selectedTab) {
        TextView[] tabs = {tabAll, tabPending, tabFixing, tabDone};
        for (TextView tab : tabs) {
            if (tab == selectedTab) {
                tab.setBackgroundResource(R.drawable.bg_primary_button);
                tab.setTextColor(ContextCompat.getColor(this, R.color.white));
            } else {
                tab.setBackgroundResource(R.drawable.bg_glass_card);
                tab.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0x15FFFFFF));
                tab.setTextColor(0xFFC1C6D7);
            }
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, com.example.quanlynhatro.ui.thong_ke.TongQuanActivity.class));
                return true;
            } else if (id == R.id.nav_rooms) {
                startActivity(new Intent(this, com.example.quanlynhatro.ui.phong.DanhSachPhongActivity.class));
                return true;
            } else if (id == R.id.nav_invoices) {
                startActivity(new Intent(this, com.example.quanlynhatro.ui.hoa_don.DanhSachHoaDonActivity.class));
                return true;
            } else if (id == R.id.nav_reports) {
                startActivity(new Intent(this, com.example.quanlynhatro.ui.thong_ke.BaoCaoActivity.class));
                return true;
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, com.example.quanlynhatro.ui.setting.CaiDatActivity.class));
                return true;
            }
            return false;
        });
    }
}
