package com.app.zhardem.controllers;

import com.app.zhardem.dto.auth.AuthenticationRequestDto;
import com.app.zhardem.dto.auth.AuthenticationResponseDto;
import com.app.zhardem.dto.auth.RegisterRequestDto;
import com.app.zhardem.exceptions.entity.EntityNotFoundException;
import com.app.zhardem.jwt.JwtFactory;
import com.app.zhardem.models.User;
import com.app.zhardem.repositories.UserRepository;
import com.app.zhardem.services.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final JwtFactory jwtFactory;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponseDto register(
            @RequestBody @Valid RegisterRequestDto request
    ) {
        return authenticationService.register(request);
    }

    @GetMapping("/oauth")
    public AuthenticationResponseDto handleAuthRedirect(
            @RequestParam(name = "state") String state,
            @RequestParam(name = "code") String code,
            @RequestParam(name = "scope") String scope,
            @RequestParam(name = "authuser") int authuser,
            @RequestParam(name = "prompt") String prompt) {

        String clientId = "609756543286-sm69ucl3n8m36ri8motg0odu0q1dpinq.apps.googleusercontent.com";
        String clientSecret = "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";
        String redirectUri = "https://sea-lion-app-usoaj.ondigitalocean.app/api/v1/auth/oauth";
        String tokenEndpoint = "https://oauth2.googleapis.com/token";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("client_id", clientId);
        requestBody.put("client_secret", clientSecret);
        requestBody.put("code", code);
        requestBody.put("grant_type", "authorization_code");
        requestBody.put("redirect_uri", redirectUri);

        Map<String, Object> response = restTemplate.postForObject(tokenEndpoint, requestBody, Map.class);
        String accessToken = (String) response.get("access_token");

        String userInfoEndpoint = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken;
        Map<String, Object> userInfo = restTemplate.getForObject(userInfoEndpoint, Map.class);

        String email = (String) userInfo.get("email");

        RegisterRequestDto requestDto = RegisterRequestDto.builder()
                .email(email)
                .password(UUID.randomUUID().toString())
                .build();


        return authenticationService.registerOrUpdateUser(requestDto);
    }
    @GetMapping("/verify")
    @ResponseBody
    public ResponseEntity<AuthenticationResponseDto> verifyEmail(@RequestParam("code") String code) {
        User user = userRepository.findByVerificationCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Invalid verification token"));

        if (user.getCodeExpiration().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    AuthenticationResponseDto.builder()
                            .errorMessage("Token has expired")
                            .build()
            );
        }

        user.setVerificationCode(null);
        user.setCodeExpiration(null);
        userRepository.save(user);

        String accessToken = jwtFactory.generateAccessToken(user);
        String refreshToken = jwtFactory.generateRefreshToken(user);

        AuthenticationResponseDto authenticationResponseDto = AuthenticationResponseDto.builder()
                .userId(user.getId())
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();

        return ResponseEntity.ok(authenticationResponseDto);
    }


    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponseDto authenticate(
            @RequestBody @Valid AuthenticationRequestDto request)
    {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/refresh-token")
    public AuthenticationResponseDto refreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION)
            @Pattern(regexp = "Bearer .*", message = "Authorization header must start with 'Bearer '")
            String authHeader
    ) {
        return authenticationService.refreshToken(authHeader);
    }




}
