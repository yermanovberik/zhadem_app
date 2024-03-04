package com.app.zhardem.services;

import com.app.zhardem.dto.auth.AuthenticationRequestDto;
import com.app.zhardem.dto.auth.AuthenticationResponseDto;
import com.app.zhardem.dto.auth.RegisterRequestDto;

public interface AuthenticationService {

    AuthenticationResponseDto register(RegisterRequestDto request);
    AuthenticationResponseDto authenticate(AuthenticationRequestDto request);
    AuthenticationResponseDto refreshToken(String authHeader);

}
