package com.example.quanlynhatro.ui.chi_so;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.Phong;
import com.example.quanlynhatro.data.repository.ChiSoRepository;
import com.example.quanlynhatro.data.repository.PhongRepository;

import java.util.Calendar;
import java.util.List;

/**
 * NhapChiSoActivity: Màn hình để nhân viên nhập chỉ số điện và nước hàng tháng.
 *
 * LUỒNG HOẠT ĐỘNG:
 * 1. Khi mở màn hình → Tải danh sách phòng vào Spinner (ô chọn phòng)
 * 2. Người dùng chọn phòng → Tự động hiển thị chỉ số cũ (tháng trước) từ DB
 * 3. Người dùng gõ chỉ số mới → Tự động tính "tiêu thụ = mới - cũ"
 * 4. Nhấn "Lưu" → Kiểm tra hợp lệ, lưu vào DB, thông báo thành công
 */
public class NhapChiSoActivity extends AppCompatActivity {

    // ===== KHAI BÁO CÁC BIẾN THÀNH VIÊN (instance variables) =====
    // Các biến này được khai báo ở đầu class để dùng được trong nhiều hàm khác nhau

    // --- View (thành phần giao diện) ---
    private Spinner spinnerPhong;           // Ô dropdown chọn phòng
    private TextView tvThangNam;            // Hiển thị "Tháng 4/2026"
    private TextView tvDienChiSoCu;         // Hiển thị chỉ số điện tháng trước
    private TextView tvDienTieuThu;         // Hiển thị điện tiêu thụ (tính tự động)
    private EditText etDienMoi;             // Ô nhập chỉ số điện mới
    private TextView tvNuocChiSoCu;         // Hiển thị chỉ số nước tháng trước
    private TextView tvNuocTieuThu;         // Hiển thị nước tiêu thụ (tính tự động)
    private EditText etNuocMoi;             // Ô nhập chỉ số nước mới
    private Button btnLuuChiSo;             // Nút lưu
    private android.widget.ImageButton btnBack; // Nút quay lại


    // --- Data (dữ liệu) ---
    private List<Phong> danhSachPhong;      // Danh sách phòng lấy từ DB
    private PhongRepository phongRepo;      // Kho dữ liệu phòng
    private ChiSoRepository chiSoRepo;      // Kho dữ liệu chỉ số
    private int phongDuocChon = -1;         // ID phòng đang được chọn (-1 = chưa chọn)
    private int idDichVuDien  = -1;         // ID loại dịch vụ "Điện" trong DB
    private int idDichVuNuoc  = -1;         // ID loại dịch vụ "Nước" trong DB

    // --- Chỉ số cũ (tháng trước) ---
    private double dienChiSoCu = 0.0;       // Số điện cũ để tính tiêu thụ
    private double nuocChiSoCu = 0.0;       // Số nước cũ để tính tiêu thụ

    // --- Thông tin tháng/năm hiện tại ---
    private int thangHienTai;
    private int namHienTai;

    // ==========================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_chi_so);

        // BƯỚC 1: Khởi tạo các Repository (kho dữ liệu)
        phongRepo  = new PhongRepository(this);
        chiSoRepo  = new ChiSoRepository(this);

        // BƯỚC 2: Lấy tháng/năm hiện tại từ hệ thống
        // Calendar.getInstance() trả về đối tượng lịch hiện tại
        Calendar cal   = Calendar.getInstance();
        thangHienTai   = cal.get(Calendar.MONTH) + 1; // MONTH bắt đầu từ 0, nên +1
        namHienTai     = cal.get(Calendar.YEAR);

        // BƯỚC 3: Lấy ID loại dịch vụ (Điện, Nước) từ DB một lần để dùng lại nhiều lần
        idDichVuDien = chiSoRepo.getLoaiDichVuId(DatabaseHelper.LOAI_DICH_VU_DIEN);
        idDichVuNuoc = chiSoRepo.getLoaiDichVuId(DatabaseHelper.LOAI_DICH_VU_NUOC);

        // BƯỚC 4: Ánh xạ (liên kết) biến Java với các View trong file XML
        anhXaCacView();

        // BƯỚC 5: Hiển thị tháng/năm lên màn hình
        tvThangNam.setText(String.format("Nhập cho Tháng %d/%d", thangHienTai, namHienTai));

