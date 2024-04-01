package com.app.zhardem.services;

import com.app.zhardem.dto.doctor.DoctopTopResponse;
import com.app.zhardem.dto.doctor.DoctorRequestDto;
import com.app.zhardem.dto.doctor.DoctorResponseDto;
import com.app.zhardem.models.Doctor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public interface DoctorService extends CrudService<Doctor, DoctorRequestDto, DoctorResponseDto>{
         List<DoctopTopResponse> findTopDoctor();

         DoctorResponseDto createWithAvatar(DoctorRequestDto requestDto) throws IOException;

}
