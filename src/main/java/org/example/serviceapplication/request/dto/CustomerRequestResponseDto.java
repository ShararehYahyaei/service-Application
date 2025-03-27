package org.example.serviceapplication.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.serviceapplication.request.model.RequestStatus;


import java.time.LocalDate;

public record CustomerRequestResponseDto(
        Long requestNumber,
        double price,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate deadLineTime,
        String address,
        RequestStatus requestStatus
) {
}

