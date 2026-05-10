package com.ptithcm.controller;

import java.util.List;

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

import com.ptithcm.entity.SinhVien;
import com.ptithcm.entity.Users;

import jakarta.servlet.http.HttpSession;

@Controller
@Transactional
public class LoginController {

    @Autowired
    private SessionFactory factory;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String handleLogin(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("type") String type,
                             HttpSession session,
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
            
            if (user.getRoleId() == 1) {
                session.setAttribute("role", "PGV");
                return "redirect:/index";
            } else if (user.getRoleId() == 2) {
                session.setAttribute("role", "KHOA");
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
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
