# Tài Liệu Tổng Hợp Kiến Thức Dự Án (LESSON NOTES)

Tài liệu này tóm tắt toàn bộ kiến thức cốt lõi và các bài học lập trình thực tế rút ra từ dự án **Quản lý Điểm Sinh viên Hệ Tín chỉ (QLDSV_HTC_WEB)**. Dự án được phát triển dựa trên kiến trúc **Spring MVC**, **Hibernate ORM**, kết hợp với **SQL Server** làm hệ quản trị cơ sở dữ liệu.

---

## 🗺️ Mục Lục & Phân Chia Chương Bài Học
*   **[Chương 1: Kiến Trúc Spring MVC & Luồng Xử Lý](./lessons/Chuong_1_Spring_MVC.md)**
*   **[Chương 2: Quản Trị Giao Dịch & Tương Tác CSDL với Hibernate ORM](./lessons/Chuong_2_Hibernate_ORM.md)**
*   **[Chương 3: Xử Lý Khóa Chính Phức Hợp (Composite Key)](./lessons/Chuong_3_Composite_Keys.md)**
*   **[Chương 4: Bảo Mật & Phân Quyền Vai Trò (RBAC) với Interceptor](./lessons/Chuong_4_Auth_Interceptor.md)**
*   **[Chương 5: Tích Hợp API Restful & Cơ Chế AJAX](./lessons/Chuong_5_API_RESTful_AJAX.md)**
*   **[Chương 6: Tối Ưu Hóa Câu Lệnh SQL & Gọi Stored Procedure](./lessons/Chuong_6_Stored_Procedures.md)**

---

## 1. Kiến Trúc Spring MVC & Luồng Xử Lý
*(Chi tiết nội dung và các bài tập thực hành xem tại: **[Chương 1: Kiến Trúc Spring MVC & Luồng Xử Lý](./lessons/Chuong_1_Spring_MVC.md)**)*

### 🔄 Tổng quan luồng đi của Request (Request Lifecycle)
Dự án áp dụng mô hình kiến trúc MVC tiêu chuẩn thông qua các bước xử lý của Spring:
1. **Client/Browser** gửi HTTP request lên server.
2. [web.xml](../src/main/webapp/WEB-INF/web.xml) cấu hình `DispatcherServlet` làm Front Controller nhận toàn bộ các request.
3. `DispatcherServlet` tham khảo [spring-config-mvc.xml](../src/main/webapp/WEB-INF/configs/spring-config-mvc.xml) và chạy qua bộ lọc [AuthInterceptor](../src/main/java/com/ptithcm/interceptor/AuthInterceptor.java) để kiểm tra đăng nhập/phân quyền.
4. Request chuyển tiếp tới các class được đánh dấu `@Controller` trong package `com.ptithcm.controller`.
5. Controller thực hiện logic nghiệp vụ qua `SessionFactory` (Hibernate) và đổ dữ liệu vào `ModelMap`.
6. `DispatcherServlet` sử dụng `InternalResourceViewResolver` (cấu hình trong `spring-config-mvc.xml`) để tìm kiếm view `.jsp` tương ứng trong thư mục `/WEB-INF/views/` và trả về giao diện HTML hoàn chỉnh cho client.

### ⚙️ Cấu hình cốt lõi (Spring Configuration)
*   **Quét các Controller tự động:**
    ```xml
    <context:component-scan base-package="com.ptithcm.controller" />
    ```
*   **Ánh xạ tài nguyên tĩnh (CSS, JS):**
    ```xml
    <mvc:resources mapping="/resources/**" location="/resources/" />
    ```
*   **Hỗ trợ UTF-8 cho API Response:**
    ```xml
    <mvc:message-converters>
        <bean class="org.springframework.http.converter.StringHttpMessageConverter">
            <property name="supportedMediaTypes">
                <list>
                    <value>text/plain;charset=UTF-8</value>
                    <value>text/html;charset=UTF-8</value>
                    <value>application/json;charset=UTF-8</value>
                </list>
            </property>
        </bean>
    </mvc:message-converters>
    ```

---

## 2. Quản Trị Giao Dịch & Tương Tác CSDL với Hibernate ORM
*(Chi tiết nội dung và các bài tập thực hành xem tại: **[Chương 2: Quản Trị Giao Dịch & Tương Tác CSDL với Hibernate ORM](./lessons/Chuong_2_Hibernate_ORM.md)**)*

