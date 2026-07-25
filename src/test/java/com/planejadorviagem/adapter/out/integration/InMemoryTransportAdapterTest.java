package com.planejadorviagem.adapter.out.integration;

import com.planejadorviagem.domain.model.TransportRecommendation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryTransportAdapterTest {

    private final InMemoryTransportAdapter adapter = new InMemoryTransportAdapter();

    @Test
    void shouldReturnTransportRecommendation() {
        TransportRecommendation rec = adapter.recommend("São Paulo", "Rio de Janeiro");

        assertThat(rec.origin()).isEqualTo("São Paulo");
        assertThat(rec.destination()).isEqualTo("Rio de Janeiro");
        assertThat(rec.modal()).isNotBlank();
        assertThat(rec.estimatedDuration()).isNotBlank();
    }
}
