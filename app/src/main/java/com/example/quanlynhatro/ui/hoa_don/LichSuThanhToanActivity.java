package com.example.quanlynhatro.ui.hoa_don;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.model.HoaDon;
import com.example.quanlynhatro.data.model.ThanhToan;
import com.example.quanlynhatro.data.repository.HoaDonRepository;
import com.example.quanlynhatro.data.repository.ThanhToanRepository;

import java.util.List;

/**
 * Màn hình hiển thị danh sách các lần trả tiền của một hóa đơn.
 * Giúp chủ trọ theo dõi được khách đã trả bao nhiêu lần, mỗi lần bao nhiêu.
 */
public class LichSuThanhToanActivity extends AppCompatActivity {

    private RecyclerView rvLichSu;
    private ImageButton btnBack;
    private TextView tvTitle, tvEmpty;
    private LinearLayout layoutHeader;

    private ThanhToanRepository thanhToanRepository;
    private HoaDonRepository hoaDonRepository;
    private int hoaDonId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_thanh_toan);

        thanhToanRepository = new ThanhToanRepository(this);
        hoaDonRepository = new HoaDonRepository(this);
        
        // Nhận ID hóa đơn từ màn hình trước gửi sang
        hoaDonId = getIntent().getIntExtra("hoa_don_id", -1);

        initViews();
        loadData();
    }

    private void initViews() {
        rvLichSu = findViewById(R.id.rvLichSuThanhToan);
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvToolbarTitle);
        tvEmpty = findViewById(R.id.tvEmptyState);
        
        rvLichSu.setLayoutManager(new LinearLayoutManager(this));
        
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadData() {
        if (hoaDonId == -1) {
            Toast.makeText(this, "Lỗi dữ liệu!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 1. Lấy thông tin hóa đơn để hiển thị tiêu đề
        HoaDon hd = hoaDonRepository.getHoaDonById(hoaDonId);
        if (hd != null) {
            tvTitle.setText("Lịch sử thu tiền: " + hd.getMaHoaDon());
        }

        // 2. Lấy danh sách các đợt trả tiền từ Repository
        List<ThanhToan> list = thanhToanRepository.getLichSuThanhToan(hoaDonId);

        if (list.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvLichSu.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvLichSu.setVisibility(View.VISIBLE);
            
            // Đổ dữ liệu vào Adapter (giống DataBind trong C#)
            ThanhToanAdapter adapter = new ThanhToanAdapter(list);
            rvLichSu.setAdapter(adapter);
        }
    }
}
