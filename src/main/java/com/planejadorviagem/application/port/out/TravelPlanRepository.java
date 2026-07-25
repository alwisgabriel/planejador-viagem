package com.planejadorviagem.application.port.out;

import com.planejadorviagem.domain.model.TravelPlan;

import java.util.Optional;
import java.util.UUID;

public interface TravelPlanRepository {

    TravelPlan save(TravelPlan plan);

    Optional<TravelPlan> findByTripId(UUID tripId);
}
