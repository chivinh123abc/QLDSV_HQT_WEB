package com.ptithcm.entities;

import java.io.Serializable;
import java.util.Objects;

public class DangKyId implements Serializable {
    private String lopTinChi;
    private String sinhVien;

    public DangKyId() {
    }

    public DangKyId(String lopTinChi, String sinhVien) {
        this.lopTinChi = lopTinChi;
        this.sinhVien = sinhVien;
    }

    public String getLopTinChi() {
        return lopTinChi;
    }

    public void setLopTinChi(String lopTinChi) {
        this.lopTinChi = lopTinChi;
    }

    public String getSinhVien() {
        return sinhVien;
    }

    public void setSinhVien(String sinhVien) {
        this.sinhVien = sinhVien;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DangKyId that = (DangKyId) o;
        return Objects.equals(lopTinChi, that.lopTinChi) && Objects.equals(sinhVien, that.sinhVien);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lopTinChi, sinhVien);
    }
}
