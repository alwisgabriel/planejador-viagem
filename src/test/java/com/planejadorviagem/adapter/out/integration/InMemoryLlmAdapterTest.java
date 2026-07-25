package com.planejadorviagem.adapter.out.integration;

import com.planejadorviagem.domain.model.GeneratedPlan;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryLlmAdapterTest {

    private final InMemoryLlmAdapter adapter = new InMemoryLlmAdapter();

    @Test
    void shouldGeneratePlan() {
        GeneratedPlan plan = adapter.generate("Crie um roteiro para São Paulo de 5 dias");

        assertThat(plan.content()).isNotBlank();
        assertThat(plan.content()).contains("São Paulo");
    }
}
