package org.example.serviceapplication.exception;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<ErrorResponseDTO> customApiException(CustomApiException ex) {
        ErrorResponseDTO errorDTO = ErrorResponseDTO.builder()
                .message(ex.getMessage())
                .statusCode(ex.getType().getCode())
                .build();

        return ResponseEntity.status(errorDTO.getStatusCode()).body(errorDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> genericException(Exception ex) {
        ErrorResponseDTO errorDTO = ErrorResponseDTO.builder()
                .message(ex.getMessage())
                .statusCode(500)
                .build();

        return ResponseEntity.internalServerError().body(errorDTO);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