        // BƯỚC 6: Tải danh sách phòng và đổ vào Spinner
        taiDanhSachPhong();

        // BƯỚC 7: Đăng ký sự kiện lắng nghe (listeners)
        // Khi người dùng chọn phòng → cập nhật chỉ số cũ
        setupSpinnerPhong();
        // Khi người dùng gõ chỉ số mới → tự tính tiêu thụ
        setupTinhTieuThuTuDong();
        // Khi nhấn nút Lưu → kiểm tra và lưu vào DB
        btnLuuChiSo.setOnClickListener(v -> xuLyLuuChiSo());
        
        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());
    }


    /**
     * Ánh xạ (Map): Kết nối biến Java với ID View trong file XML.
     * Quy tắc: Tên biến Java phải khớp với android:id trong XML.
     */
    private void anhXaCacView() {
        spinnerPhong   = findViewById(R.id.spinnerPhong);
        tvThangNam     = findViewById(R.id.tvThangNam);
        tvDienChiSoCu  = findViewById(R.id.tvDienChiSoCu);
        tvDienTieuThu  = findViewById(R.id.tvDienTieuThu);
        etDienMoi      = findViewById(R.id.etDienMoi);
        tvNuocChiSoCu  = findViewById(R.id.tvNuocChiSoCu);
        tvNuocTieuThu  = findViewById(R.id.tvNuocTieuThu);
        etNuocMoi      = findViewById(R.id.etNuocMoi);
        btnLuuChiSo    = findViewById(R.id.btnLuuChiSo);
        btnBack        = findViewById(R.id.btnBack);
    }


    /**
     * Lấy danh sách phòng từ DB và hiển thị lên Spinner.
     * ArrayAdapter là "cầu nối" giữa dữ liệu (List) và View (Spinner).
     */
    private void taiDanhSachPhong() {
        // Lấy tất cả phòng từ database
        danhSachPhong = phongRepo.getAllPhong();

        if (danhSachPhong.isEmpty()) {
            Toast.makeText(this, "Chưa có phòng nào trong hệ thống!", Toast.LENGTH_LONG).show();
            return;
        }

        // Tạo mảng tên phòng để hiển thị trong Spinner
        // Ví dụ: ["Phòng 101 - Phòng đơn", "Phòng 102 - Phòng đôi", ...]
        String[] tenPhong = new String[danhSachPhong.size()];
        for (int i = 0; i < danhSachPhong.size(); i++) {
            Phong p = danhSachPhong.get(i);
            String so = p.getSoPhong();
            String ten = p.getTenPhong();
            
            // Nếu tên phòng đã chứa số phòng (vd: "Phòng 101" đã có "101"), chỉ hiện tên
            // Nếu tên khác số hoàn toàn (vd: "101" - "Phòng đơn"), hiện cả hai
            if (ten != null && !ten.isEmpty()) {
                if (ten.contains(so)) {
                    tenPhong[i] = ten;
                } else {
                    tenPhong[i] = "Phòng " + so + " - " + ten;
                }
            } else {
                tenPhong[i] = "Phòng " + so;
            }
        }

        // ArrayAdapter.createFromResource không dùng được vì data động,
        // nên ta dùng ArrayAdapter(Context, layoutItem, mảngData)
        // Sử dụng layout tùy chỉnh item_spinner.xml để hiển thị chữ trắng
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.item_spinner,
                tenPhong
        );
        // Đặt layout cho phần dropdown (phần hiện ra khi nhấn)
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinnerPhong.setAdapter(adapter);
    }

    /**
     * Thiết lập sự kiện khi người dùng chọn phòng từ Spinner.
     * OnItemSelectedListener sẽ được gọi mỗi khi lựa chọn thay đổi.
     */
    private void setupSpinnerPhong() {
        spinnerPhong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            /**
             * Được gọi khi người dùng chọn một mục trong Spinner.
             * @param position Vị trí của mục được chọn (0, 1, 2...)
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy đối tượng Phong tương ứng với vị trí được chọn
                Phong phongChon = danhSachPhong.get(position);
                phongDuocChon = phongChon.getId(); // Lưu ID phòng để dùng khi lưu

                // Cập nhật hiển thị chỉ số cũ theo phòng mới được chọn
                capNhatChiSoCu(phongDuocChon);

                // Xóa dữ liệu đang nhập trong EditText khi chuyển phòng
                // để tránh nhầm lẫn giữa các phòng
                etDienMoi.setText("");
                etNuocMoi.setText("");
            }

            /**
             * Được gọi khi không có mục nào được chọn (hiếm gặp).
             * Ta không cần xử lý trường hợp này.
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) { /* Bỏ qua */ }
        });
    }

    /**
     * Truy vấn DB để lấy chỉ số cũ (tháng trước) cho phòng được chọn.
     * Sau đó hiển thị lên các TextView tương ứng.
     *
     * @param phongId ID phòng cần tra cứu
     */
    private void capNhatChiSoCu(int phongId) {
        // Lấy chỉ số điện cũ từ Repository (Repository tự query DB)
        dienChiSoCu = chiSoRepo.getChiSoCuMoiNhat(phongId, idDichVuDien);
        nuocChiSoCu = chiSoRepo.getChiSoCuMoiNhat(phongId, idDichVuNuoc);

        // Hiển thị chỉ số cũ lên màn hình
        // String.format("%.1f", số) = định dạng 1 chữ số sau dấu phẩy, ví dụ: 1250.0
        tvDienChiSoCu.setText(String.format("Chỉ số cũ: %.1f kWh", dienChiSoCu));
        tvNuocChiSoCu.setText(String.format("Chỉ số cũ: %.1f m³",  nuocChiSoCu));

        // Reset tiêu thụ về "--" vì chưa nhập số mới
        tvDienTieuThu.setText("Tiêu thụ: -- kWh");
        tvNuocTieuThu.setText("Tiêu thụ: -- m³");
    }

    /**
     * Thiết lập tính tiêu thụ TỰ ĐỘNG khi người dùng đang gõ.
     * TextWatcher = lắng nghe sự thay đổi trong EditText theo từng ký tự.
     */
    private void setupTinhTieuThuTuDong() {

        // TextWatcher cho ô nhập chỉ số ĐIỆN mới
        etDienMoi.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Hàm này được gọi SAU KHI text đã thay đổi
                tinhVaHienThiTieuThu(s.toString(), dienChiSoCu, tvDienTieuThu, "kWh");
            }
            // Hai hàm dưới không cần xử lý, nhưng bắt buộc phải khai báo vì là interface
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // TextWatcher cho ô nhập chỉ số NƯỚC mới
        etNuocMoi.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                tinhVaHienThiTieuThu(s.toString(), nuocChiSoCu, tvNuocTieuThu, "m³");
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    /**
     * Tính lượng tiêu thụ và cập nhật TextView hiển thị.
     * Công thức: Tiêu thụ = Chỉ số mới - Chỉ số cũ
     *
     * @param inputText  Chuỗi người dùng vừa gõ vào EditText
     * @param chiSoCu    Chỉ số tháng trước (đã lấy từ DB)
     * @param tvTieuThu  TextView cần cập nhật kết quả
     * @param donVi      Đơn vị hiển thị ("kWh" hoặc "m³")
     */
    private void tinhVaHienThiTieuThu(String inputText, double chiSoCu,
                                       TextView tvTieuThu, String donVi) {
        // Nếu ô nhập trống → hiển thị "--"
        if (inputText.isEmpty()) {
            tvTieuThu.setText("Tiêu thụ: -- " + donVi);
            return;
        }

        try {
            // Double.parseDouble() chuyển chuỗi "1300" thành số 1300.0
            double chiSoMoi = Double.parseDouble(inputText);
            double tieuThu  = chiSoMoi - chiSoCu;

            if (tieuThu < 0) {
                // Chỉ số mới không thể nhỏ hơn chỉ số cũ → báo lỗi
                tvTieuThu.setText("⚠️ Chỉ số không hợp lệ!");
            } else {
                tvTieuThu.setText(String.format("Tiêu thụ: %.1f %s", tieuThu, donVi));
            }
        } catch (NumberFormatException e) {
            // Người dùng gõ ký tự không phải số (dù inputType đã giới hạn, vẫn nên bắt lỗi)
            tvTieuThu.setText("Tiêu thụ: -- " + donVi);
        }
    }

    /**
     * Xử lý khi người dùng nhấn nút "Lưu Chỉ Số".
     * Quy trình: Kiểm tra hợp lệ → Kiểm tra trùng lặp → Lưu điện → Lưu nước → Thông báo.
     */
    private void xuLyLuuChiSo() {

        // --- KIỂM TRA 1: Đã chọn phòng chưa? ---
        if (phongDuocChon == -1 || danhSachPhong == null || danhSachPhong.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn phòng!", Toast.LENGTH_SHORT).show();
            return; // Dừng hàm lại, không làm gì thêm
        }

        // --- KIỂM TRA 2: Đã nhập ít nhất một chỉ số chưa? ---
        String dienMoiStr = etDienMoi.getText().toString().trim();
        String nuocMoiStr = etNuocMoi.getText().toString().trim();

        if (dienMoiStr.isEmpty() && nuocMoiStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập ít nhất một chỉ số!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Biến đếm xem lưu được bao nhiêu mục thành công
        int soMucDaLuu = 0;

        // --- LƯU CHỈ SỐ ĐIỆN (nếu người dùng có nhập) ---
        if (!dienMoiStr.isEmpty() && idDichVuDien != -1) {
            double dienMoi = Double.parseDouble(dienMoiStr);

            // Kiểm tra chỉ số mới phải >= chỉ số cũ
            if (dienMoi < dienChiSoCu) {
                Toast.makeText(this,
                        "Chỉ số điện mới không được nhỏ hơn chỉ số cũ (" + dienChiSoCu + ")",
                        Toast.LENGTH_LONG).show();
                return;
            }

            // Kiểm tra tháng này đã nhập chưa để tránh trùng lặp
            if (chiSoRepo.daNhapChiSo(phongDuocChon, idDichVuDien, thangHienTai, namHienTai)) {
                Toast.makeText(this,
                        "Chỉ số điện tháng " + thangHienTai + "/" + namHienTai + " đã được nhập rồi!",
                        Toast.LENGTH_LONG).show();
                return;
            }

            // Gọi Repository để lưu vào DB, nhận về ID bản ghi vừa tạo
            long idVuaTao = chiSoRepo.luuChiSo(
                    phongDuocChon, idDichVuDien,
                    thangHienTai, namHienTai,
                    dienChiSoCu, dienMoi
            );

            // Nếu id > 0 là lưu thành công, -1 là thất bại
            if (idVuaTao > 0) soMucDaLuu++;
        }

        // --- LƯU CHỈ SỐ NƯỚC (nếu người dùng có nhập) ---
        if (!nuocMoiStr.isEmpty() && idDichVuNuoc != -1) {
            double nuocMoi = Double.parseDouble(nuocMoiStr);

            if (nuocMoi < nuocChiSoCu) {
                Toast.makeText(this,
                        "Chỉ số nước mới không được nhỏ hơn chỉ số cũ (" + nuocChiSoCu + ")",
                        Toast.LENGTH_LONG).show();
                return;
            }

            if (chiSoRepo.daNhapChiSo(phongDuocChon, idDichVuNuoc, thangHienTai, namHienTai)) {
                Toast.makeText(this,
                        "Chỉ số nước tháng " + thangHienTai + "/" + namHienTai + " đã được nhập rồi!",
                        Toast.LENGTH_LONG).show();
                return;
            }

            long idVuaTao = chiSoRepo.luuChiSo(
                    phongDuocChon, idDichVuNuoc,
                    thangHienTai, namHienTai,
                    nuocChiSoCu, nuocMoi
            );
            if (idVuaTao > 0) soMucDaLuu++;
        }

        // --- THÔNG BÁO KẾT QUẢ ---
        if (soMucDaLuu > 0) {
            Toast.makeText(this,
                    "✅ Đã lưu " + soMucDaLuu + " chỉ số thành công!",
                    Toast.LENGTH_SHORT).show();

            // Xóa dữ liệu nhập sau khi lưu thành công để người dùng nhập phòng tiếp theo
            etDienMoi.setText("");
            etNuocMoi.setText("");

            // Cập nhật lại chỉ số cũ (vì vừa lưu, chỉ số mới trở thành chỉ số cũ)
            capNhatChiSoCu(phongDuocChon);
        } else {
            Toast.makeText(this, "❌ Lưu thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }
}
