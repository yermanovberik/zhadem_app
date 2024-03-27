package com.app.zhardem.dto;

import lombok.Builder;

@Builder
public record CreatePaymentResponse(
        String sessionId,
        String sessionUrl
) {
}
