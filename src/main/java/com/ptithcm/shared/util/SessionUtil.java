package com.ptithcm.shared.util;

import com.ptithcm.entity.SinhVien;
import com.ptithcm.entity.Users;
import com.ptithcm.shared.constant.SessionConstant;

import jakarta.servlet.http.HttpSession;

public final class SessionUtil {

    private SessionUtil() {
    }

    /**
     * Lấy thuộc tính currentUser từ session.
     *
     * @param session
     *            Đối tượng HttpSession hiện tại
     * @return Đối tượng Users, hoặc null nếu không tồn tại
     */
    public static Users getUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object user = session.getAttribute(SessionConstant.USER);
        return user instanceof Users ? (Users) user : null;
    }

    /**
     * Thiết lập thuộc tính currentUser vào session.
     *
     * @param session
     *            Đối tượng HttpSession hiện tại
     * @param user
     *            Đối tượng Users cần lưu
     */
    public static void setUser(HttpSession session, Users user) {
        if (session != null) {
            session.setAttribute(SessionConstant.USER, user);
        }
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
