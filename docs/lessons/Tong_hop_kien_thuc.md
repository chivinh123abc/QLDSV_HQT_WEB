# Tài Liệu Tổng Hợp Kiến Thức Lập Trình Web (LESSON NOTES)

Tài liệu này tóm tắt toàn bộ kiến thức cốt lõi và các bài học lập trình thực tế rút ra từ các chương học trong môn Lập trình Web với **Spring MVC** và **Hibernate ORM**. Đây là nền t án quan trọng giúp xây dựng ứng dụng web Java Enterprise chuẩn mực, hiệu năng cao và bảo mật.

---

## 🗺️ Mục Lục & Phân Chia Chương Bài Học

- **[Chương 1: Kiến Trúc Spring MVC & Luồng Xử Lý](./Chuong_1_Spring_MVC.md)**
- **[Chương 2: Controller & Xử Lý Yêu Cầu](./Chuong_2_Controller.md)**
- **[Chương 3: Làm Việc Với Form & Databinding](./Chuong_3_Form.md)**
- **[Chương 4: Expression Language (EL) & JSTL](./Chuong_4_EL_JSTL.md)**
- **[Chương 5: Dependency Injection, Upload File & Gửi Email](./Chuong_5_Bean_DI_File_Email.md)**
- **[Chương 6: Tích Hợp Hibernate ORM & Transaction](./Chuong_6_Hibernate.md)**
- **[Chương 7: Validation & Interceptor](./Chuong_7_Validation_Interceptor.md)**
- **[Chương 8: Tổ Chức Giao Diện, Đa Ngôn Ngữ (i18n) & Trình Soạn Thảo](./Chuong_8_To_Chuc_Giao_Dien.md)**

---

## 1. Kiến Trúc Spring MVC & Luồng Xử Lý

_(Chi tiết xem tại: **[Chương 1: Kiến Trúc Spring MVC & Luồng Xử Lý](./Chuong_1_Spring_MVC.md)**)_

### 🔄 Luồng Đi Của Một Request (Request Lifecycle)

Dự án áp dụng mô hình kiến trúc MVC (Model-View-Controller) tiêu chuẩn của Spring Web:

1. **Client/Browser** gửi HTTP Request đến máy chủ.
2. `DispatcherServlet` (Front Controller) tiếp nhận request được cấu hình trong [web.xml](../../src/main/webapp/WEB-INF/web.xml) (ví dụ các URL có đuôi `.htm`).
3. `DispatcherServlet` tham chiếu đến `Handler Mapping` (được cấu hình trong [spring-config-mvc.xml](../../src/main/webapp/WEB-INF/configs/spring-config-mvc.xml)) để xác định Controller và Action tương ứng.
4. Yêu cầu chạy qua các bộ lọc đăng nhập/phân quyền (Interceptor) nếu có, trước khi chuyển tiếp tới `@Controller` thích hợp.
5. Controller thực hiện logic nghiệp vụ thông qua Service/DAO, tương tác cơ sở dữ liệu qua `SessionFactory` (Hibernate) và đổ kết quả vào `ModelMap`.
6. `DispatcherServlet` tham khảo `ViewResolver` (cấu hình trong `spring-config-mvc.xml`) để chuyển đổi tên logic của View thành đường dẫn vật lý đầy đủ (ví dụ: trả về `"student/index"` -> ánh xạ thành `/WEB-INF/views/student/index.jsp`).
7. Gọi View để kết xuất kết quả thành HTTP Response (giao diện HTML) gửi về cho Client.

### ⚙️ Cấu Hình Spring MVC Cốt Lõi

- **DispatcherServlet & Encoding Filter trong [web.xml](../../src/main/webapp/WEB-INF/web.xml):**
  - Cấu hình `DispatcherServlet` để nạp các file XML từ `/WEB-INF/configs/`.
  - Khai báo `CharacterEncodingFilter` để đảm bảo hệ thống làm việc chuẩn xác với bảng mã UTF-8 (hỗ trợ hiển thị và nhập liệu tiếng Việt).
