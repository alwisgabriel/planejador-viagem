package com.planejadorviagem.adapter.out.persistence.repository;

import com.planejadorviagem.adapter.out.persistence.entity.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TripJpaRepository extends JpaRepository<TripEntity, UUID> {

    List<TripEntity> findByUserIdOrderByStartDateAsc(UUID userId);
}
