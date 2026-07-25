package com.planejadorviagem.application.port.in;

import com.planejadorviagem.domain.model.Trip;

public interface CreateTripUseCase {

    Trip create(CreateTripCommand command);
}
