package com.app.zhardem.controllers;

import com.app.zhardem.dto.APIResponse;
import com.app.zhardem.exceptions.FileDownloadException;
import com.app.zhardem.exceptions.FileUploadException;
import com.app.zhardem.exceptions.entity.EntityNotFoundException;
import com.app.zhardem.models.User;
import com.app.zhardem.repositories.UserRepository;
import com.app.zhardem.services.FileService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/file")

@Validated
public class FileUploadController {
    private final FileService fileService;
    private final UserRepository userRepository;


    public FileUploadController(FileService fileUploadService, UserRepository userRepository) {
        this.fileService = fileUploadService;
        this.userRepository = userRepository;
    }

    @PostMapping("/upload/{id}")
    @Transactional
    public ResponseEntity<?> uploadFile(@PathVariable("id") long id,@RequestParam("file") MultipartFile multipartFile) throws  FileUploadException, IOException {
        if (multipartFile.isEmpty()){
            throw new FileUploadException("File is empty. Cannot save an empty file");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with this id"+id+"not found "));

        boolean isValidFile = isValidFile(multipartFile);
        List<String> allowedFileExtensions = new ArrayList<>(Arrays.asList("pdf", "txt", "epub", "csv", "png", "jpg", "jpeg", "srt"));

        if (isValidFile && allowedFileExtensions.contains(FilenameUtils.getExtension(multipartFile.getOriginalFilename()))){
            String fileName = fileService.uploadFile(multipartFile);
            log.info(fileName);
            user.setAvatarPath(fileName);
            APIResponse apiResponse = APIResponse.builder()
                    .message("file uploaded successfully. File unique name =>" + fileName)
                    .isSuccessful(true)
                    .statusCode(200)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            APIResponse apiResponse = APIResponse.builder()
                    .message("Invalid File. File extension or File name is not supported")
                    .isSuccessful(false)
                    .statusCode(400)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/download/{id}")
    public ResponseEntity<?> getDownloadUrl(@PathVariable long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with this id "+ id +" not found"));
        String fileName = user.getAvatarPath();
        URL presignedUrl = fileService.generatePresignedUrl(fileName, 60);

        return ResponseEntity.ok().body(Map.of("url", presignedUrl.toString()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("fileName") @NotBlank @NotNull String fileName){
        boolean isDeleted = fileService.delete(fileName);
        if (isDeleted){
            APIResponse apiResponse = APIResponse.builder().message("file deleted!")
                    .statusCode(200).build();
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            APIResponse apiResponse = APIResponse.builder().message("file does not exist")
                    .statusCode(404).build();
            return new ResponseEntity<>("file does not exist", HttpStatus.NOT_FOUND);
        }
    }

    private boolean isValidFile(MultipartFile multipartFile){
        log.info("Empty Status ==> {}", multipartFile.isEmpty());
        if (Objects.isNull(multipartFile.getOriginalFilename())){
            return false;
        }
        return !multipartFile.getOriginalFilename().trim().equals("");
    }
}