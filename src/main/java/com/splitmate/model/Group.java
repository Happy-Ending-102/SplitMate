// File: Group.java
package com.splitmate.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document("groups")
public class Group extends BaseEntity {
    private String name;
    private String profileImageUrl;
    private double budget;
    private Currency defaultCurrency;
    private ConversionPolicy conversionPolicy;
    private String avatarBase64;

    @DBRef private List<User> members;
    @DBRef private List<User> frozenMembers;
    @DBRef private List<Expense> expenses;

    public Group() {
        super();
        this.members = new ArrayList<>();
        this.frozenMembers = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }

    public String getAvatarBase64() { return avatarBase64; }
    public void setAvatarBase64(String avatarBase64) { this.avatarBase64 = avatarBase64; }

    public Currency getDefaultCurrency() { return defaultCurrency; }
    public void setDefaultCurrency(Currency defaultCurrency) { this.defaultCurrency = defaultCurrency; }

    public ConversionPolicy getConversionPolicy() { return conversionPolicy; }
    public void setConversionPolicy(ConversionPolicy conversionPolicy) { this.conversionPolicy = conversionPolicy; }

    public List<User> getMembers() { return members; }
    public void addMember(User u) { 
        this.members.add(u); 
        u.joinGroup(this);
    }

    public List<User> getFrozenMembers() { return frozenMembers; }
    public void setFrozenMembers(List<User> frozenMembers) { this.frozenMembers = frozenMembers; }

    public List<Expense> getExpenses() { return expenses; }
    public void addExpense(Expense e) { this.expenses.add(e); }

}
