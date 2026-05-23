package com.ptithcm.modules.giangvien;

import com.ptithcm.entity.GiangVien;
import com.ptithcm.entity.Khoa;
import com.ptithcm.shared.constant.SessionConstant;
import com.ptithcm.shared.enumtype.RoleEnum;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/lecturer")
public class GiangVienController {

    @Autowired
    private GiangVienService giangVienService;

    @RequestMapping()
    public String index(ModelMap model, @RequestParam(value = "maKhoa", required = false) String maKhoa,
            HttpSession httpSession) {

        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        String sessionMaKhoa = (String) httpSession.getAttribute(SessionConstant.MA_KHOA);

        List<Khoa> khoaList = giangVienService.listKhoa();

        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
            khoaList = khoaList.stream().filter(k -> k.getMaKhoa().equals(sessionMaKhoa)).collect(Collectors.toList());
        }

        List<GiangVien> gvList;
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            gvList = giangVienService.listGiangVienByKhoa(sessionMaKhoa);
            maKhoa = sessionMaKhoa;
        } else if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all")) {
            gvList = giangVienService.listGiangVienByKhoa(maKhoa);
        } else {
            gvList = giangVienService.listAllGiangVien();
        }

        populateCanDelete(gvList);

        model.addAttribute("khoaList", khoaList);
        model.addAttribute("gvList", gvList);
        model.addAttribute("maKhoa", maKhoa);
        return "lecturer/index";
    }

    private void populateCanDelete(List<GiangVien> list) {
        if (list.isEmpty())
            return;
        List<String> ltcMaGV = giangVienService.listLtcMaGV();
        List<String> userMaGV = giangVienService.listUserMaGV();

        java.util.Set<String> dependentIds = new java.util.HashSet<>();
        for (String id : ltcMaGV)
            if (id != null)
                dependentIds.add(id.trim());
        for (String id : userMaGV)
            if (id != null)
                dependentIds.add(id.trim());

        for (GiangVien gv : list) {
            String trimmed = gv.getMaGV() != null ? gv.getMaGV().trim() : "";
            gv.setCanDelete(!dependentIds.contains(trimmed));
        }
    }

    @RequestMapping(value = "/api/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<GiangVien> listGV(@RequestParam(value = "maKhoa", required = false) String maKhoa,
            HttpSession httpSession) {

        String sessionRole = (String) httpSession.getAttribute(SessionConstant.ROLE);
        String sessionMaKhoa = (String) httpSession.getAttribute(SessionConstant.MA_KHOA);

        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            maKhoa = sessionMaKhoa;
        }

        List<GiangVien> list;
        if (RoleEnum.KHOA.getCode().equals(sessionRole) && sessionMaKhoa != null) {
            list = giangVienService.listGiangVienByKhoa(sessionMaKhoa);
        } else if (maKhoa == null || maKhoa.isEmpty() || maKhoa.equals("all")) {
            list = giangVienService.listAllGiangVien();
        } else {
            list = giangVienService.listGiangVienByKhoa(maKhoa);
        }
        populateCanDelete(list);
        return list;
    }

    @RequestMapping(value = "/api/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public GiangVien getGV(@RequestParam("maGV") String maGV) {
        GiangVien gv = giangVienService.getLecturerById(maGV);
        if (gv != null) {
            populateCanDelete(java.util.Collections.singletonList(gv));
        }
        return gv;
    }

    @RequestMapping(value = "/api/save", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> saveGV(@RequestBody GiangVien gv, @RequestParam("mode") String mode) {
        Map<String, Object> res = new HashMap<>();
        try {
            giangVienService.saveLecturer(gv, mode);
            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Lỗi: " + e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/api/delete", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> deleteGV(@RequestParam("maGV") String maGV) {
        Map<String, Object> res = new HashMap<>();
        try {
            giangVienService.deleteLecturer(maGV);
            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Lỗi: " + e.getMessage());
        }
        return res;
    }
}
