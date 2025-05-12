package com.splitmate.model;

import java.math.BigDecimal;

public class Balance {
    private BigDecimal amount;
    private Currency currency;

    
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public Currency getCurrency() {
        return currency;
    }
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void addAmount(BigDecimal amount) {
        this.amount = this.amount.add(amount);
    }

    public void subtractAmount(BigDecimal amount) {
        this.amount = this.amount.subtract(amount);
    }

    
}
