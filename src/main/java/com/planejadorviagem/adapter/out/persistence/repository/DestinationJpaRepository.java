package com.planejadorviagem.adapter.out.persistence.repository;

import com.planejadorviagem.adapter.out.persistence.entity.DestinationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DestinationJpaRepository extends JpaRepository<DestinationEntity, UUID> {

    List<DestinationEntity> findByTripIdOrderByDisplayOrderAsc(UUID tripId);
}
