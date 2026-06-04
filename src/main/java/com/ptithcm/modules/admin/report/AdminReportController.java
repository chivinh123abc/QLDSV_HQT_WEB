package com.ptithcm.modules.admin.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ptithcm.entities.Lop;
import com.ptithcm.modules.mark.MarkService;
import com.ptithcm.modules.report.ReportService;

@Controller
@RequestMapping("/admin/report")
public class AdminReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private MarkService markService;

    @GetMapping
    public String index(ModelMap model, @RequestParam(value = "reportType", required = false) String reportType,
            @RequestParam(value = "maLop", required = false) String maLop,
            @RequestParam(value = "nienKhoa", required = false) String nienKhoa,
            @RequestParam(value = "hocKy", required = false) Integer hocKy,
            @RequestParam(value = "maMH", required = false) String maMH,
            @RequestParam(value = "nhom", required = false) Integer nhom) {

        List<Lop> lopList = reportService.listLop();
        List<String> nienKhoaList = reportService.getNienKhoaList();

        model.addAttribute("lopList", lopList);
        model.addAttribute("nienKhoaList", nienKhoaList);

        if ("students".equals(reportType)) {
            if (nienKhoa != null && !nienKhoa.isEmpty() && hocKy != null) {
                List<Object[]> subjectList = markService.getSubjects(nienKhoa, String.valueOf(hocKy), null);
                model.addAttribute("subjectList", subjectList);

                if (maMH != null && !maMH.isEmpty()) {
                    List<Integer> groupList = markService.getGroups(nienKhoa, String.valueOf(hocKy), maMH, null);
                    model.addAttribute("groupList", groupList);
                }
            }
        }

        if ("summary".equals(reportType) && maLop != null && !maLop.isEmpty()) {
            try {
                List<Map<String, Object>> result = reportService.getSummaryMarks(maLop);
                if (!result.isEmpty()) {
                    List<String> columns = new ArrayList<>(result.get(0).keySet());
                    model.addAttribute("summaryColumns", columns);
                    model.addAttribute("summaryData", result);
                    model.addAttribute("showResult", true);
                    model.addAttribute("resultTitle", "BẢNG ĐIỂM TỔNG KẾT LỚP " + maLop);
                } else {
                    model.addAttribute("error", "Không có dữ liệu cho lớp này.");
                }
            } catch (Exception e) {
                model.addAttribute("error", "Lỗi: " + e.getMessage());
            }
        } else if ("students".equals(reportType) && nienKhoa != null && !nienKhoa.isEmpty() && hocKy != null
                && maMH != null && !maMH.isEmpty() && nhom != null) {
            try {
                List<Map<String, Object>> result = reportService.getCreditClassStudents(nienKhoa, hocKy, maMH, nhom);
                if (!result.isEmpty()) {
                    model.addAttribute("studentData", result);
                    model.addAttribute("showResult", true);
                    model.addAttribute("resultTitle", "DANH SÁCH SINH VIÊN ĐĂNG KÝ LỚP TÍN CHỈ");
                } else {
                    model.addAttribute("error", "Không có sinh viên nào đăng ký lớp tín chỉ này.");
                }
            } catch (Exception e) {
                model.addAttribute("error", "Lỗi: " + e.getMessage());
            }
        }

        model.addAttribute("reportType", reportType);
        model.addAttribute("maLop", maLop);
        model.addAttribute("nienKhoa", nienKhoa);
        model.addAttribute("hocKy", hocKy);
        model.addAttribute("maMH", maMH);
        model.addAttribute("nhom", nhom);

        return "admin/report/index";
    }
}
