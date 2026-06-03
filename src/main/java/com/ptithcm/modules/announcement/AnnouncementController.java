package com.ptithcm.modules.announcement;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptithcm.entities.TaiKhoan;
import com.ptithcm.entities.ThongBao;
import com.ptithcm.modules.account.AccountService;
import com.ptithcm.modules.announcement.dtos.AnnouncementDTO;
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

    @GetMapping("/create")
    public String create(ModelMap model, HttpSession session, RedirectAttributes redirectAttributes) {
        String role = SessionUtil.getRole(session);
        if (!"PGV".equals(role) && !"KHOA".equals(role)) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền thực hiện chức năng này!");
            return "redirect:/announcements";
        }

        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setVersion(0);
        model.addAttribute("announcementDto", dto);
        model.addAttribute("mode", "add");
        return "announcement/form";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") String id, ModelMap model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        String role = SessionUtil.getRole(session);
        if (!"PGV".equals(role) && !"KHOA".equals(role)) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền thực hiện chức năng này!");
            return "redirect:/announcements";
        }

        ThongBao tb = thongBaoService.getThongBaoById(id);
        if (tb == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông báo!");
            return "redirect:/announcements";
        }

        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setId(tb.getId());
        dto.setTieuDe(tb.getTieuDe());
        dto.setNoiDung(tb.getNoiDung());
        dto.setVersion(tb.getVersion());

        model.addAttribute("announcementDto", dto);
        model.addAttribute("mode", "edit");
        return "announcement/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("announcementDto") AnnouncementDTO dto, BindingResult bindingResult,
            @RequestParam("mode") String mode, HttpSession session, ModelMap model,
            RedirectAttributes redirectAttributes) {
        String role = SessionUtil.getRole(session);
        if (!"PGV".equals(role) && !"KHOA".equals(role)) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền thực hiện chức năng này!");
            return "redirect:/announcements";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Lỗi nhập liệu: " + bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).collect(Collectors.joining("<br>")));
            model.addAttribute("announcementDto", dto);
            model.addAttribute("mode", mode);
            return "announcement/form";
        }

        try {
            ThongBao thongBao = new ThongBao();
            if ("edit".equalsIgnoreCase(mode)) {
                thongBao.setId(dto.getId());
                thongBao.setVersion(dto.getVersion());
            } else {
                thongBao.setVersion(0);
            }
            thongBao.setTieuDe(dto.getTieuDe());
            thongBao.setNoiDung(dto.getNoiDung());

            String username = SessionUtil.getCurrentUsername(session);
            TaiKhoan creator = accountService.getAccountById(username);
            if (creator == null) {
                throw new Exception("Không tìm thấy thông tin tài khoản người tạo!");
            }
            thongBao.setNguoiTao(creator);

            thongBaoService.saveThongBao(thongBao, mode);

            redirectAttributes.addFlashAttribute("message",
                    "edit".equalsIgnoreCase(mode) ? "Cập nhật thông báo thành công!" : "Tạo thông báo thành công!");
        } catch (jakarta.persistence.OptimisticLockException
                | org.springframework.orm.ObjectOptimisticLockingFailureException ole) {
            model.addAttribute("error", "Lỗi: Dữ liệu đã bị thay đổi bởi một người dùng khác. Vui lòng tải lại trang!");
            model.addAttribute("announcementDto", dto);
            model.addAttribute("mode", mode);
            return "announcement/form";
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (e.getCause() != null) {
                errorMsg = e.getCause().getMessage();
            }
            model.addAttribute("error", "Lỗi: " + errorMsg);
            model.addAttribute("announcementDto", dto);
            model.addAttribute("mode", mode);
            return "announcement/form";
        }

        return "redirect:/announcements";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") String id, HttpSession session, RedirectAttributes redirectAttributes) {
        String role = SessionUtil.getRole(session);
        if (!"PGV".equals(role) && !"KHOA".equals(role)) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền thực hiện chức năng này!");
            return "redirect:/announcements";
        }

        try {
            thongBaoService.deleteThongBao(id);
            redirectAttributes.addFlashAttribute("message", "Xóa thông báo thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/announcements";
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
