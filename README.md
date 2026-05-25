# 🏫 Dự Án Quản Lý Điểm Sinh Viên Hệ Tín Chỉ (QLDSV_HTC_WEB)

Chào mừng bạn đến với **QLDSV_HTC_WEB** - hệ thống Web quản lý điểm sinh viên học theo hệ thống tín chỉ. Dự án được phát triển theo mô hình MVC sử dụng các công nghệ Spring Framework (Spring MVC, Spring ORM), Hibernate, và SQL Server.

---

## 📘 Tài Liệu Liên Quan

> [!IMPORTANT]
> Dự án này đi kèm tài liệu tổng hợp kiến thức lý thuyết & thực hành liên quan đến lập trình web (Spring MVC, Hibernate ORM, JSTL, DI, Validation, Interceptor, i18n, etc.) cùng danh sách nhắc nhở các tính năng bắt buộc và nâng cao cần áp dụng.
>
> 👉 **[Đọc tài liệu tổng hợp kiến thức tại đây (LESSON NOTES)](./docs/lessons/Tong_hop_kien_thuc.md)**
>
> 👉 **[Đọc danh sách tính năng & lưu ý áp dụng tại đây](./docs/Luu_y_ap_dung.md)**

---

## 📚 Phân Chia Chương Bài Học (Mục lục chi tiết)

- **[Chương 1: Kiến Trúc Spring MVC & Luồng Xử Lý](./docs/lessons/Chuong_1_Spring_MVC.md)**
- **[Chương 2: Controller & Xử Lý Yêu Cầu](./docs/lessons/Chuong_2_Controller.md)**
- **[Chương 3: Làm Việc Với Form & Databinding](./docs/lessons/Chuong_3_Form.md)**
- **[Chương 4: Expression Language (EL) & JSTL](./docs/lessons/Chuong_4_EL_JSTL.md)**
- **[Chương 5: Dependency Injection, Upload File & Gửi Email](./docs/lessons/Chuong_5_Bean_DI_File_Email.md)**
- **[Chương 6: Tích Hợp Hibernate ORM & Transaction](./docs/lessons/Chuong_6_Hibernate.md)**
- **[Chương 7: Validation & Interceptor](./docs/lessons/Chuong_7_Validation_Interceptor.md)**
- **[Chương 8: Tổ Chức Giao Diện, Đa Ngôn Ngữ (i18n) & Trình Soạn Thảo](./docs/lessons/Chuong_8_To_Chuc_Giao_Dien.md)**

---

## 🛠️ Công Nghệ Sử Dụng

Dự án được xây dựng dựa trên ngăn xếp công nghệ (Tech Stack) chuẩn Enterprise:

- **Ngôn ngữ chính:** Java 17
- **Framework cốt lõi:** Spring Framework 6.1.4 (Web MVC, Transaction, ORM)
- **Công nghệ View:** Jakarta Servlet 6.0, JSP 3.1.1, JSTL 3.0
- **Thư viện ORM:** Hibernate 6.4.4.Final
- **Hệ quản trị CSDL:** SQL Server (sử dụng Driver `mssql-jdbc` phiên bản 12.4.2)
- **Bể chứa kết nối (Connection Pooling):** Apache Commons DBCP2
- **Trình biên dịch & Đóng gói:** Maven
- **Web Server nhúng:** Apache Tomcat 10.x thông qua Cargo Maven Plugin

---

## 📂 Kiến Trúc Thư Mục Dự Án

Kiến trúc mã nguồn được phân chia rõ ràng theo mô hình layered, loại bỏ các bảng quản trị tập trung (dư thừa) và tích hợp cấu trúc đặt tên chuẩn hóa:

