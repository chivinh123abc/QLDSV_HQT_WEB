package com.ptithcm.entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.ptithcm.entities.base.LuuVetThoiGian;
import com.ptithcm.shared.enums.TrangThaiHoc;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_hoc")
    private TrangThaiHoc trangThaiHoc = TrangThaiHoc.DANG_HOC;

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

    public boolean isDaNghiHoc() {
        return this.trangThaiHoc == TrangThaiHoc.NGHI_HOC;
    }

    public void setDaNghiHoc(boolean daNghiHoc) {
        this.trangThaiHoc = daNghiHoc ? TrangThaiHoc.NGHI_HOC : TrangThaiHoc.DANG_HOC;
    }

    public TrangThaiHoc getTrangThaiHoc() {
        return trangThaiHoc;
    }

    public void setTrangThaiHoc(TrangThaiHoc trangThaiHoc) {
        this.trangThaiHoc = trangThaiHoc;
    }

    @jakarta.persistence.Version
    @Column(name = "version")
    private Integer version = 0;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }
}
