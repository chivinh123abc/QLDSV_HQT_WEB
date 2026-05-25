# Lập trình web Spring MVC - Bài 1: Giới thiệu Spring MVC

## 1. Giới thiệu Spring Framework

Spring là một Framework mã nguồn mở, được sử dụng bởi hàng triệu lập trình viên để phát triển các ứng dụng Java một cách mạnh mẽ, nhanh chóng với hiệu năng cao, dễ kiểm thử và dễ tái sử dụng mã.

- Spring có thể được sử dụng để phát triển Java Desktop, ứng dụng di động, và đặc biệt là Java Web.

- Mục tiêu chính của Spring là giúp việc phát triển các ứng dụng J2EE trở nên dễ dàng hơn dựa trên mô hình sử dụng các POJO (Plain Old Java Object).

**Kiến trúc tổng quan của Spring Framework bao gồm các module chính:**

- **Spring Core:** Chứa IoC Container đóng vai trò là nền tảng cốt lõi.

- **Spring Context:** Cung cấp các dịch vụ truy cập từ xa như ApplicationContext, JNDI, EJB, Remoting.

- **Spring DAO:** Cung cấp dịch vụ cho đối tượng truy xuất dữ liệu, bao gồm hỗ trợ JDBC, DAO và cấu trúc Transaction.

- **Spring ORM:** Hỗ trợ tích hợp các framework ánh xạ đối tượng quan hệ dữ liệu như Hibernate, iBatis, JDO.

- **Spring AOP:** Cung cấp cơ sở hạ tầng lập trình hướng khía cạnh (AOP), Metadata và quản lý giao dịch khai báo (Declarative transaction management).

- **Spring Web:** Cung cấp WebApplicationContext và dịch vụ tích hợp với các framework web khác (như Struts).

- **Spring MVC:** Là nền tảng Web MVC Framework vững chắc để xây dựng ứng dụng web, hỗ trợ JSP, Velocity, FreeMarker....

---

## 2. Mô hình hoạt động của Spring MVC

Spring MVC cung cấp kiến trúc Model-View-Controller để phân tách các luồng công việc trong phát triển ứng dụng web.

- **Control:** Bao gồm `DispatcherServlet`, `Handler Mapping`, và các `Controller` thực hiện nhiệm vụ tiếp nhận và điều hướng các request.

- **Model:** Là các POJO, Service, Entities, DAO thực hiện truy cập cơ sở dữ liệu và xử lý logic nghiệp vụ (business logic).

- **View:** Là các file giao diện hiển thị cho người dùng như JSP, HTML, XML.

**Quy trình xử lý một Request trong Spring MVC diễn ra theo 4 bước:**

1. Client gửi HTTP Request đến `DispatcherServlet`. `DispatcherServlet` sau đó chuyển URL cho `Handler Mapping` để xác định action method cần gọi.

2. Gọi phương thức action tương ứng bên trong `Controller` để xử lý và nhận lại kết quả.

3. Chuyển kết quả xử lý cho bộ phận `ViewResolver` để xác định đường dẫn đầy đủ của View.

4. Gọi tới View (file giao diện tương ứng) để kết xuất kết quả thành HTTP Response và trả về cho Client.

---

## 3. Thiết lập môi trường và Tổ chức dự án

Để phát triển ứng dụng Spring MVC, môi trường cần có các công cụ bao gồm: JDK 7+, Eclipse for JavaEE, web server Tomcat 8x, và SQL Server 2008+.

**Tổ chức một thư mục dự án Spring MVC điển hình:**

- **src (Java Resources):** Là nơi đặt tất cả các file mã nguồn Java như các Controller (ví dụ: `HelloController.java`).

- **WebContent:** Là nơi chứa các thư mục tài nguyên web như scripts, styles, file ảnh và các trang giao diện (JSP).

- **WEB-INF/lib:** Là nơi bắt buộc phải đặt tất cả các file thư viện (`.jar`) của Spring framework và các thư viện liên quan khác.

