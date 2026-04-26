package com.example.quanlynhatro.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.ThanhToan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ThanhToanRepository {
    private final DatabaseHelper dbHelper;

    public ThanhToanRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public long addThanhToan(ThanhToan tt) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_THANH_TOAN_HOA_DON_ID, tt.getHoaDonId());
        values.put(DatabaseHelper.COL_THANH_TOAN_NGAY_THANH_TOAN, tt.getNgayThanhToan());
        values.put(DatabaseHelper.COL_THANH_TOAN_SO_TIEN, tt.getSoTien());
        values.put(DatabaseHelper.COL_THANH_TOAN_PHUONG_THUC, tt.getPhuongThuc());
        values.put(DatabaseHelper.COL_THANH_TOAN_MA_GIAO_DICH, tt.getMaGiaoDich());
        values.put(DatabaseHelper.COL_GHI_CHU, tt.getGhiChu());
        values.put(DatabaseHelper.COL_CREATED_AT, now);
        
        return db.insert(DatabaseHelper.TABLE_THANH_TOAN, null, values);
    }
}
