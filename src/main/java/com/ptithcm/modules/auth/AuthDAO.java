package com.ptithcm.modules.auth;

import com.ptithcm.entity.GiangVien;
import com.ptithcm.entity.SinhVien;
import com.ptithcm.shared.dto.UserSession;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AuthDAO {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public UserSession findUserByUsernameAndPassword(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        String u = username.trim();

        // 1. Kiểm tra tài khoản Sinh viên
        String svHql = "FROM SinhVien WHERE TRIM(maSV) = :username AND password = :password";
        SinhVien sv = getSession().createQuery(svHql, SinhVien.class).setParameter("username", u)
                .setParameter("password", password).uniqueResult();
        if (sv != null) {
            return new UserSession(sv.getMaSV(), "SINHVIEN", null, sv.getHo() + " " + sv.getTen());
        }

        // 2. Kiểm tra tài khoản Giảng viên / Phòng Giáo Vụ
        String gvHql = "FROM GiangVien WHERE TRIM(maGV) = :username AND password = :password";
        GiangVien gv = getSession().createQuery(gvHql, GiangVien.class).setParameter("username", u)
                .setParameter("password", password).uniqueResult();
        if (gv != null) {
            return new UserSession(gv.getMaGV(), gv.getRole(), gv.getKhoa() != null ? gv.getKhoa().getMaKhoa() : null,
                    gv.getHo() + " " + gv.getTen());
        }

        return null;
    }

    public GiangVien findGiangVienByMaGV(String maGV) {
        String hql = "FROM GiangVien WHERE TRIM(maGV) = :maGV";
        return getSession().createQuery(hql, GiangVien.class).setParameter("maGV", maGV.trim()).uniqueResult();
    }

    public SinhVien findSinhVienByMaSV(String maSV) {
        String hql = "FROM SinhVien WHERE TRIM(maSV) = :maSV";
        return getSession().createQuery(hql, SinhVien.class).setParameter("maSV", maSV.trim()).uniqueResult();
    }

    public List<Object[]> listGiangVienNames() {
        return getSession().createQuery("SELECT maGV, ho, ten FROM GiangVien", Object[].class).list();
    }

    public List<Object[]> listSinhVienNames() {
        return getSession().createQuery("SELECT maSV, ho, ten FROM SinhVien", Object[].class).list();
    }

    public List<String> getLecturerUsernamesWithCreditClasses() {
        return getSession().createQuery(
                "SELECT distinct trim(giangVien.maGV) FROM LopTinChi WHERE giangVien IS NOT NULL", String.class).list();
    }

    public List<String> getStudentUsernamesWithRegistrations() {
        return getSession()
                .createQuery("SELECT distinct trim(sinhVien.maSV) FROM DangKy WHERE sinhVien IS NOT NULL", String.class)
                .list();
    }

    public Long countGiangVienByMaGV(String username) {
        return getSession().createQuery("SELECT COUNT(*) FROM GiangVien WHERE maGV = :username", Long.class)
                .setParameter("username", username).uniqueResult();
    }

    public Long countSinhVienByMaSV(String username) {
        return getSession().createQuery("SELECT COUNT(*) FROM SinhVien WHERE maSV = :username", Long.class)
                .setParameter("username", username).uniqueResult();
    }

    public Long countLtcByLecturerUsername(String username) {
        return getSession()
                .createQuery("SELECT COUNT(*) FROM LopTinChi WHERE upper(trim(giangVien.maGV)) = upper(trim(:uname))",
                        Long.class)
                .setParameter("uname", username).uniqueResult();
    }

    public Long countDangKyByStudentUsername(String username) {
        return getSession()
                .createQuery("SELECT COUNT(*) FROM DangKy WHERE upper(trim(sinhVien.maSV)) = upper(trim(:uname))",
                        Long.class)
                .setParameter("uname", username).uniqueResult();
    }
}
