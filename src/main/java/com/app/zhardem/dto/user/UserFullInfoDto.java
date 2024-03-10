package com.app.zhardem.dto.user;

import jakarta.persistence.Column;

import java.util.Date;

public record UserFullInfoDto(

        String IIN,

        Date birthDate,

        String sex,

        String region,

        String city,
         String address
) {
}
