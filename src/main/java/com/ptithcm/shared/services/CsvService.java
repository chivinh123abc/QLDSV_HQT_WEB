package com.ptithcm.shared.services;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

@Service
public class CsvService {

    public List<String> extractMssvFromCsv(MultipartFile file, List<String> errorLines) throws Exception {
        List<String> mssvList = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] line;
            boolean isHeader = true;
            int lineNumber = 0;
            while ((line = reader.readNext()) != null) {
                lineNumber++;
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                if (line.length == 0 || (line.length == 1 && line[0].trim().isEmpty())) {
                    continue;
                }
                try {
                    if (line[0] == null || line[0].trim().isEmpty()) {
                        errorLines.add("Dòng " + lineNumber + ": Mã sinh viên trống.");
                        continue;
                    }
                    mssvList.add(line[0].trim());
                } catch (NullPointerException | IndexOutOfBoundsException | IllegalStateException e) {
                    errorLines.add("Dòng " + lineNumber + ": Định dạng dữ liệu không hợp lệ hoặc bị lỗi ("
                            + e.getClass().getSimpleName() + ").");
                }
            }
        }
        return mssvList;
    }

    public List<String> extractMssvFromCsv(MultipartFile file) throws Exception {
        return extractMssvFromCsv(file, new ArrayList<>());
    }

    public void exportCredentialsToCsv(List<String[]> data, List<String> errorLines, HttpServletResponse response)
            throws Exception {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=tai_khoan_sv_generated.csv");
        response.setCharacterEncoding("UTF-8");

        // Ghi ký tự BOM UTF-8 (\ufeff) vào đầu stream để tránh lỗi font chữ trong
        // Microsoft Excel
        response.getOutputStream().write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

        try (CSVWriter writer = new CSVWriter(
                new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8))) {
            writer.writeNext(new String[]{"Tên Đăng Nhập", "Email"});
            for (String[] row : data) {
                writer.writeNext(row);
            }
            if (errorLines != null && !errorLines.isEmpty()) {
                writer.writeNext(new String[]{""}); // Empty row
                writer.writeNext(new String[]{"--- DANH SÁCH LỖI DÒNG KHI NHẬP CSV ---"});
                for (String err : errorLines) {
                    writer.writeNext(new String[]{err});
                }
            }
            writer.flush();
        }
    }

    public void exportCredentialsToCsv(List<String[]> data, HttpServletResponse response) throws Exception {
        exportCredentialsToCsv(data, null, response);
    }

    public List<String[]> extractAccountImportFromCsv(MultipartFile file) throws Exception {
        List<String[]> dataList = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] line;
            boolean isHeader = true;
            while ((line = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                if (line.length == 0 || (line.length == 1 && line[0].trim().isEmpty())) {
                    continue;
                }
                String mssv = line[0].trim();
                String email = "";
                if (line.length > 2 && line[2] != null) {
                    email = line[2].trim();
                }
                dataList.add(new String[]{mssv, email});
            }
        }
        return dataList;
    }
}
