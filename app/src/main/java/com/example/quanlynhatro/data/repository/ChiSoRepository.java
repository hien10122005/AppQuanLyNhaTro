package com.example.quanlynhatro.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.ChiSoDichVuThang;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * ChiSoRepository là lớp "kho dữ liệu" chuyên xử lý bảng chi_so_dich_vu_thang.
 * Nguyên tắc: Activity (màn hình) chỉ gọi Repository, không tự viết SQL.
 * Điều này giúp code dễ đọc, dễ bảo trì và tái sử dụng.
 */
public class ChiSoRepository {

    // Biến lưu trữ DatabaseHelper để dùng xuyên suốt class
    private final DatabaseHelper dbHelper;

    /**
     * Constructor: khởi tạo Repository, cần truyền vào Context (thường là Activity).
     * Context là "bối cảnh ứng dụng" để Android biết App nào đang chạy.
     */
    public ChiSoRepository(Context context) {
        // DatabaseHelper: Công cụ quản lý việc tạo bảng, nâng cấp và kết nối DB SQLite
        this.dbHelper = new DatabaseHelper(context);
    }

    // ==========================================================================
    //  PHẦN 1: LẤY CHỈ SỐ CŨ
    // ==========================================================================

    /**
     * Lấy chỉ số mới nhất (gần nhất) của một loại dịch vụ cho một phòng.
     * Mục đích: Khi nhân viên mở màn hình nhập điện/nước, hiển thị số cũ để so sánh.
     *
     * @param phongId     ID của phòng cần tra cứu
     * @param loaiDichVuId ID loại dịch vụ (1=Điện, 2=Nước...) — lấy từ bảng loai_dich_vu
     * @return            Số chỉ số mới nhất, hoặc 0.0 nếu chưa có lịch sử
     */
    /**
     * Lấy chỉ số mới nhất của một loại dịch vụ (Điện/Nước) cho một phòng.
     * Dùng để hiển thị làm 'Chỉ số cũ' khi người dùng bắt đầu chốt điện nước tháng mới.
     * @return Chỉ số mới nhất tìm thấy, hoặc 0.0 nếu đây là lần đầu tiên chốt cho phòng này
     */
    public double getChiSoCuMoiNhat(int phongId, int loaiDichVuId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Câu lệnh lấy chỉ số mới của bản ghi có thời gian gần nhất
        String sql = "SELECT " + DatabaseHelper.COL_CHI_SO_MOI
                + " FROM " + DatabaseHelper.TABLE_CHI_SO_DICH_VU_THANG
                + " WHERE " + DatabaseHelper.COL_CHI_SO_PHONG_ID + " = ?"
                + " AND "   + DatabaseHelper.COL_CHI_SO_LOAI_DICH_VU_ID + " = ?"
                + " ORDER BY " + DatabaseHelper.COL_CHI_SO_NAM + " DESC, "
                              + DatabaseHelper.COL_CHI_SO_THANG + " DESC"
                + " LIMIT 1";

        try (Cursor cursor = db.rawQuery(sql, new String[]{
                String.valueOf(phongId),
                String.valueOf(loaiDichVuId)
        })) {
            if (cursor.moveToFirst()) {
                return cursor.getDouble(0); // Trả về giá trị ở cột đầu tiên của kết quả
            }
        }
        return 0.0;
    }

    // ==========================================================================
    //  PHẦN 2: KIỂM TRA XEM THÁNG NÀY ĐÃ NHẬP CHƯA
    // ==========================================================================

    /**
     * Kiểm tra xem tháng/năm này đã có chỉ số cho phòng đó chưa.
     * Mục đích: Tránh nhập trùng 2 lần cho cùng một tháng.
     *
     * @param phongId     ID phòng
     * @param loaiDichVuId ID loại dịch vụ
     * @param thang       Tháng cần kiểm tra (1–12)
     * @param nam         Năm cần kiểm tra
     * @return            true nếu đã nhập rồi, false nếu chưa
     */
    public boolean daNhapChiSo(int phongId, int loaiDichVuId, int thang, int nam) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Đếm số bản ghi khớp với điều kiện (nếu > 0 là đã nhập rồi)
        String sql = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_CHI_SO_DICH_VU_THANG
                + " WHERE " + DatabaseHelper.COL_CHI_SO_PHONG_ID + " = ?"
                + " AND "   + DatabaseHelper.COL_CHI_SO_LOAI_DICH_VU_ID + " = ?"
                + " AND "   + DatabaseHelper.COL_CHI_SO_THANG + " = ?"
                + " AND "   + DatabaseHelper.COL_CHI_SO_NAM + " = ?";

