package com.ptithcm.modules.sinhvien;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ptithcm.entities.Khoa;
import com.ptithcm.entities.Lop;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.shared.constants.SessionConstant;
import com.ptithcm.shared.enums.RoleEnum;
import com.ptithcm.shared.validators.SinhVienValidator;

@Controller
@RequestMapping("/student")
public class SinhVienController {

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private SinhVienValidator sinhVienValidator;

    @RequestMapping()
    public String index(ModelMap model, @RequestParam(value = "maLop", required = false) String maLop,
            @RequestParam(value = "maKhoa", required = false) String maKhoa, HttpSession httpSession) {

        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        String sessionMaKhoa = (String) httpSession.getAttribute(SessionConstant.MA_KHOA);

        // Lấy danh sách tất cả các khoa
        List<Khoa> khoaList = sinhVienService.listKhoa();

        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
            khoaList = khoaList.stream().filter(k -> k.getMaKhoa().equals(sessionMaKhoa)).collect(Collectors.toList());
        }

        // Lấy danh sách lớp học (Lọc theo mã khoa nếu có)
        List<Lop> lopList;
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            lopList = sinhVienService.listLopByKhoa(sessionMaKhoa);
            maKhoa = sessionMaKhoa;
        } else if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all")) {
            lopList = sinhVienService.listLopByKhoa(maKhoa);
        } else {
            lopList = sinhVienService.listAllLop();
        }

        List<SinhVien> filteredList = new ArrayList<>();
        if (maLop != null && !maLop.isEmpty()) {
            filteredList = sinhVienService.listStudentsByClass(maLop);
        }

        model.addAttribute("lopList", lopList);
        model.addAttribute("khoaList", khoaList);
        model.addAttribute("sinhVienList", filteredList);
        model.addAttribute("maLop", maLop);
        model.addAttribute("maKhoa", maKhoa);
        return "student/index";
    }

    @RequestMapping(params = "btnInsert")
    public String insert(ModelMap model, SinhVien sinhVien) {
        try {
            sinhVienService.insertStudent(sinhVien);
        } catch (Exception e) {
            // Ghi log hoặc xử lý lỗi nếu cần thiết
        }
        return "redirect:/student?maLop=" + sinhVien.getMaLop() + "&showModal=true";
    }

    @RequestMapping(params = "btnUpdate")
    public String update(ModelMap model, SinhVien sinhVien) {
        try {
            sinhVienService.updateStudent(sinhVien);
        } catch (Exception e) {
            // Ghi log hoặc xử lý lỗi nếu cần thiết
        }
        return "redirect:/student?maLop=" + sinhVien.getMaLop() + "&showModal=true";
    }

    @RequestMapping(params = "btnDelete")
    public String delete(ModelMap model, @RequestParam("maSV") String maSV, @RequestParam("maLop") String maLop) {
        try {
            sinhVienService.deleteStudent(maSV);
        } catch (Exception e) {
            // Ghi log hoặc xử lý lỗi nếu cần thiết
        }
        return "redirect:/student?maLop=" + maLop;
    }

    @RequestMapping(params = "lnkEdit")
    public String edit(ModelMap model, @RequestParam("maSV") String maSV, @RequestParam("maLop") String maLop,
            HttpSession httpSession) {
        SinhVien sv = sinhVienService.getStudentById(maSV);
        model.addAttribute("sinhVien", sv);
        return index(model, maLop, null, httpSession);
    }

    @RequestMapping(params = "lnkDelete")
    public String deleteInit(ModelMap model, @RequestParam("maSV") String maSV, @RequestParam("maLop") String maLop,
            HttpSession httpSession) {
        SinhVien sv = sinhVienService.getStudentById(maSV);
        model.addAttribute("sinhVien", sv);
        return index(model, maLop, null, httpSession);
    }

    // TODO: Cần kiểm tra lại vì không được dùng API
    // --- CÁC ENDPOINT AJAX API ---

    @RequestMapping(value = "/api/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public SinhVien getStudent(@RequestParam("maSV") String maSV) {
        return sinhVienService.getStudentById(maSV);
    }

    @RequestMapping(value = "/api/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<SinhVien> listStudents(@RequestParam("maLop") String maLop) {
        return sinhVienService.listStudentsByClass(maLop);
    }

    @RequestMapping(value = "/api/save", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> saveStudent(@RequestBody SinhVien sinhVien, @RequestParam("mode") String mode) {
        Map<String, Object> res = new HashMap<>();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(sinhVien, "sinhVien");
        sinhVienValidator.validate(sinhVien, bindingResult);
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldErrors().stream()
                    .map(org.springframework.validation.FieldError::getDefaultMessage)
                    .collect(Collectors.joining("<br>"));
            res.put("status", "error");
            res.put("message", errorMsg);
            return res;
        }
        try {
            sinhVienService.saveStudentApi(sinhVien, mode);
            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Lỗi: " + e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/api/delete", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> deleteStudent(@RequestParam("maSV") String maSV) {
        Map<String, Object> res = new HashMap<>();
        try {
            sinhVienService.deleteStudent(maSV);
            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/api/classes", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Lop> listClasses(@RequestParam(value = "maKhoa", required = false) String maKhoa,
            HttpSession httpSession) {

        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        String sessionMaKhoa = (String) httpSession.getAttribute(SessionConstant.MA_KHOA);

        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
        }

        if (maKhoa == null || maKhoa.isEmpty() || maKhoa.equals("all")) {
            return sinhVienService.listAllLop();
        }
        return sinhVienService.listLopByKhoa(maKhoa);
    }
}
