package com.ptithcm.modules.payment;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptithcm.entities.DangKy;
import com.ptithcm.entities.Lop;
import com.ptithcm.modules.classroom.ClassroomService;
import com.ptithcm.shared.constants.SessionConstant;
import com.ptithcm.shared.enums.RoleEnum;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MoMoService moMoService;

    @Autowired
    private ClassroomService classroomService;

    // ==========================================
    // MÀN HÌNH SINH VIÊN (SV) - THANH TOÁN
    // ==========================================
    @GetMapping
    public String indexSV(ModelMap model, HttpSession session,
            @RequestParam(value = "nienKhoa", required = false) String nienKhoa,
            @RequestParam(value = "hocKy", required = false) Integer hocKy) {

        String role = (String) session.getAttribute(SessionConstant.ROLE);
        if (!RoleEnum.SINHVIEN.getCode().equals(role)) {
            return "redirect:/"; // Chỉ SV mới vào được trang này
        }

        com.ptithcm.shared.dtos.UserSession userSession = (com.ptithcm.shared.dtos.UserSession) session
                .getAttribute(SessionConstant.USER);
        String maSV = userSession.getUsername(); // Đối với SV, USERNAME chính là maSV

        if (nienKhoa == null || hocKy == null) {
            String[] latest = paymentService.getLatestSemesterOfStudent(maSV);
            if (latest != null) {
                nienKhoa = latest[0];
                hocKy = Integer.parseInt(latest[1]);
            }
        }

        if (nienKhoa != null && hocKy != null) {
            List<DangKy> dks = paymentService.getRegistrations(maSV, nienKhoa, hocKy);
            int tongTinChi = 0;
            boolean canPay = false;

            for (DangKy dk : dks) {
                if (dk.getTrangThaiDangKy().name().equals("HIEU_LUC")) {
                    tongTinChi += dk.getLopTinChi().getMonHoc().getSoTinChi();
                    if (!dk.isDaThanhToan()) {
                        canPay = true;
                    }
                }
            }

            model.addAttribute("dsDangKy", dks);
            model.addAttribute("tongTinChi", tongTinChi);
            model.addAttribute("tongTien", tongTinChi * 1_000_000L);
            model.addAttribute("canPay", canPay);
        }

        model.addAttribute("nienKhoa", nienKhoa);
        model.addAttribute("hocKy", hocKy);

        // Lấy ds học kỳ để render combobox
        model.addAttribute("allSemesters", paymentService.getAllSemesters());

        return "payment/student";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session, HttpServletRequest request, @RequestParam("nienKhoa") String nienKhoa,
            @RequestParam("hocKy") int hocKy, RedirectAttributes redirectAttributes) {
        com.ptithcm.shared.dtos.UserSession userSession = (com.ptithcm.shared.dtos.UserSession) session
                .getAttribute(SessionConstant.USER);
        String maSV = userSession.getUsername();
        List<DangKy> unpaids = paymentService.getUnpaidRegistrations(maSV, nienKhoa, hocKy);

        int tongTinChi = 0;
        for (DangKy dk : unpaids) {
            tongTinChi += dk.getLopTinChi().getMonHoc().getSoTinChi();
        }

        if (tongTinChi == 0) {
            redirectAttributes.addFlashAttribute("error", "Không có môn học nào cần thanh toán!");
            return "redirect:/payment";
        }

        long amount = tongTinChi * 1_000_000L;
        String orderId = maSV + "_" + nienKhoa + "_" + hocKy + "_" + System.currentTimeMillis();
        String orderInfo = "Thanh toan hoc phi HK" + hocKy + " " + nienKhoa + " - " + maSV;

        String baseUrl = String.format("%s://%s:%d%s", request.getScheme(), request.getServerName(),
                request.getServerPort(), request.getContextPath());
        String returnUrl = baseUrl + "/payment/momo-return";
        String ipnUrl = baseUrl + "/payment/momo-ipn";

        String mockSuccessUrl = returnUrl + "?resultCode=0&orderId=" + orderId;
        System.out.println("=================================================");
        System.out.println("[MOMO HACK] Click vao link sau de GIA LAP THANH TOAN THANH CONG (Khong can quet QR):");
        System.out.println(mockSuccessUrl);
        System.out.println("=================================================");

        String payUrl = moMoService.createPayment(orderId, orderInfo, amount, returnUrl, ipnUrl);
        if (payUrl == null) {
            redirectAttributes.addFlashAttribute("error",
                    "Không thể kết nối đến cổng thanh toán MoMo. Vui lòng thử lại sau.");
            return "redirect:/payment";
        }
        return "redirect:" + payUrl;
    }

    @GetMapping("/momo-return")
    public String momoReturn(@RequestParam Map<String, String> params, HttpSession session,
            RedirectAttributes redirectAttributes) {
        // Kiểm tra kết quả
        String resultCode = params.get("resultCode");
        String orderId = params.get("orderId");

        if ("0".equals(resultCode)) {
            // Thanh toán thành công -> update DB
            String[] parts = orderId.split("_");
            if (parts.length >= 3) {
                String maSV = parts[0];
                String nienKhoa = parts[1];
                int hocKy = Integer.parseInt(parts[2]);
                paymentService.markAsPaid(maSV, nienKhoa, hocKy);

                System.out.println("=================================================");
                System.out.println("[PAYMENT] Sinh vien " + maSV + " da THANH TOAN THANH CONG hoc phi HK" + hocKy + " ("
                        + nienKhoa + ")");
                System.out.println("=================================================");

                redirectAttributes.addFlashAttribute("message", "Thanh toán thành công!");
                return "redirect:/payment?nienKhoa=" + nienKhoa + "&hocKy=" + hocKy;
            }
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "Thanh toán thất bại hoặc đã bị hủy (Mã lỗi: " + resultCode + ")");

            String[] orderParts = orderId != null ? orderId.split("_") : new String[0];
            if (orderParts.length >= 3) {
                return "redirect:/payment?nienKhoa=" + orderParts[1] + "&hocKy=" + orderParts[2];
            }
        }

        return "redirect:/payment";
    }

    // ==========================================
    // MÀN HÌNH GIẢNG VIÊN (GV) - THỐNG KÊ
    // ==========================================
    @GetMapping("/stats")
    public String statsGV(ModelMap model, HttpSession session,
            @RequestParam(value = "maLop", required = false) String maLop,
            @RequestParam(value = "nienKhoa", required = false) String nienKhoa,
            @RequestParam(value = "hocKy", required = false) Integer hocKy) {

        String role = (String) session.getAttribute(SessionConstant.ROLE);
        if (RoleEnum.SINHVIEN.getCode().equals(role)) {
            return "redirect:/"; // SV không có quyền
        }

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

        return "payment/lecturer";
    }
}
