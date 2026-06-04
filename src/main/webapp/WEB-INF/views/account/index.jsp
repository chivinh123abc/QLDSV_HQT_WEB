<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
        <%@ taglib prefix="c" uri="jakarta.tags.core" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="utf-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <title>
                    <s:message code="account.management.title" />
                </title>
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

                    .table-custom tbody tr {
                        cursor: pointer;
                        transition: all 0.2s;
                    }

                    .table-custom tbody tr:hover {
                        background-color: #f0f7ff !important;
                    }

                    .modal-header-custom {
                        background-color: #4361ee;
                        color: white;
                        border-bottom: 0;
                    }

                    .modal-header-custom .btn-close {
                        filter: invert(1) grayscale(100%) brightness(200%);
                    }
                </style>
            </head>

            <body>
                <div class="app-layout">
                    <jsp:include page="/WEB-INF/views/shared/sidebar.jsp" />

                    <div class="app-main">
                        <jsp:include page="/WEB-INF/views/shared/header.jsp" />

                        <main id="main-content" class="app-content p-4 bg-light">
                            <div class="container-fluid max-w-7xl mx-auto">
                                <div class="d-flex align-items-center gap-2 mb-4">
                                    <i class="bi bi-person-lines-fill text-primary fs-3"></i>
                                    <h3 class="mb-0 fw-bold text-dark">
                                        <s:message code="account.management" />
                                    </h3>
                                </div>

                                <!-- FLASH MESSAGES -->
                                <c:if test="${not empty message}">
                                    <div class="alert alert-success alert-dismissible fade show rounded-3 shadow-sm border-0 mb-4"
                                        role="alert">
                                        <i class="bi bi-check-circle-fill me-2"></i> ${message}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                            aria-label="Close"></button>
                                    </div>
                                </c:if>
                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger alert-dismissible fade show rounded-3 shadow-sm border-0 mb-4"
                                        role="alert">
                                        <i class="bi bi-exclamation-triangle-fill me-2"></i> ${error}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                            aria-label="Close"></button>
                                    </div>
                                </c:if>

                                <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
                                    <div
                                        class="card-header bg-white border-bottom-0 pt-4 px-4 pb-0 d-flex justify-content-between align-items-center">
                                        <div>
                                            <h6 class="fw-bold text-primary text-uppercase small mb-1">
                                                <s:message code="account.list" />
                                            </h6>
                                            <p class="text-muted small mb-0">
                                                <s:message code="account.management.desc" />
                                            </p>
                                        </div>
                                        <div class="d-flex gap-2">
                                            <form action="${pageContext.request.contextPath}/accounts/import" method="POST" enctype="multipart/form-data" class="d-inline-flex gap-1 align-items-center mb-0">
                                                <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                                <input type="file" name="file" accept=".csv" required class="form-control form-control-sm rounded-3" style="max-width: 200px;" />
                                                <button type="submit" class="btn btn-outline-success btn-sm rounded-3 fw-bold shadow-sm">
                                                    <i class="bi bi-file-earmark-arrow-up-fill me-1"></i> <s:message code="account.import.csv" />
                                                </button>
                                            </form>
                                            <a href="${pageContext.request.contextPath}/accounts?lnkAdd=true"
                                                class="btn btn-primary btn-sm rounded-3 px-3 fw-bold shadow-sm d-inline-flex align-items-center">
                                                <i class="bi bi-plus-circle-fill me-1"></i>
                                                <s:message code="account.grant.new.btn" />
                                            </a>
                                        </div>
                                    </div>

                                    <div class="card-body px-4 pb-4">
                                        <div
                                            class="d-flex justify-content-between align-items-center mb-4 pb-3 border-bottom">
                                            <div class="input-group" style="max-width: 300px;">
                                                <span class="input-group-text bg-light border-0"><i
                                                        class="bi bi-search text-muted"></i></span>
                                                <input type="text" id="search-input"
                                                    class="form-control bg-light border-0 small"
                                                    placeholder="<s:message code="account.search.username" />"
                                                onkeyup="filterLocal()">
                                            </div>
                                            <div class="d-flex gap-3 align-items-center">
                                                <label class="fw-bold small text-muted text-uppercase mb-0">
                                                    <s:message code="account.filter.by.role" />
                                                </label>
                                                <select id="role-filter"
                                                    class="form-select form-select-sm border-0 bg-light text-primary fw-bold"
                                                    style="min-width: 150px;" onchange="filterLocalByRole()">
                                                    <option value="all">
                                                        <s:message code="account.all.roles" />
                                                    </option>
                                                    <option value="1"><s:message code="account.role.pgv" /></option>
                                                    <option value="2"><s:message code="account.role.faculty" /></option>
                                                    <option value="3"><s:message code="account.role.student" /></option>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="table-responsive rounded-3 border">
                                            <table class="table table-custom align-middle mb-0">
                                                <thead class="table-light">
                                                    <tr>
                                                        <th class="px-3" style="width: 10%;">ID</th>
                                                        <th style="width: 15%;">
                                                            <s:message code="account.username.label" />
                                                        </th>
                                                        <th style="width: 15%;">
                                                            <s:message code="account.lbl.fullName" />
                                                        </th>
                                                        <th style="width: 20%;">EMAIL</th>
                                                        <th class="text-center" style="width: 15%;">
                                                            <s:message code="account.lbl.roleGroup" />
                                                        </th>
                                                        <th class="text-center" style="width: 15%;">
                                                            TRẠNG THÁI
                                                        </th>
                                                        <th class="text-center" style="width: 10%;">
                                                            <s:message code="global.lbl.actions" />
                                                        </th>
                                                    </tr>
                                                </thead>
                                                <tbody id="user-table-body">
                                                    <s:message code="account.js.confirmDelete" var="confirmDeleteMsg" />
                                                 <c:forEach var="item" items="${userList}">
                                                        <tr data-role="${item.roleId}">
                                                            <td class="px-3"><span
                                                                    class="badge-soft-primary">${item.userId}</span>
                                                            </td>
                                                            <td>
                                                                <div class="fw-bold text-dark">${item.username}</div>
                                                            </td>
                                                            <td>
                                                                <div class="text-muted small">${item.fullName}</div>
                                                            </td>
                                                            <td>
                                                                <div class="text-muted small">${item.email}</div>
                                                            </td>
                                                            <td class="text-center">
                                                                <c:choose>
                                                                    <c:when test="${item.roleId == 1}"><span
                                                                            class="badge bg-danger"><s:message code="account.role.pgv.short" /></span></c:when>
                                                                    <c:when test="${item.roleId == 2}"><span
                                                                            class="badge bg-warning text-dark"><s:message code="account.role.faculty.short" /></span>
                                                                    </c:when>
                                                                    <c:when test="${item.roleId == 3}"><span
                                                                            class="badge bg-success"><s:message code="account.role.student.short" /></span>
                                                                    </c:when>
                                                                    <c:otherwise><span
                                                                            class="badge bg-secondary"><s:message code="account.role.unknown.short" /></span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td class="text-center">
                                                                <c:choose>
                                                                    <c:when test="${item.status == 'CHUA_KICH_HOAT'}">
                                                                        <span class="badge bg-secondary">Chưa kích hoạt</span>
                                                                    </c:when>
                                                                    <c:when test="${item.status == 'DA_KICH_HOAT'}">
                                                                        <span class="badge bg-success">Đã kích hoạt</span>
                                                                    </c:when>
                                                                    <c:when test="${item.status == 'KHOA'}">
                                                                        <span class="badge bg-danger">Bị khóa</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="badge bg-light text-dark">${item.status}</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td class="text-center">
                                                                <div class="d-flex gap-2 justify-content-center">
                                                                    <a href="${pageContext.request.contextPath}/accounts?userId=${item.userId}&lnkEdit"
                                                                        class="btn btn-sm btn-outline-primary border-0 rounded-3">
                                                                        <i class="bi bi-pencil-square"></i>
                                                                    </a>
                                                                    <form
                                                                        action="${pageContext.request.contextPath}/accounts/delete"
                                                                        method="POST"
                                                                         onsubmit="return confirm('${confirmDeleteMsg}');"
                                                                        class="d-inline">
                                                                        <input type="hidden" name="csrf_token"
                                                                            value="${csrfToken}" />
                                                                        <input type="hidden" name="userId"
                                                                            value="${item.userId}">
                                                                        <button type="submit"
                                                                            class="btn btn-sm btn-outline-danger border-0 rounded-3 ${!item.canDelete ? 'disabled opacity-25' : ''}"
                                                                            <c:if test="${!item.canDelete}">disabled
                                                                            title="
                                                                            <s:message code='account.cannot.delete' />"
                                                                            </c:if>>
                                                                            <i class="bi bi-trash3"></i>
                                                                        </button>
                                                                    </form>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                    <c:if test="${empty userList}">
                                                        <tr>
                                                            <td colspan="7" class="text-center py-5 text-muted">
                                                                <i class="bi bi-inbox fs-1 d-block mb-3 opacity-25"></i>
                                                                <s:message code="account.no.data" />
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

                <!-- ACCOUNT MODAL (SSR) -->
                <c:if test="${not empty mode || not empty param.lnkAdd}">
                    <div class="modal fade show d-block" id="userModal" tabindex="-1"
                        style="background: rgba(0,0,0,0.5);">
                        <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content border-0 shadow-lg rounded-4">
                                <div class="modal-header bg-primary text-white border-0 py-3 px-4 rounded-top-4">
                                    <h5 class="modal-title fw-bold d-flex align-items-center gap-2">
                                        <i class="bi bi-shield-lock-fill"></i>
                                        <c:choose>
                                            <c:when test="${mode == 'edit'}">
                                                <s:message code="account.update" />
                                            </c:when>
                                            <c:otherwise>
                                                <s:message code="account.grant.new" />
                                            </c:otherwise>
                                        </c:choose>
                                    </h5>
                                    <a href="${pageContext.request.contextPath}/accounts"
                                        class="btn-close btn-close-white text-decoration-none"></a>
                                </div>
                                <form action="${pageContext.request.contextPath}/accounts/save" method="POST">
                                    <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                    <input type="hidden" name="version" value="${account.version}" />
                                    <div class="modal-body p-4">
                                        <div class="row g-3">
                                            <input type="hidden" name="mode" value="${not empty mode ? mode : 'add'}">
                                            <input type="hidden" name="userId" value="${account.tenDangNhap}">
                                            <div class="col-12">
                                                <label class="form-label small fw-bold text-muted">
                                                    <s:message code="account.lbl.roleGroup" /> <span
                                                        class="text-danger">*</span>
                                                </label>
                                                 <select class="form-select rounded-3" name="roleId" id="inp_roleId"
                                                     onchange="toggleRoleOptions()" ${mode == 'edit' ? 'disabled' : ''}
                                                     required>
                                                    <option value="" disabled ${empty account.phanQuyen ? 'selected' : ''}>
                                                        <s:message code="account.select.role" />
                                                    </option>
                                                    <option value="1" ${account.phanQuyen=='PGV' ? 'selected' : '' }>
                                                        <s:message code="account.role.pgv" />
                                                    </option>
                                                    <option value="2" ${account.phanQuyen=='KHOA' ? 'selected' : '' }>
                                                        <s:message code="account.role.faculty" /></option>
                                                    <option value="3" ${account.phanQuyen=='SINHVIEN' ? 'selected' : '' }>
                                                        <s:message code="account.role.student" />
                                                    </option>
                                                </select>
                                                <c:if test="${mode == 'edit'}">
                                                    <input type="hidden" name="roleId"
                                                        value="${account.phanQuyen == 'PGV' ? '1' : (account.phanQuyen == 'KHOA' ? '2' : '3')}">
                                                </c:if>
                                            </div>

                                            <c:if test="${empty mode || mode == 'add'}">
                                                <div class="col-12" id="div_unassigned_sv" style="display: none;">
                                                    <label class="form-label small fw-bold text-muted">
                                                        <s:message code="account.select.student.noAccount" /> <span
                                                            class="text-danger">*</span>
                                                    </label>
                                                    <select class="form-select rounded-3" id="sel_unassigned_sv"
                                                        onchange="selectUnassignedSV()">
                                                        <option value="" disabled selected>
                                                            <s:message code="account.select.student" />
                                                        </option>
                                                        <c:forEach var="sv" items="${unassignedStudents}">
                                                            <option value="${sv.maSV}">${sv.ho} ${sv.ten} (${sv.maSV})
                                                            </option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                                <div class="col-12" id="div_unassigned_gv" style="display: none;">
                                                    <label class="form-label small fw-bold text-muted">
                                                        <s:message code="account.select.lecturer.noAccount" /> <span
                                                            class="text-danger">*</span>
                                                    </label>
                                                    <select class="form-select rounded-3" id="sel_unassigned_gv"
                                                        onchange="selectUnassignedGV()">
                                                        <option value="" disabled selected>
                                                            <s:message code="account.select.lecturer" />
                                                        </option>
                                                        <c:forEach var="gv" items="${unassignedLecturers}">
                                                            <option value="${gv.maGV}">${gv.ho} ${gv.ten} (${gv.maGV})
                                                            </option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </c:if>

                                            <div class="col-12" id="div_username">
                                                <label class="form-label small fw-bold text-muted">
                                                    <s:message code="account.username.label" /> <span
                                                        class="text-danger">*</span>
                                                </label>
                                                <input type="text" class="form-control rounded-3 bg-light"
                                                    name="username" id="inp_username" value="${account.tenDangNhap}"
                                                    placeholder="<s:message code="account.username.label" />" required
                                                readonly>
                                            </div>
                                            <div class="col-12">
                                                <label class="form-label small fw-bold text-muted">
                                                    <s:message code="account.lbl.password" />
                                                    <c:if test="${empty mode || mode == 'add'}"><span
                                                            class="text-danger">*</span></c:if>
                                                </label>
                                                <div class="input-group">
                                                    <input type="password" class="form-control rounded-start-3"
                                                        name="password" id="inp_password" placeholder="<s:message code="account.enter.password" />" ${mode == 'edit' ? '' : 'required'}>
                                                    <button class="btn btn-outline-secondary rounded-end-3"
                                                        type="button" id="btnTogglePass" onclick="togglePassword()">
                                                        <i class="bi bi-eye"></i>
                                                    </button>
                                                </div>
                                                <c:if test="${mode == 'edit'}">
                                                    <small class="text-muted fst-italic">
                                                        <s:message code="account.leave.blank.no.change" />
                                                    </small>
                                                </c:if>
                                            </div>
                                            <div class="col-12">
                                                <label class="form-label small fw-bold text-muted">EMAIL <span
                                                        class="text-danger">*</span></label>
                                                <input type="email" class="form-control rounded-3" name="email"
                                                    id="inp_email" value="${account.email}"
                                                    placeholder="<s:message code="account.enter.email" />" required>
                                            </div>
                                            <c:if test="${mode == 'edit'}">
                                                <div class="col-12">
                                                    <label class="form-label small fw-bold text-muted">
                                                        Trạng thái tài khoản <span class="text-danger">*</span>
                                                    </label>
                                                    <select class="form-select rounded-3" name="status" id="inp_status" required>
                                                        <option value="CHUA_KICH_HOAT" ${account.trangThai == 'CHUA_KICH_HOAT' ? 'selected' : ''}>Chưa kích hoạt</option>
                                                        <option value="DA_KICH_HOAT" ${account.trangThai == 'DA_KICH_HOAT' ? 'selected' : ''}>Đã kích hoạt</option>
                                                        <option value="KHOA" ${account.trangThai == 'KHOA' ? 'selected' : ''}>Bị khóa</option>
                                                    </select>
                                                </div>
                                            </c:if>
                                        </div>
                                    </div>
                                    <div class="modal-footer border-0 px-4 pb-4">
                                        <a href="${pageContext.request.contextPath}/accounts"
                                            class="btn btn-light rounded-3 fw-bold">
                                            <s:message code="global.btn.cancel" />
                                        </a>
                                        <c:choose>
                                            <c:when test="${mode == 'edit'}">
                                                <button type="submit" class="btn btn-primary rounded-3 fw-bold px-4"><s:message code="global.btn.save.action" />
                                                    (
                                                    <s:message code="global.btn.edit" />)
                                                </button>
                                            </c:when>
                                            <c:otherwise>
                                                <button type="submit" class="btn btn-success rounded-3 fw-bold px-4"><s:message code="global.btn.save.action" />
                                                    (
                                                    <s:message code="global.btn.add" />)
                                                </button>
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

                    function filterLocal() {
                        filterLocalByRole();
                    }

                    function filterLocalByRole() {
                        const roleFilter = document.getElementById('role-filter').value;
                        const textFilter = normalizeVN(document.getElementById('search-input').value);

                        document.querySelectorAll('#user-table-body tr').forEach(row => {
                            const textMatch = normalizeVN(row.innerText).includes(textFilter);
                            const roleMatch = (roleFilter === 'all') || (row.getAttribute('data-role') === roleFilter);
                            row.style.display = (textMatch && roleMatch) ? '' : 'none';
                        });
                    }

                    function togglePassword() {
                        const el = document.getElementById('inp_password');
                        const btn = document.getElementById('btnTogglePass');
                        if (el.type === 'password') {
                            el.type = 'text';
                            btn.innerHTML = '<i class="bi bi-eye-slash"></i>';
                        } else {
                            el.type = 'password';
                            btn.innerHTML = '<i class="bi bi-eye"></i>';
                        }
                    }

                    function toggleRoleOptions() {
                        const roleId = document.getElementById('inp_roleId').value;
                        const divSV = document.getElementById('div_unassigned_sv');
                        const divGV = document.getElementById('div_unassigned_gv');
                        const selSV = document.getElementById('sel_unassigned_sv');
                        const selGV = document.getElementById('sel_unassigned_gv');
                        const inpUsername = document.getElementById('inp_username');

                        if (roleId === '3') { // SINHVIEN
                            if (divSV) divSV.style.display = 'block';
                            if (divGV) divGV.style.display = 'none';
                            if (selSV) { selSV.disabled = false; selSV.required = true; inpUsername.value = selSV.value; }
                            if (selGV) { selGV.disabled = true; selGV.required = false; selGV.value = ''; }
                        } else if (roleId === '1' || roleId === '2') { // PGV / KHOA
                            if (divSV) divSV.style.display = 'none';
                            if (divGV) divGV.style.display = 'block';
                            if (selSV) { selSV.disabled = true; selSV.required = false; selSV.value = ''; }
                            if (selGV) { selGV.disabled = false; selGV.required = true; inpUsername.value = selGV.value; }
                        } else {
                            if (divSV) divSV.style.display = 'none';
                            if (divGV) divGV.style.display = 'none';
                            if (selSV) { selSV.disabled = true; selSV.required = false; }
                            if (selGV) { selGV.disabled = true; selGV.required = false; }
                            inpUsername.value = '';
                        }
                    }

                    function selectUnassignedSV() {
                        const val = document.getElementById('sel_unassigned_sv').value;
                        document.getElementById('inp_username').value = val;
                    }

                    function selectUnassignedGV() {
                        const val = document.getElementById('sel_unassigned_gv').value;
                        document.getElementById('inp_username').value = val;
                    }

                    // Initialize display if adding
                    window.addEventListener('DOMContentLoaded', () => {
                        const inpRole = document.getElementById('inp_roleId');
                        if (inpRole && !inpRole.disabled) {
                            toggleRoleOptions();
                        }
                    });
                </script>
            </body>

            </html>