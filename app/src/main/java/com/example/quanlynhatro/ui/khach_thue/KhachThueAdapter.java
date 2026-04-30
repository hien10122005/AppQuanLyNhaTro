package com.example.quanlynhatro.ui.khach_thue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.model.KhachThueVm;

import java.util.ArrayList;
import java.util.List;

public class KhachThueAdapter extends RecyclerView.Adapter<KhachThueAdapter.KhachThueViewHolder> {

    private List<KhachThueVm> list = new ArrayList<>();
    private OnKhachThueClickListener listener;

    public interface OnKhachThueClickListener {
        void onItemClick(KhachThueVm khachThue);
    }

    public void setOnKhachThueClickListener(OnKhachThueClickListener listener) {
        this.listener = listener;
    }

    /**
     * Cập nhật danh sách khách thuê mới và làm mới giao diện
     */
    public void setDanhSach(List<KhachThueVm> newList) {
        this.list = newList;
        notifyDataSetChanged(); // Thông báo cho RecyclerView rằng dữ liệu đã thay đổi để vẽ lại
    }

    @NonNull
    @Override
    public KhachThueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp layout item_khach_thue cho mỗi dòng trong danh sách
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khach_thue, parent, false);
        return new KhachThueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KhachThueViewHolder holder, int position) {
        // Lấy đối tượng khách thuê tại vị trí position
        KhachThueVm item = list.get(position);
        
        // Hiển thị các thông tin cơ bản
        holder.tvHoTen.setText(item.getHoTen());
        holder.tvSoDienThoai.setText(item.getSoDienThoai());
        holder.tvTenPhong.setText(item.getTenPhong());
        
        // Xử lý hiển thị trạng thái (Đang thuê / Chưa thuê) với màu sắc và background tương ứng
        if (item.isDangThue()) {
            holder.tvTrangThai.setText("Đang thuê");
            holder.tvTrangThai.setTextColor(androidx.core.content.ContextCompat.getColor(holder.itemView.getContext(), R.color.primary_blue));
            holder.tvTrangThai.setBackgroundResource(R.drawable.bg_glass_card);
            holder.tvTrangThai.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0x15007AFF));
        } else {
            holder.tvTrangThai.setText("Chưa thuê");
            holder.tvTrangThai.setTextColor(0xFF808080);
            holder.tvTrangThai.setBackgroundResource(R.drawable.bg_glass_card);
            holder.tvTrangThai.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0x15808080));
        }

        // Sự kiện click vào item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class KhachThueViewHolder extends RecyclerView.ViewHolder {
        TextView tvHoTen, tvSoDienThoai, tvTenPhong, tvTrangThai;

        public KhachThueViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoTen = itemView.findViewById(R.id.tvItemHoTen);
            tvSoDienThoai = itemView.findViewById(R.id.tvItemSoDienThoai);
            tvTenPhong = itemView.findViewById(R.id.tvItemPhong);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
        }
    }
}
