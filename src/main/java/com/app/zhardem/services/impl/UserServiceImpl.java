package com.app.zhardem.services.impl;

import com.app.zhardem.dto.user.UserAllInfo;
import com.app.zhardem.dto.user.UserRequestDto;
import com.app.zhardem.dto.user.UserResponseDto;
import com.app.zhardem.enums.Role;
import com.app.zhardem.exceptions.entity.EntityAlreadyExistsException;
import com.app.zhardem.exceptions.entity.EntityNotFoundException;
import com.app.zhardem.models.User;
import com.app.zhardem.repositories.UserRepository;
import com.app.zhardem.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    @Override
    public void throwExceptionIfUserExists(String email) {
        userRepository.findByEmail(email)
                .ifPresent(foundUser -> {
                    throw new EntityAlreadyExistsException(
                            "User with the email " + foundUser.getEmail() + " already exists"
                    );
                });
    }

    @Override
    public UserAllInfo getAllInfo(long id) {
        User user = getEntityById(id);

        UserAllInfo responseDto = UserAllInfo.builder()
                .avatarPath(user.getAvatarPath())
                .IIN(user.getIIN())
                .sex(user.getSex())
                .region(user.getRegion())
                .birthDate(user.getBirthDate())
                .city(user.getCity())
                .fullName(user.getFullName())
                .address(user.getAddress())
                .build();

        return responseDto;
    }

    @Override
    public UserResponseDto getById(long id) {
        return null;
    }

    @Override
    public UserResponseDto create(UserRequestDto requestDto) {
        log.info("Creating new User with email: {}", requestDto.email());
        throwExceptionIfUserExists(requestDto.email());
        User user = User.builder()
                .email(requestDto.email())
                .role(Role.USER)
                .build();


        user.setPassword(
                passwordEncoder.encode(requestDto.password())
        );

        user = userRepository.save(user);
        UserResponseDto responseDto = UserResponseDto.builder()
                        .email(user.getEmail())
                         .role(Role.USER)
                          .build();
        log.info("Created new User with ID: {}", user.getId());

        return responseDto;
    }

    @Override
    public UserResponseDto update(long id, UserRequestDto requestDto) {
        return null;
    }

    @Override
    public void delete(long id) {
        log.info("Deleting User with ID: {}", id);

        User user = getEntityById(id);
        userRepository.delete(user);

        log.info("Deleted User with ID: {}", user.getId());
    }

    @Override
    public User getEntityById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " does not exist"));
    }


}
