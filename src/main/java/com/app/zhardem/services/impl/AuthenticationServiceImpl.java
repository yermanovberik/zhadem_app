package com.app.zhardem.services.impl;

import com.app.zhardem.dto.auth.AuthenticationRequestDto;
import com.app.zhardem.dto.auth.AuthenticationResponseDto;
import com.app.zhardem.dto.auth.RegisterRequestDto;
import com.app.zhardem.enums.Role;
import com.app.zhardem.exceptions.JwtSubjectMissingException;
import com.app.zhardem.exceptions.JwtTokenExpiredException;
import com.app.zhardem.jwt.JwtFactory;
import com.app.zhardem.jwt.JwtParser;
import com.app.zhardem.jwt.JwtValidator;
import com.app.zhardem.models.User;
import com.app.zhardem.repositories.UserRepository;
import com.app.zhardem.services.AuthenticationService;
import com.app.zhardem.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtFactory jwtFactory;
    private final JwtValidator jwtValidator;
    private final JwtParser jwtParser;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private static final SecureRandom secureRandom = new SecureRandom();


    @Override
    @Transactional
    public AuthenticationResponseDto register(RegisterRequestDto request) {
        userService.throwExceptionIfUserExists(request.email());
        boolean isAdmin = request.fullName().equalsIgnoreCase("Zhardem App");
        Role userRole = isAdmin ? Role.ADMIN : Role.USER;
        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(userRole)
                .build();

        userRepository.save(user);

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationCode(verificationToken);
        user.setCodeExpiration(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        String emailBody = "Code is " + verificationToken;
        emailService.sendVerificationEmail(user.getEmail(), "Email Verification", emailBody);

        return new AuthenticationResponseDto(null, null, user.getId(),"null");
    }
    private String generateCode() {
        int code = 1000 + secureRandom.nextInt(9000);
        return String.valueOf(code);
    }


    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with email " + request.email() + " not found"
                ));

        String accessToken = jwtFactory.generateAccessToken(user);
        String refreshToken = jwtFactory.generateRefreshToken(user);

        log.info("User authenticated successfully with email: {}", user.getEmail());
    /*
    user_id
     */
        return AuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    public AuthenticationResponseDto refreshToken(String authHeader) {
        log.info("Received refresh token request");

        String refreshToken = authHeader.substring(7);

        User user = jwtParser.extractEmail(refreshToken)
                .map(userRepository::findByEmail)
                .orElseThrow(() -> new JwtSubjectMissingException("JWT subject cannot be null"))
                .orElseThrow(() -> new EntityNotFoundException("User with this email was not found"));

        jwtValidator.ifTokenExpiredThrow(refreshToken, () -> new JwtTokenExpiredException("Refresh token has expired"));

        String accessToken = jwtFactory.generateAccessToken(user);

        log.info("User refreshed token successfully with email: {}", user.getEmail());

        return AuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    public AuthenticationResponseDto registerOrUpdateUser(RegisterRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.email())
                .map(existingUser -> {
                    existingUser.setPassword(passwordEncoder.encode(requestDto.password())); // Обновляем пароль
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(requestDto.email());
                    newUser.setPassword(passwordEncoder.encode(requestDto.password()));
                    newUser.setRole(Role.USER);
                    return userRepository.save(newUser);
                });

        // Генерация новых токенов
        String accessToken = jwtFactory.generateAccessToken(user);
        String refreshToken = jwtFactory.generateRefreshToken(user);

        return AuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }




}
