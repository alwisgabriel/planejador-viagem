package com.planejadorviagem.application.port.in;

import com.planejadorviagem.domain.model.TravelPlan;

import java.util.UUID;

public interface GeneratePlanUseCase {

    TravelPlan generate(UUID tripId);
}
