| **HỌC VIỆN CÔNG NGHỆ BƯU CHÍNH VIỄN THÔNG**<br><br>**KHOA: CÔNG NGHỆ THÔNG TIN 2**                                      |
| ----------------------------------------------------------------------------------------------------------------------- | --- |
| Học phần: **INT1434_CLC - Lập trình Web**<br><br>Trình độ đào tạo: **Đại học** <br><br>Hình thức đào tạo: **Chính quy** |     |

**THÔNG TIN ĐỀ TÀI DỰ ÁN**

**ĐỀ TÀI SỐ 4**

**1\. Tên đề tài:** Phát triển ứng dụng Web quản lý sinh viên (Student Management App)

**2\. Số lượng sinh viên yêu cầu:** 2 sinh viên

- N23DCCN067 - Lương Chí Vinh
- N23DCCN003 - Phan Tuấn Quốc Anh

**3\. Mô tả đề tài**

**Các yêu cầu chính của đề tài:**

- **Quản lý danh mục:**
  - Quản lý thông tin Khoa, Lớp, Sinh viên, Giảng Viên.
  - Quản lý Môn học.
  - Đảm bảo ràng buộc khóa chính, khóa ngoại và toàn vẹn dữ liệu trong cơ sở dữ liệu quan hệ.
- **Quản lý lớp tín chỉ:**
  - Mở lớp tín chỉ theo: Niên khóa, Học kỳ, Môn học, Nhóm.
  - Quản lý số lượng sinh viên tối thiểu/tối đa cho mỗi lớp.
  - Cho phép hủy lớp tín chỉ.
- **Đăng ký lớp tín chỉ:**
  - Sinh viên đăng nhập bằng mã sinh viên.
  - Hiển thị danh sách lớp tín chỉ theo niên khóa và học kỳ.
  - Cho phép: Đăng ký lớp, Hủy đăng ký.
  - Hiển thị real-time: Tên môn học, Giảng viên, Số sinh viên đã đăng ký.
- **Nhập điểm:**
  - Giảng viên hoặc giáo vụ khoa nhập điểm cho sinh viên.
  - Các loại điểm: Điểm chuyên cần, Điểm giữa kỳ, Điểm cuối kỳ.
  - Tự động tính toán: Điểm hết môn = Chuyên cần \* 0.1 + Giữa kỳ \* 0.3 + Cuối kỳ \* 0.6.
- **Phân quyền hệ thống (RBAC):**
  - Hệ thống có 3 nhóm người dùng với các vai trò chuyên biệt.
  - **PHÒNG GIÁO VỤ:** Toàn quyền hệ thống, chịu trách nhiệm tạo tài khoản.
  - **KHOA:** Nhập điểm, xem và quản lý dữ liệu thuộc khoa mình.
  - **SINH VIÊN:** Đăng ký tín chỉ, xem bảng điểm cá nhân.
- **In ấn báo cáo (Xuất file PDF/Excel):**
  - Danh sách lớp tín chỉ.
  - Danh sách sinh viên đăng ký lớp.
  - Bảng điểm lớp tín chỉ.
  - Phiếu điểm cá nhân.
  - Bảng điểm tổng kết.
- **Quản trị hệ thống & Bảo mật:**
  - Tạo login, password.
  - Phân quyền linh hoạt theo vai trò (role).
  - Bảo mật thông tin người dùng, mã hóa mật khẩu bằng BCrypt thay vì lưu plain text.
- **Giao diện người dùng:**
  - Giao diện thân thiện, dễ dùng, rõ ràng, tối ưu trải nghiệm (UX/UI) cho các nghiệp vụ quản lý đào tạo.

**Mở rộng khác (tùy chọn):**

- **Xử lý Race Condition:** Áp dụng cơ chế Optimistic/Pessimistic Locking ở tầng Cơ sở dữ liệu để đảm bảo tính toàn vẹn dữ liệu khi có hàng ngàn sinh viên thao tác đăng ký cùng lúc cho một lớp tín chỉ giới hạn số lượng.
- **Kiến trúc & Design Pattern:** Áp dụng Spring AOP (Aspect-Oriented Programming) để tạo hệ thống Audit Log (lưu vết thay đổi), hỗ trợ chức năng UNDO khi người dùng không muốn dữ liệu bị thay đổi sai sót. Sử dụng Global Exception Handling để quản lý lỗi tập trung.
- **Tối ưu hiệu năng:** Ứng dụng Spring Cache cho các danh mục ít thay đổi (Khoa, Môn học). Xử lý bất đồng bộ (Asynchronous) cho tác vụ gửi thông báo/email khi có điểm mới để không làm nghẽn luồng xử lý chính.
- **Bảo mật Web tiên tiến:** Ngoài Session-based Auth, hệ thống tích hợp bảo vệ CSRF (Cross-Site Request Forgery) và cơ chế phòng chống Brute Force khi đăng nhập.
- **DevOps & Deployment:** Ứng dụng được đóng gói bằng Docker và Docker Compose, kèm theo kịch bản Data Seeding (DataSeeds.sql) để tự động khởi tạo dữ liệu mẫu, giúp quá trình triển khai môi trường nhanh chóng.
- **Tiện ích:** Tìm kiếm nhanh (Full-text search) sinh viên, lớp học. Import danh sách sinh viên/điểm hàng loạt từ file Excel.

**4\. Yêu cầu nhóm và học viên**

- **\[CLO1\]:** Phát triển ứng dụng Web bằng ngôn ngữ lập trình Java (hệ sinh thái Spring Boot / Spring MVC). Tuân thủ tuyệt đối **mô hình MVC** (Model-View-Controller) và kiến trúc 3-Tier (Controller-Service-Repository). Bắt buộc áp dụng cơ chế **Server-Side Rendering (SSR)** bằng template engine (Thymeleaf/JSP), **không xây dựng hệ thống theo dạng API/RESTful**.
- **\[CLO2\]:** Hiểu và áp dụng được luồng xử lý dữ liệu từ Database lên View, quản lý state (Session), có phân quyền bảo mật chặt chẽ (Spring Security) để bảo vệ tài khoản cá nhân và quyền riêng tư đáp ứng các yêu cầu nghiệp vụ.
- **\[CLO2\]:** Thuyết trình và bảo vệ được kết quả công việc của cá nhân. Trình bày rõ ràng các biểu đồ thiết kế (Use Case, ERD, Sequence), mô hình kiến trúc, các tiêu chuẩn và nguyên tắc thiết kế (Clean Architecture/Design Patterns) áp dụng trong dự án.
