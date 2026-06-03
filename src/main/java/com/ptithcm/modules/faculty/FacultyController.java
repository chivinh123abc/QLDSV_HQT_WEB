package com.ptithcm.modules.faculty;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptithcm.entities.Khoa;
import com.ptithcm.modules.faculty.dtos.FacultyDTO;

@Controller
@RequestMapping("/faculty")
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @GetMapping
    public String index(ModelMap model) {
        List<Khoa> khoaList = facultyService.listKhoa();
        populateCanDelete(khoaList);
        model.addAttribute("khoaList", khoaList);
        return "faculty/index";
    }

    @PostMapping(params = "btnInsert")
    public String insert(ModelMap model, @Valid @ModelAttribute("khoa") FacultyDTO facultyDto,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error",
                    "Lỗi nhập liệu khoa: " + bindingResult.getFieldErrors().stream()
                            .map(org.springframework.validation.FieldError::getDefaultMessage)
                            .collect(Collectors.joining("<br>")));
            model.addAttribute("khoa", facultyDto);
            model.addAttribute("mode", "add");
            return index(model);
        }
        try {
            Khoa khoa = new Khoa();
            khoa.setMaKhoa(facultyDto.getMaKhoa());
            khoa.setTenKhoa(facultyDto.getTenKhoa());
            facultyService.saveKhoa(khoa, "add");
            redirectAttributes.addFlashAttribute("message", "Thêm khoa [" + khoa.getMaKhoa() + "] thành công!");
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("khoa", facultyDto);
            model.addAttribute("mode", "add");
            return index(model);
        }
        return "redirect:/faculty";
    }

    @PostMapping(params = "btnUpdate")
    public String update(ModelMap model, @Valid @ModelAttribute("khoa") FacultyDTO facultyDto,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error",
                    "Lỗi nhập liệu khoa: " + bindingResult.getFieldErrors().stream()
                            .map(org.springframework.validation.FieldError::getDefaultMessage)
                            .collect(Collectors.joining("<br>")));
            model.addAttribute("khoa", facultyDto);
            model.addAttribute("mode", "edit");
            return index(model);
        }
        try {
            Khoa khoa = new Khoa();
            khoa.setMaKhoa(facultyDto.getMaKhoa());
            khoa.setTenKhoa(facultyDto.getTenKhoa());
            facultyService.saveKhoa(khoa, "edit");
            redirectAttributes.addFlashAttribute("message", "Cập nhật khoa [" + khoa.getMaKhoa() + "] thành công!");
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("khoa", facultyDto);
            model.addAttribute("mode", "edit");
            return index(model);
        }
        return "redirect:/faculty";
    }

    @PostMapping(params = "btnDelete")
    public String delete(@RequestParam("maKhoa") String maKhoa, RedirectAttributes redirectAttributes) {
        try {
            facultyService.deleteKhoa(maKhoa);
            redirectAttributes.addFlashAttribute("message", "Xóa khoa [" + maKhoa + "] thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa khoa: " + e.getMessage());
        }
        return "redirect:/faculty";
    }

    @GetMapping(params = "lnkEdit")
    public String edit(ModelMap model, @RequestParam("maKhoa") String maKhoa) {
        Khoa khoa = facultyService.getKhoaById(maKhoa);
        model.addAttribute("khoa", khoa);
        model.addAttribute("mode", "edit");
        return index(model);
    }

    private void populateCanDelete(List<Khoa> list) {
        if (list.isEmpty()) {
            return;
        }
        List<String> khoaWithLop = facultyService.listTrimmedKhoaFromLop();
        List<String> khoaWithGV = facultyService.listTrimmedKhoaFromGiangVien();
        List<String> khoaWithLTC = facultyService.listTrimmedKhoaFromLtc();

        java.util.Set<String> dependentIds = new java.util.HashSet<>();
        for (String id : khoaWithLop) {
            if (id != null) {
                dependentIds.add(id.trim().toUpperCase());
            }
        }
        for (String id : khoaWithGV) {
            if (id != null) {
                dependentIds.add(id.trim().toUpperCase());
            }
        }
        for (String id : khoaWithLTC) {
            if (id != null) {
                dependentIds.add(id.trim().toUpperCase());
            }
        }

        for (Khoa k : list) {
            String trimmed = k.getMaKhoa() != null ? k.getMaKhoa().trim().toUpperCase() : "";
            k.setCanDelete(!dependentIds.contains(trimmed));
        }
    }
}
