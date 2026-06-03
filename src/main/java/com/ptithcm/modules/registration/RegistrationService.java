package com.ptithcm.modules.registration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ptithcm.entities.DangKy;
import com.ptithcm.entities.DangKyId;
import com.ptithcm.entities.LopTinChi;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.shared.constants.MessageConstant;

@Service
@Transactional
public class RegistrationService {

    @Autowired
    private RegistrationDAO registrationDAO;

    public List<DangKy> listRegistration() {
        return registrationDAO.findAll();
    }

    public SinhVien getStudentById(String maSV) {
        return registrationDAO.getStudentById(maSV);
    }

    public void registerClass(String maLTC, String maSV) throws Exception {
        // 1. Kiểm tra Sinh viên
        SinhVien sv = registrationDAO.getStudentById(maSV);
        if (sv == null) {
            throw new Exception(MessageConstant.STUDENT_NOT_EXIST);
        }
        if (sv.isDaNghiHoc()) {
            throw new Exception(MessageConstant.STUDENT_ON_LEAVE);
        }

        // 2. Kiểm tra Lớp tín chỉ (sử dụng khóa bi quan)
        LopTinChi ltc = registrationDAO.getLtcByIdWithLock(maLTC);
        if (ltc == null) {
            throw new Exception(MessageConstant.CLASS_NOT_EXIST);
        }
        if (ltc.isHuyLop()) {
            throw new Exception(MessageConstant.CLASS_CANCELLED);
        }

        // Kiểm tra số lượng sinh viên tối đa đã đăng ký
        Long activeCount = registrationDAO.countActiveRegistrations(maLTC);
        if (activeCount >= ltc.getSoSVToiDa()) {
            throw new Exception("Lớp tín chỉ đã đạt số lượng sinh viên tối đa!");
        }

        // 3. Kiểm tra ràng buộc môn học: Không thể đăng ký cùng một môn học trong cùng
        // học kỳ
        Long countSameSubject = registrationDAO.countSubjectRegisteredInSemester(maSV, ltc.getMonHoc().getMaMH(),
                ltc.getNienKhoa(), ltc.getHocKy(), maLTC);
        if (countSameSubject > 0) {
            throw new Exception(MessageConstant.ALREADY_REGISTERED_SUBJECT);
        }

        // 4. Kiểm tra Đăng ký
        DangKy dk = registrationDAO.findById(new DangKyId(maLTC, maSV));
        if (dk != null) {
            if (!dk.isHuyDangKy()) {
                throw new Exception(MessageConstant.ALREADY_REGISTERED_CLASS);
            } else {
                dk.setHuyDangKy(false);
                registrationDAO.update(dk);
            }
        } else {
            DangKy newDk = new DangKy();
            newDk.setLopTinChi(ltc);
            newDk.setSinhVien(sv);
            newDk.setHuyDangKy(false);
            registrationDAO.save(newDk);
        }
    }

    public void cancelRegistration(String maLTC, String maSV) throws Exception {
        DangKy dk = registrationDAO.findById(new DangKyId(maLTC, maSV));
        if (dk == null || dk.isHuyDangKy()) {
            throw new Exception("Không tìm thấy thông tin đăng ký hoặc đã hủy trước đó!");
        }

        // Ràng buộc: Không thể hủy đăng ký nếu điểm số đã được nhập
        if (dk.getDiemCC() != null || dk.getDiemGK() != null || dk.getDiemCK() != null) {
            throw new Exception(MessageConstant.CANNOT_CANCEL_GRADED);
        }

        dk.setHuyDangKy(true);
        registrationDAO.update(dk);
    }

    public List<LopTinChi> getAvailableClasses() {
        return registrationDAO.getAvailableClasses();
    }
}
