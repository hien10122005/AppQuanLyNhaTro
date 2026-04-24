# 🏠 DỰ ÁN: QUẢN LÝ NHÀ TRỌ (QUANLYNHATRO)
**Branding:** H-TECH SOLUTIONS
**Platform:** Android Native (Jetpack Compose)
**Database:** SQLite (Local) / Firebase (Cloud)

---

## 1. GIỚI THIỆU TỔNG QUAN
Dự án được xây dựng nhằm tối ưu hóa việc quản lý vận hành nhà trọ, tòa nhà, và căn hộ dịch vụ. Hệ thống tập trung vào trải nghiệm người dùng hiện đại với giao diện **Dark Mode**, **Glassmorphism**, và kiến trúc **Bento Grid** giúp theo dõi dữ liệu nhanh chóng và chính xác.

---

## 2. HỆ THỐNG DATA MODELS (CƠ SỞ DỮ LIỆU)

Để hệ thống có khả năng mở rộng, các Model được phân cấp chặt chẽ như sau:

### A. Quản lý Không gian
* **Location (Khu trọ):** `id, name, address, description`
* **Block (Dãy/Tòa):** `id, location_id, name`
* **Floor (Tầng):** `id, block_id, floor_number`
* **Room (Phòng):** `id, floor_id, room_number, type, price, area, status (Available, Occupied, Fixing), max_people`

### B. Quản lý Khách thuê & Pháp lý
* **Tenant (Khách thuê):** `id, name, phone, email, id_card_number (CCCD), dob, address_origin`
* **Contract (Hợp đồng):** `id, room_id, representative_tenant_id, start_date, end_date, deposit_amount, status`
* **Member (Thành viên ở cùng):** `id, contract_id, name, phone, id_card_number`

### C. Tài chính & Vận hành
* **Service (Dịch vụ):** `id, name, unit_price, unit_type (Kwh, M3, Thang)`
* **Invoice (Hóa đơn):** `id, contract_id, month, year, elec_old, elec_new, water_old, water_new, total_amount, payment_status, created_at`
* **Maintenance (Bảo trì):** `id, room_id, issue_description, cost, status, report_date`

---

## 3. CÁC CHỨC NĂNG CỐT LÕI (CORE FEATURES)

### 📊 Dashboard & Analytics (MVP)
* Thống kê nhanh: Tổng số phòng, phòng trống, phòng đã thuê.
* Biểu đồ doanh thu hàng tháng (Line Chart).
* Cảnh báo công nợ và hợp đồng sắp hết hạn.

### 🏢 Quản lý Sơ đồ phòng
* Xem theo cấu trúc Khu -> Dãy -> Tầng -> Phòng.
* Thay đổi trạng thái phòng trực quan bằng màu sắc.
* Bộ lọc tìm kiếm nhanh theo số phòng hoặc tên khách.

### 📝 Hợp đồng & Khách thuê
* Lưu trữ hồ sơ khách thuê số hóa (ảnh CCCD).
* Tự động nhắc lịch gia hạn hợp đồng.

### ⚡ Quản lý Chỉ số & Hóa đơn
* Nhập số điện, nước định kỳ hàng tháng.
* Tự động tính tiền dựa trên biểu phí bậc thang (Logic QuanLyDien).
* Xuất hóa đơn dạng hình ảnh/PDF để gửi qua Zalo/SMS.

### 🛠️ Bảo trì & Sự cố
* Ghi nhận báo hỏng từ khách thuê.
* Theo dõi chi phí sửa chữa để tính toán lợi nhuận ròng.

---

## 4. CÔNG NGHỆ TRIỂN KHAI (TECH STACK)
* **UI Framework:** Jetpack Compose (Modern Android UI).
* **Architecture:** MVVM (Model-View-ViewModel).
* **Local Database:** Room Database (SQLite wrapper).
* **Icons:** Lucide Icons / Material Symbols.
* **Design Pattern:** Glassmorphism UI & Bento Grid Layout.

---

## 5. LỘ TRÌNH PHÁT TRIỂN (ROADMAP)

1.  **Phase 1:** Thiết kế Database Room & Tạo giao diện khung (Bento Dashboard).
2.  **Phase 2:** Triển khai Module quản lý Phòng và Khách thuê.
3.  **Phase 3:** Xây dựng Logic tính hóa đơn và xuất file PDF.
4.  **Phase 4:** Tích hợp Chart báo cáo và Notification nhắc nợ.

---
**H-TECH SOLUTIONS - Build for the future.**