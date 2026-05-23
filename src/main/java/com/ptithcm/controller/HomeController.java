package com.ptithcm.controller;

import com.ptithcm.entity.SinhVien;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Transactional
public class HomeController {

    @Autowired
    private SessionFactory factory;

    @RequestMapping({"/", "/index"})
    public String index(ModelMap model, HttpSession httpSession) {
        String role = (String) httpSession.getAttribute("role");
        if ("SINHVIEN".equals(role)) {
            Session session = factory.getCurrentSession();
            SinhVien profile = (SinhVien) httpSession.getAttribute("studentProfile");
            if (profile != null) {
                String hqlCount = "SELECT count(dk) FROM DangKy dk WHERE TRIM(dk.maSV) = :maSV AND (dk.huyDangKy = false OR dk.huyDangKy IS NULL)";
                Long registeredCount = session.createQuery(hqlCount, Long.class)
                        .setParameter("maSV", profile.getMaSV().trim()).uniqueResult();
                model.addAttribute("registeredCount", registeredCount);
            }
            return "index";
        }

        Session session = factory.getCurrentSession();

        Long studentCount = session.createQuery("SELECT COUNT(*) FROM SinhVien", Long.class).uniqueResult();
        Long classCount = session.createQuery("SELECT COUNT(*) FROM Lop", Long.class).uniqueResult();
        Long subjectCount = session.createQuery("SELECT COUNT(*) FROM MonHoc", Long.class).uniqueResult();
        Long creditClassCount = session.createQuery("SELECT COUNT(*) FROM LopTinChi", Long.class).uniqueResult();

        model.addAttribute("studentCount", studentCount);
        model.addAttribute("classCount", classCount);
        model.addAttribute("subjectCount", subjectCount);
        model.addAttribute("creditClassCount", creditClassCount);

        return "index";
    }
}
