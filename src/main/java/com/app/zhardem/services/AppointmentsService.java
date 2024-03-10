package com.app.zhardem.services;

import com.app.zhardem.dto.appointments.AppointmentsRequestDto;
import com.app.zhardem.dto.appointments.AppointmentsResponseDto;
import com.app.zhardem.models.Appointments;

public interface AppointmentsService extends CrudService<Appointments, AppointmentsRequestDto, AppointmentsResponseDto> {
}
