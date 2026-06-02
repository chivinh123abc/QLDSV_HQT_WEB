<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Xác thực tài khoản - Hệ thống Tín chỉ</title>
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
            padding: 20px;
        }
        .verify-card {
            width: 100%;
            max-width: 440px;
            background: #fff;
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.1);
            padding: 40px;
            border: 1px solid rgba(0,0,0,0.05);
        }
        .verify-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .verify-header h2 {
            font-weight: 700;
            background: var(--primary-gradient);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            margin-bottom: 10px;
        }
        .verify-header p {
            color: #6c757d;
            font-size: 0.9rem;
            line-height: 1.5;
        }
        .form-label {
            font-weight: 600;
            font-size: 0.85rem;
            color: #495057;
            margin-bottom: 8px;
            text-align: center;
            display: block;
        }
        .otp-input {
            font-size: 24px;
            font-weight: 700;
            letter-spacing: 12px;
            text-align: center;
            border-radius: 12px;
            padding: 12px 15px;
            border: 1px solid #e2e8f0;
            background-color: #f8fafc;
            transition: all 0.2s;
            max-width: 240px;
            margin: 0 auto;
            display: block;
        }
        .otp-input:focus {
            background-color: #fff;
            border-color: #4361ee;
            box-shadow: 0 0 0 4px rgba(67, 97, 238, 0.1);
            outline: none;
        }
        .verify-btn {
            background: var(--primary-gradient);
            border: none;
            padding: 12px;
            border-radius: 12px;
            font-weight: 600;
            color: #fff;
            width: 100%;
            margin-top: 25px;
            box-shadow: 0 4px 15px rgba(67, 97, 238, 0.3);
            transition: all 0.3s;
        }
        .verify-btn:hover {
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
    <div class="verify-card animate__animated animate__fadeInUp">
        <div class="verify-header">
            <div class="mb-3">
                <i class="bi bi-shield-fill-check fs-1 text-primary"></i>
            </div>
            <h2>Xác thực tài khoản</h2>
            <p>Mã OTP kích hoạt gồm 6 chữ số đã được gửi tới địa chỉ Email đã đăng ký cho tài khoản <strong>${username}</strong>.</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-custom">
                <i class="bi bi-exclamation-circle me-2"></i> ${error}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/verify" method="post">
            <input type="hidden" name="username" value="${username}" />

            <div class="mb-3">
                <label class="form-label">Nhập mã xác thực OTP</label>
                <input type="text" name="otp" class="form-control otp-input" maxlength="6" placeholder="******" required autocomplete="off" pattern="\d{6}" title="Mã OTP phải chứa 6 chữ số">
            </div>

            <button type="submit" class="verify-btn">KÍCH HOẠT TÀI KHOẢN</button>
        </form>

        <div class="mt-4 text-center d-flex flex-column gap-2">
            <div>
                <span class="text-muted small">Không nhận được mã? </span>
                <a href="${pageContext.request.contextPath}/register" class="text-decoration-none small fw-semibold text-primary">Đăng ký gửi lại</a>
            </div>
            <span class="text-muted small">Thời hạn hiệu lực mã là 5 phút.</span>
        </div>
    </div>
</body>
</html>

