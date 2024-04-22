package com.app.zhardem.dto.appointments;

import lombok.Builder;

@Builder
public record AppointmentsPaymentDto(
        String text,
        Long id
) {
}
