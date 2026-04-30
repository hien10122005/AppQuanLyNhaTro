package com.example.quanlynhatro.data.model;

import java.io.Serializable;

/**
 * ViewModel cho man danh sach bao tri.
 * Coi no nhu mot DTO trong C#: ngoai du lieu goc cua su co, no con co them ten phong
 * de RecyclerView bind du lieu ma khong phai query tung dong.
 */
public class SuCoBaoTriVm extends SuCoBaoTri implements Serializable {
    private static final long serialVersionUID = 1L;

    // Ten phong duoc JOIN them tu bang PHONG de man danh sach hien thi cho de doc.
    private String tenPhong;

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }
}
