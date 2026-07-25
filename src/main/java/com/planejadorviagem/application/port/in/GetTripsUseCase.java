package com.planejadorviagem.application.port.in;

import com.planejadorviagem.domain.model.Trip;

import java.util.List;
import java.util.UUID;

public interface GetTripsUseCase {

    List<Trip> getTripsByUserId(UUID userId);
}
