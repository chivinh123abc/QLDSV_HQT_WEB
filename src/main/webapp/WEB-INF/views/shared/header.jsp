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
        
        <div class="header-user" tabindex="0" role="button" aria-haspopup="true" aria-expanded="false">
            <div class="header-user-info d-none d-sm-block">
                <div class="name">
                    <c:choose>
                        <c:when test="${sessionScope.role == 'SINHVIEN'}">
                            ${sessionScope.studentProfile.ho} ${sessionScope.studentProfile.ten}
                        </c:when>
                        <c:otherwise>
                            ${sessionScope.user.username}
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="role">${sessionScope.role}</div>
            </div>
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
