package com.ptithcm.modules.creditclass;

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
import com.ptithcm.entities.LopTinChi;
import com.ptithcm.entities.MonHoc;
import com.ptithcm.modules.creditclass.dtos.CreditClassDTO;
import com.ptithcm.shared.constants.SessionConstant;
import com.ptithcm.shared.enums.RoleEnum;

@Controller
@RequestMapping("/credit-class")
public class CreditClassController {

    @Autowired
    private CreditClassService creditClassService;

    @GetMapping
    public String index(ModelMap model, @RequestParam(value = "maKhoa", required = false) String maKhoa,
            HttpSession httpSession) {
        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        String sessionMaKhoa = (String) httpSession.getAttribute(SessionConstant.MA_KHOA);

        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
        }

        List<LopTinChi> ltcList;
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            ltcList = creditClassService.listLtcByKhoa(sessionMaKhoa);
        } else if (maKhoa == null || maKhoa.isEmpty() || "all".equals(maKhoa)) {
            ltcList = creditClassService.listAllLtc();
        } else {
            ltcList = creditClassService.listLtcByKhoa(maKhoa);
        }

        populateCanDelete(ltcList);

        List<Khoa> khoaList = creditClassService.listKhoa();
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            khoaList = khoaList.stream().filter(k -> k.getMaKhoa().equals(sessionMaKhoa)).collect(Collectors.toList());
        }

        List<MonHoc> monHocList = creditClassService.listMonHoc();
        List<GiangVien> giangVienList;
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            giangVienList = creditClassService.listGiangVienByKhoa(sessionMaKhoa);
        } else if (maKhoa == null || maKhoa.isEmpty() || "all".equals(maKhoa)) {
            giangVienList = creditClassService.listAllGiangVien();
        } else {
            giangVienList = creditClassService.listGiangVienByKhoa(maKhoa);
        }

        model.addAttribute("ltcList", ltcList);
        model.addAttribute("khoaList", khoaList);
        model.addAttribute("monHocList", monHocList);
        model.addAttribute("giangVienList", giangVienList);
        model.addAttribute("maKhoa", maKhoa);

        return "credit-class/index";
    }

    @GetMapping(params = "lnkEdit")
    public String edit(ModelMap model, @RequestParam("maLTC") String maLTC,
            @RequestParam(value = "maKhoa", required = false) String maKhoa, HttpSession httpSession) {
        LopTinChi ltc = creditClassService.getLtcById(maLTC);
        model.addAttribute("ltc", ltc);
        model.addAttribute("mode", "edit");
        return index(model, maKhoa, httpSession);
    }

    @PostMapping(params = "btnInsert")
    public String insert(ModelMap model, @Valid @ModelAttribute("ltc") CreditClassDTO creditClassDto,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "Lỗi nhập liệu: " + bindingResult.getFieldErrors().stream()
                            .map(org.springframework.validation.FieldError::getDefaultMessage)
                            .collect(Collectors.joining("<br>")));
            redirectAttributes.addFlashAttribute("ltc", creditClassDto);
            redirectAttributes.addFlashAttribute("mode", "add");
            return "redirect:/credit-class?maKhoa=" + creditClassDto.getMaKhoa() + "&lnkAdd=true";
        }
        LopTinChi ltc = new LopTinChi();
        try {
            ltc.setMaLTC(creditClassDto.getMaLTC());
            ltc.setNienKhoa(creditClassDto.getNienKhoa());
            ltc.setHocKy(creditClassDto.getHocKy());
            ltc.setMaMH(creditClassDto.getMaMH());
            ltc.setNhom(creditClassDto.getNhom());
            ltc.setMaGV(creditClassDto.getMaGV());
            ltc.setMaKhoa(creditClassDto.getMaKhoa());
            ltc.setSoSVToiThieu(creditClassDto.getSoSVToiThieu());
            ltc.setSoSVToiDa(creditClassDto.getSoSVToiDa());
            ltc.setHuyLop(creditClassDto.isHuyLop());
            creditClassService.saveLtc(ltc, "add");
            redirectAttributes.addFlashAttribute("message", "Đã mở lớp tín chỉ mới thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            redirectAttributes.addFlashAttribute("ltc", creditClassDto);
            redirectAttributes.addFlashAttribute("mode", "add");
            return "redirect:/credit-class?maKhoa=" + creditClassDto.getMaKhoa() + "&lnkAdd=true";
        }
        return "redirect:/credit-class?maKhoa=" + creditClassDto.getMaKhoa();
    }

    @PostMapping(params = "btnUpdate")
    public String update(ModelMap model, @Valid @ModelAttribute("ltc") CreditClassDTO creditClassDto,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "Lỗi nhập liệu: " + bindingResult.getFieldErrors().stream()
                            .map(org.springframework.validation.FieldError::getDefaultMessage)
                            .collect(Collectors.joining("<br>")));
            redirectAttributes.addFlashAttribute("ltc", creditClassDto);
            redirectAttributes.addFlashAttribute("mode", "edit");
            return "redirect:/credit-class?maKhoa=" + creditClassDto.getMaKhoa() + "&maLTC=" + creditClassDto.getMaLTC()
                    + "&lnkEdit";
        }
        LopTinChi ltc = new LopTinChi();
        try {
            ltc.setMaLTC(creditClassDto.getMaLTC());
            ltc.setNienKhoa(creditClassDto.getNienKhoa());
            ltc.setHocKy(creditClassDto.getHocKy());
            ltc.setMaMH(creditClassDto.getMaMH());
            ltc.setNhom(creditClassDto.getNhom());
            ltc.setMaGV(creditClassDto.getMaGV());
            ltc.setMaKhoa(creditClassDto.getMaKhoa());
            ltc.setSoSVToiThieu(creditClassDto.getSoSVToiThieu());
            ltc.setSoSVToiDa(creditClassDto.getSoSVToiDa());
            ltc.setHuyLop(creditClassDto.isHuyLop());
            creditClassService.saveLtc(ltc, "edit");
            redirectAttributes.addFlashAttribute("message",
                    "Đã cập nhật lớp tín chỉ [" + ltc.getMaLTC() + "] thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            redirectAttributes.addFlashAttribute("ltc", creditClassDto);
            redirectAttributes.addFlashAttribute("mode", "edit");
            return "redirect:/credit-class?maKhoa=" + creditClassDto.getMaKhoa() + "&maLTC=" + creditClassDto.getMaLTC()
                    + "&lnkEdit";
        }
        return "redirect:/credit-class?maKhoa=" + creditClassDto.getMaKhoa();
    }

    @PostMapping(params = "btnDelete")
    public String delete(@RequestParam("maLTC") String maLTC, @RequestParam("maKhoa") String maKhoa,
            RedirectAttributes redirectAttributes) {
        try {
            creditClassService.deleteLtc(maLTC);
            redirectAttributes.addFlashAttribute("message", "Đã xóa lớp tín chỉ [" + maLTC + "] thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/credit-class?maKhoa=" + maKhoa;
    }

    private void populateCanDelete(List<LopTinChi> list) {
        if (list.isEmpty()) {
            return;
        }
        List<String> ltcWithReg = creditClassService.listLtcIdsWithRegistrations();
        java.util.Set<String> dependentIds = new java.util.HashSet<>(ltcWithReg);

        for (LopTinChi ltc : list) {
            ltc.setCanDelete(!dependentIds.contains(ltc.getMaLTC()));
        }
    }
}
