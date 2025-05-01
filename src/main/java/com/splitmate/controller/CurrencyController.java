// CurrencyController.java
package com.splitmate.controller;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import com.splitmate.model.*;
import com.splitmate.service.*;

/**
 * Handles manual currency conversion requests from the UI.
 */
@Component
public class CurrencyController {
    private final CurrencyConverter converter;

    public CurrencyController(CurrencyConverter converter) {
        this.converter = converter;
    }

    public BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
        throw new UnsupportedOperationException();
    }
}