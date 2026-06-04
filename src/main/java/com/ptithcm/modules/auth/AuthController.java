package com.ptithcm.modules.auth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ptithcm.entities.GiangVien;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.entities.TaiKhoan;
import com.ptithcm.modules.auth.dtos.LoginDTO;
import com.ptithcm.shared.constants.CacheConstant;
import com.ptithcm.shared.constants.MessageConstant;
import com.ptithcm.shared.dtos.UserSession;
import com.ptithcm.shared.enums.RoleEnum;
import com.ptithcm.shared.enums.TrangThaiTaiKhoan;
import com.ptithcm.shared.services.EmailOTPService;
import com.ptithcm.shared.services.RedisService;
import com.ptithcm.shared.utils.SecurityUtil;
import com.ptithcm.shared.utils.SessionUtil;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailOTPService emailOTPService;

    @Autowired
    private RedisService redisService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpSession session, @RequestParam(value = "activated", required = false) String activated,
            @RequestParam(value = "error", required = false) String error, ModelMap model) {
        if (SessionUtil.getUser(session) != null) {
            UserSession currUser = SessionUtil.getUser(session);
            TaiKhoan tk = authService.getTaiKhoanByUsername(currUser.getUsername());
            if (tk != null && tk.getTrangThai() == TrangThaiTaiKhoan.CHUA_KICH_HOAT) {
                return "redirect:/activate";
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
            HttpSession session, HttpServletResponse response, ModelMap model) {

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
            if (BCrypt.checkpw(password, tk.getMatKhau())) {
                if (tk.getTrangThai() == TrangThaiTaiKhoan.CHUA_KICH_HOAT) {
                    session.setAttribute("temp_username", tk.getTenDangNhap());
                    return "redirect:/activate";
                }
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

    @RequestMapping(value = "/activate", method = RequestMethod.GET)
    public String activate(HttpSession session, ModelMap model) {
        String tempUsername = (String) session.getAttribute("temp_username");
        if (tempUsername == null) {
            UserSession currUser = SessionUtil.getUser(session);
            if (currUser != null) {
                tempUsername = currUser.getUsername();
            }
        }
        if (tempUsername == null) {
            return "redirect:/login";
        }

        TaiKhoan tk = authService.getTaiKhoanByUsername(tempUsername);
        if (tk == null) {
            return "redirect:/login";
        }

        if (tk.getTrangThai() == TrangThaiTaiKhoan.DA_KICH_HOAT) {
            session.removeAttribute("temp_username");
            if (SessionUtil.getUser(session) != null) {
                String role = SessionUtil.getRole(session);
                if (RoleEnum.SINHVIEN.getCode().equals(role)) {
                    return "redirect:/registration";
                }
                return "redirect:/index";
            }
            return "redirect:/login?activated=true";
        }

        String email = tk.getEmail();
        model.addAttribute("email", email);

        // Mask email for privacy (e.g. sv***@student.ptit.edu.vn)
        String maskedEmail = email;
        int atIndex = email.indexOf("@");
        if (atIndex > 3) {
            maskedEmail = email.substring(0, 3) + "***" + email.substring(atIndex);
        }
        model.addAttribute("maskedEmail", maskedEmail);

        try {
            // Check if OTP already exists in Redis
            String key = CacheConstant.getOtpResetPwKey(email);
            if (redisService.get(key) == null) {
                emailOTPService.sendActivationOTP(email);
                model.addAttribute("info", "Chúng tôi đã gửi mã OTP kích hoạt đến email của bạn.");
            } else {
                model.addAttribute("info", "Mã OTP đã được gửi trước đó và vẫn còn hiệu lực.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Không thể gửi email OTP: " + e.getMessage());
        }

        return "auth/activate";
    }

    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    public String handleActivate(@RequestParam("otpCode") String otpCode, HttpSession session, ModelMap model) {
        String tempUsername = (String) session.getAttribute("temp_username");
        if (tempUsername == null) {
            UserSession currUser = SessionUtil.getUser(session);
            if (currUser != null) {
                tempUsername = currUser.getUsername();
            }
        }
        if (tempUsername == null) {
            return "redirect:/login";
        }

        TaiKhoan tk = authService.getTaiKhoanByUsername(tempUsername);
        if (tk == null) {
            return "redirect:/login";
        }

        String email = tk.getEmail();
        model.addAttribute("email", email);
        String maskedEmail = email;
        int atIndex = email.indexOf("@");
        if (atIndex > 3) {
            maskedEmail = email.substring(0, 3) + "***" + email.substring(atIndex);
        }
        model.addAttribute("maskedEmail", maskedEmail);

        if (otpCode == null || otpCode.trim().isEmpty()) {
            model.addAttribute("error", "Mã OTP không được để trống.");
            return "auth/activate";
        }

        if (emailOTPService.verifyOTP(email, otpCode)) {
            try {
                // Kích hoạt tài khoản
                authService.activateAccount(tempUsername);

                // Xóa OTP
                emailOTPService.deleteOTP(email);

                // Tự động đăng nhập
                if (RoleEnum.SINHVIEN.getCode().equals(tk.getPhanQuyen())) {
                    SinhVien sv = authService.getSinhVienProfile(tempUsername);
                    if (sv != null) {
                        UserSession user = new UserSession(sv.getMaSV(), "SINHVIEN", null,
                                sv.getHo() + " " + sv.getTen(), tk.getAvatar());
                        SessionUtil.setUser(session, user);
                        SessionUtil.setRole(session, RoleEnum.SINHVIEN.getCode());
                        SessionUtil.setStudentProfile(session, sv);
                    }
                    session.removeAttribute("temp_username");
                    return "redirect:/registration";
                } else {
                    GiangVien gv = authService.getGiangVienProfile(tempUsername);
                    if (gv != null) {
                        UserSession user = new UserSession(gv.getMaGV(), tk.getPhanQuyen(), gv.getMaKhoa(),
                                gv.getHo() + " " + gv.getTen(), tk.getAvatar());
                        SessionUtil.setUser(session, user);
                        SessionUtil.setRole(session, user.getRole());
                        if (RoleEnum.KHOA.getCode().equals(user.getRole())) {
                            SessionUtil.setMaKhoa(session, gv.getMaKhoa());
                        }
                    } else {
                        // Fallback if it's PGV or has no lecturer profile
                        UserSession user = new UserSession(tk.getTenDangNhap(), tk.getPhanQuyen(), null, "Hệ thống",
                                tk.getAvatar());
                        SessionUtil.setUser(session, user);
                        SessionUtil.setRole(session, user.getRole());
                    }
                    session.removeAttribute("temp_username");
                    return "redirect:/index";
                }
            } catch (Exception e) {
                model.addAttribute("error", "Kích hoạt thất bại: " + e.getMessage());
                return "auth/activate";
            }
        } else {
            model.addAttribute("error", "Mã OTP không chính xác hoặc đã hết hạn.");
            return "auth/activate";
        }
    }

    @RequestMapping(value = "/activate/resend", method = RequestMethod.GET)
    public String resendActivateOTP(HttpSession session, ModelMap model,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        String tempUsername = (String) session.getAttribute("temp_username");
        if (tempUsername == null) {
            UserSession currUser = SessionUtil.getUser(session);
            if (currUser != null) {
                tempUsername = currUser.getUsername();
            }
        }
        if (tempUsername == null) {
            return "redirect:/login";
        }

        TaiKhoan tk = authService.getTaiKhoanByUsername(tempUsername);
        if (tk == null) {
            return "redirect:/login";
        }

        try {
            emailOTPService.sendActivationOTP(tk.getEmail());
            redirectAttributes.addFlashAttribute("message", "Đã gửi lại mã OTP mới thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gửi lại OTP thất bại: " + e.getMessage());
        }

        return "redirect:/activate";
    }
}
