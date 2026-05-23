package com.ptithcm.interceptor;

import com.ptithcm.entity.SinhVien;
import com.ptithcm.entity.Users;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Base64;
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

        // Try to restore session from cookie if null
        if (session.getAttribute("user") == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("remember_me".equals(cookie.getName())) {
                        try {
                            String decoded = new String(Base64.getDecoder().decode(cookie.getValue()), "UTF-8");
                            String[] parts = decoded.split(":");
                            if (parts.length == 2 && factory != null) {
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
                                        session.setAttribute("user", user);
                                        if (user.getRoleId() == 1)
                                            session.setAttribute("role", "PGV");
                                        else if (user.getRoleId() == 2)
                                            session.setAttribute("role", "KHOA");
                                        else if (user.getRoleId() == 3) {
                                            session.setAttribute("role", "SINHVIEN");
                                            String svHql = "FROM SinhVien WHERE TRIM(maSV) = :maSV";
                                            Query<SinhVien> svQuery = hSession.createQuery(svHql, SinhVien.class);
                                            svQuery.setParameter("maSV", user.getUsername());
                                            SinhVien svProfile = svQuery.uniqueResult();
                                            if (svProfile != null)
                                                session.setAttribute("studentProfile", svProfile);
                                        }
                                    }
                                } finally {
                                    hSession.close();
                                }
                            }
                        } catch (Exception e) {
                            // Invalid cookie, ignore
                        }
                    }
                }
            }
        }

        // Skip interceptor redirect for login page
        String uri = request.getRequestURI();
        if (uri.contains("/login") || uri.contains("/resources/")) {
            return true;
        }

        // Check if user is logged in
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        return true;
    }
}
