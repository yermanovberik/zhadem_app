package com.app.zhardem.controllers;

import com.app.zhardem.dto.appointments.AppointmentsResponseDto;
import com.app.zhardem.models.Appointments;
import com.app.zhardem.services.AppointmentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/booking")
public class AppointmentsController {
    private final AppointmentsService appointmentsService;

    @GetMapping("/doctor/{doctorId}/availability")
    public ResponseEntity<List<AppointmentsResponseDto>> getDoctorAvailability(@PathVariable Long doctorId, @RequestParam int dayNumber) {
        List<AppointmentsResponseDto> availableTimes = appointmentsService.getDoctorAvailability(doctorId, dayNumber);
        return ResponseEntity.ok(availableTimes);
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookAppointment(@RequestParam Long doctorId, @RequestParam LocalDateTime dateTime, @RequestParam Long userId) {
        try {
            appointmentsService.bookAppointment(doctorId, dateTime, userId);
            return ResponseEntity.ok("Appointment booked successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelAppointment(@RequestParam Long appointmentId) {
        try {
            appointmentsService.cancelAppointment(appointmentId);
            return ResponseEntity.ok("Appointment canceled successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
