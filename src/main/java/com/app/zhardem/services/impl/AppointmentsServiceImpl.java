package com.app.zhardem.services.impl;

import com.app.zhardem.dto.appointments.AppointmentsRequestDto;
import com.app.zhardem.dto.appointments.AppointmentsResponseDto;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentsServiceImpl implements AppointmentsService {
    private final DoctorRepository doctorRepository;
    private final AppointmentsRepository appointmentsRepository;
    private final UserRepository userRepository;

    public List<AppointmentsResponseDto> getDoctorAvailability(Long doctorId, int dayNumber) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor with id " + doctorId  +" not found!"));

        List<LocalTime> availableTimes = generateAvailableTimes(dayNumber);

        LocalDate currentDate = LocalDate.now();

        LocalDate targetDate = currentDate.plusDays(dayNumber);

        LocalDateTime startOfDay = targetDate.atStartOfDay();
        List<Appointments> bookedAppointments = appointmentsRepository.findByDoctorAndDate(doctor, startOfDay);


        Set<LocalTime> bookedTimes = bookedAppointments.stream()
                .map(Appointments::getTime)
                .collect(Collectors.toSet());

        return availableTimes.stream()
                .map(time -> {
                    boolean disable = bookedTimes.contains(time);
                    return new AppointmentsResponseDto(time, disable);
                })
                .collect(Collectors.toList());
    }
    public boolean bookAppointment(Long doctorId, LocalDateTime dateTime, Long userId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor with id " + doctorId  +" not found!"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId  +" not found!"));

        Optional<Appointments> existingAppointment = appointmentsRepository.findByDoctorAndDateAndTime(doctor, dateTime.toLocalDate(), dateTime.toLocalTime());
        if (existingAppointment.isPresent()) {
            throw new RuntimeException("This appointment is already booked.");
        }

        Appointments appointment = new Appointments();
        appointment.setDoctor(doctor);
        appointment.setDate(dateTime);
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


    private List<LocalTime> generateAvailableTimes(int dayNumber) {
        List<LocalTime> availableTimes = new ArrayList<>();
        availableTimes.add(LocalTime.of(9, 0));
        availableTimes.add(LocalTime.of(10, 0));
        availableTimes.add(LocalTime.of(11, 0));
        availableTimes.add(LocalTime.of(13, 0));
        availableTimes.add(LocalTime.of(14, 0));
        availableTimes.add(LocalTime.of(15, 0));
        availableTimes.add(LocalTime.of(16, 0));
        availableTimes.add(LocalTime.of(17, 0));
        availableTimes.add(LocalTime.of(18, 0));
        return availableTimes;
    }
    @Override
    public AppointmentsResponseDto getById(long id) {
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
    public void delete(long id) {

    }

    @Override
    public Appointments getEntityById(long id) {
        return null;
    }
}
