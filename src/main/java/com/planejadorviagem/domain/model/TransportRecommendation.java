package com.planejadorviagem.domain.model;

public record TransportRecommendation(
        String origin,
        String destination,
        String modal,
        String estimatedDuration,
        String notes
) {
}
