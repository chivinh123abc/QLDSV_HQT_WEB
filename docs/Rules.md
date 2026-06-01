# 📐 QUY TẮC & LUẬT LỆ PHÁT TRIỂN DỰ ÁN (PROJECT RULES & GUIDELINES)

Tài liệu này tổng hợp toàn bộ các quy tắc, tiêu chuẩn lập trình, kiến trúc hệ thống và quy trình nghiệp vụ bắt buộc phải tuân thủ trong dự án **QLDSV_HTC_WEB**.

---

## 🛠️ 1. QUY TRÌNH & GIAO TIẾP CỦA AGENT (AGENT PROTOCOLS)

### 🤖 1.1. Tự động Định tuyến Agent (Agent Routing)
*   **Frontend-specialist:** Dùng cho phát triển Web UI/UX, chỉnh sửa JSP, CSS, JS, các giao diện sáng/tối.
*   **Backend-specialist:** Dùng cho API, Controller, Service, DAO, thực thể JPA, CSDL và các nghiệp vụ phía máy chủ.
*   **Checklist bắt buộc:** Trước khi đưa ra phản hồi hoặc sửa đổi mã nguồn, Agent phải hoàn thành kiểm tra định tuyến và thông báo rõ bằng cú pháp:
    `🤖 Applying knowledge of @[agent-name]...`

### 🛑 1.2. Cổng Socratic (Socratic Gate)
*   Đối với các yêu cầu tính năng mới hoặc thay đổi kiến trúc phức tạp, Agent không được tự ý giả định hay triển khai ngay.
*   **Quy trình:** Phải dừng lại và đặt ra tối thiểu **3 câu hỏi chiến lược** về các trường hợp biên (edge cases), giải pháp thay thế hoặc trade-off để làm rõ yêu cầu với người dùng.

### 📋 1.3. Quy trình Kiểm tra Cuối cùng (Final Checklist)
*   Một tác vụ chỉ được coi là hoàn thành sau khi chạy thành công công cụ định dạng và kiểm tra của dự án.
*   Thực hiện định dạng code:
    ```bash
    mvn spotless:apply  # Hoặc chạy: make format
    ```
*   Thực hiện biên dịch và kiểm tra cú pháp:
    ```bash
    mvn clean compile  # Hoặc chạy: make build
    ```

---

## 🧹 2. TIÊU CHUẨN MÃ NGUỒN SẠCH (CLEAN CODE STANDARDS)

*   **Không code dư thừa:** Mã nguồn viết trực diện, ngắn gọn, tự giải thích (self-documenting), không lạm dụng chú thích vô ích.
*   **Tách biệt mối quan tâm (Separation of Concerns):** Tuân thủ cấu trúc phân tầng sạch sẽ:
    `View (JSP)` ➡️ `Controller` ➡️ `Service` ➡️ `DAO (Repository)` ➡️ `Entity (Database)`.
*   **Loại bỏ mã cứng (No Hardcoding):**
    - Các chuỗi HTML gửi email hoặc giao diện phải được đưa ra file mẫu Template (ví dụ: `templates/otp_email.jsp`).
    - Các thông số môi trường (Database, Mail, Redis) phải đưa vào các file `.properties` tương ứng.
*   **Giữ bình luận di sản:** Giữ nguyên các ghi chú, Javadoc cũ không liên quan đến phạm vi sửa đổi để duy trì tính toàn vẹn của mã nguồn.

---

## 🏛️ 3. KIẾN TRÚC SPRING MVC & HIBERNATE 6

*   **DispatcherServlet:** Toàn bộ luồng đi vào ứng dụng phải qua Front Controller. Không viết Servlet thủ công.
*   **Mô hình Server-Side Rendering (SSR) thuần:**
    - Cấu hình `InternalResourceViewResolver` ánh xạ tới `/WEB-INF/views/` dưới dạng `.jsp`.
    - Không viết REST Controllers trả về `@ResponseBody` JSON/XML ngoại trừ các API hỗ trợ AJAX bổ trợ giao diện (ví dụ: truy vấn nhanh danh sách lớp học hoặc kiểm tra trùng lặp).
    - Mọi hành động thao tác (Đăng ký, cập nhật điểm,...) đều phải gửi thông qua thẻ `<form>` hoặc `<form:form>` với phương thức `POST` truyền thống.
    - Áp dụng cơ chế **Post-Redirect-Get (PRG)**: Sau khi thực hiện hành động sửa đổi dữ liệu (POST), bắt buộc redirect về trang hiển thị (GET) để tránh lỗi gửi lại form khi người dùng nhấn F5.
