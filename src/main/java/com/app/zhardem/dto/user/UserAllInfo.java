package com.app.zhardem.dto.user;


import lombok.Builder;
import java.util.Date;

@Builder
public record UserAllInfo(

        String fullName,
        String IIN,

        Date birthDate,

        String sex,

         String region,

        String city,

       String address


) {
}
