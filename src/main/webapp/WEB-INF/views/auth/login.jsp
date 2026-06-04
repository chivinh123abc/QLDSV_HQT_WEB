<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title><s:message code="auth.login.page.title"/></title>
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
            <h2><s:message code="auth.login.credit.system"/></h2>
            <p><s:message code="auth.login.please.login"/></p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-custom">
                <i class="bi bi-exclamation-circle me-2"></i> ${error}
            </div>
        </c:if>

        <c:if test="${not empty success}">
            <div class="alert alert-success alert-custom">
                <i class="bi bi-check-circle me-2"></i> ${success}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <input type="hidden" name="csrf_token" value="${csrfToken}" />

            <div class="mb-3">
                <label class="form-label"><s:message code="auth.login.username"/></label>
                <input type="text" name="username" class="form-control" placeholder="<s:message code="auth.login.enter.id"/>" value="${username}" required>
            </div>

            <div class="mb-4">
                <label class="form-label"><s:message code="auth.login.password"/></label>
                <div class="input-group">
                    <input type="password" name="password" id="password" class="form-control" style="border-top-right-radius: 0; border-bottom-right-radius: 0;" placeholder="<s:message code="auth.login.enter.password"/>" required>
                    <button class="btn btn-outline-secondary toggle-password" style="border-top-right-radius: 12px; border-bottom-right-radius: 12px; border-color: #e2e8f0; background-color: #f8fafc;" type="button" onclick="togglePassVisibility()">
                        <i id="eye-icon" class="bi bi-eye"></i>
                    </button>
                </div>
            </div>

            <button type="submit" class="login-btn"><s:message code="global.btn.login"/></button>
        </form>

        <div class="mt-4 text-center">
            <span class="text-muted small"><s:message code="auth.login.forgot.password"/></span>
        </div>
    </div>

    <script>
        function togglePassVisibility() {
            const input = document.getElementById('password');
            const icon = document.getElementById('eye-icon');
            if (input.type === 'password') {
                input.type = 'text';
                icon.classList.remove('bi-eye');
                icon.classList.add('bi-eye-slash');
            } else {
                input.type = 'password';
                icon.classList.remove('bi-eye-slash');
                icon.classList.add('bi-eye');
            }
        }
    </script>
</body>
</html>

