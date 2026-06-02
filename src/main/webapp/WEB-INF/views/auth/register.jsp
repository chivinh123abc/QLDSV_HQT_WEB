<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Đăng ký tài khoản - Hệ thống Tín chỉ</title>
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
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0;
            padding: 20px;
        }
        .register-card {
            width: 100%;
            max-width: 460px;
            background: #fff;
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.1);
            padding: 40px;
            border: 1px solid rgba(0,0,0,0.05);
        }
        .register-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .register-header h2 {
            font-weight: 700;
            background: var(--primary-gradient);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            margin-bottom: 10px;
        }
        .register-header p {
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
            padding: 10px 15px;
            border: 1px solid #e2e8f0;
            background-color: #f8fafc;
            transition: all 0.2s;
        }
        .form-control:focus {
            background-color: #fff;
            border-color: #4361ee;
            box-shadow: 0 0 0 4px rgba(67, 97, 238, 0.1);
        }
        .register-btn {
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
        .register-btn:hover {
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
        .captcha-container {
            display: flex;
            gap: 10px;
            align-items: center;
        }
        .captcha-img-wrapper {
            position: relative;
            cursor: pointer;
        }
        .captcha-img-wrapper img {
            height: 44px;
            border-radius: 12px;
        }
        .refresh-btn {
            border-radius: 12px;
            height: 44px;
            width: 44px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
    </style>
</head>
<body>
    <div class="register-card animate__animated animate__fadeInUp">
        <div class="register-header">
            <div class="mb-3">
                <i class="bi bi-person-plus-fill fs-1 text-primary"></i>
            </div>
            <h2>Đăng ký tài khoản</h2>
            <p>Tạo tài khoản tự phục vụ dành cho Sinh viên & Giảng viên</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-custom">
                <i class="bi bi-exclamation-circle me-2"></i> ${error}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post">
            <div class="mb-3">
                <label class="form-label">Mã số người dùng (Mã SV / Mã GV)</label>
                <div class="input-group">
                    <span class="input-group-text bg-light border-end-0"><i class="bi bi-card-text text-muted"></i></span>
                    <input type="text" name="username" class="form-control border-start-0" placeholder="VD: SV01, GV01, N22DCCN001..." value="${username}" required>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label">Địa chỉ Email (Nhận mã kích hoạt OTP)</label>
                <div class="input-group">
                    <span class="input-group-text bg-light border-end-0"><i class="bi bi-envelope text-muted"></i></span>
                    <input type="email" name="email" class="form-control border-start-0" placeholder="Nhập email của bạn..." value="${email}" required>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label">Mật khẩu</label>
                <div class="input-group">
                    <span class="input-group-text bg-light border-end-0"><i class="bi bi-key text-muted"></i></span>
                    <input type="password" name="password" class="form-control border-start-0" placeholder="Nhập mật khẩu mới..." required>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label">Xác nhận mật khẩu</label>
                <div class="input-group">
                    <span class="input-group-text bg-light border-end-0"><i class="bi bi-key-fill text-muted"></i></span>
                    <input type="password" name="confirmPassword" class="form-control border-start-0" placeholder="Nhập lại mật khẩu..." required>
                </div>
            </div>

            <div class="mb-4">
                <label class="form-label">Mã xác nhận CAPTCHA</label>
                <div class="captcha-container">
                    <div class="captcha-img-wrapper" onclick="refreshCaptcha()">
                        <img id="captchaImg" src="${pageContext.request.contextPath}/captcha" alt="CAPTCHA" title="Click để đổi hình khác" />
                    </div>
                    <button type="button" class="btn btn-outline-secondary refresh-btn" onclick="refreshCaptcha()" title="Đổi mã CAPTCHA">
                        <i class="bi bi-arrow-clockwise"></i>
                    </button>
                    <input type="text" name="captcha" class="form-control" placeholder="Nhập mã..." maxlength="5" required autocomplete="off">
                </div>
            </div>

            <button type="submit" class="register-btn">ĐĂNG KÝ NGAY</button>
        </form>

        <div class="mt-4 text-center">
            <span class="text-muted small">Đã có tài khoản? </span>
            <a href="${pageContext.request.contextPath}/login" class="text-decoration-none small fw-semibold text-primary">Đăng nhập tại đây</a>
        </div>
    </div>

    <script>
        function refreshCaptcha() {
            var img = document.getElementById('captchaImg');
            img.src = '${pageContext.request.contextPath}/captcha?' + new Date().getTime();
        }
    </script>
</body>
</html>

