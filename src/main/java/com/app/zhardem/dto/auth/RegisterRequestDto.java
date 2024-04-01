package com.app.zhardem.dto.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;


@Builder
public record RegisterRequestDto(


        @NotBlank(message = "Last name cannot be blank")
        @Size(min = 2, max = 15, message = "Last name must be between 2 and 15 characters")
        @Pattern(
                regexp = "^[A-Z][a-z]+$",
                message = "Last name must start with an uppercase letter and can include only letters"
        )
        String fullName,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password

) { }
