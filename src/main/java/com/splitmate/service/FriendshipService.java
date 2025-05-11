// File: FriendshipService.java
package com.splitmate.service;

import java.util.List;

import com.splitmate.model.Friendship;
import com.splitmate.model.Payment;
import com.splitmate.model.User;

public interface FriendshipService {
    /**
     * Called by the recipient to accept a pending friend request.
     *
     * @param requesterId the ID of the user who sent the request
     * @param recipientId the ID of the user accepting it
     */
    void acceptFriendRequest(String requesterId, String recipientId);
    void sendFriendRequest(String requesterId, String recipientId);
    List<User> getFriends(String userId);
    List<Payment> sortByAmountAsc(Friendship friendship);
    List<Payment> sortByAmountDesc(Friendship friendship);
    List<Payment> sortByDateDesc(Friendship friendship);
    Friendship getFriendshipById(String friendshipId);
    Friendship getFriendshipBetween(String userAId, String userBId);
    void friendAllInGroup(String groupId);
}
