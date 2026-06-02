package com.ptithcm.modules.subject;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ptithcm.entities.MonHoc;

@Service
@Transactional
public class SubjectService {

    @Autowired
    private SubjectDAO subjectDAO;

    public List<MonHoc> listMonHoc() {
        return subjectDAO.findAll();
    }

    public MonHoc getMonHocById(String maMH) {
        return subjectDAO.findById(maMH);
    }

    public List<String> listTrimmedSubjectIdsFromLtc() {
        return subjectDAO.listTrimmedSubjectIdsFromLtc();
    }

    public String saveMonHoc(MonHoc monHoc, String mode) throws Exception {
        MonHoc existing = subjectDAO.findById(monHoc.getMaMH());
        if ("add".equals(mode)) {
            if (existing != null) {
                throw new Exception("Mã môn học [" + monHoc.getMaMH() + "] đã tồn tại!");
            }
            subjectDAO.save(monHoc);
        } else if ("edit".equals(mode)) {
            if (existing == null) {
                throw new Exception("Không tìm thấy môn học [" + monHoc.getMaMH() + "] để chỉnh sửa!");
            }
            subjectDAO.update(monHoc);
        }
        return "success";
    }

    public void deleteMonHoc(String maMH) throws Exception {
        Long count = subjectDAO.countLtcBySubject(maMH);
        if (count > 0) {
            throw new Exception("Không thể xóa: Môn học đã được mở " + count + " lớp tín chỉ!");
        }

        MonHoc monHoc = subjectDAO.findById(maMH);
        if (monHoc != null) {
            subjectDAO.delete(monHoc);
        } else {
            throw new Exception("Không tìm thấy môn học để xóa!");
        }
    }
}
