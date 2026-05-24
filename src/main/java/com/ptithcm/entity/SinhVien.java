package com.ptithcm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import com.ptithcm.entity.base.LuuVetThoiGian;

@Entity
@Table(name = "sinh_vien")
public class SinhVien extends LuuVetThoiGian {
    @Id
    @Column(name = "id")
    private String maSV;

    @Column(name = "ho")
    private String ho;

    @Column(name = "ten")
    private String ten;

    @Column(name = "phai")
    private String phai;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "ngay_sinh")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngaySinh;

    @ManyToOne
    @JoinColumn(name = "lop_id", nullable = false)
    private Lop lop;

    @OneToMany(mappedBy = "sinhVien", fetch = FetchType.LAZY)
    private List<DangKy> dsDangKy;

    @Column(name = "is_dang_nghi_hoc")
    private boolean dangNghiHoc;

    @Column(name = "mat_khau")
    private String password;

    @Transient
    private boolean canDelete = true;

    @Transient
    private String maLop;

    public String getMaLop() {
        if (maLop != null) {
            return maLop;
        }
        return lop != null ? lop.getMaLop() : null;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

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

    public Lop getLop() {
        return lop;
    }

    public void setLop(Lop lop) {
        this.lop = lop;
    }

    public List<DangKy> getDsDangKy() {
        return dsDangKy;
    }

    public void setDsDangKy(List<DangKy> dsDangKy) {
        this.dsDangKy = dsDangKy;
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
