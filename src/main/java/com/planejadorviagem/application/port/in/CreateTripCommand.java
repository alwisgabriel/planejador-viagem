package com.planejadorviagem.application.port.in;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateTripCommand(
        UUID userId,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal budget
) {
}
