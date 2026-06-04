package com.ptithcm.modules.lecturer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ptithcm.entities.GiangVien;
import com.ptithcm.entities.Khoa;
import com.ptithcm.shared.constants.CacheConstant;
import com.ptithcm.shared.events.CacheEvictEvent;
import com.ptithcm.shared.services.RedisService;

@Service
@Transactional
public class LecturerService {

    @Autowired
    private LecturerDAO lecturerDAO;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public List<Khoa> listKhoa() {
        return lecturerDAO.listKhoa();
    }

    public List<GiangVien> listGiangVienByKhoa(String maKhoa) {
        return lecturerDAO.listGiangVienByKhoa(maKhoa);
    }

    public List<GiangVien> listAllGiangVien() {
        List<GiangVien> cached = redisService.getList(CacheConstant.LECTURER_ALL, GiangVien.class);
        if (cached != null) {
            return cached;
        }
        List<GiangVien> result = lecturerDAO.listAllGiangVien();
        redisService.set(CacheConstant.LECTURER_ALL, result, CacheConstant.DEFAULT_TTL_SECONDS);
        return result;
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
        String maGV = gv.getMaGV() != null ? gv.getMaGV().trim() : "";
        if ("add".equals(mode)) {
            if (!maGV.isEmpty()) {
                Number count = (Number) lecturerDAO.getSession()
                        .createNativeQuery("SELECT COUNT(*) FROM giang_vien WHERE id = :id", Object.class)
                        .setParameter("id", maGV).uniqueResult();
                if (count != null && count.intValue() > 0) {
                    Object ngayXoa = lecturerDAO.getSession()
                            .createNativeQuery("SELECT ngay_xoa FROM giang_vien WHERE id = :id", Object.class)
                            .setParameter("id", maGV).uniqueResult();
                    if (ngayXoa == null) {
                        throw new Exception("Mã giảng viên [" + maGV + "] đã tồn tại!");
                    } else {
                        try {
                            lecturerDAO.getSession()
                                    .createNativeMutationQuery("DELETE FROM tai_khoan WHERE ten_dang_nhap = :id")
                                    .setParameter("id", maGV).executeUpdate();
                            lecturerDAO.getSession().createNativeMutationQuery("DELETE FROM giang_vien WHERE id = :id")
                                    .setParameter("id", maGV).executeUpdate();
                            lecturerDAO.getSession().flush();
                        } catch (org.springframework.dao.DataIntegrityViolationException
                                | jakarta.persistence.PersistenceException e) {
                            throw new Exception("Mã này đã từng tồn tại và có dữ liệu lịch sử, không thể tái sử dụng.");
                        }
                    }
                }
            }
            lecturerDAO.save(gv);
        } else if ("edit".equals(mode)) {
            GiangVien existing = lecturerDAO.findById(maGV);
            if (existing == null) {
                throw new Exception("Không tìm thấy giảng viên để chỉnh sửa!");
            }
            lecturerDAO.update(gv);
        }
        eventPublisher.publishEvent(new CacheEvictEvent(this, CacheConstant.LECTURER_ALL));
    }

    public void deleteLecturer(String maGV) throws Exception {
        Long ltcCount = lecturerDAO.countLtcByLecturer(maGV);
        if (ltcCount > 0) {
            throw new Exception("Không thể xóa: Giảng viên đang phụ trách " + ltcCount + " lớp tín chỉ!");
        }

        List<GiangVien> list = lecturerDAO.getLecturerByTrimmedId(maGV);
        if (!list.isEmpty()) {
            lecturerDAO.delete(list.get(0));
            eventPublisher.publishEvent(new CacheEvictEvent(this, CacheConstant.LECTURER_ALL));
        } else {
            throw new Exception("Không tìm thấy giảng viên để xóa!");
        }
    }
}
