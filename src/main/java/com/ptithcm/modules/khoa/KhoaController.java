package com.ptithcm.modules.khoa;

import com.ptithcm.entity.Khoa;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/faculty")
public class KhoaController {

    @Autowired
    private KhoaService khoaService;

    @RequestMapping()
    public String index(ModelMap model) {
        List<Khoa> khoaList = khoaService.listKhoa();
        populateCanDelete(khoaList);
        model.addAttribute("khoaList", khoaList);
        return "faculty/index";
    }

    @RequestMapping(value = "/api/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Khoa getKhoa(@RequestParam("maKhoa") String maKhoa) {
        return khoaService.getKhoaById(maKhoa);
    }

    @RequestMapping(value = "/api/save", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> saveKhoa(@RequestBody Khoa khoa, @RequestParam("mode") String mode) {
        Map<String, Object> res = new HashMap<>();
        try {
            khoaService.saveKhoa(khoa, mode);
            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Lỗi: " + e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/api/delete", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> deleteKhoa(@RequestParam("maKhoa") String maKhoa) {
        Map<String, Object> res = new HashMap<>();
        try {
            khoaService.deleteKhoa(maKhoa);
            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Lỗi: " + e.getMessage());
        }
        return res;
    }

    private void populateCanDelete(List<Khoa> list) {
        if (list.isEmpty())
            return;
        List<String> khoaWithLop = khoaService.listTrimmedKhoaFromLop();
        List<String> khoaWithGV = khoaService.listTrimmedKhoaFromGiangVien();
        List<String> khoaWithLTC = khoaService.listTrimmedKhoaFromLtc();

        java.util.Set<String> dependentIds = new java.util.HashSet<>();
        for (String id : khoaWithLop)
            if (id != null)
                dependentIds.add(id.trim().toUpperCase());
        for (String id : khoaWithGV)
            if (id != null)
                dependentIds.add(id.trim().toUpperCase());
        for (String id : khoaWithLTC)
            if (id != null)
                dependentIds.add(id.trim().toUpperCase());

        for (Khoa k : list) {
            String trimmed = k.getMaKhoa() != null ? k.getMaKhoa().trim().toUpperCase() : "";
            k.setCanDelete(!dependentIds.contains(trimmed));
        }
    }
}
