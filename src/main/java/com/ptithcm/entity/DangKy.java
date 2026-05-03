package com.ptithcm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "DANGKY")
@IdClass(DangKyId.class)
public class DangKy {
    @Id
    @Column(name = "MALTC")
    private int maLTC;

    @Id
    @Column(name = "MASV")
    private String maSV;

    @Column(name = "DIEM_CC")
    private Float diemCC;

    @Column(name = "DIEM_GK")
    private Float diemGK;

    @Column(name = "DIEM_CK")
    private Float diemCK;

    @Column(name = "HUYDANGKY")
    private boolean huyDangKy;

    public int getMaLTC() { return maLTC; }
    public void setMaLTC(int maLTC) { this.maLTC = maLTC; }
    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }
    public Float getDiemCC() { return diemCC; }
    public void setDiemCC(Float diemCC) { this.diemCC = diemCC; }
    public Float getDiemGK() { return diemGK; }
    public void setDiemGK(Float diemGK) { this.diemGK = diemGK; }
    public Float getDiemCK() { return diemCK; }
    public void setDiemCK(Float diemCK) { this.diemCK = diemCK; }
    public boolean isHuyDangKy() { return huyDangKy; }
    public void setHuyDangKy(boolean huyDangKy) { this.huyDangKy = huyDangKy; }
}