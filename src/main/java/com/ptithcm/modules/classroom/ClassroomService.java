package com.ptithcm.modules.classroom;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ptithcm.entities.Khoa;
import com.ptithcm.entities.Lop;

@Service
@Transactional
public class ClassroomService {

    @Autowired
    private ClassroomDAO classroomDAO;

    public List<Khoa> listKhoa() {
        return classroomDAO.listKhoa();
    }

    public List<Lop> listLopByKhoa(String maKhoa) {
        return classroomDAO.listLopByKhoa(maKhoa);
    }

    public List<Lop> listAllLop() {
        return classroomDAO.listAllLop();
    }

    public List<String> listTrimmedLopFromStudents() {
        return classroomDAO.listTrimmedLopFromStudents();
    }

    public List<String> listTrimmedLopFromRegistrations() {
        return classroomDAO.listTrimmedLopFromRegistrations();
    }

    public Lop getLopById(String maLop) {
        return classroomDAO.findById(maLop);
    }

    public String saveClass(Lop lop, String mode) throws Exception {
        if (lop.getMaKhoa() != null) {
            lop.setKhoa(classroomDAO.getSession().get(Khoa.class, lop.getMaKhoa()));
        }
        Lop existing = classroomDAO.findById(lop.getMaLop());
        if ("add".equals(mode)) {
            if (existing != null) {
                throw new Exception("Mã lớp [" + lop.getMaLop() + "] đã tồn tại!");
            }
            classroomDAO.save(lop);
        } else if ("edit".equals(mode)) {
            if (existing == null) {
                throw new Exception("Không tìm thấy lớp để chỉnh sửa!");
            }
            classroomDAO.update(lop);
        }
        return "success";
    }

    public void deleteClass(String maLop) throws Exception {
        Long svCount = classroomDAO.countStudentsByLop(maLop);
        if (svCount > 0) {
            Long regCount = classroomDAO.countRegistrationsByLop(maLop);
            if (regCount > 0) {
                throw new Exception("Không thể xóa: Lớp đã có " + regCount + " lượt đăng ký lớp tín chỉ!");
            }
            throw new Exception("Không thể xóa: Lớp đang có " + svCount + " sinh viên!");
        }

        Lop lop = classroomDAO.findById(maLop);
        if (lop != null) {
            classroomDAO.delete(lop);
        } else {
            throw new Exception("Không tìm thấy lớp để xóa!");
        }
    }
}
