package com.ptithcm.modules.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.ptithcm.shared.constants.CacheConstant;
import com.ptithcm.shared.enums.RegistrationStatus;
import com.ptithcm.shared.services.RedisService;

@Component
public class RegistrationWorker {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RegistrationService registrationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedDelay = 500)
    public void processQueue() {
        try {
            String payload = redisService.rpop(CacheConstant.QUEUE_REGISTRATION);
            if (payload == null) {
                return;
            }

            System.out.println("[RegistrationWorker] Nhận yêu cầu đăng ký: " + payload);
            RegistrationRequest req;
            try {
                req = objectMapper.readValue(payload, RegistrationRequest.class);
            } catch (Exception parseEx) {
                System.err.println("[RegistrationWorker] Lỗi parse JSON payload: " + payload);
                parseEx.printStackTrace();
                return;
            }

            String statusKey = CacheConstant.getRegStatusKey(req.getMaSV(), req.getMaLTC());

            try {
                // Thực thi giao dịch đăng ký tín chỉ (đã được bọc bởi @Transactional ở tầng
                // Service)
                registrationService.registerClass(req.getMaLTC(), req.getMaSV());

                // Thành công: cập nhật trạng thái SUCCESS với TTL
                redisService.set(statusKey, RegistrationStatus.SUCCESS.name(),
                        CacheConstant.REGISTRATION_STATUS_TTL_SECONDS);
                System.out.println("[RegistrationWorker] Đăng ký THÀNH CÔNG cho SV: " + req.getMaSV() + ", LTC: "
                        + req.getMaLTC());
            } catch (Exception e) {
                System.err.println("[RegistrationWorker] Đăng ký THẤT BẠI cho SV: " + req.getMaSV() + ", LTC: "
                        + req.getMaLTC() + ". Lỗi chi tiết:");
                e.printStackTrace();

                String errorMsg = e.getMessage();
                if (e.getCause() != null) {
                    errorMsg = e.getCause().getMessage();
                }
                if (errorMsg == null) {
                    errorMsg = e.toString();
                }

                // Thất bại: cập nhật trạng thái FAILED:[Reason] với TTL
                redisService.set(statusKey, RegistrationStatus.FAILED.name() + ":" + errorMsg,
                        CacheConstant.REGISTRATION_STATUS_TTL_SECONDS);
            }
        } catch (Exception e) {
            System.err.println("[RegistrationWorker] Lỗi không mong đợi trong Worker:");
            e.printStackTrace();
        }
    }

    public static class RegistrationRequest {
        private String maLTC;
        private String maSV;

        public String getMaLTC() {
            return maLTC;
        }

        public void setMaLTC(String maLTC) {
            this.maLTC = maLTC;
        }

        public String getMaSV() {
            return maSV;
        }

        public void setMaSV(String maSV) {
            this.maSV = maSV;
        }
    }
}
