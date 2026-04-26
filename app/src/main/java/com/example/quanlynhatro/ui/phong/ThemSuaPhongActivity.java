package com.example.quanlynhatro.ui.phong;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;

public class ThemSuaPhongActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sua_phong);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        // Form views
        // etSoPhong = findViewById(R.id.etSoPhong);
        // etTenPhong = findViewById(R.id.etTenPhong);
        // ... etc
    }

}
