package com.ptithcm.modules.lop;

import com.ptithcm.entity.Khoa;
import com.ptithcm.entity.Lop;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LopService {

    @Autowired
    private LopDAO lopDAO;

    public List<Khoa> listKhoa() {
        return lopDAO.listKhoa();
    }

    public List<Lop> listLopByKhoa(String maKhoa) {
        return lopDAO.listLopByKhoa(maKhoa);
    }

    public List<Lop> listAllLop() {
        return lopDAO.listAllLop();
    }

    public List<String> listTrimmedLopFromStudents() {
        return lopDAO.listTrimmedLopFromStudents();
    }

    public List<String> listTrimmedLopFromRegistrations() {
        return lopDAO.listTrimmedLopFromRegistrations();
    }

    public Lop getLopById(String maLop) {
        return lopDAO.findById(maLop);
    }

    public String saveClass(Lop lop, String mode) throws Exception {
        if (lop.getMaKhoa() != null) {
            lop.setKhoa(lopDAO.getSession().get(Khoa.class, lop.getMaKhoa()));
        }
        Lop existing = lopDAO.findById(lop.getMaLop());
        if ("add".equals(mode)) {
            if (existing != null) {
                throw new Exception("Mã lớp [" + lop.getMaLop() + "] đã tồn tại!");
            }
            lopDAO.save(lop);
        } else if ("edit".equals(mode)) {
            if (existing == null) {
                throw new Exception("Không tìm thấy lớp để chỉnh sửa!");
            }
            lopDAO.update(lop);
        }
        return "success";
    }

    public void deleteClass(String maLop) throws Exception {
        Long svCount = lopDAO.countStudentsByLop(maLop);
        if (svCount > 0) {
            Long regCount = lopDAO.countRegistrationsByLop(maLop);
            if (regCount > 0) {
                throw new Exception("Không thể xóa: Lớp đã có " + regCount + " lượt đăng ký lớp tín chỉ!");
            }
            throw new Exception("Không thể xóa: Lớp đang có " + svCount + " sinh viên!");
        }

        Lop lop = lopDAO.findById(maLop);
        if (lop != null) {
            lopDAO.delete(lop);
        } else {
            throw new Exception("Không tìm thấy lớp để xóa!");
        }
    }
}
