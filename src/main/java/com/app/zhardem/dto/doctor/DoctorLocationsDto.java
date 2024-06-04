package com.app.zhardem.dto.doctor;


import lombok.Builder;

@Builder
public record DoctorLocationsDto(
      Long id,

       String avatarPath,

     String location
) {
}
