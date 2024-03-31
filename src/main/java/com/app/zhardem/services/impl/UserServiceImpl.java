package com.app.zhardem.services.impl;
import com.app.zhardem.dto.user.*;
import com.app.zhardem.enums.Role;
import com.app.zhardem.exceptions.entity.EntityAlreadyExistsException;
import com.app.zhardem.exceptions.entity.EntityNotFoundException;
import com.app.zhardem.models.User;
import com.app.zhardem.repositories.UserRepository;
import com.app.zhardem.services.FileService;
import com.app.zhardem.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

    @Override
    @Transactional
    public UserUploadPhotoDto uploadProfilePhoto(long id, MultipartFile file) {
        try {

            String fileName = fileService.uploadFile(file);

            User user = getEntityById(id);
            user.setAvatarPath(fileName);
            userRepository.save(user);

            return new UserUploadPhotoDto(user.getId(), user.getFullName());
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload profile photo", e);
        }
    }

    @Override
    @Transactional
    public UserAllInfo uploadFullInfo(long id,UserFullInfoDto request) {
        User user = getEntityById(id);
        user.setAddress(request.address());
        user.setCity(request.city());
        user.setIIN(request.IIN());
        user.setRegion(request.region());
        user.setSex(request.sex());
        user.setBirthDate(request.birthDate());

        UserAllInfo responseDto = UserAllInfo.builder()
                .region(user.getRegion())
                .sex(user.getSex())
                .IIN(user.getIIN())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .fullName(user.getFullName())
                .avatarPath(user.getAvatarPath())
                .build();
        return responseDto;
    }


 



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
        User user = getEntityById(id);

        UserResponseDto responseDto = UserResponseDto.builder()
                .id(id)
                .email(user.getEmail())
                .role(Role.USER)
                .build();

        return responseDto;
    }

    @Override
    public UserResponseDto findOrCreateUser(String email,Map<String, Object> attributes) {
        log.info("Finding or creating User with email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    log.info("User with email: {} not found, creating a new one", email);
                    User newUser = User.builder()
                            .email(email)
                            .fullName((String) attributes.get("name")) // Пример, зависит от структуры attributes
                            .role(Role.USER) // Предполагаем, что Role - это enum в вашей модели User
                            .build();
                    return userRepository.save(newUser);
                });

        // Создаем UserResponseDto для ответа
        UserResponseDto responseDto = UserResponseDto.builder()
                .email(user.getEmail())
                .role(user.getRole())
                // Добавьте другие поля в ответ, если необходимо
                .build();

        log.info("User with email: {} processed", email);
        return responseDto;
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
    @Transactional
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
