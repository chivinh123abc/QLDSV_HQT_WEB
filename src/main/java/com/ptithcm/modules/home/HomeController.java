package com.ptithcm.modules.home;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ptithcm.entities.SinhVien;
import com.ptithcm.shared.enums.RoleEnum;
import com.ptithcm.shared.utils.SessionUtil;

@Controller
public class HomeController {

    @Autowired
    private HomeService homeService;

    @RequestMapping({"/", "/index", "/home"})
    public String index(ModelMap model, HttpSession httpSession) {
        String role = SessionUtil.getRole(httpSession);
        if (RoleEnum.SINHVIEN.getCode().equals(role)) {
            SinhVien profile = SessionUtil.getStudentProfile(httpSession);
            if (profile != null) {
                Long registeredCount = homeService.getRegisteredCount(profile.getMaSV());
                model.addAttribute("registeredCount", registeredCount);
            }
            return "index";
        }

        Long studentCount = homeService.getStudentCount();
        Long classCount = homeService.getClassCount();
        Long subjectCount = homeService.getSubjectCount();
        Long creditClassCount = homeService.getCreditClassCount();

        model.addAttribute("studentCount", studentCount);
        model.addAttribute("classCount", classCount);
        model.addAttribute("subjectCount", subjectCount);
        model.addAttribute("creditClassCount", creditClassCount);

        return "index";
    }
}
