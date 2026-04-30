package com.example.quanlynhatro.ui.bao_tri;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.model.SuCoBaoTriVm;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BaoTriAdapter extends RecyclerView.Adapter<BaoTriAdapter.BaoTriViewHolder> {
    private final List<SuCoBaoTriVm> items = new ArrayList<>();
    private OnItemClickListener listener;
    private final DecimalFormat moneyFormat = new DecimalFormat("#,##0");

    public interface OnItemClickListener {
        void onItemClick(SuCoBaoTriVm item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<SuCoBaoTriVm> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        // notifyDataSetChanged() bao cho RecyclerView biet rang nguon du lieu da doi.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaoTriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bao_tri, parent, false);
        return new BaoTriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaoTriViewHolder holder, int position) {
        // onBindViewHolder co vai tro giong nhu "do du lieu vao tung dong" cua grid/list.
        SuCoBaoTriVm item = items.get(position);

        // Moi lan RecyclerView can hien 1 dong o vi tri position,
        // no se lay object tu items[position] roi do vao cac TextView ben duoi.
        holder.tvRoomName.setText(item.getTenPhong() != null ? item.getTenPhong() : "Phong khong xac dinh");
        holder.tvIssueTitle.setText(item.getTieuDe());
        holder.tvIssueDesc.setText(item.getNoiDung());
        holder.tvDate.setText(item.getNgayBao() != null ? item.getNgayBao() : "---");
        holder.tvCost.setText(item.getChiPhi() > 0 ? moneyFormat.format(item.getChiPhi()) + "đ" : "0đ");
        bindStatus(holder, item.getTrangThai());
        bindPriority(holder, item.getMucDoUuTien());

        holder.itemView.setOnClickListener(v -> {
            // Adapter khong tu mo Activity truc tiep.
            // No chi "bao nguoc" ra ngoai thong qua listener de Activity tu quyet dinh can lam gi.
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void bindStatus(BaoTriViewHolder holder, String status) {
        String normalized = status != null ? status : "";
        int textColor;
        int bgColor;
        String label;

        // Trang thai dang duoc luu duoi dang ma (constant), o day doi sang text va mau de hien thi.
        switch (normalized) {
            case ThemSuaBaoTriActivity.STATUS_DANG_SUA:
                label = "DANG SUA";
                textColor = 0xFFFFC107;
                bgColor = 0x20FFC107;
                break;
            case ThemSuaBaoTriActivity.STATUS_HOAN_THANH:
                label = "HOAN THANH";
                textColor = 0xFF34C759;
                bgColor = 0x2034C759;
                break;
            default:
                label = "CHO XU LY";
                textColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.primary_blue);
                bgColor = 0x20007AFF;
                break;
        }

        holder.tvStatus.setText(label);
        holder.tvStatus.setTextColor(textColor);
        holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(bgColor));
    }

    private void bindPriority(BaoTriViewHolder holder, String priority) {
        int color;
        // Cham mau nho o goc trai giup nhin nhanh muc do uu tien cua su co.
        switch (priority != null ? priority : "") {
            case ThemSuaBaoTriActivity.PRIORITY_KHAN_CAP:
                color = 0xFFFF3B30;
                break;
            case ThemSuaBaoTriActivity.PRIORITY_CAO:
                color = 0xFFFF9500;
                break;
            case ThemSuaBaoTriActivity.PRIORITY_THAP:
                color = 0xFF34C759;
                break;
            default:
                color = 0xFFFFC107;
                break;
        }
        holder.viewPriority.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    static class BaoTriViewHolder extends RecyclerView.ViewHolder {
        final View viewPriority;
        final TextView tvRoomName;
        final TextView tvIssueTitle;
        final TextView tvIssueDesc;
        final TextView tvDate;
        final TextView tvCost;
        final TextView tvStatus;

        BaoTriViewHolder(@NonNull View itemView) {
            super(itemView);

            // ViewHolder la noi giu reference den cac control cua 1 dong,
            // tranh findViewById lap lai qua nhieu lan khi cuon danh sach.
            viewPriority = itemView.findViewById(R.id.viewPriority);
            tvRoomName = itemView.findViewById(R.id.tvItemRoomName);
            tvIssueTitle = itemView.findViewById(R.id.tvItemIssueTitle);
            tvIssueDesc = itemView.findViewById(R.id.tvItemIssueDesc);
            tvDate = itemView.findViewById(R.id.tvItemDate);
            tvCost = itemView.findViewById(R.id.tvItemCost);
            tvStatus = itemView.findViewById(R.id.tvItemStatus);
        }
    }
}
