<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Đăng ký & Nhập điểm - QLDSV_HTC_WEB</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <style>
        .badge-soft-primary { background-color: #e0f2fe; color: #0369a1; border-radius: 20px; font-weight: 600; padding: 6px 12px; }
        .badge-soft-info { background-color: #ecfeff; color: #0891b2; border-radius: 20px; padding: 6px 12px; }
        .score-box { width: 60px; text-align: center; font-weight: 700; border-radius: 8px; padding: 4px; }
        .score-cc { background-color: #fef9c3; color: #854d0e; }
        .score-gk { background-color: #ffedd5; color: #9a3412; }
        .score-ck { background-color: #fce7f3; color: #9d174d; }
        .table-custom th { font-size: 0.85rem; color: #64748b; font-weight: 700; text-transform: uppercase; border-bottom: 2px solid #e2e8f0; }
        .table-custom td { vertical-align: middle; padding: 1rem 0.75rem; border-bottom: 1px solid #f1f5f9; }
        .modal-header-custom { background-color: #4361ee; color: white; border-bottom: 0; }
        .modal-header-custom .btn-close { filter: invert(1) grayscale(100%) brightness(200%); }
        .toolbar-btn { font-weight: 600; padding: 6px 12px; display: inline-flex; align-items: center; gap: 6px; }
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
                                    <i class="bi bi-pencil-square fs-4"></i>
                                </div>
                                <div>
                                    <h4 class="mb-0 fw-bold text-dark">Đăng ký & Nhập điểm</h4>
                                    <p class="text-muted small mb-0">Xử lý đăng ký môn học và quản lý điểm số</p>
                                </div>
                            </div>
                            <button class="btn btn-primary fw-bold" data-bs-toggle="modal" data-bs-target="#regModal" onclick="resetForm()">
                                <i class="bi bi-plus-circle-fill me-1"></i> Đăng Ký Mới
                            </button>
                        </div>

                        <div class="card-body px-4">
                            <!-- SEARCH/FILTER -->
                            <div class="row g-3 mb-4 pb-3 border-bottom align-items-center">
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-search"></i></span>
                                        <input type="text" class="form-control bg-light border-start-0 ps-0 text-muted fw-semibold shadow-none" placeholder="Nhập mã SV để tìm kiếm...">
                                    </div>
                                </div>
                                <div class="col-md-8 text-end">
                                    <div class="text-muted small fw-medium">
                                        <i class="bi bi-info-circle me-1"></i> Hiển thị <span class="fw-bold text-primary">${dangKyList.size()}</span> bản ghi
                                    </div>
                                </div>
                            </div>

                            <!-- TABLE -->
                            <div class="table-responsive">
                                <table class="table table-custom">
                                    <thead>
                                        <tr>
                                            <th>Mã SV</th>
                                            <th>Mã LTC</th>
                                            <th>Điểm CC</th>
                                            <th>Điểm GK</th>
                                            <th>Điểm CK</th>
                                            <th>Trạng Thái</th>
                                            <th class="text-center">Thao Tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="dk" items="${dangKyList}">
                                            <tr>
                                                <td><span class="badge-soft-primary">${dk.maSV}</span></td>
                                                <td><span class="badge-soft-info">LTC: ${dk.maLTC}</span></td>
                                                <td><div class="score-box score-cc">${dk.diemCC}</div></td>
                                                <td><div class="score-box score-gk">${dk.diemGK}</div></td>
                                                <td><div class="score-box score-ck">${dk.diemCK}</div></td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${dk.huyDangKy}">
                                                            <span class="badge bg-danger">Đã hủy</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-success">Đã đăng ký</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="text-center">
                                                    <div class="d-flex gap-2 justify-content-center">
                                                        <a href="${pageContext.request.contextPath}/registration?lnkEdit&maLTC=${dk.maLTC}&maSV=${dk.maSV}" class="btn btn-sm btn-outline-primary rounded-circle p-2" style="width: 32px; height: 32px; display: flex; align-items: center; justify-content: center;">
                                                            <i class="bi bi-pencil"></i>
                                                        </a>
                                                        <form action="${pageContext.request.contextPath}/registration" method="post" style="display:inline;" onsubmit="return confirm('Xác nhận xóa đăng ký?');">
                                                            <input type="hidden" name="maLTC" value="${dk.maLTC}">
                                                            <input type="hidden" name="maSV" value="${dk.maSV}">
                                                            <button type="submit" name="btnDelete" class="btn btn-sm btn-outline-danger rounded-circle p-2" style="width: 32px; height: 32px; display: flex; align-items: center; justify-content: center;">
                                                                <i class="bi bi-trash"></i>
                                                            </button>
                                                        </form>
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
    <div class="modal fade" id="regModal" tabindex="-1" ${not empty dk.maSV ? 'data-bs-backdrop="static"' : ''}>
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg">
                <div class="modal-header modal-header-custom">
                    <h5 class="modal-title fw-bold"><i class="bi bi-pencil-square me-2"></i> Thông tin Đăng ký & Điểm</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="${pageContext.request.contextPath}/registration" method="post">
                    <div class="modal-body p-4 bg-light">
                        <div class="d-flex gap-2 mb-4">
                            <button type="submit" name="${not empty dk.maSV ? 'btnUpdate' : 'btnInsert'}" class="btn btn-success toolbar-btn"><i class="bi bi-save"></i> Ghi</button>
                            <button type="reset" class="btn btn-warning toolbar-btn text-dark"><i class="bi bi-arrow-counterclockwise"></i> Phục hồi</button>
                            <button type="button" class="btn btn-secondary toolbar-btn" data-bs-dismiss="modal"><i class="bi bi-x-circle"></i> Đóng</button>
                        </div>

                        <div class="row g-3 bg-white p-3 rounded-3 shadow-sm">
                            <div class="col-md-6">
                                <label class="form-label fw-bold small">Mã Lớp Tín Chỉ <span class="text-danger">*</span></label>
                                <input type="number" class="form-control" name="maLTC" value="${dk.maLTC}" required ${not empty dk.maSV ? 'readonly' : ''}>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold small">Mã Sinh Viên <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" name="maSV" value="${dk.maSV}" required ${not empty dk.maSV ? 'readonly' : ''}>
                            </div>
                            
                            <div class="col-12 mt-4"><hr></div>
                            <div class="col-12"><h6 class="fw-bold text-primary"><i class="bi bi-award"></i> Nhập điểm số</h6></div>

                            <div class="col-md-4">
                                <label class="form-label fw-bold small">Điểm CC</label>
                                <input type="number" step="0.1" class="form-control score-cc" name="diemCC" value="${dk.diemCC}" min="0" max="10">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label fw-bold small">Điểm GK</label>
                                <input type="number" step="0.1" class="form-control score-gk" name="diemGK" value="${dk.diemGK}" min="0" max="10">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label fw-bold small">Điểm CK</label>
                                <input type="number" step="0.1" class="form-control score-ck" name="diemCK" value="${dk.diemCK}" min="0" max="10">
                            </div>

                            <div class="col-12 mt-3">
                                <div class="form-check form-switch">
                                    <input class="form-check-input" type="checkbox" id="huyDangKy" name="huyDangKy" value="true" ${dk.huyDangKy ? 'checked' : ''}>
                                    <label class="form-check-label fw-bold text-danger" for="huyDangKy">Hủy đăng ký (Xóa tên khỏi lớp)</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            if ("${not empty param.lnkEdit}" === "true") {
                new bootstrap.Modal(document.getElementById('regModal')).show();
            }
        });
        function resetForm() {
            // Logic to clear form when inserting
        }
    </script>
</body>
</html>
