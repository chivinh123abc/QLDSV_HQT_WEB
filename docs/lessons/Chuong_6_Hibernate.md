# Lập trình web Spring MVC - Bài 6: Tích hợp Hibernate

## 1. Tổng quan về Hibernate

Hibernate là framework mã nguồn mở hỗ trợ lập trình cơ sở dữ liệu (CSDL) trong các ứng dụng Java, đóng vai trò là tầng trung gian ORM (Object-Relational Mapping) giữa các đối tượng và CSDL quan hệ.

- **Tính trong suốt:** Hibernate trong suốt với ngôn ngữ SQL, nó sử dụng HQL (Hibernate Query Language) để truy vấn trực tiếp trên các đối tượng thay vì bảng.

- **Hiệu quả:** Giúp lập trình viên giảm thiểu lượng lớn công việc thao tác với CSDL, truy vấn các thực thể kết hợp dễ dàng.

- **Hỗ trợ đa CSDL:** Hoạt động tốt với nhiều hệ quản trị phổ biến như SQL Server, MySQL, Oracle, DB2, PostgreSQL....

- **Các thành phần cốt lõi:** Bao gồm Configuration (quản lý cấu hình), SessionFactory (sản sinh session), Session (phiên làm việc), Transaction (quản lý giao dịch), Query và Criteria.

---

## 2. Cấu hình tích hợp Spring và Hibernate

Để ứng dụng Spring MVC làm việc được với Hibernate, bạn cần cấu hình 3 bean cốt lõi trong file XML:

- **DriverManagerDataSource:** Bean khai báo thông số kết nối vật lý đến CSDL gồm Driver (ví dụ: `sqljdbc4.jar` cho SQL Server), URL, Username và Password.

- **LocalSessionFactoryBean:** Bean chịu trách nhiệm tạo ra SessionFactory, cần được tham chiếu tới `dataSource` và khai báo các thông số như `SQLServerDialect` cùng package chứa các entity class.

- **HibernateTransactionManager:** Bean quản lý các giao dịch (transaction) tự động để tăng hiệu suất. Cần tham chiếu đến `sessionFactory` và khai báo thẻ `<tx:annotation-driven/>` để cho phép dùng annotation `@Transactional` trong code.

---

## 3. Ánh xạ thực thể (Entity Mapping)

Hibernate ánh xạ các lớp (Class) thành bảng (Table), và các thuộc tính (Property) thành các cột (Column) trong CSDL thông qua XML hoặc Annotation. Trong khóa học này, chúng ta dùng Annotation.

| Annotation Cơ Bản   | Chức năng định nghĩa                    | Ví dụ áp dụng                  |
| ------------------- | --------------------------------------- | ------------------------------ |
| **@Entity**         | Đánh dấu lớp Java là một thực thể       | `@Entity`                      |
| **@Table**          | Ánh xạ lớp thực thể tới một bảng cụ thể | `@Table(name="Users")`         |
| **@Id**             | Đánh dấu thuộc tính là khóa chính       | `@Id`                          |
| **@GeneratedValue** | Chỉ định cột tự động tăng giá trị       | `@GeneratedValue`              |
| **@Column**         | Ánh xạ thuộc tính tới một cột cụ thể    | `@Column(name="Fullname")`     |
| **@Temporal**       | Chỉ định định dạng chuyển đổi thời gian | `@Temporal(TemporalType.DATE)` |

**Ánh xạ quan hệ (Relationships):**

- **Quan hệ N-1 (Nhiều - Một):** Sử dụng `@ManyToOne` kết hợp với `@JoinColumn(name="khóa_ngoại")` để thay thế khóa ngoại bằng một thực thể kết hợp.

- **Quan hệ 1-N (Một - Nhiều):** Sử dụng `@OneToMany(mappedBy="tên_trường_thực_thể", fetch=FetchType.EAGER)` để chỉ ra danh sách các thực thể con. Chế độ `EAGER` nạp kèm dữ liệu ngay lập tức, trong khi `LAZY` sẽ trì hoãn việc nạp.

---

## 4. Lập trình thao tác với Hibernate

Trước tiên, cần tiêm `SessionFactory` vào các `@Controller` thông qua annotation `@Autowired`. Tùy vào mục đích, cách mở Session sẽ khác nhau:

- **Sử dụng Session có sẵn (`getCurrentSession()`):** Dành cho việc truy vấn dữ liệu. Spring tự động commit/rollback, nhưng lập trình viên phải đặt annotation `@Transactional` trên Controller hoặc phương thức.

- **Sử dụng Session mới (`openSession()`):** Dành cho các thao tác làm thay đổi dữ liệu (Thêm, Sửa, Xóa). Lập trình viên phải tự mở transaction, commit, rollback và đóng session trong khối try-catch-finally.

**Các cú pháp truy vấn (Query) quan trọng:**

- **Truy vấn danh sách:** `session.createQuery(hql).list()`.

- **Truy vấn có tham số:** Đặt tên tham số với dấu hai chấm (`:min`) và gán giá trị bằng `query.setParameter("min", 6.5)`.

- **Phân trang:** Giới hạn kết quả bằng `query.setFirstResult(vị_trí_bắt_đầu)` và `query.setMaxResults(số_lượng)`.

- **Lấy một đối tượng/giá trị:** Dùng `query.uniqueResult()` ép kiểu về đúng định dạng, hoặc dùng trực tiếp `session.get(Class.class, Id)`.

**Các thao tác thay đổi dữ liệu (CRUD):**
Luôn phải bọc bên trong một Transaction gồm `session.beginTransaction()`, sau đó gọi các hàm tương ứng `session.save(obj)`, `session.update(obj)`, hoặc `session.delete(obj)`. Cuối cùng gọi `commit()` nếu thành công hoặc `rollback()` nếu có lỗi.

---

## 5. Ngôn ngữ HQL và Transaction

**Về HQL (Hibernate Query Language):**
HQL là ngôn ngữ truy vấn đối tượng, có cú pháp gần giống SQL nhưng thay vì gọi Tên Bảng/Tên Cột, HQL gọi Tên Thực Thể/Thuộc Tính.

- **Cú pháp cơ bản:** Hỗ trợ đầy đủ các mệnh đề `SELECT`, `FROM`, `WHERE`, `GROUP BY`, `HAVING`, `ORDER BY`.

- **Hàm hỗ trợ:** Cung cấp các hàm thống kê (`avg`, `sum`, `count`), hàm xử lý chuỗi (`concat`, `lower`, `substring`), và hàm thời gian (`current_date`, `month`).

**Về Transaction (Giao dịch):**
Đóng vai trò bảo vệ toàn vẹn dữ liệu khi hệ thống thực hiện nhiều thao tác đọc/ghi liên tục. Nguyên tắc là: Tất cả thao tác cùng thành công, hoặc không có thao tác nào được ghi nhận xuống CSDL.

- Transaction phải đáp ứng 4 tiêu chuẩn **ACID** để đảm bảo tính nhất quán của cơ sở dữ liệu: Atomicity (Nguyên tử), Consistency (Nhất quán), Isolation (Độc lập), và Durability (Bền vững).
