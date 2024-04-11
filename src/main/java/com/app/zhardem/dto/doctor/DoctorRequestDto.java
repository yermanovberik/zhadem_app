package com.app.zhardem.dto.doctor;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record DoctorRequestDto(
        String fullName,
        double distance,
        String specialization,
        String aboutText,
        String category,
        int priceOfDoctor,
        MultipartFile file
) {
}