- **Cấu hình trong [spring-config-mvc.xml](../../src/main/webapp/WEB-INF/configs/spring-config-mvc.xml):**
  - Khai báo ViewResolver:
    ```xml
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/views/" />
        <property name="suffix" value=".jsp" />
    </bean>
    ```
  - Khai báo quét các Controller tự động:
    ```xml
    <context:component-scan base-package="com.ptithcm.controller" />
    ```

### 📦 Các Đối Tượng Web & Chia Sẻ Dữ Liệu

- **HttpServletRequest:** Chứa thông tin request, chia sẻ dữ liệu trong phạm vi 1 request.
- **HttpSession:** Duy trì phiên làm việc cho từng client riêng biệt, thích hợp lưu thông tin đăng nhập.
- **ServletContext:** Phạm vi toàn cục của ứng dụng, chia sẻ dữ liệu cho tất cả các session và request. Có thể tiêm trực tiếp bằng `@Autowired`.
- **Truyền dữ liệu sang View:** Sử dụng `ModelMap` bằng cách khai báo tham số trong Action method và dùng phương thức `model.addAttribute("key", value)`. Sử dụng `${key}` bên trang JSP (EL) để hiển thị.

---

## 2. Controller & Xử Lý Yêu Cầu

_(Chi tiết xem tại: **[Chương 2: Controller & Xử Lý Yêu Cầu](./Chuong_2_Controller.md)**)_

### 🏷️ Định Tuyến với `@RequestMapping`

- Ánh xạ các yêu cầu URL từ người dùng đến các phương thức xử lý trong `@Controller`.
- Có thể cấu hình ở cấp độ Lớp (đường dẫn chung) và cấp độ Phương thức (Action cụ thể).
- Ví dụ:
  ```java
  @Controller
  @RequestMapping("/student")
  public class StudentController {
      @RequestMapping("/list")
      public String list() { return "student/list"; }
  }
  ```

### ⚡ So Sánh Phương Thức HTTP GET và POST

- **GET:** Dữ liệu truyền trên URL (sau dấu `?`), không bảo mật (lộ thông tin trên thanh địa chỉ), giới hạn kích thước (tối đa 2048 ký tự), có thể cache/bookmark. Thích hợp dùng để truy vấn/hiển thị dữ liệu.
- **POST:** Dữ liệu truyền ẩn trong HTTP Body, bảo mật tốt hơn (an toàn tuyệt đối khi đi kèm HTTPS), không giới hạn kích thước, không thể cache/bookmark. Thích hợp dùng để thay đổi trạng thái dữ liệu (thêm, sửa, xóa, submit form).

### 📥 Các Phương Pháp Nhận Tham Số Từ Client

1.  **Sử dụng `HttpServletRequest`:** Gọi `request.getParameter("tên_tham_số")`.
2.  **Sử dụng `@RequestParam`:** Tự động ép kiểu dữ liệu và hỗ trợ giá trị mặc định.
    ```java
    public String search(@RequestParam(value="query", defaultValue="", required=false) String query)
    ```
3.  **Sử dụng JavaBean:** Nhận trực tiếp đối tượng làm tham số, Spring MVC tự động gán dữ liệu form vào các thuộc tính trùng tên thông qua các hàm setter.
4.  **Sử dụng `@PathVariable`:** Trích xuất biến trực tiếp trên URL path (Clean/RESTful URL).
    ```java
    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id)
    ```
5.  **Sử dụng `@CookieValue`:** Đọc trực tiếp giá trị của Cookie từ trình duyệt.

### 📤 Đầu Ra Của Phương Thức Action

- **Trả về tên View (JSP):** `return "tên_view";` (Sẽ qua ViewResolver xử lý).
- **Trả về nội dung trực tiếp:** Đi kèm annotation `@ResponseBody` để xuất dữ liệu chuỗi hoặc định dạng JSON trực tiếp về client (thường dùng cho API/AJAX).
- **Chuyển hướng (Redirect):** `return "redirect:/đường_dẫn_mới";` giúp gửi yêu cầu redirect đến client.

---

## 3. Làm Việc Với Form & Databinding

_(Chi tiết xem tại: **[Chương 3: Làm Việc Với Form](./Chuong_3_Form.md)**)_

### 🔗 Cơ Chế Databinding (Ràng Buộc Dữ Liệu Hai Chiều)

