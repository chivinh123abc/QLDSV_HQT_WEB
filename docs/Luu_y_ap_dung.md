# 📝 DANH SÁCH LƯU Ý & CÁC TÍNH NĂNG CẦN ÁP DỤNG TRONG DỰ ÁN

Tài liệu này đóng vai trò là một checklist nhắc nhở và hướng dẫn thực hành các cơ chế lập trình, tính năng bắt buộc và các kỹ thuật nâng cao cần triển khai trong dự án **QLDSV_HTC_WEB**. Các cơ chế được thiết kế chạy hoàn toàn trên môi trường **Local (Cục bộ)**, không sử dụng hoặc tạo API, tuân thủ mô hình Server-Side Rendering (SSR) truyền thống.

---

## 🛠️ 1. CÁC TÍNH NĂNG BẮT BUỘC (MANDATORY FEATURES)

### 🌐 1.1. Đa Ngôn Ngữ (i18n)

- **Mục tiêu:** Website hỗ trợ chuyển đổi giao diện mượt mà giữa Tiếng Việt (`vi`) và Tiếng Anh (`en`).
- **Các bước áp dụng (Cơ chế Local):**
  - [ ] Tạo các file tài nguyên ngôn ngữ: `global_vi.properties` và `global_en.properties` trong thư mục `src/main/resources/i18n/`. Định dạng lưu trữ bắt buộc phải là **UTF-8**.
  - [ ] Cấu hình các Bean trong [spring-config-mvc.xml](../../src/main/webapp/WEB-INF/configs/spring-config-mvc.xml):
    - `ReloadableResourceBundleMessageSource` để nạp các file properties.
    - `CookieLocaleResolver` để lưu lựa chọn ngôn ngữ vào Cookie của trình duyệt (giúp ghi nhớ trạng thái).
    - `LocaleChangeInterceptor` để nhận diện sự thay đổi ngôn ngữ qua query parameter (ví dụ: `?language=en` hoặc `?language=vi`).
  - [ ] Trên View JSP, khai báo thẻ Spring `<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>` và thay các đoạn text tĩnh bằng `<s:message code="key.name"/>`.
  - [ ] **Cách thay đổi ngôn ngữ (Không dùng AJAX/API):** Sử dụng các liên kết HTML truyền thống để đổi ngôn ngữ và tải lại trang tự động thông qua đường dẫn (ví dụ: `<a href="?language=vi">Tiếng Việt</a>` | `<a href="?language=en">English</a>`). `LocaleChangeInterceptor` của Spring sẽ tự động đón nhận tham số và thay đổi locale trong Cookie mà không cần dùng đến JavaScript hay các REST API.

### 🌓 1.2. Chế Độ Sáng/Tối (Light/Dark Mode)

- **Mục tiêu:** Cung cấp nút chuyển đổi giao diện sáng/tối (Dark/Light Theme) cục bộ và ghi nhớ trạng thái qua LocalStorage hoặc Cookie của trình duyệt.
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
  - [ ] Viết đoạn mã JavaScript nhỏ ở đầu trang JSP để kiểm tra LocalStorage và áp dụng thuộc tính `data-theme` trước khi giao diện render nhằm tránh tình trạng nhấp nháy trang:
    ```javascript
    const currentTheme = localStorage.getItem("theme") || "light";
    document.documentElement.setAttribute("data-theme", currentTheme);
    ```
  - [ ] Đặt nút bấm chuyển đổi ở Header và viết sự kiện click để cập nhật LocalStorage và thuộc tính `data-theme` trên thẻ `<html>`.

### ✉️ 1.3. Gửi Email Tự Động (Email Service)

- **Mục tiêu:** Tự động gửi email thông báo từ server bằng thư viện JavaMail thông qua Gmail SMTP Server thật.
- **Các bước áp dụng:**

* [ ] Cấu hình thư viện `javax.mail` trong `pom.xml`.
* [ ] Tạo file [gmail.properties](../../src/main/resources/gmail.properties) chứa thông tin cấu hình tài khoản (host, port, username, password ứng dụng).
* [ ] Cấu hình bean `JavaMailSenderImpl` trong [spring-config-gmail.xml](../../src/main/webapp/WEB-INF/configs/spring-config-gmail.xml) sử dụng các biến thuộc tính từ file properties:
      `xml
  <bean id="mailSender" class="org.springframework.mail.javamail.JavaMailSenderImpl">
      <property name="host" value="${mail.host}" />
      <property name="port" value="${mail.port}" />
      <property name="username" value="${mail.username}" />
      <property name="password" value="${mail.password}" />
      <!-- Cấu hình các thuộc tính SSL cho Gmail... -->
  </bean>
  `
* [ ] Xây dựng lớp `@Service("mailer")` bọc logic gửi thư (đọc template HTML, thiết lập UTF-8) để tái sử dụng trên các Controller.

### 🛡️ 1.4. Xác Thực CAPTCHA Cục Bộ (Local Image CAPTCHA)

