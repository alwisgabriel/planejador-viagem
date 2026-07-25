package com.planejadorviagem.application.port.in;

import com.planejadorviagem.domain.model.Destination;

public interface AddDestinationUseCase {

    Destination add(AddDestinationCommand command);
}
