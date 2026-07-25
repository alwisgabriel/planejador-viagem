package com.planejadorviagem.application.port.out;

import com.planejadorviagem.domain.model.Trip;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TripRepository {

    Trip save(Trip trip);

    Optional<Trip> findById(UUID id);

    List<Trip> findByUserId(UUID userId);

    void deleteById(UUID id);
}
