package com.ptithcm.shared.dtos;

import java.util.Map;

public class MailInfoDTO {
    private String to;
    private String subject;
    private String templatePath;
    // Map containing dynamic variables to replace in the HTML template (e.g.
    // {"otpCode": "123456"})
    private Map<String, String> variables;

    public MailInfoDTO() {
    }

    public MailInfoDTO(String to, String subject, String templatePath, Map<String, String> variables) {
        this.to = to;
        this.subject = subject;
        this.templatePath = templatePath;
        this.variables = variables;
    }

    // --- Getters & Setters ---
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }
}
