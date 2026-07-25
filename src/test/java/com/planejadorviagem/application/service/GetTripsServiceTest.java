package com.planejadorviagem.application.service;

import com.planejadorviagem.application.port.out.TripRepository;
import com.planejadorviagem.domain.model.Trip;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetTripsServiceTest {

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private GetTripsService getTripsService;

    @Test
    void shouldReturnTripsForUser() {
        UUID userId = UUID.randomUUID();
        Trip trip1 = Trip.planned(userId, "Férias no Brasil", LocalDate.of(2026, 9, 10), LocalDate.of(2026, 9, 20), new BigDecimal("5000.00"));
        Trip trip2 = Trip.planned(userId, "Weekend no Uruguai", LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 7), new BigDecimal("2000.00"));

        when(tripRepository.findByUserId(userId)).thenReturn(List.of(trip1, trip2));

        List<Trip> trips = getTripsService.getTripsByUserId(userId);

        assertThat(trips).hasSize(2);
        assertThat(trips).extracting(Trip::getTitle).containsExactly("Férias no Brasil", "Weekend no Uruguai");
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoTrips() {
        UUID userId = UUID.randomUUID();

        when(tripRepository.findByUserId(userId)).thenReturn(List.of());

        List<Trip> trips = getTripsService.getTripsByUserId(userId);

        assertThat(trips).isEmpty();
    }
}
