package com.ptithcm.modules.faculty;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ptithcm.entities.Khoa;

@Service
@Transactional
public class FacultyService {

    @Autowired
    private FacultyDAO facultyDAO;

    public List<Khoa> listKhoa() {
        return facultyDAO.findAll();
    }

    public Khoa getKhoaById(String maKhoa) {
        return facultyDAO.findById(maKhoa);
    }

    public List<String> listTrimmedKhoaFromLop() {
        return facultyDAO.listTrimmedKhoaFromLop();
    }

    public List<String> listTrimmedKhoaFromGiangVien() {
        return facultyDAO.listTrimmedKhoaFromGiangVien();
    }

    public List<String> listTrimmedKhoaFromLtc() {
        return facultyDAO.listTrimmedKhoaFromLtc();
    }

    public String saveKhoa(Khoa khoa, String mode) throws Exception {
        Khoa existing = facultyDAO.findById(khoa.getMaKhoa());
        if ("add".equals(mode)) {
            if (existing != null) {
                throw new Exception("Mã khoa [" + khoa.getMaKhoa() + "] đã tồn tại!");
            }
            facultyDAO.save(khoa);
        } else if ("edit".equals(mode)) {
            if (existing == null) {
                throw new Exception("Không tìm thấy khoa để chỉnh sửa!");
            }
            facultyDAO.update(khoa);
        }
        return "success";
    }

    public void deleteKhoa(String maKhoa) throws Exception {
        Long lopCount = facultyDAO.countLopByKhoa(maKhoa);
        if (lopCount > 0) {
            throw new Exception("Không thể xóa: Khoa đang có " + lopCount + " lớp!");
        }

        Long gvCount = facultyDAO.countGiangVienByKhoa(maKhoa);
        if (gvCount > 0) {
            throw new Exception("Không thể xóa: Khoa đang có " + gvCount + " giảng viên!");
        }

        Long ltcCount = facultyDAO.countLtcByKhoa(maKhoa);
        if (ltcCount > 0) {
            throw new Exception("Không thể xóa: Khoa đang mở " + ltcCount + " lớp tín chỉ!");
        }

        Khoa khoa = facultyDAO.findById(maKhoa);
        if (khoa != null) {
            facultyDAO.delete(khoa);
        } else {
            throw new Exception("Không tìm thấy khoa để xóa!");
        }
    }
}
