package com.ptithcm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.List;
import com.ptithcm.entity.base.LuuVetThoiGian;

@Entity
@Table(name = "giang_vien")
public class GiangVien extends LuuVetThoiGian {
    @Id
    @Column(name = "id")
    private String maGV;

    @ManyToOne
    @JoinColumn(name = "khoa_id", nullable = false)
    private Khoa khoa;

    @OneToMany(mappedBy = "giangVien", fetch = FetchType.LAZY)
    private List<LopTinChi> dsLopTinChi;

    @Column(name = "ho")
    private String ho;

    @Column(name = "ten")
    private String ten;

    @Column(name = "hoc_vi")
    private String hocVi;

    @Column(name = "hoc_ham")
    private String hocHam;

    @Column(name = "chuyen_mon")
    private String chuyenMon;

    @Column(name = "mat_khau")
    private String password;

    @Column(name = "vai_tro")
    private String role = "KHOA";

    @Transient
    private boolean canDelete = true;

    @Transient
    private String maKhoa;

    public String getMaKhoa() {
        if (maKhoa != null) {
            return maKhoa;
        }
        return khoa != null ? khoa.getMaKhoa() : null;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
    }

    public Khoa getKhoa() {
        return khoa;
    }

    public void setKhoa(Khoa khoa) {
        this.khoa = khoa;
    }

    public List<LopTinChi> getDsLopTinChi() {
        return dsLopTinChi;
    }

    public void setDsLopTinChi(List<LopTinChi> dsLopTinChi) {
        this.dsLopTinChi = dsLopTinChi;
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

    public String getChuyenMon() {
        return chuyenMon;
    }

    public void setChuyenMon(String chuyenMon) {
        this.chuyenMon = chuyenMon;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }
}
