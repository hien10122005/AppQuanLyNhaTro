package com.example.quanlynhatro.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.SuCoBaoTri;
import com.example.quanlynhatro.data.model.SuCoBaoTriVm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SuCoBaoTriRepository {
    private final DatabaseHelper dbHelper;

    public SuCoBaoTriRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public long addSuCoBaoTri(SuCoBaoTri suCo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String now = now();

        // ContentValues trong Android gan giong mot "ban do cot -> gia tri"
        // truoc khi insert vao SQL.
        ContentValues values = toContentValues(suCo, now);
        if (suCo.getCreatedAt() == null || suCo.getCreatedAt().trim().isEmpty()) {
            values.put(DatabaseHelper.COL_CREATED_AT, now);
        }
        if (suCo.getUpdatedAt() == null || suCo.getUpdatedAt().trim().isEmpty()) {
            values.put(DatabaseHelper.COL_UPDATED_AT, now);
        }
        return db.insert(DatabaseHelper.TABLE_SU_CO_BAO_TRI, null, values);
    }

    public int updateSuCoBaoTri(SuCoBaoTri suCo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = toContentValues(suCo, now());
        return db.update(
                DatabaseHelper.TABLE_SU_CO_BAO_TRI,
                values,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(suCo.getId())}
        );
    }

    public int deleteSuCoBaoTri(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(
                DatabaseHelper.TABLE_SU_CO_BAO_TRI,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)}
        );
    }

    public SuCoBaoTri getSuCoBaoTriById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_SU_CO_BAO_TRI,
                null,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        )) {
            if (cursor.moveToFirst()) {
                return mapSuCoBaoTri(cursor);
            }
        }
        return null;
    }

    public List<SuCoBaoTriVm> getAllSuCoBaoTriVm() {
        List<SuCoBaoTriVm> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // JOIN de lay them ten phong. Neu chi query bang su_co_bao_tri thi RecyclerView
        // chi biet phongId, khong biet ten phong de hien thi.
        String sql = "SELECT s.*, p." + DatabaseHelper.COL_PHONG_TEN_PHONG
                + " FROM " + DatabaseHelper.TABLE_SU_CO_BAO_TRI + " s "
                + " LEFT JOIN " + DatabaseHelper.TABLE_PHONG + " p ON s."
                + DatabaseHelper.COL_SU_CO_PHONG_ID + " = p." + DatabaseHelper.COL_ID
                + " ORDER BY s." + DatabaseHelper.COL_SU_CO_NGAY_BAO + " DESC, s."
                + DatabaseHelper.COL_ID + " DESC";

        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                list.add(mapSuCoBaoTriVm(cursor));
            }
        }
        return list;
    }

    private ContentValues toContentValues(SuCoBaoTri suCo, String updatedAt) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_SU_CO_PHONG_ID, suCo.getPhongId());
        if (suCo.getHopDongId() == null) {
            values.putNull(DatabaseHelper.COL_SU_CO_HOP_DONG_ID);
        } else {
            values.put(DatabaseHelper.COL_SU_CO_HOP_DONG_ID, suCo.getHopDongId());
        }
        values.put(DatabaseHelper.COL_SU_CO_TIEU_DE, suCo.getTieuDe());
        values.put(DatabaseHelper.COL_SU_CO_NOI_DUNG, suCo.getNoiDung());
        values.put(DatabaseHelper.COL_SU_CO_NGAY_BAO, suCo.getNgayBao());
        values.put(DatabaseHelper.COL_SU_CO_MUC_DO_UU_TIEN, suCo.getMucDoUuTien());
        values.put(DatabaseHelper.COL_SU_CO_TRANG_THAI, suCo.getTrangThai());
        values.put(DatabaseHelper.COL_SU_CO_CHI_PHI, suCo.getChiPhi());
        values.put(DatabaseHelper.COL_SU_CO_NGAY_XU_LY, suCo.getNgayXuLy());
        values.put(DatabaseHelper.COL_SU_CO_NGUOI_XU_LY, suCo.getNguoiXuLy());
        values.put(DatabaseHelper.COL_GHI_CHU, suCo.getGhiChu());
        values.put(DatabaseHelper.COL_UPDATED_AT, updatedAt);
        return values;
    }

    private SuCoBaoTriVm mapSuCoBaoTriVm(Cursor cursor) {
        // Giong nhu map tu DataRow/DataReader sang object trong C#.
        SuCoBaoTri base = mapSuCoBaoTri(cursor);
        SuCoBaoTriVm vm = new SuCoBaoTriVm();

        // Buoc nay la "copy du lieu goc" tu entity sang ViewModel.
        // ViewModel se duoc dua len UI, nen ngoai field goc no co them tenPhong.
        vm.setId(base.getId());
        vm.setPhongId(base.getPhongId());
        vm.setHopDongId(base.getHopDongId());
        vm.setTieuDe(base.getTieuDe());
        vm.setNoiDung(base.getNoiDung());
        vm.setNgayBao(base.getNgayBao());
        vm.setMucDoUuTien(base.getMucDoUuTien());
        vm.setTrangThai(base.getTrangThai());
        vm.setChiPhi(base.getChiPhi());
        vm.setNgayXuLy(base.getNgayXuLy());
        vm.setNguoiXuLy(base.getNguoiXuLy());
        vm.setGhiChu(base.getGhiChu());
        vm.setCreatedAt(base.getCreatedAt());
        vm.setUpdatedAt(base.getUpdatedAt());

        // tenPhong chi co khi query JOIN voi bang PHONG.
        // Neu sau nay ban query khong JOIN, dong nay co the gay loi vi khong ton tai cot ten_phong.
        vm.setTenPhong(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONG_TEN_PHONG)));
        return vm;
    }

    private SuCoBaoTri mapSuCoBaoTri(Cursor cursor) {
        SuCoBaoTri suCo = new SuCoBaoTri();
        suCo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)));
        suCo.setPhongId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SU_CO_PHONG_ID)));

        // Cot hop_dong_id cho phep null, nen phai kiem tra truoc khi doc so nguyen.
        int hopDongIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SU_CO_HOP_DONG_ID);
        suCo.setHopDongId(cursor.isNull(hopDongIndex) ? null : cursor.getInt(hopDongIndex));

        suCo.setTieuDe(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SU_CO_TIEU_DE)));
        suCo.setNoiDung(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SU_CO_NOI_DUNG)));
        suCo.setNgayBao(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SU_CO_NGAY_BAO)));
        suCo.setMucDoUuTien(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SU_CO_MUC_DO_UU_TIEN)));
        suCo.setTrangThai(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SU_CO_TRANG_THAI)));
        suCo.setChiPhi(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SU_CO_CHI_PHI)));
        suCo.setNgayXuLy(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SU_CO_NGAY_XU_LY)));
        suCo.setNguoiXuLy(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SU_CO_NGUOI_XU_LY)));
        suCo.setGhiChu(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_GHI_CHU)));
        suCo.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CREATED_AT)));
        suCo.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UPDATED_AT)));
        return suCo;
    }

    private String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }
}
