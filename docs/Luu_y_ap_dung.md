# 📝 DANH SÁCH LƯU Ý & CÁC TÍNH NĂNG CẦN ÁP DỤNG TRONG DỰ ÁN

Tài liệu này đóng vai trò là một checklist nhắc nhở và hướng dẫn thực hành các cơ chế lập trình, tính năng bắt buộc và các kỹ thuật nâng cao cần triển khai trong dự án **QLDSV_HTC_WEB**. Việc hoàn thành đầy đủ các mục dưới đây sẽ giúp đảm bảo chất lượng phần mềm tốt nhất và đạt điểm số tối đa trong kỳ thi.

---

## 🛠️ 1. CÁC TÍNH NĂNG BẮT BUỘC (MANDATORY FEATURES)

### 🌐 1.1. Đa Ngôn Ngữ (i18n)

- **Mục tiêu:** Website hỗ trợ chuyển đổi giao diện mượt mà giữa Tiếng Việt (`vi`) và Tiếng Anh (`en`).
- **Các bước áp dụng:**
  - [ ] Tạo các file tài nguyên ngôn ngữ: `global_vi.properties` và `global_en.properties` trong thư mục `src/main/resources/i18n/`. Định dạng lưu trữ bắt buộc phải là **UTF-8**.
  - [ ] Cấu hình các Bean trong [spring-config-mvc.xml](../../src/main/webapp/WEB-INF/configs/spring-config-mvc.xml):
    - `ReloadableResourceBundleMessageSource` để nạp các file properties.
    - `CookieLocaleResolver` để lưu lựa chọn ngôn ngữ vào Cookie của trình duyệt (giúp ghi nhớ trạng thái).
    - `LocaleChangeInterceptor` để nhận diện sự thay đổi ngôn ngữ qua query parameter (ví dụ: `?lang=en` hoặc `?lang=vi`).
  - [ ] Trên View JSP, khai báo thẻ Spring `<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>` và thay các đoạn text tĩnh bằng `<s:message code="key.name"/>`.
  - [ ] Sử dụng AJAX gọi thay đổi ngôn ngữ và reload lại trang bằng JavaScript: `location.reload()`.

### 🌓 1.2. Chế Độ Sáng/Tối (Light/Dark Mode)

- **Mục tiêu:** Cung cấp nút chuyển đổi giao diện sáng/tối (Dark/Light Theme) thân thiện với người dùng và ghi nhớ trạng thái qua LocalStorage hoặc Cookie.
- **Các bước áp dụng:**
  - [ ] Định nghĩa các CSS variables tại `:root` trong [style.css](../../src/main/webapp/resources/css/style.css):
    ```css
    :root {
      --bg-color: #ffffff;
      --text-color: #1a1a1a;
      /* ... các màu sắc khác cho Light Mode ... */
    }
    [data-theme="dark"] {
      --bg-color: #121212;
      --text-color: #e0e0e0;
      /* ... các màu sắc tương ứng cho Dark Mode ... */
    }
    ```
  - [ ] Viết script JavaScript ở đầu trang để kiểm tra LocalStorage và áp dụng thuộc tính `data-theme` trước khi giao diện render (tránh tình trạng nháy trang):
    ```javascript
    const currentTheme = localStorage.getItem("theme") || "light";
    document.documentElement.setAttribute("data-theme", currentTheme);
    ```
  - [ ] Đặt nút bấm chuyển đổi ở Header và viết sự kiện click để cập nhật LocalStorage và thuộc tính `data-theme` trên thẻ `<html>`.

### ✉️ 1.3. Gửi Email Tự Động (Email Service)

- **Mục tiêu:** Tự động gửi email thông báo cho sinh viên/giảng viên khi có các sự kiện quan trọng (ví dụ: đăng ký tín chỉ thành công, điểm số được cập nhật, hoặc đặt lại mật khẩu).
- **Các bước áp dụng:**
  - [ ] Cấu hình thư viện `javax.mail` trong `pom.xml`.
  - [ ] Cấu hình bean `JavaMailSenderImpl` trong [spring-config-bean.xml](../../src/main/webapp/WEB-INF/configs/spring-config-bean.xml) kết nối tới SMTP Server (sử dụng Gmail SMTP với App Password 16 ký tự).
  - [ ] Xây dựng một `@Service("mailer")` để bao bọc logic gửi thư (đọc file template HTML, đính kèm file, thiết lập encoding UTF-8 cho tiêu đề và nội dung).
  - [ ] Gọi emailer service không đồng bộ (nếu có thể) để tránh làm nghẽn luồng xử lý chính của request.

### 🛡️ 1.4. Google reCAPTCHA (Chống Spam/Bot)

