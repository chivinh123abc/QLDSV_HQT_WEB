<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Đăng ký Môn học - QLDSV_HTC_WEB</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <style>
        .badge-soft-primary { background-color: #e0f2fe; color: #0369a1; border-radius: 20px; font-weight: 600; padding: 6px 12px; }
        .badge-soft-success { background-color: #dcfce7; color: #166534; border-radius: 20px; font-weight: 600; padding: 6px 12px; }
        .badge-soft-danger { background-color: #fee2e2; color: #991b1b; border-radius: 20px; font-weight: 600; padding: 6px 12px; }
        .table-custom th { font-size: 0.85rem; color: #64748b; font-weight: 700; text-transform: uppercase; border-bottom: 2px solid #e2e8f0; }
        .table-custom td { vertical-align: middle; padding: 1rem 0.75rem; border-bottom: 1px solid #f1f5f9; }
        .registration-card { transition: all 0.3s; border: 1px solid #e2e8f0; }
        .registration-card:hover { transform: translateY(-5px); box-shadow: 0 10px 20px rgba(0,0,0,0.05) !important; border-color: #4361ee; }
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
                    
                    <div class="d-flex align-items-center justify-content-between mb-4">
                        <div class="d-flex align-items-center gap-3">
                            <div class="bg-primary bg-opacity-10 text-primary p-2 rounded-3">
                                <i class="bi bi-person-check-fill fs-3"></i>
                            </div>
                            <div>
                                <h3 class="mb-0 fw-bold text-dark">Đăng ký Môn học</h3>
                                <p class="text-muted small mb-0">Quản lý việc đăng ký lớp tín chỉ của sinh viên</p>
                            </div>
                        </div>
                    </div>

                    <div class="row g-4">
                        <!-- LEFT SIDE: STUDENT INFO & SEARCH -->
                        <div class="col-lg-4">
                            <div class="card border-0 shadow-sm rounded-4 mb-4">
                                <div class="card-body p-4">
                                    <h6 class="fw-bold text-primary mb-3">Tra cứu Sinh viên</h6>
                                    <div class="input-group mb-3 shadow-sm rounded-3 overflow-hidden" <c:if test="${sessionScope.role == 'SINHVIEN'}">style="display:none;"</c:if>>
                                        <span class="input-group-text bg-white border-end-0"><i class="bi bi-person-badge"></i></span>
                                        <input type="text" id="inp_maSV" class="form-control border-start-0 ps-0" placeholder="Nhập Mã SV..." value="${sessionScope.role == 'SINHVIEN' ? sessionScope.user.username : ''}">
                                        <button class="btn btn-primary fw-bold" type="button" onclick="loadStudentData()">TÌM</button>
                                    </div>
                                    <div id="student-info-panel" class="bg-light p-3 rounded-3 d-none">
                                        <div class="d-flex align-items-center gap-3 mb-3">
                                            <div class="bg-white p-2 rounded-circle border shadow-sm">
                                                <i class="bi bi-person-circle fs-3 text-primary"></i>
                                            </div>
                                            <div>
                                                <h5 id="info-name" class="fw-bold mb-0 text-dark">---</h5>
                                                <span id="info-maSV" class="badge bg-primary rounded-pill small">---</span>
                                            </div>
                                        </div>
                                        <div class="small text-muted mb-1"><i class="bi bi-building me-2"></i>Lớp: <strong id="info-maLop" class="text-dark">---</strong></div>
                                        <div class="small text-muted"><i class="bi bi-info-circle me-2"></i>Trạng thái: <span id="info-status" class="badge bg-success bg-opacity-10 text-success rounded-pill">Đang học</span></div>
                                    </div>
                                    <div id="no-student-alert" class="text-center py-4 text-muted small">
                                        <i class="bi bi-search fs-2 d-block mb-2 opacity-25"></i>
                                        Vui lòng nhập Mã SV để bắt đầu
                                    </div>
                                </div>
                            </div>

                            <div class="card border-0 shadow-sm rounded-4">
                                <div class="card-body p-4">
                                    <h6 class="fw-bold text-success mb-3">Lớp tín chỉ đã đăng ký</h6>
                                    <div id="registered-list" class="list-group list-group-flush">
                                        <div class="text-center py-4 text-muted small">Chưa có dữ liệu</div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- RIGHT SIDE: AVAILABLE CLASSES -->
                        <div class="col-lg-8">
                            <div class="card border-0 shadow-sm rounded-4">
                                <div class="card-header bg-white border-bottom-0 pt-4 px-4 pb-0 d-flex justify-content-between align-items-center">
                                    <h6 class="fw-bold text-dark mb-0">Danh sách Lớp Tín Chỉ có thể đăng ký</h6>
                                    <div class="input-group" style="max-width: 200px;">
                                        <span class="input-group-text bg-light border-0"><i class="bi bi-funnel"></i></span>
                                        <input type="text" id="ltc-search" class="form-control bg-light border-0 small" placeholder="Lọc..." onkeyup="filterLTC()">
                                    </div>
                                </div>
                                <div class="card-body p-4">
                                    <div class="table-responsive rounded-3 border">
                                        <table class="table table-custom align-middle mb-0">
                                            <thead class="table-light">
                                                <tr>
                                                    <th class="px-3">MÃ LTC</th>
                                                    <th>MÔN HỌC / NHÓM</th>
                                                    <th>NIÊN KHÓA / KỲ</th>
                                                    <th class="text-center">THAO TÁC</th>
                                                </tr>
                                            </thead>
                                            <tbody id="available-ltc-body">
                                                <!-- Dynamic Content -->
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
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
                    <button type="button" class="btn btn-primary w-100 rounded-3 fw-bold" data-bs-dismiss="modal">ĐÓNG</button>
                </div>
            </div>
        </div>
    </div>

    <!-- CONFIRMATION MODAL -->
    <div class="modal fade" id="confirmModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-sm">
            <div class="modal-content border-0 shadow-lg rounded-4">
                <div class="modal-body text-center p-4">
                    <div class="mb-3 text-warning">
                        <i class="bi bi-exclamation-triangle-fill" style="font-size: 3.5rem;"></i>
                    </div>
                    <h5 class="fw-bold mb-2">Xác nhận</h5>
                    <p id="confirmMessage" class="text-muted small mb-4">Bạn có chắc chắn muốn hủy đăng ký lớp này không?</p>
                    <div class="d-flex gap-2">
                        <button type="button" class="btn btn-light w-100 rounded-3 fw-bold" data-bs-dismiss="modal">HỦY</button>
                        <button type="button" id="btnConfirmAction" class="btn btn-danger w-100 rounded-3 fw-bold">ĐỒNG Ý</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        const contextPath = '${pageContext.request.contextPath}';
        let currentStudent = null;
        let registeredLTCIds = [];
        let availableLTCList = [];

        document.addEventListener('DOMContentLoaded', () => {
            loadAvailableLTC();
            if (document.getElementById('inp_maSV').value) {
                loadStudentData();
            }
        });

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
            const btn = document.getElementById('btnConfirmAction');
            const modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('confirmModal'));
            btn.onclick = () => {
                onConfirm();
                modal.hide();
            };
            modal.show();
        }

        async function loadStudentData() {
            const maSV = document.getElementById('inp_maSV').value;
            if (!maSV) return;
            try {
                const res = await fetch(contextPath + '/student/api/get?maSV=' + maSV.trim());
                const student = await res.json();
                if (student) {
                    currentStudent = student;
                    updateStudentPanel(student);
                    await loadRegisteredLTC(maSV.trim());
                } else {
                    showNotify('Thông báo', 'Không tìm thấy sinh viên!', 'info');
                }
            } catch (e) { console.error(e); }
        }

        function updateStudentPanel(sv) {
            document.getElementById('student-info-panel').classList.remove('d-none');
            document.getElementById('no-student-alert').classList.add('d-none');
            document.getElementById('info-name').innerText = sv.ho + ' ' + sv.ten;
            document.getElementById('info-maSV').innerText = sv.maSV;
            document.getElementById('info-maLop').innerText = sv.maLop;
            document.getElementById('info-status').innerText = sv.daNghiHoc ? 'Đã nghỉ học' : 'Đang học';
            document.getElementById('info-status').className = 'badge ' + (sv.daNghiHoc ? 'bg-danger' : 'bg-success') + ' bg-opacity-10 ' + (sv.daNghiHoc ? 'text-danger' : 'text-success') + ' rounded-pill';
        }

        async function loadAvailableLTC() {
            try {
                if (availableLTCList.length === 0) {
                    const res = await fetch(contextPath + '/registration/api/available-classes');
                    availableLTCList = await res.json();
                }
                
                // Helper to get registered subjects in a specific semester
                const getRegisteredSubjects = (nk, hk) => {
                    const cleanNk = (nk || '').trim().toUpperCase();
                    const cleanHk = Number(hk);
                    return availableLTCList
                        .filter(ltc => registeredLTCIds.includes(String(ltc.maLTC)) && 
                                       (ltc.nienKhoa || '').trim().toUpperCase() === cleanNk && 
                                       Number(ltc.hocKy) === cleanHk)
                        .map(ltc => (ltc.maMH || '').trim().toUpperCase());
                };
 
                const container = document.getElementById('available-ltc-body');
                container.innerHTML = availableLTCList.map(item => {
                    const isRegistered = registeredLTCIds.includes(String(item.maLTC));
                    const subjectsInSemester = getRegisteredSubjects(item.nienKhoa, item.hocKy);
                    const cleanMaMH = (item.maMH || '').trim().toUpperCase();
                    const isSameSubjectRegistered = subjectsInSemester.includes(cleanMaMH);
                    
                    let btnHtml = '';
                    if (isRegistered) {
                        btnHtml = `
                            <button class="btn btn-sm btn-outline-danger rounded-3 px-3" onclick="confirmCancel('\${item.maLTC}')">
                                <i class="bi bi-x-circle me-1"></i> Hủy đăng ký
                            </button>
                        `;
                    } else if (isSameSubjectRegistered) {
                        btnHtml = `
                            <button class="btn btn-sm btn-secondary rounded-3 px-3" disabled title="Bạn đã đăng ký một lớp khác của môn này">
                                <i class="bi bi-dash-circle me-1"></i> Đã đăng ký môn này
                            </button>
                        `;
                    } else {
                        btnHtml = `
                            <button class="btn btn-sm btn-primary rounded-3 px-3" onclick="registerLTC('\${item.maLTC}')">
                                <i class="bi bi-plus-circle me-1"></i> Đăng ký
                            </button>
                        `;
                    }
 
                    return `
                        <tr>
                            <td class="px-3"><span class="badge-soft-primary">\${item.maLTC}</span></td>
                            <td>
                                <div class="fw-bold text-dark">\${item.maMH}</div>
                                <div class="small text-muted">Nhóm \${item.nhom} | \${item.maKhoa}</div>
                            </td>
                            <td>
                                <div class="fw-bold text-dark">\${item.nienKhoa}</div>
                                <div class="small text-muted">Học kỳ: \${item.hocKy}</div>
                            </td>
                            <td class="text-center">
                                \${btnHtml}
                            </td>
                        </tr>
                    `;
                }).join('');
            } catch (e) { console.error(e); }
        }
 
        async function loadRegisteredLTC(maSV) {
            try {
                const res = await fetch(contextPath + '/registration/api/list');
                const allReg = await res.json();
                const myReg = allReg.filter(r => (r.maSV || '').trim().toUpperCase() === maSV.trim().toUpperCase() && 
                                                 !(r.huyDangKy === true || r.huyDangKy === 1 || String(r.huyDangKy) === 'true'));
                registeredLTCIds = myReg.map(r => String(r.maLTC));
                
                const container = document.getElementById('registered-list');
                if (myReg.length === 0) {
                    container.innerHTML = '<div class="text-center py-4 text-muted small">Chưa đăng ký môn nào</div>';
                } else {
                    container.innerHTML = myReg.map(r => `
                        <div class="list-group-item px-0 py-3 d-flex justify-content-between align-items-center">
                            <div>
                                <div class="fw-bold text-dark small">Lớp LTC: \${r.maLTC}</div>
                                <div class="text-muted" style="font-size: 0.75rem;">Trạng thái: Đã đăng ký</div>
                            </div>
                            <button class="btn btn-xs btn-outline-danger border-0" onclick="confirmCancel('\${r.maLTC}')">
                                <i class="bi bi-x-circle-fill"></i> Hủy
                            </button>
                        </div>
                    `).join('');
                }
                loadAvailableLTC();
            } catch (e) { console.error(e); }
        }

        async function registerLTC(maLTC) {
            if (!currentStudent) { showNotify('Cảnh báo', 'Vui lòng chọn sinh viên trước!', 'info'); return; }
            try {
                const res = await fetch(contextPath + `/registration/api/register?maSV=\${currentStudent.maSV}&maLTC=\${maLTC}`, { method: 'POST' });
                const result = await res.json();
                if (result.status === 'success') {
                    showNotify('Thành công', result.message);
                    loadRegisteredLTC(currentStudent.maSV);
                } else showNotify('Lỗi', result.message, 'error');
            } catch (e) { console.error(e); }
        }

        function confirmCancel(maLTC) {
            showConfirm("Bạn có chắc chắn muốn hủy đăng ký lớp tín chỉ này không?", () => cancelReg(maLTC));
        }

        async function cancelReg(maLTC) {
            if (!currentStudent) return;
            try {
                const res = await fetch(contextPath + `/registration/api/cancel?maSV=\${currentStudent.maSV}&maLTC=\${maLTC}`, { method: 'POST' });
                const result = await res.json();
                if (result.status === 'success') {
                    showNotify('Thành công', result.message);
                    loadRegisteredLTC(currentStudent.maSV);
                } else showNotify('Lỗi', result.message, 'error');
            } catch (e) { console.error(e); }
        }

        function normalizeVN(str) {
            return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/\u0111/g, 'd').replace(/\u0110/g, 'D').toLowerCase();
        }

        function filterLTC() {
            const val = normalizeVN(document.getElementById('ltc-search').value);
            document.querySelectorAll('#available-ltc-body tr').forEach(row => {
                row.style.display = normalizeVN(row.innerText).includes(val) ? '' : 'none';
            });
        }
    </script>
</body>
</html>

