<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="refresh" content="3;url=${pageContext.request.contextPath}/registration/processing?maLTC=${maLTC}">
    <title>Đang xử lý đăng ký môn học...</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
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
        .processing-card {
            background: rgba(255, 255, 255, 0.85);
            backdrop-filter: blur(20px);
            -webkit-backdrop-filter: blur(20px);
            border: 1px solid rgba(255, 255, 255, 0.4);
            padding: 50px 40px;
            border-radius: 24px;
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.05);
            text-align: center;
            max-width: 480px;
            width: 100%;
        }
        .spinner-container {
            position: relative;
            width: 80px;
            height: 80px;
            margin: 0 auto 30px;
        }
        .spinner-ring {
            width: 100%;
            height: 100%;
            border: 6px solid #e2e8f0;
            border-top-color: #3b82f6;
            border-radius: 50%;
            animation: spin 1s linear infinite;
        }
        h1 {
            color: #1e293b;
            font-size: 22px;
            font-weight: 700;
            margin-bottom: 12px;
        }
        p {
            color: #64748b;
            font-size: 15px;
            line-height: 1.6;
            margin-bottom: 0;
        }
        .info-badge {
            display: inline-block;
            background: #eff6ff;
            color: #1d4ed8;
            padding: 6px 16px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: 600;
            margin-top: 20px;
        }
        @keyframes spin {
            to { transform: rotate(360deg); }
        }
    </style>
</head>
<body>
    <div class="container d-flex justify-content-center">
        <div class="processing-card">
            <div class="spinner-container">
                <div class="spinner-ring"></div>
            </div>
            <h1>Đang xử lý đăng ký...</h1>
            <p>Hệ thống đang xếp hàng và xử lý yêu cầu đăng ký tín chỉ của bạn. Vui lòng giữ nguyên màn hình, kết quả sẽ hiển thị trong giây lát.</p>
            <div class="info-badge">
                Mã lớp: ${maLTC} | Mã SV: ${maSV}
            </div>
        </div>
    </div>
</body>
</html>
