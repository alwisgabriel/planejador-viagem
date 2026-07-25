package com.planejadorviagem.adapter.out.persistence;

import com.planejadorviagem.adapter.out.persistence.entity.TripEntity;
import com.planejadorviagem.adapter.out.persistence.repository.TripJpaRepository;
import com.planejadorviagem.application.port.out.TripRepository;
import com.planejadorviagem.domain.model.Trip;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public final class TripJpaAdapter implements TripRepository {

    private final TripJpaRepository repository;

    public TripJpaAdapter(TripJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Trip> findById(UUID id) {
        return repository.findById(id)
                .map(e -> Trip.restore(e.getId(), e.getUserId(), e.getTitle(), e.getStartDate(), e.getEndDate(), e.getBudget(), Trip.Status.valueOf(e.getStatus())));
    }

    @Override
    public Trip save(Trip trip) {
        TripEntity entity = new TripEntity(
                trip.getId(), trip.getUserId(), trip.getTitle(),
                trip.getStartDate(), trip.getEndDate(), trip.getBudget(),
                trip.getStatus().name()
        );
        repository.save(entity);
        return trip;
    }

    @Override
    public List<Trip> findByUserId(UUID userId) {
        return repository.findByUserIdOrderByStartDateAsc(userId).stream()
                .map(e -> Trip.restore(e.getId(), e.getUserId(), e.getTitle(), e.getStartDate(), e.getEndDate(), e.getBudget(), Trip.Status.valueOf(e.getStatus())))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
