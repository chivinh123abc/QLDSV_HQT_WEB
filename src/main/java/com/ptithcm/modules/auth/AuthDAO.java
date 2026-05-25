package com.ptithcm.modules.auth;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ptithcm.entities.GiangVien;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.entities.TaiKhoan;
import com.ptithcm.shared.dtos.UserSession;
import com.ptithcm.shared.enums.TrangThaiTaiKhoan;

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

        // 1. Truy vấn TaiKhoan theo tenDangNhap
        String hql = "FROM TaiKhoan WHERE TRIM(tenDangNhap) = :username";
        TaiKhoan taiKhoan = getSession().createQuery(hql, TaiKhoan.class).setParameter("username", u).uniqueResult();

        if (taiKhoan == null) {
            return null;
        }

        // 2. Kiểm tra trạng thái và so khớp mật khẩu bằng BCrypt
        if (taiKhoan.getTrangThai() != TrangThaiTaiKhoan.DA_KICH_HOAT) {
            return null;
        }

        if (!BCrypt.checkpw(password, taiKhoan.getMatKhau())) {
            return null;
        }

        // 3. Khôi phục thông tin từ SinhVien hoặc GiangVien
        if ("SINHVIEN".equals(taiKhoan.getPhanQuyen())) {
            SinhVien sv = findSinhVienByMaSV(u);
            if (sv != null) {
                return new UserSession(sv.getMaSV(), "SINHVIEN", null, sv.getHo() + " " + sv.getTen());
            }
        } else {
            GiangVien gv = findGiangVienByMaGV(u);
            if (gv != null) {
                return new UserSession(gv.getMaGV(), taiKhoan.getPhanQuyen(),
                        gv.getKhoa() != null ? gv.getKhoa().getMaKhoa() : null, gv.getHo() + " " + gv.getTen());
            }
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
