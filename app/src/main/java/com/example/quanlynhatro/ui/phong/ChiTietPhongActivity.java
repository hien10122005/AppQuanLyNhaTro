package com.example.quanlynhatro.ui.phong;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.Phong;
import com.example.quanlynhatro.data.repository.PhongRepository;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * ChiTietPhongActivity hiển thị toàn bộ thông tin chi tiết của một căn phòng.
 * Bao gồm: Giá, khách thuê, hợp đồng, và các lối tắt hành động (chốt số, thu tiền).
 */
public class ChiTietPhongActivity extends AppCompatActivity {

    private TextView tvTitle, tvPriceHeader, tvTenantName, tvTenantPhone;
    private ImageButton btnBack, btnEdit;
    private LinearLayout llMeterAction, llCollectAction, llReportAction;
    
    private PhongRepository repository;
    private int phongId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_phong);

        repository = new PhongRepository(this);
        phongId = getIntent().getIntExtra("PHONG_ID", -1);

        if (phongId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin phòng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvPriceHeader = findViewById(R.id.tvPriceHeader);
        tvTenantName = findViewById(R.id.tvTenantName);
        tvTenantPhone = findViewById(R.id.tvTenantPhone);
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        llMeterAction = findViewById(R.id.llMeterAction);
        llCollectAction = findViewById(R.id.llCollectAction);
        llReportAction = findViewById(R.id.llReportAction);
    }

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, ThemSuaPhongActivity.class);
            intent.putExtra("PHONG_ID", phongId);
            startActivity(intent);
        });

        llMeterAction.setOnClickListener(v -> {
            // Chuyển sang màn hình chốt số điện nước
            Intent intent = new Intent(this, com.example.quanlynhatro.ui.chi_so.NhapChiSoActivity.class);
            intent.putExtra("PHONG_ID", phongId);
            startActivity(intent);
        });

        llCollectAction.setOnClickListener(v -> {
            // Chuyển sang màn hình thu tiền
            Intent intent = new Intent(this, com.example.quanlynhatro.ui.hoa_don.ThuTienActivity.class);
            intent.putExtra("PHONG_ID", phongId);
            startActivity(intent);
        });
    }

    private void loadData() {
        Phong phong = repository.getPhongById(phongId);
        if (phong != null) {
            tvTitle.setText("Phòng " + phong.getSoPhong());
            
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            tvPriceHeader.setText(formatter.format(phong.getGiaPhong()) + "đ");
            
            // Ở đây bạn có thể lấy thêm thông tin Khách thuê từ KhachThueRepository
            // Hiện tại tôi để mặc định hoặc lấy từ dữ liệu mẫu nếu có logic JOIN
            // tvTenantName.setText(...);
        }
    }
}
