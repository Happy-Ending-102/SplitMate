// File: OneTimeExpense.java
package com.splitmate.model;

import java.math.BigDecimal;
import java.util.Map;

public class OneTimeExpense extends Expense {
    @Override
    public Map<User, BigDecimal> calculateShares() {
        throw new UnsupportedOperationException();
    }
}

