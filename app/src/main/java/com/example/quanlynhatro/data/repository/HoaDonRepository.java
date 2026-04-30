package com.example.quanlynhatro.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.HoaDon;
import com.example.quanlynhatro.data.model.HoaDonVm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HoaDonRepository {
    private final DatabaseHelper dbHelper;

    public HoaDonRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Thêm mới một hóa đơn vào cơ sở dữ liệu
     * @return ID của hóa đơn vừa tạo
     */
    public long addHoaDon(HoaDon hoaDon) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String now = now();
        // Chuyển đổi đối tượng sang ContentValues để chuẩn bị INSERT
        ContentValues values = toContentValues(hoaDon, hoaDon.getCreatedAt() != null ? hoaDon.getCreatedAt() : now);
        values.put(DatabaseHelper.COL_UPDATED_AT, hoaDon.getUpdatedAt() != null ? hoaDon.getUpdatedAt() : now);
        return db.insert(DatabaseHelper.TABLE_HOA_DON, null, values);
    }

    /**
     * Lưu chi tiết các mục trong hóa đơn (Điện, nước, tiền phòng, dịch vụ khác...)
     */
    public long addHoaDonChiTiet(com.example.quanlynhatro.data.model.HoaDonChiTiet ct) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_HOA_DON_CT_HOA_DON_ID, ct.getHoaDonId());
        values.put(DatabaseHelper.COL_HOA_DON_CT_LOAI_DICH_VU_ID, ct.getLoaiDichVuId());
        values.put(DatabaseHelper.COL_HOA_DON_CT_TEN_MUC_PHI, ct.getTenMucPhi());
        values.put(DatabaseHelper.COL_HOA_DON_CT_SO_LUONG, ct.getSoLuong());
        values.put(DatabaseHelper.COL_HOA_DON_CT_DON_GIA_AP_DUNG, ct.getDonGiaApDung());
        values.put(DatabaseHelper.COL_HOA_DON_CT_THANH_TIEN, ct.getThanhTien());
        
        // Nếu là dịch vụ có chỉ số (điện, nước) thì lưu thêm số cũ/mới
        if (ct.getChiSoCu() != null) values.put(DatabaseHelper.COL_HOA_DON_CT_CHI_SO_CU, ct.getChiSoCu());
        if (ct.getChiSoMoi() != null) values.put(DatabaseHelper.COL_HOA_DON_CT_CHI_SO_MOI, ct.getChiSoMoi());
        
        values.put(DatabaseHelper.COL_GHI_CHU, ct.getGhiChu());
        return db.insert(DatabaseHelper.TABLE_HOA_DON_CHI_TIET, null, values);
    }

    public int updateHoaDon(HoaDon hoaDon) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = toContentValues(hoaDon, hoaDon.getCreatedAt());
        values.remove(DatabaseHelper.COL_CREATED_AT);
        values.put(DatabaseHelper.COL_UPDATED_AT, now());
        return db.update(
                DatabaseHelper.TABLE_HOA_DON,
                values,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(hoaDon.getId())}
        );
    }

    public HoaDon getHoaDonById(int hoaDonId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_HOA_DON,
                null,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(hoaDonId)},
                null,
                null,
                null
        )) {
            if (cursor.moveToFirst()) {
                return mapHoaDon(cursor);
            }
        }
        return null;
    }

    public HoaDon getHoaDonTheoKy(int hopDongId, int thang, int nam) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_HOA_DON,
                null,
                DatabaseHelper.COL_HOA_DON_HOP_DONG_ID + "=? AND "
                        + DatabaseHelper.COL_HOA_DON_THANG + "=? AND "
                        + DatabaseHelper.COL_HOA_DON_NAM + "=?",
                new String[]{String.valueOf(hopDongId), String.valueOf(thang), String.valueOf(nam)},
                null,
                null,
                null
        )) {
            if (cursor.moveToFirst()) {
                return mapHoaDon(cursor);
            }
        }
        return null;
    }

    /**
     * Kiểm tra xem tháng/năm này đã lập hóa đơn cho phòng này chưa
     */
    public boolean existsHoaDon(int phongId, int thang, int nam) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT 1 FROM " + DatabaseHelper.TABLE_HOA_DON 
                + " WHERE " + DatabaseHelper.COL_HOA_DON_PHONG_ID + "=? AND "
                + DatabaseHelper.COL_HOA_DON_THANG + "=? AND " 
                + DatabaseHelper.COL_HOA_DON_NAM + "=?",
                new String[]{String.valueOf(phongId), String.valueOf(thang), String.valueOf(nam)})) {
            return cursor.moveToFirst();
        }
    }

    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> danhSach = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_HOA_DON,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.COL_HOA_DON_NAM + " DESC, " + DatabaseHelper.COL_HOA_DON_THANG + " DESC"
        )) {
            while (cursor.moveToNext()) {
                danhSach.add(mapHoaDon(cursor));
            }
        }
        return danhSach;
    }

    /**
     * Tính tổng số tiền khách còn nợ trên toàn hệ thống
     */
    public double tinhTongCongNo() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // COALESCE dùng để trả về 0 nếu kết quả SUM là null
        try (Cursor cursor = db.rawQuery(
                "SELECT COALESCE(SUM(" + DatabaseHelper.COL_HOA_DON_CON_NO + "), 0) FROM " + DatabaseHelper.TABLE_HOA_DON,
                null
        )) {
            if (cursor.moveToFirst()) {
                return cursor.getDouble(0);
            }
        }
        return 0;
    }

    public List<com.example.quanlynhatro.data.model.HoaDonChiTiet> getChiTietHoaDon(int hoaDonId) {
        List<com.example.quanlynhatro.data.model.HoaDonChiTiet> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_HOA_DON_CHI_TIET,
                null,
                DatabaseHelper.COL_HOA_DON_CT_HOA_DON_ID + "=?",
                new String[]{String.valueOf(hoaDonId)},
                null,
                null,
                null
        )) {
            while (cursor.moveToNext()) {
                com.example.quanlynhatro.data.model.HoaDonChiTiet ct = new com.example.quanlynhatro.data.model.HoaDonChiTiet();
                ct.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)));
                ct.setHoaDonId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_CT_HOA_DON_ID)));
                ct.setLoaiDichVuId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_CT_LOAI_DICH_VU_ID)));
                ct.setTenMucPhi(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_CT_TEN_MUC_PHI)));
                ct.setSoLuong(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_CT_SO_LUONG)));
                ct.setDonGiaApDung(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_CT_DON_GIA_AP_DUNG)));
                ct.setThanhTien(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_CT_THANH_TIEN)));
                
                int idxChiSoCu = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_CT_CHI_SO_CU);
                if (!cursor.isNull(idxChiSoCu)) ct.setChiSoCu(cursor.getDouble(idxChiSoCu));
                
                int idxChiSoMoi = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_CT_CHI_SO_MOI);
                if (!cursor.isNull(idxChiSoMoi)) ct.setChiSoMoi(cursor.getDouble(idxChiSoMoi));
                
                ct.setGhiChu(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_GHI_CHU)));
                
                list.add(ct);
            }
        }
        return list;
    }

    // ============================================================
    // QUERY MỚI: Lấy danh sách hóa đơn có kèm tên phòng + tên khách
    // ============================================================

    /**
     * Lấy danh sách hóa đơn cho một tháng/năm cụ thể.
     * Dùng SQL JOIN để kết hợp 3 bảng: hoa_don + phong + khach_thue.
     *
     * Tương tự trong C#:
     *   var list = db.HoaDon
     *       .Join(db.Phong, ...)
     *       .Join(db.KhachThue, ...)
     *       .Where(hd => hd.Thang == thang && hd.Nam == nam)
     *       .ToList();
     *
     * @param thang       Tháng cần lấy (1-12), truyền 0 để lấy tất cả
     * @param nam         Năm cần lấy, truyền 0 để lấy tất cả
     * @param trangThai   Lọc theo trạng thái (null = lấy tất cả)
     * @return            Danh sách HoaDonVm đã có tên phòng + tên khách
     */
    /**
     * Lấy danh sách hóa đơn cho một tháng/năm cụ thể kèm tên phòng và tên khách.
     * Sử dụng SQL JOIN để kết hợp 4 bảng: hoa_don, phong, hop_dong, khach_thue.
     * 
     * @param thang       Tháng cần lọc (1-12), truyền 0 để lấy tất cả
     * @param nam         Năm cần lọc, truyền 0 để lấy tất cả
     * @param trangThai   Trạng thái (DA_THANH_TOAN, CHUA_THANH_TOAN...), null để lấy tất cả
     */
    public List<HoaDonVm> getDanhSachHoaDonVm(int thang, int nam, String trangThai) {
        List<HoaDonVm> danhSach = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Xây dựng câu truy vấn phức tạp
        StringBuilder sql = new StringBuilder(
            "SELECT "
            + "hd.id, hd.ma_hoa_don, hd.thang, hd.nam, "
            + "hd.han_thanh_toan, hd.tong_tien, "
            + "hd.da_thanh_toan, hd.con_no, hd.trang_thai, "
            + "p.ten_phong AS ten_phong, "               // Tên phòng từ bảng phong
            + "kh.ho_ten AS ten_khach "                  // Tên khách từ bảng khach_thue
            + "FROM " + DatabaseHelper.TABLE_HOA_DON + " hd "
            + "LEFT JOIN " + DatabaseHelper.TABLE_PHONG + " p ON hd.phong_id = p.id "
            + "LEFT JOIN " + DatabaseHelper.TABLE_HOP_DONG + " hd2 ON hd.hop_dong_id = hd2.id "
            + "LEFT JOIN " + DatabaseHelper.TABLE_KHACH_THUE + " kh ON hd2.khach_thue_dai_dien_id = kh.id "
            + "WHERE 1=1" // "WHERE 1=1" giúp việc nối chuỗi điều kiện AND phía sau dễ dàng hơn
        );

        List<String> args = new ArrayList<>();

        if (thang > 0 && nam > 0) {
            sql.append(" AND hd.thang = ? AND hd.nam = ?");
            args.add(String.valueOf(thang));
            args.add(String.valueOf(nam));
        }

        if (trangThai != null && !trangThai.isEmpty()) {
            sql.append(" AND hd.trang_thai = ?");
            args.add(trangThai);
        }

        // Sắp xếp: Thời gian mới nhất hiện lên trên đầu
        sql.append(" ORDER BY hd.nam DESC, hd.thang DESC");

        try (Cursor cursor = db.rawQuery(sql.toString(), args.toArray(new String[0]))) {
            while (cursor.moveToNext()) {
                HoaDonVm vm = new HoaDonVm();
                vm.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                vm.setMaHoaDon(cursor.getString(cursor.getColumnIndexOrThrow("ma_hoa_don")));
                vm.setThang(cursor.getInt(cursor.getColumnIndexOrThrow("thang")));
                vm.setNam(cursor.getInt(cursor.getColumnIndexOrThrow("nam")));
                vm.setHanThanhToan(cursor.getString(cursor.getColumnIndexOrThrow("han_thanh_toan")));
                vm.setTongTien(cursor.getDouble(cursor.getColumnIndexOrThrow("tong_tien")));
                vm.setDaThanhToan(cursor.getDouble(cursor.getColumnIndexOrThrow("da_thanh_toan")));
                vm.setConNo(cursor.getDouble(cursor.getColumnIndexOrThrow("con_no")));
                vm.setTrangThai(cursor.getString(cursor.getColumnIndexOrThrow("trang_thai")));
                vm.setTenPhong(cursor.getString(cursor.getColumnIndexOrThrow("ten_phong")));
                vm.setTenKhach(cursor.getString(cursor.getColumnIndexOrThrow("ten_khach")));
                danhSach.add(vm);
            }
        }
        return danhSach;
    }

    /**
     * Tính tổng tiền (phải thu, đã thu, còn nợ) theo tháng/năm.
     * Trả về mảng double[3]:
     *   [0] = tổng phải thu
     *   [1] = tổng đã thu
     *   [2] = tổng còn nợ
     */
    public double[] tinhTongTheoThang(int thang, int nam) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(
                "SELECT COALESCE(SUM(tong_tien),0), "
                + "COALESCE(SUM(da_thanh_toan),0), "
                + "COALESCE(SUM(con_no),0) "
                + "FROM " + DatabaseHelper.TABLE_HOA_DON
                + " WHERE thang=? AND nam=?",
                new String[]{String.valueOf(thang), String.valueOf(nam)}
        )) {
            if (cursor.moveToFirst()) {
                return new double[]{
                    cursor.getDouble(0),
                    cursor.getDouble(1),
                    cursor.getDouble(2)
                };
            }
        }
        return new double[]{0, 0, 0};
    }

    private ContentValues toContentValues(HoaDon hoaDon, String createdAt) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_HOA_DON_MA_HOA_DON, hoaDon.getMaHoaDon());
        values.put(DatabaseHelper.COL_HOA_DON_HOP_DONG_ID, hoaDon.getHopDongId());
        values.put(DatabaseHelper.COL_HOA_DON_PHONG_ID, hoaDon.getPhongId());
        values.put(DatabaseHelper.COL_HOA_DON_THANG, hoaDon.getThang());
        values.put(DatabaseHelper.COL_HOA_DON_NAM, hoaDon.getNam());
        values.put(DatabaseHelper.COL_HOA_DON_NGAY_LAP, hoaDon.getNgayLap());
        values.put(DatabaseHelper.COL_HOA_DON_HAN_THANH_TOAN, hoaDon.getHanThanhToan());
        values.put(DatabaseHelper.COL_HOA_DON_TONG_TIEN_TRUOC_GIAM, hoaDon.getTongTienTruocGiam());
        values.put(DatabaseHelper.COL_HOA_DON_GIAM_TRU, hoaDon.getGiamTru());
        values.put(DatabaseHelper.COL_HOA_DON_TONG_TIEN, hoaDon.getTongTien());
        values.put(DatabaseHelper.COL_HOA_DON_DA_THANH_TOAN, hoaDon.getDaThanhToan());
        values.put(DatabaseHelper.COL_HOA_DON_CON_NO, hoaDon.getConNo());
        values.put(DatabaseHelper.COL_HOA_DON_TRANG_THAI, hoaDon.getTrangThai());
        values.put(DatabaseHelper.COL_GHI_CHU, hoaDon.getGhiChu());
        if (createdAt != null) {
            values.put(DatabaseHelper.COL_CREATED_AT, createdAt);
        }
        return values;
    }

    private HoaDon mapHoaDon(Cursor cursor) {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)));
        hoaDon.setMaHoaDon(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_MA_HOA_DON)));
        hoaDon.setHopDongId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_HOP_DONG_ID)));
        hoaDon.setPhongId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_PHONG_ID)));
        hoaDon.setThang(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_THANG)));
        hoaDon.setNam(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_NAM)));
        hoaDon.setNgayLap(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_NGAY_LAP)));
        hoaDon.setHanThanhToan(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_HAN_THANH_TOAN)));
        hoaDon.setTongTienTruocGiam(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_TONG_TIEN_TRUOC_GIAM)));
        hoaDon.setGiamTru(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_GIAM_TRU)));
        hoaDon.setTongTien(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_TONG_TIEN)));
        hoaDon.setDaThanhToan(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_DA_THANH_TOAN)));
        hoaDon.setConNo(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_CON_NO)));
        hoaDon.setTrangThai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOA_DON_TRANG_THAI)));
        hoaDon.setGhiChu(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_GHI_CHU)));
        hoaDon.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CREATED_AT)));
        hoaDon.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UPDATED_AT)));
        return hoaDon;
    }

    /**
     * Lấy danh sách các hóa đơn đã quá hạn thanh toán nhưng chưa trả đủ tiền.
     * Logic: han_thanh_toan < ngày_hiện_tại AND trang_thai != 'DA_THANH_TOAN'
     */
    public List<HoaDonVm> getDanhSachHoaDonQuaHan() {
        List<HoaDonVm> danhSach = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Câu SQL tương tự getDanhSachHoaDonVm nhưng thêm điều kiện quá hạn
        String sql = "SELECT hd.id, hd.ma_hoa_don, hd.thang, hd.nam, "
                + "hd.han_thanh_toan, hd.tong_tien, hd.da_thanh_toan, hd.con_no, hd.trang_thai, "
                + "p.ten_phong, kh.ho_ten AS ten_khach "
                + "FROM " + DatabaseHelper.TABLE_HOA_DON + " hd "
                + "JOIN " + DatabaseHelper.TABLE_PHONG + " p ON hd.phong_id = p.id "
                + "JOIN " + DatabaseHelper.TABLE_HOP_DONG + " hd2 ON hd.hop_dong_id = hd2.id "
                + "JOIN " + DatabaseHelper.TABLE_KHACH_THUE + " kh ON hd2.khach_thue_dai_dien_id = kh.id "
                + "WHERE hd.han_thanh_toan < ? AND hd.trang_thai != ? "
                + "ORDER BY hd.han_thanh_toan ASC";

        try (Cursor cursor = db.rawQuery(sql, new String[]{today, DatabaseHelper.TRANG_THAI_HOA_DON_DA_THANH_TOAN})) {
            while (cursor.moveToNext()) {
                HoaDonVm vm = new HoaDonVm();
                vm.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                vm.setMaHoaDon(cursor.getString(cursor.getColumnIndexOrThrow("ma_hoa_don")));
                vm.setThang(cursor.getInt(cursor.getColumnIndexOrThrow("thang")));
                vm.setNam(cursor.getInt(cursor.getColumnIndexOrThrow("nam")));
                vm.setHanThanhToan(cursor.getString(cursor.getColumnIndexOrThrow("han_thanh_toan")));
                vm.setTongTien(cursor.getDouble(cursor.getColumnIndexOrThrow("tong_tien")));
                vm.setDaThanhToan(cursor.getDouble(cursor.getColumnIndexOrThrow("da_thanh_toan")));
                vm.setConNo(cursor.getDouble(cursor.getColumnIndexOrThrow("con_no")));
                vm.setTrangThai(cursor.getString(cursor.getColumnIndexOrThrow("trang_thai")));
                vm.setTenPhong(cursor.getString(cursor.getColumnIndexOrThrow("ten_phong")));
                vm.setTenKhach(cursor.getString(cursor.getColumnIndexOrThrow("ten_khach")));
                danhSach.add(vm);
            }
        }
        return danhSach;
    }

    private String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }
}
