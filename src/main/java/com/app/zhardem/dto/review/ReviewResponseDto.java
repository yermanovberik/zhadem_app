package com.app.zhardem.dto.review;

import lombok.Builder;

@Builder
public record ReviewResponseDto(
        Long id,
        Long userId,
        int rating,
        String reviewText
) {
}