- **Mục tiêu:** Tích hợp Google reCAPTCHA v2 hoặc v3 tại trang Đăng nhập hoặc các form gửi thông tin nhạy cảm để ngăn chặn tấn công dò mật khẩu (Brute Force) và spam.
- **Các bước áp dụng:**
  - [ ] Đăng ký Site Key và Secret Key trên Google reCAPTCHA Admin Console.
  - [ ] Nhúng thư viện reCAPTCHA JavaScript API vào trang JSP:
    ```html
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    ```
  - [ ] Đặt thẻ hiển thị captcha vào form: `<div class="g-recaptcha" data-sitekey="YOUR_SITE_KEY"></div>`.
  - [ ] Ở phía backend (Controller), khi nhận request submit, lấy tham số `g-recaptcha-response` gửi một HTTP POST request tới API của Google (`https://www.google.com/recaptcha/api/siteverify`) để xác thực tính hợp lệ trước khi cho phép đăng nhập/thực thi.

---

## 📖 2. KIẾN THỨC CỐT LÕI TỪ BÀI HỌC (THEORY & FRAMEWORK NOTES)

Dựa trên tài liệu [Tong_hop_kien_thuc.md](./lessons/Tong_hop_kien_thuc.md), các cơ chế nền tảng của Spring MVC & Hibernate cần phải tuân thủ nghiêm ngặt trong mã nguồn:

### ⚙️ 2.1. Kiến Trúc & Cấu Hình

- - [ ] **DispatcherServlet:** Đảm bảo toàn bộ request nghiệp vụ đi qua Front Controller. Không viết Servlet thủ công.
- - [ ] **ViewResolver:** Cấu hình tiền tố `/WEB-INF/views/` và hậu tố `.jsp`. Toàn bộ file JSP hiển thị phải nằm trong thư mục bảo mật `WEB-INF/views/` để tránh việc client truy cập trực tiếp.
- - [ ] **Mô hình Layered:** Tổ chức mã nguồn sạch sẽ: Entity ➡️ DAO ➡️ Service ➡️ Controller.

### 🏷️ 2.2. Định Tuyến & Nhận Tham Số (Controller)

- - [ ] **Phân biệt HTTP Method:** GET dùng để hiển thị giao diện/đọc dữ liệu. POST dùng cho các hành động Thêm/Sửa/Xóa.
- - [ ] **Redirect sau POST:** Khi thực hiện xong hành động ghi dữ liệu (như lưu điểm, thêm sinh viên), bắt buộc sử dụng `return "redirect:/đường_dẫn";` để tránh lỗi double-submit khi người dùng F5.
- - [ ] **Định tuyến tham số:** Sử dụng `@RequestParam` (cho tham số form/query) và `@PathVariable` (cho RESTful URL). Sử dụng JavaBean làm đối số để Spring tự động map dữ liệu form.

### 🔗 2.3. Form & Databinding

- - [ ] **Spring Form Taglib:** Sử dụng thẻ `<form:form>` và thuộc tính `modelAttribute` cho tất cả các màn hình nhập liệu.
- - [ ] **Đổ dữ liệu Combobox động:** Sử dụng `items="${listData}"` kết hợp `itemValue="..."` và `itemLabel="..."` để hiển thị danh sách từ cơ sở dữ liệu (ví dụ danh sách Lớp, danh sách Môn học).
- - [ ] **Giữ dữ liệu form:** Sử dụng `@ModelAttribute` để lưu giữ giá trị người dùng đã nhập khi form bị lỗi, tránh bắt người dùng nhập lại từ đầu.

### 🎨 2.4. EL & JSTL

- - [ ] **Không viết mã Java trong JSP:** Tuyệt đối loại bỏ cú pháp `<% ... %>` (Scriptlet). Sử dụng hoàn toàn EL (`${}`) và JSTL (`<c:forEach>`, `<c:if>`, `<c:choose>`).
- - [ ] **Định dạng dữ liệu hiển thị:** Dùng `<fmt:formatDate>` cho các cột ngày sinh, ngày lập và `<fmt:formatNumber>` cho điểm số, học phí.

### 🔌 2.5. Hibernate & Transaction

- - [ ] **Quản lý Session thông minh:**
  - Dùng `sessionFactory.getCurrentSession()` kết hợp `@Transactional` cho các phương thức chỉ đọc (truy vấn hiển thị bảng biểu).
  - Dùng `sessionFactory.openSession()` và bọc trong cấu trúc `try-catch-finally` kèm theo transaction `commit()` / `rollback()` cho các thao tác CRUD để bảo vệ toàn vẹn dữ liệu và tránh rò rỉ session.
