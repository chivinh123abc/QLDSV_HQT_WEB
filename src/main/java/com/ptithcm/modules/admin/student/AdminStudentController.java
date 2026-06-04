package com.ptithcm.modules.admin.student;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptithcm.entities.Khoa;
import com.ptithcm.entities.Lop;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.entities.TaiKhoan;
import com.ptithcm.modules.account.AccountDAO;
import com.ptithcm.modules.student.StudentService;
import com.ptithcm.modules.student.dtos.StudentDTO;
import com.ptithcm.shared.constants.SessionConstant;
import com.ptithcm.shared.enums.RoleEnum;
import com.ptithcm.shared.enums.TrangThaiTaiKhoan;

@Controller
@RequestMapping("/admin/student")
public class AdminStudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AccountDAO accountDAO;

    @ModelAttribute("sinhVien")
    public StudentDTO getSinhVienDefault(@RequestParam(value = "maLop", required = false) String maLop) {
        StudentDTO dto = new StudentDTO();
        if (maLop != null) {
            dto.setMaLop(maLop);
        }
        return dto;
    }

    @GetMapping
    public String index(ModelMap model, @RequestParam(value = "maLop", required = false) String maLop,
            @RequestParam(value = "maKhoa", required = false) String maKhoa, HttpSession httpSession) {

        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        String sessionMaKhoa = (String) httpSession.getAttribute(SessionConstant.MA_KHOA);

        // Security Guard: Ensure only PGV or KHOA roles can access
        if (!RoleEnum.PGV.getCode().equals(sessionRole) && !RoleEnum.KHOA.getCode().equals(sessionRole)) {
            return "redirect:/";
        }

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
        return "admin/student/index";
    }

    private void populateCanDelete(List<SinhVien> list) {
        if (list.isEmpty()) {
            return;
        }
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

        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        if (!RoleEnum.PGV.getCode().equals(sessionRole)) {
            redirectAttributes.addFlashAttribute("error", "Chỉ Giáo vụ (PGV) mới có quyền thêm sinh viên!");
            return "redirect:/admin/student?maLop=" + studentDto.getMaLop();
        }

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
        return "redirect:/admin/student?maLop=" + studentDto.getMaLop();
    }

    @PostMapping(params = "btnUpdate")
    public String update(ModelMap model, @Valid @ModelAttribute("sinhVien") StudentDTO studentDto,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession httpSession) {

        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        if (!RoleEnum.PGV.getCode().equals(sessionRole)) {
            redirectAttributes.addFlashAttribute("error", "Chỉ Giáo vụ (PGV) mới có quyền cập nhật sinh viên!");
            return "redirect:/admin/student?maLop=" + studentDto.getMaLop();
        }

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
        return "redirect:/admin/student?maLop=" + studentDto.getMaLop();
    }

    @PostMapping(params = "btnDelete")
    public String delete(@RequestParam("maSV") String maSV, @RequestParam("maLop") String maLop,
            RedirectAttributes redirectAttributes, HttpSession httpSession) {

        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        if (!RoleEnum.PGV.getCode().equals(sessionRole)) {
            redirectAttributes.addFlashAttribute("error", "Chỉ Giáo vụ (PGV) mới có quyền xóa sinh viên!");
            return "redirect:/admin/student?maLop=" + maLop;
        }

        try {
            studentService.deleteStudent(maSV);
            redirectAttributes.addFlashAttribute("message", "Xóa sinh viên [" + maSV + "] thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa sinh viên: " + e.getMessage());
        }
        return "redirect:/admin/student?maLop=" + maLop;
    }

    @GetMapping(params = "lnkEdit")
    public String edit(ModelMap model, @RequestParam("maSV") String maSV, @RequestParam("maLop") String maLop,
            HttpSession httpSession) {
        SinhVien sv = studentService.getStudentById(maSV);
        StudentDTO dto = new StudentDTO();
        dto.setMaSV(sv.getMaSV());
        dto.setHo(sv.getHo());
        dto.setTen(sv.getTen());
        dto.setPhai(sv.getPhai());
        dto.setDiaChi(sv.getDiaChi());
        dto.setNgaySinh(sv.getNgaySinh());
        dto.setMaLop(sv.getMaLop());
        dto.setDaNghiHoc(sv.isDaNghiHoc());
        dto.setVersion(sv.getVersion());
        model.addAttribute("sinhVien", dto);
        model.addAttribute("mode", "edit");
        return index(model, maLop, null, httpSession);
    }

    @GetMapping(params = "lnkDelete")
    public String deleteInit(ModelMap model, @RequestParam("maSV") String maSV, @RequestParam("maLop") String maLop,
            HttpSession httpSession) {
        SinhVien sv = studentService.getStudentById(maSV);
        StudentDTO dto = new StudentDTO();
        dto.setMaSV(sv.getMaSV());
        dto.setHo(sv.getHo());
        dto.setTen(sv.getTen());
        dto.setPhai(sv.getPhai());
        dto.setDiaChi(sv.getDiaChi());
        dto.setNgaySinh(sv.getNgaySinh());
        dto.setMaLop(sv.getMaLop());
        dto.setDaNghiHoc(sv.isDaNghiHoc());
        dto.setVersion(sv.getVersion());
        model.addAttribute("sinhVien", dto);
        model.addAttribute("mode", "delete");
        return index(model, maLop, null, httpSession);
    }

    @PostMapping("/import")
    public String importCsv(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
            HttpSession httpSession) {
        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        if (!RoleEnum.PGV.getCode().equals(sessionRole)) {
            redirectAttributes.addFlashAttribute("error", "Chỉ Giáo vụ (PGV) mới có quyền nhập CSV sinh viên!");
            return "redirect:/admin/student";
        }

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn một file CSV để nhập!");
            return "redirect:/admin/student";
        }
        try {
            List<String> errorLines = studentService.importStudentsFromCsv(file.getInputStream());
            if (errorLines.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Nhập danh sách sinh viên từ CSV thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorLines", errorLines);
                redirectAttributes.addFlashAttribute("message", "Nhập CSV hoàn tất với một số lỗi.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi xử lý file CSV: " + e.getMessage());
        }
        return "redirect:/admin/student";
    }

    @GetMapping("/export-credentials")
    public void exportCredentialsToCsv(HttpServletResponse response, HttpSession httpSession) {
        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        if (!RoleEnum.PGV.getCode().equals(sessionRole) && !RoleEnum.KHOA.getCode().equals(sessionRole)) {
            try {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            } catch (Exception ignored) {
            }
            return;
        }

        try {
            // 1. Set Encoding BẮT BUỘC phải nằm TRƯỚC khi gọi getWriter()
            response.setContentType("text/csv");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"DanhSach_TaiKhoan_SinhVien.csv\"");

            // 2. Lấy luồng ghi Text chuẩn của Spring/Tomcat
            PrintWriter writer = response.getWriter();

            // 3. Ghi ký tự BOM (\ufeff) ở vị trí đầu tiên tuyệt đối của file
            writer.write('\ufeff');

            // 4. Ghi tiêu đề Header
            writer.println("Mã SV,Họ Tên,Email Đăng Nhập,Phân Quyền,Trạng Thái");

            // 5. Lọc danh sách (Chỉ sinh viên CÓ tài khoản)
            List<SinhVien> danhSach = studentService.getStudentsWithAccount();
            List<TaiKhoan> accounts = accountDAO.getAllAccounts();
            Map<String, TaiKhoan> accountMap = accounts.stream()
                    .collect(Collectors.toMap(TaiKhoan::getTenDangNhap, a -> a, (a1, a2) -> a1));

            // 6. Ghi dữ liệu
            for (SinhVien sv : danhSach) {
                TaiKhoan tk = accountMap.get(sv.getMaSV());
                if (tk != null) {
                    // Biến đổi trạng thái số thành text để Giáo vụ dễ đọc
                    String txtTrangThai = "Chưa kích hoạt";
                    if (tk.getTrangThai() == TrangThaiTaiKhoan.DA_KICH_HOAT) {
                        txtTrangThai = "Đang hoạt động";
                    } else if (tk.getTrangThai() == TrangThaiTaiKhoan.KHOA) {
                        txtTrangThai = "Bị khóa";
                    }

                    // Ghi từng dòng (bọc ngoặc kép để chống lỗi nếu tên có dấu phẩy)
                    writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n", sv.getMaSV(), sv.getHo() + " " + sv.getTen(),
                            tk.getEmail(), "SINH_VIEN", txtTrangThai);
                }
            }

            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
