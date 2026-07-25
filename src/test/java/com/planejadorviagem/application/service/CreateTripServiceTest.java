package com.planejadorviagem.application.service;

import com.planejadorviagem.application.port.out.TripRepository;
import com.planejadorviagem.application.port.in.CreateTripCommand;
import com.planejadorviagem.domain.model.Trip;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private CreateTripService createTripService;

    @Test
    void shouldCreateTripForUser() {
        UUID userId = UUID.randomUUID();
        CreateTripCommand command = new CreateTripCommand(
                userId,
                "Férias no Brasil",
                LocalDate.of(2026, 9, 10),
                LocalDate.of(2026, 9, 20),
                new BigDecimal("5000.00")
        );

        when(tripRepository.save(org.mockito.ArgumentMatchers.any(Trip.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Trip createdTrip = createTripService.create(command);

        ArgumentCaptor<Trip> tripCaptor = ArgumentCaptor.forClass(Trip.class);
        verify(tripRepository).save(tripCaptor.capture());

        Trip persistedTrip = tripCaptor.getValue();
        assertThat(createdTrip).isSameAs(persistedTrip);
        assertThat(persistedTrip.getId()).isNotNull();
        assertThat(persistedTrip.getUserId()).isEqualTo(userId);
        assertThat(persistedTrip.getTitle()).isEqualTo("Férias no Brasil");
        assertThat(persistedTrip.getStartDate()).isEqualTo(command.startDate());
        assertThat(persistedTrip.getEndDate()).isEqualTo(command.endDate());
        assertThat(persistedTrip.getBudget()).isEqualByComparingTo("5000.00");
        assertThat(persistedTrip.getStatus()).isEqualTo(Trip.Status.PLANNED);
    }
}
