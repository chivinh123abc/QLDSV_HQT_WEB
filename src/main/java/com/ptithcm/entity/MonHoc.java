package com.ptithcm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "MONHOC")
public class MonHoc {
    @Id
    @Column(name = "MAMH")
    private String maMH;

    @Column(name = "TENMH")
    private String tenMH;

    @Column(name = "SOTIET_LT")
    private int soTietLT;

    @Column(name = "SOTIET_TH")
    private int soTietTH;

    @Transient
    private boolean canDelete = true;

    public String getMaMH() { return maMH; }
    public void setMaMH(String maMH) { this.maMH = maMH; }
    public String getTenMH() { return tenMH; }
    public void setTenMH(String tenMH) { this.tenMH = tenMH; }
    public int getSoTietLT() { return soTietLT; }
    public void setSoTietLT(int soTietLT) { this.soTietLT = soTietLT; }
    public int getSoTietTH() { return soTietTH; }
    public void setSoTietTH(int soTietTH) { this.soTietTH = soTietTH; }
    public boolean isCanDelete() { return canDelete; }
    public void setCanDelete(boolean canDelete) { this.canDelete = canDelete; }
}