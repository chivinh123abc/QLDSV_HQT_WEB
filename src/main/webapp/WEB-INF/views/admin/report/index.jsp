<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title><s:message code="report.page.title"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <style>
        .report-card {
            transition: transform 0.2s;
            cursor: pointer;
            border: 2px solid transparent;
            text-decoration: none;
            color: inherit;
        }
        .report-card:hover {
            transform: translateY(-5px);
            border-color: #0d6efd;
        }
        .report-card.active {
            border-color: #0d6efd;
            background-color: #f8fbff;
        }
        .filter-pane {
            background: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.05);
            margin-bottom: 20px;
        }
        .report-result {
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.05);
            overflow: hidden;
            margin-top: 20px;
        }
        @media print {
            .app-layout { display: block !important; }
            .app-sidebar, .header-container, .btn-print-hide, .filter-pane, .report-card-row { display: none !important; }
            .app-main { margin-left: 0 !important; width: 100% !important; }
            .report-result { box-shadow: none !important; border: 0 !important; }
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

            <main id="main-content" class="app-content">
                <div class="container-fluid p-4">
                    <h2 class="mb-4 btn-print-hide"><i class="bi bi-file-earmark-bar-graph text-primary"></i> <s:message code="report.system"/></h2>

                    <!-- FLASH MESSAGES -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show rounded-3 shadow-sm border-0 mb-4 btn-print-hide" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i> ${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <div class="row g-4 mb-4 report-card-row btn-print-hide">
                        <div class="col-md-6">
                            <a href="${pageContext.request.contextPath}/admin/report?reportType=summary" class="card h-100 report-card ${empty reportType || reportType == 'summary' ? 'active' : ''}" id="cardSummary">
                                <div class="card-body text-center py-4">
                                    <i class="bi bi-grid-3x3-gap fs-1 text-primary mb-3"></i>
                                    <h4><s:message code="report.summary.grades"/></h4>
                                    <p class="text-muted"><s:message code="report.view.grades.desc"/></p>
                                </div>
                            </a>
                        </div>
                        <div class="col-md-6">
                            <a href="${pageContext.request.contextPath}/admin/report?reportType=students" class="card h-100 report-card ${reportType == 'students' ? 'active' : ''}" id="cardStudents">
                                <div class="card-body text-center py-4">
                                    <i class="bi bi-people fs-1 text-success mb-3"></i>
                                    <h4><s:message code="report.student.list"/></h4>
                                    <p class="text-muted"><s:message code="report.view.students.desc"/></p>
                                </div>
                            </a>
                        </div>
                    </div>

                    <!-- Filters for Summary Marks -->
                    <c:if var="isSummary" test="${empty reportType || reportType == 'summary'}">
                        <div class="filter-pane btn-print-hide" id="filterSummary">
                            <form action="${pageContext.request.contextPath}/admin/report" method="GET">
                                <input type="hidden" name="reportType" value="summary" />
                                <div class="row g-3 align-items-end">
                                    <div class="col-md-4">
                                        <label class="form-label fw-bold text-muted small"><s:message code="global.lbl.selectClass"/></label>
                                        <select class="form-select" name="maLop" required>
                                            <option value=""><s:message code="classroom.management"/></option>
                                            <c:forEach var="lop" items="${lopList}">
                                                <option value="${lop.maLop}" ${maLop == lop.maLop ? 'selected' : ''}>${lop.maLop} - ${lop.tenLop}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-2">
                                        <button type="submit" class="btn btn-primary w-100">
                                            <i class="bi bi-play-fill"></i> <s:message code="report.btn.create"/>
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </c:if>

                    <!-- Filters for Credit Class Students -->
                    <c:if test="${reportType == 'students'}">
                        <div class="filter-pane btn-print-hide" id="filterStudents">
                            <form action="${pageContext.request.contextPath}/admin/report" method="GET">
                                <input type="hidden" name="reportType" value="students" />
                                <div class="row g-3">
                                    <div class="col-md-3">
                                        <label class="form-label fw-bold text-muted small"><s:message code="global.lbl.academicYear"/></label>
                                        <select class="form-select" name="nienKhoa" onchange="this.form.submit()" required>
                                            <option value=""><s:message code="mark.select.academic.year"/></option>
                                            <c:forEach var="nk" items="${nienKhoaList}">
                                                <option value="${nk}" ${nienKhoa == nk ? 'selected' : ''}>${nk}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-2">
                                        <label class="form-label fw-bold text-muted small"><s:message code="global.lbl.semester"/></label>
                                        <select class="form-select" name="hocKy" onchange="this.form.submit()" required>
                                            <option value=""><s:message code="mark.select.semester"/></option>
                                            <option value="1" ${hocKy == 1 ? 'selected' : ''}>1</option>
                                            <option value="2" ${hocKy == 2 ? 'selected' : ''}>2</option>
                                            <option value="3" ${hocKy == 3 ? 'selected' : ''}>3</option>
                                        </select>
                                    </div>
                                    <div class="col-md-4">
                                        <label class="form-label fw-bold text-muted small"><s:message code="global.lbl.subject"/></label>
                                        <select class="form-select" name="maMH" onchange="this.form.submit()" ${empty subjectList ? 'disabled' : ''} required>
                                            <option value=""><s:message code="mark.select.subject"/></option>
                                            <c:forEach var="sb" items="${subjectList}">
                                                <option value="${sb[0]}" ${maMH == sb[0] ? 'selected' : ''}>[${sb[0]}] ${sb[1]}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-2">
                                        <label class="form-label fw-bold text-muted small"><s:message code="global.lbl.group"/></label>
                                        <select class="form-select" name="nhom" onchange="this.form.submit()" ${empty groupList ? 'disabled' : ''} required>
                                            <option value=""><s:message code="mark.group"/></option>
                                            <c:forEach var="g" items="${groupList}">
                                                <option value="${g}" ${nhom == g ? 'selected' : ''}>${g}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-1 d-flex align-items-end">
                                        <button type="submit" class="btn btn-success w-100">
                                            <i class="bi bi-play-fill"></i>
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </c:if>

                    <c:if test="${showResult}">
                        <div class="report-result" id="resultArea">
                            <div class="p-3 border-bottom d-flex justify-content-between align-items-center btn-print-hide">
                                <h5 class="mb-0 fw-bold text-primary" id="resultTitle">${resultTitle}</h5>
                                <button class="btn btn-sm btn-outline-secondary rounded-pill px-3 fw-bold" onclick="window.print()">
                                    <i class="bi bi-printer me-1"></i> <s:message code="report.btn.print"/>
                                </button>
                            </div>
                            <div class="p-4 print-only d-none d-print-block text-center border-bottom">
                                <h3 class="fw-bold text-dark mb-1">${resultTitle}</h3>
                                <p class="text-muted small mb-0"><s:message code="report.system.print.date"/></p>
                            </div>
                            <div class="table-responsive p-0">
                                <table class="table table-bordered table-striped mb-0" id="reportTable">
                                    <c:choose>
                                        <c:when test="${isSummary}">
                                            <thead>
                                                <tr>
                                                    <c:forEach var="col" items="${summaryColumns}">
                                                        <th class="bg-light text-muted small fw-bold">${col}</th>
                                                    </c:forEach>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="row" items="${summaryData}">
                                                    <tr>
                                                        <c:forEach var="col" items="${summaryColumns}">
                                                            <td>${row[col] != null ? row[col] : '-'}</td>
                                                        </c:forEach>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </c:when>
                                        <c:otherwise>
                                            <thead>
                                                <tr>
                                                    <th class="bg-light text-muted small fw-bold">MASV</th>
                                                    <th class="bg-light text-muted small fw-bold"><s:message code="report.lbl.lastName"/></th>
                                                    <th class="bg-light text-muted small fw-bold"><s:message code="report.lbl.firstName"/></th>
                                                    <th class="bg-light text-muted small fw-bold"><s:message code="report.lbl.gender"/></th>
                                                    <th class="bg-light text-muted small fw-bold"><s:message code="report.lbl.classCode"/></th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="row" items="${studentData}">
                                                    <tr>
                                                        <td>${row.MASV}</td>
                                                        <td>${row.HO}</td>
                                                        <td>${row.TEN}</td>
                                                        <td>${row.PHAI}</td>
                                                        <td>${row.MALOP}</td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </c:otherwise>
                                    </c:choose>
                                </table>
                            </div>
                        </div>
                    </c:if>

                </div>
            </main>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
