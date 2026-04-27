package com.example.quanlynhatro.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.KhachThue;

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

    public List<com.example.quanlynhatro.data.model.KhachThueVm> getAllKhachThueVm() {
        List<com.example.quanlynhatro.data.model.KhachThueVm> danhSach = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        // Query JOIN với bảng HOP_DONG để lấy tên phòng
        String sql = "SELECT kt.*, p." + DatabaseHelper.COL_PHONG_TEN_PHONG 
                   + " FROM " + DatabaseHelper.TABLE_KHACH_THUE + " kt "
                   + " LEFT JOIN " + DatabaseHelper.TABLE_HOP_DONG + " hd ON kt." + DatabaseHelper.COL_ID + " = hd." + DatabaseHelper.COL_HOP_DONG_KHACH_THUE_DAI_DIEN_ID 
                   + " AND hd." + DatabaseHelper.COL_HOP_DONG_TRANG_THAI + " = '" + DatabaseHelper.TRANG_THAI_HOP_DONG_HIEU_LUC + "'"
                   + " LEFT JOIN " + DatabaseHelper.TABLE_PHONG + " p ON hd." + DatabaseHelper.COL_HOP_DONG_PHONG_ID + " = p." + DatabaseHelper.COL_ID
                   + " ORDER BY kt." + DatabaseHelper.COL_KHACH_THUE_HO_TEN + " ASC";

        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                com.example.quanlynhatro.data.model.KhachThueVm vm = new com.example.quanlynhatro.data.model.KhachThueVm();
                // Map thông tin cơ bản
                KhachThue base = mapKhachThue(cursor);
                vm.setId(base.getId());
                vm.setHoTen(base.getHoTen());
                vm.setSoDienThoai(base.getSoDienThoai());
                vm.setTenPhong(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONG_TEN_PHONG)));
                vm.setDangThue(vm.getTenPhong() != null && !vm.getTenPhong().isEmpty());
                danhSach.add(vm);
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
