package com.ptithcm.modules.auth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptithcm.entities.GiangVien;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.entities.TaiKhoan;
import com.ptithcm.modules.auth.dtos.LoginDTO;
import com.ptithcm.shared.constants.MessageConstant;
import com.ptithcm.shared.dtos.UserSession;
import com.ptithcm.shared.enums.RoleEnum;
import com.ptithcm.shared.enums.TrangThaiTaiKhoan;
import com.ptithcm.shared.services.EmailOTPService;
import com.ptithcm.shared.utils.SecurityUtil;
import com.ptithcm.shared.utils.SessionUtil;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailOTPService emailOTPService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpSession session, @RequestParam(value = "activated", required = false) String activated,
            @RequestParam(value = "error", required = false) String error, ModelMap model) {
        if (SessionUtil.getUser(session) != null) {
            UserSession currUser = SessionUtil.getUser(session);
            TaiKhoan tk = authService.getTaiKhoanByUsername(currUser.getUsername());
            if (tk != null && tk.getTrangThai() == TrangThaiTaiKhoan.CHUA_KICH_HOAT) {
                return "redirect:/auth/activate";
            }
            String role = SessionUtil.getRole(session);
            if (RoleEnum.SINHVIEN.getCode().equals(role)) {
                return "redirect:/registration";
            }
            return "redirect:/index";
        }
        if ("true".equals(activated)) {
            model.addAttribute("success", "Kích hoạt tài khoản thành công! Vui lòng đăng nhập.");
        }
        if ("locked".equals(error)) {
            model.addAttribute("error", "Tài khoản của bạn đã bị khóa!");
        }
        return "auth/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String handleLogin(@Valid @ModelAttribute("loginDto") LoginDTO loginDto, BindingResult bindingResult,
            HttpSession session, HttpServletResponse response, ModelMap model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
            model.addAttribute("username", loginDto.getUsername());
            return "auth/login";
        }

        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        TaiKhoan tk = authService.getTaiKhoanByUsername(username);
        if (tk != null) {
            if (tk.getTrangThai() == TrangThaiTaiKhoan.KHOA) {
                model.addAttribute("error", "Tài khoản của bạn đã bị khóa!");
                model.addAttribute("username", username);
                return "auth/login";
            }
            if (tk.getTrangThai() == TrangThaiTaiKhoan.CHUA_KICH_HOAT) {
                redirectAttributes.addFlashAttribute("errorMsg",
                        "Tài khoản chưa được kích hoạt, vui lòng kích hoạt lần đầu!");
                return "redirect:/auth/activate";
            }
        }

        UserSession user = authService.login(username, password);

        if (user != null) {
            SessionUtil.setUser(session, user);

            // Lưu cookie remember_me lâu dài để người dùng không cần đăng nhập lại
            try {
                String cookieValue = SecurityUtil.encodeCookie(username, password);
                if (!cookieValue.isEmpty()) {
                    jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("remember_me", cookieValue);
                    cookie.setMaxAge(30 * 24 * 60 * 60); // 30 ngày
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            } catch (Exception e) {
                // Bỏ qua lỗi lưu cookie
            }

            if (RoleEnum.PGV.getCode().equals(user.getRole())) {
                SessionUtil.setRole(session, RoleEnum.PGV.getCode());
                return "redirect:/index";
            } else if (RoleEnum.KHOA.getCode().equals(user.getRole())) {
                SessionUtil.setRole(session, RoleEnum.KHOA.getCode());

                // Với vai trò KHOA, lấy mã khoa tương ứng từ bảng GIANGVIEN
                GiangVien gv = authService.getGiangVienProfile(user.getUsername());
                if (gv != null) {
                    SessionUtil.setMaKhoa(session, gv.getMaKhoa());
                }

                return "redirect:/index";
            } else if (RoleEnum.SINHVIEN.getCode().equals(user.getRole())) {
                SessionUtil.setRole(session, RoleEnum.SINHVIEN.getCode());

                // Với vai trò SINHVIEN, lấy hồ sơ sinh viên tương ứng từ bảng SINHVIEN
                SinhVien svProfile = authService.getSinhVienProfile(user.getUsername());
                if (svProfile != null) {
                    SessionUtil.setStudentProfile(session, svProfile);
                }

                return "redirect:/registration";
            }
        }

        model.addAttribute("error", MessageConstant.LOGIN_FAILED);
        model.addAttribute("username", username);
        return "auth/login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session, HttpServletResponse response) {
        SessionUtil.invalidate(session);

        // Xóa cookie remember_me khi đăng xuất
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("remember_me", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/login";
    }

    @GetMapping("/auth/activate")
    public String activatePage(HttpSession session, ModelMap model) {
        String activatingUser = (String) session.getAttribute("ACTIVATING_USER");
        String activatingEmail = (String) session.getAttribute("ACTIVATING_EMAIL");

        if (activatingUser != null && activatingEmail != null) {
            model.addAttribute("username", activatingUser);
            model.addAttribute("email", activatingEmail);

            // Mask email for privacy
            String maskedEmail = activatingEmail;
            int atIndex = activatingEmail.indexOf("@");
            if (atIndex > 3) {
                maskedEmail = activatingEmail.substring(0, 3) + "***" + activatingEmail.substring(atIndex);
            }
            model.addAttribute("maskedEmail", maskedEmail);
            model.addAttribute("STEP_OTP_SENT", true);
        }
        return "auth/activate";
    }

    @PostMapping("/auth/activate/request-otp")
    public String requestOtp(@RequestParam("maSV") String maSV, @RequestParam("email") String email,
            HttpSession session, RedirectAttributes redirectAttributes) {
        if (maSV == null || maSV.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Mã sinh viên và email không được để trống!");
            return "redirect:/auth/activate";
        }

        maSV = maSV.trim();
        email = email.trim();

        TaiKhoan tk = authService.getTaiKhoanByUsername(maSV);
        if (tk == null) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản sinh viên không tồn tại trong hệ thống!");
            return "redirect:/auth/activate";
        }

        if (tk.getTrangThai() == TrangThaiTaiKhoan.DA_KICH_HOAT) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản này đã được kích hoạt trước đó!");
            return "redirect:/auth/activate";
        }

        if (tk.getTrangThai() == TrangThaiTaiKhoan.KHOA) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản đã bị khóa, không thể kích hoạt!");
            return "redirect:/auth/activate";
        }

        // BẮT BUỘC query DB kiểm tra Email user nhập vào từ Form PHẢI TRÙNG KHỚP 100%
        // với Email đã lưu trong DB
        if (!tk.getEmail().equals(email)) {
            redirectAttributes.addFlashAttribute("error", "Email không chính xác với thông tin tài khoản đã đăng ký!");
            return "redirect:/auth/activate";
        }

        try {
            emailOTPService.sendActivationOTP(email);
            session.setAttribute("ACTIVATING_USER", maSV);
            session.setAttribute("ACTIVATING_EMAIL", email);
            redirectAttributes.addFlashAttribute("message", "Mã OTP đã được gửi thành công đến email " + email);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể gửi OTP kích hoạt: " + e.getMessage());
        }

        return "redirect:/auth/activate";
    }

    @PostMapping("/auth/activate/confirm")
    public String confirmActivation(@RequestParam("otpCode") String otpCode,
            @RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session, RedirectAttributes redirectAttributes) {

        String username = (String) session.getAttribute("ACTIVATING_USER");
        String email = (String) session.getAttribute("ACTIVATING_EMAIL");

        if (username == null || email == null) {
            redirectAttributes.addFlashAttribute("error", "Yêu cầu kích hoạt không hợp lệ hoặc đã hết hạn!");
            return "redirect:/auth/activate";
        }

        if (otpCode == null || otpCode.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Mã OTP không được để trống!");
            return "redirect:/auth/activate";
        }

        if (newPassword == null || newPassword.trim().isEmpty() || confirmPassword == null
                || confirmPassword.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới không được để trống!");
            return "redirect:/auth/activate";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Xác nhận mật khẩu không trùng khớp!");
            return "redirect:/auth/activate";
        }

        if (!emailOTPService.verifyOTP(email, otpCode.trim())) {
            redirectAttributes.addFlashAttribute("error", "Mã OTP không chính xác hoặc đã hết hạn!");
            return "redirect:/auth/activate";
        }

        try {
            // Kích hoạt tài khoản và cập nhật mật khẩu mới
            authService.activateAccount(username, newPassword);

            // Xóa OTP và session
            emailOTPService.deleteOTP(email);
            session.removeAttribute("ACTIVATING_USER");
            session.removeAttribute("ACTIVATING_EMAIL");

            redirectAttributes.addFlashAttribute("success", "Kích hoạt tài khoản thành công! Vui lòng đăng nhập.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi kích hoạt tài khoản: " + e.getMessage());
            return "redirect:/auth/activate";
        }
    }

    @GetMapping("/auth/activate/reset")
    public String resetActivation(HttpSession session) {
        session.removeAttribute("ACTIVATING_USER");
        session.removeAttribute("ACTIVATING_EMAIL");
        return "redirect:/auth/activate";
    }
}
