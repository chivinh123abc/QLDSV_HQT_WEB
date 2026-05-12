<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="utf-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title>Nhập điểm - QLDSV_HTC_WEB</title>
            <!-- Google Fonts -->
            <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap"
                rel="stylesheet">
            <!-- Bootstrap CSS -->
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
            <!-- Bootstrap Icons -->
            <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css"
                rel="stylesheet" />
            <!-- Animate.css -->
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css" />
            <!-- Custom CSS -->
            <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
            <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
            <style>
                :root {
                    --primary-gradient: linear-gradient(135deg, #4361ee, #4895ef);
                    --success-gradient: linear-gradient(135deg, #2ecc71, #27ae60);
                    --bg-light: #f8f9fa;
                }

                body {
                    font-family: 'Inter', sans-serif;
                    background-color: var(--bg-light);
                }

                .filter-section {
                    background: #fff;
                    padding: 25px;
                    border-radius: 16px;
                    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.04);
                    margin-bottom: 30px;
                    border: 1px solid rgba(0, 0, 0, 0.05);
                }

                .page-title {
                    font-weight: 700;
                    background: var(--primary-gradient);
                    -webkit-background-clip: text;
                    background-clip: text;
                    -webkit-text-fill-color: transparent;
                    margin-bottom: 0;
                }

                .card-custom {
                    border-radius: 16px;
                    border: none;
                    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.04);
                    overflow: hidden;
                }

                .mark-table thead th {
                    background-color: #f1f5f9;
                    color: #475569;
                    font-weight: 600;
                    text-transform: uppercase;
                    font-size: 0.75rem;
                    letter-spacing: 0.05em;
                    padding: 15px;
                    border: none;
                }

                .mark-table tbody td {
                    padding: 12px 15px;
                    border-bottom: 1px solid #f1f5f9;
                    vertical-align: middle;
                }

                .input-mark {
                    width: 75px;
                    text-align: center;
                    border-radius: 8px;
                    border: 1px solid #e2e8f0;
                    transition: all 0.2s;
                    font-weight: 500;
                    padding: 8px;
                }

                .input-mark:focus {
                    border-color: #4361ee;
                    box-shadow: 0 0 0 3px rgba(67, 97, 238, 0.15);
                    outline: none;
                }

                .input-mark.invalid {
                    border-color: #ef4444;
                    background-color: #fef2f2;
                }

                .total-badge {
                    font-weight: 700;
                    font-size: 0.9rem;
                    padding: 6px 12px;
                    border-radius: 10px;
                }

                .save-all-btn {
                    background: var(--primary-gradient);
                    border: none;
                    padding: 10px 25px;
                    border-radius: 12px;
                    font-weight: 600;
                    box-shadow: 0 4px 15px rgba(67, 97, 238, 0.3);
                    transition: all 0.3s;
                }

                .save-all-btn:hover {
                    transform: translateY(-2px);
                    box-shadow: 0 6px 20px rgba(67, 97, 238, 0.4);
                }

                .summary-badge {
                    background-color: #f1f5f9;
                    color: #475569;
                    padding: 8px 15px;
                    border-radius: 12px;
                    font-size: 0.85rem;
                    font-weight: 500;
                    display: flex;
                    align-items: center;
                    gap: 8px;
                }

                .toast-container {
                    position: fixed;
                    top: 20px;
                    right: 20px;
                    z-index: 9999;
                }
            </style>
        </head>

        <body>
            <div class="app-layout">
                <jsp:include page="/WEB-INF/views/shared/sidebar.jsp" />

                <div class="app-main">
                    <jsp:include page="/WEB-INF/views/shared/header.jsp" />

                    <main id="main-content" class="app-content p-4">
                        <div class="container-fluid max-w-7xl mx-auto">

                            <div
                                class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
                                <div class="animate__animated animate__fadeInLeft">
                                    <h2 class="page-title"><i class="bi bi-grid-3x3-gap-fill me-2"></i>Quản lý Nhập điểm
                                    </h2>
                                    <p class="text-muted small mb-0">Hệ thống nhập điểm học phần theo lớp tín chỉ</p>
                                </div>
                                <div class="d-flex gap-2 animate__animated animate__fadeInRight" id="summaryHeader"
                                    style="display: none !important;">
                                    <div class="summary-badge"><i class="bi bi-book text-primary"></i> <span
                                            id="sum-subject">---</span></div>
                                    <div class="summary-badge"><i class="bi bi-people text-success"></i> <span
                                            id="sum-count">0</span> SV</div>
                                    <button class="btn btn-primary save-all-btn ms-2" id="btnSaveAll">
                                        <i class="bi bi-cloud-arrow-up-fill me-2"></i> LƯU TẤT CẢ
                                    </button>
                                </div>
                            </div>

                            <div class="filter-section animate__animated animate__fadeIn">
                                <div class="row g-3">
                                    <c:if test="${sessionScope.role == 'PGV'}">
                                        <div class="col-lg-3 col-md-6">
                                            <label class="form-label text-secondary fw-semibold small">KHOA</label>
                                            <select class="form-select border-0 bg-light rounded-3" id="maKhoaFilter" onchange="updateSubjects()">
                                                <option value="all">-- Tất cả khoa --</option>
                                                <c:forEach var="k" items="${khoaList}">
                                                    <option value="${k.maKhoa}">${k.tenKhoa}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </c:if>
                                    <div class="col-lg-2 col-md-6">
                                        <label class="form-label text-secondary fw-semibold small">NIÊN KHÓA</label>
                                        <select class="form-select border-0 bg-light rounded-3" id="nienKhoa">
                                            <option value="all">-- Tất cả niên khóa --</option>
                                            <c:forEach var="nk" items="${nienKhoaList}">
                                                <option value="${nk}">${nk}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-lg-2 col-md-6">
                                        <label class="form-label text-secondary fw-semibold small">HỌC KỲ</label>
                                        <select class="form-select border-0 bg-light rounded-3" id="hocKy">
                                            <option value="all">-- Tất cả học kỳ --</option>
                                            <option value="1">1</option>
                                            <option value="2">2</option>
                                            <option value="3">3</option>
                                            <option value="4">4</option>
                                        </select>
                                    </div>
                                    <div class="col-lg-4 col-md-12">
                                        <label class="form-label text-secondary fw-semibold small">MÔN HỌC</label>
                                        <select class="form-select border-0 bg-light rounded-3" id="monHoc" disabled>
                                            <option value="">-- Chọn môn học --</option>
                                        </select>
                                    </div>
                                    <div class="col-lg-2 col-md-8">
                                        <label class="form-label text-secondary fw-semibold small">NHÓM</label>
                                        <select class="form-select border-0 bg-light rounded-3" id="nhom" disabled>
                                            <option value="">-- Nhóm --</option>
                                        </select>
                                    </div>
                                    <div class="col-lg-2 col-md-12">
                                        <label class="form-label text-secondary fw-semibold small">TÌM MÃ SV</label>
                                        <input type="text" class="form-control border-0 bg-light rounded-3 shadow-none"
                                            id="searchMaSV" placeholder="Nhập mã SV...">
                                    </div>
                                    <div class="col-lg-1 col-md-4 d-flex align-items-end">
                                        <button type="button" class="btn btn-primary w-100 rounded-3 p-2 shadow-sm"
                                            id="btnLoad">
                                            <i class="bi bi-search fs-5"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <div class="card card-custom animate__animated animate__fadeInUp">
                                <div class="card-body p-0">
                                    <div class="table-responsive">
                                        <table class="table table-hover align-middle mb-0 mark-table"
                                            id="studentMarkTable">
                                            <thead>
                                                <tr>
                                                    <th class="ps-4">STT</th>
                                                    <th>Mã Sinh Viên</th>
                                                    <th>Họ và Tên</th>
                                                    <th>Môn học & Nhóm</th>
                                                    <th class="text-center">CC (10%)</th>
                                                    <th class="text-center">GK (30%)</th>
                                                    <th class="text-center">CK (60%)</th>
                                                    <th class="text-center">Tổng</th>
                                                    <th class="text-center">Thao tác</th>
                                                </tr>
                                            </thead>
                                            <tbody id="studentListBody">
                                                <tr>
                                                    <td colspan="8" class="text-center py-5 text-muted">
                                                        <div class="opacity-50 mb-3"><i class="bi bi-inbox fs-1"></i>
                                                        </div>
                                                        Vui lòng chọn thông tin lớp tín chỉ để bắt đầu nhập điểm.
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </main>
                </div>
            </div>

            <!-- TOAST NOTIFICATIONS -->
            <div class="toast-container"></div>

            <!-- Bootstrap JS -->
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
            <script>
                $(document).ready(function () {
                    var currentMaLTC = 0;
                    var userRole = '${sessionScope.role}';
                    var userMaKhoa = '${sessionScope.maKhoa}';

                    // If user is KHOA, ensure the filter is set before loading
                    if (userRole === 'KHOA' && userMaKhoa) {
                        $('#maKhoaFilter').val(userMaKhoa);
                    }

                    function showToast(message, type = 'success') {
                        const id = 'toast-' + Date.now();
                        const icon = type === 'success' ? 'bi-check-circle-fill' : 'bi-exclamation-triangle-fill';
                        const color = type === 'success' ? 'text-success' : 'text-danger';

                        const html = `
                    <div id="\${id}" class="toast align-items-center border-0 animate__animated animate__fadeInRight" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="d-flex">
                            <div class="toast-body d-flex align-items-center gap-2">
                                <i class="bi \${icon} \${color} fs-5"></i>
                                <span class="fw-medium">\${message}</span>
                            </div>
                            <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                        </div>
                    </div>
                `;
                        $('.toast-container').append(html);
                        const toastEl = document.getElementById(id);
                        const toast = new bootstrap.Toast(toastEl, { delay: 3000 });
                        toast.show();
                        toastEl.addEventListener('hidden.bs.toast', () => $(toastEl).remove());
                    }

                    function updateSubjects() {
                        let nk = $('#nienKhoa').val();
                        let hk = $('#hocKy').val();
                        let maKhoa = $('#maKhoaFilter').val() || '';
                        if (nk && hk) {
                            $.get('${pageContext.request.contextPath}/mark/get-subjects', { nienKhoa: nk, hocKy: hk, maKhoa: maKhoa }, function (data) {
                                let html = '<option value="">-- Chọn môn học --</option>';
                                data.forEach(item => {
                                    html += `<option value="\${item[0]}">[\${item[0]}] \${item[1]}</option>`;
                                });
                                $('#monHoc').html(html).prop('disabled', false);
                                $('#nhom').html('<option value="">-- Nhóm --</option>').prop('disabled', true);
                            });
                        }
                    }

                    $('#nienKhoa, #hocKy').change(updateSubjects);

                    $('#monHoc').change(function () {
                        let nk = $('#nienKhoa').val();
                        let hk = $('#hocKy').val();
                        let mh = $(this).val();
                        let maKhoa = $('#maKhoaFilter').val() || '';
                        if (nk && hk && mh) {
                            $.get('${pageContext.request.contextPath}/mark/get-groups', { nienKhoa: nk, hocKy: hk, maMH: mh, maKhoa: maKhoa }, function (data) {
                                let html = '<option value="">-- Nhóm --</option>';
                                data.forEach(item => {
                                    html += `<option value="\${item}">\${item}</option>`;
                                });
                                $('#nhom').html(html).prop('disabled', false);
                            });
                        }
                    });

                    function calculateTotal(row) {
                        let cc = parseFloat(row.find('input[data-field="diemCC"]').val()) || 0;
                        let gk = parseFloat(row.find('input[data-field="diemGK"]').val()) || 0;
                        let ck = parseFloat(row.find('input[data-field="diemCK"]').val()) || 0;

                        let total = (cc * 0.1) + (gk * 0.3) + (ck * 0.6);
                        let badgeClass = total >= 8.5 ? 'bg-success' : (total >= 7 ? 'bg-primary' : (total >= 5 ? 'bg-warning' : 'bg-danger'));

                        row.find('.total-mark').text(total.toFixed(1))
                            .removeClass('bg-success bg-primary bg-warning bg-danger')
                            .addClass(badgeClass + ' text-white bg-opacity-75');
                    }

                    function loadStudentListData() {
                        let nk = $('#nienKhoa').val();
                        let hk = $('#hocKy').val();
                        let mh = $('#monHoc').val();
                        let nhom = $('#nhom').val();
                        let searchVal = $('#searchMaSV').val().trim();
                        let maKhoa = $('#maKhoaFilter').val() || '';

                        if (!nk || !hk) {
                            if (!searchVal) return; // Only allow loading without filters if searching by ID
                        }

                        $('#studentListBody').html('<tr><td colspan="9" class="text-center py-5"><div class="spinner-grow text-primary" role="status"></div></td></tr>');

                        $.get('${pageContext.request.contextPath}/mark/load-students', {
                            nienKhoa: nk,
                            hocKy: hk,
                            maMH: mh || null,
                            nhom: nhom || null,
                            searchMaSV: searchVal || null,
                            maKhoa: maKhoa || null
                        }, function (data) {
                            if (!data || data.length === 0) {
                                $('#studentListBody').html('<tr><td colspan="9" class="text-center py-5 text-danger fw-medium">Không có dữ liệu sinh viên phù hợp.</td></tr>');
                                $('#summaryHeader').attr('style', 'display: none !important;');
                                return;
                            }

                            $('#sum-subject').text(searchVal ? 'Tìm kiếm: ' + searchVal : (mh ? $('#monHoc option:selected').text() : 'Tất cả môn học'));
                            $('#sum-count').text(data.length);
                            $('#summaryHeader').attr('style', 'display: flex !important;');

                            let html = '';
                            data.forEach((row, index) => {
                                // row: [0:maSV, 1:ho, 2:ten, 3:CC, 4:GK, 5:CK, 6:maLTC, 7:nhom, 8:tenMH]
                                html += `
                            <tr class="animate__animated animate__fadeIn" style="animation-delay: \${index * 0.03}s">
                                <td class="ps-4 text-muted small">\${index + 1}</td>
                                <td class="fw-bold text-dark">\${row[0]}</td>
                                <td class="fw-medium">\${row[1]} \${row[2]}</td>
                                <td class="small text-secondary">\${row[8]} <br/><span class="badge bg-light text-dark border">Nhóm \${row[7]}</span></td>
                                <td class="text-center">
                                    <input type="number" class="form-control form-control-sm mx-auto input-mark" 
                                           step="0.1" min="0" max="10" value="\${row[3] != null ? row[3] : ''}" 
                                           data-sv="\${row[0]}" data-ltc="\${row[6]}" data-field="diemCC" data-row="\${index}" data-col="0">
                                </td>
                                <td class="text-center">
                                    <input type="number" class="form-control form-control-sm mx-auto input-mark" 
                                           step="0.1" min="0" max="10" value="\${row[4] != null ? row[4] : ''}" 
                                           data-sv="\${row[0]}" data-ltc="\${row[6]}" data-field="diemGK" data-row="\${index}" data-col="1">
                                </td>
                                <td class="text-center">
                                    <input type="number" class="form-control form-control-sm mx-auto input-mark" 
                                           step="0.1" min="0" max="10" value="\${row[5] != null ? row[5] : ''}" 
                                           data-sv="\${row[0]}" data-ltc="\${row[6]}" data-field="diemCK" data-row="\${index}" data-col="2">
                                </td>
                                <td class="text-center">
                                    <span class="total-badge total-mark bg-light text-muted">0.0</span>
                                </td>
                                <td class="text-center">
                                    <button class="btn btn-sm btn-outline-primary btn-save border-0" data-sv="\${row[0]}" data-ltc="\${row[6]}">
                                        <i class="bi bi-cloud-check"></i>
                                    </button>
                                </td>
                            </tr>
                        `;
                            });
                            $('#studentListBody').html(html);

                            $('#studentListBody tr').each(function () {
                                calculateTotal($(this));
                            });
                        });
                    }

                    $('#nienKhoa, #hocKy, #monHoc, #nhom').change(function () {
                        if (this.id === 'nienKhoa' || this.id === 'hocKy') {
                            updateSubjects();
                        }
                        loadStudentListData();
                    });

                    $('#btnLoad').click(loadStudentListData);

                    $(document).on('input', '.input-mark', function () {
                        let val = $(this).val();
                        if (val < 0 || val > 10) $(this).addClass('invalid');
                        else $(this).removeClass('invalid');
                        calculateTotal($(this).closest('tr'));
                    });

                    // Keyboard Navigation
                    $(document).on('keydown', '.input-mark', function (e) {
                        let row = parseInt($(this).data('row'));
                        let col = parseInt($(this).data('col'));
                        let next;

                        switch (e.which) {
                            case 37: // Left
                                next = $(`input[data-row="\${row}"][data-col="\${col - 1}"]`);
                                break;
                            case 38: // Up
                                next = $(`input[data-row="\${row - 1}"][data-col="\${col}"]`);
                                break;
                            case 39: // Right
                                next = $(`input[data-row="\${row}"][data-col="\${col + 1}"]`);
                                break;
                            case 40: // Down
                            case 13: // Enter
                                next = $(`input[data-row="\${row + 1}"][data-col="\${col}"]`);
                                break;
                        }
                        if (next && next.length) {
                            e.preventDefault();
                            next.focus().select();
                        }
                    });

                    $(document).on('click', '.btn-save', function () {
                        let masv = $(this).data('sv');
                        let maLTC = $(this).data('ltc');
                        let row = $(this).closest('tr');
                        let diemCC = row.find('input[data-field="diemCC"]').val();
                        let diemGK = row.find('input[data-field="diemGK"]').val();
                        let diemCK = row.find('input[data-field="diemCK"]').val();

                        let btn = $(this);
                        btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span>');

                        $.post('${pageContext.request.contextPath}/mark/save-marks', {
                            maLTC: maLTC,
                            maSV: masv,
                            diemCC: diemCC || null,
                            diemGK: diemGK || null,
                            diemCK: diemCK || null
                        }, function (res) {
                            if (res.success) {
                                showToast('Đã lưu điểm cho ' + masv);
                                btn.html('<i class="bi bi-cloud-check-fill text-success"></i>').prop('disabled', false);
                            } else {
                                showToast(res.message, 'error');
                                btn.prop('disabled', false).html('<i class="bi bi-cloud-check"></i>');
                            }
                        });
                    });

                    $('#btnSaveAll').click(function () {
                        let marks = [];
                        let hasError = false;

                        $('#studentListBody tr').each(function () {
                            let inputCC = $(this).find('input[data-field="diemCC"]');
                            let masv = inputCC.data('sv');
                            let maLTC = inputCC.data('ltc');
                            let cc = inputCC.val();
                            let gk = $(this).find('input[data-field="diemGK"]').val();
                            let ck = $(this).find('input[data-field="diemCK"]').val();

                            if ($(this).find('.invalid').length > 0) hasError = true;

                            marks.push({
                                maLTC: maLTC,
                                maSV: masv,
                                diemCC: cc || null,
                                diemGK: gk || null,
                                diemCK: ck || null
                            });
                        });

                        if (hasError) {
                            showToast('Vui lòng sửa các điểm không hợp lệ (0-10)!', 'error');
                            return;
                        }

                        let btn = $(this);
                        let originalHtml = btn.html();
                        btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm me-2"></span> ĐANG LƯU...');

                        $.ajax({
                            url: '${pageContext.request.contextPath}/mark/save-all',
                            type: 'POST',
                            contentType: 'application/json',
                            data: JSON.stringify(marks),
                            success: function (res) {
                                if (res.success) {
                                    showToast(res.message);
                                    btn.removeClass('btn-primary').addClass('btn-success').html('<i class="bi bi-check-lg me-2"></i> HOÀN TẤT');
                                    setTimeout(() => {
                                        btn.removeClass('btn-success').addClass('btn-primary').html(originalHtml).prop('disabled', false);
                                    }, 3000);
                                } else {
                                    showToast(res.message, 'error');
                                    btn.html(originalHtml).prop('disabled', false);
                                }
                            },
                            error: function () {
                                showToast('Lỗi hệ thống khi lưu điểm!', 'error');
                                btn.html(originalHtml).prop('disabled', false);
                            }
                        });
                    });
                    $('#searchMaSV').on('input', function () {
                        let value = $(this).val().toLowerCase().trim();
                        $('#studentListBody tr').each(function () {
                            let masv = $(this).find('td:nth-child(2)').text().toLowerCase();
                            if (masv.indexOf(value) > -1) {
                                $(this).show();
                            } else {
                                $(this).hide();
                            }
                        });
                    });

                    $('#searchMaSV').on('keypress', function (e) {
                        if (e.which == 13) { // Enter key
                            loadStudentListData();
                        }
                    });

                    // Auto-load data on first entry
                    loadStudentListData();
                });
            </script>
        </body>

        </html>
        </script>
        </body>

        </html>