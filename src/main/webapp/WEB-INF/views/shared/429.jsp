<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>429 Too Many Requests</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <style>
        body {
            background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
            font-family: 'Inter', system-ui, -apple-system, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .error-card {
            background: rgba(255, 255, 255, 0.85);
            backdrop-filter: blur(20px);
            -webkit-backdrop-filter: blur(20px);
            border: 1px solid rgba(255, 255, 255, 0.4);
            padding: 50px 40px;
            border-radius: 24px;
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.05);
            text-align: center;
            max-width: 520px;
            width: 100%;
        }
        .icon-box {
            width: 90px;
            height: 90px;
            background: #fee2e2;
            color: #ef4444;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 30px;
            font-size: 42px;
            animation: pulse 2s infinite;
        }
        h1 {
            color: #1e293b;
            font-size: 32px;
            font-weight: 800;
            margin-bottom: 12px;
        }
        p {
            color: #64748b;
            font-size: 16px;
            line-height: 1.6;
            margin-bottom: 30px;
        }
        .btn-retry {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 12px 28px;
            background-color: #3b82f6;
            color: white;
            text-decoration: none;
            font-weight: 600;
            border-radius: 12px;
            transition: all 0.2s ease;
            box-shadow: 0 4px 12px rgba(59, 130, 246, 0.25);
        }
        .btn-retry:hover {
            background-color: #2563eb;
            transform: translateY(-1px);
            box-shadow: 0 6px 16px rgba(59, 130, 246, 0.35);
        }
        @keyframes pulse {
            0% { transform: scale(1); }
            50% { transform: scale(1.05); }
            100% { transform: scale(1); }
        }
    </style>
</head>
<body>
    <div class="container d-flex justify-content-center">
        <div class="error-card">
            <div class="icon-box">
                <i class="bi bi-shield-slash-fill"></i>
            </div>
            <h1>Yêu cầu quá nhanh!</h1>
            <p>Hệ thống đã ghi nhận quá nhiều thao tác ghi từ bạn trong thời gian ngắn để phòng chống spam. Vui lòng dừng lại và chờ một chút trước khi thử lại.</p>
            <a href="${pageContext.request.contextPath}/" class="btn-retry">
                <i class="bi bi-house-door-fill"></i> Quay lại trang chủ
            </a>
        </div>
    </div>
</body>
</html>
