package com.ptithcm.shared.services;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ptithcm.shared.constants.CacheConstant;
import com.ptithcm.shared.constants.MailConstant;
import com.ptithcm.shared.dtos.MailInfoDTO;

@Service
public class EmailOTPService {

    private static final Logger log = LoggerFactory.getLogger(EmailOTPService.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private MailerService mailerService;

    private final SecureRandom random = new SecureRandom();

    /**
     * Sinh mã OTP 6 chữ số, lưu vào Redis và gửi mail cho người dùng.
     */
    public void sendOTP(String email) throws Exception {
        // Sinh mã OTP 6 chữ số
        StringBuilder otpBuilder = new StringBuilder();
        for (int i = 0; i < CacheConstant.OTP_LENGTH; i++) {
            otpBuilder.append(random.nextInt(10));
        }
        String otpCode = otpBuilder.toString();

        // Lưu vào Redis với TTL là 5 phút
        String key = CacheConstant.getOtpResetPwKey(email);
        redisService.set(key, otpCode, CacheConstant.OTP_RESET_PW_TTL_SECONDS);

        log.info("[OTP SERVICE] Đã lưu OTP vào Redis cho email: {}, key: {}", email, key);

        // Gửi mail sử dụng MailerService
        Map<String, String> variables = new HashMap<>();
        variables.put("otpCode", otpCode);

        MailInfoDTO mailInfo = new MailInfoDTO();
        mailInfo.setTo(email);
        mailInfo.setSubject("Mã OTP đặt lại mật khẩu - QLDSV PTITHCM");
        mailInfo.setTemplatePath(MailConstant.TEMPLATE_RESET_PASSWORD);
        mailInfo.setVariables(variables);

        mailerService.sendMail(mailInfo);
        log.info("[OTP SERVICE] Đã gửi mail OTP thành công tới: {}", email);
    }

    /**
     * Kiểm tra mã OTP người dùng nhập vào.
     */
    public boolean verifyOTP(String email, String otpCode) {
        if (email == null || otpCode == null) {
            return false;
        }
        String key = CacheConstant.getOtpResetPwKey(email);
        String savedOtp = redisService.get(key);
        return savedOtp != null && savedOtp.trim().equals(otpCode.trim());
    }

    /**
     * Xóa mã OTP khỏi Redis.
     */
    public void deleteOTP(String email) {
        if (email != null) {
            String key = CacheConstant.getOtpResetPwKey(email);
            redisService.delete(key);
            log.info("[OTP SERVICE] Đã xóa OTP khỏi Redis cho email: {}", email);
        }
    }
}
