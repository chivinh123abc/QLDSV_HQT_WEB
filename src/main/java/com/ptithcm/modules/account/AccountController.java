package com.ptithcm.modules.account;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptithcm.entities.GiangVien;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.entities.TaiKhoan;
import com.ptithcm.modules.account.dtos.AccountSaveDTO;
import com.ptithcm.shared.dtos.UserSession;
import com.ptithcm.shared.services.CsvService;
import com.ptithcm.shared.utils.SessionUtil;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CsvService csvService;

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public String index(ModelMap model, HttpSession session,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "lnkAdd", required = false) String lnkAdd) {

        UserSession currentLoggedIn = SessionUtil.getUser(session);
        String currentLoggedInUser = currentLoggedIn != null ? currentLoggedIn.getUsername() : "";

        List<Map<String, Object>> userList = accountService.getAllAccounts(currentLoggedInUser);
        List<SinhVien> unassignedStudents = accountService.getUnassignedStudents();
        List<GiangVien> unassignedLecturers = accountService.getUnassignedLecturers();

        model.addAttribute("userList", userList);
        model.addAttribute("unassignedStudents", unassignedStudents);
        model.addAttribute("unassignedLecturers", unassignedLecturers);

        if (userId != null && !userId.trim().isEmpty()) {
            TaiKhoan tk = accountService.getAccountById(userId);
            if (tk != null) {
                model.addAttribute("account", tk);
                model.addAttribute("mode", "edit");
            }
        } else if (lnkAdd != null) {
            model.addAttribute("mode", "add");
        }

        return "account/index";
    }

    @RequestMapping(value = "/accounts/save", method = RequestMethod.POST)
    public String saveAccount(@Valid @ModelAttribute("accountSaveDto") AccountSaveDTO dto, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "Lỗi nhập liệu: " + bindingResult.getFieldErrors().stream()
                            .map(org.springframework.validation.FieldError::getDefaultMessage)
                            .collect(Collectors.joining("<br>")));
            redirectAttributes.addFlashAttribute("accountSaveDto", dto);
            if ("add".equalsIgnoreCase(dto.getMode())) {
                return "redirect:/accounts?lnkAdd=true";
            } else {
                return "redirect:/accounts?userId=" + dto.getUserId() + "&lnkEdit";
            }
        }
        try {
            accountService.saveAccount(dto.getUsername(), dto.getPassword(), dto.getRoleId(), dto.getEmail(),
                    dto.getMode(), dto.getUserId(), dto.getVersion());
            String targetUser = "add".equalsIgnoreCase(dto.getMode())
                    ? dto.getUsername().trim()
                    : dto.getUserId().trim();
            String actionMessage = "add".equalsIgnoreCase(dto.getMode()) ? "Cấp tài khoản" : "Cập nhật tài khoản";
            redirectAttributes.addFlashAttribute("message", actionMessage + " [" + targetUser + "] thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            if ("add".equalsIgnoreCase(dto.getMode())) {
                return "redirect:/accounts?lnkAdd=true";
            } else {
                return "redirect:/accounts?userId=" + dto.getUserId() + "&lnkEdit";
            }
        } catch (Exception e) {
            Throwable t = e;
            boolean isOptimisticLock = false;
            while (t != null) {
                if (t instanceof jakarta.persistence.OptimisticLockException
                        || t.getClass().getName().contains("StaleObjectStateException")
                        || t.getClass().getName().contains("ObjectOptimisticLockingFailureException")) {
                    isOptimisticLock = true;
                    break;
                }
                t = t.getCause();
            }
            if (isOptimisticLock) {
                redirectAttributes.addFlashAttribute("error",
                        "Dữ liệu tài khoản đã bị chỉnh sửa bởi một quản trị viên khác. Vui lòng tải lại trang và thực hiện lại!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            }
            if ("add".equalsIgnoreCase(dto.getMode())) {
                return "redirect:/accounts?lnkAdd=true";
            } else {
                return "redirect:/accounts?userId=" + dto.getUserId() + "&lnkEdit";
            }
        }
        return "redirect:/accounts";
    }

    @RequestMapping(value = "/accounts/delete", method = RequestMethod.POST)
    public String deleteAccount(@RequestParam("userId") String userId, RedirectAttributes redirectAttributes) {
        try {
            accountService.deleteAccount(userId);
            redirectAttributes.addFlashAttribute("message", "Đã xóa tài khoản [" + userId + "] thành công.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống: " + e.getMessage());
        }
        return "redirect:/accounts";
    }

    @RequestMapping(value = "/accounts/import", method = RequestMethod.POST)
    public void importCsv(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        try {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("Tệp tin tải lên rỗng hoặc không tồn tại!");
            }
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".csv")) {
                throw new IllegalArgumentException("Tệp tin tải lên không đúng định dạng. Vui lòng chọn tệp CSV!");
            }

            List<String> mssvList = csvService.extractMssvFromCsv(file);
            List<String[]> credentials = accountService.provisionStudentAccounts(mssvList);
            csvService.exportCredentialsToCsv(credentials, response);
        } catch (IllegalArgumentException e) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            } catch (Exception ex) {
                // Ignore
            }
        } catch (Exception e) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Import failed: " + e.getMessage());
            } catch (Exception ex) {
                // Ignore
            }
        }
    }
}
