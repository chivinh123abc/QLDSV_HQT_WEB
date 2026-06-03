package com.ptithcm.modules.registration;

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
import com.ptithcm.modules.registration.dtos.CourseRegistrationDTO;
import com.ptithcm.shared.constants.CacheConstant;
import com.ptithcm.shared.enums.RegistrationStatus;
import com.ptithcm.shared.services.RedisService;
import com.ptithcm.shared.utils.SessionUtil;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private RedisService redisService;

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
    public String registerCourse(@Valid @ModelAttribute("courseRegDto") CourseRegistrationDTO dto,
            BindingResult bindingResult, HttpSession session, RedirectAttributes redirectAttributes) {
        String role = SessionUtil.getRole(session);
        SinhVien studentProfile = SessionUtil.getStudentProfile(session);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Lỗi nhập liệu: " + bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).collect(Collectors.joining("<br>")));
            if (!"SINHVIEN".equals(role) && studentProfile != null) {
                return "redirect:/registration?maSV=" + studentProfile.getMaSV().trim().toUpperCase();
            }
            return "redirect:/registration";
        }

        try {
            if (studentProfile == null) {
                throw new Exception("Vui lòng tra cứu sinh viên trước khi đăng ký!");
            }
            String targetMaSV = studentProfile.getMaSV();
            String maLTC = dto.getMaLTC().trim();

            // Push to Redis Queue
            String jsonPayload = String.format("{\"maLTC\":\"%s\",\"maSV\":\"%s\"}", maLTC, targetMaSV.trim());
            String statusKey = CacheConstant.getRegStatusKey(targetMaSV, maLTC);

            // Set status to PROCESSING with TTL
            redisService.set(statusKey, RegistrationStatus.PROCESSING.name(),
                    CacheConstant.REGISTRATION_STATUS_TTL_SECONDS);

            // Push payload to Redis Queue list
            redisService.lpush(CacheConstant.QUEUE_REGISTRATION, jsonPayload);

            return "redirect:/registration/processing?maLTC=" + maLTC;
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (e.getCause() != null) {
                errorMsg = e.getCause().getMessage();
            }
            redirectAttributes.addFlashAttribute("error", errorMsg);
            if (!"SINHVIEN".equals(role) && studentProfile != null) {
                return "redirect:/registration?maSV=" + studentProfile.getMaSV().trim().toUpperCase();
            }
            return "redirect:/registration";
        }
    }

    @PostMapping(params = "btnDelete")
    public String cancelCourse(@Valid @ModelAttribute("courseRegDto") CourseRegistrationDTO dto,
            BindingResult bindingResult, HttpSession session, RedirectAttributes redirectAttributes) {
        String role = SessionUtil.getRole(session);
        SinhVien studentProfile = SessionUtil.getStudentProfile(session);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Lỗi nhập liệu: " + bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).collect(Collectors.joining("<br>")));
            if (!"SINHVIEN".equals(role) && studentProfile != null) {
                return "redirect:/registration?maSV=" + studentProfile.getMaSV().trim().toUpperCase();
            }
            return "redirect:/registration";
        }

        try {
            if (studentProfile == null) {
                throw new Exception("Vui lòng chọn sinh viên trước khi hủy!");
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

        if (!"SINHVIEN".equals(role) && studentProfile != null) {
            return "redirect:/registration?maSV=" + studentProfile.getMaSV().trim().toUpperCase();
        }
        return "redirect:/registration";
    }

    @GetMapping("/processing")
    public String processingPage(@RequestParam("maLTC") String maLTC, HttpSession session, ModelMap model,
            RedirectAttributes redirectAttributes) {
        String role = SessionUtil.getRole(session);
        SinhVien studentProfile = SessionUtil.getStudentProfile(session);
        if (studentProfile == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin sinh viên!");
            return "redirect:/registration";
        }

        String maSV = studentProfile.getMaSV();
        String statusKey = CacheConstant.getRegStatusKey(maSV, maLTC);
        String status = redisService.get(statusKey);

        if (status == null) {
            redirectAttributes.addFlashAttribute("error", "Yêu cầu đăng ký đã hết hạn hoặc không tồn tại!");
            if (!"SINHVIEN".equals(role)) {
                return "redirect:/registration?maSV=" + maSV.trim().toUpperCase();
            }
            return "redirect:/registration";
        }

        if (RegistrationStatus.PROCESSING.name().equals(status)) {
            model.addAttribute("maLTC", maLTC);
            model.addAttribute("maSV", maSV);
            return "registration/processing";
        } else if (RegistrationStatus.SUCCESS.name().equals(status)) {
            redirectAttributes.addFlashAttribute("message", "Đăng ký môn học thành công!");
            redisService.delete(statusKey);
            if (!"SINHVIEN".equals(role)) {
                return "redirect:/registration?maSV=" + maSV.trim().toUpperCase();
            }
            return "redirect:/registration";
        } else if (status.startsWith(RegistrationStatus.FAILED.name() + ":")) {
            String reason = status.substring(RegistrationStatus.FAILED.name().length() + 1);
            redirectAttributes.addFlashAttribute("error", "Đăng ký thất bại: " + reason);
            redisService.delete(statusKey);
            if (!"SINHVIEN".equals(role)) {
                return "redirect:/registration?maSV=" + maSV.trim().toUpperCase();
            }
            return "redirect:/registration";
        }

        model.addAttribute("maLTC", maLTC);
        model.addAttribute("maSV", maSV);
        return "registration/processing";
    }
}
