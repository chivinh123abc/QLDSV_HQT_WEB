# Lập trình web Spring MVC - Bài 8: Tổ chức giao diện

## Phần 1: Tổ chức giao diện Đa ngôn ngữ (i18n)

Mục tiêu của phần này là xây dựng một website có khả năng chuyển đổi qua lại giữa các ngôn ngữ khác nhau (ví dụ: Tiếng Anh và Tiếng Việt). Nguyên tắc hoạt động dựa trên việc tách nội dung văn bản ra khỏi giao diện, lưu vào các file tài nguyên, và sử dụng `LocaleResolver` để hỗ trợ lựa chọn tài nguyên theo ngôn ngữ hiện tại.

Để xây dựng website đa ngôn ngữ trong Spring MVC, bạn cần thực hiện 4 bước:

### Bước 1: Tổ chức các file tài nguyên (.properties)

- Tạo một thư mục (ví dụ `i18n-res`) trong `src` để chứa các file tài nguyên.

- Phân tách các file theo từng view hoặc layout chung (ví dụ: `global.properties`, `home.index.properties`).

- Tạo các file phiên bản ngôn ngữ khác bằng cách thêm đuôi mã ngôn ngữ vào tên file. Ví dụ: File tiếng Việt sẽ có đuôi `_vi` (như `global_vi.properties`, `home.index_vi.properties`).

- **Lưu ý quan trọng:** Các file tài nguyên này cần được thiết lập ở chế độ lưu **UTF-8** để có thể gõ và hiển thị tiếng Việt có dấu chính xác.

- Cấu trúc dữ liệu trong file là các cặp `key=value` (Ví dụ: `global.menu.home=Trang chủ`).

### Bước 2: Hiển thị nội dung lên giao diện (JSP)

Để gọi các thông điệp từ file tài nguyên lên JSP, bạn thực hiện 2 thao tác:

1. Khai báo thư viện thẻ Spring ở đầu trang: `<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>`
2. Sử dụng thẻ `<s:message>` kết hợp với tham số `code` (chính là Key trong file properties) để xuất nội dung: `<s:message code="home.about.title"/>`.

### Bước 3: Thực hiện cấu hình Spring

Trong file cấu hình Spring (thường là `spring-config-mvc.xml`), bạn cần khai báo 3 Bean cốt lõi sau:

- **`ReloadableResourceBundleMessageSource`**: Dùng để khai báo và nạp danh sách các file tài nguyên (không cần ghi phần mở rộng `.properties`).
- **`CookieLocaleResolver`**: Dùng để duy trì và ghi nhớ ngôn ngữ người dùng đã chọn thông qua Cookie (để lần sau truy cập website vẫn giữ nguyên ngôn ngữ đó). Khai báo thêm thuộc tính `cookieMaxAge` để định thời gian lưu trữ.

- **`LocaleChangeInterceptor`**: Bộ đánh chặn dùng để nhận biết sự thay đổi ngôn ngữ thông qua một tham số truyền lên URL (ví dụ: tham số `paramName="language"`).

### Bước 4: Lập trình điều khiển chọn ngôn ngữ

Trên giao diện, bạn tạo các liên kết hoặc nút bấm chuyển ngôn ngữ (VD: English | Tiếng Việt).
Phương pháp đơn giản nhất là sử dụng thư viện **jQuery/AJAX** để bắt sự kiện click:

- Sử dụng `$.get(url)` gọi đến một action (ví dụ: `home/index.htm?language=vi`) truyền theo tham số ngôn ngữ mới.

- Sau khi lời gọi AJAX thành công, dùng lệnh `location.reload()` để tải lại toàn bộ trang web hiện tại với các tài nguyên ngôn ngữ vừa được cập nhật.

---

## Phần 2: Chèn CKEditor (Trình soạn thảo văn bản) vào Website

CKEditor là một thư viện hỗ trợ biến thẻ `<textarea>` thông thường thành một khung soạn thảo văn bản giàu định dạng (như Word).

**Các bước thực hiện:**

1. Download bản `ckeditor_4.16.0_full` từ trang chủ `ckeditor.com`.

2. Giải nén và copy toàn bộ thư mục `ckeditor` vào thư mục `resources` của dự án.

3. Chèn đoạn mã Script gọi thư viện vào phần Header (`<head>`) của trang web:

`<script src="<c:url value='/resources/ckeditor/ckeditor.js'/>"></script>`.

4. Tạo thẻ `<textarea>` và gắn một thẻ `id` riêng biệt cho nó.

5. Chèn một đoạn mã JavaScript ở cuối trang (gọi lệnh `CKEDITOR.replace('id_của_textarea')`) để khởi tạo trình soạn thảo thay thế cho thẻ textarea.

---

## Phần 3: Chèn CKFinder (Trình quản lý File/Ảnh) vào Website

CKFinder thường được tích hợp chung với CKEditor để cho phép người dùng upload ảnh và quản lý file trực tiếp từ máy tính lên server.

**Các bước cấu hình tích hợp:**

1. Download gói `ckfinder_java_2.6.3` từ trang chủ `ckeditor.com/ckfinder`.

2. Giải nén và copy thư mục `ckfinder` vào trong thư mục `resources` của dự án web.

3. Cấu hình file `web.xml` (trong thư mục `WEB-INF`) bằng cách khai báo một `Servlet` mang tên `ConnectorServlet`. Cấu hình URL Pattern của servlet này trỏ tới: `/resources/ckfinder/core/connector/java/connector.java`. Đồng thời cấp tham số `XMLConfig` trỏ đường dẫn tới file cấu hình là `/WEB-INF/configs.xml`.

4. Copy file `config.xml` (từ thư mục WEB-INF của file tải về) bỏ vào thư mục `/WEB-INF/` của project.

5. **Cực kỳ quan trọng:** Copy tất cả các thư viện file `.jar` có trong thư mục `WEB-INF/lib` của gói CKFinder vừa tải về, và dán chúng vào thư mục `WEB-INF/lib` của dự án. (Bao gồm các thư viện như: `activation-1.1`, `CKFinder-2.6.3`, các Plugins của CKFinder, `commons-fileupload`, `commons-io`, `gson`, `mail`, `thumbnailator`, v.v...) .
