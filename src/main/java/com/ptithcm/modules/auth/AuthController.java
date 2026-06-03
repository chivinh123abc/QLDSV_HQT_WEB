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
import com.ptithcm.modules.auth.dtos.ActivateAccountDTO;
import com.ptithcm.modules.auth.dtos.LoginDTO;
import com.ptithcm.shared.constants.MessageConstant;
import com.ptithcm.shared.dtos.UserSession;
import com.ptithcm.shared.enums.RoleEnum;
import com.ptithcm.shared.enums.TrangThaiTaiKhoan;
import com.ptithcm.shared.utils.SecurityUtil;
import com.ptithcm.shared.utils.SessionUtil;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpSession session, @RequestParam(value = "activated", required = false) String activated,
            ModelMap model) {
        if (SessionUtil.getUser(session) != null) {
            String role = SessionUtil.getRole(session);
            if (RoleEnum.SINHVIEN.getCode().equals(role)) {
                return "redirect:/registration";
            }
            return "redirect:/index";
        }
        if ("true".equals(activated)) {
            model.addAttribute("success", "Kích hoạt tài khoản thành công! Vui lòng đăng nhập.");
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
        if (session.getAttribute("temp_username") == null) {
            return "redirect:/login";
        }
        return "auth/activate";
    }

    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    public String handleActivate(@Valid @ModelAttribute("activateDto") ActivateAccountDTO activateDto,
            BindingResult bindingResult, HttpSession session, ModelMap model) {
        String tempUsername = (String) session.getAttribute("temp_username");
        if (tempUsername == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "auth/activate";
        }

        String newPassword = activateDto.getNewPassword();

        try {
            authService.activateAccount(tempUsername, newPassword);
            session.removeAttribute("temp_username");
            return "redirect:/login?activated=true";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "auth/activate";
        }
    }
}
