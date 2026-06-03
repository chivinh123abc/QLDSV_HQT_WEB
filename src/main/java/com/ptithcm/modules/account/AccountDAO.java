package com.ptithcm.modules.account;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ptithcm.entities.GiangVien;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.entities.TaiKhoan;

@Repository
public class AccountDAO {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<TaiKhoan> getAllAccounts() {
        return getSession().createQuery("FROM TaiKhoan", TaiKhoan.class).list();
    }

    public TaiKhoan getAccountByUsername(String username) {
        if (username == null) {
            return null;
        }
        return getSession().get(TaiKhoan.class, username.trim());
    }

    public void saveAccount(TaiKhoan account) {
        if (account != null) {
            getSession().persist(account);
        }
    }

    public void updateAccount(TaiKhoan account) {
        if (account != null) {
            getSession().merge(account);
        }
    }

    public void deleteAccount(TaiKhoan account) {
        if (account != null) {
            getSession().remove(account);
        }
    }

    public List<SinhVien> getUnassignedStudents() {
        return getSession().createQuery(
                "FROM SinhVien sv WHERE TRIM(sv.maSV) NOT IN (SELECT TRIM(tk.tenDangNhap) FROM TaiKhoan tk)",
                SinhVien.class).list();
    }

    public List<GiangVien> getUnassignedLecturers() {
        return getSession().createQuery(
                "FROM GiangVien gv WHERE TRIM(gv.maGV) NOT IN (SELECT TRIM(tk.tenDangNhap) FROM TaiKhoan tk)",
                GiangVien.class).list();
    }

    public SinhVien getSinhVienByMaSV(String maSV) {
        if (maSV == null) {
            return null;
        }
        String hql = "FROM SinhVien WHERE maSV = :maSV";
        return getSession().createQuery(hql, SinhVien.class).setParameter("maSV", maSV.trim()).uniqueResult();
    }

    public GiangVien getGiangVienByMaGV(String maGV) {
        if (maGV == null) {
            return null;
        }
        String hql = "FROM GiangVien WHERE maGV = :maGV";
        return getSession().createQuery(hql, GiangVien.class).setParameter("maGV", maGV.trim()).uniqueResult();
    }
}
