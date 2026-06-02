<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title><s:message code="registration.page.title"/></title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <style>
        .badge-soft-primary { background-color: #e0f2fe; color: #0369a1; border-radius: 20px; font-weight: 600; padding: 6px 12px; }
        .badge-soft-success { background-color: #dcfce7; color: #166534; border-radius: 20px; font-weight: 600; padding: 6px 12px; }
        .badge-soft-danger { background-color: #fee2e2; color: #991b1b; border-radius: 20px; font-weight: 600; padding: 6px 12px; }
        .table-custom th { font-size: 0.85rem; color: #64748b; font-weight: 700; text-transform: uppercase; border-bottom: 2px solid #e2e8f0; }
        .table-custom td { vertical-align: middle; padding: 1rem 0.75rem; border-bottom: 1px solid #f1f5f9; }
        .registration-card { transition: all 0.3s; border: 1px solid #e2e8f0; }
        .registration-card:hover { transform: translateY(-5px); box-shadow: 0 10px 20px rgba(0,0,0,0.05) !important; border-color: #4361ee; }
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
                    
                    <div class="d-flex align-items-center justify-content-between mb-4">
                        <div class="d-flex align-items-center gap-3">
                            <div class="bg-primary bg-opacity-10 text-primary p-2 rounded-3">
                                <i class="bi bi-person-check-fill fs-3"></i>
                            </div>
                            <div>
                                <h3 class="mb-0 fw-bold text-dark"><s:message code="registration.title"/></h3>
                                <p class="text-muted small mb-0"><s:message code="registration.management.desc"/></p>
                            </div>
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

                    <div class="row g-4">
                        <!-- LEFT SIDE: STUDENT INFO & SEARCH -->
                        <div class="col-lg-4">
                            <div class="card border-0 shadow-sm rounded-4 mb-4">
                                <div class="card-body p-4">
                                    <h6 class="fw-bold text-primary mb-3"><s:message code="registration.search.student"/></h6>
                                    
                                    <!-- Search form for PGV -->
                                    <c:choose>
                                        <c:when test="${sessionScope.role == 'SINHVIEN'}">
                                            <!-- Hidden or read-only student display for student role -->
                                            <div class="bg-light p-3 rounded-3">
                                                <div class="d-flex align-items-center gap-3">
                                                    <div class="bg-white p-2 rounded-circle border shadow-sm">
                                                        <i class="bi bi-person-circle fs-3 text-primary"></i>
                                                    </div>
                                                    <div>
                                                        <h5 class="fw-bold mb-0 text-dark">${selectedStudent.ho} ${selectedStudent.ten}</h5>
                                                        <span class="badge bg-primary rounded-pill small">${selectedStudent.maSV}</span>
                                                    </div>
                                                </div>
                                                <div class="small text-muted mt-3 mb-1"><i class="bi bi-building me-2"></i><s:message code="registration.class.label"/> <strong class="text-dark">${selectedStudent.maLop}</strong></div>
                                                <div class="small text-muted"><i class="bi bi-info-circle me-2"></i><s:message code="dashboard.status"/>: 
                                                    <span class="badge ${selectedStudent.daNghiHoc ? 'bg-danger' : 'bg-success'} bg-opacity-10 ${selectedStudent.daNghiHoc ? 'text-danger' : 'text-success'} rounded-pill">
                                                        ${selectedStudent.daNghiHoc ? 'Đã nghỉ học' : 'Đang học'}
                                                    </span>
                                                </div>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <!-- Standard search input for staff -->
                                            <form action="${pageContext.request.contextPath}/registration" method="GET" class="mb-3">
                                                <div class="input-group shadow-sm rounded-3 overflow-hidden">
                                                    <span class="input-group-text bg-white border-end-0"><i class="bi bi-person-badge"></i></span>
                                                    <input type="text" name="maSV" class="form-control border-start-0 ps-0" placeholder="<s:message code="registration.enter.student.id"/>" value="${selectedStudent != null ? selectedStudent.maSV : ''}" required>
                                                    <button class="btn btn-primary fw-bold" type="submit"><s:message code="registration.btn.search"/></button>
                                                </div>
                                            </form>
                                            
                                            <c:choose>
                                                <c:when test="${not empty selectedStudent}">
                                                    <div class="bg-light p-3 rounded-3">
                                                        <div class="d-flex align-items-center gap-3 mb-3">
                                                            <div class="bg-white p-2 rounded-circle border shadow-sm">
                                                                <i class="bi bi-person-circle fs-3 text-primary"></i>
                                                            </div>
                                                            <div>
                                                                <h5 class="fw-bold mb-0 text-dark">${selectedStudent.ho} ${selectedStudent.ten}</h5>
                                                                <span class="badge bg-primary rounded-pill small">${selectedStudent.maSV}</span>
                                                            </div>
                                                        </div>
                                                        <div class="small text-muted mb-1"><i class="bi bi-building me-2"></i><s:message code="registration.class.label"/> <strong class="text-dark">${selectedStudent.maLop}</strong></div>
                                                        <div class="small text-muted"><i class="bi bi-info-circle me-2"></i><s:message code="dashboard.status"/>: 
                                                            <span class="badge ${selectedStudent.daNghiHoc ? 'bg-danger' : 'bg-success'} bg-opacity-10 ${selectedStudent.daNghiHoc ? 'text-danger' : 'text-success'} rounded-pill">
                                                                ${selectedStudent.daNghiHoc ? 'Đã nghỉ học' : 'Đang học'}
                                                            </span>
                                                        </div>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="text-center py-4 text-muted small">
                                                        <i class="bi bi-search fs-2 d-block mb-2 opacity-25"></i>
                                                        <s:message code="registration.please.enter.id"/>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <!-- REGISTERED CLASSES LIST -->
                            <div class="card border-0 shadow-sm rounded-4">
                                <div class="card-body p-4">
                                    <h6 class="fw-bold text-success mb-3"><s:message code="registration.registered.classes"/></h6>
                                    <c:choose>
                                        <c:when test="${empty selectedStudent}">
                                            <div class="text-center py-4 text-muted small"><s:message code="registration.please.search.first"/></div>
                                        </c:when>
                                        <c:when test="${empty myRegistrations}">
                                            <div class="text-center py-4 text-muted small"><s:message code="registration.no.subjects"/></div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="list-group list-group-flush">
                                                <c:forEach var="reg" items="${myRegistrations}">
                                                    <div class="list-group-item px-0 py-3 d-flex justify-content-between align-items-center">
                                                        <div>
                                                            <div class="fw-bold text-dark small"><s:message code="registration.credit.class.id"/></div>
                                                            <div class="text-muted" style="font-size: 0.75rem;"><s:message code="registration.subject.group"/></div>
                                                        </div>
                                                        <!-- Pure HTML Form to Cancel Registration (No maSV passed in parameters) -->
                                                        <form action="${pageContext.request.contextPath}/registration" method="POST" onsubmit="return confirm('Bạn có chắc chắn muốn hủy đăng ký lớp này không?');">
                                                            <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                                            <input type="hidden" name="maLTC" value="${reg.lopTinChi.maLTC}">
                                                            <button type="submit" name="btnDelete" class="btn btn-xs btn-outline-danger border-0">
                                                                <i class="bi bi-x-circle-fill"></i> <s:message code="registration.btn.cancel"/>
                                                            </button>
                                                        </form>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>

                        <!-- RIGHT SIDE: AVAILABLE CLASSES -->
                        <div class="col-lg-8">
                            <div class="card border-0 shadow-sm rounded-4">
                                <div class="card-header bg-white border-bottom-0 pt-4 px-4 pb-0 d-flex justify-content-between align-items-center">
                                    <h6 class="fw-bold text-dark mb-0"><s:message code="registration.available.classes"/></h6>
                                    <div class="input-group" style="max-width: 200px;">
                                        <span class="input-group-text bg-light border-0"><i class="bi bi-funnel"></i></span>
                                        <input type="text" id="ltc-search" class="form-control bg-light border-0 small" placeholder="<s:message code="registration.filter"/>" onkeyup="filterLTC()">
                                    </div>
                                </div>
                                <div class="card-body p-4">
                                    <div class="table-responsive rounded-3 border">
                                        <table class="table table-custom align-middle mb-0">
                                            <thead class="table-light">
                                                <tr>
                                                    <th class="px-3"><s:message code="credit-class.lbl.classId"/></th>
                                                    <th><s:message code="registration.lbl.subjectGroup"/></th>
                                                    <th><s:message code="credit-class.lbl.yearSemester"/></th>
                                                    <th class="text-center"><s:message code="global.lbl.actions"/></th>
                                                </tr>
                                            </thead>
                                            <tbody id="available-ltc-body">
                                                <c:forEach var="item" items="${availableClasses}">
                                                    <c:set var="isRegistered" value="false" />
                                                    <c:forEach var="regId" items="${registeredLtcIds}">
                                                        <c:if test="${regId == item.maLTC}">
                                                            <c:set var="isRegistered" value="true" />
                                                        </c:if>
                                                    </c:forEach>
                                                    
                                                    <c:set var="isSameSubjectRegistered" value="false" />
                                                    <c:if test="${not isRegistered}">
                                                        <c:set var="key" value="${item.monHoc.maMH.trim().toUpperCase()}-${item.nienKhoa.trim().toUpperCase()}-${item.hocKy}" />
                                                        <c:forEach var="subjSem" items="${registeredSubjectSemesters}">
                                                            <c:if test="${subjSem == key}">
                                                                 <c:set var="isSameSubjectRegistered" value="true" />
                                                            </c:if>
                                                        </c:forEach>
                                                    </c:if>
                                                    
                                                    <tr>
                                                        <td class="px-3"><span class="badge-soft-primary">${item.maLTC}</span></td>
                                                        <td>
                                                            <div class="fw-bold text-dark">${item.monHoc.maMH} - ${item.monHoc.tenMH}</div>
                                                            <div class="small text-muted"><s:message code="registration.group.info"/></div>
                                                        </td>
                                                        <td>
                                                            <div class="fw-bold text-dark">${item.nienKhoa}</div>
                                                            <div class="small text-muted"><s:message code="registration.semester"/></div>
                                                        </td>
                                                        <td class="text-center">
                                                            <c:choose>
                                                                <c:when test="${isRegistered}">
                                                                    <!-- Pure HTML Form to Cancel Registration (No maSV passed in parameters) -->
                                                                    <form action="${pageContext.request.contextPath}/registration" method="POST" onsubmit="return confirm('Bạn có chắc chắn muốn hủy đăng ký lớp này không?');">
                                                                        <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                                                        <input type="hidden" name="maLTC" value="${item.maLTC}">
                                                                        <button type="submit" name="btnDelete" class="btn btn-sm btn-outline-danger rounded-3 px-3">
                                                                            <i class="bi bi-x-circle me-1"></i> <s:message code="registration.btn.unregister"/>
                                                                        </button>
                                                                    </form>
                                                                </c:when>
                                                                <c:when test="${isSameSubjectRegistered}">
                                                                    <button class="btn btn-sm btn-secondary rounded-3 px-3" disabled title="<s:message code="registration.already.registered"/>">
                                                                        <i class="bi bi-dash-circle me-1"></i> <s:message code="registration.already.registered.label"/>
                                                                    </button>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <!-- Pure HTML Form to Register (No maSV passed in parameters) -->
                                                                    <form action="${pageContext.request.contextPath}/registration" method="POST">
                                                                        <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                                                        <input type="hidden" name="maLTC" value="${item.maLTC}">
                                                                        <button type="submit" name="btnInsert" class="btn btn-sm btn-primary rounded-3 px-3" <c:if test="${empty selectedStudent}"><s:message code="registration.disabled.search.first"/></c:if>>
                                                                            <i class="bi bi-plus-circle me-1"></i> <s:message code="registration.btn.register"/>
                                                                        </button>
                                                                    </form>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                <c:if test="${empty availableClasses}">
                                                    <tr>
                                                        <td colspan="4" class="text-center py-4 text-muted"><s:message code="registration.no.classes"/></td>
                                                    </tr>
                                                </c:if>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function normalizeVN(str) {
            return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/\u0111/g, 'd').replace(/\u0110/g, 'D').toLowerCase();
        }

        function filterLTC() {
            const val = normalizeVN(document.getElementById('ltc-search').value);
            document.querySelectorAll('#available-ltc-body tr').forEach(row => {
                row.style.display = normalizeVN(row.innerText).includes(val) ? '' : 'none';
            });
        }
    </script>
</body>
</html>
