package com.planejadorviagem.domain.model;

public record WeatherInfo(
        String city,
        double averageTempC,
        String conditions
) {
}
