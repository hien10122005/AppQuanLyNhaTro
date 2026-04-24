package com.example.quanlynhatro.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.HoaDon;

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

    public long addHoaDon(HoaDon hoaDon) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String now = now();
        ContentValues values = toContentValues(hoaDon, hoaDon.getCreatedAt() != null ? hoaDon.getCreatedAt() : now);
        values.put(DatabaseHelper.COL_UPDATED_AT, hoaDon.getUpdatedAt() != null ? hoaDon.getUpdatedAt() : now);
        return db.insert(DatabaseHelper.TABLE_HOA_DON, null, values);
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

    public double tinhTongCongNo() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
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

    private String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }
}
