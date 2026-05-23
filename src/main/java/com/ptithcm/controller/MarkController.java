package com.ptithcm.controller;

import com.ptithcm.entity.DangKy;
import com.ptithcm.entity.Khoa;
import com.ptithcm.entity.SinhVien;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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
@RequestMapping("/mark")
public class MarkController {

    @Autowired private SessionFactory factory;

    @RequestMapping()
    public String index(ModelMap model, HttpSession httpSession) {
        Session session = factory.getCurrentSession();

        String sessionRole = (String) httpSession.getAttribute("role");
        String sessionMaKhoa = (String) httpSession.getAttribute("maKhoa");

        // Get unique School Years
        List<String> nienKhoaList =
                session.createQuery("SELECT DISTINCT ltc.nienKhoa FROM LopTinChi ltc", String.class)
                        .list();
        model.addAttribute("nienKhoaList", nienKhoaList);

        // Get Faculty list for PGV
        List<Khoa> khoaList = session.createQuery("FROM Khoa", Khoa.class).list();
        if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
            khoaList =
                    khoaList.stream()
                            .filter(k -> k.getMaKhoa().equals(sessionMaKhoa))
                            .collect(java.util.stream.Collectors.toList());
        }
        model.addAttribute("khoaList", khoaList);

        return "mark/index";
    }

    @RequestMapping(value = "/get-subjects", method = RequestMethod.GET)
    @ResponseBody
    public List<Object[]> getSubjects(
            @RequestParam("nienKhoa") String nienKhoa,
            @RequestParam("hocKy") String hocKy,
            @RequestParam(value = "maKhoa", required = false) String maKhoa,
            HttpSession httpSession) {
        Session session = factory.getCurrentSession();

        String sessionRole = (String) httpSession.getAttribute("role");
        String sessionMaKhoa = (String) httpSession.getAttribute("maKhoa");
        if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
        }

        StringBuilder hql =
                new StringBuilder(
                        "SELECT DISTINCT mh.maMH, mh.tenMH FROM LopTinChi ltc JOIN MonHoc mh ON ltc.maMH = mh.maMH "
                                + "WHERE 1=1 ");

        if (nienKhoa != null && !nienKhoa.isEmpty() && !nienKhoa.equals("all"))
            hql.append("AND ltc.nienKhoa = :nienKhoa ");
        if (hocKy != null && !hocKy.isEmpty() && !hocKy.equals("all"))
            hql.append("AND ltc.hocKy = :hocKy ");
        if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all"))
            hql.append("AND ltc.maKhoa = :maKhoa ");

        Query<Object[]> query = session.createQuery(hql.toString(), Object[].class);
        if (nienKhoa != null && !nienKhoa.isEmpty() && !nienKhoa.equals("all"))
            query.setParameter("nienKhoa", nienKhoa);
        if (hocKy != null && !hocKy.isEmpty() && !hocKy.equals("all"))
            query.setParameter("hocKy", Integer.parseInt(hocKy));
        if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all"))
            query.setParameter("maKhoa", maKhoa);

        return query.list();
    }

    @RequestMapping(value = "/get-groups", method = RequestMethod.GET)
    @ResponseBody
    public List<Integer> getGroups(
            @RequestParam("nienKhoa") String nienKhoa,
            @RequestParam("hocKy") String hocKy,
            @RequestParam("maMH") String maMH,
            @RequestParam(value = "maKhoa", required = false) String maKhoa,
            HttpSession httpSession) {
        Session session = factory.getCurrentSession();

        String sessionRole = (String) httpSession.getAttribute("role");
        String sessionMaKhoa = (String) httpSession.getAttribute("maKhoa");
        if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
        }

        StringBuilder hql =
                new StringBuilder(
                        "SELECT DISTINCT ltc.nhom FROM LopTinChi ltc " + "WHERE ltc.maMH = :maMH ");

        if (nienKhoa != null && !nienKhoa.isEmpty() && !nienKhoa.equals("all"))
            hql.append("AND ltc.nienKhoa = :nienKhoa ");
        if (hocKy != null && !hocKy.isEmpty() && !hocKy.equals("all"))
            hql.append("AND ltc.hocKy = :hocKy ");
        if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all"))
            hql.append("AND ltc.maKhoa = :maKhoa ");

        Query<Integer> query = session.createQuery(hql.toString(), Integer.class);
        query.setParameter("maMH", maMH);
        if (nienKhoa != null && !nienKhoa.isEmpty() && !nienKhoa.equals("all"))
            query.setParameter("nienKhoa", nienKhoa);
        if (hocKy != null && !hocKy.isEmpty() && !hocKy.equals("all"))
            query.setParameter("hocKy", Integer.parseInt(hocKy));
        if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all"))
            query.setParameter("maKhoa", maKhoa);

        return query.list();
    }

    @RequestMapping(value = "/load-students", method = RequestMethod.GET)
    @ResponseBody
    public List<Object[]> loadStudents(
            @RequestParam(value = "nienKhoa", required = false) String nienKhoa,
            @RequestParam(value = "hocKy", required = false) String hocKy,
            @RequestParam(value = "maMH", required = false) String maMH,
            @RequestParam(value = "nhom", required = false) Integer nhom,
            @RequestParam(value = "searchMaSV", required = false) String searchMaSV,
            @RequestParam(value = "maKhoa", required = false) String maKhoa,
            HttpSession httpSession) {
        Session session = factory.getCurrentSession();

        String sessionRole = (String) httpSession.getAttribute("role");
        String sessionMaKhoa = (String) httpSession.getAttribute("maKhoa");
        if ("KHOA".equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
        }

        StringBuilder hql =
                new StringBuilder(
                        "SELECT sv.maSV, sv.ho, sv.ten, dk.diemCC, dk.diemGK, dk.diemCK, dk.maLTC, ltc.nhom, mh.tenMH "
                                + "FROM DangKy dk "
                                + "JOIN SinhVien sv ON dk.maSV = sv.maSV "
                                + "JOIN LopTinChi ltc ON dk.maLTC = ltc.maLTC "
                                + "JOIN MonHoc mh ON ltc.maMH = mh.maMH "
                                + "WHERE (dk.huyDangKy = false OR dk.huyDangKy IS NULL) ");

        if (searchMaSV != null && !searchMaSV.trim().isEmpty()) {
            hql.append("AND TRIM(sv.maSV) = :searchMaSV ");
        } else {
            if (nienKhoa != null && !nienKhoa.isEmpty() && !nienKhoa.equals("all"))
                hql.append("AND ltc.nienKhoa = :nienKhoa ");
            if (hocKy != null && !hocKy.isEmpty() && !hocKy.equals("all"))
                hql.append("AND ltc.hocKy = :hocKy ");
            if (maMH != null && !maMH.isEmpty()) hql.append("AND ltc.maMH = :maMH ");
            if (nhom != null) hql.append("AND ltc.nhom = :nhom ");
            if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all"))
                hql.append("AND ltc.maKhoa = :maKhoa ");
        }

        hql.append("ORDER BY ltc.nienKhoa DESC, ltc.hocKy DESC, ltc.nhom, sv.maSV");

        Query<Object[]> query = session.createQuery(hql.toString(), Object[].class);
        if (searchMaSV != null && !searchMaSV.trim().isEmpty()) {
            query.setParameter("searchMaSV", searchMaSV.trim());
        } else {
            if (nienKhoa != null && !nienKhoa.isEmpty() && !nienKhoa.equals("all"))
                query.setParameter("nienKhoa", nienKhoa);
            if (hocKy != null && !hocKy.isEmpty() && !hocKy.equals("all"))
                query.setParameter("hocKy", Integer.parseInt(hocKy));
            if (maMH != null && !maMH.isEmpty()) query.setParameter("maMH", maMH);
            if (nhom != null) query.setParameter("nhom", nhom);
            if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all"))
                query.setParameter("maKhoa", maKhoa);
        }

        return query.list();
    }

    @RequestMapping(value = "/save-marks", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveMarks(
            @RequestParam("maLTC") int maLTC,
            @RequestParam("maSV") String maSV,
            @RequestParam(value = "diemCC", required = false) Float diemCC,
            @RequestParam(value = "diemGK", required = false) Float diemGK,
            @RequestParam(value = "diemCK", required = false) Float diemCK) {
        Map<String, Object> response = new HashMap<>();
        Session session = factory.openSession();
        org.hibernate.Transaction t = session.beginTransaction();
        try {
            // Using a query with TRIM to handle potential padding in CHAR columns
            String hql = "FROM DangKy dk WHERE dk.maLTC = :maLTC AND TRIM(dk.maSV) = :maSV";
            Query<DangKy> query = session.createQuery(hql, DangKy.class);
            query.setParameter("maLTC", maLTC);
            query.setParameter("maSV", maSV.trim());
            DangKy dk = query.uniqueResult();

            if (dk != null) {
                dk.setDiemCC(diemCC);
                dk.setDiemGK(diemGK);
                dk.setDiemCK(diemCK);
                session.merge(dk);
                t.commit();
                response.put("success", true);
                response.put("message", "Đã lưu điểm cho sinh viên " + maSV);
            } else {
                response.put("success", false);
                response.put(
                        "message",
                        "Không tìm thấy thông tin đăng ký cho SV: " + maSV + " tại lớp: " + maLTC);
            }
        } catch (Exception e) {
            t.rollback();
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        } finally {
            session.close();
        }
        return response;
    }

    @RequestMapping(value = "/save-all", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveAll(@RequestBody List<Map<String, Object>> marks) {
        Map<String, Object> response = new HashMap<>();
        Session session = factory.openSession();
        org.hibernate.Transaction t = session.beginTransaction();
        try {
            for (Map<String, Object> mark : marks) {
                int maLTC = (int) mark.get("maLTC");
                String maSV = (String) mark.get("maSV");

                Float diemCC =
                        mark.get("diemCC") != null && !mark.get("diemCC").toString().isEmpty()
                                ? Float.valueOf(mark.get("diemCC").toString())
                                : null;
                Float diemGK =
                        mark.get("diemGK") != null && !mark.get("diemGK").toString().isEmpty()
                                ? Float.valueOf(mark.get("diemGK").toString())
                                : null;
                Float diemCK =
                        mark.get("diemCK") != null && !mark.get("diemCK").toString().isEmpty()
                                ? Float.valueOf(mark.get("diemCK").toString())
                                : null;

                String hql = "FROM DangKy dk WHERE dk.maLTC = :maLTC AND TRIM(dk.maSV) = :maSV";
                Query<DangKy> query = session.createQuery(hql, DangKy.class);
                query.setParameter("maLTC", maLTC);
                query.setParameter("maSV", maSV.trim());
                DangKy dk = query.uniqueResult();

                if (dk != null) {
                    dk.setDiemCC(diemCC);
                    dk.setDiemGK(diemGK);
                    dk.setDiemCK(diemCK);
                    session.merge(dk);
                }
            }
            t.commit();
            response.put("success", true);
            response.put("message", "Đã lưu tất cả điểm thành công!");
        } catch (Exception e) {
            if (t != null) t.rollback();
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        } finally {
            session.close();
        }
        return response;
    }

    @RequestMapping(value = "/student", method = RequestMethod.GET)
    public String studentGrades(ModelMap model, HttpSession httpSession) {
        String role = (String) httpSession.getAttribute("role");
        if (!"SINHVIEN".equals(role)) {
            return "redirect:/login";
        }

        SinhVien profile = (SinhVien) httpSession.getAttribute("studentProfile");
        if (profile == null) {
            return "redirect:/login";
        }

        Session session = factory.getCurrentSession();

        // Fetch all registered marks with pure HQL JOIN (No Stored Procedure!)
        String hql =
                "SELECT ltc.nienKhoa, ltc.hocKy, ltc.maMH, mh.tenMH, ltc.nhom, "
                        + "dk.diemCC, dk.diemGK, dk.diemCK, mh.soTietLT, mh.soTietTH "
                        + "FROM DangKy dk "
                        + "JOIN LopTinChi ltc ON dk.maLTC = ltc.maLTC "
                        + "JOIN MonHoc mh ON ltc.maMH = mh.maMH "
                        + "WHERE TRIM(dk.maSV) = :maSV AND (dk.huyDangKy = false OR dk.huyDangKy IS NULL) "
                        + "ORDER BY ltc.nienKhoa DESC, ltc.hocKy DESC, ltc.maMH ASC";

        List<Object[]> marksList =
                session.createQuery(hql, Object[].class)
                        .setParameter("maSV", profile.getMaSV().trim())
                        .list();

        // Group grades by semester using LinkedHashMap to preserve query sort order
        java.util.Map<String, List<Object[]>> groupedMarks = new java.util.LinkedHashMap<>();
        for (Object[] row : marksList) {
            String nienKhoa = (String) row[0];
            Integer hocKy = (Integer) row[1];
            String semesterKey = "Học kỳ " + hocKy + " - Năm học " + nienKhoa;
            if (!groupedMarks.containsKey(semesterKey)) {
                groupedMarks.put(semesterKey, new java.util.ArrayList<>());
            }
            groupedMarks.get(semesterKey).add(row);
        }

        model.addAttribute("groupedMarks", groupedMarks);
        model.addAttribute("student", profile);

        return "mark/student";
    }
}
