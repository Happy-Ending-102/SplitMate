package com.splitmate.model;

public class Partition {
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
    public Partition(User user, double amount, double percentage) {
        this.user = user;
        this.amount = amount;
        this.percentage = percentage;
    }
    public Partition() {
        // Default constructor
    }
    
}
