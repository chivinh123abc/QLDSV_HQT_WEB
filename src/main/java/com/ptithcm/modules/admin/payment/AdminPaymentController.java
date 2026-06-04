package com.ptithcm.modules.admin.payment;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ptithcm.entities.Lop;
import com.ptithcm.modules.classroom.ClassroomService;
import com.ptithcm.modules.payment.PaymentService;

@Controller
@RequestMapping("/admin/payment")
public class AdminPaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ClassroomService classroomService;

    @GetMapping
    public String index(ModelMap model, HttpSession session,
            @RequestParam(value = "maLop", required = false) String maLop,
            @RequestParam(value = "nienKhoa", required = false) String nienKhoa,
            @RequestParam(value = "hocKy", required = false) Integer hocKy) {

        List<Lop> lopList = classroomService.listAllLop();
        model.addAttribute("lopList", lopList);
        model.addAttribute("allSemesters", paymentService.getAllSemesters());

        if (maLop != null && nienKhoa != null && hocKy != null) {
            List<Object[]> stats = paymentService.getPaymentStatsByClass(maLop, nienKhoa, hocKy);
            model.addAttribute("stats", stats);

            long sumTongTien = 0;
            long sumDaDong = 0;
            for (Object[] row : stats) {
                long tien = (Long) row[3];
                boolean daDong = (Boolean) row[4];
                sumTongTien += tien;
                if (daDong)
                    sumDaDong += tien;
            }
            model.addAttribute("sumTongTien", sumTongTien);
            model.addAttribute("sumDaDong", sumDaDong);
        }

        model.addAttribute("maLop", maLop);
        model.addAttribute("nienKhoa", nienKhoa);
        model.addAttribute("hocKy", hocKy);

        return "admin/payment/index";
    }
}
