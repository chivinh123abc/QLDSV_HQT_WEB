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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public String index(ModelMap model, HttpSession session) {
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
                String svHql = "FROM SinhVien WHERE TRIM(maSV) = :username";
                SinhVien sv = hSession.createQuery(svHql, SinhVien.class).setParameter("username", tk.getTenDangNhap())
                        .uniqueResult();
                if (sv != null) {
                    fullName = sv.getHo() + " " + sv.getTen();
                }
            } else {
                String gvHql = "FROM GiangVien WHERE TRIM(maGV) = :username";
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

        model.addAttribute("userList", userList);
        return "account/index";
    }

    @RequestMapping(value = "/api/unassigned", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @Transactional
    public List<Map<String, Object>> getUnassigned(@RequestParam("roleId") int roleId) {
        Session hSession = sessionFactory.getCurrentSession();
        List<Map<String, Object>> res = new ArrayList<>();

        if (roleId == 3) {
            // Sinh viên chưa có tài khoản
            List<SinhVien> sinhVienList = hSession.createQuery(
                    "FROM SinhVien sv WHERE TRIM(sv.maSV) NOT IN (SELECT TRIM(tk.tenDangNhap) FROM TaiKhoan tk)",
                    SinhVien.class).list();
            for (SinhVien sv : sinhVienList) {
                Map<String, Object> map = new HashMap<>();
                map.put("maSV", sv.getMaSV().trim());
                map.put("ho", sv.getHo());
                map.put("ten", sv.getTen());
                res.add(map);
            }
        } else {
            // Giảng viên chưa có tài khoản
            List<GiangVien> giangVienList = hSession.createQuery(
                    "FROM GiangVien gv WHERE TRIM(gv.maGV) NOT IN (SELECT TRIM(tk.tenDangNhap) FROM TaiKhoan tk)",
                    GiangVien.class).list();
            for (GiangVien gv : giangVienList) {
                Map<String, Object> map = new HashMap<>();
                map.put("maGV", gv.getMaGV().trim());
                map.put("ho", gv.getHo());
                map.put("ten", gv.getTen());
                res.add(map);
            }
        }
        return res;
    }

    @RequestMapping(value = "/api/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @Transactional
    public Map<String, Object> getAccount(@RequestParam("userId") String userId) {
        Session hSession = sessionFactory.getCurrentSession();
        TaiKhoan tk = hSession.get(TaiKhoan.class, userId.trim());
        Map<String, Object> map = new HashMap<>();
        if (tk != null) {
            map.put("userId", tk.getTenDangNhap());
            map.put("username", tk.getTenDangNhap());
            map.put("email", tk.getEmail());
            int roleId = 3;
            if ("PGV".equals(tk.getPhanQuyen())) {
                roleId = 1;
            } else if ("KHOA".equals(tk.getPhanQuyen())) {
                roleId = 2;
            }
            map.put("roleId", roleId);
            map.put("password", "");
        }
        return map;
    }

    @RequestMapping(value = "/api/save", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @Transactional
    public Map<String, Object> saveAccount(@RequestBody Map<String, String> body, @RequestParam("mode") String mode) {
        Map<String, Object> res = new HashMap<>();
        try {
            Session hSession = sessionFactory.getCurrentSession();
            String username = body.get("username").trim();
            String password = body.get("password");
            String roleIdStr = body.get("roleId");
            String email = body.get("email").trim();

            if ("add".equalsIgnoreCase(mode)) {
                TaiKhoan existing = hSession.get(TaiKhoan.class, username);
                if (existing != null) {
                    res.put("status", "error");
                    res.put("message", "Tên đăng nhập đã tồn tại!");
                    return res;
                }

                TaiKhoan tk = new TaiKhoan();
                tk.setTenDangNhap(username);
                tk.setMatKhau(BCrypt.hashpw(password, BCrypt.gensalt()));
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
            } else if ("edit".equalsIgnoreCase(mode)) {
                String userId = body.get("userId").trim();
                TaiKhoan tk = hSession.get(TaiKhoan.class, userId);
                if (tk == null) {
                    res.put("status", "error");
                    res.put("message", "Không tìm thấy tài khoản!");
                    return res;
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
                    tk.setMatKhau(BCrypt.hashpw(password, BCrypt.gensalt()));
                }

                hSession.merge(tk);
            }

            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Lỗi: " + e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/api/delete", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @Transactional
    public Map<String, Object> deleteAccount(@RequestParam("userId") String userId) {
        Map<String, Object> res = new HashMap<>();
        try {
            Session hSession = sessionFactory.getCurrentSession();
            TaiKhoan tk = hSession.get(TaiKhoan.class, userId.trim());
            if (tk != null) {
                hSession.remove(tk);
            }
            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Lỗi: " + e.getMessage());
        }
        return res;
    }
}
