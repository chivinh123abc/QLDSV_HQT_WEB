package com.ptithcm.modules.account.dtos;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AccountSaveDTO {

    @NotBlank(message = "Tên đăng nhập không được để trống.")
    private String username;

    private String password;

    @NotBlank(message = "Nhóm quyền không được để trống.")
    private String roleId;

    @NotBlank(message = "Email không được để trống.")
    @Email(message = "Email không đúng định dạng.")
    private String email;

    @NotBlank(message = "Chế độ không được để trống.")
    private String mode;

    private String userId;

    @AssertTrue(message = "Mật khẩu không được để trống khi cấp tài khoản mới.")
    public boolean isValidPasswordForMode() {
        if ("add".equalsIgnoreCase(mode)) {
            return password != null && !password.trim().isEmpty();
        }
        return true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
