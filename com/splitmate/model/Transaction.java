package com.splitmate.model;

import java.util.Date;

public class Transaction extends AbstractModel {
    private double amount;
    private Date date;
    private User sender;
    private User receiver;
    
    public Transaction(double amount, Date date, User sender, User receiver) {
        this.amount = amount;
        this.date = date;
        this.sender = sender;
        this.receiver = receiver;
    }
    
    public void recordPayment() {
        // Logic to record this payment.
    }
    
    public void mergeDebts() {
        // Logic to merge debts between users.
    }
    
    // Getters
    public double getAmount() { return amount; }
    public Date getDate() { return date; }
    public User getSender() { return sender; }
    public User getReceiver() { return receiver; }
}
