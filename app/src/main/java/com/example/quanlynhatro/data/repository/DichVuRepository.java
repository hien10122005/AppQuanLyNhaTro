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

    public List<LoaiDichVu> getAllLoaiDichVu() {
        List<LoaiDichVu> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
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

    public double getDonGia(int loaiDichVuId, int phongId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        // 1. Thử tìm giá riêng cho phòng
        try (Cursor cursor = db.query(DatabaseHelper.TABLE_BANG_GIA_DICH_VU, 
                new String[]{DatabaseHelper.COL_BANG_GIA_DON_GIA},
                DatabaseHelper.COL_BANG_GIA_LOAI_DICH_VU_ID + "=? AND " + DatabaseHelper.COL_BANG_GIA_PHONG_ID + "=?",
                new String[]{String.valueOf(loaiDichVuId), String.valueOf(phongId)},
                null, null, DatabaseHelper.COL_BANG_GIA_NGAY_HIEU_LUC + " DESC", "1")) {
            if (cursor.moveToFirst()) {
                return cursor.getDouble(0);
            }
        }

        // 2. Nếu không có giá riêng, tìm giá chung (phong_id is null)
        try (Cursor cursor = db.query(DatabaseHelper.TABLE_BANG_GIA_DICH_VU, 
                new String[]{DatabaseHelper.COL_BANG_GIA_DON_GIA},
                DatabaseHelper.COL_BANG_GIA_LOAI_DICH_VU_ID + "=? AND " + DatabaseHelper.COL_BANG_GIA_PHONG_ID + " IS NULL",
                new String[]{String.valueOf(loaiDichVuId)},
                null, null, DatabaseHelper.COL_BANG_GIA_NGAY_HIEU_LUC + " DESC", "1")) {
            if (cursor.moveToFirst()) {
                return cursor.getDouble(0);
            }
        }

        // 3. Mặc định nếu không thấy cấu hình (ví dụ: Điện 3.5k, Nước 15k)
        // Đây là fallback an toàn
        return 0;
    }
}
