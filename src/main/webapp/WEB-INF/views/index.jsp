<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title><s:message code="dashboard.page.title"/></title>
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
                                <h1 class="display-5 fw-bold mb-2"><s:message code="dashboard.welcome.student" arguments="${sessionScope.studentProfile.ho},${sessionScope.studentProfile.ten}"/></h1>
                                <p class="lead opacity-75 mb-4"><s:message code="dashboard.student.portal"/></p>
                                <div class="d-flex gap-3">
                                    <a href="${pageContext.request.contextPath}/registration" class="btn btn-primary px-4 py-2 rounded-pill fw-bold shadow-sm">
                                        <i class="bi bi-pencil-square me-2"></i> <s:message code="registration.btn.register"/> <s:message code="dashboard.credit.classes"/>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/mark/student" class="btn btn-light px-4 py-2 rounded-pill fw-bold text-primary shadow-sm">
                                        <i class="bi bi-bookmark-star me-2"></i> <s:message code="dashboard.btn.view.grades"/>
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
                                                <i class="bi bi-person-circle"></i> <s:message code="dashboard.personal.info"/>
                                            </h5>
                                            <div class="row g-3">
                                                <div class="col-sm-6">
                                                    <small class="text-muted d-block"><s:message code="dashboard.student.id"/></small>
                                                    <span class="fw-bold fs-5 text-dark">${sessionScope.studentProfile.maSV}</span>
                                                </div>
                                                <div class="col-sm-6">
                                                    <small class="text-muted d-block"><s:message code="dashboard.classroom"/></small>
                                                    <span class="fw-bold fs-5 text-dark">${sessionScope.studentProfile.maLop}</span>
                                                </div>
                                                <div class="col-sm-6">
                                                    <small class="text-muted d-block"><s:message code="dashboard.gender"/></small>
                                                    <span class="fw-bold text-dark">${sessionScope.studentProfile.phai}</span>
                                                </div>
                                                <div class="col-sm-6">
                                                    <small class="text-muted d-block"><s:message code="dashboard.status"/></small>
                                                    <span class="badge bg-success bg-opacity-10 text-success rounded-pill px-3 py-1"><s:message code="dashboard.status.studying"/></span>
                                                </div>
                                                <div class="col-12">
                                                    <small class="text-muted d-block"><s:message code="dashboard.address"/></small>
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
                                                    <i class="bi bi-mortarboard-fill"></i> <s:message code="dashboard.learning.activity"/>
                                                </h5>
                                                <div class="d-flex align-items-center gap-4 mb-4">
                                                    <div class="stat-icon bg-gradient-success mb-0 d-flex align-items-center justify-content-center text-white rounded-3" style="width: 70px; height: 70px; font-size: 2rem;">
                                                        <i class="bi bi-journal-bookmark-fill"></i>
                                                    </div>
                                                    <div>
                                                        <h6 class="text-muted mb-1"><s:message code="dashboard.registered.credit.classes"/></h6>
                                                        <h2 class="fw-bold text-dark mb-0"><s:message code="dashboard.subjects.count" arguments="${registeredCount != null ? registeredCount : 0}"/></h2>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="bg-light p-3 rounded-3 mb-2">
                                                <small class="text-muted d-block"><i class="bi bi-info-circle me-1"></i> <s:message code="dashboard.note"/></small>
                                                <small class="text-muted"><s:message code="dashboard.tuition.notice"/></small>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- QUICK ACTIONS CARDS -->
                            <div class="row g-4">
                                <h5 class="fw-bold mb-3 text-dark"><s:message code="dashboard.online.features"/></h5>
                                <div class="col-md-6">
                                    <a href="${pageContext.request.contextPath}/mark/student" class="quick-action-card d-block shadow-sm h-100 p-4 bg-white text-decoration-none rounded-4 border-0">
                                        <div class="d-flex align-items-center gap-3">
                                            <div class="stat-icon bg-gradient-primary mb-0 text-white rounded-3 d-flex align-items-center justify-content-center" style="width: 60px; height: 60px; font-size: 1.8rem;">
                                                <i class="bi bi-card-checklist"></i>
                                            </div>
                                            <div class="text-start">
                                                <h5 class="fw-bold mb-1 text-dark"><s:message code="dashboard.view.grades"/></h5>
                                                <p class="text-muted small mb-0"><s:message code="dashboard.lookup.grades.desc"/></p>
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
                                                <h5 class="fw-bold mb-1 text-dark"><s:message code="dashboard.register.credit.class"/></h5>
                                                <p class="text-muted small mb-0"><s:message code="dashboard.self.register.desc"/></p>
                                            </div>
                                        </div>
                                    </a>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <!-- WELCOME BANNER -->
                            <div class="welcome-banner shadow-lg">
                                <h1 class="display-5 fw-bold mb-2"><s:message code="dashboard.welcome.admin"/></h1>
                                <p class="lead opacity-75 mb-4"><s:message code="dashboard.system.desc"/></p>
                                <div class="d-flex gap-3">
                                    <a href="${pageContext.request.contextPath}/student" class="btn btn-primary px-4 py-2 rounded-pill fw-bold">
                                        <i class="bi bi-people-fill me-2"></i> <s:message code="student.management"/>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/credit-class" class="btn btn-light px-4 py-2 rounded-pill fw-bold">
                                        <i class="bi bi-calendar-check me-2"></i> <s:message code="dashboard.today.schedule"/>
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
                                                    <h6 class="text-muted fw-bold small text-uppercase mb-1"><s:message code="dashboard.total.students"/></h6>
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
                                                    <h6 class="text-muted fw-bold small text-uppercase mb-1"><s:message code="dashboard.total.classes"/></h6>
                                                    <h2 class="fw-bold mb-0">${classCount}</h2>
                                                    <div class="mt-2 small text-muted"><s:message code="dashboard.training.type"/></div>
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
                                                    <h6 class="text-muted fw-bold small text-uppercase mb-1"><s:message code="dashboard.subjects"/></h6>
                                                    <h2 class="fw-bold mb-0">${subjectCount}</h2>
                                                    <div class="mt-2 small text-muted"><s:message code="dashboard.standardized"/></div>
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
                                                    <h6 class="text-muted fw-bold small text-uppercase mb-1"><s:message code="dashboard.credit.classes"/></h6>
                                                    <h2 class="fw-bold mb-0">${creditClassCount}</h2>
                                                    <div class="mt-2 small text-danger">
                                                        <s:message code="global.lbl.semester"/> II
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
                                            <h5 class="fw-bold mb-4"><s:message code="dashboard.quick.access"/></h5>
                                            <div class="row g-3">
                                                <div class="col-sm-4">
                                                    <a href="${pageContext.request.contextPath}/student" class="quick-action-card d-block shadow-sm h-100">
                                                        <div class="quick-action-icon"><i class="bi bi-person-plus"></i></div>
                                                        <h6 class="fw-bold mb-1"><s:message code="dashboard.add.student"/></h6>
                                                        <p class="text-muted small mb-0"><s:message code="dashboard.manage.records"/></p>
                                                    </a>
                                                </div>
                                                <div class="col-sm-4">
                                                    <a href="${pageContext.request.contextPath}/mark" class="quick-action-card d-block shadow-sm h-100">
                                                        <div class="quick-action-icon" style="color: #10b981;"><i class="bi bi-pencil-square"></i></div>
                                                        <h6 class="fw-bold mb-1"><s:message code="dashboard.enter.grades"/></h6>
                                                        <p class="text-muted small mb-0"><s:message code="dashboard.update.results"/></p>
                                                    </a>
                                                </div>
                                                <div class="col-sm-4">
                                                    <a href="${pageContext.request.contextPath}/registration" class="quick-action-card d-block shadow-sm h-100">
                                                        <div class="quick-action-icon" style="color: #f59e0b;"><i class="bi bi-clipboard-check"></i></div>
                                                        <h6 class="fw-bold mb-1"><s:message code="dashboard.register.subjects"/></h6>
                                                        <p class="text-muted small mb-0"><s:message code="dashboard.adjust.schedule"/></p>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="card border-0 shadow-sm rounded-4">
                                        <div class="card-body p-4">
                                            <div class="d-flex justify-content-between align-items-center mb-4">
                                                <h5 class="fw-bold mb-0"><s:message code="dashboard.system.analysis"/></h5>
                                                <div class="dropdown">
                                                    <button class="btn btn-light btn-sm dropdown-toggle" type="button" data-bs-toggle="dropdown"><s:message code="dashboard.this.semester"/></button>
                                                    <ul class="dropdown-menu">
                                                        <li><a class="dropdown-item" href="#"><s:message code="dashboard.prev.semester"/></a></li>
                                                        <li><a class="dropdown-item" href="#"><s:message code="dashboard.prev.year"/></a></li>
                                                    </ul>
                                                </div>
                                            </div>
                                            <div class="p-5 text-center bg-light rounded-4 border-dashed">
                                                <i class="bi bi-bar-chart-line fs-1 text-muted opacity-25 d-block mb-3"></i>
                                                <p class="text-muted"><s:message code="dashboard.analytics.dev"/></p>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- RECENT ACTIVITY & INFO -->
                                <div class="col-lg-4">
                                    <div class="card border-0 shadow-sm rounded-4 mb-4">
                                        <div class="card-body p-4">
                                            <h5 class="fw-bold mb-4"><s:message code="dashboard.recent.activities"/></h5>
                                            <div class="dashboard-activity-item">
                                                <div class="fw-bold text-dark small"><s:message code="dashboard.activity.update.exam"/></div>
                                                <div class="text-muted" style="font-size: 0.75rem;"><s:message code="dashboard.activity.10min.ago"/></div>
                                            </div>
                                            <div class="dashboard-activity-item" style="border-left-color: #10b981;">
                                                <div class="fw-bold text-dark small"><s:message code="dashboard.activity.open.class"/></div>
                                                <div class="text-muted" style="font-size: 0.75rem;"><s:message code="dashboard.activity.2hrs.ago"/></div>
                                            </div>
                                            <div class="dashboard-activity-item" style="border-left-color: #f59e0b;">
                                                <div class="fw-bold text-dark small"><s:message code="dashboard.backup.data"/></div>
                                                <div class="text-muted" style="font-size: 0.75rem;"><s:message code="dashboard.activity.yesterday"/></div>
                                            </div>
                                            <button class="btn btn-outline-primary btn-sm w-100 rounded-3 fw-bold mt-2"><s:message code="dashboard.view.all"/></button>
                                        </div>
                                    </div>

                                    <div class="card bg-primary text-white border-0 shadow-lg rounded-4 overflow-hidden">
                                        <div class="card-body p-4 position-relative">
                                            <i class="bi bi-lightning-charge-fill position-absolute" style="right: -10px; bottom: -20px; font-size: 8rem; opacity: 0.1;"></i>
                                            <h5 class="fw-bold mb-3"><s:message code="dashboard.technical.support"/></h5>
                                            <p class="small opacity-75 mb-4"><s:message code="dashboard.support.desc"/></p>
                                            <a href="mailto:ptquanh.contact@gmail.com" class="btn btn-light btn-sm px-4 fw-bold text-primary"><s:message code="dashboard.btn.contact"/></a>
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

