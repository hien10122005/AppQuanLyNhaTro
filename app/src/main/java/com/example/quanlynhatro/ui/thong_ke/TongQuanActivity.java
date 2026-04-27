package com.example.quanlynhatro.ui.thong_ke;

// ============================================================
// IMPORT - Khai báo các thư viện cần dùng
// Giống như "import" trong Python, Java cần khai báo rõ ràng
// những lớp nào sẽ sử dụng trong file này.
// ============================================================
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.ui.chi_so.NhapChiSoActivity;
import com.example.quanlynhatro.ui.hoa_don.DanhSachHoaDonActivity;
import com.example.quanlynhatro.ui.khach_thue.DanhSachKhachThueActivity;
import com.example.quanlynhatro.ui.phong.DanhSachPhongActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * TongQuanActivity - Màn hình Tổng Quan (Dashboard)
 *
 * Đây là màn hình "trung tâm điều khiển" của ứng dụng.
 * Nhiệm vụ chính:
 *   1. Hiển thị ngày tháng hiện tại
 *   2. Đọc dữ liệu từ Database để tính toán số liệu thống kê
 *   3. Hiển thị các con số lên các View (TextView, ProgressBar)
 *   4. Xử lý sự kiện nhấn tab BottomNavigationView
 *
 * AppCompatActivity là lớp cha cung cấp các chức năng cơ bản
 * của một màn hình Android (Activity).
 */
public class TongQuanActivity extends AppCompatActivity {

    // ============================================================
    // KHAI BÁO BIẾN (Instance Variables)
    // Các biến ở đây được dùng xuyên suốt Activity.
    // ============================================================

    // DatabaseHelper: cầu nối giữa code Java và file SQLite
    private DatabaseHelper dbHelper;

    // --- Các View cần cập nhật dữ liệu ---
    // TextView là ô chữ trong XML, ta dùng biến Java để điều khiển nó
    private TextView tvNgayHienTai;       // Dòng hiển thị ngày tháng
    private TextView tvTongDoanhThu;      // Tổng doanh thu tháng
    private TextView tvDaThu;            // Tiền đã thu được
    private TextView tvChuaThu;          // Tiền chưa thu
    private TextView tvTongPhong;         // Tổng số phòng
    private TextView tvPhongDangO;        // Số phòng đang có người ở
    private TextView tvPhongTrong;        // Số phòng đang trống
    private TextView tvHoaDonQuaHan;      // Số hóa đơn quá hạn chưa thanh toán
    private TextView tvTiLeLapDay;        // Phần trăm tỉ lệ lấp đầy (vd: "80%")

    // ProgressBar: thanh tiến độ (xanh lá) thể hiện % thu tiền và % lấp đầy
    private ProgressBar pbTiLeThuTien;   // Thanh progress thu tiền trong Hero Card
    private ProgressBar pbLapDay;         // Thanh progress tỉ lệ lấp đầy
    private TextView btnProfile;          // Nút Profile trên header
    private TextView tvXemTatCaHoaDon;    // Nút "Xem tất cả" hóa đơn
    private LinearLayout layoutTongPhong, layoutPhongDangO, layoutPhongTrong, layoutHoaDonQuaHan;
    private LinearLayout btnQuickTenants, btnQuickContracts, btnQuickMeter, btnQuickMaintenance;


    // BottomNavigationView: thanh tab điều hướng ở dưới màn hình

    private BottomNavigationView bottomNavigation;

    // ============================================================
    // VÒNG ĐỜI ACTIVITY - onCreate()
    // Đây là hàm đầu tiên chạy khi Activity được tạo.
    // Giống như "main()" nhưng dành cho màn hình Android.
    // ============================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Luôn phải gọi super.onCreate() trước tiên
        // Đây là quy tắc bắt buộc của Android
        super.onCreate(savedInstanceState);

        // setContentView: "Hãy dùng file XML này làm giao diện cho màn hình này"
        // R.layout.activity_tong_quan tương ứng với file activity_tong_quan.xml
        setContentView(R.layout.activity_tong_quan);

        // --- BƯỚC 1: Khởi tạo DatabaseHelper ---
        // "this" = Activity hiện tại, đóng vai trò là Context
        dbHelper = new DatabaseHelper(this);

        // --- BƯỚC 2: Kết nối biến Java với View trong XML (findViewById) ---
        // findViewById("ID_TRONG_XML") trả về đối tượng View tương ứng
        // Sau bước này, biến Java và View trong XML đã "liên kết" với nhau
        khoiTaoViews();

