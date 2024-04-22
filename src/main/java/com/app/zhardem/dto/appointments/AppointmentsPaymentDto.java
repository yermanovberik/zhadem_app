package com.app.zhardem.dto.appointments;

import com.app.zhardem.enums.Status;
import com.app.zhardem.models.Doctor;
import com.app.zhardem.models.Payment;
import com.app.zhardem.models.User;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record AppointmentsPaymentDto(
        String text,
        Long id,
        Long doctorId,
        LocalDate date,
        LocalTime time,
        long amountPaid
) {
}
