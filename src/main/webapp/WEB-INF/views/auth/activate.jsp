<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title><s:message code="auth.activate.title"/></title>
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Animate.css -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css" />
    <style>
        :root {
            --primary-gradient: linear-gradient(135deg, #4361ee, #4895ef);
            --bg-light: #f8f9fa;
        }
        body {
            font-family: 'Inter', sans-serif;
            background: #f0f2f5;
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0;
        }
        .login-card {
            width: 100%;
            max-width: 420px;
            background: #fff;
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.1);
            padding: 40px;
            border: 1px solid rgba(0,0,0,0.05);
        }
        .login-header {
            text-align: center;
            margin-bottom: 35px;
        }
        .login-header h2 {
            font-weight: 700;
            background: var(--primary-gradient);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            margin-bottom: 10px;
        }
        .login-header p {
            color: #6c757d;
            font-size: 0.9rem;
        }
        .form-label {
            font-weight: 600;
            font-size: 0.85rem;
            color: #495057;
            margin-bottom: 8px;
        }
        .form-control {
            border-radius: 12px;
            padding: 12px 15px;
            border: 1px solid #e2e8f0;
            background-color: #f8fafc;
            transition: all 0.2s;
        }
        .form-control:focus {
            background-color: #fff;
            border-color: #4361ee;
            box-shadow: 0 0 0 4px rgba(67, 97, 238, 0.1);
        }
        .login-btn {
            background: var(--primary-gradient);
            border: none;
            padding: 12px;
            border-radius: 12px;
            font-weight: 600;
            color: #fff;
            width: 100%;
            margin-top: 15px;
            box-shadow: 0 4px 15px rgba(67, 97, 238, 0.3);
            transition: all 0.3s;
        }
        .login-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(67, 97, 238, 0.4);
        }

        .alert-custom {
            border-radius: 12px;
            padding: 12px;
            font-size: 0.85rem;
            font-weight: 500;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="login-card animate__animated animate__zoomIn">
        <div class="login-header">
            <div class="mb-3">
                <i class="bi bi-shield-lock-fill fs-1 text-primary"></i>
            </div>
            <h2>Kích hoạt tài khoản</h2>
            <p class="text-muted small">Chúng tôi đã gửi một mã OTP gồm 6 chữ số đến email: <br><strong>${maskedEmail}</strong></p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-custom animate__animated animate__shakeX">
                <i class="bi bi-exclamation-circle me-2"></i> ${error}
            </div>
        </c:if>
        <c:if test="${not empty message}">
            <div class="alert alert-success alert-custom animate__animated animate__fadeIn">
                <i class="bi bi-check-circle me-2"></i> ${message}
            </div>
        </c:if>
        <c:if test="${not empty info}">
            <div class="alert alert-info alert-custom animate__animated animate__fadeIn">
                <i class="bi bi-info-circle me-2"></i> ${info}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/activate" method="post">
            <input type="hidden" name="csrf_token" value="${csrfToken}" />

            <div class="mb-4">
                <label class="form-label">Mã xác thực OTP (5 phút)</label>
                <div class="input-group">
                    <span class="input-group-text bg-light border-end-0" style="border-top-left-radius: 12px; border-bottom-left-radius: 12px; border-color: #e2e8f0;">
                        <i class="bi bi-key-fill text-muted"></i>
                    </span>
                    <input type="text" name="otpCode" id="otpCode" class="form-control border-start-0 text-center fw-bold fs-5" style="border-top-right-radius: 12px; border-bottom-right-radius: 12px; border-color: #e2e8f0; background-color: #f8fafc; letter-spacing: 2px;" placeholder="Mã OTP" maxlength="6" autocomplete="off" required>
                </div>
            </div>

            <button type="submit" class="login-btn">KÍCH HOẠT TÀI KHOẢN</button>
        </form>

        <div class="mt-4 d-flex justify-content-between align-items-center">
            <a href="${pageContext.request.contextPath}/logout" class="text-decoration-none small fw-semibold text-muted">
                <i class="bi bi-arrow-left me-1"></i>Đăng xuất
            </a>
            <a href="${pageContext.request.contextPath}/activate/resend" class="text-decoration-none small fw-semibold text-primary">
                Gửi lại mã OTP <i class="bi bi-arrow-right-short"></i>
            </a>
        </div>
    </div>
</body>
</html>
