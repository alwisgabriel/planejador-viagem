package com.planejadorviagem.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planejadorviagem.adapter.in.web.dto.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class PlanejadorViagemIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String authenticate(String suffix) throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test-%s@email.com\", \"password\": \"123456\"}".formatted(suffix)))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), AuthResponse.class).token();
    }

    @Test
    void shouldRegisterAndLogin() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "user@email.com", "password": "123456"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("user@email.com"))
                .andExpect(jsonPath("$.token").isNotEmpty());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "user@email.com", "password": "123456"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void shouldRejectDuplicateEmail() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "duplicate@email.com", "password": "123456"}
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "duplicate@email.com", "password": "123456"}
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldRejectInvalidLogin() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "unknown@email.com", "password": "123456"}
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldCreateAndListTrips() throws Exception {
        String token = authenticate("list");

        mockMvc.perform(post("/trips")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateTripRequest("Minha Viagem", LocalDate.of(2026, 10, 1),
                                        LocalDate.of(2026, 10, 10), new BigDecimal("3000.00")))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Minha Viagem"))
                .andExpect(jsonPath("$.id").isNotEmpty());

        mockMvc.perform(get("/trips")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(1)));
    }

    @Test
    void shouldCreateAndDeleteTrip() throws Exception {
        String token = authenticate("delete");

        MvcResult result = mockMvc.perform(post("/trips")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateTripRequest("Para Deletar", LocalDate.of(2026, 11, 1),
                                        LocalDate.of(2026, 11, 5), new BigDecimal("1000.00")))))
                .andExpect(status().isCreated())
                .andReturn();

        String tripId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(delete("/trips/" + tripId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldAddDestinationToTrip() throws Exception {
        String token = authenticate("dest");

        MvcResult result = mockMvc.perform(post("/trips")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateTripRequest("Viagem com Destinos", LocalDate.of(2026, 12, 1),
                                        LocalDate.of(2026, 12, 10), new BigDecimal("5000.00")))))
                .andExpect(status().isCreated())
                .andReturn();

        String tripId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(post("/trips/" + tripId + "/destinations")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new AddDestinationRequest("São Paulo", "Brasil", 1))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.city").value("São Paulo"))
                .andExpect(jsonPath("$.country").value("Brasil"))
                .andExpect(jsonPath("$.displayOrder").value(1));
    }
}
