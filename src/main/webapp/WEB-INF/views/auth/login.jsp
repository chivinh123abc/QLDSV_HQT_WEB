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
            --primary-gradient: linear-gradient(135deg, #1e3c72, #2a5298);
            --bg-light: #f8f9fa;
        }
        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #eef2f3 0%, #8e9eab 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0;
            padding: 20px;
        }
        .login-card {
            width: 100%;
            max-width: 440px;
            background: #fff;
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.1);
            padding: 40px;
            border: 1px solid rgba(0,0,0,0.05);
        }
        .login-header {
            text-align: center;
            margin-bottom: 30px;
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
            border-color: #2a5298;
            box-shadow: 0 0 0 4px rgba(42, 82, 152, 0.1);
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
            box-shadow: 0 4px 15px rgba(42, 82, 152, 0.3);
            transition: all 0.3s;
        }
        .login-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(42, 82, 152, 0.4);
        }
        .alert-custom {
            border-radius: 12px;
            padding: 12px;
            font-size: 0.85rem;
            font-weight: 500;
            margin-bottom: 20px;
        }
        .input-group-text {
            border-top-left-radius: 12px;
            border-bottom-left-radius: 12px;
            border-color: #e2e8f0;
        }
        .toggle-btn {
            border-top-right-radius: 12px;
            border-bottom-right-radius: 12px;
            border-color: #e2e8f0;
            background-color: #f8fafc;
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

        <%-- Alerts --%>
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-custom animate__animated animate__shakeX">
                <i class="bi bi-exclamation-circle me-2"></i> ${error}
            </div>
        </c:if>

        <c:if test="${not empty errorMsg}">
            <div class="alert alert-danger alert-custom text-center animate__animated animate__shakeX" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i> ${errorMsg}
                <c:if test="${errorMsg.contains('chưa được kích hoạt') || errorMsg.contains('chưa kích hoạt')}">
                    <div class="mt-2">
                        <a href="<c:url value='/auth/activate'/>" class="alert-link fw-bold text-decoration-underline text-danger">
                            <s:message code="auth.login.activate.here"/>
                        </a>
                    </div>
                </c:if>
            </div>
        </c:if>

        <c:if test="${not empty success}">
            <div class="alert alert-success alert-custom animate__animated animate__fadeIn">
                <i class="bi bi-check-circle me-2"></i> ${success}
            </div>
        </c:if>

        <c:if test="${not empty successMsg}">
            <div class="alert alert-success alert-custom animate__animated animate__fadeIn">
                <i class="bi bi-check-circle me-2"></i> ${successMsg}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <input type="hidden" name="csrf_token" value="${csrfToken}" />

            <div class="mb-3">
                <label class="form-label"><s:message code="auth.login.username"/></label>
                <div class="input-group">
                    <span class="input-group-text bg-light"><i class="bi bi-person text-muted"></i></span>
                    <input type="text" name="username" class="form-control" placeholder="<s:message code="auth.login.enter.id"/>" value="${username}" required autofocus>
                </div>
            </div>

            <div class="mb-4">
                <label class="form-label"><s:message code="auth.login.password"/></label>
                <div class="input-group">
                    <span class="input-group-text bg-light border-end-0"><i class="bi bi-lock text-muted"></i></span>
                    <input type="password" name="password" id="password" class="form-control border-start-0 border-end-0" placeholder="<s:message code="auth.login.enter.password"/>" required>
                    <button class="btn btn-outline-secondary toggle-btn border-start-0" type="button" id="togglePasswordBtn">
                        <i class="bi bi-eye-slash text-muted" id="togglePasswordIcon"></i>
                    </button>
                </div>
            </div>

            <button type="submit" class="login-btn"><s:message code="global.btn.login"/></button>
        </form>

        <hr class="text-muted my-4">

        <div class="text-center mt-2">
            <p class="mb-2 text-muted" style="font-size: 0.9rem;"><s:message code="auth.login.new.student"/></p>
            <a href="<c:url value='/auth/activate'/>" class="btn btn-outline-success btn-sm w-100 fw-bold mb-3 shadow-sm" style="border-radius: 12px; padding: 10px;">
                <i class="bi bi-person-check-fill"></i> <s:message code="auth.login.activate.btn"/>
            </a>
            
            <a href="#" class="text-decoration-none small fw-semibold text-muted">
                <s:message code="auth.login.forgot.password"/>
            </a>
        </div>
    </div>

    <script>
        // Toggle password visibility
        document.getElementById('togglePasswordBtn')?.addEventListener('click', function() {
            const passwordInput = document.getElementById('password');
            const icon = document.getElementById('togglePasswordIcon');
            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                icon.classList.remove('bi-eye-slash');
                icon.classList.add('bi-eye');
            } else {
                passwordInput.type = 'password';
                icon.classList.remove('bi-eye');
                icon.classList.add('bi-eye-slash');
            }
        });
    </script>
</body>
</html>
