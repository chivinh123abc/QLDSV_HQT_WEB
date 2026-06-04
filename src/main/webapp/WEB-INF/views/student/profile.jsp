<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title><s:message code="profile.title"/> | QLDSV_HTC</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
</head>
<body class="app-body">
    <!-- Navbar -->
    <jsp:include page="/WEB-INF/views/shared/header.jsp" />

    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-3 col-lg-2 d-md-block sidebar collapse bg-white shadow-sm">
                <jsp:include page="/WEB-INF/views/shared/sidebar.jsp" />
            </div>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4 content-wrapper">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-4 border-bottom">
                    <h1 class="h2 text-primary fw-bold">
                        <i class="bi bi-person-circle me-2"></i><s:message code="profile.title"/>
                    </h1>
                </div>

                <!-- Flash Alert Messages -->
                <c:if test="${not empty successMsg}">
                    <div class="alert alert-success alert-dismissible fade show shadow-sm border-0 rounded-3 mb-4" role="alert">
                        <div class="d-flex align-items-center">
                            <i class="bi bi-check-circle-fill me-2 fs-5"></i>
                            <div>${successMsg}</div>
                        </div>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>
                <c:if test="${not empty errorMsg}">
                    <div class="alert alert-danger alert-dismissible fade show shadow-sm border-0 rounded-3 mb-4" role="alert">
                        <div class="d-flex align-items-center">
                            <i class="bi bi-exclamation-triangle-fill me-2 fs-5"></i>
                            <div>${errorMsg}</div>
                        </div>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <div class="row g-4">
                    <!-- Left: Profile Details Card -->
                    <div class="col-lg-6">
                        <div class="card border-0 shadow-sm rounded-4 h-100">
                            <div class="card-body p-4">
                                <div class="text-center border-bottom pb-4 mb-4">
                                    <div class="position-relative d-inline-block mb-3">
                                        <c:choose>
                                            <c:when test="${not empty sessionScope.user.avatar}">
                                                <img id="avatarPreviewImg" src="${pageContext.request.contextPath}${sessionScope.user.avatar}" 
                                                     alt="Avatar" class="rounded-circle shadow-sm border border-3 border-primary-subtle" 
                                                     style="width: 120px; height: 120px; object-fit: cover;">
                                            </c:when>
                                            <c:otherwise>
                                                <img id="avatarPreviewImg" src="" 
                                                     alt="Avatar" class="rounded-circle shadow-sm border border-3 border-primary-subtle d-none" 
                                                     style="width: 120px; height: 120px; object-fit: cover;">
                                                <div id="avatarPlaceholder" class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center mx-auto shadow-sm" 
                                                     style="width: 100px; height: 100px; font-size: 2.5rem; font-weight: 700; text-transform: uppercase;">
                                                    <c:choose>
                                                        <c:when test="${sessionScope.role == 'SINHVIEN' && not empty studentProfile}">
                                                            ${studentProfile.ten.substring(0,1)}
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${account.tenDangNhap.substring(0,1)}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                        <form action="${pageContext.request.contextPath}/student/update-avatar" method="POST" enctype="multipart/form-data" class="mt-3 mx-auto" style="max-width: 250px;" id="avatarUploadForm">
                                            <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                            <div class="mb-2 text-start">
                                                <input type="file" name="avatarFile" id="avatarFileInput" class="d-none" accept="image/png, image/jpeg" required />
                                                <label for="avatarFileInput" class="btn btn-sm btn-outline-secondary w-100 rounded-3 mb-1" style="cursor: pointer;">
                                                    <i class="bi bi-folder2-open me-1"></i>
                                                    <s:message code="profile.avatar.select_file"/>
                                                </label>
                                                <div id="fileSelectedName" class="text-muted small text-center text-truncate mx-auto" style="max-width: 250px;">
                                                    <s:message code="profile.avatar.no_file"/>
                                                </div>
                                            </div>
                                            <button type="submit" class="btn btn-sm btn-outline-primary w-100 rounded-pill py-1.5 fw-semibold">
                                                <i class="bi bi-camera me-1"></i><s:message code="profile.avatar.update_btn"/>
                                            </button>
                                            <div id="avatarFileError" class="text-danger small mt-1" style="display: none;"></div>
                                        </form>
                                    </div>
                                    <h4 class="fw-bold mb-1 text-primary mt-2">
                                        <c:choose>
                                            <c:when test="${not empty studentProfile}">
                                                ${studentProfile.ho} ${studentProfile.ten}
                                            </c:when>
                                            <c:otherwise>
                                                ${account.tenDangNhap}
                                            </c:otherwise>
                                        </c:choose>
                                    </h4>
                                    <span class="badge bg-secondary-subtle text-secondary rounded-pill px-3 py-1 text-uppercase">${account.phanQuyen}</span>
                                </div>

                                <div class="d-flex flex-column gap-3">
                                    <div class="row">
                                        <div class="col-sm-4 text-muted fw-semibold"><s:message code="profile.username"/>:</div>
                                        <div class="col-sm-8 fw-semibold">${account.tenDangNhap}</div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-4 text-muted fw-semibold"><s:message code="profile.email"/>:</div>
                                        <div class="col-sm-8">${account.email}</div>
                                    </div>

                                    <!-- Student Profile Specifics -->
                                    <c:if test="${not empty studentProfile}">
                                        <div class="row">
                                            <div class="col-sm-4 text-muted fw-semibold"><s:message code="student.lbl.classCode"/>:</div>
                                            <div class="col-sm-8 fw-semibold">${studentProfile.maLop}</div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-4 text-muted fw-semibold"><s:message code="student.lbl.dob.upper"/>:</div>
                                            <div class="col-sm-8">
                                                <fmt:formatDate value="${studentProfile.ngaySinh}" pattern="dd/MM/yyyy"/>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-4 text-muted fw-semibold"><s:message code="student.lbl.gender.upper"/>:</div>
                                            <div class="col-sm-8">
                                                <c:choose>
                                                    <c:when test="${studentProfile.phai == 'NAM' || studentProfile.phai == 'Nam'}">
                                                        <s:message code="global.gender.male"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <s:message code="global.gender.female"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-4 text-muted fw-semibold"><s:message code="student.address"/>:</div>
                                            <div class="col-sm-8 text-secondary">${studentProfile.diaChi}</div>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Right: Password Reset Card -->
                    <div class="col-lg-6">
                        <div class="card border-0 shadow-sm rounded-4 h-100">
                            <div class="card-body p-4">
                                <h4 class="card-title fw-bold mb-4 text-secondary">
                                    <i class="bi bi-shield-lock me-2 text-primary"></i><s:message code="profile.change_password.title"/>
                                </h4>

                                <!-- Form 1: Trigger Verification Code (OTP) -->
                                <div class="mb-4 bg-light rounded-3 p-3">
                                    <p class="small text-muted mb-3">
                                        <s:message code="profile.otp.instruction"/>
                                    </p>
                                    <form action="${pageContext.request.contextPath}/student/send-otp" method="POST">
                                        <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                        <button type="submit" class="btn btn-outline-primary rounded-pill px-4 btn-sm d-flex align-items-center gap-2">
                                            <i class="bi bi-envelope-at-fill"></i>
                                            <s:message code="profile.get_otp" />
                                        </button>
                                    </form>
                                </div>

                                <!-- Form 2: Password Update details -->
                                <form action="${pageContext.request.contextPath}/student/change-password" method="POST">
                                    <input type="hidden" name="csrf_token" value="${csrfToken}" />
                                    
                                    <s:message code="profile.old_password.placeholder" var="oldPwdPlaceholder"/>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold text-muted small text-uppercase"><s:message code="profile.old_password"/> <span class="text-danger">*</span></label>
                                        <div class="input-group">
                                            <input type="password" name="oldPassword" id="oldPassword" class="form-control rounded-start-3" required placeholder="${oldPwdPlaceholder}"/>
                                            <button class="btn btn-outline-secondary rounded-end-3 toggle-password" type="button" data-target="oldPassword">
                                                <i class="bi bi-eye"></i>
                                            </button>
                                        </div>
                                    </div>
                                    <s:message code="profile.new_password.placeholder" var="newPwdPlaceholder"/>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold text-muted small text-uppercase"><s:message code="profile.new_password"/> <span class="text-danger">*</span></label>
                                        <div class="input-group">
                                            <input type="password" name="newPassword" id="newPassword" class="form-control rounded-start-3" required placeholder="${newPwdPlaceholder}" minlength="6"/>
                                            <button class="btn btn-outline-secondary rounded-end-3 toggle-password" type="button" data-target="newPassword">
                                                <i class="bi bi-eye"></i>
                                            </button>
                                        </div>
                                    </div>
                                    <s:message code="profile.otp.placeholder" var="otpPlaceholder"/>
                                    <div class="mb-4">
                                        <label class="form-label fw-bold text-muted small text-uppercase"><s:message code="profile.otp"/> <span class="text-danger">*</span></label>
                                        <input type="text" name="otpCode" class="form-control rounded-3" required placeholder="${otpPlaceholder}" autocomplete="off"/>
                                    </div>

                                    <button type="submit" class="btn btn-primary w-100 rounded-pill py-2.5 fw-semibold d-flex align-items-center justify-content-center gap-2">
                                        <i class="bi bi-check-circle-fill"></i>
                                        <s:message code="profile.submit"/>
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener("DOMContentLoaded", function() {
            const fileInput = document.getElementById("avatarFileInput");
            const errorDiv = document.getElementById("avatarFileError");
            const form = document.getElementById("avatarUploadForm");

            if (fileInput && form) {
                const msgInvalidType = "<s:message code='profile.avatar.err.invalid_type' text='Chỉ chấp nhận file ảnh định dạng PNG hoặc JPEG!'/>";
                const msgTooLarge = "<s:message code='profile.avatar.err.too_large' text='Kích thước file không được vượt quá 2MB!'/>";
                const msgNoFile = "<s:message code='profile.avatar.err.no_file' text='Vui lòng chọn file!'/>";
                const fileSelectedNameDiv = document.getElementById("fileSelectedName");
                const msgNoFileChosen = "<s:message code='profile.avatar.no_file' text='Không có tệp nào được chọn'/>";

                // Store original preview states
                const avatarPreviewImg = document.getElementById("avatarPreviewImg");
                const avatarPlaceholder = document.getElementById("avatarPlaceholder");
                const originalSrc = avatarPreviewImg ? avatarPreviewImg.src : "";
                const originalImgHidden = avatarPreviewImg ? avatarPreviewImg.classList.contains("d-none") : true;
                const originalPlaceholderHidden = avatarPlaceholder ? avatarPlaceholder.classList.contains("d-none") : false;

                function resetPreview() {
                    if (avatarPreviewImg) {
                        avatarPreviewImg.src = originalSrc;
                        if (originalImgHidden) {
                            avatarPreviewImg.classList.add("d-none");
                        } else {
                            avatarPreviewImg.classList.remove("d-none");
                        }
                    }
                    if (avatarPlaceholder) {
                        if (originalPlaceholderHidden) {
                            avatarPlaceholder.classList.add("d-none");
                        } else {
                            avatarPlaceholder.classList.remove("d-none");
                        }
                    }
                }

                fileInput.addEventListener("change", function() {
                    errorDiv.style.display = "none";
                    errorDiv.textContent = "";

                    if (this.files && this.files[0]) {
                        const file = this.files[0];
                        const maxSize = 2 * 1024 * 1024; // 2MB
                        const allowedTypes = ["image/jpeg", "image/png", "image/jpg"];

                        if (!allowedTypes.includes(file.type)) {
                            errorDiv.textContent = msgInvalidType;
                            errorDiv.style.display = "block";
                            this.value = ""; // Clear selection
                            if (fileSelectedNameDiv) {
                                fileSelectedNameDiv.textContent = msgNoFileChosen;
                            }
                            resetPreview();
                            return;
                        }

                        if (file.size > maxSize) {
                            errorDiv.textContent = msgTooLarge;
                            errorDiv.style.display = "block";
                            this.value = ""; // Clear selection
                            if (fileSelectedNameDiv) {
                                fileSelectedNameDiv.textContent = msgNoFileChosen;
                            }
                            resetPreview();
                            return;
                        }

                        if (fileSelectedNameDiv) {
                            fileSelectedNameDiv.textContent = file.name;
                        }

                        // Load and display preview via FileReader
                        const reader = new FileReader();
                        reader.onload = function(e) {
                            if (avatarPreviewImg) {
                                avatarPreviewImg.src = e.target.result;
                                avatarPreviewImg.classList.remove("d-none");
                            }
                            if (avatarPlaceholder) {
                                avatarPlaceholder.classList.add("d-none");
                            }
                        };
                        reader.readAsDataURL(file);
                    } else {
                        if (fileSelectedNameDiv) {
                            fileSelectedNameDiv.textContent = msgNoFileChosen;
                        }
                        resetPreview();
                    }
                });

                form.addEventListener("submit", function(e) {
                    if (fileInput.files.length === 0) {
                        e.preventDefault();
                        errorDiv.textContent = msgNoFile;
                        errorDiv.style.display = "block";
                        return;
                    }
                    const file = fileInput.files[0];
                    const maxSize = 2 * 1024 * 1024; // 2MB
                    const allowedTypes = ["image/jpeg", "image/png", "image/jpg"];

                    if (!allowedTypes.includes(file.type) || file.size > maxSize) {
                        e.preventDefault();
                        errorDiv.textContent = msgInvalidType;
                        errorDiv.style.display = "block";
                    }
                });
            }

            // Password visibility toggle logic
            document.querySelectorAll('.toggle-password').forEach(button => {
                button.addEventListener('click', function() {
                    const targetId = this.getAttribute('data-target');
                    const input = document.getElementById(targetId);
                    const icon = this.querySelector('i');
                    if (input.type === 'password') {
                        input.type = 'text';
                        icon.classList.remove('bi-eye');
                        icon.classList.add('bi-eye-slash');
                    } else {
                        input.type = 'password';
                        icon.classList.remove('bi-eye-slash');
                        icon.classList.add('bi-eye');
                    }
                });
            });
        });
    </script>
</body>
</html>
