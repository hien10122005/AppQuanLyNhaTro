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
                DatabaseHelper.COL_HOP_DONG_NGAY_BAT_DAU + " DESC",
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

    private String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }
}
