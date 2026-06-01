package com.ptithcm.modules.monhoc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ptithcm.entities.MonHoc;
import com.ptithcm.shared.validators.MonHocValidator;

@Controller
@RequestMapping("/subject")
public class MonHocController {

    @Autowired
    private MonHocService monHocService;

    @Autowired
    private MonHocValidator monHocValidator;

    @RequestMapping()
    public String index(ModelMap model) {
        List<MonHoc> monHocList = monHocService.listMonHoc();
        populateCanDelete(monHocList);
        model.addAttribute("monHocList", monHocList);
        return "subject/index";
    }

    @RequestMapping(params = "btnInsert")
    public String insert(ModelMap model, MonHoc monHoc) {
        try {
            monHocService.saveMonHoc(monHoc, "add");
            model.addAttribute("message", "Đã thêm môn học: " + monHoc.getTenMH());
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi: " + e.getMessage());
        }
        return index(model);
    }

    @RequestMapping(params = "btnUpdate")
    public String update(ModelMap model, MonHoc monHoc) {
        try {
            monHocService.saveMonHoc(monHoc, "edit");
            model.addAttribute("message", "Đã cập nhật môn học: " + monHoc.getTenMH());
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi: " + e.getMessage());
        }
        return index(model);
    }

    @RequestMapping(params = "btnDelete")
    public String delete(ModelMap model, @RequestParam("maMH") String maMH) {
        try {
            monHocService.deleteMonHoc(maMH);
            model.addAttribute("message", "Đã xóa môn học: " + maMH);
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi: " + e.getMessage());
        }
        return index(model);
    }

    @RequestMapping(params = "lnkEdit")
    public String edit(ModelMap model, @RequestParam("maMH") String maMH) {
        MonHoc monHoc = monHocService.getMonHocById(maMH);
        model.addAttribute("monHoc", monHoc);
        return index(model);
    }

    // TODO: Cần kiểm tra lại vì không được tạo API
    // --- CÁC ENDPOINT AJAX API ---

    @RequestMapping(value = "/api/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public MonHoc getSubject(@RequestParam("maMH") String maMH) {
        return monHocService.getMonHocById(maMH);
    }

    @RequestMapping(value = "/api/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<MonHoc> listSubjects() {
        List<MonHoc> list = monHocService.listMonHoc();
        populateCanDelete(list);
        return list;
    }

    private void populateCanDelete(List<MonHoc> list) {
        if (list.isEmpty())
            return;
        List<String> mhWithLTC = monHocService.listTrimmedSubjectIdsFromLtc();
        java.util.Set<String> dependentIds = new java.util.HashSet<>();
        for (String id : mhWithLTC)
            if (id != null)
                dependentIds.add(id.trim().toUpperCase());

        for (MonHoc mh : list) {
            String trimmed = mh.getMaMH() != null ? mh.getMaMH().trim().toUpperCase() : "";
            mh.setCanDelete(!dependentIds.contains(trimmed));
        }
    }

    @RequestMapping(value = "/api/save", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> saveSubject(@RequestBody MonHoc monHoc, @RequestParam("mode") String mode) {
        Map<String, Object> res = new HashMap<>();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(monHoc, "monHoc");
        monHocValidator.validate(monHoc, bindingResult);
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldErrors().stream()
                    .map(org.springframework.validation.FieldError::getDefaultMessage)
                    .collect(Collectors.joining("<br>"));
            res.put("status", "error");
            res.put("message", errorMsg);
            return res;
        }
        try {
            monHocService.saveMonHoc(monHoc, mode);
            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/api/delete", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> deleteSubject(@RequestParam("maMH") String maMH) {
        Map<String, Object> res = new HashMap<>();
        try {
            monHocService.deleteMonHoc(maMH);
            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", e.getMessage());
        }
        return res;
    }
}
