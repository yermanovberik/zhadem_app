package com.app.zhardem.repositories;

import com.app.zhardem.models.Doctor;
import com.app.zhardem.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByDoctor(Doctor doctor);
    List<Review> findByDoctorOrderByCreatedAt(Doctor doctor);

}
