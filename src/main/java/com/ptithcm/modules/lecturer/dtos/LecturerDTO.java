package com.ptithcm.modules.lecturer.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LecturerDTO {

    @NotBlank(message = "Mã giảng viên không được để trống!")
    @Size(max = 10, message = "Mã giảng viên không được dài quá 10 ký tự!")
    private String maGV;

    @NotBlank(message = "Họ không được để trống!")
    private String ho;

    @NotBlank(message = "Tên không được để trống!")
    private String ten;

    @NotBlank(message = "Mã khoa không được để trống!")
    private String maKhoa;

    private String hocVi;
    private String hocHam;
    private String chuyenMon;

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
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

    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }

    public String getHocVi() {
        return hocVi;
    }

    public void setHocVi(String hocVi) {
        this.hocVi = hocVi;
    }

    public String getHocHam() {
        return hocHam;
    }

    public void setHocHam(String hocHam) {
        this.hocHam = hocHam;
    }

    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getChuyenMon() {
        return chuyenMon;
    }

    public void setChuyenMon(String chuyenMon) {
        this.chuyenMon = chuyenMon;
    }
}
