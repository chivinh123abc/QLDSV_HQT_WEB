package com.ptithcm.modules.admin.mark;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptithcm.entities.Khoa;
import com.ptithcm.modules.mark.MarkService;
import com.ptithcm.shared.enums.RoleEnum;
import com.ptithcm.shared.utils.SessionUtil;

@Controller
@RequestMapping("/admin/mark")
public class AdminMarkController {

    @Autowired
    private MarkService markService;

    @GetMapping
    public String index(ModelMap model, @RequestParam(value = "maKhoa", required = false) String maKhoa,
            @RequestParam(value = "nienKhoa", required = false) String nienKhoa,
            @RequestParam(value = "hocKy", required = false) String hocKy,
            @RequestParam(value = "maMH", required = false) String maMH,
            @RequestParam(value = "nhom", required = false) Integer nhom,
            @RequestParam(value = "searchMaSV", required = false) String searchMaSV, HttpSession httpSession) {

        String sessionRole = SessionUtil.getRole(httpSession);
        String sessionMaKhoa = SessionUtil.getMaKhoa(httpSession);

        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
        }

        // Base lists
        List<String> nienKhoaList = markService.getNienKhoaList();
        model.addAttribute("nienKhoaList", nienKhoaList);

        List<Khoa> khoaList = markService.listKhoa();
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            khoaList = khoaList.stream().filter(k -> k.getMaKhoa().equals(sessionMaKhoa)).collect(Collectors.toList());
        }
        model.addAttribute("khoaList", khoaList);

        // Dependent dropdowns & student lists loaded in SSR
        if (nienKhoa != null && !nienKhoa.isEmpty() && !"all".equals(nienKhoa) && hocKy != null && !hocKy.isEmpty()
                && !"all".equals(hocKy)) {
            List<Object[]> subjectList = markService.getSubjects(nienKhoa, hocKy, maKhoa);
            model.addAttribute("subjectList", subjectList);

            if (maMH != null && !maMH.isEmpty()) {
                List<Integer> groupList = markService.getGroups(nienKhoa, hocKy, maMH, maKhoa);
                model.addAttribute("groupList", groupList);

                if (nhom != null || (searchMaSV != null && !searchMaSV.trim().isEmpty())) {
                    List<Object[]> studentList = markService.loadStudents(nienKhoa, hocKy, maMH, nhom, searchMaSV,
                            maKhoa);
                    model.addAttribute("studentMarkList", studentList);
                }
            }
        } else if (searchMaSV != null && !searchMaSV.trim().isEmpty()) {
            List<Object[]> studentList = markService.loadStudents(null, null, null, null, searchMaSV, maKhoa);
            model.addAttribute("studentMarkList", studentList);
        }

        model.addAttribute("maKhoa", maKhoa);
        model.addAttribute("nienKhoa", nienKhoa);
        model.addAttribute("hocKy", hocKy);
        model.addAttribute("maMH", maMH);
        model.addAttribute("nhom", nhom);
        model.addAttribute("searchMaSV", searchMaSV);

        return "admin/mark/index";
    }

    @PostMapping("/save")
    public String saveMarks(@RequestParam("maSV") List<String> maSVs, @RequestParam("maLTC") List<String> maLTCs,
            @RequestParam(value = "diemCC", required = false) List<String> diemCCs,
            @RequestParam(value = "diemGK", required = false) List<String> diemGKs,
            @RequestParam(value = "diemCK", required = false) List<String> diemCKs,
            @RequestParam(value = "nienKhoa", required = false) String nienKhoa,
            @RequestParam(value = "hocKy", required = false) String hocKy,
            @RequestParam(value = "maMH", required = false) String maMH,
            @RequestParam(value = "nhom", required = false) Integer nhom,
            @RequestParam(value = "searchMaSV", required = false) String searchMaSV,
            @RequestParam(value = "maKhoa", required = false) String maKhoa, RedirectAttributes redirectAttributes) {

        try {
            for (int i = 0; i < maSVs.size(); i++) {
                String maSV = maSVs.get(i);
                String maLTC = maLTCs.get(i);

                String ccStr = (diemCCs != null && i < diemCCs.size()) ? diemCCs.get(i) : "";
                String gkStr = (diemGKs != null && i < diemGKs.size()) ? diemGKs.get(i) : "";
                String ckStr = (diemCKs != null && i < diemCKs.size()) ? diemCKs.get(i) : "";

                Float cc = (ccStr == null || ccStr.trim().isEmpty()) ? null : Float.valueOf(ccStr.trim());
                Float gk = (gkStr == null || gkStr.trim().isEmpty()) ? null : Float.valueOf(gkStr.trim());
                Float ck = (ckStr == null || ckStr.trim().isEmpty()) ? null : Float.valueOf(ckStr.trim());

                markService.saveMark(maLTC, maSV, cc, gk, ck);
            }
            redirectAttributes.addFlashAttribute("message", "Đã lưu tất cả điểm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu điểm: " + e.getMessage());
        }

        StringBuilder redirectUrl = new StringBuilder("redirect:/admin/mark?");
        if (nienKhoa != null)
            redirectUrl.append("nienKhoa=").append(nienKhoa).append("&");
        if (hocKy != null)
            redirectUrl.append("hocKy=").append(hocKy).append("&");
        if (maMH != null)
            redirectUrl.append("maMH=").append(maMH).append("&");
        if (nhom != null)
            redirectUrl.append("nhom=").append(nhom).append("&");
        if (searchMaSV != null)
            redirectUrl.append("searchMaSV=").append(searchMaSV).append("&");
        if (maKhoa != null)
            redirectUrl.append("maKhoa=").append(maKhoa);

        return redirectUrl.toString();
    }
}
