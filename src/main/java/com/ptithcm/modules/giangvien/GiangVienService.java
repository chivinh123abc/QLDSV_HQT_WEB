package com.ptithcm.modules.giangvien;

import com.ptithcm.entity.GiangVien;
import com.ptithcm.entity.Khoa;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GiangVienService {

    @Autowired
    private GiangVienDAO giangVienDAO;

    public List<Khoa> listKhoa() {
        return giangVienDAO.listKhoa();
    }

    public List<GiangVien> listGiangVienByKhoa(String maKhoa) {
        return giangVienDAO.listGiangVienByKhoa(maKhoa);
    }

    public List<GiangVien> listAllGiangVien() {
        return giangVienDAO.listAllGiangVien();
    }

    public List<String> listLtcMaGV() {
        return giangVienDAO.listLtcMaGV();
    }

    public List<String> listUserMaGV() {
        return giangVienDAO.listUserMaGV();
    }

    public GiangVien getLecturerById(String maGV) {
        return giangVienDAO.findById(maGV);
    }

    public void saveLecturer(GiangVien gv, String mode) throws Exception {
        if (gv.getMaKhoa() != null) {
            gv.setKhoa(giangVienDAO.getSession().get(Khoa.class, gv.getMaKhoa()));
        }
        GiangVien existing = giangVienDAO.findById(gv.getMaGV());
        if ("add".equals(mode)) {
            if (existing != null) {
                throw new Exception("Mã giảng viên [" + gv.getMaGV() + "] đã tồn tại!");
            }
            giangVienDAO.save(gv);
        } else if ("edit".equals(mode)) {
            if (existing == null) {
                throw new Exception("Không tìm thấy giảng viên để chỉnh sửa!");
            }
            giangVienDAO.update(gv);
        }
    }

    public void deleteLecturer(String maGV) throws Exception {
        Long ltcCount = giangVienDAO.countLtcByLecturer(maGV);
        if (ltcCount > 0) {
            throw new Exception("Không thể xóa: Giảng viên đang phụ trách " + ltcCount + " lớp tín chỉ!");
        }

        List<GiangVien> list = giangVienDAO.getLecturerByTrimmedId(maGV);
        if (!list.isEmpty()) {
            giangVienDAO.delete(list.get(0));
        } else {
            throw new Exception("Không tìm thấy giảng viên để xóa!");
        }
    }
}
