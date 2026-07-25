package com.planejadorviagem.adapter.out.integration;

import com.planejadorviagem.domain.model.SafetyInfo;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemorySecurityAdapterTest {

    private final InMemorySecurityAdapter adapter = new InMemorySecurityAdapter();

    @Test
    void shouldReturnSafetyInfoForCity() {
        SafetyInfo info = adapter.getSafety("São Paulo", "Brasil");

        assertThat(info.city()).isEqualTo("São Paulo");
        assertThat(info.level()).isNotNull();
        assertThat(info.description()).isNotBlank();
    }
}
