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

    private TextView tvTitle, tvPriceHeader, tvTenantName, tvMemberCount, tvDepositValue, tvMoveInDateValue, tvExpiryDateValue;
    private ImageButton btnBack, btnEdit, btnDelete, btnCallTenant;
    private com.google.android.material.button.MaterialButton btnTerminateContract;
    private androidx.cardview.widget.CardView btnRoomHistory;
    private LinearLayout llMeterAction, llCollectAction, llReportAction;
    
    private PhongRepository repository;
    private com.example.quanlynhatro.data.repository.HopDongRepository hopDongRepository;
    private com.example.quanlynhatro.data.repository.KhachThueRepository khachThueRepository;
    private int phongId = -1;
    private com.example.quanlynhatro.data.model.HopDong currentContract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_phong);

        repository = new PhongRepository(this);
        hopDongRepository = new com.example.quanlynhatro.data.repository.HopDongRepository(this);
        khachThueRepository = new com.example.quanlynhatro.data.repository.KhachThueRepository(this);
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
        tvMemberCount = findViewById(R.id.tvMemberCount);
        tvDepositValue = findViewById(R.id.tvDepositValue);
        tvMoveInDateValue = findViewById(R.id.tvMoveInDateValue);
        tvExpiryDateValue = findViewById(R.id.tvExpiryDateValue);
        
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnCallTenant = findViewById(R.id.btnCallTenant);
        btnTerminateContract = findViewById(R.id.btnTerminateContract);
        btnRoomHistory = findViewById(R.id.btnRoomHistory);
        
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
            Intent intent = new Intent(this, com.example.quanlynhatro.ui.chi_so.NhapChiSoActivity.class);
            intent.putExtra("PHONG_ID", phongId);
            startActivity(intent);
        });

        llCollectAction.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.example.quanlynhatro.ui.hoa_don.ThuTienActivity.class);
            intent.putExtra("PHONG_ID", phongId);
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> {
            com.example.quanlynhatro.utils.UIUtils.showConfirmDialog(
                    this,
                    "Xác nhận xóa phòng",
                    "Bạn có chắc chắn muốn xóa phòng này không? Mọi dữ liệu liên quan sẽ bị mất.",
                    () -> {
                        int result = repository.deletePhong(phongId);
                        if (result > 0) {
                            Toast.makeText(this, "Xóa phòng thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Lỗi khi xóa phòng!", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        });

        btnRoomHistory.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.example.quanlynhatro.ui.hop_dong.DanhSachHopDongActivity.class);
            intent.putExtra("FILTER_PHONG_ID", phongId);
            startActivity(intent);
        });

        btnTerminateContract.setOnClickListener(v -> {
            if (currentContract == null) return;
            
            com.example.quanlynhatro.utils.UIUtils.showConfirmDialog(
                this,
                "Xác nhận trả phòng",
                "Bạn có chắc chắn muốn kết thúc hợp đồng và trả phòng này không?",
                () -> {
                    boolean success = hopDongRepository.thanhLyHopDong(currentContract.getId(), phongId);
                    if (success) {
                        Toast.makeText(this, "Thanh lý hợp đồng thành công!", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(this, "Lỗi khi thanh lý hợp đồng!", Toast.LENGTH_SHORT).show();
                    }
                }
            );
        });
        
        btnCallTenant.setOnClickListener(v -> {
            // Logic gọi điện nếu cần
        });
    }

    private void loadData() {
        Phong phong = repository.getPhongById(phongId);
        if (phong != null) {
            tvTitle.setText("Phòng " + phong.getSoPhong());
            
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            tvPriceHeader.setText(formatter.format(phong.getGiaPhong()) + "đ");
            
            // Load current contract
            currentContract = hopDongRepository.getHopDongHieuLucTheoPhong(phongId);
            if (currentContract != null) {
                // Rented
                com.example.quanlynhatro.data.model.KhachThue tenant = khachThueRepository.getKhachThueById(currentContract.getKhachThueDaiDienId());
                if (tenant != null) {
                    tvTenantName.setText(tenant.getHoTen());
                }
                
                // Get member count
                java.util.List<com.example.quanlynhatro.data.model.ThanhVienVm> members = hopDongRepository.getThanhViensByHopDong(currentContract.getId());
                tvMemberCount.setText("Đang ở: " + (members.size() + 1) + " người"); // +1 for representative
                
                tvDepositValue.setText(formatter.format(currentContract.getTienCoc()) + "đ");
                tvMoveInDateValue.setText(currentContract.getNgayBatDau());
                tvExpiryDateValue.setText(currentContract.getNgayKetThuc());
                
                btnTerminateContract.setVisibility(android.view.View.VISIBLE);
            } else {
                // Empty
                tvTenantName.setText("Phòng trống");
                tvMemberCount.setText("Chưa có khách");
                tvDepositValue.setText("0đ");
                tvMoveInDateValue.setText("--/--/----");
                tvExpiryDateValue.setText("--/--/----");
                btnTerminateContract.setVisibility(android.view.View.GONE);
            }
        }
    }
}
