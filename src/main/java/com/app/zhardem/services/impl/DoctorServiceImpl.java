package com.app.zhardem.services.impl;

import com.app.zhardem.dto.category.CategoryRequestDto;
import com.app.zhardem.dto.doctor.DoctopTopResponse;
import com.app.zhardem.dto.doctor.DoctorRequestDto;
import com.app.zhardem.dto.doctor.DoctorResponseDto;
import com.app.zhardem.exceptions.entity.EntityAlreadyExistsException;
import com.app.zhardem.exceptions.entity.EntityNotFoundException;
import com.app.zhardem.models.Category;
import com.app.zhardem.models.Doctor;
import com.app.zhardem.repositories.CategoryRepository;
import com.app.zhardem.repositories.DoctorRepository;
import com.app.zhardem.services.CategoryService;
import com.app.zhardem.services.DoctorService;
import com.app.zhardem.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final FileService fileService;

    @Override
    public DoctorResponseDto getById(long id) {
        Doctor doctor = getEntityById(id);

        DoctorResponseDto responseDto = DoctorResponseDto.builder()
                .avatarPath(doctor.getAvatarPath())
                .about(doctor.getAboutText())
                .fullName(doctor.getFullName())
                .distance(doctor.getDistance())
                .rating(doctor.getAverageRating())
                .build();
        return responseDto;
    }


    @Override
    public DoctorResponseDto create(DoctorRequestDto requestDto) {
       throwExceptionIfDoctorExists(requestDto.fullName());
        Doctor doctor = Doctor.builder()
                .aboutText(requestDto.aboutText())
                .distance(requestDto.distance())
                .fullName(requestDto.fullName())
                .specialization(requestDto.specialization())
                .priceOfDoctor(requestDto.priceOfDoctor())
                .build();

        doctorRepository.save(doctor);
        DoctorResponseDto responseDto = DoctorResponseDto.builder()
                .avatarPath(doctor.getAvatarPath())
                .about(doctor.getAboutText())
                .fullName(doctor.getFullName())
                .distance(doctor.getDistance())
                .rating(doctor.getAverageRating())
                .build();

        return responseDto;
    }

    @Override
    public DoctorResponseDto update(long id, DoctorRequestDto requestDto) {
        Doctor doctor = getEntityById(id);
        return null;
    }

    @Override
    @Transactional
    public void delete(long id) {
        Doctor doctor = getEntityById(id);
        doctorRepository.delete(doctor);
    }

    @Override
    public Doctor getEntityById(long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor with this id " + id + " not found!"));
    }

    private void throwExceptionIfDoctorExists(String name) {
        doctorRepository.findByFullName(name)
                .ifPresent(foundTeam -> {
                    throw new EntityAlreadyExistsException(
                            "Doctor with the full name '" + name + "' already exists"
                    );
                });
    }

    @Override
    public List<DoctopTopResponse> findTopDoctor() {
        List<Object[]> topDoctorsData = doctorRepository.findTopDoctors();

        List<DoctopTopResponse> responses = new ArrayList<>();
        for(Object[] row : topDoctorsData){
            DoctopTopResponse response = new DoctopTopResponse(
                    (String) row[1],
                    (String) row[4],
                    (String) row[2],
                    (Double) row[3],
                    (Double) row[5]
            );

            responses.add(response);
        }

        return responses;
    }

    @Override
    public DoctorResponseDto createWithAvatar(DoctorRequestDto requestDto) throws IOException {

        String fileName = fileService.uploadFile(requestDto.file());
        Doctor doctor = Doctor.builder()
                .aboutText(requestDto.aboutText())
                .distance(requestDto.distance())
                .fullName(requestDto.fullName())
                .specialization(requestDto.specialization())
                .priceOfDoctor(requestDto.priceOfDoctor())
                .build();

        doctorRepository.save(doctor);
        DoctorResponseDto responseDto = DoctorResponseDto.builder()
                .avatarPath(doctor.getAvatarPath())
                .about(doctor.getAboutText())
                .fullName(doctor.getFullName())
                .distance(doctor.getDistance())
                .rating(doctor.getAverageRating())
                .avatarPath(fileName)
                .build();

        return responseDto;
    }

}
