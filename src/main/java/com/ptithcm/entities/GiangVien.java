package com.ptithcm.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.ptithcm.entities.base.LuuVetThoiGian;

@Entity
@Table(name = "giang_vien")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GiangVien extends LuuVetThoiGian {
    @Id
    @Column(name = "id")
    private String maGV;

    @ManyToOne
    @JoinColumn(name = "khoa_id", nullable = false)
    private Khoa khoa;

    @JsonIgnore
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
