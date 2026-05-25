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
                                    String tkHql = "FROM TaiKhoan WHERE TRIM(tenDangNhap) = :username";
                                    Query<TaiKhoan> tkQuery = hSession.createQuery(tkHql, TaiKhoan.class);
                                    tkQuery.setParameter("username", username);
                                    TaiKhoan tk = tkQuery.uniqueResult();

                                    if (tk != null && tk.getTrangThai() == TrangThaiTaiKhoan.DA_KICH_HOAT
                                            && BCrypt.checkpw(password, tk.getMatKhau())) {
                                        if ("SINHVIEN".equals(tk.getPhanQuyen())) {
                                            String svHql = "FROM SinhVien WHERE TRIM(maSV) = :username";
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
                                            String gvHql = "FROM GiangVien WHERE TRIM(maGV) = :username";
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
        if (uri.contains("/login") || uri.contains("/resources/") || uri.contains("/register")
                || uri.contains("/captcha") || uri.contains("/verify")) {
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
