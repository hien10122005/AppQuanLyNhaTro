package com.example.quanlynhatro.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DatabaseHelper là lớp trung gian giúp ứng dụng Android làm việc với cơ sở dữ liệu SQLite.
 * Nó kế thừa từ SQLiteOpenHelper để quản lý việc tạo mới, cập nhật và kết nối CSDL.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "quan_ly_nha_tro.db";
    public static final int DATABASE_VERSION = 2;

    public static final String TABLE_PHONG = "phong";
    public static final String TABLE_KHACH_THUE = "khach_thue";
    public static final String TABLE_HOP_DONG = "hop_dong";
    public static final String TABLE_HOP_DONG_THANH_VIEN = "hop_dong_thanh_vien";
    public static final String TABLE_LOAI_DICH_VU = "loai_dich_vu";
    public static final String TABLE_BANG_GIA_DICH_VU = "bang_gia_dich_vu";
    public static final String TABLE_CHI_SO_DICH_VU_THANG = "chi_so_dich_vu_thang";
    public static final String TABLE_HOA_DON = "hoa_don";
    public static final String TABLE_HOA_DON_CHI_TIET = "hoa_don_chi_tiet";
    public static final String TABLE_THANH_TOAN = "thanh_toan";
    public static final String TABLE_SU_CO_BAO_TRI = "su_co_bao_tri";

    // --- ĐỊNH NGHĨA CÁC CỘT CHUNG ---
    // Tại sao dùng hằng số (public static final)? 
    // 1. Tránh gõ sai tên cột (ví dụ gõ "id" thành "id_").
    // 2. Nếu muốn đổi tên cột, chỉ cần sửa ở 1 nơi duy nhất này.
    public static final String COL_ID = "id";
    public static final String COL_CREATED_AT = "created_at";
    public static final String COL_UPDATED_AT = "updated_at";
    public static final String COL_GHI_CHU = "ghi_chu";

    public static final String COL_PHONG_SO_PHONG = "so_phong";
    public static final String COL_PHONG_TEN_PHONG = "ten_phong";
    public static final String COL_PHONG_LOAI_PHONG = "loai_phong";
    public static final String COL_PHONG_GIA_PHONG_MAC_DINH = "gia_phong_mac_dinh";
    public static final String COL_PHONG_DIEN_TICH = "dien_tich";
    public static final String COL_PHONG_SO_NGUOI_TOI_DA = "so_nguoi_toi_da";
    public static final String COL_PHONG_TRANG_THAI = "trang_thai";
    public static final String COL_PHONG_MO_TA = "mo_ta";

    public static final String COL_KHACH_THUE_HO_TEN = "ho_ten";
    public static final String COL_KHACH_THUE_SO_DIEN_THOAI = "so_dien_thoai";
    public static final String COL_KHACH_THUE_CCCD = "cccd";
    public static final String COL_KHACH_THUE_NGAY_SINH = "ngay_sinh";
    public static final String COL_KHACH_THUE_GIOI_TINH = "gioi_tinh";
    public static final String COL_KHACH_THUE_DIA_CHI_THUONG_TRU = "dia_chi_thuong_tru";
    public static final String COL_KHACH_THUE_EMAIL = "email";

    public static final String COL_HOP_DONG_MA_HOP_DONG = "ma_hop_dong";
    public static final String COL_HOP_DONG_PHONG_ID = "phong_id";
    public static final String COL_HOP_DONG_KHACH_THUE_DAI_DIEN_ID = "khach_thue_dai_dien_id";
    public static final String COL_HOP_DONG_NGAY_KY = "ngay_ky";
    public static final String COL_HOP_DONG_NGAY_BAT_DAU = "ngay_bat_dau";
    public static final String COL_HOP_DONG_NGAY_KET_THUC = "ngay_ket_thuc";
    public static final String COL_HOP_DONG_GIA_THUE_CHOT = "gia_thue_chot";
    public static final String COL_HOP_DONG_TIEN_COC = "tien_coc";
    public static final String COL_HOP_DONG_CHU_KY_THANH_TOAN = "chu_ky_thanh_toan";
    public static final String COL_HOP_DONG_TRANG_THAI = "trang_thai";

    public static final String COL_THANH_VIEN_HOP_DONG_ID = "hop_dong_id";
    public static final String COL_THANH_VIEN_KHACH_THUE_ID = "khach_thue_id";
    public static final String COL_THANH_VIEN_VAI_TRO = "vai_tro";
    public static final String COL_THANH_VIEN_NGAY_THAM_GIA = "ngay_tham_gia";
    public static final String COL_THANH_VIEN_NGAY_ROI_DI = "ngay_roi_di";

    public static final String COL_LOAI_DICH_VU_MA_LOAI = "ma_loai";
    public static final String COL_LOAI_DICH_VU_TEN_LOAI = "ten_loai";
    public static final String COL_LOAI_DICH_VU_KIEU_TINH = "kieu_tinh";
    public static final String COL_LOAI_DICH_VU_DON_VI = "don_vi";
    public static final String COL_LOAI_DICH_VU_HOAT_DONG = "hoat_dong";

    public static final String COL_BANG_GIA_LOAI_DICH_VU_ID = "loai_dich_vu_id";
    public static final String COL_BANG_GIA_PHONG_ID = "phong_id";
    public static final String COL_BANG_GIA_DON_GIA = "don_gia";
    public static final String COL_BANG_GIA_SO_LUONG_MAC_DINH = "so_luong_mac_dinh";
    public static final String COL_BANG_GIA_NGAY_HIEU_LUC = "ngay_hieu_luc";
    public static final String COL_BANG_GIA_NGAY_HET_HIEU_LUC = "ngay_het_hieu_luc";

    public static final String COL_CHI_SO_PHONG_ID = "phong_id";
    public static final String COL_CHI_SO_LOAI_DICH_VU_ID = "loai_dich_vu_id";
    public static final String COL_CHI_SO_THANG = "thang";
    public static final String COL_CHI_SO_NAM = "nam";
    public static final String COL_CHI_SO_CU = "chi_so_cu";
    public static final String COL_CHI_SO_MOI = "chi_so_moi";
    public static final String COL_CHI_SO_SO_LUONG_TIEU_THU = "so_luong_tieu_thu";
    public static final String COL_CHI_SO_NGAY_CHOT = "ngay_chot";

    public static final String COL_HOA_DON_MA_HOA_DON = "ma_hoa_don";
    public static final String COL_HOA_DON_HOP_DONG_ID = "hop_dong_id";
    public static final String COL_HOA_DON_PHONG_ID = "phong_id";
    public static final String COL_HOA_DON_THANG = "thang";
    public static final String COL_HOA_DON_NAM = "nam";
    public static final String COL_HOA_DON_NGAY_LAP = "ngay_lap";
    public static final String COL_HOA_DON_HAN_THANH_TOAN = "han_thanh_toan";
    public static final String COL_HOA_DON_TONG_TIEN_TRUOC_GIAM = "tong_tien_truoc_giam";
    public static final String COL_HOA_DON_GIAM_TRU = "giam_tru";
    public static final String COL_HOA_DON_TONG_TIEN = "tong_tien";
    public static final String COL_HOA_DON_DA_THANH_TOAN = "da_thanh_toan";
    public static final String COL_HOA_DON_CON_NO = "con_no";
    public static final String COL_HOA_DON_TRANG_THAI = "trang_thai";

    public static final String COL_HOA_DON_CT_HOA_DON_ID = "hoa_don_id";
    public static final String COL_HOA_DON_CT_LOAI_DICH_VU_ID = "loai_dich_vu_id";
    public static final String COL_HOA_DON_CT_TEN_MUC_PHI = "ten_muc_phi";
    public static final String COL_HOA_DON_CT_SO_LUONG = "so_luong";
    public static final String COL_HOA_DON_CT_DON_GIA_AP_DUNG = "don_gia_ap_dung";
    public static final String COL_HOA_DON_CT_THANH_TIEN = "thanh_tien";
    public static final String COL_HOA_DON_CT_CHI_SO_CU = "chi_so_cu";
    public static final String COL_HOA_DON_CT_CHI_SO_MOI = "chi_so_moi";

    // --- BẢNG THANH TOÁN ---
    public static final String COL_THANH_TOAN_HOA_DON_ID = "hoa_don_id"; // Liên kết tới bảng Hóa Đơn
    public static final String COL_THANH_TOAN_NGAY_THANH_TOAN = "ngay_thanh_toan";
    public static final String COL_THANH_TOAN_SO_TIEN = "so_tien";
    public static final String COL_THANH_TOAN_PHUONG_THUC = "phuong_thuc";
    public static final String COL_THANH_TOAN_MA_GIAO_DICH = "ma_giao_dich";

    public static final String COL_SU_CO_PHONG_ID = "phong_id";
    public static final String COL_SU_CO_HOP_DONG_ID = "hop_dong_id";
    public static final String COL_SU_CO_TIEU_DE = "tieu_de";
    public static final String COL_SU_CO_NOI_DUNG = "noi_dung";
    public static final String COL_SU_CO_NGAY_BAO = "ngay_bao";
    public static final String COL_SU_CO_MUC_DO_UU_TIEN = "muc_do_uu_tien";
    public static final String COL_SU_CO_TRANG_THAI = "trang_thai";
    public static final String COL_SU_CO_CHI_PHI = "chi_phi";
    public static final String COL_SU_CO_NGAY_XU_LY = "ngay_xu_ly";
    public static final String COL_SU_CO_NGUOI_XU_LY = "nguoi_xu_ly";

    public static final String TRANG_THAI_PHONG_TRONG = "TRONG";
    public static final String TRANG_THAI_PHONG_DANG_THUE = "DANG_THUE";
    public static final String TRANG_THAI_PHONG_BAO_TRI = "BAO_TRI";
    public static final String TRANG_THAI_PHONG_NGUNG_SU_DUNG = "NGUNG_SU_DUNG";

    public static final String TRANG_THAI_HOP_DONG_HIEU_LUC = "HIEU_LUC";
    public static final String TRANG_THAI_HOP_DONG_HET_HAN = "HET_HAN";
    public static final String TRANG_THAI_HOP_DONG_DA_THANH_LY = "DA_THANH_LY";
    public static final String TRANG_THAI_HOP_DONG_HUY = "HUY";

    public static final String TRANG_THAI_HOA_DON_CHUA_THANH_TOAN = "CHUA_THANH_TOAN";
    public static final String TRANG_THAI_HOA_DON_THANH_TOAN_MOT_PHAN = "THANH_TOAN_MOT_PHAN";
    public static final String TRANG_THAI_HOA_DON_DA_THANH_TOAN = "DA_THANH_TOAN";
    public static final String TRANG_THAI_HOA_DON_HUY = "HUY";

    public static final String TRANG_THAI_SU_CO_MOI_TAO = "MOI_TAO";
    public static final String TRANG_THAI_SU_CO_DANG_XU_LY = "DANG_XU_LY";
    public static final String TRANG_THAI_SU_CO_DA_XU_LY = "DA_XU_LY";
    public static final String TRANG_THAI_SU_CO_HUY = "HUY";

    // --- CÁC LOẠI DỊCH VỤ MẶC ĐỊNH ---
    // Đây là các giá trị cố định để hệ thống nhận diện loại phí khi tính toán.
    public static final String LOAI_DICH_VU_TIEN_PHONG = "TIEN_PHONG";
    public static final String LOAI_DICH_VU_DIEN = "DIEN";
    public static final String LOAI_DICH_VU_NUOC = "NUOC";
    public static final String LOAI_DICH_VU_RAC = "RAC";
    public static final String LOAI_DICH_VU_WIFI = "WIFI";
    public static final String LOAI_DICH_VU_GUI_XE = "GUI_XE";
    public static final String LOAI_DICH_VU_PHAT_SINH = "PHAT_SINH";
    // --- CÁC CÂU LỆNH SQL TẠO BẢNG ---
    // Ghi chú cho người mới: Dấu + dùng để nối các chuỗi lại với nhau cho dễ đọc.
    // INTEGER PRIMARY KEY AUTOINCREMENT: Cột ID tự động tăng, duy nhất cho mỗi dòng.
    // FOREIGN KEY: Khóa ngoại dùng để liên kết các bảng với nhau (ví dụ: Hợp đồng phải thuộc về một Phòng).

    private static final String SQL_CREATE_PHONG =
            "CREATE TABLE " + TABLE_PHONG + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_PHONG_SO_PHONG + " TEXT NOT NULL UNIQUE, "
                    + COL_PHONG_TEN_PHONG + " TEXT, "
                    + COL_PHONG_LOAI_PHONG + " TEXT, "
                    + COL_PHONG_GIA_PHONG_MAC_DINH + " REAL NOT NULL DEFAULT 0, "
                    + COL_PHONG_DIEN_TICH + " REAL, "
                    + COL_PHONG_SO_NGUOI_TOI_DA + " INTEGER NOT NULL DEFAULT 1, "
                    + COL_PHONG_TRANG_THAI + " TEXT NOT NULL, "
                    + COL_PHONG_MO_TA + " TEXT, "
                    + COL_CREATED_AT + " TEXT NOT NULL, "
                    + COL_UPDATED_AT + " TEXT NOT NULL"
                    + ");";

    private static final String SQL_CREATE_KHACH_THUE =
            "CREATE TABLE " + TABLE_KHACH_THUE + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_KHACH_THUE_HO_TEN + " TEXT NOT NULL, "
                    + COL_KHACH_THUE_SO_DIEN_THOAI + " TEXT NOT NULL, "
                    + COL_KHACH_THUE_CCCD + " TEXT UNIQUE, "
                    + COL_KHACH_THUE_NGAY_SINH + " TEXT, "
                    + COL_KHACH_THUE_GIOI_TINH + " TEXT, "
                    + COL_KHACH_THUE_DIA_CHI_THUONG_TRU + " TEXT, "
                    + COL_KHACH_THUE_EMAIL + " TEXT, "
                    + COL_GHI_CHU + " TEXT, "
                    + COL_CREATED_AT + " TEXT NOT NULL, "
                    + COL_UPDATED_AT + " TEXT NOT NULL"
                    + ");";

    private static final String SQL_CREATE_HOP_DONG =
            "CREATE TABLE " + TABLE_HOP_DONG + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_HOP_DONG_MA_HOP_DONG + " TEXT NOT NULL UNIQUE, "
                    + COL_HOP_DONG_PHONG_ID + " INTEGER NOT NULL, "
                    + COL_HOP_DONG_KHACH_THUE_DAI_DIEN_ID + " INTEGER NOT NULL, "
                    + COL_HOP_DONG_NGAY_KY + " TEXT, "
                    + COL_HOP_DONG_NGAY_BAT_DAU + " TEXT NOT NULL, "
                    + COL_HOP_DONG_NGAY_KET_THUC + " TEXT, "
                    + COL_HOP_DONG_GIA_THUE_CHOT + " REAL NOT NULL DEFAULT 0, "
                    + COL_HOP_DONG_TIEN_COC + " REAL NOT NULL DEFAULT 0, "
                    + COL_HOP_DONG_CHU_KY_THANH_TOAN + " INTEGER NOT NULL DEFAULT 1, "
                    + COL_HOP_DONG_TRANG_THAI + " TEXT NOT NULL, "
                    + COL_GHI_CHU + " TEXT, "
                    + COL_CREATED_AT + " TEXT NOT NULL, "
                    + COL_UPDATED_AT + " TEXT NOT NULL, "
                    + "FOREIGN KEY (" + COL_HOP_DONG_PHONG_ID + ") REFERENCES " + TABLE_PHONG + "(" + COL_ID + "), "
                    + "FOREIGN KEY (" + COL_HOP_DONG_KHACH_THUE_DAI_DIEN_ID + ") REFERENCES " + TABLE_KHACH_THUE + "(" + COL_ID + ")"
                    + ");";

    private static final String SQL_CREATE_HOP_DONG_THANH_VIEN =
            "CREATE TABLE " + TABLE_HOP_DONG_THANH_VIEN + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_THANH_VIEN_HOP_DONG_ID + " INTEGER NOT NULL, "
                    + COL_THANH_VIEN_KHACH_THUE_ID + " INTEGER NOT NULL, "
                    + COL_THANH_VIEN_VAI_TRO + " TEXT NOT NULL DEFAULT 'THANH_VIEN', "
                    + COL_THANH_VIEN_NGAY_THAM_GIA + " TEXT, "
                    + COL_THANH_VIEN_NGAY_ROI_DI + " TEXT, "
                    + COL_GHI_CHU + " TEXT, "
                    + "FOREIGN KEY (" + COL_THANH_VIEN_HOP_DONG_ID + ") REFERENCES " + TABLE_HOP_DONG + "(" + COL_ID + "), "
                    + "FOREIGN KEY (" + COL_THANH_VIEN_KHACH_THUE_ID + ") REFERENCES " + TABLE_KHACH_THUE + "(" + COL_ID + ")"
                    + ");";

    private static final String SQL_CREATE_LOAI_DICH_VU =
            "CREATE TABLE " + TABLE_LOAI_DICH_VU + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_LOAI_DICH_VU_MA_LOAI + " TEXT NOT NULL UNIQUE, "
                    + COL_LOAI_DICH_VU_TEN_LOAI + " TEXT NOT NULL, "
                    + COL_LOAI_DICH_VU_KIEU_TINH + " TEXT NOT NULL, "
                    + COL_LOAI_DICH_VU_DON_VI + " TEXT, "
                    + COL_LOAI_DICH_VU_HOAT_DONG + " INTEGER NOT NULL DEFAULT 1"
                    + ");";

    private static final String SQL_CREATE_BANG_GIA_DICH_VU =
            "CREATE TABLE " + TABLE_BANG_GIA_DICH_VU + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_BANG_GIA_LOAI_DICH_VU_ID + " INTEGER NOT NULL, "
                    + COL_BANG_GIA_PHONG_ID + " INTEGER, "
                    + COL_BANG_GIA_DON_GIA + " REAL NOT NULL, "
                    + COL_BANG_GIA_SO_LUONG_MAC_DINH + " REAL, "
                    + COL_BANG_GIA_NGAY_HIEU_LUC + " TEXT NOT NULL, "
                    + COL_BANG_GIA_NGAY_HET_HIEU_LUC + " TEXT, "
                    + COL_GHI_CHU + " TEXT, "
                    + COL_CREATED_AT + " TEXT NOT NULL, "
                    + "FOREIGN KEY (" + COL_BANG_GIA_LOAI_DICH_VU_ID + ") REFERENCES " + TABLE_LOAI_DICH_VU + "(" + COL_ID + "), "
                    + "FOREIGN KEY (" + COL_BANG_GIA_PHONG_ID + ") REFERENCES " + TABLE_PHONG + "(" + COL_ID + ")"
                    + ");";

    private static final String SQL_CREATE_CHI_SO_DICH_VU_THANG =
            "CREATE TABLE " + TABLE_CHI_SO_DICH_VU_THANG + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_CHI_SO_PHONG_ID + " INTEGER NOT NULL, "
                    + COL_CHI_SO_LOAI_DICH_VU_ID + " INTEGER NOT NULL, "
                    + COL_CHI_SO_THANG + " INTEGER NOT NULL, "
                    + COL_CHI_SO_NAM + " INTEGER NOT NULL, "
                    + COL_CHI_SO_CU + " REAL NOT NULL DEFAULT 0, "
                    + COL_CHI_SO_MOI + " REAL NOT NULL DEFAULT 0, "
                    + COL_CHI_SO_SO_LUONG_TIEU_THU + " REAL NOT NULL DEFAULT 0, "
                    + COL_CHI_SO_NGAY_CHOT + " TEXT, "
                    + COL_GHI_CHU + " TEXT, "
                    + COL_CREATED_AT + " TEXT NOT NULL, "
                    + "FOREIGN KEY (" + COL_CHI_SO_PHONG_ID + ") REFERENCES " + TABLE_PHONG + "(" + COL_ID + "), "
                    + "FOREIGN KEY (" + COL_CHI_SO_LOAI_DICH_VU_ID + ") REFERENCES " + TABLE_LOAI_DICH_VU + "(" + COL_ID + "), "
                    + "UNIQUE (" + COL_CHI_SO_PHONG_ID + ", " + COL_CHI_SO_LOAI_DICH_VU_ID + ", " + COL_CHI_SO_THANG + ", " + COL_CHI_SO_NAM + ")"
                    + ");";

    private static final String SQL_CREATE_HOA_DON =
            "CREATE TABLE " + TABLE_HOA_DON + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_HOA_DON_MA_HOA_DON + " TEXT NOT NULL UNIQUE, "
                    + COL_HOA_DON_HOP_DONG_ID + " INTEGER NOT NULL, "
                    + COL_HOA_DON_PHONG_ID + " INTEGER NOT NULL, "
                    + COL_HOA_DON_THANG + " INTEGER NOT NULL, "
                    + COL_HOA_DON_NAM + " INTEGER NOT NULL, "
                    + COL_HOA_DON_NGAY_LAP + " TEXT NOT NULL, "
                    + COL_HOA_DON_HAN_THANH_TOAN + " TEXT, "
                    + COL_HOA_DON_TONG_TIEN_TRUOC_GIAM + " REAL NOT NULL DEFAULT 0, "
                    + COL_HOA_DON_GIAM_TRU + " REAL NOT NULL DEFAULT 0, "
                    + COL_HOA_DON_TONG_TIEN + " REAL NOT NULL DEFAULT 0, "
                    + COL_HOA_DON_DA_THANH_TOAN + " REAL NOT NULL DEFAULT 0, "
                    + COL_HOA_DON_CON_NO + " REAL NOT NULL DEFAULT 0, "
                    + COL_HOA_DON_TRANG_THAI + " TEXT NOT NULL, "
                    + COL_GHI_CHU + " TEXT, "
                    + COL_CREATED_AT + " TEXT NOT NULL, "
                    + COL_UPDATED_AT + " TEXT NOT NULL, "
                    + "FOREIGN KEY (" + COL_HOA_DON_HOP_DONG_ID + ") REFERENCES " + TABLE_HOP_DONG + "(" + COL_ID + "), "
                    + "FOREIGN KEY (" + COL_HOA_DON_PHONG_ID + ") REFERENCES " + TABLE_PHONG + "(" + COL_ID + "), "
                    + "UNIQUE (" + COL_HOA_DON_HOP_DONG_ID + ", " + COL_HOA_DON_THANG + ", " + COL_HOA_DON_NAM + ")"
                    + ");";

    private static final String SQL_CREATE_HOA_DON_CHI_TIET =
            "CREATE TABLE " + TABLE_HOA_DON_CHI_TIET + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_HOA_DON_CT_HOA_DON_ID + " INTEGER NOT NULL, "
                    + COL_HOA_DON_CT_LOAI_DICH_VU_ID + " INTEGER NOT NULL, "
                    + COL_HOA_DON_CT_TEN_MUC_PHI + " TEXT NOT NULL, "
                    + COL_HOA_DON_CT_SO_LUONG + " REAL NOT NULL DEFAULT 1, "
                    + COL_HOA_DON_CT_DON_GIA_AP_DUNG + " REAL NOT NULL DEFAULT 0, "
                    + COL_HOA_DON_CT_THANH_TIEN + " REAL NOT NULL DEFAULT 0, "
                    + COL_HOA_DON_CT_CHI_SO_CU + " REAL, "
                    + COL_HOA_DON_CT_CHI_SO_MOI + " REAL, "
                    + COL_GHI_CHU + " TEXT, "
                    + "FOREIGN KEY (" + COL_HOA_DON_CT_HOA_DON_ID + ") REFERENCES " + TABLE_HOA_DON + "(" + COL_ID + "), "
                    + "FOREIGN KEY (" + COL_HOA_DON_CT_LOAI_DICH_VU_ID + ") REFERENCES " + TABLE_LOAI_DICH_VU + "(" + COL_ID + ")"
                    + ");";

    private static final String SQL_CREATE_THANH_TOAN =
            "CREATE TABLE " + TABLE_THANH_TOAN + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_THANH_TOAN_HOA_DON_ID + " INTEGER NOT NULL, "
                    + COL_THANH_TOAN_NGAY_THANH_TOAN + " TEXT NOT NULL, "
                    + COL_THANH_TOAN_SO_TIEN + " REAL NOT NULL, "
                    + COL_THANH_TOAN_PHUONG_THUC + " TEXT NOT NULL, "
                    + COL_THANH_TOAN_MA_GIAO_DICH + " TEXT, "
                    + COL_GHI_CHU + " TEXT, "
                    + COL_CREATED_AT + " TEXT NOT NULL, "
                    + "FOREIGN KEY (" + COL_THANH_TOAN_HOA_DON_ID + ") REFERENCES " + TABLE_HOA_DON + "(" + COL_ID + ")"
                    + ");";

    private static final String SQL_CREATE_SU_CO_BAO_TRI =
            "CREATE TABLE " + TABLE_SU_CO_BAO_TRI + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_SU_CO_PHONG_ID + " INTEGER NOT NULL, "
                    + COL_SU_CO_HOP_DONG_ID + " INTEGER, "
                    + COL_SU_CO_TIEU_DE + " TEXT NOT NULL, "
                    + COL_SU_CO_NOI_DUNG + " TEXT NOT NULL, "
                    + COL_SU_CO_NGAY_BAO + " TEXT NOT NULL, "
                    + COL_SU_CO_MUC_DO_UU_TIEN + " TEXT NOT NULL DEFAULT 'TRUNG_BINH', "
                    + COL_SU_CO_TRANG_THAI + " TEXT NOT NULL, "
                    + COL_SU_CO_CHI_PHI + " REAL NOT NULL DEFAULT 0, "
                    + COL_SU_CO_NGAY_XU_LY + " TEXT, "
                    + COL_SU_CO_NGUOI_XU_LY + " TEXT, "
                    + COL_GHI_CHU + " TEXT, "
                    + COL_CREATED_AT + " TEXT NOT NULL, "
                    + COL_UPDATED_AT + " TEXT NOT NULL, "
                    + "FOREIGN KEY (" + COL_SU_CO_PHONG_ID + ") REFERENCES " + TABLE_PHONG + "(" + COL_ID + "), "
                    + "FOREIGN KEY (" + COL_SU_CO_HOP_DONG_ID + ") REFERENCES " + TABLE_HOP_DONG + "(" + COL_ID + ")"
                    + ");";

    public DatabaseHelper(Context context) {
        // Gọi constructor của lớp cha (SQLiteOpenHelper) để khởi tạo file CSDL
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * onConfigure được gọi trước khi onCreate hoặc onUpgrade.
     * Ở đây chúng ta bật tính năng Foreign Key (Khóa ngoại) để đảm bảo tính toàn vẹn dữ liệu.
     */
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    /**
     * onCreate được gọi lần đầu tiên khi ứng dụng được cài đặt và CSDL chưa tồn tại.
     * Đây là nơi chúng ta tạo ra các bảng và dữ liệu mẫu ban đầu.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Sử dụng Transaction giúp đảm bảo: hoặc là tất cả bảng được tạo thành công, 
        // hoặc là không có bảng nào được tạo (nếu xảy ra lỗi giữa chừng).
        db.beginTransaction();
        try {
            db.execSQL(SQL_CREATE_PHONG);
            db.execSQL(SQL_CREATE_KHACH_THUE);
            db.execSQL(SQL_CREATE_HOP_DONG);
            db.execSQL(SQL_CREATE_HOP_DONG_THANH_VIEN);
            db.execSQL(SQL_CREATE_LOAI_DICH_VU);
            db.execSQL(SQL_CREATE_BANG_GIA_DICH_VU);
            db.execSQL(SQL_CREATE_CHI_SO_DICH_VU_THANG);
            db.execSQL(SQL_CREATE_HOA_DON);
            db.execSQL(SQL_CREATE_HOA_DON_CHI_TIET);
            db.execSQL(SQL_CREATE_THANH_TOAN);
            db.execSQL(SQL_CREATE_SU_CO_BAO_TRI);

            createIndexes(db); // Tạo chỉ mục để tìm kiếm nhanh hơn
            seedLoaiDichVu(db); // Thêm dữ liệu mẫu ban đầu

            db.setTransactionSuccessful(); // Đánh dấu mọi thứ đã ổn
        } finally {
            db.endTransaction(); // Kết thúc quá trình (hoàn tất hoặc hủy bỏ)
        }
    }

    /**
     * onUpgrade được gọi khi bạn tăng số DATABASE_VERSION (ví dụ từ 1 lên 2).
     * Thông thường, chúng ta sẽ xóa các bảng cũ và gọi lại onCreate để tạo bảng mới.
     * Lưu ý: Cách này sẽ làm mất hết dữ liệu cũ!
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.beginTransaction();
        try {
            // Xóa theo thứ tự ngược lại để tránh lỗi ràng buộc khóa ngoại
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SU_CO_BAO_TRI);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_THANH_TOAN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOA_DON_CHI_TIET);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOA_DON);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHI_SO_DICH_VU_THANG);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BANG_GIA_DICH_VU);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOAI_DICH_VU);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOP_DONG_THANH_VIEN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOP_DONG);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_KHACH_THUE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHONG);
            onCreate(db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Index (Chỉ mục) giống như mục lục của một cuốn sách.
     * Nó giúp CSDL tìm kiếm dữ liệu cực nhanh mà không cần quét qua toàn bộ các dòng.
     */
    private void createIndexes(SQLiteDatabase db) {
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_phong_so_phong ON " + TABLE_PHONG + " (" + COL_PHONG_SO_PHONG + ")");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_hop_dong_phong_trang_thai ON " + TABLE_HOP_DONG + " (" + COL_HOP_DONG_PHONG_ID + ", " + COL_HOP_DONG_TRANG_THAI + ")");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_hop_dong_ngay_ket_thuc ON " + TABLE_HOP_DONG + " (" + COL_HOP_DONG_NGAY_KET_THUC + ")");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_hoa_don_hop_dong_ky ON " + TABLE_HOA_DON + " (" + COL_HOA_DON_HOP_DONG_ID + ", " + COL_HOA_DON_THANG + ", " + COL_HOA_DON_NAM + ")");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_hoa_don_trang_thai ON " + TABLE_HOA_DON + " (" + COL_HOA_DON_TRANG_THAI + ")");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_chi_so_dich_vu_ky ON " + TABLE_CHI_SO_DICH_VU_THANG + " (" + COL_CHI_SO_PHONG_ID + ", " + COL_CHI_SO_LOAI_DICH_VU_ID + ", " + COL_CHI_SO_THANG + ", " + COL_CHI_SO_NAM + ")");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_su_co_phong_trang_thai ON " + TABLE_SU_CO_BAO_TRI + " (" + COL_SU_CO_PHONG_ID + ", " + COL_SU_CO_TRANG_THAI + ")");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_thanh_toan_hoa_don_ngay ON " + TABLE_THANH_TOAN + " (" + COL_THANH_TOAN_HOA_DON_ID + ", " + COL_THANH_TOAN_NGAY_THANH_TOAN + ")");
    }

    private void seedLoaiDichVu(SQLiteDatabase db) {
        insertLoaiDichVu(db, LOAI_DICH_VU_TIEN_PHONG, "Tien phong", "CO_DINH", "thang");
        insertLoaiDichVu(db, LOAI_DICH_VU_DIEN, "Tien dien", "THEO_CHI_SO", "kWh");
        insertLoaiDichVu(db, LOAI_DICH_VU_NUOC, "Tien nuoc", "THEO_CHI_SO", "m3");
        insertLoaiDichVu(db, LOAI_DICH_VU_RAC, "Phi rac", "CO_DINH", "thang");
        insertLoaiDichVu(db, LOAI_DICH_VU_WIFI, "Wifi", "CO_DINH", "thang");
        insertLoaiDichVu(db, LOAI_DICH_VU_GUI_XE, "Gui xe", "SO_LUONG", "xe");
        insertLoaiDichVu(db, LOAI_DICH_VU_PHAT_SINH, "Phi khac", "CO_DINH", "lan");
    }

    private void insertLoaiDichVu(SQLiteDatabase db, String maLoai, String tenLoai, String kieuTinh, String donVi) {
        ContentValues values = new ContentValues();
        values.put(COL_LOAI_DICH_VU_MA_LOAI, maLoai);
        values.put(COL_LOAI_DICH_VU_TEN_LOAI, tenLoai);
        values.put(COL_LOAI_DICH_VU_KIEU_TINH, kieuTinh);
        values.put(COL_LOAI_DICH_VU_DON_VI, donVi);
        values.put(COL_LOAI_DICH_VU_HOAT_DONG, 1);
        // CONFLICT_IGNORE: Nếu mã loại dịch vụ đã tồn tại rồi thì không báo lỗi, chỉ đơn giản là bỏ qua.
        db.insertWithOnConflict(TABLE_LOAI_DICH_VU, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }
}
