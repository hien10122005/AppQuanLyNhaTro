package com.example.quanlynhatro.ui.hoa_don;

// ============================================================
// ADAPTER - Cầu nối giữa Dữ liệu và RecyclerView
// ============================================================
// Trong C# WinForms bạn set dataGridView.DataSource = list;
// Trong Android KHÔNG có cách đó. Thay vào đó ta dùng Adapter.
//
// Cách hoạt động:
//   1. Bạn có List<HoaDonVm> (danh sách dữ liệu)
//   2. Adapter nhận list đó
//   3. Adapter biết cách "vẽ" từng phần tử ra 1 item_hoa_don.xml
//   4. RecyclerView hỏi Adapter: "Cho tôi View của dòng số 5!"
//   5. Adapter trả về View đã điền dữ liệu của phần tử số 5
// ============================================================
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.database.DatabaseHelper;
import com.example.quanlynhatro.data.model.HoaDonVm;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * HoaDonAdapter - Adapter cho RecyclerView danh sách hóa đơn.
 *
 * Kế thừa RecyclerView.Adapter<HoaDonAdapter.ViewHolder>:
 *   - HoaDonAdapter = tên lớp này
 *   - ViewHolder    = lớp nội bộ (inner class) giữ tham chiếu đến các View
 *                     trong 1 item, tránh gọi findViewById() nhiều lần
 */
public class HoaDonAdapter extends RecyclerView.Adapter<HoaDonAdapter.ViewHolder> {

    // Danh sách dữ liệu (tương đương DataSource trong WinForms)
    private List<HoaDonVm> danhSach;

    // Context cần để truy cập tài nguyên màu sắc, drawable...
    private final Context context;

    // Interface để xử lý sự kiện click vào 1 item trong danh sách
    // Tương đương DataGridView.CellClick event trong C#
    private OnItemClickListener listener;

    /**
     * Định nghĩa interface callback khi người dùng nhấn vào 1 hóa đơn.
     * Activity sẽ implement interface này để xử lý (mở màn hình chi tiết).
     */
    public interface OnItemClickListener {
        void onItemClick(HoaDonVm hoaDon); // Truyền object HoaDon đã click
    }

    // Constructor: nhận vào dữ liệu và context
    public HoaDonAdapter(Context context, List<HoaDonVm> danhSach) {
        this.context = context;
        this.danhSach = danhSach;
    }

    // Setter để gán listener từ Activity
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // ============================================================
    // 3 HÀM BẮT BUỘC CỦA RECYCLERVIEW.ADAPTER
    // ============================================================

    /**
     * [1] onCreateViewHolder - Tạo mới 1 "khung" (ViewHolder) từ file XML.
     * Hàm này chỉ chạy khi RecyclerView cần thêm item mới lên màn hình.
     * Tương tự: tạo mới 1 dòng rỗng trong DataGridView.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater.inflate = "Đọc file XML và tạo ra View object tương ứng"
        // Giống như InitializeComponent() trong WinForms
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_hoa_don, parent, false);
        return new ViewHolder(view);
    }

    /**
     * [2] onBindViewHolder - Điền dữ liệu vào "khung" (ViewHolder) cho vị trí `position`.
     * Hàm này chạy mỗi khi cần hiển thị dữ liệu mới vào 1 item.
     * Tương tự: DataGridView hiển thị giá trị của từng cell.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Lấy object HoaDonVm tại vị trí position trong danh sách
        HoaDonVm item = danhSach.get(position);

        // --- Điền dữ liệu vào từng View ---
        holder.tvTenPhong.setText(item.getTenPhong() != null ? item.getTenPhong() : "Phòng ?");
        holder.tvTenKhach.setText(item.getTenKhach() != null ? item.getTenKhach() : "Khách ?");

        // Hiển thị "Mã HĐ · Hạn: ngày"
        String han = item.getHanThanhToan() != null ? item.getHanThanhToan() : "Chưa có";
        holder.tvMaVaHan.setText(item.getMaHoaDon() + " · Hạn: " + han);

        // Định dạng số tiền với dấu phẩy (1250000 → "1,250,000 đ")
        NumberFormat fmt = NumberFormat.getNumberInstance(Locale.getDefault());
        holder.tvTongTien.setText(fmt.format(item.getTongTien()) + " đ");

        // --- Thiết lập chip trạng thái theo màu tương ứng ---
        apDungMauTrangThai(holder.tvTrangThai, item.getTrangThai());

        // --- Xử lý sự kiện click vào cả item ---
        holder.itemView.setOnClickListener(v -> {
            // Chỉ gọi nếu listener đã được gán (không null)
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    /**
     * [3] getItemCount - Trả về tổng số phần tử trong danh sách.
     * RecyclerView dùng hàm này để biết sẽ hiển thị bao nhiêu item.
     */
    @Override
    public int getItemCount() {
        return danhSach != null ? danhSach.size() : 0;
    }

