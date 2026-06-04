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

import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.ptithcm.entities.base.LuuVetThoiGian;

@Entity
@Table(name = "lop")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SQLRestriction("ngay_xoa IS NULL")
public class Lop extends LuuVetThoiGian {
    @Id
    @Column(name = "id")
    private String maLop;

    @Column(name = "ten_lop")
    private String tenLop;

    @Column(name = "khoa_hoc")
    private String khoaHoc;

    @ManyToOne
    @JoinColumn(name = "khoa_id", nullable = false)
    private Khoa khoa;

    @JsonIgnore
    @OneToMany(mappedBy = "lop", fetch = FetchType.LAZY)
    private List<SinhVien> dsSinhVien;

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

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public String getKhoaHoc() {
        return khoaHoc;
    }

    public void setKhoaHoc(String khoaHoc) {
        this.khoaHoc = khoaHoc;
    }

    public Khoa getKhoa() {
        return khoa;
    }

    public void setKhoa(Khoa khoa) {
        this.khoa = khoa;
    }

    public List<SinhVien> getDsSinhVien() {
        return dsSinhVien;
    }

    public void setDsSinhVien(List<SinhVien> dsSinhVien) {
        this.dsSinhVien = dsSinhVien;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }
}
