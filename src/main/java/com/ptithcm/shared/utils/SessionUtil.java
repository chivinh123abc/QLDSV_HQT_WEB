package com.ptithcm.shared.utils;

import jakarta.servlet.http.HttpSession;

import com.ptithcm.entities.SinhVien;
import com.ptithcm.shared.constants.SessionConstant;
import com.ptithcm.shared.dtos.UserSession;

public final class SessionUtil {

    private SessionUtil() {
    }

    /**
     * Lấy thuộc tính currentUser từ session.
     *
     * @param session
     *            Đối tượng HttpSession hiện tại
     * @return Đối tượng UserSession, hoặc null nếu không tồn tại
     */
    public static UserSession getUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object user = session.getAttribute(SessionConstant.USER);
        return user instanceof UserSession ? (UserSession) user : null;
    }

    /**
     * Thiết lập thuộc tính currentUser vào session.
     *
     * @param session
     *            Đối tượng HttpSession hiện tại
     * @param user
     *            Đối tượng UserSession cần lưu
     */
    public static void setUser(HttpSession session, UserSession user) {
        if (session != null) {
            session.setAttribute(SessionConstant.USER, user);
        }
    }

    /**
     * Lấy tên đăng nhập (username) của người dùng từ session.
     *
     * @param session
     *            Đối tượng HttpSession hiện tại
     * @return Tên đăng nhập dạng chuỗi, hoặc null nếu không tồn tại
     */
    public static String getCurrentUsername(HttpSession session) {
        UserSession user = getUser(session);
        return user != null ? user.getUsername() : null;
    }

    /**
     * Lấy vai trò (role) của người dùng từ session.
     *
     * @param session
     *            Đối tượng HttpSession hiện tại
     * @return Chuỗi mã vai trò, hoặc null nếu không tồn tại
     */
    public static String getRole(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object role = session.getAttribute(SessionConstant.ROLE);
        return role instanceof String ? (String) role : null;
    }

    /**
     * Thiết lập vai trò (role) của người dùng vào session.
     *
     * @param session
     *            Đối tượng HttpSession hiện tại
     * @param role
     *            Chuỗi mã vai trò cần lưu
     */
    public static void setRole(HttpSession session, String role) {
        if (session != null) {
            session.setAttribute(SessionConstant.ROLE, role);
        }
    }

    /**
     * Lấy thông tin hồ sơ sinh viên (student profile) từ session.
     *
     * @param session
     *            Đối tượng HttpSession hiện tại
     * @return Đối tượng SinhVien, hoặc null nếu không tồn tại
     */
    public static SinhVien getStudentProfile(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object profile = session.getAttribute(SessionConstant.STUDENT_PROFILE);
        return profile instanceof SinhVien ? (SinhVien) profile : null;
    }

    /**
     * Thiết lập thông tin hồ sơ sinh viên (student profile) vào session.
     *
     * @param session
     *            Đối tượng HttpSession hiện tại
     * @param profile
     *            Đối tượng SinhVien cần lưu
     */
    public static void setStudentProfile(HttpSession session, SinhVien profile) {
        if (session != null) {
            session.setAttribute(SessionConstant.STUDENT_PROFILE, profile);
        }
    }

    /**
     * Lấy mã khoa (maKhoa) của người dùng đăng nhập từ session.
     *
     * @param session
     *            Đối tượng HttpSession hiện tại
     * @return Mã khoa dạng chuỗi, hoặc null nếu không tồn tại
     */
    public static String getMaKhoa(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object maKhoa = session.getAttribute(SessionConstant.MA_KHOA);
        return maKhoa instanceof String ? (String) maKhoa : null;
    }

    /**
     * Thiết lập mã khoa (maKhoa) của người dùng đăng nhập vào session.
     *
     * @param session
     *            Đối tượng HttpSession hiện tại
     * @param maKhoa
     *            Mã khoa cần lưu
     */
    public static void setMaKhoa(HttpSession session, String maKhoa) {
        if (session != null) {
            session.setAttribute(SessionConstant.MA_KHOA, maKhoa);
        }
    }

    /**
     * Hủy hiệu lực (invalidate) của session hiện tại.
     *
     * @param session
     *            Đối tượng HttpSession cần hủy
     */
    public static void invalidate(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }
}
