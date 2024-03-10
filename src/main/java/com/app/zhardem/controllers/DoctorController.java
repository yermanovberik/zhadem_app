package com.app.zhardem.controllers;

import com.app.zhardem.dto.doctor.DoctorRequestDto;
import com.app.zhardem.dto.doctor.DoctorResponseDto;
import com.app.zhardem.dto.review.ReviewRequestDto;
import com.app.zhardem.dto.review.ReviewResponseDto;
import com.app.zhardem.services.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.simpleframework.xml.Path;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorResponseDto createDoctor(@RequestBody @Valid DoctorRequestDto requestDto) {
        return doctorService.create(requestDto);
    }

    @PutMapping("/{id}")
    public DoctorResponseDto updateDoctor(@PathVariable long id, @RequestBody @Valid DoctorRequestDto requestDto) {
        return doctorService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDoctor(@PathVariable long id) {
        doctorService.delete(id);
    }
}
