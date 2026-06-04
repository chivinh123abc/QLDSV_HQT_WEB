package com.ptithcm.modules.student.registration;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptithcm.entities.DangKy;
import com.ptithcm.entities.LopTinChi;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.modules.registration.RegistrationService;
import com.ptithcm.modules.registration.dtos.CourseRegistrationDTO;
import com.ptithcm.shared.constants.CacheConstant;
import com.ptithcm.shared.enums.RegistrationStatus;
import com.ptithcm.shared.services.RedisService;
import com.ptithcm.shared.utils.SessionUtil;

@Controller
@RequestMapping("/student/registration")
public class StudentRegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private RedisService redisService;

    @GetMapping
    public String index(HttpSession session, ModelMap model) {
        SinhVien student = SessionUtil.getStudentProfile(session);

        if (student != null) {
            model.addAttribute("selectedStudent", student);

            String studentMaSV = student.getMaSV();
            List<DangKy> myRegistrations = registrationService.listRegistration().stream().filter(
                    r -> r.getSinhVien().getMaSV().trim().equalsIgnoreCase(studentMaSV.trim()) && !r.isHuyDangKy())
                    .toList();
            model.addAttribute("myRegistrations", myRegistrations);

            List<String> registeredLtcIds = myRegistrations.stream().map(r -> r.getLopTinChi().getMaLTC()).toList();
            model.addAttribute("registeredLtcIds", registeredLtcIds);

            List<String> registeredSubjectSemesters = myRegistrations.stream()
                    .map(r -> (r.getLopTinChi().getMonHoc().getMaMH().trim().toUpperCase() + "-"
                            + r.getLopTinChi().getNienKhoa().trim().toUpperCase() + "-" + r.getLopTinChi().getHocKy()))
                    .toList();
            model.addAttribute("registeredSubjectSemesters", registeredSubjectSemesters);
        }

        List<LopTinChi> availableClasses = registrationService.getAvailableClasses();
        model.addAttribute("availableClasses", availableClasses);

        return "student/registration/index";
    }

    @PostMapping(params = "btnInsert")
    public String registerCourse(@Valid @ModelAttribute("courseRegDto") CourseRegistrationDTO dto,
            BindingResult bindingResult, HttpSession session, RedirectAttributes redirectAttributes) {
        SinhVien studentProfile = SessionUtil.getStudentProfile(session);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Lỗi nhập liệu: " + bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).collect(Collectors.joining("<br>")));
            return "redirect:/student/registration";
        }

        try {
            if (studentProfile == null) {
                throw new Exception("Vui lòng đăng nhập trước khi đăng ký!");
            }
            String targetMaSV = studentProfile.getMaSV();
            String maLTC = dto.getMaLTC().trim();

            String jsonPayload = String.format("{\"maLTC\":\"%s\",\"maSV\":\"%s\"}", maLTC, targetMaSV.trim());
            String statusKey = CacheConstant.getRegStatusKey(targetMaSV, maLTC);

            redisService.set(statusKey, RegistrationStatus.PROCESSING.name(),
                    CacheConstant.REGISTRATION_STATUS_TTL_SECONDS);

            redisService.lpush(CacheConstant.QUEUE_REGISTRATION, jsonPayload);

            return "redirect:/student/registration/processing?maLTC=" + maLTC;
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (e.getCause() != null) {
                errorMsg = e.getCause().getMessage();
            }
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/student/registration";
        }
    }

    @PostMapping(params = "btnDelete")
    public String cancelCourse(@Valid @ModelAttribute("courseRegDto") CourseRegistrationDTO dto,
            BindingResult bindingResult, HttpSession session, RedirectAttributes redirectAttributes) {
        SinhVien studentProfile = SessionUtil.getStudentProfile(session);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Lỗi nhập liệu: " + bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).collect(Collectors.joining("<br>")));
            return "redirect:/student/registration";
        }

        try {
            if (studentProfile == null) {
                throw new Exception("Vui lòng đăng nhập trước khi hủy!");
            }
            String targetMaSV = studentProfile.getMaSV();
            registrationService.cancelRegistration(dto.getMaLTC(), targetMaSV.trim());
            redirectAttributes.addFlashAttribute("message", "Đã hủy đăng ký thành công!");
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (e.getCause() != null) {
                errorMsg = e.getCause().getMessage();
            }
            redirectAttributes.addFlashAttribute("error", errorMsg);
        }

        return "redirect:/student/registration";
    }

    @GetMapping("/processing")
    public String processingPage(@RequestParam("maLTC") String maLTC, HttpSession session, ModelMap model,
            RedirectAttributes redirectAttributes) {
        SinhVien studentProfile = SessionUtil.getStudentProfile(session);
        if (studentProfile == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin sinh viên!");
            return "redirect:/student/registration";
        }

        String maSV = studentProfile.getMaSV();
        String statusKey = CacheConstant.getRegStatusKey(maSV, maLTC);
        String status = redisService.get(statusKey);

        if (status == null) {
            redirectAttributes.addFlashAttribute("error", "Yêu cầu đăng ký đã hết hạn hoặc không tồn tại!");
            return "redirect:/student/registration";
        }

        if (RegistrationStatus.PROCESSING.name().equals(status)) {
            model.addAttribute("maLTC", maLTC);
            model.addAttribute("maSV", maSV);
            return "student/registration/processing";
        } else if (RegistrationStatus.SUCCESS.name().equals(status)) {
            redirectAttributes.addFlashAttribute("message", "Đăng ký môn học thành công!");
            redisService.delete(statusKey);
            return "redirect:/student/registration";
        } else if (status.startsWith(RegistrationStatus.FAILED.name() + ":")) {
            String reason = status.substring(RegistrationStatus.FAILED.name().length() + 1);
            redirectAttributes.addFlashAttribute("error", "Đăng ký thất bại: " + reason);
            redisService.delete(statusKey);
            return "redirect:/student/registration";
        }

        model.addAttribute("maLTC", maLTC);
        model.addAttribute("maSV", maSV);
        return "student/registration/processing";
    }
}
