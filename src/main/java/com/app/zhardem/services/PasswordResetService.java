package com.app.zhardem.services;

import com.app.zhardem.models.User;

public interface PasswordResetService {
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
    String generatePasswordResetToken(User user);
}
