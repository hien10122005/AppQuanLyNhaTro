package com.example.quanlynhatro.ui.hoa_don;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.model.ThanhToan;
import com.example.quanlynhatro.utils.CurrencyUtils;
import com.example.quanlynhatro.utils.UIUtils;

import java.util.List;

/**
 * Adapter để hiển thị danh sách các đợt thanh toán.
 * Tương tự như việc đổ dữ liệu vào DataGridView trong WinForms.
 */
public class ThanhToanAdapter extends RecyclerView.Adapter<ThanhToanAdapter.ViewHolder> {

    private final List<ThanhToan> list;

    public ThanhToanAdapter(List<ThanhToan> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thanh_toan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThanhToan item = list.get(position);
        
        // Hiển thị ngày và số tiền
        holder.tvNgay.setText("Ngày: " + item.getNgayThanhToan());
        holder.tvSoTien.setText("+" + CurrencyUtils.formatCurrency(item.getSoTien()));
        
        // Hiển thị phương thức và ghi chú
        String details = "PT: " + item.getPhuongThuc();
        if (item.getMaGiaoDich() != null && !item.getMaGiaoDich().isEmpty()) {
            details += " | Mã: " + item.getMaGiaoDich();
        }
        holder.tvChiTiet.setText(details);
        
        if (item.getGhiChu() != null && !item.getGhiChu().isEmpty()) {
            holder.tvGhiChu.setVisibility(View.VISIBLE);
            holder.tvGhiChu.setText("Ghi chú: " + item.getGhiChu());
        } else {
            holder.tvGhiChu.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNgay, tvSoTien, tvChiTiet, tvGhiChu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNgay = itemView.findViewById(R.id.tvNgayThanhToan);
            tvSoTien = itemView.findViewById(R.id.tvSoTienThanhToan);
            tvChiTiet = itemView.findViewById(R.id.tvChiTietThanhToan);
            tvGhiChu = itemView.findViewById(R.id.tvGhiChuThanhToan);
        }
    }
}
