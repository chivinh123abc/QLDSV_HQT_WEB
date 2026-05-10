<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quản lý Lớp Tín Chỉ - QLDSV_HTC_WEB</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <style>
        .badge-soft-primary { background-color: #e0f2fe; color: #0369a1; border-radius: 20px; font-weight: 600; padding: 6px 12px; }
        .badge-soft-secondary { background-color: #f1f5f9; color: #475569; border-radius: 20px; padding: 6px 12px; font-weight: 500; }
        .badge-soft-danger { background-color: #fee2e2; color: #991b1b; border-radius: 20px; font-weight: 600; padding: 6px 12px; }
        .badge-soft-success { background-color: #dcfce7; color: #166534; border-radius: 20px; font-weight: 600; padding: 6px 12px; }
        .table-custom th { font-size: 0.85rem; color: #64748b; font-weight: 700; text-transform: uppercase; border-bottom: 2px solid #e2e8f0; }
        .table-custom td { vertical-align: middle; padding: 1rem 0.75rem; border-bottom: 1px solid #f1f5f9; }
        .table-custom tbody tr { cursor: pointer; transition: all 0.2s; }
        .table-custom tbody tr:hover { background-color: #f0f7ff !important; }
        .ltc-title { font-weight: 700; color: #1e293b; margin-bottom: 2px; }
        .ltc-subtitle { font-size: 0.8rem; color: #94a3b8; display: flex; align-items: center; gap: 4px; }
        
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
                    
                    <div class="d-flex align-items-center gap-3 mb-4">
                        <div class="bg-primary bg-opacity-10 text-primary p-2 rounded-3">
                            <i class="bi bi-layers-fill fs-3"></i>
                        </div>
                        <div>
                            <h3 class="mb-0 fw-bold text-dark">Quản lý Lớp Tín Chỉ</h3>
                            <p class="text-muted small mb-0">Thiết lập kế hoạch mở lớp và giảng dạy</p>
                        </div>
                    </div>

                    <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
                        <div class="card-header bg-white border-bottom-0 pt-4 px-4 pb-0 d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="fw-bold text-primary text-uppercase small mb-1">Danh sách lớp tín chỉ</h6>
                            </div>
                            <button class="btn btn-primary btn-sm rounded-3 px-3 fw-bold shadow-sm" onclick="resetForm()">
                                <i class="bi bi-plus-circle-fill me-1"></i> Mở Lớp Mới
                            </button>
                        </div>

                        <div class="card-body px-4 pb-4">
                            <!-- FILTERS -->
                            <div class="row g-3 mb-4 pb-3 border-bottom align-items-center">
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <span class="input-group-text bg-light border-0"><i class="bi bi-building"></i></span>
                                        <select id="khoa-filter" class="form-select bg-light border-0 fw-semibold" onchange="refreshTable()">
                                            <option value="all">Tất cả Khoa</option>
                                            <c:forEach var="k" items="${khoaList}">
                                                <option value="${k.maKhoa}">${k.tenKhoa}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-8 text-end">
                                    <div class="text-muted small fw-medium">
                                        <i class="bi bi-info-circle me-1"></i> Tìm thấy <strong id="ltc-count" class="text-primary">${ltcList.size()}</strong> lớp tín chỉ
                                    </div>
                                </div>
                            </div>

                            <!-- TABLE -->
                            <div class="table-responsive rounded-3 border">
                                <table class="table table-custom align-middle mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th class="px-3">MÃ LTC</th>
                                            <th>MÔN HỌC</th>
                                            <th>NIÊN KHÓA / KỲ</th>
                                            <th class="text-center">NHÓM</th>
                                            <th>GIẢNG VIÊN</th>
                                            <th class="text-center">TRẠNG THÁI</th>
                                            <th class="text-center">THAO TÁC</th>
                                        </tr>
                                    </thead>
                                    <tbody id="ltc-table-body">
                                        <c:forEach var="item" items="${ltcList}">
                                            <tr onclick="selectLTC('${item.maLTC}', 'edit', false)">
                                                <td class="px-3"><span class="badge-soft-primary">${item.maLTC}</span></td>
                                                <td>
                                                    <div class="ltc-title">${item.maMH}</div>
                                                    <div class="ltc-subtitle"><i class="bi bi-building"></i> ${item.maKhoa}</div>
                                                </td>
                                                <td>
                                                    <div class="fw-bold text-dark">${item.nienKhoa}</div>
                                                    <div class="small text-muted">Học kỳ: ${item.hocKy}</div>
                                                </td>
                                                <td class="text-center"><span class="badge-soft-secondary">Nhóm ${item.nhom}</span></td>
                                                <td>
                                                    <div class="fw-semibold text-primary">${item.maGV}</div>
                                                    <div class="small text-muted">Tối thiểu: ${item.soSVToiThieu} SV</div>
                                                </td>
                                                <td class="text-center">
                                                    <c:choose>
                                                        <c:when test="${item.huyLop}">
                                                            <span class="badge-soft-danger small"><i class="bi bi-x-circle"></i> Đã hủy</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge-soft-success small"><i class="bi bi-check-circle"></i> Đang mở</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="text-center">
                                                    <div class="d-flex gap-2 justify-content-center">
                                                        <button onclick="event.stopPropagation(); selectLTC('${item.maLTC}', 'edit', true)" class="btn btn-sm btn-outline-primary border-0 rounded-3">
                                                            <i class="bi bi-pencil-square"></i>
                                                        </button>
                                                        <button onclick="event.stopPropagation(); selectLTC('${item.maLTC}', 'delete', true)" class="btn btn-sm btn-outline-danger border-0 rounded-3">
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
            </main>
        </div>
    </div>

    <!-- MODAL -->
    <div class="modal fade" id="ltcModal" tabindex="-1">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg rounded-4">
                <div class="modal-header bg-primary text-white border-0 py-3 px-4 rounded-top-4">
                    <h5 class="modal-title fw-bold d-flex align-items-center gap-2">
                        <i class="bi bi-layers-fill"></i> Thiết lập Lớp Tín Chỉ
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body p-4">
                    <!-- TOOLBAR -->
                    <div class="d-flex gap-2 mb-4 p-2 bg-light rounded-3 border shadow-sm">
                        <button type="button" id="btn_mode_add" class="btn btn-toolbar-add fw-bold flex-fill py-2" onclick="handleModeClick('add')">
                            <i class="bi bi-plus-circle"></i> <span class="btn-text">MỞ LỚP</span>
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

                    <div class="row g-3 p-3 bg-white border rounded-3 shadow-sm mb-4">
                        <input type="hidden" id="inp_maLTC">
                        <div class="col-md-6">
                            <label class="form-label small fw-bold text-muted">NIÊN KHÓA</label>
                            <input type="text" class="form-control" id="inp_nienKhoa" placeholder="VD: 2023-2024" required disabled>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label small fw-bold text-muted">HỌC KỲ</label>
                            <input type="number" class="form-control" id="inp_hocKy" min="1" max="3" value="1" required disabled>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label small fw-bold text-muted">NHÓM</label>
                            <input type="number" class="form-control" id="inp_nhom" min="1" value="1" required disabled>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-bold text-muted">MÔN HỌC</label>
                            <select class="form-select" id="inp_maMH" required disabled>
                                <option value="">-- Chọn môn học --</option>
                                <c:forEach var="mh" items="${monHocList}">
                                    <option value="${mh.maMH}">[${mh.maMH}] ${mh.tenMH}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-bold text-muted">GIẢNG VIÊN</label>
                            <select class="form-select" id="inp_maGV" required disabled>
                                <option value="">-- Chọn giảng viên --</option>
                                <c:forEach var="gv" items="${giangVienList}">
                                    <option value="${gv.maGV}">[${gv.maGV}] ${gv.ho} ${gv.ten}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-bold text-muted">KHOA QUẢN LÝ</label>
                            <select id="inp_maKhoa" class="form-select" disabled>
                                <c:forEach var="k" items="${khoaList}">
                                    <option value="${k.maKhoa}">${k.tenKhoa}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label small fw-bold text-muted">SV TỐI THIỂU</label>
                            <input type="number" class="form-control" id="inp_soSVToiThieu" min="1" value="1" required disabled>
                        </div>
                        <div class="col-md-3 d-flex align-items-end pb-1">
                            <div class="form-check form-switch mb-2">
                                <input class="form-check-input" type="checkbox" id="inp_huyLop" disabled>
                                <label class="form-check-label fw-bold text-danger small" for="inp_huyLop">Hủy lớp</label>
                            </div>
                        </div>
                    </div>

                    <!-- MINI TABLE FOR REFERENCE -->
                    <div class="mt-4 pt-3 border-top">
                        <div class="d-flex align-items-center justify-content-between mb-3">
                            <h6 class="fw-bold text-primary mb-0"><i class="bi bi-list-ul"></i> Lớp tín chỉ đã mở</h6>
                            <div class="input-group" style="max-width:220px;">
                                <span class="input-group-text py-1 border-0 bg-white"><i class="bi bi-search small text-muted"></i></span>
                                <input type="text" id="mini-ltc-search" class="form-control form-control-sm border-0 border-bottom" placeholder="Lọc nhanh..." onkeyup="filterMiniTable()">
                            </div>
                        </div>
                        <div class="table-responsive rounded-3 border bg-white" style="max-height: 200px; overflow-y: auto;">
                            <table class="table table-hover table-sm align-middle mb-0">
                                <thead class="table-light sticky-top">
                                    <tr>
                                        <th class="px-3 small">MÃ LTC</th>
                                        <th class="small">MÔN / NHÓM</th>
                                        <th class="small">KỲ / NIÊN KHÓA</th>
                                        <th class="text-center small">THAO TÁC</th>
                                    </tr>
                                </thead>
                                <tbody id="mini-table-body">
                                    <c:forEach var="m" items="${ltcList}">
                                        <tr onclick="selectLTC('${m.maLTC}', 'edit', false)">
                                            <td class="px-3 small fw-bold text-primary">${m.maLTC}</td>
                                            <td class="small">
                                                <div class="fw-bold text-dark">${m.maMH}</div>
                                                <div class="text-muted">Nhóm ${m.nhom}</div>
                                            </td>
                                            <td class="small">
                                                <div>Kỳ ${m.hocKy}</div>
                                                <div class="text-muted">${m.nienKhoa}</div>
                                            </td>
                                            <td class="text-center">
                                                <div class="d-flex justify-content-center gap-1">
                                                    <button type="button" onclick="event.stopPropagation(); selectLTC('${m.maLTC}', 'edit', true)" class="btn btn-xs btn-outline-primary border-0 p-1">
                                                        <i class="bi bi-pencil-square"></i>
                                                    </button>
                                                    <button type="button" onclick="event.stopPropagation(); selectLTC('${m.maLTC}', 'delete', true)" class="btn btn-xs btn-outline-danger border-0 p-1">
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
        const inputs = ['inp_nienKhoa', 'inp_hocKy', 'inp_nhom', 'inp_maMH', 'inp_maGV', 'inp_maKhoa', 'inp_soSVToiThieu', 'inp_huyLop'];

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
            const modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('confirmModal'));
            btn.onclick = () => { modal.hide(); onConfirm(); };
            modal.show();
        }

        async function refreshTable() {
            const maKhoa = document.getElementById('khoa-filter').value;
            try {
                const response = await fetch(contextPath + '/credit-class/api/list?maKhoa=' + maKhoa);
                const data = await response.json();
                renderTable(data);
                renderMiniTable(data);
            } catch (e) { console.error(e); }
        }

        function renderTable(data) {
            const container = document.getElementById('ltc-table-body');
            document.getElementById('ltc-count').innerText = data.length;
            if (data.length === 0) {
                container.innerHTML = '<tr><td colspan="7" class="text-center py-5 text-muted">Không tìm thấy lớp tín chỉ nào</td></tr>';
                return;
            }
            container.innerHTML = data.map(item => `
                <tr onclick="selectLTC(\${item.maLTC}, 'edit', false)">
                    <td class="px-3"><span class="badge-soft-primary">\${item.maLTC}</span></td>
                    <td>
                        <div class="ltc-title">\${item.maMH}</div>
                        <div class="ltc-subtitle"><i class="bi bi-building"></i> \${item.maKhoa}</div>
                    </td>
                    <td>
                        <div class="fw-bold text-dark">\${item.nienKhoa}</div>
                        <div class="small text-muted">Học kỳ: \${item.hocKy}</div>
                    </td>
                    <td class="text-center"><span class="badge-soft-secondary">Nhóm \${item.nhom}</span></td>
                    <td>
                        <div class="fw-semibold text-primary">\${item.maGV}</div>
                        <div class="small text-muted">Tối thiểu: \${item.soSVToiThieu} SV</div>
                    </td>
                    <td class="text-center">
                        \${item.huyLop ? 
                            '<span class="badge-soft-danger small"><i class="bi bi-x-circle"></i> Đã hủy</span>' : 
                            '<span class="badge-soft-success small"><i class="bi bi-check-circle"></i> Đang mở</span>'}
                    </td>
                    <td class="text-center">
                        <div class="d-flex gap-2 justify-content-center">
                            <button onclick="event.stopPropagation(); selectLTC(\${item.maLTC}, 'edit', true)" class="btn btn-sm btn-outline-primary border-0 rounded-3"><i class="bi bi-pencil-square"></i></button>
                            <button onclick="event.stopPropagation(); selectLTC(\${item.maLTC}, 'delete', true)" class="btn btn-sm btn-outline-danger border-0 rounded-3"><i class="bi bi-trash3"></i></button>
                        </div>
                    </td>
                </tr>
            `).join('');
        }

        function renderMiniTable(data) {
            const container = document.getElementById('mini-table-body');
            container.innerHTML = data.map(item => `
                <tr onclick="selectLTC(\${item.maLTC}, 'edit', false)">
                    <td class="px-3 small fw-bold text-primary">\${item.maLTC}</td>
                    <td class="small">
                        <div class="fw-bold text-dark">\${item.maMH}</div>
                        <div class="text-muted">Nhóm \${item.nhom}</div>
                    </td>
                    <td class="small">
                        <div>Kỳ \${item.hocKy}</div>
                        <div class="text-muted">\${item.nienKhoa}</div>
                    </td>
                    <td class="text-center">
                        <div class="d-flex justify-content-center gap-1">
                            <button type="button" onclick="event.stopPropagation(); selectLTC(\${item.maLTC}, 'edit', true)" class="btn btn-xs btn-outline-primary border-0 p-1">
                                <i class="bi bi-pencil-square"></i>
                            </button>
                            <button type="button" onclick="event.stopPropagation(); selectLTC(\${item.maLTC}, 'delete', true)" class="btn btn-xs btn-outline-danger border-0 p-1">
                                <i class="bi bi-trash3"></i>
                            </button>
                        </div>
                    </td>
                </tr>
            `).join('');
        }

        async function selectLTC(maLTC, mode, forceMode = false) {
            try {
                const res = await fetch(contextPath + '/credit-class/api/get?maLTC=' + maLTC);
                const data = await res.json();
                fillForm(data);
                bootstrap.Modal.getOrCreateInstance(document.getElementById('ltcModal')).show();
                if (forceMode || currentMode === 'none') setMode(mode);
                else setMode(currentMode);
            } catch (e) { console.error(e); }
        }

        function fillForm(data) {
            document.getElementById('inp_maLTC').value = data.maLTC || '';
            document.getElementById('inp_nienKhoa').value = data.nienKhoa || '';
            document.getElementById('inp_hocKy').value = data.hocKy || 1;
            document.getElementById('inp_nhom').value = data.nhom || 1;
            document.getElementById('inp_maMH').value = data.maMH || '';
            document.getElementById('inp_maGV').value = data.maGV || '';
            document.getElementById('inp_maKhoa').value = data.maKhoa || '';
            document.getElementById('inp_soSVToiThieu').value = data.soSVToiThieu || 1;
            document.getElementById('inp_huyLop').checked = data.huyLop || false;
        }

        function clearForm() {
            document.getElementById('inp_maLTC').value = '0';
            inputs.forEach(id => {
                const el = document.getElementById(id);
                if (el.type === 'checkbox') el.checked = false;
                else el.value = (id === 'inp_hocKy' || id === 'inp_nhom' || id === 'inp_soSVToiThieu') ? 1 : '';
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
                document.getElementById(id).disabled = mode === 'none' || mode === 'delete';
            });

            if (mode === 'none') clearForm();
            else if (mode === 'add') {
                ['edit', 'delete'].forEach(m => document.getElementById(btns[m]).classList.add('btn-toolbar-disabled'));
                document.getElementById(btns.add).classList.add('active');
            } else if (mode === 'edit') {
                ['add', 'delete'].forEach(m => document.getElementById(btns[m]).classList.add('btn-toolbar-disabled'));
                document.getElementById(btns.edit).classList.add('active');
            } else if (mode === 'delete') {
                ['add', 'edit'].forEach(m => document.getElementById(btns[m]).classList.add('btn-toolbar-disabled'));
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
                maLTC: parseInt(document.getElementById('inp_maLTC').value) || 0,
                nienKhoa: document.getElementById('inp_nienKhoa').value,
                hocKy: parseInt(document.getElementById('inp_hocKy').value),
                nhom: parseInt(document.getElementById('inp_nhom').value),
                maMH: document.getElementById('inp_maMH').value,
                maGV: document.getElementById('inp_maGV').value,
                maKhoa: document.getElementById('inp_maKhoa').value,
                soSVToiThieu: parseInt(document.getElementById('inp_soSVToiThieu').value),
                huyLop: document.getElementById('inp_huyLop').checked
            };
            if (!data.nienKhoa || !data.maMH || !data.maGV) { showNotify('Cảnh báo', 'Vui lòng điền đủ Niên khóa, Môn học, Giảng viên!', 'info'); return; }
            try {
                const res = await fetch(contextPath + '/credit-class/api/save?mode=' + currentMode, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(data)
                });
                const result = await res.json();
                if (result.status === 'success') {
                    showNotify('Thành công', 'Thông tin lớp tín chỉ đã được ghi.');
                    refreshTable();
                    setMode('none');
                } else showNotify('Lỗi', result.message, 'error');
            } catch (e) { showNotify('Lỗi', e.message, 'error'); }
        }

        async function performDelete() {
            const maLTC = document.getElementById('inp_maLTC').value;
            showConfirm('Xác nhận xóa lớp tín chỉ này?', async () => {
                try {
                    const res = await fetch(contextPath + '/credit-class/api/delete?maLTC=' + maLTC, { method: 'POST' });
                    const result = await res.json();
                    if (result.status === 'success') {
                        showNotify('Thành công', 'Lớp tín chỉ đã được xóa.');
                        refreshTable();
                        setMode('none');
                    } else showNotify('Lỗi', result.message, 'error');
                } catch (e) { showNotify('Lỗi', e.message, 'error'); }
            });
        }

        function normalizeVN(str) {
            return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/\u0111/g, 'd').replace(/\u0110/g, 'D').toLowerCase();
        }

        function filterMiniTable() {
            const val = normalizeVN(document.getElementById('mini-ltc-search').value);
            document.querySelectorAll('#mini-table-body tr').forEach(row => {
                row.style.display = normalizeVN(row.innerText).includes(val) ? '' : 'none';
            });
        }

        function resetForm() {
            clearForm();
            setMode('none');
            bootstrap.Modal.getOrCreateInstance(document.getElementById('ltcModal')).show();
        }
    </script>
</body>
</html>
