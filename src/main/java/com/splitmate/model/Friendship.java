// File: Friendship.java
package com.splitmate.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("friendships")
public class Friendship extends BaseEntity {
    @DBRef private User userA;
    @DBRef private User userB;
    @DBRef private List<Transaction> history;

    public User getUserA() { return userA; }
    public void setUserA(User userA) { this.userA = userA; }

    public User getUserB() { return userB; }
    public void setUserB(User userB) { this.userB = userB; }

    public List<Transaction> getHistory() { return history;}
    public void setHistory(List<Transaction> history) {this.history = history;}

    public User getOtherUser(User me) {
        if (me == null) return null;
        if (me.getId().equals(userA.getId())) return userB;
        if (me.getId().equals(userB.getId())) return userA;
        return null;
    }

}

