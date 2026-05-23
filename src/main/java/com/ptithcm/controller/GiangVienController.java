package com.ptithcm.controller;

import com.ptithcm.entity.GiangVien;
import com.ptithcm.entity.Khoa;
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
@RequestMapping("/lecturer")
public class GiangVienController {

    @Autowired private SessionFactory factory;

    @RequestMapping()
    public String index(
            ModelMap model,
            @RequestParam(value = "maKhoa", required = false) String maKhoa,
            HttpSession httpSession) {
        Session session = factory.getCurrentSession();

        String sessionRole = (String) httpSession.getAttribute("role");
        String sessionMaKhoa = (String) httpSession.getAttribute("maKhoa");

        List<Khoa> khoaList = session.createQuery("FROM Khoa", Khoa.class).list();

        if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
            khoaList =
                    khoaList.stream()
                            .filter(k -> k.getMaKhoa().equals(sessionMaKhoa))
                            .collect(Collectors.toList());
        }

        List<GiangVien> gvList;
        if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
            gvList =
                    session.createQuery("FROM GiangVien WHERE maKhoa = :maKhoa", GiangVien.class)
                            .setParameter("maKhoa", sessionMaKhoa)
                            .list();
            maKhoa = sessionMaKhoa;
        } else if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all")) {
            gvList =
                    session.createQuery("FROM GiangVien WHERE maKhoa = :maKhoa", GiangVien.class)
                            .setParameter("maKhoa", maKhoa)
                            .list();
        } else {
            gvList = session.createQuery("FROM GiangVien", GiangVien.class).list();
        }

        populateCanDelete(session, gvList);

        model.addAttribute("khoaList", khoaList);
        model.addAttribute("gvList", gvList);
        model.addAttribute("maKhoa", maKhoa);
        return "lecturer/index";
    }

    private void populateCanDelete(Session session, List<GiangVien> list) {
        if (list.isEmpty()) return;
        List<String> ltcMaGV =
                session.createQuery(
                                "SELECT distinct maGV FROM LopTinChi WHERE maGV IS NOT NULL",
                                String.class)
                        .list();
        List<String> userMaGV =
                session.createQuery(
                                "SELECT distinct username FROM Users WHERE username IS NOT NULL",
                                String.class)
                        .list();

        java.util.Set<String> dependentIds = new java.util.HashSet<>();
        for (String id : ltcMaGV) if (id != null) dependentIds.add(id.trim());
        for (String id : userMaGV) if (id != null) dependentIds.add(id.trim());

        for (GiangVien gv : list) {
            String trimmed = gv.getMaGV() != null ? gv.getMaGV().trim() : "";
            gv.setCanDelete(!dependentIds.contains(trimmed));
        }
    }

    @RequestMapping(value = "/api/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<GiangVien> listGV(
            @RequestParam(value = "maKhoa", required = false) String maKhoa,
            HttpSession httpSession) {
        Session session = factory.getCurrentSession();

        String sessionRole = (String) httpSession.getAttribute("role");
        String sessionMaKhoa = (String) httpSession.getAttribute("maKhoa");

        if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
        }

        List<GiangVien> list;
        if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
            list =
                    session.createQuery("FROM GiangVien WHERE maKhoa = :maKhoa", GiangVien.class)
                            .setParameter("maKhoa", sessionMaKhoa)
                            .list();
        } else if (maKhoa == null || maKhoa.isEmpty() || maKhoa.equals("all")) {
            list = session.createQuery("FROM GiangVien", GiangVien.class).list();
        } else {
            list =
                    session.createQuery("FROM GiangVien WHERE maKhoa = :maKhoa", GiangVien.class)
                            .setParameter("maKhoa", maKhoa)
                            .list();
        }
        populateCanDelete(session, list);
        return list;
    }

    @RequestMapping(value = "/api/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public GiangVien getGV(@RequestParam("maGV") String maGV) {
        Session session = factory.getCurrentSession();
        GiangVien gv = session.get(GiangVien.class, maGV);
        if (gv != null) {
            populateCanDelete(session, java.util.Collections.singletonList(gv));
        }
        return gv;
    }

    @RequestMapping(value = "/api/save", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> saveGV(
            @RequestBody GiangVien gv, @RequestParam("mode") String mode) {
        Map<String, Object> res = new HashMap<>();
        Session session = factory.openSession();
        org.hibernate.Transaction t = session.beginTransaction();
        try {
            GiangVien existing = session.get(GiangVien.class, gv.getMaGV());
            if (mode.equals("add")) {
                if (existing != null) {
                    res.put("status", "error");
                    res.put("message", "Mã giảng viên [" + gv.getMaGV() + "] đã tồn tại!");
                    return res;
                }
                session.persist(gv);
            } else if (mode.equals("edit")) {
                if (existing == null) {
                    res.put("status", "error");
                    res.put("message", "Không tìm thấy giảng viên để chỉnh sửa!");
                    return res;
                }
                session.merge(gv);
            }
            t.commit();
            res.put("status", "success");
        } catch (Exception e) {
            if (t != null) t.rollback();
            res.put("status", "error");
            res.put("message", "Lỗi: " + e.getMessage());
        } finally {
            session.close();
        }
        return res;
    }

    @RequestMapping(
            value = "/api/delete",
            method = RequestMethod.POST,
            produces = "application/json")
    @ResponseBody
    public Map<String, Object> deleteGV(@RequestParam("maGV") String maGV) {
        Map<String, Object> res = new HashMap<>();
        Session session = factory.openSession();
        org.hibernate.Transaction t = session.beginTransaction();
        try {
            // Check dependencies in LOPTINCHI
            Long ltcCount =
                    session.createQuery(
                                    "SELECT COUNT(*) FROM LopTinChi WHERE trim(maGV) = trim(:maGV)",
                                    Long.class)
                            .setParameter("maGV", maGV.trim())
                            .uniqueResult();
            if (ltcCount > 0) {
                res.put("status", "error");
                res.put(
                        "message",
                        "Không thể xóa: Giảng viên đang phụ trách " + ltcCount + " lớp tín chỉ!");
                return res;
            }

            // Check dependencies in USERS
            Long userCount =
                    session.createQuery(
                                    "SELECT COUNT(*) FROM Users WHERE trim(username) = trim(:maGV)",
                                    Long.class)
                            .setParameter("maGV", maGV.trim())
                            .uniqueResult();
            if (userCount > 0) {
                res.put("status", "error");
                res.put(
                        "message",
                        "Không thể xóa: Giảng viên đang được cấp tài khoản đăng nhập trong hệ thống!");
                return res;
            }

            List<GiangVien> list =
                    session.createQuery(
                                    "FROM GiangVien WHERE trim(maGV) = trim(:maGV)",
                                    GiangVien.class)
                            .setParameter("maGV", maGV.trim())
                            .list();
            if (!list.isEmpty()) {
                session.remove(list.get(0));
                t.commit();
                res.put("status", "success");
            } else {
                res.put("status", "error");
                res.put("message", "Không tìm thấy giảng viên để xóa!");
            }
        } catch (Exception e) {
            if (t != null) t.rollback();
            res.put("status", "error");
            res.put("message", "Lỗi: " + e.getMessage());
        } finally {
            session.close();
        }
        return res;
    }
}
