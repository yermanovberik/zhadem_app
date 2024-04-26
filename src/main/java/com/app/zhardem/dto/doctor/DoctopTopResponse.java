package com.app.zhardem.dto.doctor;

import lombok.Builder;

@Builder
public record DoctopTopResponse(
        String fullName,
        String imagePath,
        String specialization,
        double rating,
        double distance

) {
}
