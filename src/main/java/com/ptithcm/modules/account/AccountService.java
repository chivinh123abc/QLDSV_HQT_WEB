package com.ptithcm.modules.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ptithcm.entities.GiangVien;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.entities.TaiKhoan;
import com.ptithcm.shared.enums.TrangThaiTaiKhoan;

@Service
@Transactional
public class AccountService {

    @Autowired
    private AccountDAO accountDAO;

    public List<String[]> provisionStudentAccounts(List<String[]> importData) {
        List<String[]> results = new ArrayList<>();

        for (String[] data : importData) {
            String mssv = data[0];
            String csvEmail = data[1];

            TaiKhoan tk = accountDAO.getAccountByUsername(mssv);
            if (tk == null) {
                String email = (csvEmail != null && !csvEmail.trim().isEmpty())
                        ? csvEmail.trim()
                        : (mssv + "@student.ptit.edu.vn");

                tk = new TaiKhoan();
                tk.setTenDangNhap(mssv);
                tk.setMatKhau(BCrypt.hashpw("", BCrypt.gensalt(12)));
                tk.setEmail(email);
                tk.setPhanQuyen("SINHVIEN");
                tk.setTrangThai(TrangThaiTaiKhoan.CHUA_KICH_HOAT);
                accountDAO.saveAccount(tk);

                results.add(new String[]{mssv, email});
            }
        }
        return results;
    }

    public List<Map<String, Object>> getAllAccounts(String currentLoggedInUser) {
        List<TaiKhoan> list = accountDAO.getAllAccounts();
        List<Map<String, Object>> userList = new ArrayList<>();

        for (TaiKhoan tk : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", tk.getTenDangNhap());
            map.put("username", tk.getTenDangNhap());
            map.put("email", tk.getEmail());

            int roleId = 3; // Mặc định SINHVIEN
            String roleName = "UNKNOWN";
            if ("PGV".equals(tk.getPhanQuyen())) {
                roleId = 1;
                roleName = "PGV";
            } else if ("KHOA".equals(tk.getPhanQuyen())) {
                roleId = 2;
                roleName = "KHOA";
            } else if ("SINHVIEN".equals(tk.getPhanQuyen())) {
                roleId = 3;
                roleName = "SINHVIEN";
            }
            map.put("roleId", roleId);
            map.put("roleName", roleName);

            // Lấy tên hiển thị
            String fullName = "Hệ thống";
            if (roleId == 3) {
                SinhVien sv = accountDAO.getSinhVienByMaSV(tk.getTenDangNhap());
                if (sv != null) {
                    fullName = sv.getHo() + " " + sv.getTen();
                }
            } else {
                GiangVien gv = accountDAO.getGiangVienByMaGV(tk.getTenDangNhap());
                if (gv != null) {
                    fullName = gv.getHo() + " " + gv.getTen();
                }
            }
            map.put("fullName", fullName);

            // Không cho phép tự xóa chính mình
            boolean canDelete = !tk.getTenDangNhap().equalsIgnoreCase(currentLoggedInUser);
            map.put("canDelete", canDelete);
            map.put("status", tk.getTrangThai() != null ? tk.getTrangThai().name() : "");

            userList.add(map);
        }
        return userList;
    }

    public List<SinhVien> getUnassignedStudents() {
        return accountDAO.getUnassignedStudents();
    }

    public List<GiangVien> getUnassignedLecturers() {
        return accountDAO.getUnassignedLecturers();
    }

    public TaiKhoan getAccountById(String userId) {
        return accountDAO.getAccountByUsername(userId);
    }

    public void updateAccount(TaiKhoan account) {
        accountDAO.updateAccount(account);
    }

    public void saveAccount(String username, String password, String roleIdStr, String email, String mode,
            String userId, Integer version, String statusStr) throws Exception {
        username = username.trim();
        email = email.trim();

        if ("add".equalsIgnoreCase(mode)) {
            TaiKhoan existing = accountDAO.getAccountByUsername(username);
            if (existing != null) {
                throw new IllegalArgumentException("Tên đăng nhập đã tồn tại!");
            }

            TaiKhoan tk = new TaiKhoan();
            tk.setTenDangNhap(username);
            tk.setMatKhau(BCrypt.hashpw(password, BCrypt.gensalt(12)));
            tk.setEmail(email);

            String phanQuyen = "SINHVIEN";
            if ("1".equals(roleIdStr)) {
                phanQuyen = "PGV";
            } else if ("2".equals(roleIdStr)) {
                phanQuyen = "KHOA";
            }
            tk.setPhanQuyen(phanQuyen);
            if ("SINHVIEN".equals(phanQuyen)) {
                tk.setTrangThai(TrangThaiTaiKhoan.CHUA_KICH_HOAT);
            } else {
                tk.setTrangThai(TrangThaiTaiKhoan.DA_KICH_HOAT); // PGV/KHOA tạo thì kích hoạt trực tiếp
            }

            accountDAO.saveAccount(tk);
        } else if ("edit".equalsIgnoreCase(mode)) {
            userId = userId.trim();
            TaiKhoan tk = accountDAO.getAccountByUsername(userId);
            if (tk == null) {
                throw new IllegalArgumentException("Không tìm thấy tài khoản!");
            }

            tk.setVersion(version);
            tk.setEmail(email);
            String phanQuyen = "SINHVIEN";
            if ("1".equals(roleIdStr)) {
                phanQuyen = "PGV";
            } else if ("2".equals(roleIdStr)) {
                phanQuyen = "KHOA";
            }
            tk.setPhanQuyen(phanQuyen);

            if (password != null && !password.trim().isEmpty()) {
                tk.setMatKhau(BCrypt.hashpw(password, BCrypt.gensalt(12)));
            }

            if (statusStr != null && !statusStr.trim().isEmpty()) {
                tk.setTrangThai(TrangThaiTaiKhoan.valueOf(statusStr));
            }

            accountDAO.updateAccount(tk);
        }
    }

    public void deleteAccount(String userId) throws Exception {
        TaiKhoan tk = accountDAO.getAccountByUsername(userId);
        if (tk != null) {
            accountDAO.deleteAccount(tk);
        } else {
            throw new IllegalArgumentException("Không tìm thấy tài khoản để xóa!");
        }
    }
}
