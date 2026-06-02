package com.ptithcm.modules.subject;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ptithcm.entities.MonHoc;
import com.ptithcm.shared.bases.BaseDAO;

@Repository
public class SubjectDAO extends BaseDAO<MonHoc, String> {

    public SubjectDAO() {
        super(MonHoc.class);
    }

    public List<String> listTrimmedSubjectIdsFromLtc() {
        List<String> list = getSession().createQuery("SELECT distinct ltc.monHoc.maMH FROM LopTinChi ltc", String.class)
                .list();
        if (list != null) {
            list.replaceAll(s -> s != null ? s.trim() : null);
        }
        return list;
    }

    public Long countLtcBySubject(String maMH) {
        return getSession().createQuery("SELECT COUNT(*) FROM LopTinChi ltc WHERE ltc.monHoc.maMH = :maMH", Long.class)
                .setParameter("maMH", maMH).uniqueResult();
    }
}
