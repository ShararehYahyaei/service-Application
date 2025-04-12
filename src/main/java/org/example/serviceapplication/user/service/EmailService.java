    package org.example.serviceapplication.user.service;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.mail.SimpleMailMessage;
    import org.springframework.mail.javamail.JavaMailSender;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    @Service
    public class EmailService {

        @Autowired
        private JavaMailSender mailSender;
        @Transactional
        public void sendVerificationEmail(String toEmail, String token) {
            String subject = "تأیید ایمیل";
            String url = "http://localhost:8082/verify?token=" + token;
            String body = "سلام! برای فعال‌سازی حساب خود، روی لینک زیر کلیک کنید:\n\n" + url;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        }
    }
