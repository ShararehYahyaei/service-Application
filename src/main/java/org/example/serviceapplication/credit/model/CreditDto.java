package org.example.serviceapplication.credit.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreditDto(
         Long userId,
         Double balance,
         LocalDateTime expirationDate
) {
}
