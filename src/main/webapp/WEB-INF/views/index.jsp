<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Bảng điều khiển - QLDSV_HTC_WEB</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <style>
        .stat-card {
            border: none;
            border-radius: 20px;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            overflow: hidden;
            position: relative;
        }
        .stat-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 15px 30px rgba(0,0,0,0.1) !important;
        }
        .stat-icon {
            width: 60px;
            height: 60px;
            border-radius: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.8rem;
            margin-bottom: 20px;
        }
        .bg-gradient-primary { background: linear-gradient(135deg, #4361ee, #4cc9f0); color: white; }
        .bg-gradient-success { background: linear-gradient(135deg, #10b981, #34d399); color: white; }
        .bg-gradient-warning { background: linear-gradient(135deg, #f59e0b, #fbbf24); color: white; }
        .bg-gradient-danger { background: linear-gradient(135deg, #ef4444, #f87171); color: white; }
        
        .quick-action-card {
            border: 1px solid #e2e8f0;
            border-radius: 16px;
            padding: 20px;
            text-align: center;
            transition: all 0.2s;
            cursor: pointer;
            text-decoration: none;
            color: inherit;
        }
        .quick-action-card:hover {
            background-color: #f8fafc;
            border-color: #4361ee;
            transform: scale(1.02);
        }
        .quick-action-icon {
            font-size: 2rem;
            color: #4361ee;
            margin-bottom: 12px;
        }
        .welcome-banner {
            background: linear-gradient(rgba(0,0,0,0.5), rgba(0,0,0,0.5)), url('https://images.unsplash.com/photo-1523050853064-8521a3998379?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80');
            background-size: cover;
            background-position: center;
            border-radius: 24px;
            padding: 60px 40px;
            color: white;
            margin-bottom: 40px;
        }
        .dashboard-activity-item {
            border-left: 3px solid #e2e8f0;
            padding-left: 20px;
            margin-bottom: 25px;
            position: relative;
        }
        .dashboard-activity-item::before {
            content: '';
            position: absolute;
            left: -8px;
            top: 0;
            width: 13px;
            height: 13px;
            border-radius: 50%;
            background-color: #4361ee;
        }
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
                    <c:choose>
                        <c:when test="${sessionScope.role == 'SINHVIEN'}">
                            <!-- WELCOME BANNER FOR STUDENT -->
                            <div class="welcome-banner shadow-lg" style="background: linear-gradient(rgba(0,0,0,0.65), rgba(0,0,0,0.65)), url('https://images.unsplash.com/photo-1541339907198-e08756dedf3f?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80'); background-size: cover; background-position: center;">
                                <h1 class="display-5 fw-bold mb-2">Chào mừng trở lại, ${sessionScope.studentProfile.ho} ${sessionScope.studentProfile.ten}!</h1>
                                <p class="lead opacity-75 mb-4">Cổng thông tin sinh viên - Đăng ký tín chỉ & Xem điểm trực tuyến</p>
                                <div class="d-flex gap-3">
                                    <a href="${pageContext.request.contextPath}/registration" class="btn btn-primary px-4 py-2 rounded-pill fw-bold shadow-sm">
                                        <i class="bi bi-pencil-square me-2"></i> Đăng ký Lớp Tín Chỉ
                                    </a>
                                    <a href="${pageContext.request.contextPath}/mark/student" class="btn btn-light px-4 py-2 rounded-pill fw-bold text-primary shadow-sm">
                                        <i class="bi bi-bookmark-star me-2"></i> Xem Điểm
                                    </a>
                                </div>
                            </div>

                            <!-- STUDENT INFO & ACTIONS GRID -->
                            <div class="row g-4 mb-5">
                                <!-- INFO CARD -->
                                <div class="col-lg-6">
                                    <div class="card border-0 shadow-sm rounded-4 h-100">
                                        <div class="card-body p-4">
                                            <h5 class="fw-bold mb-4 text-primary d-flex align-items-center gap-2">
                                                <i class="bi bi-person-circle"></i> Thông tin cá nhân
                                            </h5>
                                            <div class="row g-3">
                                                <div class="col-sm-6">
                                                    <small class="text-muted d-block">Mã số sinh viên</small>
                                                    <span class="fw-bold fs-5 text-dark">${sessionScope.studentProfile.maSV}</span>
                                                </div>
                                                <div class="col-sm-6">
                                                    <small class="text-muted d-block">Lớp học</small>
                                                    <span class="fw-bold fs-5 text-dark">${sessionScope.studentProfile.maLop}</span>
                                                </div>
                                                <div class="col-sm-6">
                                                    <small class="text-muted d-block">Giới tính</small>
                                                    <span class="fw-bold text-dark">${sessionScope.studentProfile.phai}</span>
                                                </div>
                                                <div class="col-sm-6">
                                                    <small class="text-muted d-block">Trạng thái</small>
                                                    <span class="badge bg-success bg-opacity-10 text-success rounded-pill px-3 py-1">Đang học</span>
                                                </div>
                                                <div class="col-12">
                                                    <small class="text-muted d-block">Địa chỉ</small>
                                                    <span class="fw-medium text-dark">${sessionScope.studentProfile.diaChi}</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- STATS CARD -->
                                <div class="col-lg-6">
                                    <div class="card border-0 shadow-sm rounded-4 h-100">
                                        <div class="card-body p-4 d-flex flex-column justify-content-between">
                                            <div>
                                                <h5 class="fw-bold mb-4 text-success d-flex align-items-center gap-2">
                                                    <i class="bi bi-mortarboard-fill"></i> Hoạt động học tập
                                                </h5>
                                                <div class="d-flex align-items-center gap-4 mb-4">
                                                    <div class="stat-icon bg-gradient-success mb-0 d-flex align-items-center justify-content-center text-white rounded-3" style="width: 70px; height: 70px; font-size: 2rem;">
                                                        <i class="bi bi-journal-bookmark-fill"></i>
                                                    </div>
                                                    <div>
                                                        <h6 class="text-muted mb-1">Lớp tín chỉ đăng ký học kỳ này</h6>
                                                        <h2 class="fw-bold text-dark mb-0">${registeredCount != null ? registeredCount : 0} môn</h2>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="bg-light p-3 rounded-3 mb-2">
                                                <small class="text-muted d-block"><i class="bi bi-info-circle me-1"></i> Lưu ý:</small>
                                                <small class="text-muted">Sinh viên vui lòng hoàn thành học phí đúng thời hạn quy định để giữ quyền thi kết thúc môn học.</small>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- QUICK ACTIONS CARDS -->
                            <div class="row g-4">
                                <h5 class="fw-bold mb-3 text-dark">Chức năng trực tuyến</h5>
                                <div class="col-md-6">
                                    <a href="${pageContext.request.contextPath}/mark/student" class="quick-action-card d-block shadow-sm h-100 p-4 bg-white text-decoration-none rounded-4 border-0">
                                        <div class="d-flex align-items-center gap-3">
                                            <div class="stat-icon bg-gradient-primary mb-0 text-white rounded-3 d-flex align-items-center justify-content-center" style="width: 60px; height: 60px; font-size: 1.8rem;">
                                                <i class="bi bi-card-checklist"></i>
                                            </div>
                                            <div class="text-start">
                                                <h5 class="fw-bold mb-1 text-dark">XEM ĐIỂM HỌC TẬP</h5>
                                                <p class="text-muted small mb-0">Tra cứu điểm quá trình, điểm thi & điểm tổng kết môn.</p>
                                            </div>
                                        </div>
                                    </a>
                                </div>
                                <div class="col-md-6">
                                    <a href="${pageContext.request.contextPath}/registration" class="quick-action-card d-block shadow-sm h-100 p-4 bg-white text-decoration-none rounded-4 border-0">
                                        <div class="d-flex align-items-center gap-3">
                                            <div class="stat-icon bg-gradient-success mb-0 text-white rounded-3 d-flex align-items-center justify-content-center" style="width: 60px; height: 60px; font-size: 1.8rem;">
                                                <i class="bi bi-calendar2-week"></i>
                                            </div>
                                            <div class="text-start">
                                                <h5 class="fw-bold mb-1 text-dark">ĐĂNG KÝ LỚP TÍN CHỈ</h5>
                                                <p class="text-muted small mb-0">Tự đăng ký lớp học cho bản thân trong các học kỳ được mở.</p>
                                            </div>
                                        </div>
                                    </a>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <!-- WELCOME BANNER -->
                            <div class="welcome-banner shadow-lg">
                                <h1 class="display-5 fw-bold mb-2">Chào mừng trở lại, Admin!</h1>
                                <p class="lead opacity-75 mb-4">Hệ thống quản lý điểm sinh viên theo hệ tín chỉ - Phiên bản 2.0</p>
                                <div class="d-flex gap-3">
                                    <a href="${pageContext.request.contextPath}/student" class="btn btn-primary px-4 py-2 rounded-pill fw-bold">
                                        <i class="bi bi-people-fill me-2"></i> Quản lý Sinh viên
                                    </a>
                                    <a href="${pageContext.request.contextPath}/credit-class" class="btn btn-light px-4 py-2 rounded-pill fw-bold">
                                        <i class="bi bi-calendar-check me-2"></i> Lịch học hôm nay
                                    </a>
                                </div>
                            </div>

                            <!-- STATISTICS CARDS -->
                            <div class="row g-4 mb-5">
                                <div class="col-xl-3 col-md-6">
                                    <div class="card stat-card shadow-sm h-100">
                                        <div class="card-body p-4">
                                            <div class="d-flex align-items-center justify-content-between">
                                                <div>
                                                    <h6 class="text-muted fw-bold small text-uppercase mb-1">Tổng Sinh Viên</h6>
                                                    <h2 class="fw-bold mb-0">${studentCount}</h2>
                                                    <div class="mt-2 small text-success">
                                                        <i class="bi bi-arrow-up"></i> 12%
                                                    </div>
                                                </div>
                                                <div class="stat-icon bg-gradient-primary mb-0">
                                                    <i class="bi bi-people"></i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xl-3 col-md-6">
                                    <div class="card stat-card shadow-sm h-100">
                                        <div class="card-body p-4">
                                            <div class="d-flex align-items-center justify-content-between">
                                                <div>
                                                    <h6 class="text-muted fw-bold small text-uppercase mb-1">Tổng Lớp Học</h6>
                                                    <h2 class="fw-bold mb-0">${classCount}</h2>
                                                    <div class="mt-2 small text-muted">Đào tạo tập trung</div>
                                                </div>
                                                <div class="stat-icon bg-gradient-success mb-0">
                                                    <i class="bi bi-building"></i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xl-3 col-md-6">
                                    <div class="card stat-card shadow-sm h-100">
                                        <div class="card-body p-4">
                                            <div class="d-flex align-items-center justify-content-between">
                                                <div>
                                                    <h6 class="text-muted fw-bold small text-uppercase mb-1">Môn Học</h6>
                                                    <h2 class="fw-bold mb-0">${subjectCount}</h2>
                                                    <div class="mt-2 small text-muted">Chuẩn hóa</div>
                                                </div>
                                                <div class="stat-icon bg-gradient-warning mb-0">
                                                    <i class="bi bi-journal-bookmark"></i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xl-3 col-md-6">
                                    <div class="card stat-card shadow-sm h-100">
                                        <div class="card-body p-4">
                                            <div class="d-flex align-items-center justify-content-between">
                                                <div>
                                                    <h6 class="text-muted fw-bold small text-uppercase mb-1">Lớp Tín Chỉ</h6>
                                                    <h2 class="fw-bold mb-0">${creditClassCount}</h2>
                                                    <div class="mt-2 small text-danger">
                                                        Học kỳ II
                                                    </div>
                                                </div>
                                                <div class="stat-icon bg-gradient-danger mb-0">
                                                    <i class="bi bi-layers"></i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row g-4">
                                <!-- QUICK ACTIONS -->
                                <div class="col-lg-8">
                                    <div class="card border-0 shadow-sm rounded-4 mb-4">
                                        <div class="card-body p-4">
                                            <h5 class="fw-bold mb-4">Truy cập nhanh</h5>
                                            <div class="row g-3">
                                                <div class="col-sm-4">
                                                    <a href="${pageContext.request.contextPath}/student" class="quick-action-card d-block shadow-sm h-100">
                                                        <div class="quick-action-icon"><i class="bi bi-person-plus"></i></div>
                                                        <h6 class="fw-bold mb-1">Thêm Sinh Viên</h6>
                                                        <p class="text-muted small mb-0">Quản lý hồ sơ mới</p>
                                                    </a>
                                                </div>
                                                <div class="col-sm-4">
                                                    <a href="${pageContext.request.contextPath}/mark" class="quick-action-card d-block shadow-sm h-100">
                                                        <div class="quick-action-icon" style="color: #10b981;"><i class="bi bi-pencil-square"></i></div>
                                                        <h6 class="fw-bold mb-1">Nhập Điểm</h6>
                                                        <p class="text-muted small mb-0">Cập nhật kết quả học tập</p>
                                                    </a>
                                                </div>
                                                <div class="col-sm-4">
                                                    <a href="${pageContext.request.contextPath}/registration" class="quick-action-card d-block shadow-sm h-100">
                                                        <div class="quick-action-icon" style="color: #f59e0b;"><i class="bi bi-clipboard-check"></i></div>
                                                        <h6 class="fw-bold mb-1">Đăng Ký Môn</h6>
                                                        <p class="text-muted small mb-0">Điều chỉnh lịch học</p>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="card border-0 shadow-sm rounded-4">
                                        <div class="card-body p-4">
                                            <div class="d-flex justify-content-between align-items-center mb-4">
                                                <h5 class="fw-bold mb-0">Phân tích hệ thống</h5>
                                                <div class="dropdown">
                                                    <button class="btn btn-light btn-sm dropdown-toggle" type="button" data-bs-toggle="dropdown">Học kỳ này</button>
                                                    <ul class="dropdown-menu">
                                                        <li><a class="dropdown-item" href="#">Học kỳ trước</a></li>
                                                        <li><a class="dropdown-item" href="#">Năm học trước</a></li>
                                                    </ul>
                                                </div>
                                            </div>
                                            <div class="p-5 text-center bg-light rounded-4 border-dashed">
                                                <i class="bi bi-bar-chart-line fs-1 text-muted opacity-25 d-block mb-3"></i>
                                                <p class="text-muted">Tính năng phân tích và biểu đồ đang được phát triển...</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- RECENT ACTIVITY & INFO -->
                                <div class="col-lg-4">
                                    <div class="card border-0 shadow-sm rounded-4 mb-4">
                                        <div class="card-body p-4">
                                            <h5 class="fw-bold mb-4">Hoạt động gần đây</h5>
                                            <div class="dashboard-activity-item">
                                                <div class="fw-bold text-dark small">Cập nhật điểm thi kết thúc học kỳ</div>
                                                <div class="text-muted" style="font-size: 0.75rem;">10 phút trước • Bởi Admin</div>
                                            </div>
                                            <div class="dashboard-activity-item" style="border-left-color: #10b981;">
                                                <div class="fw-bold text-dark small">Mở thêm lớp tín chỉ môn CTDL</div>
                                                <div class="text-muted" style="font-size: 0.75rem;">2 giờ trước • Bởi Phòng Đào Tạo</div>
                                            </div>
                                            <div class="dashboard-activity-item" style="border-left-color: #f59e0b;">
                                                <div class="fw-bold text-dark small">Sao lưu dữ liệu hệ thống</div>
                                                <div class="text-muted" style="font-size: 0.75rem;">Hôm qua • Tự động</div>
                                            </div>
                                            <button class="btn btn-outline-primary btn-sm w-100 rounded-3 fw-bold mt-2">Xem tất cả</button>
                                        </div>
                                    </div>

                                    <div class="card bg-primary text-white border-0 shadow-lg rounded-4 overflow-hidden">
                                        <div class="card-body p-4 position-relative">
                                            <i class="bi bi-lightning-charge-fill position-absolute" style="right: -10px; bottom: -20px; font-size: 8rem; opacity: 0.1;"></i>
                                            <h5 class="fw-bold mb-3">Hỗ trợ kỹ thuật</h5>
                                            <p class="small opacity-75 mb-4">Gặp sự cố với hệ thống? Liên hệ ngay với đội ngũ kỹ thuật để được hỗ trợ nhanh nhất.</p>
                                            <a href="mailto:support@ptithcm.edu.vn" class="btn btn-light btn-sm px-4 fw-bold text-primary">LIÊN HỆ</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </main>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
