package com.ptithcm.modules.mark;

import java.util.List;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.ptithcm.entities.DangKy;
import com.ptithcm.entities.DangKyId;
import com.ptithcm.entities.Khoa;
import com.ptithcm.shared.bases.BaseDAO;
import com.ptithcm.shared.enums.TrangThaiDangKy;

@Repository
public class MarkDAO extends BaseDAO<DangKy, DangKyId> {

    public MarkDAO() {
        super(DangKy.class);
    }

    public List<String> getNienKhoaList() {
        return getSession().createQuery("SELECT DISTINCT ltc.nienKhoa FROM LopTinChi ltc", String.class).list();
    }

    public List<Khoa> listKhoa() {
        return getSession().createQuery("FROM Khoa", Khoa.class).list();
    }

    public List<Object[]> getSubjects(String nienKhoa, String hocKy, String maKhoa) {
        StringBuilder hql = new StringBuilder(
                "SELECT DISTINCT ltc.monHoc.maMH, ltc.monHoc.tenMH FROM LopTinChi ltc " + "WHERE 1=1 ");

        if (nienKhoa != null && !nienKhoa.isEmpty() && !nienKhoa.equals("all"))
            hql.append("AND ltc.nienKhoa = :nienKhoa ");
        if (hocKy != null && !hocKy.isEmpty() && !hocKy.equals("all"))
            hql.append("AND ltc.hocKy = :hocKy ");
        if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all"))
            hql.append("AND ltc.khoa.maKhoa = :maKhoa ");

        Query<Object[]> query = getSession().createQuery(hql.toString(), Object[].class);
        if (nienKhoa != null && !nienKhoa.isEmpty() && !nienKhoa.equals("all"))
            query.setParameter("nienKhoa", nienKhoa);
        if (hocKy != null && !hocKy.isEmpty() && !hocKy.equals("all"))
            query.setParameter("hocKy", Integer.parseInt(hocKy));
        if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all"))
            query.setParameter("maKhoa", maKhoa);

        return query.list();
    }

    public List<Integer> getGroups(String nienKhoa, String hocKy, String maMH, String maKhoa) {
        StringBuilder hql = new StringBuilder(
                "SELECT DISTINCT ltc.nhom FROM LopTinChi ltc " + "WHERE ltc.monHoc.maMH = :maMH ");

        if (nienKhoa != null && !nienKhoa.isEmpty() && !nienKhoa.equals("all"))
            hql.append("AND ltc.nienKhoa = :nienKhoa ");
        if (hocKy != null && !hocKy.isEmpty() && !hocKy.equals("all"))
            hql.append("AND ltc.hocKy = :hocKy ");
        if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all"))
            hql.append("AND ltc.khoa.maKhoa = :maKhoa ");

        Query<Integer> query = getSession().createQuery(hql.toString(), Integer.class);
        query.setParameter("maMH", maMH);
        if (nienKhoa != null && !nienKhoa.isEmpty() && !nienKhoa.equals("all"))
            query.setParameter("nienKhoa", nienKhoa);
        if (hocKy != null && !hocKy.isEmpty() && !hocKy.equals("all"))
            query.setParameter("hocKy", Integer.parseInt(hocKy));
        if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all"))
            query.setParameter("maKhoa", maKhoa);

        return query.list();
    }

    public List<Object[]> loadStudents(String nienKhoa, String hocKy, String maMH, Integer nhom, String searchMaSV,
            String maKhoa) {
        StringBuilder hql = new StringBuilder(
                "SELECT dk.sinhVien.maSV, dk.sinhVien.ho, dk.sinhVien.ten, dk.diemCC, dk.diemGK, dk.diemCK, dk.lopTinChi.maLTC, dk.lopTinChi.nhom, dk.lopTinChi.monHoc.tenMH "
                        + "FROM DangKy dk " + "WHERE dk.trangThaiDangKy = :hieuLuc ");

        if (searchMaSV != null && !searchMaSV.trim().isEmpty()) {
            hql.append("AND dk.sinhVien.maSV = :searchMaSV ");
        } else {
            if (nienKhoa != null && !nienKhoa.isEmpty() && !nienKhoa.equals("all"))
                hql.append("AND dk.lopTinChi.nienKhoa = :nienKhoa ");
            if (hocKy != null && !hocKy.isEmpty() && !hocKy.equals("all"))
                hql.append("AND dk.lopTinChi.hocKy = :hocKy ");
            if (maMH != null && !maMH.isEmpty())
                hql.append("AND dk.lopTinChi.monHoc.maMH = :maMH ");
            if (nhom != null)
                hql.append("AND dk.lopTinChi.nhom = :nhom ");
            if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all"))
                hql.append("AND dk.lopTinChi.khoa.maKhoa = :maKhoa ");
        }

        hql.append("ORDER BY dk.lopTinChi.nienKhoa DESC, dk.lopTinChi.hocKy DESC, dk.lopTinChi.nhom, dk.sinhVien.maSV");

        Query<Object[]> query = getSession().createQuery(hql.toString(), Object[].class);
        if (searchMaSV != null && !searchMaSV.trim().isEmpty()) {
            query.setParameter("searchMaSV", searchMaSV.trim());
        } else {
            if (nienKhoa != null && !nienKhoa.isEmpty() && !nienKhoa.equals("all"))
                query.setParameter("nienKhoa", nienKhoa);
            if (hocKy != null && !hocKy.isEmpty() && !hocKy.equals("all"))
                query.setParameter("hocKy", Integer.parseInt(hocKy));
            if (maMH != null && !maMH.isEmpty())
                query.setParameter("maMH", maMH);
            if (nhom != null)
                query.setParameter("nhom", nhom);
            if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all"))
                query.setParameter("maKhoa", maKhoa);
        }
        query.setParameter("hieuLuc", TrangThaiDangKy.HIEU_LUC);

        return query.list();
    }

    public DangKy getRegistrationByLtcAndStudent(String maLTC, String maSV) {
        String hql = "FROM DangKy dk WHERE dk.lopTinChi.maLTC = :maLTC AND dk.sinhVien.maSV = :maSV";
        return getSession().createQuery(hql, DangKy.class).setParameter("maLTC", maLTC)
                .setParameter("maSV", maSV != null ? maSV.trim() : "").uniqueResult();
    }

    public List<Object[]> getStudentGrades(String maSV) {
        String hql = "SELECT dk.lopTinChi.nienKhoa, dk.lopTinChi.hocKy, dk.lopTinChi.monHoc.maMH, dk.lopTinChi.monHoc.tenMH, dk.lopTinChi.nhom, "
                + "dk.diemCC, dk.diemGK, dk.diemCK, dk.lopTinChi.monHoc.soTietLT, dk.lopTinChi.monHoc.soTietTH "
                + "FROM DangKy dk " + "WHERE dk.sinhVien.maSV = :maSV AND dk.trangThaiDangKy = :hieuLuc "
                + "ORDER BY dk.lopTinChi.nienKhoa DESC, dk.lopTinChi.hocKy DESC, dk.lopTinChi.monHoc.maMH ASC";

        return getSession().createQuery(hql, Object[].class).setParameter("maSV", maSV != null ? maSV.trim() : "")
                .setParameter("hieuLuc", TrangThaiDangKy.HIEU_LUC).list();
    }
}
