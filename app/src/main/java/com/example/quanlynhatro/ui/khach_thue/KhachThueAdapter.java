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

    public void setDanhSach(List<KhachThueVm> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public KhachThueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khach_thue, parent, false);
        return new KhachThueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KhachThueViewHolder holder, int position) {
        KhachThueVm item = list.get(position);
        holder.tvHoTen.setText(item.getHoTen());
        holder.tvSoDienThoai.setText(item.getSoDienThoai());
        holder.tvTenPhong.setText(item.getTenPhong());
        
        if (item.isDangThue()) {
            holder.tvTrangThai.setText("Đang thuê");
            holder.tvTrangThai.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.primary_blue));
            holder.tvTrangThai.setBackgroundResource(R.drawable.bg_glass_card);
            holder.tvTrangThai.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0x15007AFF));
        } else {
            holder.tvTrangThai.setText("Chưa thuê");
            holder.tvTrangThai.setTextColor(0xFF808080);
            holder.tvTrangThai.setBackgroundResource(R.drawable.bg_glass_card);
            holder.tvTrangThai.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0x15808080));
        }

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
