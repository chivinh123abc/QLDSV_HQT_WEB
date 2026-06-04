<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title><s:message code="announcement.title"/> | QLDSV_HTC</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <style>
        .announcement-card {
            border: none;
            border-radius: 20px;
            background: rgba(255, 255, 255, 0.8);
            backdrop-filter: blur(10px);
            -webkit-backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.25);
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.02);
            overflow: hidden;
            margin-bottom: 25px;
        }
        .announcement-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.06);
            border-color: rgba(67, 97, 238, 0.3);
        }
        .announcement-header {
            padding: 24px 28px 12px;
            border-bottom: 1px dashed #e2e8f0;
        }
        .announcement-body {
            padding: 20px 28px 24px;
            color: #475569;
            font-size: 15px;
            line-height: 1.6;
        }
        .announcement-title {
            color: #0f172a;
            font-weight: 700;
            font-size: 1.25rem;
            margin-bottom: 10px;
        }
        .meta-item {
            font-size: 0.8rem;
            color: #64748b;
            display: inline-flex;
            align-items: center;
            gap: 6px;
        }
        .action-btn {
            border-radius: 10px;
            padding: 6px 14px;
            font-size: 0.85rem;
            font-weight: 600;
            transition: all 0.2s;
        }
        .empty-state {
            text-align: center;
            padding: 80px 40px;
            background: rgba(255, 255, 255, 0.5);
            border-radius: 24px;
            border: 2px dashed #cbd5e1;
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
                    <!-- Tiêu đề trang -->
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <div>
                            <h2 class="fw-bold text-dark mb-1">📢 <s:message code="announcement.title"/></h2>
                            <p class="text-muted mb-0"><s:message code="announcement.desc"/></p>
                        </div>

                    </div>

                    <!-- Flash messages -->
                    <c:if test="${not empty message}">
                        <div class="alert alert-success alert-dismissible fade show border-0 rounded-3 shadow-sm mb-4" role="alert">
                            <i class="bi bi-check-circle-fill me-2"></i> ${message}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show border-0 rounded-3 shadow-sm mb-4" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i> ${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <!-- Danh sách thông báo -->
                    <div class="announcements-list">
                        <c:choose>
                            <c:when test="${not empty announcements}">
                                 <c:forEach var="tb" items="${announcements}">
                                     <c:set var="isRead" value="${not empty readMap && readMap[tb.id] == true}" />
                                     <c:set var="cardClass" value="${isRead ? 'card announcement-card border-start border-secondary border-3' : 'card announcement-card border-start border-primary border-4'}" />
                                     <div class="${cardClass}">
                                         <div class="announcement-header d-flex justify-content-between align-items-start gap-3">
                                             <div>
                                                 <c:set var="titleLinkClass" value="${isRead ? 'text-decoration-none fw-normal text-muted' : 'text-decoration-none fw-bold text-dark'}" />
                                                 <h3 class="announcement-title d-flex align-items-center gap-2 mb-2">
                                                     <a href="${pageContext.request.contextPath}/announcements/detail/${tb.id}" class="${titleLinkClass}">
                                                         ${tb.tieuDe}
                                                     </a>
                                                     <c:if test="${!isRead}">
                                                         <span class="badge bg-primary text-white rounded-pill px-2 py-1" style="font-size: 0.65rem; font-weight: 700;">
                                                             MỚI
                                                         </span>
                                                     </c:if>
                                                 </h3>
                                                 <div class="d-flex flex-wrap align-items-center gap-3">
                                                     <span class="meta-item">
                                                         <i class="bi bi-person"></i> <s:message code="announcement.postedBy"/> <strong>${tb.nguoiTao.tenDangNhap}</strong>
                                                     </span>
                                                     <span class="meta-item">
                                                         <i class="bi bi-calendar3"></i> <s:message code="announcement.postedAt"/> <strong>${tb.ngayTaoFormatted}</strong>
                                                     </span>
                                                     <c:if test="${isRead}">
                                                         <span class="meta-item text-success">
                                                             <i class="bi bi-check2-all"></i> <span style="font-size: 0.8rem; font-weight: 600;">Đã đọc</span>
                                                         </span>
                                                     </c:if>
                                                 </div>
                                             </div>
                                         </div>
                                         <div class="announcement-body">
                                             ${tb.noiDung}
                                         </div>
                                     </div>
                                 </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-state">
                                    <i class="bi bi-bell-slash fs-1 text-muted opacity-50 d-block mb-3"></i>
                                    <h4 class="fw-bold text-slate-800"><s:message code="announcement.empty"/></h4>
                                    <p class="text-slate-600 mb-0"><s:message code="announcement.empty.desc"/></p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
