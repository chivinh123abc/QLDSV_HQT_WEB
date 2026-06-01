# Lập trình web Spring MVC - Bài 2: Controller

Bài học này giúp bạn nắm vững cách sử dụng annotation `@RequestMapping`, phân biệt các phương thức HTTP (GET/POST), xử lý tham số từ người dùng và hiểu rõ các loại kết quả trả về của phương thức action.

## 1. Sử dụng Annotation `@RequestMapping`

Annotation `@RequestMapping` được sử dụng để ánh xạ (map) một yêu cầu (request) từ người dùng đến một phương thức action cụ thể trong Controller.

- **Ánh xạ cơ bản:** Cú pháp `@RequestMapping("say-hello")` là cách viết thu gọn của `@RequestMapping(value="say-hello")`. Khi người dùng gọi URL `say-hello.htm`, phương thức tương ứng sẽ được thực thi.

- **Ánh xạ cấp độ Class (Lớp):** `@RequestMapping` có thể được đặt trên đầu khai báo lớp Controller để thiết lập một đường dẫn chung cho tất cả các action bên trong lớp đó. Ví dụ: Nếu lớp cấu hình `@RequestMapping("/home/")` và phương thức cấu hình `@RequestMapping("index")`, thì URL truy cập sẽ là `home/index.htm`.

---

## 2. Phân biệt phương thức POST và GET

Trong lập trình web, POST và GET là hai phương thức của giao thức HTTP dùng để gửi dữ liệu về server xử lý. Trước khi gửi, dữ liệu được mã hóa thành các cặp name/value.

### So sánh chi tiết GET và POST

| Đặc điểm                    | Phương thức GET                                                                                  | Phương thức POST                                                            |
| --------------------------- | ------------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------- |
| **Cách truyền dữ liệu**     | Gửi thông qua đường dẫn URL nằm trên thanh địa chỉ của trình duyệt, dữ liệu nằm sau dấu hỏi (?). | Truyền thông tin thông qua HTTP header, ẩn bên trong HTML form.             |
| **Bảo mật**                 | Không bảo mật (không nên dùng để gửi password hoặc thông tin nhạy cảm).                          | Bảo mật hơn do dữ liệu bị ẩn, đặc biệt an toàn khi kết hợp giao thức HTTPS. |
| **Giới hạn dữ liệu**        | Giới hạn tối đa 2048 ký tự và không gửi được dữ liệu nhị phân.                                   | Không hạn chế kích thước, hỗ trợ cả dữ liệu ASCII và nhị phân.              |
| **Lưu trữ (Cache/History)** | Có thể được cache bởi trình duyệt, lưu trong lịch sử web và có thể bookmark.                     | Không được cache, không lưu trong lịch sử và không thể bookmark.            |
| **Tốc độ thực thi**         | Nhanh hơn (do thường được lấy từ cache nếu trùng yêu cầu).                                       | Chậm hơn (server luôn phải thực thi và xử lý lại yêu cầu).                  |
| **Trường hợp tải lại (F5)** | Có thể dễ dàng tải lại trang.                                                                    | Trình duyệt sẽ hiển thị hộp thoại cảnh báo trước khi gửi lại dữ liệu form.  |

### Ứng dụng trong Spring MVC

- Bạn có thể phân biệt hành động thông qua tham số `method` của `@RequestMapping`.

- Cú pháp: `@RequestMapping(value="login", method=RequestMethod.GET)` hoặc `method=RequestMethod.POST`.

- **Thực tiễn:** Thông thường, phương thức GET được dùng để hiển thị giao diện, còn POST được dùng để xử lý dữ liệu (ví dụ: các nút chức năng submit form).

---

## 3. Phân biệt ánh xạ theo Tham số

Spring MVC không chỉ phân biệt phương thức qua URL hay GET/POST, mà còn cho phép phân biệt dựa trên tham số truyền kèm.

- **Cú pháp:** `@RequestMapping(value="say-hello", params="mvc")`.

- **Ý nghĩa:** Phương thức này chỉ được gọi nếu URL có chứa tham số `mvc` (ví dụ: `say-hello.htm?mvc`).

