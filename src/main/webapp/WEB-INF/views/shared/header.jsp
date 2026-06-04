<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<script>
    (function () {
        const currentTheme = localStorage.getItem("theme") || "light";
        document.documentElement.setAttribute("data-theme", currentTheme);
    })();
</script>

<header class="app-header">
    <div class="d-flex align-items-center gap-3">
        <button class="btn d-lg-none" id="sidebarToggle" type="button" aria-label="Toggle Navigation Sidebar" aria-expanded="false" aria-controls="appSidebar">
            <i class="bi bi-list fs-4" aria-hidden="true"></i>
        </button>
        <h1><s:message code="global.menu.home"/></h1>
    </div>
    <div class="d-flex align-items-center gap-3">
        <!-- Khối chuyển đổi ngôn ngữ -->
        <div class="d-flex align-items-center gap-2 me-2">
            <a href="javascript:void(0);" onclick="changeLanguage('vi')" title="<s:message code="global.lang.vi"/>" class="text-decoration-none text-dark fw-semibold px-2 py-1 rounded hover-bg-light" style="font-size: 0.9rem;">
                VN
            </a>
            <span class="text-muted" style="opacity: 0.5;">|</span>
            <a href="javascript:void(0);" onclick="changeLanguage('en')" title="<s:message code="global.lang.en"/>" class="text-decoration-none text-dark fw-semibold px-2 py-1 rounded hover-bg-light" style="font-size: 0.9rem;">
                EN
            </a>
        </div>

        <!-- Theme Toggle Button -->
        <button id="themeToggleBtn" class="btn btn-link text-secondary p-2 rounded-circle hover-bg-light border-0" style="width: 40px; height: 40px; display: inline-flex; align-items: center; justify-content: center; text-decoration: none;" aria-label="Chuyển chế độ sáng/tối">
            <i class="bi bi-sun-fill fs-5" id="themeToggleIcon"></i>
        </button>

        <!-- Notification Bell -->
        <div class="position-relative d-inline-flex me-2 text-secondary p-2 rounded-circle hover-bg-light" style="cursor: pointer; width: 40px; height: 40px; align-items: center; justify-content: center;" onclick="window.location.href='${pageContext.request.contextPath}/announcements'" title="<s:message code="announcement.bellTitle"/>">
            <i class="bi bi-bell-fill fs-5"></i>
            <span id="unreadBadge" class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger" 
                  style="${unreadCount > 0 ? '' : 'display: none;'} font-size: 0.65rem; padding: 0.25em 0.5em;">
                ${unreadCount}
            </span>
        </div>
        
        <div class="dropdown" style="position: relative;">
            <a href="#" class="header-user text-decoration-none d-flex align-items-center border-0" id="myAvatarBtn" role="button" style="outline: none; cursor: pointer;">
                <div class="header-user-info d-none d-sm-block text-end me-2">
                    <div class="name text-dark fw-semibold">
                        <c:choose>
                            <c:when test="${sessionScope.role == 'SINHVIEN'}">
                                ${sessionScope.studentProfile.ho} ${sessionScope.studentProfile.ten}
                            </c:when>
                            <c:otherwise>
                                ${sessionScope.user.username}
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="role text-muted small">
                        <c:choose>
                            <c:when test="${sessionScope.role == 'SINHVIEN'}">
                                <s:message code="role.display.student" />
                            </c:when>
                            <c:when test="${sessionScope.role == 'KHOA'}">
                                <s:message code="role.display.faculty" />
                            </c:when>
                            <c:when test="${sessionScope.role == 'PGV'}">
                                <s:message code="role.display.pgv" />
                            </c:when>
                            <c:otherwise>
                                ${sessionScope.role}
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <c:choose>
                    <c:when test="${not empty sessionScope.user.avatar}">
                        <img src="${pageContext.request.contextPath}${sessionScope.user.avatar}" 
                             class="rounded-circle border border-2 border-primary-subtle shadow-sm" 
                             style="width: 40px; height: 40px; object-fit: cover;" alt="Avatar">
                    </c:when>
                    <c:otherwise>
                        <div class="header-avatar" aria-hidden="true">
                            <c:choose>
                                <c:when test="${sessionScope.role == 'SINHVIEN'}">
                                    ${sessionScope.studentProfile.ten.substring(0,1)}
                                </c:when>
                                <c:otherwise>
                                    ${sessionScope.user.username.substring(0,1).toUpperCase()}
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:otherwise>
                </c:choose>
            </a>
            <ul class="dropdown-menu dropdown-menu-end shadow border-0 rounded-3 mt-2" id="myAvatarMenu" style="position: absolute; right: 0; top: 100%; display: none;">
                <li>
                    <a class="dropdown-item d-flex align-items-center gap-2 py-2" href="${pageContext.request.contextPath}${sessionScope.role == 'SINHVIEN' ? '/student/profile' : '/profile'}">
                        <i class="bi bi-person-circle text-primary fs-5"></i>
                        <span><s:message code="profile.title"/></span>
                    </a>
                </li>
                <li><hr class="dropdown-divider"></li>
                <li>
                    <a class="dropdown-item text-danger d-flex align-items-center gap-2 py-2" href="${pageContext.request.contextPath}/logout">
                        <i class="bi bi-box-arrow-right fs-5"></i>
                        <span><s:message code="sidebar.btn.logout"/></span>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</header>

