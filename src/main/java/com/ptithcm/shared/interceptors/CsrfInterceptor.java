package com.ptithcm.shared.interceptors;

import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

public class CsrfInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CsrfInterceptor.class);
    private static final String CSRF_TOKEN_SESSION_ATTR = "CSRF_TOKEN";
    private static final String CSRF_TOKEN_REQ_PARAM = "csrf_token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        String method = request.getMethod();

        // 1. Nếu là GET: Sinh Token đưa xuống View
        if ("GET".equalsIgnoreCase(method)) {
            String csrfToken = (String) session.getAttribute(CSRF_TOKEN_SESSION_ATTR);
            if (csrfToken == null) {
                csrfToken = UUID.randomUUID().toString();
                session.setAttribute(CSRF_TOKEN_SESSION_ATTR, csrfToken);
            }
            // Đưa vào request để JSP có thể đọc được bằng ${csrfToken}
            request.setAttribute("csrfToken", csrfToken);
            return true;
        }

        // 2. Nếu là POST, PUT, DELETE: Bắt buộc kiểm tra Token
        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method)) {
            String sessionToken = (String) session.getAttribute(CSRF_TOKEN_SESSION_ATTR);
            String requestToken = request.getParameter(CSRF_TOKEN_REQ_PARAM);

            if (sessionToken != null && sessionToken.equals(requestToken)) {
                return true; // Token hợp lệ, cho phép đi tiếp vào Controller
            }

            // Token sai hoặc không có -> Báo lỗi 403 Forbidden
            logger.warn("[SECURITY ALERT] Suspicious CSRF activity detected from IP: {}", request.getRemoteAddr());
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Yêu cầu bị từ chối do thiếu hoặc sai mã bảo mật CSRF.");
            return false;
        }

        return true;
    }
}