        try (Cursor cursor = db.rawQuery(sql, new String[]{
                String.valueOf(phongId),
                String.valueOf(loaiDichVuId),
                String.valueOf(thang),
                String.valueOf(nam)
        })) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0) > 0; // Nếu đếm được > 0 bản ghi là đã nhập
            }
        }
        return false;
    }

    // ==========================================================================
    //  PHẦN 3: LẤY ID LOẠI DỊCH VỤ THEO MÃ
    // ==========================================================================

    /**
     * Tìm ID của loại dịch vụ dựa vào mã loại (ví dụ: "DIEN", "NUOC").
     * Lý do cần hàm này: Bảng chi_so lưu loaiDichVuId (số), không phải mã chữ.
     * Vì vậy ta cần tra cứu ID từ bảng loai_dich_vu trước.
     *
     * @param maLoai Mã loại dịch vụ, ví dụ DatabaseHelper.LOAI_DICH_VU_DIEN
     * @return       ID của loại dịch vụ, hoặc -1 nếu không tìm thấy
     */
    public int getLoaiDichVuId(String maLoai) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_LOAI_DICH_VU,        // Tên bảng cần query
                new String[]{DatabaseHelper.COL_ID},       // Chỉ lấy cột id
                DatabaseHelper.COL_LOAI_DICH_VU_MA_LOAI + " = ?", // Điều kiện WHERE
                new String[]{maLoai},                      // Giá trị cho dấu ?
                null, null, null
        )) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
            }
        }
        return -1; // Không tìm thấy
    }

    /**
     * Lấy thông tin chỉ số chi tiết cho một tháng/năm cụ thể.
     */
    public ChiSoDichVuThang getChiSoByThangNam(int phongId, int loaiDichVuId, int thang, int nam) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(DatabaseHelper.TABLE_CHI_SO_DICH_VU_THANG, null,
                DatabaseHelper.COL_CHI_SO_PHONG_ID + "=? AND " + DatabaseHelper.COL_CHI_SO_LOAI_DICH_VU_ID + "=? AND "
                + DatabaseHelper.COL_CHI_SO_THANG + "=? AND " + DatabaseHelper.COL_CHI_SO_NAM + "=?",
                new String[]{String.valueOf(phongId), String.valueOf(loaiDichVuId), String.valueOf(thang), String.valueOf(nam)},
                null, null, null)) {
            
            if (cursor.moveToFirst()) {
                ChiSoDichVuThang cs = new ChiSoDichVuThang();
                cs.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)));
                cs.setPhongId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CHI_SO_PHONG_ID)));
                cs.setLoaiDichVuId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CHI_SO_LOAI_DICH_VU_ID)));
                cs.setThang(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CHI_SO_THANG)));
                cs.setNam(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CHI_SO_NAM)));
                cs.setChiSoCu(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CHI_SO_CU)));
                cs.setChiSoMoi(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CHI_SO_MOI)));
                cs.setSoLuongTieuThu(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CHI_SO_SO_LUONG_TIEU_THU)));
                return cs;
            }
        }
        return null;
    }

    // ==========================================================================
    //  PHẦN 4: LƯU CHỈ SỐ MỚI VÀO DATABASE
    // ==========================================================================

    /**
     * Lưu chỉ số mới vào bảng chi_so_dich_vu_thang.
     * Hàm này được gọi khi người dùng nhấn nút "Lưu".
     *
     * @param phongId         ID phòng
     * @param loaiDichVuId    ID loại dịch vụ
     * @param thang           Tháng
     * @param nam             Năm
     * @param chiSoCu         Chỉ số tháng trước (để tham chiếu)
     * @param chiSoMoi        Chỉ số tháng này (người dùng vừa nhập)
     * @return                ID của bản ghi vừa tạo, hoặc -1 nếu lỗi
     */
    /**
     * Thực hiện lưu bản ghi chỉ số mới vào cơ sở dữ liệu
     * @param chiSoCu Chỉ số của tháng trước đó
     * @param chiSoMoi Chỉ số vừa ghi nhận được
     * @return ID dòng vừa chèn, hoặc -1 nếu thất bại
     */
    public long luuChiSo(int phongId, int loaiDichVuId,
                         int thang, int nam,
                         double chiSoCu, double chiSoMoi) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_CHI_SO_PHONG_ID,        phongId);
        values.put(DatabaseHelper.COL_CHI_SO_LOAI_DICH_VU_ID, loaiDichVuId);
        values.put(DatabaseHelper.COL_CHI_SO_THANG,           thang);
        values.put(DatabaseHelper.COL_CHI_SO_NAM,             nam);
        values.put(DatabaseHelper.COL_CHI_SO_CU,              chiSoCu);
        values.put(DatabaseHelper.COL_CHI_SO_MOI,             chiSoMoi);

        // Tính toán lượng tiêu thụ ngay tại đây để lưu vào DB
        double tieuThu = chiSoMoi - chiSoCu;
        values.put(DatabaseHelper.COL_CHI_SO_SO_LUONG_TIEU_THU, tieuThu);

        values.put(DatabaseHelper.COL_CHI_SO_NGAY_CHOT, ngayHomNay());
        values.put(DatabaseHelper.COL_CREATED_AT, nowFull());

        return db.insert(DatabaseHelper.TABLE_CHI_SO_DICH_VU_THANG, null, values);
    }

    // ==========================================================================
    //  PHẦN 5: CÁC HÀM TIỆN ÍCH (PRIVATE - nội bộ dùng thôi)
    // ==========================================================================

    /**
     * Trả về ngày hôm nay dạng "yyyy-MM-dd" (ví dụ: "2026-04-25")
     * Dùng để lưu vào cột ngay_chot.
     */
    private String ngayHomNay() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    /**
     * Trả về ngày giờ đầy đủ dạng "yyyy-MM-dd HH:mm:ss" (ví dụ: "2026-04-25 14:30:00")
     * Dùng để lưu vào cột created_at.
     */
    private String nowFull() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }
}
