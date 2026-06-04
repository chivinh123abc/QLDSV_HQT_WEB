package com.ptithcm.modules.student;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.CSVReader;

import com.ptithcm.entities.Khoa;
import com.ptithcm.entities.Lop;
import com.ptithcm.entities.SinhVien;
import com.ptithcm.entities.TaiKhoan;
import com.ptithcm.modules.account.AccountDAO;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    @org.springframework.context.annotation.Lazy
    private StudentService self;

    public boolean studentExists(String maSV) {
        if (maSV == null) {
            return false;
        }
        return studentDAO.findById(maSV.trim()) != null;
    }

    public boolean accountExists(String username) {
        if (username == null) {
            return false;
        }
        return accountDAO.getAccountByUsername(username.trim()) != null;
    }

    public List<SinhVien> getStudentsWithAccount() {
        return studentDAO.getStudentsWithAccount();
    }

    @Transactional
    public void importStudentAndCreateAccount(SinhVien sv, TaiKhoan tk) throws Exception {
        if (sv.getMaLop() != null) {
            Lop lop = studentDAO.getSession().get(Lop.class, sv.getMaLop().trim());
            if (lop == null) {
                throw new Exception("Mã lớp [" + sv.getMaLop() + "] không tồn tại!");
            }
            sv.setLop(lop);
        } else {
            throw new Exception("Mã lớp không được để trống!");
        }
        studentDAO.save(sv);
        accountDAO.saveAccount(tk);
    }

    public List<Khoa> listKhoa() {
        return studentDAO.listKhoa();
    }

    public List<Lop> listLopByKhoa(String maKhoa) {
        return studentDAO.listLopByKhoa(maKhoa);
    }

    public List<Lop> listAllLop() {
        return studentDAO.listAllLop();
    }

    public List<SinhVien> listStudentsByClass(String maLop) {
        return studentDAO.listStudentsByClass(maLop);
    }

    public SinhVien getStudentById(String maSV) {
        return studentDAO.findById(maSV);
    }

    public void insertStudent(SinhVien sv) {
        if (sv.getMaLop() != null) {
            sv.setLop(studentDAO.getSession().get(Lop.class, sv.getMaLop()));
        }
        studentDAO.save(sv);
    }

    public void updateStudent(SinhVien sv) {
        if (sv.getMaLop() != null) {
            sv.setLop(studentDAO.getSession().get(Lop.class, sv.getMaLop()));
        }
        studentDAO.update(sv);
    }

    public void deleteStudent(String maSV) throws Exception {
        Long count = studentDAO.countDangKyByStudent(maSV);
        if (count > 0) {
            throw new Exception("Không thể xóa: Sinh viên đã có " + count + " bản ghi đăng ký môn học!");
        }
        SinhVien sv = studentDAO.findById(maSV);
        if (sv != null) {
            studentDAO.delete(sv);
        } else {
            throw new Exception("Không tìm thấy sinh viên để xóa!");
        }
    }

    public Long countDangKyByStudent(String maSV) {
        return studentDAO.countDangKyByStudent(maSV);
    }

    public String saveStudentApi(SinhVien sv, String mode) throws Exception {
        if (sv.getMaLop() != null) {
            sv.setLop(studentDAO.getSession().get(Lop.class, sv.getMaLop()));
        }
        SinhVien existing = studentDAO.findById(sv.getMaSV());
        if (mode.equals("add")) {
            if (existing != null) {
                throw new Exception("Mã sinh viên [" + sv.getMaSV() + "] đã tồn tại!");
            }
            studentDAO.save(sv);
        } else if (mode.equals("edit")) {
            if (existing == null) {
                throw new Exception("Không tìm thấy sinh viên để chỉnh sửa!");
            }
            studentDAO.update(sv);
        }
        return "success";
    }

    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED)
    public List<String> importStudentsFromCsv(InputStream inputStream) {
        List<String> errorLines = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String[] header = csvReader.readNext();
            if (header == null) {
                errorLines.add("File CSV trống.");
                return errorLines;
            }
            // Check headers
            if (header.length < 6) {
                errorLines.add(
                        "File CSV không đúng định dạng. Cần ít nhất 6 cột: MASV, HOTEN, GIOITINH, NGAYSINH, EMAIL, MALOP");
                return errorLines;
            }
            // Normalize header
            for (int i = 0; i < header.length; i++) {
                header[i] = header[i].trim().toUpperCase().replace("\uFEFF", ""); // remove BOM if present
            }
            if (!header[0].equals("MASV") || !header[1].equals("HOTEN") || !header[2].equals("GIOITINH")
                    || !header[3].equals("NGAYSINH") || !header[4].equals("EMAIL") || !header[5].equals("MALOP")) {
                errorLines.add("Header không hợp lệ. Yêu cầu: MASV, HOTEN, GIOITINH, NGAYSINH, EMAIL, MALOP");
                return errorLines;
            }

            String[] nextLine;
            int lineNumber = 1;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);

            while ((nextLine = csvReader.readNext()) != null) {
                lineNumber++;
                if (nextLine.length == 0 || (nextLine.length == 1 && nextLine[0].trim().isEmpty())) {
                    continue; // Skip empty rows
                }
                if (nextLine.length < 6) {
                    errorLines.add("Dòng " + lineNumber + ": Không đủ dữ liệu cột (yêu cầu 6 cột).");
                    continue;
                }

                String maSV = nextLine[0].trim();
                String hoTen = nextLine[1].trim();
                String gioiTinh = nextLine[2].trim();
                String ngaySinhStr = nextLine[3].trim();
                String email = nextLine[4].trim();
                String maLop = nextLine[5].trim();

                if (maSV.isEmpty() || hoTen.isEmpty() || gioiTinh.isEmpty() || ngaySinhStr.isEmpty() || email.isEmpty()
                        || maLop.isEmpty()) {
                    errorLines.add("Dòng " + lineNumber + ": Có trường dữ liệu rỗng.");
                    continue;
                }

                // Check gender format
                if (!gioiTinh.equalsIgnoreCase("Nam") && !gioiTinh.equalsIgnoreCase("Nữ")) {
                    errorLines.add("Dòng " + lineNumber + ": Giới tính phải là 'Nam' hoặc 'Nữ'.");
                    continue;
                }

                // Check date format
                java.util.Date ngaySinh;
                try {
                    ngaySinh = sdf.parse(ngaySinhStr);
                } catch (ParseException e) {
                    errorLines.add("Dòng " + lineNumber + ": Định dạng ngày sinh không hợp lệ (yêu cầu yyyy-MM-dd).");
                    continue;
                }

                // Email basic check
                if (!email.contains("@")) {
                    errorLines.add("Dòng " + lineNumber + ": Định dạng email không hợp lệ.");
                    continue;
                }

                try {
                    // Split Ho and Ten
                    int lastSpaceIdx = hoTen.lastIndexOf(' ');
                    String ho = "";
                    String ten = "";
                    if (lastSpaceIdx > 0) {
                        ho = hoTen.substring(0, lastSpaceIdx).trim();
                        ten = hoTen.substring(lastSpaceIdx + 1).trim();
                    } else {
                        ten = hoTen;
                    }

                    // Create objects
                    SinhVien sv = new SinhVien();
                    sv.setMaSV(maSV);
                    sv.setHo(ho);
                    sv.setTen(ten);
                    sv.setPhai(gioiTinh);
                    sv.setNgaySinh(ngaySinh);
                    sv.setMaLop(maLop);
                    sv.setTrangThaiHoc(com.ptithcm.shared.enums.TrangThaiHoc.DANG_HOC);
                    sv.setDiaChi("Chưa cập nhật");

                    TaiKhoan tk = new TaiKhoan();
                    tk.setTenDangNhap(maSV);
                    tk.setEmail(email);
                    tk.setPhanQuyen("SINHVIEN");
                    tk.setTrangThai(com.ptithcm.shared.enums.TrangThaiTaiKhoan.CHUA_KICH_HOAT);

                    // Generate random UUID password and BCrypt hash it
                    String rawPassword = UUID.randomUUID().toString().substring(0, 8);
                    String hashedPw = org.mindrot.jbcrypt.BCrypt.hashpw(rawPassword,
                            org.mindrot.jbcrypt.BCrypt.gensalt(12));
                    tk.setMatKhau(hashedPw);

                    self.importSingleStudent(sv, tk);
                } catch (Exception e) {
                    errorLines.add("Dòng " + lineNumber + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            errorLines.add("Lỗi đọc file: " + e.getMessage());
        }
        return errorLines;
    }

    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void importSingleStudent(SinhVien sv, TaiKhoan tk) throws Exception {
        if (studentExists(sv.getMaSV())) {
            throw new Exception("Mã sinh viên [" + sv.getMaSV() + "] đã tồn tại!");
        }
        if (accountExists(tk.getTenDangNhap())) {
            throw new Exception("Tài khoản với tên đăng nhập [" + tk.getTenDangNhap() + "] đã tồn tại!");
        }
        importStudentAndCreateAccount(sv, tk);
    }
}
