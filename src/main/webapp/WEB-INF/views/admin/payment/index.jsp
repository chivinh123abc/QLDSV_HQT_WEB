<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title><s:message code="payment.stats.title"/> | QLDSV_HTC</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
</head>
<body class="app-body">
    <!-- Navbar -->
    <jsp:include page="/WEB-INF/views/shared/header.jsp" />

    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-3 col-lg-2 d-md-block sidebar collapse bg-white shadow-sm">
                <jsp:include page="/WEB-INF/views/shared/sidebar.jsp" />
            </div>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4 content-wrapper">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-4 border-bottom">
                    <h1 class="h2 text-primary fw-bold">
                        <i class="bi bi-pie-chart me-2"></i><s:message code="payment.stats.title"/>
                    </h1>
                </div>

                <!-- Filter -->
                <div class="card shadow-sm mb-4 border-0 rounded-4">
                    <div class="card-body p-4">
                        <form action="${pageContext.request.contextPath}/admin/payment" method="GET" class="row g-3">
                            <div class="col-md-4">
                                <label class="form-label fw-bold text-muted small text-uppercase"><s:message code="payment.select.class"/></label>
                                <select class="form-select" name="maLop" required>
                                    <option value=""><s:message code="payment.select.class.placeholder"/></option>
                                    <c:forEach var="lop" items="${lopList}">
                                        <option value="${lop.maLop}" ${maLop == lop.maLop ? 'selected' : ''}>
                                            ${lop.tenLop}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label fw-bold text-muted small text-uppercase"><s:message code="payment.select.semester"/></label>
                                <select class="form-select" name="nienKhoaHocKy" required>
                                    <option value=""><s:message code="payment.select.semester.placeholder"/></option>
                                    <c:forEach var="hk" items="${allSemesters}">
                                        <c:set var="val" value="${hk[0]}_${hk[1]}"/>
                                        <option value="${val}" ${nienKhoa == hk[0] && hocKy == hk[1] ? 'selected' : ''}>
                                            NK: ${hk[0]} - <s:message code="registration.semester" arguments="${hk[1]}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-4 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary px-4 shadow-sm w-100" onclick="splitNienKhoaHocKy(this.form)">
                                    <i class="bi bi-search me-2"></i><s:message code="payment.lecturer.btn.view"/>
                                </button>
                            </div>
                        </form>
                        <script>
                            function splitNienKhoaHocKy(form) {
                                var select = form.querySelector('select[name="nienKhoaHocKy"]');
                                if (select.value) {
                                    var parts = select.value.split('_');
                                    
                                    var inputNK = document.createElement('input');
                                    inputNK.type = 'hidden'; inputNK.name = 'nienKhoa'; inputNK.value = parts[0];
                                    
                                    var inputHK = document.createElement('input');
                                    inputHK.type = 'hidden'; inputHK.name = 'hocKy'; inputHK.value = parts[1];
                                    
                                    form.appendChild(inputNK);
                                    form.appendChild(inputHK);
                                    select.name = ''; // Prevent sending combined value
                                }
                            }
                        </script>
                    </div>
                </div>

                <!-- Stats List -->
                <c:if test="${not empty stats}">
                    <div class="row mb-4">
                        <div class="col-md-6">
                            <div class="card border-0 shadow-sm rounded-4 bg-primary text-white">
                                <div class="card-body p-4 text-center">
                                    <h5 class="card-title fw-bold"><s:message code="payment.lecturer.totalClass"/></h5>
                                    <h2 class="mb-0"><fmt:formatNumber value="${sumTongTien}" type="currency" currencySymbol="VNĐ"/></h2>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="card border-0 shadow-sm rounded-4 bg-success text-white">
                                <div class="card-body p-4 text-center">
                                    <h5 class="card-title fw-bold"><s:message code="payment.lecturer.totalCollected"/></h5>
                                    <h2 class="mb-0"><fmt:formatNumber value="${sumDaDong}" type="currency" currencySymbol="VNĐ"/></h2>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="card shadow-sm mb-4">
                        <div class="card-body p-4">
                            <h5 class="card-title fw-bold mb-4 text-secondary"><s:message code="payment.lecturer.listTitle" arguments="${maLop}"/></h5>
                            <div class="table-responsive">
                                <table class="table table-hover align-middle table-custom">
                                    <thead>
                                        <tr>
                                            <th><s:message code="payment.lecturer.studentId"/></th>
                                            <th><s:message code="payment.lecturer.studentName"/></th>
                                            <th><s:message code="payment.lecturer.registeredCredits"/></th>
                                            <th><s:message code="payment.lecturer.totalAmount"/></th>
                                            <th><s:message code="payment.lecturer.status"/></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="row" items="${stats}">
                                            <tr>
                                                <td><span class="badge bg-secondary">${row[0]}</span></td>
                                                <td class="fw-semibold">${row[1]}</td>
                                                <td>${row[2]}</td>
                                                <td><fmt:formatNumber value="${row[3]}" type="currency" currencySymbol="VNĐ"/></td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${row[4]}">
                                                            <span class="badge bg-success rounded-pill px-3 py-2"><s:message code="payment.lecturer.status.paid"/></span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-danger rounded-pill px-3 py-2"><s:message code="payment.lecturer.status.unpaid"/></span>
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
                </c:if>
            </main>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
