package com.planejadorviagem.application.service;

import com.planejadorviagem.application.port.in.GetTripsUseCase;
import com.planejadorviagem.application.port.out.TripRepository;
import com.planejadorviagem.domain.model.Trip;

import java.util.List;
import java.util.UUID;

public final class GetTripsService implements GetTripsUseCase {

    private final TripRepository tripRepository;

    public GetTripsService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public List<Trip> getTripsByUserId(UUID userId) {
        return tripRepository.findByUserId(userId);
    }
}
