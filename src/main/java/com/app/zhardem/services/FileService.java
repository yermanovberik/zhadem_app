package com.app.zhardem.services;

import com.app.zhardem.exceptions.FileDownloadException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

public interface FileService {
    String uploadFile(MultipartFile multipartFile) throws FileUploadException, IOException;

    Object downloadFile(String fileName) throws FileDownloadException, IOException;

    boolean delete(String fileName);

     URL generatePresignedUrl(String objectKey, int expirationInMinutes);

}