package com.app.zhardem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public class ApiExceptionResponseDtoBase {
    protected final int errorCode;
    protected final HttpStatus httpStatus;
    protected final ZonedDateTime timestamp;

}
