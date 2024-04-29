package com.app.zhardem.services;

import com.app.zhardem.dto.auth.AuthenticationRequestDto;
import com.app.zhardem.dto.auth.AuthenticationResponseDto;
import com.app.zhardem.dto.auth.RegisterRequestDto;
import com.app.zhardem.enums.Role;
import com.app.zhardem.models.User;

public interface AuthenticationService {

    AuthenticationResponseDto register(RegisterRequestDto request);
    AuthenticationResponseDto authenticate(AuthenticationRequestDto request);
    AuthenticationResponseDto refreshToken(String authHeader);

   AuthenticationResponseDto registerOrUpdateUser(RegisterRequestDto requestDto);

}
