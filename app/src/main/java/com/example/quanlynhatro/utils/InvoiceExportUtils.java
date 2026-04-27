package com.example.quanlynhatro.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;

import com.example.quanlynhatro.data.model.HoaDonVm;
import com.example.quanlynhatro.data.model.HoaDonChiTiet;
import com.example.quanlynhatro.utils.CurrencyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Lớp tiện ích hỗ trợ xuất hóa đơn ra file PDF.
 * Tương tự như việc sử dụng thư viện báo cáo (ReportViewer) trong C# WinForms.
 */
public class InvoiceExportUtils {

    /**
     * Xuất hóa đơn ra file PDF sử dụng API mặc định của Android (PdfDocument).
     * @param context   Context của ứng dụng
     * @param hoaDon    Dữ liệu hóa đơn tổng quát
     * @param chiTiet   Danh sách các mục phí chi tiết (Điện, nước,...)
     */
    public static void exportToPdf(Context context, HoaDonVm hoaDon, List<HoaDonChiTiet> chiTiet) {
        // 1. Khởi tạo đối tượng PdfDocument (Giống như tạo một file PDF trống)
        PdfDocument document = new PdfDocument();

        // 2. Định nghĩa kích thước trang (A4 khoảng 595x842 points)
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        
        // --- Vẽ Tiêu Đề ---
        paint.setColor(Color.BLACK);
        paint.setTextSize(24);
        paint.setFakeBoldText(true);
        canvas.drawText("HÓA ĐƠN TIỀN NHÀ", 180, 50, paint);

        // --- Vẽ Thông Tin Chung ---
        paint.setTextSize(14);
        paint.setFakeBoldText(false);
        int y = 100;
        canvas.drawText("Mã hóa đơn: " + hoaDon.getMaHoaDon(), 50, y, paint);
        y += 25;
        canvas.drawText("Phòng: " + hoaDon.getTenPhong(), 50, y, paint);
        canvas.drawText("Tháng: " + hoaDon.getThang() + "/" + hoaDon.getNam(), 350, y, paint);
        y += 25;
        canvas.drawText("Khách thuê: " + hoaDon.getTenKhach(), 50, y, paint);
        y += 40;

        // --- Vẽ Bảng Chi Tiết ---
        paint.setFakeBoldText(true);
        canvas.drawText("Nội dung", 50, y, paint);
        canvas.drawText("Số lượng", 300, y, paint);
        canvas.drawText("Thành tiền", 450, y, paint);
        
        paint.setFakeBoldText(false);
        y += 10;
        canvas.drawLine(50, y, 550, y, paint); // Vẽ đường kẻ ngang
        y += 25;

        for (HoaDonChiTiet item : chiTiet) {
            canvas.drawText(item.getTenMucPhi(), 50, y, paint);
            canvas.drawText(String.valueOf(item.getSoLuong()), 300, y, paint);
            canvas.drawText(CurrencyUtils.formatCurrency(item.getThanhTien()), 450, y, paint);
            y += 25;
            
            // Nếu có chỉ số điện nước thì vẽ thêm hàng phụ
            if (item.getChiSoCu() != null && item.getChiSoMoi() != null) {
                paint.setTextSize(12);
                paint.setColor(Color.GRAY);
                canvas.drawText("(Số cũ: " + item.getChiSoCu() + " - Số mới: " + item.getChiSoMoi() + ")", 60, y, paint);
                paint.setTextSize(14);
                paint.setColor(Color.BLACK);
                y += 20;
            }
        }

        y += 20;
        canvas.drawLine(50, y, 550, y, paint);
        y += 40;

        // --- Vẽ Tổng Tiền ---
        paint.setTextSize(18);
        paint.setFakeBoldText(true);
        canvas.drawText("TỔNG CỘNG: " + CurrencyUtils.formatCurrency(hoaDon.getTongTien()), 300, y, paint);

        // 3. Kết thúc trang
        document.finishPage(page);

        // 4. Lưu file vào thư mục Downloads của máy
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "HoaDon_" + hoaDon.getMaHoaDon() + ".pdf";
        File file = new File(downloadsDir, fileName);

        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(context, "Đã lưu PDF tại thư mục Downloads", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Lỗi khi xuất PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // 5. Đóng document
        document.close();
    }
}
