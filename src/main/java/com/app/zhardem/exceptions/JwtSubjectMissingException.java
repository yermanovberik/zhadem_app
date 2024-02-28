package com.app.zhardem.exceptions;


import org.springframework.http.HttpStatus;

public class JwtSubjectMissingException extends ApiAuthenticationException {

    public JwtSubjectMissingException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
