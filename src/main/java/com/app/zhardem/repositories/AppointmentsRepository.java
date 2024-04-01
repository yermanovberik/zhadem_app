package com.app.zhardem.repositories;

import com.app.zhardem.models.Appointments;
import com.app.zhardem.models.Doctor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface AppointmentsRepository extends JpaRepository<Appointments, Long> {
    List<Appointments> findByDoctorAndDate(Doctor doctor, LocalDate date);
    Optional<Appointments> findByDoctorAndDateAndTime(Doctor doctor, LocalDate date, LocalTime time);

    @Query("SELECT a FROM Appointments a")
    List<Appointments> getAll();
}
