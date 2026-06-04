package com.ptithcm.modules.admin.classroom;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;
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
import com.ptithcm.entities.Lop;
import com.ptithcm.modules.classroom.ClassroomService;
import com.ptithcm.modules.classroom.dtos.ClassroomDTO;
import com.ptithcm.shared.constants.SessionConstant;
import com.ptithcm.shared.enums.RoleEnum;

@Controller
@RequestMapping("/admin/classroom")
public class AdminClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @ModelAttribute("lop")
    public ClassroomDTO getLopDefault(@RequestParam(value = "maKhoa", required = false) String maKhoa) {
        ClassroomDTO lop = new ClassroomDTO();
        if (maKhoa != null && !maKhoa.equals("all")) {
            lop.setMaKhoa(maKhoa);
        }
        lop.setKhoaHoc("2025-2026");
        return lop;
    }

    @GetMapping
    public String index(ModelMap model, @RequestParam(value = "maKhoa", required = false) String maKhoa,
            HttpSession httpSession) {

        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        String sessionMaKhoa = (String) httpSession.getAttribute(SessionConstant.MA_KHOA);

        List<Khoa> khoaList = classroomService.listKhoa();

        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
            khoaList = khoaList.stream().filter(k -> k.getMaKhoa().equals(sessionMaKhoa)).collect(Collectors.toList());
        }

        List<Lop> lopList;
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            lopList = classroomService.listLopByKhoa(sessionMaKhoa);
            maKhoa = sessionMaKhoa;
        } else if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all")) {
            lopList = classroomService.listLopByKhoa(maKhoa);
        } else {
            lopList = classroomService.listAllLop();
        }

        populateCanDelete(lopList);
        model.addAttribute("lopList", lopList);
        model.addAttribute("khoaList", khoaList);
        model.addAttribute("maKhoa", maKhoa);
        return "admin/classroom/index";
    }

    private void populateCanDelete(List<Lop> list) {
        if (list.isEmpty()) {
            return;
        }

        List<String> lopWithSV = classroomService.listTrimmedLopFromStudents();
        List<String> lopWithReg = classroomService.listTrimmedLopFromRegistrations();

        java.util.Set<String> dependentIds = new java.util.HashSet<>();
        for (String id : lopWithSV) {
            if (id != null) {
                dependentIds.add(id.trim().toUpperCase());
            }
        }
        for (String id : lopWithReg) {
            if (id != null) {
                dependentIds.add(id.trim().toUpperCase());
            }
        }

        for (Lop lop : list) {
            String trimmed = lop.getMaLop() != null ? lop.getMaLop().trim().toUpperCase() : "";
            lop.setCanDelete(!dependentIds.contains(trimmed));
        }
    }

    @PostMapping(params = "btnInsert")
    public String insert(ModelMap model, @Valid @ModelAttribute("lop") ClassroomDTO classroomDto,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        String khoaHoc = classroomDto.getKhoaHoc();
        if (khoaHoc != null && khoaHoc.matches("^\\d{4}-\\d{4}$")) {
            String[] years = khoaHoc.split("-");
            int startYear = Integer.parseInt(years[0]);
            int endYear = Integer.parseInt(years[1]);
            if (endYear != startYear + 1) {
                bindingResult.rejectValue("khoaHoc", "error.khoaHoc",
                        "Khóa học phải gồm 2 năm liên tiếp (Ví dụ: 2025-2026)!");
            }
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("error",
                    "Lỗi nhập liệu lớp học: " + bindingResult.getFieldErrors().stream()
                            .map(org.springframework.validation.FieldError::getDefaultMessage)
                            .collect(Collectors.joining("<br>")));
            model.addAttribute("lop", classroomDto);
            model.addAttribute("mode", "add");
            return index(model, classroomDto.getMaKhoa(), httpSession);
        }
        try {
            Lop lop = new Lop();
            lop.setMaLop(classroomDto.getMaLop());
            lop.setTenLop(classroomDto.getTenLop());
            lop.setMaKhoa(classroomDto.getMaKhoa());
            lop.setKhoaHoc(classroomDto.getKhoaHoc());
            classroomService.saveClass(lop, "add");
            redirectAttributes.addFlashAttribute("message", "Thêm lớp [" + lop.getMaLop() + "] thành công!");
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("lop", classroomDto);
            model.addAttribute("mode", "add");
            return index(model, classroomDto.getMaKhoa(), httpSession);
        }
        return "redirect:/admin/classroom?maKhoa=" + classroomDto.getMaKhoa();
    }

    @PostMapping(params = "btnUpdate")
    public String update(ModelMap model, @Valid @ModelAttribute("lop") ClassroomDTO classroomDto,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        String khoaHoc = classroomDto.getKhoaHoc();
        if (khoaHoc != null && khoaHoc.matches("^\\d{4}-\\d{4}$")) {
            String[] years = khoaHoc.split("-");
            int startYear = Integer.parseInt(years[0]);
            int endYear = Integer.parseInt(years[1]);
            if (endYear != startYear + 1) {
                bindingResult.rejectValue("khoaHoc", "error.khoaHoc",
                        "Khóa học phải gồm 2 năm liên tiếp (Ví dụ: 2025-2026)!");
            }
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("error",
                    "Lỗi nhập liệu lớp học: " + bindingResult.getFieldErrors().stream()
                            .map(org.springframework.validation.FieldError::getDefaultMessage)
                            .collect(Collectors.joining("<br>")));
            model.addAttribute("lop", classroomDto);
            model.addAttribute("mode", "edit");
            return index(model, classroomDto.getMaKhoa(), httpSession);
        }
        try {
            Lop lop = new Lop();
            lop.setMaLop(classroomDto.getMaLop());
            lop.setTenLop(classroomDto.getTenLop());
            lop.setMaKhoa(classroomDto.getMaKhoa());
            lop.setKhoaHoc(classroomDto.getKhoaHoc());
            classroomService.saveClass(lop, "edit");
            redirectAttributes.addFlashAttribute("message", "Cập nhật lớp [" + lop.getMaLop() + "] thành công!");
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("lop", classroomDto);
            model.addAttribute("mode", "edit");
            return index(model, classroomDto.getMaKhoa(), httpSession);
        }
        return "redirect:/admin/classroom?maKhoa=" + classroomDto.getMaKhoa();
    }

    @PostMapping(params = "btnDelete")
    public String delete(@RequestParam("maLop") String maLop, @RequestParam("maKhoa") String maKhoa,
            RedirectAttributes redirectAttributes) {
        try {
            classroomService.deleteClass(maLop);
            redirectAttributes.addFlashAttribute("message", "Xóa lớp [" + maLop + "] thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa lớp: " + e.getMessage());
        }
        return "redirect:/admin/classroom?maKhoa=" + maKhoa;
    }

    @GetMapping(params = "lnkEdit")
    public String edit(ModelMap model, @RequestParam("maLop") String maLop, @RequestParam("maKhoa") String maKhoa,
            HttpSession httpSession) {
        Lop lop = classroomService.getLopById(maLop);
        ClassroomDTO dto = new ClassroomDTO();
        dto.setMaLop(lop.getMaLop());
        dto.setTenLop(lop.getTenLop());
        dto.setMaKhoa(lop.getMaKhoa());
        dto.setKhoaHoc(lop.getKhoaHoc());
        model.addAttribute("lop", dto);
        model.addAttribute("mode", "edit");
        return index(model, maKhoa, httpSession);
    }
}
