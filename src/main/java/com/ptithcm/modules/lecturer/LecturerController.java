package com.ptithcm.modules.lecturer;

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

import com.ptithcm.entities.GiangVien;
import com.ptithcm.entities.Khoa;
import com.ptithcm.modules.lecturer.dtos.LecturerDTO;
import com.ptithcm.shared.constants.SessionConstant;
import com.ptithcm.shared.enums.RoleEnum;

@Controller
@RequestMapping("/lecturer")
public class LecturerController {

    @Autowired
    private LecturerService lecturerService;

    @ModelAttribute("giangVien")
    public LecturerDTO getGiangVienDefault(@RequestParam(value = "maKhoa", required = false) String maKhoa) {
        LecturerDTO dto = new LecturerDTO();
        if (maKhoa != null && !maKhoa.equals("all")) {
            dto.setMaKhoa(maKhoa);
        }
        return dto;
    }

    @GetMapping
    public String index(ModelMap model, @RequestParam(value = "maKhoa", required = false) String maKhoa,
            HttpSession httpSession) {

        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        String sessionMaKhoa = (String) httpSession.getAttribute(SessionConstant.MA_KHOA);

        List<Khoa> khoaList = lecturerService.listKhoa();

        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
            khoaList = khoaList.stream().filter(k -> k.getMaKhoa().equals(sessionMaKhoa)).collect(Collectors.toList());
        }

