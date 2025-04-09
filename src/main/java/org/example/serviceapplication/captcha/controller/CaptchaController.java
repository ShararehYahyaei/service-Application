package org.example.serviceapplication.captcha.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class CaptchaController {
    @Autowired
    private CaptchaGenerator captchaGenerator;

    @GetMapping("/captcha/image")
    @ResponseBody
    public void getCaptchaImage(HttpServletResponse response, HttpSession session) throws IOException {

        CaptchaData captchaData = captchaGenerator.generateCaptcha();

        session.setAttribute("captcha", captchaData.getText());

        response.setContentType("image/jpeg");
        OutputStream out = response.getOutputStream();
        ImageIO.write(captchaData.getImage(), "JPEG", out);
        out.close();
    }
}