package com.ptithcm.modules.subject;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptithcm.entities.MonHoc;
import com.ptithcm.shared.validators.SubjectValidator;

@Controller
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SubjectValidator monHocValidator;

    @GetMapping
    public String index(ModelMap model) {
        List<MonHoc> monHocList = subjectService.listMonHoc();
        populateCanDelete(monHocList);
        model.addAttribute("monHocList", monHocList);
        return "subject/index";
    }

    @PostMapping(params = "btnInsert")
    public String insert(ModelMap model, MonHoc monHoc, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        monHocValidator.validate(monHoc, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Lỗi nhập liệu môn học!");
            model.addAttribute("monHoc", monHoc);
            model.addAttribute("mode", "add");
            return index(model);
        }
        try {
            subjectService.saveMonHoc(monHoc, "add");
            redirectAttributes.addFlashAttribute("message", "Thêm môn học [" + monHoc.getMaMH() + "] thành công!");
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("monHoc", monHoc);
            model.addAttribute("mode", "add");
            return index(model);
        }
        return "redirect:/subject";
    }

    @PostMapping(params = "btnUpdate")
    public String update(ModelMap model, MonHoc monHoc, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        monHocValidator.validate(monHoc, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Lỗi nhập liệu môn học!");
            model.addAttribute("monHoc", monHoc);
            model.addAttribute("mode", "edit");
            return index(model);
        }
        try {
            subjectService.saveMonHoc(monHoc, "edit");
            redirectAttributes.addFlashAttribute("message", "Cập nhật môn học [" + monHoc.getMaMH() + "] thành công!");
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("monHoc", monHoc);
            model.addAttribute("mode", "edit");
            return index(model);
        }
        return "redirect:/subject";
    }

    @PostMapping(params = "btnDelete")
    public String delete(@RequestParam("maMH") String maMH, RedirectAttributes redirectAttributes) {
        try {
            subjectService.deleteMonHoc(maMH);
            redirectAttributes.addFlashAttribute("message", "Xóa môn học [" + maMH + "] thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa môn học: " + e.getMessage());
        }
        return "redirect:/subject";
    }

    @GetMapping(params = "lnkEdit")
    public String edit(ModelMap model, @RequestParam("maMH") String maMH) {
        MonHoc monHoc = subjectService.getMonHocById(maMH);
        model.addAttribute("monHoc", monHoc);
        model.addAttribute("mode", "edit");
        return index(model);
    }

    private void populateCanDelete(List<MonHoc> list) {
        if (list.isEmpty()) {
            return;
        }
        List<String> mhWithLTC = subjectService.listTrimmedSubjectIdsFromLtc();
        java.util.Set<String> dependentIds = new java.util.HashSet<>();
        for (String id : mhWithLTC) {
            if (id != null) {
                dependentIds.add(id.trim().toUpperCase());
            }
        }

        for (MonHoc mh : list) {
            String trimmed = mh.getMaMH() != null ? mh.getMaMH().trim().toUpperCase() : "";
            mh.setCanDelete(!dependentIds.contains(trimmed));
        }
    }
}
