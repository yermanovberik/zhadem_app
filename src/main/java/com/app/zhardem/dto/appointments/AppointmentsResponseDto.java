package com.app.zhardem.dto.appointments;

import lombok.Builder;

@Builder
public record AppointmentsResponseDto(
        String time,
        boolean disable
) {

}