<script src="${pageContext.request.contextPath}/resources/js/theme.js?v=2"></script>
<script>
    function changeLanguage(lang) {
        // Lấy URL hiện tại của trình duyệt
        let currentUrl = new URL(window.location.href);
        
        // Ghi đè hoặc thêm tham số 'language'
        currentUrl.searchParams.set('language', lang);
        
        // Điều hướng lại trang với URL mới
        window.location.href = currentUrl.href;
    }
</script>

<!-- Toastr and Pusher Integration for Real-time System Notifications -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css" />
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>
<script src="https://js.pusher.com/8.0/pusher.min.js"></script>

<script>
    // 1. ÉP PUSHER PHẢI LOG RA CONSOLE ĐỂ DEBUG
    Pusher.logToConsole = true;

    // 2. Chờ HTML và jQuery load xong 100% mới chạy
    $(document).ready(function() {
        try {
            // Khởi tạo Pusher Client
            const pusher = new Pusher('${pusherKey}', {
                cluster: '${pusherCluster}',
                forceTLS: true // Bắt buộc dùng HTTPS, rất quan trọng!
            });

            // Bắt sự kiện kết nối để biết chắc chắn Frontend đã thông với Cloud
            pusher.connection.bind('connected', function() {
                console.log('✅ [Pusher] Đã kết nối thành công tới Server!');
            });

            // Subscribe and Bind events
            const channel = pusher.subscribe('student-channel');
            channel.bind('new-announcement', function(data) {
                console.log('🔥 [Pusher] BẮT ĐƯỢC DATA TỪ BACKEND:', data);
                
                // 1. Logic UX: Hiệu ứng nảy số ở dấu chấm đỏ (Chuông)
                let badge = $('#unreadBadge');
                let currentCount = parseInt(badge.text().trim()) || 0;
                badge.text(currentCount + 1).show();
                
                toastr.options = {
                    "closeButton": true,
                    "debug": false,
                    "newestOnTop": true,
                    "progressBar": true,
                    "positionClass": "toast-top-right",
                    "preventDuplicates": false,
                    "onclick": function() {
                        window.location.href = "${pageContext.request.contextPath}/announcements/detail?id=" + data.id;
                    },
                    "showDuration": "300",
                    "hideDuration": "1000",
                    "timeOut": "10000",
                    "extendedTimeOut": "1000",
                    "showEasing": "swing",
                    "hideEasing": "linear",
                    "showMethod": "fadeIn",
                    "hideMethod": "fadeOut"
                };
                toastr.info('<s:message code="announcement.newNotice"/>' + data.title, '<s:message code="announcement.systemNotice"/>');
            });
        } catch (err) {
            console.error("❌ [Lỗi Hệ Thống] Không thể khởi tạo Pusher hoặc Toastr:", err);
        }
    });
</script>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        const avatarBtn = document.getElementById('myAvatarBtn');
        const avatarMenu = document.getElementById('myAvatarMenu');

        if (avatarBtn && avatarMenu) {
            avatarBtn.addEventListener('click', function(e) {
                e.preventDefault();
                e.stopPropagation();
                if (avatarMenu.style.display === 'block') {
                    avatarMenu.style.display = 'none';
                } else {
                    avatarMenu.style.display = 'block';
                }
            });

            document.addEventListener('click', function(e) {
                if (!avatarBtn.contains(e.target) && !avatarMenu.contains(e.target)) {
                    avatarMenu.style.display = 'none';
                }
            });
        }
    });
</script>
