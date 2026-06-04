<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title><s:message code="student.management.title"/></title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <style>
        .badge-soft-primary { background-color: #e0f2fe; color: #0369a1; border-radius: 20px; font-weight: 600; padding: 6px 12px; }
        .badge-soft-secondary { background-color: #f1f5f9; color: #475569; border-radius: 20px; padding: 6px 12px; font-weight: 500; }
        .badge-soft-success { background-color: #dcfce7; color: #166534; border-radius: 20px; font-weight: 600; padding: 6px 12px; }
        .badge-soft-danger { background-color: #fee2e2; color: #991b1b; border-radius: 20px; font-weight: 600; padding: 6px 12px; }
        .table-custom th { font-size: 0.85rem; color: #64748b; font-weight: 700; text-transform: uppercase; border-bottom: 2px solid #e2e8f0; }
        .table-custom td { vertical-align: middle; padding: 1rem 0.75rem; border-bottom: 1px solid #f1f5f9; }
        .table-custom tr:hover { background-color: #f8fafc; }
        .class-row:hover { background-color: #f8fbff !important; }
        .border-dashed { border: 2px dashed #dee2e6 !important; }
        .form-control:focus, .form-select:focus { box-shadow: none; border-color: #4361ee; }
        .app-content { min-height: 100vh; }
        .btn-xs { padding: 0.1rem 0.3rem; font-size: 0.75rem; }
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
                        <i class="bi bi-mortarboard-fill text-primary fs-3"></i>
                        <h3 class="mb-0 fw-bold text-dark"><s:message code="student.management"/></h3>
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
                    <c:if test="${not empty errorLines}">
                        <div class="alert alert-warning alert-dismissible fade show rounded-3 shadow-sm border-0 mb-4" role="alert">
                            <h6 class="fw-bold"><i class="bi bi-exclamation-triangle-fill me-2"></i> 
                                <s:message code="student.import.errorList" arguments="${errorLines.size()}" />
                            </h6>
                            <ul class="mb-0 small" style="max-height: 150px; overflow-y: auto; padding-left: 20px;">
                                <c:forEach var="errLine" items="${errorLines}">
                                    <li>${errLine}</li>
                                </c:forEach>
                            </ul>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <!-- MASTER SECTION: CLASS LIST -->
                    <div class="card border-0 shadow-sm rounded-4 mb-4 overflow-hidden">
                        <div class="card-header bg-white border-bottom-0 pt-4 px-4 pb-2">
                            <h6 class="fw-bold text-primary text-uppercase small mb-0"><s:message code="student.management.by.class"/></h6>
                        </div>
                        <div class="card-body px-4 pb-4">
                            <!-- SEARCH & FILTER TOOLBAR -->
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <div class="input-group" style="max-width: 300px;">
                                    <span class="input-group-text bg-light border-0"><i class="bi bi-search text-muted"></i></span>
                                    <s:message code="student.search.class" var="lblStudentSearchClass"/>
                                    <input type="text" id="class-search" class="form-control bg-light border-0 small" placeholder="${lblStudentSearchClass}" onkeyup="filterLocalClasses()">
                                </div>
                                <div class="d-flex gap-2">
                                    <c:choose>
                                        <c:when test="${sessionScope.role == 'PGV'}">
                                            <!-- Standard SSR form filter for PGV role -->
                                            <form action="${pageContext.request.contextPath}/admin/student" method="GET" class="d-inline">
                                                <select name="maKhoa" class="form-select form-select-sm border-0 bg-light text-muted fw-bold" onchange="this.form.submit()">
                                                    <option value="all"><s:message code="student.all.faculties"/></option>
                                                    <c:forEach var="khoa" items="${khoaList}">
                                                        <option value="${khoa.maKhoa}" ${param.maKhoa == khoa.maKhoa || maKhoa == khoa.maKhoa ? 'selected' : ''}>${khoa.tenKhoa}</option>
                                                    </c:forEach>
                                                </select>
                                                <c:if test="${not empty maLop}">
                                                    <input type="hidden" name="maLop" value="${maLop}">
                                                </c:if>
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
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th class="border-0 px-3 small fw-bold text-muted"><s:message code="classroom.lbl.classCode"/></th>
                                            <th class="border-0 small fw-bold text-muted"><s:message code="classroom.lbl.className"/></th>
                                            <th class="border-0 text-center small fw-bold text-muted"><s:message code="classroom.lbl.academicTerm"/></th>
                                            <th class="border-0 text-center small fw-bold text-muted">KHOA</th>
                                        </tr>
                                    </thead>
                                    <tbody id="class-table-body" class="border-top-0">
                                        <c:forEach var="lop" items="${lopList}">
                                            <c:set var="classUrl" value="${pageContext.request.contextPath}/admin/student?maLop=${lop.maLop}" />
                                            <c:if test="${not empty maKhoa && maKhoa != 'all'}">
                                                <c:set var="classUrl" value="${classUrl}&maKhoa=${maKhoa}" />
                                            </c:if>
                                            <tr onclick="window.location.href='${classUrl}'" style="cursor: pointer; transition: 0.2s;" class="${lop.maLop == maLop ? 'table-primary shadow-sm' : ''} class-row">
                                                <td class="px-3">
                                                    <span class="badge ${lop.maLop == maLop ? 'bg-primary' : 'bg-primary bg-opacity-10 text-primary'} rounded-2 px-3 py-2">
                                                        ${lop.maLop}
                                                    </span>
                                                </td>
                                                <td class="fw-semibold text-dark">${lop.tenLop}</td>
                                                <td class="text-center"><span class="text-muted small fw-bold">${lop.khoaHoc}</span></td>
                                                <td class="text-center">
                                                    <span class="badge border border-info text-info rounded-pill px-3 py-1 fw-bold small">${lop.maKhoa}</span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty lopList}">
                                            <tr>
                                                <td colspan="4" class="text-center py-4 text-muted"><s:message code="student.no.classes.found"/></td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                            <div class="mt-2 text-muted small px-1">
                                <i class="bi bi-info-circle me-1"></i> <s:message code="student.select.class.hint"/>
                            </div>
                        </div>
                    </div>

                    <!-- DETAIL SECTION: STUDENT LIST -->
                    <div id="student-section">
                        <c:choose>
                            <c:when test="${not empty maLop}">
                                <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
                                    <div class="card-header bg-white border-bottom-0 pt-4 px-4 pb-3 d-flex justify-content-between align-items-center">
                                        <h6 class="fw-bold text-success text-uppercase small mb-0 d-flex align-items-center gap-2">
                                            <i class="bi bi-people-fill text-success"></i>
                                            Danh sách sinh viên - <s:message code="registration.class.label"/> <span class="text-dark">${maLop}</span>
                                        </h6>
                                        <c:if test="${sessionScope.role == 'PGV'}">
                                            <div class="d-flex gap-2">
                                                <!-- Import Button -->
                                                <button type="button" class="btn btn-info btn-sm text-white rounded-3 px-3 fw-bold shadow-sm" data-bs-toggle="modal" data-bs-target="#importCsvModal">
                                                    <i class="bi bi-file-earmark-arrow-up-fill me-1"></i> <s:message code="student.btn.importCsv"/>
                                                </button>
                                                <!-- Export Button -->
                                                <a href="${pageContext.request.contextPath}/admin/student/export-credentials" class="btn btn-success btn-sm rounded-3 px-3 fw-bold shadow-sm">
                                                    <i class="bi bi-file-earmark-arrow-down-fill me-1"></i> <s:message code="student.btn.exportAccounts"/>
                                                </a>
                                                <!-- Button to trigger ADD modal via query parameter (clean reload) -->
                                                <a href="${pageContext.request.contextPath}/admin/student?maLop=${maLop}&lnkAdd" class="btn btn-primary btn-sm rounded-3 px-3 fw-bold shadow-sm">
                                                    <i class="bi bi-person-plus-fill me-1"></i> <s:message code="student.btn.add"/>
                                                </a>
                                            </div>
                                        </c:if>
                                    </div>
                                    <div class="card-body px-4 pb-4">
                                        <div class="d-flex justify-content-between align-items-center mb-3">
                                            <div class="input-group" style="max-width: 300px;">
                                                <span class="input-group-text bg-light border-0"><i class="bi bi-search text-muted"></i></span>
                                                <s:message code="student.search.student" var="lblStudentSearchStudent"/>
                                                <input type="text" id="student-search" class="form-control bg-light border-0 small" placeholder="${lblStudentSearchStudent}" onkeyup="filterLocalStudents()">
                                            </div>
                                        </div>

                                        <div class="table-responsive rounded-3 border">
                                            <table class="table table-hover align-middle mb-0">
                                                <thead class="table-light">
                                                    <tr>
                                                        <th class="border-0 px-3 small fw-bold text-muted"><s:message code="student.lbl.studentId"/></th>
                                                        <th class="border-0 small fw-bold text-muted"><s:message code="student.lbl.lastName"/> VÀ <s:message code="lecturer.lbl.firstName.required"/></th>
                                                        <th class="border-0 text-center small fw-bold text-muted"><s:message code="student.lbl.gender.upper"/></th>
                                                        <th class="border-0 text-center small fw-bold text-muted"><s:message code="student.lbl.dob.upper"/></th>
                                                        <th class="border-0 small fw-bold text-muted"><s:message code="student.address"/></th>
                                                        <th class="border-0 text-center small fw-bold text-muted"><s:message code="credit-class.lbl.status"/></th>
                                                        <c:if test="${sessionScope.role == 'PGV'}">
                                                            <th class="border-0 text-center small fw-bold text-muted"><s:message code="global.lbl.actions"/></th>
                                                        </c:if>
                                                    </tr>
                                                </thead>
                                                <tbody id="main-student-table-body">
                                                    <c:forEach var="sv" items="${sinhVienList}">
                                                        <tr>
                                                            <td class="px-3"><span class="badge bg-info bg-opacity-10 text-info fw-bold">${sv.maSV}</span></td>
                                                            <td>
                                                                <div class="fw-bold text-dark d-flex align-items-center gap-2">
                                                                    <i class="bi bi-person-circle text-muted"></i>
                                                                    ${sv.ho} ${sv.ten}
                                                                </div>
                                                            </td>
                                                            <td class="text-center">
                                                                <span class="badge rounded-pill border border-secondary text-secondary px-3 py-1 small">
                                                                    <i class="bi ${sv.phai == 'Nam' ? 'bi-gender-male' : 'bi-gender-female'} me-1"></i>
                                                                    ${sv.phai}
                                                                </span>
                                                            </td>
                                                            <td class="text-center text-muted small">
                                                                <i class="bi bi-calendar3 me-1"></i>
                                                                <fmt:formatDate value="${sv.ngaySinh}" pattern="dd/MM/yyyy" />
                                                            </td>
                                                            <td class="text-muted small"><i class="bi bi-geo-alt me-1"></i> ${sv.diaChi}</td>
                                                            <td class="text-center">
                                                                <span class="badge border ${sv.daNghiHoc ? 'border-danger text-danger' : 'border-success text-success'} rounded-pill px-3 py-1 small fw-bold">
                                                                    <s:message code="${sv.daNghiHoc ? 'student.status.suspended' : 'student.status.active'}"/>
                                                                </span>
                                                            </td>
                                                            <c:if test="${sessionScope.role == 'PGV'}">
                                                                <td class="text-center">
                                                                    <div class="d-flex gap-2 justify-content-center align-items-center">
                                                                        <!-- GET link for Edit modal reload -->
                                                                        <a href="${pageContext.request.contextPath}/admin/student?maLop=${maLop}&maSV=${sv.maSV}&lnkEdit" class="btn btn-sm btn-outline-primary border-0 rounded-3">
                                                                            <i class="bi bi-pencil-square"></i>
                                                                        </a>
                                                                        
                                                                        <!-- Form POST for secure Delete action -->
                                                                        <form action="${pageContext.request.contextPath}/admin/student" method="POST" onsubmit="return confirm('<s:message code="student.js.confirmDelete"/>');" class="d-inline">
                                                                            <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                                                            <input type="hidden" name="maSV" value="${sv.maSV}">
                                                                            <input type="hidden" name="maLop" value="${maLop}">
                                                                            <s:message code="student.cannot.delete" var="cannotDeleteMsg" />
                                                                            <button type="submit" name="btnDelete" class="btn btn-sm btn-outline-danger border-0 rounded-3 ${!sv.canDelete ? 'disabled opacity-25' : ''}" ${!sv.canDelete ? 'disabled' : ''} title="${!sv.canDelete ? cannotDeleteMsg : ''}">
                                                                                <i class="bi bi-trash3"></i>
                                                                            </button>
                                                                        </form>
                                                                    </div>
                                                                </td>
                                                            </c:if>
                                                        </tr>
                                                    </c:forEach>
                                                    <c:if test="${empty sinhVienList}">
                                                        <tr>
                                                            <td colspan="7" class="text-center py-5 text-muted">
                                                                <i class="bi bi-inbox fs-1 d-block mb-2"></i>
                                                                <s:message code="student.no.data"/>
                                                            </td>
                                                        </tr>
                                                    </c:if>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-5 bg-white rounded-4 shadow-sm border border-dashed">
                                    <i class="bi bi-arrow-up-circle fs-1 text-primary opacity-25 d-block mb-3"></i>
                                    <h5 class="text-muted fw-bold"><s:message code="student.please.select.class"/></h5>
                                    <p class="text-muted small"><s:message code="student.info.placeholder"/></p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                </div>
            </main>
        </div>
    </div>

    <!-- STUDENT MODAL -->
    <c:if test="${sessionScope.role == 'PGV' && (not empty mode || not empty param.lnkAdd)}">
        <div class="modal fade show d-block" id="studentModal" tabindex="-1" style="background: rgba(0,0,0,0.5);">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg rounded-4">
                    <div class="modal-header bg-primary text-white border-0 py-3 px-4 rounded-top-4">
                        <h5 class="modal-title fw-bold d-flex align-items-center gap-2">
                            <i class="bi bi-person-vcard"></i>
                            <c:choose>
                                <c:when test="${mode == 'edit'}"><s:message code="student.update.profile"/></c:when>
                                <c:otherwise><s:message code="student.add.new"/></c:otherwise>
                            </c:choose>
                        </h5>
                        <a href="${pageContext.request.contextPath}/admin/student?maLop=${maLop}" class="btn-close btn-close-white text-decoration-none"></a>
                    </div>

                    <%-- Spring Form Taglib: form:form bind modelAttribute="sinhVien" (StudentDTO) --%>
                    <form:form action="${pageContext.request.contextPath}/admin/student" method="POST" modelAttribute="sinhVien">
                        <input type="hidden" name="csrf_token" value="${csrfToken}" />
                        <%-- form:hidden giữ version để hỗ trợ optimistic locking --%>
                        <form:hidden path="version"/>
                        <div class="modal-body p-4">
                            <div class="row g-3">
                                <div class="col-md-4">
                                    <label class="form-label small fw-bold text-muted"><s:message code="student.lbl.studentId.form"/></label>
                                    <%-- form:input tự động điền lại giá trị khi form lỗi (giữ dữ liệu đã nhập) --%>
                                    <s:message code="student.example.id" var="lblStudentExampleId"/>
                                    <form:input path="maSV" cssClass="form-control rounded-3"
                                        id="inp_maSV" placeholder="${lblStudentExampleId}"
                                        readonly="${mode == 'edit'}"/>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-bold text-muted"><s:message code="student.lbl.lastName"/></label>
                                    <s:message code="student.example.name" var="lblStudentExampleName"/>
                                    <form:input path="ho" cssClass="form-control rounded-3"
                                        id="inp_ho" placeholder="${lblStudentExampleName}"/>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-bold text-muted"><s:message code="lecturer.lbl.firstName.required"/></label>
                                    <s:message code="student.example.firstName" var="lblStudentExampleFirstName"/>
                                    <form:input path="ten" cssClass="form-control rounded-3"
                                        id="inp_ten" placeholder="${lblStudentExampleFirstName}"/>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-bold text-muted"><s:message code="student.lbl.gender.upper"/></label>
                                    <%-- form:select tự động chọn đúng option theo giá trị hiện tại --%>
                                    <s:message code="global.gender.male" var="lblGenderMale"/>
                                    <s:message code="global.gender.female" var="lblGenderFemale"/>
                                    <form:select path="phai" cssClass="form-select rounded-3" id="inp_phai">
                                        <form:option value="Nam" label="${lblGenderMale}"/>
                                        <form:option value="Nữ" label="${lblGenderFemale}"/>
                                    </form:select>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-bold text-muted"><s:message code="student.lbl.dob.upper"/></label>
                                    <%-- fmt:formatDate để format ngày sinh sang yyyy-MM-dd cho input[type=date] --%>
                                    <fmt:formatDate var="fmtDate" value="${sinhVien.ngaySinh}" pattern="yyyy-MM-dd" />
                                    <input type="date" class="form-control rounded-3" id="inp_ngaySinh" name="ngaySinh" value="${fmtDate}" />
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-bold text-muted"><s:message code="student.lbl.currentClass"/></label>
                                    <%-- form:input path="maLop" readonly vì lớp được chọn từ sidebar, không cho sửa --%>
                                    <form:input path="maLop" cssClass="form-control rounded-3 bg-light" readonly="true"/>
                                </div>
                                <div class="col-md-12">
                                    <label class="form-label small fw-bold text-muted"><s:message code="student.address"/></label>
                                    <s:message code="student.example.address" var="lblStudentExampleAddress"/>
                                    <form:input path="diaChi" cssClass="form-control rounded-3"
                                        id="inp_diaChi" placeholder="${lblStudentExampleAddress}"/>
                                </div>
                                <div class="col-md-12">
                                    <div class="form-check form-switch p-0 d-flex align-items-center gap-2">
                                        <%-- form:checkbox tự động render checked dựa vào giá trị boolean --%>
                                        <form:checkbox path="daNghiHoc" cssClass="form-check-input ms-0 mt-0"
                                            id="daNghiHoc" value="true"/>
                                        <label class="form-check-label fw-bold text-danger" for="daNghiHoc"><s:message code="student.status.dropped"/></label>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="modal-footer border-0 px-4 pb-4">
                            <a href="${pageContext.request.contextPath}/admin/student?maLop=${maLop}" class="btn btn-light rounded-3 fw-bold"><s:message code="global.btn.cancel"/></a>
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

    <!-- IMPORT CSV MODAL -->
    <div class="modal fade" id="importCsvModal" tabindex="-1" aria-labelledby="importCsvModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg rounded-4">
                <div class="modal-header bg-info text-white border-0 py-3 px-4 rounded-top-4">
                    <h5 class="modal-title fw-bold d-flex align-items-center gap-2" id="importCsvModalLabel">
                        <i class="bi bi-file-earmark-arrow-up-fill"></i>
                        <s:message code="student.import.modal.title"/>
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="${pageContext.request.contextPath}/admin/student/import" method="POST" enctype="multipart/form-data">
                    <input type="hidden" name="csrf_token" value="${csrfToken}" />
                    <div class="modal-body p-4">
                        <!-- Hướng dẫn cấu trúc file -->
                        <div class="alert alert-info border-0 rounded-3 mb-3 small">
                            <h6 class="fw-bold mb-2"><i class="bi bi-info-circle-fill me-1"></i> <s:message code="student.import.guide.title"/></h6>
                            <ul class="ps-3 mb-2" style="list-style-type: disc;">
                                <li><s:message code="student.import.guide.headers"/> <strong>MASV, HOTEN, GIOITINH, NGAYSINH, EMAIL, MALOP</strong></li>
                                <li><s:message code="student.import.guide.separator"/> <strong>,</strong></li>
                                <li><s:message code="student.import.guide.encoding"/></li>
                                <li><s:message code="student.import.guide.dob"/></li>
                                <li><s:message code="student.import.guide.gender"/></li>
                            </ul>
                            <div class="text-muted small border-top pt-2 mt-2">
                                <strong><s:message code="student.import.guide.example"/></strong><br>
                                <code>MASV,HOTEN,GIOITINH,NGAYSINH,EMAIL,MALOP</code><br>
                                <code>N21DCCN001,Nguyễn Văn A,Nam,2003-05-15,n21dccn001@student.ptit.edu.vn,D21CQCN01</code>
                            </div>
                        </div>

                        <!-- Chọn tệp dịch chuyển label -->
                        <div class="mb-3">
                            <label class="form-label small fw-bold text-muted"><s:message code="student.import.file.select"/></label>
                            <div class="input-group">
                                <input type="file" name="file" accept=".csv" required class="form-control d-none" id="csvFileInput" onchange="updateFileName(this)">
                                <label for="csvFileInput" class="btn btn-outline-secondary rounded-start-3 mb-0" style="border-top-left-radius: 12px; border-bottom-left-radius: 12px;"><s:message code="student.import.file.selectBtn"/></label>
                                <span class="form-control text-muted bg-light" id="csvFileName" data-no-file-text="<s:message code='student.import.file.noFile'/>" style="border-top-right-radius: 12px; border-bottom-right-radius: 12px; font-size: 0.9rem; line-height: 24px; display: flex; align-items: center;"><s:message code="student.import.file.noFile"/></span>
                            </div>
                            <div class="form-text text-muted small mt-1"><s:message code="student.import.file.hint"/></div>
                        </div>
                    </div>
                    <div class="modal-footer border-0 px-4 pb-4">
                        <button type="button" class="btn btn-light rounded-3 fw-bold" data-bs-dismiss="modal"><s:message code="global.btn.cancel"/></button>
                        <button type="submit" class="btn btn-info text-white rounded-3 fw-bold px-4"><s:message code="student.import.btn.submit"/></button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function updateFileName(input) {
            const nameSpan = document.getElementById('csvFileName');
            if (input.files && input.files.length > 0) {
                nameSpan.textContent = input.files[0].name;
                nameSpan.classList.remove('text-muted');
            } else {
                nameSpan.textContent = nameSpan.getAttribute('data-no-file-text');
                nameSpan.classList.add('text-muted');
            }
        }

        function normalizeVN(str) {
            return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/\u0111/g, 'd').replace(/\u0110/g, 'D').toLowerCase();
        }

        function filterLocalClasses() {
            const val = normalizeVN(document.getElementById('class-search').value);
            const rows = document.querySelectorAll('#class-table-body tr');
            rows.forEach(row => {
                row.style.display = normalizeVN(row.innerText).includes(val) ? '' : 'none';
            });
        }

        function filterLocalStudents() {
            const val = normalizeVN(document.getElementById('student-search').value);
            const rows = document.querySelectorAll('#main-student-table-body tr');
            rows.forEach(row => {
                row.style.display = normalizeVN(row.innerText).includes(val) ? '' : 'none';
            });
        }
    </script>
</body>
</html>
