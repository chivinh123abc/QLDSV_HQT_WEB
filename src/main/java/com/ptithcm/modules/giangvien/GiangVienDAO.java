package com.ptithcm.modules.giangvien;

import com.ptithcm.entity.GiangVien;
import com.ptithcm.entity.Khoa;
import com.ptithcm.shared.base.BaseDAO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class GiangVienDAO extends BaseDAO<GiangVien, String> {

    public GiangVienDAO() {
        super(GiangVien.class);
    }

    public List<Khoa> listKhoa() {
        return getSession().createQuery("FROM Khoa", Khoa.class).list();
    }

    public List<GiangVien> listGiangVienByKhoa(String maKhoa) {
        return getSession().createQuery("FROM GiangVien WHERE maKhoa = :maKhoa", GiangVien.class)
                .setParameter("maKhoa", maKhoa).list();
    }

    public List<GiangVien> listAllGiangVien() {
        return getSession().createQuery("FROM GiangVien", GiangVien.class).list();
    }

    public List<String> listLtcMaGV() {
        return getSession().createQuery("SELECT distinct maGV FROM LopTinChi WHERE maGV IS NOT NULL", String.class)
                .list();
    }

    public List<String> listUserMaGV() {
        return getSession().createQuery("SELECT distinct username FROM Users WHERE username IS NOT NULL", String.class)
                .list();
    }

    public List<GiangVien> getLecturerByTrimmedId(String maGV) {
        return getSession().createQuery("FROM GiangVien WHERE trim(maGV) = trim(:maGV)", GiangVien.class)
                .setParameter("maGV", maGV.trim()).list();
    }

    public Long countLtcByLecturer(String maGV) {
        return getSession().createQuery("SELECT COUNT(*) FROM LopTinChi WHERE trim(maGV) = trim(:maGV)", Long.class)
                .setParameter("maGV", maGV.trim()).uniqueResult();
    }

    public Long countUserByLecturer(String maGV) {
        return getSession().createQuery("SELECT COUNT(*) FROM Users WHERE trim(username) = trim(:maGV)", Long.class)
                .setParameter("maGV", maGV.trim()).uniqueResult();
    }
}
