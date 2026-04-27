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

/**
 * ThuTienActivity: Xử lý ghi nhận khi khách trả tiền nhà.
 * Hoạt động giống như Form lập phiếu thu trong WinForms.
 */
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

        // Nhận ID hóa đơn cần thu tiền
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

        // Nạp danh sách phương thức (ComboBox trong WinForms)
        String[] phuongThucs = {"Tiền mặt", "Chuyển khoản", "Thanh toán qua App"};
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

        tvMaHoaDon.setText("Mã HĐ: " + currentHoaDon.getMaHoaDon());
        tvSoTienCanThu.setText(formatter.format(currentHoaDon.getConNo()) + "đ");
        
        // Gợi ý số tiền thu là số tiền còn nợ (khách thường trả hết)
        etSoTienThu.setText(String.valueOf((long)currentHoaDon.getConNo()));
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());
        btnXacNhan.setOnClickListener(v -> handleXacNhan());
    }

    /**
     * Xử lý khi nhấn nút Xác nhận thu tiền
     */
    private void handleXacNhan() {
        String sTienStr = etSoTienThu.getText().toString().trim();
        if (sTienStr.isEmpty()) {
            Toast.makeText(this, "Hãy nhập số tiền khách trả!", Toast.LENGTH_SHORT).show();
            return;
        }

        double soTienThu = Double.parseDouble(sTienStr);
        if (soTienThu <= 0) {
            Toast.makeText(this, "Số tiền không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Tạo bản ghi ThanhToan để lưu lịch sử (để biết ngày nào thu bao nhiêu)
        ThanhToan tt = new ThanhToan();
        tt.setHoaDonId(hoaDonId);
        tt.setSoTien(soTienThu);
        tt.setNgayThanhToan(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        tt.setPhuongThuc(spinnerPhuongThuc.getSelectedItem().toString());
        tt.setGhiChu(etGhiChu.getText().toString());
        
        long res = thanhToanRepository.addThanhToan(tt);
        if (res > 0) {
            // 2. CẬP NHẬT TRẠNG THÁI HÓA ĐƠN
            // Tính toán số tiền đã trả mới và số tiền còn nợ
            double daThanhToanMoi = currentHoaDon.getDaThanhToan() + soTienThu;
            double conNoMoi = currentHoaDon.getTongTien() - daThanhToanMoi;
            
            if (conNoMoi < 0) conNoMoi = 0; // Tránh trường hợp khách trả dư (tiền thừa)

            currentHoaDon.setDaThanhToan(daThanhToanMoi);
            currentHoaDon.setConNo(conNoMoi);
            
            // Tự động chuyển trạng thái: Đã thu hết hoặc Thu một phần
            if (conNoMoi <= 0) {
                currentHoaDon.setTrangThai(DatabaseHelper.TRANG_THAI_HOA_DON_DA_THANH_TOAN);
            } else {
                currentHoaDon.setTrangThai(DatabaseHelper.TRANG_THAI_HOA_DON_THANH_TOAN_MOT_PHAN);
            }

            hoaDonRepository.updateHoaDon(currentHoaDon);
            
            Toast.makeText(this, "Ghi nhận thu tiền thành công!", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK); // Báo cho màn hình danh sách biết là có thay đổi để Load lại
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi lưu dữ liệu thanh toán!", Toast.LENGTH_SHORT).show();
        }
    }
}
