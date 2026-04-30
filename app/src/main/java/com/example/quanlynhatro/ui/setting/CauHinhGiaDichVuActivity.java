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

        // Khởi tạo repository để tương tác với bảng giá dịch vụ trong database
        dichVuRepository = new DichVuRepository(this);

        initViews();            // Ánh xạ các View
        setupSpinners();        // Cài đặt danh sách lựa chọn cho Spinner (Đơn vị tính)
        loadCurrentData();      // Tải giá hiện tại từ database lên giao diện
        setupEvents();          // Cài đặt sự kiện click cho nút Lưu
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

    /**
     * Cài đặt các lựa chọn cho Spinner (Cách tính tiền điện, nước)
     */
    private void setupSpinners() {
        // Cài đặt Spinner cho điện
        String[] loaiTinhDien = {"Theo chỉ số (kWh)"};
        ArrayAdapter<String> adapterDien = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loaiTinhDien);
        adapterDien.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoaiDien.setAdapter(adapterDien);

        // Cài đặt Spinner cho nước (có 2 cách tính phổ biến)
        String[] loaiTinhNuoc = {"Theo chỉ số (m³)", "Theo đầu người (người/tháng)"};
        ArrayAdapter<String> adapterNuoc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loaiTinhNuoc);
        adapterNuoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoaiNuoc.setAdapter(adapterNuoc);
    }

    /**
     * Tải đơn giá dịch vụ hiện hành từ database lên các ô nhập liệu
     */
    private void loadCurrentData() {
        // Tạm thời hiển thị giá mặc định phổ biến (trong thực tế sẽ lấy từ dichVuRepository)
        etGiaDien.setText("3500");
        etGiaNuoc.setText("25000");
        etGiaInternet.setText("100000");
        etGiaRac.setText("30000");
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> saveConfig());
    }

    /**
     * Xử lý lưu các thay đổi về giá dịch vụ vào database
     */
    private void saveConfig() {
        try {
            // Lấy giá trị từ giao diện và chuyển sang kiểu số
            double giaDien = Double.parseDouble(etGiaDien.getText().toString());
            double giaNuoc = Double.parseDouble(etGiaNuoc.getText().toString());
            double giaInternet = Double.parseDouble(etGiaInternet.getText().toString());
            double giaRac = Double.parseDouble(etGiaRac.getText().toString());

            // Lưu vào database thông qua repository
            dichVuRepository.saveBangGiaChung(DatabaseHelper.LOAI_DICH_VU_DIEN, giaDien);
            dichVuRepository.saveBangGiaChung(DatabaseHelper.LOAI_DICH_VU_NUOC, giaNuoc);
            dichVuRepository.saveBangGiaChung(DatabaseHelper.LOAI_DICH_VU_WIFI, giaInternet);
            dichVuRepository.saveBangGiaChung(DatabaseHelper.LOAI_DICH_VU_RAC, giaRac);

            Toast.makeText(this, "Lưu cấu hình giá dịch vụ thành công!", Toast.LENGTH_SHORT).show();
            finish(); // Đóng màn hình sau khi lưu thành công
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập số tiền hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}
