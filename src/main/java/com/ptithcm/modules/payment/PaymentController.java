package com.ptithcm.modules.payment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptithcm.entities.DangKy;
import com.ptithcm.entities.Lop;
import com.ptithcm.modules.classroom.ClassroomService;
import com.ptithcm.modules.payment.providers.PaymentProvider;
import com.ptithcm.shared.constants.MessageConstant;
import com.ptithcm.shared.constants.SessionConstant;
import com.ptithcm.shared.enums.RoleEnum;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ClassroomService classroomService;

    @jakarta.annotation.Resource(name = "paymentProviderRegistry")
    private Map<String, PaymentProvider> paymentProviderRegistry;

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
            @RequestParam("hocKy") int hocKy, @RequestParam(value = "method", defaultValue = "momo") String method,
            RedirectAttributes redirectAttributes) {

        com.ptithcm.shared.dtos.UserSession userSession = (com.ptithcm.shared.dtos.UserSession) session
                .getAttribute(SessionConstant.USER);
        String maSV = userSession.getUsername();
        List<DangKy> unpaids = paymentService.getUnpaidRegistrations(maSV, nienKhoa, hocKy);

        int tongTinChi = 0;
        for (DangKy dk : unpaids) {
            tongTinChi += dk.getLopTinChi().getMonHoc().getSoTinChi();
        }

        if (tongTinChi == 0) {
            redirectAttributes.addFlashAttribute("error", MessageConstant.PAYMENT_NO_SUBJECT);
            return "redirect:/payment";
        }

        PaymentProvider provider = paymentProviderRegistry.get(method);
        if (provider == null) {
            log.warn("[PAYMENT] Yêu cầu cổng thanh toán không được hỗ trợ: {}", method);
            redirectAttributes.addFlashAttribute("error", MessageConstant.PAYMENT_METHOD_UNSUPPORTED);
            return "redirect:/payment";
        }

        long amount = tongTinChi * 1_000_000L;
        String orderId = maSV + "_" + nienKhoa + "_" + hocKy + "_" + System.currentTimeMillis();

        String baseUrl = String.format("%s://%s:%d%s", request.getScheme(), request.getServerName(),
                request.getServerPort(), request.getContextPath());
        String returnUrl = baseUrl + "/payment/momo-return";

        String mockSuccessUrl = returnUrl + "?resultCode=0&orderId=" + orderId;
        log.info("=================================================");
        log.info("[MOMO HACK] Click vao link sau de GIA LAP THANH TOAN THANH CONG (Khong can quet QR):");
        log.info(mockSuccessUrl);
        log.info("=================================================");

        try {
            String payUrl = provider.generatePaymentUrl(orderId, amount, baseUrl);
            if (payUrl == null) {
                redirectAttributes.addFlashAttribute("error",
                        String.format(MessageConstant.PAYMENT_CONNECTION_ERROR_TEMPLATE, method.toUpperCase()));
                return "redirect:/payment";
            }
            return "redirect:" + payUrl;
        } catch (Exception e) {
            log.error("[PAYMENT] Exception generating payment url via {}", method, e);
            redirectAttributes.addFlashAttribute("error", MessageConstant.PAYMENT_CONNECTION_FAILED);
            return "redirect:/payment";
        }
    }

    @GetMapping("/momo-return")
    public String momoReturn(@RequestParam Map<String, String> params, HttpSession session,
            RedirectAttributes redirectAttributes) {

        PaymentProvider provider = paymentProviderRegistry.get("momo");
        if (provider == null || !provider.verifySignature(params)) {
            log.warn("[PAYMENT] Cảnh báo bảo mật: Chữ ký thanh toán không hợp lệ!");
            redirectAttributes.addFlashAttribute("error", MessageConstant.PAYMENT_SIGNATURE_INVALID);
            return "redirect:/payment";
        }

        String resultCode = params.get("resultCode");
        String orderId = params.get("orderId");

        if ("0".equals(resultCode)) {
            try {
                provider.processIpn(params); // Tận dụng method processIpn của MoMoPaymentProvider để cập nhật DB
                log.info("[PAYMENT] Sinh vien da THANH TOAN THANH CONG qua MoMo cho order {}", orderId);
                redirectAttributes.addFlashAttribute("message", MessageConstant.PAYMENT_SUCCESS);
                String[] parts = orderId.split("_");
                if (parts.length >= 3) {
                    return "redirect:/payment?nienKhoa=" + parts[1] + "&hocKy=" + parts[2];
                }
            } catch (Exception e) {
                log.error("[PAYMENT] Error processing checkout redirect success for order {}", orderId, e);
                redirectAttributes.addFlashAttribute("error", MessageConstant.PAYMENT_RECORD_ERROR);
            }
        } else {
            redirectAttributes.addFlashAttribute("error",
                    String.format(MessageConstant.PAYMENT_FAILED_TEMPLATE, resultCode));

            String[] orderParts = orderId != null ? orderId.split("_") : new String[0];
            if (orderParts.length >= 3) {
                return "redirect:/payment?nienKhoa=" + orderParts[1] + "&hocKy=" + orderParts[2];
            }
        }

        return "redirect:/payment";
    }

    @PostMapping("/momo-ipn")
    @ResponseBody
    public ResponseEntity<Void> momoIpn(@RequestBody(required = false) Map<String, String> jsonParams,
            @RequestParam(required = false) Map<String, String> queryParams) {

        Map<String, String> params = new HashMap<>();
        if (queryParams != null) {
            params.putAll(queryParams);
        }
        if (jsonParams != null) {
            params.putAll(jsonParams);
        }

        log.info("[MOMO IPN] Received IPN request: {}", params);

        PaymentProvider provider = paymentProviderRegistry.get("momo");
        if (provider == null || !provider.verifySignature(params)) {
            log.warn("[MOMO IPN] Invalid signature for IPN!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            provider.processIpn(params);
        } catch (Exception e) {
            log.error("[MOMO IPN] Error processing IPN database update", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.noContent().build(); // Trả về HTTP 204 No Content
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
