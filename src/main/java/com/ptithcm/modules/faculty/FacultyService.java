package com.ptithcm.modules.faculty;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ptithcm.entities.Khoa;
import com.ptithcm.shared.constants.CacheConstant;
import com.ptithcm.shared.events.CacheEvictEvent;
import com.ptithcm.shared.services.RedisService;

@Service
@Transactional
public class FacultyService {

    @Autowired
    private FacultyDAO facultyDAO;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public List<Khoa> listKhoa() {
        List<Khoa> cached = redisService.getList(CacheConstant.FACULTY_ALL, Khoa.class);
        if (cached != null) {
            return cached;
        }
        List<Khoa> result = facultyDAO.findAll();
        redisService.set(CacheConstant.FACULTY_ALL, result, CacheConstant.DEFAULT_TTL_SECONDS);
        return result;
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
        eventPublisher.publishEvent(new CacheEvictEvent(this, CacheConstant.FACULTY_ALL));
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
            eventPublisher.publishEvent(new CacheEvictEvent(this, CacheConstant.FACULTY_ALL));
        } else {
            throw new Exception("Không tìm thấy khoa để xóa!");
        }
    }
}