```text
QLDSV_HQT_WEB
├── Gendb.sql                                # File script tạo CSDL & dữ liệu mẫu SQL Server
├── pom.xml                                  # File quản lý thư viện và cấu hình Cargo plugin
├── db.dbml                                  # Bản vẽ thiết kế CSDL (Lowercase snake_case)
├── README.md                                # Tài liệu tổng quan dự án (File này)
├── docs/                                    # Thư mục lưu trữ tài liệu liên quan
│   ├── Luu_y_ap_dung.md                     # Danh sách lưu ý & các tính năng cần áp dụng (Đọc trước)
│   └── lessons/                             # Thư mục phân chia chương bài học lý thuyết & thực hành
│       ├── Chuong_1_Spring_MVC.md
│       ├── Chuong_2_Controller.md
│       ├── Chuong_3_Form.md
│       ├── Chuong_4_EL_JSTL.md
│       ├── Chuong_5_Bean_DI_File_Email.md
│       ├── Chuong_6_Hibernate.md
│       ├── Chuong_7_Validation_Interceptor.md
│       ├── Chuong_8_To_Chuc_Giao_Dien.md
│       └── Tong_hop_kien_thuc.md            # Tài liệu tổng hợp kiến thức (LESSON NOTES)
└── src
    └── main
        ├── java
        │   └── com
        │       └── ptithcm
        │           ├── entity               # Các Java class ánh xạ trực tiếp sang các bảng CSDL
        │           │   ├── base
        │           │   │   └── LuuVetThoiGian.java # MappedSuperclass chứa các trường audit (ngay_tao, ngay_cap_nhat, ngay_xoa)
        │           │   ├── DangKy.java
        │           │   ├── DangKyId.java     # Khóa chính phức hợp cho bảng Đăng ký
        │           │   ├── GiangVien.java
        │           │   ├── Khoa.java
        │           │   ├── Lop.java
        │           │   ├── LopTinChi.java
        │           │   ├── MonHoc.java
        │           │   └── SinhVien.java
        │           ├── modules              # Tổ chức mã nguồn theo Module (Controller, Service, DAO)
        │           │   ├── auth/            # Module xác thực (Đăng nhập, đăng xuất, cookie)
        │           │   │   ├── AuthDAO.java
        │           │   │   ├── AuthService.java
        │           │   │   └── LoginController.java
        │           │   ├── dangky/          # Module đăng ký lớp tín chỉ
        │           │   │   ├── DangKyController.java
        │           │   │   ├── DangKyDAO.java
        │           │   │   └── DangKyService.java
        │           │   ├── diemso/          # Module quản lý điểm số
        │           │   │   ├── MarkController.java
        │           │   │   ├── MarkDAO.java
        │           │   │   └── MarkService.java
        │           │   ├── giangvien/       # Module quản lý thông tin giảng viên
        │           │   │   ├── GiangVienController.java
        │           │   │   ├── GiangVienDAO.java
        │           │   │   └── GiangVienService.java
        │           │   ├── home/            # Module trang chủ điều hướng
        │           │   │   ├── HomeController.java
        │           │   │   ├── HomeDAO.java
        │           │   │   └── HomeService.java
        │           │   ├── khoa/            # Module quản lý danh mục khoa
        │           │   │   ├── KhoaController.java
        │           │   │   ├── KhoaDAO.java
        │           │   │   └── KhoaService.java
        │           │   ├── lop/             # Module quản lý lớp sinh viên
        │           │   │   ├── LopController.java
        │           │   │   ├── LopDAO.java
        │           │   │   └── LopService.java
        │           │   ├── loptinchi/       # Module quản lý lớp tín chỉ
        │           │   │   ├── LopTinChiController.java
        │           │   │   ├── LopTinChiDAO.java
        │           │   │   └── LopTinChiService.java
        │           │   ├── monhoc/          # Module quản lý danh mục môn học
        │           │   │   ├── MonHocController.java
        │           │   │   ├── MonHocDAO.java
        │           │   │   └── MonHocService.java
        │           │   ├── report/          # Module thống kê & báo cáo dữ liệu
        │           │   │   ├── ReportController.java
        │           │   │   ├── ReportDAO.java
        │           │   │   └── ReportService.java
        │           │   └── sinhvien/        # Module quản lý sinh viên
        │           │       ├── SinhVienController.java
        │           │       ├── SinhVienDAO.java
        │           │       └── SinhVienService.java
        │           └── shared               # Các DTO, Interceptor, Validator và Utility dùng chung
        │               ├── base/            # DAO cơ bản và Query Builder động
        │               │   ├── BaseDAO.java
        │               │   └── HqlQueryBuilder.java
        │               ├── constant/        # Định nghĩa các hằng số hệ thống
        │               │   ├── MessageConstant.java
        │               │   └── SessionConstant.java
        │               ├── dto/             # Data Transfer Objects
        │               │   ├── FindOptions.java
        │               │   ├── PaginationDTO.java
        │               │   ├── PaginationResult.java
        │               │   ├── Sort.java
        │               │   └── UserSession.java
        │               ├── interceptor/     # Bộ lọc phân quyền (Authentication/RBAC)
        │               │   └── AuthInterceptor.java
        │               ├── util/            # Các tiện ích (Mã hóa, Xử lý chuỗi, Session)
        │               │   ├── DateUtil.java
        │               │   ├── SecurityUtil.java
        │               │   └── SessionUtil.java
        │               └── validator/       # Bộ xác thực dữ liệu đầu vào (Validation)
        │                   ├── LopTinChiValidator.java
        │                   ├── LopValidator.java
        │                   ├── MonHocValidator.java
        │                   └── SinhVienValidator.java
        └── webapp
            ├── resources                    # Thư mục chứa CSS, Javascript tĩnh
            │   ├── css
            │   │   └── style.css
            │   └── js
            │       └── main.js
            └── WEB-INF
                 ├── config                  # Cấu hình Spring Beans, MVC, Hibernate Datasource
                 │   ├── spring-config-bean.xml
                 │   └── spring-config-mvc.xml
                ├── views                    # Các trang hiển thị JSP (được tổ chức theo thực thể)
                │   ├── class
                │   ├── credit-class
                │   ├── faculty
                │   ├── lecturer
                │   ├── mark
                │   ├── registration
                │   ├── report
                │   ├── shared
                │   ├── student
                │   ├── subject
                │   ├── index.jsp
                │   └── login.jsp
                └── web.xml                  # Cấu hình khởi tạo DispatcherServlet và Filters
```

