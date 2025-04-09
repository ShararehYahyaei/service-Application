package org.example.serviceapplication.order.model;

import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record OrderDtoSearch(String subService,
                             String category,
                             String fromLocalDate,
                             String toLocalDate,
                             String orderStatus){}
