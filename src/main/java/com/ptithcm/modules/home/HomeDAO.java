package com.ptithcm.modules.home;

import com.ptithcm.entity.SinhVien;
import com.ptithcm.shared.base.BaseDAO;
import org.springframework.stereotype.Repository;

@Repository
public class HomeDAO extends BaseDAO<SinhVien, String> {

    public HomeDAO() {
        super(SinhVien.class);
    }

    public Long getRegisteredCount(String maSV) {
        String hqlCount = "SELECT count(dk) FROM DangKy dk WHERE TRIM(dk.maSV) = :maSV AND (dk.huyDangKy = false OR dk.huyDangKy IS NULL)";
        return getSession().createQuery(hqlCount, Long.class).setParameter("maSV", maSV.trim()).uniqueResult();
    }

    public Long getStudentCount() {
        return getSession().createQuery("SELECT COUNT(*) FROM SinhVien", Long.class).uniqueResult();
    }

    public Long getClassCount() {
        return getSession().createQuery("SELECT COUNT(*) FROM Lop", Long.class).uniqueResult();
    }

    public Long getSubjectCount() {
        return getSession().createQuery("SELECT COUNT(*) FROM MonHoc", Long.class).uniqueResult();
    }

    public Long getCreditClassCount() {
        return getSession().createQuery("SELECT COUNT(*) FROM LopTinChi", Long.class).uniqueResult();
    }
}
