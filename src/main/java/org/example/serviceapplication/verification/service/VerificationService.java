package org.example.serviceapplication.verification.service;

import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.Setter;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.EmailService;
import org.example.serviceapplication.user.service.UserService;
import org.example.serviceapplication.verification.model.VerificationToken;
import org.example.serviceapplication.verification.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Getter
@Setter
public class VerificationService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserService userService;
    private final EmailService emailService;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public VerificationService(VerificationTokenRepository verificationTokenRepository, UserService userService, EmailService emailService) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Transactional
    public String generateVerificationToken(String email) {
        User user = userService.getUserByEmail(email);
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    @Transactional
    public boolean verifyToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

    @Transactional
    public void sendVerificationEmail(String email, String token) {
        String url = "http://localhost:8082/verify?token=" + token;
        String subject = "Email Verification";
        String htmlContent = "<p>Click the link below to verify your email:</p>"
                + "<a href=\"" + url + "\">Verify Email</a>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("shariiishari662@gmail.com");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Error sending HTML email: " + e.getMessage());
        }

    }
}
