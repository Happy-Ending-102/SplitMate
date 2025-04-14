package com.splitmate.model;

import java.util.Date;

/**
 * A concrete notification for friend requests.
 */
public class FriendRequestNotification extends AbstractNotification {
    public FriendRequestNotification(String message, Date date) {
        super(message, date);
    }
    
    @Override
    public void sendNotification() {
        // Implement your notification logic; here we simply print out the message.
        System.out.println("Friend Request: " + message);
    }
}
