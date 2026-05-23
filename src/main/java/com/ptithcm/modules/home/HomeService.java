package com.ptithcm.modules.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HomeService {

    @Autowired
    private HomeDAO homeDAO;

    public Long getRegisteredCount(String maSV) {
        return homeDAO.getRegisteredCount(maSV);
    }

    public Long getStudentCount() {
        return homeDAO.getStudentCount();
    }

    public Long getClassCount() {
        return homeDAO.getClassCount();
    }

    public Long getSubjectCount() {
        return homeDAO.getSubjectCount();
    }

    public Long getCreditClassCount() {
        return homeDAO.getCreditClassCount();
    }
}
