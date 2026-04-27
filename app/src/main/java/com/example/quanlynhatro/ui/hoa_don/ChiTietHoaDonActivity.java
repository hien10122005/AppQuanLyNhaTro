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
import com.example.quanlynhatro.data.repository.DichVuRepository;
import com.example.quanlynhatro.data.repository.HoaDonRepository;
import com.example.quanlynhatro.data.repository.PhongRepository;

import java.text.DecimalFormat;
import java.util.List;

/**
 * ChiTietHoaDonActivity: Màn hình hiển thị chi tiết hóa đơn.
 * Xử lý phân loại các khoản phí: Tiền phòng, Điện, Nước, và các Dịch vụ khác.
 */
public class ChiTietHoaDonActivity extends AppCompatActivity {

    private ImageButton btnBack, btnShare;
    private TextView tvTenPhong, tvKyHoaDon, tvTrangThai, tvTongTien;
    
    // Các TextView trong các thẻ Card (Phần này tương đương GroupBox/Panel trong WinForms)
    private TextView tvTienPhong;
    private TextView tvThanhTienDien, tvChiSoDien, tvDonGiaDien;
    private TextView tvThanhTienNuoc, tvChiSoNuoc, tvDonGiaNuoc;
    private TextView tvThanhTienDichVu, tvChiTietDichVu;
    
    private Button btnGhiNhanThuTien;
    private com.google.android.material.button.MaterialButton btnLichSuThanhToan, btnExportPdf;

    private HoaDonRepository hoaDonRepository;
    private PhongRepository phongRepository;
    private DichVuRepository dichVuRepository;

