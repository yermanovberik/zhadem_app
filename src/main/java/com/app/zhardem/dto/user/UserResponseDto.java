package com.app.zhardem.dto.user;

import com.app.zhardem.enums.Role;
import lombok.Builder;

@Builder
public record UserResponseDto(
        long id,
        String email,
        Role role
) { }
