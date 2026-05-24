package com.ptithcm.modules.dangky;

import com.ptithcm.entity.DangKy;
import com.ptithcm.entity.LopTinChi;
import com.ptithcm.shared.constant.MessageConstant;

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
@RequestMapping("/registration")
public class DangKyController {

    @Autowired
    private DangKyService dangKyService;

    @RequestMapping()
    public String index(ModelMap model) {
        List<DangKy> registrationList = dangKyService.listRegistration();
        model.addAttribute("registrationList", registrationList);
        return "registration/index";
    }

    @RequestMapping(params = "btnInsert")
    public String insert(ModelMap model, @RequestParam("maLTC") String maLTC, @RequestParam("maSV") String maSV) {
        try {
            dangKyService.registerClass(maLTC, maSV);
            model.addAttribute("message", MessageConstant.SUCCESS_REGISTER);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (e.getCause() != null)
                errorMsg = e.getCause().getMessage();
            model.addAttribute("message", "Lỗi: " + errorMsg);
        }
        return index(model);
    }

    @RequestMapping(params = "btnUpdate")
    public String update(ModelMap model, DangKy dangKy) {
        try {
            dangKyService.updateRegistration(dangKy);
            model.addAttribute("message", MessageConstant.SUCCESS_UPDATE);
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi: " + e.getMessage());
        }
        return index(model);
    }

    @RequestMapping(params = "btnDelete")
    public String delete(ModelMap model, @RequestParam("maLTC") String maLTC, @RequestParam("maSV") String maSV) {
        try {
            dangKyService.cancelRegistration(maLTC, maSV);
            model.addAttribute("message", "Đã hủy đăng ký thành công");
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (e.getCause() != null)
                errorMsg = e.getCause().getMessage();
            model.addAttribute("message", "Lỗi: " + errorMsg);
        }
        return index(model);
    }

    // TODO: Cần kiểm tra lại vì không được tạo API
    // --- CÁC ENDPOINT AJAX API ---

    @RequestMapping(value = "/api/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<DangKy> listRegistration() {
        return dangKyService.listRegistration();
    }

    @RequestMapping(value = "/api/register", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> apiRegister(@RequestParam("maLTC") String maLTC, @RequestParam("maSV") String maSV) {
        Map<String, Object> res = new HashMap<>();
        try {
            dangKyService.registerClass(maLTC, maSV);
            res.put("status", "success");
            res.put("message", MessageConstant.SUCCESS_REGISTER);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (e.getCause() != null)
                errorMsg = e.getCause().getMessage();
            res.put("status", "error");
            res.put("message", "Lỗi: " + errorMsg);
        }
        return res;
    }

    @RequestMapping(value = "/api/cancel", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> apiCancel(@RequestParam("maLTC") String maLTC, @RequestParam("maSV") String maSV) {
        Map<String, Object> res = new HashMap<>();
        try {
            dangKyService.cancelRegistration(maLTC, maSV);
            res.put("status", "success");
            res.put("message", "Đã hủy đăng ký thành công!");
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (e.getCause() != null)
                errorMsg = e.getCause().getMessage();
            res.put("status", "error");
            res.put("message", "Lỗi: " + errorMsg);
        }
        return res;
    }

    @RequestMapping(value = "/api/available-classes", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<LopTinChi> availableClasses() {
        return dangKyService.getAvailableClasses();
    }
}
