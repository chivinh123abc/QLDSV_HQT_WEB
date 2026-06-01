package com.ptithcm.modules.home;

import org.springframework.stereotype.Repository;

import com.ptithcm.entities.SinhVien;
import com.ptithcm.shared.bases.BaseDAO;
import com.ptithcm.shared.enums.TrangThaiDangKy;

@Repository
public class HomeDAO extends BaseDAO<SinhVien, String> {

    public HomeDAO() {
        super(SinhVien.class);
    }

    public Long getRegisteredCount(String maSV) {
        String hqlCount = "SELECT count(dk) FROM DangKy dk WHERE dk.sinhVien.maSV = :maSV AND dk.trangThaiDangKy = :hieuLuc";
        return getSession().createQuery(hqlCount, Long.class).setParameter("maSV", maSV != null ? maSV.trim() : "")
                .setParameter("hieuLuc", TrangThaiDangKy.HIEU_LUC).uniqueResult();
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
