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

public class XuatExcelActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout optRooms, optTenants, optFinance, optInvoices;
    private LinearLayout btnFromDate, btnToDate;
    private TextView tvFromDate, tvToDate;
    private Button btnExport;

    private String selectedDataType = "ROOMS"; // Default
    private Calendar calendarFrom = Calendar.getInstance();
    private Calendar calendarTo = Calendar.getInstance();

    private final ActivityResultLauncher<String> createDocumentLauncher = registerForActivityResult(
            new ActivityResultContracts.CreateDocument("text/csv"),
            uri -> {
                if (uri != null) {
                    saveFile(uri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuat_excel);

        initViews();
        setupEvents();
        updateDateViews();
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

    private void setupEvents() {
        btnBack.setOnClickListener(v -> finish());

        optRooms.setOnClickListener(v -> {
            selectedDataType = "ROOMS";
            highlightOption(optRooms);
        });

        optTenants.setOnClickListener(v -> {
            selectedDataType = "TENANTS";
            highlightOption(optTenants);
        });

        optFinance.setOnClickListener(v -> {
            selectedDataType = "FINANCE";
            highlightOption(optFinance);
        });

        optInvoices.setOnClickListener(v -> {
            selectedDataType = "INVOICES";
            highlightOption(optInvoices);
        });

        btnFromDate.setOnClickListener(v -> showDatePicker(true));
        btnToDate.setOnClickListener(v -> showDatePicker(false));

        btnExport.setOnClickListener(v -> {
            String fileName = "Xuat_" + selectedDataType + "_" + System.currentTimeMillis() + ".csv";
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

    private void showDatePicker(boolean isFrom) {
        Calendar cal = isFrom ? calendarFrom : calendarTo;
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            cal.set(year, month, dayOfMonth);
            updateDateViews();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateViews() {
        tvFromDate.setText(String.format("%02d/%02d/%d", calendarFrom.get(Calendar.DAY_OF_MONTH), calendarFrom.get(Calendar.MONTH) + 1, calendarFrom.get(Calendar.YEAR)));
        tvToDate.setText(String.format("%02d/%02d/%d", calendarTo.get(Calendar.DAY_OF_MONTH), calendarTo.get(Calendar.MONTH) + 1, calendarTo.get(Calendar.YEAR)));
    }

    private void saveFile(Uri uri) {
        try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
            String csvContent = generateCsvContent();
            if (outputStream != null) {
                outputStream.write(csvContent.getBytes(StandardCharsets.UTF_8));
                Toast.makeText(this, "Xuất file thành công!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String generateCsvContent() {
        StringBuilder sb = new StringBuilder();
        // Add BOM for Excel UTF-8 support
        sb.append('\ufeff');

        switch (selectedDataType) {
            case "ROOMS":
                sb.append("ID,Số Phòng,Tên Phòng,Loại Phòng,Giá Thuê,Diện Tích,Số Người Tối Đa,Trạng Thái\n");
                PhongRepository repo = new PhongRepository(this);
                List<Phong> rooms = repo.getAllPhong();
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
