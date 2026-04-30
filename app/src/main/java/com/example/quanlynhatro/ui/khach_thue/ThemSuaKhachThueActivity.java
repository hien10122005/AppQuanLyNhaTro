package com.example.quanlynhatro.ui.khach_thue;

import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.model.KhachThue;
import com.example.quanlynhatro.data.repository.KhachThueRepository;

public class ThemSuaKhachThueActivity extends AppCompatActivity {

    private EditText etFullName, etPhone, etIdCard, etAddress;
    private Button btnSave;
    private TextView tvTitle;
    private View btnBack;

    private int khachThueId = -1;
    private KhachThueRepository repository;
    private boolean isEditMode = false;
    private KhachThue currentKhachThue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sua_khach_thue);

        repository = new KhachThueRepository(this);
        
        // Kiểm tra xem là chế độ Thêm mới hay Chỉnh sửa thông qua ID truyền vào
        khachThueId = getIntent().getIntExtra("KHACH_THUE_ID", -1);
        isEditMode = khachThueId != -1;

        anhXaView();    // Ánh xạ View
        setupUI();      // Cài đặt tiêu đề phù hợp với chế độ (Thêm/Sửa)
        setupEvents();  // Cài đặt sự kiện Lưu/Quay lại

        if (isEditMode) {
            loadData(); // Nếu là chỉnh sửa, tải thông tin cũ lên các ô nhập
        }
    }

    /**
     * Ánh xạ các thành phần giao diện từ layout XML
     */
    private void anhXaView() {
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etIdCard = findViewById(R.id.etIdCard);
        etAddress = findViewById(R.id.etAddress);
        btnSave = findViewById(R.id.btnSave);
        tvTitle = findViewById(R.id.tvTitle);
        btnBack = findViewById(R.id.btnBack);

        // Ẩn phần chọn phòng nếu có (Vì màn hình này chỉ tập trung vào thông tin cá nhân khách thuê)
        View spRoom = findViewById(R.id.spRoom);
        View rentalSection = findAncestorCard(spRoom);
        if (rentalSection != null) {
            rentalSection.setVisibility(View.GONE);
        }
    }

    private View findAncestorCard(View child) {
        ViewParent parent = child != null ? child.getParent() : null;
        while (parent instanceof View) {
            View parentView = (View) parent;
            if (parentView.getId() != View.NO_ID && parentView.getId() != R.id.spRoom) {
                ViewParent grandParent = parentView.getParent();
                if (!(grandParent instanceof View)) {
                    return parentView;
                }
            }
            if (parentView.getClass().getSimpleName().contains("CardView")) {
                return parentView;
            }
            parent = parentView.getParent();
        }
        return null;
    }

    /**
     * Cập nhật giao diện dựa trên chế độ đang thực hiện
     */
    private void setupUI() {
        if (isEditMode) {
            tvTitle.setText("Sửa khách thuê");
        } else {
            tvTitle.setText("Thêm khách thuê mới");
        }
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveKhachThue());
    }

    /**
     * Tải thông tin khách thuê hiện tại từ database lên giao diện (chỉ dùng cho chế độ Sửa)
     */
    private void loadData() {
        currentKhachThue = repository.getKhachThueById(khachThueId);
        if (currentKhachThue != null) {
            etFullName.setText(currentKhachThue.getHoTen());
            etPhone.setText(currentKhachThue.getSoDienThoai());
            etIdCard.setText(currentKhachThue.getCccd());
            etAddress.setText(currentKhachThue.getDiaChiThuongTru());
        }
    }

    /**
     * Xử lý lưu thông tin khách thuê vào database
     */
    private void saveKhachThue() {
        // Lấy dữ liệu từ các ô nhập
        String hoTen = etFullName.getText().toString().trim();
        String sdt = etPhone.getText().toString().trim();
        String cccd = etIdCard.getText().toString().trim();
        String diaChi = etAddress.getText().toString().trim();

        // Kiểm tra tính hợp lệ của dữ liệu (Validate)
        if (hoTen.isEmpty()) {
            etFullName.setError("Vui lòng nhập họ tên");
            return;
        }
        if (sdt.isEmpty()) {
            etPhone.setError("Vui lòng nhập số điện thoại");
            return;
        }

        // Tạo hoặc cập nhật đối tượng khách thuê
        KhachThue kt = isEditMode && currentKhachThue != null ? currentKhachThue : new KhachThue();
        if (isEditMode) {
            kt.setId(khachThueId);
        }
        kt.setHoTen(hoTen);
        kt.setSoDienThoai(sdt);
        kt.setCccd(cccd);
        kt.setDiaChiThuongTru(diaChi);

        // Gọi Repository để lưu vào database
        boolean result;
        if (isEditMode) {
            result = repository.updateKhachThue(kt) > 0;
        } else {
            result = repository.addKhachThue(kt) > 0;
        }

        // Thông báo kết quả cho người dùng
        if (result) {
            Toast.makeText(this, "Lưu thông tin thành công!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK); // Trả về kết quả thành công cho Activity gọi nó
            finish();            // Đóng màn hình
        } else {
            Toast.makeText(this, "Lưu thông tin thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
