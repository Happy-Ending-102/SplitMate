// File: CurrencyConverter.java
package com.splitmate.service;

import java.math.BigDecimal;
import com.splitmate.model.Currency;

public interface CurrencyConverter {
    BigDecimal convert(BigDecimal amount, Currency from, Currency to);
}
