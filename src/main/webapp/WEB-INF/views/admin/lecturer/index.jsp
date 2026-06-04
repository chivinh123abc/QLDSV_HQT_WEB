<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">

        <head>
            <meta charset="utf-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title><s:message code="lecturer.management.title"/></title>
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

                .lecturer-name {
                    font-weight: 700;
                    color: #1e293b;
                    margin-bottom: 2px;
                }

                .lecturer-info {
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
                <jsp:include page="/WEB-INF/views/shared/sidebar.jsp" />

                <div class="app-main">
                    <jsp:include page="/WEB-INF/views/shared/header.jsp" />

                    <main id="main-content" class="app-content p-4 bg-light">
                        <div class="container-fluid max-w-7xl mx-auto">
                            <div class="d-flex align-items-center gap-2 mb-4">
                                <i class="bi bi-person-badge-fill text-primary fs-3"></i>
                                <h3 class="mb-0 fw-bold text-dark"><s:message code="lecturer.management"/></h3>
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
                                        <h6 class="fw-bold text-primary text-uppercase small mb-1"><s:message code="lecturer.list"/></h6>
                                        <p class="text-muted small mb-0"><s:message code="lecturer.management.desc"/></p>
                                    </div>
                                    <c:if test="${sessionScope.role == 'PGV'}">
                                        <!-- GET link to open add modal -->
                                        <a href="${pageContext.request.contextPath}/admin/lecturer?maKhoa=${maKhoa}&lnkAdd=true" class="btn btn-primary btn-sm rounded-3 px-3 fw-bold shadow-sm">
                                            <i class="bi bi-plus-circle-fill me-1"></i> <s:message code="lecturer.btn.add"/>
                                        </a>
                                    </c:if>
                                </div>

                                <div class="card-body px-4 pb-4">
                                    <div class="d-flex justify-content-between align-items-center mb-4 pb-3 border-bottom">
                                        <div class="input-group" style="max-width: 300px;">
                                            <span class="input-group-text bg-light border-0"><i class="bi bi-search text-muted"></i></span>
                                            <input type="text" id="search-input" class="form-control bg-light border-0 small" placeholder="<s:message code="lecturer.search"/>" onkeyup="filterLocal()">
                                        </div>
                                        <div class="d-flex gap-3 align-items-center">
                                            <label class="fw-bold small text-muted text-uppercase mb-0"><s:message code="lecturer.filter.by.faculty"/></label>
                                            <c:choose>
                                                <c:when test="${sessionScope.role == 'PGV'}">
                                                    <form action="${pageContext.request.contextPath}/admin/lecturer" method="GET" class="d-inline">
                                                        <select name="maKhoa" class="form-select form-select-sm border-0 bg-light text-primary fw-bold" style="min-width: 200px;" onchange="this.form.submit()">
                                                            <option value="all"><s:message code="lecturer.all.faculties"/></option>
                                                            <c:forEach var="k" items="${khoaList}">
                                                                <option value="${k.maKhoa}" ${param.maKhoa == k.maKhoa || maKhoa == k.maKhoa ? 'selected' : ''}>${k.tenKhoa}</option>
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

                                    <div class="table-responsive rounded-3 border">
                                        <table class="table table-custom align-middle mb-0">
                                            <thead class="table-light">
                                                <tr>
                                                    <th class="px-3"><s:message code="lecturer.lbl.code"/></th>
                                                    <th><s:message code="account.lbl.fullName"/></th>
                                                    <th class="text-center"><s:message code="lecturer.lbl.degree"/></th>
                                                    <th class="text-center"><s:message code="lecturer.lbl.specialization"/></th>
                                                    <th class="text-center">KHOA</th>
                                                    <c:if test="${sessionScope.role == 'PGV'}">
                                                        <th class="text-center"><s:message code="global.lbl.actions"/></th>
                                                    </c:if>
                                                </tr>
                                            </thead>
                                            <tbody id="gv-table-body">
                                                <c:forEach var="item" items="${gvList}">
                                                    <tr>
                                                        <td class="px-3"><span class="badge bg-primary bg-opacity-10 text-primary fw-bold">${item.maGV}</span></td>
                                                        <td>
                                                            <div class="lecturer-name">${item.ho} ${item.ten}</div>
                                                        </td>
                                                        <td class="text-center">
                                                            <span class="text-muted small">
                                                                <c:choose>
                                                                    <c:when test="${not empty item.hocVi && not empty item.hocHam}">
                                                                        ${item.hocVi} / ${item.hocHam}
                                                                    </c:when>
                                                                    <c:when test="${not empty item.hocVi}">
                                                                        ${item.hocVi}
                                                                    </c:when>
                                                                    <c:when test="${not empty item.hocHam}">
                                                                        ${item.hocHam}
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        -
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </span>
                                                        </td>
                                                        <td class="text-center">
                                                            <span class="text-dark small fw-medium">${item.chuyenMon}</span>
                                                        </td>
                                                        <td class="text-center">
                                                            <span class="badge border border-info text-info rounded-pill px-3 py-1 fw-bold small">${item.maKhoa}</span>
                                                        </td>
                                                        <c:if test="${sessionScope.role == 'PGV'}">
                                                            <td class="text-center">
                                                                <div class="d-flex gap-2 justify-content-center">
                                                                    <!-- Edit GET Link -->
                                                                    <a href="${pageContext.request.contextPath}/admin/lecturer?maKhoa=${maKhoa}&maGV=${item.maGV}&lnkEdit" class="btn btn-sm btn-outline-primary border-0 rounded-3">
                                                                        <i class="bi bi-pencil-square"></i>
                                                                    </a>
                                                                    <!-- Delete POST Form -->
                                                                    <form action="${pageContext.request.contextPath}/admin/lecturer" method="POST" onsubmit="return confirm('<s:message code="lecturer.js.confirmDelete"/>');" class="d-inline">
                                                                        <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                                                        <input type="hidden" name="maGV" value="${item.maGV}">
                                                                        <input type="hidden" name="maKhoa" value="${maKhoa}">
                                                                        <button type="submit" name="btnDelete" class="btn btn-sm btn-outline-danger border-0 rounded-3 ${!item.canDelete ? 'disabled opacity-25' : ''}" <c:if test="${!item.canDelete}">disabled title="<s:message code='lecturer.cannot.delete'/>"</c:if>>
                                                                            <i class="bi bi-trash3"></i>
                                                                        </button>
                                                                    </form>
                                                                </div>
                                                            </td>
                                                        </c:if>
                                                    </tr>
                                                </c:forEach>
                                                <c:if test="${empty gvList}">
                                                    <tr>
                                                        <td colspan="${sessionScope.role == 'PGV' ? 6 : 5}" class="text-center py-5 text-muted">
                                                            <i class="bi bi-inbox fs-1 d-block mb-3 opacity-25"></i>
                                                            <s:message code="lecturer.no.data"/>
                                                        </td>
                                                    </tr>
                                                </c:if>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="mt-3 text-muted small px-1">
                                         <i class="bi bi-info-circle me-1"></i> <s:message code="lecturer.display"/> <strong id="gv-count">${gvList.size()}</strong> <s:message code="lecturer.count.suffix"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </main>
                </div>
            </div>

            <!-- GV MODAL -->
            <c:if test="${sessionScope.role == 'PGV' && (not empty mode || not empty param.lnkAdd)}">
                <div class="modal fade show d-block" id="gvModal" tabindex="-1" style="background: rgba(0,0,0,0.5);">
                    <div class="modal-dialog modal-lg modal-dialog-centered">
                        <div class="modal-content border-0 shadow-lg rounded-4">
                            <div class="modal-header bg-primary text-white border-0 py-3 px-4 rounded-top-4">
                                <h5 class="modal-title fw-bold d-flex align-items-center gap-2">
                                    <i class="bi bi-person-lines-fill"></i>
                                    <c:choose>
                                        <c:when test="${mode == 'edit'}"><s:message code="lecturer.update"/></c:when>
                                        <c:otherwise><s:message code="lecturer.add.new"/></c:otherwise>
                                    </c:choose>
                                </h5>
                                <a href="${pageContext.request.contextPath}/admin/lecturer?maKhoa=${maKhoa}" class="btn-close btn-close-white text-decoration-none"></a>
                            </div>
                            <form:form action="${pageContext.request.contextPath}/admin/lecturer" method="POST" modelAttribute="giangVien">
                                <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                <form:hidden path="version"/>
                                <div class="modal-body p-4">
                                    <div class="row g-3">
                                        <div class="col-md-6">
                                            <label class="form-label small fw-bold text-muted"><s:message code="lecturer.lbl.code"/> <span class="text-danger">*</span></label>
                                            <form:input path="maGV" cssClass="form-control rounded-3"
                                                placeholder="VD: GV01"
                                                readonly="${mode == 'edit'}"/>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label small fw-bold text-muted"><s:message code="sidebar.menu.faculty"/> <span class="text-danger">*</span></label>
                                            <s:message code="lecturer.select.faculty" var="lblSelectFaculty"/>
                                            <form:select path="maKhoa" cssClass="form-select rounded-3">
                                                <form:option value="" label="${lblSelectFaculty}"/>
                                                <form:options items="${khoaList}" itemValue="maKhoa" itemLabel="tenKhoa"/>
                                            </form:select>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label small fw-bold text-muted"><s:message code="lecturer.lbl.lastName.required"/> <span class="text-danger">*</span></label>
                                            <form:input path="ho" cssClass="form-control rounded-3"
                                                placeholder="<s:message code="lecturer.example.name"/>"/>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label small fw-bold text-muted"><s:message code="lecturer.lbl.firstName.required"/> <span class="text-danger">*</span></label>
                                            <form:input path="ten" cssClass="form-control rounded-3"
                                                placeholder="<s:message code="lecturer.example.firstName"/>"/>
                                        </div>
                                        <div class="col-md-4">
                                            <label class="form-label small fw-bold text-muted"><s:message code="lecturer.lbl.academicDegree"/></label>
                                            <form:input path="hocVi" cssClass="form-control rounded-3"
                                                placeholder="<s:message code="lecturer.placeholder.degree"/>"/>
                                        </div>
                                        <div class="col-md-4">
                                            <label class="form-label small fw-bold text-muted"><s:message code="lecturer.lbl.academicRank"/></label>
                                            <form:input path="hocHam" cssClass="form-control rounded-3"
                                                placeholder="<s:message code="lecturer.placeholder.rank"/>"/>
                                        </div>
                                        <div class="col-md-4">
                                            <label class="form-label small fw-bold text-muted"><s:message code="lecturer.lbl.specialization"/></label>
                                            <form:input path="chuyenMon" cssClass="form-control rounded-3"
                                                placeholder="<s:message code="lecturer.placeholder.specialty"/>"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer border-0 px-4 pb-4">
                                    <a href="${pageContext.request.contextPath}/admin/lecturer?maKhoa=${maKhoa}" class="btn btn-light rounded-3 fw-bold"><s:message code="global.btn.cancel"/></a>
                                    <c:choose>
                                        <c:when test="${mode == 'edit'}">
                                            <button type="submit" name="btnUpdate" class="btn btn-primary rounded-3 fw-bold px-4"><s:message code="global.btn.save.action"/> (<s:message code="global.btn.edit"/>)</button>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="submit" name="btnInsert" class="btn btn-success rounded-3 fw-bold px-4"><s:message code="global.btn.save.action"/> (<s:message code="global.btn.add"/>)</button>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </form:form>
                        </div>
                    </div>
                </div>
            </c:if>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
            <script>
                function normalizeVN(str) {
                    return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/\u0111/g, 'd').replace(/\u0110/g, 'D').toLowerCase();
                }

                function filterLocal() {
                    const val = normalizeVN(document.getElementById('search-input').value);
                    document.querySelectorAll('#gv-table-body tr').forEach(row => {
                        row.style.display = normalizeVN(row.innerText).includes(val) ? '' : 'none';
                    });
                }
            </script>
        </body>
</html>
