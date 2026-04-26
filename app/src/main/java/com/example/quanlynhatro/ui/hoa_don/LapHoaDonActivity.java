package com.example.quanlynhatro.ui.hoa_don;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.HoaDon;
import com.example.quanlynhatro.data.model.HoaDonChiTiet;
import com.example.quanlynhatro.data.model.HopDong;
import com.example.quanlynhatro.data.model.LoaiDichVu;
import com.example.quanlynhatro.data.model.Phong;
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

    private List<Phong> listPhong;
    private HopDong currentHopDong;
    private List<HoaDonChiTiet> currentListChiTiet = new ArrayList<>();
    private double tongTienCalculated = 0;

    private DecimalFormat formatter = new DecimalFormat("#,###");

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
    }

    private void setupSpinners() {
        // 1. Spinner Phòng: Chỉ hiện những phòng đang thuê (có hợp đồng hiệu lực)
        listPhong = phongRepository.getPhongByTrangThai(DatabaseHelper.TRANG_THAI_PHONG_DANG_THUE);
        List<String> tenPhongs = new ArrayList<>();
        for (Phong p : listPhong) {
            tenPhongs.add(p.getTenPhong() != null ? p.getTenPhong() : "Phòng " + p.getSoPhong());
        }
        ArrayAdapter<String> adapterPhong = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenPhongs);
        adapterPhong.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhong.setAdapter(adapterPhong);

        // 2. Spinner Tháng
        String[] thangs = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        ArrayAdapter<String> adapterThang = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, thangs);
        spinnerThang.setAdapter(adapterThang);

        // 3. Spinner Năm
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        String[] nams = {String.valueOf(currentYear - 1), String.valueOf(currentYear), String.valueOf(currentYear + 1)};
        ArrayAdapter<String> adapterNam = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nams);
        spinnerNam.setAdapter(adapterNam);

        // Mặc định chọn tháng hiện tại
        spinnerThang.setSelection(cal.get(Calendar.MONTH));
        spinnerNam.setSelection(1); // Năm hiện tại
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

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
    }

    private void calculateInvoice() {
        if (listPhong.isEmpty()) return;

        int phongIdx = spinnerPhong.getSelectedItemPosition();
        Phong phong = listPhong.get(phongIdx);
        int thang = Integer.parseInt(spinnerThang.getSelectedItem().toString());
        int nam = Integer.parseInt(spinnerNam.getSelectedItem().toString());

        // 1. Kiểm tra hóa đơn đã tồn tại chưa
        if (hoaDonRepository.existsHoaDon(phong.getId(), thang, nam)) {
            showError("Hóa đơn tháng này cho phòng này đã tồn tại!");
            return;
        }

        // 2. Lấy hợp đồng hiệu lực
        currentHopDong = hopDongRepository.getHopDongHieuLucTheoPhong(phong.getId());
        if (currentHopDong == null) {
            showError("Không tìm thấy hợp đồng hiệu lực cho phòng này!");
            return;
        }

        tvKhachThue.setText("Hợp đồng: " + currentHopDong.getMaHopDong());
        
        // 3. Bắt đầu tính toán
        currentListChiTiet.clear();
        tongTienCalculated = 0;
        layoutChiTiet.removeAllViews();

        // --- A. Tiền phòng ---
        addDetailRow("Tiền thuê phòng", 1, currentHopDong.getGiaThueChot(), DatabaseHelper.LOAI_DICH_VU_TIEN_PHONG, null, null);

        // --- B. Tiền điện & Nước (Theo chỉ số) ---
        processUtility(phong.getId(), thang, nam, DatabaseHelper.LOAI_DICH_VU_DIEN, "Tiền điện");
        processUtility(phong.getId(), thang, nam, DatabaseHelper.LOAI_DICH_VU_NUOC, "Tiền nước");

        // --- C. Các dịch vụ cố định (Wifi, Rác...) ---
        processFixedServices(phong.getId());

        // Cập nhật UI
        tvTongTien.setText(formatter.format(tongTienCalculated) + "đ");
        layoutPreview.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        btnLuuHoaDon.setEnabled(true);
    }

    private void processUtility(int phongId, int thang, int nam, String maLoai, String tenHienThi) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        // Lấy ID loại dịch vụ
        int loaiId = -1;
        try (Cursor c = db.query(DatabaseHelper.TABLE_LOAI_DICH_VU, new String[]{DatabaseHelper.COL_ID}, 
                DatabaseHelper.COL_LOAI_DICH_VU_MA_LOAI + "=?", new String[]{maLoai}, null, null, null)) {
            if (c.moveToFirst()) loaiId = c.getInt(0);
        }
        if (loaiId == -1) return;

        // Lấy chỉ số tháng này
        try (Cursor c = db.query(DatabaseHelper.TABLE_CHI_SO_DICH_VU_THANG, null,
                DatabaseHelper.COL_CHI_SO_PHONG_ID + "=? AND " + DatabaseHelper.COL_CHI_SO_LOAI_DICH_VU_ID + "=? AND "
                + DatabaseHelper.COL_CHI_SO_THANG + "=? AND " + DatabaseHelper.COL_CHI_SO_NAM + "=?",
                new String[]{String.valueOf(phongId), String.valueOf(loaiId), String.valueOf(thang), String.valueOf(nam)},
                null, null, null)) {
            
            if (c.moveToFirst()) {
                double csCu = c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COL_CHI_SO_CU));
                double csMoi = c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COL_CHI_SO_MOI));
                double tieuThu = c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COL_CHI_SO_SO_LUONG_TIEU_THU));
                double donGia = dichVuRepository.getDonGia(loaiId, phongId);
                
                addDetailRow(tenHienThi, tieuThu, donGia, maLoai, csCu, csMoi);
            } else {
                // Nếu chưa có chỉ số điện/nước, có thể cảnh báo nhưng vẫn cho lập (với số lượng 0)
                // Tuy nhiên theo quy trình chuẩn nên bắt nhập chỉ số trước.
            }
        }
    }

    private void processFixedServices(int phongId) {
        List<LoaiDichVu> allServices = dichVuRepository.getAllLoaiDichVu();
        for (LoaiDichVu dv : allServices) {
            String ma = dv.getMaLoai();
            if (ma.equals(DatabaseHelper.LOAI_DICH_VU_DIEN) || ma.equals(DatabaseHelper.LOAI_DICH_VU_NUOC) 
                    || ma.equals(DatabaseHelper.LOAI_DICH_VU_TIEN_PHONG) || ma.equals(DatabaseHelper.LOAI_DICH_VU_PHAT_SINH)) {
                continue;
            }
            
            double donGia = dichVuRepository.getDonGia(dv.getId(), phongId);
            if (donGia > 0) {
                addDetailRow(dv.getTenLoai(), 1, donGia, ma, null, null);
            }
        }
    }

    private void addDetailRow(String ten, double soLuong, double donGia, String maLoai, Double csCu, Double csMoi) {
        double thanhTien = soLuong * donGia;
        tongTienCalculated += thanhTien;

        // Lưu vào list để sau này insert DB
        HoaDonChiTiet ct = new HoaDonChiTiet();
        ct.setTenMucPhi(ten);
        ct.setSoLuong(soLuong);
        ct.setDonGiaApDung(donGia);
        ct.setThanhTien(thanhTien);
        ct.setChiSoCu(csCu);
        ct.setChiSoMoi(csMoi);
        // Cần tìm ID loại dịch vụ
        ct.setLoaiDichVuId(getLoaiId(maLoai));
        currentListChiTiet.add(ct);

        // Hiển thị lên UI
        View row = LayoutInflater.from(this).inflate(R.layout.item_hoa_don_chi_tiet_row, layoutChiTiet, false);
        TextView tvTen = row.findViewById(R.id.tvTenPhi);
        TextView tvSub = row.findViewById(R.id.tvSubInfo);
        TextView tvTien = row.findViewById(R.id.tvThanhTien);

        tvTen.setText(ten);
        if (csCu != null && csMoi != null) {
            tvSub.setText(String.format("Số: %s → %s (%s)", formatNum(csCu), formatNum(csMoi), formatNum(soLuong)));
        } else {
            tvSub.setText(String.format("SL: %s x %s", formatNum(soLuong), formatter.format(donGia)));
        }
        tvTien.setText(formatter.format(thanhTien) + "đ");

        layoutChiTiet.addView(row);
    }

    private void saveInvoice() {
        if (currentHopDong == null || currentListChiTiet.isEmpty()) return;

        int thang = Integer.parseInt(spinnerThang.getSelectedItem().toString());
        int nam = Integer.parseInt(spinnerNam.getSelectedItem().toString());

        HoaDon hd = new HoaDon();
        hd.setMaHoaDon("HD-" + nam + String.format("%02d", thang) + "-" + currentHopDong.getMaHopDong());
        hd.setHopDongId(currentHopDong.getId());
        hd.setPhongId(currentHopDong.getPhongId());
        hd.setThang(thang);
        hd.setNam(nam);
        hd.setNgayLap(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        
        // Hạn thanh toán sau 5 ngày
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 5);
        hd.setHanThanhToan(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime()));
        
        hd.setTongTienTruocGiam(tongTienCalculated);
        hd.setGiamTru(0);
        hd.setTongTien(tongTienCalculated);
        hd.setDaThanhToan(0);
        hd.setConNo(tongTienCalculated);
        hd.setTrangThai(DatabaseHelper.TRANG_THAI_HOA_DON_CHUA_THANH_TOAN);

        long hdId = hoaDonRepository.addHoaDon(hd);
        if (hdId > 0) {
            for (HoaDonChiTiet ct : currentListChiTiet) {
                ct.setHoaDonId((int) hdId);
                hoaDonRepository.addHoaDonChiTiet(ct);
            }
            Toast.makeText(this, "Lập hóa đơn thành công!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi lưu hóa đơn!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showError(String msg) {
        tvError.setText(msg);
        tvError.setVisibility(View.VISIBLE);
        layoutPreview.setVisibility(View.GONE);
        btnLuuHoaDon.setEnabled(false);
    }

    private int getLoaiId(String maLoai) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query(DatabaseHelper.TABLE_LOAI_DICH_VU, new String[]{DatabaseHelper.COL_ID}, 
                DatabaseHelper.COL_LOAI_DICH_VU_MA_LOAI + "=?", new String[]{maLoai}, null, null, null)) {
            if (c.moveToFirst()) return c.getInt(0);
        }
        return 0;
    }

    private String formatNum(double d) {
        if (d == (long) d) return String.format("%d", (long) d);
        else return String.format("%s", d);
    }
}
