package com.ptithcm.shared.interceptor;

import com.ptithcm.entity.SinhVien;
import com.ptithcm.entity.Users;
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
                                    String hql = "FROM Users WHERE username = :username AND password = :password";
                                    Query<Users> query = hSession.createQuery(hql, Users.class);
                                    query.setParameter("username", username);
                                    query.setParameter("password", password);
                                    Users user = query.uniqueResult();

                                    if (user != null) {
                                        SessionUtil.setUser(session, user);
                                        if (user.getRoleId() == RoleEnum.PGV.getId()) {
                                            SessionUtil.setRole(session, RoleEnum.PGV.getCode());
                                        } else if (user.getRoleId() == RoleEnum.KHOA.getId()) {
                                            SessionUtil.setRole(session, RoleEnum.KHOA.getCode());
                                        } else if (user.getRoleId() == RoleEnum.SINHVIEN.getId()) {
                                            SessionUtil.setRole(session, RoleEnum.SINHVIEN.getCode());
                                            String svHql = "FROM SinhVien WHERE TRIM(maSV) = :maSV";
                                            Query<SinhVien> svQuery = hSession.createQuery(svHql, SinhVien.class);
                                            svQuery.setParameter("maSV", user.getUsername());
                                            SinhVien svProfile = svQuery.uniqueResult();
                                            if (svProfile != null) {
                                                SessionUtil.setStudentProfile(session, svProfile);
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
