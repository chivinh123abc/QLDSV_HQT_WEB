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
        .table-custom tr:hover { background-color: #f8fafc; }
        .subject-title { font-weight: 700; color: #1e293b; margin-bottom: 2px; }
        .subject-subtitle { font-size: 0.8rem; color: #94a3b8; display: flex; align-items: center; gap: 4px; }
        
        .modal-header-custom { background-color: #4361ee; color: white; border-bottom: 0; }
        .modal-header-custom .btn-close { filter: invert(1) grayscale(100%) brightness(200%); }
        .toolbar-btn { font-weight: 600; padding: 6px 12px; display: inline-flex; align-items: center; gap: 6px; }
        .input-group-text { background-color: transparent; color: #4361ee; border-right: 0; }
        .form-control-icon { border-left: 0; padding-left: 0; }
        .form-control-icon:focus { box-shadow: none; border-color: #dee2e6; }
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
                                    <i class="bi bi-journal-bookmark-fill fs-4"></i>
                                </div>
                                <div>
                                    <h4 class="mb-0 fw-bold text-dark">Danh sách Môn Học</h4>
                                    <p class="text-muted small mb-0">Quản lý chương trình và định mức môn học</p>
                                </div>
                            </div>
                            <button class="btn btn-primary fw-bold" data-bs-toggle="modal" data-bs-target="#subjectModal" onclick="resetForm()">
                                <i class="bi bi-plus-circle-fill me-1"></i> Thêm Môn Học
                            </button>
                        </div>

                        <!-- TOOLBAR -->
                        <div class="card-body px-4">
                            <div class="d-flex justify-content-between align-items-center mb-4 pb-3 border-bottom">
                                <div class="input-group" style="max-width: 300px;">
                                    <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-search"></i></span>
                                    <input type="text" class="form-control bg-light border-start-0 ps-0" placeholder="Tìm mã hoặc tên môn...">
                                </div>
                                
                                <div class="text-muted small fw-medium">
                                    <i class="bi bi-info-circle me-1"></i> Tổng cộng <span class="fw-bold text-primary">${monHocList.size()}</span> môn học
                                </div>
                            </div>

                            <!-- TABLE -->
                            <div class="table-responsive">
                                <table class="table table-custom">
                                    <thead>
                                        <tr>
                                            <th><i class="bi bi-hash text-primary"></i> MÃ MH</th>
                                            <th><i class="bi bi-journal-text text-primary"></i> TÊN MÔN HỌC</th>
                                            <th><i class="bi bi-clock text-primary"></i> TIẾT LT</th>
                                            <th><i class="bi bi-clock-history text-primary"></i> TIẾT TH</th>
                                            <th class="text-center"><i class="bi bi-lightning-charge text-primary"></i> THAO TÁC</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${monHocList}">
                                            <tr>
                                                <td>
                                                    <span class="badge-soft-primary">${item.maMH}</span>
                                                </td>
                                                <td>
                                                    <div class="subject-title">${item.tenMH}</div>
                                                    <div class="subject-subtitle">
                                                        <i class="bi bi-info-circle"></i> Đã chuẩn hóa
                                                    </div>
                                                </td>
                                                <td>
                                                    <span class="badge-soft-secondary">${item.soTietLT} tiết</span>
                                                </td>
                                                <td>
                                                    <span class="badge-soft-secondary">${item.soTietTH} tiết</span>
                                                </td>
                                                <td class="text-center">
                                                    <div class="d-flex gap-2 justify-content-center">
                                                        <a href="${pageContext.request.contextPath}/subject?lnkEdit&maMH=${item.maMH}" class="btn btn-sm btn-outline-primary rounded-circle p-2" style="width: 32px; height: 32px; display: flex; align-items: center; justify-content: center;">
                                                            <i class="bi bi-pencil"></i>
                                                        </a>
                                                        <form action="${pageContext.request.contextPath}/subject" method="post" style="display:inline;" onsubmit="return confirm('Bạn có chắc chắn muốn xóa môn học này?');">
                                                            <input type="hidden" name="maMH" value="${item.maMH}">
                                                            <button type="submit" name="btnDelete" class="btn btn-sm btn-outline-danger rounded-circle p-2" style="width: 32px; height: 32px; display: flex; align-items: center; justify-content: center;">
                                                                <i class="bi bi-trash"></i>
                                                            </button>
                                                        </form>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty monHocList}">
                                            <tr>
                                                <td colspan="5" class="text-center py-5 text-muted">
                                                    <i class="bi bi-inbox fs-1 d-block mb-3"></i>
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

    <!-- MODAL NHẬP MÔN HỌC -->
    <div class="modal fade" id="subjectModal" tabindex="-1" aria-labelledby="subjectModalLabel" aria-hidden="true" ${not empty monHoc.maMH ? 'data-bs-backdrop="static" data-bs-keyboard="false"' : ''}>
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg">
                <div class="modal-header modal-header-custom">
                    <h5 class="modal-title d-flex align-items-center gap-2 fw-bold" id="subjectModalLabel">
                        <i class="bi bi-journal-plus"></i> Nhập danh mục Môn Học
                        <span class="badge bg-white text-primary ms-2 fs-6">Chế độ: ${not empty monHoc.maMH ? 'Sửa' : 'Thêm'}</span>
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                
                <form action="${pageContext.request.contextPath}/subject" method="post">
                    <div class="modal-body p-4 bg-light">
                        
                        <!-- TOOLBAR -->
                        <div class="d-flex flex-wrap gap-2 mb-4 pb-3 border-bottom">
                            <button type="submit" name="${not empty monHoc.maMH ? 'btnUpdate' : 'btnInsert'}" class="btn btn-success toolbar-btn">
                                <i class="bi bi-save"></i> Ghi
                            </button>
                            <button type="reset" class="btn btn-warning toolbar-btn text-dark">
                                <i class="bi bi-arrow-counterclockwise"></i> Phục hồi
                            </button>
                            <button type="button" class="btn btn-secondary toolbar-btn" data-bs-dismiss="modal">
                                <i class="bi bi-x-circle"></i> Đóng
                            </button>
                        </div>

                        <!-- INPUT -->
                        <div class="row g-3 bg-white p-3 rounded-3 shadow-sm">
                            <div class="col-md-6">
                                <label class="form-label fw-bold small text-muted">MÃ MÔN HỌC <span class="text-danger">*</span></label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-hash"></i></span>
                                    <input type="text" class="form-control form-control-icon" id="modal_maMH" name="maMH" value="${monHoc.maMH}" placeholder="VD: CTDL" required ${not empty monHoc.maMH ? 'readonly' : ''}>
                                </div>
                            </div>
                            
                            <div class="col-md-6">
                                <label class="form-label fw-bold small text-muted">TÊN MÔN HỌC <span class="text-danger">*</span></label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-journal-text"></i></span>
                                    <input type="text" class="form-control form-control-icon" name="tenMH" value="${monHoc.tenMH}" placeholder="VD: Cấu trúc dữ liệu" required>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-bold small text-muted">SỐ TIẾT LÝ THUYẾT <span class="text-danger">*</span></label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-clock"></i></span>
                                    <input type="number" class="form-control form-control-icon" name="soTietLT" value="${monHoc.soTietLT}" placeholder="VD: 30" required>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-bold small text-muted">SỐ TIẾT THỰC HÀNH <span class="text-danger">*</span></label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-clock-history"></i></span>
                                    <input type="number" class="form-control form-control-icon" name="soTietTH" value="${monHoc.soTietTH}" placeholder="VD: 30" required>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            if ("${not empty param.lnkEdit}" === "true") {
                var myModal = new bootstrap.Modal(document.getElementById('subjectModal'));
                myModal.show();
            }
        });

        function resetForm() {
            document.getElementById('modal_maMH').readOnly = false;
            document.getElementById('modal_maMH').value = '';
            document.querySelector('#subjectModalLabel span').innerText = 'Chế độ: Thêm';
            document.querySelector('button[name="btnUpdate"]')?.setAttribute('name', 'btnInsert');
        }
    </script>
</body>
</html>
