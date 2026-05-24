package com.ptithcm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import com.ptithcm.entity.base.LuuVetThoiGian;

@Entity
@Table(name = "dang_ky")
@IdClass(DangKyId.class)
public class DangKy extends LuuVetThoiGian {
    @Id
    @ManyToOne
    @JoinColumn(name = "lop_tin_chi_id")
    private LopTinChi lopTinChi;

    @Id
    @ManyToOne
    @JoinColumn(name = "sinh_vien_id")
    private SinhVien sinhVien;

    @Column(name = "diem_chuyen_can")
    private Float diemCC;

    @Column(name = "diem_giua_ky")
    private Float diemGK;

    @Column(name = "diem_cuoi_ky")
    private Float diemCK;

    @Column(name = "is_huy_dang_ky")
    private boolean huyDangKy;

    public LopTinChi getLopTinChi() {
        return lopTinChi;
    }

    public void setLopTinChi(LopTinChi lopTinChi) {
        this.lopTinChi = lopTinChi;
    }

    public SinhVien getSinhVien() {
        return sinhVien;
    }

    public void setSinhVien(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
    }

    public String getMaLTC() {
        return lopTinChi != null ? lopTinChi.getMaLTC() : null;
    }

    public String getMaSV() {
        return sinhVien != null ? sinhVien.getMaSV() : null;
    }

    public Float getDiemCC() {
        return diemCC;
    }

    public void setDiemCC(Float diemCC) {
        this.diemCC = diemCC;
    }

    public Float getDiemGK() {
        return diemGK;
    }

    public void setDiemGK(Float diemGK) {
        this.diemGK = diemGK;
    }

    public Float getDiemCK() {
        return diemCK;
    }

    public void setDiemCK(Float diemCK) {
        this.diemCK = diemCK;
    }

    public boolean isHuyDangKy() {
        return huyDangKy;
    }

    public void setHuyDangKy(boolean huyDangKy) {
        this.huyDangKy = huyDangKy;
    }
}
