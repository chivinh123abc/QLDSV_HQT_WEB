<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title><s:message code="subject.management.title"/></title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <style>
        .badge-soft-primary {
            background-color: #e0f2fe;
            color: #0369a1;
            border-radius: 20px;
            font-weight: 600;
            padding: 6px 12px;
        }

        .badge-soft-secondary {
            background-color: #f1f5f9;
            color: #475569;
            border-radius: 20px;
            padding: 6px 12px;
            font-weight: 500;
        }

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

        .table-custom tbody tr {
            cursor: pointer;
            transition: all 0.2s;
        }

        .table-custom tbody tr:hover {
            background-color: #f0f7ff !important;
        }

        .subject-title {
            font-weight: 700;
            color: #1e293b;
            margin-bottom: 2px;
        }

        .subject-subtitle {
            font-size: 0.8rem;
            color: #94a3b8;
            display: flex;
            align-items: center;
            gap: 4px;
        }

        .modal-header-custom {
            background-color: #4361ee;
            color: white;
            border-bottom: 0;
        }

        .modal-header-custom .btn-close {
            filter: invert(1) grayscale(100%) brightness(200%);
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

                    <div class="d-flex align-items-center gap-2 mb-4">
                        <i class="bi bi-journal-bookmark-fill text-primary fs-3"></i>
                        <h3 class="mb-0 fw-bold text-dark"><s:message code="subject.management"/></h3>
                    </div>

                    <!-- FLASH MESSAGES -->
                    <c:if test="${not empty message}">
                        <div class="alert alert-success alert-dismissible fade show rounded-3 shadow-sm border-0 mb-4" role="alert">
                            <i class="bi bi-check-circle-fill me-2"></i> ${message}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show rounded-3 shadow-sm border-0 mb-4" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i> ${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
                        <div class="card-header bg-white border-bottom-0 pt-4 px-4 pb-0 d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="fw-bold text-primary text-uppercase small mb-1"><s:message code="subject.directory"/></h6>
                                <p class="text-muted small mb-0"><s:message code="subject.management.desc"/></p>
                            </div>
                            <c:if test="${sessionScope.role == 'PGV'}">
                                <a href="${pageContext.request.contextPath}/admin/subject?lnkAdd=true" class="btn btn-primary btn-sm rounded-3 px-3 fw-bold shadow-sm">
                                    <i class="bi bi-plus-circle-fill me-1"></i> <s:message code="subject.btn.add"/>
                                </a>
                            </c:if>
                        </div>

                        <div class="card-body px-4 pb-4">
                            <!-- SEARCH TOOLBAR -->
                            <div class="d-flex justify-content-between align-items-center mb-4 pb-3 border-bottom">
                                <div class="input-group" style="max-width: 300px;">
                                    <span class="input-group-text bg-light border-0"><i class="bi bi-search text-muted"></i></span>
                                    <input type="text" id="search-input" class="form-control bg-light border-0 small" placeholder="<s:message code="subject.search"/>" onkeyup="filterLocal()">
                                </div>
                                <div class="text-muted small">
                                    <i class="bi bi-info-circle me-1"></i> <s:message code="subject.total"/> <strong id="subject-count">${monHocList.size()}</strong> <s:message code="subject.count.suffix"/>
                                </div>
                            </div>

                            <!-- TABLE -->
                            <div class="table-responsive rounded-3 border">
                                <table class="table table-custom align-middle mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th class="px-3"><s:message code="subject.lbl.code"/></th>
                                            <th><s:message code="subject.lbl.name"/></th>
                                            <th class="text-center"><s:message code="subject.lbl.theoryHours"/></th>
                                            <th class="text-center"><s:message code="subject.lbl.practiceHours"/></th>
                                            <c:if test="${sessionScope.role == 'PGV'}">
                                                <th class="text-center"><s:message code="global.lbl.actions"/></th>
                                            </c:if>
                                        </tr>
                                    </thead>
                                    <tbody id="subject-table-body">
                                        <c:forEach var="item" items="${monHocList}">
                                            <tr>
                                                <td class="px-3">
                                                    <span class="badge-soft-primary">${item.maMH}</span>
                                                </td>
                                                <td>
                                                    <div class="subject-title">${item.tenMH}</div>
                                                    <div class="subject-subtitle">
                                                        <i class="bi bi-info-circle"></i> <s:message code="global.lbl.standardized"/>
                                                    </div>
                                                </td>
                                                <td class="text-center">
                                                    <span class="badge-soft-secondary">${item.soTietLT} <s:message code="global.lbl.hours"/></span>
                                                </td>
                                                <td class="text-center">
                                                    <span class="badge-soft-secondary">${item.soTietTH} <s:message code="global.lbl.hours"/></span>
                                                </td>
                                                <c:if test="${sessionScope.role == 'PGV'}">
                                                    <td class="text-center">
                                                        <div class="d-flex gap-2 justify-content-center">
                                                            <a href="${pageContext.request.contextPath}/admin/subject?maMH=${item.maMH}&lnkEdit" class="btn btn-sm btn-outline-primary border-0 rounded-3">
                                                                <i class="bi bi-pencil-square"></i>
                                                            </a>
                                                            <form action="${pageContext.request.contextPath}/admin/subject" method="POST" onsubmit="return confirm('<s:message code="subject.js.confirmDelete"/>');" class="d-inline">
                                                                <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                                                <input type="hidden" name="maMH" value="${item.maMH}">
                                                                <button type="submit" name="btnDelete" class="btn btn-sm btn-outline-danger border-0 rounded-3 ${!item.canDelete ? 'disabled opacity-25' : ''}" <c:if test="${!item.canDelete}">disabled title="<s:message code='subject.cannot.delete'/>"</c:if>>
                                                                    <i class="bi bi-trash3"></i>
                                                                </button>
                                                            </form>
                                                        </div>
                                                    </td>
                                                </c:if>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty monHocList}">
                                            <tr>
                                                <td colspan="5" class="text-center py-5 text-muted">
                                                    <i class="bi bi-inbox fs-1 d-block mb-3 opacity-25"></i>
                                                    <s:message code="subject.no.data"/>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <!-- SUBJECT MODAL (SSR) -->
    <c:if test="${sessionScope.role == 'PGV' && (not empty mode || not empty param.lnkAdd)}">
        <div class="modal fade show d-block" id="subjectModal" tabindex="-1" style="background: rgba(0,0,0,0.5);">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg rounded-4">
                    <div class="modal-header bg-primary text-white border-0 py-3 px-4 rounded-top-4">
                        <h5 class="modal-title fw-bold d-flex align-items-center gap-2">
                            <i class="bi bi-journal-plus"></i>
                            <c:choose>
                                <c:when test="${mode == 'edit'}"><s:message code="subject.update"/></c:when>
                                <c:otherwise><s:message code="subject.add.new"/></c:otherwise>
                            </c:choose>
                        </h5>
                        <a href="${pageContext.request.contextPath}/admin/subject" class="btn-close btn-close-white text-decoration-none"></a>
                    </div>
                    <form:form action="${pageContext.request.contextPath}/admin/subject" method="POST" modelAttribute="monHoc">
                        <input type="hidden" name="csrf_token" value="${csrfToken}" />
                        <div class="modal-body p-4">
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label small fw-bold text-muted"><s:message code="subject.lbl.code"/> <span class="text-danger">*</span></label>
                                    <form:input path="maMH" cssClass="form-control rounded-3" placeholder="VD: CTDL" readonly="${mode == 'edit'}"/>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label small fw-bold text-muted"><s:message code="subject.lbl.name"/> <span class="text-danger">*</span></label>
                                    <form:input path="tenMH" cssClass="form-control rounded-3" placeholder="VD: Cấu trúc dữ liệu"/>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label small fw-bold text-muted"><s:message code="subject.lbl.theoryHours"/> <span class="text-danger">*</span></label>
                                    <form:input path="soTietLT" type="number" cssClass="form-control rounded-3" placeholder="VD: 30"/>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label small fw-bold text-muted"><s:message code="subject.lbl.practiceHours"/> <span class="text-danger">*</span></label>
                                    <form:input path="soTietTH" type="number" cssClass="form-control rounded-3" placeholder="VD: 15"/>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer border-0 px-4 pb-4">
                            <a href="${pageContext.request.contextPath}/admin/subject" class="btn btn-light rounded-3 fw-bold"><s:message code="global.btn.cancel"/></a>
                            <c:choose>
                                <c:when test="${mode == 'edit'}">
                                    <button type="submit" name="btnUpdate" class="btn btn-primary rounded-3 fw-bold px-4">GHI (<s:message code="global.btn.edit"/>)</button>
                                </c:when>
                                <c:otherwise>
                                    <button type="submit" name="btnInsert" class="btn btn-success rounded-3 fw-bold px-4">GHI (<s:message code="global.btn.add"/>)</button>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </c:if>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function normalizeVN(str) {
            return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/\u0111/g, 'd').replace(/\u0110/g, 'D').toLowerCase();
        }

        function filterLocal() {
            const val = normalizeVN(document.getElementById('search-input').value);
            document.querySelectorAll('#subject-table-body tr').forEach(row => {
                row.style.display = normalizeVN(row.innerText).includes(val) ? '' : 'none';
            });
        }
    </script>
</body>

</html>