---

## 👥 Vai Trò & Phân Quyền (RBAC)

Hệ thống cung cấp cơ chế phân quyền dựa trên 3 nhóm vai trò (Roles) chính được xác định trực tiếp từ dữ liệu sinh viên/giảng viên:

| Quyền | Vai trò (Role Code)      | Phạm vi thao tác                                                                                                                                                                                                                 |
| :---- | :----------------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **1** | **PGV** (Phòng Giáo Vụ)  | Toàn quyền CRUD trên danh mục Khoa, Lớp, Môn Học, Lớp Tín Chỉ, Sinh Viên. Nhập và lưu điểm cho mọi sinh viên. Xem các loại báo cáo trên toàn trường.                                                                             |
| **2** | **KHOA** (Khoa)          | Quyền hạn tương tự PGV nhưng **bị giới hạn phạm vi**: chỉ được phép xem, thêm, sửa, xóa các Lớp, Sinh Viên, Lớp Tín Chỉ, và Điểm số thuộc về Khoa của mình (được nhận diện qua mã Khoa của tài khoản giảng viên đang đăng nhập). |
| **3** | **SINHVIEN** (Sinh Viên) | Chỉ được phép đăng nhập vào giao diện Đăng ký / Hủy đăng ký Lớp Tín Chỉ (trong học kỳ hiện tại), và xem bảng điểm cá nhân của mình.                                                                                              |

---

## 🔄 Quy Trình & Luồng Xử Lý Tính Năng (Feature Flows)

### 1. Luồng Đăng Nhập & Tái Lập Phiên Làm Việc (Authentication Flow)

