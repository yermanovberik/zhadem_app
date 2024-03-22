package com.app.zhardem.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Chat {
    private Long id;

    private User user;

    private Doctor doctor;

    private String message;

    private LocalTime time;

    private LocalDate date;

    private String type;
}
