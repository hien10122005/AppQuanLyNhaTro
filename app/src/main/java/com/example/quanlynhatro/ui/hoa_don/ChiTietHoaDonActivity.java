package com.example.quanlynhatro.ui.hoa_don;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.HoaDon;
import com.example.quanlynhatro.data.model.HoaDonChiTiet;
import com.example.quanlynhatro.data.model.Phong;
import com.example.quanlynhatro.data.repository.HoaDonRepository;
import com.example.quanlynhatro.data.repository.PhongRepository;

import java.text.DecimalFormat;
import java.util.List;

public class ChiTietHoaDonActivity extends AppCompatActivity {

    // Khai báo các View trên giao diện
    private ImageButton btnBack, btnShare;
    private TextView tvTenPhong, tvKyHoaDon, tvTrangThai, tvTongTien;
    
    // Card Tiền Phòng
    private TextView tvTienPhong;
    
    // Card Tiền Điện
    private TextView tvThanhTienDien, tvChiSoDien, tvDonGiaDien;
    
    // Card Tiền Nước
    private TextView tvThanhTienNuoc, tvChiSoNuoc, tvDonGiaNuoc;
    
    // Card Dịch Vụ Khác
    private TextView tvThanhTienDichVu, tvChiTietDichVu;
    
    private Button btnGhiNhanThuTien;

    // Khai báo các Repository để lấy dữ liệu từ CSDL
    private HoaDonRepository hoaDonRepository;
    private PhongRepository phongRepository;

    // Biến lưu trữ ID của hóa đơn đang xem
    private int hoaDonId = -1;

    // Định dạng tiền tệ kiểu Việt Nam (VD: 1.500.000)
    private DecimalFormat formatter = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Nạp giao diện từ file XML
        setContentView(R.layout.activity_chi_tiet_hoa_don);

        // Khởi tạo Repository (giống như tạo DbContext trong C# Entity Framework)
        hoaDonRepository = new HoaDonRepository(this);
        phongRepository = new PhongRepository(this);

        // Nhận ID hóa đơn được truyền từ màn hình danh sách (nếu có)
        // Intent giống như cách truyền tham số giữa các Form trong WinForms
        hoaDonId = getIntent().getIntExtra("hoa_don_id", -1);

        // 1. Ánh xạ các thành phần giao diện (Tìm các Control trên Form)
        initViews();

        // 2. Lấy dữ liệu và hiển thị lên màn hình
        loadData();

        // 3. Đăng ký các sự kiện Click (giống như viết hàm Button_Click trong C#)
        setupEvents();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnShare = findViewById(R.id.btnShare);
        
        tvTenPhong = findViewById(R.id.tvTenPhong);
        tvKyHoaDon = findViewById(R.id.tvKyHoaDon);
        tvTrangThai = findViewById(R.id.tvTrangThai);
        tvTongTien = findViewById(R.id.tvTongTien);
        
        tvTienPhong = findViewById(R.id.tvTienPhong);
        
        tvThanhTienDien = findViewById(R.id.tvThanhTienDien);
        tvChiSoDien = findViewById(R.id.tvChiSoDien);
        tvDonGiaDien = findViewById(R.id.tvDonGiaDien);
        
        tvThanhTienNuoc = findViewById(R.id.tvThanhTienNuoc);
        tvChiSoNuoc = findViewById(R.id.tvChiSoNuoc);
        tvDonGiaNuoc = findViewById(R.id.tvDonGiaNuoc);
        
        tvThanhTienDichVu = findViewById(R.id.tvThanhTienDichVu);
        tvChiTietDichVu = findViewById(R.id.tvChiTietDichVu);
        