*   **Không viết mã Java trong JSP:** Tuyệt đối loại bỏ cú pháp scriptlet `<% ... %>` trên trang JSP (ngoại trừ thẻ khai báo directive). Sử dụng hoàn toàn EL (`${}`) và thẻ JSTL (`<c:forEach>`, `<c:if>`, `<c:choose>`).
*   **Quản lý Session Hibernate:**
    - Sử dụng `sessionFactory.getCurrentSession()` kết hợp với `@Transactional` cho các phương thức đọc dữ liệu hiển thị.
    - Sử dụng `sessionFactory.openSession()` bọc trong cấu trúc `try-catch-finally` kèm theo transaction `commit()` / `rollback()` cho các thao tác ghi (CRUD) để bảo toàn dữ liệu và tránh rò rỉ session.
*   **Ánh xạ JPA LAZY:** Thiết lập `@ManyToOne` và `@OneToMany(mappedBy="...", fetch=FetchType.LAZY)`. Hạn chế tối đa dùng `FetchType.EAGER` để tránh tải thừa dữ liệu làm chậm ứng dụng.

---

## ✉️ 4. CÁC TÍNH NĂNG ĐẶC THÙ & CƠ CHẾ CỤC BỘ (LOCAL MECHANISMS)

### 🌐 4.1. Đa Ngôn Ngữ (i18n)
*   Các file properties ngôn ngữ đặt tại `src/main/resources/i18n/` phải có định dạng **UTF-8**.
*   Sử dụng `CookieLocaleResolver` và `LocaleChangeInterceptor` để nhận diện ngôn ngữ thay đổi từ query parameter (ví dụ: `?language=vi` hoặc `?language=en`) để đổi ngôn ngữ mà không cần dùng đến JavaScript hay các API bên thứ ba.

### 🌓 4.2. Chế Độ Sáng/Tối (Light/Dark Mode)
*   Sử dụng CSS variables tại `:root` kết hợp với thuộc tính `data-theme="dark"` để tùy biến giao diện.
*   Lưu trạng thái lựa chọn của người dùng vào `LocalStorage` hoặc `Cookie` để tự động khôi phục giao diện trong lần truy cập tiếp theo mà không gây giật màn hình (nhấp nháy trang).

### 🛡️ 4.3. Xác thực CAPTCHA Cục bộ (Local CAPTCHA)
*   Tạo và xác thực mã bảo vệ CAPTCHA hoàn toàn trên máy chủ Local, không gọi dịch vụ ngoài như Google reCAPTCHA.
*   Ủy thác logic tạo ảnh nhiễu và lưu đáp án bí mật vào `HttpSession` (key: `"captcha_key"`) thông qua dịch vụ chuyên biệt `CaptchaService`.

### 🗄️ 4.4. Lưu trữ OTP kích hoạt tài khoản qua Redis
*   Tài khoản đăng ký mới sẽ được lưu ở trạng thái `CHUA_KICH_HOAT`.
*   Mã OTP kích hoạt 6 chữ số sẽ được lưu trữ tạm thời trên bộ nhớ đệm **Redis** với thời gian sống (TTL) là 300 giây (5 phút) thông qua `RedisService`.
*   Lợi ích: Tận dụng cơ chế tự động hủy khóa của Redis khi hết thời gian sống (TTL), giải phóng SQL Server khỏi việc lưu trữ và dọn dẹp các mã OTP hết hạn.
*   **Lưu ý cấu hình:** Cấu hình kết nối Redis thông qua file `redis.properties` sử dụng `JedisPool` an toàn. Nếu mật khẩu để trống, hệ thống sẽ tự động bỏ qua việc xác thực mật khẩu để tương thích với môi trường không bảo mật cục bộ.

---

## 🚀 5. CÁC KỸ THUẬT NÂNG CAO KHUYẾN NGHỊ

*   **Sử dụng UUID cho khóa chính:** Thay vì dùng khóa tự tăng (Auto-increment ID) dễ bị đoán biết, ưu tiên sử dụng UUID (chuỗi 36 ký tự ngẫu nhiên) để tăng tính bảo mật cho thực thể.
*   **Time-stamping & Soft Delete (Lưu vết thời gian):**
    - Tất cả các thực thể nghiệp vụ chính phải kế thừa từ `@MappedSuperclass` `LuuVetThoiGian`.
    - Tự động điền `createdAt`, `updatedAt` thông qua các hàm callback JPA `@PrePersist` và `@PreUpdate`.
    - Hỗ trợ cơ chế xóa mềm bằng cách cập nhật trường `deletedAt` thay vì xóa trực tiếp bản ghi vật lý trong DB.
*   **Thao tác Tải tệp cục bộ (Local File Upload):** Sử dụng `CommonsMultipartResolver` của Spring để nhận file và lưu thẳng vào thư mục cục bộ của máy chủ (ví dụ: `resources/uploads/`), tuyệt đối không tích hợp các API đám mây công cộng.
*   **Stored Procedures với Dynamic Pivot:** Đối với các tác vụ xuất bảng điểm tổng kết lớp học có số môn thay đổi động, bắt buộc phải viết Stored Procedure sử dụng kỹ thuật Dynamic Pivot trên SQL Server để xoay ngang dữ liệu điểm số, nâng cao tốc độ tải trang.
