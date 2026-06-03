package com.ptithcm.shared.interceptors;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ptithcm.entities.GiangVien;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.entities.TaiKhoan;
import com.ptithcm.shared.dtos.UserSession;
import com.ptithcm.shared.enums.RoleEnum;
import com.ptithcm.shared.enums.TrangThaiTaiKhoan;
import com.ptithcm.shared.utils.SecurityUtil;
import com.ptithcm.shared.utils.SessionUtil;

public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private SessionFactory factory;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {
        HttpSession session = request.getSession();

        // Cố gắng khôi phục session từ cookie nếu user null
        if (SessionUtil.getUser(session) == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("remember_me".equals(cookie.getName())) {
                        try {
                            String[] parts = SecurityUtil.decodeCookie(cookie.getValue());
                            if (parts != null && parts.length == 2 && factory != null) {
                                String username = parts[0];
                                String password = parts[1];

                                Session hSession = factory.openSession();
                                try {
                                    // 1. Truy vấn TaiKhoan theo tenDangNhap
                                    String tkHql = "FROM TaiKhoan WHERE tenDangNhap = :username";
                                    Query<TaiKhoan> tkQuery = hSession.createQuery(tkHql, TaiKhoan.class);
                                    tkQuery.setParameter("username", username);
                                    TaiKhoan tk = tkQuery.uniqueResult();

                                    if (tk != null && tk.getTrangThai() == TrangThaiTaiKhoan.DA_KICH_HOAT
                                            && BCrypt.checkpw(password, tk.getMatKhau())) {
                                        if ("SINHVIEN".equals(tk.getPhanQuyen())) {
                                            String svHql = "FROM SinhVien WHERE maSV = :username";
                                            Query<SinhVien> svQuery = hSession.createQuery(svHql, SinhVien.class);
                                            svQuery.setParameter("username", username);
                                            SinhVien sv = svQuery.uniqueResult();

                                            if (sv != null) {
                                                UserSession user = new UserSession(sv.getMaSV(), "SINHVIEN", null,
                                                        sv.getHo() + " " + sv.getTen());
                                                SessionUtil.setUser(session, user);
                                                SessionUtil.setRole(session, RoleEnum.SINHVIEN.getCode());
                                                SessionUtil.setStudentProfile(session, sv);
                                            }
                                        } else {
                                            String gvHql = "FROM GiangVien WHERE maGV = :username";
                                            Query<GiangVien> gvQuery = hSession.createQuery(gvHql, GiangVien.class);
                                            gvQuery.setParameter("username", username);
                                            GiangVien gv = gvQuery.uniqueResult();

                                            if (gv != null) {
                                                UserSession user = new UserSession(gv.getMaGV(), tk.getPhanQuyen(),
                                                        gv.getMaKhoa(), gv.getHo() + " " + gv.getTen());
                                                SessionUtil.setUser(session, user);
                                                SessionUtil.setRole(session, user.getRole());
                                                if (RoleEnum.KHOA.getCode().equals(user.getRole())) {
                                                    SessionUtil.setMaKhoa(session, gv.getMaKhoa());
                                                }
                                            }
                                        }
                                    }
                                } finally {
                                    hSession.close();
                                }
                            }
                        } catch (Exception e) {
                            // Cookie không hợp lệ, bỏ qua
                        }
                    }
                }
            }
        }

        // Bỏ qua chuyển hướng interceptor cho trang đăng nhập hoặc tài nguyên tĩnh
        String uri = request.getRequestURI();

        // 1. Luồng kích hoạt tài khoản
        if (uri.contains("/activate")) {
            if (session.getAttribute("temp_username") != null) {
                return true;
            }
            UserSession currUser = SessionUtil.getUser(session);
            if (currUser != null) {
                // Nếu đã đăng nhập chính thức, kiểm tra xem có thực sự CHUA_KICH_HOAT không
                boolean isUnactivated = false;
                Session hSession = factory.openSession();
                try {
                    TaiKhoan tk = hSession.get(TaiKhoan.class, currUser.getUsername());
                    if (tk != null && tk.getTrangThai() == TrangThaiTaiKhoan.CHUA_KICH_HOAT) {
                        isUnactivated = true;
                    }
                } finally {
                    hSession.close();
                }
                if (isUnactivated) {
                    return true;
                }
            }
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        // 2. Chặn truy cập nếu tài khoản đang chờ kích hoạt
        if (session.getAttribute("temp_username") != null) {
            if (uri.contains("/login") || uri.contains("/logout") || uri.contains("/resources/")) {
                return true;
            }
            response.sendRedirect(request.getContextPath() + "/activate");
            return false;
        }

        // 3. Phòng hờ: nếu user có session chính thức nhưng DB báo CHUA_KICH_HOAT
        UserSession currUser = SessionUtil.getUser(session);
        if (currUser != null) {
            boolean isUnactivated = false;
            Session hSession = factory.openSession();
            try {
                TaiKhoan tk = hSession.get(TaiKhoan.class, currUser.getUsername());
                if (tk != null && tk.getTrangThai() == TrangThaiTaiKhoan.CHUA_KICH_HOAT) {
                    isUnactivated = true;
                }
            } finally {
                hSession.close();
            }
            if (isUnactivated) {
                if (uri.contains("/login") || uri.contains("/logout") || uri.contains("/resources/")) {
                    return true;
                }
                response.sendRedirect(request.getContextPath() + "/activate");
                return false;
            }
        }

        if (uri.contains("/login") || uri.contains("/resources/") || uri.contains("/logout")
                || uri.contains("/activate")) {
            return true;
        }

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (currUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        return true;
    }
}
