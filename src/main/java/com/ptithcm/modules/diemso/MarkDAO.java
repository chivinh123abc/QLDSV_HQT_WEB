package com.ptithcm.modules.diemso;

import com.ptithcm.entity.DangKy;
import com.ptithcm.entity.DangKyId;
import com.ptithcm.entity.Khoa;
import com.ptithcm.shared.base.BaseDAO;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

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
                "SELECT DISTINCT mh.maMH, mh.tenMH FROM LopTinChi ltc JOIN MonHoc mh ON ltc.maMH = mh.maMH "
                        + "WHERE 1=1 ");

        if (nienKhoa != null && !nienKhoa.isEmpty() && !nienKhoa.equals("all"))
            hql.append("AND ltc.nienKhoa = :nienKhoa ");
        if (hocKy != null && !hocKy.isEmpty() && !hocKy.equals("all"))
            hql.append("AND ltc.hocKy = :hocKy ");
        if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all"))
            hql.append("AND ltc.maKhoa = :maKhoa ");

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
                "SELECT DISTINCT ltc.nhom FROM LopTinChi ltc " + "WHERE ltc.maMH = :maMH ");

        if (nienKhoa != null && !nienKhoa.isEmpty() && !nienKhoa.equals("all"))
            hql.append("AND ltc.nienKhoa = :nienKhoa ");
        if (hocKy != null && !hocKy.isEmpty() && !hocKy.equals("all"))
            hql.append("AND ltc.hocKy = :hocKy ");
        if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all"))
            hql.append("AND ltc.maKhoa = :maKhoa ");

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
                "SELECT sv.maSV, sv.ho, sv.ten, dk.diemCC, dk.diemGK, dk.diemCK, dk.maLTC, ltc.nhom, mh.tenMH "
                        + "FROM DangKy dk " + "JOIN SinhVien sv ON dk.maSV = sv.maSV "
                        + "JOIN LopTinChi ltc ON dk.maLTC = ltc.maLTC " + "JOIN MonHoc mh ON ltc.maMH = mh.maMH "
                        + "WHERE (dk.huyDangKy = false OR dk.huyDangKy IS NULL) ");

        if (searchMaSV != null && !searchMaSV.trim().isEmpty()) {
            hql.append("AND TRIM(sv.maSV) = :searchMaSV ");
        } else {
            if (nienKhoa != null && !nienKhoa.isEmpty() && !nienKhoa.equals("all"))
                hql.append("AND ltc.nienKhoa = :nienKhoa ");
            if (hocKy != null && !hocKy.isEmpty() && !hocKy.equals("all"))
                hql.append("AND ltc.hocKy = :hocKy ");
            if (maMH != null && !maMH.isEmpty())
                hql.append("AND ltc.maMH = :maMH ");
            if (nhom != null)
                hql.append("AND ltc.nhom = :nhom ");
            if (maKhoa != null && !maKhoa.isEmpty() && !maKhoa.equals("all"))
                hql.append("AND ltc.maKhoa = :maKhoa ");
        }

        hql.append("ORDER BY ltc.nienKhoa DESC, ltc.hocKy DESC, ltc.nhom, sv.maSV");

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

        return query.list();
    }

    public DangKy getRegistrationByLtcAndStudent(int maLTC, String maSV) {
        String hql = "FROM DangKy dk WHERE dk.maLTC = :maLTC AND TRIM(dk.maSV) = :maSV";
        return getSession().createQuery(hql, DangKy.class).setParameter("maLTC", maLTC)
                .setParameter("maSV", maSV.trim()).uniqueResult();
    }

    public List<Object[]> getStudentGrades(String maSV) {
        String hql = "SELECT ltc.nienKhoa, ltc.hocKy, ltc.maMH, mh.tenMH, ltc.nhom, "
                + "dk.diemCC, dk.diemGK, dk.diemCK, mh.soTietLT, mh.soTietTH " + "FROM DangKy dk "
                + "JOIN LopTinChi ltc ON dk.maLTC = ltc.maLTC " + "JOIN MonHoc mh ON ltc.maMH = mh.maMH "
                + "WHERE TRIM(dk.maSV) = :maSV AND (dk.huyDangKy = false OR dk.huyDangKy IS NULL) "
                + "ORDER BY ltc.nienKhoa DESC, ltc.hocKy DESC, ltc.maMH ASC";

        return getSession().createQuery(hql, Object[].class).setParameter("maSV", maSV).list();
    }
}
