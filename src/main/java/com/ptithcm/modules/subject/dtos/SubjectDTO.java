package com.ptithcm.modules.subject.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SubjectDTO {

    @NotBlank(message = "Mã môn học không được để trống!")
    @Size(max = 10, message = "Mã môn học không được dài quá 10 ký tự!")
    private String maMH;

    @NotBlank(message = "Tên môn học không được để trống!")
    private String tenMH;

    @Min(value = 0, message = "Số tiết lý thuyết phải lớn hơn hoặc bằng 0!")
    private int soTietLT;

    @Min(value = 0, message = "Số tiết thực hành phải lớn hơn hoặc bằng 0!")
    private int soTietTH;

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getTenMH() {
        return tenMH;
    }

    public void setTenMH(String tenMH) {
        this.tenMH = tenMH;
    }

    public int getSoTietLT() {
        return soTietLT;
    }

    public void setSoTietLT(int soTietLT) {
        this.soTietLT = soTietLT;
    }

    public int getSoTietTH() {
        return soTietTH;
    }

    public void setSoTietTH(int soTietTH) {
        this.soTietTH = soTietTH;
    }
}
