<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quản lý Sinh viên - QLDSV_HTC_WEB</title>
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
                        <h3 class="mb-0 fw-bold text-dark">Quản lý Sinh viên</h3>
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

                    <!-- MASTER SECTION: CLASS LIST -->
                    <div class="card border-0 shadow-sm rounded-4 mb-4 overflow-hidden">
                        <div class="card-header bg-white border-bottom-0 pt-4 px-4 pb-2">
                            <h6 class="fw-bold text-primary text-uppercase small mb-0">Quản lý sinh viên - Theo Lớp</h6>
                        </div>
                        <div class="card-body px-4 pb-4">
                            <!-- SEARCH & FILTER TOOLBAR -->
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <div class="input-group" style="max-width: 300px;">
                                    <span class="input-group-text bg-light border-0"><i class="bi bi-search text-muted"></i></span>
                                    <input type="text" id="class-search" class="form-control bg-light border-0 small" placeholder="Tìm mã lớp hoặc tên lớp..." onkeyup="filterLocalClasses()">
                                </div>
                                <div class="d-flex gap-2">
                                    <c:choose>
                                        <c:when test="${sessionScope.role == 'PGV'}">
                                            <!-- Standard SSR form filter for PGV role -->
                                            <form action="${pageContext.request.contextPath}/student" method="GET" class="d-inline">
                                                <select name="maKhoa" class="form-select form-select-sm border-0 bg-light text-muted fw-bold" onchange="this.form.submit()">
                                                    <option value="all">-- Tất cả khoa --</option>
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
                                            <th class="border-0 px-3 small fw-bold text-muted">MÃ LỚP</th>
                                            <th class="border-0 small fw-bold text-muted">TÊN LỚP</th>
                                            <th class="border-0 text-center small fw-bold text-muted">KHÓA HỌC</th>
                                            <th class="border-0 text-center small fw-bold text-muted">KHOA</th>
                                        </tr>
                                    </thead>
                                    <tbody id="class-table-body" class="border-top-0">
                                        <c:forEach var="lop" items="${lopList}">
                                            <c:set var="classUrl" value="${pageContext.request.contextPath}/student?maLop=${lop.maLop}" />
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
                                                <td colspan="4" class="text-center py-4 text-muted">Không tìm thấy lớp học nào</td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                            <div class="mt-2 text-muted small px-1">
                                <i class="bi bi-info-circle me-1"></i> Chọn một dòng để nạp danh sách sinh viên tương ứng phía dưới
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
                                            Danh sách sinh viên - Lớp: <span class="text-dark">${maLop}</span>
                                        </h6>
                                        <c:if test="${sessionScope.role == 'PGV'}">
                                            <!-- Button to trigger ADD modal via query parameter (clean reload) -->
                                            <a href="${pageContext.request.contextPath}/student?maLop=${maLop}&lnkAdd" class="btn btn-primary btn-sm rounded-3 px-3 fw-bold shadow-sm">
                                                <i class="bi bi-person-plus-fill me-1"></i> Thêm Sinh viên
                                            </a>
                                        </c:if>
                                    </div>
                                    <div class="card-body px-4 pb-4">
                                        <div class="d-flex justify-content-between align-items-center mb-3">
                                            <div class="input-group" style="max-width: 300px;">
                                                <span class="input-group-text bg-light border-0"><i class="bi bi-search text-muted"></i></span>
                                                <input type="text" id="student-search" class="form-control bg-light border-0 small" placeholder="Tìm MSV hoặc tên sinh viên..." onkeyup="filterLocalStudents()">
                                            </div>
                                        </div>

                                        <div class="table-responsive rounded-3 border">
                                            <table class="table table-hover align-middle mb-0">
                                                <thead class="table-light">
                                                    <tr>
                                                        <th class="border-0 px-3 small fw-bold text-muted">MÃ SV</th>
                                                        <th class="border-0 small fw-bold text-muted">HỌ VÀ TÊN</th>
                                                        <th class="border-0 text-center small fw-bold text-muted">PHÁI</th>
                                                        <th class="border-0 text-center small fw-bold text-muted">NGÀY SINH</th>
                                                        <th class="border-0 small fw-bold text-muted">ĐỊA CHỈ</th>
                                                        <th class="border-0 text-center small fw-bold text-muted">TRẠNG THÁI</th>
                                                        <c:if test="${sessionScope.role == 'PGV'}">
                                                            <th class="border-0 text-center small fw-bold text-muted">THAO TÁC</th>
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
                                                                    <i class="bi bi-dot"></i> ${sv.daNghiHoc ? 'Đã nghỉ' : 'Đang học'}
                                                                </span>
                                                            </td>
                                                            <c:if test="${sessionScope.role == 'PGV'}">
                                                                <td class="text-center">
                                                                    <div class="d-flex gap-2 justify-content-center align-items-center">
                                                                        <!-- GET link for Edit modal reload -->
                                                                        <a href="${pageContext.request.contextPath}/student?maLop=${maLop}&maSV=${sv.maSV}&lnkEdit" class="btn btn-sm btn-outline-primary border-0 rounded-3">
                                                                            <i class="bi bi-pencil-square"></i>
                                                                        </a>
                                                                        
                                                                        <!-- Form POST for secure Delete action -->
                                                                        <form action="${pageContext.request.contextPath}/student" method="POST" onsubmit="return confirm('Bạn có chắc chắn muốn xóa sinh viên này không? Thao tác này không thể hoàn tác.');" class="d-inline">
                                                                            <input type="hidden" name="maSV" value="${sv.maSV}">
                                                                            <input type="hidden" name="maLop" value="${maLop}">
                                                                            <button type="submit" name="btnDelete" class="btn btn-sm btn-outline-danger border-0 rounded-3 ${!sv.canDelete ? 'disabled opacity-25' : ''}" ${!sv.canDelete ? 'disabled title="Sinh viên này không thể xóa do có dữ liệu liên quan"' : ''}>
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
                                                                Chưa có dữ liệu sinh viên cho lớp này
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
                                    <h5 class="text-muted fw-bold">Vui lòng chọn một lớp để xem danh sách sinh viên</h5>
                                    <p class="text-muted small">Thông tin sinh viên sẽ hiển thị tại đây sau khi bạn chọn lớp từ danh sách phía trên.</p>
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
                                <c:when test="${mode == 'edit'}">Cập nhật Hồ sơ Sinh viên</c:when>
                                <c:otherwise>Thêm mới Sinh viên</c:otherwise>
                            </c:choose>
                        </h5>
                        <a href="${pageContext.request.contextPath}/student?maLop=${maLop}" class="btn-close btn-close-white text-decoration-none"></a>
                    </div>
                    
                    <form action="${pageContext.request.contextPath}/student" method="POST">
                        <div class="modal-body p-4">
                            <div class="row g-3">
                                <div class="col-md-4">
                                    <label class="form-label small fw-bold text-muted">MÃ SINH VIÊN</label>
                                    <input type="text" class="form-control rounded-3" id="inp_maSV" name="maSV" value="${sinhVien.maSV}" placeholder="VD: N23DCCN001" required ${mode == 'edit' ? 'readonly' : ''}>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-bold text-muted">HỌ</label>
                                    <input type="text" class="form-control rounded-3" id="inp_ho" name="ho" value="${sinhVien.ho}" placeholder="VD: Nguyễn Văn" required>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-bold text-muted">TÊN</label>
                                    <input type="text" class="form-control rounded-3" id="inp_ten" name="ten" value="${sinhVien.ten}" placeholder="VD: An" required>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-bold text-muted">PHÁI</label>
                                    <select class="form-select rounded-3" id="inp_phai" name="phai">
                                        <option value="Nam" ${sinhVien.phai == 'Nam' ? 'selected' : ''}>Nam</option>
                                        <option value="Nữ" ${sinhVien.phai == 'Nữ' ? 'selected' : ''}>Nữ</option>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-bold text-muted">NGÀY SINH</label>
                                    <fmt:formatDate var="fmtDate" value="${sinhVien.ngaySinh}" pattern="yyyy-MM-dd" />
                                    <!-- Fallback to param values if bindingResult failed and format wasn't parsed -->
                                    <input type="date" class="form-control rounded-3" id="inp_ngaySinh" name="ngaySinh" value="${not empty fmtDate ? fmtDate : param.ngaySinh}" required>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-bold text-muted">LỚP HIỆN TẠI</label>
                                    <input type="text" class="form-control rounded-3 bg-light" name="maLop" value="${maLop}" readonly>
                                </div>
                                <div class="col-md-12">
                                    <label class="form-label small fw-bold text-muted">ĐỊA CHỈ</label>
                                    <input type="text" class="form-control rounded-3" id="inp_diaChi" name="diaChi" value="${sinhVien.diaChi}" placeholder="VD: 97 Man Thiện, Q.9, TP.HCM">
                                </div>
                                <div class="col-md-12">
                                    <div class="form-check form-switch p-0 d-flex align-items-center gap-2">
                                        <input class="form-check-input ms-0 mt-0" type="checkbox" role="switch" id="daNghiHoc" name="daNghiHoc" value="true" ${sinhVien.daNghiHoc ? 'checked' : ''}>
                                        <label class="form-check-label fw-bold text-danger" for="daNghiHoc">Đã nghỉ học</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="modal-footer border-0 px-4 pb-4">
                            <a href="${pageContext.request.contextPath}/student?maLop=${maLop}" class="btn btn-light rounded-3 fw-bold">HỦY</a>
                            <c:choose>
                                <c:when test="${mode == 'edit'}">
                                    <button type="submit" name="btnUpdate" class="btn btn-primary rounded-3 fw-bold px-4">GHI (SỬA)</button>
                                </c:when>
                                <c:otherwise>
                                    <button type="submit" name="btnInsert" class="btn btn-success rounded-3 fw-bold px-4">GHI (THÊM)</button>
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
