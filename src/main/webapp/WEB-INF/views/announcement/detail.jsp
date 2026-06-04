<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>${announcement.tieuDe} | QLDSV_HTC</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <style>
        .announcement-detail-card {
            border: none;
            border-radius: 20px;
            background: rgba(255, 255, 255, 0.85);
            backdrop-filter: blur(10px);
            -webkit-backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.25);
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.03);
            overflow: hidden;
            margin-bottom: 25px;
        }
        .detail-header {
            padding: 30px 40px 20px;
            border-bottom: 1px dashed #cbd5e1;
        }
        .detail-body {
            padding: 40px;
            color: #334155;
            font-size: 16px;
            line-height: 1.8;
        }
        .meta-item {
            font-size: 0.85rem;
            color: #64748b;
            display: inline-flex;
            align-items: center;
            gap: 6px;
        }
        .action-btn {
            border-radius: 10px;
            padding: 8px 16px;
            font-size: 0.9rem;
            font-weight: 600;
            transition: all 0.2s;
        }
    </style>
</head>
<body>
    <div class="app-layout">
        <!-- SIDEBAR -->
        <jsp:include page="/WEB-INF/views/shared/sidebar.jsp" />

        <div class="app-main">
            <!-- HEADER -->
            <jsp:include page="/WEB-INF/views/shared/header.jsp" />

            <!-- MAIN CONTENT -->
            <main id="main-content" class="app-content p-4 bg-light">
                <div class="container-fluid max-w-5xl mx-auto">
                    <!-- Nút quay lại -->
                    <div class="mb-4">
                        <a href="${pageContext.request.contextPath}/announcements" class="btn btn-outline-secondary rounded-pill px-4 fw-bold action-btn">
                            <i class="bi bi-arrow-left me-2"></i> <s:message code="announcement.backToList"/>
                        </a>
                    </div>

                    <!-- Flash messages -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show border-0 rounded-3 shadow-sm mb-4" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i> ${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <!-- Chi tiết thông báo -->
                    <div class="card announcement-detail-card">
                        <div class="detail-header d-flex justify-content-between align-items-start gap-4">
                            <div>
                                <h1 class="fw-bold text-dark mb-3" style="font-size: 1.75rem; line-height: 1.3;">
                                    ${announcement.tieuDe}
                                </h1>
                                <div class="d-flex flex-wrap align-items-center gap-4">
                                    <span class="meta-item">
                                        <i class="bi bi-person-circle fs-6"></i> <s:message code="announcement.postedBy"/> <strong>${announcement.nguoiTao.tenDangNhap}</strong>
                                    </span>
                                    <span class="meta-item">
                                        <i class="bi bi-calendar3 fs-6"></i> <s:message code="announcement.postedAt"/> <strong>${announcement.ngayTaoFormatted}</strong>
                                    </span>
                                </div>
                            </div>
                            
                        </div>
                        
                        <div class="detail-body">
                            <c:out value="${announcement.noiDung}" escapeXml="false" />
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>