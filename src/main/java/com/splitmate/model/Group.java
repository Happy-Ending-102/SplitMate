// File: Group.java
package com.splitmate.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("groups")
public class Group extends BaseEntity {
    private String name;
    private String profileImageUrl;
    private Currency defaultCurrency;
    private ConversionPolicy conversionPolicy;

    @DBRef private List<User> members;
    @DBRef private List<User> frozenMembers;
    @DBRef private List<Expense> expenses;
    @DBRef private List<Notification> notifications;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public Currency getDefaultCurrency() { return defaultCurrency; }
    public void setDefaultCurrency(Currency defaultCurrency) { this.defaultCurrency = defaultCurrency; }

    public ConversionPolicy getConversionPolicy() { return conversionPolicy; }
    public void setConversionPolicy(ConversionPolicy conversionPolicy) { this.conversionPolicy = conversionPolicy; }

    public List<User> getMembers() { return members; }
    public void addMember(User u) { this.members.add(u); }

    public List<User> getFrozenMembers() { return frozenMembers; }
    public void setFrozenMembers(List<User> frozenMembers) { this.frozenMembers = frozenMembers; }

    public List<Expense> getExpenses() { return expenses; }
    public void addExpense(Expense e) { this.expenses.add(e); }

    public List<Notification> getNotifications() { return notifications; }
    public void addNotification(Notification n) { this.notifications.add(n); }
}
