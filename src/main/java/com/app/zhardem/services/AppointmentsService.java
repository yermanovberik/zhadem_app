package com.app.zhardem.services;

import com.app.zhardem.dto.appointments.AppointmentsPaymentDto;
import com.app.zhardem.dto.appointments.AppointmentsRequestDto;
import com.app.zhardem.dto.appointments.AppointmentsResponseDto;
import com.app.zhardem.dto.appointments.ScheduledDto;
import com.app.zhardem.enums.Status;
import com.app.zhardem.exceptions.entity.EntityNotFoundException;
import com.app.zhardem.models.Appointments;
import com.app.zhardem.models.Doctor;
import com.app.zhardem.models.User;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface AppointmentsService extends CrudService<Appointments, AppointmentsRequestDto, AppointmentsResponseDto> {

    List<AppointmentsResponseDto> getDoctorAvailability(Long doctorId, int dayNumber);

    String handleBooking(Long doctorId, Long userId,LocalDateTime dayNumber);
    List<ScheduledDto> getScheduled(Long userId);

    AppointmentsPaymentDto bookAppointment(Long doctorId, LocalDateTime dateTime, Long userId);

    boolean cancelAppointment(Long appointmentId);

    boolean bookOrUpdateAppointment(Long doctorId, LocalDateTime dateTime, Long userId);

}
