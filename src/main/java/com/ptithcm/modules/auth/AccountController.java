package com.ptithcm.modules.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptithcm.entities.GiangVien;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.entities.TaiKhoan;
import com.ptithcm.shared.dtos.UserSession;
import com.ptithcm.shared.enums.TrangThaiTaiKhoan;
import com.ptithcm.shared.utils.SessionUtil;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private SessionFactory sessionFactory;

    @GetMapping
    @Transactional
    public String index(ModelMap model, HttpSession session,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "lnkAdd", required = false) String lnkAdd) {
        Session hSession = sessionFactory.getCurrentSession();
        List<TaiKhoan> list = hSession.createQuery("FROM TaiKhoan", TaiKhoan.class).list();

        List<Map<String, Object>> userList = new ArrayList<>();
        UserSession currentLoggedIn = SessionUtil.getUser(session);
        String currentLoggedInUser = currentLoggedIn != null ? currentLoggedIn.getUsername() : "";

        for (TaiKhoan tk : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", tk.getTenDangNhap());
            map.put("username", tk.getTenDangNhap());
            map.put("email", tk.getEmail());

            int roleId = 3; // Mặc định SINHVIEN
            String roleName = "UNKNOWN";
            if ("PGV".equals(tk.getPhanQuyen())) {
                roleId = 1;
                roleName = "PGV";
            } else if ("KHOA".equals(tk.getPhanQuyen())) {
                roleId = 2;
                roleName = "KHOA";
            } else if ("SINHVIEN".equals(tk.getPhanQuyen())) {
                roleId = 3;
                roleName = "SINHVIEN";
            }
            map.put("roleId", roleId);
            map.put("roleName", roleName);

            // Lấy tên hiển thị
            String fullName = "Hệ thống";
            if (roleId == 3) {
                String svHql = "FROM SinhVien WHERE maSV = :username";
                SinhVien sv = hSession.createQuery(svHql, SinhVien.class).setParameter("username", tk.getTenDangNhap())
                        .uniqueResult();
                if (sv != null) {
                    fullName = sv.getHo() + " " + sv.getTen();
                }
            } else {
                String gvHql = "FROM GiangVien WHERE maGV = :username";
                GiangVien gv = hSession.createQuery(gvHql, GiangVien.class)
                        .setParameter("username", tk.getTenDangNhap()).uniqueResult();
                if (gv != null) {
                    fullName = gv.getHo() + " " + gv.getTen();
                }
            }
            map.put("fullName", fullName);

            // Không cho phép tự xóa chính mình
            boolean canDelete = !tk.getTenDangNhap().equalsIgnoreCase(currentLoggedInUser);
            map.put("canDelete", canDelete);

            userList.add(map);
        }

        // Nạp danh sách chưa gán tài khoản
        List<SinhVien> unassignedStudents = hSession.createQuery(
                "FROM SinhVien sv WHERE TRIM(sv.maSV) NOT IN (SELECT TRIM(tk.tenDangNhap) FROM TaiKhoan tk)",
                SinhVien.class).list();
        List<GiangVien> unassignedLecturers = hSession.createQuery(
                "FROM GiangVien gv WHERE TRIM(gv.maGV) NOT IN (SELECT TRIM(tk.tenDangNhap) FROM TaiKhoan tk)",
                GiangVien.class).list();

        model.addAttribute("userList", userList);
        model.addAttribute("unassignedStudents", unassignedStudents);
        model.addAttribute("unassignedLecturers", unassignedLecturers);

        // Edit support
        if (userId != null && !userId.trim().isEmpty()) {
            TaiKhoan tk = hSession.get(TaiKhoan.class, userId.trim());
            if (tk != null) {
                model.addAttribute("account", tk);
                model.addAttribute("mode", "edit");
            }
        } else if (lnkAdd != null) {
            model.addAttribute("mode", "add");
        }

        return "account/index";
    }

    @PostMapping(value = "/save")
    @Transactional
    public String saveAccount(@RequestParam("username") String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam("roleId") String roleIdStr, @RequestParam("email") String email,
            @RequestParam("mode") String mode, @RequestParam(value = "userId", required = false) String userId,
            RedirectAttributes redirectAttributes) {
        try {
            Session hSession = sessionFactory.getCurrentSession();
            username = username.trim();
            email = email.trim();

            if ("add".equalsIgnoreCase(mode)) {
                TaiKhoan existing = hSession.get(TaiKhoan.class, username);
                if (existing != null) {
                    redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại!");
                    return "redirect:/account?lnkAdd=true";
                }

                TaiKhoan tk = new TaiKhoan();
                tk.setTenDangNhap(username);
                tk.setMatKhau(BCrypt.hashpw(password, BCrypt.gensalt(12)));
                tk.setEmail(email);

                String phanQuyen = "SINHVIEN";
                if ("1".equals(roleIdStr)) {
                    phanQuyen = "PGV";
                } else if ("2".equals(roleIdStr)) {
                    phanQuyen = "KHOA";
                }
                tk.setPhanQuyen(phanQuyen);
                tk.setTrangThai(TrangThaiTaiKhoan.DA_KICH_HOAT); // Admin tạo thì kích hoạt trực tiếp

                hSession.persist(tk);
                redirectAttributes.addFlashAttribute("message", "Cấp tài khoản [" + username + "] thành công!");
            } else if ("edit".equalsIgnoreCase(mode)) {
                userId = userId.trim();
                TaiKhoan tk = hSession.get(TaiKhoan.class, userId);
                if (tk == null) {
                    redirectAttributes.addFlashAttribute("error", "Không tìm thấy tài khoản!");
                    return "redirect:/account";
                }

                tk.setEmail(email);
                String phanQuyen = "SINHVIEN";
                if ("1".equals(roleIdStr)) {
                    phanQuyen = "PGV";
                } else if ("2".equals(roleIdStr)) {
                    phanQuyen = "KHOA";
                }
                tk.setPhanQuyen(phanQuyen);

                if (password != null && !password.trim().isEmpty()) {
                    tk.setMatKhau(BCrypt.hashpw(password, BCrypt.gensalt(12)));
                }

                hSession.merge(tk);
                redirectAttributes.addFlashAttribute("message", "Cập nhật tài khoản [" + userId + "] thành công!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            if ("add".equalsIgnoreCase(mode)) {
                return "redirect:/account?lnkAdd=true";
            } else {
                return "redirect:/account?userId=" + userId + "&lnkEdit";
            }
        }
        return "redirect:/account";
    }

    @PostMapping(value = "/delete")
    @Transactional
    public String deleteAccount(@RequestParam("userId") String userId, RedirectAttributes redirectAttributes) {
        try {
            Session hSession = sessionFactory.getCurrentSession();
            TaiKhoan tk = hSession.get(TaiKhoan.class, userId.trim());
            if (tk != null) {
                hSession.remove(tk);
                redirectAttributes.addFlashAttribute("message", "Đã xóa tài khoản [" + userId + "] thành công.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy tài khoản để xóa!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/account";
    }
}
