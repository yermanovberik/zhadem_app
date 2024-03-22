package com.app.zhardem.loader;

import com.app.zhardem.models.Appointments;
import com.app.zhardem.models.Doctor;
import com.app.zhardem.repositories.AppointmentsRepository;
import com.app.zhardem.repositories.DoctorRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitialDataLoader {
    private final AppointmentsRepository appointmentsRepository;
    private final DoctorRepository doctorRepository;

    //@PostConstruct
    public void loadData() {
        List<Doctor> doctors = doctorRepository.findAll();
        for (Doctor doctor : doctors) {
            createInitialAppointmentsForDoctor(doctor);
        }
    }

    private void createInitialAppointmentsForDoctor(Doctor doctor) {
        List<LocalTime> availableTimes = generateAvailableTimes();
        for(LocalTime time : availableTimes){
            LocalDateTime appointmentDateTime = LocalDateTime.of(LocalDate.now(), time);

            Appointments appointments = Appointments.builder()
                    .doctor(doctor)
                    .date(LocalDate.from(appointmentDateTime))
                    .time(appointmentDateTime.toLocalTime())
                    .disabled(false)
                    .build();
            appointmentsRepository.save(appointments);
        }
    }

    private List<LocalTime> generateAvailableTimes() {
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
}
