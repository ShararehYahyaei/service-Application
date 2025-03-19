package org.example.serviceapplication.offer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.serviceapplication.user.model.User;

import java.time.LocalDate;

public record OfferUpdateDto(
        double offerPrice,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate offerDate,
        int estimationTime

) {
}
