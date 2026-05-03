package com.ptithcm.entity;

import java.io.Serializable;
import java.util.Objects;

public class DangKyId implements Serializable {
    private int maLTC;
    private String maSV;

    public DangKyId() {}

    public DangKyId(int maLTC, String maSV) {
        this.maLTC = maLTC;
        this.maSV = maSV;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DangKyId dangKyId = (DangKyId) o;
        return maLTC == dangKyId.maLTC && Objects.equals(maSV, dangKyId.maSV);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maLTC, maSV);
    }
}
