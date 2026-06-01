package com.ptithcm.shared.services;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ptithcm.shared.dtos.MailInfoDTO;

@Service
public class MailerService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(MailInfoDTO mailInfo) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String fromAddress = ((JavaMailSenderImpl) mailSender).getUsername();

        helper.setFrom(fromAddress);
        helper.setTo(mailInfo.getTo());
        helper.setSubject(mailInfo.getSubject());

        String htmlContent = loadAndParseTemplate(mailInfo.getTemplatePath(), mailInfo.getVariables());

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    private String loadAndParseTemplate(String templatePath, Map<String, String> variables) throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(templatePath)) {
            if (is == null) {
                throw new FileNotFoundException("Email template not found in classpath: " + templatePath);
            }
            String template = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            if (variables != null) {
                for (Map.Entry<String, String> entry : variables.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue() != null ? entry.getValue() : "";
                    template = template.replace("{{" + key + "}}", value);
                }
            }
            return template;
        }
    }
}
