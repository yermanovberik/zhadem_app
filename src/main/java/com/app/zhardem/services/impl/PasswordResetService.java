package com.app.zhardem.services.impl;

import com.app.zhardem.exceptions.entity.EntityNotFoundException;
import com.app.zhardem.models.User;
import com.app.zhardem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

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


    private void saveResetCode(String email, String code) {
    }

    private String generateCode() {
        return String.valueOf(new SecureRandom().nextInt(899999) + 100000);
    }


    public boolean validateResetCode(String email, String code) {
        return userRepository.findByEmail(email)
                .filter(user -> code.equals(user.getResetCode()))
                .filter(user -> user.getResetCodeExpiration().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    public boolean resetPassword(String email, String code, String newPassword) {
        if (validateResetCode(email, code)) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User with email: " + email + " not found."));

            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetCode(null);
            user.setResetCodeExpiration(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
