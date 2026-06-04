<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title><s:message code="${mode == 'edit' ? 'announcement.form.edit' : 'announcement.create'}"/> | QLDSV_HTC</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <!-- CKEditor CDN -->
    <script src="https://cdn.ckeditor.com/4.16.2/full/ckeditor.js"></script>
    <style>
        .form-card {
            border: none;
            border-radius: 24px;
            background: rgba(255, 255, 255, 0.85);
            backdrop-filter: blur(15px);
            -webkit-backdrop-filter: blur(15px);
            border: 1px solid rgba(255, 255, 255, 0.4);
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.05);
            padding: 35px;
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
                    <!-- Điều hướng quay lại -->
                    <div class="mb-3">
                        <a href="${pageContext.request.contextPath}/admin/announcement" class="text-decoration-none text-muted small">
                            <i class="bi bi-arrow-left"></i> Quay lại danh sách quản lý
                        </a>
                    </div>

                    <!-- Tiêu đề trang -->
                    <div class="mb-4">
                        <h2 class="fw-bold text-dark mb-1">
                            <c:choose>
                                <c:when test="${mode == 'edit'}">✏️ <s:message code="announcement.form.edit"/></c:when>
                                <c:otherwise>📢 <s:message code="announcement.create"/></c:otherwise>
                            </c:choose>
                        </h2>
                        <p class="text-muted mb-0">
                            <s:message code="${mode == 'edit' ? 'announcement.form.editDesc' : 'announcement.form.createDesc'}"/>
                        </p>
                    </div>

                    <!-- Báo lỗi -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger border-0 rounded-3 shadow-sm mb-4" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i> ${error}
                        </div>
                    </c:if>

                    <!-- Form soạn thảo -->
                    <div class="form-card">
                        <form action="${pageContext.request.contextPath}/admin/announcement/save" method="POST" id="announcementForm">
                            <input type="hidden" name="csrf_token" value="${csrfToken}" />
                            <input type="hidden" name="mode" value="${mode}" />
                            <input type="hidden" name="id" value="${announcementDto.id}" />
                            <input type="hidden" name="version" value="${announcementDto.version}" />

                            <div class="mb-4">
                                <label for="tieuDe" class="form-label fw-bold text-dark">Tiêu đề <span class="text-danger">*</span></label>
                                <input type="text" class="form-control rounded-3" id="tieuDe" name="tieuDe" 
                                       value="${announcementDto.tieuDe}" placeholder="Nhập tiêu đề thông báo" required />
                            </div>

                            <div class="mb-4">
                                <label for="noiDung" class="form-label fw-bold text-dark">Nội dung <span class="text-danger">*</span></label>
                                <textarea id="noiDung" name="noiDung" rows="10" required>${announcementDto.noiDung}</textarea>
                            </div>

                            <div class="d-flex justify-content-end gap-3 mt-4">
                                <a href="${pageContext.request.contextPath}/admin/announcement" class="btn btn-light px-4 py-2 rounded-pill fw-bold">
                                    Hủy
                                </a>
                                <button type="submit" class="btn btn-primary px-4 py-2 rounded-pill fw-bold shadow-sm">
                                    <i class="bi bi-send me-1"></i> <s:message code="${mode == 'edit' ? 'announcement.form.btn.update' : 'announcement.form.btn.publishRealtime'}"/>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Khởi tạo CKEditor -->
    <script>
        CKEDITOR.replace('noiDung', {
            filebrowserBrowseUrl: '${pageContext.request.contextPath}/ckfinder/ckfinder.html',
            filebrowserImageBrowseUrl: '${pageContext.request.contextPath}/ckfinder/ckfinder.html?type=Images',
            height: '400px'
        });
    </script>
</body>
</html>
