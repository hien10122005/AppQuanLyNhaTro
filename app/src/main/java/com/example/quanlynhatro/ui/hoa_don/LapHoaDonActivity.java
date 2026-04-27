package com.example.quanlynhatro.ui.hoa_don;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.ChiSoDichVuThang;
import com.example.quanlynhatro.data.model.HoaDon;
import com.example.quanlynhatro.data.model.HoaDonChiTiet;
import com.example.quanlynhatro.data.model.HopDong;
import com.example.quanlynhatro.data.model.LoaiDichVu;
import com.example.quanlynhatro.data.model.Phong;
import com.example.quanlynhatro.data.repository.ChiSoRepository;
import com.example.quanlynhatro.data.repository.DichVuRepository;
import com.example.quanlynhatro.data.repository.HoaDonRepository;
import com.example.quanlynhatro.data.repository.HopDongRepository;
import com.example.quanlynhatro.data.repository.PhongRepository;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * LapHoaDonActivity: Màn hình xử lý lập hóa đơn hàng tháng.
 * Logic xử lý:
 *   1. Chọn Phòng + Tháng + Năm.
 *   2. Tự động tính tiền phòng từ Hợp đồng.
 *   3. Tự động lấy chỉ số Điện/Nước đã nhập trong tháng đó để tính tiền.
 *   4. Tự động lấy các dịch vụ cố định (Wifi, Rác...) đang áp dụng cho phòng.
 */
public class LapHoaDonActivity extends AppCompatActivity {

    private Spinner spinnerPhong, spinnerThang, spinnerNam;
    private LinearLayout layoutPreview, layoutChiTiet;
    private TextView tvKhachThue, tvTongTien, tvError;
    private Button btnLuuHoaDon;
    private ImageButton btnBack;

    private PhongRepository phongRepository;
    private HopDongRepository hopDongRepository;
    private HoaDonRepository hoaDonRepository;
    private DichVuRepository dichVuRepository;
    private ChiSoRepository chiSoRepository;

    private List<Phong> listPhong;
    private HopDong currentHopDong;
    private List<HoaDonChiTiet> currentListChiTiet = new ArrayList<>();
    private double tongTienCalculated = 0;
    private boolean hasMissingReadings = false; // Cờ kiểm tra xem có thiếu chỉ số điện nước không

    private DecimalFormat formatter = new DecimalFormat("#,###");

