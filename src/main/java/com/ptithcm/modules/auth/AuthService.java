package com.ptithcm.modules.auth;

import com.ptithcm.entity.GiangVien;
import com.ptithcm.entity.SinhVien;
import com.ptithcm.shared.dto.UserSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private AuthDAO authDAO;

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
