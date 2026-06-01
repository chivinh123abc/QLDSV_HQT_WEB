package com.ptithcm.shared.services;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

@Service
public class CaptchaService {

    public BufferedImage generateCaptcha(HttpSession session) {
        int width = 120;
        int height = 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // Nền
        g.setColor(new Color(245, 245, 245));
        g.fillRect(0, 0, width, height);

        // Ký tự captcha
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        Random random = new Random();
        StringBuilder captchaStr = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            char c = chars.charAt(random.nextInt(chars.length()));
            captchaStr.append(c);
        }

        session.setAttribute("captcha_key", captchaStr.toString());

        // Vẽ các ký tự
        g.setFont(new Font("Segoe UI", Font.BOLD, 22));
        for (int i = 0; i < 5; i++) {
            g.setColor(new Color(30 + random.nextInt(100), 30 + random.nextInt(100), 30 + random.nextInt(100)));
            g.drawString(String.valueOf(captchaStr.charAt(i)), 15 + i * 18, 28);
        }

        // Vẽ nhiễu
        g.setColor(new Color(200, 200, 200));
        for (int i = 0; i < 5; i++) {
            g.drawLine(random.nextInt(width), random.nextInt(height), random.nextInt(width), random.nextInt(height));
        }

        g.dispose();
        return image;
    }
}
