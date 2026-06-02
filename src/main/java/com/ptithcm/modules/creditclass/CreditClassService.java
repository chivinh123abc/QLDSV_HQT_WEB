package com.ptithcm.modules.creditclass;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ptithcm.entities.GiangVien;
import com.ptithcm.entities.Khoa;
import com.ptithcm.entities.LopTinChi;
import com.ptithcm.entities.MonHoc;

@Service
@Transactional
public class CreditClassService {

    @Autowired
    private CreditClassDAO creditClassDAO;

    public List<LopTinChi> listLtcByKhoa(String maKhoa) {
        return creditClassDAO.listLtcByKhoa(maKhoa);
    }

    public List<LopTinChi> listAllLtc() {
        return creditClassDAO.listAllLtc();
    }

    public List<Khoa> listKhoa() {
        return creditClassDAO.listKhoa();
    }

    public List<MonHoc> listMonHoc() {
        return creditClassDAO.listMonHoc();
    }

    public List<GiangVien> listGiangVienByKhoa(String maKhoa) {
        return creditClassDAO.listGiangVienByKhoa(maKhoa);
    }

    public List<GiangVien> listAllGiangVien() {
        return creditClassDAO.listAllGiangVien();
    }

    public List<String> listLtcIdsWithRegistrations() {
        return creditClassDAO.listLtcIdsWithRegistrations();
    }

    public LopTinChi getLtcById(String maLTC) {
        return creditClassDAO.findById(maLTC);
    }

    public void saveLtc(LopTinChi ltc, String mode) throws Exception {
        org.hibernate.Session session = creditClassDAO.getSession();
        if (ltc.getMaMH() != null) {
            ltc.setMonHoc(session.get(MonHoc.class, ltc.getMaMH()));
        }
        if (ltc.getMaGV() != null) {
            ltc.setGiangVien(session.get(GiangVien.class, ltc.getMaGV()));
        }
        if (ltc.getMaKhoa() != null) {
            ltc.setKhoa(session.get(Khoa.class, ltc.getMaKhoa()));
        }

        if ("add".equals(mode)) {
            // Kiểm tra trùng lặp logic: nienKhoa, hocKy, maMH, nhom
            Long count = creditClassDAO.countDuplicateLtc(ltc.getNienKhoa(), ltc.getHocKy(), ltc.getMaMH(),
                    ltc.getNhom());
            if (count > 0) {
                throw new Exception("Lớp tín chỉ này đã tồn tại (trùng Niên khóa, Học kỳ, Môn học, Nhóm)!");
            }
            creditClassDAO.save(ltc);
        } else if ("edit".equals(mode)) {
            LopTinChi existing = creditClassDAO.findById(ltc.getMaLTC());
            if (existing == null) {
                throw new Exception("Không tìm thấy lớp tín chỉ để chỉnh sửa!");
            }
            // Hibernate merge/update
            creditClassDAO.update(ltc);
        }
    }

    public void deleteLtc(String maLTC) throws Exception {
        // Kiểm tra các ràng buộc phụ thuộc: DANGKY (Đăng ký)
        Long count = creditClassDAO.countRegistrationsByLtc(maLTC);
        if (count > 0) {
            throw new Exception("Không thể xóa: Lớp tín chỉ đã có " + count + " sinh viên đăng ký!");
        }

        LopTinChi ltc = creditClassDAO.findById(maLTC);
        if (ltc != null) {
            creditClassDAO.delete(ltc);
        } else {
            throw new Exception("Không tìm thấy lớp tín chỉ để xóa!");
        }
    }
}
