package com.app.zhardem.repositories;

import com.app.zhardem.models.Appointments;
import com.app.zhardem.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentsRepository extends JpaRepository<Appointments, Long> {
    List<Appointments> findByDoctorAndDate(Doctor doctor, LocalDateTime dateTime);

    Optional<Appointments> findByDoctorAndDateAndTime(Doctor doctor, LocalDate date, LocalTime time);

}