- Đồng bộ hóa tự động dữ liệu giữa thuộc tính của JavaBean (Backend) và các điều khiển trên form giao diện (Frontend).
- **Chiều lên:** Dữ liệu người dùng nhập vào các thẻ input trên giao diện tự động đổ vào đối tượng Java khi submit.
- **Chiều về:** Dữ liệu có sẵn trong đối tượng Java tự động hiển thị lên lại các ô nhập liệu khi form được tải.

### 🛠️ Thư Viện Thẻ Spring Form Taglib

- Khai báo đầu trang JSP: `<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>`.
- Form được khai báo bằng thẻ `<form:form action="..." modelAttribute="tên_bean">`.
- Các thẻ control tương đương HTML:
  - `<form:input path="propertyName"/>` - Text field.
  - `<form:password path="propertyName"/>` - Password field.
  - `<form:select path="propertyName" items="${listData}" itemValue="..." itemLabel="..."/>` - Combobox/Dropdown list.
  - `<form:radiobuttons path="..." items="..."/>` - Nhóm các nút radio.
  - `<form:checkboxes path="..." items="..."/>` - Nhóm các ô chọn checkbox.

### ⚙️ Annotation `@ModelAttribute`

- **Trên tham số của action:** Tự động trích xuất và map dữ liệu form vào đối số, đồng thời lưu trữ đối số đó vào Model để hiển thị lại dữ liệu cũ khi quay lại form (rất hữu ích khi form có lỗi cần nhập lại).
- **Trên phương thức độc lập:** Phương thức này tự động chạy trước mọi action khác trong Controller để đưa một danh sách dữ liệu (như tỉnh thành, giới tính, khoa...) vào Model dùng chung cho các Combobox/Select ở các view khác nhau.

---

## 4. Expression Language (EL) & JSTL

_(Chi tiết xem tại: **[Chương 4: EL và JSTL](./Chuong_4_EL_JSTL.md)**)_

### 🔍 Biểu Thức Expression Language (EL)

- Cú pháp: `${biểu_thức}`. Dùng để lấy dữ liệu từ các scope một cách nhanh gọn, tránh việc dùng thẻ Scriptlet Java (`<% ... %>`) rườm rà.
- Trình tự tìm kiếm scope mặc định: `pageScope` -> `requestScope` -> `sessionScope` -> `applicationScope`.
- Truy xuất thuộc tính JavaBean: `${student.name}` (thực tế gọi phương thức `getName()`).
- Truy xuất Array/Collection: `${list[0]}`.
- Truy xuất Map: `${map.key}` hoặc `${map['key']}`.
- Truy xuất tham số URL & Cookie: `${param.username}`, `${cookie.theme.value}`.

### 🎨 Bộ Thư Viện Thẻ JSTL (Java Standard Tag Library)

- **JSTL Core (`prefix="c"`):**
  - `<c:if test="${điều_kiện}">...</c:if>`: Rẽ nhánh điều kiện đơn giản.
  - `<c:choose>`, `<c:when>`, `<c:otherwise>`: Rẽ nhánh nhiều trường hợp (tương tự `switch-case` hoặc `if-else`).
  - `<c:forEach var="item" items="${list}">`: Vòng lặp duyệt danh sách.
  - `<c:set var="..." value="..." scope="..."/>`: Khai báo biến mới vào scope.
- **JSTL Format (`prefix="fmt"`):**
  - `<fmt:formatNumber value="${price}" type="currency"/>`: Định dạng tiền tệ.
  - `<fmt:formatDate value="${date}" pattern="dd/MM/yyyy"/>`: Định dạng ngày tháng hiển thị.
- **JSTL Functions (`prefix="fn"`):**
  - Cung cấp các hàm xử lý chuỗi và đếm số lượng phần tử trực tiếp trong EL. Ví dụ: `${fn:length(list)}`, `${fn:contains(str, 'admin')}`, `${fn:trim(str)}`.

---

## 5. Dependency Injection, Upload File & Gửi Email

_(Chi tiết xem tại: **[Chương 5: Bean và Dependency Injection, Upload File & Gửi Email](./Chuong_5_Bean_DI_File_Email.md)**)_

### 🤝 Dependency Injection (DI) & Quản Lý Bean

