package com.ptithcm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "SINHVIEN")
public class SinhVien {
    @Id
    @Column(name = "MASV")
    private String maSV;

    @Column(name = "HO")
    private String ho;

    @Column(name = "TEN")
    private String ten;

    @Column(name = "PHAI")
    private String phai;

    @Column(name = "DIACHI")
    private String diaChi;

    @Column(name = "NGAYSINH")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngaySinh;

    @Column(name = "MALOP")
    private String maLop;

    @Column(name = "DANGHIHOC")
    private boolean dangNghiHoc;

    @Column(name = "PASSWORD")
    private String password;

    @Transient private boolean canDelete = true;

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

    public boolean isDangNghiHoc() {
        return dangNghiHoc;
    }

    public void setDangNghiHoc(boolean dangNghiHoc) {
        this.dangNghiHoc = dangNghiHoc;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }
}
