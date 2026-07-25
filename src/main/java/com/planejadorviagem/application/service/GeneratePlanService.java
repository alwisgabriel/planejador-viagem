package com.planejadorviagem.application.service;

import com.planejadorviagem.adapter.out.integration.WikipediaAdapter;
import com.planejadorviagem.application.port.in.GeneratePlanUseCase;
import com.planejadorviagem.application.port.out.*;
import com.planejadorviagem.domain.model.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class GeneratePlanService implements GeneratePlanUseCase {

    private final TripRepository tripRepository;
    private final DestinationQueryPort destinationQuery;
    private final TravelPlanRepository travelPlanRepository;
    private final WeatherPort weatherPort;
    private final SecurityPort securityPort;
    private final TransportPort transportPort;
    private final LlmPort llmPort;
    private final WikipediaAdapter wikipediaAdapter;

    public GeneratePlanService(
            TripRepository tripRepository,
            DestinationQueryPort destinationQuery,
            TravelPlanRepository travelPlanRepository,
            WeatherPort weatherPort,
            SecurityPort securityPort,
            TransportPort transportPort,
            LlmPort llmPort,
            WikipediaAdapter wikipediaAdapter
    ) {
        this.tripRepository = tripRepository;
        this.destinationQuery = destinationQuery;
        this.travelPlanRepository = travelPlanRepository;
        this.weatherPort = weatherPort;
        this.securityPort = securityPort;
        this.transportPort = transportPort;
        this.llmPort = llmPort;
        this.wikipediaAdapter = wikipediaAdapter;
    }

    @Override
    public TravelPlan generate(UUID tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Viagem não encontrada"));

        List<Destination> destinations = destinationQuery.findByTripId(tripId);

        StringBuilder prompt = new StringBuilder();
        prompt.append("Crie um roteiro de viagem completo e detalhado.\n\n");

        if (!destinations.isEmpty()) {
            String destStr = destinations.stream()
                    .map(d -> d.getCity() + "/" + d.getCountry())
                    .collect(Collectors.joining(", "));
            prompt.append("Destinos: ").append(destStr).append("\n");

            Destination first = destinations.getFirst();
            String wikiInfo = wikipediaAdapter.search(first.getCity() + ", " + first.getCountry());
            if (!wikiInfo.isEmpty()) {
                prompt.append("\nInformações reais sobre o destino:\n").append(wikiInfo).append("\n\n");
            }
        }

        prompt.append("Período: ").append(trip.getStartDate()).append(" a ").append(trip.getEndDate()).append("\n");
        prompt.append("Orçamento: R$ ").append(trip.getBudget()).append("\n");

        if (!destinations.isEmpty()) {
            Destination first = destinations.getFirst();
            try {
                WeatherInfo weather = weatherPort.getWeather(first.getCity(), trip.getStartDate(), trip.getEndDate());
                prompt.append("Clima: ").append(weather.averageTempC()).append("°C, ").append(weather.conditions()).append("\n");

                SafetyInfo safety = securityPort.getSafety(first.getCity(), first.getCountry());
                prompt.append("Segurança: nível ").append(safety.level()).append(" - ").append(safety.description()).append("\n");

                if (destinations.size() > 1) {
                    Destination second = destinations.get(1);
                    TransportRecommendation transport = transportPort.recommend(first.getCity(), second.getCity());
                    prompt.append("Transporte sugerido: ").append(transport.modal())
                            .append(" (").append(transport.estimatedDuration()).append(")\n");
                }
            } catch (Exception e) {
                prompt.append("Dados climáticos e de segurança: indisponíveis no momento\n");
            }
        }

        GeneratedPlan generated = llmPort.generate(prompt.toString());

        TravelPlan plan = TravelPlan.create(tripId, generated.content());
        return travelPlanRepository.save(plan);
    }
}
