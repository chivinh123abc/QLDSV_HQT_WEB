<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<aside class="app-sidebar" id="appSidebar">
    <div class="sidebar-brand">
        <h2>QLDSV_HTC</h2>
        <p>Quản lý điểm sinh viên</p>
    </div>

    <nav class="sidebar-nav" aria-label="Main Navigation">
        <ul>
            <li>
                <a class="nav-link" href="${pageContext.request.contextPath}/index">
                    <i class="bi bi-house-door"></i> Bảng điều khiển
                </a>
            </li>
            <li>
                <a class="nav-link" href="${pageContext.request.contextPath}/student">
                    <i class="bi bi-people"></i> Sinh viên
                </a>
            </li>
            <li>
                <a class="nav-link" href="${pageContext.request.contextPath}/class">
                    <i class="bi bi-building"></i> Lớp học
                </a>
            </li>
            <li>
                <a class="nav-link" href="${pageContext.request.contextPath}/subject">
                    <i class="bi bi-book"></i> Môn học
                </a>
            </li>
            <li>
                <a class="nav-link" href="${pageContext.request.contextPath}/credit-class">
                    <i class="bi bi-journal-text"></i> Lớp tín chỉ
                </a>
            </li>
            <li>
                <a class="nav-link" href="${pageContext.request.contextPath}/registration">
                    <i class="bi bi-pencil-square"></i> Đăng ký môn
                </a>
            </li>
        </ul>
    </nav>

    <div class="sidebar-footer">
        <div class="sidebar-roles">
            <span class="role-badge role-pgv active">PGV</span>
            <span class="role-badge role-khoa">KHOA</span>
            <span class="role-badge role-sv">SV</span>
        </div>
        <form action="${pageContext.request.contextPath}/logout" method="post">
            <button type="submit" class="sidebar-logout" aria-label="Đăng xuất khỏi hệ thống">
                <i class="bi bi-box-arrow-left" aria-hidden="true"></i> Đăng xuất
            </button>
        </form>
    </div>
</aside>
