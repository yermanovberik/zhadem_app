package com.app.zhardem.dto.category;

import lombok.Builder;

@Builder
public record CategoryResponseDto(
        String name,

        long id
) {
}
