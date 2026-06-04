package com.ptithcm.shared.services;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import com.ptithcm.entities.DangKy;
import com.ptithcm.entities.SinhVien;

@Service
public class InvoicePdfService {

    public byte[] generateInvoicePdf(SinhVien sv, List<DangKy> dsDangKy, String invoiceNumber, String nienKhoa,
            int hocKy) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Font styles
            Font titleFont = getFont(20, Font.BOLD, new Color(67, 97, 238));
            Font sectionFont = getFont(14, Font.BOLD, new Color(33, 37, 41));
            Font boldFont = getFont(11, Font.BOLD, new Color(33, 37, 41));
            Font normalFont = getFont(11, Font.NORMAL, new Color(73, 80, 87));
            Font headerFont = getFont(11, Font.BOLD, Color.WHITE);
            Font footerFont = getFont(9, Font.ITALIC, new Color(108, 117, 125));

            // Header Paragraph
            Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN HỌC PHÍ", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Invoice Info Table
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(20);

            // Left side: Student info
            String fullName = sv.getHo() + " " + sv.getTen();
            String classCode = sv.getLop() != null ? sv.getLop().getMaLop() : "N/A";
            String className = sv.getLop() != null ? sv.getLop().getTenLop() : "N/A";

            PdfPCell leftCell = new PdfPCell();
            leftCell.setBorder(PdfPCell.NO_BORDER);
            leftCell.addElement(new Paragraph("Thông tin sinh viên:", sectionFont));
            leftCell.addElement(new Paragraph("Họ và tên: " + fullName, normalFont));
            leftCell.addElement(new Paragraph("Mã sinh viên: " + sv.getMaSV(), normalFont));
            leftCell.addElement(new Paragraph("Lớp: " + classCode + " - " + className, normalFont));
            infoTable.addCell(leftCell);

            // Right side: Invoice metadata
            PdfPCell rightCell = new PdfPCell();
            rightCell.setBorder(PdfPCell.NO_BORDER);
            rightCell.addElement(new Paragraph("Chi tiết hóa đơn:", sectionFont));
            rightCell.addElement(new Paragraph("Mã hóa đơn: " + invoiceNumber, normalFont));
            rightCell.addElement(new Paragraph("Học kỳ: " + hocKy + " (Niên khóa " + nienKhoa + ")", normalFont));
            rightCell.addElement(new Paragraph(
                    "Ngày thanh toán: "
                            + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    normalFont));
            infoTable.addCell(rightCell);

            document.add(infoTable);

            // Horizontal Line
            Paragraph line = new Paragraph(
                    "----------------------------------------------------------------------------------------------------------------------------------",
                    normalFont);
            line.setSpacingAfter(15);
            document.add(line);

            // Subjects Table
            PdfPTable table = new PdfPTable(new float[]{1, 2.5f, 4, 1.5f, 3});
            table.setWidthPercentage(100);
            table.setSpacingAfter(20);

            // Headers
            addHeaderCell(table, "STT", headerFont);
            addHeaderCell(table, "Mã môn học", headerFont);
            addHeaderCell(table, "Tên môn học", headerFont);
            addHeaderCell(table, "Số tín chỉ", headerFont);
            addHeaderCell(table, "Học phí (VNĐ)", headerFont);

            int index = 1;
            int totalCredits = 0;
            long totalAmount = 0;

            for (DangKy dk : dsDangKy) {
                int tinChi = dk.getLopTinChi().getMonHoc().getSoTinChi();
                long amount = tinChi * 1_000_000L;

                totalCredits += tinChi;
                totalAmount += amount;

                addBodyCell(table, String.valueOf(index++), normalFont, Element.ALIGN_CENTER);
                addBodyCell(table, dk.getLopTinChi().getMonHoc().getMaMH(), normalFont, Element.ALIGN_LEFT);
                addBodyCell(table, dk.getLopTinChi().getMonHoc().getTenMH(), normalFont, Element.ALIGN_LEFT);
                addBodyCell(table, String.valueOf(tinChi), normalFont, Element.ALIGN_CENTER);
                addBodyCell(table, String.format("%,d", amount), normalFont, Element.ALIGN_RIGHT);
            }

            document.add(table);

            // Total section
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);
            totalTable.setSpacingAfter(30);

            PdfPCell totalLeft = new PdfPCell(new Phrase("Tổng số tín chỉ:", boldFont));
            totalLeft.setBorder(PdfPCell.NO_BORDER);
            totalTable.addCell(totalLeft);

            PdfPCell totalLeftVal = new PdfPCell(new Phrase(String.valueOf(totalCredits), boldFont));
            totalLeftVal.setBorder(PdfPCell.NO_BORDER);
            totalLeftVal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.addCell(totalLeftVal);

            PdfPCell totalRight = new PdfPCell(new Phrase("Tổng tiền thanh toán:", titleFont));
            totalRight.setBorder(PdfPCell.NO_BORDER);
            totalTable.addCell(totalRight);

            PdfPCell totalRightVal = new PdfPCell(new Phrase(String.format("%,d VNĐ", totalAmount), titleFont));
            totalRightVal.setBorder(PdfPCell.NO_BORDER);
            totalRightVal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.addCell(totalRightVal);

            document.add(totalTable);

            // Footer / Signature
            Paragraph footer = new Paragraph(
                    "Cảm ơn quý sinh viên đã hoàn thành học phí đúng thời hạn.\nHóa đơn được xuất tự động từ hệ thống và có giá trị xác nhận thanh toán trực tuyến.",
                    footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
        } catch (Exception e) {
            System.err.println("[InvoicePdfService] Error generating PDF: " + e.getMessage());
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new Color(67, 97, 238));
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }

    private void addBodyCell(PdfPTable table, String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(6);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }

    private Font getFont(float size, int style, Color color) {
        try {
            String[] fontPaths = {"C:\\Windows\\Fonts\\arial.ttf", "C:\\Windows\\Fonts\\times.ttf",
                    "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf",
                    "/usr/share/fonts/truetype/msttcorefonts/Arial.ttf"};

            String chosenPath = null;
            for (String path : fontPaths) {
                if (new java.io.File(path).exists()) {
                    chosenPath = path;
                    break;
                }
            }

            if (chosenPath != null) {
                BaseFont bf = BaseFont.createFont(chosenPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                return new Font(bf, size, style, color);
            }
        } catch (Exception e) {
            System.err.println("[InvoicePdfService] Failed to load unicode font: " + e.getMessage());
        }
        return FontFactory.getFont(FontFactory.HELVETICA, size, style, color);
    }
}
