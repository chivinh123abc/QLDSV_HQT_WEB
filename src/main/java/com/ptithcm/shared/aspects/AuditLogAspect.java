package com.ptithcm.shared.aspects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ptithcm.shared.constants.SessionConstant;
import com.ptithcm.shared.dtos.UserSession;

@Aspect
@Component
public class AuditLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogAspect.class);

    @AfterReturning("execution(* com.ptithcm.modules..*Service.save*(..)) || "
            + "execution(* com.ptithcm.modules..*Service.insert*(..)) || "
            + "execution(* com.ptithcm.modules..*Service.update*(..)) || "
            + "execution(* com.ptithcm.modules..*Service.delete*(..))")
    public void logAuditActivity(JoinPoint joinPoint) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                HttpSession session = request.getSession(false);

                String username = "System";
                if (session != null && session.getAttribute(SessionConstant.USER) != null) {
                    UserSession userSession = (UserSession) session.getAttribute(SessionConstant.USER);
                    username = userSession.getUsername();
                }

                String methodName = joinPoint.getSignature().getName();
                String className = joinPoint.getTarget().getClass().getSimpleName();

                // Sử dụng MDC (Mapped Diagnostic Context) để truyền Tên Class vào %X{context}
                // trong logback.xml
                MDC.put("context", className);

                String message = String.format("User: %s | Action: %s", username, methodName);

                // Phân loại màu sắc (Log Level) dựa trên hành động giống NestJS
                if (methodName.startsWith("delete")) {
                    logger.warn("Xoa du lieu: {}", message); // WARN sẽ có màu vàng
                } else if (methodName.startsWith("update")) {
                    logger.info("Cap nhat: {}", message); // INFO sẽ có màu xanh
                } else {
                    logger.info("Them moi: {}", message); // INFO sẽ có màu xanh
                }
            }
        } catch (Exception e) {
            MDC.put("context", "AuditLogAspect");
            logger.error("Loi khi ghi Audit Log: ", e); // ERROR sẽ có màu đỏ
        } finally {
            // Luôn nhớ xóa MDC sau khi log xong để tránh rò rỉ dữ liệu sang thread khác
            MDC.remove("context");
        }
    }
}
