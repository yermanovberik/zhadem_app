package com.app.zhardem.dto.appointments;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record AppointmentsResponseDto(
        LocalTime time,

        boolean disable
) {
}
