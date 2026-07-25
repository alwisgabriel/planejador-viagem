package com.planejadorviagem.application.port.out;

import com.planejadorviagem.domain.model.Destination;

import java.util.List;
import java.util.UUID;

public interface DestinationQueryPort {

    List<Destination> findByTripId(UUID tripId);
}
