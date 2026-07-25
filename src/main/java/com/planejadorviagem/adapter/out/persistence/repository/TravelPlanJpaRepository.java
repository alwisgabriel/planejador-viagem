package com.planejadorviagem.adapter.out.persistence.repository;

import com.planejadorviagem.adapter.out.persistence.entity.TravelPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TravelPlanJpaRepository extends JpaRepository<TravelPlanEntity, UUID> {

    Optional<TravelPlanEntity> findByTripId(UUID tripId);
}
