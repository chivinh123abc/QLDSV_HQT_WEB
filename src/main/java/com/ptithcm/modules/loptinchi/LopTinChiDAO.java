package com.ptithcm.modules.loptinchi;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ptithcm.entities.GiangVien;
import com.ptithcm.entities.Khoa;
import com.ptithcm.entities.LopTinChi;
import com.ptithcm.entities.MonHoc;
import com.ptithcm.shared.bases.BaseDAO;

@Repository
public class LopTinChiDAO extends BaseDAO<LopTinChi, String> {

    public LopTinChiDAO() {
        super(LopTinChi.class);
    }

    public List<LopTinChi> listLtcByKhoa(String maKhoa) {
        return getSession().createQuery("FROM LopTinChi WHERE khoa.maKhoa = :maKhoa", LopTinChi.class)
                .setParameter("maKhoa", maKhoa).list();
    }

    public List<LopTinChi> listAllLtc() {
        return getSession().createQuery("FROM LopTinChi", LopTinChi.class).list();
    }

    public List<Khoa> listKhoa() {
        return getSession().createQuery("FROM Khoa", Khoa.class).list();
    }

    public List<MonHoc> listMonHoc() {
        return getSession().createQuery("FROM MonHoc", MonHoc.class).list();
    }

    public List<GiangVien> listGiangVienByKhoa(String maKhoa) {
        return getSession().createQuery("FROM GiangVien WHERE khoa.maKhoa = :maKhoa", GiangVien.class)
                .setParameter("maKhoa", maKhoa).list();
    }

    public List<GiangVien> listAllGiangVien() {
        return getSession().createQuery("FROM GiangVien", GiangVien.class).list();
    }

    public List<String> listLtcIdsWithRegistrations() {
        return getSession().createQuery("SELECT distinct lopTinChi.maLTC FROM DangKy", String.class).list();
    }

    public Long countDuplicateLtc(String nienKhoa, int hocKy, String maMH, int nhom) {
        String hql = "SELECT COUNT(*) FROM LopTinChi WHERE nienKhoa = :nk AND hocKy = :hk AND monHoc.maMH = :mh AND nhom = :nh";
        return getSession().createQuery(hql, Long.class).setParameter("nk", nienKhoa).setParameter("hk", hocKy)
                .setParameter("mh", maMH).setParameter("nh", nhom).uniqueResult();
    }

    public String getMaxLtcId() {
        return getSession().createQuery("SELECT MAX(maLTC) FROM LopTinChi", String.class).uniqueResult();
    }

    public Long countRegistrationsByLtc(String maLTC) {
        return getSession().createQuery("SELECT COUNT(*) FROM DangKy WHERE lopTinChi.maLTC = :maLTC", Long.class)
                .setParameter("maLTC", maLTC).uniqueResult();
    }
}
