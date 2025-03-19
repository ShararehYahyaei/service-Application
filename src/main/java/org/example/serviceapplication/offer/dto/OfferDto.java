package org.example.serviceapplication.offer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record OfferDto(
        Long offerId,
        Long userId,
        double offerPrice,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate offerDate,
        int estimationTime,
        Long customerRequestId


) {
}
