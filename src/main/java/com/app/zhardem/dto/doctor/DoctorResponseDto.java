package com.app.zhardem.dto.doctor;

import lombok.Builder;

@Builder
public record DoctorResponseDto(
        Long id,
        String fullName,
        double rating,
        double distance,

        String about,
        String avatarPath,

        String category
) {
}
