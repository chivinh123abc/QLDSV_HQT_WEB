package com.ptithcm.shared.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 2. Hàm hứng lỗi 500 (Code chạy sai)
    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, ModelMap model) {
        // Ghi log lỗi chi tiết ra console/file để developer debug
        logger.error("Hệ thống xảy ra lỗi nghiêm trọng: ", ex);

        // Trả thông báo thân thiện ra View
        model.addAttribute("errorMsg", "Hệ thống đang bảo trì hoặc xảy ra sự cố gián đoạn. Vui lòng thử lại sau!");
        return "shared/error";
    }
}
