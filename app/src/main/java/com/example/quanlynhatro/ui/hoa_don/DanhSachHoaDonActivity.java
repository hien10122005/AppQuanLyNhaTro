package com.example.quanlynhatro.ui.hoa_don;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlynhatro.R;

public class DanhSachHoaDonActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_hoa_don);
        Button btnLapHoaDon = findViewById(R.id.btnLapHoaDon);
        Button btnThuTien = findViewById(R.id.btnThuTien);

        btnLapHoaDon.setOnClickListener(v -> startActivity(
                new android.content.Intent(this, LapHoaDonActivity.class)
        ));
        btnThuTien.setOnClickListener(v -> startActivity(
                new android.content.Intent(this, ThuTienActivity.class)
        ));
    }
}
