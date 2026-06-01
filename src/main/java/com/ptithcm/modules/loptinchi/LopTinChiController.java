package com.ptithcm.modules.loptinchi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ptithcm.entities.GiangVien;
import com.ptithcm.entities.Khoa;
import com.ptithcm.entities.LopTinChi;
import com.ptithcm.entities.MonHoc;
import com.ptithcm.shared.constants.SessionConstant;
import com.ptithcm.shared.enums.RoleEnum;
import com.ptithcm.shared.validators.LopTinChiValidator;

@Controller
@RequestMapping("/credit-class")
public class LopTinChiController {

    @Autowired
    private LopTinChiService lopTinChiService;

    @Autowired
    private LopTinChiValidator lopTinChiValidator;

    @RequestMapping()
    public String index(ModelMap model, HttpSession httpSession) {
        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        String sessionMaKhoa = (String) httpSession.getAttribute(SessionConstant.MA_KHOA);

        List<LopTinChi> ltcList;
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            ltcList = lopTinChiService.listLtcByKhoa(sessionMaKhoa);
        } else {
            ltcList = lopTinChiService.listAllLtc();
        }

        populateCanDelete(ltcList);

        List<Khoa> khoaList = lopTinChiService.listKhoa();
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            khoaList = khoaList.stream().filter(k -> k.getMaKhoa().equals(sessionMaKhoa)).collect(Collectors.toList());
        }

        List<MonHoc> monHocList = lopTinChiService.listMonHoc();
        List<GiangVien> giangVienList;
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            giangVienList = lopTinChiService.listGiangVienByKhoa(sessionMaKhoa);
        } else {
            giangVienList = lopTinChiService.listAllGiangVien();
        }

        model.addAttribute("ltcList", ltcList);
        model.addAttribute("khoaList", khoaList);
        model.addAttribute("monHocList", monHocList);
        model.addAttribute("giangVienList", giangVienList);
        return "credit-class/index";
    }

    @RequestMapping(params = "btnInsert")
    public String insert(ModelMap model, LopTinChi ltc, HttpSession httpSession) {
        try {
            lopTinChiService.insertLtc(ltc);
            model.addAttribute("message", "Đã thêm lớp tín chỉ");
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi: " + e.getMessage());
        }
        return index(model, httpSession);
    }

    @RequestMapping(params = "btnUpdate")
    public String update(ModelMap model, LopTinChi ltc, HttpSession httpSession) {
        try {
            lopTinChiService.updateLtc(ltc);
            model.addAttribute("message", "Đã cập nhật lớp tín chỉ");
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi: " + e.getMessage());
        }
        return index(model, httpSession);
    }

    @RequestMapping(params = "btnDelete")
    public String delete(ModelMap model, @RequestParam("maLTC") String maLTC, HttpSession httpSession) {
        try {
            lopTinChiService.deleteLtc(maLTC);
            model.addAttribute("message", "Đã xóa lớp tín chỉ: " + maLTC);
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi: " + e.getMessage());
        }
        return index(model, httpSession);
    }

    // TODO: Cần kiểm tra lại vì không được tạo API
    // --- CÁC ENDPOINT AJAX API ---

    @RequestMapping(value = "/api/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public LopTinChi getLTC(@RequestParam("maLTC") String maLTC) {
        return lopTinChiService.getLtcById(maLTC);
    }

    @RequestMapping(value = "/api/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<LopTinChi> listLTC(@RequestParam(value = "maKhoa", required = false) String maKhoa,
            HttpSession httpSession) {

        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        String sessionMaKhoa = (String) httpSession.getAttribute(SessionConstant.MA_KHOA);

        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
        }

        List<LopTinChi> list;
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            list = lopTinChiService.listLtcByKhoa(sessionMaKhoa);
        } else if (maKhoa == null || maKhoa.isEmpty() || maKhoa.equals("all")) {
            list = lopTinChiService.listAllLtc();
        } else {
            list = lopTinChiService.listLtcByKhoa(maKhoa);
        }
        populateCanDelete(list);
        return list;
    }

    private void populateCanDelete(List<LopTinChi> list) {
        if (list.isEmpty())
            return;
        List<String> ltcWithReg = lopTinChiService.listLtcIdsWithRegistrations();
        java.util.Set<String> dependentIds = new java.util.HashSet<>(ltcWithReg);

        for (LopTinChi ltc : list) {
            ltc.setCanDelete(!dependentIds.contains(ltc.getMaLTC()));
        }
    }

    @RequestMapping(value = "/api/save", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> saveLTC(@RequestBody LopTinChi ltc, @RequestParam("mode") String mode) {
        Map<String, Object> res = new HashMap<>();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(ltc, "lopTinChi");
        lopTinChiValidator.validate(ltc, bindingResult);
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldErrors().stream()
                    .map(org.springframework.validation.FieldError::getDefaultMessage)
                    .collect(Collectors.joining("<br>"));
            res.put("status", "error");
            res.put("message", errorMsg);
            return res;
        }
        try {
            lopTinChiService.saveLtcApi(ltc, mode);
            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Lỗi: " + e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/api/delete", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> deleteLTC(@RequestParam("maLTC") String maLTC) {
        Map<String, Object> res = new HashMap<>();
        try {
            lopTinChiService.deleteLtcApi(maLTC);
            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Lỗi: " + e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/api/monhoc", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<MonHoc> listMonHoc() {
        return lopTinChiService.listMonHoc();
    }

    @RequestMapping(value = "/api/gv", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<GiangVien> listGiangVien(@RequestParam(value = "maKhoa", required = false) String maKhoa,
            HttpSession httpSession) {

        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        String sessionMaKhoa = (String) httpSession.getAttribute(SessionConstant.MA_KHOA);

        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
        }

        if (maKhoa == null || maKhoa.isEmpty() || maKhoa.equals("all")) {
            return lopTinChiService.listAllGiangVien();
        }
        return lopTinChiService.listGiangVienByKhoa(maKhoa);
    }
}
