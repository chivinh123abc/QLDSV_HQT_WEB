package com.ptithcm.controller;

import com.ptithcm.entity.MonHoc;
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
@RequestMapping("/subject")
public class MonHocController {

    @Autowired private SessionFactory factory;

    @RequestMapping()
    public String index(ModelMap model) {
        Session session = factory.getCurrentSession();
        List<MonHoc> monHocList = session.createQuery("FROM MonHoc", MonHoc.class).list();
        populateCanDelete(session, monHocList);
        model.addAttribute("monHocList", monHocList);
        return "subject/index";
    }

    @RequestMapping(params = "btnInsert")
    public String insert(ModelMap model, MonHoc monHoc) {
        Session session = factory.openSession();
        org.hibernate.Transaction t = session.beginTransaction();
        try {
            session.persist(monHoc);
            t.commit();
            model.addAttribute("message", "Đã thêm môn học: " + monHoc.getTenMH());
        } catch (Exception e) {
            t.rollback();
            model.addAttribute("message", "Lỗi: " + e.getMessage());
        } finally {
            session.close();
        }
        return index(model);
    }

    @RequestMapping(params = "btnUpdate")
    public String update(ModelMap model, MonHoc monHoc) {
        Session session = factory.openSession();
        org.hibernate.Transaction t = session.beginTransaction();
        try {
            session.merge(monHoc);
            t.commit();
            model.addAttribute("message", "Đã cập nhật môn học: " + monHoc.getTenMH());
        } catch (Exception e) {
            t.rollback();
            model.addAttribute("message", "Lỗi: " + e.getMessage());
        } finally {
            session.close();
        }
        return index(model);
    }

    @RequestMapping(params = "btnDelete")
    public String delete(ModelMap model, @RequestParam("maMH") String maMH) {
        Session session = factory.openSession();
        org.hibernate.Transaction t = session.beginTransaction();
        try {
            MonHoc monHoc = session.get(MonHoc.class, maMH);
            if (monHoc != null) {
                session.remove(monHoc);
                t.commit();
                model.addAttribute("message", "Đã xóa môn học: " + maMH);
            }
        } catch (Exception e) {
            t.rollback();
            model.addAttribute("message", "Lỗi: " + e.getMessage());
        } finally {
            session.close();
        }
        return index(model);
    }

    @RequestMapping(params = "lnkEdit")
    public String edit(ModelMap model, @RequestParam("maMH") String maMH) {
        Session session = factory.getCurrentSession();
        MonHoc monHoc = session.get(MonHoc.class, maMH);
        model.addAttribute("monHoc", monHoc);
        return index(model);
    }

    // --- AJAX API ENDPOINTS ---

    @RequestMapping(value = "/api/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public MonHoc getSubject(@RequestParam("maMH") String maMH) {
        Session session = factory.getCurrentSession();
        return session.get(MonHoc.class, maMH);
    }

    @RequestMapping(value = "/api/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<MonHoc> listSubjects() {
        Session session = factory.getCurrentSession();
        List<MonHoc> list = session.createQuery("FROM MonHoc", MonHoc.class).list();
        populateCanDelete(session, list);
        return list;
    }

    private void populateCanDelete(Session session, List<MonHoc> list) {
        if (list.isEmpty()) return;
        List<String> mhWithLTC =
                session.createQuery("SELECT distinct trim(maMH) FROM LopTinChi", String.class)
                        .list();
        java.util.Set<String> dependentIds = new java.util.HashSet<>();
        for (String id : mhWithLTC) if (id != null) dependentIds.add(id.trim().toUpperCase());

        for (MonHoc mh : list) {
            String trimmed = mh.getMaMH() != null ? mh.getMaMH().trim().toUpperCase() : "";
            mh.setCanDelete(!dependentIds.contains(trimmed));
        }
    }

    @RequestMapping(value = "/api/save", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> saveSubject(
            @RequestBody MonHoc monHoc, @RequestParam("mode") String mode) {
        Map<String, Object> res = new HashMap<>();
        Session session = factory.openSession();
        org.hibernate.Transaction t = session.beginTransaction();
        try {
            MonHoc existing = session.get(MonHoc.class, monHoc.getMaMH());
            if (mode.equals("add")) {
                if (existing != null) {
                    res.put("status", "error");
                    res.put("message", "Mã môn học [" + monHoc.getMaMH() + "] đã tồn tại!");
                    return res;
                }
            } else if (mode.equals("edit")) {
                if (existing == null) {
                    res.put("status", "error");
                    res.put(
                            "message",
                            "Không tìm thấy môn học [" + monHoc.getMaMH() + "] để chỉnh sửa!");
                    return res;
                }
            }

            session.merge(monHoc);
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
    public Map<String, Object> deleteSubject(@RequestParam("maMH") String maMH) {
        Map<String, Object> res = new HashMap<>();
        Session session = factory.openSession();
        org.hibernate.Transaction t = session.beginTransaction();
        try {
            // Check dependencies: LOPTINCHI
            Long count =
                    session.createQuery(
                                    "SELECT COUNT(*) FROM LopTinChi WHERE maMH = :maMH", Long.class)
                            .setParameter("maMH", maMH)
                            .uniqueResult();

            if (count > 0) {
                res.put("status", "error");
                res.put("message", "Không thể xóa: Môn học đã được mở " + count + " lớp tín chỉ!");
                return res;
            }

            MonHoc monHoc = session.get(MonHoc.class, maMH);
            if (monHoc != null) {
                session.remove(monHoc);
                t.commit();
                res.put("status", "success");
            } else {
                res.put("status", "error");
                res.put("message", "Không tìm thấy môn học để xóa!");
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
