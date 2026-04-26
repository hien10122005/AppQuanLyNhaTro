package com.example.quanlynhatro.ui.hoa_don;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.HoaDon;
import com.example.quanlynhatro.data.model.Phong;
import com.example.quanlynhatro.data.model.ThanhToan;
import com.example.quanlynhatro.data.repository.HoaDonRepository;
import com.example.quanlynhatro.data.repository.PhongRepository;
import com.example.quanlynhatro.data.repository.ThanhToanRepository;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ThuTienActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvTenPhong, tvMaHoaDon, tvSoTienCanThu;
    private EditText etSoTienThu, etGhiChu;
    private Spinner spinnerPhuongThuc;
    private Button btnXacNhan;

    private HoaDonRepository hoaDonRepository;
    private PhongRepository phongRepository;
    private ThanhToanRepository thanhToanRepository;

    private int hoaDonId = -1;
    private HoaDon currentHoaDon;
    private DecimalFormat formatter = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thu_tien);

        hoaDonId = getIntent().getIntExtra("hoa_don_id", -1);
        if (hoaDonId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy hóa đơn!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initRepositories();
        initViews();
        loadData();
        setupEvents();
    }

    private void initRepositories() {
        hoaDonRepository = new HoaDonRepository(this);
        phongRepository = new PhongRepository(this);
        thanhToanRepository = new ThanhToanRepository(this);
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvTenPhong = findViewById(R.id.tvTenPhong);
        tvMaHoaDon = findViewById(R.id.tvMaHoaDon);
        tvSoTienCanThu = findViewById(R.id.tvSoTienCanThu);
        etSoTienThu = findViewById(R.id.etSoTienThu);
        etGhiChu = findViewById(R.id.etGhiChu);
        spinnerPhuongThuc = findViewById(R.id.spinnerPhuongThuc);
        btnXacNhan = findViewById(R.id.btnXacNhan);

        String[] phuongThucs = {"Tiền mặt", "Chuyển khoản", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, phuongThucs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhuongThuc.setAdapter(adapter);
    }

    private void loadData() {
        currentHoaDon = hoaDonRepository.getHoaDonById(hoaDonId);
        if (currentHoaDon == null) {
            finish();
            return;
        }

        Phong phong = phongRepository.getPhongById(currentHoaDon.getPhongId());
        if (phong != null) {
            tvTenPhong.setText(phong.getTenPhong() != null ? phong.getTenPhong() : "Phòng " + phong.getSoPhong());
        }

        tvMaHoaDon.setText(currentHoaDon.getMaHoaDon());
        tvSoTienCanThu.setText(formatter.format(currentHoaDon.getConNo()) + "đ");
        
        // Mặc định khách trả hết số còn nợ
        etSoTienThu.setText(String.valueOf((long)currentHoaDon.getConNo()));
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());
        btnXacNhan.setOnClickListener(v -> handleXacNhan());
    }

    private void handleXacNhan() {
        String sTienStr = etSoTienThu.getText().toString().trim();
        if (sTienStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số tiền!", Toast.LENGTH_SHORT).show();
            return;
        }

        double soTienThu = Double.parseDouble(sTienStr);
        if (soTienThu <= 0) {
            Toast.makeText(this, "Số tiền phải lớn hơn 0!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Lưu bản ghi thanh toán
        ThanhToan tt = new ThanhToan();
        tt.setHoaDonId(hoaDonId);
        tt.setSoTien(soTienThu);
        tt.setNgayThanhToan(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        tt.setPhuongThuc(spinnerPhuongThuc.getSelectedItem().toString());
        tt.setGhiChu(etGhiChu.getText().toString());
        
        long res = thanhToanRepository.addThanhToan(tt);
        if (res > 0) {
            // 2. Cập nhật hóa đơn
            double daThanhToanMoi = currentHoaDon.getDaThanhToan() + soTienThu;
            double conNoMoi = currentHoaDon.getTongTien() - daThanhToanMoi;
            if (conNoMoi < 0) conNoMoi = 0; // Tránh số âm

            currentHoaDon.setDaThanhToan(daThanhToanMoi);
            currentHoaDon.setConNo(conNoMoi);
            
            // Cập nhật trạng thái
            if (conNoMoi == 0) {
                currentHoaDon.setTrangThai(DatabaseHelper.TRANG_THAI_HOA_DON_DA_THANH_TOAN);
            } else {
                currentHoaDon.setTrangThai(DatabaseHelper.TRANG_THAI_HOA_DON_THANH_TOAN_MOT_PHAN);
            }

            hoaDonRepository.updateHoaDon(currentHoaDon);
            
            Toast.makeText(this, "Ghi nhận thu tiền thành công!", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi lưu thanh toán!", Toast.LENGTH_SHORT).show();
        }
    }
}
