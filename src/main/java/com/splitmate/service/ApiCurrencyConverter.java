// File: ApiCurrencyConverter.java
package com.splitmate.service;

import java.math.BigDecimal;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.splitmate.model.Currency;

@Service
public class ApiCurrencyConverter implements CurrencyConverter {
    @Value("${frankfurter.api.key:}") 
    private String apiKey;

    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
        try {
            // Build the URL (Frankfurter doesn’t actually need a key by default)
            String url = String.format(
                "https://api.frankfurter.app/latest?amount=%s&from=%s&to=%s",
                amount, from, to
            );
            System.out.println("URL: " + url);
            HttpRequest req = HttpRequest.newBuilder()
                                .uri(URI.create(url))
                                .GET()
                                .build();

            HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
            JsonNode tree = MAPPER.readTree(resp.body());
            return new BigDecimal(tree.get("rates").get(to.name()).asText());
        } catch (Exception e) {
            throw new RuntimeException("Currency conversion failed", e);
        }
    }
}
