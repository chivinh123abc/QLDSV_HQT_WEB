package com.ptithcm.modules.announcement;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptithcm.entities.ThongBao;
import com.ptithcm.modules.account.AccountService;
import com.ptithcm.shared.utils.SessionUtil;

@Controller
@RequestMapping("/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementService thongBaoService;

    @Autowired
    private AccountService accountService;

    @GetMapping
    public String index(ModelMap model, HttpSession session) {
        List<ThongBao> list = thongBaoService.listThongBao();
        model.addAttribute("announcements", list);
        model.addAttribute("role", SessionUtil.getRole(session));
        return "announcement/index";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("id") String id, ModelMap model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        String username = SessionUtil.getCurrentUsername(session);
        if (username == null || username.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để xem chi tiết thông báo!");
            return "redirect:/login";
        }

        ThongBao tb = thongBaoService.getThongBaoById(id);
        if (tb == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông báo!");
            return "redirect:/announcements";
        }

        // Đánh dấu đã đọc ngầm cho user hiện tại
        try {
            thongBaoService.markAsRead(id, username);
        } catch (Exception e) {
            System.err.println("[AnnouncementController] Lỗi khi lưu vết đọc: " + e.getMessage());
        }

        model.addAttribute("announcement", tb);
        model.addAttribute("role", SessionUtil.getRole(session));
        return "announcement/detail";
    }
}
