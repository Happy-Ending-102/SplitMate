// File: FriendshipService.java
package com.splitmate.service;

public interface FriendshipService {
    /**
     * Called by the recipient to accept a pending friend request.
     *
     * @param requesterId the ID of the user who sent the request
     * @param recipientId the ID of the user accepting it
     */
    void acceptFriendRequest(String requesterId, String recipientId);
    void sendFriendRequest(String requesterId, String recipientId);
}
