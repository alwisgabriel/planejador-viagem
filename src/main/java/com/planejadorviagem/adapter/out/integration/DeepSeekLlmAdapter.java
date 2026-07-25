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
import java.util.List;
import java.util.Map;

@Component
public final class DeepSeekLlmAdapter implements LlmPort {

    private static final Logger log = LoggerFactory.getLogger(DeepSeekLlmAdapter.class);

    private final HttpClient client;
    private final String apiKey;
    private final String model;
    private final ObjectMapper mapper;

    public DeepSeekLlmAdapter(
            @Value("${deepseek.api-key:}") String apiKey,
            @Value("${deepseek.model:deepseek-chat}") String model
    ) {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.apiKey = apiKey;
        this.model = model;
        this.mapper = new ObjectMapper();
    }

    @Override
    public GeneratedPlan generate(String prompt) {
        try {
            var messages = List.of(
                    Map.of("role", "system", "content", "Você é um especialista em planejamento de viagens. Gere roteiros detalhados e práticos."),
                    Map.of("role", "user", "content", prompt)
            );

            var body = Map.of(
                    "model", model,
                    "messages", messages,
                    "max_tokens", 2048,
                    "temperature", 0.7
            );

            String json = mapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.deepseek.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .timeout(Duration.ofSeconds(60))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error("DeepSeek retornou {}: {}", response.statusCode(), response.body());
                return new GeneratedPlan("Erro ao gerar plano: API retornou HTTP " + response.statusCode());
            }

            var node = mapper.readTree(response.body());
            var content = node.get("choices").get(0).get("message").get("content").asText();
            return new GeneratedPlan(content);
        } catch (Exception e) {
            log.error("Erro ao chamar DeepSeek: {}", e.getMessage());
            return new GeneratedPlan("Erro de conexão com a API. Verifique sua chave e conexão com internet.");
        }
    }
}
