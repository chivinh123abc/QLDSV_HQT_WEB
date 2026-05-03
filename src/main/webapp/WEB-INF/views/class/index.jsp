<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <style>
        .badge-soft-primary { background-color: #e0f2fe; color: #0369a1; border-radius: 20px; font-weight: 600; padding: 6px 12px; }
        .badge-soft-secondary { background-color: #f1f5f9; color: #475569; border-radius: 20px; padding: 6px 12px; font-weight: 500; }
        .table-custom th { font-size: 0.85rem; color: #64748b; font-weight: 700; text-transform: uppercase; border-bottom: 2px solid #e2e8f0; }
        .table-custom td { vertical-align: middle; padding: 1rem 0.75rem; border-bottom: 1px solid #f1f5f9; }
        .table-custom tr:hover { background-color: #f8fafc; }
        .class-title { font-weight: 700; color: #1e293b; margin-bottom: 2px; }
        .class-subtitle { font-size: 0.8rem; color: #94a3b8; display: flex; align-items: center; gap: 4px; }
        
        .modal-header-custom { background-color: #4361ee; color: white; border-bottom: 0; }
        .modal-header-custom .btn-close { filter: invert(1) grayscale(100%) brightness(200%); }
        .toolbar-btn { font-weight: 600; padding: 6px 12px; display: inline-flex; align-items: center; gap: 6px; }
        .input-group-text { background-color: transparent; color: #4361ee; border-right: 0; }
        .form-control-icon { border-left: 0; padding-left: 0; }
        .form-control-icon:focus { box-shadow: none; border-color: #dee2e6; }
        .input-group:focus-within { box-shadow: 0 0 0 0.25rem rgba(67, 97, 238, 0.25); border-radius: 0.375rem; }
        .input-group:focus-within .input-group-text, .input-group:focus-within .form-control-icon { border-color: #4361ee; }
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
            <main id="main-content" class="app-content p-4">
                <div class="container-fluid max-w-7xl mx-auto">
                    
                    <c:if test="${not empty message}">
                        <div class="alert alert-info alert-dismissible fade show shadow-sm" role="alert">
                            <i class="bi bi-info-circle-fill me-2"></i> ${message}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <div class="card border-0 shadow-sm rounded-3">
                        <!-- HEADER -->
                        <div class="card-header bg-white border-bottom-0 pt-4 pb-0 px-4 d-flex justify-content-between align-items-center">
                            <div class="d-flex align-items-center gap-3">
                                <div class="bg-primary bg-opacity-10 text-primary p-2 rounded-3">
                                    <i class="bi bi-mortarboard-fill fs-4"></i>
                                </div>
                                <div>
                                    <h4 class="mb-0 fw-bold text-dark">Danh sách Lớp toàn trường</h4>
                                    <p class="text-muted small mb-0">Quản lý danh mục lớp và sinh viên</p>
                                </div>
                            </div>
                            <button class="btn btn-primary fw-bold" data-bs-toggle="modal" data-bs-target="#classModal" onclick="resetForm()">
                                <i class="bi bi-plus-circle-fill me-1"></i> Cập nhật Lớp
                            </button>
                        </div>

                        <!-- TOOLBAR -->
                        <div class="card-body px-4">
                            <div class="d-flex justify-content-between align-items-center mb-4 pb-3 border-bottom">
                                <div class="input-group" style="max-width: 300px;">
                                    <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-search"></i></span>
                                    <input type="text" class="form-control bg-light border-start-0 ps-0" placeholder="Tìm mã hoặc tên lớp...">
                                </div>
                                
                                <div>
                                    <select class="form-select bg-light border-0 shadow-none text-muted fw-semibold" id="khoa-filter" onchange="filterClasses()">
                                        <option value="">— Tất cả khoa —</option>
                                        <c:forEach var="k" items="${khoaList}">
                                            <option value="${k.maKhoa}">${k.tenKhoa}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="text-muted small fw-medium">
                                    <i class="bi bi-info-circle me-1"></i> Hiển thị <span class="fw-bold text-primary">${lopList.size()}</span> lớp
                                </div>
                            </div>

                            <!-- TABLE -->
                            <div class="table-responsive">
                                <table class="table table-custom">
                                    <thead>
                                        <tr>
                                            <th><i class="bi bi-hash text-primary"></i> MÃ LỚP</th>
                                            <th><i class="bi bi-book text-primary"></i> TÊN LỚP</th>
                                            <th><i class="bi bi-calendar3 text-primary"></i> KHÓA HỌC</th>
                                            <th><i class="bi bi-building text-primary"></i> KHOA</th>
                                            <th class="text-center"><i class="bi bi-lightning-charge text-primary"></i> THAO TÁC</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${lopList}">
                                            <tr>
                                                <td>
                                                    <span class="badge-soft-primary">${item.maLop}</span>
                                                </td>
                                                <td>
                                                    <div class="class-title">${item.tenLop}</div>
                                                    <div class="class-subtitle">
                                                        <i class="bi bi-people-fill"></i> Chưa rõ số sinh viên
                                                    </div>
                                                </td>
                                                <td>
                                                    <span class="badge-soft-secondary">${item.khoaHoc}</span>
                                                </td>
                                                <td>
                                                    <span class="badge-soft-secondary">${item.maKhoa}</span>
                                                </td>
                                                <td class="text-center">
                                                    <div class="d-flex gap-2 justify-content-center">
                                                        <a href="${pageContext.request.contextPath}/class?lnkEdit&maLop=${item.maLop}" class="btn btn-sm btn-outline-primary rounded-circle p-2" style="width: 32px; height: 32px; display: flex; align-items: center; justify-content: center;">
                                                            <i class="bi bi-pencil"></i>
                                                        </a>
                                                        <form action="${pageContext.request.contextPath}/class" method="post" style="display:inline;" onsubmit="return confirm('Bạn có chắc chắn muốn xóa?');">
                                                            <input type="hidden" name="maLop" value="${item.maLop}">
                                                            <button type="submit" name="btnDelete" class="btn btn-sm btn-outline-danger rounded-circle p-2" style="width: 32px; height: 32px; display: flex; align-items: center; justify-content: center;">
                                                                <i class="bi bi-trash"></i>
                                                            </button>
                                                        </form>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty lopList}">
                                            <tr>
                                                <td colspan="5" class="text-center py-5 text-muted">
                                                    <i class="bi bi-inbox fs-1 d-block mb-3"></i>
                                                    Chưa có dữ liệu lớp học
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

    <!-- MODAL NHẬP DANH MỤC LỚP -->
    <div class="modal fade" id="classModal" tabindex="-1" aria-labelledby="classModalLabel" aria-hidden="true" ${not empty lop.maLop ? 'data-bs-backdrop="static" data-bs-keyboard="false"' : ''}>
        <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
            <div class="modal-content border-0 shadow-lg">
                <div class="modal-header modal-header-custom">
                    <h5 class="modal-title d-flex align-items-center gap-2 fw-bold" id="classModalLabel">
                        <i class="bi bi-mortarboard-fill"></i> Nhập danh mục Lớp
                        <span class="badge bg-white text-primary ms-2 fs-6">Chế độ: ${not empty lop.maLop ? 'Sửa' : 'Thêm'}</span>
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                
                <form action="${pageContext.request.contextPath}/class" method="post">
                    <div class="modal-body p-4 bg-light">
                        
                        <!-- TOOLBAR ACTION -->
                        <div class="d-flex flex-wrap gap-2 mb-4 pb-3 border-bottom">
                            <button type="submit" name="${not empty lop.maLop ? 'btnUpdate' : 'btnInsert'}" class="btn btn-success toolbar-btn">
                                <i class="bi bi-save"></i> Ghi
                            </button>
                            <button type="reset" class="btn btn-warning toolbar-btn text-dark">
                                <i class="bi bi-arrow-counterclockwise"></i> Phục hồi
                            </button>
                            <button type="button" class="btn btn-secondary toolbar-btn" data-bs-dismiss="modal">
                                <i class="bi bi-x-circle"></i> Đóng
                            </button>
                        </div>

                        <!-- INPUT FORM -->
                        <div class="row g-3 bg-white p-3 rounded-3 shadow-sm mb-4">
                            <div class="col-md-6">
                                <label class="form-label fw-bold small text-muted">MÃ LỚP <span class="text-danger">*</span></label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-hash"></i></span>
                                    <input type="text" class="form-control form-control-icon" id="modal_maLop" name="maLop" value="${lop.maLop}" placeholder="VD: D15CQCN01-N" required ${not empty lop.maLop ? 'readonly' : ''}>
                                </div>
                            </div>
                            
                            <div class="col-md-6">
                                <label class="form-label fw-bold small text-muted">TÊN LỚP <span class="text-danger">*</span></label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-mortarboard"></i></span>
                                    <input type="text" class="form-control form-control-icon" name="tenLop" value="${lop.tenLop}" placeholder="VD: Công nghệ thông tin 01" required>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-bold small text-muted">KHÓA HỌC <span class="text-danger">*</span></label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-calendar3"></i></span>
                                    <input type="number" class="form-control form-control-icon" name="khoaHoc" value="${lop.khoaHoc}" placeholder="VD: 2015" required>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-bold small text-muted">KHOA <span class="text-danger">*</span></label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-building"></i></span>
                                    <select class="form-select form-control-icon" name="maKhoa" required>
                                        <option value="" disabled ${empty lop.maKhoa ? 'selected' : ''}>— Chọn khoa —</option>
                                        <c:forEach var="k" items="${khoaList}">
                                            <option value="${k.maKhoa}" ${lop.maKhoa == k.maKhoa ? 'selected' : ''}>${k.tenKhoa}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <!-- ALERT INFO -->
                        <div class="alert alert-primary bg-primary bg-opacity-10 border-0 d-flex gap-3 mb-4 rounded-3 shadow-sm">
                            <i class="bi bi-info-circle-fill text-primary fs-3"></i>
                            <div>
                                <h6 class="alert-heading fw-bold text-primary mb-1">Ghi chú</h6>
                                <p class="mb-0 small text-dark">Khi cập nhật lớp, hệ thống sẽ kiểm tra tính duy nhất của mã lớp. Bạn có thể xem danh sách lớp hiện có ở bảng bên dưới để tránh trùng lặp.</p>
                            </div>
                        </div>

                        <!-- MINI TABLE -->
                        <h6 class="fw-bold text-primary mb-3 d-flex align-items-center gap-2">
                            <i class="bi bi-list-ul"></i> Danh sách lớp hiện có
                        </h6>
                        <div class="table-responsive bg-white rounded-3 shadow-sm border" style="max-height: 250px; overflow-y: auto;">
                            <table class="table table-custom table-sm mb-0">
                                <thead class="table-light sticky-top">
                                    <tr>
                                        <th><i class="bi bi-hash"></i> MÃ LỚP</th>
                                        <th><i class="bi bi-mortarboard"></i> TÊN LỚP</th>
                                        <th><i class="bi bi-calendar3"></i> KHÓA</th>
                                        <th><i class="bi bi-building"></i> KHOA</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${lopList}">
                                        <tr class="${item.maLop == lop.maLop ? 'table-primary' : ''}">
                                            <td><span class="badge-soft-primary" style="font-size: 0.75rem">${item.maLop}</span></td>
                                            <td class="fw-bold text-dark" style="font-size: 0.85rem">${item.tenLop}</td>
                                            <td><span class="badge-soft-secondary" style="font-size: 0.75rem">${item.khoaHoc}</span></td>
                                            <td><span class="badge-soft-secondary" style="font-size: 0.75rem">${item.maKhoa}</span></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Hiển thị Modal nếu đang ở chế độ Sửa (có tham số lnkEdit)
        document.addEventListener('DOMContentLoaded', function() {
            if ("${not empty param.lnkEdit}" === "true") {
                var myModal = new bootstrap.Modal(document.getElementById('classModal'));
                myModal.show();
            }
        });
        
        function resetForm() {
            // Remove readonly and clear value when clicking "Thêm mới"
            document.getElementById('modal_maLop').readOnly = false;
            document.getElementById('modal_maLop').value = '';
            document.querySelector('#classModalLabel span').innerText = 'Chế độ: Thêm';
            document.querySelector('button[name="btnUpdate"]')?.setAttribute('name', 'btnInsert');
        }

        function filterClasses() {
            const selectedKhoa = document.getElementById('khoa-filter').value;
            const rows = document.querySelectorAll('.table-custom tbody tr');
            
            rows.forEach(row => {
                const khoaCell = row.cells[3]; // KHOA column
                if (!khoaCell) return;
                
                const khoaBadge = khoaCell.querySelector('.badge-soft-secondary');
                if (!khoaBadge) return;
                
                const maKhoa = khoaBadge.innerText.trim();
                if (!selectedKhoa || maKhoa === selectedKhoa) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        }
    </script>
</body>
</html>
