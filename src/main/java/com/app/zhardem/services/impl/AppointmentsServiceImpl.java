package com.app.zhardem.services.impl;

import com.app.zhardem.dto.appointments.AppointmentsRequestDto;
import com.app.zhardem.dto.appointments.AppointmentsResponseDto;
import com.app.zhardem.models.Appointments;
import com.app.zhardem.services.AppointmentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentsServiceImpl implements AppointmentsService {
    @Override
    public AppointmentsResponseDto getById(long id) {
        return null;
    }

    @Override
    public AppointmentsResponseDto create(AppointmentsRequestDto requestDto) {
        return null;
    }

    @Override
    public AppointmentsResponseDto update(long id, AppointmentsRequestDto requestDto) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Appointments getEntityById(long id) {
        return null;
    }
}
