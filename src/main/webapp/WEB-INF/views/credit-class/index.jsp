<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="utf-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title><s:message code="credit-class.management.title"/></title>
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

                .badge-soft-danger {
                    background-color: #fee2e2;
                    color: #991b1b;
                    border-radius: 20px;
                    font-weight: 600;
                    padding: 6px 12px;
                }

                .badge-soft-success {
                    background-color: #dcfce7;
                    color: #166534;
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

                .table-custom tbody tr {
                    cursor: pointer;
                    transition: all 0.2s;
                }

                .table-custom tbody tr:hover {
                    background-color: #f0f7ff !important;
                }

                .ltc-title {
                    font-weight: 700;
                    color: #1e293b;
                    margin-bottom: 2px;
                }

                .ltc-subtitle {
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

                .toolbar-btn {
                    font-weight: 600;
                    padding: 6px 12px;
                    display: inline-flex;
                    align-items: center;
                    gap: 6px;
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
                <!-- SIDEBAR -->
                <jsp:include page="/WEB-INF/views/shared/sidebar.jsp" />

                <div class="app-main">
                    <!-- HEADER -->
                    <jsp:include page="/WEB-INF/views/shared/header.jsp" />

                    <!-- MAIN CONTENT -->
                    <main id="main-content" class="app-content p-4 bg-light">
                        <div class="container-fluid max-w-7xl mx-auto">

                            <div class="d-flex align-items-center gap-3 mb-4">
                                <div class="bg-primary bg-opacity-10 text-primary p-2 rounded-3">
                                    <i class="bi bi-layers-fill fs-3"></i>
                                </div>
                                <div>
                                    <h3 class="mb-0 fw-bold text-dark"><s:message code="credit-class.management.title"/></h3>
                                    <p class="text-muted small mb-0"><s:message code="credit-class.plan.desc"/></p>
                                </div>
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
                                        <h6 class="fw-bold text-primary text-uppercase small mb-1"><s:message code="credit-class.list.title"/>
                                        </h6>
                                    </div>
                                    <c:if test="${sessionScope.role == 'PGV'}">
                                        <a href="${pageContext.request.contextPath}/credit-class?maKhoa=${maKhoa}&lnkAdd=true" class="btn btn-primary btn-sm rounded-3 px-3 fw-bold shadow-sm">
                                            <i class="bi bi-plus-circle-fill me-1"></i> Mở <s:message code="global.lbl.selectClass"/> Mới
                                        </a>
                                    </c:if>
                                </div>

                                <div class="card-body px-4 pb-4">
                                    <!-- FILTERS -->
                                    <div class="row g-3 mb-4 pb-3 border-bottom align-items-center">
                                        <div class="col-md-4">
                                            <c:choose>
                                                <c:when test="${sessionScope.role == 'PGV'}">
                                                    <div class="input-group">
                                                        <span class="input-group-text bg-light border-0"><i
                                                                class="bi bi-building"></i></span>
                                                        <form action="${pageContext.request.contextPath}/credit-class" method="GET" class="d-inline flex-grow-1">
                                                            <select name="maKhoa"
                                                                class="form-select bg-light border-0 fw-semibold"
                                                                onchange="this.form.submit()">
                                                                <option value="all"><s:message code="credit-class.all.faculties"/></option>
                                                                <c:forEach var="k" items="${khoaList}">
                                                                    <option value="${k.maKhoa}" ${param.maKhoa == k.maKhoa || maKhoa == k.maKhoa ? 'selected' : ''}>${k.tenKhoa}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </form>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="d-flex align-items-center gap-2 px-3 py-2 bg-light rounded-3">
                                                        <i class="bi bi-building text-primary"></i>
                                                        <span class="fw-bold text-dark">${khoaList[0].tenKhoa}</span>
                                                        <input type="hidden" id="khoa-filter" value="${sessionScope.maKhoa}">
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="col-md-8 text-end">
                                            <div class="text-muted small fw-medium">
                                                <i class="bi bi-info-circle me-1"></i> <s:message code="credit-class.found"/> <strong
                                                    class="text-primary">${ltcList.size()}</strong> <s:message code="credit-class.count.suffix"/>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- TABLE -->
                                    <div class="table-responsive rounded-3 border">
                                        <table class="table table-custom align-middle mb-0">
                                            <thead class="table-light">
                                                <tr>
                                                    <th class="px-3"><s:message code="credit-class.lbl.classId"/></th>
                                                    <th><s:message code="credit-class.lbl.subject"/></th>
                                                    <th><s:message code="credit-class.lbl.yearSemester"/></th>
                                                    <th class="text-center"><s:message code="credit-class.lbl.group"/></th>
                                                    <th><s:message code="credit-class.lbl.lecturer"/></th>
                                                    <th class="text-center"><s:message code="credit-class.lbl.status"/></th>
                                                    <c:if test="${sessionScope.role == 'PGV'}">
                                                        <th class="text-center"><s:message code="global.lbl.actions"/></th>
                                                    </c:if>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="item" items="${ltcList}">
                                                    <tr>
                                                        <td class="px-3"><span
                                                                class="badge-soft-primary">${item.maLTC}</span></td>
                                                        <td>
                                                            <div class="ltc-title">${item.monHoc.tenMH}</div>
                                                            <div class="ltc-subtitle"><i class="bi bi-building"></i>
                                                                ${item.khoa.maKhoa}</div>
                                                        </td>
                                                        <td>
                                                            <div class="fw-bold text-dark">${item.nienKhoa}</div>
                                                            <div class="small text-muted"><s:message code="credit-class.semester"/></div>
                                                        </td>
                                                        <td class="text-center"><span class="badge-soft-secondary"><s:message code="global.lbl.group"/>
                                                                ${item.nhom}</span></td>
                                                        <td>
                                                            <div class="fw-semibold text-primary">${item.giangVien.ho} ${item.giangVien.ten}</div>
                                                            <div class="small text-muted"><s:message code="credit-class.lbl.minStudents"/>
                                                                ${item.soSVToiThieu} SV</div>
                                                        </td>
                                                        <td class="text-center">
                                                            <c:choose>
                                                                <c:when test="${item.huyLop}">
                                                                    <span class="badge-soft-danger small"><i
                                                                            class="bi bi-x-circle"></i> <s:message code="credit-class.canceled"/></span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="badge-soft-success small"><i
                                                                            class="bi bi-check-circle"></i> <s:message code="credit-class.status.open"/></span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <c:if test="${sessionScope.role == 'PGV'}">
                                                            <td class="text-center">
                                                                <div class="d-flex gap-2 justify-content-center">
                                                                    <a href="${pageContext.request.contextPath}/credit-class?maKhoa=${maKhoa}&maLTC=${item.maLTC}&lnkEdit"
                                                                        class="btn btn-sm btn-outline-primary border-0 rounded-3">
                                                                        <i class="bi bi-pencil-square"></i>
                                                                    </a>
                                                                    <form action="${pageContext.request.contextPath}/credit-class" method="POST" onsubmit="return confirm('Bạn có chắc chắn muốn xóa lớp tín chỉ này không?');" class="d-inline">
                                                                        <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                                                        <input type="hidden" name="maLTC" value="${item.maLTC}">
                                                                        <input type="hidden" name="maKhoa" value="${maKhoa}">
                                                                        <button type="submit" name="btnDelete" class="btn btn-sm btn-outline-danger border-0 rounded-3 ${!item.canDelete ? 'disabled opacity-25' : ''}"
                                                                            ${!item.canDelete ? 'disabled title="<s:message code="credit-class.cannot.delete"/>"' : ''}>
                                                                            <i class="bi bi-trash3"></i>
                                                                        </button>
                                                                    </form>
                                                                </div>
                                                            </td>
                                                        </c:if>
                                                    </tr>
                                                </c:forEach>
                                                <c:if test="${empty ltcList}">
                                                    <tr>
                                                        <td colspan="${sessionScope.role == 'PGV' ? 7 : 6}" class="text-center py-5 text-muted">
                                                            <i class="bi bi-inbox fs-1 d-block mb-3 opacity-25"></i>
                                                            <s:message code="credit-class.no.data"/>
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

            <!-- MODAL (SSR) -->
            <c:if test="${sessionScope.role == 'PGV' && (not empty mode || not empty param.lnkAdd)}">
                <div class="modal fade show d-block" id="ltcModal" tabindex="-1" style="background: rgba(0,0,0,0.5);">
                    <div class="modal-dialog modal-lg modal-dialog-centered">
                        <div class="modal-content border-0 shadow-lg rounded-4">
                            <div class="modal-header bg-primary text-white border-0 py-3 px-4 rounded-top-4">
                                <h5 class="modal-title fw-bold d-flex align-items-center gap-2">
                                    <i class="bi bi-layers-fill"></i>
                                    <c:choose>
                                        <c:when test="${mode == 'edit'}"><s:message code="credit-class.update"/></c:when>
                                        <c:otherwise><s:message code="credit-class.open.new"/></c:otherwise>
                                    </c:choose>
                                </h5>
                                <a href="${pageContext.request.contextPath}/credit-class?maKhoa=${maKhoa}" class="btn-close btn-close-white text-decoration-none"></a>
                            </div>
                            <form action="${pageContext.request.contextPath}/credit-class" method="POST">
                                <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                <div class="modal-body p-4">
                                    <div class="row g-3 p-3 bg-white border rounded-3 shadow-sm mb-4">
                                        <input type="hidden" name="maLTC" value="${ltc.maLTC}">
                                        <div class="col-md-6">
                                            <label class="form-label small fw-bold text-muted"><s:message code="global.lbl.academicYear.upper"/> <span class="text-danger">*</span></label>
                                            <input type="text" class="form-control" name="nienKhoa" value="${ltc.nienKhoa}"
                                                placeholder="VD: 2023-2024" required>
                                        </div>
                                        <div class="col-md-3">
                                            <label class="form-label small fw-bold text-muted"><s:message code="student.lbl.lastName"/>C KỲ <span class="text-danger">*</span></label>
                                            <input type="number" class="form-control" name="hocKy" min="1" max="3" value="${not empty ltc.hocKy ? ltc.hocKy : 1}"
                                                required>
                                        </div>
                                        <div class="col-md-3">
                                            <label class="form-label small fw-bold text-muted"><s:message code="credit-class.lbl.group"/> <span class="text-danger">*</span></label>
                                            <input type="number" class="form-control" name="nhom" min="1" value="${not empty ltc.nhom ? ltc.nhom : 1}" required>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label small fw-bold text-muted"><s:message code="credit-class.lbl.subject"/> <span class="text-danger">*</span></label>
                                            <select class="form-select" name="maMH" required>
                                                <option value=""><s:message code="credit-class.select.subject"/></option>
                                                <c:forEach var="mh" items="${monHocList}">
                                                    <option value="${mh.maMH}" ${ltc.monHoc.maMH == mh.maMH ? 'selected' : ''}>[${mh.maMH}] ${mh.tenMH}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label small fw-bold text-muted"><s:message code="credit-class.lbl.lecturer"/> <span class="text-danger">*</span></label>
                                            <select class="form-select" name="maGV" required>
                                                <option value=""><s:message code="credit-class.select.lecturer"/></option>
                                                <c:forEach var="gv" items="${giangVienList}">
                                                    <option value="${gv.maGV}" ${ltc.giangVien.maGV == gv.maGV ? 'selected' : ''}>[${gv.maGV}] ${gv.ho} ${gv.ten}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label small fw-bold text-muted"><s:message code="credit-class.lbl.faculty.required"/> <span class="text-danger">*</span></label>
                                            <select name="maKhoa" class="form-select">
                                                <c:forEach var="k" items="${khoaList}">
                                                    <option value="${k.maKhoa}" ${(not empty ltc.khoa.maKhoa ? ltc.khoa.maKhoa == k.maKhoa : maKhoa == k.maKhoa) ? 'selected' : ''}>${k.tenKhoa}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="col-md-3">
                                            <label class="form-label small fw-bold text-muted"><s:message code="credit-class.lbl.minStudents.required"/> <span class="text-danger">*</span></label>
                                            <input type="number" class="form-control" name="soSVToiThieu" min="1" value="${not empty ltc.soSVToiThieu ? ltc.soSVToiThieu : 1}"
                                                required>
                                        </div>
                                        <div class="col-md-3 d-flex align-items-end pb-1">
                                            <div class="form-check form-switch mb-2">
                                                <input class="form-check-input" type="checkbox" name="huyLop" id="inp_huyLop" value="true" ${ltc.huyLop ? 'checked' : ''}>
                                                <label class="form-check-label fw-bold text-danger small" for="inp_huyLop"><s:message code="credit-class.btn.cancel.class"/></label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer border-0 px-4 pb-4">
                                    <a href="${pageContext.request.contextPath}/credit-class?maKhoa=${maKhoa}" class="btn btn-light rounded-3 fw-bold"><s:message code="global.btn.cancel"/></a>
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
        </body>
        </html>
