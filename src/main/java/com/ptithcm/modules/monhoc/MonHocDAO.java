package com.ptithcm.modules.monhoc;

import com.ptithcm.entity.MonHoc;
import com.ptithcm.shared.base.BaseDAO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class MonHocDAO extends BaseDAO<MonHoc, String> {

    public MonHocDAO() {
        super(MonHoc.class);
    }

    public List<String> listTrimmedSubjectIdsFromLtc() {
        return getSession().createQuery("SELECT distinct trim(maMH) FROM LopTinChi", String.class).list();
    }

    public Long countLtcBySubject(String maMH) {
        return getSession().createQuery("SELECT COUNT(*) FROM LopTinChi WHERE maMH = :maMH", Long.class)
                .setParameter("maMH", maMH).uniqueResult();
    }
}
