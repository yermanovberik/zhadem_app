package com.app.zhardem.dto.doctor;

import org.springframework.web.multipart.MultipartFile;

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
