# Lập trình web Spring MVC - Bài 3: Làm việc với Form

## 1. Tổng quan về Databinding (Buộc dữ liệu)

Databinding là cơ chế kết nối tự động dữ liệu của bean (đặt trong model) đến các điều khiển (controls) trên form giao diện. Nguyên tắc hoạt động của nó là khi thay đổi dữ liệu trong bean thì dữ liệu hiển thị trên các điều khiển cũng thay đổi theo, và ngược lại.

**Ràng buộc dữ liệu hoạt động theo 2 chiều:**

- **Chiều lên:** Chuyển dữ liệu từ các điều khiển trên form HTML vào các thuộc tính của bean trong Java.

- **Chiều về:** Hiển thị dữ liệu từ các thuộc tính của bean lên các điều khiển của form HTML.

**Hạn chế của việc buộc dữ liệu bằng HTML và EL truyền thống:**

- Phải viết mã trên giao diện rất dài dòng và khó quản lý.

- Việc đổ dữ liệu vào các List Control (như Combobox, Checkbox, Radio) rất phức tạp và khó khăn.

- Gặp khó khăn trong việc kiểm tra và thông báo lỗi (validation).

---

## 2. Thư viện thẻ Spring Form

Để khắc phục những hạn chế của HTML thông thường, Spring MVC cung cấp một thư viện thẻ (taglib) riêng giúp việc buộc dữ liệu từ bean vào form trở nên vô cùng dễ dàng.

- **Khai báo thư viện (ở đầu file JSP):** `<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>`.

- **Cú pháp khai báo Form cơ bản:**

`<form:form action="url" modelAttribute="tên_bean">`.

### Bảng các điều khiển Form của Spring

| Thẻ Spring Form                          | Chức năng / Tương đương HTML    | Ghi chú                                    |
| ---------------------------------------- | ------------------------------- | ------------------------------------------ |
| `<form:form>`                            | Sinh ra thẻ `<form>` trong HTML | Yêu cầu thuộc tính `modelAttribute`        |
| `<form:input path="..."/>`               | Thẻ `<input type="text">`       | <br>`path` trỏ đến tên thuộc tính của bean |
| `<form:password path="..."/>`            | Thẻ `<input type="password">`   |                                            |
| `<form:textarea path="..."/>`            | Thẻ `<textarea>`                |                                            |
| `<form:hidden path="..."/>`              | Thẻ `<input type="hidden">`     |                                            |
| `<form:button>`                          | Thẻ `<button>`                  |                                            |
| `<form:checkbox>` / `<form:radiobutton>` | Checkbox hoặc Radio đơn lẻ      |                                            |
| `<form:select>`                          | Combobox / Dropdown list        | List Control                               |
| `<form:checkboxes>`                      | Nhóm nhiều checkbox             | List Control cần cấp Collection/Map        |
| `<form:radiobuttons>`                    | Nhóm nhiều radio buttons        | List Control cần cấp Collection/Map        |

**Ưu điểm vượt trội của Spring Form:**

- Cung cấp cơ chế buộc dữ liệu lên các điều khiển một cách rõ ràng, đơn giản.

- Dữ liệu đồng bộ 2 chiều tự động.

- Cấp dữ liệu vào các List Control trở nên cực kỳ đơn giản.

- Kiểm tra và hiển thị thông báo lỗi dễ dàng.

---

## 3. Sử dụng List Control và Đổ dữ liệu

Đối với các thẻ cho phép người dùng chọn từ một danh sách (Combobox, RadioButtons, Checkboxes), chúng ta cần chuyển từ việc nhập tay sang đổ dữ liệu tự động. Để làm được điều này:

1.  **Tại Controller:** Phải cung cấp dữ liệu dạng `Array`, `Collection` (như List), hoặc `Map` vào model.

2.  **Tại View (JSP):** Thay thế điều khiển cũ và cấu hình các thuộc tính ánh xạ dữ liệu.

**Cú pháp đổ dữ liệu vào List Control (Ví dụ với Select):**

`<form:select path="property" items="${items}" itemValue="prop1" itemLabel="prop2"/>`

- **`items`**: Chỉ ra tập dữ liệu (Collection, Map, Array) đang đặt trong Model để đổ vào điều khiển.

- **`itemValue`**: Chỉ ra tên thuộc tính của object dùng làm giá trị (value) ngầm định mang tính hệ thống.

- **`itemLabel`**: Chỉ ra tên thuộc tính của object dùng làm nhãn (label) để hiển thị cho người dùng nhìn thấy.
  (Lưu ý: `itemValue` và `itemLabel` chỉ được sử dụng khi tập `items` là một Collection chứa các Object phức tạp ).

---

## 4. Annotation `@ModelAttribute`

Trong Spring MVC, `@ModelAttribute` là một công cụ đắc lực để bổ sung attribute vào model và thường được dùng trong 2 trường hợp cụ thể:

### A. Đặt trên tham số của phương thức Action (`@ModelAttribute(name) argument`)

- **Chức năng:** Tự động lấy dữ liệu từ form chuyển vào các thuộc tính của đối số, sau đó bổ sung một attribute vào model với tên được chỉ định.

- **Tương đương lệnh:** `model.addAttribute(name, argument)`.

- **Tác dụng:** Attribute này sẽ lập tức buộc dữ liệu lên lại các điều khiển khi hệ thống quay trở lại form (ví dụ khi bị lỗi cần nhập lại).

### B. Đặt trên một phương thức độc lập (`@ModelAttribute(name) method`)

- **Chức năng:** Sẽ thực thi phương thức, lấy kết quả trả về của phương thức đó và tự động bổ sung vào model với tên attribute được chỉ định.

- **Tương đương lệnh:** `model.addAttribute(name, method())`.

- **Ứng dụng phổ biến:** Rất hữu ích để tạo các danh sách (List, Map, Array) nhằm cung cấp dữ liệu chung cho các List Control (như danh sách chuyên ngành, danh sách tỉnh thành) hiển thị trên giao diện.

---

## 5. Các thuộc tính thường dùng khác của Form Tags

Bên cạnh `path` và `items`, các thẻ Spring Form còn hỗ trợ một số thuộc tính bổ sung để tinh chỉnh giao diện tương tự HTML:

- **`cssClass`**: Hoạt động thay thế cho thuộc tính `class` trong HTML thông thường (ví dụ: `cssClass="form-control"`).

- **`disabled`**: Vô hiệu hóa điều khiển, thay cho thuộc tính `disabled` trong HTML.

- **`readonly`**: Chỉ cho phép đọc, thay cho thuộc tính `readonly` trong HTML (ví dụ: `readonly="true"`).

- **`cssErrorClass`**: Cung cấp class CSS định dạng riêng để làm nổi bật điều khiển khi có lỗi xảy ra (validation).
