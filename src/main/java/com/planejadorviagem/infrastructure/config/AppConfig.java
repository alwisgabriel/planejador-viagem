package com.planejadorviagem.infrastructure.config;

import com.planejadorviagem.adapter.out.integration.*;
import com.planejadorviagem.adapter.out.persistence.*;
import com.planejadorviagem.adapter.out.persistence.repository.*;
import com.planejadorviagem.application.port.out.*;
import com.planejadorviagem.application.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public UserRepository userRepository(UserJpaRepository repo) {
        return new UserJpaAdapter(repo);
    }

    @Bean
    public TripRepository tripRepository(TripJpaRepository repo) {
        return new TripJpaAdapter(repo);
    }

    @Bean
    public DestinationRepository destinationRepository(DestinationJpaRepository repo) {
        return new DestinationJpaAdapter(repo);
    }

    @Bean
    public CreateTripService createTripService(TripRepository tripRepository) {
        return new CreateTripService(tripRepository);
    }

    @Bean
    public GetTripsService getTripsService(TripRepository tripRepository) {
        return new GetTripsService(tripRepository);
    }

    @Bean
    public DeleteTripService deleteTripService(TripRepository tripRepository) {
        return new DeleteTripService(tripRepository);
    }

    @Bean
    public AddDestinationService addDestinationService(DestinationRepository destinationRepository) {
        return new AddDestinationService(destinationRepository);
    }

    @Bean
    public TravelPlanRepository travelPlanRepository(TravelPlanJpaRepository repo) {
        return new TravelPlanJpaAdapter(repo);
    }

    @Bean
    public WeatherPort weatherPort() {
        return new InMemoryWeatherAdapter();
    }

    @Bean
    public SecurityPort securityPort() {
        return new InMemorySecurityAdapter();
    }

    @Bean
    public TransportPort transportPort() {
        return new InMemoryTransportAdapter();
    }

    @Bean
    public LlmPort llmPort(GroqLlmAdapter adapter) {
        return adapter;
    }

    @Bean
    public GeneratePlanService generatePlanService(
            TripRepository tripRepository,
            DestinationRepository destinationRepository,
            TravelPlanRepository travelPlanRepository,
            WeatherPort weatherPort,
            SecurityPort securityPort,
            TransportPort transportPort,
            LlmPort llmPort,
            WikipediaAdapter wikipediaAdapter
    ) {
        return new GeneratePlanService(
                tripRepository, destinationRepository, travelPlanRepository,
                weatherPort, securityPort, transportPort, llmPort, wikipediaAdapter
        );
    }
}
