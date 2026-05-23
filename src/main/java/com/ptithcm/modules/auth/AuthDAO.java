package com.ptithcm.modules.auth;

import com.ptithcm.entity.GiangVien;
import com.ptithcm.entity.SinhVien;
import com.ptithcm.entity.Users;
import com.ptithcm.shared.base.BaseDAO;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class AuthDAO extends BaseDAO<Users, Integer> {

    public AuthDAO() {
        super(Users.class);
    }

    public Users findUserByUsernameAndPassword(String username, String password) {
        String hql = "FROM Users WHERE username = :username AND password = :password";
        return getSession().createQuery(hql, Users.class).setParameter("username", username)
                .setParameter("password", password).uniqueResult();
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
        return getSession()
                .createQuery("SELECT distinct trim(maGV) FROM LopTinChi WHERE maGV IS NOT NULL", String.class).list();
    }

    public List<String> getStudentUsernamesWithRegistrations() {
        return getSession().createQuery("SELECT distinct trim(maSV) FROM DangKy WHERE maSV IS NOT NULL", String.class)
                .list();
    }

    public List<SinhVien> getUnassignedStudents() {
        String hql = "FROM SinhVien sv WHERE sv.maSV NOT IN (SELECT u.username FROM Users u)";
        return getSession().createQuery(hql, SinhVien.class).list();
    }

    public List<GiangVien> getUnassignedLecturers() {
        String hql = "FROM GiangVien gv WHERE gv.maGV NOT IN (SELECT u.username FROM Users u)";
        return getSession().createQuery(hql, GiangVien.class).list();
    }

    public Long countGiangVienByMaGV(String username) {
        return getSession().createQuery("SELECT COUNT(*) FROM GiangVien WHERE maGV = :username", Long.class)
                .setParameter("username", username).uniqueResult();
    }

    public Long countSinhVienByMaSV(String username) {
        return getSession().createQuery("SELECT COUNT(*) FROM SinhVien WHERE maSV = :username", Long.class)
                .setParameter("username", username).uniqueResult();
    }

    public Long countUsersByUsername(String username) {
        return getSession().createQuery("SELECT COUNT(*) FROM Users WHERE username = :username", Long.class)
                .setParameter("username", username).uniqueResult();
    }

    public Long countUsersByUsernameExcludingId(String username, int userId) {
        return getSession()
                .createQuery("SELECT COUNT(*) FROM Users WHERE username = :username AND userId != :userId", Long.class)
                .setParameter("username", username).setParameter("userId", userId).uniqueResult();
    }

    public Long countLtcByLecturerUsername(String username) {
        return getSession()
                .createQuery("SELECT COUNT(*) FROM LopTinChi WHERE upper(trim(maGV)) = upper(trim(:uname))", Long.class)
                .setParameter("uname", username).uniqueResult();
    }

    public Long countDangKyByStudentUsername(String username) {
        return getSession()
                .createQuery("SELECT COUNT(*) FROM DangKy WHERE upper(trim(maSV)) = upper(trim(:uname))", Long.class)
                .setParameter("uname", username).uniqueResult();
    }
}
