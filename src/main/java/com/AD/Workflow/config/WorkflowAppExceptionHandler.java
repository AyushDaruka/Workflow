package com.AD.Workflow.config;

import com.AD.Workflow.dto.ErrorResponseDTO;
import com.AD.Workflow.exception.WorkflowNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class WorkflowAppExceptionHandler {

    @ExceptionHandler(WorkflowNotFoundException.class)
    public ResponseEntity<String> handleWorkflowNotFoundException(WorkflowNotFoundException ex) {
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(ResponseStatusException ex) {
        HttpStatusCode statusCode = ex.getStatusCode();
        HttpStatus httpStatus = HttpStatus.resolve(statusCode.value());
        String errorMessage = httpStatus.getReasonPhrase();
        int internalErrorCode = 9999; // A generic error code for unexpected errors
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
//                httpStatus.value(),
                errorMessage,
                ex.getReason(),
                internalErrorCode
        );

        return ResponseEntity.status(httpStatus).body(errorResponse);

//        return new ResponseEntity<>(errorResponse, httpStatus);
    }

}
