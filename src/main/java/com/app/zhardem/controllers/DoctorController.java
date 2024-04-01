package com.app.zhardem.controllers;

import com.app.zhardem.dto.doctor.DoctopTopResponse;
import com.app.zhardem.dto.doctor.DoctorRequestDto;
import com.app.zhardem.dto.doctor.DoctorResponseDto;
import com.app.zhardem.services.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @PostMapping("/createWithAvatar")
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorResponseDto createDoctorWithAvatar(@RequestBody @Valid DoctorRequestDto requestDto) throws IOException {
        return doctorService.createWithAvatar(requestDto);
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

    @GetMapping("/getTopDoctor")
    public List<DoctopTopResponse> getTopDoctor(){
        return doctorService.findTopDoctor();
    }
}
