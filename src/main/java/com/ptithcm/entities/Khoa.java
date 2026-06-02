package com.ptithcm.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.ptithcm.entities.base.LuuVetThoiGian;

@Entity
@Table(name = "khoa")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Khoa extends LuuVetThoiGian {
    @Id
    @Column(name = "id")
    private String maKhoa;

    @Column(name = "ten_khoa")
    private String tenKhoa;

    @JsonIgnore
    @OneToMany(mappedBy = "khoa", fetch = FetchType.LAZY)
    private List<Lop> dsLop;

    @JsonIgnore
    @OneToMany(mappedBy = "khoa", fetch = FetchType.LAZY)
    private List<GiangVien> dsGiangVien;

    @JsonIgnore
    @OneToMany(mappedBy = "khoa", fetch = FetchType.LAZY)
    private List<LopTinChi> dsLopTinChi;

    @Transient
    private boolean canDelete = true;

    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }

    public String getTenKhoa() {
        return tenKhoa;
    }

    public void setTenKhoa(String tenKhoa) {
        this.tenKhoa = tenKhoa;
    }

    public List<Lop> getDsLop() {
        return dsLop;
    }

    public void setDsLop(List<Lop> dsLop) {
        this.dsLop = dsLop;
    }

    public List<GiangVien> getDsGiangVien() {
        return dsGiangVien;
    }

    public void setDsGiangVien(List<GiangVien> dsGiangVien) {
        this.dsGiangVien = dsGiangVien;
    }

    public List<LopTinChi> getDsLopTinChi() {
        return dsLopTinChi;
    }

    public void setDsLopTinChi(List<LopTinChi> dsLopTinChi) {
        this.dsLopTinChi = dsLopTinChi;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }
}
