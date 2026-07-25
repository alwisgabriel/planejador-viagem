package com.planejadorviagem.application.service;

import com.planejadorviagem.application.port.in.CreateTripCommand;
import com.planejadorviagem.application.port.out.TripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CreateTripValidationTest {

    @Mock
    private TripRepository tripRepository;

    @Test
    void shouldRejectEndDateBeforeStartDate() {
        CreateTripService service = new CreateTripService(tripRepository);
        CreateTripCommand command = new CreateTripCommand(
                UUID.randomUUID(),
                "Férias inválidas",
                LocalDate.of(2026, 9, 20),
                LocalDate.of(2026, 9, 10),
                new BigDecimal("5000.00")
        );

        assertThatThrownBy(() -> service.create(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A data final deve ser igual ou posterior à data inicial");
    }

    @Test
    void shouldRejectNegativeBudget() {
        CreateTripService service = new CreateTripService(tripRepository);
        CreateTripCommand command = new CreateTripCommand(
                UUID.randomUUID(),
                "Orçamento inválido",
                LocalDate.of(2026, 9, 10),
                LocalDate.of(2026, 9, 20),
                new BigDecimal("-1.00")
        );

        assertThatThrownBy(() -> service.create(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O orçamento não pode ser negativo");
    }
}
