package com.ptithcm.modules.payment.providers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.ptithcm.modules.payment.PaymentService;
import com.ptithcm.modules.payment.configs.MoMoProperties;

public class MoMoPaymentProvider implements PaymentProvider {

    private static final Logger log = LoggerFactory.getLogger(MoMoPaymentProvider.class);

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private MoMoProperties moMoProperties;

    @Autowired
    private PaymentService paymentService;

    public void setMoMoProperties(MoMoProperties moMoProperties) {
        this.moMoProperties = moMoProperties;
    }

    @Override
    public String generatePaymentUrl(String orderId, long amount, String baseUrl) throws Exception {
        try {
            String requestId = String.valueOf(System.currentTimeMillis());
            String extraData = "";

            // Reconstruct orderInfo from orderId parts
            String orderInfo = "Thanh toan hoc phi";
            String[] parts = orderId.split("_");
            if (parts.length >= 3) {
                orderInfo = "Thanh toan hoc phi HK" + parts[2] + " " + parts[1] + " - " + parts[0];
            }

            String returnUrl = baseUrl + "/payment/momo-return";
            String ipnUrl = baseUrl + "/payment/momo-ipn";

            String rawSignature = "accessKey=" + moMoProperties.getAccessKey() + "&amount=" + amount + "&extraData="
                    + extraData + "&ipnUrl=" + ipnUrl + "&orderId=" + orderId + "&orderInfo=" + orderInfo
                    + "&partnerCode=" + moMoProperties.getPartnerCode() + "&redirectUrl=" + returnUrl + "&requestId="
                    + requestId + "&requestType=" + moMoProperties.getRequestType();

            String signature = signHmacSHA256(rawSignature, moMoProperties.getSecretKey());

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("partnerCode", moMoProperties.getPartnerCode());
            requestBody.put("requestId", requestId);
            requestBody.put("amount", amount);
            requestBody.put("orderId", orderId);
            requestBody.put("orderInfo", orderInfo);
            requestBody.put("redirectUrl", returnUrl);
            requestBody.put("ipnUrl", ipnUrl);
            requestBody.put("requestType", moMoProperties.getRequestType());
            requestBody.put("extraData", extraData);
            requestBody.put("lang", "vi");
            requestBody.put("signature", signature);

            String jsonBody = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(moMoProperties.getEndpoint()))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8)).build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode jsonNode = objectMapper.readTree(response.body());
                if (jsonNode.has("payUrl")) {
                    String payUrl = jsonNode.get("payUrl").asText();
                    log.info("[MOMO PROVIDER] Created payment link successfully: {}", payUrl);
                    return payUrl;
                } else {
                    log.error("[MOMO PROVIDER] Error response from MoMo: {}", response.body());
                }
            } else {
                log.error("[MOMO PROVIDER] HTTP Error {}: {}", response.statusCode(), response.body());
            }

        } catch (Exception e) {
            log.error("[MOMO PROVIDER] Exception generating payment url for order: {}", orderId, e);
            throw e;
        }

        return null;
    }

    @Override
    public boolean verifySignature(Map<String, String> params) {
        // try {
        // String rawSignature = "accessKey=" + moMoProperties.getAccessKey() +
        // "&amount=" + params.get("amount")
        // + "&extraData=" + params.get("extraData") + "&message=" +
        // params.get("message") + "&orderId="
        // + params.get("orderId") + "&orderInfo=" + params.get("orderInfo") +
        // "&orderType="
        // + params.get("orderType") + "&partnerCode=" + params.get("partnerCode") +
        // "&payType="
        // + params.get("payType") + "&requestId=" + params.get("requestId") +
        // "&responseTime="
        // + params.get("responseTime") + "&resultCode=" + params.get("resultCode") +
        // "&transId="
        // + params.get("transId");

        // String expectedSignature = signHmacSHA256(rawSignature,
        // moMoProperties.getSecretKey());
        // String actualSignature = params.get("signature");

        // if (expectedSignature == null || actualSignature == null) {
        // return false;
        // }

        // return
        // MessageDigest.isEqual(expectedSignature.getBytes(StandardCharsets.UTF_8),
        // actualSignature.getBytes(StandardCharsets.UTF_8));
        // } catch (Exception e) {
        // log.error("[MOMO PROVIDER] Exception verifying signature", e);
        // return false;
        // }
        log.info("[MOMO PROVIDER] Bypassing signature verification (Development Mode)");
        // TODO: REVERT FOR PRODUCTION
        return true;
    }

    @Override
    public void processIpn(Map<String, String> params) throws Exception {
        String resultCode = params.get("resultCode");
        String orderId = params.get("orderId");

        if ("0".equals(resultCode) && orderId != null) {
            String[] parts = orderId.split("_");
            if (parts.length >= 3) {
                String maSV = parts[0];
                String nienKhoa = parts[1];
                int hocKy = Integer.parseInt(parts[2]);
                paymentService.markAsPaid(maSV, nienKhoa, hocKy);
                log.info("[MOMO IPN] Successfully marked as paid for student: {}, HK: {}, NK: {}", maSV, hocKy,
                        nienKhoa);
            }
        } else {
            log.warn("[MOMO IPN] Payment failed or cancelled with resultCode: {}", resultCode);
        }
    }

    private String signHmacSHA256(String data, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSHA256.init(secretKeySpec);
        byte[] hash = hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
