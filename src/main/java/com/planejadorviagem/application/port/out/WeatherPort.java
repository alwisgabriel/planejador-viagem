package com.planejadorviagem.application.port.out;

import com.planejadorviagem.domain.model.WeatherInfo;

import java.time.LocalDate;

public interface WeatherPort {

    WeatherInfo getWeather(String city, LocalDate startDate, LocalDate endDate);
}
