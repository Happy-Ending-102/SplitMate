// File: RecurringExpense.java
package com.splitmate.model;

import java.math.BigDecimal;
import java.util.Map;

public class RecurringExpense extends Expense {
    private Frequency frequency;

    public Frequency getFrequency() { return frequency; }
    public void setFrequency(Frequency frequency) { this.frequency = frequency; }

    @Override
    public Map<User, BigDecimal> calculateShares() {
        throw new UnsupportedOperationException();
    }
}
