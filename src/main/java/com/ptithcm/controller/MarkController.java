package com.ptithcm.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ptithcm.entity.DangKy;
import com.ptithcm.entity.DangKyId;
import com.ptithcm.entity.LopTinChi;
import com.ptithcm.entity.MonHoc;

@Controller
@Transactional
@RequestMapping("/mark")
public class MarkController {

    @Autowired
    private SessionFactory factory;

    @RequestMapping()
    public String index(ModelMap model) {
        Session session = factory.getCurrentSession();
        
        // Get unique School Years
        List<String> nienKhoaList = session.createQuery("SELECT DISTINCT ltc.nienKhoa FROM LopTinChi ltc", String.class).list();
        model.addAttribute("nienKhoaList", nienKhoaList);
        
        return "mark/index";
    }

    @RequestMapping(value = "/get-subjects", method = RequestMethod.GET)
    @ResponseBody
    public List<Object[]> getSubjects(@RequestParam("nienKhoa") String nienKhoa, @RequestParam("hocKy") int hocKy) {
        Session session = factory.getCurrentSession();
        String hql = "SELECT DISTINCT mh.maMH, mh.tenMH FROM LopTinChi ltc JOIN MonHoc mh ON ltc.maMH = mh.maMH " +
                     "WHERE ltc.nienKhoa = :nienKhoa AND ltc.hocKy = :hocKy";
        Query query = session.createQuery(hql);
        query.setParameter("nienKhoa", nienKhoa);
        query.setParameter("hocKy", hocKy);
        return query.list();
    }

    @RequestMapping(value = "/get-groups", method = RequestMethod.GET)
    @ResponseBody
    public List<Integer> getGroups(@RequestParam("nienKhoa") String nienKhoa, 
                                  @RequestParam("hocKy") int hocKy, 
                                  @RequestParam("maMH") String maMH) {
        Session session = factory.getCurrentSession();
        String hql = "SELECT DISTINCT ltc.nhom FROM LopTinChi ltc " +
                     "WHERE ltc.nienKhoa = :nienKhoa AND ltc.hocKy = :hocKy AND ltc.maMH = :maMH";
        Query query = session.createQuery(hql);
        query.setParameter("nienKhoa", nienKhoa);
        query.setParameter("hocKy", hocKy);
        query.setParameter("maMH", maMH);
        return query.list();
    }

    @RequestMapping(value = "/load-students", method = RequestMethod.GET)
    @ResponseBody
    public List<Object[]> loadStudents(@RequestParam("nienKhoa") String nienKhoa, 
                                      @RequestParam("hocKy") int hocKy, 
                                      @RequestParam("maMH") String maMH, 
                                      @RequestParam("nhom") int nhom) {
        Session session = factory.getCurrentSession();
        
        // Get maLTC first
        String hqlLTC = "SELECT ltc.maLTC FROM LopTinChi ltc " +
                        "WHERE ltc.nienKhoa = :nienKhoa AND ltc.hocKy = :hocKy AND ltc.maMH = :maMH AND ltc.nhom = :nhom";
        Query<Integer> queryLTC = session.createQuery(hqlLTC, Integer.class);
        queryLTC.setParameter("nienKhoa", nienKhoa);
        queryLTC.setParameter("hocKy", hocKy);
        queryLTC.setParameter("maMH", maMH);
        queryLTC.setParameter("nhom", nhom);
        Integer maLTC = queryLTC.uniqueResult();

        if (maLTC == null) return null;

        // Get students and marks
        String hql = "SELECT sv.maSV, sv.ho, sv.ten, dk.diemCC, dk.diemGK, dk.diemCK, dk.maLTC " +
                     "FROM DangKy dk JOIN SinhVien sv ON dk.maSV = sv.maSV " +
                     "WHERE dk.maLTC = :maLTC AND (dk.huyDangKy = false OR dk.huyDangKy IS NULL)";
        Query query = session.createQuery(hql);
        query.setParameter("maLTC", maLTC);
        return query.list();
    }

    @RequestMapping(value = "/save-marks", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveMarks(@RequestParam("maLTC") int maLTC, 
                                        @RequestParam("maSV") String maSV, 
                                        @RequestParam(value="diemCC", required=false) Float diemCC, 
                                        @RequestParam(value="diemGK", required=false) Float diemGK, 
                                        @RequestParam(value="diemCK", required=false) Float diemCK) {
        Map<String, Object> response = new HashMap<>();
        Session session = factory.openSession();
        org.hibernate.Transaction t = session.beginTransaction();
        try {
            DangKyId id = new DangKyId(maLTC, maSV);
            DangKy dk = session.get(DangKy.class, id);
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
                response.put("message", "Không tìm thấy thông tin đăng ký");
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
}
