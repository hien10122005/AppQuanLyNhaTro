package com.example.quanlynhatro.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.KhachThue;
import com.example.quanlynhatro.data.model.KhachThueVm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class KhachThueRepository {
    private final DatabaseHelper dbHelper;

    public KhachThueRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Thêm một khách thuê mới vào danh sách hệ thống
     * @return ID của khách thuê vừa tạo
     */
    public long addKhachThue(KhachThue khachThue) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String now = now();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_KHACH_THUE_HO_TEN, khachThue.getHoTen());
        values.put(DatabaseHelper.COL_KHACH_THUE_SO_DIEN_THOAI, khachThue.getSoDienThoai());
        values.put(DatabaseHelper.COL_KHACH_THUE_CCCD, khachThue.getCccd());
        values.put(DatabaseHelper.COL_KHACH_THUE_NGAY_SINH, khachThue.getNgaySinh());
        values.put(DatabaseHelper.COL_KHACH_THUE_GIOI_TINH, khachThue.getGioiTinh());
        values.put(DatabaseHelper.COL_KHACH_THUE_DIA_CHI_THUONG_TRU, khachThue.getDiaChiThuongTru());
        values.put(DatabaseHelper.COL_KHACH_THUE_EMAIL, khachThue.getEmail());
        values.put(DatabaseHelper.COL_GHI_CHU, khachThue.getGhiChu());
        values.put(DatabaseHelper.COL_CREATED_AT, khachThue.getCreatedAt() != null ? khachThue.getCreatedAt() : now);
        values.put(DatabaseHelper.COL_UPDATED_AT, khachThue.getUpdatedAt() != null ? khachThue.getUpdatedAt() : now);
        return db.insert(DatabaseHelper.TABLE_KHACH_THUE, null, values);
    }

    public int updateKhachThue(KhachThue khachThue) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_KHACH_THUE_HO_TEN, khachThue.getHoTen());
        values.put(DatabaseHelper.COL_KHACH_THUE_SO_DIEN_THOAI, khachThue.getSoDienThoai());
        values.put(DatabaseHelper.COL_KHACH_THUE_CCCD, khachThue.getCccd());
        values.put(DatabaseHelper.COL_KHACH_THUE_NGAY_SINH, khachThue.getNgaySinh());
        values.put(DatabaseHelper.COL_KHACH_THUE_GIOI_TINH, khachThue.getGioiTinh());
        values.put(DatabaseHelper.COL_KHACH_THUE_DIA_CHI_THUONG_TRU, khachThue.getDiaChiThuongTru());
        values.put(DatabaseHelper.COL_KHACH_THUE_EMAIL, khachThue.getEmail());
        values.put(DatabaseHelper.COL_GHI_CHU, khachThue.getGhiChu());
        values.put(DatabaseHelper.COL_UPDATED_AT, now());
        return db.update(
                DatabaseHelper.TABLE_KHACH_THUE,
                values,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(khachThue.getId())}
        );
    }

    public int deleteKhachThue(int khachThueId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(
                DatabaseHelper.TABLE_KHACH_THUE,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(khachThueId)}
        );
    }

    public boolean xoaKhachThue(int khachThueId) {
        return deleteKhachThue(khachThueId) > 0;
    }

    public KhachThue getKhachThueById(int khachThueId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_KHACH_THUE,
                null,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(khachThueId)},
                null,
                null,
                null
        )) {
            if (cursor.moveToFirst()) {
                return mapKhachThue(cursor);
            }
        }
        return null;
    }

    /**
     * Lấy danh sách khách thuê kèm theo thông tin phòng họ đang thuê (nếu có)
     * Đây là hàm quan trọng sử dụng LEFT JOIN giữa 3 bảng: KhachThue, HopDong, và Phong
     * @return Danh sách các đối tượng KhachThueVm (ViewModel) chứa đầy đủ thông tin hiển thị
     */
    public List<KhachThueVm> getAllKhachThueVm() {
        List<KhachThueVm> danhSach = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        // SQL JOIN: Lấy khách thuê + Tên phòng (nếu khách đó có hợp đồng đang HIỆU LỰC)
        String sql = "SELECT kt.*, p." + DatabaseHelper.COL_PHONG_TEN_PHONG
                + " FROM " + DatabaseHelper.TABLE_KHACH_THUE + " kt "
                + " LEFT JOIN " + DatabaseHelper.TABLE_HOP_DONG + " hd ON kt." + DatabaseHelper.COL_ID
                + " = hd." + DatabaseHelper.COL_HOP_DONG_KHACH_THUE_DAI_DIEN_ID
                + " AND hd." + DatabaseHelper.COL_HOP_DONG_TRANG_THAI + " = '"
                + DatabaseHelper.TRANG_THAI_HOP_DONG_HIEU_LUC + "'"
                + " LEFT JOIN " + DatabaseHelper.TABLE_PHONG + " p ON hd."
                + DatabaseHelper.COL_HOP_DONG_PHONG_ID + " = p." + DatabaseHelper.COL_ID
                + " ORDER BY kt." + DatabaseHelper.COL_KHACH_THUE_HO_TEN + " ASC";

        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                danhSach.add(mapKhachThueVm(cursor));
            }
        }
        return danhSach;
    }

    public List<KhachThue> getAllKhachThue() {
        List<KhachThue> danhSach = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_KHACH_THUE,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.COL_KHACH_THUE_HO_TEN + " ASC"
        )) {
            while (cursor.moveToNext()) {
                danhSach.add(mapKhachThue(cursor));
            }
        }
        return danhSach;
    }

    /**
     * Lấy thông tin chi tiết một khách thuê kèm tên phòng theo ID
     */
    public KhachThueVm getKhachThueVmById(int id) {
        String query = "SELECT k.*, p." + DatabaseHelper.COL_PHONG_TEN_PHONG + " "
                + "FROM " + DatabaseHelper.TABLE_KHACH_THUE + " k "
                + "LEFT JOIN " + DatabaseHelper.TABLE_HOP_DONG + " h ON k." + DatabaseHelper.COL_ID
                + " = h." + DatabaseHelper.COL_HOP_DONG_KHACH_THUE_DAI_DIEN_ID
                + " AND h." + DatabaseHelper.COL_HOP_DONG_TRANG_THAI + " = ? "
                + "LEFT JOIN " + DatabaseHelper.TABLE_PHONG + " p ON h."
                + DatabaseHelper.COL_HOP_DONG_PHONG_ID + " = p." + DatabaseHelper.COL_ID + " "
                + "WHERE k." + DatabaseHelper.COL_ID + " = ?";

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery(query, new String[]{
                     DatabaseHelper.TRANG_THAI_HOP_DONG_HIEU_LUC,
                     String.valueOf(id)
             })) {
            if (cursor.moveToFirst()) {
                return mapKhachThueVm(cursor);
            }
        }
        return null;
    }

    /**
     * Chuyển đổi Cursor sang đối tượng KhachThueVm (bao gồm trạng thái thuê phòng)
     */
    private KhachThueVm mapKhachThueVm(Cursor cursor) {
        KhachThue base = mapKhachThue(cursor); // Lấy thông tin cơ bản trước
        KhachThueVm vm = new KhachThueVm();
        vm.setId(base.getId());
        vm.setHoTen(base.getHoTen());
        vm.setSoDienThoai(base.getSoDienThoai());
        vm.setCccd(base.getCccd());
        vm.setNgaySinh(base.getNgaySinh());
        vm.setGioiTinh(base.getGioiTinh());
        vm.setDiaChiThuongTru(base.getDiaChiThuongTru());
        vm.setEmail(base.getEmail());
        vm.setGhiChu(base.getGhiChu());
        vm.setCreatedAt(base.getCreatedAt());
        vm.setUpdatedAt(base.getUpdatedAt());

        // Lấy thêm tên phòng từ kết quả JOIN
        String tenPhong = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONG_TEN_PHONG));
        vm.setTenPhong(tenPhong);
        
        // Xác định khách có đang thuê hay không dựa trên việc có tên phòng hay không
        vm.setDangThue(tenPhong != null && !tenPhong.trim().isEmpty());
        return vm;
    }

    private KhachThue mapKhachThue(Cursor cursor) {
        KhachThue khachThue = new KhachThue();
        khachThue.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)));
        khachThue.setHoTen(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_KHACH_THUE_HO_TEN)));
        khachThue.setSoDienThoai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_KHACH_THUE_SO_DIEN_THOAI)));
        khachThue.setCccd(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_KHACH_THUE_CCCD)));
        khachThue.setNgaySinh(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_KHACH_THUE_NGAY_SINH)));
        khachThue.setGioiTinh(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_KHACH_THUE_GIOI_TINH)));
        khachThue.setDiaChiThuongTru(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_KHACH_THUE_DIA_CHI_THUONG_TRU)));
        khachThue.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_KHACH_THUE_EMAIL)));
        khachThue.setGhiChu(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_GHI_CHU)));
        khachThue.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CREATED_AT)));
        khachThue.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UPDATED_AT)));
        return khachThue;
    }

    private String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }
}
