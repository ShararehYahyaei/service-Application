package org.example.serviceapplication.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
