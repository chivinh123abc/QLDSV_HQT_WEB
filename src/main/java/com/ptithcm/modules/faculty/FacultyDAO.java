package com.ptithcm.modules.faculty;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ptithcm.entities.Khoa;
import com.ptithcm.shared.bases.BaseDAO;

@Repository
public class FacultyDAO extends BaseDAO<Khoa, String> {

    public FacultyDAO() {
        super(Khoa.class);
    }

    public Long countLopByKhoa(String maKhoa) {
        return getSession().createQuery("SELECT COUNT(*) FROM Lop l WHERE l.khoa.maKhoa = :maKhoa", Long.class)
                .setParameter("maKhoa", maKhoa).uniqueResult();
    }

    public Long countGiangVienByKhoa(String maKhoa) {
        return getSession().createQuery("SELECT COUNT(*) FROM GiangVien gv WHERE gv.khoa.maKhoa = :maKhoa", Long.class)
                .setParameter("maKhoa", maKhoa).uniqueResult();
    }

    public Long countLtcByKhoa(String maKhoa) {
        return getSession()
                .createQuery("SELECT COUNT(*) FROM LopTinChi ltc WHERE ltc.khoa.maKhoa = :maKhoa", Long.class)
                .setParameter("maKhoa", maKhoa).uniqueResult();
    }

    public List<String> listTrimmedKhoaFromLop() {
        List<String> list = getSession().createQuery("SELECT distinct l.khoa.maKhoa FROM Lop l", String.class).list();
        if (list != null) {
            list.replaceAll(s -> s != null ? s.trim() : null);
        }
        return list;
    }

    public List<String> listTrimmedKhoaFromGiangVien() {
        List<String> list = getSession().createQuery("SELECT distinct gv.khoa.maKhoa FROM GiangVien gv", String.class)
                .list();
        if (list != null) {
            list.replaceAll(s -> s != null ? s.trim() : null);
        }
        return list;
    }

    public List<String> listTrimmedKhoaFromLtc() {
        List<String> list = getSession().createQuery("SELECT distinct ltc.khoa.maKhoa FROM LopTinChi ltc", String.class)
                .list();
        if (list != null) {
            list.replaceAll(s -> s != null ? s.trim() : null);
        }
        return list;
    }
}
