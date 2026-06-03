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

    public List<String> extractMssvFromCsv(MultipartFile file) throws Exception {
        List<String> mssvList = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] line;
            boolean isHeader = true;
            while ((line = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                if (line.length == 0 || line[0].trim().isEmpty()) {
                    continue;
                }
                mssvList.add(line[0].trim());
            }
        }
        return mssvList;
    }

    public void exportCredentialsToCsv(List<String[]> data, HttpServletResponse response) throws Exception {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=tai_khoan_sv_generated.csv");
        response.setCharacterEncoding("UTF-8");

        // Ghi ký tự BOM UTF-8 (\ufeff) vào đầu stream để tránh lỗi font chữ trong
        // Microsoft Excel
        response.getOutputStream().write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

        try (CSVWriter writer = new CSVWriter(
                new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8))) {
            writer.writeNext(new String[]{"Tên Đăng Nhập", "Mật Khẩu Thô", "Email"});
            for (String[] row : data) {
                writer.writeNext(row);
            }
            writer.flush();
        }
    }
}
