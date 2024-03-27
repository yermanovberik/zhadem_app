package com.app.zhardem.controllers;

import com.app.zhardem.dto.user.*;
import com.app.zhardem.services.UserService;
import com.app.zhardem.services.impl.PasswordResetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/password")
@Slf4j
public class UserController {
    private final UserService userService;
    private final PasswordResetService passwordResetService;
    @GetMapping("/success")
    public ResponseEntity<?> getUserData(Authentication authentication) {
        // Если Spring Security успешно аутентифицировал пользователя,
        // то вы можете получить данные пользователя из объекта Authentication
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        // Получаем необходимые данные пользователя
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        // ... другие данные
        log.info(email);
        log.info("Name " + name);

        // Создаем DTO или Map с данными пользователя для возврата в ответе
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", name);
        userInfo.put("email", email);


        // Возвращаем данные пользователя
        return ResponseEntity.ok(userInfo);
    }
    @GetMapping("/all-info/{id}")
    public UserAllInfo getAllInfo(@PathVariable long id){
        return userService.getAllInfo(id);
    }


    @PostMapping("/{id}/profile-photo")
    public ResponseEntity<UserUploadPhotoDto> uploadProfilePhoto(@PathVariable("id") long id,
                                                              @RequestParam("file") MultipartFile file) {
        UserUploadPhotoDto responseDto = userService.uploadProfilePhoto(id, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(
            @RequestBody @Valid UserRequestDto userRequestDto
    ) {
        return userService.create(userRequestDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUserById(
            @PathVariable @Positive(message = "Id must be positive") long id
    ) {
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @PathVariable @Positive(message = "Id must be positive") long id
    ) {
        userService.delete(id);
    }


    @PostMapping("/{id}/full-info")
    public ResponseEntity<UserAllInfo> uploadFullInfo(
            @PathVariable("id") long id,
            @RequestBody @Valid UserFullInfoDto request) {
        UserAllInfo responseDto = userService.uploadFullInfo(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/request-reset")
    public ResponseEntity<?> requestPasswordReset(@RequestParam String email) {
        passwordResetService.sendResetCode(email);
        return ResponseEntity.ok("Verification code sent.");
    }

    @PostMapping("/validate-code")
    public ResponseEntity<?> validateResetCode(@RequestParam String email, @RequestParam String code) {
        boolean isValid = passwordResetService.validateResetCode(email, code);
        return isValid ? ResponseEntity.ok("Code is valid.") : ResponseEntity.badRequest().body("Invalid code.");
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String code, @RequestParam String newPassword) {
        boolean isSuccess = passwordResetService.resetPassword(email, code, newPassword);
        return isSuccess ? ResponseEntity.ok("Password has been reset.") : ResponseEntity.badRequest().body("Password reset failed.");
    }

}
