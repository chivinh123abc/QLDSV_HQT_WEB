package com.ptithcm.modules.sinhvien;

import com.ptithcm.entity.Khoa;
import com.ptithcm.entity.Lop;
import com.ptithcm.entity.SinhVien;
import com.ptithcm.shared.base.BaseDAO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SinhVienDAO extends BaseDAO<SinhVien, String> {

    public SinhVienDAO() {
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
}
