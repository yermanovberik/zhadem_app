package com.app.zhardem.dto.review;

import lombok.Builder;

@Builder
public record ReviewResponseDto(
       String name,
       Long userId,
        int rating,
        String reviewText
) {
}
