package com.ptithcm.shared.interceptor;

import com.ptithcm.entity.GiangVien;
import com.ptithcm.entity.SinhVien;
import com.ptithcm.shared.dto.UserSession;
import com.ptithcm.shared.enumtype.RoleEnum;
import com.ptithcm.shared.util.SecurityUtil;
import com.ptithcm.shared.util.SessionUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

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
                                    // 1. Kiểm tra Sinh viên
                                    String svHql = "FROM SinhVien WHERE TRIM(maSV) = :username AND password = :password";
                                    Query<SinhVien> svQuery = hSession.createQuery(svHql, SinhVien.class);
                                    svQuery.setParameter("username", username);
                                    svQuery.setParameter("password", password);
                                    SinhVien sv = svQuery.uniqueResult();

                                    if (sv != null) {
                                        UserSession user = new UserSession(sv.getMaSV(), "SINHVIEN", null,
                                                sv.getHo() + " " + sv.getTen());
                                        SessionUtil.setUser(session, user);
                                        SessionUtil.setRole(session, RoleEnum.SINHVIEN.getCode());
                                        SessionUtil.setStudentProfile(session, sv);
                                    } else {
                                        // 2. Kiểm tra Giảng viên
                                        String gvHql = "FROM GiangVien WHERE TRIM(maGV) = :username AND password = :password";
                                        Query<GiangVien> gvQuery = hSession.createQuery(gvHql, GiangVien.class);
                                        gvQuery.setParameter("username", username);
                                        gvQuery.setParameter("password", password);
                                        GiangVien gv = gvQuery.uniqueResult();

                                        if (gv != null) {
                                            UserSession user = new UserSession(gv.getMaGV(), gv.getRole(),
                                                    gv.getMaKhoa(), gv.getHo() + " " + gv.getTen());
                                            SessionUtil.setUser(session, user);
                                            SessionUtil.setRole(session, user.getRole());
                                            if (RoleEnum.KHOA.getCode().equals(user.getRole())) {
                                                SessionUtil.setMaKhoa(session, gv.getMaKhoa());
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
        if (uri.contains("/login") || uri.contains("/resources/")) {
            return true;
        }

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (SessionUtil.getUser(session) == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        return true;
    }
}