- **Mục tiêu:** Tạo và xác thực mã bảo vệ CAPTCHA hoàn toàn trên máy chủ Local bằng cách sinh ảnh ngẫu nhiên và lưu đáp án vào HttpSession, loại bỏ hoàn toàn Google reCAPTCHA (vì reCAPTCHA yêu cầu gọi HTTP tới Google API để xác thực).
- **Các bước áp dụng:**
  - [ ] **Tạo Controller sinh ảnh CAPTCHA:** Viết một Controller (ví dụ `/captcha.htm`) sử dụng thư viện Java `BufferedImage` và `Graphics2D` vẽ ra một ảnh chứa chuỗi chữ/số ngẫu nhiên kèm hiệu ứng nhiễu nhẹ.
  - [ ] **Lưu vào Session:** Trước khi trả hình ảnh về client, lưu chuỗi đáp án ngẫu nhiên này vào Session:
    ```java
    session.setAttribute("captcha_key", secretToken);
    ```
  - [ ] **Hiển thị trên Form:** Sử dụng thẻ `<img>` trỏ đến URL sinh ảnh:
    ```html
    <img src="<c:url value='/captcha.htm'/>" id="captcha-img" />
    ```
  - [ ] **Xác thực truyền thống:** Khi người dùng submit form, Controller nhận chuỗi captcha nhập vào từ client và so khớp trực tiếp với giá trị lưu trong `session.getAttribute("captcha_key")`. Nếu không khớp, trả về lỗi validation bằng `errors.rejectValue(...)`.

---

## 📖 2. KIẾN THỨC CỐT LÕI TỪ BÀI HỌC (THEORY & FRAMEWORK NOTES)

Dựa trên tài liệu [Tong_hop_kien_thuc.md](./lessons/Tong_hop_kien_thuc.md), các cơ chế nền tảng của Spring MVC & Hibernate cần phải tuân thủ nghiêm ngặt trong mã nguồn:

### ⚙️ 2.1. Kiến Trúc & Cấu Hình

- - [ ] **DispatcherServlet:** Đảm bảo toàn bộ request nghiệp vụ đi qua Front Controller. Không viết Servlet thủ công.
- - [ ] **ViewResolver:** Cấu hình tiền tố `/WEB-INF/views/` và hậu tố `.jsp`. Toàn bộ file JSP hiển thị phải nằm trong thư mục bảo mật `WEB-INF/views/` để tránh việc client truy cập trực tiếp.
- - [ ] **Mô hình Layered:** Tổ chức mã nguồn sạch sẽ: Entity ➡️ DAO ➡️ Service ➡️ Controller.

### 🏷️ 2.2. Định Tuyến & Nhận Tham Số (Controller)

- - [ ] **Phân biệt HTTP Method:** GET dùng để hiển thị giao diện/đọc dữ liệu. POST dùng cho các hành động Thêm/Sửa/Xóa.
- - [ ] **Redirect sau POST (Post-Redirect-Get):** Khi thực hiện xong hành động ghi dữ liệu (như lưu điểm, thêm sinh viên), bắt buộc sử dụng `return "redirect:/đường_dẫn";` để tránh lỗi double-submit khi người dùng F5.
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
  - [x] Tạo một `@MappedSuperclass` (ví dụ [LuuVetThoiGian.java](../../src/main/java/com/ptithcm/entity/base/LuuVetThoiGian.java)) chứa 3 trường: `createdAt`, `updatedAt`, `deletedAt`.
  - [x] Cho các Entity khác (SinhVien, GiangVien, Lop...) kế thừa từ class này.
  - [x] Sử dụng các callback `@PrePersist` và `@PreUpdate` để tự động gán thời gian hiện tại của hệ thống khi ghi xuống CSDL.

### 🔄 3.3. Tận Dụng Form Submit Truyền Thống và Mô Hình SSR (Không Tạo API/AJAX)

- **Lý do:** Tuân thủ quy định không sử dụng và tạo các endpoint API/AJAX. Hệ thống hoạt động thuần túy theo mô hình kết xuất phía máy chủ (Server-Side Rendering).
- **Cách áp dụng:**
  - [x] Loại bỏ hoàn toàn việc viết các REST Controllers hoặc các action trả về `@ResponseBody` JSON/XML.
  - [x] Mọi hành động thao tác (Đăng ký môn học, Hủy đăng ký, Cập nhật điểm, Lưu thông tin) đều phải gửi thông qua thẻ `<form>` hoặc `<form:form>` với phương thức `POST` truyền thống.
  - [x] Sau khi xử lý POST thành công ở Controller, sử dụng `redirect` về trang GET tương ứng để hiển thị trạng thái dữ liệu mới.
  - [x] Dữ liệu kết quả được đưa vào `ModelMap` và kết xuất thẳng ra HTML thông qua JSP JSTL trên server trước khi trả về cho client.
  - [x] **Lưu file Upload cục bộ:** Khi tải lên file (ảnh sinh viên, tài liệu), sử dụng `CommonsMultipartResolver` của Spring để nhận file và lưu thẳng vào thư mục cục bộ của máy chủ (ví dụ: `resources/uploads/` thông qua `ServletContext.getRealPath`) bằng hàm `file.transferTo(savedFile)`. Tuyệt đối không dùng các API upload lưu trữ đám mây bên ngoài.

