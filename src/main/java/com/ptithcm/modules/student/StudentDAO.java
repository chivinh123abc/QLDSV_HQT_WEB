package com.ptithcm.modules.student;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ptithcm.entities.Khoa;
import com.ptithcm.entities.Lop;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.shared.bases.BaseDAO;

@Repository
public class StudentDAO extends BaseDAO<SinhVien, String> {

    public StudentDAO() {
        super(SinhVien.class);
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

    public List<SinhVien> listStudentsByClass(String maLop) {
        return getSession().createQuery("FROM SinhVien WHERE lop.maLop = :maLop", SinhVien.class)
                .setParameter("maLop", maLop).list();
    }

    public Long countDangKyByStudent(String maSV) {
        return getSession().createQuery("SELECT COUNT(*) FROM DangKy WHERE sinhVien.maSV = :maSV", Long.class)
                .setParameter("maSV", maSV).uniqueResult();
    }

    public List<SinhVien> getStudentsWithAccount() {
        return getSession()
                .createQuery("SELECT s FROM SinhVien s WHERE s.maSV IN (SELECT t.tenDangNhap FROM TaiKhoan t)",
                        SinhVien.class)
                .list();
    }
}
