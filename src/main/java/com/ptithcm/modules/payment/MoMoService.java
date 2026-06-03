package com.ptithcm.modules.payment;

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

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MoMoService {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String createPayment(String orderId, String orderInfo, long amount, String returnUrl, String ipnUrl) {
        try {
            String requestId = String.valueOf(System.currentTimeMillis());
            String extraData = "";

            String rawSignature = "accessKey=" + MoMoConfig.ACCESS_KEY + "&amount=" + amount + "&extraData=" + extraData
                    + "&ipnUrl=" + ipnUrl + "&orderId=" + orderId + "&orderInfo=" + orderInfo + "&partnerCode="
                    + MoMoConfig.PARTNER_CODE + "&redirectUrl=" + returnUrl + "&requestId=" + requestId
                    + "&requestType=" + MoMoConfig.REQUEST_TYPE;

            String signature = signHmacSHA256(rawSignature, MoMoConfig.SECRET_KEY);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("partnerCode", MoMoConfig.PARTNER_CODE);
            requestBody.put("requestId", requestId);
            requestBody.put("amount", amount);
            requestBody.put("orderId", orderId);
            requestBody.put("orderInfo", orderInfo);
            requestBody.put("redirectUrl", returnUrl);
            requestBody.put("ipnUrl", ipnUrl);
            requestBody.put("requestType", MoMoConfig.REQUEST_TYPE);
            requestBody.put("extraData", extraData);
            requestBody.put("lang", "vi");
            requestBody.put("signature", signature);

            String jsonBody = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(MoMoConfig.ENDPOINT))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8)).build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode jsonNode = objectMapper.readTree(response.body());
                if (jsonNode.has("payUrl")) {
                    String payUrl = jsonNode.get("payUrl").asText();
                    System.out.println("=================================================");
                    System.out.println("[MOMO] LINK THANH TOÁN: " + payUrl);
                    System.out.println("=================================================");
                    return payUrl;
                } else {
                    System.err.println("[MOMO] Lỗi: " + response.body());
                }
            } else {
                System.err.println("[MOMO] Lỗi HTTP " + response.statusCode() + ": " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean verifySignature(Map<String, String> params) {
        try {
            String rawSignature = "accessKey=" + MoMoConfig.ACCESS_KEY + "&amount=" + params.get("amount")
                    + "&extraData=" + params.get("extraData") + "&message=" + params.get("message") + "&orderId="
                    + params.get("orderId") + "&orderInfo=" + params.get("orderInfo") + "&orderType="
                    + params.get("orderType") + "&partnerCode=" + params.get("partnerCode") + "&payType="
                    + params.get("payType") + "&requestId=" + params.get("requestId") + "&responseTime="
                    + params.get("responseTime") + "&resultCode=" + params.get("resultCode") + "&transId="
                    + params.get("transId");

            String expectedSignature = signHmacSHA256(rawSignature, MoMoConfig.SECRET_KEY);
            return expectedSignature.equals(params.get("signature"));
        } catch (Exception e) {
            return false;
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
