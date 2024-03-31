package com.app.zhardem.repositories;

import com.app.zhardem.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByFullName(String fullName);

    @Query("SELECT d.id, d.fullName, d.specialization, COALESCE(AVG(r.rating), 0), d.avatarPath, d.distance " +
            "FROM Doctor d LEFT JOIN d.reviews r " +
            "GROUP BY d.id " +
            "ORDER BY COALESCE(AVG(r.rating), 0) DESC")
    List<Object[]> findTopDoctors();

}
