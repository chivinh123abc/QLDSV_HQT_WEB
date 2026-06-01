# Lập trình web Spring MVC - Bài 4: EL và JSTL

Mục tiêu chính của bài học này là giúp bạn nắm vững kỹ thuật lập trình giao diện (UI) trong JSP thông qua hai công cụ mạnh mẽ: Expression Language (EL) và Java Standard Tag Library (JSTL).

## 1. Expression Language (EL)

EL (Expression Language) được giới thiệu từ phiên bản JSP 2.0, mang đến sự rút ngắn tuyệt vời trong việc viết mã để làm việc với các attribute đặt trong các scope (phạm vi) khác nhau.

- **Cú pháp cơ bản:** `${<biểu thức>}`.

- **Chức năng:** Kết xuất giá trị của biểu thức tại vị trí đặt nó. Biểu thức có thể chứa attribute, tham số (parameter), cookie, hoặc header.

- **Ví dụ tính toán:** `${salary * 2}` sẽ nhân đôi giá trị của attribute `salary` và hiển thị ra màn hình.

### Các cách truy xuất dữ liệu bằng EL

**1. Truy xuất Attribute trong các Scope**
Trong JSP có 4 scope: Page, Request, Session, và Application. Trình tự tìm kiếm mặc định của EL (ví dụ: `${message}`) là: `pageScope` -> `requestScope` -> `sessionScope` -> `applicationScope`. Nếu tìm thấy sẽ dừng lại, nếu không sẽ trả về rỗng. Bạn có thể chỉ định rõ scope:

- `${pageScope['x']}` hoặc `${pageScope.x}`

- `${requestScope['x']}` hoặc `${requestScope.x}`

- `${sessionScope['x']}` hoặc `${sessionScope.x}`

- `${applicationScope['x']}` hoặc `${applicationScope.x}`

**2. Truy xuất Thuộc tính của JavaBean**
Để EL gọi được, lớp Bean phải là `public`, có constructor mặc định và có các hàm getter/setter.

- **Cú pháp:** `${bean.property}`.

- **Ý nghĩa:** Cú pháp này tương đương với việc gọi phương thức `bean.getProperty()` trong Java. (Ví dụ: `${student.mark}` xuất ra kết quả của `student.getMark()[cite_start]`).

**3. Truy xuất Mảng (Array), Tập hợp (Collection) và Map**

- **Với Mảng/Collection:** Sử dụng chỉ số (index) để truy xuất phần tử.

- **Với Map:** Sử dụng key để truy xuất theo 2 cách: `${map['key']}` hoặc `${map.key}`. (Ví dụ: `${student['name']}` hoặc `${student.mark}`) .

**4. Truy xuất Parameter và Cookie**

- **Tham số (Parameter):** `${param[<tên tham số>]}` hoặc `${param.<tên tham số>}`. (Ví dụ: `${param.salary}`) .

- **Cookie:** `${cookie[<tên cookie>].value}` hoặc `${cookie.<tên cookie>.value}`.

---

## 2. Java Standard Tag Library (JSTL)

JSTL là bộ thư viện thẻ chuẩn được bổ sung để tối ưu hóa việc lập trình giao diện JSP. Để sử dụng, dự án cần có hai file `.jar` là `jstl-api.jar` và `jstl-impl.jar`.

JSTL được chia thành nhiều bộ thư viện phục vụ các mục đích khác nhau: Core (điều khiển), Format (định dạng), Xml, Sql, và Function (hàm hỗ trợ). Khóa học tập trung vào 3 bộ thư viện chính:

### A. Thư viện Core (Khai báo: `prefix="c"`)

Thư viện Core thay thế các lệnh cơ bản trong Java thành cú pháp thẻ HTML/XML.

- Khai báo đầu trang: `<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>`.

| Thẻ JSTL Core     | Tương đương trong Java     | Chức năng & Cú pháp                                                                                                                   |
| ----------------- | -------------------------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| **`<c:if>`**      | Lệnh `if`                  | Kết xuất nội dung nếu điều kiện đúng. <br> <br>Cú pháp: `<c:if test="${<điều kiện>}">...</c:if>`.                                     |
| **`<c:choose>`**  | Lệnh `if...else if...else` | Xét điều kiện từ trên xuống bằng các thẻ `<c:when>` và mặc định là `<c:otherwise>`.                                                   |
| **`<c:forEach>`** | Lệnh `for-each`            | Lặp qua tập hợp phần tử. Các thuộc tính: `var` (biến hiện tại), `items` (tập hợp), `begin`, `end`, `varStatus` (trạng thái vòng lặp). |
| **`<c:set>`**     | `setAttribute()`           | Tạo một attribute mới vào scope . <br> <br>Cú pháp: `<c:set var="name" value="val" scope="session"/>`.                                |
| **`<c:remove>`**  | `removeAttribute()`        | Xóa một attribute khỏi scope. <br> <br>Cú pháp: `<c:remove var="name" scope="session"/>`.                                             |

### B. Thư viện Format (Khai báo: `prefix="fmt"`)

Cung cấp các thẻ để định dạng dữ liệu hiển thị.

- Khai báo đầu trang: `<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>`.

1. **Định dạng Số (`<fmt:formatNumber>`)**

- Thuộc tính: `value` (số cần định dạng), `type` (kiểu định dạng như `currency` - tiền tệ, `percent` - phần trăm).

- Ví dụ: `<fmt:formatNumber value="${product.price}" type="currency"/>`.

2. **Định dạng Thời gian (`<fmt:formatDate>`)**

- Thuộc tính: `value` (thời gian cần định dạng), `pattern` (mẫu định dạng).

- Ví dụ: `<fmt:formatDate value="${product.date}" pattern="dd-MM-yyyy"/>`.

### C. Thư viện Function (Khai báo: `prefix="fn"`)

JSTL cung cấp tập hợp các hàm tập trung vào việc xử lý chuỗi và tập hợp trực tiếp bên trong biểu thức EL.

- Khai báo đầu trang: `<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>`.

**Các hàm xử lý nổi bật:**

| Tên Hàm                                                                 | Mô tả mục đích                                                                       |
| ----------------------------------------------------------------------- | ------------------------------------------------------------------------------------ |
| **`fn:contains`** / **`fn:containsIgnoreCase`**                         | Kiểm tra chuỗi 1 có chứa chuỗi 2 hay không (có/không phân biệt hoa thường).          |
| **`fn:startsWith`** / **`fn:endsWith`**                                 | Kiểm tra chuỗi có bắt đầu/kết thúc bởi một chuỗi con khác không.                     |
| **`fn:length`**                                                         | Tìm độ dài của chuỗi hoặc số lượng phần tử trong tập hợp (Array, Map, Collection).   |
| **`fn:substring`** / **`fn:substringAfter`** / **`fn:substringBefore`** | Cắt chuỗi con theo vị trí (index) hoặc dựa vào vị trí trước/sau một chuỗi phân cách. |
| **`fn:replace`**                                                        | Thay thế một chuỗi con bằng một chuỗi khác.                                          |
| **`fn:split`** / **`fn:join`**                                          | Tách chuỗi thành mảng dựa trên ký tự phân cách, hoặc ghép mảng thành chuỗi.          |
| **`fn:toLowerCase`** / **`fn:toUpperCase`**                             | Chuyển đổi toàn bộ chuỗi sang chữ thường hoặc chữ in HOA.                            |
| **`fn:trim`**                                                           | Cắt bỏ khoảng trắng dư thừa ở 2 đầu chuỗi.                                           |
