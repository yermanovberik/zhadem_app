package com.app.zhardem.repositories;

import com.app.zhardem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByVerificationCode(String code);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String token);
}
