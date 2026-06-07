# 🛠️ Hướng Dẫn Cài Đặt & Chạy Dự Án (SETUP.md)

Tài liệu này hướng dẫn chi tiết cách cài đặt môi trường, cấu hình cơ sở dữ liệu, thiết lập biến môi trường và khởi chạy dự án **QLDSV_HTC_WEB** bằng Maven hoặc Docker Compose.

---

## 📋 Yêu cầu Hệ thống & Tiền đề (Prerequisites)

Trước khi bắt đầu, hãy đảm bảo máy tính của bạn đã cài đặt các công cụ sau:

1. **Java Development Kit (JDK):** Phiên bản **17** trở lên.
2. **Apache Maven:** Phiên bản **3.8** trở lên (dùng để quản lý dependencies và build project).
3. **SQL Server:** Phiên bản **2019** trở lên (Cổng mặc định: `1433`).
4. **Redis Server:** Phiên bản **6.0** trở lên (Cổng mặc định: `6379`).
5. **Docker & Docker Compose** *(Tùy chọn - khuyên dùng nếu muốn deploy hoặc chạy môi trường cô lập).*

---

## 🗄️ Bước 1: Thiết lập Cơ sở dữ liệu SQL Server

1. Khởi động công cụ **SQL Server Management Studio (SSMS)** hoặc bất kỳ Database client nào hỗ trợ SQL Server.
2. Kết nối tới SQL Server instance cục bộ (mặc định cổng `1433`).
3. Mở file [Gendb.sql](../Gendb.sql) nằm ở thư mục gốc của project.
4. Nhấn **Execute** (hoặc phím `F5`) để chạy toàn bộ file script. Script này sẽ:
   - Tạo cơ sở dữ liệu `QLDSV_HTC_WEB`.
   - Tạo toàn bộ cấu trúc bảng theo chuẩn snake_case.
   - Tạo các ràng buộc khóa chính, khóa ngoại, các stored procedures cần thiết.
   - Chèn dữ liệu mẫu cho các bảng (Sinh viên, Lớp học, Môn học, Tài khoản đăng nhập mẫu).

---

## 🔄 Bước 2: Khởi chạy Redis Server

Hệ thống sử dụng Redis để quản lý hàng đợi đăng ký tín chỉ bất đồng bộ (Redis Queue), lưu mã OTP xác thực email và quản lý giới hạn tần suất (Rate Limiting).

Khởi động Redis Server trên máy của bạn bằng command:
```bash
redis-server
```
Đảm bảo Redis đang lắng nghe tại cổng mặc định `6379`.

---

## ⚙️ Bước 3: Cấu hình Datasource & Biến bảo mật

### 1. Cấu hình Kết nối SQL Server
Nếu SQL Server của bạn sử dụng tài khoản, mật khẩu hoặc cổng khác với cấu hình mặc định:
- Mở file [spring-config-bean.xml](../src/main/webapp/WEB-INF/configs/spring-config-bean.xml).
- Chỉnh sửa thông số của bean `dataSource` (từ dòng 15 tới 20):
```xml
<property name="url" value="jdbc:sqlserver://localhost:1433;databaseName=QLDSV_HTC_WEB;encrypt=true;trustServerCertificate=true;" />
<property name="username" value="sa" />
<property name="password" value="mat_khau_cua_ban" />
```

### 2. Cấu hình Gửi Email (Gmail SMTP)
Để hệ thống có thể gửi mã OTP kích hoạt tài khoản và đổi mật khẩu qua email:
- Mở file [spring-config-gmail.xml](../src/main/webapp/WEB-INF/configs/spring-config-gmail.xml).
- Điền thông tin tài khoản Gmail và App Password (Mật khẩu ứng dụng của Google):
```xml
<property name="username" value="your_email@gmail.com" />
<property name="password" value="your_google_app_password" />
```

### 3. Cấu hình Cổng thanh toán MoMo
Cấu hình API Keys của MoMo nằm trong file [spring-config-payment.xml](../src/main/webapp/WEB-INF/configs/spring-config-payment.xml) (hoặc file properties tương ứng):
- Điền các khóa Partner Code, Access Key, Secret Key của tài khoản MoMo Sandbox hoặc Integration thật.

### 4. Cấu hình Pusher (Thông báo Real-time)
Cấu hình Pusher API key cho thông báo đẩy được định nghĩa trong [spring-config-task.xml](../src/main/webapp/WEB-INF/configs/spring-config-task.xml):
- Điền App ID, Key, Secret, Cluster tương ứng với Pusher App của bạn.

---

## 🚀 Bước 4: Khởi chạy Dự án

Dự án sử dụng **Cargo Maven Plugin** để tự động tải và chạy Web Server Apache Tomcat 10 nhúng. Bạn không cần phải tải hay cài đặt Tomcat độc lập bên ngoài.

### Cách 1: Chạy bằng Makefile (Nhanh nhất)
Nếu máy bạn có hỗ trợ công cụ `make`:
```bash
make dev
```

### Cách 2: Chạy trực tiếp qua lệnh Maven
Mở terminal tại thư mục gốc của project và chạy lệnh:
```bash
mvn clean package cargo:run
```
Lệnh này sẽ tiến hành biên dịch mã nguồn Java, đóng gói ứng dụng thành file `.war`, sau đó khởi động server Tomcat nhúng lắng nghe ở cổng `8080`.

### Cách 3: Khởi chạy bằng Docker Compose
Nếu muốn khởi chạy toàn bộ stack dịch vụ bao gồm SQL Server, Redis và ứng dụng web được đóng gói cô lập:
```bash
docker-compose up -d --build
```
Ứng dụng sẽ tự động được build và chạy trong container Tomcat.

---

## 🔑 Bước 5: Đăng nhập & Trải nghiệm Hệ thống

Khi server đã khởi động hoàn tất, hãy mở trình duyệt và truy cập:
```text
http://localhost:8080/
```

Sử dụng các tài khoản mẫu sau để trải nghiệm đầy đủ 3 vai trò trong hệ thống:

| Tài khoản đăng nhập (Username) | Mật khẩu (Password) | Quyền hạn (Role) | Chú thích                                                  |
| :----------------------------- | :------------------ | :--------------- | :--------------------------------------------------------- |
| `GV01`                         | `123`               | **PGV**          | Tài khoản giảng viên thuộc Phòng Giáo Vụ (Toàn quyền CRUD) |
| `GV02`                         | `123`               | **KHOA**         | Giảng viên thuộc khoa CNTT (Chỉ quản lý dữ liệu CNTT)      |
| `SV01`                         | `123`               | **SINHVIEN**     | Sinh viên Nguyễn A - Lớp CNTT1 (Xem điểm, Đăng ký môn)     |
| `SV02`                         | `123`               | **SINHVIEN**     | Sinh viên Trần B - Lớp CNTT1 (Xem điểm, Đăng ký môn)       |