        btnGhiNhanThuTien = findViewById(R.id.btnGhiNhanThuTien);
    }

    private void loadData() {
        if (hoaDonId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID hóa đơn!", Toast.LENGTH_SHORT).show();
            finish(); // Đóng màn hình nếu lỗi
            return;
        }

        // --- Lấy dữ liệu hóa đơn chính ---
        HoaDon hoaDon = hoaDonRepository.getHoaDonById(hoaDonId);
        if (hoaDon == null) return;

        // Lấy thông tin phòng để hiển thị tên phòng
        Phong phong = phongRepository.getPhongById(hoaDon.getPhongId());
        if (phong != null) {
            tvTenPhong.setText(phong.getTenPhong() != null ? phong.getTenPhong() : "Phòng " + phong.getSoPhong());
        }

        // --- Hiển thị thông tin tổng quan ---
        tvKyHoaDon.setText("Tháng " + String.format("%02d", hoaDon.getThang()) + "/" + hoaDon.getNam());
        tvTongTien.setText(formatTien(hoaDon.getTongTien()));
        
        // Cập nhật giao diện trạng thái thanh toán
        hienThiTrangThai(hoaDon.getTrangThai());

        // --- Lấy danh sách chi tiết hóa đơn (Các dòng phí) ---
        List<HoaDonChiTiet> listChiTiet = hoaDonRepository.getChiTietHoaDon(hoaDonId);
        
        // Khởi tạo các biến để gom nhóm "Dịch vụ khác"
        double tongTienDichVuKhac = 0;
        StringBuilder sbDichVuKhac = new StringBuilder();

        // Xóa text mặc định
        tvTienPhong.setText("0đ");
        tvThanhTienDien.setText("0đ");
        tvThanhTienNuoc.setText("0đ");

        // Duyệt qua từng dòng phí để phân loại
        for (HoaDonChiTiet ct : listChiTiet) {
            String tenPhi = ct.getTenMucPhi().toLowerCase();

            // Nếu là Tiền Phòng
            if (tenPhi.contains("phong") || tenPhi.contains("phòng")) {
                tvTienPhong.setText(formatTien(ct.getThanhTien()));
            } 
            // Nếu là Tiền Điện
            else if (tenPhi.contains("dien") || tenPhi.contains("điện")) {
                tvThanhTienDien.setText(formatTien(ct.getThanhTien()));
                tvDonGiaDien.setText(formatTien(ct.getDonGiaApDung()) + "/kWh");
                
                String csCu = formatNumber(ct.getChiSoCu());
                String csMoi = formatNumber(ct.getChiSoMoi());
                String tieuThu = formatNumber(ct.getSoLuong());
                
                tvChiSoDien.setText(String.format("CS cũ: %s | CS mới: %s\nTiêu thụ: %s kWh", csCu, csMoi, tieuThu));
            }
            // Nếu là Tiền Nước
            else if (tenPhi.contains("nuoc") || tenPhi.contains("nước")) {
                tvThanhTienNuoc.setText(formatTien(ct.getThanhTien()));
                tvDonGiaNuoc.setText(formatTien(ct.getDonGiaApDung()) + "/khối");
                
                String csCu = formatNumber(ct.getChiSoCu());
                String csMoi = formatNumber(ct.getChiSoMoi());
                String tieuThu = formatNumber(ct.getSoLuong());
                
                tvChiSoNuoc.setText(String.format("CS cũ: %s | CS mới: %s\nTiêu thụ: %s khối", csCu, csMoi, tieuThu));
            }
            // Những loại phí còn lại (Rác, Wifi, Gửi xe...) gom vào Dịch vụ khác
            else {
                tongTienDichVuKhac += ct.getThanhTien();
                sbDichVuKhac.append(ct.getTenMucPhi())
                            .append(": ")
                            .append(formatTien(ct.getThanhTien()))
                            .append("\n");
            }
        }

        // Cập nhật card Dịch vụ khác
        tvThanhTienDichVu.setText(formatTien(tongTienDichVuKhac));
        if (sbDichVuKhac.length() > 0) {
            // Xóa ký tự xuống dòng dư thừa ở cuối
            tvChiTietDichVu.setText(sbDichVuKhac.toString().trim());
        } else {
            tvChiTietDichVu.setText("Không có dịch vụ phát sinh");
        }
    }

    private void hienThiTrangThai(String trangThai) {
        if (DatabaseHelper.TRANG_THAI_HOA_DON_DA_THANH_TOAN.equals(trangThai)) {
            tvTrangThai.setText("Đã thanh toán");
            // Set màu nền xanh lá (Success Green)
            tvTrangThai.setBackgroundResource(R.drawable.bg_chip_green);
            tvTrangThai.setTextColor(getResources().getColor(R.color.success_green, null));
            
            // Nếu đã thanh toán rồi thì ẩn nút "Ghi nhận thu tiền"
            btnGhiNhanThuTien.setVisibility(View.GONE);
        } else {
            tvTrangThai.setText("Chưa thanh toán");
            // Set màu nền đỏ (Warning Red)
            tvTrangThai.setBackgroundResource(R.drawable.bg_chip_red);
            tvTrangThai.setTextColor(getResources().getColor(R.color.warning_red, null));
            
            // Hiện lại nút ghi nhận
            btnGhiNhanThuTien.setVisibility(View.VISIBLE);
        }
    }

    private void setupEvents() {
        // Sự kiện khi bấm nút Quay Lại
        btnBack.setOnClickListener(v -> finish()); // finish() giống như this.Close() trong WinForms

        // Sự kiện khi bấm Ghi nhận thu tiền
        btnGhiNhanThuTien.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, ThuTienActivity.class);
            intent.putExtra("hoa_don_id", hoaDonId);
            startActivity(intent);
        });

        // Sự kiện khi bấm nút Chia sẻ
        btnShare.setOnClickListener(v -> {
            // TODO: Tạo hình ảnh hóa đơn và gọi Intent chia sẻ qua Zalo/Facebook
            Toast.makeText(this, "Chức năng chia sẻ đang được phát triển!", Toast.LENGTH_SHORT).show();
        });
    }

    // Các hàm tiện ích để format số liệu cho đẹp
    private String formatTien(double tien) {
        return formatter.format(tien) + "đ";
    }

    private String formatNumber(Double number) {
        if (number == null) return "0";
        // Nếu là số chẵn không có số lẻ thì bỏ số 0 ở sau (ví dụ: 5.0 -> 5)
        if (number == number.intValue()) {
            return String.valueOf(number.intValue());
        }
        return String.valueOf(number);
    }
}
