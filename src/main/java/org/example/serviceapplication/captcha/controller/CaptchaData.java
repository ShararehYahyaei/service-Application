package org.example.serviceapplication.captcha.controller;

import java.awt.image.BufferedImage;

public class CaptchaData {
    private BufferedImage image;
    private String text;

    public CaptchaData(BufferedImage image, String text) {
        this.image = image;
        this.text = text;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getText() {
        return text;
    }
}
