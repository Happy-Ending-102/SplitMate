// File: User.java
package com.splitmate.model;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
public class User extends BaseEntity {
    private String name;
    private String email;
    private String passwordHash;
    private String passwordSalt;
    private String profileImageUrl;
    private List<Balance> balances;
    private Currency baseCurrency;

    @DBRef private List<Friendship> friends;
    @DBRef private List<Group> groups;
    @DBRef private List<Notification> notifications;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getPasswordSalt() { return passwordSalt; }
    public void setPasswordSalt(String passwordSalt) { this.passwordSalt = passwordSalt; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public Currency getBaseCurrency() { return baseCurrency; }
    public void setBaseCurrency(Currency baseCurrency) { this.baseCurrency = baseCurrency; }

    public List<Friendship> getFriends() { return friends; }
    public void addFriend(Friendship f) { this.friends.add(f); }

    public List<Group> getGroups() { return groups; }
    public void joinGroup(Group g) { this.groups.add(g); }

    public List<Notification> getNotifications() { return notifications; }
    public void addNotification(Notification n) { this.notifications.add(n); }

    public List<Balance> getBalances() { return balances;}
    public void setBalances(List<Balance> balances) { this.balances = balances;}

    


}
