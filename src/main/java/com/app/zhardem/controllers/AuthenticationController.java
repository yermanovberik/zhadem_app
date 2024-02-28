package com.app.zhardem.controllers;

import com.app.zhardem.dto.auth.AuthenticationRequestDto;
import com.app.zhardem.dto.auth.AuthenticationResponseDto;
import com.app.zhardem.dto.auth.RegisterRequestDto;
import com.app.zhardem.services.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponseDto register(
            @RequestBody @Valid RegisterRequestDto request
    ) {
        return authenticationService.register(request);
    }

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponseDto authenticate(
            @RequestBody @Valid AuthenticationRequestDto request)
    {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/refresh-token")
    public AuthenticationResponseDto refreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION)
            @Pattern(regexp = "Bearer .*", message = "Authorization header must start with 'Bearer '")
            String authHeader
    ) {
        return authenticationService.refreshToken(authHeader);
    }



    /*
    access token bitkesin -> refresh tokenge get zapros,chtobi poluchit Refresh
     */


}
