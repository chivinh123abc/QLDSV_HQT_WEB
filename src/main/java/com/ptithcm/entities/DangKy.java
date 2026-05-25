package com.ptithcm.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.ptithcm.entities.base.LuuVetThoiGian;
import com.ptithcm.shared.enums.TrangThaiDangKy;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_dang_ky")
    private TrangThaiDangKy trangThaiDangKy = TrangThaiDangKy.HIEU_LUC;

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
        return this.trangThaiDangKy == TrangThaiDangKy.DA_HUY;
    }

    public void setHuyDangKy(boolean huyDangKy) {
        this.trangThaiDangKy = huyDangKy ? TrangThaiDangKy.DA_HUY : TrangThaiDangKy.HIEU_LUC;
    }

    public TrangThaiDangKy getTrangThaiDangKy() {
        return trangThaiDangKy;
    }

    public void setTrangThaiDangKy(TrangThaiDangKy trangThaiDangKy) {
        this.trangThaiDangKy = trangThaiDangKy;
    }
}
