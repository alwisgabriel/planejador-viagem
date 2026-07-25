package com.planejadorviagem.adapter.out.integration;

import com.planejadorviagem.application.port.out.WeatherPort;
import com.planejadorviagem.domain.model.WeatherInfo;

import java.time.LocalDate;

public final class InMemoryWeatherAdapter implements WeatherPort {

    @Override
    public WeatherInfo getWeather(String city, LocalDate startDate, LocalDate endDate) {
        return new WeatherInfo(city, 25.0, "Ensolarado com possibilidade de chuva");
    }
}
