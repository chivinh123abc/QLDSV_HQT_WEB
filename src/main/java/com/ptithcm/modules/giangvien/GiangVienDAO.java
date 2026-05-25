package com.ptithcm.modules.giangvien;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ptithcm.entities.GiangVien;
import com.ptithcm.entities.Khoa;
import com.ptithcm.shared.bases.BaseDAO;

@Repository
public class GiangVienDAO extends BaseDAO<GiangVien, String> {

    public GiangVienDAO() {
        super(GiangVien.class);
    }

    public List<Khoa> listKhoa() {
        return getSession().createQuery("FROM Khoa", Khoa.class).list();
    }

    public List<GiangVien> listGiangVienByKhoa(String maKhoa) {
        return getSession().createQuery("FROM GiangVien WHERE khoa.maKhoa = :maKhoa", GiangVien.class)
                .setParameter("maKhoa", maKhoa).list();
    }

    public List<GiangVien> listAllGiangVien() {
        return getSession().createQuery("FROM GiangVien", GiangVien.class).list();
    }

    public List<String> listLtcMaGV() {
        return getSession()
                .createQuery("SELECT distinct giangVien.maGV FROM LopTinChi WHERE giangVien IS NOT NULL", String.class)
                .list();
    }

    public List<String> listUserMaGV() {
        return new java.util.ArrayList<>();
    }

    public List<GiangVien> getLecturerByTrimmedId(String maGV) {
        return getSession().createQuery("FROM GiangVien WHERE trim(maGV) = trim(:maGV)", GiangVien.class)
                .setParameter("maGV", maGV.trim()).list();
    }

    public Long countLtcByLecturer(String maGV) {
        return getSession()
                .createQuery("SELECT COUNT(*) FROM LopTinChi WHERE trim(giangVien.maGV) = trim(:maGV)", Long.class)
                .setParameter("maGV", maGV.trim()).uniqueResult();
    }

    public Long countUserByLecturer(String maGV) {
        return 0L;
    }
}