        // --- BƯỚC 3: Hiển thị ngày tháng hôm nay ---
        hienThiNgayHomNay();

        // --- BƯỚC 4: Đọc dữ liệu từ DB và cập nhật lên màn hình ---
        taiDuLieuVaHienThi();

        // --- BƯỚC 5: Thiết lập sự kiện cho thanh điều hướng ---
        thietLapBottomNavigation();

        // --- BƯỚC 6: Thiết lập sự kiện click cho các thẻ Bento ---
        thietLapSuKienClick();
    }


    // ============================================================
    // BƯỚC 2: KHỞI TẠO VIEWS
    // Hàm này nối biến Java → View trong XML theo ID
    // ============================================================

    /**
     * Dùng findViewById() để tìm View trong layout XML theo android:id.
     * Sau khi gọi xong, các biến "tv..." và "pb..." đã trỏ đến View thật
     * trên màn hình và sẵn sàng để hiển thị dữ liệu.
     */
    private void khoiTaoViews() {
        tvNgayHienTai   = findViewById(R.id.tvNgayHienTai);
        tvTongDoanhThu  = findViewById(R.id.tvTongDoanhThu);
        tvDaThu         = findViewById(R.id.tvDaThu);
        tvChuaThu       = findViewById(R.id.tvChuaThu);
        tvTongPhong     = findViewById(R.id.tvTongPhong);
        tvPhongDangO    = findViewById(R.id.tvPhongDangO);
        tvPhongTrong    = findViewById(R.id.tvPhongTrong);
        tvHoaDonQuaHan  = findViewById(R.id.tvHoaDonQuaHan);
        tvTiLeLapDay    = findViewById(R.id.tvTiLeLapDay);
        pbTiLeThuTien   = findViewById(R.id.pbTiLeThuTien);
        pbLapDay        = findViewById(R.id.pbLapDay);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        btnProfile      = findViewById(R.id.btnProfile);
        tvXemTatCaHoaDon = findViewById(R.id.tvXemTatCaHoaDon);
        layoutTongPhong = findViewById(R.id.layoutTongPhong);
        layoutPhongDangO = findViewById(R.id.layoutPhongDangO);
        layoutPhongTrong = findViewById(R.id.layoutPhongTrong);
        layoutHoaDonQuaHan = findViewById(R.id.layoutHoaDonQuaHan);
        
        btnQuickTenants = findViewById(R.id.btnQuickTenants);
        btnQuickContracts = findViewById(R.id.btnQuickContracts);
        btnQuickMeter = findViewById(R.id.btnQuickMeter);
        btnQuickMaintenance = findViewById(R.id.btnQuickMaintenance);
    }



    // ============================================================
    // BƯỚC 3: HIỂN THỊ NGÀY THÁNG
    // ============================================================

    /**
     * Lấy ngày giờ hiện tại từ hệ thống và định dạng thành chuỗi tiếng Việt.
     *
     * Calendar.getInstance() = lấy đối tượng lịch hiện tại của hệ thống.
     * SimpleDateFormat = lớp định dạng ngày tháng theo pattern tùy chọn.
     */
    private void hienThiNgayHomNay() {
        // Lấy ngày hiện tại (ví dụ: Thứ Sáu, 25 Tháng 4, 2026)
        // "EEEE" = tên thứ đầy đủ, "d" = ngày, "MMMM" = tên tháng, "yyyy" = năm
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM, yyyy", new Locale("vi", "VN"));
        String ngayHomNay = sdf.format(new Date());

        // Chuyển ký tự đầu thành hoa (vd: "thứ sáu" → "Thứ sáu")
        ngayHomNay = Character.toUpperCase(ngayHomNay.charAt(0)) + ngayHomNay.substring(1);

        // setText() = thay đổi nội dung hiển thị của TextView
        tvNgayHienTai.setText(ngayHomNay);
    }

    // ============================================================
    // BƯỚC 4: TẢI DỮ LIỆU TỪ DATABASE VÀ HIỂN THỊ
    // ============================================================

    /**
     * Đây là hàm "tổng điều phối" - gọi lần lượt các hàm con để:
     *   1. Đếm số phòng → hiển thị Bento Grid
     *   2. Tính tiền hóa đơn → hiển thị Hero Card
     *   3. Tính tỉ lệ lấp đầy → cập nhật ProgressBar
     */
    private void taiDuLieuVaHienThi() {
        // Lấy tháng và năm hiện tại để lọc dữ liệu đúng kỳ
        Calendar cal = Calendar.getInstance();
        int thangHienTai = cal.get(Calendar.MONTH) + 1; // Calendar.MONTH bắt đầu từ 0 nên +1
        int namHienTai   = cal.get(Calendar.YEAR);

        // Gọi lần lượt từng hàm tính toán
        capNhatThongKePhong();
        capNhatThongKeHoaDon(thangHienTai, namHienTai);
    }

    /**
     * Đọc bảng PHONG từ Database để đếm:
     * - Tổng số phòng
     * - Số phòng đang có người ở (trạng thái = DANG_THUE)
     * - Số phòng trống (trạng thái = TRONG)
     *
     * Cursor: giống như con trỏ duyệt qua từng dòng kết quả của câu SQL.
     */
    private void capNhatThongKePhong() {
        // Mở kết nối database ở chế độ chỉ đọc (không cần ghi)
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // --- Đếm tổng số phòng ---
        // rawQuery() chạy câu SQL thuần, trả về Cursor
        Cursor curTong = db.rawQuery(
                "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_PHONG,
                null // null = không có tham số
        );
        int tongPhong = 0;
        if (curTong.moveToFirst()) { // moveToFirst() = chuyển cursor đến dòng đầu tiên
            tongPhong = curTong.getInt(0); // getInt(0) = lấy giá trị cột đầu tiên (index 0)
        }
        curTong.close(); // QUAN TRỌNG: phải đóng Cursor sau khi dùng xong để giải phóng bộ nhớ

        // --- Đếm phòng đang có người ở ---
        // Dùng "?" làm placeholder, giá trị thật truyền vào mảng String phía sau
        // Cách này an toàn hơn nối String trực tiếp (tránh lỗi SQL Injection)
        Cursor curDangO = db.rawQuery(
                "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_PHONG
                + " WHERE " + DatabaseHelper.COL_PHONG_TRANG_THAI + " = ?",
                new String[]{DatabaseHelper.TRANG_THAI_PHONG_DANG_THUE}
        );
        int phongDangO = 0;
        if (curDangO.moveToFirst()) {
            phongDangO = curDangO.getInt(0);
        }
        curDangO.close();

        // --- Đếm phòng trống ---
        Cursor curTrong = db.rawQuery(
                "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_PHONG
                + " WHERE " + DatabaseHelper.COL_PHONG_TRANG_THAI + " = ?",
                new String[]{DatabaseHelper.TRANG_THAI_PHONG_TRONG}
        );
        int phongTrong = 0;
        if (curTrong.moveToFirst()) {
            phongTrong = curTrong.getInt(0);
        }
        curTrong.close();

        // --- Cập nhật lên giao diện ---
        // String.valueOf() = chuyển số int thành String để setText()
        tvTongPhong.setText(String.valueOf(tongPhong));
        tvPhongDangO.setText(String.valueOf(phongDangO));
        tvPhongTrong.setText(String.valueOf(phongTrong));

        // --- Tính và hiển thị tỉ lệ lấp đầy ---
        // Tránh chia cho 0 khi chưa có phòng nào
        if (tongPhong > 0) {
            int phanTram = (int) ((phongDangO * 100.0) / tongPhong);
            tvTiLeLapDay.setText(phanTram + "%");
            pbLapDay.setProgress(phanTram); // setProgress(n) = đặt thanh progress ở mức n/100
        } else {
            tvTiLeLapDay.setText("0%");
            pbLapDay.setProgress(0);
        }
    }

    /**
     * Đọc bảng HOA_DON để tính toán tình hình tài chính tháng này:
     * - Tổng tiền phải thu (SUM tong_tien của tháng hiện tại)
     * - Tiền đã thu (SUM da_thanh_toan)
     * - Tiền còn nợ (SUM con_no)
     * - Số hóa đơn quá hạn (trạng thái = CHUA_THANH_TOAN)
     *
     * @param thang  Tháng hiện tại (1-12)
     * @param nam    Năm hiện tại
     */
    private void capNhatThongKeHoaDon(int thang, int nam) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // --- Tính tổng tiền phải thu, đã thu, còn nợ của tháng này ---
        // SUM() = hàm tính tổng trong SQL
        // COALESCE(SUM(...), 0) = nếu SUM trả về NULL (khi chưa có hóa đơn nào)
        //                         thì dùng giá trị 0 thay thế
        Cursor curTien = db.rawQuery(
                "SELECT "
                + "COALESCE(SUM(" + DatabaseHelper.COL_HOA_DON_TONG_TIEN + "), 0), "
                + "COALESCE(SUM(" + DatabaseHelper.COL_HOA_DON_DA_THANH_TOAN + "), 0), "
                + "COALESCE(SUM(" + DatabaseHelper.COL_HOA_DON_CON_NO + "), 0) "
                + "FROM " + DatabaseHelper.TABLE_HOA_DON
                + " WHERE " + DatabaseHelper.COL_HOA_DON_THANG + " = ?"
                + " AND "   + DatabaseHelper.COL_HOA_DON_NAM   + " = ?",
                new String[]{String.valueOf(thang), String.valueOf(nam)}
        );

        double tongTien = 0, daThu = 0, conNo = 0;
        if (curTien.moveToFirst()) {
            tongTien = curTien.getDouble(0); // Cột 0: SUM(tong_tien)
            daThu    = curTien.getDouble(1); // Cột 1: SUM(da_thanh_toan)
            conNo    = curTien.getDouble(2); // Cột 2: SUM(con_no)
        }
        curTien.close();

        // --- Đếm hóa đơn quá hạn (chưa thanh toán XONG và đã qua ngày hạn) ---
        // Logic: han_thanh_toan < ngày_hiện_tại AND trang_thai != 'DA_THANH_TOAN'
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Cursor curQuaHan = db.rawQuery(
                "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_HOA_DON
                + " WHERE " + DatabaseHelper.COL_HOA_DON_HAN_THANH_TOAN + " < ?"
                + " AND " + DatabaseHelper.COL_HOA_DON_TRANG_THAI + " != ?",
                new String[]{today, DatabaseHelper.TRANG_THAI_HOA_DON_DA_THANH_TOAN}
        );
        int soQuaHan = 0;
        if (curQuaHan.moveToFirst()) {
            soQuaHan = curQuaHan.getInt(0);
        }
        curQuaHan.close();

        // --- Định dạng số tiền sang dạng có dấu phẩy ---
        // NumberFormat.getNumberInstance() → tự thêm dấu phẩy ngăn cách hàng nghìn
        // Ví dụ: 1250000 → "1,250,000"
        NumberFormat fmt = NumberFormat.getNumberInstance(Locale.getDefault());
        tvTongDoanhThu.setText(fmt.format(tongTien) + " đ");
        tvDaThu.setText(fmt.format(daThu) + " đ");
        tvChuaThu.setText(fmt.format(conNo) + " đ");
        tvHoaDonQuaHan.setText(String.valueOf(soQuaHan));

        // --- Cập nhật Progress Bar tỉ lệ thu tiền ---
        if (tongTien > 0) {
            int phanTramThu = (int) ((daThu * 100.0) / tongTien);
            pbTiLeThuTien.setProgress(phanTramThu);
        } else {
            // Nếu tháng này chưa có hóa đơn nào, để progress = 0
            pbTiLeThuTien.setProgress(0);
        }
    }

    // ============================================================
    // BƯỚC 5: THIẾT LẬP BOTTOM NAVIGATION
    // ============================================================

    /**
     * Xử lý sự kiện khi người dùng nhấn vào các tab ở thanh điều hướng.
     *
     * Intent: "ý định" chuyển màn hình.
     * startActivity(intent): thực sự chuyển sang màn hình mới.
     * finish(): đóng màn hình hiện tại sau khi chuyển (tùy chọn).
     */
    private void thietLapBottomNavigation() {
        // Đánh dấu tab "Trang chủ" đang được chọn (hiển thị màu xanh)
        bottomNavigation.setSelectedItemId(R.id.nav_home);

        // setOnItemSelectedListener: lắng nghe khi người dùng nhấn một tab bất kỳ
        bottomNavigation.setOnItemSelectedListener(new com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // item.getItemId() trả về ID của tab vừa được nhấn
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    // Đã ở trang chủ rồi, không cần làm gì
                    return true;

                } else if (id == R.id.nav_rooms) {
                    // Chuyển sang màn hình Danh Sách Phòng
                    // Intent(context, DestinationActivity.class) = tạo lệnh chuyển màn hình
                    startActivity(new Intent(TongQuanActivity.this, DanhSachPhongActivity.class));
                    return true;

                } else if (id == R.id.nav_invoices) {
                    // Chuyển sang màn hình Danh Sách Hóa Đơn
                    startActivity(new Intent(TongQuanActivity.this, DanhSachHoaDonActivity.class));
                    return true;

                } else if (id == R.id.nav_reports) {
                    // Chuyển sang màn hình Báo cáo & Thống kê
                    startActivity(new Intent(TongQuanActivity.this, com.example.quanlynhatro.ui.thong_ke.BaoCaoActivity.class));
                    return true;

                } else if (id == R.id.nav_settings) {
                    // Chuyển sang màn hình Cài đặt
                    startActivity(new Intent(TongQuanActivity.this, com.example.quanlynhatro.ui.setting.CaiDatActivity.class));
                    return true;
                }


                return false; // Trả về false nếu không xử lý được tab nào
            }
        });
    }

    /**
     * Thiết lập sự kiện click cho các thẻ Bento Grid và các nút khác trên dashboard.
     */
    private void thietLapSuKienClick() {
        // Nhấn vào Profile -> Cài đặt
        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, com.example.quanlynhatro.ui.setting.CaiDatActivity.class));
        });

        // Nhấn vào thẻ Tổng Phòng -> Danh sách phòng
        layoutTongPhong.setOnClickListener(v -> {
            startActivity(new Intent(this, DanhSachPhongActivity.class));
        });

        // Nhấn vào thẻ Đang Ở -> Danh sách phòng (lọc Đang thuê)
        layoutPhongDangO.setOnClickListener(v -> {
            Intent intent = new Intent(this, DanhSachPhongActivity.class);
            intent.putExtra("FILTER_STATUS", "Đang thuê");
            startActivity(intent);
        });

        // Nhấn vào thẻ Phòng Trống -> Danh sách phòng (lọc Trống)
        layoutPhongTrong.setOnClickListener(v -> {
            Intent intent = new Intent(this, DanhSachPhongActivity.class);
            intent.putExtra("FILTER_STATUS", "Trống");
            startActivity(intent);
        });

        // Nhấn vào thẻ Quá Hạn -> Danh sách hóa đơn (lọc Quá hạn)
        layoutHoaDonQuaHan.setOnClickListener(v -> {
            Intent intent = new Intent(this, DanhSachHoaDonActivity.class);
            intent.putExtra("FILTER_STATUS", "QUA_HAN");
            startActivity(intent);
        });

        // Nhấn vào "Xem tất cả" hóa đơn
        tvXemTatCaHoaDon.setOnClickListener(v -> {
            startActivity(new Intent(this, DanhSachHoaDonActivity.class));
        });

        // --- Tiện ích nhanh ---
        btnQuickTenants.setOnClickListener(v -> {
            startActivity(new Intent(this, com.example.quanlynhatro.ui.khach_thue.DanhSachKhachThueActivity.class));
        });

        btnQuickContracts.setOnClickListener(v -> {
            startActivity(new Intent(this, com.example.quanlynhatro.ui.hop_dong.DanhSachHopDongActivity.class));
        });

        btnQuickMeter.setOnClickListener(v -> {
            startActivity(new Intent(this, com.example.quanlynhatro.ui.chi_so.NhapChiSoActivity.class));
        });

        btnQuickMaintenance.setOnClickListener(v -> {
            startActivity(new Intent(this, com.example.quanlynhatro.ui.bao_tri.DanhSachBaoTriActivity.class));
        });
    }


    // ============================================================

    // VÒNG ĐỜI ACTIVITY - onResume()
    // Hàm này chạy mỗi khi màn hình được hiển thị lại
    // (ví dụ: người dùng nhấn Back từ màn hình khác quay về)
    // ============================================================

    @Override
    protected void onResume() {
        super.onResume();
        // Tải lại dữ liệu khi quay về Dashboard để đảm bảo số liệu luôn mới nhất
        // Ví dụ: Người dùng vừa thu tiền ở màn hình khác → quay lại Dashboard
        //        → Dashboard cần hiển thị lại số "Đã Thu" đã được cập nhật
        taiDuLieuVaHienThi();

        // --- CẢNH BÁO THÔNG MINH (Smart Warning) ---
        // Nếu có hóa đơn quá hạn, hiện thông báo nhắc nhở nhẹ nhàng
        int soQuaHan = Integer.parseInt(tvHoaDonQuaHan.getText().toString());
        if (soQuaHan > 0) {
            android.widget.Toast.makeText(this, 
                "⚠️ Bạn có " + soQuaHan + " hóa đơn đã quá hạn thanh toán!", 
                android.widget.Toast.LENGTH_LONG).show();
        }
    }

    // ============================================================
    // VÒNG ĐỜI ACTIVITY - onDestroy()
    // Hàm này chạy khi màn hình bị đóng hoàn toàn
    // ============================================================

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đóng kết nối Database để giải phóng tài nguyên hệ thống
        // Nếu không đóng, ứng dụng có thể bị rò rỉ bộ nhớ (memory leak)
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
