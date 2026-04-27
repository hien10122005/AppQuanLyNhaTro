package com.example.quanlynhatro.ui.phong;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.Phong;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * PhongAdapter là "Người kết nối" giữa danh sách dữ liệu (List<Phong>) và giao diện (RecyclerView).
 * Nó giống như một vòng lặp: Với mỗi phòng trong danh sách, nó sẽ tạo ra một hàng (item) tương ứng.
 */
public class PhongAdapter extends RecyclerView.Adapter<PhongAdapter.PhongViewHolder> {

    private List<Phong> danhSachPhong = new ArrayList<>();
    private OnPhongClickListener listener;

    // Interface để xử lý sự kiện khi người dùng nhấn vào các nút trên từng hàng
    public interface OnPhongClickListener {
        void onEditClick(Phong phong);
        void onItemClick(Phong phong);
        void onElectricityClick(Phong phong);
        void onWaterClick(Phong phong);
    }

    public void setOnPhongClickListener(OnPhongClickListener listener) {
        this.listener = listener;
    }

    public void setDanhSachPhong(List<Phong> list) {
        this.danhSachPhong = list;
        notifyDataSetChanged(); // Thông báo cho giao diện biết dữ liệu đã thay đổi để vẽ lại
    }

    @NonNull
    @Override
    public PhongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // "Bơm" layout item_phong.xml vào code Java
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phong, parent, false);
        return new PhongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhongViewHolder holder, int position) {
        // Đổ dữ liệu từ đối tượng Phong vào các View (TextView, ImageView...)
        Phong phong = danhSachPhong.get(position);
        holder.bind(phong);
    }

    @Override
    public int getItemCount() {
        return danhSachPhong.size();
    }

    // ViewHolder là lớp giữ các tham chiếu đến các View trong 1 hàng (giúp tăng hiệu năng)
    class PhongViewHolder extends RecyclerView.ViewHolder {
        TextView tvSoPhong, tvDetail, tvGiaPhong, tvTrangThai;
        ImageButton btnEditRoom;
        ImageView btnElectricity, btnWater;

        public PhongViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các ID từ file XML
            tvSoPhong = itemView.findViewById(R.id.tvSoPhong);
            tvDetail = itemView.findViewById(R.id.tvDetail);
            tvGiaPhong = itemView.findViewById(R.id.tvGiaPhong);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            btnEditRoom = itemView.findViewById(R.id.btnEditRoom);
            btnElectricity = itemView.findViewById(R.id.btnElectricity);
            btnWater = itemView.findViewById(R.id.btnWater);

            // Xử lý sự kiện click cho cả hàng
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(danhSachPhong.get(getAdapterPosition()));
            });

            // Xử lý sự kiện click cho nút sửa
            btnEditRoom.setOnClickListener(v -> {
                if (listener != null) listener.onEditClick(danhSachPhong.get(getAdapterPosition()));
            });

            // Click vào biểu tượng điện
            btnElectricity.setOnClickListener(v -> {
                if (listener != null) listener.onElectricityClick(danhSachPhong.get(getAdapterPosition()));
            });

            // Click vào biểu tượng nước
            btnWater.setOnClickListener(v -> {
                if (listener != null) listener.onWaterClick(danhSachPhong.get(getAdapterPosition()));
            });
        }

        public void bind(Phong phong) {
            tvSoPhong.setText("P." + phong.getSoPhong());
            
            // Hiển thị Loại phòng và Diện tích (VD: Standard • 25m²)
            String detail = phong.getLoaiPhong() + " • " + phong.getDienTich() + "m²";
            tvDetail.setText(detail);

            // Định dạng tiền tệ (VD: 3.500.000)
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            tvGiaPhong.setText(formatter.format(phong.getGiaPhong()) + "đ");

            // Xử lý màu sắc và text cho Trạng thái
            setupStatusUI(phong.getTrangThai());
        }

        private void setupStatusUI(String trangThai) {
            switch (trangThai) {
                case DatabaseHelper.TRANG_THAI_PHONG_TRONG:
                    tvTrangThai.setText("Còn trống");
                    tvTrangThai.setTextColor(itemView.getContext().getResources().getColor(R.color.success_green));
                    tvTrangThai.getBackground().setTint(0x1534C759); // Màu xanh lá nhạt
                    break;
                case DatabaseHelper.TRANG_THAI_PHONG_DANG_THUE:
                    tvTrangThai.setText("Đang thuê");
                    tvTrangThai.setTextColor(itemView.getContext().getResources().getColor(R.color.primary_blue));
                    tvTrangThai.getBackground().setTint(0x15007AFF); // Màu xanh dương nhạt
                    break;
                case DatabaseHelper.TRANG_THAI_PHONG_BAO_TRI:
                    tvTrangThai.setText("Bảo trì");
                    tvTrangThai.setTextColor(itemView.getContext().getResources().getColor(R.color.neon_yellow));
                    tvTrangThai.getBackground().setTint(0x15FFCC00); // Màu vàng nhạt
                    break;
                default:
                    tvTrangThai.setText(trangThai);
                    break;
            }
        }
    }
}
