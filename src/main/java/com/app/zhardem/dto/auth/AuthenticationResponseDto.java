package com.app.zhardem.dto.auth;

import lombok.Builder;

@Builder
public record AuthenticationResponseDto(
        String accessToken,
        String refreshToken,
        Long userId
) { }
