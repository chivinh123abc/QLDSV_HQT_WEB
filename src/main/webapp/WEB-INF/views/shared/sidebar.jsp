<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>

        <aside class="app-sidebar" id="appSidebar">
            <div class="sidebar-brand">
                <h2>QLDSV_HTC</h2>
                <p><s:message code="shared.sidebar.credit.system"/></p>
            </div>

            <nav class="sidebar-nav" aria-label="Main Navigation">
                <ul>
                    <%-- Trang chủ: All users --%>
                    <li>
                        <a class="nav-link" href="${pageContext.request.contextPath}/index">
                            <i class="bi bi-house-door"></i> <s:message code="global.menu.home"/>
                        </a>
                    </li>
                    <c:if test="${sessionScope.role != 'PGV' && sessionScope.role != 'KHOA'}">
                        <li>
                            <a class="nav-link" href="${pageContext.request.contextPath}/announcements">
                                <i class="bi bi-bell"></i> <s:message code="announcement.menu"/>
                            </a>
                        </li>
                    </c:if>

                    <%-- Student Marks: Students only --%>
                    <c:if test="${sessionScope.role == 'SINHVIEN'}">
                        <li>
                            <a class="nav-link" href="${pageContext.request.contextPath}/student/mark">
                                <i class="bi bi-bookmark-star"></i> <s:message code="sidebar.menu.viewGrades"/>
                            </a>
                        </li>
                        <li>
                            <a class="nav-link" href="${pageContext.request.contextPath}/student/payment">
                                <i class="bi bi-credit-card"></i> <s:message code="payment.title"/>
                            </a>
                        </li>
                    </c:if>

                        <%-- Student Management & Classes: PGV and KHOA --%>
                            <c:if test="${sessionScope.role == 'PGV' || sessionScope.role == 'KHOA'}">
                                <li>
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/student">
                                        <i class="bi bi-people"></i> <s:message code="sidebar.menu.students"/>
                                    </a>
                                </li>
                                <li>
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/classroom">
                                        <i class="bi bi-building"></i> <s:message code="dashboard.classroom"/>
                                    </a>
                                </li>
                                <li>
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/subject">
                                        <i class="bi bi-book"></i> <s:message code="global.lbl.subject"/>
                                    </a>
                                </li>
                                <li>
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/creditclass">
                                        <i class="bi bi-journal-text"></i> <s:message code="sidebar.menu.creditClasses"/>
                                    </a>
                                </li>
                                <li>
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/lecturer">
                                        <i class="bi bi-person-badge"></i> <s:message code="sidebar.menu.lecturers"/>
                                    </a>
                                </li>
                                <li>
                                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/announcement">
                                        <i class="bi bi-megaphone"></i> <s:message code="announcement.menu"/>
                                    </a>
                                </li>
                            </c:if>


                                <%-- Registration: Students & PGV --%>
                                    <c:if test="${sessionScope.role == 'SINHVIEN'}">
                                        <li>
                                            <a class="nav-link" href="${pageContext.request.contextPath}/student/registration">
                                                 <i class="bi bi-pencil-square"></i> <s:message code="sidebar.menu.registration"/>
                                             </a>
                                         </li>
                                    </c:if>
                                    <c:if test="${sessionScope.role == 'PGV'}">
                                        <li>
                                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/registration">
                                                 <i class="bi bi-pencil-square"></i> <s:message code="sidebar.menu.registration"/>
                                             </a>
                                         </li>
                                    </c:if>

                                    <%-- Mark Entry: KHOA and PGV (Full access) --%>
                                        <c:if test="${sessionScope.role == 'KHOA' || sessionScope.role == 'PGV'}">
                                            <li>
                                                <a class="nav-link" href="${pageContext.request.contextPath}/admin/mark">
                                                    <i class="bi bi-check2-square"></i> <s:message code="sidebar.menu.enterGrades"/>
                                                </a>
                                            </li>
                                        </c:if>

                                        <%-- Faculty: PGV only --%>
                                            <c:if test="${sessionScope.role == 'PGV'}">
                                                <li>
                                                    <a class="nav-link"
                                                        href="${pageContext.request.contextPath}/admin/faculty">
                                                        <i class="bi bi-diagram-3"></i> <s:message code="sidebar.menu.faculty"/>
                                                    </a>
                                                </li>
                                            </c:if>

                                            <%-- Reports: PGV and KHOA --%>
                                                <c:if
                                                    test="${sessionScope.role == 'PGV' || sessionScope.role == 'KHOA'}">
                                                    <li>
                                                        <a class="nav-link"
                                                            href="${pageContext.request.contextPath}/admin/report">
                                                            <i class="bi bi-file-earmark-text"></i> <s:message code="sidebar.menu.reports"/>
                                                        </a>
                                                    </li>
                                                    <li>
                                                        <a class="nav-link"
                                                            href="${pageContext.request.contextPath}/admin/payment">
                                                            <i class="bi bi-pie-chart"></i> <s:message code="payment.stats.title"/>
                                                        </a>
                                                    </li>
                                                </c:if>

                                                <%-- Role Management: PGV only --%>
                                                    <c:if test="${sessionScope.role == 'PGV'}">
                                                         <li>
                                                             <a class="nav-link"
                                                                 href="${pageContext.request.contextPath}/admin/account">
                                                                 <i class="bi bi-person-lines-fill"></i> <s:message code="sidebar.menu.accountRoles"/>
                                                             </a>
                                                         </li>
                                                     </c:if>
                </ul>
            </nav>

            <div class="sidebar-footer">
                <div class="sidebar-roles">
                    <c:choose>
                        <c:when test="${sessionScope.role == 'PGV'}">
                            <span class="role-badge role-pgv active"><s:message code="role.display.pgv"/></span>
                        </c:when>
                        <c:when test="${sessionScope.role == 'KHOA'}">
                            <span class="role-badge role-khoa active"><s:message code="role.display.faculty"/></span>
                        </c:when>
                        <c:when test="${sessionScope.role == 'SINHVIEN'}">
                            <span class="role-badge role-sv active"><s:message code="role.display.student"/></span>
                        </c:when>
                    </c:choose>
                </div>
                <a href="${pageContext.request.contextPath}/logout" class="sidebar-logout text-decoration-none"
                    aria-label="<s:message code="sidebar.tooltip.logout"/>">
                    <i class="bi bi-box-arrow-left" aria-hidden="true"></i> <s:message code="sidebar.btn.logout"/>
                </a>
            </div>
        </aside>

        <script>
            (function () {
                const currentPath = window.location.pathname;
                document.querySelectorAll('.sidebar-nav .nav-link').forEach(link => {
                    const href = link.getAttribute('href');
                    if (href) {
                        try {
                            const linkPath = new URL(href, window.location.origin).pathname;
                            if (currentPath === linkPath || (linkPath !== '/' && currentPath.startsWith(linkPath))) {
                                link.classList.add('active');
                            }
                        } catch (e) {
                            // Fallback
                            if (currentPath.includes(href)) {
                                link.classList.add('active');
                            }
                        }
                    }
                });
            })();
        </script>
