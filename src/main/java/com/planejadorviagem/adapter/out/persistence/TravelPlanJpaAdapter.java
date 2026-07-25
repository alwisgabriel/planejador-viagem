package com.planejadorviagem.adapter.out.persistence;

import com.planejadorviagem.adapter.out.persistence.entity.TravelPlanEntity;
import com.planejadorviagem.adapter.out.persistence.repository.TravelPlanJpaRepository;
import com.planejadorviagem.application.port.out.TravelPlanRepository;
import com.planejadorviagem.domain.model.TravelPlan;

import java.util.Optional;
import java.util.UUID;

public final class TravelPlanJpaAdapter implements TravelPlanRepository {

    private final TravelPlanJpaRepository repository;

    public TravelPlanJpaAdapter(TravelPlanJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public TravelPlan save(TravelPlan plan) {
        TravelPlanEntity entity = new TravelPlanEntity(plan.getId(), plan.getTripId(), plan.getContent());
        repository.save(entity);
        return plan;
    }

    @Override
    public Optional<TravelPlan> findByTripId(UUID tripId) {
        return repository.findByTripId(tripId)
                .map(e -> TravelPlan.create(e.getTripId(), e.getContent()));
    }
}
