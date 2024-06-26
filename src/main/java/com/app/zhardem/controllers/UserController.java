package com.app.zhardem.controllers;

import com.app.zhardem.dto.PasswordTokenResponse;
import com.app.zhardem.dto.Response;
import com.app.zhardem.dto.user.*;
import com.app.zhardem.exceptions.entity.EntityNotFoundException;
import com.app.zhardem.services.UserService;
import com.app.zhardem.services.impl.PasswordResetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {
    private final UserService userService;
    private final PasswordResetService passwordResetService;

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
    public ResponseEntity<PasswordTokenResponse> requestPasswordReset(@RequestParam String email) {
        passwordResetService.sendResetCode(email);
        PasswordTokenResponse resetToken = passwordResetService.forgotYourPassword(email);
        return ResponseEntity.ok(resetToken);
    }

    @PostMapping("/validate-code")
    public ResponseEntity<Response> validateResetCode(@RequestHeader("token") String token) {
        boolean isValid = passwordResetService.validateResetToken(token);
        if (isValid) {
            Response response = Response.builder()
                    .response("Code is valid")
                    .build();
            return ResponseEntity.ok(response);
        } else {
            Response response = Response.builder()
                    .response("Invalid code.")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<Response> resetPassword(@RequestHeader("token") String token, @RequestParam String newPassword) {
        boolean isSuccess = passwordResetService.resetPassword(token, newPassword);
        log.info(newPassword.toString() + " password empty ?");
        if(newPassword.equals("")){
            throw new EntityNotFoundException("Empty password");
        }
        Response response;
        if (isSuccess) {
            response = Response.builder()
                    .response("Password has been reset.")
                    .build();
            return ResponseEntity.ok(response);
        } else {
            response = Response.builder()
                    .response("Password reset failed.")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

}