- **Ứng dụng:** Rất hữu ích khi một form có nhiều nút bấm (Thêm, Sửa, Xóa). Bạn có thể gán các nút này chung một URL nhưng định tuyến vào các phương thức khác nhau dựa trên tham số nút được nhấn (ví dụ: `params="btnInsert"`, `params="btnUpdate"`).

---

## 4. Các phương pháp nhận Tham số từ người dùng

Tham số là dữ liệu truyền từ client (form hoặc URL) đến server. Spring MVC hỗ trợ 4 cách chính để nhận dữ liệu:

### 4.1. Sử dụng `HttpServletRequest`

- Hoạt động tương tự như Servlet truyền thống. Bạn chỉ cần thêm `HttpServletRequest request` vào tham số của phương thức action.

- Lấy giá trị thông qua lệnh: `request.getParameter("tên_tham_số")`.

### 4.2. Sử dụng `@RequestParam`

- Thể hiện tính chuyên nghiệp và hỗ trợ tự động ép kiểu dữ liệu.

- **Cú pháp đầy đủ:** `@RequestParam(value="tên_tham_số", defaultValue="giá_trị_mặc_định", required=true/false) Kiểu_Dữ_Liệu tên_biến`.

- Trong đó: `value` là tên tham số gửi lên, `defaultValue` dùng khi tham số không tồn tại, và `required` quy định tham số đó có bắt buộc hay không.

### 4.3. Sử dụng JavaBean

- Để sử dụng, lớp Bean phải là `public`, có constructor không tham số và cấu trúc đọc/ghi dữ liệu qua `getter/setter`.

- Các thuộc tính của bean được xác định dựa trên tên của getter/setter (bỏ từ khóa get/set và viết thường chữ cái đầu).

- Bạn chỉ cần đặt đối tượng Bean làm tham số của phương thức. Spring MVC sẽ tự động nhận các tham số gửi lên có tên trùng khớp với các thuộc tính của Bean và gán giá trị tương ứng.

### 4.4. Sử dụng `@PathVariable`

- Cho phép lấy trực tiếp một phần dữ liệu ngay trên đường dẫn URL (ví dụ: `student/Nguyễn Văn Tèo.htm`).

- **Cú pháp:** Đặt biến trong URL bằng cặp ngoặc nhọn `@RequestMapping(value="/{name}")` và lấy ra bằng đối số `@PathVariable("name") String name`.

---

## 5. Session và Cookie (Sử dụng `@CookieValue`)

- **Phân biệt cơ bản:** Session là phiên làm việc lưu trữ ở phía Server và kết thúc khi đóng trình duyệt, an toàn hơn. Cookie lưu trữ ở phía Client (trình duyệt) và tồn tại cho đến khi hết hạn, dễ bị đánh cắp/sửa đổi.

- **Nhận Cookie trong Spring MVC:** Thay vì dùng `HttpServletRequest` phức tạp, bạn dùng annotation `@CookieValue` trực tiếp trên đối số của hàm.

- **Cú pháp:** `@CookieValue(value="tên_cookie", defaultValue="giá_trị_mặc_định", required=true/false) String biến`.

---

## 6. Đầu ra của phương thức Action

Giá trị trả về (`return`) của phương thức action không chỉ giới hạn là tên của một View. Có 3 trường hợp xảy ra:

1.  **Trả về Tên View:** Cú pháp `return "tên_view";`. ViewResolver sẽ tiếp nhận và xác định đường dẫn đầy đủ của file giao diện (ví dụ: `/WEB-INF/views/hello.jsp`).

2.  **Trả về Nội dung trực tiếp:** Sử dụng kết hợp với annotation `@ResponseBody` ngay trên phương thức. Nội dung chuỗi trả về sẽ được hiển thị trực tiếp lên trình duyệt (client) mà không cần qua ViewResolver. Cách này thường dùng để trả về dữ liệu định dạng JSON, JavaScript, hoặc XML.

3.  **Gọi một Action khác (Chuyển hướng):** Sử dụng cú pháp `return "redirect:/đường_dẫn";` để chuyển hướng yêu cầu tới một URL action khác trong hệ thống.
