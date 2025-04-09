package org.example.serviceapplication.captcha.controller;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Component
public class CaptchaGenerator {
    private static final String CAPTCHA_SESSION_KEY = "captcha";
    public CaptchaData generateCaptcha() {
        int width = 150;
        int height = 50;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();


        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);


        String text = generateRandomText();
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.setColor(Color.BLACK);
        g.drawString(text, 20, 35);

        return new CaptchaData(image, text);
    }

    private String generateRandomText() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
