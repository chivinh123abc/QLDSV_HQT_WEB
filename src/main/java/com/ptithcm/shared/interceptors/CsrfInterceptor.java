package com.ptithcm.shared.interceptors;

import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

public class CsrfInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CsrfInterceptor.class);
    private static final String CSRF_TOKEN_SESSION_ATTR = "CSRF_TOKEN";
    private static final String CSRF_TOKEN_REQ_PARAM = "csrf_token";

    @Value("${pusher.key}")
    private String pusherKey;

    @Value("${pusher.cluster}")
    private String pusherCluster;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        request.setAttribute("pusherKey", pusherKey);
        request.setAttribute("pusherCluster", pusherCluster);

        HttpSession session = request.getSession();
        String method = request.getMethod();

        // Đảm bảo luôn có CSRF Token trong Session và đưa vào Request Attribute
        // (để phục vụ việc render JSP ngay cả khi Forward từ một yêu cầu POST thất bại)
        String csrfToken = (String) session.getAttribute(CSRF_TOKEN_SESSION_ATTR);
        if (csrfToken == null) {
            csrfToken = UUID.randomUUID().toString();
            session.setAttribute(CSRF_TOKEN_SESSION_ATTR, csrfToken);
        }
        request.setAttribute("csrfToken", csrfToken);

        // 1. Nếu là GET: Cho phép đi tiếp (token đã được đưa vào request ở trên)
        if ("GET".equalsIgnoreCase(method)) {
            return true;
        }

        // 2. Nếu là POST, PUT, DELETE: Bắt buộc kiểm tra Token
        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method)) {
            String requestToken = request.getParameter(CSRF_TOKEN_REQ_PARAM);

            if (csrfToken.equals(requestToken)) {
                return true; // Token hợp lệ, cho phép đi tiếp vào Controller
            }

            // Token sai hoặc không có -> Redirect về trang login với thông báo lỗi
            logger.warn("[SECURITY ALERT] Suspicious CSRF activity detected from IP: {}", request.getRemoteAddr());
            response.sendRedirect(request.getContextPath() + "/login?error=session_expired");
            return false;
        }

        return true;
    }
}
