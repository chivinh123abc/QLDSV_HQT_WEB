package com.ptithcm.modules.report;

import com.ptithcm.entity.Lop;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @RequestMapping()
    public String index(ModelMap model) {
        List<Lop> lopList = reportService.listLop();
        List<String> nienKhoaList = reportService.getNienKhoaList();

        model.addAttribute("lopList", lopList);
        model.addAttribute("nienKhoaList", nienKhoaList);
        return "report/index";
    }

    @RequestMapping(value = "/summary-marks", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getSummaryMarks(@RequestParam("maLop") String maLop) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> result = reportService.getSummaryMarks(maLop);

            if (result.isEmpty()) {
                response.put("success", false);
                response.put("message", "Không có dữ liệu cho lớp này.");
                return response;
            }

            // Trích xuất các cột từ dòng đầu tiên để gửi lại cho client
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
    public Map<String, Object> getCreditClassStudents(@RequestParam("nienKhoa") String nienKhoa,
            @RequestParam("hocKy") int hocKy, @RequestParam("maMH") String maMH, @RequestParam("nhom") int nhom) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> result = reportService.getCreditClassStudents(nienKhoa, hocKy, maMH, nhom);
            response.put("success", true);
            response.put("data", result);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
        }
        return response;
    }
}