```mermaid
sequenceDiagram
    autonumber
    actor User as Người dùng
    participant Client as Browser (JSP)
    participant Interceptor as AuthInterceptor
    participant Controller as LoginController
    participant DB as SQL Server

    User->>Client: Nhập Username/Password & submit
    Client->>Controller: POST /login (username, password)
    Note over Controller, DB: Kiểm tra kép (Dual Query) trên 2 bảng sinh_vien và giang_vien
    Controller->>DB: Truy vấn thông tin trong sinh_vien
    alt Tìm thấy sinh viên
        DB-->>Controller: Trả về SinhVien profile
    else Không thấy sinh viên
        Controller->>DB: Truy vấn thông tin trong giang_vien
        DB-->>Controller: Trả về GiangVien profile (chứa vai trò PGV/KHOA)
    end
    alt Đăng nhập thành công
        Controller->>Client: Lưu UserSession vào Session & Tạo cookie remember_me (mã hóa Base64)
        Controller-->>User: Chuyển hướng tới /index (hoặc /registration nếu là SINHVIEN)
    else Sai thông tin
        Controller-->>User: Trả về trang login kèm thông báo lỗi
    end

    Note over User, Interceptor: Khi truy cập lại sau đó:
    User->>Interceptor: Gửi request bất kỳ kèm Cookie
    Interceptor->>Interceptor: Phát hiện Session trống nhưng có Cookie "remember_me"
    Interceptor->>DB: Giải mã Cookie lấy username/password kiểm tra trên 2 bảng
    DB-->>Interceptor: Khớp dữ liệu sinh_vien hoặc giang_vien
    Interceptor->>Interceptor: Tái lập UserSession & StudentProfile (nếu có) vào Session
    Interceptor-->>User: Cho phép truy cập tài nguyên trực tiếp
```

### 2. Luồng Đăng Ký Lớp Tín Chỉ (Course Registration Flow)

```mermaid
graph TD
    A[Sinh viên chọn môn học & đăng ký] --> B{Kiểm tra trạng thái SV}
    B -- Đang nghỉ học --> C[Báo lỗi: SV nghỉ học]
    B -- Đang đi học --> D{Kiểm tra trạng thái lớp}
    D -- Lớp đã bị hủy --> E[Báo lỗi: Lớp đã hủy]
    D -- Lớp đang mở --> F{Kiểm tra môn học trùng lặp}
    F -- Đã đăng ký môn này trong học kỳ --> G[Báo lỗi: Trùng môn học trong học kỳ]
    F -- Chưa đăng ký --> H{Kiểm tra lịch sử đăng ký trước đó}
    H -- Đã từng đăng ký và hủy --> I[Cập nhật is_huy_dang_ky = false]
    H -- Lần đầu đăng ký --> J[Thêm bản ghi dang_ky mới]
    I --> K[Thành công & Cập nhật giao diện bằng AJAX]
    J --> K
```

### 3. Luồng Quản Lý Điểm Số (Marks Management Flow)

1. Giảng viên/Giáo vụ chọn **Niên khóa**, **Học kỳ**, **Môn học**, và **Nhóm** (Lớp tín chỉ).
2. Hệ thống gọi API `load-students` tải danh sách sinh viên đã đăng ký lớp học này kèm theo các cột điểm hiện tại: Điểm chuyên cần (diem_chuyen_can), Điểm giữa kỳ (diem_giua_ky), Điểm cuối kỳ (diem_cuoi_ky).
3. Người dùng nhập điểm trực tiếp trên bảng Excel-like.
4. Khi ấn **Lưu tất cả**, một JSON array chứa danh sách điểm được gửi lên API `/mark/save-all` qua phương thức POST AJAX.
5. Controller thực hiện cập nhật đồng loạt (Batch Update) dữ liệu điểm vào CSDL.
6. **Ràng buộc quan trọng:** Hệ thống cấm sinh viên tự ý hủy đăng ký môn học một khi bất kỳ cột điểm nào đã được giáo vụ nhập điểm.

### 4. Luồng Xuất Báo Cáo (Reports Flow)

Hệ thống cung cấp hai loại báo cáo chính gọi thông qua stored procedure:

- **Danh sách sinh viên đăng ký lớp tín chỉ:**
  - Truy vấn danh sách sinh viên có trạng thái `is_huy_dang_ky = 0` dựa theo bộ lọc niên khóa, học kỳ, môn học, nhóm.
  - Gọi Procedure: `sp_LayDanhSachSinhVienDangKyLopTinChi`
- **Bảng điểm tổng kết lớp học:**
  - Tải bảng điểm tổng kết của cả một lớp học (tất cả các môn học trong chương trình). Cột môn học được xoay ngang động (Pivot) trên Database.
  - Gọi Procedure: `sp_LayBangDiemTongKet`

