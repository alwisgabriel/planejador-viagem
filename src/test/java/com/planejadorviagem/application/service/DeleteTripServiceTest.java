package com.planejadorviagem.application.service;

import com.planejadorviagem.application.port.out.TripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteTripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private DeleteTripService deleteTripService;

    @Test
    void shouldDeleteTripById() {
        UUID tripId = UUID.randomUUID();

        deleteTripService.deleteById(tripId);

        verify(tripRepository).deleteById(tripId);
    }
}
