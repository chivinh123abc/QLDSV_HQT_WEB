package com.ptithcm.shared.constants;

public final class MessageConstant {
    public static final String LOGIN_FAILED = "Sai tên đăng nhập hoặc mật khẩu!";
    public static final String STUDENT_NOT_EXIST = "Sinh viên không tồn tại!";
    public static final String STUDENT_ON_LEAVE = "Sinh viên đang trong trạng thái nghỉ học!";
    public static final String CLASS_NOT_EXIST = "Lớp tín chỉ không tồn tại!";
    public static final String CLASS_CANCELLED = "Lớp tín chỉ đã bị hủy!";
    public static final String ALREADY_REGISTERED_SUBJECT = "Sinh viên đã đăng ký môn học này trong học kỳ này rồi!";
    public static final String ALREADY_REGISTERED_CLASS = "Sinh viên đã đăng ký lớp tín chỉ này rồi!";
    public static final String CANNOT_CANCEL_GRADED = "Không thể hủy đăng ký vì lớp tín chỉ này đã được nhập điểm!";
    public static final String SUCCESS_REGISTER = "Đăng ký thành công";
    public static final String SUCCESS_UPDATE = "Cập nhật thành công";
    public static final String SUCCESS_DELETE = "Xóa thành công";

    // Profile & Password Change Messages
    public static final String REQUIRE_LOGIN = "Vui lòng đăng nhập để thực hiện tác vụ này!";
    public static final String EMAIL_NOT_FOUND = "Không tìm thấy địa chỉ email hợp lệ liên kết với tài khoản!";
    public static final String OTP_SENT_SUCCESS = "Mã OTP xác thực đã được gửi tới email %s!";
    public static final String OTP_SEND_ERROR = "Có lỗi xảy ra khi gửi mã OTP. Vui lòng thử lại sau!";
    public static final String USER_NOT_FOUND = "Không tìm thấy tài khoản người dùng!";
    public static final String USER_EMAIL_EMPTY = "Tài khoản chưa được liên kết với email!";
    public static final String OTP_INVALID = "Mã OTP không hợp lệ hoặc đã hết hạn!";
    public static final String OLD_PASSWORD_INCORRECT = "Mật khẩu cũ không chính xác!";
    public static final String PASSWORD_MUST_BE_DIFFERENT = "Mật khẩu mới không được trùng với mật khẩu cũ!";
    public static final String PASSWORD_CHANGE_SUCCESS = "Đổi mật khẩu thành công!";
    public static final String PASSWORD_CHANGE_ERROR = "Có lỗi xảy ra khi cập nhật mật khẩu. Vui lòng thử lại sau!";

    // Payment Messages
    public static final String PAYMENT_NO_SUBJECT = "Không có môn học nào cần thanh toán!";
    public static final String PAYMENT_METHOD_UNSUPPORTED = "Cổng thanh toán không hỗ trợ!";
    public static final String PAYMENT_CONNECTION_ERROR_TEMPLATE = "Không thể kết nối đến cổng thanh toán %s. Vui lòng thử lại sau.";
    public static final String PAYMENT_CONNECTION_FAILED = "Lỗi xử lý kết nối cổng thanh toán!";
    public static final String PAYMENT_SIGNATURE_INVALID = "Thông tin xác thực thanh toán không hợp lệ!";
    public static final String PAYMENT_SUCCESS = "Thanh toán thành công!";
    public static final String PAYMENT_RECORD_ERROR = "Lỗi ghi nhận thanh toán vào hệ thống!";
    public static final String PAYMENT_FAILED_TEMPLATE = "Thanh toán thất bại hoặc đã bị hủy (Mã lỗi: %s)";

    private MessageConstant() {
    }
}
