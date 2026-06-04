package com.ptithcm.modules.student.payment;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptithcm.entities.DangKy;
import com.ptithcm.modules.payment.PaymentService;
import com.ptithcm.modules.payment.providers.PaymentProvider;
import com.ptithcm.shared.constants.MessageConstant;
import com.ptithcm.shared.constants.SessionConstant;

@Controller
@RequestMapping("/student/payment")
public class StudentPaymentController {

    private static final Logger log = LoggerFactory.getLogger(StudentPaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @jakarta.annotation.Resource(name = "paymentProviderRegistry")
    private Map<String, PaymentProvider> paymentProviderRegistry;

    @GetMapping
    public String indexSV(ModelMap model, HttpSession session,
            @RequestParam(value = "nienKhoa", required = false) String nienKhoa,
            @RequestParam(value = "hocKy", required = false) Integer hocKy) {

        com.ptithcm.shared.dtos.UserSession userSession = (com.ptithcm.shared.dtos.UserSession) session
                .getAttribute(SessionConstant.USER);
        if (userSession == null) {
            return "redirect:/login";
        }
        String maSV = userSession.getUsername();

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
        model.addAttribute("allSemesters", paymentService.getAllSemesters());

        return "student/payment/index";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session, HttpServletRequest request, @RequestParam("nienKhoa") String nienKhoa,
            @RequestParam("hocKy") int hocKy, @RequestParam(value = "method", defaultValue = "momo") String method,
            RedirectAttributes redirectAttributes) {

        com.ptithcm.shared.dtos.UserSession userSession = (com.ptithcm.shared.dtos.UserSession) session
                .getAttribute(SessionConstant.USER);
        if (userSession == null) {
            return "redirect:/login";
        }
        String maSV = userSession.getUsername();
        List<DangKy> unpaids = paymentService.getUnpaidRegistrations(maSV, nienKhoa, hocKy);

        int tongTinChi = 0;
        for (DangKy dk : unpaids) {
            tongTinChi += dk.getLopTinChi().getMonHoc().getSoTinChi();
        }

        if (tongTinChi == 0) {
            redirectAttributes.addFlashAttribute("error", MessageConstant.PAYMENT_NO_SUBJECT);
            return "redirect:/student/payment";
        }

        PaymentProvider provider = paymentProviderRegistry.get(method);
        if (provider == null) {
            log.warn("[PAYMENT] Yêu cầu cổng thanh toán không được hỗ trợ: {}", method);
            redirectAttributes.addFlashAttribute("error", MessageConstant.PAYMENT_METHOD_UNSUPPORTED);
            return "redirect:/student/payment";
        }

        long amount = tongTinChi * 1_000_000L;
        String orderId = maSV + "_" + nienKhoa + "_" + hocKy + "_" + System.currentTimeMillis();

        String baseUrl = String.format("%s://%s:%d%s", request.getScheme(), request.getServerName(),
                request.getServerPort(), request.getContextPath());
        String returnUrl = baseUrl + "/student/payment/momo-return";

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
                return "redirect:/student/payment";
            }
            return "redirect:" + payUrl;
        } catch (Exception e) {
            log.error("[PAYMENT] Exception generating payment url via {}", method, e);
            redirectAttributes.addFlashAttribute("error", MessageConstant.PAYMENT_CONNECTION_FAILED);
            return "redirect:/student/payment";
        }
    }

    @GetMapping("/momo-return")
    public String momoReturn(@RequestParam Map<String, String> params, HttpSession session,
            RedirectAttributes redirectAttributes) {

        PaymentProvider provider = paymentProviderRegistry.get("momo");
        if (provider == null || !provider.verifySignature(params)) {
            log.warn("[PAYMENT] Cảnh báo bảo mật: Chữ ký thanh toán không hợp lệ!");
            redirectAttributes.addFlashAttribute("error", MessageConstant.PAYMENT_SIGNATURE_INVALID);
            return "redirect:/student/payment";
        }

        String resultCode = params.get("resultCode");
        String orderId = params.get("orderId");

        if ("0".equals(resultCode)) {
            try {
                provider.processIpn(params);
                log.info("[PAYMENT] Sinh vien da THANH TOAN THANH CONG qua MoMo cho order {}", orderId);
                redirectAttributes.addFlashAttribute("message", MessageConstant.PAYMENT_SUCCESS);
                String[] parts = orderId.split("_");
                if (parts.length >= 3) {
                    return "redirect:/student/payment?nienKhoa=" + parts[1] + "&hocKy=" + parts[2];
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
                return "redirect:/student/payment?nienKhoa=" + orderParts[1] + "&hocKy=" + orderParts[2];
            }
        }

        return "redirect:/student/payment";
    }
}
