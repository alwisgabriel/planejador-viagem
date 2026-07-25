package com.planejadorviagem.adapter.out.integration;

import com.planejadorviagem.application.port.out.TransportPort;
import com.planejadorviagem.domain.model.TransportRecommendation;

public final class InMemoryTransportAdapter implements TransportPort {

    @Override
    public TransportRecommendation recommend(String origin, String destination) {
        return new TransportRecommendation(
                origin, destination, "Avião", "1h30min",
                "Voo direto recomendado. Alternativa de ônibus: 6h."
        );
    }
}
