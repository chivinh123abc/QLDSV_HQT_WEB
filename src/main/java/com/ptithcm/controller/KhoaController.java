package com.ptithcm.controller;

import com.ptithcm.entity.Khoa;
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
@RequestMapping("/faculty")
public class KhoaController {

    @Autowired private SessionFactory factory;

    @RequestMapping()
    public String index(ModelMap model) {
        Session session = factory.getCurrentSession();
        List<Khoa> khoaList = session.createQuery("FROM Khoa", Khoa.class).list();
        populateCanDelete(session, khoaList);
        model.addAttribute("khoaList", khoaList);
        return "faculty/index";
    }

    @RequestMapping(value = "/api/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Khoa getKhoa(@RequestParam("maKhoa") String maKhoa) {
        Session session = factory.getCurrentSession();
        return session.get(Khoa.class, maKhoa);
    }

    @RequestMapping(value = "/api/save", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> saveKhoa(@RequestBody Khoa khoa, @RequestParam("mode") String mode) {
        Map<String, Object> res = new HashMap<>();
        Session session = factory.openSession();
        org.hibernate.Transaction t = session.beginTransaction();
        try {
            Khoa existing = session.get(Khoa.class, khoa.getMaKhoa());
            if (mode.equals("add")) {
                if (existing != null) {
                    res.put("status", "error");
                    res.put("message", "Mã khoa [" + khoa.getMaKhoa() + "] đã tồn tại!");
                    return res;
                }
                session.persist(khoa);
            } else if (mode.equals("edit")) {
                if (existing == null) {
                    res.put("status", "error");
                    res.put("message", "Không tìm thấy khoa để chỉnh sửa!");
                    return res;
                }
                session.merge(khoa);
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
    public Map<String, Object> deleteKhoa(@RequestParam("maKhoa") String maKhoa) {
        Map<String, Object> res = new HashMap<>();
        Session session = factory.openSession();
        org.hibernate.Transaction t = session.beginTransaction();
        try {
            // Check dependencies in LOP
            Long lopCount =
                    session.createQuery(
                                    "SELECT COUNT(*) FROM Lop WHERE maKhoa = :maKhoa", Long.class)
                            .setParameter("maKhoa", maKhoa)
                            .uniqueResult();
            if (lopCount > 0) {
                res.put("status", "error");
                res.put("message", "Không thể xóa: Khoa đang có " + lopCount + " lớp!");
                return res;
            }

            // Check dependencies in GIANGVIEN
            Long gvCount =
                    session.createQuery(
                                    "SELECT COUNT(*) FROM GiangVien WHERE maKhoa = :maKhoa",
                                    Long.class)
                            .setParameter("maKhoa", maKhoa)
                            .uniqueResult();
            if (gvCount > 0) {
                res.put("status", "error");
                res.put("message", "Không thể xóa: Khoa đang có " + gvCount + " giảng viên!");
                return res;
            }

            // Check dependencies in LOPTINCHI
            Long ltcCount =
                    session.createQuery(
                                    "SELECT COUNT(*) FROM LopTinChi WHERE maKhoa = :maKhoa",
                                    Long.class)
                            .setParameter("maKhoa", maKhoa)
                            .uniqueResult();
            if (ltcCount > 0) {
                res.put("status", "error");
                res.put("message", "Không thể xóa: Khoa đang mở " + ltcCount + " lớp tín chỉ!");
                return res;
            }

            Khoa khoa = session.get(Khoa.class, maKhoa);
            if (khoa != null) {
                session.remove(khoa);
                t.commit();
                res.put("status", "success");
            } else {
                res.put("status", "error");
                res.put("message", "Không tìm thấy khoa để xóa!");
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

    private void populateCanDelete(Session session, List<Khoa> list) {
        if (list.isEmpty()) return;
        List<String> khoaWithLop =
                session.createQuery("SELECT distinct trim(maKhoa) FROM Lop", String.class).list();
        List<String> khoaWithGV =
                session.createQuery("SELECT distinct trim(maKhoa) FROM GiangVien", String.class)
                        .list();
        List<String> khoaWithLTC =
                session.createQuery("SELECT distinct trim(maKhoa) FROM LopTinChi", String.class)
                        .list();

        java.util.Set<String> dependentIds = new java.util.HashSet<>();
        for (String id : khoaWithLop) if (id != null) dependentIds.add(id.trim().toUpperCase());
        for (String id : khoaWithGV) if (id != null) dependentIds.add(id.trim().toUpperCase());
        for (String id : khoaWithLTC) if (id != null) dependentIds.add(id.trim().toUpperCase());

        for (Khoa k : list) {
            String trimmed = k.getMaKhoa() != null ? k.getMaKhoa().trim().toUpperCase() : "";
            k.setCanDelete(!dependentIds.contains(trimmed));
        }
    }
}
