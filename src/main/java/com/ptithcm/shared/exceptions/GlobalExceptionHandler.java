package com.ptithcm.shared.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 2. Hàm hứng lỗi 500 (Code chạy sai)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception ex) {
        // Ghi log lỗi chi tiết ra console/file để developer debug
        logger.error("System error occurred: ", ex);

        // Trả thông báo thân thiện ra View
        ModelAndView mav = new ModelAndView("shared/error");
        mav.addObject("errorMsg", "Hệ thống đang bảo trì hoặc xảy ra sự cố gián đoạn. Vui lòng thử lại sau!");
        return mav;
    }
}
