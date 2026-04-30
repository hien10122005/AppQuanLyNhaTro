package com.example.quanlynhatro.ui.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;

public class DoiMatKhauActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private ImageView ivToggleCurrent, ivToggleNew, ivToggleConfirm;
    private Button btnSave;
    private SharedPreferences sharedPreferences;

    private boolean isCurrentVisible = false;
    private boolean isNewVisible = false;
    private boolean isConfirmVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);

        // Khởi tạo SharedPreferences để kiểm tra và cập nhật mật khẩu
        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

        initViews();    // Ánh xạ View
        setupEvents();  // Cài đặt sự kiện click và ẩn/hiện mật khẩu
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        ivToggleCurrent = findViewById(R.id.ivToggleCurrent);
        ivToggleNew = findViewById(R.id.ivToggleNew);
        ivToggleConfirm = findViewById(R.id.ivToggleConfirm);
        btnSave = findViewById(R.id.btnSave);
    }

    /**
     * Cài đặt các sự kiện click cho nút quay lại, nút lưu và các icon ẩn/hiện mật khẩu
     */
    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        // Ẩn/Hiện mật khẩu hiện tại
        ivToggleCurrent.setOnClickListener(v -> {
            isCurrentVisible = !isCurrentVisible;
            togglePasswordVisibility(etCurrentPassword, ivToggleCurrent, isCurrentVisible);
        });

        // Ẩn/Hiện mật khẩu mới
        ivToggleNew.setOnClickListener(v -> {
            isNewVisible = !isNewVisible;
            togglePasswordVisibility(etNewPassword, ivToggleNew, isNewVisible);
        });

        // Ẩn/Hiện xác nhận mật khẩu mới
        ivToggleConfirm.setOnClickListener(v -> {
            isConfirmVisible = !isConfirmVisible;
            togglePasswordVisibility(etConfirmPassword, ivToggleConfirm, isConfirmVisible);
        });

        // Nút lưu mật khẩu mới
        btnSave.setOnClickListener(v -> updatePassword());
    }

    /**
     * Chuyển đổi định dạng của EditText giữa mật khẩu ẩn (bullet) và văn bản thuần túy
     */
    private void togglePasswordVisibility(EditText editText, ImageView imageView, boolean isVisible) {
        if (isVisible) {
            // Hiển thị mật khẩu dưới dạng text
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView.setImageResource(R.drawable.ic_visibility);
        } else {
            // Ẩn mật khẩu (dạng dấu chấm)
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView.setImageResource(R.drawable.ic_visibility_off);
        }
        // Đưa con trỏ chuột về cuối dòng văn bản sau khi thay đổi kiểu nhập
        editText.setSelection(editText.getText().length());
    }

    /**
     * Xử lý logic cập nhật mật khẩu mới
     */
    private void updatePassword() {
        String currentPass = etCurrentPassword.getText().toString();
        String newPass = etNewPassword.getText().toString();
        String confirmPass = etConfirmPassword.getText().toString();

        // Lấy mật khẩu cũ đang được lưu trong SharedPreferences (mặc định là admin123)
        String savedPass = sharedPreferences.getString("password", "admin123");

        // 1. Kiểm tra các ô nhập liệu có bị trống không
        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Kiểm tra mật khẩu hiện tại có đúng không
        if (!currentPass.equals(savedPass)) {
            Toast.makeText(this, "Mật khẩu hiện tại không chính xác", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Kiểm tra độ dài mật khẩu mới (tối thiểu 8 ký tự)
        if (newPass.length() < 8) {
            Toast.makeText(this, "Mật khẩu mới phải có ít nhất 8 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Kiểm tra xác nhận mật khẩu có khớp không
        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Xác nhận mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thực hiện lưu mật khẩu mới vào SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", newPass);
        editor.apply();

        Toast.makeText(this, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show();
        finish(); // Quay lại màn hình trước đó
    }
}
