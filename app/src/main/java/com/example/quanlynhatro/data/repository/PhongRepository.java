package com.example.quanlynhatro.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.Phong;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PhongRepository {
    private final DatabaseHelper dbHelper;

    public PhongRepository(Context context) {
        // DatabaseHelper là cầu nối để mở và quản lý file SQLite
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Thêm mới một phòng trọ vào cơ sở dữ liệu
     * @param phong Đối tượng chứa thông tin phòng cần thêm
     * @return ID của bản ghi mới được tạo, hoặc -1 nếu có lỗi
     */
    public long addPhong(Phong phong) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Mở DB ở chế độ ghi
        String now = now();
        ContentValues values = new ContentValues();
        
        // Đưa dữ liệu từ đối tượng Phong vào cặp (Cột, Giá trị)
        values.put(DatabaseHelper.COL_PHONG_SO_PHONG, phong.getSoPhong());
        values.put(DatabaseHelper.COL_PHONG_TEN_PHONG, phong.getTenPhong());
        values.put(DatabaseHelper.COL_PHONG_LOAI_PHONG, phong.getLoaiPhong());
        values.put(DatabaseHelper.COL_PHONG_GIA_PHONG_MAC_DINH, phong.getGiaPhong());
        values.put(DatabaseHelper.COL_PHONG_DIEN_TICH, phong.getDienTich());
        values.put(DatabaseHelper.COL_PHONG_SO_NGUOI_TOI_DA, phong.getSoNguoiToiDa());
        values.put(DatabaseHelper.COL_PHONG_TRANG_THAI, phong.getTrangThai());
        values.put(DatabaseHelper.COL_PHONG_MO_TA, phong.getMoTa());
        
        // Tự động gán thời gian tạo nếu chưa có
        values.put(DatabaseHelper.COL_CREATED_AT, phong.getCreatedAt() != null ? phong.getCreatedAt() : now);
        values.put(DatabaseHelper.COL_UPDATED_AT, phong.getUpdatedAt() != null ? phong.getUpdatedAt() : now);
        
        return db.insert(DatabaseHelper.TABLE_PHONG, null, values);
    }

    /**
     * Cập nhật toàn bộ thông tin của một phòng trọ hiện có
     * @param phong Đối tượng chứa thông tin mới kèm theo ID hợp lệ
     * @return Số lượng dòng bị ảnh hưởng (thường là 1)
     */
    public int updatePhong(Phong phong) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_PHONG_SO_PHONG, phong.getSoPhong());
        values.put(DatabaseHelper.COL_PHONG_TEN_PHONG, phong.getTenPhong());
        values.put(DatabaseHelper.COL_PHONG_LOAI_PHONG, phong.getLoaiPhong());
        values.put(DatabaseHelper.COL_PHONG_GIA_PHONG_MAC_DINH, phong.getGiaPhong());
        values.put(DatabaseHelper.COL_PHONG_DIEN_TICH, phong.getDienTich());
        values.put(DatabaseHelper.COL_PHONG_SO_NGUOI_TOI_DA, phong.getSoNguoiToiDa());
        values.put(DatabaseHelper.COL_PHONG_TRANG_THAI, phong.getTrangThai());
        values.put(DatabaseHelper.COL_PHONG_MO_TA, phong.getMoTa());
        values.put(DatabaseHelper.COL_UPDATED_AT, now()); // Cập nhật thời gian sửa
        
        return db.update(
                DatabaseHelper.TABLE_PHONG,
                values,
                DatabaseHelper.COL_ID + "=?", // Điều kiện theo ID
                new String[]{String.valueOf(phong.getId())}
        );
    }

    /**
     * Cập nhật nhanh trạng thái phòng (Ví dụ: Chuyển sang 'Đang thuê' khi có hợp đồng)
     */
    public int updateTrangThaiPhong(int phongId, String trangThai) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_PHONG_TRANG_THAI, trangThai);
        values.put(DatabaseHelper.COL_UPDATED_AT, now());
        return db.update(
                DatabaseHelper.TABLE_PHONG,
                values,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(phongId)}
        );
    }

    /**
     * Xóa một phòng khỏi hệ thống
     */
    public int deletePhong(int phongId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(
                DatabaseHelper.TABLE_PHONG,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(phongId)}
        );
    }

    /**
     * Tìm kiếm phòng theo ID
     * @return Đối tượng Phong nếu tìm thấy, null nếu không có
     */
    public Phong getPhongById(int phongId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // Mở DB ở chế độ đọc
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_PHONG,
                null, // Lấy tất cả các cột
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(phongId)},
                null,
                null,
                null
        )) {
            if (cursor.moveToFirst()) {
                return mapPhong(cursor); // Chuyển đổi từ Cursor sang đối tượng Java
            }
        }
        return null;
    }

    /**
     * Lấy toàn bộ danh sách phòng, sắp xếp theo số phòng tăng dần
     */
    public List<Phong> getAllPhong() {
        List<Phong> danhSachPhong = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_PHONG,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.COL_PHONG_SO_PHONG + " ASC" // Sắp xếp A-Z
        )) {
            while (cursor.moveToNext()) {
                danhSachPhong.add(mapPhong(cursor));
            }
        }
        return danhSachPhong;
    }

    public int demPhongTheoTrangThai(String trangThai) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_PHONG
                        + " WHERE " + DatabaseHelper.COL_PHONG_TRANG_THAI + "=?",
                new String[]{trangThai}
        )) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        }
        return 0;
    }

    public List<Phong> getPhongByTrangThai(String trangThai) {
        List<Phong> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(DatabaseHelper.TABLE_PHONG, null,
                DatabaseHelper.COL_PHONG_TRANG_THAI + "=?", new String[]{trangThai},
                null, null, DatabaseHelper.COL_PHONG_SO_PHONG + " ASC")) {
            while (cursor.moveToNext()) {
                list.add(mapPhong(cursor));
            }
        }
        return list;
    }

    /**
     * Hàm phụ trợ (Utility) để chuyển đổi dữ liệu từ Cursor (SQLite) sang đối tượng Phong (Java)
     */
    private Phong mapPhong(Cursor cursor) {
        Phong phong = new Phong();
        //getColumnIndexOrThrow giúp lấy vị trí cột một cách an toàn
        phong.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)));
        phong.setSoPhong(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONG_SO_PHONG)));
        phong.setTenPhong(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONG_TEN_PHONG)));
        phong.setLoaiPhong(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONG_LOAI_PHONG)));
        phong.setGiaPhong(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONG_GIA_PHONG_MAC_DINH)));
        phong.setDienTich(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONG_DIEN_TICH)));
        phong.setSoNguoiToiDa(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONG_SO_NGUOI_TOI_DA)));
        phong.setTrangThai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONG_TRANG_THAI)));
        phong.setMoTa(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONG_MO_TA)));
        phong.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CREATED_AT)));
        phong.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UPDATED_AT)));
        return phong;
    }

    private String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }
}
