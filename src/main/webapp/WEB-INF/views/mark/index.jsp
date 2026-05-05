<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Nhập điểm - QLDSV_HTC_WEB</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <style>
        .filter-section {
            background: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.05);
            margin-bottom: 25px;
        }
        .mark-table th {
            background-color: #f8f9fa;
            font-weight: 600;
        }
        .input-mark {
            width: 80px;
            text-align: center;
        }
        .status-cell {
            font-size: 0.85rem;
            min-width: 120px;
        }
    </style>
</head>
<body>
    <div class="app-layout">
        <jsp:include page="/WEB-INF/views/shared/sidebar.jsp" />

        <div class="app-main">
            <jsp:include page="/WEB-INF/views/shared/header.jsp" />

            <main id="main-content" class="app-content">
                <div class="container-fluid p-4">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h2><i class="bi bi-check2-square text-primary"></i> Nhập điểm sinh viên</h2>
                    </div>

                    <div class="filter-section">
                        <div class="row g-3">
                            <div class="col-md-3">
                                <label class="form-label">Niên khóa</label>
                                <select class="form-select" id="nienKhoa">
                                    <option value="">-- Chọn niên khóa --</option>
                                    <c:forEach var="nk" items="${nienKhoaList}">
                                        <option value="${nk}">${nk}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <label class="form-label">Học kỳ</label>
                                <select class="form-select" id="hocKy">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Môn học</label>
                                <select class="form-select" id="monHoc" disabled>
                                    <option value="">-- Chọn môn học --</option>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <label class="form-label">Nhóm</label>
                                <select class="form-select" id="nhom" disabled>
                                    <option value="">-- Nhóm --</option>
                                </select>
                            </div>
                            <div class="col-md-1 d-flex align-items-end">
                                <button type="button" class="btn btn-primary w-100" id="btnLoad">
                                    <i class="bi bi-search"></i>
                                </button>
                            </div>
                        </div>
                    </div>

                    <div class="card shadow-sm border-0">
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0 mark-table" id="studentMarkTable">
                                    <thead>
                                        <tr>
                                            <th class="ps-4">STT</th>
                                            <th>Mã SV</th>
                                            <th>Họ và Tên</th>
                                            <th class="text-center">Điểm CC</th>
                                            <th class="text-center">Điểm GK</th>
                                            <th class="text-center">Điểm CK</th>
                                            <th class="text-center">Trạng thái</th>
                                        </tr>
                                    </thead>
                                    <tbody id="studentListBody">
                                        <tr>
                                            <td colspan="7" class="text-center py-5 text-muted">
                                                Vui lòng chọn thông tin lớp tín chỉ để hiển thị danh sách sinh viên.
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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        $(document).ready(function() {
            var currentMaLTC = 0;

            function updateSubjects() {
                let nk = $('#nienKhoa').val();
                let hk = $('#hocKy').val();
                if (nk && hk) {
                    $.get('${pageContext.request.contextPath}/mark/get-subjects', { nienKhoa: nk, hocKy: hk }, function(data) {
                        let html = '<option value="">-- Chọn môn học --</option>';
                        data.forEach(item => {
                            html += `<option value="${item[0]}">${item[1]}</option>`;
                        });
                        $('#monHoc').html(html).prop('disabled', false);
                        $('#nhom').html('<option value="">-- Nhóm --</option>').prop('disabled', true);
                    });
                }
            }

            $('#nienKhoa, #hocKy').change(updateSubjects);

            $('#monHoc').change(function() {
                let nk = $('#nienKhoa').val();
                let hk = $('#hocKy').val();
                let mh = $(this).val();
                if (nk && hk && mh) {
                    $.get('${pageContext.request.contextPath}/mark/get-groups', { nienKhoa: nk, hocKy: hk, maMH: mh }, function(data) {
                        let html = '<option value="">-- Nhóm --</option>';
                        data.forEach(item => {
                            html += `<option value="${item}">${item}</option>`;
                        });
                        $('#nhom').html(html).prop('disabled', false);
                    });
                }
            });

            $('#btnLoad').click(function() {
                let nk = $('#nienKhoa').val();
                let hk = $('#hocKy').val();
                let mh = $('#monHoc').val();
                let nhom = $('#nhom').val();

                if (!nk || !hk || !mh || !nhom) {
                    alert('Vui lòng chọn đầy đủ thông tin!');
                    return;
                }

                $('#studentListBody').html('<tr><td colspan="7" class="text-center py-5"><div class="spinner-border text-primary" role="status"></div></td></tr>');

                $.get('${pageContext.request.contextPath}/mark/load-students', { nienKhoa: nk, hocKy: hk, maMH: mh, nhom: nhom }, function(data) {
                    if (!data || data.length === 0) {
                        $('#studentListBody').html('<tr><td colspan="7" class="text-center py-5 text-danger">Không có sinh viên nào đăng ký lớp này.</td></tr>');
                        return;
                    }

                    currentMaLTC = data[0][6];
                    let html = '';
                    data.forEach((row, index) => {
                        html += `
                            <tr>
                                <td class="ps-4">${index + 1}</td>
                                <td class="fw-bold text-primary">${row[0]}</td>
                                <td>${row[1]} ${row[2]}</td>
                                <td class="text-center">
                                    <input type="number" class="form-control form-control-sm mx-auto input-mark" 
                                           step="0.1" min="0" max="10" value="${row[3] != null ? row[3] : ''}" 
                                           data-sv="${row[0]}" data-field="diemCC">
                                </td>
                                <td class="text-center">
                                    <input type="number" class="form-control form-control-sm mx-auto input-mark" 
                                           step="0.1" min="0" max="10" value="${row[4] != null ? row[4] : ''}" 
                                           data-sv="${row[0]}" data-field="diemGK">
                                </td>
                                <td class="text-center">
                                    <input type="number" class="form-control form-control-sm mx-auto input-mark" 
                                           step="0.1" min="0" max="10" value="${row[5] != null ? row[5] : ''}" 
                                           data-sv="${row[0]}" data-field="diemCK">
                                </td>
                                <td class="text-center status-cell" id="status-${row[0]}">
                                    <button class="btn btn-sm btn-outline-success btn-save" data-sv="${row[0]}">
                                        <i class="bi bi-save"></i> Lưu
                                    </button>
                                </td>
                            </tr>
                        `;
                    });
                    $('#studentListBody').html(html);
                });
            });

            $(document).on('click', '.btn-save', function() {
                let masv = $(this).data('sv');
                let row = $(this).closest('tr');
                let diemCC = row.find('input[data-field="diemCC"]').val();
                let diemGK = row.find('input[data-field="diemGK"]').val();
                let diemCK = row.find('input[data-field="diemCK"]').val();
                
                let btn = $(this);
                btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span>');

                $.post('${pageContext.request.contextPath}/mark/save-marks', {
                    maLTC: currentMaLTC,
                    maSV: masv,
                    diemCC: diemCC || null,
                    diemGK: diemGK || null,
                    diemCK: diemCK || null
                }, function(res) {
                    if (res.success) {
                        btn.removeClass('btn-outline-success').addClass('btn-success').html('<i class="bi bi-check"></i> Đã lưu');
                        setTimeout(() => {
                            btn.removeClass('btn-success').addClass('btn-outline-success').html('<i class="bi bi-save"></i> Lưu').prop('disabled', false);
                        }, 2000);
                    } else {
                        alert(res.message);
                        btn.prop('disabled', false).html('<i class="bi bi-save"></i> Lưu');
                    }
                });
            });
        });
    </script>
</body>
</html>