- **Khái niệm:** DI giúp tách rời các module phụ thuộc lẫn nhau, chuyển việc khởi tạo đối tượng cho Spring Container quản lý.
- **Khai báo Bean:** Đánh dấu các class nghiệp vụ bằng `@Component`, `@Service`, `@Repository` và khai báo cấu hình quét `<context:component-scan>` trong XML.
- **Tiêm Bean (`@Autowired`):** Tự động đưa instance của Bean vào vị trí cần thiết. Có 3 kiểu tiêm: Tiêm vào trường (Field Injection), tiêm qua Constructor, và tiêm qua phương thức Setter.
- **Giải quyết tranh chấp:** Sử dụng `@Qualifier("id_bean")` khi có nhiều Bean cùng một Interface/kiểu dữ liệu để chỉ định chính xác Bean cần tiêm.

### 📁 Cơ Chế Upload File Lên Server

1.  **Thư viện:** Cần nạp `commons-fileupload` và `commons-io`.
2.  **Cấu hình Bean** `CommonsMultipartResolver` trong [spring-config-bean.xml](../../src/main/webapp/WEB-INF/configs/spring-config-bean.xml):
    ```xml
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <property name="maxUploadSize" value="20971520" /> <!-- Giới hạn 20MB -->
    </bean>
    ```
3.  **Form HTML:** Phải thiết lập `method="POST"` và `enctype="multipart/form-data"`.
4.  **Xử lý ở Controller:**
    ```java
    public String upload(@RequestParam("photo") MultipartFile file) {
        if (!file.isEmpty()) {
            File savedFile = new File("đường_dẫn_vật_lý/" + file.getOriginalFilename());
            file.transferTo(savedFile);
        }
        return "success";
    }
    ```

### ✉️ Tích Hợp Gửi Email tự động

- Khai báo và cấu hình bean `JavaMailSenderImpl` (id: `mailSender`) cấu hình thông số SMTP Server (ví dụ: host `smtp.gmail.com`, port `465` hoặc `587`, username và App Password bảo mật).
- Sử dụng lớp `MimeMessageHelper` để thiết lập nội dung gửi: người gửi (`from`), người nhận (`to`), tiêu đề (`subject`), nội dung định dạng HTML (`setText(body, true)`), và đính kèm file (`addAttachment`).
- Khuyến khích đóng gói các phương thức gửi email thành một lớp `@Service("mailer")` chung để dễ dàng tái sử dụng ở nhiều Controller khác nhau.

---

## 6. Tích Hợp Hibernate ORM & Transaction

_(Chi tiết xem tại: **[Chương 6: Tích Hợp Hibernate ORM & Transaction](./Chuong_6_Hibernate.md)**)_

### 🗄️ Ánh Xạ Đối Tượng - Quan Hệ (ORM)

- **Hibernate** ánh xạ Class thành Table và thuộc tính thành Column thông qua các JPA Annotation phổ biến:
  - `@Entity`, `@Table(name="TÊN_BẢNG")`.
  - `@Id`, `@GeneratedValue(strategy=...)` định nghĩa khóa chính.
  - `@Column(name="TÊN_CỘT")` ánh xạ thuộc tính.
  - `@Temporal(TemporalType.DATE/TIMESTAMP)` chuyển đổi kiểu ngày tháng Java sang SQL Date/DateTime.
- **Thiết lập mối quan hệ giữa các bảng:**
  - `@ManyToOne` đi kèm `@JoinColumn(name="MÃ_KHÓA_NGOẠI")` ánh xạ liên kết N-1 (ví dụ: SinhVien -> Lop).
  - `@OneToMany(mappedBy="...", fetch=FetchType.LAZY/EAGER)` ánh xạ quan hệ 1-N (ví dụ: Lop -> Danh sách SinhVien). Chế độ `LAZY` trì hoãn việc tải dữ liệu cho đến khi được gọi, giúp tiết kiệm bộ nhớ; `EAGER` tải kèm dữ liệu liên kết ngay lập tức.

### 🔌 Cơ Chế Quản Lý Session & Transaction

