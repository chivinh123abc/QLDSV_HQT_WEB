package com.ptithcm.modules.lop;

import com.ptithcm.entity.Khoa;
import com.ptithcm.entity.Lop;
import com.ptithcm.shared.constant.SessionConstant;
import com.ptithcm.shared.enumtype.RoleEnum;
import com.ptithcm.shared.validator.LopValidator;

import jakarta.servlet.http.HttpSession;
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

@Controller
@RequestMapping("/class")
public class LopController {

    @Autowired
    private LopService lopService;

    @Autowired
    private LopValidator lopValidator;

    @RequestMapping()
    public String index(ModelMap model, @RequestParam(value = "maKhoa", required = false) String maKhoa,
            HttpSession httpSession) {

        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        String sessionMaKhoa = (String) httpSession.getAttribute(SessionConstant.MA_KHOA);

        List<Khoa> khoaList = lopService.listKhoa();

        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
            khoaList = khoaList.stream().filter(k -> k.getMaKhoa().equals(sessionMaKhoa)).collect(Collectors.toList());
        }

        List<Lop> lopList;
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            lopList = lopService.listLopByKhoa(sessionMaKhoa);
            maKhoa = sessionMaKhoa;
        } else if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all")) {
            lopList = lopService.listLopByKhoa(maKhoa);
        } else {
            lopList = lopService.listAllLop();
        }

        populateCanDelete(lopList);
        model.addAttribute("lopList", lopList);
        model.addAttribute("khoaList", khoaList);
        model.addAttribute("maKhoa", maKhoa);
        return "class/index";
    }

    // TODO: Cần kiểm tra lại vì không được tạo API
    // --- CÁC ENDPOINT AJAX API ---

    @RequestMapping(value = "/api/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Lop getClass(@RequestParam("maLop") String maLop) {
        return lopService.getLopById(maLop);
    }

    @RequestMapping(value = "/api/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Lop> listClasses(@RequestParam(value = "maKhoa", required = false) String maKhoa,
            HttpSession httpSession) {

        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        String sessionMaKhoa = (String) httpSession.getAttribute(SessionConstant.MA_KHOA);

        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
        }

        List<Lop> list;
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            list = lopService.listLopByKhoa(sessionMaKhoa);
        } else if (maKhoa == null || maKhoa.isEmpty() || maKhoa.equals("all")) {
            list = lopService.listAllLop();
        } else {
            list = lopService.listLopByKhoa(maKhoa);
        }
        populateCanDelete(list);
        return list;
    }

    private void populateCanDelete(List<Lop> list) {
        if (list.isEmpty())
            return;

        // Kiểm tra xem lớp có sinh viên nào không
        List<String> lopWithSV = lopService.listTrimmedLopFromStudents();

        // Kiểm tra xem có sinh viên nào của lớp này đăng ký lớp tín chỉ không
        List<String> lopWithReg = lopService.listTrimmedLopFromRegistrations();

        java.util.Set<String> dependentIds = new java.util.HashSet<>();
        for (String id : lopWithSV)
            if (id != null)
                dependentIds.add(id.trim().toUpperCase());
        for (String id : lopWithReg)
            if (id != null)
                dependentIds.add(id.trim().toUpperCase());

        for (Lop lop : list) {
            String trimmed = lop.getMaLop() != null ? lop.getMaLop().trim().toUpperCase() : "";
            lop.setCanDelete(!dependentIds.contains(trimmed));
        }
    }

    @RequestMapping(value = "/api/save", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> saveClass(@RequestBody Lop lop, @RequestParam("mode") String mode) {
        Map<String, Object> res = new HashMap<>();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(lop, "lop");
        lopValidator.validate(lop, bindingResult);
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldErrors().stream()
                    .map(org.springframework.validation.FieldError::getDefaultMessage)
                    .collect(Collectors.joining("<br>"));
            res.put("status", "error");
            res.put("message", errorMsg);
            return res;
        }
        try {
            lopService.saveClass(lop, mode);
            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Lỗi: " + e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/api/delete", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> deleteClass(@RequestParam("maLop") String maLop) {
        Map<String, Object> res = new HashMap<>();
        try {
            lopService.deleteClass(maLop);
            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", e.getMessage());
        }
        return res;
    }
}
