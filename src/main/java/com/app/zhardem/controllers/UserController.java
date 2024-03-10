package com.app.zhardem.controllers;

import com.app.zhardem.dto.user.*;
import com.app.zhardem.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

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

}
