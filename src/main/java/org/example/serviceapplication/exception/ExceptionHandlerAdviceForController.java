package org.example.serviceapplication.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerAdviceForController {

        @ExceptionHandler(CustomApiException.class)
        public String handleCustomApiException(CustomApiException ex,Model model) {
            ErrorResponseDTO errorDTO = ErrorResponseDTO.builder()
                    .message(ex.getMessage())
                    .statusCode(ex.getType().getCode())
                    .build();
            model.addAttribute("error", errorDTO.getMessage());
            return "error";
        }


    @ExceptionHandler(ConstraintViolationException.class)
    public String handleValidationExceptions(ConstraintViolationException ex, Model model) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        model.addAttribute("errors", errors);
        return "error";
    }
}
