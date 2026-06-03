package com.ptithcm.shared.advices;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.ptithcm.modules.announcement.AnnouncementService;
import com.ptithcm.shared.utils.SessionUtil;

@ControllerAdvice
public class GlobalUIConfigAdvice {

    @Autowired
    private AnnouncementService thongBaoService;

    @ModelAttribute("unreadCount")
    public int getUnreadCount(HttpSession session) {
        String username = SessionUtil.getCurrentUsername(session);
        if (username != null && !username.isEmpty()) {
            try {
                return thongBaoService.countUnread(username);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }
}
