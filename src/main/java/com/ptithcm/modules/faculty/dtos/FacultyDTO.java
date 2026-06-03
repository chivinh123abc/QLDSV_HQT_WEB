package com.ptithcm.modules.faculty.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FacultyDTO {

    @NotBlank(message = "Mã khoa không được để trống!")
    @Size(max = 10, message = "Mã khoa không được dài quá 10 ký tự!")
    private String maKhoa;

    @NotBlank(message = "Tên khoa không được để trống!")
    private String tenKhoa;

    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }

    public String getTenKhoa() {
        return tenKhoa;
    }

    public void setTenKhoa(String tenKhoa) {
        this.tenKhoa = tenKhoa;
    }
}
