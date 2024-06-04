package com.app.zhardem.controllers;

import com.app.zhardem.dto.doctor.DoctopTopResponse;
import com.app.zhardem.dto.doctor.DoctorLocationsDto;
import com.app.zhardem.dto.doctor.DoctorRequestDto;
import com.app.zhardem.dto.doctor.DoctorResponseDto;
import com.app.zhardem.models.Doctor;
import com.app.zhardem.services.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DoctorResponseDto getDoctorById(@PathVariable long id) {
        return doctorService.getById(id);
    }

    @GetMapping("/getRecentDoctors")
    public List<DoctorResponseDto> getRecentDoctors(){
        return doctorService.getRecentDoctors();
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorResponseDto createDoctor(@RequestBody @Valid DoctorRequestDto requestDto) {
        return doctorService.create(requestDto);
    }

    @PostMapping(value = "/createWithAvatar")
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorResponseDto createDoctorWithAvatar(
            @RequestParam("fullName") String fullName,
            @RequestParam("distance") double distance,
            @RequestParam("specialization") String specialization,
            @RequestParam("aboutText") String aboutText,
            @RequestParam("category") String category,
            @RequestParam("priceOfDoctor") int priceOfDoctor,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        DoctorRequestDto doctorRequestDto = DoctorRequestDto.builder()
                .fullName(fullName)
                .priceOfDoctor(priceOfDoctor)
                .specialization(specialization)
                .distance(distance)
                .aboutText(aboutText)
                .specialization(category)
                .specialization(category)
                .file(file)
                .build();
        return doctorService.createWithAvatar(doctorRequestDto);
    }


    @PutMapping("/{id}")
    public DoctorResponseDto updateDoctor(
            @RequestParam("id") long id,
            @RequestParam("fullName") String fullName,
            @RequestParam("distance") double distance,
            @RequestParam("specialization") String specialization,
            @RequestParam("aboutText") String aboutText,
            @RequestParam("category") String category,
            @RequestParam("priceOfDoctor") int priceOfDoctor,
            @RequestParam("file") MultipartFile file
    ) {
        DoctorRequestDto doctorRequestDto = DoctorRequestDto.builder()
                .fullName(fullName)
                .priceOfDoctor(priceOfDoctor)
                .specialization(specialization)
                .distance(distance)
                .aboutText(aboutText)
                .specialization(category)
                .specialization(category)
                .file(file)
                .build();
        return doctorService.update(id, doctorRequestDto);
    }

    @GetMapping("/getLocations")
    @ResponseStatus(HttpStatus.OK)
    public List<DoctorLocationsDto> getLocationsDoctors() {
        return doctorService.getLocationsDoctors();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDoctor(@PathVariable long id) {
        doctorService.delete(id);
    }

    @GetMapping("/getTopDoctor")
    public List<DoctopTopResponse> getTopDoctor(){
        return doctorService.findTopDoctor();
    }
}
