<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Báo cáo - QLDSV_HTC_WEB</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <style>
        .report-card {
            transition: transform 0.2s;
            cursor: pointer;
            border: 2px solid transparent;
        }
        .report-card:hover {
            transform: translateY(-5px);
            border-color: #0d6efd;
        }
        .report-card.active {
            border-color: #0d6efd;
            background-color: #f8fbff;
        }
        .filter-pane {
            background: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.05);
            margin-bottom: 20px;
            display: none;
        }
        .report-result {
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.05);
            overflow: hidden;
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
                    <h2 class="mb-4"><i class="bi bi-file-earmark-bar-graph text-primary"></i> Hệ thống báo cáo</h2>

                    <div class="row g-4 mb-4">
                        <div class="col-md-6">
                            <div class="card h-100 report-card" id="cardSummary">
                                <div class="card-body text-center py-4">
                                    <i class="bi bi-grid-3x3-gap fs-1 text-primary mb-3"></i>
                                    <h4>Bảng điểm tổng kết</h4>
                                    <p class="text-muted">Xem điểm tổng kết của tất cả sinh viên trong một lớp.</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="card h-100 report-card" id="cardStudents">
                                <div class="card-body text-center py-4">
                                    <i class="bi bi-people fs-1 text-success mb-3"></i>
                                    <h4>Danh sách sinh viên lớp tín chỉ</h4>
                                    <p class="text-muted">Xem danh sách sinh viên đăng ký một lớp tín chỉ cụ thể.</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Filters for Summary Marks -->
                    <div class="filter-pane" id="filterSummary">
                        <div class="row g-3 align-items-end">
                            <div class="col-md-4">
                                <label class="form-label">Chọn lớp</label>
                                <select class="form-select" id="selLop">
                                    <c:forEach var="lop" items="${lopList}">
                                        <option value="${lop.maLop}">${lop.maLop} - ${lop.tenLop}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <button class="btn btn-primary w-100" id="btnGenSummary">
                                    <i class="bi bi-play-fill"></i> Tạo báo cáo
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- Filters for Credit Class Students -->
                    <div class="filter-pane" id="filterStudents">
                        <div class="row g-3">
                            <div class="col-md-3">
                                <label class="form-label">Niên khóa</label>
                                <select class="form-select" id="nk">
                                    <c:forEach var="nk" items="${nienKhoaList}">
                                        <option value="${nk}">${nk}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <label class="form-label">Học kỳ</label>
                                <select class="form-select" id="hk">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Môn học</label>
                                <select class="form-select" id="mh">
                                    <option value="">-- Tải môn học... --</option>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <label class="form-label">Nhóm</label>
                                <select class="form-select" id="nhom">
                                    <option value="">-- Nhóm --</option>
                                </select>
                            </div>
                            <div class="col-md-1 d-flex align-items-end">
                                <button class="btn btn-success w-100" id="btnGenStudents">
                                    <i class="bi bi-play-fill"></i>
                                </button>
                            </div>
                        </div>
                    </div>

                    <div class="report-result d-none" id="resultArea">
                        <div class="p-3 border-bottom d-flex justify-content-between align-items-center">
                            <h5 class="mb-0" id="resultTitle">Kết quả báo cáo</h5>
                            <button class="btn btn-sm btn-outline-secondary" onclick="window.print()">
                                <i class="bi bi-printer"></i> In báo cáo
                            </button>
                        </div>
                        <div class="table-responsive p-0">
                            <table class="table table-bordered table-striped mb-0" id="reportTable">
                                <thead></thead>
                                <tbody></tbody>
                            </table>
                        </div>
                    </div>

                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        $(document).ready(function() {
            // Tab switching
            $('.report-card').click(function() {
                $('.report-card').removeClass('active');
                $(this).addClass('active');
                $('.filter-pane').hide();
                $('#resultArea').addClass('d-none');
                
                if (this.id === 'cardSummary') {
                    $('#filterSummary').fadeIn();
                } else {
                    $('#filterStudents').fadeIn();
                    updateSubjects();
                }
            });

            // Summary Mark Sheet logic
            $('#btnGenSummary').click(function() {
                let maLop = $('#selLop').val();
                $('#resultArea').addClass('d-none');
                
                $.get('${pageContext.request.contextPath}/report/summary-marks', { maLop: maLop }, function(res) {
                    if (res.success) {
                        $('#resultArea').removeClass('d-none');
                        $('#resultTitle').text('Bảng điểm tổng kết - Lớp ' + maLop);
                        
                        // Build header
                        let headHtml = '<tr>';
                        res.columns.forEach(col => {
                            headHtml += `<th class="bg-light">${col}</th>`;
                        });
                        headHtml += '</tr>';
                        $('#reportTable thead').html(headHtml);

                        // Build body
                        let bodyHtml = '';
                        res.data.forEach(row => {
                            bodyHtml += '<tr>';
                            res.columns.forEach(col => {
                                let val = row[col];
                                bodyHtml += `<td>${val != null ? val : '-'}</td>`;
                            });
                            bodyHtml += '</tr>';
                        });
                        $('#reportTable tbody').html(bodyHtml);
                    } else {
                        alert(res.message);
                    }
                });
            });

            // Credit Class Students logic
            function updateSubjects() {
                let nkValue = $('#nk').val();
                let hkValue = $('#hk').val();
                $.get('${pageContext.request.contextPath}/mark/get-subjects', { nienKhoa: nkValue, hocKy: hkValue }, function(data) {
                    let html = '<option value="">-- Chọn môn học --</option>';
                    data.forEach(item => html += `<option value="${item[0]}">${item[1]}</option>`);
                    $('#mh').html(html);
                    $('#nhom').html('<option value="">-- Nhóm --</option>');
                });
            }

            $('#nk, #hk').change(updateSubjects);

            $('#mh').change(function() {
                $.get('${pageContext.request.contextPath}/mark/get-groups', { 
                    nienKhoa: $('#nk').val(), 
                    hocKy: $('#hk').val(), 
                    maMH: $(this).val() 
                }, function(data) {
                    let html = '<option value="">-- Nhóm --</option>';
                    data.forEach(item => html += `<option value="${item}">${item}</option>`);
                    $('#nhom').html(html);
                });
            });

            $('#btnGenStudents').click(function() {
                let params = {
                    nienKhoa: $('#nk').val(),
                    hocKy: $('#hk').val(),
                    maMH: $('#mh').val(),
                    nhom: $('#nhom').val()
                };
                
                if (!params.maMH || !params.nhom) {
                    alert('Vui lòng chọn đầy đủ thông tin!');
                    return;
                }

                $.get('${pageContext.request.contextPath}/report/credit-class-students', params, function(res) {
                    if (res.success) {
                        $('#resultArea').removeClass('d-none');
                        $('#resultTitle').text('Danh sách sinh viên lớp tín chỉ');
                        
                        $('#reportTable thead').html(`
                            <tr>
                                <th>MASV</th>
                                <th>Họ</th>
                                <th>Tên</th>
                                <th>Phái</th>
                                <th>Mã Lớp</th>
                            </tr>
                        `);

                        let bodyHtml = '';
                        res.data.forEach(row => {
                            bodyHtml += `
                                <tr>
                                    <td>${row.MASV}</td>
                                    <td>${row.HO}</td>
                                    <td>${row.TEN}</td>
                                    <td>${row.PHAI}</td>
                                    <td>${row.MALOP}</td>
                                </tr>
                            `;
                        });
                        $('#reportTable tbody').html(bodyHtml);
                    }
                });
            });
        });
    </script>
</body>
</html>
