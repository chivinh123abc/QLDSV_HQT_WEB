package com.ptithcm.modules.monhoc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ptithcm.entities.MonHoc;

@Service
@Transactional
public class MonHocService {

    @Autowired
    private MonHocDAO monHocDAO;

    public List<MonHoc> listMonHoc() {
        return monHocDAO.findAll();
    }

    public MonHoc getMonHocById(String maMH) {
        return monHocDAO.findById(maMH);
    }

    public List<String> listTrimmedSubjectIdsFromLtc() {
        return monHocDAO.listTrimmedSubjectIdsFromLtc();
    }

    public String saveMonHoc(MonHoc monHoc, String mode) throws Exception {
        MonHoc existing = monHocDAO.findById(monHoc.getMaMH());
        if ("add".equals(mode)) {
            if (existing != null) {
                throw new Exception("Mã môn học [" + monHoc.getMaMH() + "] đã tồn tại!");
            }
            monHocDAO.save(monHoc);
        } else if ("edit".equals(mode)) {
            if (existing == null) {
                throw new Exception("Không tìm thấy môn học [" + monHoc.getMaMH() + "] để chỉnh sửa!");
            }
            monHocDAO.update(monHoc);
        }
        return "success";
    }

    public void deleteMonHoc(String maMH) throws Exception {
        Long count = monHocDAO.countLtcBySubject(maMH);
        if (count > 0) {
            throw new Exception("Không thể xóa: Môn học đã được mở " + count + " lớp tín chỉ!");
        }

        MonHoc monHoc = monHocDAO.findById(maMH);
        if (monHoc != null) {
            monHocDAO.delete(monHoc);
        } else {
            throw new Exception("Không tìm thấy môn học để xóa!");
        }
    }
}
