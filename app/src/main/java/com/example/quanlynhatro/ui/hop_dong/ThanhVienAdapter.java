package com.example.quanlynhatro.ui.hop_dong;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.model.ThanhVienVm;

import java.util.ArrayList;
import java.util.List;

public class ThanhVienAdapter extends RecyclerView.Adapter<ThanhVienAdapter.ThanhVienViewHolder> {

    private List<ThanhVienVm> list = new ArrayList<>();
    private OnThanhVienClickListener listener;

    public interface OnThanhVienClickListener {
        void onDeleteClick(ThanhVienVm tv);
    }

    public void setOnThanhVienClickListener(OnThanhVienClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<ThanhVienVm> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ThanhVienViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thanh_vien, parent, false);
        return new ThanhVienViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThanhVienViewHolder holder, int position) {
        ThanhVienVm item = list.get(position);
        holder.tvHoTen.setText(item.getHoTen());
        holder.tvVaiTro.setText(item.getVaiTro());

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ThanhVienViewHolder extends RecyclerView.ViewHolder {
        TextView tvHoTen, tvVaiTro;
        ImageButton btnDelete;

        public ThanhVienViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoTen = itemView.findViewById(R.id.tvHoTen);
            tvVaiTro = itemView.findViewById(R.id.tvVaiTro);
            btnDelete = itemView.findViewById(R.id.btnDeleteMember);
        }
    }
}
