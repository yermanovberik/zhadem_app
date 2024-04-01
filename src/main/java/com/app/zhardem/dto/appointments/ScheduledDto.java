package com.app.zhardem.dto.appointments;

import com.app.zhardem.enums.Status;
import com.app.zhardem.models.Doctor;
import lombok.Builder;


import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record ScheduledDto(
        String fullName,
         String specialization,

         String avatarPath,
         LocalDate date,
        LocalTime time,
        Status status
) {
}
