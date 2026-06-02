package com.ptithcm.modules.registration;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptithcm.entities.DangKy;
import com.ptithcm.entities.LopTinChi;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.shared.utils.SessionUtil;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @GetMapping
    public String index(@RequestParam(value = "maSV", required = false) String maSV, HttpSession session,
            ModelMap model) {
        String role = SessionUtil.getRole(session);
        SinhVien student = null;

        if ("SINHVIEN".equals(role)) {
            student = SessionUtil.getStudentProfile(session);
        } else {
            if (maSV != null && !maSV.trim().isEmpty()) {
                student = registrationService.getStudentById(maSV.trim().toUpperCase());
                if (student != null) {
                    SessionUtil.setStudentProfile(session, student);
                } else {
                    SessionUtil.setStudentProfile(session, null);
                    model.addAttribute("error", "Không tìm thấy sinh viên!");
                }
            } else {
                student = SessionUtil.getStudentProfile(session);
            }
        }

        if (student != null) {
            model.addAttribute("selectedStudent", student);

            // Load active registrations
            String studentMaSV = student.getMaSV();
            List<DangKy> myRegistrations = registrationService.listRegistration().stream().filter(
                    r -> r.getSinhVien().getMaSV().trim().equalsIgnoreCase(studentMaSV.trim()) && !r.isHuyDangKy())
                    .toList();
            model.addAttribute("myRegistrations", myRegistrations);

            // Expose registered LTC IDs
            List<String> registeredLtcIds = myRegistrations.stream().map(r -> r.getLopTinChi().getMaLTC()).toList();
            model.addAttribute("registeredLtcIds", registeredLtcIds);

            // Expose subject semesters to prevent duplicate subject in same semester
            List<String> registeredSubjectSemesters = myRegistrations.stream()
                    .map(r -> (r.getLopTinChi().getMonHoc().getMaMH().trim().toUpperCase() + "-"
                            + r.getLopTinChi().getNienKhoa().trim().toUpperCase() + "-" + r.getLopTinChi().getHocKy()))
                    .toList();
            model.addAttribute("registeredSubjectSemesters", registeredSubjectSemesters);
        }

        // Always load available classes
        List<LopTinChi> availableClasses = registrationService.getAvailableClasses();
        model.addAttribute("availableClasses", availableClasses);

        return "registration/index";
    }

    @PostMapping(params = "btnInsert")
    public String registerCourse(@RequestParam("maLTC") String maLTC, HttpSession session,
            RedirectAttributes redirectAttributes) {
        String role = SessionUtil.getRole(session);
        SinhVien studentProfile = SessionUtil.getStudentProfile(session);

        try {
            if (studentProfile == null) {
                throw new Exception("Vui lòng tra cứu sinh viên trước khi đăng ký!");
            }
            String targetMaSV = studentProfile.getMaSV();
            registrationService.registerClass(maLTC, targetMaSV.trim());
            redirectAttributes.addFlashAttribute("message", "Đăng ký môn học thành công!");
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (e.getCause() != null) {
                errorMsg = e.getCause().getMessage();
            }
            redirectAttributes.addFlashAttribute("error", errorMsg);
        }

        if (!"SINHVIEN".equals(role) && studentProfile != null) {
            return "redirect:/registration?maSV=" + studentProfile.getMaSV().trim().toUpperCase();
        }
        return "redirect:/registration";
    }

    @PostMapping(params = "btnDelete")
    public String cancelCourse(@RequestParam("maLTC") String maLTC, HttpSession session,
            RedirectAttributes redirectAttributes) {
        String role = SessionUtil.getRole(session);
        SinhVien studentProfile = SessionUtil.getStudentProfile(session);

        try {
            if (studentProfile == null) {
                throw new Exception("Vui lòng chọn sinh viên trước khi hủy!");
            }
            String targetMaSV = studentProfile.getMaSV();
            registrationService.cancelRegistration(maLTC, targetMaSV.trim());
            redirectAttributes.addFlashAttribute("message", "Đã hủy đăng ký thành công!");
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (e.getCause() != null) {
                errorMsg = e.getCause().getMessage();
            }
            redirectAttributes.addFlashAttribute("error", errorMsg);
        }

        if (!"SINHVIEN".equals(role) && studentProfile != null) {
            return "redirect:/registration?maSV=" + studentProfile.getMaSV().trim().toUpperCase();
        }
        return "redirect:/registration";
    }
}
