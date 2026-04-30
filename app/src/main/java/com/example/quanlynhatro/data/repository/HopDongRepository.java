package com.example.quanlynhatro.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.HopDong;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HopDongRepository {
    private final DatabaseHelper dbHelper;

    public HopDongRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Tạo một hợp đồng thuê phòng mới
     * @return ID của hợp đồng vừa tạo
     */
    public long addHopDong(HopDong hopDong) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String now = now();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_HOP_DONG_MA_HOP_DONG, hopDong.getMaHopDong());
        values.put(DatabaseHelper.COL_HOP_DONG_PHONG_ID, hopDong.getPhongId());
        values.put(DatabaseHelper.COL_HOP_DONG_KHACH_THUE_DAI_DIEN_ID, hopDong.getKhachThueDaiDienId());
        values.put(DatabaseHelper.COL_HOP_DONG_NGAY_KY, hopDong.getNgayKy());
        values.put(DatabaseHelper.COL_HOP_DONG_NGAY_BAT_DAU, hopDong.getNgayBatDau());
        values.put(DatabaseHelper.COL_HOP_DONG_NGAY_KET_THUC, hopDong.getNgayKetThuc());
        values.put(DatabaseHelper.COL_HOP_DONG_GIA_THUE_CHOT, hopDong.getGiaThueChot());
        values.put(DatabaseHelper.COL_HOP_DONG_TIEN_COC, hopDong.getTienCoc());
        values.put(DatabaseHelper.COL_HOP_DONG_CHU_KY_THANH_TOAN, hopDong.getChuKyThanhToan());
        values.put(DatabaseHelper.COL_HOP_DONG_TRANG_THAI, hopDong.getTrangThai());
        values.put(DatabaseHelper.COL_GHI_CHU, hopDong.getGhiChu());
        values.put(DatabaseHelper.COL_CREATED_AT, hopDong.getCreatedAt() != null ? hopDong.getCreatedAt() : now);
        values.put(DatabaseHelper.COL_UPDATED_AT, hopDong.getUpdatedAt() != null ? hopDong.getUpdatedAt() : now);
        return db.insert(DatabaseHelper.TABLE_HOP_DONG, null, values);
    }

    public HopDong getHopDongById(int hopDongId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_HOP_DONG,
                null,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(hopDongId)},
                null,
                null,
                null
        )) {
            if (cursor.moveToFirst()) {
                return mapHopDong(cursor);
            }
        }
        return null;
    }

    /**
     * Lấy thông tin hợp đồng đầy đủ (kèm tên phòng và tên khách đại diện) theo ID
     */
    public com.example.quanlynhatro.data.model.HopDongVm getHopDongVmById(int hopDongId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Câu lệnh SQL JOIN để lấy dữ liệu từ 3 bảng: HopDong, Phong, KhachThue
        String query = "SELECT h.*, p." + DatabaseHelper.COL_PHONG_TEN_PHONG + ", k." + DatabaseHelper.COL_KHACH_THUE_HO_TEN 
                     + ", k." + DatabaseHelper.COL_KHACH_THUE_SO_DIEN_THOAI + ", k." + DatabaseHelper.COL_KHACH_THUE_CCCD
                     + " FROM " + DatabaseHelper.TABLE_HOP_DONG + " h"
                     + " JOIN " + DatabaseHelper.TABLE_PHONG + " p ON h." + DatabaseHelper.COL_HOP_DONG_PHONG_ID + " = p." + DatabaseHelper.COL_ID
                     + " JOIN " + DatabaseHelper.TABLE_KHACH_THUE + " k ON h." + DatabaseHelper.COL_HOP_DONG_KHACH_THUE_DAI_DIEN_ID + " = k." + DatabaseHelper.COL_ID
                     + " WHERE h." + DatabaseHelper.COL_ID + " = ?";
        
        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(hopDongId)})) {
            if (cursor.moveToFirst()) {
                return mapHopDongVm(cursor);
            }
        }
        return null;
    }

    public List<HopDong> getAllHopDong() {
        List<HopDong> danhSach = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_HOP_DONG,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.COL_HOP_DONG_NGAY_BAT_DAU + " DESC"
        )) {
            while (cursor.moveToNext()) {
                danhSach.add(mapHopDong(cursor));
            }
        }
        return danhSach;
    }

    public List<com.example.quanlynhatro.data.model.HopDongVm> getAllHopDongVm() {
        List<com.example.quanlynhatro.data.model.HopDongVm> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT h.*, p." + DatabaseHelper.COL_PHONG_TEN_PHONG + ", k." + DatabaseHelper.COL_KHACH_THUE_HO_TEN 
                     + ", k." + DatabaseHelper.COL_KHACH_THUE_SO_DIEN_THOAI + ", k." + DatabaseHelper.COL_KHACH_THUE_CCCD
                     + " FROM " + DatabaseHelper.TABLE_HOP_DONG + " h"
                     + " JOIN " + DatabaseHelper.TABLE_PHONG + " p ON h." + DatabaseHelper.COL_HOP_DONG_PHONG_ID + " = p." + DatabaseHelper.COL_ID
                     + " JOIN " + DatabaseHelper.TABLE_KHACH_THUE + " k ON h." + DatabaseHelper.COL_HOP_DONG_KHACH_THUE_DAI_DIEN_ID + " = k." + DatabaseHelper.COL_ID
                     + " ORDER BY h." + DatabaseHelper.COL_HOP_DONG_NGAY_BAT_DAU + " DESC";
        
        try (Cursor cursor = db.rawQuery(query, null)) {
            while (cursor.moveToNext()) {
                list.add(mapHopDongVm(cursor));
            }
        }
        return list;
    }

    /**
     * Tìm hợp đồng đang có hiệu lực của một phòng cụ thể
     * Thường dùng để lấy giá thuê và thông tin khách đang ở trong phòng đó
     */
    public HopDong getHopDongHieuLucTheoPhong(int phongId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_HOP_DONG,
                null,
                DatabaseHelper.COL_HOP_DONG_PHONG_ID + "=? AND "
                        + DatabaseHelper.COL_HOP_DONG_TRANG_THAI + "=?",
                new String[]{String.valueOf(phongId), DatabaseHelper.TRANG_THAI_HOP_DONG_HIEU_LUC},
                null,
                null,
                DatabaseHelper.COL_HOP_DONG_NGAY_BAT_DAU + " DESC", // Lấy cái mới nhất
                "1"
        )) {
            if (cursor.moveToFirst()) {
                return mapHopDong(cursor);
            }
        }
        return null;
    }

    private HopDong mapHopDong(Cursor cursor) {
        HopDong hopDong = new HopDong();
        hopDong.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)));
        hopDong.setMaHopDong(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_MA_HOP_DONG)));
        hopDong.setPhongId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_PHONG_ID)));
        hopDong.setKhachThueDaiDienId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_KHACH_THUE_DAI_DIEN_ID)));
        hopDong.setNgayKy(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_NGAY_KY)));
        hopDong.setNgayBatDau(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_NGAY_BAT_DAU)));
        hopDong.setNgayKetThuc(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_NGAY_KET_THUC)));
        hopDong.setGiaThueChot(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_GIA_THUE_CHOT)));
        hopDong.setTienCoc(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_TIEN_COC)));
        hopDong.setChuKyThanhToan(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_CHU_KY_THANH_TOAN)));
        hopDong.setTrangThai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_TRANG_THAI)));
        hopDong.setGhiChu(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_GHI_CHU)));
        hopDong.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CREATED_AT)));
        hopDong.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UPDATED_AT)));
        return hopDong;
    }

    private com.example.quanlynhatro.data.model.HopDongVm mapHopDongVm(Cursor cursor) {
        com.example.quanlynhatro.data.model.HopDongVm vm = new com.example.quanlynhatro.data.model.HopDongVm();
        vm.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)));
        vm.setMaHopDong(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_MA_HOP_DONG)));
        vm.setPhongId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_PHONG_ID)));
        vm.setKhachThueDaiDienId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_KHACH_THUE_DAI_DIEN_ID)));
        vm.setNgayKy(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_NGAY_KY)));
        vm.setNgayBatDau(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_NGAY_BAT_DAU)));
        vm.setNgayKetThuc(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_NGAY_KET_THUC)));
        vm.setGiaThueChot(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_GIA_THUE_CHOT)));
        vm.setTienCoc(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_TIEN_COC)));
        vm.setChuKyThanhToan(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_CHU_KY_THANH_TOAN)));
        vm.setTrangThai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HOP_DONG_TRANG_THAI)));
        vm.setGhiChu(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_GHI_CHU)));
        vm.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CREATED_AT)));
        vm.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UPDATED_AT)));
        
        vm.setTenPhong(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONG_TEN_PHONG)));
        vm.setTenKhachThue(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_KHACH_THUE_HO_TEN)));
        vm.setSdtKhachThue(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_KHACH_THUE_SO_DIEN_THOAI)));
        vm.setCccdKhachThue(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_KHACH_THUE_CCCD)));
        
        return vm;
    }

    /**
     * Lấy danh sách toàn bộ hợp đồng (cả cũ và mới) của một phòng để xem lịch sử thuê.
     */
    public List<HopDong> getLichSuThueTheoPhong(int phongId) {
        List<HopDong> danhSach = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_HOP_DONG,
                null,
                DatabaseHelper.COL_HOP_DONG_PHONG_ID + "=?",
                new String[]{String.valueOf(phongId)},
                null,
                null,
                DatabaseHelper.COL_HOP_DONG_NGAY_BAT_DAU + " DESC"
        )) {
            while (cursor.moveToNext()) {
                danhSach.add(mapHopDong(cursor));
            }
        }
        return danhSach;
    }

    /**
     * Thanh lý hợp đồng (Trả phòng): 
     * Đây là quy trình nghiệp vụ quan trọng, sử dụng Transaction để đảm bảo tính toàn vẹn:
     * 1. Chuyển trạng thái hợp đồng thành DA_THANH_LY (Đã thanh lý)
     * 2. Chuyển trạng thái phòng tương ứng về TRONG (Trống) để người khác có thể thuê
     */
    public boolean thanhLyHopDong(int hopDongId, int phongId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction(); // Bắt đầu giao dịch (Transaction)
        try {
            // Bước 1: Cập nhật trạng thái Hợp đồng
            ContentValues cvHd = new ContentValues();
            cvHd.put(DatabaseHelper.COL_HOP_DONG_TRANG_THAI, DatabaseHelper.TRANG_THAI_HOP_DONG_DA_THANH_LY);
            cvHd.put(DatabaseHelper.COL_UPDATED_AT, now());
            db.update(DatabaseHelper.TABLE_HOP_DONG, cvHd, DatabaseHelper.COL_ID + "=?", new String[]{String.valueOf(hopDongId)});

            // Bước 2: Cập nhật trạng thái Phòng về 'Trống'
            ContentValues cvPhong = new ContentValues();
            cvPhong.put(DatabaseHelper.COL_PHONG_TRANG_THAI, DatabaseHelper.TRANG_THAI_PHONG_TRONG);
            cvPhong.put(DatabaseHelper.COL_UPDATED_AT, now());
            db.update(DatabaseHelper.TABLE_PHONG, cvPhong, DatabaseHelper.COL_ID + "=?", new String[]{String.valueOf(phongId)});

            db.setTransactionSuccessful(); // Đánh dấu thành công
            return true;
        } catch (Exception e) {
            return false; // Có lỗi xảy ra, transaction sẽ tự động rollback
        } finally {
            db.endTransaction(); // Kết thúc giao dịch
        }
    }

    /**
     * Lấy danh sách thành viên trong một hợp đồng
     */
    public List<com.example.quanlynhatro.data.model.ThanhVienVm> getThanhViensByHopDong(int hopDongId) {
        List<com.example.quanlynhatro.data.model.ThanhVienVm> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT tv.*, k." + DatabaseHelper.COL_KHACH_THUE_HO_TEN + ", k." + DatabaseHelper.COL_KHACH_THUE_SO_DIEN_THOAI
                     + ", k." + DatabaseHelper.COL_KHACH_THUE_CCCD
                     + " FROM " + DatabaseHelper.TABLE_HOP_DONG_THANH_VIEN + " tv"
                     + " JOIN " + DatabaseHelper.TABLE_KHACH_THUE + " k ON tv." + DatabaseHelper.COL_THANH_VIEN_KHACH_THUE_ID + " = k." + DatabaseHelper.COL_ID
                     + " WHERE tv." + DatabaseHelper.COL_THANH_VIEN_HOP_DONG_ID + " = ?";
        
        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(hopDongId)})) {
            while (cursor.moveToNext()) {
                com.example.quanlynhatro.data.model.ThanhVienVm vm = new com.example.quanlynhatro.data.model.ThanhVienVm();
                vm.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)));
                vm.setHopDongId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_THANH_VIEN_HOP_DONG_ID)));
                vm.setKhachThueId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_THANH_VIEN_KHACH_THUE_ID)));
                vm.setVaiTro(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_THANH_VIEN_VAI_TRO)));
                vm.setNgayThamGia(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_THANH_VIEN_NGAY_THAM_GIA)));
                
                vm.setHoTen(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_KHACH_THUE_HO_TEN)));
                vm.setSoDienThoai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_KHACH_THUE_SO_DIEN_THOAI)));
                vm.setCccd(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_KHACH_THUE_CCCD)));
                
                list.add(vm);
            }
        }
        return list;
    }

    /**
     * Thêm thành viên vào phòng (hợp đồng)
     */
    public long addThanhVien(int hopDongId, int khachThueId, String vaiTro) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_THANH_VIEN_HOP_DONG_ID, hopDongId);
        values.put(DatabaseHelper.COL_THANH_VIEN_KHACH_THUE_ID, khachThueId);
        values.put(DatabaseHelper.COL_THANH_VIEN_VAI_TRO, vaiTro);
        values.put(DatabaseHelper.COL_THANH_VIEN_NGAY_THAM_GIA, now());
        return db.insert(DatabaseHelper.TABLE_HOP_DONG_THANH_VIEN, null, values);
    }

    /**
     * Xóa thành viên khỏi phòng
     */
    public boolean removeThanhVien(int thanhVienId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DatabaseHelper.TABLE_HOP_DONG_THANH_VIEN, DatabaseHelper.COL_ID + "=?", new String[]{String.valueOf(thanhVienId)}) > 0;
    }

    /**
     * Lấy hợp đồng đang hiệu lực của một khách thuê
     */
    public HopDong getActiveHopDongByKhachThue(int khachThueId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_HOP_DONG + 
                      " WHERE " + DatabaseHelper.COL_HOP_DONG_KHACH_THUE_DAI_DIEN_ID + " = ? " +
                      " AND " + DatabaseHelper.COL_HOP_DONG_TRANG_THAI + " = ? LIMIT 1";
        
        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(khachThueId), DatabaseHelper.TRANG_THAI_HOP_DONG_HIEU_LUC})) {
            if (cursor.moveToFirst()) {
                return mapHopDong(cursor);
            }
        }
        return null;
    }

    private String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }
}
