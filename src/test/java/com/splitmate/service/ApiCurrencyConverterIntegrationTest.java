package com.splitmate.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.splitmate.model.Currency;

@SpringBootTest
class ApiCurrencyConverterIntegrationTest {

    @Autowired
    private ApiCurrencyConverter converter;

    /**
     * Hit the real API and assert that converting 100 USD → EUR
     * returns a non‐null, positive value within a plausible range.
     */
    @Test
    void convertUsdToEur() {
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal result = converter.convert(amount, Currency.USD, Currency.EUR);

        assertNotNull(result, "Converted result should not be null");
        assertTrue(result.compareTo(BigDecimal.ZERO) > 0, 
                   "Result should be positive");

        // You can tighten these bounds if you like:
        assertTrue(result.compareTo(BigDecimal.valueOf(50))   > 0 &&
                   result.compareTo(BigDecimal.valueOf(200)) < 0,
                   "Result should be within a realistic USD→EUR range");
    }
}
