package com.app.zhardem.services;

import com.app.zhardem.dto.user.*;
import com.app.zhardem.enums.Role;
import com.app.zhardem.exceptions.entity.EntityNotFoundException;
import com.app.zhardem.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public interface UserService extends CrudService<User, UserRequestDto, UserResponseDto> {

    void throwExceptionIfUserExists(String email);
    UserAllInfo getAllInfo(long id);


   UserUploadPhotoDto uploadProfilePhoto(long id, MultipartFile file);

   UserAllInfo uploadFullInfo(long id,UserFullInfoDto responseDto);

}
