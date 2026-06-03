package com.ptithcm.modules.announcement;

import org.springframework.stereotype.Repository;

import com.ptithcm.entities.ThongBao;
import com.ptithcm.shared.bases.BaseDAO;

@Repository
public class AnnouncementDAO extends BaseDAO<ThongBao, String> {

    public AnnouncementDAO() {
        super(ThongBao.class);
    }

    public void markAsRead(String idThongBao, String tenDangNhap) {
        String sql = "INSERT INTO thong_bao_da_doc (id_thong_bao, ten_dang_nhap) "
                + "SELECT :idThongBao, :tenDangNhap WHERE NOT EXISTS ("
                + "SELECT 1 FROM thong_bao_da_doc WHERE id_thong_bao = :idThongBao AND ten_dang_nhap = :tenDangNhap)";
        getSession().createNativeMutationQuery(sql).setParameter("idThongBao", idThongBao)
                .setParameter("tenDangNhap", tenDangNhap).executeUpdate();
    }

    public int countUnread(String tenDangNhap) {
        String sql = "SELECT COUNT(*) FROM thong_bao t " + "WHERE t.ngay_xoa IS NULL AND t.id NOT IN ("
                + "SELECT id_thong_bao FROM thong_bao_da_doc WHERE ten_dang_nhap = :tenDangNhap)";
        Long count = getSession().createNativeQuery(sql, Long.class).setParameter("tenDangNhap", tenDangNhap)
                .getSingleResult();
        return count != null ? count.intValue() : 0;
    }
}
