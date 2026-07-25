package com.planejadorviagem.application.service;

import com.planejadorviagem.application.port.in.DeleteTripUseCase;
import com.planejadorviagem.application.port.out.TripRepository;

import java.util.UUID;

public final class DeleteTripService implements DeleteTripUseCase {

    private final TripRepository tripRepository;

    public DeleteTripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public void deleteById(UUID id) {
        tripRepository.deleteById(id);
    }
}
