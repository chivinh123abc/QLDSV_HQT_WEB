<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Thanh Toán Học Phí</title>
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
                        <i class="bi bi-credit-card me-2"></i>Thanh Toán Học Phí
                    </h1>
                </div>

                <c:if test="${not empty message}">
                    <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
                        <i class="bi bi-check-circle-fill me-2"></i>${message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i>${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <!-- Filter -->
                <div class="card shadow-sm mb-4 border-0 rounded-4">
                    <div class="card-body p-4">
                        <form action="${pageContext.request.contextPath}/payment" method="GET" class="row g-3 align-items-end">
                            <div class="col-md-4">
                                <label class="form-label fw-bold text-muted small text-uppercase">Chọn Niên Khóa / Học Kỳ</label>
                                <select class="form-select" name="nienKhoaHocKy" onchange="splitAndSubmit(this)">
                                    <option value="">-- Chọn --</option>
                                    <c:forEach var="hk" items="${allSemesters}">
                                        <c:set var="val" value="${hk[0]}_${hk[1]}"/>
                                        <option value="${val}" ${nienKhoa == hk[0] && hocKy == hk[1] ? 'selected' : ''}>
                                            NK: ${hk[0]} - Học kỳ ${hk[1]}
                                        </option>
                                    </c:forEach>
                                </select>
                                <script>
                                    function splitAndSubmit(selectElement) {
                                        var form = selectElement.form;
                                        if(selectElement.value) {
                                            var parts = selectElement.value.split('_');
                                            
                                            var inputNK = document.createElement('input');
                                            inputNK.type = 'hidden'; inputNK.name = 'nienKhoa'; inputNK.value = parts[0];
                                            
                                            var inputHK = document.createElement('input');
                                            inputHK.type = 'hidden'; inputHK.name = 'hocKy'; inputHK.value = parts[1];
                                            
                                            form.appendChild(inputNK);
                                            form.appendChild(inputHK);
                                            selectElement.name = ''; // Prevent sending the combined string
                                        }
                                        form.submit();
                                    }
                                </script>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Registration List -->
                <c:if test="${not empty dsDangKy}">
                    <div class="card shadow-sm mb-4">
                        <div class="card-body p-4">
                            <h5 class="card-title fw-bold mb-4 text-secondary">Học Phí Các Môn Đã Đăng Ký (Niên khóa: ${nienKhoa} - HK: ${hocKy})</h5>
                            <div class="table-responsive">
                                <table class="table table-hover align-middle table-custom">
                                    <thead>
                                        <tr>
                                            <th>Mã MH</th>
                                            <th>Tên Môn Học</th>
                                            <th>Nhóm</th>
                                            <th>Số TC</th>
                                            <th>Thành Tiền</th>
                                            <th>Trạng Thái</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="dk" items="${dsDangKy}">
                                            <c:if test="${dk.trangThaiDangKy == 'HIEU_LUC'}">
                                                <tr>
                                                    <td><span class="badge bg-secondary">${dk.lopTinChi.monHoc.maMH}</span></td>
                                                    <td class="fw-semibold">${dk.lopTinChi.monHoc.tenMH}</td>
                                                    <td>${dk.lopTinChi.nhom}</td>
                                                    <td>${dk.lopTinChi.monHoc.soTinChi}</td>
                                                    <td><fmt:formatNumber value="${dk.lopTinChi.monHoc.soTinChi * 1000000}" type="currency" currencySymbol="VNĐ"/></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${dk.daThanhToan}">
                                                                <span class="badge bg-success rounded-pill px-3 py-2"><i class="bi bi-check-circle me-1"></i>Đã thanh toán</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-warning text-dark rounded-pill px-3 py-2"><i class="bi bi-hourglass-split me-1"></i>Chưa thanh toán</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                    </tbody>
                                    <tfoot class="fw-bold">
                                        <tr>
                                            <td colspan="3" class="text-end">TỔNG CỘNG:</td>
                                            <td>${tongTinChi} TC</td>
                                            <td colspan="2" class="text-danger fs-5"><fmt:formatNumber value="${tongTien}" type="currency" currencySymbol="VNĐ"/></td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>

                            <div class="d-flex justify-content-end mt-4">
                                <c:if test="${canPay}">
                                    <form action="${pageContext.request.contextPath}/payment/checkout" method="POST">
                                        <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                        <input type="hidden" name="nienKhoa" value="${nienKhoa}"/>
                                        <input type="hidden" name="hocKy" value="${hocKy}"/>
                                        <button type="submit" class="btn btn-primary btn-lg px-5 shadow rounded-pill d-flex align-items-center">
                                            <i class="bi bi-wallet2 me-2"></i>Thanh Toán Bằng MoMo
                                        </button>
                                    </form>
                                </c:if>
                                <c:if test="${!canPay}">
                                    <button class="btn btn-success btn-lg px-5 shadow rounded-pill disabled" disabled>
                                        <i class="bi bi-check-circle me-2"></i>Đã Hoàn Thành Học Phí
                                    </button>
                                </c:if>
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
