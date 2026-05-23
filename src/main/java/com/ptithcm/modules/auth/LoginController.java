package com.ptithcm.modules.auth;

import com.ptithcm.entity.GiangVien;
import com.ptithcm.entity.SinhVien;
import com.ptithcm.entity.Users;
import com.ptithcm.shared.constant.MessageConstant;
import com.ptithcm.shared.enumtype.RoleEnum;
import com.ptithcm.shared.util.SecurityUtil;
import com.ptithcm.shared.util.SessionUtil;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpSession session) {
        if (SessionUtil.getUser(session) != null) {
            String role = SessionUtil.getRole(session);
            if (RoleEnum.SINHVIEN.getCode().equals(role)) {
                return "redirect:/registration";
            }
            return "redirect:/index";
        }
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String handleLogin(@RequestParam("username") String username, @RequestParam("password") String password,
            @RequestParam("type") String type, HttpSession session, jakarta.servlet.http.HttpServletResponse response,
            ModelMap model) {

        Users user = authService.login(username, password);

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

            if (user.getRoleId() == RoleEnum.PGV.getId()) {
                SessionUtil.setRole(session, RoleEnum.PGV.getCode());
                return "redirect:/index";
            } else if (user.getRoleId() == RoleEnum.KHOA.getId()) {
                SessionUtil.setRole(session, RoleEnum.KHOA.getCode());

                // Với vai trò KHOA, lấy mã khoa tương ứng từ bảng GIANGVIEN
                GiangVien gv = authService.getGiangVienProfile(user.getUsername());
                if (gv != null) {
                    SessionUtil.setMaKhoa(session, gv.getMaKhoa());
                }

                return "redirect:/index";
            } else if (user.getRoleId() == RoleEnum.SINHVIEN.getId()) {
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
        model.addAttribute("type", type);
        model.addAttribute("username", username);
        return "login";
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
