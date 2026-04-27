package com.example.quanlynhatro.ui.hop_dong;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.model.HopDongVm;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * HopDongAdapter - Bộ nạp dữ liệu cho danh sách Hợp Đồng.
 * 
 * Dành cho người mới học từ WinForms C#:
 * - Adapter này đóng vai trò giống như việc bạn đổ dữ liệu vào một Custom ListView hoặc DataGridView.
 * - ViewHolder đóng vai trò như các "Control" trên một dòng (Row) của bảng.
 * - Hàm onBindViewHolder là nơi bạn gán dữ liệu từ List vào các Label/TextView trên giao diện.
 */
public class HopDongAdapter extends RecyclerView.Adapter<HopDongAdapter.HopDongViewHolder> {

    // Danh sách dữ liệu (Giống như List<T> hoặc DataTable trong C#)
    private List<HopDongVm> listHopDong = new ArrayList<>();
    private OnHopDongClickListener listener;

    /**
     * Interface để xử lý sự kiện click (giống như Event Handler trong C#).
     * Khi người dùng click vào 1 dòng, Activity bên ngoài sẽ biết và xử lý.
     */
    public interface OnHopDongClickListener {
        void onItemClick(HopDongVm hopDong);
    }

    public void setOnHopDongClickListener(OnHopDongClickListener listener) {
        this.listener = listener;
    }

    /**
     * Hàm cập nhật dữ liệu (Giống như DataSource = newList trong WinForms).
     * Mỗi khi dữ liệu thay đổi, ta gọi notifyDataSetChanged() để RecyclerView vẽ lại.
     */
    public void setListHopDong(List<HopDongVm> newList) {
        this.listHopDong = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HopDongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // "Bơm" (Inflate) file giao diện item_hop_dong.xml vào một đối tượng View
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hop_dong, parent, false);
        return new HopDongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HopDongViewHolder holder, int position) {
        // Lấy dữ liệu của dòng hiện tại (position)
        HopDongVm item = listHopDong.get(position);
        
        // 1. Gán tên khách thuê
        holder.tvTenantName.setText(item.getTenKhachThue());
        
        // 2. Gán thông tin phòng (Gộp chuỗi giống String.Format hoặc interpolation)
        holder.tvRoomInfo.setText("Phòng " + item.getTenPhong());
        
        // 3. Định dạng tiền tệ VNĐ (Giống String.Format("{0:N0}đ", value))
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.tvDeposit.setText(currencyFormat.format(item.getTienCoc()) + "đ");

        // 4. Hiển thị thời hạn hợp đồng (Ngày bắt đầu - Ngày kết thúc)
        holder.tvDateRange.setText(item.getNgayBatDau() + " - " + item.getNgayKetThuc());

        // 5. Trạng thái hợp đồng (Mặc định là Đang thực hiện)
        holder.tvProgressText.setText(item.getTrangThai() != null ? item.getTrangThai() : "Đang hiệu lực");
        
        // 6. Xử lý thanh tiến độ ProgressBar (Demo 100% nếu đã xong, 50% nếu đang chờ)
        if ("Hoàn thành".equals(item.getTrangThai())) {
            holder.pbContract.setProgress(100);
            holder.tvBadgeWarning.setVisibility(View.GONE);
        } else {
            holder.pbContract.setProgress(60); // Giá trị demo
            holder.tvBadgeWarning.setVisibility(View.VISIBLE);
            holder.tvBadgeWarning.setText("Đang hiệu lực");
        }

        // Sự kiện Click vào dòng này
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return listHopDong.size();
    }

    /**
     * ViewHolder: Lớp này dùng để Ánh xạ (Map) các ID trong XML thành các biến Java.
     * Nó giúp tối ưu hiệu năng vì chỉ cần tìm ID (findViewById) một lần duy nhất.
     */
    static class HopDongViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenantName, tvRoomInfo, tvDeposit, tvDateRange, tvProgressText, tvBadgeWarning;
        ProgressBar pbContract;

        public HopDongViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các control (Giống như thiết kế Designer trong WinForms)
            tvTenantName = itemView.findViewById(R.id.tvTenantName);
            tvRoomInfo = itemView.findViewById(R.id.tvRoomInfo);
            tvDeposit = itemView.findViewById(R.id.tvDeposit);
            tvDateRange = itemView.findViewById(R.id.tvDateRange);
            tvProgressText = itemView.findViewById(R.id.tvProgressText);
            tvBadgeWarning = itemView.findViewById(R.id.tvBadgeWarning);
            pbContract = itemView.findViewById(R.id.pbContract);
        }
    }
}
