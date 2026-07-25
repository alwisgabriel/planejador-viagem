package com.planejadorviagem.adapter.out.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planejadorviagem.application.port.out.LlmPort;
import com.planejadorviagem.domain.model.GeneratedPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@Component
public final class OllamaLlmAdapter implements LlmPort {

    private static final Logger log = LoggerFactory.getLogger(OllamaLlmAdapter.class);

    private final HttpClient client;
    private final String apiUrl;
    private final String model;
    private final ObjectMapper mapper;

    public OllamaLlmAdapter(
            @Value("${llm.api-url:http://localhost:11434}") String apiUrl,
            @Value("${llm.model:qwen2.5-coder:3b}") String model
    ) {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.apiUrl = apiUrl;
        this.model = model;
        this.mapper = new ObjectMapper();
    }

    @Override
    public GeneratedPlan generate(String prompt) {
        try {
            String json = mapper.writeValueAsString(Map.of(
                    "model", model,
                    "prompt", prompt,
                    "stream", false,
                    "options", Map.of("num_predict", 512)
            ));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "/api/generate"))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(180))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error("Ollama retornou {}: {}", response.statusCode(), response.body());
                return new GeneratedPlan("Erro ao gerar plano: IA indisponível (HTTP " + response.statusCode() + ")");
            }

            var node = mapper.readTree(response.body());
            return new GeneratedPlan(node.get("response").asText());
        } catch (Exception e) {
            log.error("Erro ao chamar Ollama: {}", e.getMessage());
            return new GeneratedPlan("Erro de conexão com a IA local. Certifique-se de que o Ollama está rodando.");
        }
    }
}
