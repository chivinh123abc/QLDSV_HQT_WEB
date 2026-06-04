package com.ptithcm.modules.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ptithcm.entities.GiangVien;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.entities.TaiKhoan;
import com.ptithcm.shared.dtos.UserSession;

@Service
@Transactional
public class AuthService {

    @Autowired
    private AuthDAO authDAO;

    public TaiKhoan getTaiKhoanByUsername(String username) {
        return authDAO.findTaiKhoanByUsername(username);
    }

    public void activateAccount(String username, String newPassword) {
        TaiKhoan tk = authDAO.findTaiKhoanByUsername(username);
        if (tk != null) {
            tk.setMatKhau(org.mindrot.jbcrypt.BCrypt.hashpw(newPassword, org.mindrot.jbcrypt.BCrypt.gensalt(12)));
            tk.setTrangThai(com.ptithcm.shared.enums.TrangThaiTaiKhoan.DA_KICH_HOAT);
            authDAO.getSession().merge(tk);
        }
    }

    public void activateAccount(String username) {
        TaiKhoan tk = authDAO.findTaiKhoanByUsername(username);
        if (tk != null) {
            tk.setTrangThai(com.ptithcm.shared.enums.TrangThaiTaiKhoan.DA_KICH_HOAT);
            authDAO.getSession().merge(tk);
        }
    }

    public UserSession login(String username, String password) {
        return authDAO.findUserByUsernameAndPassword(username, password);
    }

    public GiangVien getGiangVienProfile(String username) {
        return authDAO.findGiangVienByMaGV(username);
    }

    public SinhVien getSinhVienProfile(String username) {
        return authDAO.findSinhVienByMaSV(username);
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
}
