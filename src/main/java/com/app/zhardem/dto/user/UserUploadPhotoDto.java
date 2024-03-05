package com.app.zhardem.dto.user;

import com.app.zhardem.enums.Role;
import lombok.Builder;

@Builder
public record UserUploadPhotoDto(
        long id,
        String photoUrl,
        Role role
) {
}
