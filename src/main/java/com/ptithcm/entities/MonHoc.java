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
@Table(name = "mon_hoc")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MonHoc extends LuuVetThoiGian {
    @Id
    @Column(name = "id")
    private String maMH;

    @Column(name = "ten_mon_hoc")
    private String tenMH;

    @Column(name = "so_tiet_ly_thuyet")
    private int soTietLT;

    @Column(name = "so_tiet_thuc_hanh")
    private int soTietTH;

    @JsonIgnore
    @OneToMany(mappedBy = "monHoc", fetch = FetchType.LAZY)
    private List<LopTinChi> dsLopTinChi;

    @Transient
    private boolean canDelete = true;

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getTenMH() {
        return tenMH;
    }

    public void setTenMH(String tenMH) {
        this.tenMH = tenMH;
    }

    public int getSoTietLT() {
        return soTietLT;
    }

    public void setSoTietLT(int soTietLT) {
        this.soTietLT = soTietLT;
    }

    public int getSoTietTH() {
        return soTietTH;
    }

    public void setSoTietTH(int soTietTH) {
        this.soTietTH = soTietTH;
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
