package com.ptithcm.modules.creditclass.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreditClassDTO {

    private String maLTC;

    @NotBlank(message = "Niên khóa không được để trống!")
    private String nienKhoa;

    @Min(value = 1, message = "Học kỳ phải lớn hơn 0!")
    private int hocKy;

    @NotBlank(message = "Mã môn học không được để trống!")
    private String maMH;

    @Min(value = 1, message = "Nhóm phải lớn hơn 0!")
    private int nhom;

    @NotBlank(message = "Mã giảng viên không được để trống!")
    private String maGV;

    @NotBlank(message = "Mã khoa không được để trống!")
    private String maKhoa;

    @Min(value = 1, message = "Số sinh viên tối thiểu phải lớn hơn 0!")
    private int soSVToiThieu;

    private int soSVToiDa;

    private boolean huyLop;

    public String getMaLTC() {
        return maLTC;
    }

    public void setMaLTC(String maLTC) {
        this.maLTC = maLTC;
    }

    public String getNienKhoa() {
        return nienKhoa;
    }

    public void setNienKhoa(String nienKhoa) {
        this.nienKhoa = nienKhoa;
    }

    public int getHocKy() {
        return hocKy;
    }

    public void setHocKy(int hocKy) {
        this.hocKy = hocKy;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public int getNhom() {
        return nhom;
    }

    public void setNhom(int nhom) {
        this.nhom = nhom;
    }

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
    }

    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }

    public int getSoSVToiThieu() {
        return soSVToiThieu;
    }

    public void setSoSVToiThieu(int soSVToiThieu) {
        this.soSVToiThieu = soSVToiThieu;
    }

    public int getSoSVToiDa() {
        return soSVToiDa;
    }

    public void setSoSVToiDa(int soSVToiDa) {
        this.soSVToiDa = soSVToiDa;
    }

    public boolean isHuyLop() {
        return huyLop;
    }

    public void setHuyLop(boolean huyLop) {
        this.huyLop = huyLop;
    }
}
