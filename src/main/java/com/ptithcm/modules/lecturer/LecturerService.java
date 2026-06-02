package com.ptithcm.modules.lecturer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ptithcm.entities.GiangVien;
import com.ptithcm.entities.Khoa;

@Service
@Transactional
public class LecturerService {

    @Autowired
    private LecturerDAO lecturerDAO;

    public List<Khoa> listKhoa() {
        return lecturerDAO.listKhoa();
    }

    public List<GiangVien> listGiangVienByKhoa(String maKhoa) {
        return lecturerDAO.listGiangVienByKhoa(maKhoa);
    }

    public List<GiangVien> listAllGiangVien() {
        return lecturerDAO.listAllGiangVien();
    }

    public List<String> listLtcMaGV() {
        return lecturerDAO.listLtcMaGV();
    }

    public List<String> listUserMaGV() {
        return lecturerDAO.listUserMaGV();
    }

    public GiangVien getLecturerById(String maGV) {
        return lecturerDAO.findById(maGV);
    }

    public void saveLecturer(GiangVien gv, String mode) throws Exception {
        if (gv.getMaKhoa() != null) {
            gv.setKhoa(lecturerDAO.getSession().get(Khoa.class, gv.getMaKhoa()));
        }
        GiangVien existing = lecturerDAO.findById(gv.getMaGV());
        if ("add".equals(mode)) {
            if (existing != null) {
                throw new Exception("Mã giảng viên [" + gv.getMaGV() + "] đã tồn tại!");
            }
            lecturerDAO.save(gv);
        } else if ("edit".equals(mode)) {
            if (existing == null) {
                throw new Exception("Không tìm thấy giảng viên để chỉnh sửa!");
            }
            lecturerDAO.update(gv);
        }
    }

    public void deleteLecturer(String maGV) throws Exception {
        Long ltcCount = lecturerDAO.countLtcByLecturer(maGV);
        if (ltcCount > 0) {
            throw new Exception("Không thể xóa: Giảng viên đang phụ trách " + ltcCount + " lớp tín chỉ!");
        }

        List<GiangVien> list = lecturerDAO.getLecturerByTrimmedId(maGV);
        if (!list.isEmpty()) {
            lecturerDAO.delete(list.get(0));
        } else {
            throw new Exception("Không tìm thấy giảng viên để xóa!");
        }
    }
}
