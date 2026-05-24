package com.ptithcm.modules.sinhvien;

import com.ptithcm.entity.Khoa;
import com.ptithcm.entity.Lop;
import com.ptithcm.entity.SinhVien;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SinhVienService {

    @Autowired
    private SinhVienDAO sinhVienDAO;

    public List<Khoa> listKhoa() {
        return sinhVienDAO.listKhoa();
    }

    public List<Lop> listLopByKhoa(String maKhoa) {
        return sinhVienDAO.listLopByKhoa(maKhoa);
    }

    public List<Lop> listAllLop() {
        return sinhVienDAO.listAllLop();
    }

    public List<SinhVien> listStudentsByClass(String maLop) {
        return sinhVienDAO.listStudentsByClass(maLop);
    }

    public SinhVien getStudentById(String maSV) {
        return sinhVienDAO.findById(maSV);
    }

    public void insertStudent(SinhVien sv) {
        if (sv.getMaLop() != null) {
            sv.setLop(sinhVienDAO.getSession().get(Lop.class, sv.getMaLop()));
        }
        sinhVienDAO.save(sv);
    }

    public void updateStudent(SinhVien sv) {
        if (sv.getMaLop() != null) {
            sv.setLop(sinhVienDAO.getSession().get(Lop.class, sv.getMaLop()));
        }
        sinhVienDAO.update(sv);
    }

    public void deleteStudent(String maSV) throws Exception {
        Long count = sinhVienDAO.countDangKyByStudent(maSV);
        if (count > 0) {
            throw new Exception("Không thể xóa: Sinh viên đã có " + count + " bản ghi đăng ký môn học!");
        }
        SinhVien sv = sinhVienDAO.findById(maSV);
        if (sv != null) {
            sinhVienDAO.delete(sv);
        } else {
            throw new Exception("Không tìm thấy sinh viên để xóa!");
        }
    }

    public String saveStudentApi(SinhVien sv, String mode) throws Exception {
        if (sv.getMaLop() != null) {
            sv.setLop(sinhVienDAO.getSession().get(Lop.class, sv.getMaLop()));
        }
        SinhVien existing = sinhVienDAO.findById(sv.getMaSV());
        if (mode.equals("add")) {
            if (existing != null) {
                throw new Exception("Mã sinh viên [" + sv.getMaSV() + "] đã tồn tại!");
            }
            sinhVienDAO.save(sv);
        } else if (mode.equals("edit")) {
            if (existing == null) {
                throw new Exception("Không tìm thấy sinh viên để chỉnh sửa!");
            }
            sinhVienDAO.update(sv);
        }
        return "success";
    }
}
