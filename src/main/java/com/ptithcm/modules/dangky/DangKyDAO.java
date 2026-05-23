package com.ptithcm.modules.dangky;

import com.ptithcm.entity.DangKy;
import com.ptithcm.entity.DangKyId;
import com.ptithcm.entity.LopTinChi;
import com.ptithcm.entity.SinhVien;
import com.ptithcm.shared.base.BaseDAO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DangKyDAO extends BaseDAO<DangKy, DangKyId> {

    public DangKyDAO() {
        super(DangKy.class);
    }

    public List<DangKy> listRegistration() {
        return getSession().createQuery("FROM DangKy", DangKy.class).list();
    }

    public SinhVien getStudentById(String maSV) {
        return getSession().get(SinhVien.class, maSV);
    }

    public LopTinChi getLtcById(int maLTC) {
        return getSession().get(LopTinChi.class, maLTC);
    }

    public Long countSubjectRegisteredInSemester(String maSV, String maMH, String nienKhoa, int hocKy,
            int currentMaLTC) {
        String hqlSubject = "SELECT count(dk) FROM DangKy dk " + "JOIN LopTinChi ltc_ref ON dk.maLTC = ltc_ref.maLTC "
                + "WHERE upper(trim(dk.maSV)) = upper(trim(:maSV)) " + "AND dk.huyDangKy = false "
                + "AND upper(trim(ltc_ref.maMH)) = upper(trim(:maMH)) "
                + "AND upper(trim(ltc_ref.nienKhoa)) = upper(trim(:nk)) " + "AND ltc_ref.hocKy = :hk "
                + "AND dk.maLTC != :currentMaLTC";
        return getSession().createQuery(hqlSubject, Long.class).setParameter("maSV", maSV).setParameter("maMH", maMH)
                .setParameter("nk", nienKhoa).setParameter("hk", hocKy).setParameter("currentMaLTC", currentMaLTC)
                .uniqueResult();
    }

    public List<LopTinChi> getAvailableClasses() {
        return getSession().createQuery("FROM LopTinChi WHERE huyLop = false", LopTinChi.class).list();
    }
}