    private EditText etGiamTru, etGhiChu;
    private Button btnGoToNhapChiSo;
    private View layoutExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lap_hoa_don);

        initRepositories();
        initViews();
        setupSpinners();
        setupEvents();
    }

    private void initRepositories() {
        phongRepository = new PhongRepository(this);
        hopDongRepository = new HopDongRepository(this);
        hoaDonRepository = new HoaDonRepository(this);
        dichVuRepository = new DichVuRepository(this);
        chiSoRepository = new ChiSoRepository(this);
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        spinnerPhong = findViewById(R.id.spinnerPhong);
        spinnerThang = findViewById(R.id.spinnerThang);
        spinnerNam = findViewById(R.id.spinnerNam);
        layoutPreview = findViewById(R.id.layoutPreview);
        layoutChiTiet = findViewById(R.id.layoutChiTiet);
        tvKhachThue = findViewById(R.id.tvKhachThue);
        tvTongTien = findViewById(R.id.tvTongTien);
        tvError = findViewById(R.id.tvError);
        btnLuuHoaDon = findViewById(R.id.btnLuuHoaDon);
        
        etGiamTru = findViewById(R.id.etGiamTru);
        etGhiChu = findViewById(R.id.etGhiChu);
        btnGoToNhapChiSo = findViewById(R.id.btnGoToNhapChiSo);
        layoutExtras = findViewById(R.id.layoutExtras);
    }

    private void setupSpinners() {
        // Load danh sách phòng đang thuê
        listPhong = phongRepository.getPhongByTrangThai(DatabaseHelper.TRANG_THAI_PHONG_DANG_THUE);
        List<String> tenPhongs = new ArrayList<>();
        for (Phong p : listPhong) {
            tenPhongs.add(p.getTenPhong() != null ? p.getTenPhong() : "Phòng " + p.getSoPhong());
        }
        ArrayAdapter<String> adapterPhong = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenPhongs);
        adapterPhong.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhong.setAdapter(adapterPhong);

        // Tháng 1-12
        String[] thangs = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        spinnerThang.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, thangs));

        // Năm (trước, hiện tại, sau)
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        String[] nams = {String.valueOf(currentYear - 1), String.valueOf(currentYear), String.valueOf(currentYear + 1)};
        spinnerNam.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nams));

        spinnerThang.setSelection(cal.get(Calendar.MONTH));
        spinnerNam.setSelection(1);
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        // Khi thay đổi bất kỳ Spinner nào -> Tính toán lại hóa đơn ngay lập tức
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateInvoice();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerPhong.setOnItemSelectedListener(listener);
        spinnerThang.setOnItemSelectedListener(listener);
        spinnerNam.setOnItemSelectedListener(listener);

        btnLuuHoaDon.setOnClickListener(v -> saveInvoice());

        btnGoToNhapChiSo.setOnClickListener(v -> {
            startActivity(new Intent(this, com.example.quanlynhatro.ui.chi_so.NhapChiSoActivity.class));
        });

        // Lắng nghe thay đổi giảm trừ để cập nhật tổng tiền
        etGiamTru.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void afterTextChanged(android.text.Editable s) {
                updateTotalDisplay();
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void updateTotalDisplay() {
        double giamTru = 0;
        try {
            String s = etGiamTru.getText().toString();
            if (!s.isEmpty()) giamTru = Double.parseDouble(s);
        } catch (Exception ignored) {}

        double finalTotal = Math.max(0, tongTienCalculated - giamTru);
        tvTongTien.setText(formatter.format(finalTotal) + "đ");
    }

    /**
     * HÀM QUAN TRỌNG NHẤT: Tính toán các khoản phí của hóa đơn
     */
    private void calculateInvoice() {
        if (listPhong.isEmpty()) return;

        int phongIdx = spinnerPhong.getSelectedItemPosition();
        Phong phong = listPhong.get(phongIdx);
        int thang = Integer.parseInt(spinnerThang.getSelectedItem().toString());
        int nam = Integer.parseInt(spinnerNam.getSelectedItem().toString());

        // 2. Tìm hợp đồng còn hiệu lực để lấy giá thuê
        currentHopDong = hopDongRepository.getHopDongHieuLucTheoPhong(phong.getId());
        if (currentHopDong == null) {
            showError("Lỗi: Phòng này hiện không có hợp đồng thuê nào còn hiệu lực!");
            return;
        }

        // 1. Kiểm tra xem tháng này đã lập hóa đơn chưa (Tránh trùng)
        HoaDon existingHd = hoaDonRepository.getHoaDonTheoKy(currentHopDong.getId(), thang, nam);
        if (existingHd != null) {
            showError("Hóa đơn tháng " + thang + " của phòng này đã được lập.");
            btnGoToNhapChiSo.setVisibility(View.VISIBLE);
            btnGoToNhapChiSo.setText("Xem hóa đơn đã lập ➔");
            btnGoToNhapChiSo.setOnClickListener(v -> {
                Intent intent = new Intent(this, ChiTietHoaDonActivity.class);
                intent.putExtra("hoa_don_id", existingHd.getId());
                startActivity(intent);
            });
            return;
        }

        tvKhachThue.setText("Hợp đồng: " + currentHopDong.getMaHopDong());
        
        // Reset dữ liệu cũ
        currentListChiTiet.clear();
        tongTienCalculated = 0;
        hasMissingReadings = false;
        layoutChiTiet.removeAllViews();
        tvError.setVisibility(View.GONE);
        btnGoToNhapChiSo.setVisibility(View.GONE);
        // Reset text và listener mặc định cho trường hợp không có hóa đơn nhưng thiếu chỉ số
        btnGoToNhapChiSo.setText("Chốt số điện nước ngay ➔");
        btnGoToNhapChiSo.setOnClickListener(v -> {
            startActivity(new Intent(this, com.example.quanlynhatro.ui.chi_so.NhapChiSoActivity.class));
        });

        // --- BƯỚC A: Tiền phòng ---
        // Lấy giá từ hợp đồng đã chốt
        addDetailRow("Tiền thuê phòng", 1, currentHopDong.getGiaThueChot(), DatabaseHelper.LOAI_DICH_VU_TIEN_PHONG, null, null);

        // --- BƯỚC B: Tiền Điện & Nước ---
        // Logic: Phải lấy chỉ số chốt của tháng đang chọn
        processUtility(phong.getId(), thang, nam, DatabaseHelper.LOAI_DICH_VU_DIEN, "Tiền điện");
        processUtility(phong.getId(), thang, nam, DatabaseHelper.LOAI_DICH_VU_NUOC, "Tiền nước");

        // --- BƯỚC C: Các phí dịch vụ khác (Wifi, Rác...) ---
        processFixedServices(phong.getId());

        // Hiển thị tổng cộng ban đầu
        updateTotalDisplay();
        layoutPreview.setVisibility(View.VISIBLE);
        layoutExtras.setVisibility(View.VISIBLE);
        
        // Nếu thiếu chỉ số điện/nước thì cảnh báo và không cho lưu
        if (hasMissingReadings) {
            tvError.setText("Cảnh báo: Chưa nhập chỉ số Điện hoặc Nước cho tháng này. Hãy chốt số trước khi lập hóa đơn!");
            tvError.setVisibility(View.VISIBLE);
            btnGoToNhapChiSo.setVisibility(View.VISIBLE);
            btnLuuHoaDon.setEnabled(false);
            btnLuuHoaDon.setAlpha(0.5f);
        } else {
            btnLuuHoaDon.setEnabled(true);
            btnLuuHoaDon.setAlpha(1.0f);
        }
    }

    /**
     * Xử lý tính tiền điện/nước dựa trên chỉ số tiêu thụ
     */
    private void processUtility(int phongId, int thang, int nam, String maLoai, String tenHienThi) {
        int loaiId = chiSoRepository.getLoaiDichVuId(maLoai);
        if (loaiId == -1) return;

        // Tìm chỉ số đã nhập trong database
        ChiSoDichVuThang cs = chiSoRepository.getChiSoByThangNam(phongId, loaiId, thang, nam);
        
        if (cs != null) {
            double donGia = dichVuRepository.getDonGia(loaiId, phongId);
            addDetailRow(tenHienThi, cs.getSoLuongTieuThu(), donGia, maLoai, cs.getChiSoCu(), cs.getChiSoMoi());
        } else {
            // Đánh dấu là thiếu dữ liệu
            hasMissingReadings = true;
            // Vẫn add dòng vào UI nhưng để giá trị 0 để người dùng thấy là đang thiếu
            addDetailRow(tenHienThi + " (Chưa có số)", 0, 0, maLoai, null, null);
        }
    }

    /**
     * Xử lý các dịch vụ tính trọn gói theo tháng (Wifi, Vệ sinh, Gửi xe...)
     */
    private void processFixedServices(int phongId) {
        List<LoaiDichVu> allServices = dichVuRepository.getAllLoaiDichVu();
        for (LoaiDichVu dv : allServices) {
            String ma = dv.getMaLoai();
            // Bỏ qua các loại đã tính riêng ở trên
            if (ma.equals(DatabaseHelper.LOAI_DICH_VU_DIEN) || ma.equals(DatabaseHelper.LOAI_DICH_VU_NUOC) 
                    || ma.equals(DatabaseHelper.LOAI_DICH_VU_TIEN_PHONG)) {
                continue;
            }
            
            double donGia = dichVuRepository.getDonGia(dv.getId(), phongId);
            // Chỉ thêm nếu dịch vụ này có cài đặt giá (đang áp dụng) cho phòng
            if (donGia > 0) {
                addDetailRow(dv.getTenLoai(), 1, donGia, ma, null, null);
            }
        }
    }

    /**
     * Hàm phụ: Thêm một dòng chi phí vào danh sách và giao diện
     */
    private void addDetailRow(String ten, double soLuong, double donGia, String maLoai, Double csCu, Double csMoi) {
        double thanhTien = soLuong * donGia;
        tongTienCalculated += thanhTien;

        // Tạo object chi tiết để lưu sau này
        HoaDonChiTiet ct = new HoaDonChiTiet();
        ct.setTenMucPhi(ten);
        ct.setSoLuong(soLuong);
        ct.setDonGiaApDung(donGia);
        ct.setThanhTien(thanhTien);
        ct.setChiSoCu(csCu);
        ct.setChiSoMoi(csMoi);
        ct.setLoaiDichVuId(chiSoRepository.getLoaiDichVuId(maLoai));
        currentListChiTiet.add(ct);

        // Vẽ dòng này lên màn hình (LayoutInflater giống như kéo thả Control động trong WinForms)
        View row = LayoutInflater.from(this).inflate(R.layout.item_hoa_don_chi_tiet_row, layoutChiTiet, false);
        TextView tvTen = row.findViewById(R.id.tvTenPhi);
        TextView tvSub = row.findViewById(R.id.tvSubInfo);
        TextView tvTien = row.findViewById(R.id.tvThanhTien);

        tvTen.setText(ten);
        if (csCu != null && csMoi != null) {
            tvSub.setText(String.format("Chỉ số: %s -> %s (Dùng %s)", formatNum(csCu), formatNum(csMoi), formatNum(soLuong)));
        } else if (donGia > 0) {
            tvSub.setText(String.format("Đơn giá: %s", formatter.format(donGia)));
        } else {
            tvSub.setText("-");
        }
        tvTien.setText(formatter.format(thanhTien) + "đ");

        layoutChiTiet.addView(row);
    }

    /**
     * Thực hiện lưu hóa đơn vào CSDL
     */
    private void saveInvoice() {
        if (currentHopDong == null || currentListChiTiet.isEmpty() || hasMissingReadings) return;

        int thang = Integer.parseInt(spinnerThang.getSelectedItem().toString());
        int nam = Integer.parseInt(spinnerNam.getSelectedItem().toString());

        HoaDon hd = new HoaDon();
        // Tạo mã hóa đơn tự động: HD-202604-MAHD
        hd.setMaHoaDon("HD-" + nam + String.format("%02d", thang) + "-" + currentHopDong.getMaHopDong());
        hd.setHopDongId(currentHopDong.getId());
        hd.setPhongId(currentHopDong.getPhongId());
        hd.setThang(thang);
        hd.setNam(nam);
        hd.setNgayLap(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        
        // Hạn thanh toán mặc định là 5 ngày sau khi lập
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 5);
        hd.setHanThanhToan(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime()));
        
        double giamTru = 0;
        try {
            String s = etGiamTru.getText().toString();
            if (!s.isEmpty()) giamTru = Double.parseDouble(s);
        } catch (Exception ignored) {}

        hd.setTongTienTruocGiam(tongTienCalculated);
        hd.setGiamTru(giamTru);
        hd.setTongTien(Math.max(0, tongTienCalculated - giamTru));
        hd.setDaThanhToan(0);
        hd.setConNo(hd.getTongTien());
        hd.setTrangThai(DatabaseHelper.TRANG_THAI_HOA_DON_CHUA_THANH_TOAN);
        hd.setGhiChu(etGhiChu.getText().toString());

        // 1. Lưu bảng chính (HoaDon)
        long hdId = hoaDonRepository.addHoaDon(hd);
        if (hdId > 0) {
            // 2. Lưu các bảng phụ (Chi tiết từng loại phí)
            for (HoaDonChiTiet ct : currentListChiTiet) {
                ct.setHoaDonId((int) hdId);
                hoaDonRepository.addHoaDonChiTiet(ct);
            }
            Toast.makeText(this, "Đã lập hóa đơn thành công!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi lưu dữ liệu!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showError(String msg) {
        tvError.setText(msg);
        tvError.setVisibility(View.VISIBLE);
        layoutPreview.setVisibility(View.GONE);
        btnLuuHoaDon.setEnabled(false);
    }

    private String formatNum(double d) {
        if (d == (long) d) return String.format("%d", (long) d);
        else return String.format("%s", d);
    }
}
