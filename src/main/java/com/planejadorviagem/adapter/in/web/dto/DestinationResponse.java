package com.planejadorviagem.adapter.in.web.dto;

import com.planejadorviagem.domain.model.Destination;

import java.util.UUID;

public record DestinationResponse(
        UUID id,
        UUID tripId,
        String city,
        String country,
        int displayOrder
) {
    public static DestinationResponse from(Destination destination) {
        return new DestinationResponse(
                destination.getId(), destination.getTripId(),
                destination.getCity(), destination.getCountry(),
                destination.getDisplayOrder()
        );
    }
}
