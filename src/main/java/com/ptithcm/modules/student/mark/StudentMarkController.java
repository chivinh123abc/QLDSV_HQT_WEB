package com.ptithcm.modules.student.mark;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ptithcm.entities.SinhVien;
import com.ptithcm.modules.mark.MarkService;
import com.ptithcm.shared.utils.SessionUtil;

@Controller
@RequestMapping("/student/mark")
public class StudentMarkController {

    @Autowired
    private MarkService markService;

    @GetMapping
    public String studentGrades(ModelMap model, HttpSession httpSession) {
        SinhVien profile = SessionUtil.getStudentProfile(httpSession);
        if (profile == null) {
            return "redirect:/login";
        }

        List<Object[]> marksList = markService.getStudentGrades(profile.getMaSV().trim());

        Map<String, List<Object[]>> groupedMarks = new LinkedHashMap<>();
        for (Object[] row : marksList) {
            String nienKhoa = (String) row[0];
            Integer hocKy = (Integer) row[1];
            String semesterKey = "Học kỳ " + hocKy + " - Năm học " + nienKhoa;
            if (!groupedMarks.containsKey(semesterKey)) {
                groupedMarks.put(semesterKey, new ArrayList<>());
            }
            groupedMarks.get(semesterKey).add(row);
        }

        model.addAttribute("groupedMarks", groupedMarks);
        model.addAttribute("student", profile);

        return "student/mark/index";
    }
}
