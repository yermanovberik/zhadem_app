package com.app.zhardem.services.impl;

import com.app.zhardem.dto.appointments.AppointmentsRequestDto;
import com.app.zhardem.dto.appointments.AppointmentsResponseDto;
import com.app.zhardem.enums.Status;
import com.app.zhardem.exceptions.entity.EntityNotFoundException;
import com.app.zhardem.models.Appointments;
import com.app.zhardem.models.Doctor;
import com.app.zhardem.models.User;
import com.app.zhardem.repositories.AppointmentsRepository;
import com.app.zhardem.repositories.DoctorRepository;
import com.app.zhardem.repositories.UserRepository;
import com.app.zhardem.services.AppointmentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;



@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentsServiceImpl implements AppointmentsService {
    private final DoctorRepository doctorRepository;
    private final AppointmentsRepository appointmentsRepository;
    private final UserRepository userRepository;

    @Override
    public List<AppointmentsResponseDto> getDoctorAvailability(Long doctorId, int dayNumber) {
        LocalDate appointmentDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), dayNumber);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor with id " + doctorId + " does not exist."));

        List<Appointments> existingAppointments = appointmentsRepository.findByDoctorAndDate(doctor, appointmentDate);
        Set<LocalTime> bookedTimes = existingAppointments.stream()
                .map(Appointments::getTime)
                .collect(Collectors.toSet());

        List<AppointmentsResponseDto> availableTimeSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);
        long step = Duration.ofHours(1).toMinutes();

        for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(step)) {
            boolean isDisabled = bookedTimes.contains(time);
            availableTimeSlots.add(new AppointmentsResponseDto(time.toString(), isDisabled));
        }

        return availableTimeSlots;
    }



    public boolean bookAppointment(Long doctorId, LocalDateTime dateTime, Long userId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor with id " + doctorId  +" not found!"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId  +" not found!"));

        Optional<Appointments> existingAppointment = appointmentsRepository.findByDoctorAndDateAndTime(doctor, dateTime.toLocalDate(), dateTime.toLocalTime());
        if (existingAppointment.isPresent()) {
            throw new EntityNotFoundException("This appointment is already booked.");
        }

        Appointments appointment = new Appointments();
        appointment.setDoctor(doctor);
        appointment.setDate(dateTime.toLocalDate());
        appointment.setTime(dateTime.toLocalTime());
        appointment.setStatus(Status.IN_PROGRESS.toString());
        appointment.setDisabled(true);
        appointment.setUser(user);
        appointmentsRepository.save(appointment);
        return true;
    }

    public boolean cancelAppointment(Long appointmentId) {
        Appointments appointment = appointmentsRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment with id " + appointmentId + " not found!"));

        appointmentsRepository.delete(appointment);
        return true;
    }



    @Override
    public AppointmentsResponseDto getById(long id) {
        Appointments appointments = appointmentsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment with id " + id + " not found!"));
      return null;
    }

    @Override
    public AppointmentsResponseDto create(AppointmentsRequestDto requestDto) {
        return null;
    }

    @Override
    public AppointmentsResponseDto update(long id, AppointmentsRequestDto requestDto) {
        return null;
    }

    @Override
    @Transactional
    public void delete(long id) {
        Appointments appointments = appointmentsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment with id " + id + " not found!"));
        appointmentsRepository.delete(appointments);
    }

    @Override
    public Appointments getEntityById(long id) {
        return null;
    }
}
