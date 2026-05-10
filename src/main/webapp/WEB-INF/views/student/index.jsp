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

                    .badge-soft-success {
                        background-color: #dcfce7;
                        color: #166534;
                        border-radius: 20px;
                        font-weight: 600;
                        padding: 6px 12px;
                    }

                    .badge-soft-danger {
                        background-color: #fee2e2;
                        color: #991b1b;
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

                    .table-clickable tbody tr {
                        cursor: pointer;
                        transition: all 0.2s;
                    }

                    .table-clickable tbody tr:hover {
                        background-color: #eff6ff !important;
                        transform: translateX(4px);
                    }

                    .student-title {
                        font-weight: 700;
                        color: #1e293b;
                        margin-bottom: 2px;
                    }

                    .student-subtitle {
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

                    .toolbar-btn:disabled {
                        opacity: 0.5;
                        cursor: not-allowed;
                    }

                    .input-group-text {
                        background-color: transparent;
                        color: #4361ee;
                        border-right: 0;
                    }

                    .form-control-icon {
                        border-left: 0;
                        padding-left: 0;
                    }

                    .form-control-icon:focus {
                        box-shadow: none;
                        border-color: #dee2e6;
                    }

                    .input-group:focus-within {
                        box-shadow: 0 0 0 0.25rem rgba(67, 97, 238, 0.25);
                        border-radius: 0.375rem;
                    }

                    .input-group:focus-within .input-group-text,
                    .input-group:focus-within .form-control-icon {
                        border-color: #4361ee;
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

                                <div class="d-flex align-items-center gap-2 mb-4">
                                    <i class="bi bi-mortarboard-fill text-primary fs-3"></i>
                                    <h3 class="mb-0 fw-bold text-dark">Quản lý Sinh viên</h3>
                                </div>

                                <!-- MASTER SECTION: CLASS LIST -->
                                <div class="card border-0 shadow-sm rounded-4 mb-4 overflow-hidden">
                                    <div class="card-header bg-white border-bottom-0 pt-4 px-4 pb-2">
                                        <h6 class="fw-bold text-primary text-uppercase small mb-0">Quản lý sinh viên -
                                            Theo Lớp</h6>
                                    </div>
                                    <div class="card-body px-4 pb-4">
                                        <!-- SEARCH & FILTER TOOLBAR -->
                                        <div class="d-flex justify-content-between align-items-center mb-3">
                                            <div class="input-group" style="max-width: 300px;">
                                                <span class="input-group-text bg-light border-0"><i
                                                        class="bi bi-search text-muted"></i></span>
                                                <input type="text" id="class-search"
                                                    class="form-control bg-light border-0 small"
                                                    placeholder="Tìm mã lớp hoặc tên lớp..."
                                                    onkeyup="filterLocalClasses()">
                                            </div>
                                            <div class="d-flex gap-2">
                                                <select id="khoa-filter"
                                                    class="form-select form-select-sm border-0 bg-light text-muted fw-bold"
                                                    onchange="filterByKhoa()">
                                                    <option value="all">-- Tất cả khoa --</option>
                                                    <c:forEach var="khoa" items="${khoaList}">
                                                        <option value="${khoa.maKhoa}" ${khoa.maKhoa == maKhoa ? 'selected' : ''}>${khoa.tenKhoa}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="table-responsive rounded-3 border">
                                            <table class="table table-hover align-middle mb-0">
                                                <thead class="table-light">
                                                    <tr>
                                                        <th class="border-0 px-3 small fw-bold text-muted">MÃ LỚP</th>
                                                        <th class="border-0 small fw-bold text-muted">TÊN LỚP</th>
                                                        <th class="border-0 text-center small fw-bold text-muted">KHÓA
                                                            HỌC</th>
                                                        <th class="border-0 text-center small fw-bold text-muted">KHOA
                                                        </th>
                                                    </tr>
                                                </thead>
                                                <tbody id="class-table-body" class="border-top-0">
                                                    <c:forEach var="lop" items="${lopList}">
                                                        <c:set var="classUrl" value="${pageContext.request.contextPath}/student?maLop=${lop.maLop}" />
                                                        <c:if test="${not empty maKhoa && maKhoa != 'all'}">
                                                            <c:set var="classUrl" value="${classUrl}&maKhoa=${maKhoa}" />
                                                        </c:if>
                                                        <tr onclick="window.location.href='${classUrl}'"
                                                            style="cursor: pointer; transition: 0.2s;"
                                                            class="${lop.maLop == maLop ? 'table-primary shadow-sm' : ''} class-row">
                                                            <td class="px-3">
                                                                <span
                                                                    class="badge ${lop.maLop == maLop ? 'bg-primary' : 'bg-primary bg-opacity-10 text-primary'} rounded-2 px-3 py-2">
                                                                    ${lop.maLop}
                                                                </span>
                                                            </td>
                                                            <td class="fw-semibold text-dark">${lop.tenLop}</td>
                                                            <td class="text-center"><span
                                                                    class="text-muted small fw-bold">${lop.khoaHoc}</span>
                                                            </td>
                                                            <td class="text-center">
                                                                <span
                                                                    class="badge border border-info text-info rounded-pill px-3 py-1 fw-bold small">${lop.maKhoa}</span>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="mt-2 text-muted small px-1">
                                            <i class="bi bi-info-circle me-1"></i> Chọn một dòng để nạp danh sách sinh
                                            viên tương ứng phía dưới
                                        </div>
                                    </div>
                                </div>

                                <!-- DETAIL SECTION: STUDENT LIST -->
                                <div id="student-section">
                                    <c:choose>
                                        <c:when test="${not empty maLop}">
                                            <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
                                                <div
                                                    class="card-header bg-white border-bottom-0 pt-4 px-4 pb-3 d-flex justify-content-between align-items-center">
                                                    <h6
                                                        class="fw-bold text-success text-uppercase small mb-0 d-flex align-items-center gap-2">
                                                        <i class="bi bi-people-fill text-success"></i>
                                                        Danh sách sinh viên - Lớp: <span
                                                            class="text-dark">${maLop}</span>
                                                    </h6>
                                                    <button
                                                        class="btn btn-primary btn-sm rounded-3 px-3 fw-bold shadow-sm"
                                                        data-bs-toggle="modal" data-bs-target="#studentModal"
                                                        onclick="resetForm()">
                                                        <i class="bi bi-person-plus-fill me-1"></i> Cập nhật Sinh viên
                                                    </button>
                                                </div>
                                                <div class="card-body px-4 pb-4">
                                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                                        <div class="input-group" style="max-width: 300px;">
                                                            <span class="input-group-text bg-light border-0"><i
                                                                    class="bi bi-search text-muted"></i></span>
                                                            <input type="text" id="student-search"
                                                                class="form-control bg-light border-0 small"
                                                                placeholder="Tìm MSV hoặc tên sinh viên..."
                                                                onkeyup="filterLocalStudents()">
                                                        </div>
                                                    </div>

                                                    <div class="table-responsive rounded-3 border">
                                                        <table class="table table-hover align-middle mb-0">
                                                            <thead class="table-light">
                                                                <tr>
                                                                    <th class="border-0 px-3 small fw-bold text-muted">
                                                                        MÃ SV</th>
                                                                    <th class="border-0 small fw-bold text-muted">HỌ VÀ
                                                                        TÊN</th>
                                                                    <th
                                                                        class="border-0 text-center small fw-bold text-muted">
                                                                        PHÁI</th>
                                                                    <th
                                                                        class="border-0 text-center small fw-bold text-muted">
                                                                        NGÀY SINH</th>
                                                                    <th class="border-0 small fw-bold text-muted">ĐỊA
                                                                        CHỈ</th>
                                                                    <th
                                                                        class="border-0 text-center small fw-bold text-muted">
                                                                        TRẠNG THÁI</th>
                                                                    <th
                                                                        class="border-0 text-center small fw-bold text-muted">
                                                                        THAO TÁC</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody id="main-student-table-body">
                                                                <c:forEach var="sv" items="${sinhVienList}">
                                                                    <tr>
                                                                        <td class="px-3"><span
                                                                                class="badge bg-info bg-opacity-10 text-info fw-bold">${sv.maSV}</span>
                                                                        </td>
                                                                        <td>
                                                                            <div
                                                                                class="fw-bold text-dark d-flex align-items-center gap-2">
                                                                                <i
                                                                                    class="bi bi-person-circle text-muted"></i>
                                                                                ${sv.ho} ${sv.ten}
                                                                            </div>
                                                                        </td>
                                                                        <td class="text-center">
                                                                            <span
                                                                                class="badge rounded-pill border border-secondary text-secondary px-3 py-1 small">
                                                                                <i
                                                                                    class="bi ${sv.phai == 'Nam' ? 'bi-gender-male' : 'bi-gender-female'} me-1"></i>
                                                                                ${sv.phai}
                                                                            </span>
                                                                        </td>
                                                                        <td class="text-center text-muted small">
                                                                            <i class="bi bi-calendar3 me-1"></i>
                                                                            <fmt:formatDate value="${sv.ngaySinh}"
                                                                                pattern="dd/MM/yyyy" />
                                                                        </td>
                                                                        <td class="text-muted small"><i
                                                                                class="bi bi-geo-alt me-1"></i>
                                                                            ${sv.diaChi}</td>
                                                                        <td class="text-center">
                                                                            <c:choose>
                                                                                <c:when test="${sv.dangNghiHoc}">
                                                                                    <span
                                                                                        class="badge border border-danger text-danger rounded-pill px-3 py-1 small fw-bold">
                                                                                        <i class="bi bi-dot"></i> Đang
                                                                                        nghỉ
                                                                                    </span>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <span
                                                                                        class="badge border border-success text-success rounded-pill px-3 py-1 small fw-bold">
                                                                                        <i class="bi bi-dot"></i> Đang
                                                                                        học
                                                                                    </span>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </td>
                                                                        <td class="text-center">
                                                                            <div
                                                                                class="d-flex gap-2 justify-content-center">
                                                                                <button type="button"
                                                                                    onclick="selectStudent('${sv.maSV}', 'edit')"
                                                                                    class="btn btn-sm btn-outline-primary border-0 rounded-3">
                                                                                    <i class="bi bi-pencil-square"></i>
                                                                                </button>
                                                                                <button type="button"
                                                                                    onclick="selectStudent('${sv.maSV}', 'delete')"
                                                                                    class="btn btn-sm btn-outline-danger border-0 rounded-3">
                                                                                    <i class="bi bi-trash3"></i>
                                                                                </button>
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                </c:forEach>
                                                                <c:if test="${empty sinhVienList}">
                                                                    <tr>
                                                                        <td colspan="7"
                                                                            class="text-center py-5 text-muted">
                                                                            <i
                                                                                class="bi bi-inbox fs-1 d-block mb-2"></i>
                                                                            Chưa có dữ liệu sinh viên cho lớp này
                                                                        </td>
                                                                    </tr>
                                                                </c:if>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                    <div class="mt-3 d-flex justify-content-between align-items-center">
                                                        <div class="text-muted small">Hiển thị
                                                            <strong>${sinhVienList.size()}</strong> sinh viên
                                                        </div>
                                                        <nav>
                                                            <ul class="pagination pagination-sm mb-0">
                                                                <li class="page-item disabled"><a class="page-link"
                                                                        href="#"><i class="bi bi-chevron-left"></i></a>
                                                                </li>
                                                                <li class="page-item active"><a class="page-link"
                                                                        href="#">1</a></li>
                                                                <li class="page-item disabled"><a class="page-link"
                                                                        href="#"><i class="bi bi-chevron-right"></i></a>
                                                                </li>
                                                            </ul>
                                                        </nav>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div
                                                class="text-center py-5 bg-white rounded-4 shadow-sm border border-dashed">
                                                <i
                                                    class="bi bi-arrow-up-circle fs-1 text-primary opacity-25 d-block mb-3"></i>
                                                <h5 class="text-muted fw-bold">Vui lòng chọn một lớp để xem danh sách
                                                    sinh viên</h5>
                                                <p class="text-muted small">Thông tin sinh viên sẽ hiển thị tại đây sau
                                                    khi bạn chọn lớp từ danh sách phía trên.</p>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                            </div>
                        </main>
                    </div>
                </div>

                <!-- STUDENT MODAL -->
                <div class="modal fade" id="studentModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-lg modal-dialog-centered">
                        <div class="modal-content border-0 shadow-lg rounded-4">
                            <div class="modal-header bg-primary text-white border-0 py-3 px-4 rounded-top-4">
                                <h5 class="modal-title fw-bold d-flex align-items-center gap-2">
                                    <i class="bi bi-person-vcard"></i> Quản lý Hồ sơ Sinh viên
                                </h5>
                                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                                    aria-label="Close"></button>
                            </div>
                            <form id="studentForm" action="${pageContext.request.contextPath}/student" method="post">
                                <div class="modal-body p-4">

                                    <!-- TOOLBAR -->
                                    <div class="d-flex gap-2 mb-4 p-2 bg-light rounded-3 border shadow-sm">
                                        <button type="button" id="btn_mode_add"
                                            class="btn btn-toolbar-add fw-bold flex-fill py-2"
                                            onclick="handleModeClick('add')">
                                            <i class="bi bi-plus-circle"></i> <span class="btn-text">THÊM</span>
                                        </button>
                                        <button type="button" id="btn_mode_edit"
                                            class="btn btn-toolbar-edit fw-bold flex-fill py-2"
                                            onclick="handleModeClick('edit')">
                                            <i class="bi bi-pencil-square"></i> <span class="btn-text">SỬA</span>
                                        </button>
                                        <button type="button" id="btn_mode_delete"
                                            class="btn btn-toolbar-delete fw-bold flex-fill py-2"
                                            onclick="handleModeClick('delete')">
                                            <i class="bi bi-trash3"></i> <span class="btn-text">XÓA</span>
                                        </button>
                                        <button type="button" id="btn_mode_cancel"
                                            class="btn btn-toolbar-cancel fw-bold flex-fill py-2"
                                            onclick="handleModeClick('none')" disabled>
                                            <i class="bi bi-x-circle"></i> HỦY
                                        </button>

                                        <!-- Hidden real submit buttons for Spring Controller -->
                                        <button type="submit" id="real_submit_insert" name="btnInsert"
                                            style="display:none;"></button>
                                        <button type="submit" id="real_submit_update" name="btnUpdate"
                                            style="display:none;"></button>
                                        <button type="submit" id="real_submit_delete" name="btnDelete"
                                            style="display:none;"></button>
                                    </div>

                                    <div class="row g-3">
                                        <div class="col-md-4">
                                            <label class="form-label small fw-bold text-muted">MÃ SINH VIÊN</label>
                                            <input type="text" class="form-control rounded-3" id="inp_maSV" name="maSV"
                                                value="${sinhVien.maSV}" placeholder="VD: N23DCCN001" required disabled>
                                        </div>
                                        <div class="col-md-4">
                                            <label class="form-label small fw-bold text-muted">HỌ</label>
                                            <input type="text" class="form-control rounded-3" id="inp_ho" name="ho"
                                                value="${sinhVien.ho}" placeholder="VD: Nguyễn Văn" required disabled>
                                        </div>
                                        <div class="col-md-4">
                                            <label class="form-label small fw-bold text-muted">TÊN</label>
                                            <input type="text" class="form-control rounded-3" id="inp_ten" name="ten"
                                                value="${sinhVien.ten}" placeholder="VD: An" required disabled>
                                        </div>
                                        <div class="col-md-4">
                                            <label class="form-label small fw-bold text-muted">PHÁI</label>
                                            <select class="form-select rounded-3" id="inp_phai" name="phai" disabled>
                                                <option value="Nam" ${sinhVien.phai=='Nam' ? 'selected' : '' }>Nam
                                                </option>
                                                <option value="Nữ" ${sinhVien.phai=='Nữ' ? 'selected' : '' }>Nữ</option>
                                            </select>
                                        </div>
                                        <div class="col-md-4">
                                            <label class="form-label small fw-bold text-muted">NGÀY SINH</label>
                                            <fmt:formatDate var="fmtDate" value="${sinhVien.ngaySinh}"
                                                pattern="yyyy-MM-dd" />
                                            <input type="date" class="form-control rounded-3" id="inp_ngaySinh"
                                                name="ngaySinh" value="${fmtDate}" required disabled>
                                        </div>
                                        <div class="col-md-4">
                                            <label class="form-label small fw-bold text-muted">LỚP HIỆN TẠI</label>
                                            <input type="text" class="form-control rounded-3 bg-light" name="maLop"
                                                value="${not empty maLop ? maLop : sinhVien.maLop}" readonly>
                                        </div>
                                        <div class="col-md-12">
                                            <label class="form-label small fw-bold text-muted">ĐỊA CHỈ</label>
                                            <input type="text" class="form-control rounded-3" id="inp_diaChi"
                                                name="diaChi" value="${sinhVien.diaChi}"
                                                placeholder="VD: 97 Man Thiện, Q.9, TP.HCM" disabled>
                                        </div>
                                        <div class="col-md-12">
                                            <div class="form-check form-switch p-0 d-flex align-items-center gap-2">
                                                <input class="form-check-input ms-0 mt-0" type="checkbox" role="switch"
                                                    id="dangNghiHoc" name="dangNghiHoc" value="true"
                                                    ${sinhVien.dangNghiHoc ? 'checked' : '' } disabled>
                                                <label class="form-check-label fw-bold text-danger"
                                                    for="dangNghiHoc">Đang nghỉ học</label>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- MINI TABLE: STUDENTS IN CLASS -->
                                    <div class="mt-4 pt-3 border-top">
                                        <h6 class="fw-bold text-primary mb-3 d-flex align-items-center gap-2">
                                            <i class="bi bi-list-ul"></i> Sinh viên hiện có trong lớp ${maLop}
                                        </h6>
                                        <div class="table-responsive rounded-3 border bg-white shadow-sm"
                                            style="max-height: 200px; overflow-y: auto;">
                                            <table class="table table-hover table-sm align-middle mb-0">
                                                <thead class="table-light sticky-top">
                                                    <tr>
                                                        <th class="border-0 px-3 small fw-bold text-muted">MÃ SV</th>
                                                        <th class="border-0 small fw-bold text-muted">HỌ VÀ TÊN</th>
                                                        <th class="border-0 text-center small fw-bold text-muted">PHÁI
                                                        </th>
                                                        <th class="border-0 text-center small fw-bold text-muted">THAO
                                                            TÁC</th>
                                                    </tr>
                                                </thead>
                                                <tbody id="mini-table-body">
                                                    <c:forEach var="sv_item" items="${sinhVienList}">
                                                        <tr
                                                            class="${sv_item.maSV == sinhVien.maSV ? 'table-primary shadow-sm' : ''}">
                                                            <td class="px-3"><span
                                                                    class="badge bg-primary bg-opacity-10 text-primary small">${sv_item.maSV}</span>
                                                            </td>
                                                            <td class="fw-bold text-dark small">${sv_item.ho}
                                                                ${sv_item.ten}</td>
                                                            <td class="text-center small"><span
                                                                    class="badge border text-secondary rounded-pill px-2">${sv_item.phai}</span>
                                                            </td>
                                                            <td class="text-center">
                                                                <div class="d-flex justify-content-center gap-1">
                                                                    <button type="button"
                                                                        onclick="event.stopPropagation(); selectStudent('${sv_item.maSV}', 'edit', true)"
                                                                        class="btn btn-xs btn-outline-primary border-0 p-1">
                                                                        <i class="bi bi-pencil-square"></i>
                                                                    </button>
                                                                    <button type="button"
                                                                        onclick="event.stopPropagation(); selectStudent('${sv_item.maSV}', 'delete', true)"
                                                                        class="btn btn-xs btn-outline-danger border-0 p-1">
                                                                        <i class="bi bi-trash3"></i>
                                                                    </button>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                    <c:if test="${empty sinhVienList}">
                                                        <tr>
                                                            <td colspan="4" class="text-center py-3 text-muted small">
                                                                Chưa có sinh viên</td>
                                                        </tr>
                                                    </c:if>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- NOTIFICATION MODAL -->
                <div class="modal fade" id="notifyModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered modal-sm">
                        <div class="modal-content border-0 shadow-lg rounded-4">
                            <div class="modal-body text-center p-4">
                                <div id="notifyIcon" class="mb-3"></div>
                                <h5 id="notifyTitle" class="fw-bold mb-2"></h5>
                                <p id="notifyMessage" class="text-muted small mb-4"></p>
                                <button type="button" class="btn btn-primary w-100 rounded-3 fw-bold shadow-sm"
                                    data-bs-dismiss="modal">ĐÓNG</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- CONFIRM MODAL -->
                <div class="modal fade" id="confirmModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered modal-sm">
                        <div class="modal-content border-0 shadow-lg rounded-4">
                            <div class="modal-body text-center p-4">
                                <div class="mb-3">
                                    <i class="bi bi-exclamation-triangle-fill text-warning"
                                        style="font-size: 3.5rem;"></i>
                                </div>
                                <h5 id="confirmTitle" class="fw-bold mb-2"></h5>
                                <p id="confirmMessage" class="text-muted small mb-4"></p>
                                <div class="d-flex gap-2">
                                    <button type="button" class="btn btn-light w-100 rounded-3 fw-bold"
                                        data-bs-dismiss="modal">HỦY</button>
                                    <button type="button" id="confirmOkBtn"
                                        class="btn btn-danger w-100 rounded-3 fw-bold shadow-sm">XÓA</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Bootstrap JS -->
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
                <style>
                    .class-row:hover {
                        background-color: #f8fbff !important;
                    }

                    .table-hover tbody tr {
                        transition: all 0.2s;
                    }

                    .border-dashed {
                        border: 2px dashed #dee2e6 !important;
                    }

                    .form-control:focus,
                    .form-select:focus {
                        box-shadow: none;
                        border-color: #4361ee;
                    }

                    .app-content {
                        min-height: 100vh;
                    }

                    .btn-xs {
                        padding: 0.1rem 0.3rem;
                        font-size: 0.75rem;
                    }

                    /* Premium Toolbar Colors */
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
                <script>
                    let currentMode = 'none'; // 'none', 'add', 'edit', 'delete'
                    let originalMaSV = '';
                    const inputs = ['inp_maSV', 'inp_ho', 'inp_ten', 'inp_phai', 'inp_ngaySinh', 'inp_diaChi', 'dangNghiHoc'];
                    const contextPath = '${pageContext.request.contextPath}';
                    const currentMaLop = '${maLop}';

                    document.addEventListener('DOMContentLoaded', function () {
                        // Khởi tạo trang: Nếu có mã lớp thì tải danh sách
                        if (currentMaLop) {
                            refreshTables();
                        }
                    });

                    function openModal() {
                        var myModalElement = document.getElementById('studentModal');
                        var myModal = bootstrap.Modal.getOrCreateInstance(myModalElement);
                        myModal.show();
                    }

                    function showNotify(title, message, type = 'success') {
                        const titleEl = document.getElementById('notifyTitle');
                        const msgEl = document.getElementById('notifyMessage');
                        const iconEl = document.getElementById('notifyIcon');

                        titleEl.innerText = title;
                        msgEl.innerText = message;

                        if (type === 'success') {
                            iconEl.innerHTML = '<i class="bi bi-check-circle-fill text-success" style="font-size: 3.5rem;"></i>';
                        } else if (type === 'error') {
                            iconEl.innerHTML = '<i class="bi bi-x-circle-fill text-danger" style="font-size: 3.5rem;"></i>';
                        } else {
                            iconEl.innerHTML = '<i class="bi bi-info-circle-fill text-primary" style="font-size: 3.5rem;"></i>';
                        }

                        const notifyModalElement = document.getElementById('notifyModal');
                        const notifyModal = bootstrap.Modal.getOrCreateInstance(notifyModalElement);
                        notifyModal.show();
                    }

                    function showConfirm(title, message, onConfirm) {
                        const titleEl = document.getElementById('confirmTitle');
                        const msgEl = document.getElementById('confirmMessage');
                        const okBtn = document.getElementById('confirmOkBtn');

                        titleEl.innerText = title;
                        msgEl.innerText = message;

                        const confirmModalElement = document.getElementById('confirmModal');
                        const confirmModal = bootstrap.Modal.getOrCreateInstance(confirmModalElement);

                        okBtn.onclick = () => {
                            confirmModal.hide();
                            onConfirm();
                        };

                        confirmModal.show();
                    }

                    async function refreshTables() {
                        if (!currentMaLop) return;
                        try {
                            const response = await fetch(contextPath + '/student/api/list?maLop=' + currentMaLop);
                            const students = await response.json();
                            renderTable('main-student-table-body', students, false);
                            renderTable('mini-table-body', students, true);
                        } catch (error) {
                            console.error('Error refreshing tables:', error);
                        }
                    }

                    function renderTable(containerId, students, isMini) {
                        const container = document.getElementById(containerId);
                        if (!container) return;

                        if (students.length === 0) {
                            container.innerHTML = '<tr><td colspan="' + (isMini ? 4 : 7) + '" class="text-center py-3 text-muted small">Chưa có sinh viên</td></tr>';
                            return;
                        }

                        container.innerHTML = students.map(sv => {
                            const date = sv.ngaySinh ? new Date(sv.ngaySinh) : null;
                            const ngaySinhStr = date ? (String(date.getDate()).padStart(2, '0') + '/' + String(date.getMonth() + 1).padStart(2, '0') + '/' + date.getFullYear()) : '';

                            const actionBtns = '<div class="d-flex justify-content-center gap-1">' +
                                '<button type="button" onclick="event.stopPropagation(); selectStudent(\'' + sv.maSV + '\', \'edit\', true)" class="btn ' + (isMini ? 'btn-xs' : 'btn-sm') + ' btn-outline-primary border-0">' +
                                '<i class="bi bi-pencil-square"></i>' +
                                '</button>' +
                                '<button type="button" onclick="event.stopPropagation(); selectStudent(\'' + sv.maSV + '\', \'delete\', true)" class="btn ' + (isMini ? 'btn-xs' : 'btn-sm') + ' btn-outline-danger border-0">' +
                                '<i class="bi bi-trash3"></i>' +
                                '</button>' +
                                '</div>';

                            let rowHtml = '<tr onclick="selectStudent(\'' + sv.maSV + '\', \'edit\', false)" style="cursor: pointer;">' +
                                '<td class="px-3"><span class="badge bg-primary bg-opacity-10 text-primary fw-bold">' + (isMini ? sv.maSV.substring(0, 8) : sv.maSV) + '</span></td>' +
                                '<td>' +
                                '<div class="fw-bold text-dark small">' + sv.ho + ' ' + sv.ten + '</div>' +
                                (!isMini ? '<div class="text-muted small"><i class="bi bi-geo-alt"></i> ' + (sv.diaChi || '') + '</div>' : '') +
                                '</td>' +
                                '<td class="text-center small"><span class="badge border text-secondary rounded-pill px-2">' + sv.phai + '</span></td>';

                            if (!isMini) {
                                rowHtml += '<td class="text-center small">' + ngaySinhStr + '</td>' +
                                    '<td class="small">' + (sv.diaChi || '') + '</td>' +
                                    '<td class="text-center">' +
                                    '<span class="badge ' + (sv.dangNghiHoc ? 'bg-danger' : 'bg-success') + ' bg-opacity-10 ' + (sv.dangNghiHoc ? 'text-danger' : 'text-success') + ' rounded-pill px-3 py-1 small">' +
                                    (sv.dangNghiHoc ? 'Nghỉ học' : 'Đang học') +
                                    '</span>' +
                                    '</td>';
                            }

                            rowHtml += '<td class="text-center">' + actionBtns + '</td></tr>';
                            return rowHtml;
                        }).join('');
                    }

                    async function selectStudent(maSV, mode, forceMode = false) {
                        try {
                            const response = await fetch(contextPath + '/student/api/get?maSV=' + maSV);
                            const sv = await response.json();
                            if (sv) {
                                fillForm(sv);
                                openModal();

                                if (forceMode || currentMode === 'none') {
                                    setMode(mode, true);
                                } else {
                                    setMode(currentMode, true); // Giữ nguyên chế độ hiện tại
                                }
                            }
                        } catch (error) {
                            console.error('Error selecting student:', error);
                        }
                    }

                    function fillForm(sv) {
                        document.getElementById('inp_maSV').value = sv.maSV || '';
                        originalMaSV = sv.maSV || '';
                        document.getElementById('inp_ho').value = sv.ho || '';
                        document.getElementById('inp_ten').value = sv.ten || '';
                        document.getElementById('inp_phai').value = sv.phai || 'Nam';
                        document.getElementById('inp_diaChi').value = sv.diaChi || '';
                        document.getElementById('dangNghiHoc').checked = sv.dangNghiHoc || false;

                        if (sv.ngaySinh) {
                            const date = new Date(sv.ngaySinh);
                            const yyyy = date.getFullYear();
                            const mm = String(date.getMonth() + 1).padStart(2, '0');
                            const dd = String(date.getDate()).padStart(2, '0');
                            document.getElementById('inp_ngaySinh').value = yyyy + '-' + mm + '-' + dd;
                        } else {
                            document.getElementById('inp_ngaySinh').value = '';
                        }
                    }

                    async function handleModeClick(mode) {
                        if (mode === 'none') {
                            setMode('none');
                            return;
                        }

                        if (currentMode === mode) {
                            if (mode === 'delete') {
                                await performDelete();
                            } else {
                                await performSave();
                            }
                        } else if (currentMode === 'none') {
                            setMode(mode);
                            if (mode === 'add') clearForm();
                        }
                    }

                    function clearForm() {
                        inputs.forEach(id => {
                            const el = document.getElementById(id);
                            if (el.type === 'checkbox') el.checked = false;
                            else el.value = '';
                        });
                    }

                    async function performSave() {
                        const sv = {
                            maSV: document.getElementById('inp_maSV').value,
                            ho: document.getElementById('inp_ho').value,
                            ten: document.getElementById('inp_ten').value,
                            phai: document.getElementById('inp_phai').value,
                            ngaySinh: document.getElementById('inp_ngaySinh').value,
                            diaChi: document.getElementById('inp_diaChi').value,
                            dangNghiHoc: document.getElementById('dangNghiHoc').checked,
                            maLop: currentMaLop
                        };

                        if (!sv.maSV || !sv.ho || !sv.ten) {
                            showNotify('Thông báo', 'Vui lòng điền đầy đủ Mã SV, Họ và Tên!', 'info');
                            return;
                        }

                        try {
                            let url = contextPath + '/student/api/save?mode=' + currentMode;
                            if (currentMode === 'edit') url += '&oldMaSV=' + originalMaSV;

                            const response = await fetch(url, {
                                method: 'POST',
                                headers: { 'Content-Type': 'application/json' },
                                body: JSON.stringify(sv)
                            });
                            const result = await response.json();
                            if (result.status === 'success') {
                                showNotify('Thành công', 'Thông tin sinh viên đã được lưu lại hệ thống.');
                                refreshTables();
                                setMode('none');
                            } else {
                                showNotify('Lỗi', result.message, 'error');
                            }
                        } catch (error) {
                            showNotify('Lỗi', 'Không thể lưu dữ liệu: ' + error, 'error');
                        }
                    }

                    async function performDelete() {
                        const maSV = document.getElementById('inp_maSV').value;

                        showConfirm('Xác nhận xóa', 'Bạn có chắc chắn muốn xóa sinh viên này khỏi danh sách? Thao tác này không thể hoàn tác.', async () => {
                            try {
                                const response = await fetch(contextPath + '/student/api/delete?maSV=' + maSV, {
                                    method: 'POST'
                                });
                                const result = await response.json();
                                if (result.status === 'success') {
                                    showNotify('Đã xóa', 'Sinh viên đã được loại bỏ khỏi danh sách.');
                                    refreshTables();
                                    setMode('none');
                                } else {
                                    showNotify('Lỗi', result.message, 'error');
                                }
                            } catch (error) {
                                showNotify('Lỗi', 'Lỗi khi thực hiện xóa: ' + error, 'error');
                            }
                        });
                    }

                    function setMode(mode, fromInit = false) {
                        currentMode = mode;
                        const btnAdd = document.getElementById('btn_mode_add');
                        const btnEdit = document.getElementById('btn_mode_edit');
                        const btnDelete = document.getElementById('btn_mode_delete');
                        const btnCancel = document.getElementById('btn_mode_cancel');

                        [btnAdd, btnEdit, btnDelete, btnCancel].forEach(btn => {
                            btn.disabled = false;
                            btn.classList.remove('active', 'btn-toolbar-disabled');
                        });

                        btnAdd.querySelector('.btn-text').innerText = 'THÊM';
                        btnEdit.querySelector('.btn-text').innerText = 'SỬA';
                        btnDelete.querySelector('.btn-text').innerText = 'XÓA';

                        btnCancel.disabled = true;
                        inputs.forEach(id => {
                            const el = document.getElementById(id);
                            el.disabled = true;
                            el.classList.remove('bg-white');
                        });

                        if (mode === 'none') {
                            clearForm();
                        } else {
                            btnCancel.disabled = false;

                            if (mode === 'add') {
                                btnEdit.classList.add('btn-toolbar-disabled');
                                btnDelete.classList.add('btn-toolbar-disabled');
                                btnAdd.classList.add('active');
                                btnAdd.querySelector('.btn-text').innerText = 'GHI (THÊM)';
                                inputs.forEach(id => {
                                    const el = document.getElementById(id);
                                    el.disabled = false;
                                    el.classList.add('bg-white');
                                    el.readOnly = false;
                                });
                            } else if (mode === 'edit') {
                                btnAdd.classList.add('btn-toolbar-disabled');
                                btnDelete.classList.add('btn-toolbar-disabled');
                                btnEdit.classList.add('active');
                                btnEdit.querySelector('.btn-text').innerText = 'GHI (SỬA)';
                                inputs.forEach(id => {
                                    const el = document.getElementById(id);
                                    el.disabled = false;
                                    el.classList.add('bg-white');
                                    if (id === 'inp_maSV') el.readOnly = true;
                                    else el.readOnly = false;
                                });
                            } else if (mode === 'delete') {
                                btnAdd.classList.add('btn-toolbar-disabled');
                                btnEdit.classList.add('btn-toolbar-disabled');
                                btnDelete.classList.add('active');
                                btnDelete.querySelector('.btn-text').innerText = 'GHI (XÓA)';
                            }
                        }
                    }

                    function resetForm() {
                        clearForm();
                        setMode('none');
                        openModal();
                    }

                    async function filterByKhoa() {
                        const maKhoa = document.getElementById('khoa-filter').value;
                        try {
                            const response = await fetch(contextPath + '/student/api/classes?maKhoa=' + maKhoa);
                            const classes = await response.json();
                            renderClassTable(classes);
                        } catch (error) {
                            console.error('Error filtering classes:', error);
                        }
                    }

                    function navigateToClass(maLop) {
                        const maKhoa = document.getElementById('khoa-filter').value;
                        let url = contextPath + '/student?maLop=' + maLop;
                        if (maKhoa && maKhoa !== 'all') url += '&maKhoa=' + maKhoa;
                        window.location.href = url;
                    }

                    function renderClassTable(classes) {
                        const container = document.getElementById('class-table-body');
                        if (!container) return;

                        if (classes.length === 0) {
                            container.innerHTML = '<tr><td colspan="4" class="text-center py-4 text-muted">Không tìm thấy lớp nào cho khoa này</td></tr>';
                            return;
                        }

                        container.innerHTML = classes.map(lop => {
                            const isActive = lop.maLop === currentMaLop;
                            const activeClass = isActive ? 'table-primary shadow-sm' : '';
                            const badgeClass = isActive ? 'bg-primary' : 'bg-primary bg-opacity-10 text-primary';
                            return '<tr onclick="navigateToClass(\'' + lop.maLop + '\')" style="cursor: pointer; transition: 0.2s;" class="' + activeClass + ' class-row">' +
                                '<td class="px-3"><span class="badge ' + badgeClass + ' rounded-2 px-3 py-2">' + lop.maLop + '</span></td>' +
                                '<td class="fw-semibold text-dark">' + lop.tenLop + '</td>' +
                                '<td class="text-center"><span class="text-muted small fw-bold">' + lop.khoaHoc + '</span></td>' +
                                '<td class="text-center"><span class="badge border border-info text-info rounded-pill px-3 py-1 fw-bold small">' + lop.maKhoa + '</span></td>' +
                                '</tr>';
                        }).join('');
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