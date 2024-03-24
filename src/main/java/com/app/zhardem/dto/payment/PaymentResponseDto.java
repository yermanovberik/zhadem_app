package com.app.zhardem.dto.payment;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PaymentResponseDto(
       Long id,
       Long userID,
         String cardNumber,
        LocalDateTime expirationDate,
        int cvv

) {
}
