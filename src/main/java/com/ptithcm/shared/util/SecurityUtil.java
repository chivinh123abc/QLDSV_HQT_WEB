package com.ptithcm.shared.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class SecurityUtil {

    private SecurityUtil() {
    }

    /**
     * Mã hóa tên đăng nhập và mật khẩu thành một chuỗi Base64 cho cookie
     * remember-me.
     *
     * @param username
     *            Tên đăng nhập
     * @param password
     *            Mật khẩu
     * @return Chuỗi mã hóa Base64, hoặc chuỗi rỗng nếu có tham số null
     */
    public static String encodeCookie(String username, String password) {
        if (username == null || password == null) {
            return "";
        }
        String credentials = username + ":" + password;
        return Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Giải mã giá trị Base64 của cookie remember-me thành mảng chứa tên đăng nhập
     * và mật khẩu.
     *
     * @param cookieValue
     *            Giá trị cookie Base64 cần giải mã
     * @return Mảng String [username, password], hoặc null nếu không hợp lệ
     */
    public static String[] decodeCookie(String cookieValue) {
        if (cookieValue == null || cookieValue.isEmpty()) {
            return null;
        }
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(cookieValue);
            String credentials = new String(decodedBytes, StandardCharsets.UTF_8);
            String[] parts = credentials.split(":");
            if (parts.length == 2) {
                return parts;
            }
        } catch (Exception e) {
            // Định dạng hoặc mã hóa không hợp lệ
        }
        return null;
    }
}
