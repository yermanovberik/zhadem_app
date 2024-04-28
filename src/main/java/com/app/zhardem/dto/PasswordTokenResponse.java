package com.app.zhardem.dto;

import lombok.Builder;

@Builder
public record PasswordTokenResponse(
        String token
) {
}
