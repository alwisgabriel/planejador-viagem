package com.planejadorviagem.adapter.out.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Component
public final class WikipediaAdapter {

    private static final Logger log = LoggerFactory.getLogger(WikipediaAdapter.class);

    private final HttpClient client;
    private final ObjectMapper mapper;

    public WikipediaAdapter() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        this.mapper = new ObjectMapper();
    }

    public String search(String query) {
        try {
            String url = "https://pt.wikipedia.org/api/rest_v1/page/summary/"
                    + URLEncoder.encode(query, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                var node = mapper.readTree(response.body());
                String extract = node.has("extract") ? node.get("extract").asText() : "";
                if (!extract.isEmpty()) {
                    return extract.length() > 1500 ? extract.substring(0, 1500) + "..." : extract;
                }
            }
            return "";
        } catch (Exception e) {
            log.warn("Wikipedia search failed for '{}': {}", query, e.getMessage());
            return "";
        }
    }
}
