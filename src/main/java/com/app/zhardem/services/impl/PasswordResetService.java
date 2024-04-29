package com.app.zhardem.services.impl;

import com.app.zhardem.dto.PasswordTokenResponse;
import com.app.zhardem.exceptions.entity.EntityNotFoundException;
import com.app.zhardem.models.User;
import com.app.zhardem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;


    public void sendResetCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email: " + email + " not found."));

        String code = generateCode();
        user.setResetCode(code);
        user.setResetCodeExpiration(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        emailService.sendEmailWithCode(email, code);
    }
    public PasswordTokenResponse forgotYourPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiration(LocalDateTime.now().plusHours(1));
        userRepository.save(user);
        PasswordTokenResponse passwordTokenResponse = PasswordTokenResponse.builder()
                .token(resetToken)
                .build();
        return passwordTokenResponse;
    }

    private String generateCode() {
        return String.valueOf(new SecureRandom().nextInt(899999) + 100000);
    }


    public boolean validateResetToken(String token) {
        return userRepository.findByResetToken(token)
                .filter(user -> user.getResetTokenExpiration().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .filter(u -> u.getResetTokenExpiration().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new EntityNotFoundException("Invalid or expired reset token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiration(null);
        userRepository.save(user);
        return true;
    }
}
