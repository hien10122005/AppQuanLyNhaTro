package com.example.quanlynhatro.ui.phong;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlynhatro.R;

public class DanhSachPhongActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_phong);
        Button btnThemPhong = findViewById(R.id.btnThemPhong);
        btnThemPhong.setOnClickListener(v -> startActivity(
                new android.content.Intent(this, ThemSuaPhongActivity.class)
        ));
    }
}
