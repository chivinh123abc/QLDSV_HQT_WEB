# Chương 3: Xử Lý Khóa Chính Phức Hợp (Composite Key)

## 1. Lý thuyết cốt lõi
*   *Lớp học có thể bổ sung chi tiết nội dung lý thuyết tại đây...*

## 2. Áp dụng thực tế trong dự án
*   **Bảng DANGKY:** Sử dụng khóa chính phức hợp gồm hai trường (`maLTC` và `maSV`).
*   **Khóa ngoại và ID Class:**
    *   Lớp khóa chính phức hợp: [DangKyId.java](../../src/main/java/com/ptithcm/entity/DangKyId.java) triển khai `Serializable`, `equals()`, và `hashCode()`.
    *   Lớp ánh xạ thực thể: [DangKy.java](../../src/main/java/com/ptithcm/entity/DangKy.java) sử dụng annotation `@IdClass(DangKyId.class)` để định nghĩa quan hệ.
