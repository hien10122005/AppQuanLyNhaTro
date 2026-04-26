package com.example.quanlynhatro.ui.setting;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.repository.DichVuRepository;

public class CauHinhGiaDichVuActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etGiaDien, etGiaNuoc, etGiaInternet, etGiaRac;
    private Spinner spinnerLoaiDien, spinnerLoaiNuoc;
    private Button btnSave;
    private DichVuRepository dichVuRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cau_hinh_gia_dich_vu);

        dichVuRepository = new DichVuRepository(this);

        initViews();
        setupSpinners();
        loadCurrentData();
        setupEvents();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        etGiaDien = findViewById(R.id.etGiaDien);
        etGiaNuoc = findViewById(R.id.etGiaNuoc);
        etGiaInternet = findViewById(R.id.etGiaInternet);
        etGiaRac = findViewById(R.id.etGiaRac);
        spinnerLoaiDien = findViewById(R.id.spinnerLoaiDien);
        spinnerLoaiNuoc = findViewById(R.id.spinnerLoaiNuoc);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupSpinners() {
        String[] loaiTinhDien = {"Theo chỉ số (kWh)"};
        ArrayAdapter<String> adapterDien = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loaiTinhDien);
        adapterDien.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoaiDien.setAdapter(adapterDien);

        String[] loaiTinhNuoc = {"Theo chỉ số (m³)", "Theo đầu người (người/tháng)"};
        ArrayAdapter<String> adapterNuoc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loaiTinhNuoc);
        adapterNuoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoaiNuoc.setAdapter(adapterNuoc);
    }

    private void loadCurrentData() {
        // Lấy giá hiện tại từ DB (phongId = -1 hoặc dùng phương thức getDonGia với phongId null nếu repository hỗ trợ)
        // Ở đây repository getDonGia(loaiId, phongId), ta cần tìm loaiId trước.
        
        // Demo: Tạm thời hiển thị giá mặc định hoặc lấy từ repository
        // Lưu ý: Cần thêm phương thức lấy loaiId từ maLoai trong repository hoặc dùng hardcode ID nếu biết chắc chắn
        // Nhưng tốt nhất là dùng mã.
        
        // Tạm thời để giá mặc định nếu chưa có trong DB
        etGiaDien.setText("3500");
        etGiaNuoc.setText("25000");
        etGiaInternet.setText("100000");
        etGiaRac.setText("30000");
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> saveConfig());
    }

    private void saveConfig() {
        try {
            double giaDien = Double.parseDouble(etGiaDien.getText().toString());
            double giaNuoc = Double.parseDouble(etGiaNuoc.getText().toString());
            double giaInternet = Double.parseDouble(etGiaInternet.getText().toString());
            double giaRac = Double.parseDouble(etGiaRac.getText().toString());

            dichVuRepository.saveBangGiaChung(DatabaseHelper.LOAI_DICH_VU_DIEN, giaDien);
            dichVuRepository.saveBangGiaChung(DatabaseHelper.LOAI_DICH_VU_NUOC, giaNuoc);
            dichVuRepository.saveBangGiaChung(DatabaseHelper.LOAI_DICH_VU_WIFI, giaInternet);
            dichVuRepository.saveBangGiaChung(DatabaseHelper.LOAI_DICH_VU_RAC, giaRac);

            Toast.makeText(this, "Lưu cấu hình giá dịch vụ thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập số tiền hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}
