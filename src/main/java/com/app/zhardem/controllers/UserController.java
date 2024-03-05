package com.app.zhardem.controllers;

import com.app.zhardem.dto.user.UserAllInfo;
import com.app.zhardem.dto.user.UserResponseDto;
import com.app.zhardem.dto.user.UserUploadPhotoDto;
import com.app.zhardem.services.UserService;
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
}
