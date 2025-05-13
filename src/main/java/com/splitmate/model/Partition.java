package com.splitmate.model;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.TypeAlias;

@Document("partitions")
public class Partition extends BaseEntity{
    private User user;
    private double amount;
    private double percentage;
    
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    // only amount or percentage is needed. we only need one of them
    public double getPercentage() {
        return percentage;
    }
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
    public Partition(User user, double amount, double percentage, BigDecimal totalAmount) {
        this.user = user;
        // use precision to compare to zero
        if(amount<0){
            this.percentage = percentage;
            this.amount = totalAmount.doubleValue() * percentage / 100;
        }
        else if(percentage<0){
            this.amount = amount;
            this.percentage = (amount / totalAmount.doubleValue()) * 100;
        }
    }
    public Partition() {
        // Default constructor
    }
    
}
