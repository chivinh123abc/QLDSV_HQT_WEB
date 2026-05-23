package com.ptithcm.controller;

import com.ptithcm.entity.Lop;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Transactional
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private SessionFactory factory;

    @RequestMapping()
    public String index(ModelMap model) {
        Session session = factory.getCurrentSession();
        List<Lop> lopList = session.createQuery("FROM Lop", Lop.class).list();
        List<String> nienKhoaList = session.createQuery("SELECT DISTINCT ltc.nienKhoa FROM LopTinChi ltc", String.class)
                .list();

        model.addAttribute("lopList", lopList);
        model.addAttribute("nienKhoaList", nienKhoaList);
        return "report/index";
    }

    @RequestMapping(value = "/summary-marks", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
    public Map<String, Object> getSummaryMarks(@RequestParam("maLop") String maLop) {
        Map<String, Object> response = new HashMap<>();
        Session session = factory.getCurrentSession();

        try {
            NativeQuery query = session.createNativeQuery("EXEC sp_LayBangDiemTongKet :maLop");
            query.setParameter("maLop", maLop);
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

            List<Map<String, Object>> result = query.list();

            if (result.isEmpty()) {
                response.put("success", false);
                response.put("message", "Không có dữ liệu cho lớp này.");
                return response;
            }

            // Extract columns from the first row to send back to client
            List<String> columns = new ArrayList<>(result.get(0).keySet());

            response.put("success", true);
            response.put("columns", columns);
            response.put("data", result);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/credit-class-students", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
    public Map<String, Object> getCreditClassStudents(@RequestParam("nienKhoa") String nienKhoa,
            @RequestParam("hocKy") int hocKy, @RequestParam("maMH") String maMH, @RequestParam("nhom") int nhom) {
        Map<String, Object> response = new HashMap<>();
        Session session = factory.getCurrentSession();

        try {
            NativeQuery query = session
                    .createNativeQuery("EXEC sp_LayDanhSachSinhVienDangKyLopTinChi :nk, :hk, :mh, :nhom");
            query.setParameter("nk", nienKhoa);
            query.setParameter("hk", hocKy);
            query.setParameter("mh", maMH);
            query.setParameter("nhom", nhom);
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

            List<Map<String, Object>> result = query.list();

            response.put("success", true);
            response.put("data", result);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        }
        return response;
    }
}
