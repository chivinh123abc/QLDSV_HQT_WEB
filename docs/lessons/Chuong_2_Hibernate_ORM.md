# Chương 2: Quản Trị Giao Dịch & Tương Tác CSDL với Hibernate ORM

## 1. Lý thuyết cốt lõi
*   *Lớp học có thể bổ sung chi tiết nội dung lý thuyết tại đây...*

## 2. Áp dụng thực tế trong dự án
*   **SessionFactory & Transaction Manager** cấu hình tại: [spring-config-bean.xml](../../src/main/webapp/WEB-INF/configs/spring-config-bean.xml)
*   **Annotation `@Transactional`:** Được sử dụng trên tất cả các Controller như [LopController.java](../../src/main/java/com/ptithcm/controller/LopController.java) để tự động quản lý transaction (commit/rollback) mà không cần viết mã thủ công.
*   **Truy vấn HQL:** Sử dụng Hibernate Query Language thay thế cho SQL thô để tương tác với các thực thể Java (Entity) trong package [entity](../../src/main/java/com/ptithcm/entity).
