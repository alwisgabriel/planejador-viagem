package com.planejadorviagem.adapter.out.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "destinations")
public class DestinationEntity {

    @Id
    private UUID id;

    @Column(name = "trip_id", nullable = false)
    private UUID tripId;

    @Column(nullable = false, length = 120)
    private String city;

    @Column(nullable = false, length = 120)
    private String country;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public DestinationEntity() {}

    public DestinationEntity(UUID id, UUID tripId, String city, String country, int displayOrder) {
        this.id = id;
        this.tripId = tripId;
        this.city = city;
        this.country = country;
        this.displayOrder = displayOrder;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public UUID getTripId() { return tripId; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public int getDisplayOrder() { return displayOrder; }
}
