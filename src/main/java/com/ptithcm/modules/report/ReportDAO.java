package com.ptithcm.modules.report;

import com.ptithcm.entity.Lop;
import com.ptithcm.shared.base.BaseDAO;
import java.util.List;
import java.util.Map;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.stereotype.Repository;

@Repository
public class ReportDAO extends BaseDAO<Lop, String> {

    public ReportDAO() {
        super(Lop.class);
    }

    public List<String> getNienKhoaList() {
        return getSession().createQuery("SELECT DISTINCT ltc.nienKhoa FROM LopTinChi ltc", String.class).list();
    }

    @SuppressWarnings({"deprecation", "unchecked"})
    public List<Map<String, Object>> getSummaryMarks(String maLop) {
        NativeQuery<Map<String, Object>> query = getSession().createNativeQuery("EXEC sp_LayBangDiemTongKet :maLop");
        query.setParameter("maLop", maLop);
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.list();
    }

    @SuppressWarnings({"deprecation", "unchecked"})
    public List<Map<String, Object>> getCreditClassStudents(String nienKhoa, int hocKy, String maMH, int nhom) {
        NativeQuery<Map<String, Object>> query = getSession()
                .createNativeQuery("EXEC sp_LayDanhSachSinhVienDangKyLopTinChi :nk, :hk, :mh, :nhom");
        query.setParameter("nk", nienKhoa);
        query.setParameter("hk", hocKy);
        query.setParameter("mh", maMH);
        query.setParameter("nhom", nhom);
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query.list();
    }
}
