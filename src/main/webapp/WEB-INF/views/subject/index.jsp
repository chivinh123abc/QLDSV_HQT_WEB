<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quản lý Môn học - QLDSV_HTC_WEB</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <style>
        .badge-soft-primary { background-color: #e0f2fe; color: #0369a1; border-radius: 20px; font-weight: 600; padding: 6px 12px; }
        .badge-soft-secondary { background-color: #f1f5f9; color: #475569; border-radius: 20px; padding: 6px 12px; font-weight: 500; }
        .table-custom th { font-size: 0.85rem; color: #64748b; font-weight: 700; text-transform: uppercase; border-bottom: 2px solid #e2e8f0; }
        .table-custom td { vertical-align: middle; padding: 1rem 0.75rem; border-bottom: 1px solid #f1f5f9; }
        .table-custom tbody tr { cursor: pointer; transition: all 0.2s; }
        .table-custom tbody tr:hover { background-color: #f0f7ff !important; }
        .subject-title { font-weight: 700; color: #1e293b; margin-bottom: 2px; }
        .subject-subtitle { font-size: 0.8rem; color: #94a3b8; display: flex; align-items: center; gap: 4px; }
        
        .modal-header-custom { background-color: #4361ee; color: white; border-bottom: 0; }
        .modal-header-custom .btn-close { filter: invert(1) grayscale(100%) brightness(200%); }
        .toolbar-btn { font-weight: 600; padding: 6px 12px; display: inline-flex; align-items: center; gap: 6px; }
        
        /* Premium Toolbar Colors */
        .btn-toolbar-add { color: #10b981; border-color: #10b981; }
        .btn-toolbar-add:hover, .btn-toolbar-add.active { background-color: #10b981; color: white; box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3); }
        .btn-toolbar-edit { color: #3b82f6; border-color: #3b82f6; }
        .btn-toolbar-edit:hover, .btn-toolbar-edit.active { background-color: #3b82f6; color: white; box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3); }
        .btn-toolbar-delete { color: #ef4444; border-color: #ef4444; }
        .btn-toolbar-delete:hover, .btn-toolbar-delete.active { background-color: #ef4444; color: white; box-shadow: 0 4px 12px rgba(239, 68, 68, 0.3); }
        .btn-toolbar-cancel { color: #64748b; border-color: #e2e8f0; }
        .btn-toolbar-cancel:hover { background-color: #64748b; color: white; }
        .btn-toolbar-disabled { opacity: 0.4; filter: grayscale(100%); pointer-events: none; }
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
                        <i class="bi bi-journal-bookmark-fill text-primary fs-3"></i>
                        <h3 class="mb-0 fw-bold text-dark">Quản lý Môn học</h3>
                    </div>

                    <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
                        <div class="card-header bg-white border-bottom-0 pt-4 px-4 pb-0 d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="fw-bold text-primary text-uppercase small mb-1">Danh mục Môn học</h6>
                                <p class="text-muted small mb-0">Quản lý chương trình và định mức môn học</p>
                            </div>
                            <button class="btn btn-primary btn-sm rounded-3 px-3 fw-bold shadow-sm" onclick="resetForm()">
                                <i class="bi bi-plus-circle-fill me-1"></i> Cập nhật Môn học
                            </button>
                        </div>

                        <div class="card-body px-4 pb-4">
                            <!-- SEARCH TOOLBAR -->
                            <div class="d-flex justify-content-between align-items-center mb-4 pb-3 border-bottom">
                                <div class="input-group" style="max-width: 300px;">
                                    <span class="input-group-text bg-light border-0"><i class="bi bi-search text-muted"></i></span>
                                    <input type="text" id="search-input" class="form-control bg-light border-0 small" placeholder="Tìm mã hoặc tên môn..." onkeyup="filterLocal()">
                                </div>
                                <div class="text-muted small">
                                    <i class="bi bi-info-circle me-1"></i> Tổng cộng <strong id="subject-count">${monHocList.size()}</strong> môn học
                                </div>
                            </div>

                            <!-- TABLE -->
                            <div class="table-responsive rounded-3 border">
                                <table class="table table-custom align-middle mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th class="px-3">MÃ MH</th>
                                            <th>TÊN MÔN HỌC</th>
                                            <th class="text-center">TIẾT LT</th>
                                            <th class="text-center">TIẾT TH</th>
                                            <th class="text-center">THAO TÁC</th>
                                        </tr>
                                    </thead>
                                    <tbody id="subject-table-body">
                                        <c:forEach var="item" items="${monHocList}">
                                            <tr onclick="selectSubject('${item.maMH}', 'edit', false)">
                                                <td class="px-3">
                                                    <span class="badge-soft-primary">${item.maMH}</span>
                                                </td>
                                                <td>
                                                    <div class="subject-title">${item.tenMH}</div>
                                                    <div class="subject-subtitle">
                                                        <i class="bi bi-info-circle"></i> Đã chuẩn hóa
                                                    </div>
                                                </td>
                                                <td class="text-center">
                                                    <span class="badge-soft-secondary">${item.soTietLT} tiết</span>
                                                </td>
                                                <td class="text-center">
                                                    <span class="badge-soft-secondary">${item.soTietTH} tiết</span>
                                                </td>
                                                <td class="text-center">
                                                    <div class="d-flex gap-2 justify-content-center">
                                                        <button onclick="event.stopPropagation(); selectSubject('${item.maMH}', 'edit', true)" class="btn btn-sm btn-outline-primary border-0 rounded-3">
                                                            <i class="bi bi-pencil-square"></i>
                                                        </button>
                                                        <button onclick="event.stopPropagation(); selectSubject('${item.maMH}', 'delete', true)" class="btn btn-sm btn-outline-danger border-0 rounded-3">
                                                            <i class="bi bi-trash3"></i>
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty monHocList}">
                                            <tr>
                                                <td colspan="5" class="text-center py-5 text-muted">
                                                    <i class="bi bi-inbox fs-1 d-block mb-3 opacity-25"></i>
                                                    Chưa có dữ liệu môn học
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

    <!-- SUBJECT MODAL -->
    <div class="modal fade" id="subjectModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg rounded-4">
                <div class="modal-header bg-primary text-white border-0 py-3 px-4 rounded-top-4">
                    <h5 class="modal-title fw-bold d-flex align-items-center gap-2">
                        <i class="bi bi-journal-plus"></i> Quản lý Thông tin Môn học
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body p-4">
                    <!-- TOOLBAR -->
                    <div class="d-flex gap-2 mb-4 p-2 bg-light rounded-3 border shadow-sm">
                        <button type="button" id="btn_mode_add" class="btn btn-toolbar-add fw-bold flex-fill py-2" onclick="handleModeClick('add')">
                            <i class="bi bi-plus-circle"></i> <span class="btn-text">THÊM</span>
                        </button>
                        <button type="button" id="btn_mode_edit" class="btn btn-toolbar-edit fw-bold flex-fill py-2" onclick="handleModeClick('edit')">
                            <i class="bi bi-pencil-square"></i> <span class="btn-text">SỬA</span>
                        </button>
                        <button type="button" id="btn_mode_delete" class="btn btn-toolbar-delete fw-bold flex-fill py-2" onclick="handleModeClick('delete')">
                            <i class="bi bi-trash3"></i> <span class="btn-text">XÓA</span>
                        </button>
                        <button type="button" id="btn_mode_cancel" class="btn btn-toolbar-cancel fw-bold flex-fill py-2" onclick="handleModeClick('none')" disabled>
                            <i class="bi bi-x-circle"></i> HỦY
                        </button>
                    </div>

                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label small fw-bold text-muted">MÃ MÔN HỌC <span class="text-danger">*</span></label>
                            <input type="text" class="form-control rounded-3" id="inp_maMH" placeholder="VD: CTDL" required disabled>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-bold text-muted">TÊN MÔN HỌC <span class="text-danger">*</span></label>
                            <input type="text" class="form-control rounded-3" id="inp_tenMH" placeholder="VD: Cấu trúc dữ liệu" required disabled>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-bold text-muted">SỐ TIẾT LÝ THUYẾT <span class="text-danger">*</span></label>
                            <input type="number" class="form-control rounded-3" id="inp_soTietLT" placeholder="VD: 30" required disabled>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-bold text-muted">SỐ TIẾT THỰC HÀNH <span class="text-danger">*</span></label>
                            <input type="number" class="form-control rounded-3" id="inp_soTietTH" placeholder="VD: 15" required disabled>
                        </div>
                    </div>

                    <!-- MINI TABLE FOR REFERENCE -->
                    <div class="mt-4 pt-3 border-top">
                        <h6 class="fw-bold text-primary mb-3 d-flex align-items-center gap-2">
                            <i class="bi bi-list-ul"></i> Danh mục môn học hiện có
                        </h6>
                        <div class="table-responsive rounded-3 border bg-white shadow-sm" style="max-height: 200px; overflow-y: auto;">
                            <table class="table table-hover table-sm align-middle mb-0">
                                <thead class="table-light sticky-top">
                                    <tr>
                                        <th class="border-0 px-3 small fw-bold text-muted">MÃ MH</th>
                                        <th class="border-0 small fw-bold text-muted">TÊN MÔN HỌC</th>
                                        <th class="border-0 text-center small fw-bold text-muted">LT/TH</th>
                                        <th class="border-0 text-center small fw-bold text-muted">THAO TÁC</th>
                                    </tr>
                                </thead>
                                <tbody id="mini-table-body">
                                    <c:forEach var="m" items="${monHocList}">
                                        <tr onclick="selectSubject('${m.maMH}', 'edit', false)">
                                            <td class="px-3"><span class="badge bg-primary bg-opacity-10 text-primary small">${m.maMH}</span></td>
                                            <td class="fw-bold text-dark small">${m.tenMH}</td>
                                            <td class="text-center small text-muted">${m.soTietLT}/${m.soTietTH}</td>
                                            <td class="text-center">
                                                <div class="d-flex justify-content-center gap-1">
                                                    <button type="button" onclick="event.stopPropagation(); selectSubject('${m.maMH}', 'edit', true)" class="btn btn-xs btn-outline-primary border-0 p-1">
                                                        <i class="bi bi-pencil-square"></i>
                                                    </button>
                                                    <button type="button" onclick="event.stopPropagation(); selectSubject('${m.maMH}', 'delete', true)" class="btn btn-xs btn-outline-danger border-0 p-1">
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

    <!-- NOTIFICATION & CONFIRM MODALS -->
    <div class="modal fade" id="notifyModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-sm">
            <div class="modal-content border-0 shadow-lg rounded-4">
                <div class="modal-body text-center p-4">
                    <div id="notifyIcon" class="mb-3"></div>
                    <h5 id="notifyTitle" class="fw-bold mb-2"></h5>
                    <p id="notifyMessage" class="text-muted small mb-4"></p>
                    <button type="button" class="btn btn-primary w-100 rounded-3 fw-bold" data-bs-dismiss="modal">ĐÓNG</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="confirmModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-sm">
            <div class="modal-content border-0 shadow-lg rounded-4">
                <div class="modal-body text-center p-4">
                    <div class="mb-3"><i class="bi bi-exclamation-triangle-fill text-warning" style="font-size: 3.5rem;"></i></div>
                    <h5 id="confirmTitle" class="fw-bold mb-2">Xác nhận</h5>
                    <p id="confirmMessage" class="text-muted small mb-4"></p>
                    <div class="d-flex gap-2">
                        <button type="button" class="btn btn-light w-100 rounded-3 fw-bold" data-bs-dismiss="modal">HỦY</button>
                        <button type="button" id="confirmOkBtn" class="btn btn-danger w-100 rounded-3 fw-bold shadow-sm">XÓA</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        let currentMode = 'none';
        const contextPath = '${pageContext.request.contextPath}';
        const inputs = ['inp_maMH', 'inp_tenMH', 'inp_soTietLT', 'inp_soTietTH'];

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

        async function refreshTable() {
            try {
                const response = await fetch(contextPath + '/subject/api/list');
                const data = await response.json();
                renderTable(data);
                renderMiniTable(data);
            } catch (e) { console.error(e); }
        }

        function filterLocal() {
            const val = document.getElementById('search-input').value.toLowerCase();
            document.querySelectorAll('#subject-table-body tr').forEach(row => {
                const text = row.innerText.toLowerCase();
                row.style.display = text.includes(val) ? '' : 'none';
            });
        }

        function renderTable(data) {
            const container = document.getElementById('subject-table-body');
            document.getElementById('subject-count').innerText = data.length;
            if (data.length === 0) {
                container.innerHTML = '<tr><td colspan="5" class="text-center py-5 text-muted">Không tìm thấy môn học nào</td></tr>';
                return;
            }
            container.innerHTML = data.map(item => `
                <tr onclick="selectSubject('\${item.maMH}', 'edit', false)">
                    <td class="px-3"><span class="badge-soft-primary">\${item.maMH}</span></td>
                    <td>
                        <div class="subject-title">\${item.tenMH}</div>
                        <div class="subject-subtitle"><i class="bi bi-info-circle"></i> Đã chuẩn hóa</div>
                    </td>
                    <td class="text-center"><span class="badge-soft-secondary">\${item.soTietLT} tiết</span></td>
                    <td class="text-center"><span class="badge-soft-secondary">\${item.soTietTH} tiết</span></td>
                    <td class="text-center">
                        <div class="d-flex gap-2 justify-content-center">
                            <button onclick="event.stopPropagation(); selectSubject('\${item.maMH}', 'edit', true)" class="btn btn-sm btn-outline-primary border-0 rounded-3"><i class="bi bi-pencil-square"></i></button>
                            <button onclick="event.stopPropagation(); selectSubject('\${item.maMH}', 'delete', true)" class="btn btn-sm btn-outline-danger border-0 rounded-3"><i class="bi bi-trash3"></i></button>
                        </div>
                    </td>
                </tr>
            `).join('');
        }

        function renderMiniTable(data) {
            const container = document.getElementById('mini-table-body');
            container.innerHTML = data.map(item => `
                <tr onclick="selectSubject('\${item.maMH}', 'edit', false)">
                    <td class="px-3"><span class="badge bg-primary bg-opacity-10 text-primary small">\${item.maMH}</span></td>
                    <td class="fw-bold text-dark small">\${item.tenMH}</td>
                    <td class="text-center small text-muted">\${item.soTietLT}/\${item.soTietTH}</td>
                    <td class="text-center">
                        <div class="d-flex justify-content-center gap-1">
                            <button type="button" onclick="event.stopPropagation(); selectSubject('\${item.maMH}', 'edit', true)" class="btn btn-xs btn-outline-primary border-0 p-1">
                                <i class="bi bi-pencil-square"></i>
                            </button>
                            <button type="button" onclick="event.stopPropagation(); selectSubject('\${item.maMH}', 'delete', true)" class="btn btn-xs btn-outline-danger border-0 p-1">
                                <i class="bi bi-trash3"></i>
                            </button>
                        </div>
                    </td>
                </tr>
            `).join('');
        }

        async function selectSubject(maMH, mode, forceMode = false) {
            try {
                const res = await fetch(contextPath + '/subject/api/get?maMH=' + maMH);
                const data = await res.json();
                fillForm(data);
                const modalElement = document.getElementById('subjectModal');
                const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
                modal.show();
                
                if (forceMode || currentMode === 'none') {
                    setMode(mode);
                } else {
                    setMode(currentMode);
                }
            } catch (e) { console.error(e); }
        }

        function fillForm(data) {
            document.getElementById('inp_maMH').value = data.maMH || '';
            document.getElementById('inp_tenMH').value = data.tenMH || '';
            document.getElementById('inp_soTietLT').value = data.soTietLT || 0;
            document.getElementById('inp_soTietTH').value = data.soTietTH || 0;
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
                if (mode === 'edit' && id === 'inp_maMH') el.readOnly = true;
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
                maMH: document.getElementById('inp_maMH').value,
                tenMH: document.getElementById('inp_tenMH').value,
                soTietLT: parseInt(document.getElementById('inp_soTietLT').value),
                soTietTH: parseInt(document.getElementById('inp_soTietTH').value)
            };
            if (!data.maMH || !data.tenMH) { showNotify('Cảnh báo', 'Vui lòng nhập đầy đủ thông tin!', 'info'); return; }
            try {
                const res = await fetch(contextPath + '/subject/api/save?mode=' + currentMode, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(data)
                });
                const result = await res.json();
                if (result.status === 'success') {
                    showNotify('Thành công', 'Thông tin môn học đã được lưu.');
                    refreshTable();
                    setMode('none');
                } else showNotify('Lỗi', result.message, 'error');
            } catch (e) { showNotify('Lỗi', e.message, 'error'); }
        }

        async function performDelete() {
            const maMH = document.getElementById('inp_maMH').value;
            showConfirm('Bạn có chắc chắn muốn xóa môn học này?', async () => {
                try {
                    const res = await fetch(contextPath + '/subject/api/delete?maMH=' + maMH, { method: 'POST' });
                    const result = await res.json();
                    if (result.status === 'success') {
                        showNotify('Thành công', 'Môn học đã được xóa.');
                        refreshTable();
                        setMode('none');
                    } else showNotify('Lỗi', result.message, 'error');
                } catch (e) { showNotify('Lỗi', e.message, 'error'); }
            });
        }

        function resetForm() {
            clearForm();
            setMode('none');
            const modalElement = document.getElementById('subjectModal');
            const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
            modal.show();
        }
    </script>
</body>
</html>
