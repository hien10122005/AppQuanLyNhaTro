package com.example.quanlynhatro.ui.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;

public class ChinhSuaHoSoActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etFullName, etPhone, etEmail, etAddress;
    private Button btnSave;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sua_ho_so);

        // Khởi tạo SharedPreferences để quản lý dữ liệu hồ sơ người dùng
        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

        initViews();            // Ánh xạ View
        loadProfileData();      // Đổ dữ liệu hiện tại lên các ô nhập
        setupEvents();          // Cài đặt sự kiện Lưu dữ liệu
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        btnSave = findViewById(R.id.btnSave);
    }

    /**
     * Tải thông tin người dùng từ SharedPreferences và hiển thị lên các EditText
     */
    private void loadProfileData() {
        String fullName = sharedPreferences.getString("fullName", "Hien Phan");
        String phone = sharedPreferences.getString("phone", "0987654321");
        String email = sharedPreferences.getString("email", "hien.phan@gmail.com");
        String address = sharedPreferences.getString("address", "Thành phố Hồ Chí Minh");

        etFullName.setText(fullName);
        etPhone.setText(phone);
        etEmail.setText(email);
        etAddress.setText(address);
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> saveProfile());
    }

    /**
     * Xử lý lưu các thay đổi của hồ sơ vào SharedPreferences
     */
    private void saveProfile() {
        // Lấy thông tin từ các ô nhập liệu
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        // Kiểm tra họ tên không được để trống
        if (fullName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập họ tên", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thực hiện lưu dữ liệu vào SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fullName", fullName);
        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.putString("address", address);
        editor.apply(); // Áp dụng thay đổi

        Toast.makeText(this, "Cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show();
        finish(); // Đóng màn hình
    }
}
