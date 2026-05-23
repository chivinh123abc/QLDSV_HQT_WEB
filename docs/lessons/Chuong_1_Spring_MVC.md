# Chương 1: Kiến Trúc Spring MVC & Luồng Xử Lý

## 1. Lý thuyết cốt lõi
*   *Lớp học có thể bổ sung chi tiết nội dung lý thuyết tại đây...*

## 2. Áp dụng thực tế trong dự án
*   **DispatcherServlet** cấu hình tại: [web.xml](../../src/main/webapp/WEB-INF/web.xml)
*   **Spring Configurations** tại:
    *   [spring-config-bean.xml](../../src/main/webapp/WEB-INF/configs/spring-config-bean.xml) (Quản lý Datasource, Hibernate SessionFactory, Transaction Manager).
    *   [spring-config-mvc.xml](../../src/main/webapp/WEB-INF/configs/spring-config-mvc.xml) (Quét Component Controller, ViewResolver, Mapping tài nguyên tĩnh).
*   **Controller chính:** Các Controller như `HomeController.java` và `LoginController.java` xử lý request của người dùng và chuyển hướng tới view JSP thích hợp.
