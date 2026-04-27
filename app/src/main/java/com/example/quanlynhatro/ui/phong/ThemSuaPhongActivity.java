package com.example.quanlynhatro.ui.phong;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.Phong;
import com.example.quanlynhatro.data.repository.PhongRepository;
import com.google.android.material.button.MaterialButton;

/**
 * ThemSuaPhongActivity dùng chung cho cả việc "Thêm mới" và "Chỉnh sửa".
 * Nếu truyền vào một PHONG_ID, nó sẽ hiểu là đang Sửa. Ngược lại là Thêm.
 * 
 * Cách làm này giống hệt Form trong WinForms: 
 * - Khởi tạo repository để gọi DB.
 * - Map dữ liệu từ Object vào UI (khi Sửa).
 * - Map dữ liệu từ UI vào Object rồi Lưu (khi nhấn Lưu).
 */
public class ThemSuaPhongActivity extends AppCompatActivity {

    private EditText etSoPhong, etTenPhong, etGiaPhong, etDienTich, etSoNguoi, etMoTa;
    private Spinner spinnerLoaiPhong, spinnerTrangThai;
    private MaterialButton btnSave;
    private ImageButton btnBack;
    private TextView tvTitle;

    private PhongRepository repository;
    private int phongId = -1; // Mặc định là -1 (nghĩa là Thêm mới)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sua_phong);

        repository = new PhongRepository(this);

        initViews();
        setupSpinners();
        checkEditMode();

        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveRoom());
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        etSoPhong = findViewById(R.id.etSoPhong);
        etTenPhong = findViewById(R.id.etTenPhong);
        etGiaPhong = findViewById(R.id.etGiaPhongMacDinh);
        etDienTich = findViewById(R.id.etDienTich);
        etSoNguoi = findViewById(R.id.etSoNguoiToiDa);
        etMoTa = findViewById(R.id.etMoTa);
        spinnerLoaiPhong = findViewById(R.id.spinnerLoaiPhong);
        spinnerTrangThai = findViewById(R.id.spinnerTrangThai);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupSpinners() {
        // Cài đặt dữ liệu cho Spinner Loại phòng (Giống ComboBox trong WinForms)
        String[] loaiPhongs = {"Standard", "VIP", "Studio", "Penthouse"};
        ArrayAdapter<String> adapterLoai = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loaiPhongs);
        adapterLoai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoaiPhong.setAdapter(adapterLoai);

        // Cài đặt dữ liệu cho Spinner Trạng thái
        String[] trangThais = {
                DatabaseHelper.TRANG_THAI_PHONG_TRONG,
                DatabaseHelper.TRANG_THAI_PHONG_DANG_THUE,
                DatabaseHelper.TRANG_THAI_PHONG_BAO_TRI
        };
        ArrayAdapter<String> adapterTrangThai = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, trangThais);
        adapterTrangThai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrangThai.setAdapter(adapterTrangThai);
    }

    private void checkEditMode() {
        // Kiểm tra xem có nhận được ID từ màn hình Danh sách không
        if (getIntent().hasExtra("PHONG_ID")) {
            phongId = getIntent().getIntExtra("PHONG_ID", -1);
            if (tvTitle != null) tvTitle.setText("Chỉnh sửa phòng");
            loadRoomData();
        } else {
            if (tvTitle != null) tvTitle.setText("Thêm phòng mới");
        }
    }

    private void loadRoomData() {
        // Lấy thông tin phòng từ DB và điền vào các ô nhập
        Phong phong = repository.getPhongById(phongId);
        if (phong != null) {
            etSoPhong.setText(phong.getSoPhong());
            etTenPhong.setText(phong.getTenPhong());
            etGiaPhong.setText(String.valueOf((long)phong.getGiaPhong()));
            etDienTich.setText(String.valueOf(phong.getDienTich()));
            etSoNguoi.setText(String.valueOf(phong.getSoNguoiToiDa()));
            etMoTa.setText(phong.getMoTa());

            // Chọn giá trị tương ứng trong Spinner
            selectSpinnerItem(spinnerLoaiPhong, phong.getLoaiPhong());
            selectSpinnerItem(spinnerTrangThai, phong.getTrangThai());
        }
    }

    private void saveRoom() {
        // 1. Lấy dữ liệu từ giao diện
        String soPhong = etSoPhong.getText().toString().trim();
        String giaStr = etGiaPhong.getText().toString().trim();
        
        // 2. Kiểm tra dữ liệu (Validation)
        if (soPhong.isEmpty()) {
            etSoPhong.setError("Vui lòng nhập số phòng");
            return;
        }
        if (giaStr.isEmpty()) {
            etGiaPhong.setError("Vui lòng nhập giá phòng");
            return;
        }

        // 3. Đổ dữ liệu vào đối tượng Phong
        Phong phong = new Phong();
        if (phongId != -1) phong.setId(phongId);
        phong.setSoPhong(soPhong);
        phong.setTenPhong(etTenPhong.getText().toString().trim());
        phong.setGiaPhong(Double.parseDouble(giaStr));
        phong.setDienTich(Double.parseDouble(etDienTich.getText().toString().isEmpty() ? "0" : etDienTich.getText().toString()));
        phong.setSoNguoiToiDa(Integer.parseInt(etSoNguoi.getText().toString().isEmpty() ? "1" : etSoNguoi.getText().toString()));
        phong.setLoaiPhong(spinnerLoaiPhong.getSelectedItem().toString());
        phong.setTrangThai(spinnerTrangThai.getSelectedItem().toString());
        phong.setMoTa(etMoTa.getText().toString().trim());

        // 4. Lưu vào CSDL
        long result;
        if (phongId == -1) {
            result = repository.addPhong(phong);
            if (result > 0) Toast.makeText(this, "Thêm phòng thành công!", Toast.LENGTH_SHORT).show();
        } else {
            result = repository.updatePhong(phong);
            if (result > 0) Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
        }

        if (result > 0) {
            finish(); // Đóng màn hình sau khi lưu xong
        } else {
            Toast.makeText(this, "Lỗi khi lưu dữ liệu!", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectSpinnerItem(Spinner spinner, String value) {
        if (value == null) return;
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++) {
            if (adapter.getItem(position).toString().equals(value)) {
                spinner.setSelection(position);
                return;
            }
        }
    }
}
