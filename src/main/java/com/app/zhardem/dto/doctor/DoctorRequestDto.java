package com.app.zhardem.dto.doctor;

public record DoctorRequestDto(
        String fullName,
        double distance,
        String specialization,
        String aboutText,
        String category,
        int priceOfDoctor
) {
}
