package com.ptithcm.controller;

import com.ptithcm.entity.Khoa;
import com.ptithcm.entity.Lop;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
@RequestMapping("/class")
public class LopController {

    @Autowired
    private SessionFactory factory;

    @RequestMapping()
    public String index(ModelMap model, @RequestParam(value = "maKhoa", required = false) String maKhoa,
            HttpSession httpSession) {
        Session session = factory.getCurrentSession();

        String sessionRole = (String) httpSession.getAttribute("role");
        String sessionMaKhoa = (String) httpSession.getAttribute("maKhoa");

        List<Khoa> khoaList = session.createQuery("FROM Khoa", Khoa.class).list();

        if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
            khoaList = khoaList.stream().filter(k -> k.getMaKhoa().equals(sessionMaKhoa)).collect(Collectors.toList());
        }

        List<Lop> lopList;
        if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
            lopList = session.createQuery("FROM Lop WHERE maKhoa = :maKhoa", Lop.class)
                    .setParameter("maKhoa", sessionMaKhoa).list();
            maKhoa = sessionMaKhoa;
        } else if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all")) {
            lopList = session.createQuery("FROM Lop WHERE maKhoa = :maKhoa", Lop.class).setParameter("maKhoa", maKhoa)
                    .list();
        } else {
            lopList = session.createQuery("FROM Lop", Lop.class).list();
        }

        populateCanDelete(session, lopList);
        model.addAttribute("lopList", lopList);
        model.addAttribute("khoaList", khoaList);
        model.addAttribute("maKhoa", maKhoa);
        return "class/index";
    }

    // --- AJAX API ENDPOINTS ---

    @RequestMapping(value = "/api/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Lop getClass(@RequestParam("maLop") String maLop) {
        Session session = factory.getCurrentSession();
        return session.get(Lop.class, maLop);
    }

    @RequestMapping(value = "/api/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Lop> listClasses(@RequestParam(value = "maKhoa", required = false) String maKhoa,
            HttpSession httpSession) {
        Session session = factory.getCurrentSession();

        String sessionRole = (String) httpSession.getAttribute("role");
        String sessionMaKhoa = (String) httpSession.getAttribute("maKhoa");

        if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
        }

        List<Lop> list;
        if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
            list = session.createQuery("FROM Lop WHERE maKhoa = :maKhoa", Lop.class)
                    .setParameter("maKhoa", sessionMaKhoa).list();
        } else if (maKhoa == null || maKhoa.isEmpty() || maKhoa.equals("all")) {
            list = session.createQuery("FROM Lop", Lop.class).list();
        } else {
            list = session.createQuery("FROM Lop WHERE maKhoa = :maKhoa", Lop.class).setParameter("maKhoa", maKhoa)
                    .list();
        }
        populateCanDelete(session, list);
        return list;
    }

    private void populateCanDelete(Session session, List<Lop> list) {
        if (list.isEmpty())
            return;

        // Check for students in class
        List<String> lopWithSV = session.createQuery("SELECT distinct trim(maLop) FROM SinhVien", String.class).list();

        // Check for registrations from students of the class
        List<String> lopWithReg = session
                .createQuery("SELECT distinct trim(sv.maLop) FROM DangKy dk JOIN SinhVien sv ON dk.maSV = sv.maSV",
                        String.class)
                .list();

        java.util.Set<String> dependentIds = new java.util.HashSet<>();
        for (String id : lopWithSV)
            if (id != null)
                dependentIds.add(id.trim().toUpperCase());
        for (String id : lopWithReg)
            if (id != null)
                dependentIds.add(id.trim().toUpperCase());

        for (Lop lop : list) {
            String trimmed = lop.getMaLop() != null ? lop.getMaLop().trim().toUpperCase() : "";
            lop.setCanDelete(!dependentIds.contains(trimmed));
        }
    }

    @RequestMapping(value = "/api/save", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> saveClass(@RequestBody Lop lop, @RequestParam("mode") String mode) {
        Map<String, Object> res = new HashMap<>();
        Session session = factory.openSession();
        org.hibernate.Transaction t = session.beginTransaction();
        try {
            Lop existing = session.get(Lop.class, lop.getMaLop());
            if (mode.equals("add")) {
                if (existing != null) {
                    res.put("status", "error");
                    res.put("message", "Mã lớp [" + lop.getMaLop() + "] đã tồn tại!");
                    return res;
                }
                session.persist(lop);
            } else if (mode.equals("edit")) {
                if (existing == null) {
                    res.put("status", "error");
                    res.put("message", "Không tìm thấy lớp để chỉnh sửa!");
                    return res;
                }
                session.merge(lop);
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
    public Map<String, Object> deleteClass(@RequestParam("maLop") String maLop) {
        Map<String, Object> res = new HashMap<>();
        Session session = factory.openSession();
        org.hibernate.Transaction t = session.beginTransaction();
        try {
            // Check dependencies: SINHVIEN
            Long svCount = session.createQuery("SELECT COUNT(*) FROM SinhVien WHERE maLop = :maLop", Long.class)
                    .setParameter("maLop", maLop).uniqueResult();

            if (svCount > 0) {
                // Also check if they have registrations for a better error message
                Long regCount = session.createQuery(
                        "SELECT COUNT(dk) FROM DangKy dk JOIN SinhVien sv ON dk.maSV = sv.maSV WHERE sv.maLop = :maLop",
                        Long.class).setParameter("maLop", maLop).uniqueResult();

                if (regCount > 0) {
                    res.put("status", "error");
                    res.put("message", "Không thể xóa: Lớp đã có " + regCount + " lượt đăng ký lớp tín chỉ!");
                    return res;
                }

                res.put("status", "error");
                res.put("message", "Không thể xóa: Lớp đang có " + svCount + " sinh viên!");
                return res;
            }

            Lop lop = session.get(Lop.class, maLop);
            if (lop != null) {
                session.remove(lop);
                t.commit();
                res.put("status", "success");
            } else {
                res.put("status", "error");
                res.put("message", "Không tìm thấy lớp để xóa!");
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
