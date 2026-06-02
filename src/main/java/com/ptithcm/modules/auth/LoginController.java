package com.ptithcm.modules.auth;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ptithcm.entities.GiangVien;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.shared.constants.MessageConstant;
import com.ptithcm.shared.dtos.UserSession;
import com.ptithcm.shared.enums.RoleEnum;
import com.ptithcm.shared.utils.SecurityUtil;
import com.ptithcm.shared.utils.SessionUtil;

@Controller
public class LoginController {

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
    public String handleLogin(@RequestParam("username") String username, @RequestParam("password") String password,
            HttpSession session, jakarta.servlet.http.HttpServletResponse response, ModelMap model) {

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
    public String logout(HttpSession session, jakarta.servlet.http.HttpServletResponse response) {
        SessionUtil.invalidate(session);

        // Xóa cookie remember_me khi đăng xuất
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("remember_me", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/login";
    }
}
