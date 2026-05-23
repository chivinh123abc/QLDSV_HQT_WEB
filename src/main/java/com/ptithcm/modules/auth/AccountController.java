package com.ptithcm.modules.auth;

import com.ptithcm.entity.Users;
import com.ptithcm.shared.enumtype.RoleEnum;
import com.ptithcm.shared.util.SessionUtil;
import com.ptithcm.shared.validator.UsersValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UsersValidator usersValidator;

    @RequestMapping()
    public String index(ModelMap model, jakarta.servlet.http.HttpSession httpSession) {
        List<Users> userList = authService.listUsers();

        Map<String, String> nameMap = new HashMap<>();
        List<Object[]> gvList = authService.listGiangVienNames();
        for (Object[] gv : gvList) {
            nameMap.put((String) gv[0], (gv[1] != null ? gv[1] + " " : "") + (gv[2] != null ? gv[2] : ""));
        }
        List<Object[]> svList = authService.listSinhVienNames();
        for (Object[] sv : svList) {
            nameMap.put((String) sv[0], (sv[1] != null ? sv[1] + " " : "") + (sv[2] != null ? sv[2] : ""));
        }

        // Find lecturers with credit classes
        List<String> ltcMaGV = authService.getLecturerUsernamesWithCreditClasses();
        java.util.Set<String> dependentUsernames = new java.util.HashSet<>();
        for (String s : ltcMaGV)
            if (s != null)
                dependentUsernames.add(s.trim().toUpperCase());

        // Find students with registrations
        List<String> dkMaSV = authService.getStudentUsernamesWithRegistrations();
        for (String s : dkMaSV)
            if (s != null)
                dependentUsernames.add(s.trim().toUpperCase());

        Users currentUser = SessionUtil.getUser(httpSession);
        java.util.List<Map<String, Object>> userListWithNames = new java.util.ArrayList<>();
        for (Users u : userList) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", u.getUserId());
            map.put("username", u.getUsername());
            map.put("password", u.getPassword());
            map.put("roleId", u.getRoleId());
            String name = nameMap.get(u.getUsername());
            map.put("fullName", name != null ? name.trim() : "Tài khoản tùy chỉnh");

            // canDelete logic: not self AND (if lecturer/student, no dependencies)
            boolean canDelete = true;
            if (currentUser != null && currentUser.getUserId() == u.getUserId()) {
                canDelete = false;
            } else {
                String uname = u.getUsername() != null ? u.getUsername().trim().toUpperCase() : "";
                if (dependentUsernames.contains(uname)) {
                    canDelete = false;
                }
            }
            map.put("canDelete", canDelete);

            userListWithNames.add(map);
        }

        model.addAttribute("userList", userListWithNames);
        return "account/index";
    }

    @RequestMapping(value = "/api/get", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Users getUser(@RequestParam("userId") int userId) {
        return authService.getUserById(userId);
    }

    @RequestMapping(value = "/api/unassigned", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<?> getUnassignedUsers(@RequestParam("roleId") int roleId) {
        return authService.getUnassignedUsers(roleId, RoleEnum.SINHVIEN.getId());
    }

    @RequestMapping(value = "/api/save", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> saveUser(@RequestBody Users user, @RequestParam("mode") String mode,
            jakarta.servlet.http.HttpSession httpSession) {
        Map<String, Object> res = new HashMap<>();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(user, "user");
        usersValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldErrors().stream()
                    .map(org.springframework.validation.FieldError::getDefaultMessage)
                    .collect(Collectors.joining("<br>"));
            res.put("status", "error");
            res.put("message", errorMsg);
            return res;
        }
        try {
            // Prevent self-demotion
            Users currentUser = SessionUtil.getUser(httpSession);
            if (currentUser != null && currentUser.getUserId() == user.getUserId()) {
                if (currentUser.getRoleId() != user.getRoleId()) {
                    res.put("status", "error");
                    res.put("message", "Không thể tự thay đổi Nhóm quyền của tài khoản đang đăng nhập!");
                    return res;
                }
                currentUser.setUsername(user.getUsername());
                currentUser.setPassword(user.getPassword());
            }

            authService.saveUser(user, mode);
            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Lỗi: " + e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/api/delete", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String, Object> deleteUser(@RequestParam("userId") int userId,
            jakarta.servlet.http.HttpSession httpSession) {
        Map<String, Object> res = new HashMap<>();
        try {
            Users currentUser = SessionUtil.getUser(httpSession);
            if (currentUser != null && currentUser.getUserId() == userId) {
                res.put("status", "error");
                res.put("message", "Không thể xóa tài khoản mà bạn đang sử dụng để đăng nhập!");
                return res;
            }

            authService.deleteUser(userId);
            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Lỗi: " + e.getMessage());
        }
        return res;
    }
}
