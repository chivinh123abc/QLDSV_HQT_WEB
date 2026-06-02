package com.ptithcm.modules.student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptithcm.entities.Khoa;
import com.ptithcm.entities.Lop;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.shared.constants.SessionConstant;
import com.ptithcm.shared.enums.RoleEnum;
import com.ptithcm.shared.validators.StudentValidator;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentValidator sinhVienValidator;

    @GetMapping
    public String index(ModelMap model, @RequestParam(value = "maLop", required = false) String maLop,
            @RequestParam(value = "maKhoa", required = false) String maKhoa, HttpSession httpSession) {

        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        String sessionMaKhoa = (String) httpSession.getAttribute(SessionConstant.MA_KHOA);

        // Lấy danh sách tất cả các khoa
        List<Khoa> khoaList = studentService.listKhoa();

        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
            khoaList = khoaList.stream().filter(k -> k.getMaKhoa().equals(sessionMaKhoa)).collect(Collectors.toList());
        }

        // Lấy danh sách lớp học (Lọc theo mã khoa nếu có)
        List<Lop> lopList;
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            lopList = studentService.listLopByKhoa(sessionMaKhoa);
            maKhoa = sessionMaKhoa;
        } else if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all")) {
            lopList = studentService.listLopByKhoa(maKhoa);
        } else {
            lopList = studentService.listAllLop();
        }

        List<SinhVien> filteredList = new ArrayList<>();
        if (maLop != null && !maLop.isEmpty()) {
            filteredList = studentService.listStudentsByClass(maLop);
        }

        // Đánh dấu thuộc tính canDelete cho các sinh viên trong danh sách
        populateCanDelete(filteredList);

        model.addAttribute("lopList", lopList);
        model.addAttribute("khoaList", khoaList);
        model.addAttribute("sinhVienList", filteredList);
        model.addAttribute("maLop", maLop);
        model.addAttribute("maKhoa", maKhoa);
        return "student/index";
    }

    private void populateCanDelete(List<SinhVien> list) {
        if (list.isEmpty()) {
            return;
        }
        // Gọi service để xem các sv đã có đăng ký môn học chưa
        for (SinhVien sv : list) {
            try {
                Long count = studentService.countDangKyByStudent(sv.getMaSV());
                sv.setCanDelete(count == 0);
            } catch (Exception e) {
                sv.setCanDelete(false);
            }
        }
    }

    @PostMapping(params = "btnInsert")
    public String insert(ModelMap model, SinhVien sinhVien, BindingResult bindingResult,
            RedirectAttributes redirectAttributes, HttpSession httpSession) {
        sinhVienValidator.validate(sinhVien, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Lỗi nhập liệu sinh viên!");
            model.addAttribute("sinhVien", sinhVien);
            model.addAttribute("mode", "add");
            return index(model, sinhVien.getMaLop(), null, httpSession);
        }
        try {
            studentService.insertStudent(sinhVien);
            redirectAttributes.addFlashAttribute("message", "Thêm sinh viên [" + sinhVien.getMaSV() + "] thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm sinh viên: " + e.getMessage());
        }
        return "redirect:/student?maLop=" + sinhVien.getMaLop();
    }

    @PostMapping(params = "btnUpdate")
    public String update(ModelMap model, SinhVien sinhVien, BindingResult bindingResult,
            RedirectAttributes redirectAttributes, HttpSession httpSession) {
        sinhVienValidator.validate(sinhVien, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Lỗi nhập liệu sinh viên!");
            model.addAttribute("sinhVien", sinhVien);
            model.addAttribute("mode", "edit");
            return index(model, sinhVien.getMaLop(), null, httpSession);
        }
        try {
            studentService.updateStudent(sinhVien);
            redirectAttributes.addFlashAttribute("message",
                    "Cập nhật sinh viên [" + sinhVien.getMaSV() + "] thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật sinh viên: " + e.getMessage());
        }
        return "redirect:/student?maLop=" + sinhVien.getMaLop();
    }

    @PostMapping(params = "btnDelete")
    public String delete(@RequestParam("maSV") String maSV, @RequestParam("maLop") String maLop,
            RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(maSV);
            redirectAttributes.addFlashAttribute("message", "Xóa sinh viên [" + maSV + "] thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa sinh viên: " + e.getMessage());
        }
        return "redirect:/student?maLop=" + maLop;
    }

    @GetMapping(params = "lnkEdit")
    public String edit(ModelMap model, @RequestParam("maSV") String maSV, @RequestParam("maLop") String maLop,
            HttpSession httpSession) {
        SinhVien sv = studentService.getStudentById(maSV);
        model.addAttribute("sinhVien", sv);
        model.addAttribute("mode", "edit");
        return index(model, maLop, null, httpSession);
    }

    @GetMapping(params = "lnkDelete")
    public String deleteInit(ModelMap model, @RequestParam("maSV") String maSV, @RequestParam("maLop") String maLop,
            HttpSession httpSession) {
        SinhVien sv = studentService.getStudentById(maSV);
        model.addAttribute("sinhVien", sv);
        model.addAttribute("mode", "delete");
        return index(model, maLop, null, httpSession);
    }
}
