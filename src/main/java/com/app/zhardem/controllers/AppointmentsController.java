package com.app.zhardem.controllers;

import com.app.zhardem.dto.appointments.AppointmentsResponseDto;
import com.app.zhardem.dto.appointments.ScheduledDto;
import com.app.zhardem.exceptions.entity.EntityNotFoundException;
import com.app.zhardem.models.Appointments;
import com.app.zhardem.services.AppointmentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/booking")
public class AppointmentsController {
    private final AppointmentsService appointmentsService;

    @GetMapping("/{doctorId}/{userId}/{appointmentId}")
    public String handleBooking(@PathVariable("doctorId") Long doctorId,
                                @PathVariable("userId") Long userId,
                                @PathVariable("appointmentId") Long appointmentId) {

        return appointmentsService.handleBooking(doctorId, userId, appointmentId);
    }
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

    @PostMapping("/bookOrUpdate")
    public ResponseEntity<String> bookOrUpdateAppointment(@RequestParam Long doctorId,
                                                          @RequestParam String dateTime,
                                                          @RequestParam Long userId) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy | hh:mm a");
            LocalDateTime appointmentDateTime = LocalDateTime.parse(dateTime, formatter);

            boolean result = appointmentsService.bookOrUpdateAppointment(doctorId, appointmentDateTime, userId);
            if (result) {
                return ResponseEntity.ok("Appointment booked/updated successfully.");
            } else {
                return ResponseEntity.ok("Failed to book/update appointment.");
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while booking/updating the appointment.");
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

    @GetMapping("/scheduled")
    public List<ScheduledDto> getScheduled(){
        return appointmentsService.getScheduled();
    }


}
