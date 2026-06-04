<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title><s:message code="announcement.title"/> (Admin) | QLDSV_HTC</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <style>
        .table-custom th {
            font-size: 0.85rem;
            color: #64748b;
            font-weight: 700;
            text-transform: uppercase;
            border-bottom: 2px solid #e2e8f0;
        }
        .table-custom td {
            vertical-align: middle;
            padding: 1rem 0.75rem;
            border-bottom: 1px solid #f1f5f9;
        }
        .table-custom tbody tr:hover {
            background-color: #f8fafc;
        }
        .action-btn {
            border-radius: 10px;
            padding: 6px 14px;
            font-size: 0.85rem;
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
                <div class="container-fluid max-w-7xl mx-auto">
                    <!-- Tiêu đề trang -->
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <div>
                            <h2 class="fw-bold text-dark mb-1">📢 <s:message code="announcement.title"/> (Admin)</h2>
                            <p class="text-muted mb-0">Quản lý thông báo realtime hệ thống.</p>
                        </div>
                        <a href="${pageContext.request.contextPath}/admin/announcement/create" class="btn btn-primary px-4 py-2 rounded-pill fw-bold shadow-sm">
                            <i class="bi bi-plus-lg me-2"></i> <s:message code="announcement.create"/>
                        </a>
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

                    <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
                        <div class="card-body p-4">
                            <div class="table-responsive rounded-3 border">
                                <table class="table table-custom align-middle mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th class="px-3" style="width: 45%;">Tiêu đề</th>
                                            <th style="width: 20%;">Người tạo</th>
                                            <th style="width: 20%;">Ngày tạo</th>
                                            <th class="text-center" style="width: 15%;">Hành động</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <s:message code="announcement.confirmDelete" var="confirmDeleteMsg" />
                                        <c:choose>
                                            <c:when test="${not empty announcements}">
                                                <c:forEach var="tb" items="${announcements}">
                                                    <tr>
                                                        <td class="px-3">
                                                            <div class="fw-bold text-dark">${tb.tieuDe}</div>
                                                        </td>
                                                        <td>
                                                            <div class="text-slate-600">${tb.nguoiTao.tenDangNhap}</div>
                                                        </td>
                                                        <td>
                                                            <div class="text-muted small">${tb.ngayTaoFormatted}</div>
                                                        </td>
                                                        <td class="text-center">
                                                            <div class="d-flex justify-content-center gap-2">
                                                                <a href="${pageContext.request.contextPath}/admin/announcement/edit?id=${tb.id}" class="btn btn-outline-primary action-btn">
                                                                    <i class="bi bi-pencil"></i> <s:message code="announcement.edit"/>
                                                                </a>
                                                                <form action="${pageContext.request.contextPath}/admin/announcement/delete" method="POST" onsubmit="return confirm('${confirmDeleteMsg}');" class="m-0">
                                                                    <input type="hidden" name="id" value="${tb.id}" />
                                                                    <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                                                    <button type="submit" class="btn btn-outline-danger action-btn">
                                                                        <i class="bi bi-trash"></i> <s:message code="announcement.delete"/>
                                                                    </button>
                                                                </form>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <tr>
                                                    <td colspan="4" class="text-center py-5 text-muted">
                                                        <i class="bi bi-inbox fs-1 d-block mb-3 opacity-25"></i>
                                                        <s:message code="announcement.empty"/>
                                                    </td>
                                                </tr>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>
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
