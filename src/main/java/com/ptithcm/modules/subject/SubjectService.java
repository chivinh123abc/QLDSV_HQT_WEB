package com.ptithcm.modules.subject;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ptithcm.entities.MonHoc;
import com.ptithcm.shared.constants.CacheConstant;
import com.ptithcm.shared.events.CacheEvictEvent;
import com.ptithcm.shared.services.RedisService;

@Service
@Transactional
public class SubjectService {

    @Autowired
    private SubjectDAO subjectDAO;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public List<MonHoc> listMonHoc() {
        List<MonHoc> cached = redisService.getList(CacheConstant.SUBJECT_ALL, MonHoc.class);
        if (cached != null) {
            return cached;
        }
        List<MonHoc> result = subjectDAO.findAll();
        redisService.set(CacheConstant.SUBJECT_ALL, result, CacheConstant.DEFAULT_TTL_SECONDS);
        return result;
    }

    public MonHoc getMonHocById(String maMH) {
        return subjectDAO.findById(maMH);
    }

    public List<String> listTrimmedSubjectIdsFromLtc() {
        return subjectDAO.listTrimmedSubjectIdsFromLtc();
    }

    public String saveMonHoc(MonHoc monHoc, String mode) throws Exception {
        MonHoc existing = subjectDAO.findById(monHoc.getMaMH());
        if ("add".equals(mode)) {
            if (existing != null) {
                throw new Exception("Mã môn học [" + monHoc.getMaMH() + "] đã tồn tại!");
            }
            subjectDAO.save(monHoc);
        } else if ("edit".equals(mode)) {
            if (existing == null) {
                throw new Exception("Không tìm thấy môn học [" + monHoc.getMaMH() + "] để chỉnh sửa!");
            }
            subjectDAO.update(monHoc);
        }
        eventPublisher.publishEvent(new CacheEvictEvent(this, CacheConstant.SUBJECT_ALL));
        return "success";
    }

    public void deleteMonHoc(String maMH) throws Exception {
        Long count = subjectDAO.countLtcBySubject(maMH);
        if (count > 0) {
            throw new Exception("Không thể xóa: Môn học đã được mở " + count + " lớp tín chỉ!");
        }

        MonHoc monHoc = subjectDAO.findById(maMH);
        if (monHoc != null) {
            subjectDAO.delete(monHoc);
            eventPublisher.publishEvent(new CacheEvictEvent(this, CacheConstant.SUBJECT_ALL));
        } else {
            throw new Exception("Không tìm thấy môn học để xóa!");
        }
    }
}
