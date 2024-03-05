package com.app.zhardem.services;

import com.app.zhardem.dto.user.UserAllInfo;
import com.app.zhardem.dto.user.UserRequestDto;
import com.app.zhardem.dto.user.UserResponseDto;
import com.app.zhardem.models.User;

public interface UserService extends CrudService<User, UserRequestDto, UserResponseDto> {

    void throwExceptionIfUserExists(String email);
    UserAllInfo getAllInfo(long id);


}
