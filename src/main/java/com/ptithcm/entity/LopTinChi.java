package com.ptithcm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "LOPTINCHI")
public class LopTinChi {
    @Id
    @Column(name = "MALTC")
    private int maLTC;

    @Column(name = "NIENKHOA")
    private String nienKhoa;

    @Column(name = "HOCKY")
    private int hocKy;

    @Column(name = "MAMH")
    private String maMH;

    @Column(name = "NHOM")
    private int nhom;

    @Column(name = "MAGV")
    private String maGV;

    @Column(name = "MAKHOA")
    private String maKhoa;

    @Column(name = "SOSVTOITHIEU")
    private int soSVToiThieu;

    @Column(name = "HUYLOP")
    private boolean huyLop;

    @Transient
    private boolean canDelete = true;

    public int getMaLTC() { return maLTC; }
    public void setMaLTC(int maLTC) { this.maLTC = maLTC; }
    public String getNienKhoa() { return nienKhoa; }
    public void setNienKhoa(String nienKhoa) { this.nienKhoa = nienKhoa; }
    public int getHocKy() { return hocKy; }
    public void setHocKy(int hocKy) { this.hocKy = hocKy; }
    public String getMaMH() { return maMH; }
    public void setMaMH(String maMH) { this.maMH = maMH; }
    public int getNhom() { return nhom; }
    public void setNhom(int nhom) { this.nhom = nhom; }
    public String getMaGV() { return maGV; }
    public void setMaGV(String maGV) { this.maGV = maGV; }
    public String getMaKhoa() { return maKhoa; }
    public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }
    public int getSoSVToiThieu() { return soSVToiThieu; }
    public void setSoSVToiThieu(int soSVToiThieu) { this.soSVToiThieu = soSVToiThieu; }
    public boolean isHuyLop() { return huyLop; }
    public void setHuyLop(boolean huyLop) { this.huyLop = huyLop; }
    public boolean isCanDelete() { return canDelete; }
    public void setCanDelete(boolean canDelete) { this.canDelete = canDelete; }
}