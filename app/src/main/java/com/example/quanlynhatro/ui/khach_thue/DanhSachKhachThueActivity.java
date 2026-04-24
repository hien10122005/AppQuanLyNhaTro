package com.example.quanlynhatro.ui.khach_thue;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlynhatro.R;

public class DanhSachKhachThueActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_khach_thue);
        Button btnThemKhachThue = findViewById(R.id.btnThemKhachThue);
        btnThemKhachThue.setOnClickListener(v -> startActivity(
                new android.content.Intent(this, ThemSuaKhachThueActivity.class)
        ));
    }
}
