package com.app.zhardem.controllers;

import com.app.zhardem.dto.user.UserAllInfo;
import com.app.zhardem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
