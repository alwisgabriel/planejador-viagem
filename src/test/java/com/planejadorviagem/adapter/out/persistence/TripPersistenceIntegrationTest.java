package com.planejadorviagem.adapter.out.persistence;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class TripPersistenceIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @BeforeAll
    static void migrateDatabase() {
        Flyway.configure()
                .dataSource(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword())
                .load()
                .migrate();
    }

    @Test
    void shouldPersistTripInPostgres() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID tripId = UUID.randomUUID();

        try (Connection connection = DriverManager.getConnection(
                POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO users (id, email, password_hash) VALUES ("
                    + "'" + userId + "', 'integration@example.com', 'hash')");
            statement.executeUpdate("INSERT INTO trips "
                    + "(id, user_id, title, start_date, end_date, budget, status) VALUES ("
                    + "'" + tripId + "', '" + userId + "', 'Teste PostgreSQL', '2026-09-10', "
                    + "'" + LocalDate.of(2026, 9, 20) + "', " + new BigDecimal("5000.00") + ", 'PLANNED')");

            try (ResultSet resultSet = statement.executeQuery(
                    "SELECT title, budget FROM trips WHERE id = '" + tripId + "'")) {
                assertThat(resultSet.next()).isTrue();
                assertThat(resultSet.getString("title")).isEqualTo("Teste PostgreSQL");
                assertThat(resultSet.getBigDecimal("budget")).isEqualByComparingTo("5000.00");
            }
        }
    }
}
