package com.ptithcm.modules.payment.providers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZaloPayPaymentProvider implements PaymentProvider {

    private static final Logger log = LoggerFactory.getLogger(ZaloPayPaymentProvider.class);

    @Override
    public String generatePaymentUrl(String orderId, long amount, String baseUrl) throws Exception {
        log.info("[ZALOPAY PROVIDER] ZaloPay chưa được triển khai!");
        throw new UnsupportedOperationException("ZaloPay chưa được triển khai!");
    }

    @Override
    public boolean verifySignature(Map<String, String> params) {
        log.info("[ZALOPAY PROVIDER] ZaloPay chưa được triển khai!");
        throw new UnsupportedOperationException("ZaloPay chưa được triển khai!");
    }

    @Override
    public void processIpn(Map<String, String> params) throws Exception {
        log.info("[ZALOPAY PROVIDER] ZaloPay chưa được triển khai!");
        throw new UnsupportedOperationException("ZaloPay chưa được triển khai!");
    }
}
