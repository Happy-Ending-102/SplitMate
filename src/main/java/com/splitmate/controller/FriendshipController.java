// File: FriendshipController.java
package com.splitmate.controller;

import org.springframework.stereotype.Component;

import com.splitmate.service.FriendshipService;



@Component
public class FriendshipController {
    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    /** Recipient calls this to accept a friend request. */
    public void acceptFriendRequest(String requesterId, String recipientId){
        friendshipService.acceptFriendRequest(requesterId, recipientId);
    }
}