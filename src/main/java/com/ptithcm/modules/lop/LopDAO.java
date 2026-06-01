package com.ptithcm.modules.lop;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ptithcm.entities.Khoa;
import com.ptithcm.entities.Lop;
import com.ptithcm.shared.bases.BaseDAO;

@Repository
public class LopDAO extends BaseDAO<Lop, String> {

    public LopDAO() {
        super(Lop.class);
    }

    public List<Khoa> listKhoa() {
        return getSession().createQuery("FROM Khoa", Khoa.class).list();
    }

    public List<Lop> listLopByKhoa(String maKhoa) {
        return getSession().createQuery("FROM Lop WHERE khoa.maKhoa = :maKhoa", Lop.class)
                .setParameter("maKhoa", maKhoa).list();
    }

    public List<Lop> listAllLop() {
        return getSession().createQuery("FROM Lop", Lop.class).list();
    }

    public List<String> listTrimmedLopFromStudents() {
        List<String> list = getSession().createQuery("SELECT distinct lop.maLop FROM SinhVien", String.class).list();
        if (list != null) {
            list.replaceAll(s -> s != null ? s.trim() : null);
        }
        return list;
    }

    public List<String> listTrimmedLopFromRegistrations() {
        List<String> list = getSession()
                .createQuery("SELECT distinct dk.sinhVien.lop.maLop FROM DangKy dk", String.class).list();
        if (list != null) {
            list.replaceAll(s -> s != null ? s.trim() : null);
        }
        return list;
    }

    public Long countStudentsByLop(String maLop) {
        return getSession().createQuery("SELECT COUNT(*) FROM SinhVien WHERE lop.maLop = :maLop", Long.class)
                .setParameter("maLop", maLop).uniqueResult();
    }

    public Long countRegistrationsByLop(String maLop) {
        return getSession()
                .createQuery("SELECT COUNT(dk) FROM DangKy dk WHERE dk.sinhVien.lop.maLop = :maLop", Long.class)
                .setParameter("maLop", maLop).uniqueResult();
    }
}
