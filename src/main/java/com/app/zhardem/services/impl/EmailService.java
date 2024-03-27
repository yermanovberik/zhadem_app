package com.app.zhardem.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    public void sendEmailWithCode(String email, String code) {
        // Реализация отправки электронного письма с кодом
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@example.com");
        message.setTo(email);
        message.setSubject("Your Password Reset Code");
        message.setText("Here is your password reset code: " + code);
        mailSender.send(message);
    }
}
