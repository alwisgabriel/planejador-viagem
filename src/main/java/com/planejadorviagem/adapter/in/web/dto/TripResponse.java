package com.planejadorviagem.adapter.in.web.dto;

import com.planejadorviagem.domain.model.Trip;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TripResponse(
        UUID id,
        UUID userId,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal budget,
        String status
) {
    public static TripResponse from(Trip trip) {
        return new TripResponse(
                trip.getId(), trip.getUserId(), trip.getTitle(),
                trip.getStartDate(), trip.getEndDate(), trip.getBudget(),
                trip.getStatus().name()
        );
    }
}
