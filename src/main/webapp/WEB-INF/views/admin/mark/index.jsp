<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title><s:message code="mark.page.title" text="Tuition and Grades Management"/></title>
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <style>
        :root {
            --primary-gradient: linear-gradient(135deg, #4361ee, #4895ef);
            --success-gradient: linear-gradient(135deg, #2ecc71, #27ae60);
            --bg-light: #f8f9fa;
        }

        body {
            font-family: 'Inter', sans-serif;
            background-color: var(--bg-light);
        }

        .filter-section {
            background: #fff;
            padding: 25px;
            border-radius: 16px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.04);
            margin-bottom: 30px;
            border: 1px solid rgba(0, 0, 0, 0.05);
        }

        .page-title {
            font-weight: 700;
            background: var(--primary-gradient);
            -webkit-background-clip: text;
            background-clip: text;
            -webkit-text-fill-color: transparent;
            margin-bottom: 0;
        }

        .card-custom {
            border-radius: 16px;
            border: none;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.04);
            overflow: hidden;
        }

        .mark-table thead th {
            background-color: #f1f5f9;
            color: #475569;
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.75rem;
            letter-spacing: 0.05em;
            padding: 15px;
            border: none;
        }

        .mark-table tbody td {
            padding: 12px 15px;
            border-bottom: 1px solid #f1f5f9;
            vertical-align: middle;
        }

        .input-mark {
            width: 75px;
            text-align: center;
            border-radius: 8px;
            border: 1px solid #e2e8f0;
            transition: all 0.2s;
            font-weight: 500;
            padding: 8px;
        }

        .input-mark:focus {
            border-color: #4361ee;
            box-shadow: 0 0 0 3px rgba(67, 97, 238, 0.15);
            outline: none;
        }

        .input-mark.invalid {
            border-color: #ef4444;
            background-color: #fef2f2;
        }

        .total-badge {
            font-weight: 700;
            font-size: 0.9rem;
            padding: 6px 12px;
            border-radius: 10px;
        }

        .save-all-btn {
            background: var(--primary-gradient);
            border: none;
            padding: 10px 25px;
            border-radius: 12px;
            font-weight: 600;
            box-shadow: 0 4px 15px rgba(67, 97, 238, 0.3);
            transition: all 0.3s;
        }

        .save-all-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(67, 97, 238, 0.4);
        }

        .summary-badge {
            background-color: #f1f5f9;
            color: #475569;
            padding: 8px 15px;
            border-radius: 12px;
            font-size: 0.85rem;
            font-weight: 500;
            display: flex;
            align-items: center;
            gap: 8px;
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

            <main id="main-content" class="app-content p-4">
                <div class="container-fluid max-w-7xl mx-auto">

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

                    <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
                        <div>
                            <h2 class="page-title"><i class="bi bi-grid-3x3-gap-fill me-2"></i><s:message code="mark.lbl.title" text="Tuition and Grades Management"/></h2>
                            <p class="text-muted small mb-0"><s:message code="mark.system.desc" text="Enter and manage student grades."/></p>
                        </div>
                        <c:if test="${not empty studentMarkList}">
                            <div class="d-flex gap-2 align-items-center">
                                <div class="summary-badge"><i class="bi bi-people text-success"></i> <span>${studentMarkList.size()}</span> SV</div>
                                <button type="button" onclick="document.getElementById('marksForm').submit();" class="btn btn-primary save-all-btn">
                                    <i class="bi bi-cloud-arrow-up-fill me-2"></i> <s:message code="global.btn.save"/> TẤT CẢ
                                </button>
                            </div>
                        </c:if>
                    </div>

                    <!-- SEARCH & FILTER FORM -->
                    <form action="${pageContext.request.contextPath}/admin/mark" method="GET" class="filter-section">
                        <div class="row g-3">
                            <c:choose>
                                <c:when test="${sessionScope.role == 'PGV'}">
                                    <div class="col-lg-3 col-md-6">
                                        <label class="form-label text-secondary fw-semibold small">KHOA</label>
                                        <select class="form-select border-0 bg-light rounded-3" name="maKhoa" onchange="this.form.submit()">
                                            <option value="all"><s:message code="mark.all.faculties" text="All Faculties"/></option>
                                            <c:forEach var="k" items="${khoaList}">
                                                <option value="${k.maKhoa}" ${maKhoa == k.maKhoa ? 'selected' : ''}>${k.tenKhoa}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" name="maKhoa" value="${maKhoa}">
                                </c:otherwise>
                            </c:choose>

                            <div class="col-lg-2 col-md-6">
                                <label class="form-label text-secondary fw-semibold small"><s:message code="global.lbl.academicYear.upper"/></label>
                                <select class="form-select border-0 bg-light rounded-3" name="nienKhoa" onchange="this.form.submit()">
                                    <option value=""><s:message code="mark.select.academic.year" text="Select Academic Year"/></option>
                                    <c:forEach var="nk" items="${nienKhoaList}">
                                        <option value="${nk}" ${nienKhoa == nk ? 'selected' : ''}>${nk}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="col-lg-2 col-md-6">
                                <label class="form-label text-secondary fw-semibold small">HỌC KỲ</label>
                                <select class="form-select border-0 bg-light rounded-3" name="hocKy" onchange="this.form.submit()">
                                    <option value=""><s:message code="mark.select.semester" text="Select Semester"/></option>
                                    <option value="1" ${hocKy == '1' ? 'selected' : ''}>1</option>
                                    <option value="2" ${hocKy == '2' ? 'selected' : ''}>2</option>
                                    <option value="3" ${hocKy == '3' ? 'selected' : ''}>3</option>
                                    <option value="4" ${hocKy == '4' ? 'selected' : ''}>4</option>
                                </select>
                            </div>

                            <div class="col-lg-3 col-md-12">
                                <label class="form-label text-secondary fw-semibold small"><s:message code="credit-class.lbl.subject"/></label>
                                <select class="form-select border-0 bg-light rounded-3" name="maMH" onchange="this.form.submit()" ${empty subjectList ? 'disabled' : ''}>
                                    <option value=""><s:message code="mark.select.subject" text="Select Subject"/></option>
                                    <c:forEach var="sb" items="${subjectList}">
                                        <option value="${sb[0]}" ${maMH == sb[0] ? 'selected' : ''}>[${sb[0]}] ${sb[1]}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="col-lg-1 col-md-6">
                                <label class="form-label text-secondary fw-semibold small"><s:message code="credit-class.lbl.group"/></label>
                                <select class="form-select border-0 bg-light rounded-3" name="nhom" onchange="this.form.submit()" ${empty groupList ? 'disabled' : ''}>
                                    <option value=""><s:message code="mark.group" text="Select Group"/></option>
                                    <c:forEach var="g" items="${groupList}">
                                        <option value="${g}" ${nhom == g ? 'selected' : ''}>${g}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="col-lg-2 col-md-6">
                                <label class="form-label text-secondary fw-semibold small"><s:message code="mark.lbl.searchStudentId"/></label>
                                <s:message code="mark.enter.student.id" var="lblMarkEnterStudentId"/>
                                <input type="text" class="form-control border-0 bg-light rounded-3 shadow-none" name="searchMaSV" value="${searchMaSV}" placeholder="${lblMarkEnterStudentId}">
                            </div>

                            <div class="col-lg-1 col-md-12 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary w-100 rounded-3 p-2 shadow-sm">
                                    <i class="bi bi-search fs-5"></i>
                                </button>
                            </div>
                        </div>
                    </form>

                    <!-- MARKS ENTRY FORM -->
                    <form action="${pageContext.request.contextPath}/admin/mark/save" method="POST" id="marksForm">
                        <input type="hidden" name="csrf_token" value="${csrfToken}" />
                        <input type="hidden" name="maKhoa" value="${maKhoa}">
                        <input type="hidden" name="nienKhoa" value="${nienKhoa}">
                        <input type="hidden" name="hocKy" value="${hocKy}">
                        <input type="hidden" name="maMH" value="${maMH}">
                        <input type="hidden" name="nhom" value="${nhom}">
                        <input type="hidden" name="searchMaSV" value="${searchMaSV}">

                        <div class="card card-custom">
                            <div class="card-body p-0">
                                <div class="table-responsive">
                                    <table class="table table-hover align-middle mb-0 mark-table" id="studentMarkTable">
                                        <thead>
                                            <tr>
                                                <th class="ps-4">STT</th>
                                                <th><s:message code="global.lbl.studentId"/></th>
                                                <th><s:message code="global.lbl.fullName"/></th>
                                                <th><s:message code="mark.subject.group" text="Subject & Group"/></th>
                                                <th class="text-center">CC (10%)</th>
                                                <th class="text-center">GK (30%)</th>
                                                <th class="text-center">CK (60%)</th>
                                                <th class="text-center"><s:message code="global.lbl.total"/></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="row" items="${studentMarkList}" varStatus="status">
                                                <!-- row: [0:maSV, 1:ho, 2:ten, 3:CC, 4:GK, 5:CK, 6:maLTC, 7:nhom, 8:tenMH] -->
                                                <tr>
                                                    <td class="ps-4 text-muted small">${status.index + 1}</td>
                                                    <td class="fw-bold text-dark">
                                                        ${row[0]}
                                                        <input type="hidden" name="maSV" value="${row[0]}">
                                                        <input type="hidden" name="maLTC" value="${row[6]}">
                                                    </td>
                                                    <td class="fw-medium">${row[1]} ${row[2]}</td>
                                                    <td class="small text-secondary">
                                                        ${row[8]} <br/>
                                                        <span class="badge bg-light text-dark border"><s:message code="global.lbl.group"/> ${row[7]}</span>
                                                    </td>
                                                    <td class="text-center">
                                                        <input type="number" class="form-control form-control-sm mx-auto input-mark" 
                                                               step="0.1" min="0" max="10" name="diemCC" value="${row[3] != null ? row[3] : ''}" 
                                                               data-row="${status.index}" data-col="0">
                                                    </td>
                                                    <td class="text-center">
                                                        <input type="number" class="form-control form-control-sm mx-auto input-mark" 
                                                               step="0.1" min="0" max="10" name="diemGK" value="${row[4] != null ? row[4] : ''}" 
                                                               data-row="${status.index}" data-col="1">
                                                    </td>
                                                    <td class="text-center">
                                                        <input type="number" class="form-control form-control-sm mx-auto input-mark" 
                                                               step="0.1" min="0" max="10" name="diemCK" value="${row[5] != null ? row[5] : ''}" 
                                                               data-row="${status.index}" data-col="2">
                                                    </td>
                                                    <td class="text-center">
                                                        <span class="total-badge total-mark bg-light text-muted">0.0</span>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            <c:if test="${empty studentMarkList}">
                                                <tr>
                                                    <td colspan="8" class="text-center py-5 text-muted">
                                                        <div class="opacity-50 mb-3"><i class="bi bi-inbox fs-1"></i></div>
                                                        <s:message code="mark.please.select.info" text="Please select academic year, semester, subject, and group to query data."/>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </main>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function calculateTotal(row) {
            let cc = parseFloat(row.querySelector('input[data-col="0"]').value) || 0;
            let gk = parseFloat(row.querySelector('input[data-col="1"]').value) || 0;
            let ck = parseFloat(row.querySelector('input[data-col="2"]').value) || 0;

            let total = (cc * 0.1) + (gk * 0.3) + (ck * 0.6);
            let badgeClass = total >= 8.5 ? 'bg-success' : (total >= 7 ? 'bg-primary' : (total >= 5 ? 'bg-warning' : 'bg-danger'));

            let totalEl = row.querySelector('.total-mark');
            totalEl.innerText = total.toFixed(1);
            totalEl.className = 'total-badge total-mark ' + badgeClass + ' text-white bg-opacity-75';
        }

        document.querySelectorAll('#studentMarkTable tbody tr').forEach(row => {
            const inputs = row.querySelectorAll('.input-mark');
            if (inputs.length > 0) {
                calculateTotal(row);
                inputs.forEach(input => {
                    input.addEventListener('input', () => {
                        if (input.value < 0 || input.value > 10) input.classList.add('invalid');
                        else input.classList.remove('invalid');
                        calculateTotal(row);
                    });

                    // Keyboard navigation
                    input.addEventListener('keydown', (e) => {
                        let rowIdx = parseInt(input.dataset.row);
                        let colIdx = parseInt(input.dataset.col);
                        let next;

                        switch (e.key) {
                            case 'ArrowLeft':
                                next = document.querySelector(`input[data-row="${rowIdx}"][data-col="${colIdx - 1}"]`);
                                break;
                            case 'ArrowUp':
                                next = document.querySelector(`input[data-row="${rowIdx - 1}"][data-col="${colIdx}"]`);
                                break;
                            case 'ArrowRight':
                                next = document.querySelector(`input[data-row="${rowIdx}"][data-col="${colIdx + 1}"]`);
                                break;
                            case 'ArrowDown':
                            case 'Enter':
                                e.preventDefault();
                                next = document.querySelector(`input[data-row="${rowIdx + 1}"][data-col="${colIdx}"]`);
                                break;
                        }
                        if (next) {
                            next.focus();
                            next.select();
                        }
                    });
                });
            }
        });
    </script>
</body>

</html>
