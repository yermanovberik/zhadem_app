package com.app.zhardem.repositories;

import com.app.zhardem.models.Appointments;
import com.app.zhardem.models.Doctor;
import com.app.zhardem.models.User;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface AppointmentsRepository extends JpaRepository<Appointments, Long> {
    List<Appointments> findByDoctorAndDate(Doctor doctor, LocalDate date);
    Optional<Appointments> findByDoctorAndDateAndTime(Doctor doctor, LocalDate date, LocalTime time);
    List<Appointments> findByUser(User user);
    @Query("SELECT a FROM Appointments a")
    List<Appointments> getAll();

    @Query("SELECT a FROM Appointments a WHERE a.user.id = :userId AND a.doctor.id = :doctorId AND EXTRACT(DAY FROM a.date) = :dayNumber")
    List<Appointments> findAppointmentsByUserAndDoctorAndDay(
            @Param("userId") Long userId,
            @Param("doctorId") Long doctorId,
            @Param("dayNumber") int dayNumber);

}
