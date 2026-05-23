package com.ptithcm.modules.auth;

import com.ptithcm.entity.GiangVien;
import com.ptithcm.entity.SinhVien;
import com.ptithcm.entity.Users;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private AuthDAO authDAO;

    public Users login(String username, String password) {
        return authDAO.findUserByUsernameAndPassword(username, password);
    }

    public GiangVien getGiangVienProfile(String username) {
        return authDAO.findGiangVienByMaGV(username);
    }

    public SinhVien getSinhVienProfile(String username) {
        return authDAO.findSinhVienByMaSV(username);
    }

    public List<Users> listUsers() {
        return authDAO.findAll();
    }

    public List<Object[]> listGiangVienNames() {
        return authDAO.listGiangVienNames();
    }

    public List<Object[]> listSinhVienNames() {
        return authDAO.listSinhVienNames();
    }

    public List<String> getLecturerUsernamesWithCreditClasses() {
        return authDAO.getLecturerUsernamesWithCreditClasses();
    }

    public List<String> getStudentUsernamesWithRegistrations() {
        return authDAO.getStudentUsernamesWithRegistrations();
    }

    public List<?> getUnassignedUsers(int roleId, int studentRoleId) {
        if (roleId == studentRoleId) {
            return authDAO.getUnassignedStudents();
        } else {
            return authDAO.getUnassignedLecturers();
        }
    }

    public Users getUserById(int userId) {
        return authDAO.findById(userId);
    }

    public String saveUser(Users user, String mode) throws Exception {
        // Nếu tên đăng nhập không khớp với ID được chọn, đảm bảo nó không trùng với
        // mã của Giảng viên/Sinh viên khác đã tồn tại
        if (user.getSelectedId() != null && !user.getUsername().equals(user.getSelectedId())) {
            Long countGV = authDAO.countGiangVienByMaGV(user.getUsername());
            Long countSV = authDAO.countSinhVienByMaSV(user.getUsername());
            if (countGV > 0 || countSV > 0) {
                throw new Exception("Tên đăng nhập [" + user.getUsername()
                        + "] trùng với Mã của một Giảng viên/Sinh viên khác! Vui lòng đặt tên khác.");
            }
        }

        if ("add".equals(mode)) {
            // Kiểm tra xem tên đăng nhập đã có tài khoản chưa
            Long existCount = authDAO.countUsersByUsername(user.getUsername());
            if (existCount > 0) {
                throw new Exception("Tên đăng nhập [" + user.getUsername() + "] đã được cấp tài khoản trước đó!");
            }
            authDAO.save(user);
        } else if ("edit".equals(mode)) {
            Users existing = authDAO.findById(user.getUserId());
            if (existing == null) {
                throw new Exception("Không tìm thấy tài khoản để chỉnh sửa!");
            }
            // Kiểm tra xem tên đăng nhập có bị trùng với tài khoản khác không
            Long existCount = authDAO.countUsersByUsernameExcludingId(user.getUsername(), user.getUserId());
            if (existCount > 0) {
                throw new Exception("Tên đăng nhập [" + user.getUsername() + "] đã được sử dụng bởi tài khoản khác!");
            }
            authDAO.update(user);
        }
        return "success";
    }

    public void deleteUser(int userId) throws Exception {
        Users user = authDAO.findById(userId);
        if (user == null) {
            throw new Exception("Không tìm thấy tài khoản để xóa!");
        }

        String uname = user.getUsername() != null ? user.getUsername().trim() : "";
        Long ltcCount = authDAO.countLtcByLecturerUsername(uname);
        if (ltcCount > 0) {
            throw new Exception(
                    "Không thể xóa: Tài khoản này thuộc về Giảng viên đang phụ trách " + ltcCount + " lớp tín chỉ!");
        }

        Long dkCount = authDAO.countDangKyByStudentUsername(uname);
        if (dkCount > 0) {
            throw new Exception(
                    "Không thể xóa: Tài khoản này thuộc về Sinh viên đã đăng ký " + dkCount + " lớp tín chỉ!");
        }

        authDAO.delete(user);
    }
}
