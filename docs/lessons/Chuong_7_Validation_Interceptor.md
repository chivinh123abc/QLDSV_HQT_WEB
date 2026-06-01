# Lập trình web Spring MVC - Bài 7: Validation & Interceptor

## 1. Tầm quan trọng của Validation (Kiểm lỗi)

Dữ liệu đầu vào không hợp lệ sẽ gây ra các lỗi khó lường và ảnh hưởng đến tính toàn vẹn của hệ thống. Vì vậy, việc kiểm soát dữ liệu trước khi xử lý đóng vai trò đặc biệt quan trọng.

- **Các lỗi thường gặp khi người dùng nhập liệu:**
- Để trống ô nhập (Họ tên, Mật khẩu...).
- Không đúng định dạng (Email, số điện thoại, thẻ tín dụng, URL...).
- Sai kiểu dữ liệu (Nhập chữ vào ô số nguyên/số thực, sai định dạng ngày giờ...).
- Vi phạm giới hạn (Nhỏ hơn giá trị tối thiểu, lớn hơn giá trị tối đa, ngoài phạm vi từ 0-10...).
- Lỗi logic nghiệp vụ (Nhập lại mật khẩu không khớp, sai mã captcha, trùng lặp mã sinh viên/username đã tồn tại...).

---

## 2. Kiểm lỗi bằng tay (Manual Validation)

Đây là phương pháp lập trình viên tự viết các câu lệnh điều kiện để kiểm tra và chủ động gán lỗi nếu dữ liệu vi phạm.

- **Sử dụng đối tượng Errors/BindingResult:** Tại Controller, bạn phải đặt tham số `BindingResult` (hoặc `Errors`) **ngay sát phía sau** đối tượng Bean nhận dữ liệu form.
- **Hàm `rejectValue()`:** Dùng để ghi nhận một lỗi cho một thuộc tính cụ thể.
- Cú pháp: `errors.rejectValue("tên_thuộc_tính", "tên_thuộc_tính", "Thông báo lỗi tiếng Việt");`

- **Kiểm tra trạng thái:** Dùng phương thức `errors.hasErrors()` để kiểm tra xem có bất kỳ lỗi nào vừa được ghi nhận hay không. Nếu có (`true`), bạn trả về lại View chứa form để người dùng tiến hành nhập lại.

---

## 3. Hiển thị thông báo lỗi trên View (JSP)

Để hiển thị các lỗi đã được `rejectValue()` bắt được, bạn thao tác trên giao diện JSP có sử dụng thẻ Spring Form:

- **Thẻ hiển thị lỗi:** Dùng thẻ `<form:errors path="tên_thuộc_tính" element="span"/>`. Mặc định thuộc tính `element` dùng để bọc dòng thông báo lỗi là thẻ `<span>`.
- **Định dạng bằng CSS (CSS Selector):** Spring tự động sinh ra một thuộc tính ID cho thông báo lỗi với cấu trúc `{thuộc_tính}.errors` (ví dụ: `id="name.errors"`). Bạn có thể định dạng hàng loạt thông báo lỗi bằng CSS thông minh:

```css
*[id$=".errors"] {
  /* Chọn tất cả các id có kết thúc bằng chuỗi .errors */
  color: red;
  font-style: italic;
}
```

---

## 4. Kiểm lỗi bằng Annotation (Tự động hóa)

Thay vì code thủ công dài dòng, bạn có thể thiết lập luật kiểm tra trực tiếp trên khai báo thuộc tính của class Bean bằng các Annotation. Khung làm việc (Spring & Hibernate Validator) sẽ tự động làm phần việc còn lại.

- **Các Annotation kiểm lỗi phổ biến:**
- `@NotBlank`: Chuỗi không được để rỗng hoặc chỉ chứa khoảng trắng.
- `@NotNull`: Dữ liệu không được `null`.
- `@DecimalMin(value)` / `@DecimalMax(value)`: Kiểm tra miền giá trị cho các kiểu số.
- `@Email`: Kiểm tra tính hợp lệ của định dạng thư điện tử.
- `@Pattern(regexp="...")`: Kiểm tra dữ liệu khớp với một biểu thức chính quy (Regex).

- **Kích hoạt tại Controller:** Tại action method, bạn chỉ cần bổ sung annotation `@Validated` (hoặc `@Valid`) ngay phía trước tham số Bean. Các thuộc tính của bean sẽ tự động được kiểm tra theo luật, và nếu có lỗi, chúng sẽ tự động được đẩy vào đối tượng `Errors`.

---

## 5. Cơ chế và Ý nghĩa của Interceptor

Interceptor (Bộ đánh chặn) hoạt động như một màng lọc bảo vệ các Controller, đứng ở giữa Request và Controller.

- **Mục đích:** Khi có những đoạn logic cần áp dụng chung cho nhiều Request (như kiểm tra xem người dùng đã đăng nhập chưa trước khi cho phép mua hàng), thay vì phải copy-paste code vào từng Action method, bạn chỉ cần viết code 1 lần ở Interceptor.
- **Hoạt động:** Nó cho phép bạn can thiệp vào quá trình xử lý trước khi Request đến Controller (`preHandle`), sau khi Controller xử lý xong nhưng View chưa hiển thị (`postHandle`), hoặc sau khi View đã hiển thị hoàn tất (`afterCompletion`).

---

## 6. Xây dựng và Cấu hình Interceptor

Để tạo một Interceptor, bạn tạo một lớp Java kế thừa từ lớp `HandlerInterceptorAdapter` (hoặc implement Interface `HandlerInterceptor`).

**1. Code Class Interceptor (Ví dụ: SecurityInterceptor):**
Bạn sẽ ghi đè (override) phương thức `preHandle()`. Phương thức này trả về kiểu `boolean`:

- Nếu return `true`: Cho phép request đi tiếp tới Controller.
- Nếu return `false`: Chặn request lại (thường kết hợp với hàm điều hướng `response.sendRedirect(...)`).

**2. Cấu hình trong `spring-config-mvc.xml`:**
Bạn khai báo quy tắc đánh chặn bằng thẻ `<mvc:interceptors>`:

- `<mvc:mapping path="/url/"/>`: Báo cho Spring biết đường dẫn nào bị Interceptor này chặn lại kiểm tra.
- `<mvc:exclude-mapping path="/url/ngoai-le.htm"/>`: Khai báo những đường dẫn ngoại lệ không cần qua màng lọc (ví dụ: các action như `/user/login.htm` hay `/user/register.htm` sẽ được cho phép đi qua tự do).
- `<bean class="..."/>`: Chỉ ra đường dẫn đến class Interceptor mà bạn vừa code.

---

## 7. Ứng dụng bảo vệ tài nguyên riêng tư (Authorization)

Một ứng dụng kinh điển của Interceptor là bảo mật các URL nội bộ của hệ thống (như quản lý thông tin tài khoản, đặt hàng).

- **Logic thực thi trong `preHandle()`:**

1. Lấy session hiện tại bằng `request.getSession()`.
2. Kiểm tra Attribute đăng nhập (ví dụ `session.getAttribute("user")`).
3. Nếu bằng `null` (khách vãng lai, chưa đăng nhập): Gọi lệnh `response.sendRedirect(...)` đẩy người dùng về trang đăng nhập `/user/login.htm`, rồi `return false`.
4. Nếu khác `null` (người dùng hợp lệ): `return true` để cho phép thao tác truy cập dữ liệu diễn ra bình thường.
