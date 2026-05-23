# Chương 6: Tối Ưu Hóa Câu Lệnh SQL & Gọi Stored Procedure

## 1. Lý thuyết cốt lõi
*   *Lớp học có thể bổ sung chi tiết nội dung lý thuyết tại đây...*

## 2. Áp dụng thực tế trong dự án
*   **Stored Procedure:** Được định nghĩa trên database SQL Server (có cấu trúc mẫu trong [Gendb.sql](../../Gendb.sql)).
*   **Gọi Procedure từ Hibernate:** Sử dụng `session.createNativeQuery()` trong [ReportController.java](../../src/main/java/com/ptithcm/controller/ReportController.java) để thực thi `sp_LayBangDiemTongKet` và `sp_LayDanhSachSinhVienDangKyLopTinChi`.
*   **Chuyển đổi dữ liệu động:** Sử dụng `AliasToEntityMapResultTransformer` để biến các dòng cột động từ CSDL thành danh sách các đối tượng Map truyền về giao diện.
