package com.ptithcm.shared.dtos;

import java.io.Serializable;

public class UserSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String role; // "PGV", "KHOA", "SINHVIEN"
    private String maKhoa;
    private String fullName;
    private String avatar;

    public UserSession() {
    }

    public UserSession(String username, String role, String maKhoa, String fullName) {
        this.username = username;
        this.role = role;
        this.maKhoa = maKhoa;
        this.fullName = fullName;
    }

    public UserSession(String username, String role, String maKhoa, String fullName, String avatar) {
        this.username = username;
        this.role = role;
        this.maKhoa = maKhoa;
        this.fullName = fullName;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
