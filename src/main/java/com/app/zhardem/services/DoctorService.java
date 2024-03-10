package com.app.zhardem.services;

import com.app.zhardem.dto.doctor.DoctorRequestDto;
import com.app.zhardem.dto.doctor.DoctorResponseDto;
import com.app.zhardem.models.Doctor;


public interface DoctorService extends CrudService<Doctor, DoctorRequestDto, DoctorResponseDto>{
}
