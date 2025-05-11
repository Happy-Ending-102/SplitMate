// File: Payment.java
package com.splitmate.model;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;


public abstract class Payment extends BaseEntity {
    @DBRef private User from;
    @DBRef private User to;
    private BigDecimal amount;
    
    
    // Getters and Setters
    public User getFrom() { return from; }
    public void setFrom(User from) { this.from = from; }

    public User getTo() { return to; }
    public void setTo(User to) { this.to = to; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }


}