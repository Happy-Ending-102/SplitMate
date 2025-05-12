// File: Notification.java
package com.splitmate.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("notifications")
public class Notification extends BaseEntity {
    @DBRef private User user; // Assuming a notification is related to a user
    @DBRef private Group group; // Assuming a notification can be related to a group
    @DBRef private User friendUser; // Assuming a notification can be a friend request from another user
    @DBRef private Transaction transaction; // Assuming a notification can be related to a transaction
    private NotificationType type;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public User getFriendUser() { return friendUser; }
    public void setFriendUser(User friendUser) { this.friendUser = friendUser; }

    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }
}
