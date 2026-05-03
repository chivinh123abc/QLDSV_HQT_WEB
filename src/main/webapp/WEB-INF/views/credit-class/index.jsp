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
        .table-custom tr:hover { background-color: #f8fafc; }
        .ltc-title { font-weight: 700; color: #1e293b; margin-bottom: 2px; }
        .ltc-subtitle { font-size: 0.8rem; color: #94a3b8; display: flex; align-items: center; gap: 4px; }
        
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
                                    <i class="bi bi-layers-fill fs-4"></i>
                                </div>
                                <div>
                                    <h4 class="mb-0 fw-bold text-dark">Danh sách Lớp Tín Chỉ</h4>
                                    <p class="text-muted small mb-0">Quản lý mở lớp và điều phối giảng dạy</p>
                                </div>
                            </div>
                            <button class="btn btn-primary fw-bold" data-bs-toggle="modal" data-bs-target="#ltcModal" onclick="resetForm()">
                                <i class="bi bi-plus-circle-fill me-1"></i> Mở Lớp Tín Chỉ
                            </button>
                        </div>

                        <!-- TOOLBAR -->
                        <div class="card-body px-4">
                            <div class="row g-3 mb-4 pb-3 border-bottom align-items-center">
                                <div class="col-md-3">
                                    <div class="input-group">
                                        <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-calendar-range"></i></span>
                                        <select class="form-select bg-light border-start-0 ps-0 text-muted fw-semibold shadow-none">
                                            <option value="">— Niên khóa —</option>
                                            <option>2023-2024</option>
                                            <option>2022-2023</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-2">
                                    <select class="form-select bg-light border-0 shadow-none text-muted fw-semibold">
                                        <option value="">— Học kỳ —</option>
                                        <option>1</option>
                                        <option>2</option>
                                        <option>3</option>
                                    </select>
                                </div>
                                <div class="col-md-3">
                                    <select class="form-select bg-light border-0 shadow-none text-muted fw-semibold">
                                        <option value="">— Khoa —</option>
                                        <c:forEach var="k" items="${khoaList}">
                                            <option value="${k.maKhoa}">${k.tenKhoa}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-4 text-end">
                                    <div class="text-muted small fw-medium">
                                        <i class="bi bi-info-circle me-1"></i> Tổng cộng <span class="fw-bold text-primary">${ltcList.size()}</span> lớp tín chỉ
                                    </div>
                                </div>
                            </div>

                            <!-- TABLE -->
                            <div class="table-responsive">
                                <table class="table table-custom">
                                    <thead>
                                        <tr>
                                            <th>Mã LTC</th>
                                            <th>Môn Học</th>
                                            <th>Niên Khóa / Kỳ</th>
                                            <th>Nhóm</th>
                                            <th>Giảng Viên</th>
                                            <th>Trạng Thái</th>
                                            <th class="text-center">Thao Tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${ltcList}">
                                            <tr>
                                                <td><span class="badge-soft-primary">${item.maLTC}</span></td>
                                                <td>
                                                    <div class="ltc-title">${item.maMH}</div>
                                                    <div class="ltc-subtitle"><i class="bi bi-building"></i> ${item.maKhoa}</div>
                                                </td>
                                                <td>
                                                    <div class="fw-bold text-dark">${item.nienKhoa}</div>
                                                    <div class="small text-muted">Học kỳ: ${item.hocKy}</div>
                                                </td>
                                                <td><span class="badge-soft-secondary">Nhóm ${item.nhom}</span></td>
                                                <td>
                                                    <div class="fw-semibold text-primary">${item.maGV}</div>
                                                    <div class="small text-muted">Tối thiểu: ${item.soSVToiThieu} SV</div>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${item.huyLop}">
                                                            <span class="badge-soft-danger"><i class="bi bi-x-circle"></i> Đã hủy</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge-soft-success"><i class="bi bi-check-circle"></i> Đang mở</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="text-center">
                                                    <div class="d-flex gap-2 justify-content-center">
                                                        <a href="${pageContext.request.contextPath}/credit-class?lnkEdit&maLTC=${item.maLTC}" class="btn btn-sm btn-outline-primary rounded-circle p-2" style="width: 32px; height: 32px; display: flex; align-items: center; justify-content: center;">
                                                            <i class="bi bi-pencil"></i>
                                                        </a>
                                                        <form action="${pageContext.request.contextPath}/credit-class" method="post" style="display:inline;" onsubmit="return confirm('Xác nhận xóa lớp tín chỉ này?');">
                                                            <input type="hidden" name="maLTC" value="${item.maLTC}">
                                                            <button type="submit" name="btnDelete" class="btn btn-sm btn-outline-danger rounded-circle p-2" style="width: 32px; height: 32px; display: flex; align-items: center; justify-content: center;">
                                                                <i class="bi bi-trash"></i>
                                                            </button>
                                                        </form>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty ltcList}">
                                            <tr>
                                                <td colspan="7" class="text-center py-5 text-muted">Chưa có dữ liệu</td>
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

    <!-- MODAL -->
    <div class="modal fade" id="ltcModal" tabindex="-1" ${not empty ltc.maLTC ? 'data-bs-backdrop="static"' : ''}>
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg">
                <div class="modal-header modal-header-custom">
                    <h5 class="modal-title fw-bold">Thiết lập Lớp Tín Chỉ</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="${pageContext.request.contextPath}/credit-class" method="post">
                    <div class="modal-body p-4 bg-light">
                        <div class="d-flex gap-2 mb-4">
                            <button type="submit" name="${not empty ltc.maLTC ? 'btnUpdate' : 'btnInsert'}" class="btn btn-success toolbar-btn"><i class="bi bi-save"></i> Ghi</button>
                            <button type="reset" class="btn btn-warning toolbar-btn text-dark"><i class="bi bi-arrow-counterclockwise"></i> Phục hồi</button>
                        </div>

                        <div class="row g-3 bg-white p-3 rounded-3 shadow-sm">
                            <input type="hidden" name="maLTC" value="${ltc.maLTC}">
                            <div class="col-md-6">
                                <label class="form-label fw-bold small">Niên Khóa</label>
                                <input type="text" class="form-control" name="nienKhoa" value="${ltc.nienKhoa}" placeholder="VD: 2023-2024" required>
                            </div>
                            <div class="col-md-3">
                                <label class="form-label fw-bold small">Học Kỳ</label>
                                <input type="number" class="form-control" name="hocKy" value="${ltc.hocKy}" min="1" max="3" required>
                            </div>
                            <div class="col-md-3">
                                <label class="form-label fw-bold small">Nhóm</label>
                                <input type="number" class="form-control" name="nhom" value="${ltc.nhom}" min="1" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold small">Mã Môn Học</label>
                                <input type="text" class="form-control" name="maMH" value="${ltc.maMH}" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold small">Mã Giảng Viên</label>
                                <input type="text" class="form-control" name="maGV" value="${ltc.maGV}" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold small">Mã Khoa</label>
                                <input type="text" class="form-control" name="maKhoa" value="${ltc.maKhoa}" required>
                            </div>
                            <div class="col-md-3">
                                <label class="form-label fw-bold small">SV Tối Thiểu</label>
                                <input type="number" class="form-control" name="soSVToiThieu" value="${ltc.soSVToiThieu}" min="1" required>
                            </div>
                            <div class="col-md-3 d-flex align-items-end pb-1">
                                <div class="form-check form-switch">
                                    <input class="form-check-input" type="checkbox" id="huyLop" name="huyLop" value="true" ${ltc.huyLop ? 'checked' : ''}>
                                    <label class="form-check-label fw-bold text-danger" for="huyLop">Hủy lớp</label>
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
                new bootstrap.Modal(document.getElementById('ltcModal')).show();
            }
        });
        function resetForm() {
            document.querySelector('#ltcModal input[name="maLTC"]').value = '0';
        }
    </script>
</body>
</html>
