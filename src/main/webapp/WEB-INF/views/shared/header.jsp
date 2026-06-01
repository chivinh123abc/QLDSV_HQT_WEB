<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<header class="app-header">
    <div class="d-flex align-items-center gap-3">
        <button class="btn d-lg-none" id="sidebarToggle" type="button" aria-label="Toggle Navigation Sidebar" aria-expanded="false" aria-controls="appSidebar">
            <i class="bi bi-list fs-4" aria-hidden="true"></i>
        </button>
        <h1>Trang chủ</h1>
    </div>
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
</header>

