package com.app.zhardem.dto.password;

import lombok.Builder;

@Builder
public record PasswordString(
        String email
) {
}
