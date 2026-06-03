package com.ptithcm.modules.payment.providers;

import java.util.Map;

public interface PaymentProvider {
    String generatePaymentUrl(String orderId, long amount, String baseUrl) throws Exception;
    boolean verifySignature(Map<String, String> params);
    void processIpn(Map<String, String> params) throws Exception;
}
