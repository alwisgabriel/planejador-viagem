package com.planejadorviagem.domain.model;

import java.time.Instant;
import java.util.UUID;

public final class TravelPlan {

    private final UUID id;
    private final UUID tripId;
    private final String content;
    private final Instant createdAt;

    private TravelPlan(UUID id, UUID tripId, String content, Instant createdAt) {
        this.id = id;
        this.tripId = tripId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static TravelPlan create(UUID tripId, String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("O plano não pode estar vazio");
        }
        return new TravelPlan(UUID.randomUUID(), tripId, content, Instant.now());
    }

    public UUID getId() { return id; }
    public UUID getTripId() { return tripId; }
    public String getContent() { return content; }
    public Instant getCreatedAt() { return createdAt; }
}
