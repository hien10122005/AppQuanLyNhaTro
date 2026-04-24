package com.example.quanlynhatro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.ui.bao_tri.DanhSachBaoTriActivity;
import com.example.quanlynhatro.ui.chi_so.NhapChiSoActivity;
import com.example.quanlynhatro.ui.hoa_don.DanhSachHoaDonActivity;
import com.example.quanlynhatro.ui.hoa_don.LapHoaDonActivity;
import com.example.quanlynhatro.ui.hoa_don.ThuTienActivity;
import com.example.quanlynhatro.ui.hop_dong.DanhSachHopDongActivity;
import com.example.quanlynhatro.ui.khach_thue.DanhSachKhachThueActivity;
import com.example.quanlynhatro.ui.phong.DanhSachPhongActivity;
import com.example.quanlynhatro.ui.thong_ke.TongQuanActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DatabaseHelper(this).getWritableDatabase().close();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupNavigation();
    }

    private void setupNavigation() {
        bindButton(R.id.btnTongQuan, TongQuanActivity.class);
        bindButton(R.id.btnPhong, DanhSachPhongActivity.class);
        bindButton(R.id.btnKhachThue, DanhSachKhachThueActivity.class);
        bindButton(R.id.btnHopDong, DanhSachHopDongActivity.class);
        bindButton(R.id.btnChiSo, NhapChiSoActivity.class);
        bindButton(R.id.btnHoaDon, DanhSachHoaDonActivity.class);
        bindButton(R.id.btnLapHoaDon, LapHoaDonActivity.class);
        bindButton(R.id.btnThuTien, ThuTienActivity.class);
        bindButton(R.id.btnBaoTri, DanhSachBaoTriActivity.class);
    }

    private void bindButton(int viewId, Class<?> activityClass) {
        Button button = findViewById(viewId);
        button.setOnClickListener(v -> startActivity(new Intent(this, activityClass)));
    }
}
