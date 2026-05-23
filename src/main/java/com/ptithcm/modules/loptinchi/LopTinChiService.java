package com.ptithcm.modules.loptinchi;

import com.ptithcm.entity.GiangVien;
import com.ptithcm.entity.Khoa;
import com.ptithcm.entity.LopTinChi;
import com.ptithcm.entity.MonHoc;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LopTinChiService {

    @Autowired
    private LopTinChiDAO lopTinChiDAO;

    public List<LopTinChi> listLtcByKhoa(String maKhoa) {
        return lopTinChiDAO.listLtcByKhoa(maKhoa);
    }

    public List<LopTinChi> listAllLtc() {
        return lopTinChiDAO.listAllLtc();
    }

    public List<Khoa> listKhoa() {
        return lopTinChiDAO.listKhoa();
    }

    public List<MonHoc> listMonHoc() {
        return lopTinChiDAO.listMonHoc();
    }

    public List<GiangVien> listGiangVienByKhoa(String maKhoa) {
        return lopTinChiDAO.listGiangVienByKhoa(maKhoa);
    }

    public List<GiangVien> listAllGiangVien() {
        return lopTinChiDAO.listAllGiangVien();
    }

    public List<Integer> listLtcIdsWithRegistrations() {
        return lopTinChiDAO.listLtcIdsWithRegistrations();
    }

    public LopTinChi getLtcById(int maLTC) {
        return lopTinChiDAO.findById(maLTC);
    }

    public void insertLtc(LopTinChi ltc) {
        lopTinChiDAO.save(ltc);
    }

    public void updateLtc(LopTinChi ltc) {
        lopTinChiDAO.update(ltc);
    }

    public void deleteLtc(int maLTC) throws Exception {
        LopTinChi ltc = lopTinChiDAO.findById(maLTC);
        if (ltc != null) {
            lopTinChiDAO.delete(ltc);
        } else {
            throw new Exception("Không tìm thấy lớp tín chỉ để xóa!");
        }
    }

    public String saveLtcApi(LopTinChi ltc, String mode) throws Exception {
        if ("add".equals(mode)) {
            // Kiểm tra trùng lặp logic: nienKhoa, hocKy, maMH, nhom
            Long count = lopTinChiDAO.countDuplicateLtc(ltc.getNienKhoa(), ltc.getHocKy(), ltc.getMaMH(),
                    ltc.getNhom());
            if (count > 0) {
                throw new Exception("Lớp tín chỉ này đã tồn tại (trùng Niên khóa, Học kỳ, Môn học, Nhóm)!");
            }

            // Tự sinh ID thủ công để tránh lỗi "Cannot insert NULL into MALTC"
            Integer maxId = lopTinChiDAO.getMaxLtcId();
            ltc.setMaLTC(maxId == null ? 1 : maxId + 1);

            lopTinChiDAO.save(ltc);
        } else if ("edit".equals(mode)) {
            LopTinChi existing = lopTinChiDAO.findById(ltc.getMaLTC());
            if (existing == null) {
                throw new Exception("Không tìm thấy lớp tín chỉ để chỉnh sửa!");
            }
            lopTinChiDAO.update(ltc);
        }
        return "success";
    }

    public void deleteLtcApi(int maLTC) throws Exception {
        // Kiểm tra các ràng buộc phụ thuộc: DANGKY (Đăng ký)
        Long count = lopTinChiDAO.countRegistrationsByLtc(maLTC);
        if (count > 0) {
            throw new Exception("Không thể xóa: Lớp tín chỉ đã có " + count + " sinh viên đăng ký!");
        }

        LopTinChi ltc = lopTinChiDAO.findById(maLTC);
        if (ltc != null) {
            lopTinChiDAO.delete(ltc);
        } else {
            throw new Exception("Không tìm thấy lớp tín chỉ để xóa!");
        }
    }
}
