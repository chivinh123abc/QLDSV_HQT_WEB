<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title><s:message code="mark.student.page.title"/></title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700;800&family=Plus+Jakarta+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet" />
    <style>
        body {
            font-family: 'Plus Jakarta Sans', sans-serif;
            background-color: #f8fafc;
        }
        h1, h2, h3, h4, h5, h6, .fw-bold {
            font-family: 'Outfit', sans-serif;
        }
        .grade-card {
            border: none;
            border-radius: 20px;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            background: rgba(255, 255, 255, 0.8);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.4);
        }
        .grade-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 30px rgba(0,0,0,0.05) !important;
        }
        .grade-badge {
            font-weight: 700;
            padding: 6px 14px;
            border-radius: 12px;
            font-size: 0.85rem;
            display: inline-block;
        }
        .grade-a { background-color: #dcfce7; color: #166534; border: 1px solid #bbf7d0; }
        .grade-b { background-color: #e0f2fe; color: #0369a1; border: 1px solid #bae6fd; }
        .grade-c { background-color: #fef3c7; color: #92400e; border: 1px solid #fde68a; }
        .grade-f { background-color: #fee2e2; color: #991b1b; border: 1px solid #fecaca; }
        .grade-none { background-color: #f1f5f9; color: #475569; border: 1px solid #cbd5e1; }

        .table-custom th {
            font-size: 0.8rem;
            color: #64748b;
            font-weight: 700;
            text-transform: uppercase;
            border-bottom: 2px solid #e2e8f0;
            letter-spacing: 0.5px;
            padding: 1rem 0.75rem;
        }
        .table-custom td {
            vertical-align: middle;
            padding: 0.9rem 0.75rem;
            border-bottom: 1px solid #f1f5f9;
        }
        .semester-header {
            background: linear-gradient(90deg, #4361ee, #4cc9f0);
            padding: 12px 24px;
            border-radius: 16px;
            font-weight: 700;
            color: white;
            box-shadow: 0 4px 15px rgba(67, 97, 238, 0.15);
        }
        .bg-gradient-primary { background: linear-gradient(135deg, #4361ee, #4cc9f0); color: white; }
        .bg-gradient-success { background: linear-gradient(135deg, #10b981, #34d399); color: white; }
        .bg-gradient-warning { background: linear-gradient(135deg, #f59e0b, #fbbf24); color: white; }
        .bg-gradient-danger { background: linear-gradient(135deg, #ef4444, #f87171); color: white; }
        
        .summary-box {
            background-color: #ffffff;
            border-radius: 16px;
            border: 1px solid #e2e8f0;
            transition: all 0.2s ease;
        }
        .summary-box:hover {
            border-color: #cbd5e1;
            box-shadow: 0 8px 20px rgba(0,0,0,0.02);
        }
        
        @media print {
            .app-layout { display: block !important; }
            .sidebar-nav, .header-container, .btn-print-hide, .filter-panel { display: none !important; }
            .app-main { margin-left: 0 !important; width: 100% !important; }
            .semester-header { background: #f1f5f9 !important; color: #000000 !important; box-shadow: none !important; border: 1px solid #cbd5e1; }
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
                    
                    <!-- TITLE & INFO -->
                    <div class="d-flex align-items-center justify-content-between mb-4 btn-print-hide">
                        <div class="d-flex align-items-center gap-3">
                            <div class="bg-primary bg-opacity-10 text-primary p-2 rounded-3">
                                <i class="bi bi-mortarboard fs-3"></i>
                            </div>
                            <div>
                                <h3 class="mb-0 fw-bold text-dark"><s:message code="mark.student.personal.results"/></h3>
                                <p class="text-muted small mb-0"><s:message code="mark.student.grade.profile"/> <strong class="text-primary">${student.ho} ${student.ten}</strong> <s:message code="mark.student.class.info" arguments="${student.maSV},${student.maLop}"/></p>
                            </div>
                        </div>
                        <button class="btn btn-outline-primary fw-bold rounded-pill px-4" onclick="window.print()">
                            <i class="bi bi-printer me-2"></i> <s:message code="mark.student.btn.print"/>
                        </button>
                    </div>

                    <!-- STATISTICS TILES -->
                    <div class="row g-4 mb-4 btn-print-hide">
                        <div class="col-md-3">
                            <div class="card grade-card shadow-sm h-100">
                                <div class="card-body p-4 text-center">
                                    <h6 class="text-muted fw-bold small text-uppercase mb-2"><s:message code="mark.student.registered.subjects"/></h6>
                                    <h2 id="total-subjects" class="fw-bold mb-0 text-primary">0</h2>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card grade-card shadow-sm h-100">
                                <div class="card-body p-4 text-center">
                                    <h6 class="text-muted fw-bold small text-uppercase mb-2"><s:message code="mark.student.accumulated.credits"/></h6>
                                    <h2 id="passed-credits-stat" class="fw-bold mb-0 text-success">0</h2>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card grade-card shadow-sm h-100">
                                <div class="card-body p-4 text-center">
                                    <h6 class="text-muted fw-bold small text-uppercase mb-2"><s:message code="mark.student.failed.subjects"/></h6>
                                    <h2 id="failed-subjects" class="fw-bold mb-0 text-danger">0</h2>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card grade-card shadow-sm h-100">
                                <div class="card-body p-4 text-center">
                                    <h6 class="text-muted fw-bold small text-uppercase mb-2"><s:message code="mark.student.gpa.scale10"/></h6>
                                    <h2 id="average-gpa-stat" class="fw-bold mb-0 text-warning">0.00</h2>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- SEARCH AND FILTER PANEL -->
                    <div class="card border-0 shadow-sm rounded-4 mb-4 filter-panel btn-print-hide">
                        <div class="card-body p-3 d-flex flex-wrap gap-3 align-items-center justify-content-between">
                            <div class="input-group" style="max-width: 300px;">
                                <span class="input-group-text bg-light border-0"><i class="bi bi-search"></i></span>
                                <s:message code="mark.student.search.subject" var="lblMarkStudentSearchSubject"/>
                                <input type="text" id="subject-search" class="form-control bg-light border-0" placeholder="${lblMarkStudentSearchSubject}" onkeyup="filterGrades()">
                            </div>
                            <div class="d-flex align-items-center gap-2">
                                <label for="semester-filter" class="text-muted small fw-bold mb-0 text-nowrap"><s:message code="mark.student.display.semester"/></label>
                                <select id="semester-filter" class="form-select bg-light border-0" onchange="filterGrades()">
                                    <option value="all"><s:message code="mark.student.all.semesters"/></option>
                                    <!-- Dynamic options filled by script -->
                                </select>
                            </div>
                        </div>
                    </div>

                    <!-- SEMESTER SECTIONS -->
                    <c:choose>
                        <c:when test="${empty groupedMarks}">
                            <div class="card border-0 shadow-sm rounded-4 p-5 text-center text-muted">
                                <i class="bi bi-emoji-neutral fs-1 d-block mb-3 opacity-25"></i>
                                <s:message code="mark.student.no.data"/>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div id="semesters-container">
                                <c:forEach var="entry" items="${groupedMarks}">
                                    <div class="semester-section mb-5" data-semester-name="${entry.key}">
                                        <!-- Header Học kỳ -->
                                        <div class="semester-header mb-3 d-flex align-items-center justify-content-between">
                                            <span class="fs-5"><i class="bi bi-calendar3 me-2"></i>${entry.key}</span>
                                            <span class="badge bg-white text-primary rounded-pill px-3 py-1 small fw-bold semester-subjects-count"><s:message code="mark.student.zero.subjects"/></span>
                                        </div>

                                        <!-- Bảng điểm chi tiết học kỳ -->
                                        <div class="card border-0 shadow-sm rounded-4 overflow-hidden mb-3">
                                            <div class="card-body p-0">
                                                <div class="table-responsive">
                                                    <s:message code="mark.student.accumulated.success" var="lblMarkAccumulatedSuccess"/>
                                                    <s:message code="mark.student.failed.retake" var="lblMarkFailedRetake"/>
                                                    <s:message code="mark.student.incomplete.grade" var="lblMarkIncompleteGrade"/>
                                                    <table class="table table-custom align-middle mb-0">
                                                        <thead class="table-light">
                                                            <tr>
                                                                <th class="text-center" style="width: 60px;">STT</th>
                                                                <th style="width: 140px;"><s:message code="global.lbl.subjectCode"/></th>
                                                                <th><s:message code="global.lbl.subjectName"/></th>
                                                                <th class="text-center" style="width: 80px;"><s:message code="global.lbl.group"/></th>
                                                                <th class="text-center" style="width: 90px;"><s:message code="global.lbl.credits"/></th>
                                                                <th class="text-center" style="width: 110px;">CC (10%)</th>
                                                                <th class="text-center" style="width: 110px;">GK (30%)</th>
                                                                <th class="text-center" style="width: 110px;">CK (60%)</th>
                                                                <th class="text-center" style="width: 110px;"><s:message code="global.lbl.finalScore"/></th>
                                                                <th class="text-center" style="width: 100px;"><s:message code="global.lbl.grade"/></th>
                                                                <th class="text-center" style="width: 110px;"><s:message code="global.lbl.passed"/></th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:forEach var="row" items="${entry.value}" varStatus="status">
                                                                <!-- Calculate hours/periods into Standard PTIT Credits -->
                                                                <c:set var="tietLT" value="${row[8] != null ? row[8] : 0}" />
                                                                <c:set var="tietTH" value="${row[9] != null ? row[9] : 0}" />
                                                                <c:set var="tinChi" value="${(tietLT + tietTH) / 15}" />
                                                                <fmt:formatNumber var="tinChiRounded" value="${tinChi}" maxFractionDigits="0" />
                                                                <c:if test="${tinChiRounded < 1}"><c:set var="tinChiRounded" value="2" /></c:if>

                                                                <!-- Grade system conversion variables -->
                                                                <c:choose>
                                                                    <c:when test="${row[5] != null && row[6] != null && row[7] != null}">
                                                                        <c:set var="finalScore" value="${row[5]*0.1 + row[6]*0.3 + row[7]*0.6}" />
                                                                        <c:choose>
                                                                            <c:when test="${finalScore >= 9.0}">
                                                                                <c:set var="letterGrade" value="A+" />
                                                                                <c:set var="point4" value="4.0" />
                                                                                <c:set var="statusClass" value="grade-a" />
                                                                                <c:set var="isPassed" value="true" />
                                                                            </c:when>
                                                                            <c:when test="${finalScore >= 8.5}">
                                                                                <c:set var="letterGrade" value="A" />
                                                                                <c:set var="point4" value="3.7" />
                                                                                <c:set var="statusClass" value="grade-a" />
                                                                                <c:set var="isPassed" value="true" />
                                                                            </c:when>
                                                                            <c:when test="${finalScore >= 8.0}">
                                                                                <c:set var="letterGrade" value="B+" />
                                                                                <c:set var="point4" value="3.5" />
                                                                                <c:set var="statusClass" value="grade-b" />
                                                                                <c:set var="isPassed" value="true" />
                                                                            </c:when>
                                                                            <c:when test="${finalScore >= 7.0}">
                                                                                <c:set var="letterGrade" value="B" />
                                                                                <c:set var="point4" value="3.0" />
                                                                                <c:set var="statusClass" value="grade-b" />
                                                                                <c:set var="isPassed" value="true" />
                                                                            </c:when>
                                                                            <c:when test="${finalScore >= 6.5}">
                                                                                <c:set var="letterGrade" value="C+" />
                                                                                <c:set var="point4" value="2.5" />
                                                                                <c:set var="statusClass" value="grade-c" />
                                                                                <c:set var="isPassed" value="true" />
                                                                            </c:when>
                                                                            <c:when test="${finalScore >= 5.5}">
                                                                                <c:set var="letterGrade" value="C" />
                                                                                <c:set var="point4" value="2.0" />
                                                                                <c:set var="statusClass" value="grade-c" />
                                                                                <c:set var="isPassed" value="true" />
                                                                            </c:when>
                                                                            <c:when test="${finalScore >= 5.0}">
                                                                                <c:set var="letterGrade" value="D+" />
                                                                                <c:set var="point4" value="1.5" />
                                                                                <c:set var="statusClass" value="grade-c" />
                                                                                <c:set var="isPassed" value="true" />
                                                                            </c:when>
                                                                            <c:when test="${finalScore >= 4.0}">
                                                                                <c:set var="letterGrade" value="D" />
                                                                                <c:set var="point4" value="1.0" />
                                                                                <c:set var="statusClass" value="grade-c" />
                                                                                <c:set var="isPassed" value="true" />
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <c:set var="letterGrade" value="F" />
                                                                                <c:set var="point4" value="0.0" />
                                                                                <c:set var="statusClass" value="grade-f" />
                                                                                <c:set var="isPassed" value="false" />
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <c:set var="letterGrade" value="-" />
                                                                        <c:set var="point4" value="-" />
                                                                        <c:set var="statusClass" value="grade-none" />
                                                                        <c:set var="isPassed" value="false" />
                                                                    </c:otherwise>
                                                                </c:choose>

                                                                <tr class="grade-row" 
                                                                    data-credits="${tinChiRounded}" 
                                                                    data-score10="${finalScore}" 
                                                                    data-score4="${point4}" 
                                                                    data-passed="${isPassed}"
                                                                    data-completed="${row[5] != null && row[6] != null && row[7] != null}"
                                                                    data-subject-name="${row[3]}">
                                                                    <td class="text-center fw-bold text-muted">${status.index + 1}</td>
                                                                    <td><span class="badge bg-light text-dark font-monospace border">${row[2]}</span></td>
                                                                    <td class="fw-bold text-dark">${row[3]}</td>
                                                                    <td class="text-center">${row[4]}</td>
                                                                    <td class="text-center fw-bold text-muted">${tinChiRounded}</td>
                                                                    <td class="text-center fw-medium">${row[5] != null ? row[5] : '-'}</td>
                                                                    <td class="text-center fw-medium">${row[6] != null ? row[6] : '-'}</td>
                                                                    <td class="text-center fw-medium">${row[7] != null ? row[7] : '-'}</td>
                                                                    <td class="text-center fw-bold text-primary">
                                                                        <c:choose>
                                                                            <c:when test="${row[5] != null && row[6] != null && row[7] != null}">
                                                                                <fmt:formatNumber value="${finalScore}" maxFractionDigits="1" minFractionDigits="1" />
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <span class="text-muted fw-normal small">-</span>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <span class="badge ${statusClass != 'grade-none' ? 'bg-light text-dark border' : 'text-muted'} fw-bold px-2 py-1 fs-6">
                                                                            ${letterGrade}
                                                                        </span>
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <c:choose>
                                                                            <c:when test="${isPassed == 'true'}">
                                                                                <span class="text-success fs-5" title="${lblMarkAccumulatedSuccess}"><i class="bi bi-check-circle-fill"></i></span>
                                                                            </c:when>
                                                                            <c:when test="${row[5] != null && row[6] != null && row[7] != null}">
                                                                                <span class="text-danger fs-5" title="${lblMarkFailedRetake}"><i class="bi bi-x-circle-fill"></i></span>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <span class="text-muted small" title="${lblMarkIncompleteGrade}"><i class="bi bi-dash-circle"></i></span>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                </tr>
                                                            </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- Summary Box -->
                                        <div class="summary-box p-4 mb-4">
                                            <div class="row g-3">
                                                <div class="col-lg-4 col-md-6 border-end-md">
                                                    <div class="d-flex flex-column gap-2">
                                                        <div class="small text-muted d-flex justify-content-between pe-3">
                                                            <span><s:message code="mark.student.gpa.semester.scale4"/></span>
                                                            <strong class="text-dark sem-gpa4">0.00</strong>
                                                        </div>
                                                        <div class="small text-muted d-flex justify-content-between pe-3">
                                                            <span>Điểm TB học kỳ (Hệ 10)</span>
                                                            <strong class="text-dark sem-gpa10">0.00</strong>
                                                        </div>
                                                        <div class="small text-muted d-flex justify-content-between pe-3">
                                                            <span>Số tín chỉ đạt học kỳ</span>
                                                            <strong class="text-dark sem-passed-credits">0</strong>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-lg-4 col-md-6 border-end-lg">
                                                    <div class="d-flex flex-column gap-2 ps-md-3">
                                                        <div class="small text-muted d-flex justify-content-between pe-3">
                                                            <span>Điểm TB tích lũy (Hệ 4)</span>
                                                            <strong class="text-dark cum-gpa4">0.00</strong>
                                                        </div>
                                                        <div class="small text-muted d-flex justify-content-between pe-3">
                                                            <span>Điểm TB tích lũy (Hệ 10)</span>
                                                            <strong class="text-dark cum-gpa10">0.00</strong>
                                                        </div>
                                                        <div class="small text-muted d-flex justify-content-between pe-3">
                                                            <span>Số tín chỉ tích lũy đạt</span>
                                                            <strong class="text-dark cum-passed-credits">0</strong>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-lg-4 col-md-12">
                                                    <div class="d-flex flex-column justify-content-center h-100 ps-lg-4 text-lg-start text-center mt-lg-0 mt-3">
                                                        <div class="small text-muted mb-1"><s:message code="mark.student.gpa.classification"/></div>
                                                        <h4 class="fw-bold mb-0 sem-ranking text-success"><s:message code="mark.student.grade.good"/></h4>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>

                </div>
            </main>
        </div>
    </div>

    <!-- Bootstrap Bundle JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Dynamic Dashboard Javascript -->
    <script>
        document.addEventListener('DOMContentLoaded', () => {
            calculateDynamicGrades();
            populateSemestersFilter();
        });

        function calculateDynamicGrades() {
            const sections = Array.from(document.querySelectorAll('.semester-section')).reverse();
            
            let cumTotalCredits = 0;
            let cumPassedCredits = 0;
            let cumWeightedScore10 = 0;
            let cumWeightedScore4 = 0;
            let cumCreditsWithScore = 0;
            
            let overallTotalSubjects = 0;
            let overallFailedSubjects = 0;

            sections.forEach(section => {
                const rows = section.querySelectorAll('.grade-row');
                
                let semCredits = 0;
                let semPassedCredits = 0;
                let semWeightedScore10 = 0;
                let semWeightedScore4 = 0;
                let semCreditsWithScore = 0;
                
                let semSubjectsCount = 0;

                rows.forEach(row => {
                    semSubjectsCount++;
                    overallTotalSubjects++;

                    const credits = parseInt(row.getAttribute('data-credits') || 0);
                    const completed = row.getAttribute('data-completed') === 'true';
                    const passed = row.getAttribute('data-passed') === 'true';

                    semCredits += credits;

                    if (passed) {
                        semPassedCredits += credits;
                    } else if (completed) {
                        overallFailedSubjects++;
                    }

                    if (completed) {
                        const score10 = parseFloat(row.getAttribute('data-score10') || 0);
                        const score4 = parseFloat(row.getAttribute('data-score4') || 0);

                        semWeightedScore10 += score10 * credits;
                        semWeightedScore4 += score4 * credits;
                        semCreditsWithScore += credits;
                    }
                });

                const semGpa10 = semCreditsWithScore > 0 ? (semWeightedScore10 / semCreditsWithScore) : 0;
                const semGpa4 = semCreditsWithScore > 0 ? (semWeightedScore4 / semCreditsWithScore) : 0;

                cumPassedCredits += semPassedCredits;
                cumWeightedScore10 += semWeightedScore10;
                cumWeightedScore4 += semWeightedScore4;
                cumCreditsWithScore += semCreditsWithScore;

                const cumGpa10 = cumCreditsWithScore > 0 ? (cumWeightedScore10 / cumCreditsWithScore) : 0;
                const cumGpa4 = cumCreditsWithScore > 0 ? (cumWeightedScore4 / cumCreditsWithScore) : 0;

                section.querySelector('.semester-subjects-count').innerText = semSubjectsCount + ' <s:message code="mark.student.js.subjectSuffix"/>';
                section.querySelector('.sem-gpa4').innerText = semGpa4.toFixed(2);
                section.querySelector('.sem-gpa10').innerText = semGpa10.toFixed(2);
                section.querySelector('.sem-passed-credits').innerText = semPassedCredits;

                section.querySelector('.cum-gpa4').innerText = cumGpa4.toFixed(2);
                section.querySelector('.cum-gpa10').innerText = cumGpa10.toFixed(2);
                section.querySelector('.cum-passed-credits').innerText = cumPassedCredits;

                const rankEl = section.querySelector('.sem-ranking');
                if (semGpa10 >= 9.0) {
                    rankEl.innerText = '<s:message code="mark.student.rank.excellent"/>';
                    rankEl.className = 'fw-bold mb-0 sem-ranking text-success';
                } else if (semGpa10 >= 8.0) {
                    rankEl.innerText = '<s:message code="mark.student.rank.veryGood"/>';
                    rankEl.className = 'fw-bold mb-0 sem-ranking text-success';
                } else if (semGpa10 >= 6.5) {
                    rankEl.innerText = '<s:message code="mark.student.rank.good"/>';
                    rankEl.className = 'fw-bold mb-0 sem-ranking text-primary';
                } else if (semGpa10 >= 5.0) {
                    rankEl.innerText = '<s:message code="mark.student.rank.average"/>';
                    rankEl.className = 'fw-bold mb-0 sem-ranking text-warning';
                } else if (semGpa10 > 0) {
                    rankEl.innerText = '<s:message code="mark.student.rank.weak"/>';
                    rankEl.className = 'fw-bold mb-0 sem-ranking text-danger';
                } else {
                    rankEl.innerText = '<s:message code="mark.student.rank.unranked"/>';
                    rankEl.className = 'fw-bold mb-0 sem-ranking text-muted';
                }
            });

            const finalOverallGpa10 = cumCreditsWithScore > 0 ? (cumWeightedScore10 / cumCreditsWithScore) : 0;
            
            animateValue('total-subjects', overallTotalSubjects);
            animateValue('passed-credits-stat', cumPassedCredits);
            animateValue('failed-subjects', overallFailedSubjects);
            document.getElementById('average-gpa-stat').innerText = finalOverallGpa10.toFixed(2);
        }

        function animateValue(id, endValue) {
            const el = document.getElementById(id);
            if (!el) return;
            let current = 0;
            const duration = 400;
            const stepTime = Math.max(Math.floor(duration / (endValue || 1)), 10);
            
            if (endValue === 0) {
                el.innerText = '0';
                return;
            }

            const timer = setInterval(() => {
                current++;
                el.innerText = current;
                if (current >= endValue) {
                    clearInterval(timer);
                    el.innerText = endValue;
                }
            }, stepTime);
        }

        function populateSemestersFilter() {
            const filterSelect = document.getElementById('semester-filter');
            const sections = document.querySelectorAll('.semester-section');
            sections.forEach(section => {
                const semName = section.getAttribute('data-semester-name');
                if (semName) {
                    const opt = document.createElement('option');
                    opt.value = semName;
                    opt.innerText = semName;
                    filterSelect.appendChild(opt);
                }
            });
        }

        function filterGrades() {
            const query = document.getElementById('subject-search').value.normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase();
            const semesterFilter = document.getElementById('semester-filter').value;
            const sections = document.querySelectorAll('.semester-section');

            sections.forEach(section => {
                const semName = section.getAttribute('data-semester-name');
                const rows = section.querySelectorAll('.grade-row');
                let hasVisibleRow = false;

                rows.forEach(row => {
                    const subjectName = row.getAttribute('data-subject-name').normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase();
                    const matchesSearch = subjectName.includes(query);
                    const matchesSemester = semesterFilter === 'all' || semName === semesterFilter;

                    if (matchesSearch && matchesSemester) {
                        row.style.display = '';
                        hasVisibleRow = true;
                    } else {
                        row.style.display = 'none';
                    }
                });

                const matchesSemesterSection = semesterFilter === 'all' || semName === semesterFilter;
                if (matchesSemesterSection && hasVisibleRow) {
                    section.style.display = '';
                } else {
                    section.style.display = 'none';
                }
            });
        }
    </script>
</body>
</html>
