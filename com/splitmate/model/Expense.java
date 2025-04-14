package com.splitmate.model;

import java.util.Date;
import java.util.Map;

public class Expense extends AbstractModel {
    private double amount;
    private String description;
    private String currency;
    private Date date;
    private User payer;
    private Map<User, Double> splitDetails;  // Each user mapped to share amount.
    
    public Expense(double amount, String description, String currency, Date date, User payer, Map<User, Double> splitDetails) {
        this.amount = amount;
        this.description = description;
        this.currency = currency;
        this.date = date;
        this.payer = payer;
        this.splitDetails = splitDetails;
    }
    
    public Map<User, Double> calculateSplit() {
        // In a real implementation, calculate splits based on fixed/percentage rules.
        return splitDetails;
    }
    
    public void attachReceipt(String filePath) {
        // Store or process receipt information.
    }
    
    // Getters
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public String getCurrency() { return currency; }
    public Date getDate() { return date; }
    public User getPayer() { return payer; }
    public Map<User, Double> getSplitDetails() { return splitDetails; }
}
