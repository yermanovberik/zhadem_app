package com.app.zhardem.dto.payment;

import java.time.LocalDateTime;

public record PaymentRequestDto(
        Long userId,
        String cardNumber,
        LocalDateTime expirationDate,
        int cvv
) {
}
