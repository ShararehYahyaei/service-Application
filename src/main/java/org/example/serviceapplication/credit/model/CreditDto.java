package org.example.serviceapplication.credit.model;

import org.springframework.format.FormatterRegistry;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record CreditDto(
         Integer userId,
         Integer balance
) {

}
