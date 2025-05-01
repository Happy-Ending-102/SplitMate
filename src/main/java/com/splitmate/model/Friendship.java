// File: Friendship.java
package com.splitmate.model;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("friendships")
public class Friendship extends BaseEntity {
    @DBRef private User userA;
    @DBRef private User userB;
    private LocalDate since;

    public User getUserA() { return userA; }
    public void setUserA(User userA) { this.userA = userA; }

    public User getUserB() { return userB; }
    public void setUserB(User userB) { this.userB = userB; }

    public LocalDate getSince() { return since; }
    public void setSince(LocalDate since) { this.since = since; }
}

