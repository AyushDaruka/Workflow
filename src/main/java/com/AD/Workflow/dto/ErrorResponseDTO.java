package com.AD.Workflow.dto;


import java.time.LocalDateTime;

public class ErrorResponseDTO {
    private LocalDateTime timestamp;
    //    private int status;
    private String error;
    private String message;
    private int errorCode;

    public ErrorResponseDTO(String error, String message, int errorCode) {
        this.timestamp = LocalDateTime.now();
//        this.status = status;
        this.error = error;
        this.message = message;
        this.errorCode = errorCode;
    }

    // Getters for all fields
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

//    public int getStatus() {
//        return status;
//    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }
}