    private int hoaDonId = -1;
    private HoaDon currentHoaDon; // Lưu lại để dùng khi xuất PDF
    private List<HoaDonChiTiet> currentChiTiet; // Lưu lại để dùng khi xuất PDF
    private DecimalFormat formatter = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_hoa_don);

        hoaDonRepository = new HoaDonRepository(this);
        phongRepository = new PhongRepository(this);
        dichVuRepository = new DichVuRepository(this);

        hoaDonId = getIntent().getIntExtra("hoa_don_id", -1);

        initViews();
        loadData();
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
        btnLichSuThanhToan = findViewById(R.id.btnLichSuThanhToan);
        btnExportPdf = findViewById(R.id.btnExportPdf);
    }

    private void loadData() {
        if (hoaDonId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin hóa đơn!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentHoaDon = hoaDonRepository.getHoaDonById(hoaDonId);
        if (currentHoaDon == null) return;

        // 1. Thông tin chung
        Phong phong = phongRepository.getPhongById(currentHoaDon.getPhongId());
        if (phong != null) {
            tvTenPhong.setText(phong.getTenPhong() != null ? phong.getTenPhong() : "Phòng " + phong.getSoPhong());
        }
        tvKyHoaDon.setText("Kỳ hóa đơn: Tháng " + String.format("%02d", currentHoaDon.getThang()) + "/" + currentHoaDon.getNam());
        tvTongTien.setText(formatTien(currentHoaDon.getTongTien()));
        hienThiTrangThai(currentHoaDon.getTrangThai());

        // 2. Xử lý phân loại từng dòng phí (Chi tiết hóa đơn)
        currentChiTiet = hoaDonRepository.getChiTietHoaDon(hoaDonId);
        
        double tongTienDichVuKhac = 0;
        StringBuilder sbDichVuKhac = new StringBuilder();

        for (HoaDonChiTiet ct : currentChiTiet) {
            // Lấy mã loại dịch vụ (DIEN, NUOC, PHONG...) để kiểm tra chính xác hơn dùng tên
            String maLoai = dichVuRepository.getMaLoaiById(ct.getLoaiDichVuId());

            if (maLoai.equals(DatabaseHelper.LOAI_DICH_VU_TIEN_PHONG)) {
                tvTienPhong.setText(formatTien(ct.getThanhTien()));
            } 
            else if (maLoai.equals(DatabaseHelper.LOAI_DICH_VU_DIEN)) {
                tvThanhTienDien.setText(formatTien(ct.getThanhTien()));
                tvDonGiaDien.setText(formatTien(ct.getDonGiaApDung()) + "/kWh");
                tvChiSoDien.setText(String.format("Số cũ: %s | Số mới: %s\nTiêu thụ: %s kWh", 
                        formatNum(ct.getChiSoCu()), formatNum(ct.getChiSoMoi()), formatNum(ct.getSoLuong())));
            }
            else if (maLoai.equals(DatabaseHelper.LOAI_DICH_VU_NUOC)) {
                tvThanhTienNuoc.setText(formatTien(ct.getThanhTien()));
                tvDonGiaNuoc.setText(formatTien(ct.getDonGiaApDung()) + "/khối");
                tvChiSoNuoc.setText(String.format("Số cũ: %s | Số mới: %s\nTiêu thụ: %s khối", 
                        formatNum(ct.getChiSoCu()), formatNum(ct.getChiSoMoi()), formatNum(ct.getSoLuong())));
            }
            else {
                // Gom tất cả các loại phí khác (Wifi, Rác...) vào một nhóm
                tongTienDichVuKhac += ct.getThanhTien();
                sbDichVuKhac.append("• ").append(ct.getTenMucPhi())
                            .append(": ").append(formatTien(ct.getThanhTien()))
                            .append("\n");
            }
        }

        tvThanhTienDichVu.setText(formatTien(tongTienDichVuKhac));
        tvChiTietDichVu.setText(sbDichVuKhac.length() > 0 ? sbDichVuKhac.toString().trim() : "Không có dịch vụ khác");
    }

    private void hienThiTrangThai(String trangThai) {
        if (DatabaseHelper.TRANG_THAI_HOA_DON_DA_THANH_TOAN.equals(trangThai)) {
            tvTrangThai.setText("ĐÃ THANH TOÁN");
            tvTrangThai.setBackgroundResource(R.drawable.bg_chip_green);
            tvTrangThai.setTextColor(getResources().getColor(R.color.success_green, null));
            btnGhiNhanThuTien.setVisibility(View.GONE); // Đã thanh toán thì không hiện nút thu tiền nữa
        } else {
            tvTrangThai.setText("CHƯA THANH TOÁN");
            tvTrangThai.setBackgroundResource(R.drawable.bg_chip_red);
            tvTrangThai.setTextColor(getResources().getColor(R.color.warning_red, null));
            btnGhiNhanThuTien.setVisibility(View.VISIBLE);
        }
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        btnGhiNhanThuTien.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, ThuTienActivity.class);
            intent.putExtra("hoa_don_id", hoaDonId);
            startActivity(intent);
        });

        // Sự kiện xem lịch sử các đợt trả tiền (Item 5.4 trong NHIEM_VU)
        btnLichSuThanhToan.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, LichSuThanhToanActivity.class);
            intent.putExtra("hoa_don_id", hoaDonId);
            startActivity(intent);
        });

        // Sự kiện xuất hóa đơn ra PDF (Item 5.5 trong NHIEM_VU)
        btnExportPdf.setOnClickListener(v -> {
            if (currentHoaDon != null && currentChiTiet != null) {
                // Chúng ta cần một đối tượng HoaDonVm có đầy đủ tên phòng/khách để in PDF đẹp hơn
                com.example.quanlynhatro.data.model.HoaDonVm vm = new com.example.quanlynhatro.data.model.HoaDonVm();
                vm.setMaHoaDon(currentHoaDon.getMaHoaDon());
                vm.setTenPhong(tvTenPhong.getText().toString());
                vm.setTenKhach("Khách thuê"); // Có thể lấy từ repository nếu muốn chi tiết hơn
                vm.setThang(currentHoaDon.getThang());
                vm.setNam(currentHoaDon.getNam());
                vm.setTongTien(currentHoaDon.getTongTien());
                
                com.example.quanlynhatro.utils.InvoiceExportUtils.exportToPdf(this, vm, currentChiTiet);
            }
        });

        btnShare.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng chia sẻ hóa đơn qua Zalo đang phát triển!", Toast.LENGTH_SHORT).show();
        });
    }

    private String formatTien(double tien) {
        return formatter.format(tien) + "đ";
    }

    private String formatNum(Double d) {
        if (d == null) return "0";
        if (d == d.intValue()) return String.valueOf(d.intValue());
        return String.valueOf(d);
    }
}
