package com.example.quanlynhatro.ui.hop_dong;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.HopDong;
import com.example.quanlynhatro.data.model.KhachThue;
import com.example.quanlynhatro.data.model.Phong;
import com.example.quanlynhatro.data.repository.HopDongRepository;
import com.example.quanlynhatro.data.repository.KhachThueRepository;
import com.example.quanlynhatro.data.repository.PhongRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * ThemSuaHopDongActivity - Xử lý logic lập hợp đồng mới.
 * 
 * Logic chính:
 * 1. Load danh sách phòng đang TRỐNG.
 * 2. Load danh sách tất cả khách thuê.
 * 3. Tính toán ngày kết thúc = Ngày bắt đầu + Số tháng thuê.
 * 4. Khi lưu: Lưu Hợp đồng VÀ cập nhật trạng thái Phòng sang "Đang thuê".
 */
public class ThemSuaHopDongActivity extends AppCompatActivity {

    private Spinner spinnerPhong, spinnerKhachThue, spinnerKyThanhToan;
    private LinearLayout btnNgayBatDau;
    private TextView tvNgayBatDau, tvTitle;
    private EditText etThoiHan, etTienCoc, etGhiChu;
    
    private PhongRepository phongRepo;
    private KhachThueRepository khachRepo;
    private HopDongRepository hopDongRepo;
    
    private Calendar calendarBatDau = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sua_hop_dong);

        // Khởi tạo các Repository (Giống khởi tạo Service/DAL trong C#)
        phongRepo = new PhongRepository(this);
        khachRepo = new KhachThueRepository(this);
        hopDongRepo = new HopDongRepository(this);

        initViews();
        setupSpinners();
        setupDatePicker();
        
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnSave).setOnClickListener(v -> saveContract());
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        spinnerPhong = findViewById(R.id.spinnerPhong);
        spinnerKhachThue = findViewById(R.id.spinnerKhachThue);
        spinnerKyThanhToan = findViewById(R.id.spinnerKyThanhToan);
        btnNgayBatDau = findViewById(R.id.btnNgayBatDau);
        tvNgayBatDau = findViewById(R.id.tvNgayBatDau);
        etThoiHan = findViewById(R.id.etThoiHan);
        etTienCoc = findViewById(R.id.etTienCoc);
        etGhiChu = findViewById(R.id.etGhiChu);

        // Mặc định ngày bắt đầu là hôm nay
        tvNgayBatDau.setText(dateFormat.format(calendarBatDau.getTime()));
    }

    private void setupSpinners() {
        // 1. Load danh sách phòng TRỐNG
        List<Phong> listPhongTrong = phongRepo.getPhongByTrangThai(DatabaseHelper.TRANG_THAI_PHONG_TRONG);
        if (listPhongTrong.isEmpty()) {
            Toast.makeText(this, "Không có phòng nào đang trống!", Toast.LENGTH_LONG).show();
        }
        
        // Tạo Adapter hiển thị tên phòng (Sử dụng toString của object hoặc tạo Adapter tùy biến)
        // Ở đây tôi dùng một danh sách String đơn giản cho dễ đọc
        String[] tenPhongs = new String[listPhongTrong.size()];
        for (int i = 0; i < listPhongTrong.size(); i++) {
            tenPhongs[i] = "Phòng " + listPhongTrong.get(i).getSoPhong();
        }
        ArrayAdapter<String> adapterPhong = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenPhongs);
        adapterPhong.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhong.setAdapter(adapterPhong);
        // Lưu lại danh sách object để lấy ID khi cần
        spinnerPhong.setTag(listPhongTrong);

        // 2. Load danh sách khách thuê
        List<KhachThue> listKhach = khachRepo.getAllKhachThue();
        String[] tenKhachs = new String[listKhach.size()];
        for (int i = 0; i < listKhach.size(); i++) {
            tenKhachs[i] = listKhach.get(i).getHoTen();
        }
        ArrayAdapter<String> adapterKhach = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenKhachs);
        adapterKhach.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKhachThue.setAdapter(adapterKhach);
        spinnerKhachThue.setTag(listKhach);

        // 3. Chu kỳ thanh toán
        String[] chuKys = {"1 tháng/lần", "2 tháng/lần", "3 tháng/lần", "6 tháng/lần"};
        ArrayAdapter<String> adapterKy = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, chuKys);
        adapterKy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKyThanhToan.setAdapter(adapterKy);
    }

    private void setupDatePicker() {
        btnNgayBatDau.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                calendarBatDau.set(Calendar.YEAR, year);
                calendarBatDau.set(Calendar.MONTH, month);
                calendarBatDau.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                tvNgayBatDau.setText(dateFormat.format(calendarBatDau.getTime()));
            }, 
            calendarBatDau.get(Calendar.YEAR), 
            calendarBatDau.get(Calendar.MONTH), 
            calendarBatDau.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void saveContract() {
        // Kiểm tra dữ liệu đầu vào
        if (spinnerPhong.getSelectedItem() == null) {
            Toast.makeText(this, "Vui lòng chọn phòng!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spinnerKhachThue.getSelectedItem() == null) {
            Toast.makeText(this, "Vui lòng chọn khách thuê!", Toast.LENGTH_SHORT).show();
            return;
        }

        String thoiHanStr = etThoiHan.getText().toString().trim();
        if (thoiHanStr.isEmpty()) {
            etThoiHan.setError("Nhập thời hạn");
            return;
        }

        // Lấy Object từ Tag đã lưu
        List<Phong> listP = (List<Phong>) spinnerPhong.getTag();
        Phong phongChon = listP.get(spinnerPhong.getSelectedItemPosition());
        
        List<KhachThue> listK = (List<KhachThue>) spinnerKhachThue.getTag();
        KhachThue khachChon = listK.get(spinnerKhachThue.getSelectedItemPosition());

        // Tính toán ngày kết thúc
        int soThang = Integer.parseInt(thoiHanStr);
        Calendar calendarKetThuc = (Calendar) calendarBatDau.clone();
        calendarKetThuc.add(Calendar.MONTH, soThang);

        // Tạo đối tượng Hợp đồng
        HopDong hd = new HopDong();
        hd.setMaHopDong("HD-" + System.currentTimeMillis()); // Tạo mã tự động
        hd.setPhongId(phongChon.getId());
        hd.setKhachThueDaiDienId(khachChon.getId());
        hd.setNgayBatDau(tvNgayBatDau.getText().toString());
        hd.setNgayKetThuc(dateFormat.format(calendarKetThuc.getTime()));
        hd.setNgayKy(dateFormat.format(new Date()));
        hd.setGiaThueChot(phongChon.getGiaPhong());
        hd.setTienCoc(Double.parseDouble(etTienCoc.getText().toString().isEmpty() ? "0" : etTienCoc.getText().toString()));
        hd.setTrangThai(DatabaseHelper.TRANG_THAI_HOP_DONG_HIEU_LUC);
        hd.setGhiChu(etGhiChu.getText().toString());

        // THỰC HIỆN LƯU (Transaction-like logic)
        long result = hopDongRepo.addHopDong(hd);
        if (result > 0) {
            // QUAN TRỌNG: Cập nhật trạng thái phòng sang "Đang thuê"
            phongRepo.updateTrangThaiPhong(phongChon.getId(), DatabaseHelper.TRANG_THAI_PHONG_DANG_THUE);
            
            Toast.makeText(this, "Ký hợp đồng thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi lưu hợp đồng!", Toast.LENGTH_SHORT).show();
        }
    }
}
