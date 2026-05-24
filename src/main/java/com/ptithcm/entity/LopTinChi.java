package com.ptithcm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.List;
import com.ptithcm.entity.base.LuuVetThoiGian;

@Entity
@Table(name = "lop_tin_chi")
public class LopTinChi extends LuuVetThoiGian {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String maLTC;

    @Column(name = "nien_khoa")
    private String nienKhoa;

    @Column(name = "hoc_ky")
    private int hocKy;

    @ManyToOne
    @JoinColumn(name = "mon_hoc_id", nullable = false)
    private MonHoc monHoc;

    @Column(name = "nhom")
    private int nhom;

    @ManyToOne
    @JoinColumn(name = "giang_vien_id", nullable = false)
    private GiangVien giangVien;

    @ManyToOne
    @JoinColumn(name = "khoa_id", nullable = false)
    private Khoa khoa;

    @Column(name = "so_sv_toi_thieu")
    private int soSVToiThieu;

    @Column(name = "so_sv_toi_da")
    private int soSVToiDa;

    @Column(name = "is_huy_lop")
    private boolean huyLop;

    @OneToMany(mappedBy = "lopTinChi", fetch = FetchType.LAZY)
    private List<DangKy> dsDangKy;

    @Transient
    private boolean canDelete = true;

    @Transient
    private String maMH;
    @Transient
    private String maGV;
    @Transient
    private String maKhoa;

    public String getMaMH() {
        if (maMH != null) {
            return maMH;
        }
        return monHoc != null ? monHoc.getMaMH() : null;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getMaGV() {
        if (maGV != null) {
            return maGV;
        }
        return giangVien != null ? giangVien.getMaGV() : null;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
    }

    public String getMaKhoa() {
        if (maKhoa != null) {
            return maKhoa;
        }
        return khoa != null ? khoa.getMaKhoa() : null;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }

    public String getMaLTC() {
        return maLTC;
    }

    public void setMaLTC(String maLTC) {
        this.maLTC = maLTC;
    }

    public String getNienKhoa() {
        return nienKhoa;
    }

    public void setNienKhoa(String nienKhoa) {
        this.nienKhoa = nienKhoa;
    }

    public int getHocKy() {
        return hocKy;
    }

    public void setHocKy(int hocKy) {
        this.hocKy = hocKy;
    }

    public MonHoc getMonHoc() {
        return monHoc;
    }

    public void setMonHoc(MonHoc monHoc) {
        this.monHoc = monHoc;
    }

    public int getNhom() {
        return nhom;
    }

    public void setNhom(int nhom) {
        this.nhom = nhom;
    }

    public GiangVien getGiangVien() {
        return giangVien;
    }

    public void setGiangVien(GiangVien giangVien) {
        this.giangVien = giangVien;
    }

    public Khoa getKhoa() {
        return khoa;
    }

    public void setKhoa(Khoa khoa) {
        this.khoa = khoa;
    }

    public int getSoSVToiThieu() {
        return soSVToiThieu;
    }

    public void setSoSVToiThieu(int soSVToiThieu) {
        this.soSVToiThieu = soSVToiThieu;
    }

    public int getSoSVToiDa() {
        return soSVToiDa;
    }

    public void setSoSVToiDa(int soSVToiDa) {
        this.soSVToiDa = soSVToiDa;
    }

    public boolean isHuyLop() {
        return huyLop;
    }

    public void setHuyLop(boolean huyLop) {
        this.huyLop = huyLop;
    }

    public List<DangKy> getDsDangKy() {
        return dsDangKy;
    }

    public void setDsDangKy(List<DangKy> dsDangKy) {
        this.dsDangKy = dsDangKy;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }
}
