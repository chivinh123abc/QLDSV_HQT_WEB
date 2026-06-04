package com.ptithcm.modules.student;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptithcm.entities.SinhVien;
import com.ptithcm.entities.TaiKhoan;
import com.ptithcm.modules.account.AccountService;
import com.ptithcm.shared.constants.MessageConstant;
import com.ptithcm.shared.dtos.UserSession;
import com.ptithcm.shared.services.EmailOTPService;
import com.ptithcm.shared.utils.SessionUtil;

@Controller
@RequestMapping("/student")
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailOTPService emailOTPService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ServletContext servletContext;

    @GetMapping("/profile")
    public String showProfile(ModelMap model, HttpSession session) {
        String role = SessionUtil.getRole(session);
        if (!"SINHVIEN".equals(role)) {
            log.warn("[STUDENT-PORTAL] Unauthorized profile access attempt. Redirecting to home.");
            return "redirect:/";
        }

        String username = SessionUtil.getCurrentUsername(session);
        if (username == null) {
            log.warn("[STUDENT-PORTAL] No username found in session. Redirecting to home.");
            return "redirect:/";
        }

        TaiKhoan account = accountService.getAccountById(username);
        if (account == null) {
            log.error("[STUDENT-PORTAL] Account not found for username: {}", username);
            return "redirect:/";
        }

        model.addAttribute("account", account);

        SinhVien studentProfile = SessionUtil.getStudentProfile(session);
        if (studentProfile == null) {
            studentProfile = studentService.getStudentById(username);
            if (studentProfile != null) {
                SessionUtil.setStudentProfile(session, studentProfile);
            }
        }
        model.addAttribute("studentProfile", studentProfile);

        return "student/profile";
    }

    @PostMapping("/send-otp")
    public String sendOtp(HttpSession session, RedirectAttributes redirectAttributes) {
        String role = SessionUtil.getRole(session);
        if (!"SINHVIEN".equals(role)) {
            return "redirect:/";
        }

        String username = SessionUtil.getCurrentUsername(session);
        if (username == null) {
            redirectAttributes.addFlashAttribute("errorMsg", MessageConstant.REQUIRE_LOGIN);
            return "redirect:/";
        }

        try {
            TaiKhoan account = accountService.getAccountById(username);
            if (account == null || account.getEmail() == null || account.getEmail().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMsg", MessageConstant.EMAIL_NOT_FOUND);
                return "redirect:/student/profile";
            }

            String email = account.getEmail().trim();
            emailOTPService.sendOTP(email);

            redirectAttributes.addFlashAttribute("successMsg",
                    String.format(MessageConstant.OTP_SENT_SUCCESS, maskEmail(email)));
        } catch (Exception e) {
            log.error("[STUDENT-PORTAL] Error sending OTP for user: {}", username, e);
            redirectAttributes.addFlashAttribute("errorMsg", MessageConstant.OTP_SEND_ERROR);
        }

        return "redirect:/student/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword, @RequestParam("otpCode") String otpCode,
            HttpSession session, RedirectAttributes redirectAttributes) {

        String role = SessionUtil.getRole(session);
        if (!"SINHVIEN".equals(role)) {
            return "redirect:/";
        }

        String username = SessionUtil.getCurrentUsername(session);
        if (username == null) {
            redirectAttributes.addFlashAttribute("errorMsg", MessageConstant.REQUIRE_LOGIN);
            return "redirect:/";
        }

        try {
            TaiKhoan account = accountService.getAccountById(username);
            if (account == null) {
                redirectAttributes.addFlashAttribute("errorMsg", MessageConstant.USER_NOT_FOUND);
                return "redirect:/student/profile";
            }

            String email = account.getEmail();
            if (email == null || email.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMsg", MessageConstant.USER_EMAIL_EMPTY);
                return "redirect:/student/profile";
            }

            if (!emailOTPService.verifyOTP(email, otpCode)) {
                redirectAttributes.addFlashAttribute("errorMsg", MessageConstant.OTP_INVALID);
                return "redirect:/student/profile";
            }

            if (!BCrypt.checkpw(oldPassword, account.getMatKhau())) {
                redirectAttributes.addFlashAttribute("errorMsg", MessageConstant.OLD_PASSWORD_INCORRECT);
                return "redirect:/student/profile";
            }

            if (oldPassword.trim().equals(newPassword.trim())) {
                redirectAttributes.addFlashAttribute("errorMsg", MessageConstant.PASSWORD_MUST_BE_DIFFERENT);
                return "redirect:/student/profile";
            }

            String hashedNewPassword = BCrypt.hashpw(newPassword.trim(), BCrypt.gensalt(12));
            account.setMatKhau(hashedNewPassword);
            accountService.updateAccount(account);

            emailOTPService.deleteOTP(email);

            redirectAttributes.addFlashAttribute("successMsg", MessageConstant.PASSWORD_CHANGE_SUCCESS);
        } catch (Exception e) {
            log.error("[STUDENT-PORTAL] Error changing password for user: {}", username, e);
            redirectAttributes.addFlashAttribute("errorMsg", MessageConstant.PASSWORD_CHANGE_ERROR);
        }

        return "redirect:/student/profile";
    }

    @PostMapping("/update-avatar")
    public String updateAvatar(@RequestParam("avatarFile") MultipartFile file, HttpSession session,
            RedirectAttributes redirectAttributes) {

        String role = SessionUtil.getRole(session);
        if (!"SINHVIEN".equals(role)) {
            return "redirect:/";
        }

        String username = SessionUtil.getCurrentUsername(session);
        if (username == null) {
            redirectAttributes.addFlashAttribute("errorMsg", MessageConstant.REQUIRE_LOGIN);
            return "redirect:/";
        }

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMsg", "Vui lòng chọn ảnh!");
            return "redirect:/student/profile";
        }

        if (file.getSize() > 2 * 1024 * 1024) {
            redirectAttributes.addFlashAttribute("errorMsg", "Kích thước file không được vượt quá 2MB!");
            return "redirect:/student/profile";
        }

        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        boolean isValidType = contentType != null && (contentType.equals("image/jpeg")
                || contentType.equals("image/png") || contentType.equals("image/jpg"));
        boolean isValidExt = originalFilename != null && (originalFilename.toLowerCase().endsWith(".png")
                || originalFilename.toLowerCase().endsWith(".jpg") || originalFilename.toLowerCase().endsWith(".jpeg"));

        if (!isValidType || !isValidExt) {
            redirectAttributes.addFlashAttribute("errorMsg", "Chỉ chấp nhận file ảnh định dạng PNG hoặc JPEG!");
            return "redirect:/student/profile";
        }

        try {
            TaiKhoan account = accountService.getAccountById(username);
            if (account == null) {
                redirectAttributes.addFlashAttribute("errorMsg", "Không tìm thấy thông tin tài khoản!");
                return "redirect:/student/profile";
            }

            String uploadDir = servletContext.getRealPath("/resources/uploads/avatars/");
            if (uploadDir == null) {
                uploadDir = "/usr/local/tomcat/webapps/ROOT/resources/uploads/avatars/";
            }
            java.io.File dir = new java.io.File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            } else {
                extension = ".png";
            }
            String newFilename = java.util.UUID.randomUUID().toString() + extension;

            java.nio.file.Path path = java.nio.file.Paths.get(uploadDir + newFilename);
            java.nio.file.Files.write(path, file.getBytes());

            String avatarUrl = "/resources/uploads/avatars/" + newFilename;
            account.setAvatar(avatarUrl);
            accountService.updateAccount(account);

            UserSession user = SessionUtil.getUser(session);
            if (user != null) {
                user.setAvatar(avatarUrl);
                SessionUtil.setUser(session, user);
            }

            redirectAttributes.addFlashAttribute("successMsg", "Cập nhật ảnh đại diện thành công!");
        } catch (Exception e) {
            log.error("[STUDENT-PORTAL] Error updating avatar for user: {}", username, e);
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi lưu ảnh: " + e.getMessage());
        }

        return "redirect:/student/profile";
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String name = parts[0];
        String domain = parts[1];
        if (name.length() <= 3) {
            return name + "***@" + domain;
        }
        return name.substring(0, 3) + "***@" + domain;
    }
}
