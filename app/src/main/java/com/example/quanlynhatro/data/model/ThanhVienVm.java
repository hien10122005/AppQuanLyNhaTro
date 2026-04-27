package com.example.quanlynhatro.data.model;

/**
 * Model đại diện cho một thành viên trong hợp đồng (người ở cùng phòng).
 */
public class ThanhVienVm extends KhachThue {
    private int hopDongId;
    private int khachThueId;
    private String vaiTro;
    private String ngayThamGia;
    private String ngayRoiDi;

    public ThanhVienVm() {
        super();
    }

    public int getHopDongId() {
        return hopDongId;
    }

    public void setHopDongId(int hopDongId) {
        this.hopDongId = hopDongId;
    }

    public int getKhachThueId() {
        return khachThueId;
    }

    public void setKhachThueId(int khachThueId) {
        this.khachThueId = khachThueId;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getNgayThamGia() {
        return ngayThamGia;
    }

    public void setNgayThamGia(String ngayThamGia) {
        this.ngayThamGia = ngayThamGia;
    }

    public String getNgayRoiDi() {
        return ngayRoiDi;
    }

    public void setNgayRoiDi(String ngayRoiDi) {
        this.ngayRoiDi = ngayRoiDi;
    }
}
