package com.ptithcm.modules.classroom.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClassroomDTO {

    @NotBlank(message = "Mã lớp không được để trống!")
    @Size(max = 10, message = "Mã lớp không được dài quá 10 ký tự!")
    private String maLop;

    @NotBlank(message = "Tên lớp không được để trống!")
    private String tenLop;

    @NotBlank(message = "Mã khoa không được để trống!")
    private String maKhoa;

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }
}
