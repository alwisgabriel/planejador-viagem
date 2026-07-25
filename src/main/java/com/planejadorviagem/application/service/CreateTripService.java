package com.planejadorviagem.application.service;

import com.planejadorviagem.application.port.in.CreateTripCommand;
import com.planejadorviagem.application.port.in.CreateTripUseCase;
import com.planejadorviagem.application.port.out.TripRepository;
import com.planejadorviagem.domain.model.Trip;

public final class CreateTripService implements CreateTripUseCase {

    private final TripRepository tripRepository;

    public CreateTripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip create(CreateTripCommand command) {
        Trip trip = Trip.planned(
                command.userId(),
                command.title(),
                command.startDate(),
                command.endDate(),
                command.budget()
        );

        return tripRepository.save(trip);
    }
}
