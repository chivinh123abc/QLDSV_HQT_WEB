package com.ptithcm.modules.student.dtos;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

public class StudentDTO {

    @NotBlank(message = "Mã sinh viên không được để trống!")
    @Size(max = 10, message = "Mã sinh viên không được dài quá 10 ký tự!")
    private String maSV;

    @NotBlank(message = "Họ sinh viên không được để trống!")
    private String ho;

    @NotBlank(message = "Tên sinh viên không được để trống!")
    private String ten;

    private String phai;
    private String diaChi;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngaySinh;

    @NotBlank(message = "Mã lớp không được để trống!")
    private String maLop;

    private boolean daNghiHoc;

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getPhai() {
        return phai;
    }

    public void setPhai(String phai) {
        this.phai = phai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean isDaNghiHoc() {
        return daNghiHoc;
    }

    public void setDaNghiHoc(boolean daNghiHoc) {
        this.daNghiHoc = daNghiHoc;
    }
}
