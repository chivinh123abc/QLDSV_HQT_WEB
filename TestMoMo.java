import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TestMoMo {
    public static final String PARTNER_CODE = "MOMOBKUN20180529";
    public static final String ACCESS_KEY = "klm05TvNBzhg7h7j";
    public static final String SECRET_KEY = "at67qH6mk8w5Y1nAyMoYKMWACiEi2bsa";
    public static final String ENDPOINT = "https://test-payment.momo.vn/v2/gateway/api/create";
    public static final String REQUEST_TYPE = "captureWallet";

    public static void main(String[] args) throws Exception {
        long amount = 1000000;
        String orderId = "SV01_2025-2026_1_" + System.currentTimeMillis();
        String orderInfo = "Thanh toan";
        String returnUrl = "http://localhost:8080/return";
        String ipnUrl = "http://localhost:8080/ipn";
        String requestId = String.valueOf(System.currentTimeMillis());
        String extraData = "";

        String rawSignature = "accessKey=" + ACCESS_KEY + "&amount=" + amount + "&extraData=" + extraData
                + "&ipnUrl=" + ipnUrl + "&orderId=" + orderId + "&orderInfo=" + orderInfo + "&partnerCode="
                + PARTNER_CODE + "&redirectUrl=" + returnUrl + "&requestId=" + requestId
                + "&requestType=" + REQUEST_TYPE;

        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSHA256.init(secretKeySpec);
        byte[] hash = hmacSHA256.doFinal(rawSignature.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        String signature = hexString.toString();
        
        System.out.println("Raw Signature: " + rawSignature);
        System.out.println("Signature: " + signature);

        String jsonBody = "{"
            + "\"partnerCode\":\"" + PARTNER_CODE + "\","
            + "\"requestId\":\"" + requestId + "\","
            + "\"amount\":" + amount + ","
            + "\"orderId\":\"" + orderId + "\","
            + "\"orderInfo\":\"" + orderInfo + "\","
            + "\"redirectUrl\":\"" + returnUrl + "\","
            + "\"ipnUrl\":\"" + ipnUrl + "\","
            + "\"requestType\":\"" + REQUEST_TYPE + "\","
            + "\"extraData\":\"" + extraData + "\","
            + "\"lang\":\"vi\","
            + "\"signature\":\"" + signature + "\""
            + "}";

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(ENDPOINT))
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8)).build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status: " + response.statusCode());
        System.out.println("Body: " + response.body());
    }
}
