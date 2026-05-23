# Chương 4: Bảo Mật & Phân Quyền Vai Trò (RBAC) với Interceptor

## 1. Lý thuyết cốt lõi
*   *Lớp học có thể bổ sung chi tiết nội dung lý thuyết tại đây...*

## 2. Áp dụng thực tế trong dự án
*   **AuthInterceptor:** Định nghĩa trong [AuthInterceptor.java](../../src/main/java/com/ptithcm/interceptor/AuthInterceptor.java) và khai báo trong [spring-config-mvc.xml](../../src/main/webapp/WEB-INF/configs/spring-config-mvc.xml).
*   **Remember Me Cookie:** Sử dụng cơ chế ghi nhớ đăng nhập bằng Cookie mã hóa Base64 để lưu thông tin đăng nhập tự động tái lập Session.
*   **Phân quyền (PGV, KHOA, SINHVIEN):** Logic kiểm tra phân quyền nằm trong [LoginController.java](../../src/main/java/com/ptithcm/controller/LoginController.java) và các Controller khác khi xử lý nghiệp vụ lọc khoa hoặc kiểm tra quyền sinh viên.
