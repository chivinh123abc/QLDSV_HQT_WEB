package com.ptithcm.modules.registration;

import java.util.List;

import org.hibernate.LockMode;
import org.springframework.stereotype.Repository;

import com.ptithcm.entities.DangKy;
import com.ptithcm.entities.DangKyId;
import com.ptithcm.entities.LopTinChi;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.shared.bases.BaseDAO;
import com.ptithcm.shared.enums.TrangThaiDangKy;
import com.ptithcm.shared.enums.TrangThaiLop;

@Repository
public class RegistrationDAO extends BaseDAO<DangKy, DangKyId> {

    public RegistrationDAO() {
        super(DangKy.class);
    }

    public List<DangKy> listRegistration() {
        return getSession().createQuery("FROM DangKy", DangKy.class).list();
    }

    public SinhVien getStudentById(String maSV) {
        return getSession().get(SinhVien.class, maSV);
    }

    public LopTinChi getLtcById(String maLTC) {
        return getSession().get(LopTinChi.class, maLTC);
    }

    public Long countSubjectRegisteredInSemester(String maSV, String maMH, String nienKhoa, int hocKy,
            String currentMaLTC) {
        String hqlSubject = "SELECT count(dk) FROM DangKy dk "
                + "WHERE dk.sinhVien.maSV = :maSV AND dk.trangThaiDangKy = :hieuLuc "
                + "AND dk.lopTinChi.monHoc.maMH = :maMH "
                + "AND dk.lopTinChi.nienKhoa = :nk AND dk.lopTinChi.hocKy = :hk "
                + "AND dk.lopTinChi.maLTC != :currentMaLTC";
        return getSession().createQuery(hqlSubject, Long.class)
                .setParameter("maSV", maSV != null ? maSV.trim().toUpperCase() : "")
                .setParameter("hieuLuc", TrangThaiDangKy.HIEU_LUC)
                .setParameter("maMH", maMH != null ? maMH.trim().toUpperCase() : "")
                .setParameter("nk", nienKhoa != null ? nienKhoa.trim().toUpperCase() : "").setParameter("hk", hocKy)
                .setParameter("currentMaLTC", currentMaLTC).uniqueResult();
    }

    public List<LopTinChi> getAvailableClasses() {
        return getSession().createQuery("FROM LopTinChi WHERE trangThaiLop = :hoatDong", LopTinChi.class)
                .setParameter("hoatDong", TrangThaiLop.HOAT_DONG).list();
    }

    public LopTinChi getLtcByIdWithLock(String maLTC) {
        return getSession().get(LopTinChi.class, maLTC, LockMode.PESSIMISTIC_WRITE);
    }

    public Long countActiveRegistrations(String maLTC) {
        String hql = "SELECT count(dk) FROM DangKy dk "
                + "WHERE dk.lopTinChi.maLTC = :maLTC AND dk.trangThaiDangKy = :hieuLuc";
        return getSession().createQuery(hql, Long.class).setParameter("maLTC", maLTC)
                .setParameter("hieuLuc", TrangThaiDangKy.HIEU_LUC).uniqueResult();
    }
}