- Khai báo `LocalSessionFactoryBean` (tạo `SessionFactory`) và `HibernateTransactionManager` (quản lý transaction) trong file cấu hình Spring Bean XML.
- **Truy vấn dữ liệu (Read-only):** Sử dụng `sessionFactory.getCurrentSession()` để tận dụng session hiện tại của Spring. Yêu cầu đặt annotation `@Transactional` trên Controller hoặc method. Spring tự động đóng session và quản lý vòng đời transaction.
- **Thao tác ghi dữ liệu (Thêm, Sửa, Xóa):** Sử dụng `sessionFactory.openSession()` để tạo một session độc lập. Lập trình viên phải tự bắt đầu transaction, commit khi thành công, rollback nếu xảy ra ngoại lệ, và đóng session trong khối `finally` để tránh rò rỉ tài nguyên (resource leak).
  ```java
  Session session = sessionFactory.openSession();
  Transaction tx = session.beginTransaction();
  try {
      session.saveOrUpdate(entity);
      tx.commit();
  } catch (Exception e) {
      tx.rollback();
      throw e;
  } finally {
      session.close();
  }
  ```
- **Ngôn ngữ HQL (Hibernate Query Language):** Truy vấn dữ liệu hướng đối tượng. Truy vấn trực tiếp trên tên lớp thực thể và thuộc tính Java.
- **Phân trang kết quả truy vấn:**
  ```java
  Query query = session.createQuery("FROM DangKy");
  query.setFirstResult(0); // Vị trí bắt đầu
  query.setMaxResults(10); // Số lượng bản ghi cần lấy
  List<DangKy> list = query.list();
  ```

---

## 7. Validation & Interceptor

_(Chi tiết xem tại: **[Chương 7: Validation & Interceptor](./Chuong_7_Validation_Interceptor.md)**)_

### 🔍 Kiểm Lỗi Dữ Liệu Nhập Liệu (Validation)

- **Manual Validation (Kiểm lỗi bằng tay):**
  - Tại Controller, đặt tham số `BindingResult errors` ngay sát phía sau Bean nhận dữ liệu form.
  - Sử dụng câu lệnh `if` kiểm tra logic, gọi `errors.rejectValue("tên_trường", "mã_lỗi", "Thông báo hiển thị")` khi phát hiện sai phạm.
  - Kiểm tra `errors.hasErrors()`, nếu `true` thì chuyển hướng quay lại view chứa form nhập liệu để người dùng sửa đổi.
- **Annotation Validation (Tự động hóa):**
  - Khai báo luật trực tiếp trên class Bean bằng các Annotation: `@NotBlank` (chuỗi không rỗng/khoảng trắng), `@NotNull` (không được null), `@DecimalMin` / `@DecimalMax` (kiểm tra giới hạn số), `@Email` (định dạng email), `@Pattern(regexp="...")` (kiểm tra regex).
  - Tại Controller, thêm `@Validated` (hoặc `@Valid`) ngay trước Bean nhận dữ liệu để kích hoạt cơ chế kiểm tra tự động của Hibernate Validator.
- **Hiển thị lỗi trên giao diện JSP:** Sử dụng thẻ `<form:errors path="tên_trường" element="span"/>`.

### 🛡️ Bộ Đánh Chặn (Interceptor) & Bảo Mật Vai Trò (RBAC)

- **Cơ chế hoạt động:** Interceptor đóng vai trò làm màng lọc nằm giữa Client và Controller, đánh chặn các request gửi đến để kiểm tra các tiền điều kiện (như đăng nhập, quyền truy cập).
- **Khai báo và xây dựng:**
  - Tạo class Java kế thừa `HandlerInterceptorAdapter` (hoặc implement `HandlerInterceptor`).
  - Ghi đè phương thức `preHandle(request, response, handler)`:
    - Trả về `true`: Request được phép đi tiếp tới Controller.
    - Trả về `false`: Request bị chặn lại. Có thể kết hợp gọi `response.sendRedirect(...)` để chuyển hướng người dùng (ví dụ về trang đăng nhập).
- **Cấu hình quy tắc trong [spring-config-mvc.xml](../../src/main/webapp/WEB-INF/configs/spring-config-mvc.xml):**
  ```xml
  <mvc:interceptors>
      <mvc:interceptor>
          <mvc:mapping path="/admin/**" />
          <mvc:exclude-mapping path="/admin/login.htm" />
          <bean class="com.ptithcm.shared.interceptor.AuthInterceptor" />
      </mvc:interceptor>
  </mvc:interceptors>
  ```
