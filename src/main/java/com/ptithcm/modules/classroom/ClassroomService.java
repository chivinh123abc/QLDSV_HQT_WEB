package com.ptithcm.modules.classroom;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ptithcm.entities.Khoa;
import com.ptithcm.entities.Lop;
import com.ptithcm.shared.constants.CacheConstant;
import com.ptithcm.shared.events.CacheEvictEvent;
import com.ptithcm.shared.services.RedisService;

@Service
@Transactional
public class ClassroomService {

    @Autowired
    private ClassroomDAO classroomDAO;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public List<Khoa> listKhoa() {
        return classroomDAO.listKhoa();
    }

    public List<Lop> listLopByKhoa(String maKhoa) {
        return classroomDAO.listLopByKhoa(maKhoa);
    }

    public List<Lop> listAllLop() {
        List<Lop> cached = redisService.getList(CacheConstant.CLASSROOM_ALL, Lop.class);
        if (cached != null) {
            return cached;
        }
        List<Lop> result = classroomDAO.listAllLop();
        redisService.set(CacheConstant.CLASSROOM_ALL, result, CacheConstant.DEFAULT_TTL_SECONDS);
        return result;
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
        String maLop = lop.getMaLop() != null ? lop.getMaLop().trim() : "";
        if ("add".equals(mode)) {
            if (!maLop.isEmpty()) {
                Number count = (Number) classroomDAO.getSession()
                        .createNativeQuery("SELECT COUNT(*) FROM lop WHERE id = :id", Object.class)
                        .setParameter("id", maLop).uniqueResult();
                if (count != null && count.intValue() > 0) {
                    Object ngayXoa = classroomDAO.getSession()
                            .createNativeQuery("SELECT ngay_xoa FROM lop WHERE id = :id", Object.class)
                            .setParameter("id", maLop).uniqueResult();
                    if (ngayXoa == null) {
                        throw new Exception("Mã lớp [" + maLop + "] đã tồn tại!");
                    } else {
                        try {
                            classroomDAO.getSession().createNativeMutationQuery("DELETE FROM lop WHERE id = :id")
                                    .setParameter("id", maLop).executeUpdate();
                            classroomDAO.getSession().flush();
                        } catch (org.springframework.dao.DataIntegrityViolationException
                                | jakarta.persistence.PersistenceException e) {
                            throw new Exception("Mã này đã từng tồn tại và có dữ liệu lịch sử, không thể tái sử dụng.");
                        }
                    }
                }
            }
            classroomDAO.save(lop);
        } else if ("edit".equals(mode)) {
            Lop existing = classroomDAO.findById(maLop);
            if (existing == null) {
                throw new Exception("Không tìm thấy lớp để chỉnh sửa!");
            }
            classroomDAO.update(lop);
        }
        eventPublisher.publishEvent(new CacheEvictEvent(this, CacheConstant.CLASSROOM_ALL));
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
            eventPublisher.publishEvent(new CacheEvictEvent(this, CacheConstant.CLASSROOM_ALL));
        } else {
            throw new Exception("Không tìm thấy lớp để xóa!");
        }
    }
}
