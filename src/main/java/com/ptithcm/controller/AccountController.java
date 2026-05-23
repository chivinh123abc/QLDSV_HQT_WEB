package com.ptithcm.controller;

import com.ptithcm.entity.Users;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Transactional
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private SessionFactory factory;

    @RequestMapping()
    public String index(ModelMap model, jakarta.servlet.http.HttpSession httpSession) {
        Session session = factory.getCurrentSession();
        List<Users> userList = session.createQuery("FROM Users", Users.class).list();

        Map<String, String> nameMap = new HashMap<>();
        List<Object[]> gvList = session.createQuery("SELECT maGV, ho, ten FROM GiangVien", Object[].class).list();
        for (Object[] gv : gvList) {
            nameMap.put((String) gv[0], (gv[1] != null ? gv[1] + " " : "") + (gv[2] != null ? gv[2] : ""));
        }
        List<Object[]> svList = session.createQuery("SELECT maSV, ho, ten FROM SinhVien", Object[].class).list();
        for (Object[] sv : svList) {
            nameMap.put((String) sv[0], (sv[1] != null ? sv[1] + " " : "") + (sv[2] != null ? sv[2] : ""));
        }

        // Find lecturers with credit classes
        List<String> ltcMaGV = session
                .createQuery("SELECT distinct trim(maGV) FROM LopTinChi WHERE maGV IS NOT NULL", String.class).list();
        java.util.Set<String> dependentUsernames = new java.util.HashSet<>();
        for (String s : ltcMaGV)
            if (s != null)
                dependentUsernames.add(s.trim().toUpperCase());

        // Find students with registrations
        List<String> dkMaSV = session
                .createQuery("SELECT distinct trim(maSV) FROM DangKy WHERE maSV IS NOT NULL", String.class).list();
        for (String s : dkMaSV)
            if (s != null)
                dependentUsernames.add(s.trim().toUpperCase());

        Users currentUser = (Users) httpSession.getAttribute("user");
        java.util.List<Map<String, Object>> userListWithNames = new java.util.ArrayList<>();
        for (Users u : userList) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", u.getUserId());
            map.put("username", u.getUsername());
            map.put("password", u.getPassword());
            map.put("roleId", u.getRoleId());
            String name = nameMap.get(u.getUsername());
            map.put("fullName", name != null ? name.trim() : "Tài khoản tùy chỉnh");

            // canDelete logic: not self AND (if lecturer/student, no dependencies)
            boolean canDelete = true;
            if (currentUser != null && currentUser.getUserId() == u.getUserId()) {
                canDelete = false;
            } else {
                String uname = u.getUsername() != null ? u.getUsername().trim().toUpperCase() : "";
                if (dependentUsernames.contains(uname)) {
                    canDelete = false;
                }
            }
            map.put("canDelete", canDelete);

            userListWithNames.add(map);
        }

        model.addAttribute("userList", userListWithNames);
        return "account/index";
    }

    @RequestMapping(value = "/api/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Users getUser(@RequestParam("userId") int userId) {
        Session session = factory.getCurrentSession();
        return session.get(Users.class, userId);
    }

    @RequestMapping(value = "/api/unassigned", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<?> getUnassignedUsers(@RequestParam("roleId") int roleId) {
        Session session = factory.getCurrentSession();
        if (roleId == 3) {
            String hql = "FROM SinhVien sv WHERE sv.maSV NOT IN (SELECT u.username FROM Users u)";
            return session.createQuery(hql, com.ptithcm.entity.SinhVien.class).list();
        } else {
            String hql = "FROM GiangVien gv WHERE gv.maGV NOT IN (SELECT u.username FROM Users u)";
            return session.createQuery(hql, com.ptithcm.entity.GiangVien.class).list();
        }
    }

    @RequestMapping(value = "/api/save", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> saveUser(@RequestBody Users user, @RequestParam("mode") String mode,
            jakarta.servlet.http.HttpSession httpSession) {
        Map<String, Object> res = new HashMap<>();
        Session session = factory.openSession();
        org.hibernate.Transaction t = session.beginTransaction();
        try {
            // If username does not match selectedId, ensure it does not steal another
            // existing
            // MaGV/MaSV
            if (user.getSelectedId() != null && !user.getUsername().equals(user.getSelectedId())) {
                Long countGV = session.createQuery("SELECT COUNT(*) FROM GiangVien WHERE maGV = :username", Long.class)
                        .setParameter("username", user.getUsername()).uniqueResult();
                Long countSV = session.createQuery("SELECT COUNT(*) FROM SinhVien WHERE maSV = :username", Long.class)
                        .setParameter("username", user.getUsername()).uniqueResult();
                if (countGV > 0 || countSV > 0) {
                    res.put("status", "error");
                    res.put("message", "Tên đăng nhập [" + user.getUsername()
                            + "] trùng với Mã của một Giảng viên/Sinh viên khác! Vui lòng đặt tên khác.");
                    return res;
                }
            }

            if (mode.equals("add")) {
                // Check if username already has an account
                Long existCount = session
                        .createQuery("SELECT COUNT(*) FROM Users WHERE username = :username", Long.class)
                        .setParameter("username", user.getUsername()).uniqueResult();
                if (existCount > 0) {
                    res.put("status", "error");
                    res.put("message", "Tên đăng nhập [" + user.getUsername() + "] đã được cấp tài khoản trước đó!");
                    return res;
                }

                session.persist(user);
            } else if (mode.equals("edit")) {
                Users existing = session.get(Users.class, user.getUserId());
                if (existing == null) {
                    res.put("status", "error");
                    res.put("message", "Không tìm thấy tài khoản để chỉnh sửa!");
                    return res;
                }
                // Verify if another user has the same username
                Long existCount = session
                        .createQuery("SELECT COUNT(*) FROM Users WHERE username = :username AND userId != :userId",
                                Long.class)
                        .setParameter("username", user.getUsername()).setParameter("userId", user.getUserId())
                        .uniqueResult();
                if (existCount > 0) {
                    res.put("status", "error");
                    res.put("message",
                            "Tên đăng nhập [" + user.getUsername() + "] đã được sử dụng bởi tài khoản khác!");
                    return res;
                }

                // Prevent self-demotion
                Users currentUser = (Users) httpSession.getAttribute("user");
                if (currentUser != null && currentUser.getUserId() == user.getUserId()) {
                    if (currentUser.getRoleId() != user.getRoleId()) {
                        res.put("status", "error");
                        res.put("message", "Không thể tự thay đổi Nhóm quyền của tài khoản đang đăng nhập!");
                        return res;
                    }
                    currentUser.setUsername(user.getUsername());
                    currentUser.setPassword(user.getPassword());
                }

                session.merge(user);
            }
            t.commit();
            res.put("status", "success");
        } catch (Exception e) {
            if (t != null)
                t.rollback();
            res.put("status", "error");
            res.put("message", "Lỗi: " + e.getMessage());
        } finally {
            session.close();
        }
        return res;
    }

    @RequestMapping(value = "/api/delete", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> deleteUser(@RequestParam("userId") int userId,
            jakarta.servlet.http.HttpSession httpSession) {
        Map<String, Object> res = new HashMap<>();
        Session session = factory.openSession();
        org.hibernate.Transaction t = session.beginTransaction();
        try {
            Users currentUser = (Users) httpSession.getAttribute("user");
            if (currentUser != null && currentUser.getUserId() == userId) {
                res.put("status", "error");
                res.put("message", "Không thể xóa tài khoản mà bạn đang sử dụng để đăng nhập!");
                return res;
            }

            Users user = session.get(Users.class, userId);
            if (user != null) {
                // Check if this user is a lecturer with credit classes
                String uname = user.getUsername() != null ? user.getUsername().trim() : "";
                Long ltcCount = session
                        .createQuery("SELECT COUNT(*) FROM LopTinChi WHERE upper(trim(maGV)) = upper(trim(:uname))",
                                Long.class)
                        .setParameter("uname", uname).uniqueResult();
                if (ltcCount > 0) {
                    res.put("status", "error");
                    res.put("message", "Không thể xóa: Tài khoản này thuộc về Giảng viên đang phụ trách " + ltcCount
                            + " lớp tín chỉ!");
                    return res;
                }

                // Check if this user is a student with registrations
                Long dkCount = session
                        .createQuery("SELECT COUNT(*) FROM DangKy WHERE upper(trim(maSV)) = upper(trim(:uname))",
                                Long.class)
                        .setParameter("uname", uname).uniqueResult();
                if (dkCount > 0) {
                    res.put("status", "error");
                    res.put("message",
                            "Không thể xóa: Tài khoản này thuộc về Sinh viên đã đăng ký " + dkCount + " lớp tín chỉ!");
                    return res;
                }

                session.remove(user);
                t.commit();
                res.put("status", "success");
            } else {
                res.put("status", "error");
                res.put("message", "Không tìm thấy tài khoản để xóa!");
            }
        } catch (Exception e) {
            if (t != null)
                t.rollback();
            res.put("status", "error");
            res.put("message", "Lỗi: " + e.getMessage());
        } finally {
            session.close();
        }
        return res;
    }
}
