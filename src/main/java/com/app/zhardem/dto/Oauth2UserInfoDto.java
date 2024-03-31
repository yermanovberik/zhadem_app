package com.app.zhardem.dto;

import lombok.Builder;

@Builder
public record Oauth2UserInfoDto(
        String name,
        String id,
        String email

) {
}
