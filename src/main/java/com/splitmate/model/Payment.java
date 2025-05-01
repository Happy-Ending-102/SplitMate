// File: Payment.java
package com.splitmate.model;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("payments")
public class Payment extends BaseEntity {
    @DBRef private User from;
    @DBRef private User to;
    private BigDecimal amount;
    private Currency currency;
    private boolean isPaid;

    public User getFrom() { return from; }
    public void setFrom(User from) { this.from = from; }

    public User getTo() { return to; }
    public void setTo(User to) { this.to = to; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Currency getCurrency() { return currency; }
    public void setCurrency(Currency currency) { this.currency = currency; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean isPaid) { this.isPaid = isPaid; }
}
