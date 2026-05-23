package com.ptithcm.modules.loptinchi;

import com.ptithcm.entity.GiangVien;
import com.ptithcm.entity.Khoa;
import com.ptithcm.entity.LopTinChi;
import com.ptithcm.entity.MonHoc;
import com.ptithcm.shared.base.BaseDAO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class LopTinChiDAO extends BaseDAO<LopTinChi, Integer> {

    public LopTinChiDAO() {
        super(LopTinChi.class);
    }

    public List<LopTinChi> listLtcByKhoa(String maKhoa) {
        return getSession().createQuery("FROM LopTinChi WHERE maKhoa = :maKhoa", LopTinChi.class)
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
        return getSession().createQuery("FROM GiangVien WHERE maKhoa = :maKhoa", GiangVien.class)
                .setParameter("maKhoa", maKhoa).list();
    }

    public List<GiangVien> listAllGiangVien() {
        return getSession().createQuery("FROM GiangVien", GiangVien.class).list();
    }

    public List<Integer> listLtcIdsWithRegistrations() {
        return getSession().createQuery("SELECT distinct maLTC FROM DangKy", Integer.class).list();
    }

    public Long countDuplicateLtc(String nienKhoa, int hocKy, String maMH, int nhom) {
        String hql = "SELECT COUNT(*) FROM LopTinChi WHERE nienKhoa = :nk AND hocKy = :hk AND maMH = :mh AND nhom = :nh";
        return getSession().createQuery(hql, Long.class).setParameter("nk", nienKhoa).setParameter("hk", hocKy)
                .setParameter("mh", maMH).setParameter("nh", nhom).uniqueResult();
    }

    public Integer getMaxLtcId() {
        return getSession().createQuery("SELECT MAX(maLTC) FROM LopTinChi", Integer.class).uniqueResult();
    }

    public Long countRegistrationsByLtc(int maLTC) {
        return getSession().createQuery("SELECT COUNT(*) FROM DangKy WHERE maLTC = :maLTC", Long.class)
                .setParameter("maLTC", maLTC).uniqueResult();
    }
}