### 🗄️ 3.4. Stored Procedures với Dynamic Pivot (Tối Ưu Truy Vấn Lớn)

- **Lý do:** Khi cần xuất bảng điểm tổng kết lớp, cấu trúc số cột môn học sẽ thay đổi động tùy thuộc vào số môn lớp đó đã học. Viết SQL thông thường sẽ vô cùng phức tạp và chậm.
- **Cách áp dụng:**
  - [x] Tạo Stored Procedure trên SQL Server sử dụng kỹ thuật **Dynamic Pivot** để xoay ngang dữ liệu điểm số các môn học theo từng sinh viên ngay trên Database.
  - [x] Trong Java, gọi procedure bằng `session.createNativeQuery("EXEC sp_LayBangDiemTongKet :maLop")`.
  - [x] Dùng `AliasToEntityMapResultTransformer.INSTANCE` để nhận kết quả dạng `List<Map<String, Object>>`, giúp render bảng cột động ở giao diện cực kỳ linh hoạt.

### 🛡️ 3.5. Global Exception Handling (Bắt lỗi tập trung với @ControllerAdvice)

- **Lý do:** Trong đồ án sinh viên, khi code văng lỗi (ví dụ: NullPointerException, đứt cáp Database), màn hình sẽ hiện ra trang báo lỗi mặc định của Tomcat đỏ lòm chứa toàn Stack Trace. Thầy cô cực kỳ ghét điều này vì nó trông rất nghiệp dư và lộ cấu trúc hệ thống. Việc xử lý lỗi tập trung thể hiện sự chuyên nghiệp và chỉn chu trong thiết kế phần mềm.
- **Cách áp dụng:**
  - [x] Tạo một class `GlobalExceptionHandler` gắn `@ControllerAdvice`. Class này sẽ tự động "hứng" mọi Exception văng ra từ bất kỳ Controller nào và redirect người dùng về một trang `shared/error` được thiết kế đẹp mắt.
  ```java
  @ControllerAdvice
  public class GlobalExceptionHandler {
      @ExceptionHandler(Exception.class)
      public String handleAllExceptions(Exception ex, ModelMap model) {
          model.addAttribute("errorMsg", "Hệ thống đang bảo trì, vui lòng thử lại sau!");
          return "shared/error"; // Trả về 1 file giao diện thân thiện
      }
  }
  ```

### 🕵️‍♂️ 3.6. AOP (Aspect-Oriented Programming) cho Audit Logging

- **Lý do:** Để ghi log lịch sử (Ví dụ: Giảng viên A vừa sửa điểm của Sinh viên B), sinh viên thường phải viết các dòng lệnh `System.out.println` hoặc gọi hàm log rải rác khắp hàng chục class Controller. AOP là một khái niệm nâng cao của Spring giúp tách biệt nghiệp vụ ghi nhật ký hệ thống ra khỏi logic nghiệp vụ chính, giữ cho code sạch sẽ và dễ bảo trì.
- **Cách áp dụng:**
  - [x] Tạo một class `AuditLogAspect`, dùng annotation `@AfterReturning` để lắng nghe mọi hàm có tên bắt đầu bằng `insert`, `update`, `delete` trong package modules. Mỗi khi có ai đó gọi hàm cập nhật điểm hay đăng ký môn, Aspect này tự động chạy ngầm và ghi ra file log hoặc lưu vào DB: "Thời gian X, User Y vừa thực hiện hành động Z".

### 🔒 3.7. Bảo mật Anti-CSRF Token cho Form POST

- **Lý do:** Vì đồ án dùng 100% Form HTML và SSR, điểm yếu chí mạng của kiến trúc này chính là tấn công CSRF (Cross-Site Request Forgery). Kẻ gian có thể lừa sinh viên bấm vào một link lạ để tự động gửi form POST ẩn lên `/registration` và xóa hết môn học. Triển khai Anti-CSRF thể hiện tư duy của một Security Engineer thực thụ.
- **Cách áp dụng:**
  1. **Viết một CsrfInterceptor:** Mỗi khi người dùng GET một trang, tự động sinh ra một chuỗi ngẫu nhiên (UUID) lưu vào Session và đưa xuống file JSP.
  2. **Tích hợp vào Form HTML:** Trong mọi thẻ `<form>` hoặc `<form:form>`, gắn thêm:
     ```html
     <input type="hidden" name="csrf_token" value="${csrfToken}" />
     ```
  3. **Xác thực Token:** Khi form POST lên, Interceptor sẽ chặn lại kiểm tra xem Token gửi lên có khớp với Token trong Session không. Nếu không, đá văng ra trang lỗi (403 Forbidden).
