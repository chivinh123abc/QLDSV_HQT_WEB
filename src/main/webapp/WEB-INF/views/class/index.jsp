<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="utf-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title>Quản lý Lớp - QLDSV_HTC_WEB</title>
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

                .class-title {
                    font-weight: 700;
                    color: #1e293b;
                    margin-bottom: 2px;
                }

                .class-subtitle {
                    font-size: 0.8rem;
                    color: #94a3b8;
                    display: flex;
                    align-items: center;
                    gap: 4px;
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

                /* Premium Toolbar Colors (matching student page) */
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
                                <i class="bi bi-building-fill text-primary fs-3"></i>
                                <h3 class="mb-0 fw-bold text-dark">Quản lý Lớp học</h3>
                            </div>

                            <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
                                <div
                                    class="card-header bg-white border-bottom-0 pt-4 px-4 pb-0 d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="fw-bold text-primary text-uppercase small mb-1">Danh mục Lớp</h6>
                                        <p class="text-muted small mb-0">Quản lý thông tin các lớp học theo từng khoa
                                        </p>
                                    </div>
                                    <button class="btn btn-primary btn-sm rounded-3 px-3 fw-bold shadow-sm"
                                        onclick="resetForm()">
                                        <i class="bi bi-plus-circle-fill me-1"></i> Cập nhật Lớp
                                    </button>
                                </div>

                                <div class="card-body px-4 pb-4">
                                    <!-- SEARCH & FILTER TOOLBAR -->
                                    <div
                                        class="d-flex justify-content-between align-items-center mb-4 pb-3 border-bottom">
                                        <div class="input-group" style="max-width: 300px;">
                                            <span class="input-group-text bg-light border-0"><i
                                                    class="bi bi-search text-muted"></i></span>
                                            <input type="text" id="search-input"
                                                class="form-control bg-light border-0 small"
                                                placeholder="Tìm mã hoặc tên lớp..." onkeyup="filterLocal()">
                                        </div>
                                        <div class="d-flex gap-3 align-items-center">
                                            <label class="fw-bold small text-muted text-uppercase mb-0">Lọc theo
                                                khoa:</label>
                                            <select id="khoa-filter"
                                                class="form-select form-select-sm border-0 bg-light text-primary fw-bold"
                                                style="min-width: 200px;" onchange="filterByKhoa()">
                                                <option value="all">-- Tất cả khoa --</option>
                                                <c:forEach var="k" items="${khoaList}">
                                                    <option value="${k.maKhoa}" ${k.maKhoa==maKhoa ? 'selected' : '' }>
                                                        ${k.tenKhoa}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>

                                    <!-- TABLE -->
                                    <div class="table-responsive rounded-3 border">
                                        <table class="table table-custom align-middle mb-0">
                                            <thead class="table-light">
                                                <tr>
                                                    <th class="px-3">MÃ LỚP</th>
                                                    <th>TÊN LỚP</th>
                                                    <th class="text-center">KHÓA HỌC</th>
                                                    <th class="text-center">KHOA</th>
                                                    <th class="text-center">THAO TÁC</th>
                                                </tr>
                                            </thead>
                                            <tbody id="class-table-body">
                                                <c:forEach var="item" items="${lopList}">
                                                    <tr onclick="selectClass('${item.maLop}', 'edit')">
                                                        <td class="px-3">
                                                            <span class="badge-soft-primary">${item.maLop}</span>
                                                        </td>
                                                        <td>
                                                            <div class="class-title">${item.tenLop}</div>
                                                            <div class="class-subtitle">
                                                                <i class="bi bi-info-circle"></i> Hệ chính quy
                                                            </div>
                                                        </td>
                                                        <td class="text-center">
                                                            <span class="badge-soft-secondary">${item.khoaHoc}</span>
                                                        </td>
                                                        <td class="text-center">
                                                            <span
                                                                class="badge border border-info text-info rounded-pill px-3 py-1 fw-bold small">${item.maKhoa}</span>
                                                        </td>
                                                        <td class="text-center">
                                                            <div class="d-flex gap-2 justify-content-center">
                                                                <button
                                                                    onclick="event.stopPropagation(); selectClass('${item.maLop}', 'edit', true)"
                                                                    class="btn btn-sm btn-outline-primary border-0 rounded-3">
                                                                    <i class="bi bi-pencil-square"></i>
                                                                </button>
                                                                <button
                                                                    onclick="event.stopPropagation(); selectClass('${item.maLop}', 'delete', true)"
                                                                    class="btn btn-sm btn-outline-danger border-0 rounded-3">
                                                                    <i class="bi bi-trash3"></i>
                                                                </button>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                <c:if test="${empty lopList}">
                                                    <tr>
                                                        <td colspan="5" class="text-center py-5 text-muted">
                                                            <i class="bi bi-inbox fs-1 d-block mb-3 opacity-25"></i>
                                                            Chưa có dữ liệu lớp học
                                                        </td>
                                                    </tr>
                                                </c:if>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="mt-3 text-muted small px-1">
                                        <i class="bi bi-info-circle me-1"></i> Hiển thị <strong
                                            id="class-count">${lopList.size()}</strong> lớp học
                                    </div>
                                </div>
                            </div>
                        </div>
                    </main>
                </div>
            </div>

            <!-- CLASS MODAL -->
            <div class="modal fade" id="classModal" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-lg modal-dialog-centered">
                    <div class="modal-content border-0 shadow-lg rounded-4">
                        <div class="modal-header bg-primary text-white border-0 py-3 px-4 rounded-top-4">
                            <h5 class="modal-title fw-bold d-flex align-items-center gap-2">
                                <i class="bi bi-mortarboard-fill"></i> Quản lý Thông tin Lớp
                            </h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                                aria-label="Close"></button>
                        </div>
                        <div class="modal-body p-4">
                            <!-- TOOLBAR -->
                            <div class="d-flex gap-2 mb-4 p-2 bg-light rounded-3 border shadow-sm">
                                <button type="button" id="btn_mode_add"
                                    class="btn btn-toolbar-add fw-bold flex-fill py-2" onclick="handleModeClick('add')">
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
                            </div>

                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label small fw-bold text-muted">MÃ LỚP <span
                                            class="text-danger">*</span></label>
                                    <input type="text" class="form-control rounded-3" id="inp_maLop"
                                        placeholder="VD: D15CQCN01-N" required disabled>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label small fw-bold text-muted">TÊN LỚP <span
                                            class="text-danger">*</span></label>
                                    <input type="text" class="form-control rounded-3" id="inp_tenLop"
                                        placeholder="VD: Công nghệ thông tin 01" required disabled>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label small fw-bold text-muted">KHÓA HỌC <span
                                            class="text-danger">*</span></label>
                                    <div class="position-relative d-flex align-items-center" style="max-width: 240px;">
                                        <button id="btn_year_down"
                                            class="btn btn-sm btn-light rounded-circle border shadow-sm p-0 position-absolute start-0 ms-2 d-flex align-items-center justify-content-center"
                                            style="z-index: 5; width: 28px; height: 28px; transition: all 0.2s;"
                                            type="button" onclick="adjustYear(-1)" disabled>
                                            <i class="bi bi-dash"></i>
                                        </button>
                                        <input type="text"
                                            class="form-control rounded-pill text-center fw-bold bg-light"
                                            style="padding-left: 40px; padding-right: 40px; height: 42px; border-color: #e2e8f0;"
                                            id="inp_khoaHoc" placeholder="VD: 2024-2025" required disabled>
                                        <button id="btn_year_up"
                                            class="btn btn-sm btn-light rounded-circle border shadow-sm p-0 position-absolute end-0 me-2 d-flex align-items-center justify-content-center"
                                            style="z-index: 5; width: 28px; height: 28px; transition: all 0.2s;"
                                            type="button" onclick="adjustYear(1)" disabled>
                                            <i class="bi bi-plus"></i>
                                        </button>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label small fw-bold text-muted">KHOA <span
                                            class="text-danger">*</span></label>
                                    <select class="form-select rounded-3" id="inp_maKhoa" disabled>
                                        <option value="" disabled selected>-- Chọn khoa --</option>
                                        <c:forEach var="k" items="${khoaList}">
                                            <option value="${k.maKhoa}">${k.tenKhoa}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                            <!-- MINI TABLE FOR REFERENCE -->
                            <div class="mt-4 pt-3 border-top">
                                <div class="d-flex align-items-center justify-content-between mb-3">
                                    <h6 class="fw-bold text-primary mb-0 d-flex align-items-center gap-2">
                                        <i class="bi bi-list-ul"></i> Danh sách lớp hiện có
                                    </h6>
                                    <div class="d-flex gap-2 align-items-center">
                                        <select id="mini-khoa-filter" class="form-select form-select-sm border-0 bg-light text-muted fw-bold" style="max-width: 180px;" onchange="filterMiniTableByKhoa()">
                                            <option value="all">-- Tất cả khoa --</option>
                                            <c:forEach var="k" items="${khoaList}">
                                                <option value="${k.maKhoa}" ${k.maKhoa == maKhoa ? 'selected' : ''}>${k.tenKhoa}</option>
                                            </c:forEach>
                                        </select>
                                        <div class="input-group" style="max-width: 180px;">
                                            <span class="input-group-text py-1 border-0 bg-white"><i class="bi bi-search small text-muted"></i></span>
                                            <input type="text" id="mini-class-search" class="form-control form-control-sm border-0 border-bottom" placeholder="Lọc nhanh..." onkeyup="filterMiniTable()">
                                        </div>
                                    </div>
                                </div>
                                <div class="table-responsive rounded-3 border bg-white shadow-sm"
                                    style="max-height: 200px; overflow-y: auto;">
                                    <table class="table table-hover table-sm align-middle mb-0">
                                        <thead class="table-light sticky-top">
                                            <tr>
                                                <th class="border-0 px-3 small fw-bold text-muted">MÃ LỚP</th>
                                                <th class="border-0 small fw-bold text-muted">TÊN LỚP</th>
                                                <th class="border-0 text-center small fw-bold text-muted">KHOA</th>
                                                <th class="border-0 text-center small fw-bold text-muted">THAO TÁC</th>
                                            </tr>
                                        </thead>
                                        <tbody id="mini-table-body">
                                            <c:forEach var="l" items="${lopList}">
                                                <tr onclick="selectClass('${l.maLop}', 'edit')">
                                                    <td class="px-3"><span
                                                            class="badge bg-primary bg-opacity-10 text-primary small">${l.maLop}</span>
                                                    </td>
                                                    <td class="fw-bold text-dark small">${l.tenLop}</td>
                                                    <td class="text-center small"><span
                                                            class="badge border text-secondary rounded-pill px-2">${l.maKhoa}</span>
                                                    </td>
                                                    <td class="text-center">
                                                        <div class="d-flex justify-content-center gap-1">
                                                            <button type="button"
                                                                onclick="event.stopPropagation(); selectClass('${l.maLop}', 'edit', true)"
                                                                class="btn btn-xs btn-outline-primary border-0 p-1">
                                                                <i class="bi bi-pencil-square"></i>
                                                            </button>
                                                            <button type="button"
                                                                onclick="event.stopPropagation(); selectClass('${l.maLop}', 'delete', true)"
                                                                class="btn btn-xs btn-outline-danger border-0 p-1">
                                                                <i class="bi bi-trash3"></i>
                                                            </button>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- NOTIFICATION & CONFIRM MODALS (Reused from student page style) -->
            <div class="modal fade" id="notifyModal" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered modal-sm">
                    <div class="modal-content border-0 shadow-lg rounded-4">
                        <div class="modal-body text-center p-4">
                            <div id="notifyIcon" class="mb-3"></div>
                            <h5 id="notifyTitle" class="fw-bold mb-2"></h5>
                            <p id="notifyMessage" class="text-muted small mb-4"></p>
                            <button type="button" class="btn btn-primary w-100 rounded-3 fw-bold"
                                data-bs-dismiss="modal">ĐÓNG</button>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="confirmModal" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered modal-sm">
                    <div class="modal-content border-0 shadow-lg rounded-4">
                        <div class="modal-body text-center p-4">
                            <div class="mb-3"><i class="bi bi-exclamation-triangle-fill text-warning"
                                    style="font-size: 3.5rem;"></i></div>
                            <h5 id="confirmTitle" class="fw-bold mb-2">Xác nhận</h5>
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
            <script>
                let currentMode = 'none';
                let originalMaLop = '';
                const contextPath = '${pageContext.request.contextPath}';
                const inputs = ['inp_maLop', 'inp_tenLop', 'inp_khoaHoc', 'inp_maKhoa'];

                function showNotify(title, message, type = 'success') {
                    document.getElementById('notifyTitle').innerText = title;
                    document.getElementById('notifyMessage').innerText = message;
                    const icon = document.getElementById('notifyIcon');
                    icon.innerHTML = type === 'success' ? '<i class="bi bi-check-circle-fill text-success" style="font-size: 3.5rem;"></i>' :
                        (type === 'error' ? '<i class="bi bi-x-circle-fill text-danger" style="font-size: 3.5rem;"></i>' :
                            '<i class="bi bi-info-circle-fill text-primary" style="font-size: 3.5rem;"></i>');
                    const notifyModalElement = document.getElementById('notifyModal');
                    const notifyModal = bootstrap.Modal.getOrCreateInstance(notifyModalElement);
                    notifyModal.show();
                }

                function showConfirm(message, onConfirm) {
                    document.getElementById('confirmMessage').innerText = message;
                    const btn = document.getElementById('confirmOkBtn');
                    const confirmModalElement = document.getElementById('confirmModal');
                    const confirmModal = bootstrap.Modal.getOrCreateInstance(confirmModalElement);
                    btn.onclick = () => { confirmModal.hide(); onConfirm(); };
                    confirmModal.show();
                }

                async function filterByKhoa() {
                    const maKhoa = document.getElementById('khoa-filter').value;
                    const miniFilter = document.getElementById('mini-khoa-filter');
                    if (miniFilter) miniFilter.value = maKhoa;
                    try {
                        const response = await fetch(contextPath + '/class/api/list?maKhoa=' + maKhoa);
                        const data = await response.json();
                        renderTable(data);
                        renderMiniTable(data);
                    } catch (e) { console.error(e); }
                }

                function filterLocal() {
                    const val = document.getElementById('search-input').value.toLowerCase();
                    document.querySelectorAll('#class-table-body tr').forEach(row => {
                        const text = row.innerText.toLowerCase();
                        row.style.display = text.includes(val) ? '' : 'none';
                    });
                }

                function filterMiniTable() {
                    const val = document.getElementById('mini-class-search').value.toLowerCase();
                    const rows = document.querySelectorAll('#mini-table-body tr');
                    rows.forEach(row => {
                        const text = row.innerText.toLowerCase();
                        row.style.display = text.includes(val) ? '' : 'none';
                    });
                }

                async function filterMiniTableByKhoa() {
                    const maKhoa = document.getElementById('mini-khoa-filter').value;
                    const outerFilter = document.getElementById('khoa-filter');
                    if (outerFilter) outerFilter.value = maKhoa;
                    try {
                        const response = await fetch(contextPath + '/class/api/list?maKhoa=' + maKhoa);
                        const data = await response.json();
                        renderTable(data);
                        renderMiniTable(data);
                    } catch (e) { console.error(e); }
                }

                function renderTable(data) {
                    const container = document.getElementById('class-table-body');
                    document.getElementById('class-count').innerText = data.length;
                    if (data.length === 0) {
                        container.innerHTML = '<tr><td colspan="5" class="text-center py-5 text-muted">Không tìm thấy lớp nào</td></tr>';
                        return;
                    }
                    container.innerHTML = data.map(item => `
                <tr onclick="selectClass('\${item.maLop}', 'edit')">
                    <td class="px-3"><span class="badge-soft-primary">\${item.maLop}</span></td>
                    <td>
                        <div class="class-title">\${item.tenLop}</div>
                        <div class="class-subtitle"><i class="bi bi-info-circle"></i> Hệ chính quy</div>
                    </td>
                    <td class="text-center"><span class="badge-soft-secondary">\${item.khoaHoc}</span></td>
                    <td class="text-center"><span class="badge border border-info text-info rounded-pill px-3 py-1 fw-bold small">\${item.maKhoa}</span></td>
                    <td class="text-center">
                        <div class="d-flex gap-2 justify-content-center">
                            <button onclick="event.stopPropagation(); selectClass('\${item.maLop}', 'edit', true)" class="btn btn-sm btn-outline-primary border-0 rounded-3"><i class="bi bi-pencil-square"></i></button>
                            <button onclick="event.stopPropagation(); selectClass('\${item.maLop}', 'delete', true)" class="btn btn-sm btn-outline-danger border-0 rounded-3"><i class="bi bi-trash3"></i></button>
                        </div>
                    </td>
                </tr>
            `).join('');
                }

                function renderMiniTable(data) {
                    const container = document.getElementById('mini-table-body');
                    container.innerHTML = data.map(item => `
                <tr onclick="selectClass('\${item.maLop}', 'edit')">
                    <td class="px-3"><span class="badge bg-primary bg-opacity-10 text-primary small">\${item.maLop}</span></td>
                    <td class="fw-bold text-dark small">\${item.tenLop}</td>
                    <td class="text-center small"><span class="badge border text-secondary rounded-pill px-2">\${item.maKhoa}</span></td>
                    <td class="text-center">
                        <div class="d-flex justify-content-center gap-1">
                            <button type="button" onclick="event.stopPropagation(); selectClass('\${item.maLop}', 'edit', true)" class="btn btn-xs btn-outline-primary border-0 p-1">
                                <i class="bi bi-pencil-square"></i>
                            </button>
                            <button type="button" onclick="event.stopPropagation(); selectClass('\${item.maLop}', 'delete', true)" class="btn btn-xs btn-outline-danger border-0 p-1">
                                <i class="bi bi-trash3"></i>
                            </button>
                        </div>
                    </td>
                </tr>
            `).join('');
                }

                async function selectClass(maLop, mode, forceMode = false) {
                    try {
                        const res = await fetch(contextPath + '/class/api/get?maLop=' + maLop);
                        const data = await res.json();
                        fillForm(data);
                        const classModalElement = document.getElementById('classModal');
                        const classModal = bootstrap.Modal.getOrCreateInstance(classModalElement);
                        classModal.show();

                        if (forceMode || currentMode === 'none') {
                            setMode(mode);
                        } else {
                            setMode(currentMode); // Preserve current mode
                        }
                    } catch (e) { console.error(e); }
                }

                function fillForm(data) {
                    document.getElementById('inp_maLop').value = data.maLop || '';
                    originalMaLop = data.maLop || '';
                    document.getElementById('inp_tenLop').value = data.tenLop || '';
                    document.getElementById('inp_khoaHoc').value = data.khoaHoc || '';
                    document.getElementById('inp_maKhoa').value = data.maKhoa || '';
                }

                function clearForm() {
                    inputs.forEach(id => {
                        const el = document.getElementById(id);
                        if (id === 'inp_khoaHoc') {
                            const year = new Date().getFullYear();
                            el.value = year + '-' + (year + 1);
                        } else {
                            el.value = '';
                        }
                    });
                }

                function setMode(mode) {
                    currentMode = mode;
                    const btns = { add: 'btn_mode_add', edit: 'btn_mode_edit', delete: 'btn_mode_delete', cancel: 'btn_mode_cancel' };
                    Object.values(btns).forEach(id => {
                        const btn = document.getElementById(id);
                        btn.disabled = false;
                        btn.classList.remove('active', 'btn-toolbar-disabled');
                    });
                    document.getElementById(btns.cancel).disabled = mode === 'none';
                    inputs.forEach(id => {
                        const el = document.getElementById(id);
                        el.disabled = mode === 'none' || mode === 'delete';
                        if (mode === 'edit' && id === 'inp_maLop') el.readOnly = true;
                        else el.readOnly = false;
                    });

                    const yearBtnsDisabled = mode === 'none' || mode === 'delete';
                    document.getElementById('btn_year_down').disabled = yearBtnsDisabled;
                    document.getElementById('btn_year_up').disabled = yearBtnsDisabled;

                    if (mode === 'none') clearForm();
                    else if (mode === 'add') {
                        document.getElementById(btns.edit).classList.add('btn-toolbar-disabled');
                        document.getElementById(btns.delete).classList.add('btn-toolbar-disabled');
                        document.getElementById(btns.add).classList.add('active');
                    } else if (mode === 'edit') {
                        document.getElementById(btns.add).classList.add('btn-toolbar-disabled');
                        document.getElementById(btns.delete).classList.add('btn-toolbar-disabled');
                        document.getElementById(btns.edit).classList.add('active');
                    } else if (mode === 'delete') {
                        document.getElementById(btns.add).classList.add('btn-toolbar-disabled');
                        document.getElementById(btns.edit).classList.add('btn-toolbar-disabled');
                        document.getElementById(btns.delete).classList.add('active');
                    }
                }

                function handleModeClick(mode) {
                    if (mode === 'none') { setMode('none'); return; }
                    if (currentMode === mode) {
                        if (mode === 'delete') performDelete();
                        else performSave();
                    } else if (currentMode === 'none') {
                        setMode(mode);
                        if (mode === 'add') clearForm();
                    }
                }

                async function performSave() {
                    const data = {
                        maLop: document.getElementById('inp_maLop').value,
                        tenLop: document.getElementById('inp_tenLop').value,
                        khoaHoc: document.getElementById('inp_khoaHoc').value,
                        maKhoa: document.getElementById('inp_maKhoa').value
                    };
                    if (!data.maLop || !data.tenLop) { showNotify('Cảnh báo', 'Vui lòng nhập đầy đủ thông tin!', 'info'); return; }
                    try {
                        let url = contextPath + '/class/api/save?mode=' + currentMode;
                        if (currentMode === 'edit') url += '&oldMaLop=' + originalMaLop;

                        const res = await fetch(url, {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify(data)
                        });
                        const result = await res.json();
                        if (result.status === 'success') {
                            showNotify('Thành công', 'Thông tin lớp đã được lưu.');
                            filterByKhoa();
                            setMode('none');
                        } else showNotify('Lỗi', result.message, 'error');
                    } catch (e) { showNotify('Lỗi', e.message, 'error'); }
                }

                async function performDelete() {
                    const maLop = document.getElementById('inp_maLop').value;
                    showConfirm('Bạn có chắc chắn muốn xóa lớp này?', async () => {
                        try {
                            const res = await fetch(contextPath + '/class/api/delete?maLop=' + maLop, { method: 'POST' });
                            const result = await res.json();
                            if (result.status === 'success') {
                                showNotify('Thành công', 'Lớp đã được xóa.');
                                filterByKhoa();
                                setMode('none');
                            } else showNotify('Lỗi', result.message, 'error');
                        } catch (e) { showNotify('Lỗi', e.message, 'error'); }
                    });
                }

                function resetForm() {
                    clearForm();
                    setMode('none');
                    const classModalElement = document.getElementById('classModal');
                    const classModal = bootstrap.Modal.getOrCreateInstance(classModalElement);
                    classModal.show();
                }
                function adjustYear(delta) {
                    const el = document.getElementById('inp_khoaHoc');
                    if (el.disabled) return;
                    const current = el.value || '';
                    const startYear = parseInt(current.split('-')[0]) || new Date().getFullYear();
                    const nextStart = startYear + delta;
                    el.value = nextStart + '-' + (nextStart + 1);
                }
            </script>
        </body>

        </html>