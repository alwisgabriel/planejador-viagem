package com.planejadorviagem.adapter.in.web;

import com.planejadorviagem.adapter.in.web.dto.*;
import com.planejadorviagem.application.port.in.AddDestinationCommand;
import com.planejadorviagem.application.port.in.CreateTripCommand;
import com.planejadorviagem.application.service.AddDestinationService;
import com.planejadorviagem.application.service.CreateTripService;
import com.planejadorviagem.application.service.DeleteTripService;
import com.planejadorviagem.application.service.GetTripsService;
import com.planejadorviagem.domain.model.Destination;
import com.planejadorviagem.domain.model.Trip;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    private static final UUID DEFAULT_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private final CreateTripService createTripService;
    private final GetTripsService getTripsService;
    private final DeleteTripService deleteTripService;
    private final AddDestinationService addDestinationService;

    public TripController(CreateTripService createTripService, GetTripsService getTripsService,
                          DeleteTripService deleteTripService, AddDestinationService addDestinationService) {
        this.createTripService = createTripService;
        this.getTripsService = getTripsService;
        this.deleteTripService = deleteTripService;
        this.addDestinationService = addDestinationService;
    }

    @GetMapping
    public List<TripResponse> listTrips() {
        return getTripsService.getTripsByUserId(DEFAULT_USER_ID).stream()
                .map(TripResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<TripResponse> createTrip(@Valid @RequestBody CreateTripRequest request) {
        CreateTripCommand command = new CreateTripCommand(
                DEFAULT_USER_ID, request.title(), request.startDate(), request.endDate(), request.budget()
        );
        Trip trip = createTripService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(TripResponse.from(trip));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable UUID id) {
        deleteTripService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{tripId}/destinations")
    public ResponseEntity<DestinationResponse> addDestination(
            @PathVariable UUID tripId,
            @Valid @RequestBody AddDestinationRequest request) {
        AddDestinationCommand command = new AddDestinationCommand(
                tripId, request.city(), request.country(), request.displayOrder()
        );
        Destination destination = addDestinationService.add(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(DestinationResponse.from(destination));
    }
}
