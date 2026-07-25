package com.planejadorviagem.application.service;

import com.planejadorviagem.application.port.in.AddDestinationCommand;
import com.planejadorviagem.application.port.in.AddDestinationUseCase;
import com.planejadorviagem.application.port.out.DestinationRepository;
import com.planejadorviagem.domain.model.Destination;

public final class AddDestinationService implements AddDestinationUseCase {

    private final DestinationRepository destinationRepository;

    public AddDestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    @Override
    public Destination add(AddDestinationCommand command) {
        Destination destination = Destination.create(
                command.tripId(),
                command.city(),
                command.country(),
                command.displayOrder()
        );

        return destinationRepository.save(destination);
    }
}
