package com.AD.Workflow.config;

import com.AD.Workflow.dto.ErrorResponseDTO;
import com.AD.Workflow.exception.WorkflowNotFoundException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

//@RestControllerAdvice
public class WorkflowAppExceptionHandler {

    @ExceptionHandler(WorkflowNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleWorkflowNotFoundException(WorkflowNotFoundException ex) {
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
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


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        HttpStatus httpStatus = HttpStatus.resolve(400);
        String errorMessage = httpStatus.getReasonPhrase();
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                errorMessage,
                ex.getMessage(),
                4090
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityException(DataIntegrityViolationException ex) {
        HttpStatus httpStatus = HttpStatus.resolve(409);
        String errorMessage = httpStatus.getReasonPhrase();
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                errorMessage,
                ex.getMessage(),
                409
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonParseError(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();

        if (ex.getCause() instanceof UnrecognizedPropertyException unrecognized) {
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("badFields", unrecognized.getPropertyName());
//            errorBody.put("knownFields", unrecognized.getKnownPropertyIds());

            return ResponseEntity.badRequest().body(errorBody);
        }

        Map<String, Object> fallback = Map.of("error", "Malformed JSON request");
        return ResponseEntity.badRequest().body(fallback);
    }



//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    public ResponseEntity<Map<String, String>> handleMissingParam(MissingServletRequestParameterException ex) {
//        String name = ex.getParameterName();
//        String msg = "Required request parameter '" + name + "' is not present";
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", msg));
//    }

}
