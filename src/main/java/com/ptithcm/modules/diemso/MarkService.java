package com.ptithcm.modules.diemso;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ptithcm.entities.DangKy;
import com.ptithcm.entities.Khoa;

@Service
@Transactional
public class MarkService {

    @Autowired
    private MarkDAO markDAO;

    public List<String> getNienKhoaList() {
        return markDAO.getNienKhoaList();
    }

    public List<Khoa> listKhoa() {
        return markDAO.listKhoa();
    }

    public List<Object[]> getSubjects(String nienKhoa, String hocKy, String maKhoa) {
        return markDAO.getSubjects(nienKhoa, hocKy, maKhoa);
    }

    public List<Integer> getGroups(String nienKhoa, String hocKy, String maMH, String maKhoa) {
        return markDAO.getGroups(nienKhoa, hocKy, maMH, maKhoa);
    }

    public List<Object[]> loadStudents(String nienKhoa, String hocKy, String maMH, Integer nhom, String searchMaSV,
            String maKhoa) {
        return markDAO.loadStudents(nienKhoa, hocKy, maMH, nhom, searchMaSV, maKhoa);
    }

    public void saveMark(String maLTC, String maSV, Float diemCC, Float diemGK, Float diemCK) throws Exception {
        DangKy dk = markDAO.getRegistrationByLtcAndStudent(maLTC, maSV);
        if (dk != null) {
            dk.setDiemCC(diemCC);
            dk.setDiemGK(diemGK);
            dk.setDiemCK(diemCK);
            markDAO.update(dk);
        } else {
            throw new Exception("Không tìm thấy thông tin đăng ký cho SV: " + maSV + " tại lớp: " + maLTC);
        }
    }

    public void saveAllMarks(List<Map<String, Object>> marks) throws Exception {
        for (Map<String, Object> mark : marks) {
            String maLTC = mark.get("maLTC") != null ? mark.get("maLTC").toString() : "";
            String maSV = (String) mark.get("maSV");

            Float diemCC = mark.get("diemCC") != null && !mark.get("diemCC").toString().isEmpty()
                    ? Float.valueOf(mark.get("diemCC").toString())
                    : null;
            Float diemGK = mark.get("diemGK") != null && !mark.get("diemGK").toString().isEmpty()
                    ? Float.valueOf(mark.get("diemGK").toString())
                    : null;
            Float diemCK = mark.get("diemCK") != null && !mark.get("diemCK").toString().isEmpty()
                    ? Float.valueOf(mark.get("diemCK").toString())
                    : null;

            DangKy dk = markDAO.getRegistrationByLtcAndStudent(maLTC, maSV);
            if (dk != null) {
                dk.setDiemCC(diemCC);
                dk.setDiemGK(diemGK);
                dk.setDiemCK(diemCK);
                markDAO.update(dk);
            }
        }
    }

    public List<Object[]> getStudentGrades(String maSV) {
        return markDAO.getStudentGrades(maSV);
    }
}
