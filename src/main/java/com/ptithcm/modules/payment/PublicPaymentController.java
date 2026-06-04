package com.ptithcm.modules.payment;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ptithcm.modules.payment.providers.PaymentProvider;

@Controller
@RequestMapping("/api/payment")
public class PublicPaymentController {

    private static final Logger log = LoggerFactory.getLogger(PublicPaymentController.class);

    @jakarta.annotation.Resource(name = "paymentProviderRegistry")
    private Map<String, PaymentProvider> paymentProviderRegistry;

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

        return ResponseEntity.noContent().build();
    }
}
