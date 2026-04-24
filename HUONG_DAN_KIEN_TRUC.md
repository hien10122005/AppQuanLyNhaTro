# 🏗️ TÀI LIỆU KIẾN TRÚC DỰ ÁN: QUẢN LÝ NHÀ TRỌ (H-TECH SOLUTIONS)

**Mô hình:** MVVM (Model - View - ViewModel)
**Chiến lược thư mục:** Chia gói theo tính năng (Package by Feature)

Để dự án dễ bảo trì, dễ tìm lỗi và cho phép nhiều người cùng code một lúc mà không bị trùng lặp, toàn bộ mã nguồn sẽ được tổ chức theo từng "Cụm nghiệp vụ" bằng tiếng Việt không dấu.

Hệ thống Android chia tách rõ ràng: **Logic (Java)** nằm riêng biệt với **Giao diện (XML)**. Tài liệu này hướng dẫn cách tổ chức cả hai phần sao cho đồng bộ.

---

## 1. CÂY THƯ MỤC LẬP TRÌNH LOGIC (`java/com/htech/quanlynhatro/`)

Tuyệt đối không để chung tất cả file vào một chỗ. Phải chia theo chức năng.

```text
com.htech.quanlynhatro
│
├── loi_he_thong/           # 1. LÕI DÙNG CHUNG (Core - Các file dùng ở mọi nơi)
│   ├── csdl/               # Khởi tạo kết nối Room/SQLite
│   │   └── CsdlNhaTro.java 
│   └── tien_ich/           # Các hàm công cụ hỗ trợ
│       ├── XuLyNgayThang.java  
│       └── DinhDangTien.java   
│
├── tinh_nang/              # 2. CÁC NGHIỆP VỤ CHÍNH (Gắn liền với thanh Menu dưới cùng)
│   │
│   ├── tong_quan/          # --> Tab 1: Trang chủ
│   │   ├── TongQuanFragment.java    
│   │   └── TongQuanViewModel.java   
│   │
│   ├── phong_tro/          # --> Tab 2: Quản lý phòng
│   │   ├── DanhSachPhongFragment.java 
│   │   ├── ChiTietPhongFragment.java  
│   │   ├── PhongViewModel.java        
│   │   └── mo_hinh/                   
│   │       ├── Phong.java             
│   │       └── KhachThue.java         
│   │
│   ├── hoa_don/            # --> Tab 3: Quản lý thu chi
│   │   ├── DanhSachHoaDonFragment.java
│   │   ├── ChotDienNuocFragment.java  
│   │   ├── HoaDonViewModel.java
│   │   └── mo_hinh/
│   │       └── HoaDonThang.java       
│   │
│   ├── bao_cao/            # --> Tab 4: Thống kê 
│   │   └── BaoCaoDoanhThuFragment.java
│   │
│   └── cai_dat/            # --> Tab 5: Cấu hình hệ thống
│       └── CaiDatHeThongFragment.java
│
└── ManHinhChinhActivity.java  # File gốc khởi chạy App, chứa thanh Menu điều hướng



res/layout/
│
├── BỘ KHUNG CHÍNH (Activities)
│   └── manhinh_chinh.xml                (Khung chứa Bottom Navigation)
│
├── NHÓM: TỔNG QUAN (Dashboard)
│   └── manhinh_tong_quan.xml            (Giao diện màn hình chính)
│
├── NHÓM: QUẢN LÝ PHÒNG (Rooms)
│   ├── manhinh_danhsach_phong.xml       (Giao diện danh sách phòng)
│   ├── manhinh_chitiet_phong.xml        (Giao diện chi tiết 1 phòng)
│   └── the_phong_tro.xml                (Giao diện của 1 "thẻ phòng" nhỏ để lặp lại trong danh sách)
│
├── NHÓM: HÓA ĐƠN & THU TIỀN (Invoices)
│   ├── manhinh_danhsach_hoadon.xml      (Giao diện danh sách hóa đơn)
│   ├── manhinh_chot_dien_nuoc.xml       (Giao diện form chốt điện nước)
│   └── the_hoa_don.xml                  (Giao diện 1 dòng hóa đơn)
│
└── NHÓM: POPUP / DIALOG (Dùng chung)
    ├── cuaso_them_khach.xml             (Popup thêm khách)
    └── cuaso_xacnhan_traphong.xml       (Popup xác nhận trả phòng)