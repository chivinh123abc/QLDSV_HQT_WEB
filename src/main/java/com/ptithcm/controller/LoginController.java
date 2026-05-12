package com.ptithcm.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ptithcm.entity.GiangVien;
import com.ptithcm.entity.SinhVien;
import com.ptithcm.entity.Users;

import jakarta.servlet.http.HttpSession;

@Controller
@Transactional
public class LoginController {

    @Autowired
    private SessionFactory factory;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpSession session) {
        if (session.getAttribute("user") != null) {
            String role = (String) session.getAttribute("role");
            if ("SINHVIEN".equals(role)) {
                return "redirect:/registration";
            }
            return "redirect:/index";
        }
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String handleLogin(@RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("type") String type,
            HttpSession session,
            jakarta.servlet.http.HttpServletResponse response,
            ModelMap model) {
        Session hSession = factory.getCurrentSession();

        // Use USERS table for all authentications as per user's database structure
        String hql = "FROM Users WHERE username = :username AND password = :password";
        Query<Users> query = hSession.createQuery(hql, Users.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        Users user = query.uniqueResult();

        if (user != null) {
            session.setAttribute("user", user);

            // Set persistent cookie to survive server restarts
            try {
                String cookieValue = java.util.Base64.getEncoder()
                        .encodeToString((username + ":" + password).getBytes("UTF-8"));
                jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("remember_me", cookieValue);
                cookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
                cookie.setPath("/");
                response.addCookie(cookie);
            } catch (Exception e) {
                // Ignore cookie error
            }

            if (user.getRoleId() == 1) {
                session.setAttribute("role", "PGV");
                return "redirect:/index";
            } else if (user.getRoleId() == 2) {
                session.setAttribute("role", "KHOA");

                // For KHOA role, get their faculty from GIANGVIEN table
                String gvHql = "FROM GiangVien WHERE TRIM(maGV) = :maGV";
                Query<GiangVien> gvQuery = hSession.createQuery(gvHql, GiangVien.class);
                gvQuery.setParameter("maGV", user.getUsername().trim());
                GiangVien gv = gvQuery.uniqueResult();
                if (gv != null) {
                    session.setAttribute("maKhoa", gv.getMaKhoa());
                }

                return "redirect:/index";
            } else if (user.getRoleId() == 3) {
                session.setAttribute("role", "SINHVIEN");

                // For students, we might need their profile from SINHVIEN table too
                String svHql = "FROM SinhVien WHERE TRIM(maSV) = :maSV";
                Query<SinhVien> svQuery = hSession.createQuery(svHql, SinhVien.class);
                svQuery.setParameter("maSV", user.getUsername());
                SinhVien svProfile = svQuery.uniqueResult();
                if (svProfile != null) {
                    session.setAttribute("studentProfile", svProfile);
                }

                return "redirect:/registration";
            }
        }

        model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
        model.addAttribute("type", type);
        model.addAttribute("username", username);
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session, jakarta.servlet.http.HttpServletResponse response) {
        session.invalidate();

        // Clear remember_me cookie
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("remember_me", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/login";
    }
}