- **Ứng dụng thực tế:** Kiểm tra session người dùng trong `preHandle()`. Nếu không tìm thấy thông tin session (chưa đăng nhập), chuyển hướng về trang đăng nhập `/login.htm` và chặn request đi tiếp.

---

## 8. Tổ Chức Giao Diện, Đa Ngôn Ngữ (i18n) & Trình Soạn Thảo

_(Chi tiết xem tại: **[Chương 8: Tổ Chức Giao Diện & Tiện Ích Đa Phương Tiện](./Chuong_8_To_Chuc_Giao_Dien.md)**)_

### 🌐 Hỗ Trợ Đa Ngôn Ngữ (i18n)

- Tách biệt toàn bộ nội dung văn bản (label, placeholder, message) ra khỏi mã nguồn JSP và lưu trữ dưới dạng các cặp `key=value` trong các file tài nguyên cấu hình ngôn ngữ `.properties` (được lưu dưới định dạng **UTF-8**).
- Tạo các file theo chuẩn ngôn ngữ: `global_en.properties` (tiếng Anh), `global_vi.properties` (tiếng Việt).
- **Hiển thị trên JSP:**
  - Khai báo thẻ Spring: `<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>`.
  - Hiển thị thông điệp bằng key tương ứng: `<s:message code="global.title"/>`.
- **Cấu hình Spring đa ngôn ngữ:** Khai báo 3 Bean cốt lõi:
  1.  `ReloadableResourceBundleMessageSource`: Chỉ ra vị trí nạp các file `.properties`.
  2.  `CookieLocaleResolver`: Lưu trữ thông tin ngôn ngữ người dùng đã chọn vào Cookie của trình duyệt để duy trì trạng thái trong các phiên sau.
  3.  `LocaleChangeInterceptor`: Bộ đánh chặn bắt tham số đổi ngôn ngữ trên URL (ví dụ: `?language=vi` hoặc `?language=en`).
- **Thay đổi ngôn ngữ động:** Tạo các nút liên kết ngôn ngữ trên giao diện, sử dụng JavaScript/AJAX để gửi tham số thay đổi ngôn ngữ lên server, sau đó gọi `location.reload()` để tải lại giao diện với ngôn ngữ mới.

### 📝 Tích Hợp Trình Soạn Thảo CKEditor

- Biến một thẻ `<textarea>` thông thường thành khung soạn thảo văn bản giàu tính năng (định dạng chữ, bảng, chèn ảnh tương tự Microsoft Word).
- **Thực hiện:** Tải thư mục thư viện CKEditor, đặt vào `resources/ckeditor/`, liên kết file script `ckeditor.js` ở đầu trang và khởi tạo trình soạn thảo thay thế thẻ textarea:
  ```javascript
  CKEDITOR.replace("id_của_textarea");
  ```

### 📁 Tích Hợp Quản Lý Tập Tin CKFinder

- Cho phép người dùng upload hình ảnh, tài liệu trực tiếp từ máy tính cá nhân lên thư mục lưu trữ của máy chủ thông qua giao diện CKEditor.
- **Thực hiện:**
  - Đặt thư viện CKFinder vào `resources/ckfinder/`.
  - Khai báo `ConnectorServlet` trong `web.xml` để đón các yêu cầu xử lý file và kết nối đến cấu hình `/WEB-INF/config.xml`.
  - Copy toàn bộ file thư viện `.jar` đi kèm của CKFinder vào thư mục `WEB-INF/lib` để hệ thống Java biên dịch và hỗ trợ xử lý file upload một cách chuẩn xác.

---

> 💡 **Lời khuyên thực tiễn:** Việc nắm vững và áp dụng chuẩn chỉ bộ khung Spring MVC, Hibernate ORM và các kỹ thuật validation, bảo mật (Interceptor), đa ngôn ngữ (i18n) sẽ giúp tạo ra các sản phẩm phần mềm Java Enterprise vững chắc, dễ bảo trì, sẵn sàng tích hợp các công nghệ hiện đại hơn như Spring Boot hoặc Microservices.
