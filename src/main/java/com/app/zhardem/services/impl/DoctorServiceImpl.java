package com.app.zhardem.services.impl;

import com.app.zhardem.dto.category.CategoryRequestDto;
import com.app.zhardem.dto.doctor.DoctopTopResponse;
import com.app.zhardem.dto.doctor.DoctorLocationsDto;
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
import java.net.URL;
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
        String fileName = doctor.getAvatarPath();
        URL presignedUrl = fileService.generatePresignedUrl(fileName, 60);
        DoctorResponseDto responseDto = DoctorResponseDto.builder()
                .id(doctor.getId())
                .avatarPath(presignedUrl.toString())
                .about(doctor.getAboutText())
                .fullName(doctor.getFullName())
                .distance(doctor.getDistance())
                .rating(doctor.getAverageRating())
                .specialization(doctor.getSpecialization())
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
        if (requestDto.fullName() != null && !requestDto.fullName().isEmpty()) {
            doctor.setFullName(requestDto.fullName());
        }
        if (requestDto.aboutText() != null && !requestDto.aboutText().isEmpty()) {
            doctor.setAboutText(requestDto.aboutText());
        }
        if (requestDto.distance() != 0) {
            doctor.setDistance(requestDto.distance());
        }
        if (requestDto.specialization() != null && !requestDto.specialization().isEmpty()) {
            doctor.setSpecialization(requestDto.specialization());
        }
        if (requestDto.priceOfDoctor() != 0) {
            doctor.setPriceOfDoctor(requestDto.priceOfDoctor());
        }

        if (requestDto.file() != null) {
            try {
                String newFileName = fileService.uploadFile(requestDto.file());
                doctor.setAvatarPath(newFileName);
            } catch (IOException e) {
                log.error("Error uploading file for doctor with ID " + id, e);
            }
        }

        doctorRepository.save(doctor);

        URL presignedUrl = fileService.generatePresignedUrl(doctor.getAvatarPath(), 60);
        DoctorResponseDto responseDto = DoctorResponseDto.builder()
                .id(doctor.getId())
                .avatarPath(presignedUrl.toString())
                .about(doctor.getAboutText())
                .fullName(doctor.getFullName())
                .distance(doctor.getDistance())
                .specialization(doctor.getSpecialization())
                .rating(doctor.getAverageRating())
                .build();

        return responseDto;
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
            String fileName =  (String) row[4];
            URL presignedUrl = fileService.generatePresignedUrl(fileName, 60);
            DoctopTopResponse response = new DoctopTopResponse(
                    (Long) row[0],
                    (String) row[1],
                    presignedUrl.toString(),
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
                .avatarPath(fileName)
                .specialization(requestDto.specialization())
                .priceOfDoctor(requestDto.priceOfDoctor())
                .build();

        doctorRepository.save(doctor);
        log.info("File is " + fileName);
        DoctorResponseDto responseDto = DoctorResponseDto.builder()
                .id(doctor.getId())
                .avatarPath(doctor.getAvatarPath())
                .about(doctor.getAboutText())
                .fullName(doctor.getFullName())
                .distance(doctor.getDistance())
                .specialization(doctor.getSpecialization())
                .rating(doctor.getAverageRating())
                .avatarPath(fileName)
                .build();

        return responseDto;
    }

    @Override
    public List<DoctorResponseDto> getRecentDoctors() {
        List<Doctor> allDoctors = doctorRepository.findAll();
        List<DoctorResponseDto> getRecentDoctors = new ArrayList<>();
        for(Doctor doctor : allDoctors){
            String fileName =  doctor.getAvatarPath();
            URL presignedUrl = fileService.generatePresignedUrl(fileName, 60);
            DoctorResponseDto doctorResponseDto = DoctorResponseDto.builder()
                    .id(doctor.getId())
                    .avatarPath(doctor.getAvatarPath())
                    .rating(doctor.getAverageRating())
                    .fullName(doctor.getFullName())
                    .specialization(doctor.getSpecialization())
                    .avatarPath(presignedUrl.toString())
                    .build();
            log.info("Avatar path is " + doctor.getAvatarPath());
            getRecentDoctors.add(doctorResponseDto);
        }
        return getRecentDoctors;
    }

    @Override
    public List<DoctorLocationsDto> getLocationsDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();

        List<DoctorLocationsDto> locationsDtos = new ArrayList<>();

        for(Doctor doctor : doctors){
            String fileName = doctor.getAvatarPath();
            URL presignedUrl = fileService.generatePresignedUrl(fileName, 60);
            DoctorLocationsDto doctorLocationsDto = DoctorLocationsDto.builder()
                    .id(doctor.getId())
                    .location(doctor.getLocation())
                    .avatarPath(presignedUrl.toString())
                    .build();
            locationsDtos.add(doctorLocationsDto);
        }
        return locationsDtos;
    }

}
