package com.example.quanlynhatro.ui.setting;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlynhatro.R;
import com.example.quanlynhatro.data.model.Phong;
import com.example.quanlynhatro.data.repository.PhongRepository;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.List;

/**
 * LỚP XỬ LÝ XUẤT DỮ LIỆU RA EXCEL (DƯỚI DẠNG CSV)
 * Chức năng chính: Truy vấn DB -> Chuyển thành văn bản CSV -> Ghi vào file hệ thống.
 */
public class XuatExcelActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout optRooms, optTenants, optFinance, optInvoices;
    private LinearLayout btnFromDate, btnToDate;
    private TextView tvFromDate, tvToDate;
    private Button btnExport;

    private String selectedDataType = "ROOMS"; // Lưu loại dữ liệu người dùng chọn
    private Calendar calendarFrom = Calendar.getInstance();
    private Calendar calendarTo = Calendar.getInstance();

    /**
     * HÀM GỌI HỆ THỐNG: CreateDocument
     * - Đây là hàm của Android (ActivityResultContracts) giúp mở trình chọn nơi lưu file.
     * - "text/csv": Chỉ định định dạng file là CSV (Excel mở rất tốt).
     */
    private final ActivityResultLauncher<String> createDocumentLauncher = registerForActivityResult(
            new ActivityResultContracts.CreateDocument("text/csv"),
            uri -> {
                if (uri != null) {
                    // Sau khi người dùng chọn chỗ lưu, hàm saveFile sẽ được gọi để ghi dữ liệu
                    saveFile(uri);
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuat_excel);

        initViews();            // Ánh xạ các View và thiết lập giao diện mặc định
        setupEvents();          // Cài đặt sự kiện chọn loại dữ liệu và xuất file
        updateDateViews();      // Hiển thị ngày tháng mặc định lên giao diện
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        optRooms = findViewById(R.id.optRooms);
        optTenants = findViewById(R.id.optTenants);
        optFinance = findViewById(R.id.optFinance);
        optInvoices = findViewById(R.id.optInvoices);
        btnFromDate = findViewById(R.id.btnFromDate);
        btnToDate = findViewById(R.id.btnToDate);
        tvFromDate = findViewById(R.id.tvFromDate);
        tvToDate = findViewById(R.id.tvToDate);
        btnExport = findViewById(R.id.btnExport);

        // Highlight default selection
        highlightOption(optRooms);
    }

    /**
     * Thiết lập các sự kiện click cho các thành phần giao diện
     */
    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        // Chọn loại dữ liệu cần xuất là PHÒNG
        optRooms.setOnClickListener(v -> {
            selectedDataType = "ROOMS";
            highlightOption(optRooms);
        });

        // Chọn loại dữ liệu cần xuất là KHÁCH THUÊ
        optTenants.setOnClickListener(v -> {
            selectedDataType = "TENANTS";
            highlightOption(optTenants);
        });

        // Chọn loại dữ liệu cần xuất là TÀI CHÍNH
        optFinance.setOnClickListener(v -> {
            selectedDataType = "FINANCE";
            highlightOption(optFinance);
        });

        // Chọn loại dữ liệu cần xuất là HÓA ĐƠN
        optInvoices.setOnClickListener(v -> {
            selectedDataType = "INVOICES";
            highlightOption(optInvoices);
        });

        // Chọn khoảng thời gian (Từ ngày - Đến ngày)
        btnFromDate.setOnClickListener(v -> showDatePicker(true));
        btnToDate.setOnClickListener(v -> showDatePicker(false));

        // Nút lệnh bắt đầu quá trình xuất file
        btnExport.setOnClickListener(v -> {
            // Tạo tên file gợi ý dựa trên loại dữ liệu và thời gian hiện tại
            String fileName = "Xuat_" + selectedDataType + "_" + System.currentTimeMillis() + ".csv";
            // Kích hoạt trình chọn nơi lưu file của hệ thống Android
            createDocumentLauncher.launch(fileName);
        });
    }

    private void highlightOption(View view) {
        optRooms.setAlpha(0.5f);
        optTenants.setAlpha(0.5f);
        optFinance.setAlpha(0.5f);
        optInvoices.setAlpha(0.5f);
        view.setAlpha(1.0f);
    }

    /**
     * Hiển thị hộp thoại chọn ngày (DatePickerDialog)
     */
    private void showDatePicker(boolean isFrom) {
        Calendar cal = isFrom ? calendarFrom : calendarTo;
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            cal.set(year, month, dayOfMonth);
            updateDateViews(); // Cập nhật lại TextView hiển thị ngày sau khi chọn
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateViews() {
        tvFromDate.setText(String.format("%02d/%02d/%d", calendarFrom.get(Calendar.DAY_OF_MONTH), calendarFrom.get(Calendar.MONTH) + 1, calendarFrom.get(Calendar.YEAR)));
        tvToDate.setText(String.format("%02d/%02d/%d", calendarTo.get(Calendar.DAY_OF_MONTH), calendarTo.get(Calendar.MONTH) + 1, calendarTo.get(Calendar.YEAR)));
    }

    /**
     * Thực hiện ghi nội dung văn bản CSV vào file tại vị trí người dùng đã chọn
     */
    private void saveFile(Uri uri) {
        try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
            // Tạo nội dung CSV dựa trên loại dữ liệu đã chọn
            String csvContent = generateCsvContent();
            if (outputStream != null) {
                // Ghi dữ liệu vào stream với bảng mã UTF-8
                outputStream.write(csvContent.getBytes(StandardCharsets.UTF_8));
                Toast.makeText(this, "Xuất file thành công!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * HÀM TẠO NỘI DUNG FILE CSV
     * - sb.append('\ufeff'): Thêm ký tự BOM để Excel hiểu đây là file UTF-8 (không bị lỗi tiếng Việt).
     * - PhongRepository: Gọi file xử lý DB để lấy dữ liệu thật.
     */
    private String generateCsvContent() {
        StringBuilder sb = new StringBuilder();
        // Thêm Byte Order Mark (BOM) để Excel nhận diện đúng mã UTF-8
        sb.append('\ufeff');

        switch (selectedDataType) {
            case "ROOMS":
                // Tiêu đề cột
                sb.append("ID,Số Phòng,Tên Phòng,Loại Phòng,Giá Thuê,Diện Tích,Số Người Tối Đa,Trạng Thái\n");
                
                // GỌI FILE KHÁC: Sử dụng PhongRepository để lấy dữ liệu từ SQLite
                PhongRepository repo = new PhongRepository(this);
                List<Phong> rooms = repo.getAllPhong(); // Lấy danh sách từ file PhongRepository.java
                
                for (Phong r : rooms) {
                    sb.append(r.getId()).append(",")
                            .append(r.getSoPhong()).append(",")
                            .append(r.getTenPhong()).append(",")
                            .append(r.getLoaiPhong()).append(",")
                            .append(r.getGiaPhong()).append(",")
                            .append(r.getDienTich()).append(",")
                            .append(r.getSoNguoiToiDa()).append(",")
                            .append(r.getTrangThai()).append("\n");
                }
                break;

            case "TENANTS":
                sb.append("ID,Họ Tên,SĐT,CCCD,Email,Quê Quán\n");
                // To be implemented fully with KhachThueRepository
                sb.append("1,Ví dụ,0900000000,123456789,test@gmail.com,Hà Nội\n");
                break;
            default:
                sb.append("Dữ liệu đang được cập nhật...\n");
                break;
        }
        return sb.toString();
    }
}
