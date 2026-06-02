<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="utf-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title><s:message code="classroom.management.title"/></title>
            <!-- Bootstrap CSS -->
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
            <!-- Bootstrap Icons -->
            <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css"
                rel="stylesheet" />
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

                .table-custom tr:hover {
                    background-color: #f8fafc;
                }

                .class-title {
                    font-weight: 700;
                    color: #1e293b;
                    margin-bottom: 2px;
                }

                .class-subtitle {
                    font-size: 0.8rem;
                    color: #94a3b8;
                    display: flex;
                    align-items: center;
                    gap: 4px;
                }

                .table-custom tbody tr {
                    cursor: pointer;
                    transition: all 0.2s;
                }

                .table-custom tbody tr:hover {
                    background-color: #f0f7ff !important;
                }

                .modal-header-custom {
                    background-color: #4361ee;
                    color: white;
                    border-bottom: 0;
                }

                .modal-header-custom .btn-close {
                    filter: invert(1) grayscale(100%) brightness(200%);
                }

                .toolbar-btn {
                    font-weight: 600;
                    padding: 6px 12px;
                    display: inline-flex;
                    align-items: center;
                    gap: 6px;
                }

                .toolbar-btn:disabled {
                    opacity: 0.5;
                    cursor: not-allowed;
                }

                /* Premium Toolbar Colors (matching student page) */
                .btn-toolbar-add {
                    color: #10b981;
                    border-color: #10b981;
                }

                .btn-toolbar-add:hover,
                .btn-toolbar-add.active {
                    background-color: #10b981;
                    color: white;
                    box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
                }

                .btn-toolbar-edit {
                    color: #3b82f6;
                    border-color: #3b82f6;
                }

                .btn-toolbar-edit:hover,
                .btn-toolbar-edit.active {
                    background-color: #3b82f6;
                    color: white;
                    box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
                }

                .btn-toolbar-delete {
                    color: #ef4444;
                    border-color: #ef4444;
                }

                .btn-toolbar-delete:hover,
                .btn-toolbar-delete.active {
                    background-color: #ef4444;
                    color: white;
                    box-shadow: 0 4px 12px rgba(239, 68, 68, 0.3);
                }

                .btn-toolbar-cancel {
                    color: #64748b;
                    border-color: #e2e8f0;
                }

                .btn-toolbar-cancel:hover {
                    background-color: #64748b;
                    color: white;
                }

                .btn-toolbar-disabled {
                    opacity: 0.4;
                    filter: grayscale(100%);
                    pointer-events: none;
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
                                <i class="bi bi-building-fill text-primary fs-3"></i>
                                <h3 class="mb-0 fw-bold text-dark"><s:message code="classroom.management"/></h3>
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
                                <div
                                    class="card-header bg-white border-bottom-0 pt-4 px-4 pb-0 d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="fw-bold text-primary text-uppercase small mb-1"><s:message code="classroom.directory"/></h6>
                                        <p class="text-muted small mb-0"><s:message code="classroom.management.desc"/>
                                        </p>
                                    </div>
                                    <c:if test="${sessionScope.role == 'PGV'}">
                                        <!-- GET link to open add modal -->
                                        <a href="${pageContext.request.contextPath}/class?maKhoa=${maKhoa}&lnkAdd=true" class="btn btn-primary btn-sm rounded-3 px-3 fw-bold shadow-sm">
                                            <i class="bi bi-plus-circle-fill me-1"></i> <s:message code="classroom.btn.add"/>
                                        </a>
                                    </c:if>
                                </div>

                                <div class="card-body px-4 pb-4">
                                    <!-- SEARCH & FILTER TOOLBAR -->
                                    <div
                                        class="d-flex justify-content-between align-items-center mb-4 pb-3 border-bottom">
                                        <div class="input-group" style="max-width: 300px;">
                                            <span class="input-group-text bg-light border-0"><i
                                                    class="bi bi-search text-muted"></i></span>
                                            <input type="text" id="search-input"
                                                class="form-control bg-light border-0 small"
                                                placeholder="<s:message code="classroom.search"/>" onkeyup="filterLocal()">
                                        </div>
                                        <div class="d-flex gap-3 align-items-center">
                                            <label class="fw-bold small text-muted text-uppercase mb-0"><s:message code="global.btn.filter"/>
                                                khoa:</label>
                                            <c:choose>
                                                <c:when test="${sessionScope.role == 'PGV'}">
                                                    <form action="${pageContext.request.contextPath}/class" method="GET" class="d-inline">
                                                        <select name="maKhoa"
                                                            class="form-select form-select-sm border-0 bg-light text-primary fw-bold"
                                                            style="min-width: 200px;" onchange="this.form.submit()">
                                                            <option value="all"><s:message code="classroom.all.faculties"/></option>
                                                            <c:forEach var="k" items="${khoaList}">
                                                                <option value="${k.maKhoa}" ${param.maKhoa == k.maKhoa || maKhoa == k.maKhoa ? 'selected' : ''}>
                                                                    ${k.tenKhoa}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </form>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="d-flex align-items-center gap-2 px-3 py-1 bg-light rounded-pill border">
                                                        <i class="bi bi-building text-primary small"></i>
                                                        <span class="fw-bold text-dark small">${khoaList[0].tenKhoa}</span>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <!-- TABLE -->
                                    <div class="table-responsive rounded-3 border">
                                        <table class="table table-custom align-middle mb-0">
                                            <thead class="table-light">
                                                <tr>
                                                    <th class="px-3"><s:message code="classroom.lbl.classCode"/></th>
                                                    <th><s:message code="classroom.lbl.className"/></th>
                                                    <th class="text-center"><s:message code="classroom.lbl.academicTerm"/></th>
                                                    <th class="text-center">KHOA</th>
                                                    <c:if test="${sessionScope.role == 'PGV'}">
                                                        <th class="text-center"><s:message code="global.lbl.actions"/></th>
                                                    </c:if>
                                                </tr>
                                            </thead>
                                            <tbody id="class-table-body">
                                                <c:forEach var="item" items="${lopList}">
                                                    <tr>
                                                        <td class="px-3">
                                                            <span class="badge bg-primary bg-opacity-10 text-primary fw-bold">${item.maLop}</span>
                                                        </td>
                                                        <td>
                                                            <div class="class-title">${item.tenLop}</div>
                                                            <div class="class-subtitle">
                                                                <i class="bi bi-info-circle"></i> <s:message code="global.lbl.regular"/>
                                                            </div>
                                                        </td>
                                                        <td class="text-center">
                                                            <span class="badge-soft-secondary">${item.khoaHoc}</span>
                                                        </td>
                                                        <td class="text-center">
                                                            <span
                                                                class="badge border border-info text-info rounded-pill px-3 py-1 fw-bold small">${item.maKhoa}</span>
                                                        </td>
                                                        <c:if test="${sessionScope.role == 'PGV'}">
                                                            <td class="text-center">
                                                                <div class="d-flex gap-2 justify-content-center">
                                                                    <!-- Edit Link GET -->
                                                                    <a href="${pageContext.request.contextPath}/class?maKhoa=${maKhoa}&maLop=${item.maLop}&lnkEdit" class="btn btn-sm btn-outline-primary border-0 rounded-3">
                                                                        <i class="bi bi-pencil-square"></i>
                                                                    </a>
                                                                    <!-- Delete Form POST -->
                                                                    <form action="${pageContext.request.contextPath}/class" method="POST" onsubmit="return confirm('Bạn có chắc chắn muốn xóa lớp này không?');" class="d-inline">
                                                                        <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                                                        <input type="hidden" name="maLop" value="${item.maLop}">
                                                                        <input type="hidden" name="maKhoa" value="${maKhoa}">
                                                                        <button type="submit" name="btnDelete" class="btn btn-sm btn-outline-danger border-0 rounded-3 ${!item.canDelete ? 'disabled opacity-25' : ''}" <c:if test="${!item.canDelete}">disabled title="<s:message code='classroom.cannot.delete'/>"</c:if>>
                                                                            <i class="bi bi-trash3"></i>
                                                                        </button>
                                                                    </form>
                                                                </div>
                                                            </td>
                                                        </c:if>
                                                    </tr>
                                                </c:forEach>
                                                <c:if test="${empty lopList}">
                                                    <tr>
                                                        <td colspan="${sessionScope.role == 'PGV' ? 5 : 4}" class="text-center py-5 text-muted">
                                                            <i class="bi bi-inbox fs-1 d-block mb-3 opacity-25"></i>
                                                            <s:message code="classroom.no.data"/>
                                                        </td>
                                                    </tr>
                                                </c:if>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="mt-3 text-muted small px-1">
                                        <i class="bi bi-info-circle me-1"></i> <s:message code="classroom.display"/> <strong
                                            id="class-count">${lopList.size()}</strong> <s:message code="classroom.count.suffix"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </main>
                </div>
            </div>

            <!-- CLASS MODAL -->
            <c:if test="${sessionScope.role == 'PGV' && (not empty mode || not empty param.lnkAdd)}">
                <div class="modal fade show d-block" id="classModal" tabindex="-1" style="background: rgba(0,0,0,0.5);">
                    <div class="modal-dialog modal-lg modal-dialog-centered">
                        <div class="modal-content border-0 shadow-lg rounded-4">
                            <div class="modal-header bg-primary text-white border-0 py-3 px-4 rounded-top-4">
                                <h5 class="modal-title fw-bold d-flex align-items-center gap-2">
                                    <i class="bi bi-mortarboard-fill"></i>
                                    <c:choose>
                                        <c:when test="${mode == 'edit'}"><s:message code="classroom.update"/></c:when>
                                        <c:otherwise><s:message code="classroom.add.new"/></c:otherwise>
                                    </c:choose>
                                </h5>
                                <a href="${pageContext.request.contextPath}/class?maKhoa=${maKhoa}" class="btn-close btn-close-white text-decoration-none"></a>
                            </div>
                            <form action="${pageContext.request.contextPath}/class" method="POST">
                                <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                <div class="modal-body p-4">
                                    <div class="row g-3">
                                        <div class="col-md-6">
                                            <label class="form-label small fw-bold text-muted"><s:message code="classroom.lbl.classCode"/> <span class="text-danger">*</span></label>
                                            <input type="text" class="form-control rounded-3" name="maLop" value="${lop.maLop}" placeholder="VD: D15CQCN01-N" required ${mode == 'edit' ? 'readonly' : ''}>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label small fw-bold text-muted"><s:message code="classroom.lbl.className"/> <span class="text-danger">*</span></label>
                                            <input type="text" class="form-control rounded-3" name="tenLop" value="${lop.tenLop}" placeholder="<s:message code="classroom.example.name"/>" required>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label small fw-bold text-muted"><s:message code="classroom.lbl.academicTerm"/> <span class="text-danger">*</span></label>
                                            <div class="position-relative d-flex align-items-center" style="max-width: 240px;">
                                                <button id="btn_year_down" class="btn btn-sm btn-light rounded-circle border shadow-sm p-0 position-absolute start-0 ms-2 d-flex align-items-center justify-content-center" style="z-index: 5; width: 28px; height: 28px;" type="button" onclick="adjustYear(-1)">
                                                    <i class="bi bi-dash"></i>
                                                </button>
                                                <input type="text" class="form-control rounded-pill text-center fw-bold bg-white" style="padding-left: 40px; padding-right: 40px; height: 42px; border-color: #e2e8f0;" name="khoaHoc" id="inp_khoaHoc" value="${not empty lop.khoaHoc ? lop.khoaHoc : '2025-2026'}" required>
                                                <button id="btn_year_up" class="btn btn-sm btn-light rounded-circle border shadow-sm p-0 position-absolute end-0 me-2 d-flex align-items-center justify-content-center" style="z-index: 5; width: 28px; height: 28px;" type="button" onclick="adjustYear(1)">
                                                    <i class="bi bi-plus"></i>
                                                </button>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label small fw-bold text-muted">KHOA <span class="text-danger">*</span></label>
                                            <select class="form-select rounded-3" name="maKhoa" required>
                                                <option value="" disabled selected><s:message code="classroom.select.faculty"/></option>
                                                <c:forEach var="k" items="${khoaList}">
                                                    <option value="${k.maKhoa}" ${(not empty lop.maKhoa ? lop.maKhoa == k.maKhoa : maKhoa == k.maKhoa) ? 'selected' : ''}>${k.tenKhoa}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer border-0 px-4 pb-4">
                                    <a href="${pageContext.request.contextPath}/class?maKhoa=${maKhoa}" class="btn btn-light rounded-3 fw-bold"><s:message code="global.btn.cancel"/></a>
                                    <c:choose>
                                        <c:when test="${mode == 'edit'}">
                                            <button type="submit" name="btnUpdate" class="btn btn-primary rounded-3 fw-bold px-4">GHI (<s:message code="global.btn.edit"/>)</button>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="submit" name="btnInsert" class="btn btn-success rounded-3 fw-bold px-4">GHI (<s:message code="global.btn.add"/>)</button>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </form>
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
                    document.querySelectorAll('#class-table-body tr').forEach(row => {
                        row.style.display = normalizeVN(row.innerText).includes(val) ? '' : 'none';
                    });
                }

                function adjustYear(val) {
                    const input = document.getElementById('inp_khoaHoc');
                    const parts = input.value.split('-');
                    if (parts.length === 2) {
                        let start = parseInt(parts[0]);
                        let end = parseInt(parts[1]);
                        if (!isNaN(start) && !isNaN(end)) {
                            start += val;
                            end += val;
                            input.value = start + '-' + end;
                        }
                    }
                }
            </script>
        </body>
        </html>
