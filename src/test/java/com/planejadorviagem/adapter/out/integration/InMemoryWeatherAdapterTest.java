package com.planejadorviagem.adapter.out.integration;

import com.planejadorviagem.domain.model.WeatherInfo;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryWeatherAdapterTest {

    private final InMemoryWeatherAdapter adapter = new InMemoryWeatherAdapter();

    @Test
    void shouldReturnWeatherInfoForCityAndPeriod() {
        WeatherInfo info = adapter.getWeather("São Paulo", LocalDate.of(2026, 9, 10), LocalDate.of(2026, 9, 20));

        assertThat(info.city()).isEqualTo("São Paulo");
        assertThat(info.averageTempC()).isBetween(15.0, 35.0);
        assertThat(info.conditions()).isNotBlank();
    }
}
