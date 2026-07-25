package com.planejadorviagem.adapter.in.web.dto;

import com.planejadorviagem.domain.model.TravelPlan;

import java.time.Instant;
import java.util.UUID;

public record PlanResponse(
        UUID id,
        UUID tripId,
        String content,
        Instant createdAt
) {
    public static PlanResponse from(TravelPlan plan) {
        return new PlanResponse(plan.getId(), plan.getTripId(), plan.getContent(), plan.getCreatedAt());
    }
}
