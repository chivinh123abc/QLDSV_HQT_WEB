package com.ptithcm.modules.auth;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ptithcm.entities.GiangVien;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.entities.TaiKhoan;
import com.ptithcm.shared.dtos.MailInfoDTO;
import com.ptithcm.shared.enums.TrangThaiTaiKhoan;
import com.ptithcm.shared.services.CaptchaService;
import com.ptithcm.shared.services.MailerService;
import com.ptithcm.shared.services.RedisService;

@Controller
public class RegisterController {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private MailerService mailerService;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private RedisService redisService;

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void getCaptcha(HttpSession session, HttpServletResponse response) throws Exception {
        BufferedImage image = captchaService.generateCaptcha(session);
        response.setContentType("image/jpeg");
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "jpeg", os);
        os.flush();
        os.close();
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Transactional
    public String handleRegister(@RequestParam("username") String username, @RequestParam("email") String email,
            @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam("captcha") String captcha, HttpSession session, ModelMap model) {

        model.addAttribute("username", username);
        model.addAttribute("email", email);

        // 1. Kiểm tra captcha
        String sessionCaptcha = (String) session.getAttribute("captcha_key");
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(captcha)) {
            model.addAttribute("error", "Mã CAPTCHA không chính xác!");
            return "register";
        }

        // 2. Kiểm tra mật khẩu khớp
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "register";
        }

        Session hSession = sessionFactory.getCurrentSession();

        // 3. Kiểm tra tài khoản đã tồn tại
        TaiKhoan existingTk = hSession.get(TaiKhoan.class, username.trim());
        if (existingTk != null) {
            model.addAttribute("error", "Tài khoản cho mã số này đã tồn tại!");
            return "register";
        }

        // 4. Kiểm tra mã người dùng (Sinh viên hoặc Giảng viên)
        String phanQuyen = null;
        String trimmedUser = username.trim();

        // Tìm sinh viên
        String svHql = "FROM SinhVien WHERE TRIM(maSV) = :username";
        SinhVien sv = hSession.createQuery(svHql, SinhVien.class).setParameter("username", trimmedUser).uniqueResult();
        if (sv != null) {
            phanQuyen = "SINHVIEN";
        } else {
            // Tìm giảng viên
            String gvHql = "FROM GiangVien WHERE TRIM(maGV) = :username";
            GiangVien gv = hSession.createQuery(gvHql, GiangVien.class).setParameter("username", trimmedUser)
                    .uniqueResult();
            if (gv != null) {
                phanQuyen = "GV01".equalsIgnoreCase(trimmedUser) ? "PGV" : "KHOA";
            }
        }

        if (phanQuyen == null) {
            model.addAttribute("error", "Mã số người dùng không tồn tại trên hệ thống!");
            return "register";
        }

        // 5. Tạo tài khoản mới
        TaiKhoan newTk = new TaiKhoan();
        newTk.setTenDangNhap(trimmedUser);
        newTk.setMatKhau(BCrypt.hashpw(password, BCrypt.gensalt()));
        newTk.setEmail(email.trim());
        newTk.setPhanQuyen(phanQuyen);
        newTk.setTrangThai(TrangThaiTaiKhoan.CHUA_KICH_HOAT);

        hSession.persist(newTk);

        // 6. Tạo OTP 6 chữ số và lưu vào Redis (hết hạn trong 5 phút = 300 giây)
        String otp = String.format("%06d", new Random().nextInt(1000000));
        redisService.set("otp:activation:" + trimmedUser, otp, 300);

        // 7. Gửi email OTP
        try {
            Map<String, String> variables = new HashMap<>();
            variables.put("otpCode", otp);
            MailInfoDTO mailInfo = new MailInfoDTO(email.trim(), "Mã xác thực OTP kích hoạt tài khoản QLDSV",
                    "templates/otp_email.html", variables);
            mailerService.sendMail(mailInfo);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Lỗi gửi email OTP: " + e.getMessage());
            return "register";
        }

        return "redirect:/verify?username=" + trimmedUser;
    }

    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public String verify(@RequestParam("username") String username, ModelMap model) {
        model.addAttribute("username", username);
        return "verify";
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    @Transactional
    public String handleVerify(@RequestParam("username") String username, @RequestParam("otp") String otp,
            ModelMap model) {

        model.addAttribute("username", username);

        if (username == null || username.trim().isEmpty() || otp == null || otp.trim().isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập mã OTP!");
            return "verify";
        }

        Session hSession = sessionFactory.getCurrentSession();
        TaiKhoan tk = hSession.get(TaiKhoan.class, username.trim());

        if (tk == null) {
            model.addAttribute("error", "Tài khoản không tồn tại!");
            return "verify";
        }

        if (tk.getTrangThai() == TrangThaiTaiKhoan.DA_KICH_HOAT) {
            model.addAttribute("error", "Tài khoản này đã kích hoạt rồi!");
            return "verify";
        }

        // Lấy OTP từ Redis
        String storedOtp = redisService.get("otp:activation:" + username.trim());
        if (storedOtp == null) {
            model.addAttribute("error", "Mã OTP đã hết hạn hoặc không tồn tại! Vui lòng đăng ký lại.");
            return "verify";
        }

        if (!storedOtp.equals(otp.trim())) {
            model.addAttribute("error", "Mã OTP không chính xác!");
            return "verify";
        }

        // Kích hoạt thành công
        tk.setTrangThai(TrangThaiTaiKhoan.DA_KICH_HOAT);
        hSession.merge(tk);

        // Xóa OTP khỏi Redis
        redisService.delete("otp:activation:" + username.trim());

        return "redirect:/login?activated=true";
    }
}