        List<GiangVien> gvList;
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            gvList = lecturerService.listGiangVienByKhoa(sessionMaKhoa);
            maKhoa = sessionMaKhoa;
        } else if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all")) {
            gvList = lecturerService.listGiangVienByKhoa(maKhoa);
        } else {
            gvList = lecturerService.listAllGiangVien();
        }

        populateCanDelete(gvList);

        model.addAttribute("khoaList", khoaList);
        model.addAttribute("gvList", gvList);
        model.addAttribute("maKhoa", maKhoa);
        return "lecturer/index";
    }

    private void populateCanDelete(List<GiangVien> list) {
        if (list.isEmpty()) {
            return;
        }
        List<String> ltcMaGV = lecturerService.listLtcMaGV();
        List<String> userMaGV = lecturerService.listUserMaGV();

        java.util.Set<String> dependentIds = new java.util.HashSet<>();
        for (String id : ltcMaGV) {
            if (id != null) {
                dependentIds.add(id.trim());
            }
        }
        for (String id : userMaGV) {
            if (id != null) {
                dependentIds.add(id.trim());
            }
        }

        for (GiangVien gv : list) {
            String trimmed = gv.getMaGV() != null ? gv.getMaGV().trim() : "";
            gv.setCanDelete(!dependentIds.contains(trimmed));
        }
    }

    @PostMapping(params = "btnInsert")
    public String insert(ModelMap model, @Valid @ModelAttribute("giangVien") LecturerDTO lecturerDto,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error",
                    "Lỗi nhập liệu giảng viên: " + bindingResult.getFieldErrors().stream()
                            .map(org.springframework.validation.FieldError::getDefaultMessage)
                            .collect(Collectors.joining("<br>")));
            model.addAttribute("giangVien", lecturerDto);
            model.addAttribute("mode", "add");
            return index(model, lecturerDto.getMaKhoa(), httpSession);
        }
        try {
            GiangVien gv = new GiangVien();
            gv.setMaGV(lecturerDto.getMaGV());
            gv.setHo(lecturerDto.getHo());
            gv.setTen(lecturerDto.getTen());
            gv.setMaKhoa(lecturerDto.getMaKhoa());
            gv.setHocVi(lecturerDto.getHocVi());
            gv.setHocHam(lecturerDto.getHocHam());
            gv.setChuyenMon(lecturerDto.getChuyenMon());
            lecturerService.saveLecturer(gv, "add");
            redirectAttributes.addFlashAttribute("message", "Thêm giảng viên [" + gv.getMaGV() + "] thành công!");
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            model.addAttribute("giangVien", lecturerDto);
            model.addAttribute("mode", "add");
            return index(model, lecturerDto.getMaKhoa(), httpSession);
        }
        return "redirect:/lecturer?maKhoa=" + lecturerDto.getMaKhoa();
    }

    @PostMapping(params = "btnUpdate")
    public String update(ModelMap model, @Valid @ModelAttribute("giangVien") LecturerDTO lecturerDto,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error",
                    "Lỗi nhập liệu giảng viên: " + bindingResult.getFieldErrors().stream()
                            .map(org.springframework.validation.FieldError::getDefaultMessage)
                            .collect(Collectors.joining("<br>")));
            model.addAttribute("giangVien", lecturerDto);
            model.addAttribute("mode", "edit");
            return index(model, lecturerDto.getMaKhoa(), httpSession);
        }
        try {
            GiangVien gv = new GiangVien();
            gv.setMaGV(lecturerDto.getMaGV());
            gv.setHo(lecturerDto.getHo());
            gv.setTen(lecturerDto.getTen());
            gv.setMaKhoa(lecturerDto.getMaKhoa());
            gv.setHocVi(lecturerDto.getHocVi());
            gv.setHocHam(lecturerDto.getHocHam());
            gv.setChuyenMon(lecturerDto.getChuyenMon());
            gv.setVersion(lecturerDto.getVersion());
            lecturerService.saveLecturer(gv, "edit");
            redirectAttributes.addFlashAttribute("message", "Cập nhật giảng viên [" + gv.getMaGV() + "] thành công!");
        } catch (Exception e) {
            Throwable t = e;
            boolean isOptimisticLock = false;
            while (t != null) {
                if (t instanceof jakarta.persistence.OptimisticLockException
                        || t.getClass().getName().contains("StaleObjectStateException")
                        || t.getClass().getName().contains("ObjectOptimisticLockingFailureException")) {
                    isOptimisticLock = true;
                    break;
                }
                t = t.getCause();
            }
            if (isOptimisticLock) {
                model.addAttribute("error",
                        "Dữ liệu giảng viên đã bị chỉnh sửa bởi một quản trị viên khác. Vui lòng tải lại trang và thực hiện lại!");
            } else {
                model.addAttribute("error", "Lỗi: " + e.getMessage());
            }
            model.addAttribute("giangVien", lecturerDto);
            model.addAttribute("mode", "edit");
            return index(model, lecturerDto.getMaKhoa(), httpSession);
        }
        return "redirect:/lecturer?maKhoa=" + lecturerDto.getMaKhoa();
    }

    @PostMapping(params = "btnDelete")
    public String delete(@RequestParam("maGV") String maGV, @RequestParam("maKhoa") String maKhoa,
            RedirectAttributes redirectAttributes) {
        try {
            lecturerService.deleteLecturer(maGV);
            redirectAttributes.addFlashAttribute("message", "Xóa giảng viên [" + maGV + "] thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa giảng viên: " + e.getMessage());
        }
        return "redirect:/lecturer?maKhoa=" + maKhoa;
    }

    @GetMapping(params = "lnkEdit")
    public String edit(ModelMap model, @RequestParam("maGV") String maGV, @RequestParam("maKhoa") String maKhoa,
            HttpSession httpSession) {
        GiangVien gv = lecturerService.getLecturerById(maGV);
        // Chuyển entity GiangVien sang LecturerDTO để Spring Form Taglib binding
        LecturerDTO dto = new LecturerDTO();
        dto.setMaGV(gv.getMaGV());
        dto.setHo(gv.getHo());
        dto.setTen(gv.getTen());
        dto.setMaKhoa(gv.getMaKhoa());
        dto.setHocVi(gv.getHocVi());
        dto.setHocHam(gv.getHocHam());
        dto.setChuyenMon(gv.getChuyenMon());
        dto.setVersion(gv.getVersion());
        model.addAttribute("giangVien", dto);
        model.addAttribute("mode", "edit");
        return index(model, maKhoa, httpSession);
    }
}
