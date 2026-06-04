package com.ptithcm.shared.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.ptithcm.shared.constants.SessionConstant;
import com.ptithcm.shared.dtos.UserSession;

public class AdminAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        UserSession user = (UserSession) session.getAttribute(SessionConstant.USER);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        String role = user.getRole();

        // 1. Must be PGV or KHOA to access admin/accounts routes
        if (!"PGV".equals(role) && !"KHOA".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/home?error=access_denied");
            return false;
        }

        // 2. Advanced routing: PGV-only features (Faculty, Registration, Account)
        String uri = request.getRequestURI();
        if (uri.contains("/admin/faculty") || uri.contains("/admin/registration") || uri.contains("/admin/account")) {
            if (!"PGV".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/home?error=pgv_only");
                return false;
            }
        }

        return true;
    }
}
