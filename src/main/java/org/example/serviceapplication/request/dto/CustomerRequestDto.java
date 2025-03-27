package org.example.serviceapplication.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record CustomerRequestDto(
        Long customerId,
        Long subServiceCategory,
        double price,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate deadLineTime,
        String address


) {

}