### 💼 Cơ chế quản lý Transaction (`@Transactional`)
Thay vì đóng mở Transaction thủ công qua JDBC, Spring hỗ trợ cơ chế Declarative Transaction:
*   Đánh dấu lớp Controller bằng `@Transactional` để mọi thao tác trong phương thức đều chạy trong một Context Transaction an toàn.
*   Cấu hình Transaction Manager trong [spring-config-bean.xml](../src/main/webapp/WEB-INF/configs/spring-config-bean.xml):
    ```xml
    <bean id="transactionManager" class="org.springframework.orm.hibernate5.HibernateTransactionManager">
        <property name="sessionFactory" ref="sessionFactory" />
    </bean>
    <tx:annotation-driven transaction-manager="transactionManager" />
    ```

### 🗄️ Tương tác dữ liệu với Session & HQL
Trong dự án, chúng ta sử dụng `getCurrentSession()` hoặc `openSession()` tùy từng trường hợp:
*   **HQL (Hibernate Query Language):** Truy vấn trên thực thể Java (Entity) thay vì bảng CSDL trực tiếp, giúp mã nguồn độc lập với cơ sở dữ liệu.
*   **Ví dụ tham số hóa để tránh SQL Injection:**
    ```java
    String hql = "FROM Users WHERE username = :username AND password = :password";
    Query<Users> query = hSession.createQuery(hql, Users.class);
    query.setParameter("username", username);
    query.setParameter("password", password);
    Users user = query.uniqueResult();
    ```

---

## 3. Xử Lý Khóa Chính Phức Hợp (Composite Key)
*(Chi tiết nội dung và các bài tập thực hành xem tại: **[Chương 3: Xử Lý Khóa Chính Phức Hợp](./lessons/Chuong_3_Composite_Keys.md)**)*

Bảng đăng ký lớp tín chỉ `DANGKY` liên kết giữa `LOPTINCHI` và `SINHVIEN`, có khóa chính gồm 2 trường (`MALTC`, `MASV`). Trong Hibernate, trường hợp này được xử lý bằng cách tạo ra một lớp Khóa chính rời độc lập.

### 🧩 Lớp Khóa chính độc lập [DangKyId.java](../src/main/java/com/ptithcm/entity/DangKyId.java)
Lớp này bắt buộc phải:
1. Implements `Serializable`.
2. Có hàm khởi tạo không tham số (No-args constructor) và đầy đủ tham số.
3. Ghi đè `equals()` và `hashCode()`.

```java
public class DangKyId implements Serializable {
    private int maLTC;
    private String maSV;

    // Constructors, Getters/Setters, hashCode, equals
}
```

### 🔗 Ánh xạ thực thể [DangKy.java](../src/main/java/com/ptithcm/entity/DangKy.java)
Sử dụng annotation `@IdClass` để liên kết:

```java
@Entity
@Table(name = "DANGKY")
@IdClass(DangKyId.class)
public class DangKy {
    @Id
    @Column(name = "MALTC")
    private int maLTC;

    @Id
    @Column(name = "MASV")
    private String maSV;

    // Các trường dữ liệu điểm số CC, GK, CK
}
```

---

## 4. Bảo Mật & Phân Quyền Vai Trò (RBAC) với Interceptor
*(Chi tiết nội dung và các bài tập thực hành xem tại: **[Chương 4: Bảo Mật & Phân Quyền Vai Trò](./lessons/Chuong_4_Auth_Interceptor.md)**)*

Ứng dụng cần hạn chế truy cập trái phép và phân chia quyền hạn giữa các vai trò: **Phòng Giáo Vụ (PGV)**, **Khoa (KHOA)**, và **Sinh Viên (SINHVIEN)**.

### 🛡️ Cơ chế hoạt động của Interceptor [AuthInterceptor.java](../src/main/java/com/ptithcm/interceptor/AuthInterceptor.java)
*   **Phương thức `preHandle`:** Thực thi trước khi Request được gửi tới Controller. Nếu người dùng chưa đăng nhập, Interceptor sẽ chuyển hướng (redirect) về trang `/login`.
*   **Cơ chế Remember Me (Ghi nhớ đăng nhập):**
    *   Khi người dùng đăng nhập thành công, một Cookie chứa thông tin đăng nhập đã mã hóa Base64 sẽ được lưu dưới trình duyệt.
    *   Khi người dùng tắt trình duyệt và quay lại sau đó, `AuthInterceptor` sẽ giải mã Cookie đó để tự động tái lập Session mà không bắt đăng nhập lại.

### 👥 Phân quyền chi tiết tại Controller (Role-Based Access Control)
*   **Lọc dữ liệu theo Khoa (KHOA Role):** Tránh việc giảng viên thuộc khoa CNTT sửa đổi hoặc xem dữ liệu của khoa Viễn thông.
    ```java
    String sessionRole = (String) httpSession.getAttribute("role");
    String sessionMaKhoa = (String) httpSession.getAttribute("maKhoa");

    if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
        maKhoa = sessionMaKhoa; // Chỉ cho phép xem/thao tác dữ liệu thuộc khoa của mình
    }
    ```
