package com.planejadorviagem.domain.model;

import java.util.UUID;

public final class Destination {

    private final UUID id;
    private final UUID tripId;
    private final String city;
    private final String country;
    private final int displayOrder;

    private Destination(UUID id, UUID tripId, String city, String country, int displayOrder) {
        this.id = id;
        this.tripId = tripId;
        this.city = city;
        this.country = country;
        this.displayOrder = displayOrder;
    }

    public static Destination create(UUID tripId, String city, String country, int displayOrder) {
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("A cidade não pode estar em branco");
        }
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("O país não pode estar em branco");
        }
        if (displayOrder <= 0) {
            throw new IllegalArgumentException("A ordem deve ser maior que zero");
        }

        return new Destination(UUID.randomUUID(), tripId, city.trim(), country.trim(), displayOrder);
    }

    public UUID getId() {
        return id;
    }

    public UUID getTripId() {
        return tripId;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}
