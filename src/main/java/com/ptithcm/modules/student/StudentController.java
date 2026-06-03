package com.ptithcm.modules.student;

import java.util.ArrayList;
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
import com.ptithcm.entities.SinhVien;
import com.ptithcm.modules.student.dtos.StudentDTO;
import com.ptithcm.shared.constants.SessionConstant;
import com.ptithcm.shared.enums.RoleEnum;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

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
    public String insert(ModelMap model, @Valid @ModelAttribute("sinhVien") StudentDTO studentDto,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error",
                    "Lỗi nhập liệu sinh viên: " + bindingResult.getFieldErrors().stream()
                            .map(org.springframework.validation.FieldError::getDefaultMessage)
                            .collect(Collectors.joining("<br>")));
            model.addAttribute("sinhVien", studentDto);
            model.addAttribute("mode", "add");
            return index(model, studentDto.getMaLop(), null, httpSession);
        }
        try {
            SinhVien sinhVien = new SinhVien();
            sinhVien.setMaSV(studentDto.getMaSV());
            sinhVien.setHo(studentDto.getHo());
            sinhVien.setTen(studentDto.getTen());
            sinhVien.setPhai(studentDto.getPhai());
            sinhVien.setDiaChi(studentDto.getDiaChi());
            sinhVien.setNgaySinh(studentDto.getNgaySinh());
            sinhVien.setMaLop(studentDto.getMaLop());
            sinhVien.setDaNghiHoc(studentDto.isDaNghiHoc());
            studentService.insertStudent(sinhVien);
            redirectAttributes.addFlashAttribute("message", "Thêm sinh viên [" + sinhVien.getMaSV() + "] thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm sinh viên: " + e.getMessage());
        }
        return "redirect:/student?maLop=" + studentDto.getMaLop();
    }

    @PostMapping(params = "btnUpdate")
    public String update(ModelMap model, @Valid @ModelAttribute("sinhVien") StudentDTO studentDto,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error",
                    "Lỗi nhập liệu sinh viên: " + bindingResult.getFieldErrors().stream()
                            .map(org.springframework.validation.FieldError::getDefaultMessage)
                            .collect(Collectors.joining("<br>")));
            model.addAttribute("sinhVien", studentDto);
            model.addAttribute("mode", "edit");
            return index(model, studentDto.getMaLop(), null, httpSession);
        }
        try {
            SinhVien sinhVien = new SinhVien();
            sinhVien.setMaSV(studentDto.getMaSV());
            sinhVien.setHo(studentDto.getHo());
            sinhVien.setTen(studentDto.getTen());
            sinhVien.setPhai(studentDto.getPhai());
            sinhVien.setDiaChi(studentDto.getDiaChi());
            sinhVien.setNgaySinh(studentDto.getNgaySinh());
            sinhVien.setMaLop(studentDto.getMaLop());
            sinhVien.setDaNghiHoc(studentDto.isDaNghiHoc());
            sinhVien.setVersion(studentDto.getVersion());
            studentService.updateStudent(sinhVien);
            redirectAttributes.addFlashAttribute("message",
                    "Cập nhật sinh viên [" + sinhVien.getMaSV() + "] thành công!");
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
                redirectAttributes.addFlashAttribute("error",
                        "Dữ liệu sinh viên đã bị chỉnh sửa bởi một quản trị viên khác. Vui lòng tải lại trang và thực hiện lại!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật sinh viên: " + e.getMessage());
            }
        }
        return "redirect:/student?maLop=" + studentDto.getMaLop();
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
