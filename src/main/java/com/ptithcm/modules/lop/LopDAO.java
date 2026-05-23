package com.ptithcm.modules.lop;

import com.ptithcm.entity.Khoa;
import com.ptithcm.entity.Lop;
import com.ptithcm.shared.base.BaseDAO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class LopDAO extends BaseDAO<Lop, String> {

    public LopDAO() {
        super(Lop.class);
    }

    public List<Khoa> listKhoa() {
        return getSession().createQuery("FROM Khoa", Khoa.class).list();
    }

    public List<Lop> listLopByKhoa(String maKhoa) {
        return getSession().createQuery("FROM Lop WHERE maKhoa = :maKhoa", Lop.class).setParameter("maKhoa", maKhoa)
                .list();
    }

    public List<Lop> listAllLop() {
        return getSession().createQuery("FROM Lop", Lop.class).list();
    }

    public List<String> listTrimmedLopFromStudents() {
        return getSession().createQuery("SELECT distinct trim(maLop) FROM SinhVien", String.class).list();
    }

    public List<String> listTrimmedLopFromRegistrations() {
        return getSession()
                .createQuery("SELECT distinct trim(sv.maLop) FROM DangKy dk JOIN SinhVien sv ON dk.maSV = sv.maSV",
                        String.class)
                .list();
    }

    public Long countStudentsByLop(String maLop) {
        return getSession().createQuery("SELECT COUNT(*) FROM SinhVien WHERE maLop = :maLop", Long.class)
                .setParameter("maLop", maLop).uniqueResult();
    }

    public Long countRegistrationsByLop(String maLop) {
        return getSession().createQuery(
                "SELECT COUNT(dk) FROM DangKy dk JOIN SinhVien sv ON dk.maSV = sv.maSV WHERE sv.maLop = :maLop",
                Long.class).setParameter("maLop", maLop).uniqueResult();
    }
}
