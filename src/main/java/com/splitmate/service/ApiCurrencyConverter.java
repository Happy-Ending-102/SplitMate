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
            // check for zero amount. use precision of 2 decimal places
            if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            }

            // Check for negativity and work with the absolute value
            boolean negative = amount.signum() < 0;
            BigDecimal absAmount = amount.abs();

            // Build URL with plain‐string format to avoid scientific notation
            String url = String.format(
                "https://api.frankfurter.app/latest?amount=%s&from=%s&to=%s",
                absAmount.toPlainString(), from.name(), to.name()
            );
            System.out.println("URL: " + url);

            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

            HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());

            // Simple status check
            if (resp.statusCode() != 200) {
                throw new RuntimeException("Currency API returned status " + resp.statusCode());
            }

            JsonNode tree = MAPPER.readTree(resp.body());
            BigDecimal converted = new BigDecimal(
                tree.path("rates").path(to.name()).asText()
            );

            // Reapply the original sign
            return negative ? converted.negate() : converted;
        } catch (Exception e) {
            throw new RuntimeException("Currency conversion failed", e);
        }
    }

}
