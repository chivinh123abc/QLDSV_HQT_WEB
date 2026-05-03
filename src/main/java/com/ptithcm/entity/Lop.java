package com.ptithcm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "LOP")
public class Lop {
    @Id
    @Column(name = "MALOP")
    private String maLop;

    @Column(name = "TENLOP")
    private String tenLop;

    @Column(name = "KHOAHOC")
    private int khoaHoc;

    @Column(name = "MAKHOA")
    private String maKhoa;

    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }
    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }
    public int getKhoaHoc() { return khoaHoc; }
    public void setKhoaHoc(int khoaHoc) { this.khoaHoc = khoaHoc; }
    public String getMaKhoa() { return maKhoa; }
    public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }
}