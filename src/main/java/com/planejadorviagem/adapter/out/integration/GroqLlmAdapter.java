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
public final class GroqLlmAdapter implements LlmPort {

    private static final Logger log = LoggerFactory.getLogger(GroqLlmAdapter.class);

    private final HttpClient client;
    private final String apiKey;
    private final String model;
    private final ObjectMapper mapper;

    public GroqLlmAdapter(
            @Value("${groq.api-key:}") String apiKey,
            @Value("${groq.model:llama-3.3-70b-versatile}") String model
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
                    Map.of("role", "system", "content", """
Você é um assistente especialista em planejamento de viagens. Sua missão é gerar roteiros personalizados, realistas e geograficamente precisos em português.

### REGRAS ABSOLUTAS DE PRECISÃO GEOGRÁFICA

1. VERACIDADE E FATOS (ZERO ALUCINAÇÃO):
   - Cite APENAS pontos turísticos, hotéis e restaurantes reais que você tenha ABSOLUTA CERTEZA de que existem na cidade solicitada.
   - Respeite a geografia real: Se a cidade for no interior (ex: cidade termal, de serra ou rural), NUNCA mencione praias, mar, portos ou restaurantes de praia.
   - SE VOCÊ NÃO TIVER CERTEZA do nome exato de um restaurante ou hotel específico na cidade, NÃO INVENTE UM NOME. Em vez disso, use descrições reais do perfil do local (ex: "Hotel com complexo de águas termais no bairro Termas", "Restaurante de comida típica colonial da região").

2. ESTRUTURA DO ROTEIRO:
   - Apresente a indicação de hospedagem e a estimativa de custos APENAS UMA VEZ, no final do documento (NUNCA coloque blocos de hospedagem dentro dos dias individuais).

3. BAGAGEM OBJETIVA:
   - Liste apenas itens concretos (ex: "guarda-chuva", "traje de banho", "documento de identidade"). Jamais use termos como "chuva".

### ESTRUTURA DA RESPOSTA (FORMATO MARKDOWN)

1. **Visão Geral da Viagem**
   - Destino, datas, orçamento e características reais do local (clima/geografia).

2. **Roteiro Dia a Dia**
   - Programação por horários coerente com a geografia real da cidade.

3. **Hospedagem Recomendada** (Apenas uma vez ao final)
   - Nome de hotel real confirmado OU recomendação da região/bairro hoteleiro ideal compatível com o orçamento.

4. **Estimativa de Custos Consolidados** (Apenas uma vez ao final)

5. **O que Levar na Bagagem**

6. **Dicas Práticas**
"""),
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
                    .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .timeout(Duration.ofSeconds(60))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error("Groq retornou {}: {}", response.statusCode(), response.body());
                return new GeneratedPlan("Erro ao gerar plano: API retornou HTTP " + response.statusCode());
            }

            var node = mapper.readTree(response.body());
            var content = node.get("choices").get(0).get("message").get("content").asText();
            return new GeneratedPlan(content);
        } catch (Exception e) {
            log.error("Erro ao chamar Groq: {}", e.getMessage());
            return new GeneratedPlan("Erro de conexão com a API. Verifique sua chave e conexão.");
        }
    }
}
