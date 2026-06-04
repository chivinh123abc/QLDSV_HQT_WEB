package com.ptithcm.modules.payment;

import java.time.OffsetDateTime;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ptithcm.entities.DangKy;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private com.ptithcm.shared.services.MailerService mailerService;

    @Autowired
    private com.ptithcm.shared.services.InvoicePdfService invoicePdfService;

    // Lấy học kỳ mới nhất mà SV có đăng ký
    public String[] getLatestSemesterOfStudent(String maSV) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT dk.lopTinChi.nienKhoa, dk.lopTinChi.hocKy FROM DangKy dk WHERE dk.sinhVien.maSV = :maSV "
                + "ORDER BY dk.lopTinChi.nienKhoa DESC, dk.lopTinChi.hocKy DESC";
        List<Object[]> results = session.createQuery(hql, Object[].class).setParameter("maSV", maSV).setMaxResults(1)
                .getResultList();

        if (!results.isEmpty()) {
            return new String[]{(String) results.get(0)[0], String.valueOf(results.get(0)[1])};
        }
        return null;
    }

    // Lấy danh sách đăng ký của SV theo học kỳ
    public List<DangKy> getRegistrations(String maSV, String nienKhoa, int hocKy) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM DangKy dk JOIN FETCH dk.lopTinChi ltc JOIN FETCH ltc.monHoc "
                + "WHERE dk.sinhVien.maSV = :maSV AND ltc.nienKhoa = :nienKhoa AND ltc.hocKy = :hocKy";
        return session.createQuery(hql, DangKy.class).setParameter("maSV", maSV).setParameter("nienKhoa", nienKhoa)
                .setParameter("hocKy", hocKy).getResultList();
    }

    // Lấy danh sách đăng ký của SV chưa thanh toán theo học kỳ
    public List<DangKy> getUnpaidRegistrations(String maSV, String nienKhoa, int hocKy) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM DangKy dk JOIN FETCH dk.lopTinChi ltc JOIN FETCH ltc.monHoc "
                + "WHERE dk.sinhVien.maSV = :maSV AND ltc.nienKhoa = :nienKhoa AND ltc.hocKy = :hocKy "
                + "AND dk.daThanhToan = false AND dk.trangThaiDangKy = 'HIEU_LUC'";
        return session.createQuery(hql, DangKy.class).setParameter("maSV", maSV).setParameter("nienKhoa", nienKhoa)
                .setParameter("hocKy", hocKy).getResultList();
    }

    // Cập nhật trạng thái thanh toán
    public void markAsPaid(String maSV, String nienKhoa, int hocKy) {
        Session session = sessionFactory.getCurrentSession();
        List<DangKy> unpaids = getUnpaidRegistrations(maSV, nienKhoa, hocKy);
        if (unpaids.isEmpty()) {
            return;
        }

        // Calculate total amount and credits
        int totalCredits = 0;
        for (DangKy dk : unpaids) {
            totalCredits += dk.getLopTinChi().getMonHoc().getSoTinChi();
        }
        long totalAmount = totalCredits * 1_000_000L;

        // Perform updates
        for (DangKy dk : unpaids) {
            dk.setDaThanhToan(true);
            dk.setNgayThanhToan(OffsetDateTime.now());
            session.merge(dk);
        }

        // Fetch Student and Account for Email/PDF
        try {
            com.ptithcm.entities.SinhVien sv = session.get(com.ptithcm.entities.SinhVien.class, maSV);
            com.ptithcm.entities.TaiKhoan tk = session.get(com.ptithcm.entities.TaiKhoan.class, maSV);

            if (sv != null && tk != null && tk.getEmail() != null && !tk.getEmail().trim().isEmpty()) {
                String invoiceNumber = "HD-" + maSV + "-" + nienKhoa.replace("-", "") + "-" + hocKy + "-"
                        + (System.currentTimeMillis() % 100000);

                // Generate PDF
                byte[] pdfBytes = invoicePdfService.generateInvoicePdf(sv, unpaids, invoiceNumber, nienKhoa, hocKy);

                // Send email
                java.util.Map<String, String> vars = new java.util.HashMap<>();
                vars.put("studentName", sv.getHo() + " " + sv.getTen());
                vars.put("studentId", maSV);
                vars.put("hocKy", String.valueOf(hocKy));
                vars.put("nienKhoa", nienKhoa);
                vars.put("amount", String.format("%,d", totalAmount));
                vars.put("paymentDate", java.time.LocalDateTime.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                vars.put("invoiceNumber", invoiceNumber);

                com.ptithcm.shared.dtos.MailInfoDTO mailInfo = new com.ptithcm.shared.dtos.MailInfoDTO(
                        tk.getEmail().trim(), "[QLDSV] Xác nhận thanh toán học phí thành công - Học kỳ " + hocKy
                                + " (Niên khóa " + nienKhoa + ")",
                        "templates/payment_success_email.html", vars);

                mailerService.sendMail(mailInfo, pdfBytes,
                        "HoaDon_HocPhi_" + maSV + "_" + nienKhoa.replace("-", "_") + "_HK" + hocKy + ".pdf");
            }
        } catch (Exception e) {
            System.err.println("[PaymentService] Error sending payment success email/PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Lấy tất cả niên khóa và học kỳ có trong hệ thống (cho GV filter)
    public List<Object[]> getAllSemesters() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT DISTINCT ltc.nienKhoa, ltc.hocKy FROM LopTinChi ltc ORDER BY ltc.nienKhoa DESC, ltc.hocKy DESC";
        return session.createQuery(hql, Object[].class).getResultList();
    }

    // Thống kê thanh toán của lớp theo học kỳ
    public List<Object[]> getPaymentStatsByClass(String maLop, String nienKhoa, int hocKy) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT sv.maSV, CONCAT(sv.ho, ' ', sv.ten), "
                + "CAST(SUM(CASE WHEN dk.trangThaiDangKy = com.ptithcm.shared.enums.TrangThaiDangKy.HIEU_LUC THEN (mh.soTietLT / 15) + (mh.soTietTH / 30) ELSE 0 END) AS int), "
                + "CAST(SUM(CASE WHEN dk.trangThaiDangKy = com.ptithcm.shared.enums.TrangThaiDangKy.HIEU_LUC THEN ((mh.soTietLT / 15) + (mh.soTietTH / 30)) * 1000000L ELSE 0L END) AS long), "
                + "CASE WHEN SUM(CASE WHEN dk.trangThaiDangKy = com.ptithcm.shared.enums.TrangThaiDangKy.HIEU_LUC AND dk.daThanhToan = false THEN 1 ELSE 0 END) = 0 THEN true ELSE false END "
                + "FROM DangKy dk " + "JOIN dk.sinhVien sv " + "JOIN dk.lopTinChi ltc " + "JOIN ltc.monHoc mh "
                + "WHERE sv.lop.maLop = :maLop AND ltc.nienKhoa = :nienKhoa AND ltc.hocKy = :hocKy "
                + "GROUP BY sv.maSV, sv.ho, sv.ten "
                + "HAVING SUM(CASE WHEN dk.trangThaiDangKy = com.ptithcm.shared.enums.TrangThaiDangKy.HIEU_LUC THEN (mh.soTietLT / 15) + (mh.soTietTH / 30) ELSE 0 END) > 0";

        return session.createQuery(hql, Object[].class).setParameter("maLop", maLop).setParameter("nienKhoa", nienKhoa)
                .setParameter("hocKy", hocKy).getResultList();
    }
}
