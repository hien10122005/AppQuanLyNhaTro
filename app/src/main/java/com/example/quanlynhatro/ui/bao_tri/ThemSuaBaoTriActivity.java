package com.example.quanlynhatro.ui.bao_tri;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.model.Phong;
import com.example.quanlynhatro.data.model.SuCoBaoTri;
import com.example.quanlynhatro.data.repository.PhongRepository;
import com.example.quanlynhatro.data.repository.SuCoBaoTriRepository;
import com.example.quanlynhatro.utils.UIUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ThemSuaBaoTriActivity extends AppCompatActivity {
    public static final String EXTRA_BAO_TRI_ID = "BAO_TRI_ID";

    public static final String STATUS_CHO_XU_LY = "CHO_XU_LY";
    public static final String STATUS_DANG_SUA = "DANG_SUA";
    public static final String STATUS_HOAN_THANH = "HOAN_THANH";

    public static final String PRIORITY_THAP = "THAP";
    public static final String PRIORITY_TRUNG_BINH = "TRUNG_BINH";
    public static final String PRIORITY_CAO = "CAO";
    public static final String PRIORITY_KHAN_CAP = "KHAN_CAP";

    private Spinner spinnerPhong;
    private Spinner spinnerMucDoUuTien;
    private Spinner spinnerTrangThai;
    private EditText etTieuDe;
    private EditText etChiPhi;
    private EditText etNguoiXuLy;
    private EditText etNgayBao;
    private EditText etNgayXuLy;
    private EditText etNoiDung;
    private EditText etGhiChu;
    private TextView tvTitle;
    private android.view.View btnDelete;

    private final List<Phong> phongList = new ArrayList<>();
    private final String[] priorityLabels = {"Thap", "Trung binh", "Cao", "Khan cap"};
    private final String[] priorityValues = {PRIORITY_THAP, PRIORITY_TRUNG_BINH, PRIORITY_CAO, PRIORITY_KHAN_CAP};
    private final String[] statusLabels = {"Cho xu ly", "Dang sua", "Hoan thanh"};
    private final String[] statusValues = {STATUS_CHO_XU_LY, STATUS_DANG_SUA, STATUS_HOAN_THANH};

    private SuCoBaoTriRepository repository;
    private PhongRepository phongRepository;
    private SuCoBaoTri currentItem;
    private int baoTriId = -1;
    private boolean isEditMode = false;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sua_bao_tri);

        repository = new SuCoBaoTriRepository(this);
        phongRepository = new PhongRepository(this);
        baoTriId = getIntent().getIntExtra(EXTRA_BAO_TRI_ID, -1);

        // isEditMode = true khi Activity duoc mo de sua ban ghi cu.
        isEditMode = baoTriId != -1;

        initViews();
        setupToolbar();
        setupSpinners();
        setupDateInputs();
        setupEvents();
        loadPhongData();

        if (isEditMode) {
            loadData();
        } else {
            fillDefaultData();
        }
    }

    private void initViews() {
        spinnerPhong = findViewById(R.id.spinnerPhong);
        spinnerMucDoUuTien = findViewById(R.id.spinnerMucDoUuTien);
        spinnerTrangThai = findViewById(R.id.spinnerTrangThai);
        etTieuDe = findViewById(R.id.etTieuDe);
        etChiPhi = findViewById(R.id.etChiPhi);
        etNguoiXuLy = findViewById(R.id.etNguoiXuLy);
        etNgayBao = findViewById(R.id.etNgayBao);
        etNgayXuLy = findViewById(R.id.etNgayXuLy);
        etNoiDung = findViewById(R.id.etNoiDung);
        etGhiChu = findViewById(R.id.etGhiChu);
        tvTitle = findViewById(R.id.tvTitle);
        btnDelete = findViewById(R.id.btnDelete);
    }

    private void setupToolbar() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        tvTitle.setText(isEditMode ? "Sua bao tri" : "Bao cao su co");
        btnDelete.setVisibility(isEditMode ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    private void setupSpinners() {
        // Spinner trong Android co the hieu nhu ComboBox trong WinForms.
        spinnerMucDoUuTien.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, priorityLabels));
        spinnerTrangThai.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusLabels));

        spinnerTrangThai.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedStatus = statusValues[position];
                // Khi trang thai chuyen sang hoan thanh, tu dong goi y ngay xu ly cho user.
                if (STATUS_HOAN_THANH.equals(selectedStatus) && etNgayXuLy.getText().toString().trim().isEmpty()) {
                    etNgayXuLy.setText(today());
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private void setupDateInputs() {
        etNgayBao.setOnClickListener(v -> showDatePicker(etNgayBao));
        etNgayXuLy.setOnClickListener(v -> showDatePicker(etNgayXuLy));
    }

    private void setupEvents() {
        findViewById(R.id.btnSave).setOnClickListener(v -> saveBaoTri());
        btnDelete.setOnClickListener(v -> UIUtils.showConfirmDialog(
                this,
                "Xoa bao tri",
                "Ban co chac chan muon xoa bao cao su co nay khong?",
                this::deleteBaoTri
        ));
    }

    private void loadPhongData() {
        phongList.clear();
        phongList.addAll(phongRepository.getAllPhong());

        List<String> phongNames = new ArrayList<>();
        for (Phong phong : phongList) {
            phongNames.add(phong.getTenPhong() != null ? phong.getTenPhong() : phong.getSoPhong());
        }

        // Spinner hien thi ten phong, con khi luu ta lay lai object Phong theo vi tri da chon.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, phongNames);
        spinnerPhong.setAdapter(adapter);
    }

    private void fillDefaultData() {
        etNgayBao.setText(today());
        etChiPhi.setText("0");
        spinnerMucDoUuTien.setSelection(1);
        spinnerTrangThai.setSelection(0);
    }

    private void loadData() {
        currentItem = repository.getSuCoBaoTriById(baoTriId);
        if (currentItem == null) {
            Toast.makeText(this, "Khong tim thay du lieu bao tri", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Day la buoc "do du lieu tu object len form" khi sua.
        // Trong WinForms ban thuong se lam: txtTen.Text = entity.Ten;
        etTieuDe.setText(currentItem.getTieuDe());
        etChiPhi.setText(String.valueOf(currentItem.getChiPhi()));
        etNguoiXuLy.setText(currentItem.getNguoiXuLy());
        etNgayBao.setText(currentItem.getNgayBao());
        etNgayXuLy.setText(currentItem.getNgayXuLy());
        etNoiDung.setText(currentItem.getNoiDung());
        etGhiChu.setText(currentItem.getGhiChu());

        setSpinnerSelectionByValue(spinnerMucDoUuTien, priorityValues, currentItem.getMucDoUuTien());
        setSpinnerSelectionByValue(spinnerTrangThai, statusValues, currentItem.getTrangThai());

        // Phong dang luu trong DB la phongId, nen can doi nguoc id -> vi tri Spinner.
        setPhongSelection(currentItem.getPhongId());
    }

    private void setPhongSelection(int phongId) {
        for (int i = 0; i < phongList.size(); i++) {
            if (phongList.get(i).getId() == phongId) {
                spinnerPhong.setSelection(i);
                return;
            }
        }
    }

    private void setSpinnerSelectionByValue(Spinner spinner, String[] values, String targetValue) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(targetValue)) {
                spinner.setSelection(i);
                return;
            }
        }
    }

    private void saveBaoTri() {
        if (phongList.isEmpty()) {
            Toast.makeText(this, "Chua co phong de gan su co", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = etTieuDe.getText().toString().trim();
        String content = etNoiDung.getText().toString().trim();
        String ngayBao = etNgayBao.getText().toString().trim();
        String ngayXuLy = etNgayXuLy.getText().toString().trim();
        String nguoiXuLy = etNguoiXuLy.getText().toString().trim();
        String ghiChu = etGhiChu.getText().toString().trim();
        String status = statusValues[spinnerTrangThai.getSelectedItemPosition()];
        String priority = priorityValues[spinnerMucDoUuTien.getSelectedItemPosition()];

        if (title.isEmpty()) {
            etTieuDe.setError("Vui long nhap tieu de su co");
            return;
        }
        if (content.isEmpty()) {
            etNoiDung.setError("Vui long mo ta chi tiet su co");
            return;
        }
        if (ngayBao.isEmpty()) {
            etNgayBao.setError("Vui long chon ngay bao");
            return;
        }

        // Neu la sua thi dung lai currentItem de giu cac field cu.
        // Neu la them moi thi tao object moi tu dau.
        SuCoBaoTri item = isEditMode && currentItem != null ? currentItem : new SuCoBaoTri();
        Phong selectedPhong = phongList.get(spinnerPhong.getSelectedItemPosition());

        // Day la buoc gom du lieu tu control tren man hinh vao object Model,
        // tuong tu nhu doc TextBox/ComboBox roi gan vao entity trong C#.
        item.setPhongId(selectedPhong.getId());
        item.setTieuDe(title);
        item.setNoiDung(content);
        item.setNgayBao(ngayBao);
        item.setMucDoUuTien(priority);
        item.setTrangThai(status);
        item.setChiPhi(parseCost(etChiPhi.getText().toString().trim()));
        item.setNguoiXuLy(nguoiXuLy);

        // Neu trang thai la HOAN_THANH ma user chua chon ngay xu ly,
        // ham ngayXuLyOrToday() se tu dong lay ngay hom nay de du lieu hop ly hon.
        // Neu chua hoan thanh thi ngay xu ly co the de null.
        item.setNgayXuLy(STATUS_HOAN_THANH.equals(status) ? ngayXuLyOrToday(ngayXuLy) : emptyToNull(ngayXuLy));
        item.setGhiChu(ghiChu);

        boolean success;
        if (isEditMode) {
            item.setId(baoTriId);

            // updateSuCoBaoTri() tra ve so dong bi anh huong.
            // > 0 nghia la update thanh cong.
            success = repository.updateSuCoBaoTri(item) > 0;
        } else {

            // addSuCoBaoTri() tra ve id moi neu insert thanh cong, hoac -1 neu that bai.
            success = repository.addSuCoBaoTri(item) > 0;
        }

        if (success) {
            Toast.makeText(this, "Luu bao tri thanh cong", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Luu bao tri that bai", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteBaoTri() {
        if (!isEditMode) {
            return;
        }

        if (repository.deleteSuCoBaoTri(baoTriId) > 0) {
            Toast.makeText(this, "Da xoa bao tri", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Xoa bao tri that bai", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePicker(EditText target) {
        Calendar calendar = Calendar.getInstance();
        String currentText = target.getText().toString().trim();
        if (!currentText.isEmpty()) {
            try {
                Date parsedDate = dateFormat.parse(currentText);
                if (parsedDate != null) {
                    calendar.setTime(parsedDate);
                }
            } catch (Exception ignored) {
            }
        }

        // DatePickerDialog giup user chon ngay thay vi nhap tay, giam loi format.
        // target la o nao duoc bam thi ket qua se do vao o do.
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            target.setText(dateFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private double parseCost(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            // Neu user nhap sai dinh dang, tam thoi dua ve 0 de tranh app crash.
            return 0;
        }
    }

    private String ngayXuLyOrToday(String value) {
        return value == null || value.trim().isEmpty() ? today() : value;
    }

    private String emptyToNull(String value) {
        return value == null || value.trim().isEmpty() ? null : value;
    }

    private String today() {
        return dateFormat.format(new Date());
    }
}
