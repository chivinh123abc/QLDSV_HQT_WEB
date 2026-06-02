package com.ptithcm.modules.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ptithcm.entities.Khoa;
import com.ptithcm.entities.Lop;
import com.ptithcm.entities.SinhVien;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentDAO studentDAO;

    public List<Khoa> listKhoa() {
        return studentDAO.listKhoa();
    }

    public List<Lop> listLopByKhoa(String maKhoa) {
        return studentDAO.listLopByKhoa(maKhoa);
    }

    public List<Lop> listAllLop() {
        return studentDAO.listAllLop();
    }

    public List<SinhVien> listStudentsByClass(String maLop) {
        return studentDAO.listStudentsByClass(maLop);
    }

    public SinhVien getStudentById(String maSV) {
        return studentDAO.findById(maSV);
    }

    public void insertStudent(SinhVien sv) {
        if (sv.getMaLop() != null) {
            sv.setLop(studentDAO.getSession().get(Lop.class, sv.getMaLop()));
        }
        studentDAO.save(sv);
    }

    public void updateStudent(SinhVien sv) {
        if (sv.getMaLop() != null) {
            sv.setLop(studentDAO.getSession().get(Lop.class, sv.getMaLop()));
        }
        studentDAO.update(sv);
    }

    public void deleteStudent(String maSV) throws Exception {
        Long count = studentDAO.countDangKyByStudent(maSV);
        if (count > 0) {
            throw new Exception("Không thể xóa: Sinh viên đã có " + count + " bản ghi đăng ký môn học!");
        }
        SinhVien sv = studentDAO.findById(maSV);
        if (sv != null) {
            studentDAO.delete(sv);
        } else {
            throw new Exception("Không tìm thấy sinh viên để xóa!");
        }
    }

    public Long countDangKyByStudent(String maSV) {
        return studentDAO.countDangKyByStudent(maSV);
    }

    public String saveStudentApi(SinhVien sv, String mode) throws Exception {
        if (sv.getMaLop() != null) {
            sv.setLop(studentDAO.getSession().get(Lop.class, sv.getMaLop()));
        }
        SinhVien existing = studentDAO.findById(sv.getMaSV());
        if (mode.equals("add")) {
            if (existing != null) {
                throw new Exception("Mã sinh viên [" + sv.getMaSV() + "] đã tồn tại!");
            }
            studentDAO.save(sv);
        } else if (mode.equals("edit")) {
            if (existing == null) {
                throw new Exception("Không tìm thấy sinh viên để chỉnh sửa!");
            }
            studentDAO.update(sv);
        }
        return "success";
    }
}
