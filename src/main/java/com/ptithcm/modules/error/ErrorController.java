package com.ptithcm.modules.error;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

    @RequestMapping("/404")
    public String handle404(ModelMap model) {
        model.addAttribute("errorMsg", "Trang bạn tìm kiếm không tồn tại hoặc đã bị xóa!");
        return "shared/error";
    }

    @RequestMapping("/500")
    public String handle500(ModelMap model) {
        model.addAttribute("errorMsg", "Hệ thống đang bảo trì hoặc xảy ra sự cố gián đoạn. Vui lòng thử lại sau!");
        return "shared/error";
    }
}
