package com.ptithcm.modules.report;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ptithcm.entities.Lop;

@Service
@Transactional
public class ReportService {

    @Autowired
    private ReportDAO reportDAO;

    public List<Lop> listLop() {
        return reportDAO.findAll();
    }

    public List<String> getNienKhoaList() {
        return reportDAO.getNienKhoaList();
    }

    public List<Map<String, Object>> getSummaryMarks(String maLop) {
        return reportDAO.getSummaryMarks(maLop);
    }

    public List<Map<String, Object>> getCreditClassStudents(String nienKhoa, int hocKy, String maMH, int nhom) {
        return reportDAO.getCreditClassStudents(nienKhoa, hocKy, maMH, nhom);
    }
}
