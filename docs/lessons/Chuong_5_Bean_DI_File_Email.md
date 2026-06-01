# Lập trình web Spring MVC - Bài 5: Bean và Dependency Injection, Upload File & Gửi Email

## 1. Dependency Injection (DI) và Quản lý Bean

Dependency Injection (DI) là một kỹ thuật cốt lõi trong Spring giúp quản lý sự phụ thuộc giữa các đối tượng.

- **Khái niệm cơ bản:** DI là cách truyền một module vào một module khác thông qua cấu hình XML hoặc viết mã, dưới sự hỗ trợ của DI container.

- **Vai trò của Spring:** Spring framework trang bị sẵn một DI container mạnh mẽ giúp thực hiện DI một cách dễ dàng.

- **Mục đích:** Kỹ thuật này làm giảm sự phụ thuộc chặt chẽ giữa các module, giúp hệ thống dễ dàng thay đổi module, bảo trì code và thực hiện testing hiệu quả hơn.

### Các hình thức Injection (Tiêm Bean)

Sau khi Bean được khai báo, nó sẽ được DI container tạo ra lúc khởi động và có thể được "tiêm" vào các Controller hoặc thành phần khác thông qua Annotation `@Autowired`. Có 3 hình thức tiêm chính:

1.  **Tiêm vào trường (Field Injection):** Đặt `@Autowired` trực tiếp ngay trên khai báo biến (ví dụ: `@Autowired Company company;`).

2.  **Tiêm qua Constructor:** Khai báo đối tượng trong tham số của hàm khởi tạo và đặt `@Autowired` trên hàm đó.

3.  **Tiêm qua Setter:** Đặt `@Autowired` trên phương thức Setter của thuộc tính.

### Cách Spring nhận diện Bean

- **Dựa vào kiểu dữ liệu:** Mặc định, `@Autowired` sẽ tự động tìm và tiêm Bean thông qua kiểu dữ liệu tương ứng.

- **Sử dụng `@Qualifier`:** Khi có nhiều Bean cùng một kiểu dữ liệu, hệ thống sẽ không biết phải tiêm Bean nào. Lúc này, bạn cần kết hợp thêm `@Qualifier("id_của_bean")` để chỉ định chính xác Bean cần tiêm thông qua ID.

### Khai báo Bean

- **Khai báo tự động:** Các lớp Java được đánh dấu bằng annotation `@Component`, `@Service`, hoặc `@Repository` sẽ tự động được khai báo thành Bean.

- **Cấu hình quét gói:** Để Spring tìm thấy các Bean tự khai báo này, bạn bắt buộc phải cấu hình thẻ `<context:component-scan base-package="tên_gói"/>` trong file cấu hình XML, dùng dấu phẩy để phân cách nếu có nhiều package.

---

## 2. Xử lý Upload File lên Server

Upload file là chức năng rất phổ biến (như tải ảnh đại diện, nộp tài liệu). Để thực hiện trong Spring MVC, bạn cần tuân thủ các cấu hình sau:

### Cấu hình thư viện và Bean

- **Thư viện:** Bắt buộc phải có 2 file thư viện là `commons-fileupload-1.2.2.jar` và `commons-io-1.3.2.jar`.

- **Khai báo Bean:** Khai báo bean `CommonsMultipartResolver` (với id bắt buộc là `multipartResolver`) trong file cấu hình. Bạn có thể cấu hình thuộc tính `maxUploadSize` để giới hạn dung lượng (mặc định là 2MB, ví dụ cấu hình `20971520` tương đương 20MB).

### Thiết kế Form HTML

- Form upload file bắt buộc phải có hai thuộc tính: `method="POST"` và `enctype="multipart/form-data"`.

### Xử lý logic tại Controller

Tại Controller, sử dụng `@RequestParam("tên_thẻ_input") MultipartFile biến` để nhận file. Lớp `MultipartFile` cung cấp các API quan trọng:

| Phương thức API         | Công dụng                                                    |
| ----------------------- | ------------------------------------------------------------ |
| `isEmpty()`             | Kiểm tra xem có file upload lên hay không.                   |
| `getOriginalFilename()` | Lấy tên file gốc từ máy người dùng.                          |
| `transferTo(File)`      | Chuyển và lưu file đến một đường dẫn vật lý mới trên server. |
| `getContentType()`      | Lấy kiểu định dạng của file (MIME type).                     |
| `getSize()`             | Lấy kích thước của file.                                     |
| `getBytes()`            | Lấy trực tiếp nội dung file dưới dạng mảng byte.             |
| `getInputStream()`      | Lấy luồng dữ liệu đầu vào để đọc file.                       |

---

## 3. Gửi Email với Spring MVC

Gửi email ứng dụng cho nhiều chức năng (kích hoạt tài khoản, báo đơn hàng, quên mật khẩu...). Spring cung cấp sẵn bean `JavaMailSender` giúp việc này trở nên vô cùng thuận tiện.

### Cấu hình và Chuẩn bị

- **Thư viện:** Cần thêm `mail.jar` và `activation.jar`.

- **Tài khoản SMTP:** Trong thực tế học tập, chúng ta dùng Gmail đóng vai trò SMTP Server. Bạn cần đăng nhập tài khoản Google, tắt chức năng xác minh 2 bước (hoặc tạo Mật khẩu ứng dụng - App Password gồm 16 ký tự) để cấp quyền cho ứng dụng Java gửi mail.

- **Khai báo Bean:** Cấu hình bean `JavaMailSenderImpl` với id `mailSender`, khai báo cổng 465, username/password (mật khẩu ứng dụng) và cấu hình SSL cho an toàn.

### Lập trình gửi Mail

Bằng cách tiêm `@Autowired JavaMailSender mailer;` vào Controller, bạn có thể tạo và gửi mail:

1.  **Tạo đối tượng Mail:** Dùng `mailer.createMimeMessage()` để tạo một email.

2.  **Sử dụng MimeMessageHelper:** Lớp trợ giúp này giúp thiết lập các thành phần của mail dễ dàng hơn:

| Phương thức của MimeMessageHelper  | Công dụng                                                                                 |
| ---------------------------------- | ----------------------------------------------------------------------------------------- |
| `setFrom(email, name)`             | Cung cấp thông tin người gửi.                                                             |
| `setTo(email)`                     | Cài đặt địa chỉ email người nhận.                                                         |
| `setCc(emails)` / `setBcc(emails)` | Danh sách những người cùng nhận (hiển thị / ẩn danh).                                     |
| `setReplyTo(email, name)`          | Cài đặt địa chỉ nhận phản hồi.                                                            |
| `setSubject(subject)`              | Gắn tiêu đề cho email.                                                                    |
| `setText(body, isHtml)`            | Cài đặt nội dung email (cho phép định dạng HTML nếu `true`).                              |
| `addAttachment(name, file)`        | Đính kèm file vào mail (Cần upload file lên server trước rồi mới lấy đường dẫn đính kèm). |

3.  **Thực thi:** Gọi lệnh `mailer.send(mail)` để gửi thư đi.

4.  **Tối ưu hóa:** Để code gọn gàng, bạn có thể tự xây dựng một bean `@Service("mailer")` (lớp `Mailer`) bọc các hàm `send(...)` lại nhằm tái sử dụng mã một cách linh hoạt tại mọi Controller trong hệ thống.