---

## 🚀 Hướng Dẫn Cài Đặt & Chạy Dự Án

Để chạy dự án ở môi trường cục bộ (Local), hãy thực hiện tuần tự các bước sau:

### Bước 1: Thiết lập Cơ sở dữ liệu SQL Server

1. Mở công cụ **SQL Server Management Studio (SSMS)**.
2. Kết nối tới SQL Server instance cục bộ (mặc định cổng `1433`).
3. Mở file [Gendb.sql](./Gendb.sql) (hoặc schema tương ứng cấu trúc snake_case).
4. Nhấn **Execute** (hoặc phím `F5`) để tạo cơ sở dữ liệu `QLDSV_HTC_WEB`, tạo cấu trúc bảng, các ràng buộc khóa ngoại và chèn dữ liệu mẫu.

### Bước 2: Cấu hình Datasource trong Project

Nếu SQL Server của bạn sử dụng tài khoản/mật khẩu khác hoặc cổng kết nối khác:

1. Mở file [spring-config-bean.xml](./src/main/webapp/WEB-INF/configs/spring-config-bean.xml).
2. Chỉnh sửa thuộc tính của bean `dataSource` (từ dòng 15 tới 20):
   ```xml
   <property name="url" value="jdbc:sqlserver://localhost:1433;databaseName=QLDSV_HTC_WEB;encrypt=true;trustServerCertificate=true;" />
   <property name="username" value="sa" />
   <property name="password" value="mật_khẩu_của_bạn" />
   ```

### Bước 3: Build và Chạy dự án bằng Maven hoặc Make

Hệ thống sử dụng **Cargo Maven Plugin** đóng gói Web Server Tomcat nhúng giúp khởi chạy ngay lập tức mà không cần cài đặt Tomcat độc lập bên ngoài.

Bạn có thể chạy dự án bằng một trong hai cách:

- **Sử dụng Make (Nhanh nhất):**
  ```bash
  make dev
  ```
- **Sử dụng lệnh Maven trực tiếp:**
  `bash
  mvn clean package cargo:run
  `
  _Lệnh này sẽ tải các thư viện phụ thuộc, biên dịch mã nguồn Java, đóng gói file `.war`, khởi chạy Tomcat nhúng ở cổng `8080`._

### Bước 4: Kiểm tra và Đăng nhập hệ thống

Mở trình duyệt web và truy cập địa chỉ:

```text
http://localhost:8080/
```

Sử dụng các tài khoản mẫu sau để trải nghiệm các phân quyền khác nhau:

| Tài khoản đăng nhập (Username) | Mật khẩu (Password) | Quyền hạn (Role) | Chú thích                                                  |
| :----------------------------- | :------------------ | :--------------- | :--------------------------------------------------------- |
| `GV01`                         | `123`               | **PGV**          | Tài khoản giảng viên có vai trò Phòng Giáo Vụ (Toàn quyền) |
| `GV02`                         | `123`               | **KHOA**         | Giảng viên thuộc khoa CNTT (chỉ quản lý dữ liệu CNTT)      |
| `SV01`                         | `123`               | **SINHVIEN**     | Sinh viên Nguyễn A - Lớp CNTT1 (Xem điểm, Đăng ký môn)     |
| `SV02`                         | `123`               | **SINHVIEN**     | Sinh viên Trần B - Lớp CNTT1 (Xem điểm, Đăng ký môn)       |

---

## 🧼 Định Dạng Mã Nguồn (Code Formatting)

Dự án tích hợp công cụ **Spotless** (`spotless-maven-plugin`) để tự động định dạng mã nguồn theo tiêu chuẩn (Java Google Format style AOSP).

Bạn có thể chạy các lệnh kiểm tra và tự động căn chỉnh mã nguồn qua **Makefile**:

- **Tự động định dạng toàn bộ mã nguồn (Java, XML, Markdown):**
  ```bash
  make format
  ```
- **Kiểm tra xem mã nguồn đã đúng chuẩn định dạng chưa (không sửa file):**
  ```bash
  make lint
  ```
