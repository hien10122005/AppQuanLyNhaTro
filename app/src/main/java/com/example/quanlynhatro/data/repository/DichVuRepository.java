package com.example.quanlynhatro.data.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.BangGiaDichVu;
import com.example.quanlynhatro.data.model.LoaiDichVu;

import java.util.ArrayList;
import java.util.List;

public class DichVuRepository {
    private final DatabaseHelper dbHelper;

    public DichVuRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Lấy toàn bộ danh sách các loại dịch vụ đang hoạt động (Ví dụ: Điện, Nước, Wifi, Rác...)
     */
    public List<LoaiDichVu> getAllLoaiDichVu() {
        List<LoaiDichVu> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Lọc những dịch vụ có hoat_dong = 1
        try (Cursor cursor = db.query(DatabaseHelper.TABLE_LOAI_DICH_VU, null, 
                DatabaseHelper.COL_LOAI_DICH_VU_HOAT_DONG + "=1", null, null, null, null)) {
            while (cursor.moveToNext()) {
                LoaiDichVu dv = new LoaiDichVu();
                dv.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)));
                dv.setMaLoai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOAI_DICH_VU_MA_LOAI)));
                dv.setTenLoai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOAI_DICH_VU_TEN_LOAI)));
                dv.setKieuTinh(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOAI_DICH_VU_KIEU_TINH)));
                dv.setDonVi(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOAI_DICH_VU_DON_VI)));
                dv.setHoatDong(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOAI_DICH_VU_HOAT_DONG)));
                list.add(dv);
            }
        }
        return list;
    }

    /**
     * Lấy đơn giá của một loại dịch vụ cho một phòng cụ thể.
     * Thứ tự ưu tiên:
     * 1. Tìm trong bảng giá riêng của phòng đó (Nếu chủ nhà quy định giá riêng cho phòng này).
     * 2. Nếu không có giá riêng, lấy giá chung của toàn hệ thống (phong_id IS NULL).
     * @return Đơn giá áp dụng, hoặc 0 nếu không tìm thấy cấu hình.
     */
    public double getDonGia(int loaiDichVuId, int phongId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        // Bước 1: Thử tìm giá riêng quy định cho phòng này
        try (Cursor cursor = db.query(DatabaseHelper.TABLE_BANG_GIA_DICH_VU, 
                new String[]{DatabaseHelper.COL_BANG_GIA_DON_GIA},
                DatabaseHelper.COL_BANG_GIA_LOAI_DICH_VU_ID + "=? AND " + DatabaseHelper.COL_BANG_GIA_PHONG_ID + "=?",
                new String[]{String.valueOf(loaiDichVuId), String.valueOf(phongId)},
                null, null, DatabaseHelper.COL_BANG_GIA_NGAY_HIEU_LUC + " DESC", "1")) {
            if (cursor.moveToFirst()) {
                return cursor.getDouble(0);
            }
        }

        // Bước 2: Nếu không thấy giá riêng, lấy giá cấu hình chung (phong_id is null)
        try (Cursor cursor = db.query(DatabaseHelper.TABLE_BANG_GIA_DICH_VU, 
                new String[]{DatabaseHelper.COL_BANG_GIA_DON_GIA},
                DatabaseHelper.COL_BANG_GIA_LOAI_DICH_VU_ID + "=? AND " + DatabaseHelper.COL_BANG_GIA_PHONG_ID + " IS NULL",
                new String[]{String.valueOf(loaiDichVuId)},
                null, null, DatabaseHelper.COL_BANG_GIA_NGAY_HIEU_LUC + " DESC", "1")) {
            if (cursor.moveToFirst()) {
                return cursor.getDouble(0);
            }
        }

        return 0; // Trả về 0 nếu hoàn toàn không có cấu hình
    }

    /**
     * Lưu hoặc cập nhật bảng giá chung cho một loại dịch vụ.
     * Hàm này xóa giá chung cũ trước khi chèn giá mới để đảm bảo tính duy nhất.
     */
    public void saveBangGiaChung(String maLoaiDichVu, double donGia) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // 1. Tìm ID của loại dịch vụ dựa trên mã (DIEN, NUOC...)
        int loaiDichVuId = -1;
        try (Cursor cursor = db.query(DatabaseHelper.TABLE_LOAI_DICH_VU, new String[]{DatabaseHelper.COL_ID},
                DatabaseHelper.COL_LOAI_DICH_VU_MA_LOAI + "=?", new String[]{maLoaiDichVu}, null, null, null)) {
            if (cursor.moveToFirst()) {
                loaiDichVuId = cursor.getInt(0);
            }
        }

        if (loaiDichVuId == -1) return;

        // 2. Xóa các cấu hình giá chung cũ của loại dịch vụ này
        db.delete(DatabaseHelper.TABLE_BANG_GIA_DICH_VU, 
                DatabaseHelper.COL_BANG_GIA_LOAI_DICH_VU_ID + "=? AND " + DatabaseHelper.COL_BANG_GIA_PHONG_ID + " IS NULL",
                new String[]{String.valueOf(loaiDichVuId)});

        // 3. Thêm cấu hình giá mới
        android.content.ContentValues values = new android.content.ContentValues();
        values.put(DatabaseHelper.COL_BANG_GIA_LOAI_DICH_VU_ID, loaiDichVuId);
        values.put(DatabaseHelper.COL_BANG_GIA_PHONG_ID, (Integer) null); // Null nghĩa là áp dụng chung
        values.put(DatabaseHelper.COL_BANG_GIA_DON_GIA, donGia);
        values.put(DatabaseHelper.COL_BANG_GIA_NGAY_HIEU_LUC, new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date()));
        values.put(DatabaseHelper.COL_CREATED_AT, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date()));
        
        db.insert(DatabaseHelper.TABLE_BANG_GIA_DICH_VU, null, values);
    }
    public String getMaLoaiById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(DatabaseHelper.TABLE_LOAI_DICH_VU, new String[]{DatabaseHelper.COL_LOAI_DICH_VU_MA_LOAI},
                DatabaseHelper.COL_ID + "=?", new String[]{String.valueOf(id)}, null, null, null)) {
            if (cursor.moveToFirst()) return cursor.getString(0);
        }
        return "";
    }

    public LoaiDichVu getLoaiDichVuById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(DatabaseHelper.TABLE_LOAI_DICH_VU, null,
                DatabaseHelper.COL_ID + "=?", new String[]{String.valueOf(id)}, null, null, null)) {
            if (cursor.moveToFirst()) {
                LoaiDichVu dv = new LoaiDichVu();
                dv.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)));
                dv.setMaLoai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOAI_DICH_VU_MA_LOAI)));
                dv.setTenLoai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOAI_DICH_VU_TEN_LOAI)));
                dv.setKieuTinh(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOAI_DICH_VU_KIEU_TINH)));
                dv.setDonVi(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOAI_DICH_VU_DON_VI)));
                return dv;
            }
        }
        return null;
    }
}


