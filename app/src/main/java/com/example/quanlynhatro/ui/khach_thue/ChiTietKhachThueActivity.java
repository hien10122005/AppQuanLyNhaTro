package com.example.quanlynhatro.ui.khach_thue;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.model.KhachThueVm;
import com.example.quanlynhatro.data.repository.KhachThueRepository;
import com.example.quanlynhatro.utils.UIUtils;

public class ChiTietKhachThueActivity extends AppCompatActivity {

    private TextView tvAvatarInitials, tvHoTen, tvRoomName, tvSdt, tvCccd, tvNgaySinh, tvGioiTinh, tvDiaChi, tvEmail;
    private View btnCall, btnSms, btnEdit, btnDelete;
    private KhachThueVm khachThue;
    private KhachThueRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_chi_tiet_khach_thue);
            repository = new KhachThueRepository(this);

            // Lấy ID khách thuê từ Intent truyền vào
            int khachThueId = getIntent().getIntExtra("KHACH_THUE_ID", -1);
            if (khachThueId != -1) {
                // Tải thông tin khách thuê từ database theo ID
                khachThue = repository.getKhachThueVmById(khachThueId);
            } else {
                // Hoặc lấy trực tiếp đối tượng từ Intent nếu có
                try {
                    khachThue = (KhachThueVm) getIntent().getSerializableExtra("KHACH_THUE");
                } catch (Exception ignored) {
                }
            }

            // Nếu không tìm thấy thông tin khách thuê, đóng màn hình
            if (khachThue == null) {
                Toast.makeText(this, "Không tìm thấy thông tin khách thuê!", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            anhXaView();       // Ánh xạ các thành phần UI
            setupToolbar();    // Cài đặt thanh công cụ (nút Back)
            hienThiDuLieu();   // Đổ dữ liệu vào các View
            setupEvents();     // Cài đặt các sự kiện click (gọi điện, nhắn tin, sửa, xóa)
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khởi tạo: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại thông tin mới nhất từ database khi quay lại màn hình này (sau khi sửa chẳng hạn)
        if (repository != null && khachThue != null && khachThue.getId() > 0) {
            KhachThueVm refreshed = repository.getKhachThueVmById(khachThue.getId());
            if (refreshed != null) {
                khachThue = refreshed;
                hienThiDuLieu();
            }
        }
    }

    /**
     * Ánh xạ các View từ layout XML vào biến trong Java
     */
    private void anhXaView() {
        tvAvatarInitials = findViewById(R.id.tvAvatarInitials);
        tvHoTen = findViewById(R.id.tvFullName);
        tvRoomName = findViewById(R.id.tvRoomNumber);
        tvSdt = findViewById(R.id.tvPhone);
        tvCccd = findViewById(R.id.tvIdCard);
        tvNgaySinh = findViewById(R.id.tvBirthday);
        tvGioiTinh = findViewById(R.id.tvGender);
        tvDiaChi = findViewById(R.id.tvAddress);
        tvEmail = findViewById(R.id.tvEmail);

        btnCall = findViewById(R.id.btnCall);
        btnSms = findViewById(R.id.btnMessage);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnTerminate);
    }

    private void setupToolbar() {
        View btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    /**
     * Hiển thị thông tin khách thuê lên các View tương ứng
     */
    private void hienThiDuLieu() {
        if (khachThue == null) {
            return;
        }

        if (tvHoTen != null) tvHoTen.setText(khachThue.getHoTen() != null ? khachThue.getHoTen() : "Không có tên");

        // Hiển thị trạng thái thuê phòng
        if (tvRoomName != null) {
            if (khachThue.isDangThue()) {
                tvRoomName.setText("Đang ở: " + (khachThue.getTenPhong() != null ? khachThue.getTenPhong() : "---"));
                tvRoomName.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.primary_blue));
            } else {
                tvRoomName.setText("Trạng thái: Chưa thuê phòng");
                tvRoomName.setTextColor(0xFF808080);
            }
        }

        if (tvSdt != null) tvSdt.setText(khachThue.getSoDienThoai() != null ? khachThue.getSoDienThoai() : "---");
        if (tvCccd != null) tvCccd.setText(khachThue.getCccd() != null ? khachThue.getCccd() : "---");
        if (tvNgaySinh != null) tvNgaySinh.setText(khachThue.getNgaySinh() != null ? khachThue.getNgaySinh() : "---");
        if (tvGioiTinh != null) tvGioiTinh.setText(khachThue.getGioiTinh() != null ? khachThue.getGioiTinh() : "---");
        if (tvDiaChi != null) tvDiaChi.setText(khachThue.getDiaChiThuongTru() != null ? khachThue.getDiaChiThuongTru() : "---");
        if (tvEmail != null) tvEmail.setText(khachThue.getEmail() != null ? khachThue.getEmail() : "---");

        // Xử lý tạo Avatar ký tự (Initials) dựa trên họ tên
        if (tvAvatarInitials != null && khachThue.getHoTen() != null && !khachThue.getHoTen().trim().isEmpty()) {
            String name = khachThue.getHoTen().trim();
            String[] words = name.split("\\s+");
            if (words.length >= 2) {
                String first = words[0].length() > 0 ? words[0].substring(0, 1) : "";
                String last = words[words.length - 1].length() > 0 ? words[words.length - 1].substring(0, 1) : "";
                tvAvatarInitials.setText((first + last).toUpperCase());
            } else if (words.length == 1 && !words[0].isEmpty()) {
                tvAvatarInitials.setText(words[0].substring(0, 1).toUpperCase());
            } else {
                tvAvatarInitials.setText("?");
            }
        } else if (tvAvatarInitials != null) {
            tvAvatarInitials.setText("?");
        }
    }

    /**
     * Cài đặt các sự kiện tương tác của người dùng
     */
    private void setupEvents() {
        // Sự kiện gọi điện
        if (btnCall != null) {
            btnCall.setOnClickListener(v -> {
                try {
                    if (khachThue.getSoDienThoai() != null) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + khachThue.getSoDienThoai()));
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Không thể gọi điện", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Sự kiện gửi tin nhắn SMS
        if (btnSms != null) {
            btnSms.setOnClickListener(v -> {
                try {
                    if (khachThue.getSoDienThoai() != null) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("sms:" + khachThue.getSoDienThoai()));
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Không thể gửi tin nhắn", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Sự kiện đi đến màn hình sửa thông tin
        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(this, ThemSuaKhachThueActivity.class);
                intent.putExtra("KHACH_THUE_ID", khachThue.getId());
                startActivity(intent);
            });
        }

        // Sự kiện Xóa khách thuê hoặc Chấm dứt hợp đồng (nếu đang thuê)
        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> {
                try {
                    if (khachThue.isDangThue()) {
                        // Nếu đang thuê: Xử lý chấm dứt hợp đồng và trả phòng
                        UIUtils.showConfirmDialog(this, "Chấm dứt hợp đồng",
                                "Xác nhận chấm dứt hợp đồng và trả phòng cho khách thuê " + khachThue.getHoTen() + "?",
                                () -> {
                                    try {
                                        com.example.quanlynhatro.data.repository.HopDongRepository hdRepo = new com.example.quanlynhatro.data.repository.HopDongRepository(this);
                                        com.example.quanlynhatro.data.model.HopDong hdActive = hdRepo.getActiveHopDongByKhachThue(khachThue.getId());

                                        if (hdActive != null) {
                                            if (hdRepo.thanhLyHopDong(hdActive.getId(), hdActive.getPhongId())) {
                                                Toast.makeText(this, "Đã chấm dứt hợp đồng và trả phòng!", Toast.LENGTH_LONG).show();
                                                khachThue = repository.getKhachThueVmById(khachThue.getId());
                                                hienThiDuLieu();
                                            } else {
                                                Toast.makeText(this, "Lỗi khi xử lý chấm dứt hợp đồng", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(this, "Không tìm thấy hợp đồng đang hiệu lực", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(this, "Lỗi hệ thống: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        // Nếu chưa thuê: Xử lý xóa vĩnh viễn khỏi hệ thống
                        UIUtils.showConfirmDialog(this, "Xóa khách thuê",
                                "Bạn có chắc chắn muốn xóa khách thuê này khỏi hệ thống?",
                                () -> {
                                    if (repository.xoaKhachThue(khachThue.getId())) {
                                        Toast.makeText(this, "Đã xóa khách thuê thành công!", Toast.LENGTH_SHORT).show();
                                        setResult(RESULT_OK);
                                        finish();
                                    } else {
                                        Toast.makeText(this, "Không thể xóa (có thể do lịch sử thanh toán)", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Lỗi hiển thị: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
