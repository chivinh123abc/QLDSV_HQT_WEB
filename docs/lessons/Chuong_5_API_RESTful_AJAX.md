# Chương 5: Tích Hợp API Restful & Cơ Chế AJAX

## 1. Lý thuyết cốt lõi
*   *Lớp học có thể bổ sung chi tiết nội dung lý thuyết tại đây...*

## 2. Áp dụng thực tế trong dự án
*   **API Response (JSON):** Các API trong [DangKyController.java](../../src/main/java/com/ptithcm/controller/DangKyController.java) (ví dụ `/api/register`, `/api/cancel`) sử dụng `@ResponseBody` để tự động trả về JSON.
*   **AJAX Client-side:** Sử dụng Fetch API trong [main.js](../../src/main/webapp/resources/js/main.js) để gửi/nhận dữ liệu động không cần tải lại toàn bộ trang web.
