package com.example.quanlynhatro.ui.khach_thue;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.model.KhachThueVm;
import com.example.quanlynhatro.data.repository.KhachThueRepository;
import com.example.quanlynhatro.utils.UIUtils;
import com.google.android.material.appbar.AppBarLayout;

public class ChiTietKhachThueActivity extends AppCompatActivity {

    private TextView tvAvatarInitials, tvHoTen, tvRoomName, tvSdt, tvCccd, tvNgaySinh, tvGioiTinh, tvDiaChi, tvEmail;
    private View btnCall, btnSms, btnEdit, btnDelete;
    private KhachThueVm khachThue;
    private KhachThueRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_khach_thue);

        khachThue = (KhachThueVm) getIntent().getSerializableExtra("KHACH_THUE");
        if (khachThue == null) {
            Toast.makeText(this, "Không tìm thấy thông tin khách thuê!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        repository = new KhachThueRepository(this);
        anhXaView();
        setupToolbar();
        hienThiDuLieu();
        setupEvents();
    }

    private void anhXaView() {
        tvAvatarInitials = findViewById(R.id.tvAvatarInitials);
        tvHoTen = findViewById(R.id.tvFullName);
        tvRoomName = findViewById(R.id.tvRoomNumber);
        tvSdt = findViewById(R.id.tvPhone);
        tvCccd = findViewById(R.id.tvIdCard);
        tvNgaySinh = findViewById(R.id.tvBirthday);
        tvGioiTinh = findViewById(R.id.tvGender);
        tvDiaChi = findViewById(R.id.tvAddress); // Sẽ thêm vào XML
        tvEmail = findViewById(R.id.tvEmail);   // Sẽ thêm vào XML

        btnCall = findViewById(R.id.btnCall);
        btnSms = findViewById(R.id.btnMessage);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnTerminate);
    }

    private void setupToolbar() {
        // Layout dùng ConstraintLayout làm Toolbar nên không dùng setSupportActionBar
        View btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Hiệu ứng hiện tiêu đề khi scroll lên (nếu có AppBarLayout và tvTitle)
        // AppBarLayout appBarLayout = findViewById(R.id.appBar); // Kiểm tra ID trong XML
        // TextView tvTitle = findViewById(R.id.tvTitle);
    }

    private void hienThiDuLieu() {
        tvHoTen.setText(khachThue.getHoTen());
        tvSdt.setText(khachThue.getSoDienThoai());
        tvCccd.setText(khachThue.getCccd() != null ? khachThue.getCccd() : "---");
        tvNgaySinh.setText(khachThue.getNgaySinh() != null ? khachThue.getNgaySinh() : "---");
        tvGioiTinh.setText(khachThue.getGioiTinh() != null ? khachThue.getGioiTinh() : "---");
        tvDiaChi.setText(khachThue.getDiaChiThuongTru() != null ? khachThue.getDiaChiThuongTru() : "---");
        tvEmail.setText(khachThue.getEmail() != null ? khachThue.getEmail() : "---");

        if (khachThue.isDangThue()) {
            tvRoomName.setText("Đang ở: " + khachThue.getTenPhong());
            tvRoomName.setTextColor(getResources().getColor(R.color.primary_blue));
        } else {
            tvRoomName.setText("Trạng thái: Chưa thuê phòng");
            tvRoomName.setTextColor(0xFF808080);
        }

        // Lấy chữ cái đầu làm avatar
        if (khachThue.getHoTen() != null && !khachThue.getHoTen().isEmpty()) {
            String[] words = khachThue.getHoTen().trim().split("\\s+");
            if (words.length >= 2) {
                tvAvatarInitials.setText((words[0].substring(0, 1) + words[words.length - 1].substring(0, 1)).toUpperCase());
            } else {
                tvAvatarInitials.setText(khachThue.getHoTen().substring(0, 1).toUpperCase());
            }
        }
    }

    private void setupEvents() {
        btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + khachThue.getSoDienThoai()));
            startActivity(intent);
        });

        btnSms.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + khachThue.getSoDienThoai()));
            startActivity(intent);
        });

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, ThemSuaKhachThueActivity.class);
            intent.putExtra("KHACH_THUE_ID", khachThue.getId());
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> {
            UIUtils.showConfirmDialog(this, "Xác nhận xóa", 
                "Bạn có chắc chắn muốn xóa khách thuê này? Hành động này không thể hoàn tác.", 
                () -> {
                    if (repository.xoaKhachThue(khachThue.getId())) {
                        Toast.makeText(this, "Đã xóa khách thuê thành công!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(this, "Không thể xóa khách thuê (có thể do ràng buộc hợp đồng)!", Toast.LENGTH_LONG).show();
                    }
                });
        });
    }
}
