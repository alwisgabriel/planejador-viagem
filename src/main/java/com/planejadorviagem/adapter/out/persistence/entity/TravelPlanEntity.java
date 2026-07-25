package com.planejadorviagem.adapter.out.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "travel_plans")
public class TravelPlanEntity {

    @Id
    private UUID id;

    @Column(name = "trip_id", nullable = false)
    private UUID tripId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public TravelPlanEntity() {}

    public TravelPlanEntity(UUID id, UUID tripId, String content) {
        this.id = id;
        this.tripId = tripId;
        this.content = content;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public UUID getTripId() { return tripId; }
    public String getContent() { return content; }
    public Instant getCreatedAt() { return createdAt; }
}
