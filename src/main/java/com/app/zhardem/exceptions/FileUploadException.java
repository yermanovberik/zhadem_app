package com.app.zhardem.exceptions;

public class FileUploadException extends SpringBootFileUploadException{

    public FileUploadException(String message) {
        super(message);
    }
}