- **WEB-INF:** Bao gồm file cấu hình ứng dụng (`web.xml`), thư mục cấu hình Spring MVC (`config/spring-config-mvc.xml`), và thư mục giao diện (`views/hello.jsp`).

---

## 4. Cấu hình dự án Spring MVC

Một dự án Spring MVC cần 2 file cấu hình cốt lõi dưới định dạng XML:

**1. File cấu hình hệ thống (`web.xml`):**

- **DispatcherServlet:** Tiếp nhận và điều phối mọi yêu cầu từ người dùng. Mọi URL có đuôi `.htm` đều được cấu hình để gửi vào Servlet này xử lý. Trong phần cấu hình `contextConfigLocation`, ta nạp tất cả các file `.xml` nằm trong thư mục `/WEB-INF/configs/` vào ứng dụng.

- **CharacterEncodingFilter:** Được khai báo để cho phép ứng dụng web xử lý và làm việc chuẩn xác với bảng mã UTF-8 (hỗ trợ hiển thị và nhập liệu tiếng Việt).

**2. File cấu hình Spring (`spring-config-mvc.xml`):**

- **ViewResolver:** Khai báo `InternalResourceViewResolver` và cấu hình tiền tố (`p:prefix="/WEB-INF/views/"`) và hậu tố (`p:suffix=".jsp"`). Khi Controller trả về tên một View, Spring sẽ tự động nối ghép lại (ví dụ: trả về `"hello"` -> hệ thống ánh xạ thành `/WEB-INF/views/hello.jsp`).

- **Quét gói Component:** Sử dụng thẻ `<context:component-scan base-package="poly.controller"/>` để chỉ cho Spring biết thư mục nào chứa các class Controller (sử dụng `@Controller`) để tự động nạp vào hệ thống.

---

## 5. Làm việc với các đối tượng Web

Trong môi trường Web, chúng ta thường làm việc với 4 đối tượng chia sẻ dữ liệu chính:

- **HttpServletRequest:** Dữ liệu gửi từ client và chia sẻ cho nhiều thành phần trong cùng một request.

- **HttpServletResponse:** Gói dữ liệu để trả về cho client.

- **HttpSession:** Phạm vi chia sẻ dữ liệu được duy trì trong suốt một phiên làm việc (session) của người dùng.

- **ServletContext:** Phạm vi chia sẻ dữ liệu rộng nhất, áp dụng trên toàn bộ ứng dụng.

Trong Spring MVC, để sử dụng các đối tượng như `HttpServletRequest`, `HttpServletResponse`, `HttpSession`, bạn chỉ cần khai báo chúng trực tiếp như một tham số của phương thức Action (ví dụ: `public String sayHello(HttpServletRequest request) {...}`).
Riêng với `ServletContext`, bạn nên sử dụng Annotation `@Autowired` để tham chiếu (inject) biến vào phạm vi toàn bộ Controller.

---

## 6. Truyền dữ liệu từ Controller sang View

Có 2 phương pháp để truyền dữ liệu sinh ra từ Controller sang View (giao diện JSP) để hiển thị:

1. **Sử dụng HttpServletRequest (kiểu truyền thống):**

- Truyền vào tham số `HttpServletRequest request` trong action method, sau đó gọi lệnh: `request.setAttribute("tên_biến", giá_trị)`.

2. **Sử dụng ModelMap (chuẩn tắc trong Spring MVC):**

- Khai báo tham số `ModelMap model` vào action method, sau đó gọi lệnh: `model.addAttribute("tên_biến", giá_trị)`.

**Cách lấy và hiển thị dữ liệu bên trang JSP:**

- Sử dụng biểu thức EL (Expression Language) với cú pháp `${tên_biến}` (Ví dụ: `${message}` hoặc `${name}`) để kết xuất giá trị ra HTML. Hoặc có thể dùng cú pháp JSP cũ là `<%=request.getAttribute("name")%>`.
