package com.ptithcm.modules.diemso;

import com.ptithcm.entity.Khoa;
import com.ptithcm.entity.SinhVien;
import com.ptithcm.shared.enumtype.RoleEnum;
import com.ptithcm.shared.util.SessionUtil;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mark")
public class MarkController {

    @Autowired
    private MarkService markService;

    @RequestMapping()
    public String index(ModelMap model, HttpSession httpSession) {
        String sessionRole = SessionUtil.getRole(httpSession);
        String sessionMaKhoa = SessionUtil.getMaKhoa(httpSession);

        // Lấy danh sách niên khóa duy nhất
        List<String> nienKhoaList = markService.getNienKhoaList();
        model.addAttribute("nienKhoaList", nienKhoaList);

        // Lấy danh sách Khoa cho PGV (Phòng Giáo Vụ)
        List<Khoa> khoaList = markService.listKhoa();
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            khoaList = khoaList.stream().filter(k -> k.getMaKhoa().equals(sessionMaKhoa))
                    .collect(java.util.stream.Collectors.toList());
        }
        model.addAttribute("khoaList", khoaList);

        return "mark/index";
    }

    @RequestMapping(value = "/get-subjects", method = RequestMethod.GET)
    @ResponseBody
    public List<Object[]> getSubjects(@RequestParam("nienKhoa") String nienKhoa, @RequestParam("hocKy") String hocKy,
            @RequestParam(value = "maKhoa", required = false) String maKhoa, HttpSession httpSession) {

        String sessionRole = SessionUtil.getRole(httpSession);
        String sessionMaKhoa = SessionUtil.getMaKhoa(httpSession);
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
        }

        return markService.getSubjects(nienKhoa, hocKy, maKhoa);
    }

    @RequestMapping(value = "/get-groups", method = RequestMethod.GET)
    @ResponseBody
    public List<Integer> getGroups(@RequestParam("nienKhoa") String nienKhoa, @RequestParam("hocKy") String hocKy,
            @RequestParam("maMH") String maMH, @RequestParam(value = "maKhoa", required = false) String maKhoa,
            HttpSession httpSession) {

        String sessionRole = SessionUtil.getRole(httpSession);
        String sessionMaKhoa = SessionUtil.getMaKhoa(httpSession);
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
        }

        return markService.getGroups(nienKhoa, hocKy, maMH, maKhoa);
    }

    @RequestMapping(value = "/load-students", method = RequestMethod.GET)
    @ResponseBody
    public List<Object[]> loadStudents(@RequestParam(value = "nienKhoa", required = false) String nienKhoa,
            @RequestParam(value = "hocKy", required = false) String hocKy,
            @RequestParam(value = "maMH", required = false) String maMH,
            @RequestParam(value = "nhom", required = false) Integer nhom,
            @RequestParam(value = "searchMaSV", required = false) String searchMaSV,
            @RequestParam(value = "maKhoa", required = false) String maKhoa, HttpSession httpSession) {

        String sessionRole = SessionUtil.getRole(httpSession);
        String sessionMaKhoa = SessionUtil.getMaKhoa(httpSession);
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
        }

        return markService.loadStudents(nienKhoa, hocKy, maMH, nhom, searchMaSV, maKhoa);
    }

    @RequestMapping(value = "/save-marks", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveMarks(@RequestParam("maLTC") String maLTC, @RequestParam("maSV") String maSV,
            @RequestParam(value = "diemCC", required = false) Float diemCC,
            @RequestParam(value = "diemGK", required = false) Float diemGK,
            @RequestParam(value = "diemCK", required = false) Float diemCK) {
        Map<String, Object> response = new HashMap<>();
        try {
            markService.saveMark(maLTC, maSV, diemCC, diemGK, diemCK);
            response.put("success", true);
            response.put("message", "Đã lưu điểm cho sinh viên " + maSV);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/save-all", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveAll(@RequestBody List<Map<String, Object>> marks) {
        Map<String, Object> response = new HashMap<>();
        try {
            markService.saveAllMarks(marks);
            response.put("success", true);
            response.put("message", "Đã lưu tất cả điểm thành công!");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/student", method = RequestMethod.GET)
    public String studentGrades(ModelMap model, HttpSession httpSession) {
        String role = SessionUtil.getRole(httpSession);
        if (!RoleEnum.SINHVIEN.getCode().equals(role)) {
            return "redirect:/login";
        }

        SinhVien profile = SessionUtil.getStudentProfile(httpSession);
        if (profile == null) {
            return "redirect:/login";
        }

        List<Object[]> marksList = markService.getStudentGrades(profile.getMaSV().trim());

        // Nhóm điểm số theo học kỳ sử dụng LinkedHashMap để giữ nguyên thứ tự sắp xếp
        // của câu truy vấn
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
