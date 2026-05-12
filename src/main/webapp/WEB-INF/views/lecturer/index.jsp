<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="utf-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title>Quản lý Giảng viên - QLDSV_HTC_WEB</title>
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

                .lecturer-name {
                    font-weight: 700;
                    color: #1e293b;
                    margin-bottom: 2px;
                }

                .lecturer-info {
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
                <jsp:include page="/WEB-INF/views/shared/sidebar.jsp" />

                <div class="app-main">
                    <jsp:include page="/WEB-INF/views/shared/header.jsp" />

                    <main id="main-content" class="app-content p-4 bg-light">
                        <div class="container-fluid max-w-7xl mx-auto">
                            <div class="d-flex align-items-center gap-2 mb-4">
                                <i class="bi bi-person-badge-fill text-primary fs-3"></i>
                                <h3 class="mb-0 fw-bold text-dark">Quản lý Giảng viên</h3>
                            </div>

                            <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
                                <div
                                    class="card-header bg-white border-bottom-0 pt-4 px-4 pb-0 d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="fw-bold text-primary text-uppercase small mb-1">Danh sách Giảng viên
                                        </h6>
                                        <p class="text-muted small mb-0">Quản lý thông tin giảng viên theo từng khoa</p>
                                    </div>
                                    <c:if test="${sessionScope.role == 'PGV'}">
                                        <button class="btn btn-primary btn-sm rounded-3 px-3 fw-bold shadow-sm"
                                            onclick="resetForm()">
                                            <i class="bi bi-plus-circle-fill me-1"></i> Cập nhật Giảng viên
                                        </button>
                                    </c:if>
                                </div>

                                <div class="card-body px-4 pb-4">
                                    <div
                                        class="d-flex justify-content-between align-items-center mb-4 pb-3 border-bottom">
                                        <div class="input-group" style="max-width: 300px;">
                                            <span class="input-group-text bg-light border-0"><i
                                                    class="bi bi-search text-muted"></i></span>
                                            <input type="text" id="search-input"
                                                class="form-control bg-light border-0 small"
                                                placeholder="Tìm tên giảng viên..." onkeyup="filterLocal()">
                                        </div>
                                        <div class="d-flex gap-3 align-items-center">
                                            <label class="fw-bold small text-muted text-uppercase mb-0">Lọc theo
                                                khoa:</label>
                                            <c:choose>
                                                <c:when test="${sessionScope.role == 'PGV'}">
                                                    <select id="khoa-filter"
                                                        class="form-select form-select-sm border-0 bg-light text-primary fw-bold"
                                                        style="min-width: 200px;" onchange="filterByKhoa()">
                                                        <option value="all">-- Tất cả khoa --</option>
                                                        <c:forEach var="k" items="${khoaList}">
                                                            <option value="${k.maKhoa}" ${k.maKhoa==maKhoa ? 'selected' : '' }>
                                                                ${k.tenKhoa}</option>
                                                        </c:forEach>
                                                    </select>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="d-flex align-items-center gap-2 px-3 py-1 bg-light rounded-pill border">
                                                        <i class="bi bi-building text-primary small"></i>
                                                        <span class="fw-bold text-dark small">${khoaList[0].tenKhoa}</span>
                                                        <input type="hidden" id="khoa-filter" value="${sessionScope.maKhoa}">
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <div class="table-responsive rounded-3 border">
                                        <table class="table table-custom align-middle mb-0">
                                            <thead class="table-light">
                                                <tr>
                                                    <th class="px-3">MÃ GV</th>
                                                    <th>HỌ TÊN</th>
                                                    <th class="text-center">HỌC VỊ/HÀM</th>
                                                    <th class="text-center">CHUYÊN MÔN</th>
                                                    <th class="text-center">KHOA</th>
                                                    <c:if test="${sessionScope.role == 'PGV'}">
                                                        <th class="text-center">THAO TÁC</th>
                                                    </c:if>
                                                </tr>
                                            </thead>
                                            <tbody id="gv-table-body">
                                                <c:forEach var="item" items="${gvList}">
                                                    <tr <c:if test="${sessionScope.role == 'PGV'}">onclick="selectGV('${item.maGV}', 'edit')"</c:if>>
                                                        <td class="px-3"><span
                                                                class="badge-soft-primary">${item.maGV}</span></td>
                                                        <td>
                                                            <div class="lecturer-name">${item.ho} ${item.ten}</div>
                                                        </td>
                                                        <td class="text-center">
                                                            <span class="text-muted small">${item.hocVi} /
                                                                ${item.hocHam}</span>
                                                        </td>
                                                        <td class="text-center">
                                                            <span
                                                                class="text-dark small fw-medium">${item.chuyenMon}</span>
                                                        </td>
                                                        <td class="text-center">
                                                            <span
                                                                class="badge border border-info text-info rounded-pill px-3 py-1 fw-bold small">${item.maKhoa}</span>
                                                        </td>
                                                        <c:if test="${sessionScope.role == 'PGV'}">
                                                            <td class="text-center">
                                                                <div class="d-flex gap-2 justify-content-center">
                                                                    <button
                                                                        onclick="event.stopPropagation(); selectGV('${item.maGV}', 'edit', true)"
                                                                        class="btn btn-sm btn-outline-primary border-0 rounded-3"><i
                                                                            class="bi bi-pencil-square"></i></button>
                                                                    <button
                                                                        onclick="event.stopPropagation(); selectGV('${item.maGV}', 'delete', true)"
                                                                        class="btn btn-sm btn-outline-danger border-0 rounded-3 ${!item.canDelete ? 'disabled opacity-25' : ''}"
                                                                        ${!item.canDelete
                                                                        ? 'disabled title="Giảng viên đang có dữ liệu, không thể xóa"'
                                                                        : '' }><i class="bi bi-trash3"></i></button>
                                                                </div>
                                                            </td>
                                                        </c:if>
                                                    </tr>
                                                </c:forEach>
                                                <c:if test="${empty gvList}">
                                                    <tr>
                                                        <td colspan="6" class="text-center py-5 text-muted">
                                                            <i class="bi bi-inbox fs-1 d-block mb-3 opacity-25"></i>
                                                            Chưa có dữ liệu giảng viên
                                                        </td>
                                                    </tr>
                                                </c:if>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="mt-3 text-muted small px-1">
                                        <i class="bi bi-info-circle me-1"></i> Hiển thị <strong
                                            id="gv-count">${gvList.size()}</strong> giảng viên
                                    </div>
                                </div>
                            </div>
                        </div>
                    </main>
                </div>
            </div>

            <!-- GV MODAL -->
            <div class="modal fade" id="gvModal" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-lg modal-dialog-centered">
                    <div class="modal-content border-0 shadow-lg rounded-4">
                        <div class="modal-header bg-primary text-white border-0 py-3 px-4 rounded-top-4">
                            <h5 class="modal-title fw-bold d-flex align-items-center gap-2">
                                <i class="bi bi-person-lines-fill"></i> Thông tin Giảng viên
                            </h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                                aria-label="Close"></button>
                        </div>
                        <div class="modal-body p-4">
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
                                    <label class="form-label small fw-bold text-muted">MÃ GV <span
                                            class="text-danger">*</span></label>
                                    <input type="text" class="form-control rounded-3" id="inp_maGV"
                                        placeholder="VD: GV01" required disabled>
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
                                <div class="col-md-6">
                                    <label class="form-label small fw-bold text-muted">HỌ LÓT <span
                                            class="text-danger">*</span></label>
                                    <input type="text" class="form-control rounded-3" id="inp_ho"
                                        placeholder="VD: Nguyễn Văn" required disabled>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label small fw-bold text-muted">TÊN <span
                                            class="text-danger">*</span></label>
                                    <input type="text" class="form-control rounded-3" id="inp_ten" placeholder="VD: A"
                                        required disabled>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-bold text-muted">HỌC VỊ</label>
                                    <input type="text" class="form-control rounded-3" id="inp_hocVi"
                                        placeholder="VD: Thạc sĩ" disabled>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-bold text-muted">HỌC HÀM</label>
                                    <input type="text" class="form-control rounded-3" id="inp_hocHam"
                                        placeholder="VD: PGS" disabled>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-bold text-muted">CHUYÊN MÔN</label>
                                    <input type="text" class="form-control rounded-3" id="inp_chuyenMon"
                                        placeholder="VD: Công nghệ phần mềm" disabled>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- NOTIFICATION MODALS -->
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

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
            <script>
                let currentMode = 'none';
                let currentCanDelete = true;
                const contextPath = '${pageContext.request.contextPath}';
                const inputs = ['inp_maGV', 'inp_maKhoa', 'inp_ho', 'inp_ten', 'inp_hocVi', 'inp_hocHam', 'inp_chuyenMon'];

                function showNotify(title, message, type = 'success') {
                    document.getElementById('notifyTitle').innerText = title;
                    document.getElementById('notifyMessage').innerText = message;
                    const icon = document.getElementById('notifyIcon');
                    icon.innerHTML = type === 'success' ? '<i class="bi bi-check-circle-fill text-success" style="font-size: 3.5rem;"></i>' :
                        (type === 'error' ? '<i class="bi bi-x-circle-fill text-danger" style="font-size: 3.5rem;"></i>' :
                            '<i class="bi bi-info-circle-fill text-primary" style="font-size: 3.5rem;"></i>');
                    bootstrap.Modal.getOrCreateInstance(document.getElementById('notifyModal')).show();
                }

                function showConfirm(message, onConfirm) {
                    document.getElementById('confirmMessage').innerText = message;
                    const btn = document.getElementById('confirmOkBtn');
                    const confirmModal = bootstrap.Modal.getOrCreateInstance(document.getElementById('confirmModal'));
                    btn.onclick = () => { confirmModal.hide(); onConfirm(); };
                    confirmModal.show();
                }

                async function filterByKhoa() {
                    const maKhoa = document.getElementById('khoa-filter').value;
                    try {
                        const response = await fetch(contextPath + '/lecturer/api/list?maKhoa=' + maKhoa);
                        const data = await response.json();
                        renderTable(data);
                    } catch (e) { console.error(e); }
                }

                function normalizeVN(str) {
                    return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/\u0111/g, 'd').replace(/\u0110/g, 'D').toLowerCase();
                }

                function filterLocal() {
                    const val = normalizeVN(document.getElementById('search-input').value);
                    document.querySelectorAll('#gv-table-body tr').forEach(row => {
                        row.style.display = normalizeVN(row.innerText).includes(val) ? '' : 'none';
                    });
                }

                function renderTable(data) {
                    const container = document.getElementById('gv-table-body');
                    document.getElementById('gv-count').innerText = data.length;
                    if (data.length === 0) {
                        container.innerHTML = '<tr><td colspan="6" class="text-center py-5 text-muted">Không tìm thấy giảng viên nào</td></tr>';
                        return;
                    }
                    container.innerHTML = data.map(item => `
                <tr onclick="selectGV('\${item.maGV}', 'edit')">
                    <td class="px-3"><span class="badge-soft-primary">\${item.maGV}</span></td>
                    <td><div class="lecturer-name">\${item.ho} \${item.ten}</div></td>
                    <td class="text-center"><span class="text-muted small">\${item.hocVi || ''} / \${item.hocHam || ''}</span></td>
                    <td class="text-center"><span class="text-dark small fw-medium">\${item.chuyenMon || ''}</span></td>
                    <td class="text-center"><span class="badge border border-info text-info rounded-pill px-3 py-1 fw-bold small">\${item.maKhoa}</span></td>
                    <td class="text-center">
                        <div class="d-flex gap-2 justify-content-center">
                            <button onclick="event.stopPropagation(); selectGV('\${item.maGV}', 'edit', true)" class="btn btn-sm btn-outline-primary border-0 rounded-3"><i class="bi bi-pencil-square"></i></button>
                            <button onclick="event.stopPropagation(); selectGV('\${item.maGV}', 'delete', true)" class="btn btn-sm btn-outline-danger border-0 rounded-3 \${!item.canDelete ? 'disabled opacity-25' : ''}" \${!item.canDelete ? 'disabled title="Giảng viên đang có dữ liệu, không thể xóa"' : ''}><i class="bi bi-trash3"></i></button>
                        </div>
                    </td>
                </tr>
            `).join('');
                }

                async function selectGV(maGV, mode, forceMode = false) {
                    try {
                        const res = await fetch(contextPath + '/lecturer/api/get?maGV=' + maGV);
                        const data = await res.json();
                        currentCanDelete = data.canDelete;
                        fillForm(data);
                        bootstrap.Modal.getOrCreateInstance(document.getElementById('gvModal')).show();

                        if (forceMode || currentMode === 'none') setMode(mode);
                        else setMode(currentMode);
                    } catch (e) { console.error(e); }
                }

                function fillForm(data) {
                    document.getElementById('inp_maGV').value = data.maGV || '';
                    document.getElementById('inp_maKhoa').value = data.maKhoa || '';
                    document.getElementById('inp_ho').value = data.ho || '';
                    document.getElementById('inp_ten').value = data.ten || '';
                    document.getElementById('inp_hocVi').value = data.hocVi || '';
                    document.getElementById('inp_hocHam').value = data.hocHam || '';
                    document.getElementById('inp_chuyenMon').value = data.chuyenMon || '';
                }

                function clearForm() {
                    inputs.forEach(id => document.getElementById(id).value = '');
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
                        if (mode === 'edit' && id === 'inp_maGV') el.readOnly = true;
                        else el.readOnly = false;
                    });

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

                    if (currentCanDelete === false) {
                        const delBtn = document.getElementById(btns.delete);
                        delBtn.disabled = true;
                        delBtn.classList.add('btn-toolbar-disabled');
                        delBtn.title = "Giảng viên đang có dữ liệu (lớp tín chỉ hoặc tài khoản), không thể xóa";
                    } else {
                        document.getElementById(btns.delete).title = "";
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
                        maGV: document.getElementById('inp_maGV').value,
                        maKhoa: document.getElementById('inp_maKhoa').value,
                        ho: document.getElementById('inp_ho').value,
                        ten: document.getElementById('inp_ten').value,
                        hocVi: document.getElementById('inp_hocVi').value,
                        hocHam: document.getElementById('inp_hocHam').value,
                        chuyenMon: document.getElementById('inp_chuyenMon').value
                    };
                    if (!data.maGV || !data.maKhoa || !data.ho || !data.ten) {
                        showNotify('Cảnh báo', 'Vui lòng nhập đầy đủ thông tin bắt buộc (*)!', 'info');
                        return;
                    }
                    try {
                        const res = await fetch(contextPath + '/lecturer/api/save?mode=' + currentMode, {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify(data)
                        });
                        const result = await res.json();
                        if (result.status === 'success') {
                            showNotify('Thành công', 'Thông tin giảng viên đã được lưu.');
                            filterByKhoa();
                            bootstrap.Modal.getInstance(document.getElementById('gvModal')).hide();
                        } else showNotify('Lỗi', result.message, 'error');
                    } catch (e) { showNotify('Lỗi', e.message, 'error'); }
                }

                async function performDelete() {
                    const maGV = document.getElementById('inp_maGV').value;
                    showConfirm('Bạn có chắc chắn muốn xóa giảng viên này?', async () => {
                        try {
                            const res = await fetch(contextPath + '/lecturer/api/delete?maGV=' + maGV, { method: 'POST' });
                            const result = await res.json();
                            if (result.status === 'success') {
                                showNotify('Thành công', 'Giảng viên đã được xóa.');
                                filterByKhoa();
                                bootstrap.Modal.getInstance(document.getElementById('gvModal')).hide();
                            } else showNotify('Lỗi', result.message, 'error');
                        } catch (e) { showNotify('Lỗi', e.message, 'error'); }
                    });
                }

                function resetForm() {
                    currentCanDelete = true;
                    clearForm();
                    setMode('none');
                    bootstrap.Modal.getOrCreateInstance(document.getElementById('gvModal')).show();
                }
            </script>
        </body>

        </html>