    // ============================================================
    // HÀM CẬP NHẬT DỮ LIỆU (gọi từ Activity khi filter thay đổi)
    // ============================================================

    /**
     * Cập nhật lại toàn bộ danh sách và refresh giao diện.
     * Tương tự: dataGridView.DataSource = newList; dataGridView.Refresh();
     */
    public void capNhatDanhSach(List<HoaDonVm> danhSachMoi) {
        this.danhSach = danhSachMoi;
        // notifyDataSetChanged() báo cho RecyclerView biết dữ liệu đã thay đổi
        // → RecyclerView sẽ tự vẽ lại toàn bộ danh sách
        notifyDataSetChanged();
    }

    // ============================================================
    // HÀM PHỤ: Áp dụng màu chip theo trạng thái
    // ============================================================

    /**
     * Thay đổi background và màu chữ của chip trạng thái.
     * Gọi hàm này thay vì lặp lại code if/else ở nhiều nơi.
     */
    private void apDungMauTrangThai(TextView tvChip, String trangThai) {
        if (trangThai == null) {
            tvChip.setText("Không rõ");
            return;
        }
        switch (trangThai) {
            case DatabaseHelper.TRANG_THAI_HOA_DON_DA_THANH_TOAN:
                // Đã thu → chip xanh lá
                tvChip.setText("Đã Thu");
                tvChip.setBackgroundResource(R.drawable.bg_chip_green);
                tvChip.setTextColor(ContextCompat.getColor(context, R.color.success_green));
                break;

            case DatabaseHelper.TRANG_THAI_HOA_DON_CHUA_THANH_TOAN:
                // Chưa thu → chip đỏ
                tvChip.setText("Chưa Thu");
                tvChip.setBackgroundResource(R.drawable.bg_chip_red);
                tvChip.setTextColor(ContextCompat.getColor(context, R.color.warning_red));
                break;

            case DatabaseHelper.TRANG_THAI_HOA_DON_THANH_TOAN_MOT_PHAN:
                // Thanh toán 1 phần → chip cam
                tvChip.setText("Trả 1 phần");
                tvChip.setBackgroundResource(R.drawable.bg_chip_orange);
                tvChip.setTextColor(Color.parseColor("#FF9500"));
                break;

            default:
                // Trạng thái khác (HUY...)
                tvChip.setText(trangThai);
                tvChip.setBackgroundResource(R.drawable.bg_glass_card);
                tvChip.setTextColor(Color.parseColor("#808BAA"));
                break;
        }
    }

    // ============================================================
    // VIEWHOLDER - Lưu tham chiếu các View trong 1 item
    // ============================================================

    /**
     * ViewHolder là lớp con (inner class) của Adapter.
     * Nó giữ sẵn tham chiếu đến các View trong item_hoa_don.xml
     * để tránh gọi findViewById() lặp đi lặp lại (tốn hiệu năng).
     *
     * Tương tự trong WinForms: bạn lưu sẵn handle của TextBox, Label
     * thay vì tìm lại control mỗi lần cần dùng.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Các View trong layout item_hoa_don.xml
        TextView tvTenPhong;
        TextView tvTenKhach;
        TextView tvMaVaHan;
        TextView tvTongTien;
        TextView tvTrangThai;

        // Constructor: nhận vào itemView (toàn bộ layout 1 item)
        // và tìm từng View con bên trong
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Tìm View theo ID (giống Control.FindControl() trong WinForms)
            tvTenPhong  = itemView.findViewById(R.id.tvTenPhong);
            tvTenKhach  = itemView.findViewById(R.id.tvTenKhach);
            tvMaVaHan   = itemView.findViewById(R.id.tvMaVaHan);
            tvTongTien  = itemView.findViewById(R.id.tvTongTien);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
        }
    }
}
