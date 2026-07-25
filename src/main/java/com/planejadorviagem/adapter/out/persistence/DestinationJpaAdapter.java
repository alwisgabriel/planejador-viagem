package com.planejadorviagem.adapter.out.persistence;

import com.planejadorviagem.adapter.out.persistence.entity.DestinationEntity;
import com.planejadorviagem.adapter.out.persistence.repository.DestinationJpaRepository;
import com.planejadorviagem.application.port.out.DestinationRepository;
import com.planejadorviagem.domain.model.Destination;

import java.util.List;
import java.util.UUID;

public final class DestinationJpaAdapter implements DestinationRepository {

    private final DestinationJpaRepository repository;

    public DestinationJpaAdapter(DestinationJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Destination save(Destination destination) {
        DestinationEntity entity = new DestinationEntity(
                destination.getId(), destination.getTripId(),
                destination.getCity(), destination.getCountry(),
                destination.getDisplayOrder()
        );
        repository.save(entity);
        return destination;
    }

    @Override
    public List<Destination> findByTripId(UUID tripId) {
        return repository.findByTripIdOrderByDisplayOrderAsc(tripId).stream()
                .map(e -> Destination.create(e.getTripId(), e.getCity(), e.getCountry(), e.getDisplayOrder()))
                .toList();
    }
}