- - [ ] **Quan hệ dữ liệu:** Thiết lập chính xác các annotation `@ManyToOne` (phía N) và `@OneToMany(mappedBy="...", fetch=FetchType.LAZY)` (phía 1). Tránh dùng `FetchType.EAGER` bừa bãi làm giảm hiệu năng hệ thống.
- - [ ] **Phân trang dữ liệu:** Áp dụng phân trang ở database bằng `setFirstResult(offset)` và `setMaxResults(limit)` cho các bảng dữ liệu lớn (như danh sách Sinh viên).

### 🛡️ 2.6. Validation & Security Interceptor

- - [ ] **Xác thực dữ liệu (Validation):** Sử dụng các Validation Annotations (`@NotBlank`, `@NotNull`, `@Email`, `@Pattern`) trên Entity hoặc DTO, đi kèm `@Validated` ở Controller. Hiển thị lỗi ra View bằng thẻ `<form:errors>`.
- - [ ] **Đánh chặn Interceptor:** Kế thừa `HandlerInterceptorAdapter` để viết bộ lọc đăng nhập & phân quyền. Đăng ký Interceptor trong `spring-config-mvc.xml` kèm các quy tắc `<mvc:mapping>` và `<mvc:exclude-mapping>` (cho phép các trang công cộng như `/login` đi qua).

---

## 🚀 3. CÁC KIẾN THỨC NÂNG CAO ĐỀ XUẤT (RECOMMENDED HIGH-SCORE FEATURES)

Để đồ án/bài thi nổi bật và đạt điểm tối đa từ hội đồng chấm thi, khuyến nghị áp dụng thêm các cơ chế hiện đại sau:

### 🔑 3.1. Sử Dụng UUID Cho Toàn Bộ Khóa Chính

- **Lý do:** Thay vì dùng khóa chính tự tăng (Auto-increment ID) dễ bị đoán biết và khai thác bảo mật, sử dụng **UUID** (chuỗi 36 ký tự ngẫu nhiên) giúp bảo mật tốt hơn và an toàn khi tích hợp hệ thống phân tán.
- **Cách áp dụng trong Entity:**
  ```java
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;
  ```

### 🕒 3.2. Tích Hợp Time-stamping / Audit Trails (Lưu Vết Thời Gian)

- **Lý do:** Đây là tính năng tiêu chuẩn của các ứng dụng doanh nghiệp thực tế, giúp ghi lại dấu vết dữ liệu được tạo ra lúc nào, ai cập nhật, và hỗ trợ xóa mềm (Soft Delete).
- **Cách áp dụng:**
  - [ ] Tạo một `@MappedSuperclass` (ví dụ [LuuVetThoiGian.java](../../src/main/java/com/ptithcm/entity/base/LuuVetThoiGian.java)) chứa 3 trường: `createdAt`, `updatedAt`, `deletedAt`.
  - [ ] Cho các Entity khác (SinhVien, GiangVien, Lop...) kế thừa từ class này.
  - [ ] Sử dụng các callback `@PrePersist` và `@PreUpdate` để tự động gán thời gian hiện tại của hệ thống khi ghi xuống CSDL.

### ⚡ 3.3. Tương Tác Không Tải Lại Trang (AJAX / Fetch API)

- **Lý do:** Tạo trải nghiệm mượt mà giống như Single Page Application (SPA), nâng cao UX.
- **Cách áp dụng:**
  - [ ] Triển khai AJAX cho các tính năng tương tác nhanh: Nhập điểm trực tiếp trên bảng, Đăng ký/Hủy môn học của sinh viên, Kiểm tra nhanh sự tồn tại của Mã sinh viên (Live validation).
  - [ ] Ở Backend, viết các hàm trả về `@ResponseBody Map<String, Object>` (tự động chuyển đổi thành JSON).
  - [ ] Ở Frontend, sử dụng `fetch()` của JavaScript để gửi dữ liệu và cập nhật trực tiếp DOM của bảng biểu mà không cần reload trang.

### 🗄️ 3.4. Stored Procedures với Dynamic Pivot (Tối Ưu Truy Vấn Lớn)

- **Lý do:** Khi cần xuất bảng điểm tổng kết lớp, cấu trúc số cột môn học sẽ thay đổi động tùy thuộc vào số môn lớp đó đã học. Viết SQL thông thường sẽ vô cùng phức tạp và chậm.
- **Cách áp dụng:**
  - [ ] Tạo Stored Procedure trên SQL Server sử dụng kỹ thuật **Dynamic Pivot** để xoay ngang dữ liệu điểm số các môn học theo từng sinh viên ngay trên Database.
  - [ ] Trong Java, gọi procedure bằng `session.createNativeQuery("EXEC sp_LayBangDiemTongKet :maLop")`.
  - [ ] Dùng `AliasToEntityMapResultTransformer.INSTANCE` để nhận kết quả dạng `List<Map<String, Object>>`, giúp render bảng cột động ở giao diện cực kỳ linh hoạt.