*   **Trang cá nhân của Sinh viên:** Hạn chế sinh viên vào trang quản trị của Giáo vụ:
    ```java
    String role = (String) httpSession.getAttribute("role");
    if (!"SINHVIEN".equals(role)) {
        return "redirect:/login";
    }
    ```

---

## 5. Tích Hợp API Restful & Cơ Chế AJAX
*(Chi tiết nội dung và các bài tập thực hành xem tại: **[Chương 5: Tích Hợp API Restful & Cơ Chế AJAX](./lessons/Chuong_5_API_RESTful_AJAX.md)**)*

Để mang lại trải nghiệm mượt mà không cần tải lại toàn bộ trang (Single Page Application - SPA), các chức năng như nhập điểm, đăng ký/hủy môn học được triển khai qua AJAX.

### 🌐 Phía Backend (Spring REST Controllers)
Sử dụng annotation `@ResponseBody` để Spring tự động serialize Java Object/Map thành dữ liệu JSON (thông qua thư viện `Jackson` được khai báo trong `pom.xml`).

```java
@RequestMapping(value = "/api/register", method = RequestMethod.POST, produces = "application/json")
@ResponseBody
public Map<String, Object> apiRegister(@RequestParam("maLTC") int maLTC, @RequestParam("maSV") String maSV) {
    Map<String, Object> res = new HashMap<>();
    // Logic xử lý đăng ký môn học và lưu vào DB...
    res.put("status", "success");
    res.put("message", "Đăng ký thành công!");
    return res;
}
```

### ⚡ Phía Frontend (JavaScript Fetch API)
Sử dụng Javascript hiện đại để gọi API và cập nhật DOM động:

```javascript
fetch('/registration/api/register', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: `maLTC=${maLTC}&maSV=${maSV}`
})
.then(response => response.json())
.then(data => {
    if (data.status === 'success') {
        alert(data.message);
        location.reload(); // Hoặc cập nhật bảng động
    } else {
        alert(data.message);
    }
});
```

---

## 6. Tối Ưu Hóa Câu Lệnh SQL & Gọi Stored Procedure
*(Chi tiết nội dung và các bài tập thực hành xem tại: **[Chương 6: Tối Ưu Hóa Câu Lệnh SQL & Gọi Stored Procedure](./lessons/Chuong_6_Stored_Procedures.md)**)*

### 🛠️ Gọi Stored Procedure từ Hibernate
Đối với các báo cáo phức tạp (như bảng điểm tổng kết lớp có cấu trúc cột môn học thay đổi động, hoặc xuất danh sách SV đăng ký lớp tín chỉ), chúng ta sử dụng Stored Procedure trên SQL Server để tối ưu hiệu năng tính toán.

Trong [ReportController.java](../src/main/java/com/ptithcm/controller/ReportController.java):
```java
NativeQuery query = session.createNativeQuery("EXEC sp_LayBangDiemTongKet :maLop");
query.setParameter("maLop", maLop);
query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
List<Map<String, Object>> result = query.list();
```
*   `NativeQuery` cho phép chạy trực tiếp mã lệnh SQL thô (Raw SQL).
*   `AliasToEntityMapResultTransformer` chuyển đổi bản ghi trả về từ Stored Procedure thành cấu trúc Map (`Tên_cột` -> `Giá_trị`), giúp việc render cột động ở frontend vô cùng dễ dàng mà không cần tạo trước các DTO (Data Transfer Object) cố định.

### 📑 Các nghiệp vụ ràng buộc dữ liệu lớn (Database Constraints)
*   **Trạng thái lớp tín chỉ:** Trước khi đăng ký, bắt buộc kiểm tra xem lớp đã bị hủy hay chưa (`HUYLOP = 0`).
*   **Trạng thái sinh viên học tập:** Không cho phép sinh viên đang trong trạng thái nghỉ học (`DANGHIHOC = 1`) đăng ký tín chỉ.
*   **Ràng buộc điểm số:** Không cho phép hủy lớp tín chỉ nếu đã có giảng viên nhập điểm chuyên cần/giữa kỳ/cuối kỳ cho bất kỳ sinh viên nào trong lớp đó.

---
> 💡 *Đây là bộ khung kiến trúc chuẩn mực của một ứng dụng Web Java Enterprise kiểu mẫu. Việc nắm vững các bài học thiết kế này sẽ giúp lập trình viên dễ dàng chuyển đổi sang các công nghệ hiện đại hơn như Spring Boot hay Microservices.*
