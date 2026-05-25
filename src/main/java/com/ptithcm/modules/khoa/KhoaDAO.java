package com.ptithcm.modules.khoa;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ptithcm.entities.Khoa;
import com.ptithcm.shared.bases.BaseDAO;

@Repository
public class KhoaDAO extends BaseDAO<Khoa, String> {

    public KhoaDAO() {
        super(Khoa.class);
    }

    public Long countLopByKhoa(String maKhoa) {
        return getSession().createQuery("SELECT COUNT(*) FROM Lop WHERE maKhoa = :maKhoa", Long.class)
                .setParameter("maKhoa", maKhoa).uniqueResult();
    }

    public Long countGiangVienByKhoa(String maKhoa) {
        return getSession().createQuery("SELECT COUNT(*) FROM GiangVien WHERE maKhoa = :maKhoa", Long.class)
                .setParameter("maKhoa", maKhoa).uniqueResult();
    }

    public Long countLtcByKhoa(String maKhoa) {
        return getSession().createQuery("SELECT COUNT(*) FROM LopTinChi WHERE maKhoa = :maKhoa", Long.class)
                .setParameter("maKhoa", maKhoa).uniqueResult();
    }

    public List<String> listTrimmedKhoaFromLop() {
        return getSession().createQuery("SELECT distinct trim(maKhoa) FROM Lop", String.class).list();
    }

    public List<String> listTrimmedKhoaFromGiangVien() {
        return getSession().createQuery("SELECT distinct trim(maKhoa) FROM GiangVien", String.class).list();
    }

    public List<String> listTrimmedKhoaFromLtc() {
        return getSession().createQuery("SELECT distinct trim(maKhoa) FROM LopTinChi", String.class).list();
    }
}
