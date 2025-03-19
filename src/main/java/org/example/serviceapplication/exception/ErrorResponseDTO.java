package org.example.serviceapplication.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponseDTO {
    private String message;
    private int statusCode;
}
