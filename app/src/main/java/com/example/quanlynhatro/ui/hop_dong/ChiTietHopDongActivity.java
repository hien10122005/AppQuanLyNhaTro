package com.example.quanlynhatro.ui.hop_dong;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.model.KhachThue;
import com.example.quanlynhatro.data.model.HopDongVm;
import com.example.quanlynhatro.data.model.ThanhVienVm;
import com.example.quanlynhatro.data.repository.HopDongRepository;
import com.example.quanlynhatro.data.repository.KhachThueRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Màn hình Chi tiết hợp đồng.
 * Cho phép xem thông tin đầy đủ và thực hiện Thanh lý (kết thúc) hợp đồng.
 */
public class ChiTietHopDongActivity extends AppCompatActivity {

    private TextView tvSoHopDong, tvTenKhach, tvPhong, tvGiaThue, tvTienCoc;
    private TextView tvNgayBatDau, tvNgayKetThuc, tvConLai, tvSdt, tvCccd, btnAddMember;
    private LinearProgressIndicator progressThoiHan;
    private MaterialButton btnThanhLy, btnGiaHan;
    private View btnBack, btnEdit;
    private RecyclerView rvMembers;
    private ThanhVienAdapter memberAdapter;

    private HopDongRepository repository;
    private KhachThueRepository khachThueRepository;
    private int hopDongId;
    private HopDongVm currentHopDong;
    private List<ThanhVienVm> listMembers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_hop_dong);

        repository = new HopDongRepository(this);
        khachThueRepository = new KhachThueRepository(this);
        hopDongId = getIntent().getIntExtra("HOP_DONG_ID", -1);

        if (hopDongId == -1) {
            Toast.makeText(this, "Không tìm thấy hợp đồng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupMemberRecyclerView();
        loadData();
    }

    private void initViews() {
        tvSoHopDong = findViewById(R.id.tvSoHopDong);
        tvTenKhach = findViewById(R.id.tvTenKhach);
        tvPhong = findViewById(R.id.tvPhong);
        tvGiaThue = findViewById(R.id.tvGiaThue);
        tvTienCoc = findViewById(R.id.tvTienCoc);
        tvNgayBatDau = findViewById(R.id.tvNgayBatDau);
        tvNgayKetThuc = findViewById(R.id.tvNgayKetThuc);
        tvConLai = findViewById(R.id.tvConLai);
        tvSdt = findViewById(R.id.tvSdt);
        tvCccd = findViewById(R.id.tvCccd);
        progressThoiHan = findViewById(R.id.progressThoiHan);
        btnThanhLy = findViewById(R.id.btnThanhLy);
        btnGiaHan = findViewById(R.id.btnGiaHan);
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        rvMembers = findViewById(R.id.rvMembers);
        btnAddMember = findViewById(R.id.btnAddMember);

        btnBack.setOnClickListener(v -> finish());
        
        btnThanhLy.setOnClickListener(v -> showConfirmThanhLy());

        btnGiaHan.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng gia hạn đang được phát triển!", Toast.LENGTH_SHORT).show();
        });

        btnEdit.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng sửa hợp đồng đang được phát triển!", Toast.LENGTH_SHORT).show();
        });

        btnAddMember.setOnClickListener(v -> showAddMemberDialog());
    }

    private void setupMemberRecyclerView() {
        rvMembers.setLayoutManager(new LinearLayoutManager(this));
        memberAdapter = new ThanhVienAdapter();
        memberAdapter.setOnThanhVienClickListener(item -> {
            showConfirmDeleteMember(item);
        });
        rvMembers.setAdapter(memberAdapter);
    }

    private void loadData() {
        currentHopDong = repository.getHopDongVmById(hopDongId);
        if (currentHopDong == null) return;

        tvSoHopDong.setText(currentHopDong.getMaHopDong());
        tvTenKhach.setText(currentHopDong.getTenKhachThue());
        tvPhong.setText("Phòng: " + currentHopDong.getTenPhong());
        
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvGiaThue.setText(currencyFormat.format(currentHopDong.getGiaThueChot()) + "đ/th");
        tvTienCoc.setText(currencyFormat.format(currentHopDong.getTienCoc()) + "đ");

        tvNgayBatDau.setText(currentHopDong.getNgayBatDau());
        tvNgayKetThuc.setText(currentHopDong.getNgayKetThuc());
        tvSdt.setText("📞 " + currentHopDong.getSdtKhachThue());
        tvCccd.setText("🪪 " + currentHopDong.getCccdKhachThue());

        // Tính toán thời gian còn lại và progress
        updateTimelineUI();

        // Load danh sách thành viên
        listMembers = repository.getThanhViensByHopDong(hopDongId);
        memberAdapter.setList(listMembers);

        // Nếu đã thanh lý thì ẩn nút thanh lý
        if ("DA_THANH_LY".equals(currentHopDong.getTrangThai())) {
            btnThanhLy.setVisibility(View.GONE);
            btnAddMember.setVisibility(View.GONE);
            tvConLai.setText("HỢP ĐỒNG ĐÃ THANH LÝ");
            tvConLai.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    private void updateTimelineUI() {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            java.util.Date startDate = sdf.parse(currentHopDong.getNgayBatDau());
            java.util.Date endDate = sdf.parse(currentHopDong.getNgayKetThuc());
            java.util.Date today = new java.util.Date();

            long totalDays = (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
            long passedDays = (today.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
            long remainingDays = (endDate.getTime() - today.getTime()) / (24 * 60 * 60 * 1000);

            if (totalDays > 0) {
                int progress = (int) ((passedDays * 100) / totalDays);
                progressThoiHan.setProgress(Math.min(100, Math.max(0, progress)));
            }

            if (remainingDays < 0) {
                tvConLai.setText("Hợp đồng đã quá hạn!");
                tvConLai.setTextColor(0xFFFF5545);
            } else {
                tvConLai.setText("Còn lại: " + remainingDays + " ngày");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showConfirmThanhLy() {
        com.example.quanlynhatro.utils.UIUtils.showConfirmDialog(
            this,
            "Thanh lý hợp đồng",
            "Bạn có chắc chắn muốn thanh lý hợp đồng này? \nPhòng sẽ được chuyển về trạng thái TRỐNG.",
            () -> {
                if (repository.thanhLyHopDong(currentHopDong.getId(), currentHopDong.getPhongId())) {
                    Toast.makeText(this, "Thanh lý thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                }
            }
        );
    }

    private void showAddMemberDialog() {
        List<KhachThue> allTenants = khachThueRepository.getAllKhachThue();
        // Lọc bớt những người đã là thành viên hoặc là người đại diện
        List<KhachThue> available = new ArrayList<>();
        for (KhachThue kt : allTenants) {
            boolean isExists = kt.getId() == currentHopDong.getKhachThueDaiDienId();
            for (ThanhVienVm tv : listMembers) {
                if (tv.getKhachThueId() == kt.getId()) {
                    isExists = true;
                    break;
                }
            }
            if (!isExists) available.add(kt);
        }

        if (available.isEmpty()) {
            Toast.makeText(this, "Không còn khách thuê nào trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] names = new String[available.size()];
        for (int i = 0; i < available.size(); i++) {
            names[i] = available.get(i).getHoTen() + " (" + available.get(i).getSoDienThoai() + ")";
        }

        final int[] selectedIndex = {-1};
        new AlertDialog.Builder(this)
                .setTitle("Chọn thành viên thêm vào phòng")
                .setSingleChoiceItems(names, -1, (dialog, which) -> selectedIndex[0] = which)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    if (selectedIndex[0] != -1) {
                        KhachThue selected = available.get(selectedIndex[0]);
                        repository.addThanhVien(hopDongId, selected.getId(), "Thành viên");
                        loadData(); // Reload list
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showConfirmDeleteMember(ThanhVienVm member) {
        com.example.quanlynhatro.utils.UIUtils.showConfirmDialog(
            this,
            "Xóa thành viên",
            "Bạn có chắc chắn muốn xóa " + member.getHoTen() + " khỏi phòng này?",
            () -> {
                if (repository.removeThanhVien(member.getId())) {
                    loadData();
                }
            }
        );
    }
}
